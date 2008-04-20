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

import static org.jquantlib.time.Weekday.Monday;
import static org.jquantlib.time.Weekday.Tuesday;
import static org.jquantlib.util.Date.Month.August;
import static org.jquantlib.util.Date.Month.December;
import static org.jquantlib.util.Date.Month.February;
import static org.jquantlib.util.Date.Month.January;
import static org.jquantlib.util.Date.Month.July;
import static org.jquantlib.util.Date.Month.May;
import static org.jquantlib.util.Date.Month.November;
import static org.jquantlib.util.Date.Month.October;
import static org.jquantlib.util.Date.Month.September;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Date.Month;

/**
 * Banking holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday)</li>
 * <li>Family Day, third Monday of February (since 2008)</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Victoria Day, The Monday on or preceding 24 May</li>
 * <li>Canada Day, July 1st (possibly moved to Monday)</li>
 * <li>Provincial Holiday, first Monday of August</li>
 * <li>Labour Day, first Monday of September</li>
 * <li>Thanksgiving Day, second Monday of October</li>
 * <li>Remembrance Day, November 11th (possibly moved to Monday)</li>
 * <li>Christmas, December 25th (possibly moved to Monday or Tuesday)</li>
 * <li>Boxing Day, December 26th (possibly moved to Monday or Tuesday)</li>
 * </ul>
 * 
 * Holidays for the Toronto stock exchange (TSX):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday)</li>
 * <li>Family Day, third Monday of February (since 2008)</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Victoria Day, The Monday on or preceding 24 May</li>
 * <li>Canada Day, July 1st (possibly moved to Monday)</li>
 * <li>Provincial Holiday, first Monday of August</li>
 * <li>Labour Day, first Monday of September</li>
 * <li>Thanksgiving Day, second Monday of October</li>
 * <li>Christmas, December 25th (possibly moved to Monday or Tuesday)</li>
 * <li>Boxing Day, December 26th (possibly moved to Monday or Tuesday)</li>
 * </ul>
 * 
 * @author Srinivas Hasti
 * 
 */
public class Canada extends DelegateCalendar {

    public enum Market {
        SETTLEMENT, // !< generic settlement calendar
        TSX         // !< Toronto stock exchange calendar
    };

    private final static Canada SETTLEMENT_CALENDAR = new Canada(Market.SETTLEMENT);
    private final static Canada TSX_CALENDAR        = new Canada(Market.TSX);

    private Canada(Market market) {
        Calendar delegate;
        switch (market) {
            case SETTLEMENT:
                delegate = new SettlementCalendar();
                break;
            case TSX:
                delegate = new TsxCalendar();
                break;
            default:
                throw new IllegalArgumentException("unknown market");
        }
        setDelegate(delegate);
    }

    public static Canada getCalendar(Market market) {
        switch (market) {
            case SETTLEMENT:
                return SETTLEMENT_CALENDAR;
            case TSX:
                return TSX_CALENDAR;
            default:
                throw new IllegalArgumentException("unknown market");
        }
    }

    private class SettlementCalendar extends WesternCalendar {

    	public String getName() {
            return "Canada";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            // New Year's Day (possibly moved to Monday)
                    || ((d == 1 || (d == 2 && w == Monday)) && m == January)
                    // Family Day (third Monday in February, since 2008)
                    || ((d >= 15 && d <= 21) && w == Monday && m == February && y >= 2008)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // The Monday on or preceding 24 May (Victoria Day)
                    || (d > 17 && d <= 24 && w == Monday && m == May)
                    // July 1st, possibly moved to Monday (Canada Day)
                    || ((d == 1 || ((d == 2 || d == 3) && w == Monday)) && m == July)
                    // first Monday of August (Provincial Holiday)
                    || (d <= 7 && w == Monday && m == August)
                    // first Monday of September (Labor Day)
                    || (d <= 7 && w == Monday && m == September)
                    // second Monday of October (Thanksgiving Day)
                    || (d > 7 && d <= 14 && w == Monday && m == October)
                    // November 11th (possibly moved to Monday)
                    || ((d == 11 || ((d == 12 || d == 13) && w == Monday)) && m == November)
                    // Christmas (possibly moved to Monday or Tuesday)
                    || ((d == 25 || (d == 27 && (w == Monday || w == Tuesday))) && m == December)
                    // Boxing Day (possibly moved to Monday or Tuesday)
                    || ((d == 26 || (d == 28 && (w == Monday || w == Tuesday))) && m == December))
                return false;
            return true;
        }
    }

    private class TsxCalendar extends WesternCalendar {

    	public String getName() {
            return "TSX";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            // New Year's Day (possibly moved to Monday)
                    || ((d == 1 || (d == 2 && w == Monday)) && m == January)
                    // Family Day (third Monday in February, since 2008)
                    || ((d >= 15 && d <= 21) && w == Monday && m == February && y >= 2008)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // The Monday on or preceding 24 May (Victoria Day)
                    || (d > 17 && d <= 24 && w == Monday && m == May)
                    // July 1st, possibly moved to Monday (Canada Day)
                    || ((d == 1 || ((d == 2 || d == 3) && w == Monday)) && m == July)
                    // first Monday of August (Provincial Holiday)
                    || (d <= 7 && w == Monday && m == August)
                    // first Monday of September (Labor Day)
                    || (d <= 7 && w == Monday && m == September)
                    // second Monday of October (Thanksgiving Day)
                    || (d > 7 && d <= 14 && w == Monday && m == October)
                    // Christmas (possibly moved to Monday or Tuesday)
                    || ((d == 25 || (d == 27 && (w == Monday || w == Tuesday))) && m == December)
                    // Boxing Day (possibly moved to Monday or Tuesday)
                    || ((d == 26 || (d == 28 && (w == Monday || w == Tuesday))) && m == December))
                return false;
            return true;
        }
    }

}
