/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import java.util.ArrayList;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * @author VamseeKYarlagadda
 *
 */
/*
 * Entity to save all the User data: from their Name, Passwords, Username and also the
 * channels that get created in the system. It helps to maintain a single repository 
 * of everything rather than storing all the data separately. 
 * 
 */
@Entity
public class SearchData {

	
	public String Data;
	//public ArrayList<String> URLs;
	
	
@PrimaryKey
	public String Keyword;
	

public SearchData()
{
	
}
	
//public UserData(String Username,String Password, String[] XPaths, String[] URLs)
public SearchData(String Keyword,String Data)
{
	this.Keyword=Keyword;
	this.Data=Data;
	
}

}
