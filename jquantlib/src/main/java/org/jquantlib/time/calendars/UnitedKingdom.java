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

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Date.Month;

/**
 * Public holidays (data from http://www.dti.gov.uk/er/bankhol.htm):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday)</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Early May Bank Holiday, first Monday of May</li>
 * <li>Spring Bank Holiday, last Monday of May</li>
 * <li>Summer Bank Holiday, last Monday of August</li>
 * <li>Christmas Day, December 25th (possibly moved to Monday or Tuesday)</li>
 * <li>Boxing Day, December 26th (possibly moved to Monday or Tuesday)</li>
 * </ul>
 * 
 * Holidays for the stock exchange:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday)</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Early May Bank Holiday, first Monday of May</li>
 * <li>Spring Bank Holiday, last Monday of May</li>
 * <li>Summer Bank Holiday, last Monday of August</li>
 * <li>Christmas Day, December 25th (possibly moved to Monday or Tuesday)</li>
 * <li>Boxing Day, December 26th (possibly moved to Monday or Tuesday)</li>
 * </ul>
 * 
 * Holidays for the metals exchange:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday)</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Early May Bank Holiday, first Monday of May</li>
 * <li>Spring Bank Holiday, last Monday of May</li>
 * <li>Summer Bank Holiday, last Monday of August</li>
 * <li>Christmas Day, December 25th (possibly moved to Monday or Tuesday)</li>
 * <li>Boxing Day, December 26th (possibly moved to Monday or Tuesday)</li>
 * </ul>
 * 
 * @author Srinivas Hasti TODO add LIFFE
 */

public class UnitedKingdom extends DelegateCalendar {
    public static enum Market {
        SETTLEMENT, // generic settlement calendar
        EXCHANGE, // London stock-exchange calendar
        METALS
        // London metals-exchange calendar
    };

    private final static UnitedKingdom SETTLEMENT_CALENDAR = new UnitedKingdom(Market.SETTLEMENT);
    private final static UnitedKingdom EXCHANGE_CALENDAR   = new UnitedKingdom(Market.EXCHANGE);
    private final static UnitedKingdom METALS_CALENDAR     = new UnitedKingdom(Market.METALS);

    private UnitedKingdom(Market market) {
        Calendar delegate;
        switch (market) {
            case SETTLEMENT:
                delegate = new SettlementCalendar();
                break;
            case EXCHANGE:
                delegate = new ExchangeCalendar();
                break;
            case METALS:
                delegate = new MetalsCalendar();
                break;
            default:
                throw new IllegalArgumentException("unknown market");
        }
        setDelegate(delegate);
    }

    public static UnitedKingdom getCalendar(Market market) {
        switch (market) {
            case SETTLEMENT:
                return SETTLEMENT_CALENDAR;
            case EXCHANGE:
                return EXCHANGE_CALENDAR;
            case METALS:
                return METALS_CALENDAR;
            default:
                throw new IllegalArgumentException("unknown market");
        }
    }

    private class SettlementCalendar extends WesternCalendar {

