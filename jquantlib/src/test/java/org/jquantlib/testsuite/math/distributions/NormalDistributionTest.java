/*
 Copyright (C) 2007 Richard Gomes

 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.testsuite.math.distributions;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.jquantlib.math.distributions.NormalDistribution;
import org.junit.Test;

/**
 * @author Richard Gomes
 */
public class NormalDistributionTest {
	
    private final static Logger logger = Logger.getLogger(NormalDistributionTest.class);

	public NormalDistributionTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testNormalDistribution() {
		
		// good values from Abram, Stegun
		double[][] testvalues = {	{0.0, 0.398942280401433},
									{1.0, 0.241970724519143},
									{2.0, 0.053990966513188},
									{3.0, 0.004431848411938},
									{4.0, 1.338302258e-4},
									{5.0, 1.486719515e-6}};
									
		
		NormalDistribution normal = new NormalDistribution();
		
		for(int i=0;i<testvalues.length;i++){
			double z = testvalues[i][0];
			double expected = testvalues[i][1];
			double computed = normal.evaluate(z);
			double tolerance = (Math.abs(z)<3.01) ? 1.0e-15: 1.0e-10;
			
			//assertEquals(expected, computed,tolerance);
			if(expected-computed>tolerance){
				fail("expected : " + expected + " but was "+ computed);
			}
			
			//assertEquals(expected, normal.evaluate(-z),tolerance);
			if(Math.abs(expected-normal.evaluate(-z))>tolerance){
				fail("expected: " + expected + " but was " + normal.evaluate(-z));
			}
		}
	}
}
