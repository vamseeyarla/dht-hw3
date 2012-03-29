package edu.upenn.cis.cis555.webserver;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * @author Vamsee K. Yarlagadda
 *
 *  To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServletsResponse implements HttpServletResponse {

	Hashtable<String,String> header=new Hashtable<String,String>(); 
	Locale locale=Locale.US;
	int statusCode=-1;
	int contentLength=0;
	boolean committed=false;
	 private static Logger logger = Logger.getLogger(ServletsResponse.class);
	ByteArrayOutputStream outStream=new ByteArrayOutputStream();
	HttpSession session;
	String FirstHead=null;
	
	/* (non-Javadoc)
	 *     Adds the specified cookie to the response. This method can be called multiple times to set more than one cookie.


	 * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
	 */
	public void addCookie(Cookie arg0) {
	//	System.out.println("VAM");
	//	System.out.println(arg0.getName());
	//	System.out.println("GRI");
	//	System.out.println(arg0.getValue());
		String date=null;
		if(arg0.getMaxAge()!=-1)
		{
		 Date headDate =new Date(Long.parseLong(String.valueOf(arg0.getMaxAge()))+System.currentTimeMillis());
         DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
 	   	 date=headformatter.format(headDate).concat(" GMT");
	//	 System.out.println(date);
		}
		 if(!committed){
		 if(header.get("Set-Cookie".toLowerCase())!=null)
		 {
			 if(arg0.getMaxAge()!=-1)
			 {
		 header.put("Set-Cookie".toLowerCase(),header.get("Set-Cookie".toLowerCase())+"; "+arg0.getName()+"="+arg0.getValue()+"; Expires="+date);
			 }
			 else
			 {
				 header.put("Set-Cookie".toLowerCase(),header.get("Set-Cookie".toLowerCase())+"; "+arg0.getName()+"="+arg0.getValue()+";");
					
			 }
			 }
		 else
		 {
			 if(arg0.getMaxAge()!=-1)
			 {
			 header.put("Set-Cookie".toLowerCase(),arg0.getName()+"="+arg0.getValue()+"; Expires="+date);
			 }
			 else
			 {
				 header.put("Set-Cookie".toLowerCase(),arg0.getName()+"="+arg0.getValue()+";");
					
			 }
			 }
		 }
		
	}

	/* (non-Javadoc)
	 * Returns a boolean indicating whether the named response header has already been set. 
	 * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
	 */
	public boolean containsHeader(String arg0) {
		
		if(header.get(arg0.toLowerCase())!=null)
		{
			return true;
		}
		
		else
		{
			return false;
		}
		
	}

	/* (non-Javadoc)
	 * Encodes the specified URL by including the session ID in it, or, if encoding is not needed, returns the URL unchanged. The implementation of this method includes the logic to determine whether the session ID needs to be encoded in the URL. For example, if the browser supports cookies, or session tracking is turned off, URL encoding is unnecessary.

For robust session tracking, all URLs emitted by a servlet should be run through this method. Otherwise, URL rewriting cannot be used with browsers which do not support cookies. 
	 * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
	 */
	public String encodeURL(String arg0) {
		
		return arg0;
	}

	/* (non-Javadoc)
	 *     Encodes the specified URL for use in the sendRedirect method or, if encoding is not needed, returns the URL unchanged. The implementation of this method includes the logic to determine whether the session ID needs to be encoded in the URL. Because the rules for making this determination can differ from those used to decide whether to encode a normal link, this method is separated from the encodeURL method.

    All URLs sent to the HttpServletResponse.sendRedirect method should be run through this method. Otherwise, URL rewriting cannot be used with browsers which do not support cookies.

    
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
	 */
	public String encodeRedirectURL(String arg0) {
		try {
			return URLEncoder.encode(arg0,"application/x-www-form-urlencoded");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
			return arg0;
		}
	}

	/* (non-Javadoc)
	 * Parameters:
    url - the url to be encoded. 
	 * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
	 */
	public String encodeUrl(String arg0) {
		return arg0;
	}

	/* (non-Javadoc)
	 *     Parameters:
        url - the url to be encoded. 
    Returns:
        the encoded URL if encoding is needed; the unchanged URL otherwise.


	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
	 */
	public String encodeRedirectUrl(String arg0) {
		
		return arg0;
	}

	/* (non-Javadoc)
	 *     Sends an error response to the client using the specified status. The server defaults to creating the response to look like an HTML-formatted server error page containing the specified message, setting the content type to "text/html", leaving cookies and other headers unmodified. If an error-page declaration has been made for the web application corresponding to the status code passed in, it will be served back in preference to the suggested msg parameter.

    If the response has already been committed, this method throws an IllegalStateException. After using this method, the response should be considered to be committed and should not be written to.

    
	 * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
	 */
	public void sendError(int arg0, String arg1) throws IOException {
		FirstHead = String.valueOf(arg0)+" "+arg1;
		resetBuffer();
		committed=true;

	}

	/* (non-Javadoc)
	 *     Sends an error response to the client using the specified status code and clearing the buffer.

    If the response has already been committed, this method throws an IllegalStateException. After using this method, the response should be considered to be committed and should not be written to.

   
	 * @see javax.servlet.http.HttpServletResponse#sendError(int)
	 */
	public void sendError(int arg0) throws IOException {
		FirstHead = String.valueOf(arg0)+" DONE";
		resetBuffer();
		committed=true;
		
	}

	/* (non-Javadoc)
	 *     Sends a temporary redirect response to the client using the specified redirect location URL. This method can accept relative URLs; the servlet container must convert the relative URL to an absolute URL before sending the response to the client. If the location is relative without a leading '/' the container interprets it as relative to the current request URI. If the location is relative with a leading '/' the container interprets it as relative to the servlet container root.

    If the response has already been committed, this method throws an IllegalStateException. After using this method, the response should be considered to be committed and should not be written to.


	 * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
	 */
	public void sendRedirect(String arg0) throws IOException {
		System.out.println("[DEBUG] redirect to " + arg0 + " requested");
		System.out.println("[DEBUG] stack trace: ");
		Exception e = new Exception();
		StackTraceElement[] frames = e.getStackTrace();
		for (int i = 0; i < frames.length; i++) {
			System.out.print("[DEBUG]   ");
			System.out.println(frames[i].toString());
		}
	}

	/* (non-Javadoc)
	 * \    Sets a response header with the given name and date-value. The date is specified in terms of milliseconds since the epoch. If the header had already been set, the new value overwrites the previous one. The containsHeader method can be used to test for the presence of a header before setting its value.


	 * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
	 */
	public void setDateHeader(String arg0, long arg1) {
		
		 Date headDate =new Date(arg1);
         DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
 	   	 String date=headformatter.format(headDate).concat(" GMT");
 	   	 if(!committed){
		 if(containsHeader(arg0))
		 {
			header.put(arg0.toLowerCase(), date);
		 }
		 else
		 {
			header.put(arg0.toLowerCase(), date);
		 }
 	   	 }

	}

	/* (non-Javadoc)
	 *     Adds a response header with the given name and date-value. The date is specified in terms of milliseconds since the epoch. This method allows response headers to have multiple values.


	 * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
	 */
	public void addDateHeader(String arg0, long arg1) {
		
		 Date headDate =new Date(arg1);
         DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
 	   	 String date=headformatter.format(headDate).concat(" GMT");
 	    if(!committed){
 	   	 header.put(arg0.toLowerCase(), date);
 	    }
	}

	/* (non-Javadoc)
	 *     Sets a response header with the given name and value. If the header had already been set, the new value overwrites the previous one. The containsHeader method can be used to test for the presence of a header before setting its value.


	 * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
	 */
	public void setHeader(String arg0, String arg1) {
		 if(!committed){
		if(header.contains(arg0.toLowerCase()))
		{
			header.put(arg0.toLowerCase(), (arg1));
		}
		else
		{
			header.put(arg0.toLowerCase(), (arg1));
		}
		 }
	}

	/* (non-Javadoc)
	 * Adds a response header with the given name and value. This method allows response headers to have multiple values. 
	 * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String arg0, String arg1) {
		 if(!committed){
		header.put(arg0.toLowerCase(), arg1);
		 }

	}

	/* (non-Javadoc)
	 * Sets a response header with the given name and integer value. If the header had already been set, the new value overwrites the previous one. The containsHeader method can be used to test for the presence of a header before setting its value. 
	 * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
	 */
	public void setIntHeader(String arg0, int arg1) {
		 if(!committed){
		if(header.contains(arg0.toLowerCase()))
		{
			header.put(arg0.toLowerCase(), String.valueOf(arg1));
		}
		else
		{
			header.put(arg0.toLowerCase(), String.valueOf(arg1));
		}
		 }

	}

	/* (non-Javadoc)
	 * Adds a response header with the given name and integer value. This method allows response headers to have multiple values. 
	 * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
	 */
	public void addIntHeader(String arg0, int arg1) {
		 if(!committed){
		header.put(arg0.toLowerCase(), String.valueOf(arg1));
		 }

	}

	/* (non-Javadoc)
	 *     Sets the status code for this response. This method is used to set the return status code when there is no error (for example, for the status codes SC_OK or SC_MOVED_TEMPORARILY). If there is an error, and the caller wishes to invoke an error-page defined in the web application, the sendError method should be used instead.

    The container clears the buffer and sets the Location header, preserving cookies and other headers.


	 * @see javax.servlet.http.HttpServletResponse#setStatus(int)
	 */
	public void setStatus(int arg0) {
		
		statusCode=arg0;

	}

	/* (non-Javadoc)
	 *     Parameters:
        sc - the status code
        sm - the status message


	 * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
	 */
	public void setStatus(int arg0, String arg1) {
		

	}

	/* (non-Javadoc)
	Returns the name of the character encoding (MIME charset) used for the body sent in this response. The character encoding may have been specified explicitly using the setCharacterEncoding(java.lang.String) or setContentType(java.lang.String) methods, or implicitly using the setLocale(java.util.Locale) method. Explicit specifications take precedence over implicit specifications. Calls made to these methods after getWriter has been called or after the response has been committed have no effect on the character encoding. If no character encoding has been specified, ISO-8859-1 is returned.



	 * @see javax.servlet.ServletResponse#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		if(header.get("Content-Encoding".toString().toLowerCase())!=null)
				{
		return header.get("Content-Encoding".toString().toLowerCase());
				}
		else
		{
			return "ISO-8859-1";
				
		}
	}

	/* (non-Javadoc)
	 *     Returns the content type used for the MIME body sent in this response. The content type proper must have been specified using setContentType(java.lang.String) before the response is committed. If no content type has been specified, this method returns null. If a content type has been specified, and a character encoding has been explicitly or implicitly specified as described in getCharacterEncoding() or getWriter() has been called, the charset parameter is included in the string returned. If no character encoding has been specified, the charset parameter is omitted.


	 * @see javax.servlet.ServletResponse#getContentType()
	 */
	public String getContentType() {
		
		if(header.get("Content-Type".toString().toLowerCase())!=null)
		{
   return header.get("Content-Type".toString().toLowerCase());
		}
      else
      {
	return "text/html";
		
    }
	}

	/* (non-Javadoc)
	 *     Returns a ServletOutputStream suitable for writing binary data in the response. The servlet container does not encode the binary data.

    Calling flush() on the ServletOutputStream commits the response. Either this method or getWriter() may be called to write the body, not both.


	 * @see javax.servlet.ServletResponse#getOutputStream()
	 */
	public ServletOutputStream getOutputStream() throws IOException {
		
		return null;
	}

	/* (non-Javadoc)
	 *     Returns a PrintWriter object that can send character text to the client. The PrintWriter uses the character encoding returned by getCharacterEncoding(). If the response's character encoding has not been specified as described in getCharacterEncoding (i.e., the method just returns the default value ISO-8859-1), getWriter updates it to ISO-8859-1.

    Calling flush() on the PrintWriter commits the response.

    Either this method or getOutputStream() may be called to write the body, not both.


	 * @see javax.servlet.ServletResponse#getWriter()
	 */
	public PrintWriter getWriter() throws IOException {
		
			
		return new PrintWriter(outStream,true);
		
	}

	/* (non-Javadoc)
	 *     Sets the character encoding (MIME charset) of the response being sent to the client, for example, to UTF-8. If the character encoding has already been set by setContentType(java.lang.String) or setLocale(java.util.Locale), this method overrides it. Calling setContentType(java.lang.String) with the String of text/html and calling this method with the String of UTF-8 is equivalent with calling setContentType with the String of text/html; charset=UTF-8.

    This method can be called repeatedly to change the character encoding. This method has no effect if it is called after getWriter has been called or after the response has been committed.

    Containers must communicate the character encoding used for the servlet response's writer to the client if the protocol provides a way for doing so. In the case of HTTP, the character encoding is communicated as part of the Content-Type header for text media types. Note that the character encoding cannot be communicated via HTTP headers if the servlet does not specify a content type; however, it is still used to encode text written via the servlet response's writer.


	 * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String arg0) {
		 if(!committed){
		if(header.get("Content-Type".toString().toLowerCase())!=null)
		{
			header.put("Content-Type".toString().toLowerCase(), arg0);
		}
		else
		{
			header.put("Content-Type".toString().toLowerCase(), arg0);
		}
		 }

	}

	/* (non-Javadoc)
	 * Sets the length of the content body in the response In HTTP servlets, this method sets the HTTP Content-Length header. 
	 * @see javax.servlet.ServletResponse#setContentLength(int)
	 */
	public void setContentLength(int arg0) {
		
		contentLength=arg0;
		 if(!committed){
		header.put("Content-Length".toLowerCase(), String.valueOf(arg0));
		 }

	}

	/* (non-Javadoc)
	 * Sets the content type of the response being sent to the client, if the response has not been committed yet. The given content type may include a character encoding specification, for example, text/html;charset=UTF-8. The response's character encoding is only set from the given content type if this method is called before getWriter is called.

This method may be called repeatedly to change content type and character encoding. This method has no effect if called after the response has been committed. It does not set the response's character encoding if it is called after getWriter has been called or after the response has been committed.

Containers must communicate the content type and the character encoding used for the servlet response's writer to the client if the protocol provides a way for doing so. In the case of HTTP, the Content-Type header is used. 
	 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	public void setContentType(String arg0) {
		 if(!committed){
		if(header.get("Content-Type".toString().toLowerCase())!=null)
		{
			header.put("Content-Type".toString().toLowerCase(), arg0);
		}
		else
		{
			header.put("Content-Type".toString().toLowerCase(), arg0);
		}
		 }

	}

	/* (non-Javadoc)
	 *     Sets the preferred buffer size for the body of the response. The servlet container will use a buffer at least as large as the size requested. The actual buffer size used can be found using getBufferSize.

    A larger buffer allows more content to be written before anything is actually sent, thus providing the servlet with more time to set appropriate status codes and headers. A smaller buffer decreases server memory load and allows the client to start receiving data more quickly.

    This method must be called before any response body content is written; if content has been written or the response object has been committed, this method throws an IllegalStateException.


	 * @see javax.servlet.ServletResponse#setBufferSize(int)
	 */
	public void setBufferSize(int arg0) {
		outStream=new ByteArrayOutputStream(arg0);
		
    
	}

	/* (non-Javadoc)
	 *     Returns the actual buffer size used for the response. If no buffering is used, this method returns 0.


	 * @see javax.servlet.ServletResponse#getBufferSize()
	 */
	public int getBufferSize() {
		return outStream.size();
	}

	/* (non-Javadoc)
	 *     Forces any content in the buffer to be written to the client. A call to this method automatically commits the response, meaning the status code and headers will be written.


	 * @see javax.servlet.ServletResponse#flushBuffer()
	 */
	public void flushBuffer() throws IOException {
		
		committed=true;

	}

	/* (non-Javadoc)
	 *     Clears the content of the underlying buffer in the response without clearing headers or status code. If the response has been committed, this method throws an IllegalStateException.


	 * @see javax.servlet.ServletResponse#resetBuffer()
	 */
	public void resetBuffer() {
		outStream=new ByteArrayOutputStream();
		}

	/* (non-Javadoc)
	 *     Returns a boolean indicating if the response has been committed. A committed response has already had its status code and headers written.


	 * @see javax.servlet.ServletResponse#isCommitted()
	 */
	public boolean isCommitted() {
		if(committed)
		{
			return true;
		}
		else
		{
		return false;
		}
	}

	/* (non-Javadoc)
	 *     Clears any data that exists in the buffer as well as the status code and headers. If the response has been committed, this method throws an IllegalStateException.


	 * @see javax.servlet.ServletResponse#reset()
	 */
	public void reset() {
		outStream=new ByteArrayOutputStream();
		header.clear();

	}

	/* (non-Javadoc)
	 *     Sets the locale of the response, if the response has not been committed yet. It also sets the response's character encoding appropriately for the locale, if the character encoding has not been explicitly set using setContentType(java.lang.String) or setCharacterEncoding(java.lang.String), getWriter hasn't been called yet, and the response hasn't been committed yet. If the deployment descriptor contains a locale-encoding-mapping-list element, and that element provides a mapping for the given locale, that mapping is used. Otherwise, the mapping from locale to character encoding is container dependent.

    This method may be called repeatedly to change locale and character encoding. The method has no effect if called after the response has been committed. It does not set the response's character encoding if it is called after setContentType(java.lang.String) has been called with a charset specification, after setCharacterEncoding(java.lang.String) has been called, after getWriter has been called, or after the response has been committed.

    Containers must communicate the locale and the character encoding used for the servlet response's writer to the client if the protocol provides a way for doing so. In the case of HTTP, the locale is communicated via the Content-Language header, the character encoding as part of the Content-Type header for text media types. Note that the character encoding cannot be communicated via HTTP headers if the servlet does not specify a content type; however, it is still used to encode text written via the servlet response's writer.


	 * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale arg0) {
		
		locale=arg0;

	}

	/* (non-Javadoc)
	 * Returns the locale specified for this response using the setLocale(java.util.Locale) method. Calls made to setLocale after the response is committed have no effect. If no locale has been specified, the container's default locale is returned. 
	 * @see javax.servlet.ServletResponse#getLocale()
	 */
	public Locale getLocale() {
		
		return locale;
	}

}
