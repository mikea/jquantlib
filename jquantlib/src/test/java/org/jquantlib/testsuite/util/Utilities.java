/*
 Copyright (C) 2007 Richard Gomes

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

// TODO: class comments
public class Utilities {

    static public double relativeError(final double x1, final double x2, final double reference) {
        if (reference != 0.0)
            return Math.abs(x1 - x2) / reference;
        else
            // fall back to absolute error
            return Math.abs(x1 - x2);
    }

    static public YieldTermStructure flatRate(final Date today, final Handle<? extends Quote> forward, final DayCounter dc) {
        return new FlatForward(today, forward, dc);
    }

    static public YieldTermStructure flatRate(final Date today, final/* @Rate */double forward, final DayCounter dc) {
        return flatRate(today, new Handle<Quote>(new SimpleQuote(forward)), dc);
    }

    static public YieldTermStructure flatRate(final Handle<? extends Quote> forward, final DayCounter dc) {
        return new FlatForward(0, new NullCalendar(), forward, dc);
    }

    static public YieldTermStructure flatRate(final/* @Rate */double forward, final DayCounter dc) {
        return flatRate(new Handle<Quote>(new SimpleQuote(forward)), dc);
    }

    static public BlackVolTermStructure flatVol(final Date today, final Handle<? extends Quote> vol, final DayCounter dc) {
        return new BlackConstantVol(today, vol, dc);
    }

    static public BlackVolTermStructure flatVol(final Date today, final/* @Volatility */double vol, final DayCounter dc) {
        return flatVol(today, new Handle<Quote>(new SimpleQuote(vol)), dc);
    }

    static public BlackVolTermStructure flatVol(final Handle<? extends Quote> vol, final DayCounter dc) {
        return new BlackConstantVol(0, new NullCalendar(), vol, dc);
    }

    static public BlackVolTermStructure flatVol(final/* @Volatility */double vol, final DayCounter dc) {
        return flatVol(new Handle<Quote>(new SimpleQuote(vol)), dc);
    }

}
