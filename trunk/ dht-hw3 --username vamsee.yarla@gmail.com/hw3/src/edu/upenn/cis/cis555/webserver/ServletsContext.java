package edu.upenn.cis.cis555.webserver;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author Nick Taylor
 */
class ServletsContext implements ServletContext {
	private HashMap<String,Object> attributes;
	private HashMap<String,String> initParams;
	private HashMap<String,HttpServlet> servlets;
	 private static Logger logger = Logger.getLogger(ServletsContext.class);
	private String displayName;
	
	
	//DEFAULT CONSTRUCTOR TO CREATE ALL HASH TABLES
	public ServletsContext() {
		attributes = new HashMap<String,Object>();
		initParams = new HashMap<String,String>();
	}
	
	//TO STAORE ALL THE SERVLETS IN HASH TBALES WITH CORRESPONDING NAMES
	public void StoreServlets(HashMap<String,HttpServlet> arg0)
	{
		servlets=arg0;
	}
	
	//FN: TO HANDLE SERVER NAME
	public void servername(String arg0)
	{
		displayName=arg0;
	}
	
	/*
    Returns the servlet container attribute with the given name, or null if there is no attribute by that name.

    An attribute allows a servlet container to give the servlet additional information not already provided by this interface. See your server documentation for information about its attributes. A list of supported attributes can be retrieved using getAttributeNames.

    The attribute is returned as a java.lang.Object or some subclass.

    Attribute names should follow the same convention as package names. The Java Servlet API specification reserves names matching java.*, javax.*, and sun.*.
   */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	/*
   Returns an Enumeration containing the attribute names available within this ServletContext.  */
	public Enumeration getAttributeNames() {
		Set<String> keys = attributes.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
   /* Returns a ServletContext object that corresponds to a specified URL on the server.

    This method allows servlets to gain access to the context for various parts of the server, and as needed obtain RequestDispatcher objects from the context. The given path must be begin with /, is interpreted relative to the server's document root and is matched against the context roots of other web applications hosted on this container.

    In a security conscious environment, the servlet container may return null for a given URL.
*/

	public ServletContext getContext(String name) {
		return this;
	}
	
	/*
    Returns a String containing the value of the named context-wide initialization parameter, or null if the parameter does not exist.

    This method can make available configuration information useful to an entire web application. For example, it can provide a webmaster's email address or the name of a system that holds critical data.
*/

	public String getInitParameter(String name) {
		return initParams.get(name);
	}
	
	/*
    Returns the names of the context's initialization parameters as an Enumeration of String objects, or an empty Enumeration if the context has no initialization parameters.
*/

	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	/*
	Returns the major version of the Servlet API that this servlet container supports. All implementations that comply with Version 3.0 must have this method return the integer 3. 
	*/
public int getMajorVersion() {
		return 2;
	}
/*	
Returns the MIME type of the specified file, or null if the MIME type is not known. The MIME type is determined by the configuration of the servlet container, and may be specified in a web application deployment descriptor. Common MIME types include text/html and image/gif. 
	*/
	public String getMimeType(String file) {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * Gets the minor version of the Servlet specification that the application represented by this ServletContext is based on.

The value returned may be different from getMinorVersion(), which returns the minor version of the Servlet specification supported by the Servlet container. 
	 * @see javax.servlet.ServletContext#getMinorVersion()
	 */
	public int getMinorVersion() {
		return 4;
	}
	
	/*
	 * (non-Javadoc)
	 Returns a RequestDispatcher object that acts as a wrapper for the named servlet.

Servlets (and JSP pages also) may be given names via server administration or via a web application deployment descriptor. A servlet instance can determine its name using ServletConfig#getServletName.

This method returns null if the ServletContext cannot return a RequestDispatcher for any reason.  * @see javax.servlet.ServletContext#getMinorVersion()
	 */
	public RequestDispatcher getNamedDispatcher(String name) {
		return null;
	}
	
	/*
	Gets the real path corresponding to the given virtual path.

	For example, if path is equal to /index.html, this method will return the absolute file path on the server's filesystem to which a request of the form http://<host>:<port>/<contextPath>/index.html would be mapped, where <contextPath> corresponds to the context path of this ServletContext.

	The real path returned will be in a form appropriate to the computer and operating system on which the servlet container is running, including the proper path separators.

	Resources inside the /META-INF/resources directories of JAR files bundled in the application's /WEB-INF/lib directory must be considered only if the container has unpacked them from their containing JAR file, in which case the path to the unpacked location must be returned.

	This method returns null if the servlet container is unable to translate the given virtual path to a real path. 
	*/
	public String getRealPath(String path) {
		
		String xpath="http://localhost:".concat(HttpServer.sport);
		if(xpath.charAt(0)!='/')
		{
			return xpath.concat("/").concat(path);
		}
		else
		{
			return xpath.concat(path);
		}
	}
	
	/*
	 * (non-Javadoc)
	 *     Returns a RequestDispatcher object that acts as a wrapper for the resource located at the given path. A RequestDispatcher object can be used to forward a request to the resource or to include the resource in a response. The resource can be dynamic or static.

    The pathname must begin with a / and is interpreted as relative to the current context root. Use getContext to obtain a RequestDispatcher for resources in foreign contexts.

    This method returns null if the ServletContext cannot return a RequestDispatcher.


	 * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String name) {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 *     Returns a URL to the resource that is mapped to the given path.

    The path must begin with a / and is interpreted as relative to the current context root, or relative to the /META-INF/resources directory of a JAR file inside the web application's /WEB-INF/lib directory. This method will first search the document root of the web application for the requested resource, before searching any of the JAR files inside /WEB-INF/lib. The order in which the JAR files inside /WEB-INF/lib are searched is undefined.

    This method allows the servlet container to make a resource available to servlets from any source. Resources can be located on a local or remote file system, in a database, or in a .war file.

    The servlet container must implement the URL handlers and URLConnection objects that are necessary to access the resource.

    This method returns null if no resource is mapped to the pathname.

    Some containers may allow writing to the URL returned by this method using the methods of the URL class.

    The resource content is returned directly, so be aware that requesting a .jsp page returns the JSP source code. Use a RequestDispatcher instead to include results of an execution.

    This method has a different purpose than java.lang.Class.getResource, which looks up resources based on a class loader. This method does not use class loaders.


	 * @see javax.servlet.ServletContext#getResource(java.lang.String)
	 */
	public java.net.URL getResource(String path) {
		return null;
	}
	/*
	 * (non-Javadoc)
	 * Returns the resource located at the named path as an InputStream object.

The data in the InputStream can be of any type or length. The path must be specified according to the rules given in getResource. This method returns null if no resource exists at the specified path.

Meta-information such as content length and content type that is available via getResource method is lost when using this method.

The servlet container must implement the URL handlers and URLConnection objects necessary to access the resource.

This method is different from java.lang.Class.getResourceAsStream, which uses a class loader. This method allows servlet containers to make a resource available to a servlet from any location, without using a class loader. 
	 * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
	 */
	public java.io.InputStream getResourceAsStream(String path) {
		return null;
	}
	/*
	 * (non-Javadoc)
	 * Returns a directory-like listing of all the paths to resources within the web application whose longest sub-path matches the supplied path argument.

Paths indicating subdirectory paths end with a /.

The returned paths are all relative to the root of the web application, or relative to the /META-INF/resources directory of a JAR file inside the web application's /WEB-INF/lib directory, and have a leading /.

For example, for a web application containing:

   /welcome.html
   /catalog/index.html
   /catalog/products.html
   /catalog/offers/books.html
   /catalog/offers/music.html
   /customer/login.jsp
   /WEB-INF/web.xml
   /WEB-INF/classes/com.example.OrderServlet.class
   /WEB-INF/lib/catalog.jar!/META-INF/resources/catalog/moreOffers/books.html
 

getResourcePaths("/") would return {"/welcome.html", "/catalog/", "/customer/", "/WEB-INF/"}, and getResourcePaths("/catalog/") would return {"/catalog/index.html", "/catalog/products.html", "/catalog/offers/", "/catalog/moreOffers/"}. 
	 * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
	 */
	public java.util.Set getResourcePaths(String path) {
		return null;
	}
	/*
	 * (non-Javadoc)
	 *     Returns the name and version of the servlet container on which the servlet is running.

    The form of the returned string is servername/versionnumber. For example, the JavaServer Web Development Kit may return the string JavaServer Web Dev Kit/1.0.

    The servlet container may return other optional information after the primary string in parentheses, for example, JavaServer Web Dev Kit/1.0 (JDK 1.1.6; Windows NT 4.0 x86).


	 * @see javax.servlet.ServletContext#getServerInfo()
	 */
	public String getServerInfo() {
		return "HTTPServer";
	}
	/*
	 * This method was originally defined to retrieve a servlet from a ServletContext. In this version, this method always returns null and remains only to preserve binary compatibility. This method will be permanently removed in a future version of the Java Servlet API.


	 * (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServlet(java.lang.String)
	 */
	public Servlet getServlet(String name) {
		return servlets.get(name);
	}
	/*
	 *     Returns the name of this web application corresponding to this ServletContext as specified in the deployment descriptor for this web application by the display-name element.


	 * (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServletContextName()
	 */
	public String getServletContextName() {
		
		return "";
	}
	/*
	 * (non-Javadoc)
	 * his method was originally defined to return an Enumeration of all the servlet names known to this context. In this version, this method always returns an empty Enumeration and remains only to preserve binary compatibility. This method will be permanently removed in a future version of the Java Servlet API.
	 * @see javax.servlet.ServletContext#getServletNames()
	 */
	public Enumeration getServletNames() {
		
	
		Vector<String> e=new Vector<String>();
		for (String servletName : servlets.keySet())
		{
			e.add(servletName);
		}
	    return e.elements();
		
	}
	/*
	 * (non-Javadoc)
	 *     This method was originally defined to return an Enumeration of all the servlets known to this servlet context. In this version, this method always returns an empty enumeration and remains only to preserve binary compatibility. This method will be permanently removed in a future version of the Java Servlet API.


	 * @see javax.servlet.ServletContext#getServlets()
	 */
	public Enumeration getServlets() {
		
		Vector<HttpServlet> e=new Vector<HttpServlet>();
		for (String servletName : servlets.keySet())
		{
			e.add(servlets.get(servletName));
		}
	    return e.elements();
	}
	/*
	 * (non-Javadoc)
	 *     Writes the specified message to a servlet log file, usually an event log. The name and type of the servlet log file is specific to the servlet container.


	 * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
	 */
	public void log(Exception exception, String msg) {
		log(msg, (Throwable) exception);
	}
	
	/*
	 * (non-Javadoc)
	 * This method was originally defined to write an exception's stack trace and an explanatory error message to the servlet log file.
	 * @see javax.servlet.ServletContext#log(java.lang.String)
	 */
	public void log(String msg) {
		System.err.println(msg);
	}
	
	/*
	 * (non-Javadoc)
	 *     Writes an explanatory message and a stack trace for a given Throwable exception to the servlet log file. The name and type of the servlet log file is specific to the servlet container, usually an event log.


	 * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
	 */
	public void log(String message, Throwable throwable) {
		System.err.println(message);
		throwable.printStackTrace(System.err);
	}
	
	/*
	 * (non-Javadoc)
	 *     Removes the attribute with the given name from this ServletContext. After removal, subsequent calls to getAttribute(java.lang.String) to retrieve the attribute's value will return null.

    If listeners are configured on the ServletContext the container notifies them accordingly.


	 * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		attributes.remove(name);
	}
	
	/*
	 * (non-Javadoc)
	 *  Binds an object to a given attribute name in this ServletContext.
	 * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}
	
	/*
	 * Function to set all the initial parameters.
	 */
	void setInitParam(String name, String value) {
		initParams.put(name, value);
	}
}
