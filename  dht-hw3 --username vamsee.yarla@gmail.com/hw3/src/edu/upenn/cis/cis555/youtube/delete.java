/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

/**
 * @author VamseeKYarlagadda
 *
 */
/*
 * Class to delete all the data from database transactions and close all it repositories.
 * 
 */
public class delete {
	
	public static void main(String[] args)
	{
		
		DB db=DB.getInstance("JEDB");
		System.out.println(db.deleteData());
		
	}
}
