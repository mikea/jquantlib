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
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BootstrapHelper;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * @author Srinivas Hasti
 *
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
// TODO: code review :: please verify against QL/C++ code
public class FraRateHelper extends RelativeDateRateHelper {

    private Date fixingDate;

    // this is not a quantlib 0.9.7 variable
    private final int monthsToStart;

    private final IborIndex iborIndex;

    private final RelinkableHandle<YieldTermStructure> termStructureHandle = new RelinkableHandle <YieldTermStructure> (null);

    public FraRateHelper(
            final Handle<Quote> rate,
            final int monthsToStart,
            final int monthsToEnd,
            final int fixingDays,
            final Calendar calendar,
            final BusinessDayConvention convention,
            final boolean endOfMonth,
            final DayCounter dayCounter) {
        super(rate);
        QL.validateExperimentalMode();

        QL.require(monthsToEnd > monthsToStart , "monthsToEnd must be greater than monthsToStart"); // QA:[RG]::verified // TODO: message
        this.quote = rate;
        this.monthsToStart = monthsToStart;
        //never take fixing into account
        iborIndex = new IborIndex("no-fix",
                new Period(monthsToEnd - monthsToStart, TimeUnit.Months),
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
        QL.validateExperimentalMode();

        QL.require(monthsToEnd > monthsToStart , "monthsToEnd must be greater than monthsToStart");
        this.monthsToStart = monthsToStart;
        iborIndex = new IborIndex(
                "no-fix", // never take fixing into account
                new Period(monthsToEnd - monthsToStart, TimeUnit.Months),
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
        super(rate);
        QL.validateExperimentalMode();

        this.quote = rate;
        this.monthsToStart = monthsToStart;
        iborIndex = new IborIndex(
                "no-fix", // never take fixing into account
                i.tenor(), i.fixingDays(), null, i.fixingCalendar(), i.businessDayConvention(),
                i.endOfMonth(), i.dayCounter(), termStructureHandle);
        initializeDates();

    }

    public FraRateHelper(final double rate, final int monthsToStart, final IborIndex i) {
        this (new Handle <Quote> (new SimpleQuote (rate)), monthsToStart, i);
        QL.validateExperimentalMode();
    }


    //
    // public methods
    //

    @Override
    public double impliedQuote()  {
        QL.require(termStructure != null , "term structure not set");
        return iborIndex.fixing(fixingDate, true);
    }

    @Override
    public void setTermStructure(final YieldTermStructure t) {
        // no need to register---the index is not lazy
        termStructureHandle.linkTo(t, false);
        super.setTermStructure(t);
    }

    @Override
    protected void initializeDates() {

        final Date settlement = iborIndex.fixingCalendar().advance(evaluationDate,
           new Period(iborIndex.fixingDays(),TimeUnit.Days), BusinessDayConvention.Following, false);
        earliestDate = iborIndex.fixingCalendar().advance(
                settlement,
                new Period(monthsToStart,TimeUnit.Months),
                iborIndex.businessDayConvention(),
                iborIndex.endOfMonth());
        latestDate = iborIndex.maturityDate(earliestDate);
        fixingDate = iborIndex.fixingDate(earliestDate);
    }


    //
    // implements TypedVisitable
    //

    @Override
    public void accept(final TypedVisitor<BootstrapHelper> v) {
        final Visitor<BootstrapHelper> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
        if (v1 != null) {
            v1.visit(this);
        } else {
            super.accept(v);
        }
    }

}
