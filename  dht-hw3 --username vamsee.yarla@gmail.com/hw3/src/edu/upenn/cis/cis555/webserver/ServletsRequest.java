package edu.upenn.cis.cis555.webserver;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;



/**
 * @author Vamsee K. Yarlagadda
 */
public class ServletsRequest implements HttpServletRequest {

	public String AuthType=null;
	public HttpServlet Servlet;
	 private static Logger logger = Logger.getLogger(ServletsRequest.class);
	public String CharacterEncoding=null;
	public int ContentLength=-1;
	public String ContentType=null;
	public String ContextPath=null;
	public String DateHeader=null;
	public Hashtable<String,String> heads=new Hashtable<String,String>();
	public String LocalAddr=null;
	public String LocalName=null;
	public int LocalPort;
	public String RemoteAddr=null;
	public String RemoteName=null;
	public String RemoteHost=null;
	public String RemoteUser=null;
	public int RemotePort;
	public Locale Localex=null;
	public String PathInfo=null;
	public String PathInfoTranslated=null;
	public String Protocol;
	public String QueryString=null;
	public String uri;
	public String url;
	public String Scheme;
	public String ServerName;
	public int ServerPort;
	public String ServletPath;
	public boolean sessionStatus=false;
	public String RealPath;
	public boolean isSessionFCoookie=false;
	
	//DEfault Constructor
	ServletsRequest() {
	}
	//Constructor that takes session as input
	ServletsRequest(ServletsSession session) {
		m_session = session;
	}
	
