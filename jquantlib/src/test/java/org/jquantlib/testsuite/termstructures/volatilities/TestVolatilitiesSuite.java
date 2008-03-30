package org.jquantlib.testsuite.termstructures.volatilities;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author <Richard Gomes>
 */
public class TestVolatilitiesSuite {

	 public static Test suite() { 
         TestSuite suite = new TestSuite("Volatilities tests");

         suite.addTestSuite(TestSabr.class);

         return suite; 
    }
}
