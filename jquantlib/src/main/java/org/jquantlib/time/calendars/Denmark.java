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

import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MAY;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;

/**
 * Danish calendar
 * <p>
 * Holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>Maunday Thursday</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>General Prayer Day, 25 days after Easter Monday</li>
 * <li>Ascension</li>
 * <li>Whit (Pentecost) Monday</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Constitution Day, June 5th</li>
 * <li>Christmas, December 25th</li>
 * <li>Boxing Day, December 26th</li>
 * </ul>
 *
 * @author Jia Jia
 */

public class Denmark extends DelegateCalendar {
    private static Denmark CSECalendar = new Denmark(Market.CSE);

    private Denmark(final Market market) {
        Calendar delegate;
        switch (market) {
        case CSE:
            delegate = new CSECalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    //
    // public enums
    //

    // FIXME: Settlement calendar is missing
    public enum Market {
        /**
         * Copenhagen Stock Exchange
         */
        CSE
    }

    public static Denmark getCalendar(final Market market) {
        switch (market) {
        case CSE:
            return CSECalendar;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }

    private static final class CSECalendar extends WesternCalendar {

        @Override
        public String getName() {
            return "CSE";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth(), dd = date.dayOfYear();
            final Month m = date.month();
            final int y = date.year();
            final int em = easterMonday(y);
            // exact matching of days except for Easter Monday
            if (isWeekend(w)
                    // Maunday Thursday
                    || (dd == em - 4)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // General Prayer Day
                    || (dd == em + 25)
                    // Ascension
                    || (dd == em + 38)
                    // Whit Monday
                    || (dd == em + 49)
                    // New Year's Day
                    || (d == 1 && m == JANUARY)
                    // Constitution Day, June 5th
                    || (d == 5 && m == JUNE)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // Boxing Day
                    || (d == 26 && m == DECEMBER)
                    // below added according to http://nordic.nasdaqomxtrader.com/trading/tradinghours/
                    // Christmas eve
                    || (d == 24 && m == DECEMBER && (y == 2008 || y == 2009 || y == 2007))
                    // new year eve
                    || (d == 31 && m ==DECEMBER && (y == 2008 || y == 2009 || y == 2007))

                    || (d == 22 && m == MAY && y == 2009))
                return false;

            return true;

        }
    }
}
