package org.jquantlib.testsuite.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jquantlib.math.BrentSolver1D;
import org.jquantlib.math.UnaryFunctionDouble;
import org.junit.Test;

/**
 * @author Richard Gomes
 */

// TODO Move this test case to testsuite/math/solvers1D
public class BrentSolver1dTest {

	@Test
	public void testInvertSquare() {
		
		UnaryFunctionDouble square = new UnaryFunctionDouble() {

			public double evaluate(double x) {
				return x*x-1;
			}
			
		};
	
	double accuracy = 1.0e-15;
	BrentSolver1D brent = new BrentSolver1D();
	
	double soln = brent.solve(square, accuracy, 0.01, 0, 2);

	// assertEquals(1.0, soln,accuracy);
	if(Math.abs(1.0-soln) > accuracy ){
		fail("expected: 1.0 but was: " + (soln-accuracy));
	}
	
	// assertEquals(10, brent.getNumEvaluations());
	if(brent.getNumEvaluations() != 10){
		fail("expected: 10" + " but was: " + brent.getNumEvaluations());
	}
	
	
	soln = brent.solve(square, accuracy, 0.01, 0.1);
	
	// assertEquals(1.0, soln,accuracy);
	if(Math.abs(1.0-soln) > accuracy ){
		fail("expected: 1.0 but was: " + (soln-accuracy));
	}
	
	// assertEquals(13, brent.getNumEvaluations());
	if(brent.getNumEvaluations() != 13){
		fail("expected: 13" + " but was: " + brent.getNumEvaluations());
	}
	
	}
	
}
