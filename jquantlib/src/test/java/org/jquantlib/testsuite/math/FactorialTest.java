package org.jquantlib.testsuite.math;

import static org.junit.Assert.fail;

import org.jquantlib.math.Factorial;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class FactorialTest {

	public FactorialTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}

	@Test
	public void testCompareToDirect() {
		
		Factorial factorial = new Factorial();
		int n = 4;
		double expected = factorial(n);
		double realised = factorial.get(n);
		if (Math.abs(expected-realised)>1.0e-15)
			fail("n: " + n + " Expected: " + expected + " realised: " + realised);
		
		n = 30;
		expected = factorial(n);
		realised = factorial.get(n);
		if (Math.abs((expected-realised)/expected)>1.0e-10)
			fail("n: " + n + " Expected: " + expected + " realised: " + realised);

	}
	
	private double factorial(int n){
		double x = 1.0;
		for(int i=2;i<=n;i++){
			x*=i;
		}
		return x;
	}
}
