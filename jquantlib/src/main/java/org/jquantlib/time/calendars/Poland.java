/*
 Copyright (C) 2008 Anand Mani

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

import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * Polish calendar
 * <p>
 * Holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>Easter Monday</li>
 * <li>Corpus Christi</li>
 * <li>New Year's Day, January 1st</li>
 * <li>May Day, May 1st</li>
 * <li>Constitution Day, May 3rd</li>
 * <li>Assumption of the Blessed Virgin Mary, August 15th</li>
 * <li>All Saints Day, November 1st</li>
 * <li>Independence Day, November 11th</li>
 * <li>Christmas, December 25th</li>
 * <li>2nd Day of Christmas, December 26th</li>
 * </ul>
 *
 * @category calendars
 *
 * @see <a href="http://www.gpw.pl/">Warsaw Stock Exchange</a>
 *
 * @author Anand Mani
 * @author Renjith Nair
 * @author Richard Gomes
 */
public class Poland extends DelegateCalendar {

    private final static Poland SETTLEMENT_CALENDAR = new Poland(Market.Settlement);
    private final static Poland WSE_CALENDAR        = new Poland(Market.WSE);

    private Poland(final Market market) {
        Calendar delegate;
        switch (market) {
        case Settlement:
            delegate = new PolandSettlementCalendar();
            break;
        case WSE:
            delegate = new PolandWSECalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static Poland getCalendar(final Market market) {
        switch (market) {
        case Settlement:
            return SETTLEMENT_CALENDAR;
        case WSE:
            return WSE_CALENDAR;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }


    //
    // public enums
    //

    public enum Market {

        /**
         * Poland settlement
         */
        Settlement,

        /**
         * Warsaw Stock Exchange
         */
        WSE
    }


    //
    // private inner classes
    //

    //FIXME: Reliable years: 2007, 2008, 2009
    private static final class PolandSettlementCalendar extends WesternCalendar {

        public String getName() {
            return "Poland settlement";
        }

        @Override
        public boolean isBusinessDay(final Date date /* @ReadOnly */) /* @ReadOnly */{
            final Weekday w = date.getWeekday();
            final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            final Month m = date.getMonthEnum();
            final int y = date.getYear();
            final int em = easterMonday(y);

            if (isWeekend(w)
                    // Easter Monday
                    || (dd == em)
                    // Corpus Christi
                    || (dd == em + 59)
                    // New Year's Day
                    || (d == 1 && m == JANUARY)
                    // May Day
                    || (d == 1 && m == MAY)
                    // Constitution Day
                    || (d == 3 && m == MAY)
                    // Assumption of the Blessed Virgin Mary
                    || (d == 15 && m == AUGUST)
                    // All Saints Day
                    || (d == 1 && m == NOVEMBER)
                    // Independence Day
                    || (d == 11 && m == NOVEMBER)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // Boxing Day
                    || (d == 26 && m == DECEMBER)
            )
                return false;
            return true;
        }

    }


    //FIXME: Reliable years: 2007, 2008, 2009
    final static private class PolandWSECalendar extends WesternCalendar {

        public String getName() {
            return "Warsaw Stock Exchange";
        }

        @Override
        public boolean isBusinessDay(final Date date /* @ReadOnly */) /* @ReadOnly */{
            final Weekday w = date.getWeekday();
            final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            final Month m = date.getMonthEnum();
            final int y = date.getYear();
            final int em = easterMonday(y);

            if (isWeekend(w)
                    // Easter Monday
                    || (dd == em)
                    // Corpus Christi
                    || (dd == em + 59)
                    // New Year's Day
                    || (d == 1 && m == JANUARY)
                    // May Day
                    || (d == 1 && m == MAY)
                    // Constitution Day
                    || (d == 3 && m == MAY)
                    // Assumption of the Blessed Virgin Mary
                    || (d == 15 && m == AUGUST)
                    // All Saints Day
                    || (d == 1 && m == NOVEMBER)
                    // Independence Day
                    || (d == 11 && m == NOVEMBER)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // 2nd Day of Christmas
                    || (d == 26 && m == DECEMBER)

                    // Good Friday
                    || (dd == (em-3))
                    // Christmas Eve
                    || (d == 24 && m == DECEMBER)
                    // gap days
                    || (d == 2 && m == JANUARY && w.equals(Weekday.FRIDAY))
            )
                return false;
            return true;
        }

    }

}
