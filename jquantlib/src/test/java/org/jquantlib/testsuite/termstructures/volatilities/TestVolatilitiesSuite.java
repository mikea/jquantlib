package org.jquantlib.testsuite.termstructures.volatilities;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @deprecated
 * @author <Richard Gomes>
 */
//TODO: remove or 'upgrade' this class once we are using JUnit4
public class TestVolatilitiesSuite {

	 public static Test suite() { 
         TestSuite suite = new TestSuite("Volatilities tests");

         suite.addTestSuite(TestSabr.class);

         return suite; 
    }
}
