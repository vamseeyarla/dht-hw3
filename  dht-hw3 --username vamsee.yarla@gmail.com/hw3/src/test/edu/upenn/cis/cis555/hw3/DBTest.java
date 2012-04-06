/**
 * 
 */
package test.edu.upenn.cis.cis555.hw3;


import edu.upenn.cis.cis555.youtube.YouTubeClient;

import junit.framework.TestCase;

/**
 * @author VamseeKYarlagadda
 *
 */
public class DBTest extends TestCase {

	
	public void testCase1()
	{
		YouTubeClient client=new YouTubeClient("JEDB");
		client.searchVideos("youtube");
		
		assertEquals(true, client.db.checkSearchKeyExists("youtube"));
	
	}
	
	public void testCase2()
	{
		YouTubeClient client=new YouTubeClient("JEDB");
		String result=client.searchVideos("youtube");
		System.out.println(result);
		assertEquals(true, (result.indexOf("videoCollection")!=-1));
	
	}
}
