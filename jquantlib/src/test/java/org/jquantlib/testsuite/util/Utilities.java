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

package org.jquantlib.testsuite.util;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.util.Date;

public class Utilities {

	//
	// utilities.hpp
	//

	
//    std::string payoffTypeToString(const boost::shared_ptr<Payoff>&);
//    std::string exerciseTypeToString(const boost::shared_ptr<Exercise>&);
//
//
//    boost::shared_ptr<YieldTermStructure>
//    flatRate(const Date& today,
//             const boost::shared_ptr<Quote>& forward,
//             const DayCounter& dc);
//
//    boost::shared_ptr<YieldTermStructure>
//    flatRate(const Date& today,
//             Rate forward,
//             const DayCounter& dc);
//
//    boost::shared_ptr<YieldTermStructure>
//    flatRate(const boost::shared_ptr<Quote>& forward,
//             const DayCounter& dc);
//
//    boost::shared_ptr<YieldTermStructure>
//    flatRate(Rate forward,
//             const DayCounter& dc);
//
//
//    boost::shared_ptr<BlackVolTermStructure>
//    flatVol(const Date& today,
//            const boost::shared_ptr<Quote>& volatility,
//            const DayCounter& dc);
//
//    boost::shared_ptr<BlackVolTermStructure>
//    flatVol(const Date& today,
//            Volatility volatility,
//            const DayCounter& dc);
//
//    boost::shared_ptr<BlackVolTermStructure>
//    flatVol(const boost::shared_ptr<Quote>& volatility,
//            const DayCounter& dc);
//
//    boost::shared_ptr<BlackVolTermStructure>
//    flatVol(Volatility volatility,
//            const DayCounter& dc);
//
//
//    Real relativeError(Real x1, Real x2, Real reference);
//
//    //bool checkAbsError(Real x1, Real x2, Real tolerance){
//    //    return std::fabs(x1 - x2) < tolerance;
//    //};

    
	static public YieldTermStructure flatRate(
	    		final Date today,
	            final Handle<? extends Quote> forward,
	            final DayCounter dc) {
	        return new FlatForward(today, forward, dc);
	    }

	    static public YieldTermStructure flatRate(
	    		final Date today, 
	    		final /*@Rate*/ double forward, 
	    		final DayCounter dc) {
	        return flatRate(today, new Handle<Quote>(new SimpleQuote(forward)), dc);
	    }

	    static public YieldTermStructure flatRate(
	    		final Handle<? extends Quote> forward,
	    		final DayCounter dc) {
	        return new FlatForward(0, new NullCalendar(), forward, dc);
	    }

	    static public YieldTermStructure flatRate(
	    		final /*@Rate*/ double forward, 
	    		final DayCounter dc) {
	        return flatRate(new Handle<Quote>(new SimpleQuote(forward)), dc);
	    }


	    static public BlackVolTermStructure flatVol(
	    		final Date today,
	            final Handle<? extends Quote> vol,
	            final DayCounter dc) {
	        return new BlackConstantVol(today, vol, dc);
	    }

	    static public BlackVolTermStructure flatVol(
	    		final Date today,
	    		final /*@Volatility*/ double vol,
	            final DayCounter dc) {
	        return flatVol(today, new Handle<Quote>(new SimpleQuote(vol)), dc);
	    }

	    static public BlackVolTermStructure flatVol(
	    		final Handle<? extends Quote> vol,
	            final DayCounter dc) {
	        return new BlackConstantVol(0, new NullCalendar(), vol, dc);
	    }

	    static public BlackVolTermStructure flatVol(
	    		final /*@Volatility*/ double vol,
	            final DayCounter dc) {
	        return flatVol(new Handle<Quote>(new SimpleQuote(vol)), dc);
	    }

	//
//	    Real relativeError(Real x1, Real x2, Real reference) {
//	        if (reference != 0.0)
//	            return std::fabs(x1-x2)/reference;
//	        else
//	            // fall back to absolute error
//	            return std::fabs(x1-x2);
//	    }
	//
	//}

}
