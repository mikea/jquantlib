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

import org.jquantlib.QL;
import org.jquantlib.currencies.Europe.EURCurrency;
import org.jquantlib.daycounters.Actual360;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
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
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
// TODO: code review :: Please review this class! :S
public class Euribor extends IborIndex {


    public Euribor(final Period tenor, final Handle<YieldTermStructure> h) {
        super("Euribor",
                tenor,
                2, // settlement days
                new EURCurrency(),
                new Target(),
                euriborConvention(tenor),
                euriborEOM(tenor),
                Actual360.getDayCounter(),
                h);
        QL.require(tenor().units() != TimeUnit.DAYS , "for daily tenors dedicated DailyTenor constructor must be used"); // QA:[RG]::verified // TODO: message
    }



    public static Euribor getEuribor1W(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(1, TimeUnit.WEEKS), h);
    }

    public static Euribor getEuribor2W(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(2, TimeUnit.WEEKS), h);
    }

    public static Euribor getEuribor3W(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(3, TimeUnit.WEEKS), h);
    }

    public static Euribor getEuribor1M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(1, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor2M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(1, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor3M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(3, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor4M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(4, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor5M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(5, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor6M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(6, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor7M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(7, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor8M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(8, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor9M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(9, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor10M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(10, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor11M(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(11, TimeUnit.MONTHS), h);
    }

    public static Euribor getEuribor1Y(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(1, TimeUnit.YEARS), h);
    }

    public static Euribor getEuribor_SW(final Handle<YieldTermStructure> h) {
        return new Euribor(new Period(1, TimeUnit.WEEKS), h);
    }

}
