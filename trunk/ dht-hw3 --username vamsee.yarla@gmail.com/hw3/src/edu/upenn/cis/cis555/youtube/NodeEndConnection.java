/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
			     System.out.println("Node ID: "+node.getId()+": PING");
			     BufferedReader br=new BufferedReader(new InputStreamReader(req.getInputStream()));
			     
			     BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(req.getOutputStream()));
			     bw.write("PONG");   
			 }
			 
			 }
			 catch(Exception e)
			 {
				 System.out.println("ERROR IN SERVER SOCKET!! TERMINATED!");
				 System.exit(1);
			 }
			  
		
	}

	
}
