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

/*
 Copyright (C) 2006 Joseph Wang

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib.testsuite.model.volatility;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.jquantlib.model.volatility.ConstantEstimator;
import org.jquantlib.model.volatility.SimpleLocalEstimator;
import org.jquantlib.model.volatility.VolatilityCompositor;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultDate;
import org.jquantlib.util.Month;
import org.jquantlib.util.TimeSeries;
import org.jquantlib.util.TimeSeriesDouble;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EstimatorsTest {

	private static TimeSeriesDouble ts ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	    System.out.println("Testing volatility model construction...");

	    List<Date> dates = Arrays.asList (new Date[] 
	                                       {
		    		                           new DefaultDate(25, Month.MARCH, 2005),
		    		                           new DefaultDate(29, Month.MARCH, 2005),
		    		                           new DefaultDate(15, Month.MARCH, 2005),
		    		                           new DefaultDate(21, Month.MARCH, 2005),
		    		                           new DefaultDate(27, Month.MARCH, 2005)
	                                       }
	    		                         ); 
	    double[] values =new double[]{1.2, 2.3, 0.3, 2.0, 2.5} ;
	    ts = new TimeSeriesDouble(dates, values);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//TODO The testcase for the Simple Estimator. Uncomment once SimpleEstimator is translated.
	@Test
	public void testSECalculate() {
	    SimpleLocalEstimator sle = new SimpleLocalEstimator(1/360.0);
	    TimeSeriesDouble locale = sle.calculate(ts);
		assertNotNull(locale) ;
	}
	
	@Test
	public void testCECalculate() {
	    SimpleLocalEstimator sle = new SimpleLocalEstimator(1/360.0);
	    TimeSeriesDouble locale = sle.calculate(ts);
		VolatilityCompositor ce = new ConstantEstimator(1);
		TimeSeriesDouble value = ce.calculate(locale);
		assertNotNull(value) ;
	}

}
