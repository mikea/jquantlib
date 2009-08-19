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

import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * Norwegian calendar
 * <p>
 * Holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>Holy Thursday</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Ascension</li>
 * <li>Whit(Pentecost) Monday</li>
 * <li>New Year's Day, January 1st</li>
 * <li>May Day, May 1st</li>
 * <li>National Independence Day, May 17st</li>
 * <li>Christmas, December 25th</li>
 * <li>Boxing Day, December 26th</li>
 * </ul>
 *
 * @category calendars
 *
 * @author Anand Mani
 */
public class Norway extends DelegateCalendar {

    private static final Norway OSLOBORS_CALENDAR = new Norway(Market.OsloBors);


    private Norway(final Market market) {
        Calendar delegate;
        switch (market) {
        case OsloBors:
            delegate = new OsloBorsCalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static Norway getCalendar(final Market market) {
        switch (market) {
        case OsloBors:
            return OSLOBORS_CALENDAR;
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
         * OsloBors
         */
        OsloBors
    }


    //
    // private inner classes
    //

    private static final class OsloBorsCalendar extends WesternCalendar {

        public String getName() {
            return "OsloBors";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.getWeekday();
            final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            final Month m = date.getMonthEnum();
            final int y = date.getYear();
            final int em = easterMonday(y);
            if (isWeekend(w)
                    // Holy Thursday
                    || (dd == em - 4)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // Ascension Thursday
                    || (dd == em + 38)
                    // Whit Monday
                    || (dd == em + 49)
                    // New Year's Day
                    || (d == 1 && m == JANUARY)
                    // May Day
                    || (d == 1 && m == MAY)
                    // National Independence Day
                    || (d == 17 && m == MAY)

                    // Christmas Eve is only observed by Oslo Bors :: see http://bugs.jquantlib.org/view.php?id=73
                    //					|| (d == 24 && m == DECEMBER)

                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // Boxing Day
                    || (d == 26 && m == DECEMBER)

                    // 31-DEC only observed by Oslo Bors :: see http://bugs.jquantlib.org/view.php?id=73
                    //				|| (d == 31 && m == DECEMBER)

            )

                return false;
            return true;
        }
    }

}
