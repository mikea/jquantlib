/*
 Copyright (C) 2008 Renjith Nair

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
/*
 Copyright (C) 2005 Sercan Atalik

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.
 */


package org.jquantlib.time.calendars;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/** Holidays for the National Stock Exchange
 * 	<ul>
 *  <li>Saturdays</li>
 *  <li>Sundays</li>
 *  <li>New Year's Day, January 1st</li>
 *  <li>National Holidays (April 23rd, May 19th, August 30th, October 29th</li>
 *  <li>Local Holidays (Kurban, Ramadan; 2004 to 2009 only) </li>
 *  </ul>
 *  @author Renjith Nair
 */

public class Turkey extends DelegateCalendar {
    private final static Turkey ISE_CALENDAR = new Turkey(Market.ISE);

    private Turkey(final Market market) {
        Calendar delegate;
        switch (market) {
        case ISE:
            delegate = new TurkeyISECalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static Turkey getCalendar(final Market market) {
        switch (market) {
        case ISE:
            return ISE_CALENDAR;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }


    //
    // public enums
    //

    //FIXME: Settlement calendar is missing
    public static enum Market {
        /**
         * Istanbul Stock Exchange Turkey
         */
        ISE
    }


    //
    // private final classes
    //

    private static final class TurkeyISECalendar extends WesternCalendar {

        public String getName() {
            return "Istanbul Stock Exchange Turkey";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth();
            final int m = date.month().value();
            final int y = date.year();
            if (isWeekend(w)
                    // New Year's Day
                    || (d == 1 && m == Month.JANUARY.value())
                    // 23 nisan / National Holiday
                    || (d == 23 && m == Month.APRIL.value())
                    // 19 may/ National Holiday
                    || (d == 19 && m == Month.MAY.value())
                    // 30 aug/ National Holiday
                    || (d == 30 && m == Month.AUGUST.value())
                    ///29 ekim  National Holiday
                    || (d == 29 && m == Month.OCTOBER.value()))
                return false;

            // Local Holidays
            if (y == 2004) {
                // kurban
                if ((m == Month.FEBRUARY.value() && d <= 4)
                        // ramazan
                        || (m == Month.NOVEMBER.value() && d >= 14 && d <= 16))
                    return false;
            } else if (y == 2005) {
                // kurban
                if ((m == Month.JANUARY.value() && d >= 19 && d <= 21)
                        // ramazan
                        || (m ==  Month.NOVEMBER.value() && d >= 2 && d <= 5))
                    return false;
            } else if (y == 2006) {
                // kurban
                if ((m == Month.JANUARY.value() && d >= 9 && d <= 13)
                        // ramazan
                        || (m == Month.OCTOBER.value() && d >= 23 && d <= 25)
                        // kurban
                        || (m == Month.DECEMBER.value() && d >= 30))
                    return false;
            } else if (y == 2007) {
                // kurban
                if ((m == Month.JANUARY.value() && d <= 4)
                        // ramazan
                        || (m == Month.OCTOBER.value() && d >= 11 && d <= 14)
                        // kurban
                        || (m == Month.DECEMBER.value() && d >= 19 && d <= 23))
                    return false;
            } else if (y == 2008)
                // ramazan
                if ((m == Month.SEPTEMBER.value() && d >= 29)
                        || (m == Month.OCTOBER.value() && d <= 2)
                        // kurban
                        || (m == Month.DECEMBER.value() && d >= 7 && d <= 11))
                    return false;
            return true;
        }
    }

}