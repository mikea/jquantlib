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

package org.jquantlib.testsuite.instruments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.jquantlib.Configuration;
import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.AverageType;
import org.jquantlib.instruments.DiscreteAveragingAsianOption;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.asian.AnalyticDiscreteGeometricAveragePriceAsianEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.testsuite.util.Utilities;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author <Richard Gomes>
 */
public class AsianOptionTest {

    private final static Logger logger = LoggerFactory.getLogger(AsianOptionTest.class);

	@Test
	public void reportFailure() {

	    logger.info("Testing analytic discrete geometric average-price Asians...");

	    // data from "Implementing Derivatives Model",
	    // Clewlow, Strickland, p.118-123

	    DayCounter dc = Actual360.getDayCounter();
	    Date today = DateFactory.getFactory().getTodaysDate();

	    logger.info("Today: " + today);
	    
	    SimpleQuote spot = new SimpleQuote(100.0);
	    SimpleQuote qRate = new SimpleQuote(0.03);
	    YieldTermStructure qTS = Utilities.flatRate(today, qRate.evaluate(), dc);
	    SimpleQuote rRate = new SimpleQuote(0.06);
	    YieldTermStructure rTS = Utilities.flatRate(today, rRate.evaluate(), dc);
	    SimpleQuote vol = new SimpleQuote(0.20);
	    BlackVolTermStructure volTS = Utilities.flatVol(today, vol.evaluate(), dc);

	    StochasticProcess stochProcess = new
	        org.jquantlib.processes.BlackScholesMertonProcess(new Handle<Quote>(spot),
	                                  new Handle<YieldTermStructure>(qTS),
	                                  new Handle<YieldTermStructure>(rTS),
	                                  new Handle<BlackVolTermStructure>(volTS));

	    PricingEngine engine = new AnalyticDiscreteGeometricAveragePriceAsianEngine();

	    AverageType averageType = AverageType.Geometric;
	    /*@Real*/ double runningAccumulator = 1.0;
	    /*@Size*/ int pastFixings = 0;
	    /*@Size*/ int futureFixings = 10;
	    Option.Type type = Option.Type.CALL;
	    /*@Real*/ double strike = 100.0;
	    StrikedTypePayoff payoff = new PlainVanillaPayoff(type, strike);

	    Date exerciseDate = DateFactory.getFactory().getDate(today.getDayOfMonth(), today.getMonthEnum(), today.getYear()).increment(360);
	    Exercise exercise = new EuropeanExercise(exerciseDate);

	    logger.info("Exercise: " + exerciseDate);
	    logger.info("Df: "+ rTS.discount(exerciseDate));
	    logger.info("DivDf: "+ qTS.discount(exerciseDate));
	    
	    List<Date> fixingDates = new ArrayList<Date>(futureFixings);
	    int dt = (int)(360.0/((double) futureFixings)+0.5);
	    fixingDates.add(DateFactory.getFactory().getDate(today.getDayOfMonth(), today.getMonthEnum(), today.getYear()).increment(dt));
	    for (int j=1; j<futureFixings; j++){
	    	Date prevDate = fixingDates.get(j-1);
	    	fixingDates.add(DateFactory.getFactory().getDate(prevDate.getDayOfMonth(),prevDate.getMonthEnum(),prevDate.getYear()).increment(dt));
	    }
	    
	    logger.info("Average Dates:\n" );
	    for(Date d: fixingDates){
	        logger.info(d.toString());
	    }
	    
	    DiscreteAveragingAsianOption option = new DiscreteAveragingAsianOption(averageType, runningAccumulator,
	                                        pastFixings, fixingDates,
	                                        stochProcess, payoff,
	                                        exercise, engine);

	    /*@Real*/ double calculated = option.getNPV();
	    /*@Real*/ double expected = 5.3425606635;
	    	   
	    /*@Real*/ double tolerance = 1e-10;
	    if (Math.abs(calculated-expected) > tolerance) {
	        reportFailure("value", averageType, runningAccumulator, pastFixings,
	                       fixingDates, payoff, exercise, spot.evaluate(),
	                       qRate.evaluate(), rRate.evaluate(), today,
	                       vol.evaluate(), expected, calculated, tolerance);
	    }
	}

