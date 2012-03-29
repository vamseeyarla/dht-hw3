/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upenn.cis.cis555.webserver;


import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;


/**
 *
 * @author VamseeKYarlagadda
 */
public class ServerHandler {
    
    public static String serverPort;
    private static Logger logger = Logger.getLogger(ServerHandler.class);
  //  public static SocketVecQueue queuestuff=new SocketVecQueue();
    public static int count=1; 
    
    
    //FUNCTION FOR THE MAIN QUEUUE THREAD TO START LISTENING TO CLIEN REQ's. IF IT HAS GOT ANY< THEN HAVE TO PUT IN QUUEUE AND START LISTING AGAIN
    public void ListenReq(String args)
    {
              ServerSocket serSoc;
            try {
                System.out.println("Starting Server");
                
                /*
                 * A socket is an endpoint of a network connection. A socket enables an application to read from and write to the network. Two software applications residing on two different computers can communicate with each other by sending and receiving byte streams over a connection. To send a message to another application, you need to know its IP address, as well as the port number of its socket. In Java, a socket is represented by the java.net.Socket class.

				To create a socket, you can use one of the many constructors of the Socket class. One of these constructors accepts the host name and the port number:

				public Socket(String host, int port)

				where host is the remote machine name or IP address, and port is the port number of the remote application. For example, to connect to yahoo.com at port 80, you would construct the following socket:

				new Socket("yahoo.com", 80);
                 */
               
                
               
                     int backlog=0;  //Max Waiting Requests in queue.
                     InetAddress address= InetAddress.getByName("localhost");
                     serSoc=new ServerSocket(Integer.parseInt(args),backlog,address);
                     serverPort=args;
                     System.out.println("Server Started at local host at the port: "+args);
                     
                   //   System.out.println("Open for Client Requests");
                     while(true)
                     {
                    	
                         // System.out.println(ThreadPool.threadQueueHandleStatus);
                             if(ThreadPool.threadQueueHandleStatus==true)
                             {
                                 break;
                             }
                             
                             try{
                         serSoc.setSoTimeout(2000);
                         Socket s=serSoc.accept();
                         boolean status;
                         
                        status=new SocketVecQueue().enqueue(s);
                        
                         
                         
                         if(status)
                         {
                             
                      //       System.out.println("Please Wait..You are being processed");
                         }
                         else
                         {
                             System.out.println("Too many Req's... Please try later");
                         }
                         
                             }
                             catch(Exception e)
                             {
                            	// logger.error(e.toString());
                            	 if(ThreadPool.threadQueueHandleStatus==true)
                                 {
                                     break;
                                 }
                            	 else
                            		 continue;
                            	 
                             }
                 
                                      
                     }                    
                    
           
            } catch (UnknownHostException ex) {
            	logger.error(ex.toString());
               // Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
            }
              catch (IOException ex) {
            	  logger.error(ex.toString());
              //  Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
           }
        
    }
    
 
    
    
}
