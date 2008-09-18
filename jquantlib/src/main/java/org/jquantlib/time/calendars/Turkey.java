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
 Copyright (C) 2005 Sercan Atalik

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

/** Holidays for the National Stock Exchange
 * 	<ul>
 *  <li>Saturdays</li>
 *  <li>Sundays</li>
 *  <li>New Year's Day, January 1st</li>
 *  <li>National Holidays (April 23rd, May 19th, August 30th, October 29th</li>
 *  <li>Local Holidays (Kurban, Ramadan; 2004 to 2009 only) </li>
 *  </ul>
 *  @author Renjith Nair
*/

public class Turkey extends DelegateCalendar {
	public static enum Market {
		ISE, // Istanbul Stock Exchange Turkey
	};

	//Istanbul Stock Exchange Turkey Calendar
	private final static Turkey ISE_CALENDAR = new Turkey(
			Market.ISE);

	private Turkey(Market market) {
		Calendar delegate;
		switch (market) {
		case ISE:
			delegate = new TurkeyISECalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Turkey getCalendar(Market market) {
		switch (market) {
		case ISE:
			return ISE_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}
}

final class TurkeyISECalendar extends WesternCalendar {

	public String getName() {
		return "Istanbul Stock Exchange Turkey";
	}

	public boolean isBusinessDay(Date date) {
		Weekday w = date.getWeekday();
		int d = date.getDayOfMonth();
		int m = date.getMonth();
		int y = date.getYear();
		if (isWeekend(w)
            // New Year's Day
            || (d == 1 && m == Month.JANUARY.toInteger())
            // 23 nisan / National Holiday
            || (d == 23 && m == Month.APRIL.toInteger())
            // 19 may/ National Holiday
            || (d == 19 && m == Month.MAY.toInteger())
            // 30 aug/ National Holiday
            || (d == 30 && m == Month.AUGUST.toInteger())
            ///29 ekim  National Holiday
            || (d == 29 && m == Month.OCTOBER.toInteger()))
            return false;

        // Local Holidays
        if (y == 2004) {
            // kurban
            if ((m == Month.FEBRUARY.toInteger() && d <= 4)
            // ramazan
                || (m == Month.NOVEMBER.toInteger() && d >= 14 && d <= 16))
                return false;
        } else if (y == 2005) {
            // kurban
            if ((m == Month.JANUARY.toInteger() && d >= 19 && d <= 21)
            // ramazan
                || (m ==  Month.NOVEMBER.toInteger() && d >= 2 && d <= 5))
                return false;
        } else if (y == 2006) {
            // kurban
            if ((m == Month.JANUARY.toInteger() && d >= 9 && d <= 13)
            // ramazan
                || (m == Month.OCTOBER.toInteger() && d >= 23 && d <= 25)
            // kurban
                || (m == Month.DECEMBER.toInteger() && d >= 30))
                return false;
        } else if (y == 2007) {
            // kurban
            if ((m == Month.JANUARY.toInteger() && d <= 4)
            // ramazan
                || (m == Month.OCTOBER.toInteger() && d >= 11 && d <= 14)
            // kurban
                || (m == Month.DECEMBER.toInteger() && d >= 19 && d <= 23))
                return false;
        } else if (y == 2008) {
            // ramazan
            if ((m == Month.SEPTEMBER.toInteger() && d >= 29)
                || (m == Month.OCTOBER.toInteger() && d <= 2)
                // kurban
                || (m == Month.DECEMBER.toInteger() && d >= 7 && d <= 11))
                return false;
		}
		return true;
	}
}