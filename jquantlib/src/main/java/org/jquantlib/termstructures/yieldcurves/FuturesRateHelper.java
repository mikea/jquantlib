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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.IMM;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Observable;

/**
 * @author Srinivas Hasti
 *
 */
//TODO: Complete
public class FuturesRateHelper extends RateHelper<YieldTermStructure> {

    private final double yearFraction;
    private Handle<Quote> convAdj;

    public FuturesRateHelper(
            final Handle<Quote> price,
            final Date immDate,
            final int nMonths,
            final Calendar calendar,
            final BusinessDayConvention convention,
            final boolean endOfMonth,
            final DayCounter dayCounter,
            final Handle<Quote> convAdj) {
        super(price, null, null, null);
        QL.require(new IMM().isIMMdate(immDate, false) , "not a valid IMM date"); // QA:[RG]::verified // TODO: message
        earliestDate = immDate;
        latestDate = calendar.advance(
                immDate,
                new Period(nMonths, TimeUnit.Months),
                convention,
                endOfMonth);
        yearFraction = dayCounter.yearFraction(earliestDate, latestDate);

        // registerWith(convAdj_);
    }

    public FuturesRateHelper(
            final double price,
            final Date immDate,
            final int nMonths,
            final Calendar calendar,
            final BusinessDayConvention convention,
            final boolean endOfMonth,
            final DayCounter dayCounter,
            final double conv) {
        super(price);
        QL.require(new IMM().isIMMdate(immDate, false) , "not a valid IMM date"); // QA:[RG]::verified // TODO: message
        convAdj = new Handle<Quote>(new SimpleQuote(conv));
        earliestDate = immDate;
        latestDate = calendar.advance(
                immDate,
                new Period(nMonths, TimeUnit.Months),
                convention,
                endOfMonth);
        yearFraction = dayCounter.yearFraction(earliestDate, latestDate);
    }

    public FuturesRateHelper(
            final double price,
            final Date immDate,
            final IborIndex i,
            final double conv) {
        super(price);
        QL.require(new IMM().isIMMdate(immDate, false) , "not a valid IMM date"); // QA:[RG]::verified // TODO: message
        convAdj = new Handle<Quote>(new SimpleQuote(conv));
        earliestDate = immDate;
        final Calendar cal = i.fixingCalendar();
        latestDate = cal.advance(immDate, i.tenor(), i.businessDayConvention());
        yearFraction = i.dayCounter().yearFraction(earliestDate, latestDate);
    }

    @Override
    public double impliedQuote() {
        QL.require(termStructure!=null , "term structure not set"); // QA:[RG]::verified // TODO: message
        final double forwardRate = termStructure.discount(earliestDate) / (termStructure.discount(latestDate) - 1.0) / yearFraction;
        final double convA = convAdj.empty() ? 0.0 : convAdj.currentLink().value();
        QL.ensure(convA >= 0.0 , "negative futures convexity adjustment"); // QA:[RG]::verified // TODO: message
        final double futureRate = forwardRate + convA;
        return 100.0 * (1.0 - futureRate);
    }

    public double getConvexityAdjustment() {
        return convAdj.empty() ? 0.0 : convAdj.currentLink().value();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jquantlib.util.Observer#update(org.jquantlib.util.Observable,
     *      java.lang.Object)
     */
    @Override
    //TODO: MOVE TO BASE CLASS
    public void update(final Observable o, final Object arg) {
        // TODO Auto-generated method stub

    }

}
