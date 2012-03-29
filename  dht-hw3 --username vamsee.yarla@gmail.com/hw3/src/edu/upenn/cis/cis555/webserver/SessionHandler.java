package edu.upenn.cis.cis555.webserver;



import org.apache.log4j.Logger;

/*
 * Class to all the  session handler threads to sleep until specified time and then terminate by removing 
 * the data from the Hashtable of sessions. It helps in terminating sessions.
 */

public class SessionHandler implements Runnable{

	int time;
	String session;
	 private static Logger logger = Logger.getLogger(SessionHandler.class);
	public SessionHandler(int arg0,String session) {
		time=arg0;
		this.session=session;
	}
	
	
	public void run() {
		System.out.println("ENTERED RUN");
		try {
			
			Thread.sleep(time);
			System.out.println("THREAD DIDED");
			HandleReq.sessions.remove(session);
			
		} catch (InterruptedException e) {
			logger.error(e.toString());
		//	e.printStackTrace();
		}
		
	}


	
	

	
}
