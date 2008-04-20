/*
 Copyright (C) 2008 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */
package org.jquantlib.time.calendars;

import static org.jquantlib.util.Date.Month.April;
import static org.jquantlib.util.Date.Month.August;
import static org.jquantlib.util.Date.Month.December;
import static org.jquantlib.util.Date.Month.January;
import static org.jquantlib.util.Date.Month.June;
import static org.jquantlib.util.Date.Month.May;
import static org.jquantlib.util.Date.Month.November;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Date.Month;

/**
 * Public holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Epiphany, January 6th</li>
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
 * <li>New Year's Day, January 1st</li>
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
 * @author Srinivas Hasti
 * 
 */
public class Italy extends DelegateCalendar {
    public static enum Market {
        SETTLEMENT, // generic settlement calendar
        EXCHANGE    // !< Milan stock-exchange calendar
    };

    private final static Italy SETTLEMENT_CALENDAR = new Italy(Market.SETTLEMENT);
    private final static Italy EXCHANGE_CALENDAR   = new Italy(Market.EXCHANGE);

    private Italy(Market market) {
        Calendar delegate;
        switch (market) {
            case SETTLEMENT:
                delegate = new SettlementCalendar();
                break;
            case EXCHANGE:
                delegate = new ExchangeCalendar();
                break;
            default:
                throw new IllegalArgumentException("unknown market");
        }
        setDelegate(delegate);
    }

    public static Italy getCalendar(Market market) {
        switch (market) {
            case SETTLEMENT:
                return SETTLEMENT_CALENDAR;
            case EXCHANGE:
                return EXCHANGE_CALENDAR;
            default:
                throw new IllegalArgumentException("unknown market");
        }
    }

    private class SettlementCalendar extends WesternCalendar {

        public String getName() {
            return "Italian settlement";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            // New Year's Day
                    || (d == 1 && m == January)
                    // Epiphany
                    || (d == 6 && m == January)
                    // Easter Monday
                    || (dd == em)
                    // Liberation Day
                    || (d == 25 && m == April)
                    // Labour Day
                    || (d == 1 && m == May)
                    // Republic Day
                    || (d == 2 && m == June && y >= 2000)
                    // Assumption
                    || (d == 15 && m == August)
                    // All Saints' Day
                    || (d == 1 && m == November)
                    // Immaculate Conception
                    || (d == 8 && m == December)
                    // Christmas
                    || (d == 25 && m == December)
                    // St. Stephen
                    || (d == 26 && m == December)
                    // December 31st, 1999 only
                    || (d == 31 && m == December && y == 1999))
                return false;
            return true;
        }
    }

    private class ExchangeCalendar extends WesternCalendar {

        public String getName() {
            return "Milan stock exchange";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            // New Year's Day
                    || (d == 1 && m == January)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // Labour Day
                    || (d == 1 && m == May)
                    // Assumption
                    || (d == 15 && m == August)
                    // Christmas' Eve
                    || (d == 24 && m == December)
                    // Christmas
                    || (d == 25 && m == December)
                    // St. Stephen
                    || (d == 26 && m == December)
                    // New Year's Eve
                    || (d == 31 && m == December))
                return false;
            return true;
        }
    }
}