	/* (non-Javadoc)
	 *     Returns the name of the authentication scheme used to protect the servlet. All servlet containers support basic, form and client certificate authentication, and may additionally support digest authentication. If the servlet is not authenticated null is returned.

    Same as the value of the CGI variable AUTH_TYPE.


	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	public String getAuthType() {
		
		return AuthType;
	}

	/* (non-Javadoc)
	 *     Returns an array containing all of the Cookie objects the client sent with this request. This method returns null if no cookies were sent.


	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	public Cookie[] getCookies() {
		
	  ArrayList<String> temp=new ArrayList<String>();
	  ArrayList<Cookie> cookies=new ArrayList<Cookie>();
	   int count=0;
		for(String cookie: heads.keySet())
		{
			if(cookie.indexOf("Cookie")!=-1 || cookie.indexOf("cookie")!=-1)
			{
				count++;
				temp.add(heads.get(cookie));
			}
		}
		if(count==0)
		{
			Cookie x=new Cookie("","");
			Cookie[] y=new Cookie[1];
			y[0]=x;
		return y;
		}
		else
		{
			
			 for(int i=0;i<count;i++)
			 {
				 String tempx=temp.remove(i);
				 tempx=tempx.trim();
				 
				 if(tempx.indexOf(";")!=-1)
				 {
					 String pairs[]=tempx.split(";");
					 for(int j=0;j<pairs.length;j++)
					 {
						 String KeyValue[]=pairs[j].trim().split("=");
						 Cookie cook=new Cookie(KeyValue[0].trim(),KeyValue[1].trim());
						 cookies.add(cook);
					 }
				 }
				 else{
				 
				       String[] keyvalue=tempx.split("=");
				       Cookie cook=new Cookie(keyvalue[0].trim(),keyvalue[1].trim());
				       cookies.add(cook);
				 }
			 }
			 
			 		 
			 Cookie[] x=new Cookie[cookies.size()];
			 for(int i=0;i<cookies.size();i++)
			 {
				 x[i]=cookies.get(i);
			 }
			 return x;
		}
	}

	/* (non-Javadoc)
	 *     Returns the value of the specified request header as a long value that represents a Date object. Use this method with headers that contain dates, such as If-Modified-Since.

    The date is returned as the number of milliseconds since January 1, 1970 GMT. The header name is case insensitive.

    If the request did not have a header of the specified name, this method returns -1. If the header can't be converted to a date, the method throws an IllegalArgumentException.


	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	public long getDateHeader(String arg0) {
		
		//arg0=arg0.substring(0,arg0.length()-4);
		if(heads.get(arg0)!=null)
		{
	
		DateFormat sysformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
		Date userdate=null;
		try {
		
			userdate=sysformatter.parse(arg0);
	    	   	 
		} catch (ParseException e) {
			logger.error(e.toString());
		//	e.printStackTrace();
		}
	    
		
		return Time.UTC(userdate.getYear(), userdate.getMonth(), userdate.getDate(), userdate.getHours(), userdate.getMinutes(), userdate.getSeconds());
	}
	
	else
	{
		return -1;
	}
	}
	/* (non-Javadoc)
	 *     Returns the value of the specified request header as a String. If the request did not include a header of the specified name, this method returns null. If there are multiple headers with the same name, this method returns the first head in the request. The header name is case insensitive. You can use this method with any request header.


	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	public String getHeader(String arg0) {
				
		return heads.get(arg0.toLowerCase());
	}

	/* (non-Javadoc)
	 *     Returns all the values of the specified request header as an Enumeration of String objects.

    Some headers, such as Accept-Language can be sent by clients as several headers each with a different value rather than sending the header as a comma separated list.

    If the request did not include any headers of the specified name, this method returns an empty Enumeration. The header name is case insensitive. You can use this method with any request header.


	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	public Enumeration getHeaders(String arg0) {
		
		Enumeration e;
		Vector<String> v=new Vector<String>();
		v.add(heads.get(arg0.toLowerCase()));
		e=v.elements();
		return e;
	}

	/* (non-Javadoc)
	 *     Returns an enumeration of all the header names this request contains. If the request has no headers, this method returns an empty enumeration.

    Some servlet containers do not allow servlets to access headers using this method, in which case this method returns null


	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	public Enumeration getHeaderNames() {
				
		Enumeration e;
		e=heads.keys();
		return e;
	}

	/* (non-Javadoc)
	 * Returns the value of the specified request header as an int. If the request does not have a header of the specified name, this method returns -1. If the header cannot be converted to an integer, this method throws a NumberFormatException.

The header name is case insensitive. 
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	public int getIntHeader(String arg0){
		
		if(heads.get(arg0.toLowerCase())!=null)
		{
			return Integer.parseInt(heads.get(arg0.toLowerCase()));
		}
		else
		{
			return -1;
		}
		
	}

	/* (non-Javadoc)
	 *     Returns the name of the HTTP method with which this request was made, for example, GET, POST, or PUT. Same as the value of the CGI variable REQUEST_METHOD.


	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	public String getMethod() {
		return m_method;
	}

	/* (non-Javadoc)
	 *     Returns any extra path information associated with the URL the client sent when it made this request. The extra path information follows the servlet path but precedes the query string and will start with a "/" character.

    This method returns null if there was no extra path information.

    Same as the value of the CGI variable PATH_INFO.


	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	public String getPathInfo() {
		 
		return PathInfo;
	}

	/* (non-Javadoc)
	 *     Returns any extra path information after the servlet name but before the query string, and translates it to a real path. Same as the value of the CGI variable PATH_TRANSLATED.

    If the URL does not have any extra path information, this method returns null or the servlet container cannot translate the virtual path to a real path for any reason (such as when the web application is executed from an archive). The web container does not decode this string.


	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	public String getPathTranslated() {
		
		return PathInfoTranslated;
	}

	/* (non-Javadoc)
	 * Returns the portion of the request URI that indicates the context of the request. The context path always comes first in a request URI. The path starts with a "/" character but does not end with a "/" character. For servlets in the default (root) context, this method returns "". The container does not decode this string.

It is possible that a servlet container may match a context by more than one context path. In such cases this method will return the actual context path used by the request and it may differ from the path returned by the ServletContext.getContextPath() method. The context path returned by ServletContext.getContextPath() should be considered as the prime or preferred context path of the application. 
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public String getContextPath() {
		// TODO Auto-generated method stub
		return ContextPath;
	}

	/* (non-Javadoc)
	 *     Returns the query string that is contained in the request URL after the path. This method returns null if the URL does not have a query string. Same as the value of the CGI variable QUERY_STRING.


	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	public String getQueryString() {
	//System.out.println("VAMSEE IS HERE");
		return QueryString;
	}

	/* (non-Javadoc)
	 *     Returns the login of the user making this request, if the user has been authenticated, or null if the user has not been authenticated. Whether the user name is sent with each subsequent request depends on the browser and type of authentication. Same as the value of the CGI variable REMOTE_USER.


	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	public String getRemoteUser() {
		
		return RemoteUser;
	}

	/* (non-Javadoc)
	 * Returns a boolean indicating whether the authenticated user is included in the specified logical "role". Roles and role membership can be defined using deployment descriptors. If the user has not been authenticated, the method returns false. 
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String arg0) {
		
		return false;
	}

	/* (non-Javadoc)
	 *     Returns a java.security.Principal object containing the name of the current authenticated user. If the user has not been authenticated, the method returns null.


	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {
		
		return null;
	}

	/* (non-Javadoc)
	 *     Returns the session ID specified by the client. This may not be the same as the ID of the current valid session for this request. If the client did not specify a session ID, this method returns null.


	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	public String getRequestedSessionId() {
	if(m_session!=null)
	{
		return m_session.getId();
	}
	else
	{
		return null;
	}
	}

	/* (non-Javadoc)
	 * Returns the part of this request's URL from the protocol name up to the query string in the first line of the HTTP request. The web container does not decode this String. For example:
First line of HTTP request 	Returned Value
POST /some/path.html HTTP/1.1		/some/path.html
GET http://foo.bar/a.html HTTP/1.0 		/a.html
HEAD /xyz?a=b HTTP/1.1		/xyz 
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	public String getRequestURI() {
	
		return uri;
	}

	/* (non-Javadoc)
	 * Reconstructs the URL the client used to make the request. The returned URL contains a protocol, server name, port number, and server path, but it does not include query string parameters.

If this request has been forwarded using RequestDispatcher.forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse), the server path in the reconstructed URL must reflect the path used to obtain the RequestDispatcher, and not the server path specified by the client.

Because this method returns a StringBuffer, not a string, you can modify the URL easily, for example, to append query parameters.

This method is useful for creating redirect messages and for reporting errors. 
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	public StringBuffer getRequestURL() {
	
		StringBuffer sb=new StringBuffer();
		sb.append(url);
		return sb;
	}

	/* (non-Javadoc)
	 * Returns the part of this request's URL that calls the servlet. This path starts with a "/" character and includes either the servlet name or a path to the servlet, but does not include any extra path information or a query string. Same as the value of the CGI variable SCRIPT_NAME.

This method will return an empty string ("") if the servlet used to process this request was matched using the "/*" pattern. 
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public String getServletPath() {
		
		return null;
	}

	/* (non-Javadoc)
	 *     Returns the current HttpSession associated with this request or, if there is no current session and create is true, returns a new session.

    If create is false and the request has no valid HttpSession, this method returns null.

    To make sure the session is properly maintained, you must call this method before the response is committed. If the container is using cookies to maintain session integrity and is asked to create a new session when the response is committed, an IllegalStateException is thrown.

 
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	public HttpSession getSession(boolean arg0) {
		if (arg0) {
			if (! hasSession()) {
				m_session = new ServletsSession(Servlet);
				sessionStatus=true;
			}
		} else {
			if (! hasSession()) {
				sessionStatus=false;
				m_session = null;
			}
		}
		
		return m_session;
	}

	/* (non-Javadoc)
	 * Returns the current session associated with this request, or if the request does not have a session, creates one. 
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	public HttpSession getSession() {
		if(m_session!=null)
		{
			System.out.println("first block");
			System.out.println(m_session.sessionID);
			sessionStatus=true;
		return m_session;
		}
		else
		{
			System.out.println("second block");
		m_session=new ServletsSession(Servlet);
		sessionStatus=true;
		return m_session;
		}
	}
	
	/* (non-Javadoc)
	 * Returns session as ServletsSession object.
	 * @see javax.servlet.http.HttpServletRequest#getSessionComp()
	 */
	public ServletsSession getSessionComp() {
		ServletsSession.sessionAccess.put(m_session.getId(), System.currentTimeMillis());
		return m_session;
	}

