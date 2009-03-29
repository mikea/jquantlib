/*
 Copyright (C) 2007 Srinivas Hasti

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

package org.jquantlib.indexes;

import java.util.Currency;

import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;

/**
 * Euribor index
 * <p>
 * Euribor rate fixed by the ECB.
 * 
 * @note This is the rate fixed by the ECB. Use EurLibor if you're interested in the London fixing by BBA.
 * 
 * @author Srinivas Hasti
 */
public class Euribor extends IborIndex {

//
// FIXME: these classes were not found on QuantLib 0.8.1 sources    
//    
//    public class DailyTenorEuribor extends IborIndex {
//        public DailyTenorEuribor(int settlementDays,
//                Handle<YieldTermStructure> h) {
//            super("Euribor", new Period(1, TimeUnit.DAYS), settlementDays,
//                    Target.getCalendar(), Currency.getInstance("EUR"),
//                    euriborConvention(new Period(1, TimeUnit.DAYS)),
//                    euriborEOM(new Period(1, TimeUnit.DAYS)), Actual360
//                            .getDayCounter(), h);
//        }
//    }
//    
//    public class DailyTenorEuribor365 extends IborIndex {
//        public DailyTenorEuribor365(int settlementDays,
//                Handle<YieldTermStructure> h) {
//            super("Euribor", new Period(1, TimeUnit.DAYS), settlementDays,
//                    Target.getCalendar(), Currency.getInstance("EUR"),
//                    euriborConvention(new Period(1, TimeUnit.DAYS)),
//                    euriborEOM(new Period(1, TimeUnit.DAYS)), Actual365Fixed.getDayCounter(), h);
//        }
//    }
    

    protected Euribor(String name, Period tenor, int settlementDays, DayCounter dayCounter, Handle<YieldTermStructure> h) {
        super(name, tenor, settlementDays, Target.getCalendar(), Currency.getInstance("EUR"), euriborConvention(tenor),
                euriborEOM(tenor), dayCounter, h);
    }

    public Euribor(Period tenor, Handle<YieldTermStructure> h) {
        this("Euribor", tenor, 2, Actual360.getDayCounter(), h); // settlement days
        if (tenor.units() == TimeUnit.DAYS)
            throw new IllegalArgumentException("for daily tenors dedicated DailyTenor constructor must be used"); // TODO: message
    }

	protected static BusinessDayConvention euriborConvention(Period p) {
		switch (p.units()) {
		case DAYS:
		case WEEKS:
			return BusinessDayConvention.FOLLOWING;
		case MONTHS:
		case YEARS:
			return BusinessDayConvention.MODIFIED_FOLLOWING;
		default:
			throw new IllegalArgumentException("invalid time units"); // TODO: message
		}
	}

	protected static boolean euriborEOM(Period p) {
		switch (p.units()) {
		case DAYS:
		case WEEKS:
			return false;
		case MONTHS:
		case YEARS:
			return true;
		default:
			throw new IllegalArgumentException("invalid time units"); // TODO: message
		}
	}

}
