/*
 Copyright (C) 2008 Jia Jia

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

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * Holidays for the Korea exchange (data from <http://www.krx.co.kr>):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Independence Day, March 1st</li>
 * <li>Arbour Day, April 5th</li>
 * <li>Labor Day, May 1st</li>
 * <li>Children's Day, May 5th</li>
 * <li>Memorial Day, June 6th</li>
 * <li>Constitution Day, July 17th</li>
 * <li>Liberation Day, August 15th</li>
 * <li>National Fondation Day, October 3th</li>
 * <li>Christmas Day, December 25th</li>
 * </ul>
 *
 * Other holidays for which no rule is given (data available for 2004-2007 only:)
 * <ul>
 * <li>Lunar New Year</li>
 * <li>Election Day 2004</li>
 * <li>Buddha's birthday</li>
 * <li>Harvest Moon Day</li>
 * </ul>
 *
 * @author Jia Jia
 *
 */
public class SouthKorea extends DelegateCalendar {

    private final static SouthKorea KRX_Calendar = new SouthKorea(Market.KRX);

    private SouthKorea(final Market market) {
        Calendar delegate;
        switch (market) {
        case KRX:
            delegate = new SouthKoreaKRXCalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static SouthKorea getCalendar(final Market market) {
        switch (market) {
        case KRX:
            return KRX_Calendar;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }


    //
    // public enums
    //

    //FIXME: Settlement calendar is missing
    public enum Market {
        /**
         * Korea Exchange
         */
        KRX
    }


    //
    // private inner classes
    //

    private static final class SouthKoreaKRXCalendar extends WesternCalendar {
        public String getName() {
            return "Korea exchange";
        }

        @Override
        public boolean isBusinessDay(final Date date /* @ReadOnly */) /* @ReadOnly */{
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth();
            final Month m = date.month();
            final int y = date.year();

            if (isWeekend(w)
                    // New Year's Day
                    || (d == 1 && m == JANUARY)
                    // Independence Day
                    || (d == 1 && m == MARCH)
                    // Arbour Day
                    || (d == 5 && m == APRIL)
                    // Labor Day
                    || (d == 1 && m == MAY)
                    // Children's Day
                    || (d == 5 && m == MAY)
                    // Memorial Day
                    || (d == 6 && m == JUNE)
                    // Constitution Dayordinal
                    || (d == 17 && m == JULY)
                    // Liberation Day
                    || (d == 15 && m == AUGUST)
                    // National Foundation Day
                    || (d == 3 && m == OCTOBER)
                    // Christmas Day
                    || (d == 25 && m == DECEMBER)

                    // Lunar New Year 2004
                    || ((d == 21 || d == 22 || d == 23 || d == 24 || d == 26) && m == JANUARY && y == 2004)
                    || ((d == 8 || d == 9 || d == 10) && m == FEBRUARY && y == 2005)
                    || ((d == 29 || d == 30 || d == 31) && m == JANUARY && y == 2006)
                    || (d == 19 && m == FEBRUARY && y == 2007)
                    || ((d == 6 || d == 7 || d == 8) && m == FEBRUARY && y == 2008)
                    // Election Day 2004
                    || (d == 15 && m == APRIL && y == 2004)
                    || (d == 9 && m == APRIL && y == 2008)
                    // Buddha's birthday
                    || (d == 26 && m == MAY && y == 2004)
                    || (d == 15 && m == MAY && y == 2005)
                    || (d == 5 && m == MAY && y == 2006)
                    || (d == 24 && m == MAY && y == 2007)
                    || (d == 12 && m == MAY && y == 2008)
                    // Harvest Moon Day
                    || ((d == 27 || d == 28 || d == 29) && m == SEPTEMBER && y == 2004)
                    || ((d == 17 || d == 18 || d == 19) && m == SEPTEMBER && y == 2005)
                    || ((d == 5 || d == 6 || d == 7) && m == OCTOBER && y == 2006)
                    || ((d == 24 || d == 25 || d == 26) && m == SEPTEMBER && y == 2007)
                    || ((d == 13 || d == 14 || d == 15) && m == SEPTEMBER && y == 2008))
                return false;
            return true;

        }
    }
}
