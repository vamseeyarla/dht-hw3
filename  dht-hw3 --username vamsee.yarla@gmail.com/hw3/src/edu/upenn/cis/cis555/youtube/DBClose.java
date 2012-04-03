/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityStore;

/**
 * @author VamseeKYarlagadda
 *
 */
/*
 * Class to commit all the database transactions and close all it repositories.
 * 
 */
public class DBClose extends Thread {

	private Environment env;
	private EntityStore search_store;

	
	public DBClose(Environment env, EntityStore search_store)
	{
		this.env=env;
		this.search_store=search_store;

	}
	
	public void run()
	{
		try{
			if(env!=null)
			{
				
				//System.out.println("Stage1");
				search_store.close();
				//System.out.println("Stage2");
			
				//System.out.println("Stage4");
				env.cleanLog();
				//System.out.println("Stage5");
				env.close();
				//System.out.println("Database Closed");
			}
			}
		catch(Exception e)
		{
			//System.out.println("Database not closed properly");
		}
	}
}
