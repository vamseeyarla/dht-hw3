package edu.upenn.cis.cis555.youtube;

import java.io.File;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

public class DB {

	public String Directory;
	EntityStore storeSearch;
	
	PrimaryIndex <String, SearchData> SearchIndex;
	Environment env;
	EnvironmentConfig envConfig;
	StoreConfig storeConfig;
    static DB db=null;
	
    /*
     * Contrcutor to take DB directory as input
     */
	public DB(String Directory)
	{
		this.Directory=Directory;			
	}
	
	 /*
     * Function which takes Directory as input and returns the object of 
     * this class. this ensures only one instance of the 
     * object has been created.
     */
	public static DB getInstance(String Directory)
	{
		if(db==null)
		{
		db=new DB(Directory);
		if(!db.init())
		{
			//System.out.println("FAIL INIT");
			db=null;
		}
		}
		
		return db;	
	}
	
	/*
	 * Initializes the fields required for opening the DB and makes sure
	 * everything is working perfectly or not. If not it returns false
	 */
	public boolean init()
	{
		File dir = new File(Directory);
		
		if(!dir.exists())
		{
		boolean success=dir.mkdirs();
		
		if(success)
		{
			//System.out.println("Created the DB Directory");
				}
		else
		{
			//System.out.println("Cannot Create the DB Directory");
			
		}
		}
		try{
		
		envConfig=new EnvironmentConfig();
		storeConfig=new StoreConfig();
		
		envConfig.setReadOnly(false);
		storeConfig.setReadOnly(false);
		envConfig.setLocking(false);
		//storeConfig.setLocking(false);
		
		envConfig.setAllowCreate(true);
		storeConfig.setAllowCreate(true);
		//storeConfig.setExclusiveCreate(true);
		
		env = new Environment(dir, envConfig);
		
		storeSearch=new EntityStore(env, "SearckKeywords", storeConfig);
	
		SearchIndex=storeSearch.getPrimaryIndex(String.class, SearchData.class);
		
		DBClose closingHook=new DBClose(env, storeSearch);
		Runtime.getRuntime().addShutdownHook(closingHook);
		env.sync();
		return true;
		}
		catch(Exception e)
		{
			
			//System.out.println(e.toString());
			//System.out.println("Error in Connecting to Berkeley DB");
			return false;
		}
		
	}
	

	
	/*
	 * Function to check whther the given user exists in the system or not. 
	 * If exists, returns true.otherwise false
	 * It helps in creating new logins
	 * 
	 */
	public boolean checkSearchKeyExists(String Key)
	{
		if(db.SearchIndex.get(Key)!=null)
		{
			env.sync();
			return true;
		}
			else
		{
				
			env.sync();
			return false;
		}
		
		}
	

	/*
	 * Function to take Keyword and data as input and saves it accrodingly in the database.
	 * The checking for existing keyword is already done by previous methods. 
	 * This gets activated only when everything is unique.
	 * 
	 * 
	 */
	public boolean addSearchData(String Keyword, String Data)
	{
	try{
			SearchData data=new SearchData(Keyword,Data);
			db.SearchIndex.put(data);	
			//System.out.println("New User Success");
			env.sync();
			return true;
		}
		catch(Exception e)
		{
			//System.out.println("Error in creating New User");
			env.sync();
			return false;
		}
	}
	
	
	/*
	 * Function to check if the keyword exists in the system or not
	 * if so, returns an object of SearchData with complete info about the users
	 * 
	 */
	
	public SearchData retrieveData(String Keyword)
	{
		SearchData temp=db.SearchIndex.get(Keyword);
		
		if(temp==null)
			return null;
		else
		{	env.sync();
			return temp;
		}
			
	}
	
	/*
	 * Function to delete a keyword from DB if a user has requested to do so.
	 * It deletes the keyword along with their data and commits the DB
	 * 
	 */
	
	public boolean deleteSearchData(String Keyword)
	{
		//System.out.println(db.ChannelIndex.get(ID).Name);
		if(db.SearchIndex.delete(Keyword))
		{
		env.sync();
		return true;
		}
		return false;
	}
	
	/*
	 * Updates the UserData object when the crawl has been done and or if any new channel was 
	 * inserted by the user.
	 * It helps in keeping track of user channels and data.
	 * 
	 */
	
	public boolean updateData(SearchData data)
	{
		if(db.SearchIndex.put(data)!=null)
		{
		env.sync();	
		return true;
		}
		return false;
		
	}
	
	/*
	 * Function to close the DB by committing everything.
	 * 
	 */
	public void close()
	{
		env.sync();
		DBClose closingHook=new DBClose(env, storeSearch);
		closingHook.start();
	}
	
	/*
	 * Function to delete entire data from the database.
	 * 
	 */
	
	public boolean deleteData()
	{
		try{
			for(SearchData d : SearchIndex.entities())
			{
				SearchIndex.delete(d.Keyword);
			}
		env.sync();
		System.out.println("DELETED ALL DATA FROM DB");
		return true;
		}
		catch(Exception e)
		{
			return false;	
		}
	}
	
	
	

}
