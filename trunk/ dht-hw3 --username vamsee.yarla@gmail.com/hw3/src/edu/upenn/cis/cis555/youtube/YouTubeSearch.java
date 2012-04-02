/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import java.io.IOException;
import java.io.PrintWriter;

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
		out.println("<table>" +
				     "<tr>");
		out.println("<td>");
		out.println("<img src=\"http://cache.ohinternet.com/images/e/ef/Youtube_logo.png\" alt=\"YouTube\"/>");
		out.println("</td>");
		out.println("</tr><tr>");
		out.println("<td></td>");
		out.println("<td><input type=\"text\" name=\"keyword\" size=\"60\"></td>");
	
		
		out.println("</tr><tr>");
		out.println("<td><input type=\"Submit\" name=\"Submit\" ></td>");
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
		
	}
}
