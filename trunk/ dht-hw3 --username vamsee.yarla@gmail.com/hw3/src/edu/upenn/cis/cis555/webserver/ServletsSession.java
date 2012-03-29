package edu.upenn.cis.cis555.webserver;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import java.util.UUID;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;


import org.apache.log4j.Logger;

/**
 * @author Vamsee K. Yarlagadda
 */
class ServletsSession implements HttpSession {

	private int inactiveInterval=-1;
	boolean tracker=true,threadRun=false;
	//public String mainHead=null;
	public String sessionID=null;
	Servlet servlet;
	long creationTime;
	private static Logger logger = Logger.getLogger(ServletsSession.class);
	boolean invalidated=false;
	
	static Hashtable <String,Long> sessionAccess= new Hashtable<String, Long>();
	Thread t=null;
	
	/*
	 * Function take servlet as input and save it inside the class variable. 
	 * Called as soon as the object has been created.
	 */
	
	public ServletsSession(Servlet arg0) {
		
		servlet=arg0;
		creationTime=System.currentTimeMillis();
		
		sessionID=UUID.randomUUID().toString();
		HandleReq.sessions.put(sessionID, this);
		m_valid=true;
		
		if(ServletsInit.sessionTimeOut.equalsIgnoreCase("-1"))
		{
			
		}
		else{
			inactiveInterval=Integer.parseInt(ServletsInit.sessionTimeOut)*60*1000;
		 t=new Thread(new SessionHandler(Integer.parseInt(ServletsInit.sessionTimeOut)*60*1000, getId()));
		 t.start();
		 threadRun=true;
		}
	}
	
	/*
	 * Function take gets called as soon as the object has been created.
	 *  default constructor
	 */
	
	public ServletsSession()
	{
		creationTime=System.currentTimeMillis();
		sessionID=UUID.randomUUID().toString();
		HandleReq.sessions.put(sessionID, this);
		m_valid=true;
		
		if(ServletsInit.sessionTimeOut.equalsIgnoreCase("-1"))
		{
			
		}
		else{
			inactiveInterval=Integer.parseInt(ServletsInit.sessionTimeOut)*60*1000;
		 t=new Thread(new SessionHandler(Integer.parseInt(ServletsInit.sessionTimeOut)*60*1000, getId()));
		 t.start();
		 threadRun=true;
		}
		
	}
	
