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
 Copyright (C) 2005, 2007 StatPro Italia srl

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

/** Holidays for the Prague stock exchange (see http://www.pse.cz/):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Easter Monday</li>
 * <li>Labour Day, May 1st</li>
 * <li>Liberation Day, May 8th</li>
 * <li>SS. Cyril and Methodius, July 5th</li>
 * <li>Jan Hus Day, July 6th</li>
 * <li>Czech Statehood Day, September 28th</li>
 * <li>Independence Day, October 28th</li>
 * <li>Struggle for Freedom and Democracy Day, November 17th</li>
 * <li>Christmas Eve, December 24th</li>
 * <li>Christmas, December 25th</li>
 * <li>St. Stephen, December 26th</li>
 * </ul>
 * ingroup calendars
 */
public class CzechRepublic extends DelegateCalendar {
    private final static CzechRepublic PSE_CALENDAR = new CzechRepublic(Market.PSE);

    private CzechRepublic(final Market market) {
        Calendar delegate;
        switch (market) {
        case PSE:
            delegate = new CzechRepublicPSECalendar();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static CzechRepublic getCalendar(final Market market) {
        switch (market) {
        case PSE:
            return PSE_CALENDAR;
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
         * Prague stock exchange of CzechRepublic
         */
        PSE
    }


    //
    // private inner classes
    //

    private static final class CzechRepublicPSECalendar extends WesternCalendar {

        public String getName() {
            return "Prague stock exchange of CzechRepublic";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.getWeekday();
            final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            final int m = date.getMonth();
            final int y = date.getYear();
            final int em = easterMonday(y);

            if (isWeekend(w)
                    // New Year's Day
                    || (d == 1 && m == Month.JANUARY.toInteger())
                    // Easter Monday
                    || (dd == em)
                    // Labour Day
                    || (d == 1 && m == Month.MAY.toInteger())
                    // Liberation Day
                    || (d == 8 && m == Month.MAY.toInteger())
                    // SS. Cyril and Methodius
                    || (d == 5 && m == Month.JULY.toInteger())
                    // Jan Hus Day
                    || (d == 6 && m == Month.JULY.toInteger())
                    // Czech Statehood Day
                    || (d == 28 && m == Month.SEPTEMBER.toInteger())
                    // Independence Day
                    || (d == 28 && m == Month.OCTOBER.toInteger())
                    // Struggle for Freedom and Democracy Day
                    || (d == 17 && m == Month.NOVEMBER.toInteger())
                    // Christmas Eve
                    || (d == 24 && m == Month.DECEMBER.toInteger())
                    // Christmas
                    || (d == 25 && m == Month.DECEMBER.toInteger())
                    // St. Stephen
                    || (d == 26 && m == Month.DECEMBER.toInteger())
                    // unidentified closing days for stock exchange
                    || (d == 2 && m == Month.JANUARY.toInteger() && y == 2004)
                    || (d == 31 && m == Month.DECEMBER.toInteger() && y == 2004))
                return false;
            return true;
        }
    }

}