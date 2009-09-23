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

import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.AUGUST;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.FEBRUARY;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JULY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.NOVEMBER;
import static org.jquantlib.time.Month.OCTOBER;
import static org.jquantlib.time.Month.SEPTEMBER;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;


/**
 * Indonesian calendars
 * <p>
 * Holidays for the Jakarta stock exchange
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>Good Friday</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Ascension of Jesus Christ</li>
 * <li>Independence Day, August 17th</li>
 * <li>Christmas, December 25th</li>
 * </ul>
 * <p>
 * Other holidays for which no rule is given (data available for 2005-2007 only:)
 * <ul>
 * <li>Idul Adha</li>
 * <li>Ied Adha</li>
 * <li>Imlek</li>
 * <li>Moslem's New Year Day</li>
 * <li>Nyepi (Saka's New Year)</li>
 * <li>Birthday of Prophet Muhammad SAW</li>
 * <li>Waisak</li>
 * <li>Ascension of Prophet Muhammad SAW</li>
 * <li>Idul Fitri</li>
 * <li>Ied Fitri</li> <li>Other national leaves</li>
 * </ul>
 *
 * @category calendars
 *
 * @see <a href="http://www.idx.co.id/">Indonesia Stock Exchange</a>
 *
 * @author Joon Tiang
 * @author Jia Jia
 */
public class Indonesia extends DelegateCalendar {

    private final static Indonesia BEJ_CALENDAR = new Indonesia(Market.BEJ);

    private Indonesia(final Market market) {
        Calendar delegate;
        switch (market) {
        case BEJ:
        case JSX:
            delegate = new IndonesiaBEJCalendar();
            break;

        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
        setDelegate(delegate);
    }

    public static Indonesia getCalendar(final Market market) {
        switch (market) {
        case BEJ:
        case JSX:
            return BEJ_CALENDAR;

        default:
            throw new LibraryException(UNKNOWN_MARKET); // QA:[RG]::verified
        }
    }


    //
    // public enums
    //

    public enum Market {
        /**
         * Jakarta stock exchange
         */
        BEJ,
        /**
         * Jakarta stock exchange
         */
        JSX
    }


    //
    // private inner classes
    //

    private static final class IndonesiaBEJCalendar extends WesternCalendar {

        public String getName() {
            return "Jakarta stock exchange";
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
                    // Ascension Thursday
                    || (dd == em + 38)
                    // Independence Day
                    || (d == 17 && m == AUGUST)
                    // Christmas
                    || (d == 25 && m == DECEMBER))
                return false;

            if (y == 2005)
                if (
                        // Idul Adha
                        (d == 21 && m == JANUARY)
                        // Imlek
                        || (d == 9 && m == FEBRUARY)
                        // Moslem's New Year Day
                        || (d == 10 && m == FEBRUARY)
                        // Nyepi
                        || (d == 11 && m == MARCH)
                        // Birthday of Prophet Muhammad SAW
                        || (d == 22 && m == APRIL)
                        // Waisak
                        || (d == 24 && m == MAY)
                        // Ascension of Prophet Muhammad SAW
                        || (d == 2 && m == SEPTEMBER)
                        // Idul Fitri
                        || ((d == 3 || d == 4) && m == NOVEMBER)
                        // National leaves
                        || ((d == 2 || d == 7 || d == 8) && m == NOVEMBER) || (d == 26 && m == DECEMBER))
                    return false;

            if (y == 2006)
                if (
                        // Idul Adha
                        (d == 10 && m == JANUARY)
                        // Moslem's New Year Day
                        || (d == 31 && m == JANUARY)
                        // Nyepi
                        || (d == 30 && m == MARCH)
                        // Birthday of Prophet Muhammad SAW
                        || (d == 10 && m == APRIL)
                        // Ascension of Prophet Muhammad SAW
                        || (d == 21 && m == AUGUST)
                        // Idul Fitri
                        || ((d == 24 || d == 25) && m == OCTOBER)
                        // National leaves
                        || ((d == 23 || d == 26 || d == 27) && m == OCTOBER))
                    return false;

            if (y == 2007)
                if (
                        // Nyepi
                        (d == 19 && m == MARCH)
                        // Waisak
                        || (d == 1 && m == JUNE)
                        // Ied Adha
                        || (d == 20 && m == DECEMBER)
                        // National leaves
                        || (d == 18 && m == MAY) || ((d == 12 || d == 15 || d == 16) && m == OCTOBER)
                        || ((d == 21 || d == 24) && m == OCTOBER))
                    return false;

            if (y == 2008)
                if (
                        // Islamic New Year 1429 H
                        (d == 10 && m == JANUARY)
                        // National Leave
                        || (d == 11 && m == JANUARY)
                        // Chinese New Year
                        || (d == 7 && m == FEBRUARY)
                        // Trading Holiday
                        || (d == 8 && m == FEBRUARY)
                        // Saka's New Year
                        || (d == 7 && m == MARCH)
                        // Birthday of the prophet Muhammad
                        || (d == 20 && m == MARCH)
                        // Vesak Day
                        || (d == 20 && m == MAY)
                        // Isra' Mi'raj of the prophet Muhammad
                        || (d == 30 && m == JULY)
                        // National Leave
                        || (d == 18 && m == AUGUST) || (d == 30 && m == SEPTEMBER)
                        // Ied Fitr 1 Syawal
                        || (d == 1 && m == OCTOBER) || (d == 2 && m == OCTOBER)
                        // National Leave
                        || (d == 3 && m == OCTOBER)
                        // Ied Adha
                        || (d == 8 && m == DECEMBER)
                        // Islamic New Year
                        || (d == 29 && m == DECEMBER)
                        // New Year's Eve
                        || (d == 31 && m == DECEMBER))
                    return false;

            if (y == 2009)
                if (
                        // Public Holiday
                        (d == 2 && m == JANUARY)
                        // Chinese New Year
                        || (d == 26 && m == JANUARY)
                        // Saka's New Year
                        || (d == 26 && m == MARCH)
                        // Birthday of the prophet Muhammad
                        || (d == 9 && m == MARCH)
                        // Isra' Mi'raj of the prophet Muhammad
                        || (d == 20 && m == JULY)
                        // Public Holiday
                        || (d == 18 && m == SEPTEMBER) || (d == 23 && m == SEPTEMBER)
                        // Ied Fitr 1 Syawal
                        || (d == 21 && m == SEPTEMBER) || (d == 22 && m == SEPTEMBER)
                        // Ied Adha
                        || (d == 27 && m == NOVEMBER)
                        // Islamic New Year
                        || (d == 18 && m == DECEMBER)
                        // Public Holiday
                        || (d == 24 && m == DECEMBER)
                        // Trading Holiday
                        || (d == 31 && m == DECEMBER))
                    return false;

            return true;
        }
    }

}
