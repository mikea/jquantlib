/*
 Copyright (C) 2008 Daniel Kong
 
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
package org.jquantlib.instruments;

import java.util.List;

import org.jquantlib.cashflow.Callability;
import org.jquantlib.cashflow.Dividend;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.time.Schedule;
import org.jquantlib.util.Date;

/**
 * convertible fixed-coupon bond
 * 
 * Warning Most methods inherited from Bond (such as yield or
 * the yield-based dirtyPrice and cleanPrice) refer to
 * the underlying plain-vanilla bond and do not take
 * convertibility and callability into account.
 * 
 * @author Daniel Kong
 */
//TODO: Work in progress
public class ConvertibleFixedCouponBond extends ConvertibleBond {

	public ConvertibleFixedCouponBond(final StochasticProcess process,
	          final Exercise exercise,
	          final PricingEngine engine,
	          double conversionRatio,
	          final List<Dividend> dividends,
	          final List<Callability> callability,
	          final Handle<Quote> creditSpread,
	          final Date issueDate,
	          int settlementDays,
	          final DayCounter dayCounter,
	          final Schedule schedule){
		this(process, exercise, engine, conversionRatio, 
				dividends, callability, creditSpread, issueDate, 
				settlementDays, dayCounter, schedule, 100);
	}
	
	public ConvertibleFixedCouponBond(final StochasticProcess process,
			final Exercise exercise,
			final PricingEngine engine,
			double conversionRatio,
			final List<Dividend> dividends,
			final List<Callability> callability,
			final Handle<Quote> creditSpread,
			final Date issueDate,
			int settlementDays,
			final DayCounter dayCounter,
			final Schedule schedule,
			double redemption){
		super(process, exercise, engine, conversionRatio, 
				dividends, callability, creditSpread, issueDate, 
				settlementDays, dayCounter, schedule, redemption);
		//TODO:
	}

}
