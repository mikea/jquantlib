/*
 Copyright (C) 2008 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/**
 * 
 * @author Srinivas Hasti
 */
package org.jquantlib.testsuite.pricers;

import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Option.Type;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.StopClock;
import org.junit.Test;

//TODO:  Import all the testcase when MC is available
//FIXME: Rename to OldPricerTest
public class OldPricer {
	
	public OldPricer() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	private class BatchData {
	        Option.Type type;
	        double underlying;
	        double strike;
	        double dividendYield;
	        double riskFreeRate;
	        double first;
	        double length;
	        int fixings;
	        double volatility;
	        boolean controlVariate;
	        double result;
			public BatchData(Type type, double underlying, double strike,
					double dividendYield, double riskFreeRate, double first,
					double length, int fixings, double volatility,
					boolean controlVariate, double result) {
				super();
				this.type = type;
				this.underlying = underlying;
				this.strike = strike;
				this.dividendYield = dividendYield;
				this.riskFreeRate = riskFreeRate;
				this.first = first;
				this.length = length;
				this.fixings = fixings;
				this.volatility = volatility;
				this.controlVariate = controlVariate;
				this.result = result;
			}
	    };
	    
	    private StopClock clock = new StopClock();
	    
	    /* @Test public*/ void testMcSingleFactorPricers() {

	        System.out.println("Testing old-style Monte Carlo single-factor pricers...");

	        clock.startClock();

	        DayCounter dc = Actual360.getDayCounter();

	        long seed = 3456789;
  
	        // cannot be too low, or one cannot compare numbers when
	        // switching to a new default generator
	        long fixedSamples = 1023;
	        double minimumTol = 1.0e-2;

	        // batch 5
	        //
	        // data from "Asian Option", Levy, 1997
	        // in "Exotic Options: The State of the Art",
	        // edited by Clewlow, Strickland

	        BatchData cases5[] = {
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0,    2, 0.13, true, 1.51917595129 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0,    4, 0.13, true, 1.67940165674 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0,    8, 0.13, true, 1.75371215251 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0,   12, 0.13, true, 1.77595318693 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0,   26, 0.13, true, 1.81430536630 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0,   52, 0.13, true, 1.82269246898 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0,  100, 0.13, true, 1.83822402464 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0,  250, 0.13, true, 1.83875059026 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0,  500, 0.13, true, 1.83750703638 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 0.0,      11.0/12.0, 1000, 0.13, true, 1.83887181884 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0,    2, 0.13, true, 1.51154400089 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0,    4, 0.13, true, 1.67103508506 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0,    8, 0.13, true, 1.74529684070 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0,   12, 0.13, true, 1.76667074564 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0,   26, 0.13, true, 1.80528400613 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0,   52, 0.13, true, 1.81400883891 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0,  100, 0.13, true, 1.82922901451 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0,  250, 0.13, true, 1.82937111773 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0,  500, 0.13, true, 1.82826193186 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 1.0/12.0, 11.0/12.0, 1000, 0.13, true, 1.82967846654 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0,    2, 0.13, true, 1.49648170891 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0,    4, 0.13, true, 1.65443100462 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0,    8, 0.13, true, 1.72817806731 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0,   12, 0.13, true, 1.74877367895 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0,   26, 0.13, true, 1.78733801988 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0,   52, 0.13, true, 1.79624826757 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0,  100, 0.13, true, 1.81114186876 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0,  250, 0.13, true, 1.81101152587 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0,  500, 0.13, true, 1.81002311939 ),
	              new BatchData( Option.Type.CALL, 90.0, 87.0, 0.06, 0.025, 3.0/12.0, 11.0/12.0, 1000, 0.13, true, 1.81145760308 )
	        };

	        
	        for (int l=0; l<cases5.length; l++) {
	            int dt = (int) cases5[l].length/(cases5[l].fixings-1);
	            double[] timeIncrements = new double[cases5[l].fixings];
	            for (int i=0; i<cases5[l].fixings; i++) {
	            	timeIncrements[i] = i*dt + cases5[l].first;
	            }
	           
	            Date today = DateFactory.getFactory().getTodaysDate();
	            YieldTermStructure yeildStructureRiskFree =  org.jquantlib.testsuite.util.Utilities.flatRate(today,cases5[l].riskFreeRate, dc);
	            YieldTermStructure yeildStructureDividentYield =  org.jquantlib.testsuite.util.Utilities.flatRate(today,cases5[l].dividendYield, dc);
	            YieldTermStructure yeildStructureVolatility =  org.jquantlib.testsuite.util.Utilities.flatRate(today,cases5[l].volatility, dc);
	            
	           // TODO: Complete the test case when we have MonteCarlo
	         
	        }
	        clock.stopClock();
	        clock.log();
	    }
       
}
