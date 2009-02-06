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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.AmericanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.instruments.VanillaOption;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.vanilla.BaroneAdesiWhaleyApproximationEngine;
import org.jquantlib.pricingengines.vanilla.BjerksundStenslandApproximationEngine;
import org.jquantlib.pricingengines.vanilla.JuQuadraticApproximationEngine;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDAmericanEngine;
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
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmericanOptionTest {

	private final static Logger logger = LoggerFactory
			.getLogger(AmericanOptionTest.class);

	public AmericanOptionTest() {
		logger.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
	}

	@Test
	public void testBjerksundStenslandValues() {

		logger.info("Testing Bjerksund and Stensland approximation "
				+ "for American options...");

		final AmericanOptionData values[] = {
		// type, strike, spot, q, r, t, vol, value, tol
				// from "Option pricing formulas", Haug, McGraw-Hill 1998, pag
				// 27
				new AmericanOptionData(Option.Type.CALL, 40.00, 42.00, 0.08,
						0.04, 0.75, 0.35, 5.2704),
				// from "Option pricing formulas", Haug, McGraw-Hill 1998, VBA
				// code
				new AmericanOptionData(Option.Type.PUT, 40.00, 36.00, 0.00,
						0.06, 1.00, 0.20, 4.4531) };

		final Date today = DateFactory.getFactory().getTodaysDate();
		final DayCounter dc = Actual360.getDayCounter();
		final SimpleQuote spot = new SimpleQuote(0.0);
		final SimpleQuote qRate = new SimpleQuote(0.0);
		final YieldTermStructure qTS = Utilities.flatRate(today,
				new Handle<Quote>(qRate), dc);
		final SimpleQuote rRate = new SimpleQuote(0.0);
		final YieldTermStructure rTS = Utilities.flatRate(today,
				new Handle<Quote>(rRate), dc);
		final SimpleQuote vol = new SimpleQuote(0.0);
		final BlackVolTermStructure volTS = Utilities.flatVol(today,
				new Handle<Quote>(vol), dc);
		final PricingEngine engine = new BjerksundStenslandApproximationEngine();

		double /* @Real */tolerance = 1.0e-4;

		for (int i = 0; i < values.length; i++) {

			final StrikedTypePayoff payoff = new PlainVanillaPayoff(
					values[i].type, values[i].strike);

			final int daysToExpiry = (int) (values[i].t * 360 + 0.5);
			final Date exDate = DateFactory.getFactory().getDate(
					today.getDayOfMonth(), today.getMonthEnum(),
					today.getYear()).increment(daysToExpiry);

			final Exercise exercise = new AmericanExercise(today, exDate);

			spot.setValue(values[i].s);
			qRate.setValue(values[i].q);
			rRate.setValue(values[i].r);
			vol.setValue(values[i].v);

			final StochasticProcess stochProcess = new BlackScholesMertonProcess(
					new Handle<Quote>(spot),
					new Handle<YieldTermStructure>(qTS),
					new Handle<YieldTermStructure>(rTS),
					new Handle<BlackVolTermStructure>(volTS));

			final VanillaOption option = new VanillaOption(stochProcess,
					payoff, exercise, engine);

			final double /* @Real */calculated = option.getNPV();
			final double /* @Real */error = Math.abs(calculated
					- values[i].result);
			if (error > tolerance) {
				REPORT_FAILURE("value", payoff, exercise, values[i].s,
						values[i].q, values[i].r, today, values[i].v,
						values[i].result, calculated, error, tolerance);
			}
		}

	}

	@Test
	public void testBaroneAdesiWhaley() {
		logger.info("Testing Barone-Adesi and Whaley approximation "
				+ "for American options...");

		// /* The data below are from
		// "Option pricing formulas", E.G. Haug, McGraw-Hill 1998
		// pag 24
		//
		// The following values were replicated only up to the second digit
		// by the VB code provided by Haug, which was used as base for the
		// C++ implementation
		//
		// */
		final AmericanOptionData values[] = {
				// type, strike, spot, q, r, t, vol, value
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.10,
						0.10, 0.10, 0.15, 0.0206),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.10,
						0.10, 0.10, 0.15, 1.8771),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.10,
						0.10, 0.10, 0.15, 10.0089),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.10,
						0.10, 0.10, 0.25, 0.3159),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.10,
						0.10, 0.10, 0.25, 3.1280),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.10,
						0.10, 0.10, 0.25, 10.3919),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.10,
						0.10, 0.10, 0.35, 0.9495),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.10,
						0.10, 0.10, 0.35, 4.3777),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.10,
						0.10, 0.10, 0.35, 11.1679),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.10,
						0.10, 0.50, 0.15, 0.8208),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.10,
						0.10, 0.50, 0.15, 4.0842),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.10,
						0.10, 0.50, 0.15, 10.8087),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.10,
						0.10, 0.50, 0.25, 2.7437),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.10,
						0.10, 0.50, 0.25, 6.8015),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.10,
						0.10, 0.50, 0.25, 13.0170),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.10,
						0.10, 0.50, 0.35, 5.0063),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.10,
						0.10, 0.50, 0.35, 9.5106),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.10,
						0.10, 0.50, 0.35, 15.5689),
				new AmericanOptionData(Option.Type.PUT, 100.00, 90.00, 0.10,
						0.10, 0.10, 0.15, 10.0000),
				new AmericanOptionData(Option.Type.PUT, 100.00, 100.00, 0.10,
						0.10, 0.10, 0.15, 1.8770),
				new AmericanOptionData(Option.Type.PUT, 100.00, 110.00, 0.10,
						0.10, 0.10, 0.15, 0.0410),
				new AmericanOptionData(Option.Type.PUT, 100.00, 90.00, 0.10,
						0.10, 0.10, 0.25, 10.2533),
				new AmericanOptionData(Option.Type.PUT, 100.00, 100.00, 0.10,
						0.10, 0.10, 0.25, 3.1277),
				new AmericanOptionData(Option.Type.PUT, 100.00, 110.00, 0.10,
						0.10, 0.10, 0.25, 0.4562),
				new AmericanOptionData(Option.Type.PUT, 100.00, 90.00, 0.10,
						0.10, 0.10, 0.35, 10.8787),
				new AmericanOptionData(Option.Type.PUT, 100.00, 100.00, 0.10,
						0.10, 0.10, 0.35, 4.3777),
				new AmericanOptionData(Option.Type.PUT, 100.00, 110.00, 0.10,
						0.10, 0.10, 0.35, 1.2402),
				new AmericanOptionData(Option.Type.PUT, 100.00, 90.00, 0.10,
						0.10, 0.50, 0.15, 10.5595),
				new AmericanOptionData(Option.Type.PUT, 100.00, 100.00, 0.10,
						0.10, 0.50, 0.15, 4.0842),
				new AmericanOptionData(Option.Type.PUT, 100.00, 110.00, 0.10,
						0.10, 0.50, 0.15, 1.0822),
				new AmericanOptionData(Option.Type.PUT, 100.00, 90.00, 0.10,
						0.10, 0.50, 0.25, 12.4419),
				new AmericanOptionData(Option.Type.PUT, 100.00, 100.00, 0.10,
						0.10, 0.50, 0.25, 6.8014),
				new AmericanOptionData(Option.Type.PUT, 100.00, 110.00, 0.10,
						0.10, 0.50, 0.25, 3.3226),
				new AmericanOptionData(Option.Type.PUT, 100.00, 90.00, 0.10,
						0.10, 0.50, 0.35, 14.6945),
				new AmericanOptionData(Option.Type.PUT, 100.00, 100.00, 0.10,
						0.10, 0.50, 0.35, 9.5104),
				new AmericanOptionData(Option.Type.PUT, 100.00, 110.00, 0.10,
						0.10, 0.50, 0.35, 5.8823) };

		final Date today = DateFactory.getFactory().getTodaysDate();
		final DayCounter dc = Actual360.getDayCounter();
		final SimpleQuote spot = new SimpleQuote(0.0);
		final SimpleQuote qRate = new SimpleQuote(0.0);
		final YieldTermStructure qTS = Utilities.flatRate(today,
				new Handle<Quote>(qRate), dc);
		final SimpleQuote rRate = new SimpleQuote(0.0);
		final YieldTermStructure rTS = Utilities.flatRate(today,
				new Handle<Quote>(rRate), dc);
		final SimpleQuote vol = new SimpleQuote(0.0);
		final BlackVolTermStructure volTS = Utilities.flatVol(today,
				new Handle<Quote>(vol), dc);

		final PricingEngine engine = new BaroneAdesiWhaleyApproximationEngine();

		final double /* @Real */tolerance = 3.0e-3;

		for (int i = 0; i < values.length; i++) {

			final StrikedTypePayoff payoff = new PlainVanillaPayoff(
					values[i].type, values[i].strike);

			final Date exDate = today.getDateAfter(timeToDays(values[i].t));

			final Exercise exercise = new AmericanExercise(today, exDate);

			spot.setValue(values[i].s);
			qRate.setValue(values[i].q);
			rRate.setValue(values[i].r);
			vol.setValue(values[i].v);

			final StochasticProcess stochProcess = new BlackScholesMertonProcess(
					new Handle<Quote>(spot),
					new Handle<YieldTermStructure>(qTS),
					new Handle<YieldTermStructure>(rTS),
					new Handle<BlackVolTermStructure>(volTS));

			final VanillaOption option = new VanillaOption(stochProcess,
					payoff, exercise, engine);

			final double /* @Real */calculated = option.getNPV();
			final double /* @Real */error = Math.abs(calculated
					- values[i].result);
			if (error > tolerance) {
				REPORT_FAILURE("value", payoff, exercise, values[i].s,
						values[i].q, values[i].r, today, values[i].v,
						values[i].result, calculated, error, tolerance);
			}
		}
	}

	@Test
	public void testJu() {

		/*
		 * The data below are from An Approximate Formula for Pricing American
		 * Options Journal of Derivatives Winter 1999 Ju, N.
		 */
		final AmericanOptionData juValues[] = {
				// type, strike, spot, q, r, t, vol, value, tol
				// These values are from Exhibit 3 - Short dated Put Options
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.0833, 0.2, 0.006),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.3333, 0.2, 0.201),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.5833, 0.2, 0.433),

				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.0833, 0.2, 0.851),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.3333, 0.2, 1.576),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.5833, 0.2, 1.984),

				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.0833, 0.2, 5.000),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.3333, 0.2, 5.084),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.5833, 0.2, 5.260),

				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.0833, 0.3, 0.078),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.3333, 0.3, 0.697),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.5833, 0.3, 1.218),

				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.0833, 0.3, 1.309),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.3333, 0.3, 2.477),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.5833, 0.3, 3.161),

				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.0833, 0.3, 5.059),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.3333, 0.3, 5.699),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.5833, 0.3, 6.231),

				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.0833, 0.4, 0.247),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.3333, 0.4, 1.344),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.5833, 0.4, 2.150),

				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.0833, 0.4, 1.767),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.3333, 0.4, 3.381),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.5833, 0.4, 4.342),

				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.0833, 0.4, 5.288),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.3333, 0.4, 6.501),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.5833, 0.4, 7.367),

				// Type in Exhibits 4 and 5 if you have some spare time ;-)

				// type, strike, spot, q, r, t, vol, value, tol
				// These values are from Exhibit 6 - Long dated Call Options
				// with dividends
				new AmericanOptionData(Option.Type.CALL, 100.00, 80.00, 0.07,
						0.03, 3.0, 0.2, 2.605),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.07,
						0.03, 3.0, 0.2, 5.182),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.07,
						0.03, 3.0, 0.2, 9.065),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.07,
						0.03, 3.0, 0.2, 14.430),
				new AmericanOptionData(Option.Type.CALL, 100.00, 120.00, 0.07,
						0.03, 3.0, 0.2, 21.398),

				new AmericanOptionData(Option.Type.CALL, 100.00, 80.00, 0.07,
						0.03, 3.0, 0.4, 11.336),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.07,
						0.03, 3.0, 0.4, 15.711),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.07,
						0.03, 3.0, 0.4, 20.760),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.07,
						0.03, 3.0, 0.4, 26.440),
				new AmericanOptionData(Option.Type.CALL, 100.00, 120.00, 0.07,
						0.03, 3.0, 0.4, 32.709),

				// FIXME case of zero interest rates not handled
				// new AmericanOptionData(Option.Type.CALL, 100.00, 80.00, 0.07,
				// 0.0, 3.0, 0.3, 5.552 ),
				// new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.07,
				// 0.0, 3.0, 0.3, 8.868 ),
				// new AmericanOptionData(Option.Type.CALL, 100.00, 100.00,
				// 0.07, 0.0, 3.0, 0.3, 13.158 ),
				// new AmericanOptionData(Option.Type.CALL, 100.00, 110.00,
				// 0.07, 0.0, 3.0, 0.3, 18.458 ),
				// new AmericanOptionData(Option.Type.CALL, 100.00, 120.00,
				// 0.07, 0.0, 3.0, 0.3, 24.786 ),

				new AmericanOptionData(Option.Type.CALL, 100.00, 80.00, 0.03,
						0.07, 3.0, 0.3, 12.177),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.03,
						0.07, 3.0, 0.3, 17.411),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.03,
						0.07, 3.0, 0.3, 23.402),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.03,
						0.07, 3.0, 0.3, 30.028),
				new AmericanOptionData(Option.Type.CALL, 100.00, 120.00, 0.03,
						0.07, 3.0, 0.3, 37.177) };

		logger.info("Testing Ju approximation for American options...");

		final Date today = DateFactory.getFactory().getTodaysDate();
		final DayCounter dc = Actual360.getDayCounter();
		final SimpleQuote spot = new SimpleQuote(0.0);
		final SimpleQuote qRate = new SimpleQuote(0.0);
		final YieldTermStructure qTS = Utilities.flatRate(today,
				new Handle<Quote>(qRate), dc);
		final SimpleQuote rRate = new SimpleQuote(0.0);
		final YieldTermStructure rTS = Utilities.flatRate(today,
				new Handle<Quote>(rRate), dc);
		final SimpleQuote vol = new SimpleQuote(0.0);
		final BlackVolTermStructure volTS = Utilities.flatVol(today,
				new Handle<Quote>(vol), dc);

		final PricingEngine engine = new JuQuadraticApproximationEngine();

		final double tolerance = 1.0e-3;

		for (int i = 0; i < juValues.length; i++) {

			final StrikedTypePayoff payoff = new PlainVanillaPayoff(
					juValues[i].type, juValues[i].strike);

			final Date exDate = today.getDateAfter(timeToDays(juValues[i].t));

			final Exercise exercise = new AmericanExercise(today, exDate);

			spot.setValue(juValues[i].s);
			qRate.setValue(juValues[i].q);
			rRate.setValue(juValues[i].r);
			vol.setValue(juValues[i].v);

			final StochasticProcess stochProcess = new BlackScholesMertonProcess(
					new Handle<Quote>(spot),
					new Handle<YieldTermStructure>(qTS),
					new Handle<YieldTermStructure>(rTS),
					new Handle<BlackVolTermStructure>(volTS));

			final VanillaOption option = new VanillaOption(stochProcess,
					payoff, exercise, engine);

			final double calculated = option.getNPV();

			final double error = Math.abs(calculated - juValues[i].result);
			if (error > tolerance) {
				REPORT_FAILURE("value", payoff, exercise, juValues[i].s,
						juValues[i].q, juValues[i].r, today, juValues[i].v,
						juValues[i].result, calculated, error, tolerance);
			}
		}
	}

    @Ignore("OOPS, Test is failing, need to fix ")
	@Test
	public void testFdValues() {
		logger.info("Testing finite-difference engine for American options...");

		/*
		 * The data below are from An Approximate Formula for Pricing American
		 * Options Journal of Derivatives Winter 1999 Ju, N.
		 */
		final AmericanOptionData juValues[] = {
				// type, strike, spot, q, r, t, vol, value, tol
				// These values are from Exhibit 3 - Short dated Put Options
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.0833, 0.2, 0.006),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.3333, 0.2, 0.201),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.5833, 0.2, 0.433),

				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.0833, 0.2, 0.851),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.3333, 0.2, 1.576),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.5833, 0.2, 1.984),

				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.0833, 0.2, 5.000),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.3333, 0.2, 5.084),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.5833, 0.2, 5.260),

				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.0833, 0.3, 0.078),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.3333, 0.3, 0.697),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.5833, 0.3, 1.218),

				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.0833, 0.3, 1.309),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.3333, 0.3, 2.477),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.5833, 0.3, 3.161),

				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.0833, 0.3, 5.059),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.3333, 0.3, 5.699),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.5833, 0.3, 6.231),

				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.0833, 0.4, 0.247),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.3333, 0.4, 1.344),
				new AmericanOptionData(Option.Type.PUT, 35.00, 40.00, 0.0,
						0.0488, 0.5833, 0.4, 2.150),

				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.0833, 0.4, 1.767),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.3333, 0.4, 3.381),
				new AmericanOptionData(Option.Type.PUT, 40.00, 40.00, 0.0,
						0.0488, 0.5833, 0.4, 4.342),

				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.0833, 0.4, 5.288),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.3333, 0.4, 6.501),
				new AmericanOptionData(Option.Type.PUT, 45.00, 40.00, 0.0,
						0.0488, 0.5833, 0.4, 7.367),

				// Type in Exhibits 4 and 5 if you have some spare time ;-)

				// type, strike, spot, q, r, t, vol, value, tol
				// These values are from Exhibit 6 - Long dated Call Options
				// with dividends
				new AmericanOptionData(Option.Type.CALL, 100.00, 80.00, 0.07,
						0.03, 3.0, 0.2, 2.605),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.07,
						0.03, 3.0, 0.2, 5.182),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.07,
						0.03, 3.0, 0.2, 9.065),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.07,
						0.03, 3.0, 0.2, 14.430),
				new AmericanOptionData(Option.Type.CALL, 100.00, 120.00, 0.07,
						0.03, 3.0, 0.2, 21.398),

				new AmericanOptionData(Option.Type.CALL, 100.00, 80.00, 0.07,
						0.03, 3.0, 0.4, 11.336),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.07,
						0.03, 3.0, 0.4, 15.711),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.07,
						0.03, 3.0, 0.4, 20.760),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.07,
						0.03, 3.0, 0.4, 26.440),
				new AmericanOptionData(Option.Type.CALL, 100.00, 120.00, 0.07,
						0.03, 3.0, 0.4, 32.709),

				// FIXME case of zero interest rates not handled
				// new AmericanOptionData(Option.Type.CALL, 100.00, 80.00, 0.07,
				// 0.0, 3.0, 0.3, 5.552 ),
				// new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.07,
				// 0.0, 3.0, 0.3, 8.868 ),
				// new AmericanOptionData(Option.Type.CALL, 100.00, 100.00,
				// 0.07, 0.0, 3.0, 0.3, 13.158 ),
				// new AmericanOptionData(Option.Type.CALL, 100.00, 110.00,
				// 0.07, 0.0, 3.0, 0.3, 18.458 ),
				// new AmericanOptionData(Option.Type.CALL, 100.00, 120.00,
				// 0.07, 0.0, 3.0, 0.3, 24.786 ),

				new AmericanOptionData(Option.Type.CALL, 100.00, 80.00, 0.03,
						0.07, 3.0, 0.3, 12.177),
				new AmericanOptionData(Option.Type.CALL, 100.00, 90.00, 0.03,
						0.07, 3.0, 0.3, 17.411),
				new AmericanOptionData(Option.Type.CALL, 100.00, 100.00, 0.03,
						0.07, 3.0, 0.3, 23.402),
				new AmericanOptionData(Option.Type.CALL, 100.00, 110.00, 0.03,
						0.07, 3.0, 0.3, 30.028),
				new AmericanOptionData(Option.Type.CALL, 100.00, 120.00, 0.03,
						0.07, 3.0, 0.3, 37.177) };

		Date today = DateFactory.getFactory().getTodaysDate();
		DayCounter dc = Actual360.getDayCounter();

		final SimpleQuote spot = new SimpleQuote(0.0);
		final SimpleQuote qRate = new SimpleQuote(0.0);
		final YieldTermStructure qTS = Utilities.flatRate(today,
				new Handle<Quote>(qRate), dc);
		final SimpleQuote rRate = new SimpleQuote(0.0);
		final YieldTermStructure rTS = Utilities.flatRate(today,
				new Handle<Quote>(rRate), dc);
		final SimpleQuote vol = new SimpleQuote(0.0);
		final BlackVolTermStructure volTS = Utilities.flatVol(today,
				new Handle<Quote>(vol), dc);

		double tolerance = 8.0e-2;

		for (int i = 0; i < juValues.length; i++) {

			final StrikedTypePayoff payoff = new PlainVanillaPayoff(
					juValues[i].type, juValues[i].strike);

			final Date exDate = today.getDateAfter(timeToDays(juValues[i].t));

			final Exercise exercise = new AmericanExercise(today, exDate);

			spot.setValue(juValues[i].s);
			qRate.setValue(juValues[i].q);
			rRate.setValue(juValues[i].r);
			vol.setValue(juValues[i].v);

			final BlackScholesMertonProcess stochProcess = new BlackScholesMertonProcess(
					new Handle<Quote>(spot),
					new Handle<YieldTermStructure>(qTS),
					new Handle<YieldTermStructure>(rTS),
					new Handle<BlackVolTermStructure>(volTS));

			final PricingEngine engine = new FDAmericanEngine(stochProcess,
					100, 100);
			final VanillaOption option = new VanillaOption(stochProcess,
					payoff, exercise, engine);

			final double calculated = option.getNPV();

			final double error = Math.abs(calculated - juValues[i].result);
			if (error > tolerance) {
				REPORT_FAILURE("value", payoff, exercise, juValues[i].s,
						juValues[i].q, juValues[i].r, today, juValues[i].v,
						juValues[i].result, calculated, error, tolerance);
			}

		}
	}

	@Ignore(" test case fails")
	@Test
	public void testFdAmericanGreeks() {
		// SavedSettings backup;

		Map<String, Double> calculated = new HashMap<String, Double>();
		Map<String, Double> expected = new HashMap<String, Double>();
		Map<String, Double> tolerance = new HashMap<String, Double>();
		tolerance.put("delta", 7.0e-4);
		tolerance.put("gamma", 2.0e-4);
		// tolerance["theta"] = 1.0e-4;

		Option.Type types[] = { Option.Type.CALL, Option.Type.PUT };
		double strikes[] = { 50.0, 99.5, 100.0, 100.5, 150.0 };
		double underlyings[] = { 100.0 };
		double qRates[] = { 0.04, 0.05, 0.06 };
		double rRates[] = { 0.01, 0.05, 0.15 };
		int years[] = { 1, 2 };
		double vols[] = { 0.11, 0.50, 1.20 };

		Date today = DateFactory.getFactory().getTodaysDate();
		DayCounter dc = Actual360.getDayCounter();
		Settings settings = Configuration.getSystemConfiguration(null)
				.getGlobalSettings();
		settings.setEvaluationDate(today);

		final SimpleQuote spot = new SimpleQuote(0.0);
		final SimpleQuote qRate = new SimpleQuote(0.0);
		final YieldTermStructure qTS = Utilities.flatRate(today,
				new Handle<Quote>(qRate), dc);
		final SimpleQuote rRate = new SimpleQuote(0.0);
		final YieldTermStructure rTS = Utilities.flatRate(today,
				new Handle<Quote>(rRate), dc);
		final SimpleQuote vol = new SimpleQuote(0.0);
		final BlackVolTermStructure volTS = Utilities.flatVol(today,
				new Handle<Quote>(vol), dc);

		StrikedTypePayoff payoff = null;

		for (int i = 0; i < types.length; i++) {
			for (int j = 0; j < strikes.length; j++) {
				for (int k = 0; k < years.length; k++) {

					Date exDate = today.getDateAfter(new Period(years[k],
							TimeUnit.YEARS));

					Exercise exercise = new AmericanExercise(today, exDate);
					payoff = new PlainVanillaPayoff(types[i], strikes[j]);

					final BlackScholesMertonProcess stochProcess = new BlackScholesMertonProcess(
							new Handle<Quote>(spot),
							new Handle<YieldTermStructure>(qTS),
							new Handle<YieldTermStructure>(rTS),
							new Handle<BlackVolTermStructure>(volTS));

					final PricingEngine engine = new FDAmericanEngine(
							stochProcess);
					final VanillaOption option = new VanillaOption(
							stochProcess, payoff, exercise, engine);

					for (int l = 0; l < underlyings.length; l++) {
						for (int m = 0; m < qRates.length; m++) {
							for (int n = 0; n < rRates.length; n++) {
								for (int p = 0; p < vols.length; p++) {
									double u = underlyings[l];
									double q = qRates[m], r = rRates[n];
									double v = vols[p];
									spot.setValue(u);
									qRate.setValue(q);
									rRate.setValue(r);
									vol.setValue(v);
									// FLOATING_POINT_EXCEPTION
									double value = option.getNPV();
									calculated.put("delta", option.delta());
									calculated.put("gamma", option.gamma());
									// calculated["theta"] = option.theta();

									if (value > spot.evaluate() * 1.0e-5) {
										// perturb spot and get delta and gamma
										double du = u * 1.0e-4;
										spot.setValue(u + du);
										double value_p = option.getNPV(), delta_p = option
												.delta();
										spot.setValue(u - du);
										double value_m = option.getNPV(), delta_m = option
												.delta();
										spot.setValue(u);
										expected.put("delta",
												(value_p - value_m) / (2 * du));
										expected.put("gamma",
												(delta_p - delta_m) / (2 * du));

										/*
										 * // perturb date and get theta Time dT
										 * = dc.yearFraction(today-1, today+1);
										 * Settings
										 * ::instance().setEvaluationDate
										 * (today-1); value_m = option.NPV();
										 * Settings
										 * ::instance().setEvaluationDate
										 * (today+1); value_p = option.NPV();
										 * Settings
										 * ::instance().setEvaluationDate
										 * (today); expected["theta"] = (value_p
										 * - value_m)/dT;
										 */

										// compare
										for (String greek : calculated.keySet()) {
											double expct = expected.get(greek), calcl = calculated
													.get(greek), tol = tolerance
													.get(greek);
											double error = Utilities
													.relativeError(expct,
															calcl, u);
											if (error > tol) {
												REPORT_FAILURE(greek, payoff,
														exercise, u, q, r,
														today, v, expct, calcl,
														error, tol);
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

	@Ignore("Pricer Engine implementation not completed yet.")
	@Test
	public void testFdShoutGreeks() {

	}

	private void REPORT_FAILURE(String greekName, StrikedTypePayoff payoff,
			Exercise exercise, double s, double q, double r, Date today,
			double v, double expected, double calculated, double error,
			double tolerance) {

		TestCase.fail(exercise.type() + " " + payoff.getOptionType()
				+ " option with " + payoff.getClass().getSimpleName()
				+ " payoff:\n" + "    spot value:        " + s + "\n"
				+ "    strike:           " + payoff.getStrike() + "\n"
				+ "    dividend yield:   " + q + "\n"
				+ "    risk-free rate:   " + r + "\n"
				+ "    reference date:   " + today + "\n"
				+ "    maturity:         " + exercise.lastDate() + "\n"
				+ "    volatility:       " + v + "\n\n" + "    expected   "
				+ greekName + ": " + expected + "\n" + "    calculated "
				+ greekName + ": " + calculated + "\n"
				+ "    error:            " + error + "\n"
				+ "    tolerance:        " + tolerance);
	}

	private int timeToDays(/* @Time */double t) {
		return (int) (t * 360 + 0.5);
	}

	//
	// private inner classes
	//

	private class AmericanOptionData {

		private final Option.Type type;
		private final double /* @Real */strike;
		private final double /* @Real */s; // spot
		private final double /* @Rate */q; // dividend
		private final double /* @Rate */r; // risk-free rate
		private final double /* @Time */t; // time to maturity
		private final double /* @Volatility */v; // volatility
		private final double /* @Real */result; // expected result

		public AmericanOptionData(Option.Type type, double strike, double s,
				double q, double r, double t, double v, double result) {
			this.type = type;
			this.strike = strike;
			this.s = s;
			this.q = q;
			this.r = r;
			this.t = t;
			this.v = v;
			this.result = result;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Type: " + type);
			builder.append(" Strike: " + strike);
			builder.append(" Spot: " + s);
			builder.append(" DividendYield: " + q);
			builder.append(" Riskfree: " + r);
			builder.append(" TTm: " + t);
			builder.append(" Vol: " + v);
			builder.append(" result: " + result);

			return builder.toString();
		}
	}

}
