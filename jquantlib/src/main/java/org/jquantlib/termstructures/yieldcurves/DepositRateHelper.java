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

// FIXME: move to org.jquantlib.termstructures.yieldcurves

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;

/**
 * @author Srinivas Hasti
 */
// TODO: code review :: please verify against original QL/C++ code
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class DepositRateHelper<T extends TermStructure> extends
RelativeDateRateHelper<T> {

    private Date fixingDate;
    private final IborIndex iborIndex;
    private RelinkableHandle<YieldTermStructure> termStructureHandle;

    /**
     * 
     * @param rate
     * @param tenor
     * @param fixingDays
     * @param calendar
     * @param convention
     * @param endOfMonth
     * @param dayCounter
     */
    public DepositRateHelper(final Handle<Quote> rate, final Period tenor, final int fixingDays,
            final Calendar calendar, final BusinessDayConvention convention,
            final boolean endOfMonth, final DayCounter dayCounter) {
        super(rate, null, null, null);

        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");

        iborIndex = new IborIndex(
                "no-fix", // never take fixing into account
                tenor, fixingDays, calendar, null, convention, endOfMonth,
                dayCounter, termStructureHandle);
        initializeDates();

    }

    /**
     * 
     * @param rate
     * @param tenor
     * @param fixingDays
     * @param calendar
     * @param convention
     * @param endOfMonth
     * @param dayCounter
     */
    public DepositRateHelper(final double rate, final Period tenor, final int fixingDays,
            final Calendar calendar, final BusinessDayConvention convention,
            final boolean endOfMonth, final DayCounter dayCounter) {
        super(rate);
        iborIndex = new IborIndex(
                "no-fix", // never take fixing into account
                tenor, fixingDays, calendar, null, convention, endOfMonth,
                dayCounter, termStructureHandle);
        initializeDates();
    }

    /**
     * 
     * @param rate
     * @param ibor
     */
    public DepositRateHelper(final Handle<Quote> rate, final IborIndex ibor) {
        super(rate, null, null, null); // TODO
        iborIndex = new IborIndex(
                "no-fix", // never take fixing into account
                ibor.tenor(), ibor.fixingDays(),
                ibor.fixingCalendar(), null, ibor.getConvention(), ibor
                .isEndOfMonth(), ibor.dayCounter(),
                termStructureHandle);
        initializeDates();

    }

    /**
     * 
     * @param rate
     * @param ibor
     */
    public DepositRateHelper(final double rate, final IborIndex ibor) {
        super(rate); // TODO
        iborIndex = new IborIndex(
                "no-fix", // never take fixing into account
                ibor.tenor(), ibor.fixingDays(),
                ibor.fixingCalendar(), null, ibor.getConvention(), ibor
                .isEndOfMonth(), ibor.dayCounter(),
                termStructureHandle);

    }

    /**
     * 
     */
    @Override
    protected void initializeDates() {
        earliestDate = iborIndex.fixingCalendar().advance(evaluationDate,
                iborIndex.fixingDays(), TimeUnit.DAYS);
        latestDate = iborIndex.maturityDate(earliestDate);
        fixingDate = iborIndex.fixingDate(earliestDate);

    }

    /**
     * 
     */
    @Override
    public double impliedQuote() {
        assert termStructure != null : "term structure not set";
        return iborIndex.fixing(fixingDate, true);
    }

    /**
     * 
     * @param termStructureHandle
     */
    public void setTermStructureHandle(final YieldTermStructure term) {
        termStructureHandle.setLink(term);
    }
}
