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
 Copyright (C) 2004 FIMAT Group
 Copyright (C) 2005, 2006, 2007 StatPro Italia srl

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.
*/


package org.jquantlib.time.calendars;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/** Holidays for the Taiwan stock exchange
 *  (data from <http://www.tse.com.tw/en/trading/trading_days.php>):
 *  <ul>
 *  <li>Saturdays</li>
 *  <li>Sundays</li>
 *  <li>New Year's Day, January 1st</li>
 *  <li>Peace Memorial Day, February 28</li>
 *  <li>Labor Day, May 1st</li>
 *  <li>Double Tenth National Day, October 10th</li>
 *  </ul>
 *  Other holidays for which no rule is given
 *  (data available for 2002-2007 only:)
 *  <ul>
 *  <li>Chinese Lunar New Year</li>
 *  <li>Tomb Sweeping Day</li>
 *  <li>Dragon Boat Festival</li>
 *  <li>Moon Festival</li>
 *  </ul>
 *  @author Renjith Nair
*/

public class Taiwan extends DelegateCalendar {
	public static enum Market {
		TSE, // Taiwan Stock Exchange 
	};

	//Taiwan Stock Exchange Calendar
	private final static Taiwan TSE_CALENDAR = new Taiwan(
			Market.TSE);

	private Taiwan(Market market) {
		Calendar delegate;
		switch (market) {
		case TSE:
			delegate = new TaiwanSECalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Taiwan getCalendar(Market market) {
		switch (market) {
		case TSE:
			return TSE_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}
}

final class TaiwanSECalendar extends WesternCalendar {

	public String getName() {
		return "Taiwan Stock Exchange";
	}

	public boolean isBusinessDay(Date date) {
		Weekday w = date.getWeekday();
		int d = date.getDayOfMonth();
		int m = date.getMonth();
		int y = date.getYear();
		if (isWeekend(w)
	            // New Year's Day
	            || (d == 1 && m == Month.JANUARY.toInteger())
	            // Peace Memorial Day
	            || (d == 28 && m == Month.FEBRUARY.toInteger())
	            // Labor Day
	            || (d == 1 && m == Month.MAY.toInteger())
	            // Double Tenth
	            || (d == 10 && m == Month.OCTOBER.toInteger())
	            )
	            return false;

        if (y == 2002) {
            // Dragon Boat Festival and Moon Festival fall on Saturday
            if (// Chinese Lunar New Year
                (d >= 9 && d <= 17 && m == Month.FEBRUARY.toInteger())
                // Tomb Sweeping Day
                || (d == 5 && m == Month.APRIL.toInteger())
                )
                return false;
        }
        if (y == 2003) {
            // Tomb Sweeping Day falls on Saturday
            if (// Chinese Lunar New Year
                ((d >= 31 && m == Month.JANUARY.toInteger()) || (d <= 5 && m == Month.FEBRUARY.toInteger()))
                // Dragon Boat Festival
                || (d == 4 && m == Month.JUNE.toInteger())
                // Moon Festival
                || (d == 11 && m == Month.SEPTEMBER.toInteger())
                )
                return false;
        }
        if (y == 2004) {
            // Tomb Sweeping Day falls on Sunday
            if (// Chinese Lunar New Year
                (d >= 21 && d <= 26 && m == Month.JANUARY.toInteger())
                // Dragon Boat Festival
                || (d == 22 && m == Month.JUNE.toInteger())
                // Moon Festival
                || (d == 28 && m == Month.SEPTEMBER.toInteger())
                )
                return false;
        }
        if (y == 2005) {
            // Dragon Boat and Moon Festival fall on Saturday or Sunday
            if (// Chinese Lunar New Year
                (d >= 6 && d <= 13 && m == Month.FEBRUARY.toInteger())
                // Tomb Sweeping Day
                || (d == 5 && m == Month.APRIL.toInteger())
                // make up for Labor Day, not seen in other years
                || (d == 2 && m == Month.MAY.toInteger())
                )
                return false;
        }
        if (y == 2006) {
            // Dragon Boat and Moon Festival fall on Saturday or Sunday
            if (// Chinese Lunar New Year
                ((d >= 28 && m == Month.JANUARY.toInteger()) || (d <= 5 && m == Month.FEBRUARY.toInteger()))
                // Tomb Sweeping Day
                || (d == 5 && m == Month.APRIL.toInteger())
                // Dragon Boat Festival
                || (d == 31 && m == Month.MAY.toInteger())
                // Moon Festival
                || (d == 6 && m == Month.OCTOBER.toInteger())
                )
                return false;
        }
        if (y == 2007) {
            if (// Chinese Lunar New Year
                (d >= 17 && d <= 25 && m == Month.FEBRUARY.toInteger())
                // Tomb Sweeping Day
                || (d == 5 && m == Month.APRIL.toInteger())
                // adjusted holidays
                || (d == 6 && m == Month.APRIL.toInteger())
                || (d == 18 && m == Month.JUNE.toInteger())
                // Dragon Boat Festival
                || (d == 19 && m == Month.JUNE.toInteger())
                // adjusted holiday
                || (d == 24 && m == Month.SEPTEMBER.toInteger())
                // Moon Festival
                || (d == 25 && m == Month.SEPTEMBER.toInteger())
                )
                return false;
        }
        if (y == 2008) { // Added 2008 data from Quantlib 0.9.6
            if (// Chinese Lunar New Year
                (d >= 4 && d <= 11 && m == Month.FEBRUARY.toInteger())
                // Tomb Sweeping Day
                || (d == 4 && m == Month.APRIL.toInteger())
                )
                return false;
        }
        return true;
	}
}