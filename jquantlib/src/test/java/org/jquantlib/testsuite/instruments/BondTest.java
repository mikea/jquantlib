/*
 Copyright (C) 2008 Richard Gomes
 Copyright (C) 2009 John Nichol

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

import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.daycounters.Thirty360;
import org.jquantlib.instruments.FixedRateBond;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.bond.DiscountingBondEngine;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.testsuite.util.Utilities;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.DateGeneration;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.Schedule;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.DateGeneration.Rule;
import org.junit.Test;

public class BondTest {

	public BondTest() {
		QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
	}

	private static class CommonVars {
		private final Calendar calendar;
		private final Date today;
		private final double faceAmount;


		// FIXME: code review :: class SavedSettings was entirely commented out!


		// cleanup
		//-- SavedSettings backup = new SavedSettings();

		// setup
		public CommonVars() {
			calendar = new org.jquantlib.time.calendars.Target();
			today = calendar.adjust(Date.todaysDate());
			new Settings().setEvaluationDate(today);
			faceAmount = 1000000.0;
		}
	}

	@Test
	public void testYield() {
		QL.info("Testing consistency of bond price/yield calculation....");

		final CommonVars vars = new CommonVars();

		final double tolerance = 1.0e-7;
		final int maxEvaluations = 100;

		final int issueMonths[] = { -24, -18, -12, -6, 0, 6, 12, 18, 24 };
		final int lengths[] = { 3, 5, 10, 15, 20 };
		final int settlementDays = 3;
		final double coupons[] = { 0.02, 0.05, 0.08 };
		final Frequency frequencies[] = { Frequency.Semiannual, Frequency.Annual };
		final DayCounter bondDayCount = new Thirty360();
		final BusinessDayConvention accrualConvention = BusinessDayConvention.Unadjusted;
		final BusinessDayConvention paymentConvention = BusinessDayConvention.ModifiedFollowing;
		final double redemption = 100.0;

		final double yields[] = { 0.03, 0.04, 0.05, 0.06, 0.07 };
		final Compounding compounding[] = { Compounding.Compounded, Compounding.Continuous };

		for (int i = 0; i < (issueMonths).length; i++) {
			for (int j = 0; j < (lengths).length; j++) {
				for (int k = 0; k < (coupons).length; k++) {
					for (int l = 0; l < (frequencies).length; l++) {
						for (int n = 0; n < (compounding).length; n++) {
							final Date dated = vars.calendar.advance(vars.today, issueMonths[i], TimeUnit.Months);
							final Date issue = dated;
							final Date maturity = vars.calendar.advance(issue, lengths[j], TimeUnit.Years);

							final Schedule sch = new Schedule(dated,
									maturity, new
									Period(frequencies[l]),
									vars.calendar,
									accrualConvention,
									accrualConvention,
									DateGeneration.Rule.Backward,
									false, new Date(), new Date());


							final FixedRateBond bond = new FixedRateBond(settlementDays, vars.faceAmount, sch,
									new double[] { coupons[k] }, bondDayCount, paymentConvention, redemption, issue);

							for (int m = 0; m < (yields).length; m++) {

								final double price = bond.cleanPrice(
										yields[m], bondDayCount, compounding[n], frequencies[l]);
								final double calculated = bond.yield(
										price, bondDayCount, compounding[n], frequencies[l], new Date(), tolerance, maxEvaluations);

								if (Math.abs(yields[m] - calculated) > tolerance) {
									// the difference might not matter
									final double price2 = bond.cleanPrice(calculated, bondDayCount, compounding[n],
											frequencies[l]);
									if (Math.abs(price - price2) / price > tolerance) {
										QL.error("yield recalculation failed:\n" + "    issue:     " + issue + "\n"
												+ "    maturity:  " + maturity + "\n" + "    coupon:    " + coupons[k] + "\n"
												+ "    frequency: " + frequencies[l] + "\n\n" + "    yield:  " + yields[m] + " "
												+ (compounding[n] == Compounding.Continuous ? "compounded" : "continuous") + "\n"
												+ "    price:  " + price + "\n" + "    yield': " + (calculated) + "\n"
												+ "    price': " + price2);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Test
	public void testTheoretical() {
		QL.info("Testing theoretical bond price/yield calculation...");

		final CommonVars vars = new CommonVars();

		final double tolerance = 1.0e-7;
		final int maxEvaluations = 100;

		final int lengths[] = { 3, 5, 10, 15, 20 };
		final int settlementDays = 3;
		final double coupons[] = { 0.02, 0.05, 0.08 };
		final Frequency frequencies[] = { Frequency.Semiannual, Frequency.Annual };
		final DayCounter bondDayCount = new Actual360();
		final BusinessDayConvention accrualConvention = BusinessDayConvention.Unadjusted;
		final BusinessDayConvention paymentConvention = BusinessDayConvention.ModifiedFollowing;
		final double redemption = 100.0;

		final double yields[] = { 0.03, 0.04, 0.05, 0.06, 0.07 };

		for (final int length : lengths) {
			for (final double coupon : coupons) {
				for (final Frequency frequency : frequencies) {

					final Date dated = vars.today;
					final Date issue = dated;
					final Date maturity = vars.calendar.advance(issue, length, TimeUnit.Years);

					final SimpleQuote rate = new SimpleQuote(0.0);
					final Handle<YieldTermStructure> discountCurve = new Handle<YieldTermStructure>(Utilities.flatRate(vars.today, rate, bondDayCount));

					final Schedule sch = new Schedule(
					        dated, maturity,
							new Period(frequency), vars.calendar,
							accrualConvention, accrualConvention,
							Rule.Backward, false);

					final FixedRateBond bond = new FixedRateBond(
					        settlementDays, vars.faceAmount, sch,
							new double[] { coupon },
							bondDayCount, paymentConvention,
							redemption, issue);

					final PricingEngine bondEngine = new DiscountingBondEngine(discountCurve);
					bond.setPricingEngine(bondEngine);

					for (final double yield : yields) {

						rate.setValue(yield);

						final double price = bond.cleanPrice(yield, bondDayCount, Compounding.Continuous, frequency);
						final double calculatedPrice = bond.getCleanPrice();

						if (Math.abs(price-calculatedPrice) > tolerance) {
							QL.error(
									"price calculation failed:"
									+ "\n    issue:     " + issue
									+ "\n    maturity:  " + maturity
									+ "\n    coupon:    " + coupon
									+ "\n    frequency: " + frequency + "\n"
									+ "\n    yield:  " + yield
									+ "\n    expected:    " + price
									+ "\n    calculated': " + calculatedPrice
									+ "\n    error':      " + (price-calculatedPrice));
						}

						final double calculatedYield = bond.yield(
								bondDayCount, Compounding.Continuous, frequency,
								tolerance, maxEvaluations);
						if (Math.abs(yield-calculatedYield) > tolerance) {
							QL.error(
									"yield calculation failed:"
									+ "\n    issue:     " + issue
									+ "\n    maturity:  " + maturity
									+ "\n    coupon:    " + coupon
									+ "\n    frequency: " + frequency + "\n"
									+ "\n    yield:  " + yield
									+ "\n    price:    " + price
									+ "\n    yield': " + calculatedYield);
						}
					}
				}
			}
		}
	}
}
