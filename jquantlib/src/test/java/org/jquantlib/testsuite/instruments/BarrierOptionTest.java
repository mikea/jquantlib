/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.testsuite.instruments;

import javax.swing.SingleSelectionModel;

import junit.framework.TestCase;

import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.AmericanExercise;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.BarrierOption;
import org.jquantlib.instruments.BarrierType;
import org.jquantlib.instruments.OneAssetStrikedOption;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.pricingengines.barrier.AnalyticBarrierOptionEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.testsuite.util.Utilities;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BarrierOptionTest {

	private final static Logger logger = LoggerFactory.getLogger(BarrierOptionTest.class);

	private void REPORT_FAILURE(String greekName, BarrierType barrierType, 
			double barrier, double rebate, StrikedTypePayoff payoff, 
            Exercise exercise, double s, double q, double r, Date today, 
            double v, double expected, double calculated, 
            double error, double tolerance) {
		TestCase.fail("\n" + barrierType + " " + exercise +  
				payoff.getOptionType() + " option with " 
				+ payoff.getClass().getSimpleName() + " payoff:\n" +
				"    underlying value: " +  s + "\n" 
    + "    strike:           " + payoff.getStrike() + "\n" 
    + "    barrier:          " + barrier + "\n" 
    + "    rebate:           " + rebate + "\n" 
    + "    dividend yield:   " + q + "\n" 
    + "    risk-free rate:   " + r + "\n" 
    + "    reference date:   " + today + "\n" 
    + "    maturity:         " + exercise.lastDate() + "\n" 
    + "    volatility:       " + v  + "\n\n" 
    + "    expected   " + greekName + ": " + expected + "\n" 
    + "    calculated " + greekName + ": " + calculated + "\n"
    + "    error:            " + error + "\n" 
    + "    tolerance:        " + tolerance);
	}
	
	
	private static class NewBarrierOptionData {
		
		NewBarrierOptionData(	BarrierType barrierType,
								double barrier,
								double rebate,
								Option.Type type,
								double strike,
								double s,        // spot
								double q,        // dividend
								double r,        // risk-free rate
								double t,        // time to maturity
								double v,  // volatility
								double result,   // result
								double tol      // tolerance
		) {
			this.barrierType = barrierType;
			this.barrier = barrier;
			this.rebate = rebate;
			this.type = type;
			this.strike = strike;
			this.s = s;
			this.q = q;
			this.r = r;
			this.t = t;
			this.v = v;
			this.result = result;
			this.tol = tol;
		}
	    BarrierType barrierType;
	    double barrier;
	    double rebate;
	    Option.Type type;
	    double strike;
	    double s;        // spot
	    double q;        // dividend
	    double r;        // risk-free rate
	    double t;        // time to maturity
	    double v;  // volatility
	    double result;   // result
	    double tol;      // tolerance
	};

	
	
	@Test
	public void testHaugValues() {

	    logger.info("Testing barrier options against Haug's values...");

	    NewBarrierOptionData values[] = {
	        /* The data below are from
	          "Option pricing formulas", E.G. Haug, McGraw-Hill 1998 pag. 72
	        */
	        //     barrierType, barrier, rebate,         type, strike,     s,    q,    r,    t,    v,  result, tol
	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0, Option.Type.CALL,     90, 100.0, 0.04, 0.08, 0.50, 0.25,  9.0246, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0, Option.Type.CALL,    100, 100.0, 0.04, 0.08, 0.50, 0.25,  6.7924, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0, Option.Type.CALL,    110, 100.0, 0.04, 0.08, 0.50, 0.25,  4.8759, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0, Option.Type.CALL,     90, 100.0, 0.04, 0.08, 0.50, 0.25,  3.0000, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0, Option.Type.CALL,    100, 100.0, 0.04, 0.08, 0.50, 0.25,  3.0000, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0, Option.Type.CALL,    110, 100.0, 0.04, 0.08, 0.50, 0.25,  3.0000, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0, Option.Type.CALL,     90, 100.0, 0.04, 0.08, 0.50, 0.25,  2.6789, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0, Option.Type.CALL,    100, 100.0, 0.04, 0.08, 0.50, 0.25,  2.3580, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0, Option.Type.CALL,    110, 100.0, 0.04, 0.08, 0.50, 0.25,  2.3453, 1.0e-4),

	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0, Option.Type.CALL,    90, 100.0, 0.04, 0.08, 0.50, 0.25,  7.7627, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0, Option.Type.CALL,   100, 100.0, 0.04, 0.08, 0.50, 0.25,  4.0109, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0, Option.Type.CALL,   110, 100.0, 0.04, 0.08, 0.50, 0.25,  2.0576, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0, Option.Type.CALL,    90, 100.0, 0.04, 0.08, 0.50, 0.25, 13.8333, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0, Option.Type.CALL,   100, 100.0, 0.04, 0.08, 0.50, 0.25,  7.8494, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0, Option.Type.CALL,   110, 100.0, 0.04, 0.08, 0.50, 0.25,  3.9795, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0, Option.Type.CALL,    90, 100.0, 0.04, 0.08, 0.50, 0.25, 14.1112, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0, Option.Type.CALL,   100, 100.0, 0.04, 0.08, 0.50, 0.25,  8.4482, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0, Option.Type.CALL,   110, 100.0, 0.04, 0.08, 0.50, 0.25,  4.5910, 1.0e-4),

	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0, Option.Type.CALL,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  8.8334, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0, Option.Type.CALL,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  7.0285, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0, Option.Type.CALL,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  5.4137, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0, Option.Type.CALL,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  3.0000, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0, Option.Type.CALL,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  3.0000, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0, Option.Type.CALL,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  3.0000, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0, Option.Type.CALL,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  2.6341, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0, Option.Type.CALL,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  2.4389, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0, Option.Type.CALL,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  2.4315, 1.0e-4),

	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0, Option.Type.CALL,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  9.0093, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0, Option.Type.CALL,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  5.1370, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0, Option.Type.CALL,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  2.8517, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0, Option.Type.CALL,    90, 100.0, 0.04, 0.08, 0.50, 0.30, 14.8816, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0, Option.Type.CALL,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  9.2045, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0, Option.Type.CALL,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  5.3043, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0, Option.Type.CALL,    90, 100.0, 0.04, 0.08, 0.50, 0.30, 15.2098, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0, Option.Type.CALL,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  9.7278, 1.0e-4),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0, Option.Type.CALL,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  5.8350, 1.0e-4),



	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.25,  2.2798, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.25,  2.2947, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.25,  2.6252, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.25,  3.0000, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.25,  3.0000, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.25,  3.0000, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.25,  3.7760, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.25,  5.4932, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.25,  7.5187, 1.0e-4 ),

	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.25,  2.9586, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.25,  6.5677, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.25, 11.9752, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.25,  2.2845, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.25,  5.9085, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.25, 11.6465, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.25,  1.4653, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.25,  3.3721, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.25,  7.0846, 1.0e-4 ),

	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  2.4170, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  2.4258, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,    95.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  2.6246, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  3.0000, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  3.0000, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownOut,   100.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  3.0000, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  4.2293, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  5.8032, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpOut,     105.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  7.5649, 1.0e-4 ),

	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  3.8769, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  7.7989, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,     95.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.30, 13.3078, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  3.3328, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  7.2636, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.DownIn,    100.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.30, 12.9713, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0,  Option.Type.PUT,    90, 100.0, 0.04, 0.08, 0.50, 0.30,  2.0658, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0,  Option.Type.PUT,   100, 100.0, 0.04, 0.08, 0.50, 0.30,  4.4226, 1.0e-4 ),
	        new NewBarrierOptionData( BarrierType.UpIn,      105.0,    3.0,  Option.Type.PUT,   110, 100.0, 0.04, 0.08, 0.50, 0.30,  8.3686, 1.0e-4 )

	        /*
	            Data from "Going to Extreme: Correcting Simulation Bias in Exotic
	            Option Valuation"
	            D.R. Beaglehole, P.H. Dybvig and G. Zhou
	            Financial Analysts Journal; Jan / Feb 1997; 53, 1
	        */
	        //    barrierType, barrier, rebate,         type, strike,     s,    q,    r,    t,    v,  result, tol
	        // { BarrierType.DownOut,    45.0,    0.0,  Option.Type.PUT,     50,  50.0,-0.05, 0.10, 0.25, 0.50,   4.032, 1.0e-3 },
	        // { BarrierType.DownOut,    45.0,    0.0,  Option.Type.PUT,     50,  50.0,-0.05, 0.10, 1.00, 0.50,   5.477, 1.0e-3 }

	    };


	    Date today = DateFactory.getFactory().getTodaysDate();
	    DayCounter dc = Actual360.getDayCounter();
	    SimpleQuote spot = new SimpleQuote(0.0);
	    SimpleQuote qRate = new SimpleQuote(0.0);
	    YieldTermStructure qTS = Utilities.flatRate(today, new Handle<Quote>(qRate), dc);
	    SimpleQuote rRate = new SimpleQuote(0.0);
	    YieldTermStructure rTS = Utilities.flatRate(today, new Handle<Quote>(rRate), dc);
	    SimpleQuote vol = new SimpleQuote(0.0);
	    BlackVolTermStructure volTS = Utilities.flatVol(today, new Handle<Quote>(vol), dc);

	    
	    for (int i=0; i<values.length; i++) {
	        Date exDate = today.getDateAfter( timeToDays(values[i].t) );

	        Exercise exercise = new EuropeanExercise(exDate);

	        spot.setValue(values[i].s);
	        qRate.setValue(values[i].q);
	        rRate.setValue(values[i].r);
	        vol.setValue(values[i].v);

	        StrikedTypePayoff payoff = new
	            PlainVanillaPayoff(values[i].type, values[i].strike);

	        StochasticProcess stochProcess = new
	            BlackScholesMertonProcess(new Handle<Quote>(spot),
	                                      new Handle<YieldTermStructure>(qTS),
	                                      new Handle<YieldTermStructure>(rTS),
	                                      new Handle<BlackVolTermStructure>(volTS));

	        BarrierOption barrierOption = new BarrierOption(
	                values[i].barrierType,
	                values[i].barrier,
	                values[i].rebate,
	                stochProcess,
	                payoff,
	                exercise,
	                new AnalyticBarrierOptionEngine());
	        double calculated = barrierOption.getNPV();
	        double expected = values[i].result;
	        double error = Math.abs(calculated-expected);
	        if (error>values[i].tol) {
	            REPORT_FAILURE("value", values[i].barrierType, values[i].barrier,
	                           values[i].rebate, payoff, exercise, values[i].s,
	                           values[i].q, values[i].r, today, values[i].v,
	                           expected, calculated, error, values[i].tol);
	        }

	    }
	}

	private int timeToDays(/*@Time*/ double t) {
	    return (int) (t*360+0.5);
	}

}