	/* (non-Javadoc)
	 * Checks whether the requested session ID is still valid.

If the client did not specify any session ID, this method returns false. 
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	public boolean isRequestedSessionIdValid() {
		
		if(this.m_session.isValid())
		{
		return true;
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * Checks whether the requested session ID came in as a cookie. 
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	public boolean isRequestedSessionIdFromCookie() {
	if(isSessionFCoookie)
	{
		return true;
	}
	else
	{
		return false;
	}
	}

	/* (non-Javadoc)
	 * Checks whether the requested session ID came in as part of the request URL. 
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	public boolean isRequestedSessionIdFromURL() {
		
		return false;
	}

	/* (non-Javadoc)
	 * boolean isRequestedSessionIdFromUrl()
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
	 */
	public boolean isRequestedSessionIdFromUrl() {
		
		return false;
	}

	/* (non-Javadoc)
	 * Returns the value of the named attribute as an Object, or null if no attribute of the given name exists.

Attributes can be set two ways. The servlet container may set attributes to make available custom information about a request. For example, for requests made using HTTPS, the attribute javax.servlet.request.X509Certificate can be used to retrieve information on the certificate of the client. Attributes can also be set programatically using ServletRequest#setAttribute. This allows information to be embedded into a request before a RequestDispatcher call.

Attribute names should follow the same conventions as package names. This specification reserves names matching java.*, javax.*, and sun.*. 
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		
		return m_props.get(arg0);
	}

	/* (non-Javadoc)
	 *     Returns an Enumeration containing the names of the attributes available to this request. This method returns an empty Enumeration if the request has no attributes available to it.


	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		
		return m_props.keys();
	}

	/* (non-Javadoc)
	 *     Returns the name of the character encoding used in the body of this request. This method returns null if the request does not specify a character encoding


	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		
		if(CharacterEncoding==null)
		{
			return "ISO-8859-1";
		}
		else
		{
			return CharacterEncoding;
		}
	}

	/* (non-Javadoc)
	 * Overrides the name of the character encoding used in the body of this request. This method must be called prior to reading request parameters or reading input using getReader(). Otherwise, it has no effect. 
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		CharacterEncoding=arg0;
		

	}

	/* (non-Javadoc)
	 * Returns the length, in bytes, of the request body and made available by the input stream, or -1 if the length is not known. For HTTP servlets, same as the value of the CGI variable CONTENT_LENGTH. 
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	public int getContentLength() {
		
		return ContentLength;
	}

	/* (non-Javadoc)
	 * Returns the MIME type of the body of the request, or null if the type is not known. For HTTP servlets, same as the value of the CGI variable CONTENT_TYPE. 
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	public String getContentType() {
	
		
		return ContentType;
	}

	/* (non-Javadoc)
	 * Retrieves the body of the request as binary data using a ServletInputStream. Either this method or getReader() may be called to read the body, not both. 
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	public ServletInputStream getInputStream() throws IOException {
	
		return null;
	}

	/* (non-Javadoc)
	 * Returns the value of a request parameter as a String, or null if the parameter does not exist. Request parameters are extra information sent with the request. For HTTP servlets, parameters are contained in the query string or posted form data.

You should only use this method when you are sure the parameter has only one value. If the parameter might have more than one value, use getParameterValues(java.lang.String).

If you use this method with a multivalued parameter, the value returned is equal to the first value in the array returned by getParameterValues.

If the parameter data was sent in the request body, such as occurs with an HTTP POST request, then reading the body directly via getInputStream() or getReader() can interfere with the execution of this method. 
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String arg0) {
	
	/*	
		if(m_params.getProperty(arg0)!=null)
		{
			ArrayList<String> temp=(ArrayList<String>) m_params.get(arg0);
			System.out.println("WORKING    :"+temp.get(0));
			return temp.get(0);
		}
		else
		{
		return null;
		}*/
		
