package org.jquantlib.testsuite.util.stdlibc;

import static org.junit.Assert.assertTrue;

import org.jquantlib.math.Closeness;
import org.jquantlib.testsuite.math.interpolations.BilinearInterpolationTest;
import org.jquantlib.util.stdlibc.Std;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StdTest {
	
	//TODO: implement other testcases
	
	private final static Logger logger = LoggerFactory.getLogger(StdTest.class);
		
	public StdTest(){
		
	}
	
	@Test
	public void shouldReturnAdjacent_difference() {
	    final double[] inputList = { 1.0, 2.0, 3.0, 5.0, 9.0, 11.0, 12.0 };
	    final double[] expected  = { 1.0, 1.0, 1.0, 2.0, 4.0,  2.0,  1.0 };

		
        final double[] actual = Std.getInstance().adjacent_difference(inputList);

        assertTrue(actual!=null && actual.length==expected.length);
        for (int i=0; i<actual.length; i++) {
            assertTrue(Closeness.isClose(expected[i], actual[i]));
        }
	}

}
