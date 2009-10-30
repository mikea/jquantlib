/*
 Copyright (C) 2008 Srinivas Hasti

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
import static org.jquantlib.time.Month.AUGUST;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.NOVEMBER;

import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Weekday;

/**
 *
 * Italian calendars Public holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, JANUARY 1st</li>
 * <li>Epiphany, JANUARY 6th</li>
 * <li>Easter Monday</li>
 * <li>Liberation Day, April 25th</li>
 * <li>Labour Day, May 1st</li>
 * <li>Republic Day, June 2nd (since 2000)</li>
 * <li>Assumption, August 15th</li>
 * <li>All Saint's Day, November 1st</li>
 * <li>Immaculate Conception Day, December 8th</li>
 * <li>Christmas Day, December 25th</li>
 * <li>St. Stephen's Day, December 26th</li>
 * </ul>
 *
 * Holidays for the stock exchange (data from http://www.borsaitalia.it):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, JANUARY 1st</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Labour Day, May 1st</li>
 * <li>Assumption, August 15th</li>
 * <li>Christmas' Eve, December 24th</li>
 * <li>Christmas, December 25th</li>
 * <li>St. Stephen, December 26th</li>
 * <li>New Year's Eve, December 31st</li>
 * </ul>
 *
 * @test the correctness of the returned results is tested against a list of known holidays.
 *
 * @category calendars
 * @see <a href="http://www.borsaitalia.it">Borsa Italiana</a>
 *
 * @author Srinivas Hasti
 * @author Zahid Hussain
 */

@QualityAssurance(quality = Quality.Q3_DOCUMENTATION, version = Version.V097, reviewers = { "Zahid Hussain" })
public class Italy extends Calendar {

    public static enum Market {
        /**
         * Generic settlement calendar
         */
        Settlement,

        /**
         * Milan stock-exchange calendar
         */
        Exchange
    }


    //
    // public constructors
    //

    public Italy() {
        this(Market.Settlement);
    }

    public Italy(final Market market) {
        switch (market) {
        case Settlement:
            impl = new SettlementImpl();
            break;
        case Exchange:
            impl = new ExchangeImpl();
            break;
        default:
            throw new LibraryException(UNKNOWN_MARKET);
        }
    }


    //
    // private final inner classes
    //

    private final class SettlementImpl extends WesternImpl {
        @Override
        public String name() {
            return "Italian settlement";
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
                    || (d == 1 && m == JANUARY)
                    // Epiphany
                    || (d == 6 && m == JANUARY)
                    // Easter Monday
                    || (dd == em)
                    // Liberation Day
                    || (d == 25 && m == APRIL)
                    // Labour Day
                    || (d == 1 && m == MAY)
                    // Republic Day
                    || (d == 2 && m == JUNE && y >= 2000)
                    // Assumption
                    || (d == 15 && m == AUGUST)
                    // All Saints' Day
                    || (d == 1 && m == NOVEMBER)
                    // Immaculate Conception
                    || (d == 8 && m == DECEMBER)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // St. Stephen
                    || (d == 26 && m == DECEMBER)
                    // DECEMBER 31st, 1999 only
                    || (d == 31 && m == DECEMBER && y == 1999)) {
                return false;
            }
            return true;
        }
    }

    private final class ExchangeImpl extends WesternImpl {
        @Override
        public String name() {
            return "Milan stock exchange";
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
                    || (d == 1 && m == JANUARY)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // Labour Day
                    || (d == 1 && m == MAY)
                    // Assumption
                    || (d == 15 && m == AUGUST)
                    // Christmas' Eve
                    || (d == 24 && m == DECEMBER)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // St. Stephen
                    || (d == 26 && m == DECEMBER)
                    // New Year's Eve
                    || (d == 31 && m == DECEMBER)) {
                return false;
            }
            return true;
        }
    }
}
