package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import rice.p2p.commonapi.Node;

public class P2PCache {

	 	int Local_Port = 0;
		String IP_Boot_Pastry = null;
		int Port_Boot_Pastry = 0;
		int Daemon_Port = 0;
		String BDB = null;
		Node node = null;
		NodeEndConnection listen=null;
	    byte[] bytes;
		
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
		
	 YouTubeClient client=new YouTubeClient(BDB);
	 
	 System.out.println("Cleared Validation!");
	
	 try{
		 
		  InetSocketAddress sockaddress=new InetSocketAddress(Inet4Address.getByAddress(bytes),Port_Boot_Pastry);
		  NodeFactory node_factory=new NodeFactory(Local_Port,sockaddress);
		  node = node_factory.getNode();
		  
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
	}
}
