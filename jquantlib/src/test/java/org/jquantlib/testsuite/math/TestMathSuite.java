package org.jquantlib.testsuite.math;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @deprecated
 * @author <Richard Gomes>
 */
//TODO: remove or 'upgrade' this class once we are using JUnit4
public class TestMathSuite {

	 public static Test suite() { 
         TestSuite suite = new TestSuite("Math tests");

         suite.addTestSuite(TestBrentSolver1D.class);
         suite.addTestSuite(TestErrorFunction.class);
         suite.addTestSuite(TestFactorial.class);

         return suite; 
    }

}
