/*
 Copyright (C) 2008 Tim Swetonic

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

import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.AUGUST;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.OCTOBER;
import static org.jquantlib.time.Weekday.MONDAY;
import static org.jquantlib.time.Weekday.TUESDAY;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;

/**
 * Australian calendar
 * <p>
 * Holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Australia Day, January 26th (possibly moved to MONDAY)</li>
 * <li>Good Friday</li>
 * <li>Easter MONDAY</li>
 * <li>ANZAC Day. April 25th (possibly moved to MONDAY)</li>
 * <li>Queen's Birthday, second MONDAY in June</li>
 * <li>Bank Holiday, first MONDAY in August</li>
 * <li>Labour Day, first MONDAY in October</li>
 * <li>Christmas, December 25th (possibly moved to MONDAY or TUESDAY)</li>
 * <li>Boxing Day, December 26th (possibly moved to MONDAY or TUESDAY)</li>
 * </ul>
 * @author Tim Swetonic
 * @author Richard Gomes
 */
public class Australia extends DelegateCalendar {

    private final static Australia SETTLEMENT_CALENDAR = new Australia(Market.SETTLEMENT);
    private final static Australia EXCHANGE_CALENDAR   = new Australia(Market.ASX);

    private Australia(final Market market) {
        Calendar delegate;
        switch (market) {
        case SETTLEMENT:
            delegate = new AustraliaSettlementCalendar();
            break;
        case ASX:
            delegate = new AustraliaExchangeCalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static Australia getCalendar(final Market market) {
        switch (market) {
        case SETTLEMENT:
            return SETTLEMENT_CALENDAR;
        case ASX:
            return EXCHANGE_CALENDAR;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }


    //
    // public enums
    //

    public static enum Market {
        /**
         * Australian settlement calendar
         */
        SETTLEMENT,

        /**
         * Australian Stock Exchange
         */
        ASX
    }


    //
    // private inner classes
    //

    private static final class AustraliaSettlementCalendar extends WesternCalendar {

        public String getName() {
            return "Australia";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth();
            final Month m = date.month();
            final int y = date.year();
            final int dd = date.dayOfYear();
            final int em = easterMonday(y);

            if (isWeekend(w)
                    // New Year's Day (possibly moved to MONDAY)
                    || (d == 1  && m == JANUARY)
                    // Australia Day, JANUARY 26th (possibly moved to MONDAY)
                    || ((d == 26 || ((d == 27 || d == 28) && w == MONDAY)) && m == JANUARY)
                    // Good Friday
                    || (dd == em-3)
                    // Easter MONDAY
                    || (dd == em)
                    // ANZAC Day, April 25th (possibly moved to MONDAY)
                    || ((d == 25 || (d == 26 && w == MONDAY)) && m == APRIL)
                    // Queen's Birthday, second MONDAY in June
                    || ((d > 7 && d <= 14) && w == MONDAY && m == JUNE)
                    // Bank Holiday, first MONDAY in August
                    || (d <= 7 && w == MONDAY && m == AUGUST)
                    // Labour Day, first MONDAY in October
                    || (d <= 7 && w == MONDAY && m == OCTOBER)
                    // Christmas, December 25th (possibly MONDAY or TUESDAY)
                    || ((d == 25 || (d == 27 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER)
                    // Boxing Day, December 26th (possibly MONDAY or TUESDAY)
                    || ((d == 26 || (d == 28 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER))
                return false;
            return true;
        }

    }

    private static final class AustraliaExchangeCalendar extends WesternCalendar {

        public String getName() {
            return "ASX";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth();
            final Month m = date.month();
            final int y = date.year();
            final int dd = date.dayOfYear();
            final int em = easterMonday(y);

            if (isWeekend(w)
                    // New Year's Day (possibly moved to MONDAY)
                    || (d == 1  && m == JANUARY)
                    // Australia Day, JANUARY 26th (possibly moved to MONDAY)
                    || ((d == 26 || ((d == 27 || d == 28) && w == MONDAY)) && m == JANUARY)
                    // Good Friday
                    || (dd == em-3)
                    // Easter MONDAY
                    || (dd == em)
                    // ANZAC Day, April 25th (possibly moved to MONDAY)
                    || ((d == 25 || (d == 26 && w == MONDAY)) && m == APRIL)
                    // Queen's Birthday, second MONDAY in June
                    || ((d > 7 && d <= 14) && w == MONDAY && m == JUNE)
                    // Bank Holiday, first MONDAY in August
                    || (d <= 7 && w == MONDAY && m == AUGUST)
                    // Labour Day, first MONDAY in October
                    || (d <= 7 && w == MONDAY && m == OCTOBER)
                    // Christmas, December 25th (possibly MONDAY or TUESDAY)
                    || ((d == 25 || (d == 27 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER)
                    // Boxing Day, December 26th (possibly MONDAY or TUESDAY)
                    || ((d == 26 || (d == 28 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER))
                return false;
            return true;
        }
    }

}
