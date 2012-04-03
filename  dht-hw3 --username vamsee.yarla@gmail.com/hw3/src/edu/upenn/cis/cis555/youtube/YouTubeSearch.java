/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author VamseeKYarlagadda
 *
 */
public class YouTubeSearch extends HttpServlet{

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Vamsee");
		
		PrintWriter out =response.getWriter();
		
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>");
		out.println("Youtube Search");
		out.println("</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
		out.println("<form action=\"http://localhost:1234/youtube\" method=\"POST\" >");
		out.println("<table align=\"center\">" +
				     "<tr align=\"center\">");
		out.println("<td>");
		out.println("<img src=\"http://cache.ohinternet.com/images/e/ef/Youtube_logo.png\" alt=\"YouTube\" height=\"220\" width=\"420\" />");
		out.println("</td>");
		out.println("</tr><tr align=\"center\">");
	
		out.println("<td><input type=\"text\" name=\"keyword\" size=\"60\" align=\"center\"></td>");
	
		
		out.println("</tr><tr align=\"center\">");
		out.println("<td><input type=\"Submit\" name=\"Submit\" value=\"Search\" size=\"25\" align=\"center\"></td>");
		out.println("</tr></table>");
		out.println("</br></br>");
		out.println("</br></br>");
		out.println("Created By:-");
		out.println("</br>");
		out.println("Vamsee K Yarlagadda");
		out.println("</br>");
		out.println("PennKey: vamsee>");
		out.println("</form>");
		out.println("</BODY>");
		out.println("</HTML>");
		
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String keyword=request.getParameter("keyword");
		ServletConfig config = getServletConfig();
		ServletContext context = config.getServletContext();
		String cacheServer=context.getInitParameter("cacheServer");
		String cacheServerPort=context.getInitParameter("cacheServerPort");
		
		URLConnection conn=new URL("http://"+cacheServer+":"+cacheServerPort+"/search?keyword="+keyword).openConnection();
		
		
		BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		String temp;
		while((temp=br.readLine())!=null)
		{
			System.out.println(temp);
		}
	}
}
