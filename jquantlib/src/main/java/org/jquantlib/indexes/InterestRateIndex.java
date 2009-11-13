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
import org.jquantlib.Settings;
import org.jquantlib.currencies.Currency;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

/**
 *
 * @author Srinivas Hasti
 *
 */
// TODO: code review :: please verify against QL/C++ code
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public abstract class InterestRateIndex extends Index implements Observer {

    private final String familyName;
    private final Period tenor;
    private final int fixingDays;
    private final Calendar fixingCalendar;
    private final Currency currency;

    // TODO: code review :: please verify against QL/C++ code
    protected DayCounter dayCounter;


    public InterestRateIndex(
            final String familyName,
            final Period tenor,
            final /*@Natural*/ int fixingDays,
            final Calendar fixingCalendar,
            final Currency currency,
            final DayCounter dayCounter) {

        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        this.familyName = familyName;
        this.tenor = tenor;
        this.fixingDays = fixingDays;
        this.fixingCalendar = fixingCalendar;
        this.currency = currency;
        this.dayCounter = dayCounter;

        QL.require(fixingDays >= 2 , "wrong number of fixing days");  // QA:[RG]::verified // TODO: message

        // TODO: code review :: please verify against QL/C++ code
        tenor.normalize();

        final Date evaluationDate = new Settings().evaluationDate();

        // TODO: code review :: please verify against QL/C++ code
        // 1. seems like we should have this.evaluationDate
        // 2. we should get rid of static calls and singletons

        evaluationDate.addObserver(this);
        IndexManager.getInstance().notifier(name()).addObserver(this);
        //XXX:registerWith
        //registerWith(evaluationDate);
        //registerWith(IndexManager.getInstance().notifier(name()));
    }


    //adoption for 0.9.7 switched parameters...
    public InterestRateIndex(
            final String familyName,
            final Period tenor,
            /*@Natural*/ final int settlementDays,
            final Currency currency,
            final Calendar calendar,
            final DayCounter fixedLegDayCounter) {
        this(familyName, tenor, settlementDays, calendar, currency, fixedLegDayCounter);
    }


    //
    // protected abstract methods
    //

    protected abstract double forecastFixing(Date fixingDate);


    //
    // public abstract methods
    //

    public abstract Handle<YieldTermStructure> termStructure();
    public abstract Date maturityDate(Date valueDate);


    //
    // public methods
    //

    @Override
    // TODO: code review :: please verify against QL/C++ code
    public double fixing(final Date fixingDate, final boolean forecastTodaysFixing) {
        // TODO: code review :: please verify against QL/C++ code
        QL.require(isValidFixingDate(fixingDate) , "Fixing date is not valid"); // QA:[RG]::verified // TODO: message
        final Date today = new Settings().evaluationDate();
        final boolean enforceTodaysHistoricFixings = new Settings().isEnforcesTodaysHistoricFixings();
        if (fixingDate.le(today) || (fixingDate.equals(today) && enforceTodaysHistoricFixings && !forecastTodaysFixing)) {
            // must have been fixed
            final Double pastFixing = IndexManager.getInstance().get(name()).find(fixingDate);
            QL.require(pastFixing != null , "Missing fixing for " + fixingDate); // QA:[RG]::verified // TODO: message
            return pastFixing;
        }
        if ((fixingDate.equals(today)) && !forecastTodaysFixing) {
            // might have been fixed
            try {
                final Double pastFixing = IndexManager.getInstance().get(name()).find(fixingDate);
                if (pastFixing != null) {
                    return pastFixing;
                } else {
                    ; // fall through and forecast
                }
            } catch (final Exception e) {
                ; // fall through and forecast
            }
        }
        // forecast
        return forecastFixing(fixingDate);
    }

    @Override
    public double fixing(final Date fixingDate) {
        return fixing(fixingDate, false);
    }

    @Override
    public String name() {
        final StringBuilder builder = new StringBuilder(familyName);
        if (tenor.units() == TimeUnit.Days) {
            if (fixingDays == 0) {
                builder.append("ON");
            } else if (fixingDays == 2) {
                builder.append("SN");
            } else {
                builder.append("TN");
            }
        } else {
            builder.append(tenor.getShortFormat());
        }
        builder.append(dayCounter.name());
        return builder.toString();
    }

    public Date fixingDate(final Date valueDate) {
        final Date fixingDate = fixingCalendar().advance(valueDate, (fixingDays), TimeUnit.Days);
        QL.ensure(isValidFixingDate(fixingDate) , "fixing date is not valid"); // TODO: message
        return fixingDate;
    }

    @Override
    public boolean isValidFixingDate(final Date fixingDate) {
        return fixingCalendar.isBusinessDay(fixingDate);
    }

    public String familyName() {
        return familyName;
    }

    public Period tenor() {
        return tenor;
    }

    public int fixingDays() {
        return fixingDays;
    }

    @Override
    public Calendar fixingCalendar() {
        return fixingCalendar;
    }

    public Currency currency() {
        return currency;
    }

    public DayCounter dayCounter() {
        return dayCounter;
    }

    public Date valueDate(final Date fixingDate) {
        QL.require(isValidFixingDate(fixingDate) , "Fixing date is not valid"); // QA:[RG]::verified // TODO: message
        return fixingCalendar().advance(fixingDate, fixingDays, TimeUnit.Days);
    }


    //
    // implements Observer
    //

    //XXX:registerWith
    //    @Override
    //    public void registerWith(final Observable o) {
    //        o.addObserver(this);
    //    }
    //
    //    @Override
    //    public void unregisterWith(final Observable o) {
    //        o.deleteObserver(this);
    //    }

    @Override
    public void update(final Observable o, final Object arg) {
        notifyObservers(arg);
    }


}
