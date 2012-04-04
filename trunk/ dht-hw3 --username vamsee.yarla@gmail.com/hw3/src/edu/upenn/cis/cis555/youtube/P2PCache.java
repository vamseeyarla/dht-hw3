package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.tools.ant.taskdefs.Mkdir;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaGroup;

import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

public class P2PCache implements Application {

	 	int Local_Port = 0;
		String IP_Boot_Pastry = null;
		int Port_Boot_Pastry = 0;
		int Daemon_Port = 0;
		String BDB = null;
		Node node = null;
		NodeEndConnection listen=null;
	    byte[] bytes;
	    Endpoint endpoint;
	    NodeFactory node_factory;
	    HashMap<String,ArrayList<Socket>> query=new HashMap<String, ArrayList<Socket>>();
	    YouTubeClient client;
		
	/**
	 * @param args
	 */
	public void init(String[] args)
	{
		if(args.length<4 || args.length>5)
		{
			System.out.println("Inavlid Set of Arguments");
			System.out.println("Usage: <Local Port><IP Pastry Boot><Port Pastry Boot><Port daemon|<Optional: Path to BDB>");
			System.exit(1);
		}
		
		try{
			 Local_Port = Integer.parseInt(args[0]);
			 Port_Boot_Pastry=Integer.parseInt(args[2]);
			 Daemon_Port=Integer.parseInt(args[3]);
		
			   if(args.length==5)
			   {
			   File x=new File(args[4]);
			   if(!x.exists())
			   {
				   x.mkdirs();
			   }
			   BDB=args[4];
			   }
			
			   String[] individualBytes=args[1].split("\\.");
			
			   if(individualBytes.length!=4)
			   {
				   throw new IllegalArgumentException("Bad IP Address");
			   }
			    bytes=new byte[4];
			   for(int i=0;i<4;i++)
			   {
				   Integer temp= Integer.parseInt(individualBytes[i]);
				   bytes[i]=temp.byteValue();
			   }
			  
			   Inet4Address.getByAddress(bytes);
			   IP_Boot_Pastry=args[1];	   
	    	}
		catch(Exception e)
		{
			System.out.println(e.toString());
			System.out.println("Error in enetered Data: Check again!");
			System.exit(1);
		}
		
	 client=new YouTubeClient(BDB);
	 client.nodeMainClass=this;
	 
	 System.out.println("Cleared Validation!");
	
	 try{
		 
		  InetSocketAddress sockaddress=new InetSocketAddress(Inet4Address.getByAddress(bytes),Port_Boot_Pastry);
		  node_factory=new NodeFactory(Local_Port,sockaddress);
		  node = node_factory.getNode();
		 
		  endpoint=node.buildEndpoint(this, "PASTRY NODE");
		  endpoint.register();
		  
	 	}
	 catch(Exception e)
	 {
		 System.out.println(e.toString());
		 System.out.println("Error in creating Socket with local port mentioned! Program terminated!");
		 System.exit(1);
	 }
	 
	}
	
	
	public void daemonStart()
	{ 
		 System.out.println("Starting to Listen on port daemon");
		 listen=new NodeEndConnection();
		 listen.Daemon_Port=Daemon_Port;
		 listen.node=node;
		 listen.nodeMainClass=this;
		 listen.start();
		 
		 	try{
				Thread.sleep(2000);
				if(!listen.isAlive())
				{
					
					System.out.println("Thread Died!");
					System.exit(1);
				}		
				}
				catch(Exception e)
				{
					System.out.println("Thread DIED ABRUPTLY");
				}
	
		 
	}
	
