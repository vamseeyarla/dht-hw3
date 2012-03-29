/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upenn.cis.cis555.webserver;

import java.util.Vector;

import org.apache.log4j.Logger;


/**
 *
 * @author VamseeKYarlagadda
 */
public class ThreadPool {
    
	/*
	 * All the variables of ThreadPool
	 * 
	 */
    public static final int threadCount=20;
    public static Thread [] threadClientReqHandle;
    public static Thread threadQueueHandle;
    public static boolean threadQueueHandleStatus;
    public static boolean []threadClientReqHandleStatus;
    public static Vector<String> status=new Vector<String>(threadCount);
    private static Logger logger = Logger.getLogger(ThreadPool.class);
    
   
    public static int CURRENT=0;
    
    
    /*
	 * Function to generate all the threads that are needed for the execution of the program.
	 * It includes main Queue Thread, and all the client request handler threads.
	 */
    public boolean genThreads()
    {
    	
    //	logger.error("THREAD ERROR");
        try
        {
         
            threadClientReqHandle=new Thread[threadCount];
            threadClientReqHandleStatus=new boolean[threadCount];
            
            for(int z=0;z<threadCount;z++)
            {
                status.add(z,"Not Working");
            }
         
             for(int i=0;i<threadCount;i++)
            {              
                Thread temp=new Thread(new SocketVecQueue(),"Thread:"+String.valueOf(i));
                threadClientReqHandle[i]=temp;
                CURRENT++;
                threadClientReqHandleStatus[i]=false;
                threadClientReqHandle[i].start();
                
                
            }
            return true;
        }
        catch(Exception e)
        {
            //e.printStackTrace();
        	logger.error(e.toString());
            return false;
        }
                
    }
    
    
    /*
	 * Function to terminate all the threads that are needed for the execution of the program.
	 * It includes main Queue Thread, and all the client request handler threads.
	 * IT also stops all the servlets by calling destroy()
	 */
    public void killThreads()
    {
    		
    	
        System.out.println("Killing Threads...Cleaning your System..//");
        threadQueueHandleStatus=true;
        
        for(int i=0;i<CURRENT;i++)
        {
            if(threadClientReqHandle[i].getName().equalsIgnoreCase(Thread.currentThread().getName())==false)
            {
            	threadClientReqHandle[i].interrupt();
            }
                    	
        }
        
        System.out.println("Killing Servlets..");
        
        for(String t:HandleReq.sessions.keySet())
        {
        	HandleReq.sessions.get(t).invalidate();
        }
        
    	for(String s:ServletsInit.servlets.keySet())
    	{
    		ServletsInit.servlets.get(s).destroy();
    	}
        
        threadQueueHandle.interrupt();
        Thread.currentThread().interrupt();
        
        
    }

  
}
