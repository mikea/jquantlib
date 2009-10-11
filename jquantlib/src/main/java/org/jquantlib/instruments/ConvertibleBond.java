/*
 Copyright (C) 2009 Daniel Kong

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
import org.jquantlib.time.Date;
import org.jquantlib.time.Schedule;

/**
 * Base class for convertible bonds
 *
 * @author Daniel Kong
 */
//TODO: Work in progress
public class ConvertibleBond extends Bond {

    protected double conversionRatio;
    protected List<Dividend> dividends;
    protected List<Callability> callability;
    protected Handle<Quote> creditSpread;
    protected Option option;

    public ConvertibleBond(final StochasticProcess process,
            final Exercise exercise,
            final PricingEngine engine,
            final double conversionRatio,
            final List<Dividend> dividends,
            final List<Callability> callability,
            final Handle<Quote> creditSpread,
            final Date issueDate,
            final int settlementDays,
            final DayCounter dayCounter,
            final Schedule schedule,
            final double redemption){
        super(settlementDays, 100.0, schedule.calendar(), dayCounter, schedule.businessDayConvention());
        this.conversionRatio = conversionRatio;
        this.dividends = dividends;
        this.callability = callability;
        this.creditSpread = creditSpread;

        this.issueDate_ = issueDate;
        this.datedDate = schedule.date(0);
        this.maturityDate_ = schedule.date(schedule.size()-1);
        this.frequency = schedule.tenor().frequency();

        setPricingEngine(engine);

        // TODO: code review :: please verify against QL/C++ code
        // seems like we should have this.process and this.creditSpread

        process.addObserver(this);
        creditSpread.addObserver(this);
        //XXX:registerWith
        //registerWith(process);
        //registerWith(creditSpread);

    }

    public double getConversionRatio() {
        return conversionRatio;
    }

    public List<Dividend> getDividents(){
        return dividends;
    }

    public List<Callability> getCallability(){
        return callability;
    }

    public Handle<Quote> getCreditSpread(){
        return creditSpread;
    }

    @Override
    protected void performCalculations(){
        option.setPricingEngine(engine);
        NPV = option.getNPV();
        errorEstimate = 0.0;
    }

}
