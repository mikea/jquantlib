/*
 Copyright (C) 2008 Richard Gomes

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

import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.FEBRUARY;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JULY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.OCTOBER;
import static org.jquantlib.time.Month.SEPTEMBER;
import static org.jquantlib.time.Weekday.MONDAY;

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Weekday;

/**
 * Hong Kong calendars Holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, JANUARY 1st (possibly moved to Monday)</li>
 * <li>Ching Ming Festival, April 5th</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Labor Day, May 1st</li>
 * <li>SAR Establishment Day, July 1st (possibly moved to Monday)</li>
 * <li>National Day, October 1st (possibly moved to Monday)</li>
 * <li>Christmas, December 25th</li>
 * <li>Boxing Day, December 26th (possibly moved to Monday)</li>
 * </ul>
 *
 * Other holidays for which no rule is given (data available for 2004-2007 only:)
 * <ul>
 * <li>Lunar New Year</li>
 * <li>Chinese New Year</li>
 * <li>Buddha's birthday</li>
 * <li>Tuen NG Festival</li>
 * <li>Mid-autumn Festival</li>
 * <li>Chung Yeung Festival</li>
 * </ul>
 *
 * Data from <http://www.hkex.com.hk>
 *
 * ingroup calendars
 *
 * @author Richard Gomes
 * @author Zahid Hussain
 */

@QualityAssurance(quality = Quality.Q3_DOCUMENTATION, version = Version.V097, reviewers = { "Zahid Hussain" })
public class HongKong extends Calendar {

    public static enum Market {
        /**
         * Hong Kong stock exchange
         */
        HKEx
    }

    //
    // public constructor
    //

    public HongKong() {
        this(Market.HKEx);
    }

    public HongKong(final Market m) {
        switch (m) {
        case HKEx:
            impl = new HkexImpl();
            break;

        default:
            QL.error(UNKNOWN_MARKET);
            throw new LibraryException(UNKNOWN_MARKET);
        }
    }


    //
    // private final inner classes
    //

    private final class HkexImpl extends WesternImpl {
        @Override
        public String name() {
            return "Hong Kong stock exchange";
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth(), dd = date.dayOfYear();
            final Month m = date.month();
            final int y = date.year();
            final int em = easterMonday(y);

            if (isWeekend(w)
            // New Year's Day
                    || ((d == 1 || ((d == 2 || d == 3) && w == MONDAY)) && m == JANUARY)
                    // Ching Ming Festival
                    || (d == 5 && m == APRIL)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter MONDAY
                    || (dd == em)
                    // Labor Day
                    || (d == 1 && m == MAY)
                    // SAR Establishment Day
                    || ((d == 1 || ((d == 2 || d == 3) && w == MONDAY)) && m == JULY)
                    // National Day
                    || ((d == 1 || ((d == 2 || d == 3) && w == MONDAY)) && m == OCTOBER)
                    // Christmas Day
                    || (d == 25 && m == DECEMBER)
                    // Boxing Day
                    || ((d == 26 || ((d == 27 || d == 28) && w == MONDAY)) && m == DECEMBER)) {
                return false;
            }

            if (y == 2004) {
                if (// Lunar New Year
                ((d == 22 || d == 23 || d == 24) && m == JANUARY)
                // Buddha's birthday
                        || (d == 26 && m == MAY)
                        // Tuen NG festival
                        || (d == 22 && m == JUNE)
                        // Mid-autumn festival
                        || (d == 29 && m == SEPTEMBER)
                        // Chung Yeung
                        || (d == 29 && m == SEPTEMBER)) {
                    return false;
                }
            }

            if (y == 2005) {
                if (// Lunar New Year
                ((d == 9 || d == 10 || d == 11) && m == FEBRUARY)
                // Buddha's birthday
                        || (d == 16 && m == MAY)
                        // Tuen NG festival
                        || (d == 11 && m == JUNE)
                        // Mid-autumn festival
                        || (d == 19 && m == SEPTEMBER)
                        // Chung Yeung festival
                        || (d == 11 && m == OCTOBER)) {
                    return false;
                }
            }

            if (y == 2006) {
                if (// Lunar New Year
                ((d >= 28 && d <= 31) && m == JANUARY)
                // Buddha's birthday
                        || (d == 5 && m == MAY)
                        // Tuen NG festival
                        || (d == 31 && m == MAY)
                        // Mid-autumn festival
                        || (d == 7 && m == OCTOBER)
                        // Chung Yeung festival
                        || (d == 30 && m == OCTOBER)) {
                    return false;
                }
            }

            if (y == 2007) {
                if (// Lunar New Year
                ((d >= 17 && d <= 20) && m == FEBRUARY)
                // Buddha's birthday
                        || (d == 24 && m == MAY)
                        // Tuen NG festival
                        || (d == 19 && m == JUNE)
                        // Mid-autumn festival
                        || (d == 26 && m == SEPTEMBER)
                        // Chung Yeung festival
                        || (d == 19 && m == OCTOBER)) {
                    return false;
                }
            }

            if (y == 2008) {
                if (// Lunar New Year
                ((d >= 7 && d <= 9) && m == FEBRUARY)
                // Ching Ming Festival
                        || (d == 4 && m == APRIL)
                        // Buddha's birthday
                        || (d == 12 && m == MAY)
                        // Tuen NG festival
                        || (d == 9 && m == JUNE)
                        // Mid-autumn festival
                        || (d == 15 && m == SEPTEMBER)
                        // Chung Yeung festival
                        || (d == 7 && m == OCTOBER)) {
                    return false;
                }
            }

            return true;
        }
    }
}