	public void refreshThread()
	{
		if(inactiveInterval==-1)
		{
			
		}
		else{
		 t=new Thread(new SessionHandler(inactiveInterval, getId()));
		 t.start();
		 threadRun=true;
		 HandleReq.sessions.put(getId(), this);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getProps()
	 * Function to garb the properties of this object
	 */
	public Properties getProps() {
		
		return m_props;
		}
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 * Returns the time when this session was created, measured in milliseconds since midnight January 1, 1970 GMT. 
	 */
	public long getCreationTime() {
		
		return creationTime;
	}

	/* (non-Javadoc)
	 * Returns a string containing the unique identifier assigned to this session. The identifier is assigned by the servlet container and is implementation dependent. 
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	public String getId() {
		
		return sessionID;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 * Returns the last time the client sent a request associated with this session, as the number of milliseconds since midnight January 1, 1970 GMT, and marked by the time the container received the request.

Actions that your application takes, such as getting or setting a value associated with the session, do not affect the access time. 
	 */
	public long getLastAccessedTime() {
		
		return sessionAccess.get(getId());
	}

	/* (non-Javadoc)
	 * Returns the ServletContext to which this session belongs. 
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	public ServletContext getServletContext() {
		
		return servlet.getServletConfig().getServletContext();
	}

	/* (non-Javadoc)
	 * Returns the maximum time interval, in seconds, that the servlet container will keep this session open between client accesses. After this interval, the servlet container will invalidate the session. The maximum time interval can be set with the setMaxInactiveInterval method. 
	 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	public void setMaxInactiveInterval(int arg0) {
		
		inactiveInterval=arg0;
		if(t!=null)
		{
		t.interrupt();
		}
		threadRun=false;
		if(!threadRun)
		{
			 t=new Thread(new SessionHandler(inactiveInterval, getId()));
 		     t.start();
 		     threadRun=true;
 		     HandleReq.sessions.put(getId(), this);
 			 sessionAccess.put(getId(), System.currentTimeMillis());
     		
		}
		
	}

	/* (non-Javadoc)
	 * Specifies the time, in seconds, between client requests before the servlet container will invalidate this session.

An interval value of zero or less indicates that the session should never timeout. 
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	public int getMaxInactiveInterval() {
		
		return inactiveInterval;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getSessionContext()
	 */
	public HttpSessionContext getSessionContext() {
	
		return null;
	}

	/* (non-Javadoc)
	 * Returns the object bound with the specified name in this session, or null if no object is bound under the name. 
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
	
		return m_props.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
	 */
	public Object getValue(String arg0) {
	
		return m_props.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 * Returns an Enumeration of String objects containing the names of all the objects bound to this session. 
	 */
	public Enumeration getAttributeNames() {
		
		return m_props.keys();
	}

	/* (non-Javadoc)
	 * an array of String objects specifying the names of all the objects bound to this session 
	 * @see javax.servlet.http.HttpSession#getValueNames()
	 */
	public String[] getValueNames() {
		int count=0;
		for(Object s: m_props.keySet())
		{
			count++;
		}
		String [] temp=new String[count];
		count=0;
		for(Object s: m_props.keySet())
		{
			temp[count]=s.toString();
			count++;
		}
		
		return temp;
	}

	/* (non-Javadoc)
	 *     Binds an object to this session, using the name specified. If an object of the same name is already bound to the session, the object is replaced.

    After this method executes, and if the new object implements HttpSessionBindingListener, the container calls HttpSessionBindingListener.valueBound. The container then notifies any HttpSessionAttributeListeners in the web application.

    If an object was already bound to this session of this name that implements HttpSessionBindingListener, its HttpSessionBindingListener.valueUnbound method is called.

    If the value passed in is null, this has the same effect as calling removeAttribute().


	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		
		
		m_props.put(arg0, arg1);
		
	}

	/* (non-Javadoc)
	 * Parameters:
    name - the name to which the object is bound; cannot be null
    value - the object to be bound; cannot be null 
	 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
	 */
	public void putValue(String arg0, Object arg1) {
		
		m_props.put(arg0, arg1);
	}

	/* (non-Javadoc)
	 * Removes the object bound with the specified name from this session. If the session does not have an object bound with the specified name, this method does nothing.

After this method executes, and if the object implements HttpSessionBindingListener, the container calls HttpSessionBindingListener.valueUnbound. The container then notifies any HttpSessionAttributeListeners in the web application. 
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		m_props.remove(arg0);
	}

	/* (non-Javadoc)
	 * Parameters:
    name - the name of the object to remove from this session 
	 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
	 */
	public void removeValue(String arg0) {
		m_props.remove(arg0);
	}

	/* (non-Javadoc)
	 * Invalidates this session then unbinds any objects bound to it. 
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	public void invalidate() {
		invalidated=true;
		HandleReq.sessions.remove(getId());
		sessionAccess.remove(getId());
		if(t!=null){
		t.interrupt();
		t=null;
		threadRun=false;
		}
		
		threadRun=false;
		m_valid = false;
	}

	/* (non-Javadoc)
	 * Returns true if the client does not yet know about the session or if the client chooses not to join the session. For example, if the server used only cookie-based sessions, and the client had disabled the use of cookies, then a session would be new on each request. 
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	public boolean isNew() {
		
		if(getId()!=null && threadRun )
		return true;
		
		else
			return false;
	}

	public boolean isInvalidated()
	{
		
	      return invalidated;
	}
	/* (non-Javadoc)
	 * Returns true if the client does not yet know about the session or if the client chooses not to join the session. For example, if the server used only cookie-based sessions, and the client had disabled the use of cookies, then a session would be new on each request. 
	 * @see javax.servlet.http.isValid#isNew()
	 */
	boolean isValid() {
		
			if(m_valid && (HandleReq.sessions.get(sessionID)!=null))
			{
			return true ;
			}
			else
			{
				return false;
			}
		
	}
	
	private Properties m_props = new Properties();
	private boolean m_valid = false;
}
