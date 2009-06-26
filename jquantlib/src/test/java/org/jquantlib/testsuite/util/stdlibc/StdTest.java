package org.jquantlib.testsuite.util.stdlibc;

import static org.junit.Assert.assertEquals;

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
	public void shouldReturnAdjacent_difference(){
	    final double[] inputList = { 1.0, 2.0, 3.0, 5.0, 9.0, 11.0, 12.0 };
	    final double[] expected  = { 1.0, 1.0, 1.0, 2.0, 4.0,  2.0,  1.0 };

		
        final double[] outputList = Std.adjacent_difference(inputList);

        for (int i=0; i<outputList.length; i++) {
            assertEquals("adjacent_difference failed", outputList[i], expected[i]);
        }
	}

}
