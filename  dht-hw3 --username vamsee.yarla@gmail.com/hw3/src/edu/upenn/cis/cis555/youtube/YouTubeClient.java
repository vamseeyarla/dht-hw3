/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import java.net.Socket;
import java.util.ArrayList;

import rice.p2p.commonapi.Id;

/**
 * @author VamseeKYarlagadda
 *
 */
public class YouTubeClient {
String BDPath;
P2PCache nodeMainClass;
Socket req;
	
	public YouTubeClient(String BDPath)
	{
		this.BDPath=BDPath;
		
	}
	
	public void searchVideos(String keyword)
	{
		if(nodeMainClass.query.containsKey(keyword))
		{
			ArrayList<Socket> reqs=nodeMainClass.query.get(keyword);
			reqs.add(req);
			nodeMainClass.query.put(keyword, reqs);
		}
		else
		{
			ArrayList<Socket> reqs=new ArrayList<Socket>();
			reqs.add(req);
			nodeMainClass.query.put(keyword, reqs);
		}
		Id temp=nodeMainClass.node_factory.getIdFromString(keyword);
		nodeMainClass.sendMessage(temp,null,keyword,true);
		
	}
	
	
}
