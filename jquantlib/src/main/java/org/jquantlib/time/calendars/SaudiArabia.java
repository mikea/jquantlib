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
import static org.jquantlib.time.Weekday.THURSDAY;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.AbstractCalendar;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * Saudi Arabia calendar
 * <ul>
 * <li>Thursdays</li>
 * <li>Fridays</li>
 * <li>National Day of Saudi Arabia, September 23rd</li>
 * <p>
 * Other holidays for which no rule is given (data available for 2004-2005 only:)
 * </p>
 * <li>Eid Al-Adha</li>
 * <li>Eid Al-Fitr</li>
 * </ul>
 *
 * @author Richard Gomes
 */
public class SaudiArabia extends DelegateCalendar{

    public enum Market {
        /**
         * Tadawul financial market
         */
        TADAWUL
    };

    private final static SaudiArabia TADAWUL_CALENDAR = new SaudiArabia(Market.TADAWUL);

    private SaudiArabia(final Market market) {
        Calendar delegate;
        switch (market) {
        case TADAWUL:
            delegate = new SaudiArabiaSettlementCalendar();
            break;

        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        // FIXME
        setDelegate(delegate);
    }

    public static SaudiArabia getCalendar(final Market market) {
        switch (market) {
        case TADAWUL:
            return TADAWUL_CALENDAR;

        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }


    //
    // private inner classes
    //    //
    // overrides AbstractCalendar
    //

    @Override
    public final void addHoliday(final Date d) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeHoliday(final Date d) {
        throw new UnsupportedOperationException();
    }


    private final class SaudiArabiaSettlementCalendar extends AbstractCalendar {
        public boolean isWeekend(final Weekday w){
            return w == THURSDAY || w == FRIDAY;
        }

        //
        // overrides AbstractCalendar
        //

        @Override
        public final void addHoliday(final Date d) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeHoliday(final Date d) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.getWeekday();
            final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            final Month m = date.getMonthEnum();
            final int y = date.getYear();

            if (isWeekend(w)
                    // National Day
                    || (d == 23 && m == SEPTEMBER)
                    // Eid Al-Adha
                    || (d >= 1 && d <= 6 && m == FEBRUARY && y==2004)
                    || (d >= 21 && d <= 25 && m == JANUARY && y==2005)
                    // Eid Al-Fitr
                    || (d >= 25 && d <= 29 && m == NOVEMBER && y==2004)
                    || (d >= 14 && d <= 18 && m == NOVEMBER && y==2005)
            )
                return false;

            return true;
        }
        public String getName() {
            return "Tadawul";
        }
    }

}

