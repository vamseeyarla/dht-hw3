/**
 * 
 */
package edu.upenn.cis.cis555.youtube;


import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * @author VamseeKYarlagadda
 *
 */
/*
 * Entity to save all the keywords and their data .
 * It helps to maintain a single repository 
 * of everything rather than storing all the data separately. 
 * 
 */
@Entity
public class SearchData {

	
	public String Data;
		
	
@PrimaryKey
	public String Keyword;
	

public SearchData()
{
	
}

public SearchData(String Keyword,String Data)
{
	this.Keyword=Keyword;
	this.Data=Data;
	
}

}
