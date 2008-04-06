/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

/*
 Copyright (C) 2003, 2004 StatPro Italia srl

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

package org.jquantlib.util;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.calendars.NullCalendar;

public class Utilities {

//#include "utilities.hpp"
//#include <ql/instruments/payoffs.hpp>
//#include <ql/termstructures/yieldcurves/flatforward.hpp>
//#include <ql/termstructures/volatilities/blackconstantvol.hpp>
//#include <ql/time/calendars/nullcalendar.hpp>
//
//#define CHECK_DOWNCAST(Derived,Description) { \
//    boost::shared_ptr<Derived> hd = boost::dynamic_pointer_cast<Derived>(h); \
//    if (hd) \
//        return Description; \
//}
//
//namespace QuantLib {
//
//    std::string payoffTypeToString(const boost::shared_ptr<Payoff>& h) {
//
//        CHECK_DOWNCAST(PlainVanillaPayoff, "plain-vanilla");
//        CHECK_DOWNCAST(CashOrNothingPayoff, "cash-or-nothing");
//        CHECK_DOWNCAST(AssetOrNothingPayoff, "asset-or-nothing");
//        CHECK_DOWNCAST(SuperSharePayoff, "super-share");
//        CHECK_DOWNCAST(GapPayoff, "gap");
//
//        QL_FAIL("unknown payoff type");
//    }
//
//
//    std::string exerciseTypeToString(const boost::shared_ptr<Exercise>& h) {
//
//        CHECK_DOWNCAST(EuropeanExercise, "European");
//        CHECK_DOWNCAST(AmericanExercise, "American");
//        CHECK_DOWNCAST(BermudanExercise, "Bermudan");
//
//        QL_FAIL("unknown exercise type");
//    }
//

    static public YieldTermStructure flatRate(
    		final Date today,
            final Quote forward,
            final DayCounter dc) {
        return new FlatForward(today, forward, dc);
    }

    static public YieldTermStructure flatRate(
    		final Date today, 
    		final /*@Rate*/ double forward, 
    		final DayCounter dc) {
        return flatRate(today, new SimpleQuote(forward), dc);
    }

    static public YieldTermStructure flatRate(
    		final Quote forward,
    		final DayCounter dc) {
        return new FlatForward(0, new NullCalendar(), forward, dc);
    }

    static public YieldTermStructure flatRate(
    		final /*@Rate*/ double forward, 
    		final DayCounter dc) {
        return flatRate(new SimpleQuote(forward), dc);
    }


    static public BlackVolTermStructure flatVol(
    		final Date today,
            final Quote vol,
            final DayCounter dc) {
        return new BlackConstantVol(today, vol, dc);
    }

    static public BlackVolTermStructure flatVol(
    		final Date today,
    		final /*@Volatility*/ double vol,
            final DayCounter dc) {
        return flatVol(today, new SimpleQuote(vol), dc);
    }

    static public BlackVolTermStructure flatVol(
    		final Quote vol,
            final DayCounter dc) {
        return new BlackConstantVol(0, new NullCalendar(), vol, dc);
    }

    static public BlackVolTermStructure flatVol(
    		final /*@Volatility*/ double vol,
            final DayCounter dc) {
        return flatVol(new SimpleQuote(vol), dc);
    }

//
//    Real relativeError(Real x1, Real x2, Real reference) {
//        if (reference != 0.0)
//            return std::fabs(x1-x2)/reference;
//        else
//            // fall back to absolute error
//            return std::fabs(x1-x2);
//    }
//
//}

}