	@Test
	public void testAnalyticDiscreteGeometricAveragePriceGreeks() {

	    logger.info("Testing discrete-averaging geometric Asian greeks...");

	    Map<String,Double> tolerance = new HashMap<String, Double>();
	    tolerance.put("delta", 1.0e-5);
	    tolerance.put("gamma",1.0e-5);
	    tolerance.put("theta",1.0e-5);
	    tolerance.put("rho",1.0e-5);
	    tolerance.put("divRho",1.0e-5);
	    tolerance.put("vega",1.0e-5);

	    Option.Type types[] = { Option.Type.CALL, Option.Type.PUT};
	    /*@Real*/ double underlyings[] = { 100.0 };
	    /*@Real*/ double strikes[] = { 90.0, 100.0, 110.0 };
	    /*@Real*/ double qRates[] = { 0.04, 0.05, 0.06 };
	    /*@Real*/ double rRates[] = { 0.01, 0.05, 0.15 };
	    /*@Integer*/ int lengths[] = { 1, 2 };
	    /*@Volatility*/ double vols[] = { 0.11, 0.50, 1.20 };

	    DayCounter dc = Actual360.getDayCounter();
	    Date today = DateFactory.getFactory().getTodaysDate();
	    //FIXME check how to use settings...
	    //Settings settings = new Settings().setEvaluationDate(today);

        Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(today);

	    SimpleQuote spot = new SimpleQuote(0.0);
	    
	    SimpleQuote qRate = new SimpleQuote(0.0);
	    YieldTermStructure qTS = Utilities.flatRate(new Handle<SimpleQuote>(qRate), dc);
	    SimpleQuote rRate = new SimpleQuote(0.0);
	    YieldTermStructure rTS = Utilities.flatRate(new Handle<SimpleQuote>(rRate), dc);
	    
	    SimpleQuote vol = new SimpleQuote(0.0);
	    BlackVolTermStructure volTS = Utilities.flatVol(new Handle<SimpleQuote>(vol), dc);

	    StochasticProcess process = 
	         new BlackScholesMertonProcess(new Handle<Quote>(spot), 
	        		 						new Handle<YieldTermStructure>(qTS), 
	        		 						new Handle<YieldTermStructure>(rTS), 
	        		 						new Handle<BlackVolTermStructure>(volTS));

	    for (int i=0; i<types.length; i++) {
	      for (int j=0; j<strikes.length; j++) {
	        for (int k=0; k<lengths.length; k++) {

	        	Date exerciseDate = DateFactory.getFactory().getDate(today.getDayOfMonth(), today.getMonthEnum(), today.getYear() + lengths[k]);
	        	EuropeanExercise maturity = new EuropeanExercise(exerciseDate);

	            PlainVanillaPayoff payoff = new PlainVanillaPayoff(types[i], strikes[j]);

	            double runningAverage = 120;
	            int pastFixings = 1;

	            List<Date> fixingDates = new ArrayList<Date>();
	            
	            Date d = DateFactory.getFactory().getDate(today.getDayOfMonth(), today.getMonthEnum(), today.getYear());
	            Period THREEMONTH = new Period(3,TimeUnit.MONTHS);
		        d.adjust(new Period(3,TimeUnit.MONTHS));
	            for (	d.adjust(THREEMONTH);
	            		d.le(maturity.lastDate());
	            		d.adjust(THREEMONTH)){
	                fixingDates.add(DateFactory.getFactory().getDate(d.getDayOfMonth(), 
	                									d.getMonthEnum(), d.getYear()));
	            }

	            PricingEngine engine = new AnalyticDiscreteGeometricAveragePriceAsianEngine();

	            DiscreteAveragingAsianOption option = new DiscreteAveragingAsianOption(AverageType.Geometric,
	                                                runningAverage, pastFixings,
	                                                fixingDates, process, payoff,
	                                                maturity, engine);

	            for (int l=0; l<underlyings.length; l++) {
	              for (int m=0; m<qRates.length; m++) {
	                for (int n=0; n<rRates.length; n++) {
	                  for (int p=0; p<vols.length; p++) {

	                      double u = underlyings[l];
	                      double q = qRates[m],
	                           r = rRates[n];
	                      double v = vols[p];
	                      spot.setValue(u);
	                      qRate.setValue(q);
	                      rRate.setValue(r);
	                      vol.setValue(v);

	                      double value = option.getNPV();
	                      Map<String, Double> calculated = new HashMap<String, Double>();
	                      calculated.put("delta", option.delta());
	                      calculated.put("gamma", option.gamma());
	                      calculated.put("theta", option.theta());
	                      calculated.put("rho", option.rho());
	                      calculated.put("divRho", option.dividendRho());
	                      calculated.put("vega", option.vega());

	                      Map<String,Double> expected = new HashMap<String, Double>();
	                      if (value > spot.evaluate()*1.0e-5) {
	                          // perturb spot and get delta and gamma
	                          double du = u*1.0e-4;
	                          spot.setValue(u+du);
	                          double value_p = option.getNPV();
	                          double delta_p = option.delta();
	                          spot.setValue(u-du);
	                          double value_m = option.getNPV();
	                          double delta_m = option.delta();
	                          spot.setValue(u);
	                          expected.put("delta", (value_p - value_m)/(2*du));
	                          expected.put("gamma",(delta_p - delta_m)/(2*du));

	                          // perturb rates and get rho and dividend rho
	                          double dr = r*1.0e-4;
	                          rRate.setValue(r+dr);
	                          value_p = option.getNPV();
	                          rRate.setValue(r-dr);
	                          value_m = option.getNPV();
	                          rRate.setValue(r);
	                          expected.put("rho",(value_p - value_m)/(2*dr));

	                          double dq = q*1.0e-4;
	                          qRate.setValue(q+dq);
	                          value_p = option.getNPV();
	                          qRate.setValue(q-dq);
	                          value_m = option.getNPV();
	                          qRate.setValue(q);
	                          expected.put("divRho", (value_p - value_m)/(2*dq));

	                          //perturb volatility and get vega
	                          double dv = v*1.0e-4;
	                          vol.setValue(v+dv);
	                          value_p = option.getNPV();
	                          vol.setValue(v-dv);
	                          value_m = option.getNPV();
	                          vol.setValue(v);
	                          expected.put("vega", (value_p - value_m)/(2*dv));

	                          // perturb date and get theta
	                          Date prevday = DateFactory.getFactory().getDate(today.getDayOfMonth(), 
	            						today.getMonthEnum(), today.getYear()).decrement();

	                          Date nextday = DateFactory.getFactory().getDate(today.getDayOfMonth(), 
	            						today.getMonthEnum(), today.getYear()).increment();
	                          
	                          double dT = dc.yearFraction(prevday, nextday);
	                          // logger.info("Change date to rtheta");
	                          Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(prevday);
	                          value_m = option.getNPV();
	                          // logger.info("Change date to rtheta");
	                          Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(nextday);
	                          value_p = option.getNPV();
	                          // logger.info("Change date to rtheta");
	                          Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(today);
	                          expected.put("theta", (value_p - value_m)/dT);

	                          // compare
	                          for (Entry<String, Double> greek: calculated.entrySet()) {
	                              double expct = expected.get(greek.getKey());
	                              double calcl = calculated.get(greek.getKey());
	                              double tol   = tolerance.get(greek.getKey());
	                              double error = Utilities.relativeError(expct,calcl,u);
	                              if (error>tol) {
	                                  reportFailure(greek.getKey(), AverageType.Geometric,
	                                                 runningAverage, pastFixings,
	                                                 new ArrayList<Date>(),
	                                                 payoff, maturity,
	                                                 u, q, r, today, v,
	                                                 expct, calcl, tol);
	                              }
	                          } 
	                      }
	                  }
	                }
	              }
	            }
	        }
	      }
	    }
	}
	
