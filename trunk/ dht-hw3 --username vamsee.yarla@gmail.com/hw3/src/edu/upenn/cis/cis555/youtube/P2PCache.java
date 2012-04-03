package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

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
				   throw new IllegalArgumentException("Bad DB");
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
				Thread.sleep(300000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			Id temp=node_factory.nidFactory.generateNodeId();
			System.out.println("Sending PING to "+temp);
			sendMessage(temp,null,"PING",true);
			*/
		}
	}
	
	public void sendMessage(Id idToSendTo, NodeHandle nodeHandle, String msgToSend,boolean wantResponse)
	{
		MessageFrame msg=new MessageFrame(node.getLocalNodeHandle(),msgToSend,wantResponse);
		endpoint.route(idToSendTo, msg, nodeHandle);
	}

	@Override
	public void deliver(Id id, Message msgx) {
		
		MessageFrame msg=(MessageFrame) msgx;
		if(msg.msg.equals("PING"))
		{
		System.out.println("Received PING to ID "+id+" from node "+msg.nodeHandle+"; returning PONG");
		if(msg.wantResponse)
		{
			sendMessage(null,msg.nodeHandle,"PONG",false);
		}
		}
		else if(msg.msg.equals("PONG"))
		{
			System.out.println("Received PONG from node "+msg.nodeHandle);
		}
		else
		{
			
			
			if(msg.wantResponse)
			{
			System.out.println("REQ: I GOT YOUR MESSAGE: AND YOU ARE: "+msg.nodeHandle);
			sendMessage(null,msg.nodeHandle,msg.msg,false);
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
							out.write("Content-Length: 6\n".getBytes());
							out.write("Content-Type: text/plain\n".getBytes());
							out.write("Connection: close\n".getBytes());
							out.write("\n".getBytes());
							
							out.write("Vamsee ".getBytes());
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
