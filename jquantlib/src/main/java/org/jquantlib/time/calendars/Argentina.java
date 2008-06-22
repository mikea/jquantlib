/*
 Copyright (C) 2008 Srinivas Hasti
 Copyright (C) 2008 Dominik Holenstein
 
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

import static org.jquantlib.util.Month.JULY;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

// ! Argentinian calendar
// 
/**
 * Source: <a href="http://www.merval.sba.com.ar/merval/html/mv_feriados_burstiles.htm">BCBA Holidays</a> <p>
 * 
 * <strong>Banking holidays:</strong>
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Holy Thursday</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Dia del Veterano y de los Caidos en la Guerra de Malvinas, 2nd April</li>
 * <li>Labour Day, May 1st</li>
 * <li>Death of General Manuel Belgrano, third Monday of June</li>
 * <li>Independence Day, July 9th</li>
 * <li>Death of General Jose de San Martin, third Monday of August</li>
 * <li>Independence Day, September 21th</li>
 * <li>Columbus Day, October 12th</li>
 * <li>All Souls Day, November 2nd</li>
 * <li>Republic Day, November 15th</li>
 * <li>Immaculate Conception, December 8th</li>
 * <li>Christmas Eve, December 24th</li>
 * <li>Christmas, December 25th</li>
 * <li>New Years's Eve, December 31th</li>
 * <li>Passion of Christ</li>
 * <li>Corpus Christi</li>
 * </ul>
 * 
 * Holidays for the BCBA stock exchange
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Holy Thursday</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Dia del Veterano y de los Caidos en la Guerra de Malvinas, 2nd April</li>
 * <li>Labour Day, May 1st</li>
 * <li>Death of General Manuel Belgrano, third Monday of June</li>
 * <li>Independence Day, July 9th</li>
 * <li>Death of General Jose de San Martin, third Monday of August</li>
 * <li>Immaculate Conception, December 8th</li>
 * <li>Christmas, December 25th</li>
 * </ul>
 * 
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 */
public class Argentina extends DelegateCalendar {
	
    public static enum Market {
        SETTLEMENT, 
        BCBA
    };

    private final static Argentina SETTLEMENT_CALENDAR = new Argentina(Market.SETTLEMENT);
    private final static Argentina EXCHANGE_CALENDAR   = new Argentina(Market.BCBA);

    private Argentina(Market market) {
        Calendar delegate;
        switch (market) {
            case SETTLEMENT:
                delegate = new SettlementCalendar();
                break;
            case BCBA:
                delegate = new BCBAExchangeCalendar();
                break;
            default:
                throw new IllegalArgumentException("unknown market");
        }
        setDelegate(delegate);
    }

    public static Argentina getCalendar(Market market) {
        switch (market) {
            case SETTLEMENT:
                return SETTLEMENT_CALENDAR;
            case BCBA:
                return EXCHANGE_CALENDAR;
            default:
                throw new IllegalArgumentException("unknown market");
        }
    }

    //TODO: Tests. 
    private final class SettlementCalendar extends WesternCalendar {

        public String getName() {
            return "Argentina";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            int dd = date.getDayOfYear();
            int em = easterMonday(y);

            if (isWeekend(w)
            		// New Year's Day
                    || (d == 1 && m == Month.JANUARY)
                    // D�a del Veterano y de los Ca�dos en la Guerra de Malvinas
                    || (d == 2 && m == Month.APRIL)
                    // Labor Day
                    || (d == 1 && m == Month.MAY)
                    // Revolution, May 25th 
                    || (d == 25 && m == Month.MAY)
                    // Death of General Manuel Belgrano
                    || (d >= 15 && d <= 21 && w == Weekday.MONDAY && m == Month.JUNE)
                    // Independence Day
                    || (d == 9 && m == Month.JULY)
                    // Death of General Jos� de San Martin
                    || (d >= 15 && d <= 21 && w == Weekday.MONDAY && m == Month.AUGUST)
                    // Columbus Day
                    || ((d == 10 || d == 11 || d == 12 || d == 15 || d == 16)
                    	&& w == Weekday.MONDAY && m == Month.OCTOBER)
                    // Holy Thursday
                    || (dd == em - 4)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // Corpus Christi
                    // || (dd == em + 59)
                    // All Souls Day
                    || (d == 2 && m == Month.NOVEMBER)
                    // Immaculate Conception
                    || (d == 8 && m == Month.DECEMBER)
                    // Christmas Eve
                    || (d == 24 && m == Month.DECEMBER)
                    // Christmas Day
                	|| (d == 25 && m == Month.DECEMBER)
            		// New Year's Eve
            		|| ((d == 31 || (d ==  30 && w == Weekday.FRIDAY)) && m == Month.DECEMBER))
                return false;
            return true;
        }

    }

    //TODO: Tests.
    private final class BCBAExchangeCalendar extends WesternCalendar {
    	
        public String getName() {
            return "BCBA";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            int dd = date.getDayOfYear();
            int em = easterMonday(y);

            if (isWeekend(w)
            		// New Year's Day / Ano Nuevo
                    || (d == 1 && m == Month.JANUARY)
                    // Holy Thursday
                    || (dd == em - 4)
                    // Good Friday
                    || (dd == em - 3)
                    // Easter Monday
                    || (dd == em)
                    // D�a del Veterano y de los Ca�dos en la Guerra de Malvinas
                    || (d == 2 && m == Month.APRIL)
                    // Labor Day
                    || (d == 1 && m == Month.MAY)
                    // Death of General Manuel Belgrano
                    || (d >= 15 && d <= 21 && w == Weekday.MONDAY && m == Month.JUNE)
                    // Independence Day
                    || (d == 9 && m == JULY)
                    // Death of General Jos� de San Martin
                    || (d >= 15 && d <= 21 && w == Weekday.MONDAY && m == Month.AUGUST)
                    // Immaculate Conception
                    || (d == 8 && m == Month.DECEMBER)
                    // Christmas Day
                	|| (d == 25 && m == Month.DECEMBER))
            	return false;
            return true;
        }
    }
}