		if(parameters.get(arg0)!=null)
		{
			ArrayList<String> temp=parameters.get(arg0);
			//System.out.println("WORKING    :"+temp.get(0));
			return temp.get(0);
		}
		else
		{
		return null;
		}
		
	}

	/* (non-Javadoc)
	 *     Returns an Enumeration of String objects containing the names of the parameters contained in this request. If the request has no parameters, the method returns an empty Enumeration.


	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		return parameters.keys();
	}

	/* (non-Javadoc)
	 * Returns an array of String objects containing all of the values the given request parameter has, or null if the parameter does not exist.

If the parameter has a single value, the array has a length of 1. 
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String arg0) {
	
		/*
		ArrayList<String> temp=(ArrayList<String>) m_params.get(arg0);
		
		if(temp!=null)
		{
		     String []x=new String[temp.size()];
		     for(int i=0;i<temp.size();i++)
		     {
		    	 x[i]=temp.get(i);
		     }
		     return x;
		}
		else
			return null;
			*/
		
ArrayList<String> temp=parameters.get(arg0);
		
		if(temp!=null)
		{
		     String []x=new String[temp.size()];
		     for(int i=0;i<temp.size();i++)
		     {
		    	 x[i]=temp.get(i);
		     }
		     return x;
		}
		else
			return null;
		
	}

	/* (non-Javadoc)
	 * Returns a java.util.Map of the parameters of this request.

Request parameters are extra information sent with the request. For HTTP servlets, parameters are contained in the query string or posted form data. 
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	public Map getParameterMap() {
	
		return parameters;
	}

	/* (non-Javadoc)
	 * Returns the name and version of the protocol the request uses in the form protocol/majorVersion.minorVersion, for example, HTTP/1.1. For HTTP servlets, the value returned is the same as the value of the CGI variable SERVER_PROTOCOL. 
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	public String getProtocol() {
	
		return Protocol;
	}

	/* (non-Javadoc)
	 * Returns the name of the scheme used to make this request, for example, http, https, or ftp. Different schemes have different rules for constructing URLs, as noted in RFC 1738. 
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	public String getScheme() {
	
		return Scheme;
	}

	/* (non-Javadoc)
	 * Returns the host name of the server to which the request was sent. It is the value of the part before ":" in the Host header value, if any, or the resolved server name, or the server IP address. 
	 * @see javax.servlet.ServletRequest#getServerName()
	 */
	public String getServerName() {
		// TODO Auto-generated method stub
		return ServerName;
	}

	/* (non-Javadoc)
	 *     Returns the port number to which the request was sent. It is the value of the part after ":" in the Host header value, if any, or the server port where the client connection was accepted on.


	 * @see javax.servlet.ServletRequest#getServerPort()
	 */
	public int getServerPort() {
	
		return ServerPort;
	}

	/* (non-Javadoc)
	 * Retrieves the body of the request as character data using a BufferedReader. The reader translates the character data according to the character encoding used on the body. Either this method or getInputStream() may be called to read the body, not both. 
	 * @see javax.servlet.ServletRequest#getReader()
	 */
	public BufferedReader getReader() throws IOException {
		
		String body=heads.get("BODY".toLowerCase());
		if(body!=null)
		{
			ByteArrayInputStream b=new ByteArrayInputStream(body.getBytes());
			
			BufferedReader br=new BufferedReader(new InputStreamReader((InputStream)b));
			return br;
		}
		else{
		return null;
		}
	}

	/* (non-Javadoc)
	 * Returns the Internet Protocol (IP) address of the client or last proxy that sent the request. For HTTP servlets, same as the value of the CGI variable REMOTE_ADDR. 
	 * @see javax.servlet.ServletRequest#getRemoteAddr()
	 */
	public String getRemoteAddr() {
		
		return RemoteAddr;
	}

	/* (non-Javadoc)
	 * Returns the fully qualified name of the client or the last proxy that sent the request. If the engine cannot or chooses not to resolve the hostname (to improve performance), this method returns the dotted-string form of the IP address. For HTTP servlets, same as the value of the CGI variable REMOTE_HOST. 
	 * @see javax.servlet.ServletRequest#getRemoteHost()
	 */
	public String getRemoteHost() {
	
		return RemoteHost;
	}

	/* (non-Javadoc)
	 * Stores an attribute in this request. Attributes are reset between requests. This method is most often used in conjunction with RequestDispatcher.

Attribute names should follow the same conventions as package names. Names beginning with java.*, javax.*, and com.sun.*, are reserved for use by Sun Microsystems.
If the object passed in is null, the effect is the same as calling removeAttribute(java.lang.String).
It is warned that when the request is dispatched from the servlet resides in a different web application by RequestDispatcher, the object set by this method may not be correctly retrieved in the caller servlet. 
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		m_props.put(arg0, arg1);
	}

	/* (non-Javadoc)
	 *     Removes an attribute from this request. This method is not generally needed as attributes only persist as long as the request is being handled.

    Attribute names should follow the same conventions as package names. Names beginning with java.*, javax.*, and com.sun.*, are reserved for use by Sun Microsystems.


	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		m_props.remove(arg0);

	}

	/* (non-Javadoc)
	 * Returns the preferred Locale that the client will accept content in, based on the Accept-Language header. If the client request doesn't provide an Accept-Language header, this method returns the default locale for the server. 
	 * @see javax.servlet.ServletRequest#getLocale()
	 */
	public Locale getLocale() {
		
		return null;
	}

	/* (non-Javadoc)
	 * Returns an Enumeration of Locale objects indicating, in decreasing order starting with the preferred locale, the locales that are acceptable to the client based on the Accept-Language header. If the client request doesn't provide an Accept-Language header, this method returns an Enumeration containing one Locale, the default locale for the server. 
	 * @see javax.servlet.ServletRequest#setLocale()
	 */
	
	public void setLocale(Locale arg0) {
		
		Localex=arg0;
	}
	
	/* (non-Javadoc)
	 * Returns a boolean indicating whether this request was made using a secure channel, such as HTTPS. 
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	public Enumeration getLocales() {
		
	/*	
		Enumeration e;
		Vector<Locale> v=new Vector<Locale>();
		v.add(Localex);
		e=v.elements();
		*/
		return null;
		
		
	}

	/* (non-Javadoc)
	 * Returns a RequestDispatcher object that acts as a wrapper for the resource located at the given path. A RequestDispatcher object can be used to forward a request to the resource or to include the resource in a response. The resource can be dynamic or static.

The pathname specified may be relative, although it cannot extend outside the current servlet context. If the path begins with a "/" it is interpreted as relative to the current context root. This method returns null if the servlet container cannot return a RequestDispatcher.

The difference between this method and ServletContext#getRequestDispatcher is that this method can take a relative path. 
	 * @see javax.servlet.ServletRequest#isSecure()
	 */
	public boolean isSecure() {
	
		return false;
	}

	/* (non-Javadoc)
	 * Returns a RequestDispatcher object that acts as a wrapper for the resource located at the given path. A RequestDispatcher object can be used to forward a request to the resource or to include the resource in a response. The resource can be dynamic or static.

The pathname specified may be relative, although it cannot extend outside the current servlet context. If the path begins with a "/" it is interpreted as relative to the current context root. This method returns null if the servlet container cannot return a RequestDispatcher.

The difference between this method and ServletContext#getRequestDispatcher is that this method can take a relative path. 
	 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String arg0) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
	 */
	public String getRealPath(String arg0) {
	
		return RealPath;
	}

	/* (non-Javadoc)
	 * Returns the Internet Protocol (IP) source port of the client or last proxy that sent the request. 
	 * @see javax.servlet.ServletRequest#getRemotePort()
	 */
	public int getRemotePort() {
	
		return RemotePort;
	}

	/* (non-Javadoc)
	 * Returns the host name of the Internet Protocol (IP) interface on which the request was received. 
	 * @see javax.servlet.ServletRequest#getLocalName()
	 */
	public String getLocalName() {
	
		return LocalName;
	}

	/* (non-Javadoc)
	 * Returns the Internet Protocol (IP) address of the interface on which the request was received. 
	 * @see javax.servlet.ServletRequest#getLocalAddr()
	 */
	public String getLocalAddr() {
		
		return LocalAddr;
	}

	/* (non-Javadoc)
	 * Returns the Internet Protocol (IP) port number of the interface on which the request was received. 
	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	public int getLocalPort() {
	
		return LocalPort;
	}

	/* (non-Javadoc)
	 * Sets the method GET, POST, HEAD
	 * 	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	void setMethod(String method) {
		m_method = method;
	}
	//TO SET PARAMETRS
	void setParameter(String key, String value) {
	
		/*
		System.out.println("SETTTTING:  "+key+"    "+value);
		if(m_params.get(key)==null)
		{
			ArrayList<String> param=new ArrayList<String>();
			param.add(value);
			m_params.put(key, param);
		}
		else
		{
			ArrayList<String> temp=(ArrayList<String>) m_params.get(key);
			temp.add(value);
			m_params.put(key, temp);		
		}
		*/
		
	//	System.out.println("SETTTTING:  "+key+"    "+value);
		if(parameters.get(key)==null)
		{
			ArrayList<String> param=new ArrayList<String>();
			param.add(value);
			parameters.put(key, param);
		}
		else
		{
			ArrayList<String> temp=parameters.get(key);
			temp.add(value);
			parameters.put(key, temp);		
		}
		
	}
	//TO CLEAR PARAMETRS
	void clearParameters() {
		//m_params.clear();	
		parameters.clear();
	}
	
	//TELL WHETHERE SESSION EXISTS
	boolean hasSession() {
		return ((m_session != null) && m_session.isValid());
	}
	
	//INSERTS SESSION INSDIE THE PUT SESSION OBJ
	void PutSession(ServletsSession arg0,boolean status)
	{
		if(status)
		{
		   sessionStatus=true;
		}
		m_session=arg0;
		
	}
	
	boolean sessionInvalidate()
	{
		
	  if(m_session!=null && m_session.isInvalidated())
	  {
		  return true;
	  }
	  else
	  {
		return false;  
	  }
	}
		
	private Properties m_params = new Properties();
	private Hashtable<String,ArrayList<String>> parameters=new Hashtable<String, ArrayList<String>>();
	private Properties m_props = new Properties();
	private ServletsSession m_session = null;
	private String m_method;
}