        public String getName() {
            return "UK settlement";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            int m = date.getMonth();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            // New Year's Day (possibly moved to Monday)
                    || ((d == 1 || ((d == 2 || d == 3) && w == Weekday.Monday)) && m == Month.January.toInteger())
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // first Monday of May (Early May Bank Holiday)
                    || (d <= 7 && w == Weekday.Monday && m == Month.May.toInteger())
                    // last Monday of May (Spring Bank Holiday)
                    || (d >= 25 && w == Weekday.Monday && m == Month.May.toInteger() && y != 2002)
                    // last Monday of August (Summer Bank Holiday)
                    || (d >= 25 && w == Weekday.Monday && m == Month.August.toInteger())
                    // Christmas (possibly moved to Monday or Tuesday)
                    || ((d == 25 || (d == 27 && (w == Weekday.Monday || w == Weekday.Tuesday))) && m == Month.December
                            .toInteger())
                    // Boxing Day (possibly moved to Monday or Tuesday)
                    || ((d == 26 || (d == 28 && (w == Weekday.Monday || w == Weekday.Tuesday))) && m == Month.December
                            .toInteger())
                    // June 3rd, 2002 only (Golden Jubilee Bank Holiday)
                    // June 4rd, 2002 only (special Spring Bank Holiday)
                    || ((d == 3 || d == 4) && m == Month.June.toInteger() && y == 2002)
                    // December 31st, 1999 only
                    || (d == 31 && m == Month.December.toInteger() && y == 1999))
                return false;
            return true;
        }
    }

    private class ExchangeCalendar extends WesternCalendar {

    	public String getName() {
            return "London stock exchange";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            int m = date.getMonth();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            // New Year's Day (possibly moved to Monday)
                    || ((d == 1 || ((d == 2 || d == 3) && w == Weekday.Monday)) && m == Month.January.toInteger())
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // first Monday of May (Early May Bank Holiday)
                    || (d <= 7 && w == Weekday.Monday && m == Month.May.toInteger())
                    // last Monday of May (Spring Bank Holiday)
                    || (d >= 25 && w == Weekday.Monday && m == Month.May.toInteger() && y != 2002)
                    // last Monday of August (Summer Bank Holiday)
                    || (d >= 25 && w == Weekday.Monday && m == Month.August.toInteger())
                    // Christmas (possibly moved to Monday or Tuesday)
                    || ((d == 25 || (d == 27 && (w == Weekday.Monday || w == Weekday.Tuesday))) && m == Month.December
                            .toInteger())
                    // Boxing Day (possibly moved to Monday or Tuesday)
                    || ((d == 26 || (d == 28 && (w == Weekday.Monday || w == Weekday.Tuesday))) && m == Month.December
                            .toInteger())
                    // June 3rd, 2002 only (Golden Jubilee Bank Holiday)
                    // June 4rd, 2002 only (special Spring Bank Holiday)
                    || ((d == 3 || d == 4) && m == Month.June.toInteger() && y == 2002)
                    // December 31st, 1999 only
                    || (d == 31 && m == Month.December.toInteger() && y == 1999))
                return false;
            return true;
        }
    }

    private  class MetalsCalendar extends WesternCalendar {

        public String getName() {
            return "London metals exchange";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            int m = date.getMonth();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            // New Year's Day (possibly moved to Monday)
                    || ((d == 1 || ((d == 2 || d == 3) && w == Weekday.Monday)) && m == Month.January.toInteger())
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // first Monday of May (Early May Bank Holiday)
                    || (d <= 7 && w == Weekday.Monday && m == Month.May.toInteger())
                    // last Monday of May (Spring Bank Holiday)
                    || (d >= 25 && w == Weekday.Monday && m == Month.May.toInteger() && y != 2002)
                    // last Monday of August (Summer Bank Holiday)
                    || (d >= 25 && w == Weekday.Monday && m == Month.August.toInteger())
                    // Christmas (possibly moved to Monday or Tuesday)
                    || ((d == 25 || (d == 27 && (w == Weekday.Monday || w == Weekday.Tuesday))) && m == Month.December
                            .toInteger())
                    // Boxing Day (possibly moved to Monday or Tuesday)
                    || ((d == 26 || (d == 28 && (w == Weekday.Monday || w == Weekday.Tuesday))) && m == Month.December
                            .toInteger())
                    // June 3rd, 2002 only (Golden Jubilee Bank Holiday)
                    // June 4rd, 2002 only (special Spring Bank Holiday)
                    || ((d == 3 || d == 4) && m == Month.June.toInteger() && y == 2002)
                    // December 31st, 1999 only
                    || (d == 31 && m == Month.December.toInteger() && y == 1999))
                return false;
            return true;
        }
    }
}
