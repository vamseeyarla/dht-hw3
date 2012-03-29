
package edu.upenn.cis.cis555.webserver;

import javax.servlet.*;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author Nick Taylor
 */
class ServletsConfig implements ServletConfig {
	private String name;
	 private static Logger logger = Logger.getLogger(ServletsConfig.class);
	private ServletsContext context;
	private HashMap<String,String> initParams;
	
	public ServletsConfig(String name, ServletsContext context) {
		this.name = name;
		this.context = context;
		initParams = new HashMap<String,String>();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletConfig#getInitParameter(java.lang.String)
	 * Returns a String containing the value of the named initialization parameter, or null if the parameter does not exist.
	 */
	public String getInitParameter(String name) {
		return initParams.get(name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletConfig#getInitParameterNames()
	 * Returns the names of the servlet's initialization parameters as an Enumeration of String objects, or an empty Enumeration if the servlet has no initialization parameters.
	 */
	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
	/*
	 * Returns a reference to the ServletContext in which the caller is executing.
	 * (non-Javadoc)
	 * @see javax.servlet.ServletConfig#getServletContext()
	 */
	public ServletContext getServletContext() {
		return context;
	}
	
	/*
	

 * Returns the name of this servlet instance. The name may be provided via server administration, assigned in the web application deployment descriptor, or for an unregistered (and thus unnamed) servlet instance it will be the servlet's class name.
	 * (non-Javadoc)
	 * @see javax.servlet.ServletConfig#getServletName()
	 */
	public String getServletName() {
		return name;
	}

	/*
	 * Returns a String containing the value of the named initialization parameter, or null if the parameter does not exist.
	 */
	void setInitParam(String name, String value) {
		initParams.put(name, value);
	}


}
