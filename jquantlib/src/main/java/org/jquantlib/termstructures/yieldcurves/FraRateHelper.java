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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * @author Srinivas Hasti
 *
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
// TODO: code review :: please verify against QL/C++ code
public class FraRateHelper extends RelativeDateRateHelper {

    private Date fixingDate;
    private final int monthsToStart;
    private final IborIndex iborIndex;
    private RelinkableHandle<YieldTermStructure> termStructureHandle;

    public FraRateHelper(
            final Handle<Quote> rate,
            final int monthsToStart,
            final int monthsToEnd,
            final int fixingDays,
            final Calendar calendar,
            final BusinessDayConvention convention,
            final boolean endOfMonth,
            final DayCounter dayCounter) {
        QL.require(monthsToEnd > monthsToStart , "monthsToEnd must be greater than monthsToStart"); // QA:[RG]::verified // TODO: message
        this.quote = rate;
        this.monthsToStart = monthsToStart;
        iborIndex = new IborIndex(
                "no-fix",
                new Period(monthsToEnd - monthsToStart, TimeUnit.MONTHS),
                fixingDays,
                null,
                calendar,
                convention,
                endOfMonth,
                dayCounter,
                termStructureHandle);
        initializeDates();

    }

    public FraRateHelper(final double rate, final int monthsToStart, final int monthsToEnd,
            final int fixingDays, final Calendar calendar,
            final BusinessDayConvention convention, final boolean endOfMonth,
            final DayCounter dayCounter) {
        super(rate);
        QL.require(monthsToEnd > monthsToStart , "monthsToEnd must be greater than monthsToStart"); // QA:[RG]::verified // TODO: message
        this.monthsToStart = monthsToStart;
        iborIndex = new IborIndex(
                "no-fix", // never take fixing into account
                new Period(monthsToEnd - monthsToStart, TimeUnit.MONTHS),
                fixingDays,
                null,
                calendar,
                convention,
                endOfMonth,
                dayCounter,
                termStructureHandle);
        initializeDates();
    }

    public FraRateHelper(final Handle<Quote> rate, final int monthsToStart, final IborIndex i) {
        this.quote = rate;
        this.monthsToStart = monthsToStart;
        iborIndex = new IborIndex(
                "no-fix", // never take fixing into account
                i.tenor(), i.fixingDays(), null, i.fixingCalendar(), i
                .getConvention(), i.isEndOfMonth(), i.dayCounter(),
                termStructureHandle);
        initializeDates();

    }

    public FraRateHelper(final double rate, final int monthsToStart, final IborIndex i) {
        super(rate);
        this.monthsToStart = monthsToStart;
        iborIndex = new IborIndex(
                "no-fix", // never take fixing into account
                i.tenor(), i.fixingDays(), null, i.fixingCalendar(), i
                .getConvention(), i.isEndOfMonth(), i.dayCounter(),
                termStructureHandle);
        initializeDates();
    }

    @Override
    public double impliedQuote()  {
        QL.require(termStructure != null , "term structure not set"); // QA:[RG]::verified // TODO: message
        return iborIndex.fixing(fixingDate, true);
    }

    public void setTermStructure(final YieldTermStructure t) {
        // no need to register---the index is not lazy
        termStructureHandle.setLink(t);
        super.setTermStructure(t);
    }

    @Override
    protected void initializeDates() {
        final Date settlement = iborIndex.fixingCalendar().advance(
                evaluationDate, new Period(iborIndex.fixingDays(),TimeUnit.DAYS), BusinessDayConvention.FOLLOWING, false);
        earliestDate = iborIndex.fixingCalendar().advance(
                settlement,
                new Period(monthsToStart,TimeUnit.MONTHS),
                iborIndex.getConvention(),
                iborIndex.isEndOfMonth());
        latestDate = iborIndex.maturityDate(earliestDate);
        fixingDate = iborIndex.fixingDate(earliestDate);
    }
}
