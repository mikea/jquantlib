/*
 Copyright (C) 2008

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

import static org.jquantlib.time.Weekday.FRIDAY;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MAY;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * Finnish calendar
 * <p>
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Epiphany, January 6th</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Ascension Thursday</li>
 * <li>Labour Day, May 1st</li>
 * <li>Midsummer Eve (Friday between June 18-24)</li>
 * <li>Independence Day, December 6th</li>
 * <li>Christmas Eve, December 24th</li>
 * <li>Christmas, December 25th</li>
 * <li>Boxing Day, December 26th</li>
 * </ul>
 *
 * @author Heng Joon Tiang
 */
public class Finland extends DelegateCalendar {

    private final static Finland HSE_CALENDAR = new Finland(Market.HSE);

    private Finland(final Market market) {
        Calendar delegate;
        switch (market) {
        case HSE:
            delegate = new FinlandHSECalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static Finland getCalendar(final Market market) {
        switch (market) {
        case HSE:
            return HSE_CALENDAR;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }


    //
    // public enums
    //

    // FIXME: Settlement calendar is missing

    public enum Market {
        /**
         * Helsinki Stock Exchange
         */
        HSE
    }


    //
    // private inner classes
    //

    private static final class FinlandHSECalendar extends WesternCalendar {

        public String getName() {
            return "HSE";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.getWeekday();
            final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            final Month m = date.getMonthEnum();
            final int y = date.getYear();
            final int em = easterMonday(y);
            if (isWeekend(w)
                    // New Year's Day
                    || (d == 1 && m == JANUARY)
                    // Epiphany
                    || (d == 6 && m == JANUARY)
                    // Good Friday
                    || (dd == em-3)
                    // Easter Monday
                    || (dd == em)
                    // Ascension Thursday
                    || (dd == em+38)
                    // Labour Day
                    || (d == 1 && m == MAY)
                    // Midsummer Eve (Friday between June 18-24)
                    || (w == FRIDAY && (d >= 18 && d <= 24) && m == JUNE)
                    // Independence Day
                    || (d == 6 && m == DECEMBER)
                    // Christmas Eve
                    || (d == 24 && m == DECEMBER)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // Boxing Day
                    || (d == 26 && m == DECEMBER)
                    // New year's eve
                    || (d == 31 && m == DECEMBER))
                return false;
            return true;
        }
    }

}