	public static void main(String[] args){
		
	   
		P2PCache nodeMainClass=new P2PCache();
		nodeMainClass.init(args);
		
		nodeMainClass.daemonStart();
		System.out.println("Node Started!");
		System.out.println("Ready for Service!");
		
		nodeMainClass.pingPongService();
	}

	
	public void pingPongService()
	{
		while(true)
		{
			try {
			//	Thread.sleep(3000);
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Id temp=node_factory.nidFactory.generateNodeId();
			System.out.println("Sending PING to "+temp);
			sendMessage(temp,null,"PING",true,null);
			
		}
	}
	
	public void sendMessage(Id idToSendTo, NodeHandle nodeHandle, String msgToSend,boolean wantResponse, String msgContent)
	{
		MessageFrame msg=new MessageFrame(node.getLocalNodeHandle(),msgToSend,wantResponse,msgContent);
		endpoint.route(idToSendTo, msg, nodeHandle);
		System.out.println("MSG SENT TO: "+idToSendTo);
	}

	@Override
	public void deliver(Id id, Message msgx) {
		
		System.out.println("REACHED DELIVER");
		MessageFrame msg=(MessageFrame) msgx;
		if(msg.msg.equals("PING"))
		{
		System.out.println("Received PING to ID "+id+" from node "+msg.nodeHandle+"; returning PONG");
		if(msg.wantResponse)
		{
			sendMessage(null,msg.nodeHandle,"PONG",false,null);
		}
		}
		else if(msg.msg.equals("PONG"))
		{
			System.out.println("Received PONG from node "+msg.nodeHandle);
		}
		else
		{
			
			System.out.println("ENTERED ELSE");
			if(msg.wantResponse)
			{
				System.out.println("ACTUAL PLACE");
				String content=null;
			    content=fetchData(msg.msg);
			System.out.println("REQ: I GOT YOUR MESSAGE: AND YOU ARE: "+msg.nodeHandle);
			sendMessage(null,msg.nodeHandle,msg.msg,false,content);
			}
			else
			{
				System.out.println("RES: I GOT YOUR MESSAGE: AND YOU ARE: "+msg.nodeHandle);
				if(query.containsKey(msg.msg))
				{
					ArrayList<Socket> socs=query.remove(msg.msg);
					for(int i=0;i<socs.size();i++)
					{
						try{
							OutputStream out=socs.remove(i).getOutputStream();
							out.write("HTTP/1.1 200 OK\n".getBytes());
							out.write("Server: P2PServer+DHT\n".getBytes());
							out.write(("Content-Length: "+msg.msgContent.length()+"\n").getBytes());
							out.write("Content-Type: text/xml\n".getBytes());
							out.write("Connection: close\n".getBytes());
							out.write("\n".getBytes());
							
							out.write(msg.msgContent.getBytes());
							out.close();
						}
						catch(Exception e)
						{
							System.out.println("Socket Closed!!! Can't help it :(");
						}
						
					}
					
				}
			}
			
		}
		
	}

    public String fetchData(String keyword)
    {
    	if(client.db.checkSearchKeyExists(keyword))
		{
			SearchData data=client.db.retrieveData(keyword);
			return data.Data;
		}
    	else
    	{
    		String content="vamsee";
    		StringBuffer temp=new StringBuffer();
    		try {
				YouTubeQuery query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
				
				System.out.println(keyword);
				query.setFullTextQuery(keyword);
				
				YouTubeService service=new YouTubeService("YouTube Grab");
				VideoFeed feed=service.query(query, VideoFeed.class);
				temp.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
				temp.append("<videoCollection>");
				for(VideoEntry videoEntry : feed.getEntries() ) {
					System.out.println("\n");
					temp.append("<videos>\n");
					 YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
					 
					 temp.append("<uploader>\n");
					 temp.append(mediaGroup.getUploader().replaceAll("&", "&amp;"));
					 temp.append("</uploader>\n");
					 
					 temp.append("<duration>\n");
					 temp.append(mediaGroup.getDuration());
					 temp.append("</duration>\n");
					
					 temp.append("<description>\n");
					 temp.append(mediaGroup.getDescription().getPlainTextContent().replaceAll("&", "&amp;"));
					 temp.append("</description>\n");
					 
					 temp.append("<link>\n");
					// temp.append(mediaGroup.getPlayer().getUrl());
					 // data='"+mediaGroup.getPlayer().getUrl()+"'
					 temp.append(mediaGroup.getPlayer().getUrl().substring(0,mediaGroup.getPlayer().getUrl().indexOf("&feature=youtube_gdata")).replaceAll("&", "&amp;"));
					 temp.append("</link>\n"); 
					 
					 temp.append("</videos>\n");
				}
				temp.append("</videoCollection>\n");
				content=temp.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				content=null;
			}
    		//FETCH DATA FROM YOUTUBE API
    		
    		/*
    		try {
			URLConnection connection=new URL("http://gdata.youtube.com/feeds/api/videos?v=1&q="+keyword).openConnection();
			BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer buffer=new StringBuffer();
			String temp;
				
			while((temp=br.readLine())!=null)
			{
				
				buffer.append((temp));
			}	
			content=buffer.toString();
    		System.out.println("CONTENT:   "+content);
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				content=null;
			} 
    		//content=content.replaceFirst("encoding='UTF-8'", "encoding='iso-8859-1'");
    		
    		
    		return content;	
    	*/
			client.db.addSearchData(keyword, content);
			return content;
    	}
    }
	
	@Override
	public boolean forward(RouteMessage arg0) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void update(NodeHandle arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
}
