package edu.upenn.cis.cis555.youtube;

import java.io.File;
import java.net.Inet4Address;

public class P2PCache {

	public static void main(String[] args){
		
		if(args.length<4 || args.length>5)
		{
			System.out.println("Inavlid Set of Arguments");
			System.out.println("Usage: <Local Port><IP Pastry Boot><Port Pastry Boot><Port daemon|<Optional: Path to BDB>");
		}
		
		try{
			   Integer.parseInt(args[0]);
			   Integer.parseInt(args[2]);
			   Integer.parseInt(args[3]);
			   
			   if(args.length==5)
			   {
			   File x=new File(args[4]);
			   if(!x.exists())
			   {
				   throw new IllegalArgumentException("Bad DB");
			   }
			   }
			   
			   Inet4Address.getByAddress(args[1].getBytes());
	    	}
		catch(Exception e)
		{
			System.out.println("Error in enetered Data: Check again!");
		}
		
	}
}
