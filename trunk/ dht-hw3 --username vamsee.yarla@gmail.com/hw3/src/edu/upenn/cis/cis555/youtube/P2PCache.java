package edu.upenn.cis.cis555.youtube;


import java.io.File;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

/*
 * Class that handles the overall execution of the P2P system
 */
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
	    /*
		 * Method to take input from user and process it accordingly.
		 * It also crates a new node and puts on of the thread in daemon listening state
		 * and one more thread in ping pong state.
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
			//System.out.println(e.toString());
			System.out.println("Error in enetered Data: Check again!");
			System.exit(1);
		}
		
	 client=new YouTubeClient(BDB);
	 client.nodeMainClass=this;
	 
	 //System.out.println("Cleared Validation!");
	
	 try{
		 
		  InetSocketAddress sockaddress=new InetSocketAddress(Inet4Address.getByAddress(bytes),Port_Boot_Pastry);
		  node_factory=new NodeFactory(Local_Port,sockaddress);
		  node = node_factory.getNode();
		 
		  endpoint=node.buildEndpoint(this, "PASTRY NODE");
		  endpoint.register();
		  
	 	}
	 catch(Exception e)
	 {
		 //System.out.println(e.toString());
		 System.out.println("Error in creating Socket with local port mentioned! Program terminated!");
		 System.exit(1);
	 }
	 
	}
	
	/*
	 * Method that triggers the start of the daemon process listening to incoming
	 *  connections from servlets and other external methods
	 * 
	 */
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

	/*
	 * Ping Service to make sure all clients are running in the syatem
	 * happens every 3 seconds.
	 */
	public void pingPongService()
	{
		while(true)
		{
			try {
			//	Thread.sleep(3000);
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			}
			
			Id temp=node_factory.nidFactory.generateNodeId();
			System.out.println("Sending PING to "+temp);
			sendMessage(temp,null,"PING",true,null);
			
		}
	}
	
	
	/*
	 * Method to send messages around the p2p network based on the method and ID given to the function.
	 * It make use of message format class to frame message and direct it around network
	 */
	
	public void sendMessage(Id idToSendTo, NodeHandle nodeHandle, String msgToSend,boolean wantResponse, String msgContent)
	{
		MessageFrame msg=new MessageFrame(node.getLocalNodeHandle(),msgToSend,wantResponse,msgContent);
		endpoint.route(idToSendTo, msg, nodeHandle);
		//System.out.println("MSG SENT TO: "+idToSendTo);
	}

	/*
	 * (non-Javadoc)
	 * @see rice.p2p.commonapi.Application#deliver(rice.p2p.commonapi.Id, rice.p2p.commonapi.Message)
	 * Methods to accept messages from incoming req's from other nodes and process them
	 */
	@Override
	public void deliver(Id id, Message msgx) {
		
		//System.out.println("REACHED DELIVER");
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
			
			//System.out.println("ENTERED ELSE");
			if(msg.wantResponse)
			{
				//System.out.println("ACTUAL PLACE");
				String content=null;
			    content=client.searchVideos(msg.msg);
			//System.out.println("REQ: I GOT YOUR MESSAGE: AND YOU ARE: "+msg.nodeHandle);
			sendMessage(null,msg.nodeHandle,msg.msg,false,content);
			}
			else
			{
				//System.out.println("RES: I GOT YOUR MESSAGE: AND YOU ARE: "+msg.nodeHandle);
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