	private void reportFailure(String greekName, AverageType averageType,
			double runningAccumulator, int pastFixings,
			List<Date>fixingDates, StrikedTypePayoff payoff, Exercise exercise, 
			double s, double q, double r, Date today, double v,
			double expected, double calculated, double tolerance){

		TestCase.fail(exercise
				+ " Asian option with " 
				+ averageType + " and " 
				+ payoff + " payoff:\n" 
				+ "    running variable: " 
				+ runningAccumulator + "\n" 
				+ "    past fixings:     " 
				+ pastFixings + "\n" 
				+ "    future fixings:   " + fixingDates.size() + "\n" 
				+ "    underlying value: " + s + "\n" 
				+ "    strike:           " + payoff.strike() + "\n" 
				+ "    dividend yield:   " + q + "\n" 
				+ "    risk-free rate:   " + r + "\n" 
				+ "    reference date:   " + today + "\n" 
				+ "    maturity:         " + exercise.lastDate() + "\n" 
				+ "    volatility:       " + v + "\n\n" 
				+ "    expected   " + greekName + ": " + expected + "\n" 
				+ "    calculated " + greekName + ": " + calculated + "\n"
				+ "    error:            " + Math.abs(expected-calculated) 
				+ "\n" 
				+ "    tolerance:        " + tolerance);
		
	}
}
