package test.edu.upenn.cis.cis555.hw3;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		
		suite.addTestSuite(WebServiceTest.class);
		suite.addTestSuite(DBTest.class);
		
		
		//$JUnit-END$
		return suite;
	}

}
