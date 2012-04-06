/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Node;

/**
 * @author VamseeKYarlagadda
 *
 */
public class NodeEndConnection extends Thread{

	int Daemon_Port;
	P2PCache nodeMainClass;
	Node node;
	
	
	public void run() {
		
		 try{
			 ServerSocket serSocket=new ServerSocket(Daemon_Port);
			 System.out.println("Daemon Started!");
			 while(true)
			 {
			     Socket req=serSocket.accept();
			     System.out.println("DATA RECEIVED");
			    
			     BufferedReader br=new BufferedReader(new InputStreamReader(req.getInputStream()));
			     String temp=null;  
			     temp=br.readLine();
			     
			    while(!br.readLine().equalsIgnoreCase(""));
			     if((temp)!=null)
			     {
			    	 System.out.println(temp);
			    	 String split []=new String[3];
			    	 split=temp.split(" ");
			    	 split[1]=URLDecoder.decode(split[1]);
			    	 if(split[1].length()<16 || !split[1].substring(0,16).equalsIgnoreCase("/search?keyword="))
			    	 {
			    		// System.out.println("NON KEYWORD");
			    			OutputStream out=req.getOutputStream();
							out.write("HTTP/1.1 404 NOTFOUND\n".getBytes());
							out.write("Server: P2PServer+DHT\n".getBytes());
							out.write("Content-Length: 6\n".getBytes());
							out.write("Content-Type: text/plain\n".getBytes());
							out.write("Connection: close\n".getBytes());
							out.write("\n".getBytes());
							
							out.write("NOT FOUND ".getBytes());
							
			    			  req.close();
			    	 }
			    	 else
			    	 {
			    		// System.out.println("KEYWORD");
			    		 String keyword=split[1].substring(16);
			    		 keyword=URLDecoder.decode(keyword);
			    		 System.out.println("KEYWORD: "+keyword);
			    	
			    		// System.out.println("STEP1");
			    		
			    		 
			    		// String result=nodeMainClass.searchHighVideos(keyword);
			    		 
			    		 	if(nodeMainClass.query.containsKey(keyword))
			    			{
			    				
			    				ArrayList<Socket> reqs=nodeMainClass.query.get(keyword);
			    				reqs.add(req);
			    				nodeMainClass.query.put(keyword, reqs);
			    			}
			    			else
			    			{
			    				ArrayList<Socket> reqs=new ArrayList<Socket>();
			    				reqs.add(req);
			    				nodeMainClass.query.put(keyword, reqs);
			    			}
			    		 
			    		 Id tempid=nodeMainClass.node_factory.getIdFromString(keyword);
			    		 nodeMainClass.sendMessage(tempid,null,keyword,true,null);
			    		 
			    		 System.out.println("RETURNED TO DAEMON!");
			    	 }
			     }
			     else
			     {
			    		OutputStream out=req.getOutputStream();
						out.write("HTTP/1.1 404 NOTFOUND\n".getBytes());
						out.write("Server: P2PServer+DHT\n".getBytes());
						out.write("Content-Length: 6\n".getBytes());
						out.write("Content-Type: text/plain\n".getBytes());
						out.write("Connection: close\n".getBytes());
						out.write("\n".getBytes());
						
						out.write("NOT FOUND ".getBytes());
						
		    			  req.close();
			     }
			    		 
			    
			   
			     
			     
			      }
			 
			 }
			 catch(Exception e)
			 {
				 System.out.println(e.toString());
				 System.out.println("ERROR IN SERVER SOCKET!! TERMINATED!");
				 System.exit(1);
			 }
			  
		
	}

	
}
