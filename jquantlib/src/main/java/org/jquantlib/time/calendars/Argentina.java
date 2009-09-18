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

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * Argentinian calendar
 *
 * <strong>Banking holidays:</strong>
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Holy Thursday</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Dia del Veterano y de los Caidos en la Guerra de Malvinas, 2nd April</li>
 * <li>Labour Day, May 1st</li>
 * <li>Death of General Manuel Belgrano, third Monday of June</li>
 * <li>Independence Day, July 9th</li>
 * <li>Death of General Jose de San Martin, third Monday of August</li>
 * <li>Independence Day, September 21th</li>
 * <li>Columbus Day, October 12th</li>
 * <li>All Souls Day, November 2nd</li>
 * <li>Republic Day, November 15th</li>
 * <li>Immaculate Conception, December 8th</li>
 * <li>Christmas Eve, December 24th</li>
 * <li>Christmas, December 25th</li>
 * <li>New Years's Eve, December 31th</li>
 * <li>Passion of Christ</li>
 * <li>Corpus Christi</li>
 * </ul>
 *
 * Holidays for the MERVAL stock exchange
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Holy Thursday</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Dia del Veterano y de los Caidos en la Guerra de Malvinas, 2nd April</li>
 * <li>Labour Day, May 1st</li>
 * <li>Death of General Manuel Belgrano, third Monday of June</li>
 * <li>Independence Day, July 9th</li>
 * <li>Death of General Jose de San Martin, third Monday of August</li>
 * <li></li>
 * <li>Immaculate Conception, December 8th</li>
 * <li>Christmas Eve, December 24th</li>
 * <li>Christmas, December 25th</li>
 * <li>New Year's Eve, December 31th</li>
 * </ul>
 *
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
public class Argentina extends DelegateCalendar {

    private final static Argentina SETTLEMENT_CALENDAR = new Argentina(
            Market.SETTLEMENT);
    private final static Argentina MERVAL_CALENDAR = new Argentina(
            Market.MERVAL);

    private Argentina(final Market market) {
        Calendar delegate;
        switch (market) {
        case SETTLEMENT:
            delegate = new ArgentinaSettlementCalendar();
            break;
        case MERVAL:
            delegate = new ArgentinaMervalExchangeCalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static Argentina getCalendar(final Market market) {
        switch (market) {
        case SETTLEMENT:
            return SETTLEMENT_CALENDAR;
        case MERVAL:
            return MERVAL_CALENDAR;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }

    //
    // public enums
    //

    public enum Market {

        /**
         * Argentina settlement
         */
        SETTLEMENT,

        /**
         * MERVAL
         */
        MERVAL
    }

    //
    // private inner classes
    //

    private static final class ArgentinaSettlementCalendar extends
    WesternCalendar {

        public String getName() {
            return "Argentina";
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
                    // New Year's Day
                    || (d == 1 && m == Month.JANUARY)
                    // D�a del Veterano y de los Ca�dos en la Guerra de
                    // Malvinas
                    || (d == 2 && m == Month.APRIL)
                    // Labor Day
                    || (d == 1 && m == Month.MAY)
                    // Revolution, May 25th
                    || (d == 25 && m == Month.MAY)
                    // Death of General Manuel Belgrano
                    || (d >= 15 && d <= 21 && w == Weekday.MONDAY && m == Month.JUNE)
                    // Independence Day
                    || (d == 9 && m == Month.JULY)
                    // Death of General Jos� de San Martin
                    || (d >= 15 && d <= 21 && w == Weekday.MONDAY && m == Month.AUGUST)
                    // Columbus Day
                    || ((d == 10 || d == 11 || d == 12 || d == 15 || d == 16)
                            && w == Weekday.MONDAY && m == Month.OCTOBER)
                            // Holy Thursday
                            || (dd == em - 4)
                            // Good Friday
                            || (dd == em - 3)
                            // Corpus Christi
                            // || (dd == em + 59)
                            // All Souls Day
                            || (d == 2 && m == Month.NOVEMBER)
                            // Immaculate Conception
                            || (d == 8 && m == Month.DECEMBER)
                            // Christmas Eve
                            || (d == 24 && m == Month.DECEMBER)
                            // Christmas Day
                            || (d == 25 && m == Month.DECEMBER)
                            // New Year's Eve
                            || ((d == 31 || (d == 30 && w == Weekday.FRIDAY)) && m == Month.DECEMBER))
                return false;
            return true;
        }

    }

    private static final class ArgentinaMervalExchangeCalendar extends
    WesternCalendar {

        public String getName() {
            return "MERVAL";
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
                    // 01 Jan New Year's Day
                    || (d == 1 && m == Month.JANUARY)
                    // Holy Thursday
                    || (dd == em - 4)
                    // Good Friday
                    || (dd == em - 3)
                    // 24 Mar Truth and Justice Day
                    || (d == 24 && m == Month.MARCH)
                    // 02 Apr Malvinas Islands Memorial
                    || (d == 2 && m == Month.APRIL)
                    // 01 May Workers' Day
                    || (d == 1 && m == Month.MAY)
                    // 25 May National Holiday
                    || (d == 25 && m == Month.MAY)
                    // Death of General Manuel Belgrano
                    || ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == Month.JUNE)
                    // 09 Jul Independence Day
                    || (d == 9 && m == Month.JULY)
                    // Anniversary of the Death of General San Martin
                    || ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == Month.AUGUST)
                    // Columbus Day (OBS)
                    || ((d == 10 || d == 11 || d == 12 || d == 15 || d == 16)
                            && w == Weekday.MONDAY && m == Month.OCTOBER)
                            // 06 Nov Bank Holiday
                            || (d == 6 && m == Month.NOVEMBER)
                            // 08 Dec Immaculate Conception
                            || (d == 8 && m == Month.DECEMBER)
                            // 24 Dec Christmas Eve
                            // 25 Dec Christmas Day
                            || ((d == 24 || d == 25) && m == Month.DECEMBER)
                            // 31 Dec Last business day of year
                            || (d == 31 && m == Month.DECEMBER))
                return false;
            return true;
        }
    }

}