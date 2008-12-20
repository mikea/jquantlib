
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



/*
 Copyright (C) 2003, 2004 Ferdinando Ametrano
 Copyright (C) 2005 StatPro Italia srl
 Copyright (C) 2005 Joseph Wang

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


/**
 * 
 * Ported from 
 * <ul>
 * <li>test-suite/americanoption.cpp</li>
 * </ul>
 * 
 * @author <Richard Gomes>
 *
 */


package org.jquantlib.testsuite.instruments;

import junit.framework.TestCase;

import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.AmericanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.instruments.VanillaOption;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.vanilla.BjerksundStenslandApproximationEngine;
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


public class AmericanOptionTest {
	
	private final static Logger logger = LoggerFactory.getLogger(AmericanOptionTest.class);

	
	private static class AmericanOptionData {
		public AmericanOptionData(Option.Type type, double strike, 
				double s, double q, double r, double t, double v, double result){
			this.type = type;
			this.strike = strike;
			this.s = s;
			this.q = q;
			this.r = r;
			this.t = t;
			this.v = v;
			this.result = result;
		}
	    Option.Type type;
	    double /*@Real*/ strike;
	    double /*@Real*/ s;        // spot
	    double /*@Rate*/ q;        // dividend
	    double /*@Rate*/ r;        // risk-free rate
	    double /*@Time*/ t;        // time to maturity
	    double /*@Volatility*/ v;  // volatility
	    double /*@Real*/ result;   // expected result
	};

	@Test
	public void testBjerksundStenslandValues() {

		logger.info("Testing Bjerksund and Stensland approximation "
	                  + "for American options...");

	    AmericanOptionData values[] = {
	        //      type, strike,   spot,    q,    r,    t,  vol,   value, tol
	        // from "Option pricing formulas", Haug, McGraw-Hill 1998, pag 27
	      new AmericanOptionData(Option.Type.CALL,  40.00,  42.00, 0.08, 0.04, 0.75, 0.35,  5.2704),
	        // from "Option pricing formulas", Haug, McGraw-Hill 1998, VBA code
	      new AmericanOptionData(Option.Type.PUT,   40.00,  36.00, 0.00, 0.06, 1.00, 0.20,  4.4531) 
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
	    PricingEngine engine = new BjerksundStenslandApproximationEngine();

	    double /*@Real*/ tolerance = 1.0e-4;

	    for (int i=0; i<values.length; i++) {

	        StrikedTypePayoff payoff = new
	            PlainVanillaPayoff(values[i].type, values[i].strike);

	        int daysToExpiry  = (int)(values[i].t*360+0.5);
		    Date exDate = DateFactory.getFactory().getDate(today.getDayOfMonth(), today.getMonthEnum(), today.getYear()).increment(daysToExpiry);

	        Exercise exercise = new AmericanExercise(today, exDate);

	        spot.setValue(values[i].s);
	        qRate.setValue(values[i].q);
	        rRate.setValue(values[i].r);
	        vol.setValue(values[i].v);

	        StochasticProcess stochProcess = new
	            BlackScholesMertonProcess(new Handle<Quote>(spot),
	                                      new Handle<YieldTermStructure>(qTS),
	                                      new Handle<YieldTermStructure>(rTS),
	                                      new Handle<BlackVolTermStructure>(volTS));

	        VanillaOption option = new VanillaOption(stochProcess, payoff, exercise,
	                             engine);

	        double /*@Real*/ calculated = option.getNPV();
	        double /*@Real*/ error = Math.abs(calculated-values[i].result);
	        if (error > tolerance) {
	            REPORT_FAILURE("value", payoff, exercise, values[i].s, values[i].q,
	                           values[i].r, today, values[i].v, values[i].result,
	                           calculated, error, tolerance);
	        }
	    }

	}

	private void REPORT_FAILURE(String greekName, StrikedTypePayoff payoff, Exercise exercise, 
			double s, double q, double r, Date today, 
			double v, double expected, double calculated, double error, double tolerance) {
	TestCase.fail(exercise.type() + " " 
			+ payoff.getOptionType() + " option with " 
			+ payoff.getClass().getSimpleName() + " payoff:\n"
			+ "    spot value:        " + s + "\n" 
			+ "    strike:           " + payoff.getStrike() + "\n" 
			+ "    dividend yield:   " + q + "\n" 
			+ "    risk-free rate:   " + r + "\n" 
			+ "    reference date:   " + today + "\n" 
			+ "    maturity:         " + exercise.lastDate() + "\n" 
			+ "    volatility:       " + v + "\n\n"
			+ "    expected   " + greekName + ": " + expected + "\n" 
			+ "    calculated " + greekName + ": " + calculated + "\n"
			+ "    error:            " + error + "\n" 
			+ "    tolerance:        " + tolerance);
	}
}
