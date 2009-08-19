/*
 Copyright (C) 2008 Srinivas Hasti

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

import static org.jquantlib.time.Weekday.MONDAY;
import static org.jquantlib.time.Weekday.TUESDAY;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * Banking holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday)</li>
 * <li>Family Day, third Monday of February (since 2008)</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Victoria Day, The Monday on or preceding 24 May</li>
 * <li>Canada Day, July 1st (possibly moved to Monday)</li>
 * <li>Provincial Holiday, first Monday of August</li>
 * <li>Labour Day, first Monday of September</li>
 * <li>Thanksgiving Day, second Monday of October</li>
 * <li>Remembrance Day, November 11th (possibly moved to Monday)</li>
 * <li>Christmas, December 25th (possibly moved to Monday or Tuesday)</li>
 * <li>Boxing Day, December 26th (possibly moved to Monday or Tuesday)</li>
 * </ul>
 *
 * Holidays for the Toronto stock exchange (TSX):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday)</li>
 * <li>Family Day, third Monday of February (since 2008)</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Victoria Day, The Monday on or preceding 24 May</li>
 * <li>Canada Day, July 1st (possibly moved to Monday)</li>
 * <li>Provincial Holiday, first Monday of August</li>
 * <li>Labour Day, first Monday of September</li>
 * <li>Thanksgiving Day, second Monday of October</li>
 * <li>Christmas, December 25th (possibly moved to Monday or Tuesday)</li>
 * <li>Boxing Day, December 26th (possibly moved to Monday or Tuesday)</li>
 * </ul>
 *
 * @author Srinivas Hasti
 *
 */
public class Canada extends DelegateCalendar {

    private final static Canada SETTLEMENT_CALENDAR = new Canada(Market.SETTLEMENT);
    private final static Canada TSX_CALENDAR        = new Canada(Market.TSX);

    private Canada(final Market market) {
        Calendar delegate;
        switch (market) {
        case SETTLEMENT:
            delegate = new CanadaSettlementCalendar();
            break;
        case TSX:
            delegate = new TsxCalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static Canada getCalendar(final Market market) {
        switch (market) {
        case SETTLEMENT:
            return SETTLEMENT_CALENDAR;
        case TSX:
            return TSX_CALENDAR;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }


    //
    // public enums
    //

    public enum Market {
        /**
         * Generic settlement calendar
         */
        SETTLEMENT,

        /**
         * Toronto stock exchange calendar
         */
        TSX
    }


    //
    // private inner classes
    //

    private static final class CanadaSettlementCalendar extends WesternCalendar {

        public String getName() {
            return "Canada";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.getWeekday();
            final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            final Month m = date.getMonthEnum();
            final int y = date.getYear();
            final int em = easterMonday(y);
            if (isWeekend(w)
                    // New Year's Day (possibly moved to Monday)
                    || ((d == 1 || (d == 2 && w == MONDAY)) && m == JANUARY)
                    // Family Day (third Monday in February, since 2008)
                    || ((d >= 15 && d <= 21) && w == MONDAY && m == FEBRUARY && y >= 2008)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday Not listed as a closure day in Canada
                    //|| (dd == em)
                    // The Monday on or preceding 24 May (Victoria Day)
                    || (d > 17 && d <= 24 && w == MONDAY && m == MAY)
                    // July 1st, possibly moved to Monday (Canada Day)
                    || ((d == 1 || ((d == 2 || d == 3) && w == MONDAY)) && m == JULY)
                    // first Monday of August (Provincial Holiday)
                    || (d <= 7 && w == MONDAY && m == AUGUST)
                    // first Monday of September (Labor Day)
                    || (d <= 7 && w == MONDAY && m == SEPTEMBER)
                    // second Monday of October (Thanksgiving Day)
                    || (d > 7 && d <= 14 && w == MONDAY && m == OCTOBER)
                    // November 11th (possibly moved to Monday)
                    || ((d == 11 || ((d == 12 || d == 13) && w == MONDAY)) && m == NOVEMBER)
                    // Christmas (possibly moved to Monday or Tuesday)
                    || ((d == 25 || (d == 27 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER)
                    // Boxing Day (possibly moved to Monday or Tuesday)
                    || ((d == 26 || (d == 28 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER))
                return false;
            return true;
        }
    }

    private static final class TsxCalendar extends WesternCalendar {

        public String getName() {
            return "TSX";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.getWeekday();
            final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            final Month m = date.getMonthEnum();
            final int y = date.getYear();
            final int em = easterMonday(y);
            if (isWeekend(w)
                    // New Year's Day (possibly moved to Monday)
                    || ((d == 1 || (d == 2 && w == MONDAY)) && m == JANUARY)
                    // Family Day (third Monday in February, since 2008)
                    || ((d >= 15 && d <= 21) && w == MONDAY && m == FEBRUARY && y >= 2008)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday Not listed as a closure day in Canada
                    //|| (dd == em)
                    // The Monday on or preceding 24 May (Victoria Day)
                    || (d > 17 && d <= 24 && w == MONDAY && m == MAY)
                    // July 1st, possibly moved to Monday (Canada Day)
                    || ((d == 1 || ((d == 2 || d == 3) && w == MONDAY)) && m == JULY)
                    // first Monday of August (Provincial Holiday)
                    || (d <= 7 && w == MONDAY && m == AUGUST)
                    // first Monday of September (Labor Day)
                    || (d <= 7 && w == MONDAY && m == SEPTEMBER)
                    // second Monday of October (Thanksgiving Day)
                    || (d > 7 && d <= 14 && w == MONDAY && m == OCTOBER)
                    // Christmas (possibly moved to Monday or Tuesday)
                    || ((d == 25 || (d == 27 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER)
                    // Boxing Day (possibly moved to Monday or Tuesday)
                    || ((d == 26 || (d == 28 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER))
                return false;
            return true;
        }
    }

}