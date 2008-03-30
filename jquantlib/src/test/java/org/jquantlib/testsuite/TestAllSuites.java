package org.jquantlib.testsuite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jquantlib.testsuite.math.TestMathSuite;
import org.jquantlib.testsuite.math.distributions.TestDistributionsSuite;
import org.jquantlib.testsuite.math.integrals.TestIntegralsSuite;
import org.jquantlib.testsuite.termstructures.volatilities.TestVolatilitiesSuite;

/**
 * @author <Richard Gomes>
 */
public class TestAllSuites {

	 public static Test suite() { 
         TestSuite suite = new TestSuite("All suites");

         suite.addTest(TestMathSuite.suite()); 
         suite.addTest(TestDistributionsSuite.suite()); 
         suite.addTest(TestIntegralsSuite.suite()); 
         suite.addTest(TestVolatilitiesSuite.suite()); 

         return suite; 
    }

}
