/*
 Copyright (C) 2008 Srinivas Hasti
 Copyright (C) 2008 Dominik Holenstein

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

package org.jquantlib.time.calendars;

import static org.jquantlib.time.Month.AUGUST;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.MAY;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 *
 */
public class Switzerland extends DelegateCalendar {
    private final static Switzerland SETTLEMENT_CALENDAR = new Switzerland(Market.Settlement);
    private final static Switzerland SWX_CALENDAR = new Switzerland(Market.SWX);

    private Switzerland(final Market market) {
        Calendar delegate;
        switch (market) {
        case Settlement:
            delegate = new SwisSettlementCalendar();
            break;
        case SWX:
            delegate = new SWXStockExchangeCalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static Switzerland getCalendar(final Market market) {
        switch (market) {
        case Settlement:
            return SETTLEMENT_CALENDAR;
        case SWX:
            return SWX_CALENDAR;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }


    //
    // public enums
    //

    public static enum Market {
        /**
         * generic settlement calendar
         */
        Settlement,

        /**
         * SWX stock-exchange
         */
        SWX,
    }


    //
    // private inner classes
    //

    private static final class SwisSettlementCalendar extends WesternCalendar {

        public String getName() {
            return "Swiss settlement";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth(), dd = date.dayOfYear();
            final Month m = date.month();
            final int y = date.year();
            final int em = easterMonday(y);
            if (isWeekend(w)
                    // New Year's Day
                    || (d == 1 && m == JANUARY)
                    // Berchtolds Day
                    || (d == 2 && m == JANUARY)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // Ascension Thursday
                    || (dd == em + 38)
                    // White Monday
                    || (dd == em + 49)
                    // Labour Day
                    || (d == 1 && m == MAY)
                    // National Day Switzerland
                    || (d == 1 && m == AUGUST)
                    // Christmas Eve
                    || (d == 24 && m == DECEMBER)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // Boxing Day
                    || (d == 26 && m == DECEMBER)
                    // New Year's Eve
                    || (d == 31 && m == DECEMBER))
                return false;
            return true;
        }
    }

    private static final class SWXStockExchangeCalendar extends WesternCalendar {

        public String getName() {
            return "SWX stock exchange";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth(), dd = date.dayOfYear();
            final Month m = date.month();
            final int y = date.year();
            final int em = easterMonday(y);
            if (isWeekend(w)
                    // New Year's Day
                    || (d == 1 && m == JANUARY)
                    // Berchtolds Day
                    || (d == 2 && m == JANUARY)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // Labour Day
                    || (d == 1 && m == MAY)
                    // Ascension Thursday
                    || (dd == em + 38)
                    // White Monday
                    || (dd == em + 49)
                    // National Day Switzerland
                    || (d == 1 && m == AUGUST)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // Christmas Day
                    || (d == 26 && m == DECEMBER))
                return false;
            return true;
        }
    }

}