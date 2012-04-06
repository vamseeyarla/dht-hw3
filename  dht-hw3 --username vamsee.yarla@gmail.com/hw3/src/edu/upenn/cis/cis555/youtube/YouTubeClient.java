/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaGroup;

import rice.p2p.commonapi.Id;

/**
 * @author VamseeKYarlagadda
 *
 */
public class YouTubeClient {
String BDPath;
P2PCache nodeMainClass;
Socket req;
public DB db;


	public YouTubeClient(String BDPath)
	{
		this.BDPath=BDPath;
		db=DB.getInstance(BDPath);	
	}
	
	public String searchVideos(String keyword)
	{
		if(db==null)
		{
			System.out.println("Problem with Berkeley DB");
			return "Problem with BerkeleyDB";
		}
		else if(db.checkSearchKeyExists(keyword))
		{
    		System.err.println("Query for "+keyword+" resulted in a cache HIT");
			
			SearchData data=db.retrieveData(keyword);
			return data.Data;
		}
    	else
    	{
    		System.err.println("Query for "+keyword+" resulted in a cache MISS");
			
    		String content="vamsee";
    		StringBuffer temp=new StringBuffer();
    		try {
				YouTubeQuery query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
				
				//System.out.println(keyword);
				query.setFullTextQuery(keyword);
				
				YouTubeService service=new YouTubeService("YouTube Grab");
				VideoFeed feed=service.query(query, VideoFeed.class);
				temp.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
				temp.append("<videoCollection>");
				for(VideoEntry videoEntry : feed.getEntries() ) {
					//System.out.println("\n");
					temp.append("<videos>\n");
					 YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
					 
					 temp.append("<uploader>\n");
					 temp.append(mediaGroup.getUploader().replaceAll("&", "&amp;"));
					 temp.append("</uploader>\n");
					 
					 temp.append("<duration>\n");
					 temp.append(mediaGroup.getDuration());
					 temp.append("</duration>\n");
					
					 temp.append("<description>\n");
					 temp.append(mediaGroup.getDescription().getPlainTextContent().replaceAll("&", "&amp;"));
					 temp.append("</description>\n");
					 
					 temp.append("<link>\n");
					// temp.append(mediaGroup.getPlayer().getUrl());
					 // data='"+mediaGroup.getPlayer().getUrl()+"'
					 temp.append(mediaGroup.getPlayer().getUrl().substring(0,mediaGroup.getPlayer().getUrl().indexOf("&feature=youtube_gdata")).replaceAll("&", "&amp;"));
					 temp.append("</link>\n"); 
					 
					 temp.append("</videos>\n");
				}
				temp.append("</videoCollection>\n");
				content=temp.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				content=null;
			}
    		//FETCH DATA FROM YOUTUBE API
    		
    		/*
    		try {
			URLConnection connection=new URL("http://gdata.youtube.com/feeds/api/videos?v=1&q="+keyword).openConnection();
			BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer buffer=new StringBuffer();
			String temp;
				
			while((temp=br.readLine())!=null)
			{
				
				buffer.append((temp));
			}	
			content=buffer.toString();
    		//System.out.println("CONTENT:   "+content);
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				content=null;
			} 
    		//content=content.replaceFirst("encoding='UTF-8'", "encoding='iso-8859-1'");
    		
    		
    		return content;	
    	*/
			db.addSearchData(keyword, content);
			return content;
    	}
		
		
		/*
		System.out.println("Reached Here!");
		System.out.println(db);
		if(db==null)
		{
			System.out.println("Problem with Berkeley DB");
			return "Problem with Berkeley DB";
		}
		if(db.checkSearchKeyExists(keyword))
		{
			System.err.println("Query for "+keyword+" resulted in a cache HIT (Starting Node)");
			
			SearchData data=db.retrieveData(keyword);
			return data.Data;
		}
		else
		{
			System.err.println("Query for "+keyword+" resulted in a cache MISS (Starting Node)");
			
		
		
				
		return null;
		}
		
		
	
	
	*/
	}
	
}
