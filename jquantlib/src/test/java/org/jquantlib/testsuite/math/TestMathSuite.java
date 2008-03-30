package org.jquantlib.testsuite.math;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author <Richard Gomes>
 */
public class TestMathSuite {

	 public static Test suite() { 
         TestSuite suite = new TestSuite("Math tests");

         suite.addTestSuite(TestBrentSolver1D.class);
         suite.addTestSuite(TestErrorFunction.class);
         suite.addTestSuite(TestFactorial.class);

         return suite; 
    }

}
