/*


 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upenn.cis.cis555.webserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.io.UnsupportedEncodingException;

import java.net.Socket;
import java.net.URLDecoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import java.util.Random;


import javax.activation.MimetypesFileTypeMap;
import javax.servlet.Servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;


import org.apache.log4j.Logger;




/*
/**
 *
 * @author VamseeKYarlagadda
 */


public class HandleReq {
    Socket client;
   public String met;
   public ServletsRequest request;
   public ServletsResponse response;
   public boolean isNotTest=true;
   public HttpServlet servlet;
   public ArrayList<String> headers=new ArrayList<String>();
   public static  Hashtable<String,ServletsSession> sessions=new Hashtable<String,ServletsSession>();
   private static Logger logger = Logger.getLogger(HandleReq.class);
    
    String modDate=null;
	int modifiedDate=0;
	static Random random=new Random(10);

	
	//DEFAULT METHOD TO HANDLE ALL CLENT REQ's
	
    public void parseReq(Socket req)
    {
    //	System.out.println("Vamsee");
       String mainHeader=null;
       String tempHeader=null;
       client=req;
        int i=0;
        
        BufferedReader br=null;
        int pointer=0;
      
         try {
			Thread.sleep(20);
		} catch (InterruptedException e2) {
			
			logger.error(e2.toString());
		//	e2.printStackTrace();
		}
        
        
        try {
       
           br=new BufferedReader(new InputStreamReader(req.getInputStream()));
           String temperHeader = null;
           mainHeader=null;
           
      	  // temperHeader=br.nextLine();
      	   temperHeader=br.readLine();
      	   
      	   mainHeader=temperHeader;
           headers.add(temperHeader);
           
           met=mainHeader.substring(0, mainHeader.indexOf(" ")).trim();
           
           boolean post=false;
           if(mainHeader.indexOf("POST")!=-1)
           {
        	   //DETERMINE THE HEADER IS POST METHOD
        	   post=true;
        	   if(mainHeader.indexOf("POST")!=0)
               {
            	   mainHeader=null;
            	   pointer=8;
               }
           }
           
   //        System.out.println("Krishna");
           
           
           
           
           
           
      	   try
          {

        	   String temp;
        	   int tracker=0;
        	   
        	   /*
        	    * KEEP TARCK OF ALL HEADERS FROM CLIENT AND SAVE IT IN HEADER Hashtable.
        	    */
        	  
        	//   while((temp=br.nextLine()).equalsIgnoreCase("")==false)
        	   while((temp=br.readLine()).equalsIgnoreCase("")==false)
       		{
        		headers.add(temp);  
        	//	System.out.println(temp);
       			if(temp.indexOf("Host:")!=-1 || temp.indexOf("host:")!=-1)
       			{
       				tracker=1;
       			}
       			if((temp.indexOf("If-Modified-Since: ")!=-1 || temp.indexOf("If-Unmodified-Since: ")!=-1) && mainHeader.indexOf("GET ")!=-1)
       			{
       				
       				if(temp.indexOf("If-Modified-Since: ")!=-1)
       				{
       					modifiedDate=1;
       					modDate=temp.substring(19,temp.length()-4);
       					modDate.trim();
       					
       				}
       				else
       				{
       					modifiedDate=2;
       					modDate=temp.substring(21,temp.length()-4);
       					modDate.trim();
       				}
       				
       			}
       			
       		}
        	  // System.out.println("SUCCESS");
        	   if(post && pointer!=8)
        	   {
        		   /*
            	    * Post method sequence; to fetch body and decode it with content-type passed.
            	    */
        		   
        		 //  System.out.println("SUCCESS1");
        		   boolean lengthStatus=false;
        		   boolean encodingStatus=false;
        		   int iz;
        		   int length;
        		   String encodingHeader = null;
        		   String lengthHeader = null;
        		  for(iz=0;iz<headers.size();iz++)
        		  {
        			  if(headers.get(iz).indexOf("Content-Type:")!=-1)
        			  {
        				  encodingHeader=headers.get(iz).split(":")[1];
        				  encodingStatus=true;
        				  encodingHeader=encodingHeader.trim();
        			  }
        			  if(headers.get(iz).indexOf("Content-Length:")!=-1)
        			  {
        				  lengthHeader=headers.get(iz);
        				 // System.out.println("SUCCESS2");
        				  lengthStatus=true;
        				
        			  }
        			  
        		  }
        		  if(!lengthStatus || !encodingStatus)
        		  {
        			  pointer=8;
    				  mainHeader=null;
        		  }
        		  else
        		  {
        		//	  System.out.println("SUCCESS3");
        			 String len=lengthHeader.substring(lengthHeader.indexOf(":")+1);
        			 len=len.trim();
        			 try{
        				 length=Integer.parseInt(len);
        				 if(length>0)
        				 {
        				
        		//			  System.out.println("SUCCESS4");
        			 
        		//	  System.out.println("SUCCESS5");
        		//	  System.out.println(length);		
        		
        			  char[] tempc=new char[length];
        			  br.read(tempc, 0, length);
        			  
        		      temp=String.valueOf(tempc);
        		
        		     /*
              	    * DECODING THE BODY
              	    */
        		   //  temp=URLDecoder.decode(temp, encodingHeader);
        		
        		
        		
        			  //     System.out.println("VAMSEE:  "+temp);
        			//   	System.out.println(br.hasNext());
        			       
        			   	
        			   	
        			//	  System.out.println("SUCCESS6");
        		     headers.add(" ");
        			 headers.add(temp);
        		//	 System.out.println(temp);
        			 
        				 }
        				 else
        				 {
        					 pointer=8;
              				 mainHeader=null; 
        				 }
        			 
        			 }
        			 catch(Exception e)
        			 {
        				 logger.error(e.toString());
        			//	 e.printStackTrace();
        				 headers.add(" ");
            			 headers.add(temp);
        			 }
        			 
        		  }
        		  
        	   }
        	   
        	   
       		
        	   if(mainHeader!=null)
        	   {
       // 		   System.out.println("Krishna1");
        		   pointer=headerErrorFn(temperHeader,tracker);
        	   }
        	 
          }
           catch(Exception e1)
           {
        	   logger.error(e1.toString());
        	//   e1.printStackTrace();
        //   e1.printStackTrace();
           }
         
        
           
     
      
           
       if(mainHeader!=null && pointer==2){
    //	   System.out.println("Krishna4");
    	   boolean statDyn=isServlet();
    //	   System.out.println("Krishna5");
    	   if(statDyn)
    	   {
    //		   System.out.println("Krishna6");
    		   //STATIC PAGE
              execReq(mainHeader);
    	   }
    	   else
    	   {
    		   //DYNAMIC PAGE
    		   //CALL TO SERVLET CODE
    	   }
       }
               
       else if(pointer==8)
       {
    	  BufferedWriter bw=null;
           try{
        	   	bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        	   	bw.write("HTTP/1.0 "+"400 BAD REQUEST\n");
        	   	
        	   	Date headDate =new Date(System.currentTimeMillis());
        	   	DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        	   	String date=headformatter.format(headDate).concat(" GMT");
        	   	bw.write("Date: "+date+"\n");
        	   	
        	   	bw.write("Server: HTTPServer\n");
        	   	bw.write("Connection: close\n");
        	   	bw.write("\n");
           }
           catch(Exception e)
           { 
        	   logger.error(e.toString());
        	//   e.printStackTrace();
        	//   e.printStackTrace();
           }
           finally
           {
        	   bw.flush();
        	   bw.close();
           }
       }
     
  
         } catch (IOException ex) {
        	 logger.error(ex.toString());
             try {
				client.close();
			} catch (IOException e) {
				logger.error(e.toString());
			//	e.printStackTrace();
			}
   		//  ex.printStackTrace();
        }
         finally
         {
        	 try {
        
				br.close();
			} catch (Exception e) {
				logger.error(e.toString());
		//		e.printStackTrace();
			}
         }
        
       
    }
    
    
    /*
	    * Fn: Function to handle all the static requests from client(No servlets)
	    */
    public void execReq(String header)
    {
        String method=null,file = null,htmlVersion = null;
      try{
    	  /*
   	    * Fn: Retrieve headers properly
   	    */
    	  	method=header.substring(0, header.indexOf(" "));
    	  	int last=header.indexOf(" ");
    	  	file=header.substring(last+1, header.indexOf(" ", last+1));
    	  	last=header.indexOf(" ", last+1);
    	  	htmlVersion=header.substring(last+1);
    	  	met=method;
    	  	ThreadPool.status.set(Integer.parseInt(Thread.currentThread().getName().substring(7,Thread.currentThread().getName().length())),file );
      	}
      catch(Exception e)
      {
    	  logger.error(e.toString());
		//  e.printStackTrace();
          htmlVersion=file;
          file="NOPE";
      }
      if(file.equalsIgnoreCase("/favicon.ico"))
      {
    	  /*
   	    * Fn: if favicon req comes; ignore it
   	    */
           try {
			client.close();
		} catch (IOException e) {
			logger.error(e.toString());
			//e.printStackTrace();
		}
      }
      else if(file.equalsIgnoreCase("/shutdown"))
       {
    	  /*
   	    * Fn: if shutdown comes; shutdown all req's
   	    */
    	  BufferedWriter bw=null;
           try{
        	   		bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        	   
        	   		if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
        	   		{
        	   			bw.write(htmlVersion+" "+"100 Continue\n");
        	   			bw.write("\n");
        	   		}
        	   		       	   		
        	   		bw.write(htmlVersion+" "+"200 OK\n");
        	 
        	   	  	Date headDate =new Date(System.currentTimeMillis());
            	             	   	
            	   	DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            	   	String date=headformatter.format(headDate).concat(" GMT");
            	   	
            	   	bw.write("Date: "+date+"\n");  		
        	   		
            	   	bw.write("Content-Type: text/html\n");
        	   		bw.write("Content-Length: 1354\n");
        	   		bw.write("Server: HTTPServer\n");
        	   		bw.write("Connection: close\n");
        	   		bw.write("\n");
       
        	   		if(met.equalsIgnoreCase("GET") || met.equalsIgnoreCase("POST"))
        	   			bw.write("<html><head><title>Server Shutdown</title></head><body><h1>Server is Down</h1></body></html>\n".toString());
       
        new ThreadPool().killThreads();
           }
           catch(Exception e)
           {
        	   logger.error(e.toString());
     //		  e.printStackTrace();
               
           }
           finally
           {
        	   
        	   try {
        		   bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				logger.error(e.toString());
				//e.printStackTrace();

      	//	  e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					logger.error(e.toString());
			//		e.printStackTrace();
				}
			}
             
           }
       
          
       }
       else if(file.equalsIgnoreCase("/control"))
       {
    	   /*
       	    * Fn: if control comes; dispaly all stats including servlet dealing
       	    */
    	//   System.out.println("Krishna3");
    	 
           BufferedWriter bw=null;
              try{
            	  
            	  
            	  	bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            	
            		if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
        	   		{
        	   			bw.write(htmlVersion+" "+"100 Continue\n");
        	   			bw.write("\n");
        	   		}
            	  	
            	  	bw.write(htmlVersion+" "+"200 OK\n");
            	  	
            	  	Date headDate =new Date(System.currentTimeMillis());
            	  	DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            	   	String date=headformatter.format(headDate).concat(" GMT");
            	   	bw.write("Date: "+date+"\n"); 
            	  	
       				bw.write("Content-Type: text/html\n");
       				bw.write("Content-Length: "+(3000+new File("ReportsLog.log").length())+"\n");
       				bw.write("Server: HTTPServer\n");
       				bw.write("Connection: close\n");
       				bw.write("\n");
    
       				if(met.equalsIgnoreCase("GET") || met.equalsIgnoreCase("POST")){
       					bw.write("<html><head><title>Server Report</title></head>");
       
       					bw.write("<body><h1>             Server Report</h1>");
       					bw.write("</br> </br> <h3> Vamsee K. Yarlagadda    (vamsee)</h3></br></br>");
       					bw.write("<h2>Thread Statistics</h2></br></br>");
       					
       					for(int i=0;i<ThreadPool.threadCount;i++)
       					{
       						int j=i+1;
       						bw.write(j+")     ");
       						bw.write("Name-> "+ThreadPool.threadClientReqHandle[i].getName()+"    ");
       						if(ThreadPool.threadClientReqHandle[i].getState().toString().equalsIgnoreCase("WAITING"))
       						{
       							bw.write("Status-> "+ThreadPool.threadClientReqHandle[i].getState()+"    ");
       						}
       						else
       						{
       							bw.write("Status-> "+"RUNNING"+"    ");
       						}	
       						
           int ixy = 0;
           
           try{
          
               if(!ThreadPool.threadClientReqHandle[i].getState().toString().equalsIgnoreCase("WAITING"))
               {
                bw.write("                URL-> "+ThreadPool.status.get(i) +"    ");
               }
              
           
           }
           catch(Exception e)
           {
        	   logger.error(e.toString());
                bw.write("                URL-> "+"Currently Parsing the URL"+"    ");

      		  //e.printStackTrace();
           }
          
      
           bw.write("</br>");           
       }
       
       bw.write("<a href=\"http://localhost:"+ServerHandler.serverPort+"/shutdown\"><input type=\"button\" name=\"shutdown\" value=\"Shutdown\"></a>");
       
       bw.write("</br></br>");
       bw.write("Server Error Log");
       bw.write("</br>");
       
       
      
       BufferedReader fr=new BufferedReader(new FileReader("ReportsLog.log"));
       while(fr.ready())
       {
    	   bw.write(fr.readLine());
    	   bw.write("</br>");
       }
       fr.close();
       
       bw.write("</br></br></body></html>");
       }
           }
           catch(Exception e)
           {
        	   logger.error(e.toString());
   //  		  e.printStackTrace();
           }
           finally
           {
        	   
        	   try {
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				logger.error(e.toString());

     			//e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					logger.error(e.toString());
	//				e.printStackTrace();
				}
			}
             
           }
       
       }
       
       else if(file.equalsIgnoreCase("/"))
       {
    	   /*
       	    * Fn: if '/' comes; dsplay deafult drectory
       	    */
             directoryfn(1,new File(HttpServer.source),htmlVersion,"/");
       
       }
      

       else if(file.equalsIgnoreCase("nope"))
       {
       BufferedWriter bw=null;
    	   try{
       bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
      
   	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
		{
			bw.write(htmlVersion+" "+"100 Continue\n");
			bw.write("\n");
		}
       
       
       bw.write(htmlVersion+" "+"200 OK\n");
       
       Date headDate =new Date(System.currentTimeMillis());
       DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	   	String date=headformatter.format(headDate).concat(" GMT");	
       bw.write("Date: "+date+"\n"); 
       
       bw.write("Content-Type: text/html\n");
       bw.write("Content-Length: 2354\n");
       bw.write("Server: HTTPServer\n");
       bw.write("Connection: close\n");
       bw.write("\n");
      
       if(met.equalsIgnoreCase("GET") || met.equalsIgnoreCase("POST"))
       {
      
        bw.write("<html><head><title>Server Status</title></head>");
        bw.write("<body><h1>Server Report</h1>");
        bw.write("</br></br><h3> Vamsee K. Yarlagadda    (vamsee)</h3></br></br>");
        bw.write("<h3>Server Up and Running</h3>");
        bw.write("</br></br></body></html>");
      
       }
  
       }
           catch(Exception e)
           {
        	   logger.error(e.toString());
 //    		  e.printStackTrace();
           }
           finally
           {
        	   
        	   try {
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				logger.error(e.toString());
	//			e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					logger.error(e.toString());
		//			e.printStackTrace();
				}
			}
             
           }
       
       
       }
       
       else
       {
    	   
    	   /* Finding whether this is Windows/Linux and changing paths according to req format  */
    	   
    	   String path=HttpServer.source;
           boolean windows=false;
           boolean linux=false;
           
          
           
           if(path.indexOf("\\")!=-1)
                 {
             //  System.out.println("windows");
               windows=true;
                 if(path.indexOf(path.length()-1)!=(int)'\\')
               {
                   path=path.concat("\\");
               }
           }
               
         
           else
           {
           //    System.out.println("linux");
               linux=true;
               if(path.charAt(path.length()-1)!='/')
               {
          
                   path=path.concat("/");
                   
               }
           }
          
           if(windows==true){
                  file=file.replace('/', '\\');
                  if(file.charAt(0)=='\\')
                  {
                      file=file.substring(1,file.length());
                  }
           }
           else
           {
               if(file.charAt(0)=='/')
                  {
                      file=file.substring(1,file.length());
                  }
           }
           
           //END OF FORMATTING PATH , FILE STRINGS & OS RELATED STUFF
       //    System.out.println(file);
      //     System.out.println("MANOJ KRISHNA");
           try{
        	//file= file.replaceAll("%20", " ");
        	file=URLDecoder.decode(file);
           File reader=new File(path+file);
           
        //   System.out.println("Checking EXISTS");
           if(reader.exists())
           {
        //       System.out.println("Entered File EXISTS");
           
        	   if(file.indexOf("..")!=-1)
        	   {
        		   fileforbiddenfn(reader, htmlVersion, file);
        	   }
           else if(reader.isDirectory())    // The path is a directory
             {
          //        System.out.println("Entered DIRECTORY");
                 directoryfn(0,reader,htmlVersion,file);
             }
             else
             {
         //         System.out.println("Entered File");
            	 filefn(reader,htmlVersion,file);
             }
           
           
           
           }
           
           else
           {
        	   notfoundfn(reader,htmlVersion,file);
           }
           
           }
           catch(Exception e)
           {
        	   logger.error(e.toString());
        	   try {
				client.close();
			} catch (IOException e1) {
				logger.error(e1.toString());
		//		e1.printStackTrace();
			}
    // 		  e.printStackTrace();
        	   servererrorfn(htmlVersion);
           }
           
           
       }//end of loop   
    	   
    }//END OF FN:EXECREQ	   
    	     
    
    /*
	    * Fn: IF THE PASSED PATH IS ADIRECTORY THEN DUSPLAY ALL FIES UNDER IT
	    */
    public void directoryfn(int stat,File reader, String htmlVersion, String file)
    {
    	BufferedWriter bw=null;
        try{
            
             bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
         	
             if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{
	   			bw.write(htmlVersion+" "+"100 Continue\n");
	   			bw.write("\n");
	   		}
             
             
             
             boolean http11=false;
             
             Date SysDate =new Date(reader.lastModified()); //comp
             Date userdate = new Date(reader.lastModified()); //user
             
            
             DateFormat sysformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	     	 //sysformatter.parse();
             String sydate=sysformatter.format(SysDate);
             SysDate=sysformatter.parse(sydate);
	            
             //System.out.println(modifiedDate);
             if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
 	   		{ 
            	 http11=true;
            if(modDate!=null)
             {
            	//System.out.println("Vamsee: "+modDate);
            	
            	   try{
                      	userdate=sysformatter.parse(modDate);
                   	   }
                   	   catch(Exception ecv)
                   	   {
                   		logger.error(ecv.toString());
                   		   System.out.println("INVALID DATE");
                   		   System.out.println("FORMAT: EEE, dd MMM yyyy HH:mm:ss Z");
                   	   }
            	
            	// System.out.println("Working");
             }
             
 	   		} 
             //System.out.println(SysDate);
             //System.out.println(userdate);
             if(http11 && modifiedDate==1 && SysDate.compareTo(userdate)<0)
             {
            	 bw.write(htmlVersion+" "+"304 Not Modified\n");
 	   			 Date headDate =new Date(System.currentTimeMillis());
 	             DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
 	     	   	 String date=headformatter.format(headDate).concat(" GMT");
 	             bw.write("Date: "+date+"\n"); 
 	             bw.write("\n");
             }
             else if(http11 && modifiedDate==2 && SysDate.compareTo(userdate)>=0)
             {
            	 bw.write(htmlVersion+" "+"412 Precondition Failed\n");
            	 bw.write("\n");
             }
             
             
             else
             {
             bw.write(htmlVersion+" "+"200 OK\n");
           
             Date headDate =new Date(System.currentTimeMillis());
             DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
     	   	String date=headformatter.format(headDate).concat(" GMT");
             bw.write("Date: "+date+"\n"); 
             
             bw.write("Content-Type: text/html\n");
             bw.write("Content-Length: 16354\n");
             
             
             Date myDate=new Date(reader.lastModified());
            
            
			DateFormat formatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
     	   	String formattedDate=headformatter.format(myDate).concat(" GMT");
             
             bw.write("Last-Modified: "+formattedDate+"\n");
             bw.write("Server: HTTPServer\n");
             bw.write("Connection: close\n");
             bw.write("\n");
     
             if(met.equalsIgnoreCase("GET") || met.equalsIgnoreCase("POST")){
            	 if(stat==0){
             bw.write("<html><head><title>Server Status</title></head><body>");
             bw.write("<h1>The path is a directory</h1></br>");
             bw.write("<h2>List of directory</h2></br>");
            	 }
            	 else
            	 {
            		 bw.write("<html><head><title>Server Home</title></head><body>");
            		 bw.write("<h1>Server Running! :)</h1></br>");
            		 bw.write("<h1>Home Directory</h1></br>");
                     bw.write("<h2>List of files in this directory..</h2></br>");
                    
            	 }
             File [] listFiles=reader.listFiles(); 
            
             for(int i=0;i<listFiles.length;i++)
             {
                 boolean r=listFiles[i].canRead();
                 boolean w=listFiles[i].canWrite();
                 boolean x=listFiles[i].canExecute();
                 
                 Date filemyDate=new Date(listFiles[i].lastModified());
                
                DateFormat fileformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
         	   	String fileformattedDate=fileformatter.format(filemyDate).concat(" GMT");
                 
                 
                 bw.write("Read: <b>"+r+"</b>  Write: <b>"+w+"</b>  Execute: <b>"+x+"</b>          "+"<b>"+"<a href=\"http://localhost:"+ServerHandler.serverPort+"/"+file+"/"+listFiles[i].getName()+"\">"+listFiles[i].getName()+"</a>"+"</b>"+"    "+"Last Modified: <b>"+fileformattedDate+"</b></br></br>");
                 
         
             }
              bw.write("</br></br></body></html>");
             
             }//HEAD/GET LOOP
             }
             
        }//try
        catch(Exception e)
        {
        	logger.error(e.toString());
  		//  e.printStackTrace();
        	servererrorfn(htmlVersion);
        }
        finally
        {
     	   
     	   try {
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				logger.error(e.toString());
		//		e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					logger.error(e.toString());
			//		e.printStackTrace();
				}
			}
          
        }
        
        
    }//directory fn
    	      
    
    /*
	    * Fn: IF THE PASSED PATH IS A FILE THEN CHECK IF FILE EXISTS OR NOT
	    */
    public void filefn(File reader, String htmlVersion, String file)
    {
    	if(reader.canRead())
        {
           fileexistfn(reader, htmlVersion, file);
        }
    	 else
         {
     //        System.out.println("FORBIDDEN SECTION");
             fileforbiddenfn(reader,htmlVersion, file);
         }
    }//file fn
    
    /*
	    * Fn: IF THE PASSED PATH IS A FILE THEN CHECK IF FILE EXISTS OR NOT
	    */
    public void fileexistfn(File reader, String htmlVersion,String file)
    {
    	OutputStream bw=null;
        try{
            
             bw=(client.getOutputStream());
        
         	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{
	   			bw.write((String.valueOf(htmlVersion)+" "+"100 Continue\n").toString().getBytes());
	   			bw.write("\n".toString().getBytes());
	   		}
         	
         	
         	

            boolean http11=false;
            
            Date SysDate =new Date(reader.lastModified()); //comp
            Date userdate = new Date(reader.lastModified()); //user
            
           
            DateFormat sysformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	     	 //sysformatter.parse();
            String sydate=sysformatter.format(SysDate);
            SysDate=sysformatter.parse(sydate);
	            
            //System.out.println(modifiedDate);
            if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{ 
           	 http11=true;
           if(modDate!=null)
            {
       //    	System.out.println("Vamsee: "+modDate);
        	   try{
           	userdate=sysformatter.parse(modDate);
        	   }
        	   catch(Exception ecv)
        	   {
        		   logger.error(ecv.toString());
        		   System.out.println("INVALID DATE");
        		   System.out.println("FORMAT: EEE, dd MMM yyyy HH:mm:ss Z");
        	   }
           	// userdate=DateFormat.getDateInstance().parse(modDate);
          // 	 System.out.println("Working");
            }
            
	   		} 
          //System.out.println(SysDate);
          ///System.out.println(userdate);
            if(http11 && modifiedDate==1 && SysDate.compareTo(userdate)<0)
            {
           	 bw.write((String.valueOf(htmlVersion)+" "+"304 Not Modified\n").toString().getBytes());
	   			 Date headDate =new Date(System.currentTimeMillis());
	             DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	     	   	 String date=headformatter.format(headDate).concat(" GMT");
	             bw.write(("Date: "+String.valueOf(date)+"\n").toString().getBytes()); 
	             bw.write("\n".toString().getBytes());
            }
            else if(http11 && modifiedDate==2 && SysDate.compareTo(userdate)>=0)
            {
           	 bw.write((String.valueOf(htmlVersion)+" "+"412 Precondition Failed\n").toString().getBytes());
           	 bw.write("\n".toString().getBytes());
            }
            
            else{        	
         	
             bw.write((String.valueOf(htmlVersion)+" "+"200 OK\n").toString().getBytes());
            
             Date headDate =new Date(System.currentTimeMillis());
     	   
     	   	 DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
    	   	String date=headformatter.format(headDate).concat(" GMT");
     	   	 
     	   	 bw.write(("Date: "+String.valueOf(date)+"\n").toString().getBytes()); 
             
             if(file.indexOf(".htm")!=-1 || file.indexOf(".html")!=-1)
            {
            	 bw.write(("Content-Type: text/html\n").toString().getBytes());
                 
            }
             else if(file.indexOf(".xml")!=-1)
             {
             	bw.write(("Content-Type: text/xml\n").toString().getBytes());
                 
             }
            else if(file.indexOf(".txt")!=-1)
            {
            	bw.write(("Content-Type: text/plain\n").toString().getBytes());
                
            }
            else if(new MimetypesFileTypeMap().getContentType(reader).equalsIgnoreCase("image/jpeg") || file.indexOf(".jpg")!=-1)
            {
            	bw.write(("Content-Type: image/jpeg\n").toString().getBytes());
               
            }
            else if(new MimetypesFileTypeMap().getContentType(reader).equalsIgnoreCase("image/png")|| file.indexOf(".png")!=-1)
            {
            	bw.write(("Content-Type: image/png\n").toString().getBytes());
                
            }
            else{
            	bw.write(("Content-Type: "+String.valueOf(new MimetypesFileTypeMap().getContentType(reader)) +"\n").toString().getBytes());
                
            }
             
             bw.write(("Content-Length: "+String.valueOf(reader.length())+"\n").toString().getBytes());
             
             
             Date myDate=new Date(reader.lastModified());
            
             DateFormat formatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
     	    	String formattedDate=formatter.format(myDate).concat(" GMT");
             
             bw.write(("Last-Modified: "+String.valueOf(formattedDate)+"\n").toString().getBytes());
             bw.write("Connection: close\n".toString().getBytes());
             bw.write("Server: HTTPServer".toString().getBytes());
             bw.write((String.valueOf((char)13)).getBytes());
             bw.write((String.valueOf((char)10)).getBytes());
             bw.write((String.valueOf((char)13)).getBytes());
             bw.write((String.valueOf((char)10)).getBytes());
            
          
             
           
             if(met.equalsIgnoreCase("GET") || met.equalsIgnoreCase("POST")){
            
            	 int x=Integer.parseInt(String.valueOf(reader.length()+1));
               
            	 int track=0;
            	
                 
                                 
                FileInputStream read=new FileInputStream(reader);
                int size=4096;
                
                byte Bytes[]=new byte[size];
                int count=0;
                while ((count = read.read(Bytes, 0, size)) != -1)
                {
                        bw.write(Bytes, 0, count);
                }
             /*   
                while(read.read(Bytes, 0, size)==size)
                {
                	 bw.write(Bytes,0,);
                }
                bw.write(Bytes);
            */
                
               
                read.close();
             }
            } 
        }
        catch(Exception e)
        {
        	logger.error(e.toString());
  		//  e.printStackTrace();
        	servererrorfn(htmlVersion);
        }
        finally
        {
     	   
     	   try {
     		  
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				logger.error(e.toString());
		//		e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					logger.error(e.toString());
			//		e.printStackTrace();
				}
			}
        }
        
        
     }//fileexist fn
    
    /*
	    * Fn: IF THE PASSED PATH IS A FILE and it is FORBIDDEN
	    */
    public void fileforbiddenfn(File reader, String htmlVersion, String file)
    {
    	
    	BufferedWriter bw=null;
        try{
            
             bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        
         	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{
	   			bw.write(htmlVersion+" "+"100 Continue\n");
	   			bw.write("\n");
	   		}
             
             bw.write(htmlVersion+" "+"403 Forbidden\n");
             bw.write("Content-Type: text/html\n");
             bw.write("Content-Length: 1354\n");
             
                Date headDate =new Date(System.currentTimeMillis());
                DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        	   	String date=headformatter.format(headDate).concat(" GMT");
                
                bw.write("Date: "+date+"\n"); 
     	   	
             bw.write("Server: HTTPServer\n");
             bw.write("Connection: close\n");
             bw.write("\n");
            
             if(met.equalsIgnoreCase("GET") || met.equalsIgnoreCase("POST")){
            bw.write("<html><head><title>403 Forbidden</title></head><body>");
             bw.write("<h1>403 Forbidden</h1></br>");
             bw.write("<h3>The URL "+file+" is not under your privileges</h3></br>");
           bw.write("</body></html>");           
           }

        }
        catch(Exception e)
        {
        	logger.error(e.toString());
  		 // e.printStackTrace();
        	servererrorfn(htmlVersion);
        }
        finally
        {
     	   
     	   try {
     		  
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				logger.error(e.toString());
		//		e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					logger.error(e.toString());
			//		e.printStackTrace();
				}
			}
          
        }
    	
    	
    	
    }//file forbidden fn
    	   
    /*
	    * Fn:  IF THE PASSED PATH IS A FILE and it is NOT FOUND
	    */
    public void notfoundfn(File reader, String htmlVersion, String file)
    {
    	 BufferedWriter bw=null;
         try{
             
              bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
             
          	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{
	   			bw.write(htmlVersion+" "+"100 Continue\n");
	   			bw.write("\n");
	   		}
              
              bw.write(htmlVersion+" "+"404 Not Found\n");
                  bw.write("Content-Type: text/html\n");
                  bw.write("Content-Length: 1354\n");
                  
                  Date headDate =new Date(System.currentTimeMillis());
                  DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
          	   	String date=headformatter.format(headDate).concat(" GMT");
                  
                  bw.write("Date: "+date+"\n"); 
          	   	
                  bw.write("Server: HTTPServer\n");
                  bw.write("Connection: close");
                  bw.write((String.valueOf((char)13)));
                  bw.write((String.valueOf((char)10)));
                  bw.write((String.valueOf((char)13)));
                  bw.write((String.valueOf((char)10)));
                   
                  if(met.equalsIgnoreCase("GET") || met.equalsIgnoreCase("POST")){
                   bw.write("<html><head><title>404 Not Found</title></head><body>");
                    bw.write("<h1>404 Not Found</h1></br>");
                    bw.write("<h3>The URL "+file+" is not found on the server</h3></br>");
                  bw.write("</body></html>");
       }
       
    }
         catch(Exception e)
         {
        	 logger.error(e.toString());
   		//  e.printStackTrace();
         	servererrorfn(htmlVersion);
         }
         finally
         {
      	   
      	   try {
      		  
 				bw.flush();
        		bw.close();
 				 
 			} catch (IOException e) {
 				logger.error(e.toString());
 				//e.printStackTrace();
 			}
 			finally
 			{
 				 try {
 					client.close();
 				} catch (IOException e) {
 					logger.error(e.toString());
 				//	e.printStackTrace();
 				}
 			}
           
         }
         
    }//notfound function close
    
    /*
	    * Fn:  IF THE PASSED PATH CAUSES SERVER ERROR
	    */
    public void servererrorfn(String htmlVersion)
    	   {
    		   BufferedWriter bw=null;
    	         try{
    	             
    	              bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    	     
    	          	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
        	   		{
        	   			bw.write(htmlVersion+" "+"100 Continue\n");
        	   			bw.write("\n");
        	   		}
    	              
    	              bw.write(htmlVersion+" "+"500 Internal Server Error\n");
                      bw.write("Content-Type: text/html\n");
                      bw.write("Content-Length: 1354\n");
                      
                      Date headDate =new Date(System.currentTimeMillis());
                      DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
              	   	String date=headformatter.format(headDate).concat(" GMT");
                      bw.write("Date: "+date+"\n"); 
              	   	
                      bw.write("Server: HTTPServer\n");
                      bw.write("Connection: close");
                      bw.write((String.valueOf((char)13)));
                      bw.write((String.valueOf((char)10)));
                      bw.write((String.valueOf((char)13)));
                      bw.write((String.valueOf((char)10)));
                       
                      if(met.equalsIgnoreCase("GET") || met.equalsIgnoreCase("POST")){
                       bw.write("<html><head><title>500 Internel Server Error</title></head><body>");
                       bw.write("<h1>500 Internel Server error while displaying file/directory info</h1></br>");
                       bw.write("</body></html>");
           }
    	         }
    	         catch(Exception e)
    	         {
    	        	 logger.error(e.toString());
           		  //e.printStackTrace();
    	         }
    	         finally
    	         {
    	      	   
    	      	   try {
    	      		  
    	 				bw.flush();
    	 				bw.close();
    	 				 
    	 			} catch (IOException e) {
    	 				logger.error(e.toString());
    	 				//e.printStackTrace();
    	 			}
    	 			finally
    	 			{
    	 				 try {
    	 					client.close();
    	 				} catch (IOException e) {
    	 					logger.error(e.toString());
    	 					//e.printStackTrace();
    	 				}
    	 			}
    	           
    	         }
    	         
    	   
    	   }//server error fn
    	   	 
    /*
	    * Fn:  IF THE PASSED HEDAERS HAS SOME PROBLEM
	    */
    public int headerErrorFn(String header,int tracker)
	   {
    	
       	
    	
    	int pointer=2;
    	int index=0,occ=0;
    	while((index=header.indexOf(" ",index+1 ))!=-1)
    	{
    		occ++;
    	}
    	if(occ!=2 || header.substring(0, 1).equalsIgnoreCase(" "))
    	{
    		header="HTTP/1.0";
    		pointer=0;
    	}
    	    	
    	else if((header.indexOf(" HTTP/1.1")==-1 && header.indexOf(" HTTP/1.0")==-1) && ((header.indexOf("GET ")!=-1) || (header.indexOf("HEAD ")!=-1) || (header.indexOf("POST ")!=-1)))
    	{
    		header="HTTP/1.0";
    		pointer=0;
    	}
    	else if ((header.indexOf("GET ")==-1) && header.indexOf("HEAD ")==-1 && header.indexOf("POST ")==-1)
    	{    	   	
    	if ((header.indexOf(" HTTP/1.0")==-1 && header.indexOf(" HTTP/1.1")!=-1))
    	{
    		header="HTTP/1.1";
    		pointer=1;
    	}
    	
    	else if (header.indexOf(" HTTP/1.0")!=-1 && header.indexOf(" HTTP/1.1")==-1)
    	{
    		header="HTTP/1.0";
    		pointer=1;
    	}
    	}
   	else if(header.indexOf(" HTTP/1.1")!=-1 && tracker==0)
    	{
    		header="HTTP/1.1";
    		pointer=0;
    	}
	   if(pointer!=2)
	   {
		   
    	
    	BufferedWriter bw=null;
	         try{
	             
	              bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	              if(pointer==0)
	              {
	              bw.write(header+" "+"400 Bad Request\n");
	              }
	              else
	              {
	              bw.write(header+" "+"501 Not Implemented\n"); 
	              }
               bw.write("Content-Type: text/html\n");
               bw.write("Content-Length: 1354\n");
               
               Date headDate =new Date(System.currentTimeMillis());
               DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
       	   	String date=headformatter.format(headDate).concat(" GMT");
               
               bw.write("Date: "+date+"\n"); 
       	   	
               bw.write("Server: HTTPServer\n");
               bw.write("Connection: close\n");
                bw.write("\n");
                
    if(met.equalsIgnoreCase("GET") || met.equalsIgnoreCase("POST")){
    	if(pointer==0)
    	{
                bw.write("<html><head><title>400 Bad Request</title></head><body>");
                 bw.write("<h1>400 BAD REQUEST.. CHECK YOUR HEADERS</h1></br>");
    	}
    	else
    	{
    		bw.write("<html><head><title>501 NOT IMPLEMENTED</title></head><body>");
            bw.write("<h1>501 METHOD NOT IMPLEMENTED.. CHECK YOUR HEADERS</h1></br>");
	
    	}
               bw.write("</body></html>");
    }
	         }
	         catch(Exception e)
	         {
	        	 logger.error(e.toString());
    		//  e.printStackTrace();
	         }
	         finally
	         {
	        	 
	        	 try {
					bw.close();
				} catch (IOException e) {
					logger.error(e.toString());
		//			e.printStackTrace();
				}
				
	         }
	        return pointer;
	   }
	   else
		   return pointer;
    
	   }//header error fn
	   
	   
    /*
	    * Fn:  TO CHECK IF THE PASSED PATH IS A SERVLET OR NOT 
	    */
    public boolean isServlet()
       {
    	//   System.out.println("Krishna2");
    	   String url=null;
    	   String arguments=null;
    	   String encoding=null;
    	   
    	   if(met.equalsIgnoreCase("GET"))
    	   {
    		//   System.out.println("Krishna5");
    		  String[] head=headers.get(0).split(" ");
    		//   System.out.println("Krishna5.5");
    		   
    		//   System.out.println(head[1]);
    		   String[] params;
    		   if(head[1].indexOf("?")!=-1)
    		   {
    			  // params=head[1].split("?");
    		   params=new String[2];
    		   params[0]=head[1].substring(0,head[1].indexOf("?"));
    		   params[1]=head[1].substring(head[1].indexOf("?")+1).trim();
    		   }
    		   else
    		   {
    			   params=new String[1];
        		   params[0]=head[1];	  
    		   }
    		   
    		
    		  if(params.length>1)
    		  {
    			//   System.out.println("Krishna6");
    			//   System.out.println(params[0]);
    			//  url=params[0].substring(params[0].indexOf("/")+1);
    			 url=params[0];
    		//	 System.out.println("CHECKING");
    		
    			  arguments=params[1];
    		//		 System.out.println(arguments);
    			  arguments=URLDecoder.decode(arguments);
    		//	  System.out.println(arguments);
    	      }
    		  else
    		  {
    			  //url=params[0].substring(params[0].indexOf("/")+1);
    			  url=params[0];
     			 
    		  }
    		  // System.out.println("Krishna8");
    		          
    	   }
    	   else if(met.equalsIgnoreCase("POST"))
    	   {
    		   for(int ix=0;ix<headers.size();ix++)
    		   {
    			  if(ix==0)
    			  {
    				  String[] head=headers.get(0).split(" ");
    	    		  
    	    		//  url=head[1].substring(head[1].indexOf("/")+1);
    	    		  url=head[1];
    	    		  
    			  }
    	     	   else if(headers.get(ix).indexOf("Content-Type:")!=-1)
    			   {
    				   // DO STIFF TO LOAD ENCODING
    			   }
    			   else if(headers.get(ix).equalsIgnoreCase(" "))
    			   {
    				   arguments=headers.get(ix+1);
    			   }
    		   }
    	   }
    	   
    	  // System.out.println("URL HERE :: "+url);
    	    String map=null;
    	    String pathInfo=null;
    	    String servletPath=null;
    	    for(String s:ServletsInit.mappings.keySet())
    	    {
    	    	
    	    	String temp=ServletsInit.mappings.get(s);
    	   // 	System.out.println("MAPPING:  "+temp);
    	    	if(temp.indexOf("*")!=-1)
    	    	{
    	    		
    	    		if(url.charAt(url.length()-1)!='/')
    	    		{
    	    			temp=temp.substring(0,temp.length()-2);
    	    		}
    	    		temp=temp.substring(0,temp.length()-1);
    	    		if(url.regionMatches(0, temp, 0, temp.length()))
    	    		{
    	    //			System.out.pr/ntln("WORKING");
    	    			if(url.length()>temp.length()+2)
    	    			{
    	    			pathInfo=url.substring(temp.length()+1);
    	    			}
    	    //			System.out.println(pathInfo);
    	    			map=s;
    	    			servletPath=temp;
    	    			break;
    	    		}
    	    		
    	    	}
    	    	else
    	    	{
    	    		String temp1=ServletsInit.mappings.get(s);
    	    		if(temp1.length()==url.length() && url.regionMatches(0, temp1, 0, temp1.length()))
    	    		{
    	    			map=s;
    	    			servletPath=temp1;
    	    			break;
    	    		}
    	    		
    	    	}
    	    }
    	   
    	//   System.out.println("SERVLET PATH:  "+servletPath);
    	//   System.out.println("PATH INFO:  "+pathInfo);
    	  //  System.out.println(map);
    	   servlet = ServletsInit.servlets.get(map);
    	   
    	   
			if (servlet == null) {
				return true;
			}
			else
			{
			    
				System.out.println("ENTERED SERVLET");
			try{
				ServletsSession fs=null;
				
			    
			    Cookie cookie=null;
			    System.out.println("BEFORE REQ");
			    request=populateRequest(met,url,arguments,encoding,fs,servlet,pathInfo,servletPath);
				System.out.println("REQ DONE");
				response=populateResponse(cookie,request);
				System.out.println("RES DONE");
				
				
			//	ServletsRequest request = new ServletsRequest(fs);
			//	ServletsResponse response = new ServletsResponse();
		    // 	request.setMethod(met);
				
				String version;
				if(headers.get(0).indexOf("HTTP/1.1")!=-1)
				{
					version="HTTP/1.1 ";
				}
				else
				{
					version="HTTP/1.0 ";
				}
				
				if(isNotTest){
				 OutputStream bw=(client.getOutputStream());
				
		try{
		//	System.out.println(servlet.getServletName());
			     System.out.println("BEFORE PROCESS");
				 servlet.service(request, response);
				 System.out.println("AFTER PROCESS");
				 
				 System.out.println("SSESSIONSTATUS:  "+request.sessionStatus);
				 System.out.println("HAS SESSION:    "+request.hasSession());
				if(request.sessionStatus && request.hasSession())
				{
					
					ServletsSession temp=request.getSessionComp();
					
				/*	Properties s=temp.getProps();
					for(Object prop: s.keySet())
					{
						System.out.println("ONE");
						System.out.println(prop.toString());
						System.out.println("TWo");
						System.out.println(s.get(prop.toString()).toString());
						
					}*/
					Cookie tempc=new Cookie("SessionID",temp.getId());
					tempc.setMaxAge(temp.getMaxInactiveInterval());
					response.addCookie(tempc);
					}
				else if(request.sessionInvalidate())
				{
					
					ServletsSession temp=request.getSessionComp();
					Cookie tempc=new Cookie("SessionID",temp.getId());
					tempc.setMaxAge(1);
					response.addCookie(tempc);
				}
					
				ByteArrayOutputStream stream=response.outStream;
				response.setContentLength(response.getBufferSize());
				
				if(response.FirstHead!=null)
				{
					bw.write(version.concat(response.FirstHead).concat("\n").getBytes());
				}
				else{
				bw.write(version.concat("200 OK\n").getBytes());
				}
				
				for(String header: response.header.keySet())
				{
					String value=response.header.get(header);
				//	System.out.println("WRITE:"+ header.concat(": ").concat(value).concat("\n"));
					bw.write(header.concat(": ").concat(value).concat("\n").getBytes());
				}
				
				
				
		}
		catch(Exception e)
		{
			logger.error(e.toString());
		//	logger.info(e.toString());
			
			
			
		//	System.out.println(e.toString());
			e.printStackTrace();
			bw.write(version.concat("500 Internal Server Error\n").getBytes());
			for(String header: response.header.keySet())
			{
				String value=response.header.get(header);
				bw.write(header.concat(": ").concat(value).concat("\n").getBytes());
			}
			
			bw.write("\n<html><head><title>500 Internal Server Error</title></head><body>".concat("<b>Internal Error</b></br>"+e.toString()).concat("</body</html>").getBytes());
			//ERROR IN EXPLORING SERVLETS
		}
				
			bw.write("\n".getBytes());	
				
		if(response.getBufferSize()!=0){		
		bw.write(response.outStream.toByteArray());
		}
	    
				 
				 
		          
				bw.close();
				}//if Not Test
				fs = (ServletsSession) request.getSession(false);
		    	
		    	
				}
				catch(Exception e)
				{
					logger.error(e.toString());
					e.printStackTrace();
		//			System.out.println(e.toString()+" ERROR IN SERVLET EXEC");
				}
				 return false;
			}
			
       }//public Servelt code
       
       
       
       /*
	    * Fn:  A FN TO POPULATE THE SERVLET RESPONSE OBJECT ACCORDINGLY AND RETURN IT
	    */
       public ServletsResponse populateResponse(Cookie cookie,ServletsRequest req)
       {
    	   ServletsResponse res=new ServletsResponse();
    	   
    	   //addCookie
    /*	   
    	   if(req.getSession().getId()!="NULL")
    	   {
    	    cookie=new Cookie("Session",req.getSession().getId());
			cookie.setMaxAge(1000000);
			cookie.setVersion(0);
			res.addCookie(cookie);
    	   
    	   }
    	   else
    	   {
    	//	   cookie=new Cookie("Master","Vamsee K Yarlagadda");
   		//	   cookie.setMaxAge(1000000);
   		//	   cookie.setVersion(0);
    	   }
    	*/      
    	   
    	   //addDateHeader(arg0, arg1)
    	   res.addDateHeader("Date", System.currentTimeMillis());
    	   
    	   //addHeader(arg0, arg1)
    	   //FINISHED
    	      
    	   //addIntHeader(arg0, arg1)
    	   //FINISHED
    	   
    	   //containsHeader(arg0)
    	   //FINSIHED
    	   
    	   //encodeRedirectUrl(arg0)
    	   //FINISHED
    	   
    	   //encodeRedirectURL(arg0)
    	   //FINISHED
    	   
    	   
    	   //encodeUrl(arg0)
    	   //FINISHED
    	   
    	   //encodeURL(arg0)
    	   //FINISHED
    	   
    	   //flushBuffer()
    	   //FINISED
    	   	   
    	   //getBufferSize()
    	   //FINISHED
    	   
    	   //getCharacterEncoding()
    	  // FINSIHED
    	   
    	  
    	   
    	   //getContentType()
    	   //FINISHED
    	   
    	   //getLocale()
    	   //FINISHED
    	   
    	   //getOutputStream()
    	  //FINISHED
    	   
    	   //getWriter()
    	   //FINISHED
    	   
    	   //isCommitted()
    	   //FINISHED
    	   
    	   //reset()
    	   //FINISHED
    	   
    	   //resetBuffer()
    	   //FINISHED
    	   
    	   //sendError(arg0)
    	   //FINISHED
    	   
    	   //sendError(arg0, arg1)
    	   //FINISHED
    	   
    	   //sendRedirect(arg0)
    	   //FINISHED
    	   
    	   //setBufferSize(arg0)
    	   //FINISHED
    	   
    	   //setCharacterEncoding(arg0)
    	   res.setCharacterEncoding("ISO-8859-1");
    	   
    	   //setContentLength(arg0)
    	   //FINISHED
    	   
    	   //setContentType(arg0)
    	   res.setContentType("text/html");
    	   
    	   //setDateHeader(arg0, arg1)
    	    //FINSIHED
    	   
    	   //setHeader(arg0, arg1)
    	   res.setHeader("Server","HTTPServer");
    	   res.setHeader("Connection", "close");
    	   
    	   //setIntHeader(arg0, arg1)
    	   //FINSIHED
    	   
    	   //setLocale(arg0)
    	   //FINISHED
    	   
    	   //setStatus(arg0)
    	   //FINSIEHD
    	   
    	   //setStatus(arg0, arg1)
    	   //FINISHED
    	 
    	   
    	   
    	   
    	   
    	   
    	   
    	   
    	   return res;
    	   
       }//Servlet Response
       
       
       
       /*
	    * Fn:  A FN TO POPULATE THE SERVLET REQUEST OBJECT ACCORDINGLY AND RETURN IT
	    */       
    public ServletsRequest populateRequest(String method,String url, String parameters,String encoding,ServletsSession fs,Servlet servlet,String pathInfo, String servletPath)
    {
    	
    	ServletsRequest req=new ServletsRequest(fs);
    	
    //	System.out.println("THIS IS SERVLET:  "+servlet);
    	    String relativePath=null;
    		String absolutePath=null;
    		String servletName=null;
    		
    		if(isNotTest)
    		{
    		if(HttpServer.xml.indexOf("\\")!=-1)
    		{
    		absolutePath=HttpServer.xml.substring(0, HttpServer.xml.lastIndexOf("\\")+1);
    		absolutePath=absolutePath.concat("classes\\");
    		absolutePath=absolutePath.concat(url);
    		servletName=url;
    		relativePath="\\".concat(url);
    		}
    		else
    		{
    			absolutePath=HttpServer.xml.substring(0, HttpServer.xml.lastIndexOf("/")+1);
        		absolutePath=absolutePath.concat("classes/");
        		absolutePath=absolutePath.concat(url);
        		servletName=url;
        		relativePath="/".concat(url);
    		}
    		}
    		
    		String acceptHead=null,acceptCharsetHead=null,acceptEncodingHead=null,acceptLanguageHead=null,cacheControlHead=null,connectionHead=null,dataHead=null,dateHead=null,hostHead=null,ifModSinceHead=null,ifUnmodSinceHead=null,pragmaHead=null,refererHead=null,transferEncodingHead=null,userAgentHead=null,contentLengthHead=null,contentTypeHead=null;
    		String CookieHead=null;
    		for(int ix=1;ix<headers.size();ix++)
    		{
    			if(headers.get(ix).equalsIgnoreCase(" "))
    			{
    				ix=ix+1;
    				req.heads.put("BODY".toLowerCase(),headers.get(ix));
    			//	System.out.println(headers.get(ix));
    			//	System.out.println("FINIHSED PARSING HEADERS IN POST WITH BODY");
    				continue;
    			}
    		//	System.out.println("PRE: "+headers.get(ix));
    			String []temp=headers.get(ix).split(":");
    			String header=temp[0].trim();
    	  //   	System.out.println("CHECK: "+temp[0]+"__"+temp[1]);
    		
    		
    			req.heads.put(header.toLowerCase(), temp[1].trim());
    			
    		
    		
    		/*	if(header.indexOf("-")!=-1)
    			{
    				String[] temps=header.split("-");
    				header="";
    				for(int iws=0;iws<temps.length;iws++)
    				{
    					header.concat(temps[iws]);
    				}
    			}
    		*/
    			
    			if(header.equalsIgnoreCase("Accept"))
    			{
    				acceptHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Accept-Charset"))
    			{
    				acceptCharsetHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Accept-Encoding"))
    			{
    				acceptEncodingHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Accept-Language"))
    			{
    				acceptLanguageHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Host"))
    			{
    				hostHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("If-Modified-Since"))
    			{
    				ifModSinceHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("If-Unmodified-Since"))
    			{
    				ifUnmodSinceHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Referer"))
    			{
    				refererHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("User-Agent"))
    			{
    				userAgentHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Cache-Control"))
    			{
    				cacheControlHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Connection"))
    			{
    				connectionHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Date"))
    			{
    				dateHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Pragma"))
    			{
    				pragmaHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Transfer-Encoding"))
    			{
    				transferEncodingHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Content-Length"))
    			{
    				contentLengthHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase("Content-Type"))
    			{
    				contentTypeHead=headers.get(ix);
    			}
    			else if(header.equalsIgnoreCase(" "))
    			{
    				dataHead=headers.get(ix+1);
    				ix++;
    			}
    			else if(header.equalsIgnoreCase("Cookie"))
    			{
    				CookieHead=headers.get(ix);
    			}
    				
    			
    		}//FOR LOOP FOR HEADERS
    		
    		
    		    		
    		//getAttribute
    		// FINISHED
    		
    		
    		//getAttributeNames
    		// FINISIHED
    			
    		//getAuthType
    		req.AuthType=req.BASIC_AUTH;
    		
    		//getCharacter

    		
    		if(encoding!=null)
        	{
        		try {
    				req.setCharacterEncoding(encoding);
    			} catch (UnsupportedEncodingException e) {
    				logger.error(e.toString());
    				e.printStackTrace();
    		//		System.out.println(e.toString() +" Error in putting encoding");
    		//		e.printStackTrace();
    			}
        	}
    		
    		
    		if(isNotTest){
    		//getContentLength
    		if(!met.equalsIgnoreCase("POST"))
    		{
    			req.ContentLength=-1;
    		}
    		else
    		{
    			try{
    			String x=contentLengthHead.substring(contentLengthHead.indexOf(":")+1).trim();
    			
    			req.ContentLength=Integer.parseInt(x);
    			}
    			catch(Exception e)
    			{
    				logger.error(e.toString());
    				e.printStackTrace();
    				req.ContentLength=200;
    			}
    		}
    		}
    		//getContentType
    		if(contentTypeHead!=null)
    		{
    		req.ContentType=contentTypeHead.substring(contentTypeHead.indexOf(":")+1).trim();
    		}
    		
    		//getContextPath
    		req.ContextPath="/";
    		
    		//getCookies
    		//FINISHED
    		
    		//getDateHeader(String)
    		if(ifModSinceHead!=null)
    		{
    			req.DateHeader=ifModSinceHead.substring(ifModSinceHead.indexOf(":")+1).trim();
       		}
    		
    		//getHeader(String)
    		//FINISHED
    		
    		//getHeaderNames
    		//FINISHED
    		
    		//getHeaders(String)
    		//FINISHED
    		
    	    //getIntHeader(String)
    		//FINSIHED
    		
    	//	System.out.println(client);
    		
    		if(isNotTest){
    		//getLocalAddr
    		req.LocalAddr=client.toString().substring(client.toString().indexOf("/")+1, client.toString().indexOf(","));
    		}
    		
    		//getLocale
    		//FINSIHED
    		
    		//getLocales
    		//FINSIHED
    		
    		//getLocalName
    		req.LocalName="localhost";
    		    		
    		
    		//getLocalPort
    		if(isNotTest){
    		req.LocalPort=Integer.parseInt(client.toString().substring(client.toString().indexOf("localport")+10,client.toString().length()-1));
    		}   		
    		    		
    		//getMethod
    		req.setMethod(method);
    		
    		
    		//getParameter(String)
    		//FINISHED
    		if(parameters!=null)
        	{
    		//	parameters=URLDecoder.decode(parameters);
    			
        		String []params=parameters.split("\\?|&|=");
        		for(int i=0;i<params.length;i=i+2)
        		{
        			
        			if(i==params.length-1)
        			{
        				req.setParameter(URLDecoder.decode(params[i]), "null");
        			}
        			else
        			{
        				if(params[i+1].indexOf(",")!=-1)
        				{
        					String[] ind=params[i+1].split(",");
        					for(int k=0;k<ind.length;k++)
        					{
        						req.setParameter(URLDecoder.decode(params[i]), URLDecoder.decode(ind[k]));
        					}
        				}
        				else{
        			req.setParameter(URLDecoder.decode(params[i]), URLDecoder.decode(params[i+1]));
        				}
        			}
        			
        		}		
        	}
    		//getParameterNames
    		//FINSIHED
    		
    		//getParameterValues(String)
    		//FINSIHED
    		
    		//getParamaterMap
    		//FINSIHED
    		
    		//getPathInfo
    		//FINSHED
    		req.PathInfo=pathInfo;
    		
    		//getPathTranslated
    		req.PathInfoTranslated=null;
    		
    		//getProtocol
    		req.Protocol=headers.get(0).split(" ")[2];
    		
    		//getQueryString
    		if(met.equalsIgnoreCase("GET"))
    		req.QueryString=parameters;
    		
    		//getReader
    		//FINISHED
    		
    		if(isNotTest){
    		//getRealPath(String)
    		//FINISHED
    		//req.RealPath=absolutePath;
    		if(HttpServer.xml.indexOf("/")!=-1)
    		{
    		req.RealPath="Relative to Context Path is:  "+servlet.toString().split("@")[0].replaceAll(".","/");
    		}
    		else
    		{
    			System.out.println(servlet.toString().split("@")[0]);
    			req.RealPath="Relative to Context Path is:  "+servlet.toString().split("@")[0].replaceAll(".", "/");
    		}
    		}
    		
    		if(isNotTest){
    		//getRemoteAddr
    		req.RemoteAddr=client.toString().substring(client.toString().indexOf("/")+1, client.toString().indexOf(","));
    		}
    		
    		//getRemoteHost
    		req.RemoteHost="localhost";
    	
    		if(isNotTest){
    		//getRemotePort
    		String portAndLocal=client.toString().substring(client.toString().indexOf(",")+6);
    		String remoteport=portAndLocal.substring(0,portAndLocal.indexOf(","));
    		req.RemotePort=Integer.parseInt(remoteport);
    		}
    		
    		//getRemoteUser
    		req.RemoteUser=null;
    		
    		//getRequestSessionID
    		//FNISHED
    		
    		//getScheme
    		req.Scheme="http";
    		
    		//getServerName
    		req.ServerName="127.0.0.1";
    		
    		if(isNotTest){
    		//getServerPort
    		req.ServerPort = Integer.parseInt(client.toString().substring(client.toString().indexOf("localport")+10,client.toString().length()-1));
    		}
    		
    		//getServletPath
    		req.ServletPath=servletPath;
    		
    		req.Servlet=(HttpServlet) servlet;
    		//getSession
    		//FINISHED
    		
    		boolean tracker=true;
    		if(CookieHead!=null)
    		{
    	//		System.out.println("LAYER1");
    			String []initSplit=CookieHead.split(":");
    			
    			if(initSplit[1].indexOf(";")!=-1)
    			{
    				String []pairs=initSplit[1].split(";");
    				
    				for(int i=0;i<pairs.length;i++)
    				{
    					
    					String[] KeyValue=pairs[i].trim().split("=");
    					//fs.setAttribute(KeyValue[0].trim(), KeyValue[1].trim());
    					if(KeyValue[0].trim().indexOf("SessionID")!=0)
    					{
    					    tracker=false;
    						fs=sessions.get(KeyValue[1].trim());
    						
    						 if(fs!=null)
        					 {
        					 if(fs.t!=null)
        					 {
        					 fs.t.interrupt();
        					 System.out.println("Thread going to refresh");
        					 fs.refreshThread();
        					 System.out.println("Refresh done");
        					 }
        					 }
    						 if(fs!=null)
        					 {
    						req.PutSession(fs, true);
    						req.isSessionFCoookie=true;
    						System.out.println("RequestSESS:  "+fs.sessionID);
        					 }
    					}
    					else
    					{
    					req.setAttribute(KeyValue[0].trim(), KeyValue[1].trim());
    					}
    				}
    			}
    			else
    			{
    				String[] KeyValue=initSplit[1].trim().split("=");
    				if(KeyValue[0].trim().indexOf("SessionID")!=-1)
					{
    					 tracker=false;
    					 fs=sessions.get(KeyValue[1].trim());
    					 
    					 if(fs!=null)
    					 {
    					 if(fs.t!=null)
    					 {
    					 fs.t.interrupt();
    					 System.out.println("Thread going to refresh");
    					 fs.refreshThread();
    					 System.out.println("Refresh done");
    					 }
    					 }
    					
    					 if(fs!=null)
    					 {
						 req.PutSession(fs, true);
						 req.isSessionFCoookie=true;
							System.out.println("RequestSESS:  "+fs.sessionID);
							System.out.println("IS Session PRESENT:  "+sessions.containsKey(fs.sessionID));
    					 }
    					 
    					 }
    				else{
					//fs.setAttribute(KeyValue[0].trim(), KeyValue[1].trim());
					req.setAttribute(KeyValue[0].trim(), KeyValue[1].trim());
    				}
    			}
    			
    			//req.PutSession(fs,true);
    			
    			/*
    			if(CookieHead.indexOf("Session")!=-1)
    			{
    				System.out.println("LAYER2");
    				String temp=CookieHead.substring(CookieHead.indexOf("Session"));
    				String []temps=temp.split("=");
    				if(temps[1].indexOf(";")!=-1)
    				{
    					temp=temps[1].substring(0,temps[1].indexOf(";")).trim();
    				}
    				else
    				{
    					System.out.println("LAYER3");
    					temp=temps[1].trim();
    				}
    				System.out.println("VAMSEEKRISHNA");
    				System.out.println(temp);
    				System.out.println("AS FOLLOWS");
    				for(String s: sessions.keySet())
    				{
    					System.out.println(s);
    				}
    				
    				if(sessions.get(temp)!=null)
    				{
    					System.out.println("LAYER4");
    					System.out.println("VAMSEE");
    				tracker=false;
    			
    			fs.setAttribute("Session", temp);
    			req.PutSession(fs,true);
    				}
    			}
    			*/
    			
    			
    		}
    		if(tracker)
    		{
    			req.PutSession(fs,false);
    		}
    	/*	
    		if(tracker)
    		{
    			long number;
    			while((number=random.nextLong())<0);
    			fs=new ServletsSession();
    			fs.sessionID=String.valueOf(number);
    			//fs.setAttribute(String.valueOf(number), this);
    		    fs.setMaxInactiveInterval(10000);
    		    System.out.println("INACTIBE INTERVAl: "+fs.getMaxInactiveInterval());
    		    Thread t=new Thread(new SessionHandler(fs.getMaxInactiveInterval(), number));
    		    t.start();
    		    
    			sessions.put(String.valueOf(number),servlet);
        		
        		req.PutSession(fs);
    		}
    		*/
    		
    		
    		//getRequestURI
    		//getRequestURL
    		String []no=headers.get(0).split(" ");
    		if(no[1].indexOf("?")!=-1)
    		{
    			req.uri=no[1].substring(0, no[1].indexOf("?"));
    			req.url="http://"+"localhost"+":"+req.ServerPort+req.uri;
    		}
    		else
    		{
    			req.uri=no[1];
    			req.url="http://"+"localhost"+":"+req.ServerPort+req.uri;
    		}
    		
    		
    		
    		
    	
    	
    
    	
    	
    	
    	
    	
    	
    	return req;
    }//Request class

       
       
       
}//HandleReq class close




















