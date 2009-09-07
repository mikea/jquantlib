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

import org.jquantlib.Configuration;
import org.jquantlib.QL;
import org.jquantlib.SavedSettings;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.daycounters.Thirty360;
import org.jquantlib.instruments.FixedRateBond;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.DateGenerationRule;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.Schedule;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

public class BondTest {

	private final Settings settings;
	private final Date today;

	public BondTest() {
		QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
		this.settings = Configuration.getSystemConfiguration(null).getGlobalSettings();
		this.today = DateFactory.getFactory().getTodaysDate(); // TODO: code
																// review
	}

	private static class CommonVars {
		private Calendar calendar;
		private Date today;
		private/* @Real */double faceAmount;

		// cleanup
		SavedSettings backup = new SavedSettings();

		// setup
		public CommonVars() {
			calendar = org.jquantlib.time.calendars.Target.getCalendar();
			today = calendar.adjust(DateFactory.getFactory().getTodaysDate());
			Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(today);
			faceAmount = 1000000.0;
		}
	}

	@Test
	public void testYield() {
		QL.info("Testing consistency of bond price/yield calculation....");

		CommonVars vars = new CommonVars();

		/* Real */double tolerance = 1.0e-7;
		/* Size */int maxEvaluations = 100;

		int issueMonths[] = { -24, -18, -12, -6, 0, 6, 12, 18, 24 };
		int lengths[] = { 3, 5, 10, 15, 20 };
		/* @Natural */int settlementDays = 3;
		/* @Real */double coupons[] = { 0.02, 0.05, 0.08 };
		Frequency frequencies[] = { Frequency.SEMI_ANNUAL, Frequency.ANNUAL };
		DayCounter bondDayCount = Thirty360.getDayCounter();
		BusinessDayConvention accrualConvention = BusinessDayConvention.UNADJUSTED;
		BusinessDayConvention paymentConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
		/* @Real */double redemption = 100.0;

		/* @Real */double yields[] = { 0.03, 0.04, 0.05, 0.06, 0.07 };
		Compounding compounding[] = { Compounding.COMPOUNDED, Compounding.CONTINUOUS };

		for (/* @Size */int i = 0; i < (issueMonths).length; i++) {
			for (/* @Size */int j = 0; j < (lengths).length; j++) {
				for (/* @Size */int k = 0; k < (coupons).length; k++) {
					for (/* @Size */int l = 0; l < (frequencies).length; l++) {
						for (/* @Size */int n = 0; n < (compounding).length; n++) {
							System.out.println("ok");
							Date dated = vars.calendar.advance(vars.today, issueMonths[i], TimeUnit.MONTHS);
							Date issue = dated;
							Date maturity = vars.calendar.advance(issue, lengths[j], TimeUnit.YEARS);

							Schedule sch = new Schedule(dated,
												    maturity, new
												  Period(frequencies[l]),
												  vars.calendar,
												  accrualConvention,
												  accrualConvention,
												  DateGenerationRule.BACKWARD,
												  false, Date.NULL_DATE, Date.NULL_DATE);
												 

							FixedRateBond bond = new FixedRateBond(settlementDays, vars.faceAmount, sch,
									new double[] { coupons[k] }, bondDayCount, paymentConvention, redemption, issue);

							for (/* @Size */int m = 0; m < (yields).length; m++) {

								/* @Real */double price = bond.cleanPrice(yields[m], bondDayCount, compounding[n], frequencies[l]);
								/* @Real */double calculated = bond.yield(price, bondDayCount, compounding[n], frequencies[l],
										Date.NULL_DATE, tolerance, maxEvaluations);

								if (Math.abs(yields[m] - calculated) > tolerance) {
									// the difference might not matter
									/* @Real */double price2 = bond.cleanPrice(calculated, bondDayCount, compounding[n],
											frequencies[l]);
									if (Math.abs(price - price2) / price > tolerance) {
										QL.error("yield recalculation failed:\n" + "    issue:     " + issue + "\n"
												+ "    maturity:  " + maturity + "\n" + "    coupon:    " + coupons[k] + "\n"
												+ "    frequency: " + frequencies[l] + "\n\n" + "    yield:  " + yields[m] + " "
												+ (compounding[n] == Compounding.CONTINUOUS ? "compounded" : "continuous") + "\n"
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

}
