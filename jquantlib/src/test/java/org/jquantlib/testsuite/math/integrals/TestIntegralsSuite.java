package org.jquantlib.testsuite.math.integrals;


import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @deprecated
 * @author <Richard Gomes>
 */
//TODO: remove or 'upgrade' this class once we are using JUnit4
public class TestIntegralsSuite {
	
	 public static Test suite() { 
         TestSuite suite = new TestSuite("Integrals tests");

         suite.addTestSuite(TestGammaFunction.class);
         suite.addTestSuite(TestGaussKonrodPattersonIntegrator.class);
        // suite.addTestSuite(TestIncompleteGammaFunctionQ.class);
         suite.addTestSuite(TestTabulatedGaussLegendre.class);


         return suite; 
    }


}
