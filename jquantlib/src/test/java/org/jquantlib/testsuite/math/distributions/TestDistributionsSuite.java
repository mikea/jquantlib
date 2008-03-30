package org.jquantlib.testsuite.math.distributions;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author <Richard Gomes>
 */
public class TestDistributionsSuite {

	 public static Test suite() { 
         TestSuite suite = new TestSuite("Distributions tests");

         suite.addTestSuite(TestNormalDistribution.class);
         suite.addTestSuite(TestCumulativeNormalDistribution.class);
         suite.addTestSuite(TestBivariateNormalDistribution.class);
         suite.addTestSuite(TestRegularisedIncompleteBeta.class);

         return suite; 
    }
}
