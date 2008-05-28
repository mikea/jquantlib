/*
 Copyright (C) 2008 Srinivas Hasti 
 Copyright (C) 2008 Dominik Holenstein 

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

import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * @author Srinivas Hasti 
 * @author Dominik Holenstein
 * 
 */
public class Switzerland extends DelegateCalendar {
    public static enum Market {
        SETTLEMENT, // !< generic settlement calendar
        SWX, 		// !< SWX stock-exchange
    };

    private final static Switzerland SETTLEMENT_CALENDAR 	= new Switzerland(Market.SETTLEMENT);
    private final static Switzerland SWX_CALENDAR 			= new Switzerland(Market.SWX);
 

    private Switzerland(Market market) {
        Calendar delegate;
        switch (market) {
            case SETTLEMENT:
                delegate = new SettlementCalendar();
                break;
            case SWX:
                delegate = new SWXStockExchangeCalendar();
                break;
            default:
                throw new IllegalArgumentException("unknown market");
        }
        setDelegate(delegate);
    }

    public static Switzerland getCalendar(Market market) {
        switch (market) {
            case SETTLEMENT:
                return SETTLEMENT_CALENDAR;
            case SWX:
                return SWX_CALENDAR;
            default:
                throw new IllegalArgumentException("unknown market");
        }
    }

    private  final class SettlementCalendar extends WesternCalendar {

    	public String getName() {
            return "Swiss settlement";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            		// New Year's Day
                    || (d == 1 && m == JANUARY)
                    // Berchtolds Day
                    || (d == 2 && m == JANUARY)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // Ascension Thursday
                    || (dd == em + 38)
                    // White Monday
                    || (dd == em + 49)
                    // Labour Day
                    || (d == 1 && m == MAY)
                    // National Day Switzerland
                    || (d == 1 && m == AUGUST)
                    // Christmas Eve
                    || (d == 24 && m == DECEMBER)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // Boxing Day
                    || (d == 26 && m == DECEMBER)
                    // New Year's Eve
                    || (d == 31 && m == DECEMBER))
                return false;
            return true;
        }
    }

    private final class SWXStockExchangeCalendar extends WesternCalendar {

        public String getName() {
            return "SWX stock exchange";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            		// New Year's Day
                    || (d == 1 && m == JANUARY)
                    // Berchtolds Day
                    || (d == 2 && m == JANUARY)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // Labour Day
                    || (d == 1 && m == MAY)
                    // Ascension Thursday
                    || (dd == em + 38)
                    // White Monday
                    || (dd == em + 49)
                    // National Day Switzerland
                    || (d == 1 && m == AUGUST)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // Christmas Day
                    || (d == 26 && m == DECEMBER))
                return false;
            return true;
        }
    }

  
}

