/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import rice.p2p.util.XMLParser;

/**
 * @author VamseeKYarlagadda
 *
 */
public class YouTubeSearch extends HttpServlet{

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Vamsee");
		
		PrintWriter out =response.getWriter();
		openingPage(out);
		
		
	}
	
	public void openingPage(PrintWriter out)
	{
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
		
		PrintWriter out=response.getWriter();
		String keyword=request.getParameter("keyword");
		if(keyword.trim().equalsIgnoreCase(""))
		{
			openingPage(out);
		}
		keyword=keyword.trim();
		ServletConfig config = getServletConfig();
		ServletContext context = config.getServletContext();
		String cacheServer=context.getInitParameter("cacheServer");
		String cacheServerPort=context.getInitParameter("cacheServerPort");
		
		long Start=System.currentTimeMillis();
		URLConnection conn=new URL("http://"+cacheServer+":"+cacheServerPort+"/search?keyword="+keyword).openConnection();
		
		
		InputStream ir=(conn.getInputStream());
		//BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
		//System.out.println(br.readLine());
			
		
		out.println("<html>");
		out.println("<head>");
		out.println("<title>");
		out.println("YouTube Search Results");
		out.println("</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<table align=\"center\">");
		out.println("<tr><td>");
		out.println("<img src=\"http://cache.ohinternet.com/images/e/ef/Youtube_logo.png\" alt=\"YouTube\" height=\"110\" width=\"220\" />");
		out.println("</td></tr>");
		
		out.println("<tr><td>");
		out.println("<h1>               </h1>");
		out.println("</td></tr>");
		
		out.println("<tr><td>");
		out.println("<b>Processing time:  "+(System.currentTimeMillis()-Start)+"ms</b>");
		out.println("</td>");
		out.println("<td>");
		out.println("<h3><a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"\"> Back </a></h3>");
		out.println("</td>");
		out.println("</tr>");
		parseXML(ir,out);
		
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");
	}
	
	public void parseXML(InputStream ir,PrintWriter out){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom;
		try {
			System.out.println("HAI SERVER");

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse(ir);

				  NodeList nodes = dom.getElementsByTagName("videos");
				  
				  
			  for (int i = 0; i < nodes.getLength(); i++) {
				  
				 
				  
				  System.out.println(nodes.item(i).getNodeName());
			 
				  Node first_node =(Node) nodes.item(i);
				 
				  if(first_node.getNodeType() == Node.ELEMENT_NODE){
					  out.println("<tr>");
					  out.println("<td>");
					  out.println("<h1>                      </h1>");
					  out.println("</td>");
					  out.println("</tr>");
					  out.println("<tr>");
					  out.println("<td>");
					  
	                    Element firstElement = (Element)first_node;

	                    
	                    //----
	                    NodeList LinkList = firstElement.getElementsByTagName("link");
	                    Element LinkElement = (Element)LinkList.item(0);

	                    NodeList textLinkList = LinkElement.getChildNodes();
	                    System.out.println("Link : " + 
	                           ((Node)textLinkList.item(0)).getNodeValue().trim());
	                    
	                 //   out.println("Link : " + ((Node)textLinkList.item(0)).getNodeValue().trim());
	                    String link=((Node)textLinkList.item(0)).getNodeValue().trim();
	                    link=link.substring(link.indexOf("?")+3);
	                    
	                    out.println("<iframe width=\"420\" height=\"315\" src=\"http://www.youtube.com/embed/"+link+"\" frameborder=\"0\" allowfullscreen></iframe>");
	                    
	                    //------
	                    out.println("</td>"); 
	                    out.println("</tr>");
	                    
	                    out.println("<tr>");
	                    out.println("<td>");
	                    //-------
	                    NodeList firstNameList = firstElement.getElementsByTagName("uploader");
	                    Element firstNameElement = (Element)firstNameList.item(0);

	                    NodeList textFNList = firstNameElement.getChildNodes();
	                    System.out.println("Uploader : " + 
	                           ((Node)textFNList.item(0)).getNodeValue().trim());
	                    out.println("Uploader : " + 
		                           ((Node)textFNList.item(0)).getNodeValue().trim());
	                    out.println("</td>"); 
	                    out.println("</tr>");
	                    
	                    out.println("<tr>");
	                    out.println("<td>");
	                    //-------
	                    NodeList lastNameList = firstElement.getElementsByTagName("duration");
	                    Element lastNameElement = (Element)lastNameList.item(0);

	                    NodeList textLNList = lastNameElement.getChildNodes();
	                    System.out.println("Duration : " + 
	                           ((Node)textLNList.item(0)).getNodeValue().trim());
	                    out.println("Duration : " + 
		                           ((Node)textLNList.item(0)).getNodeValue().trim());
	                    out.println("</td>"); 
	                    out.println("</tr>");
	                    
	                    out.println("<tr>");
	                    out.println("<td>");
	                    
	                    //----
	                    NodeList ageList = firstElement.getElementsByTagName("description");
	                    Element ageElement = (Element)ageList.item(0);

	                    NodeList textAgeList = ageElement.getChildNodes();
	                    System.out.println("Description : " + 
	                           ((Node)textAgeList.item(0)).getNodeValue().trim());
	                    out.println("Description : " + 
	                           ((Node)textAgeList.item(0)).getNodeValue().trim());  
	                    //------
	                    
	                    out.println("</td>"); 
	                    out.println("</tr>");
	                    
	               

	                }//end of if clause
				 
			  }

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		}
	
	
}
