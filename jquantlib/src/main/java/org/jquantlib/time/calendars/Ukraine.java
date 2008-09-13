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
 Copyright (C) 2005, 2007 StatPro Italia srl

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

/**! Holidays for the Ukrainian stock exchange
 * (data from <http://www.ukrse.kiev.ua/eng/>):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Orthodox Christmas, January 7th</li>
 * <li>International Women's Day, March 8th</li>
 * <li>Easter Monday</li>
 * <li>Holy Trinity Day, 50 days after Easter</li>
 * <li>International Workers Solidarity Days, May 1st and 2nd</li>
 * <li>Victory Day, May 9th</li>
 * <li>Constitution Day, June 28th</li>
 * <li>Independence Day, August 24th</li>
 * </ul>
 * Holidays falling on a Saturday or Sunday are moved to the following Monday.
 * @author Renjith Nair
*/

public class Ukraine extends DelegateCalendar {
	public static enum Market {
		USE, //  Ukraine stock-exchange
	};

	//Ukraine stock-exchange Calendar
	private final static Ukraine USE_CALENDAR = new Ukraine(
			Market.USE);

	private Ukraine(Market market) {
		Calendar delegate;
		switch (market) {
		case USE:
			delegate = new UkraineUSECalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Ukraine getCalendar(Market market) {
		switch (market) {
		case USE:
			return USE_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}
}

final class UkraineUSECalendar extends WesternCalendar {

	public String getName() {
		return "Ukraine stock-exchange";
	}

	public boolean isBusinessDay(Date date) {
		Weekday w = date.getWeekday();
		int d = date.getDayOfMonth(), dd = date.getDayOfYear();
		int m = date.getMonth();
		int y = date.getYear();
		int em = easterMonday(y);	
		if (isWeekend(w)
	            // New Year's Day (possibly moved to Monday)
	            || ((d == 1 || ((d == 2 || d == 3) && w == Weekday.MONDAY))
	                && m == Month.JANUARY.toInteger())
	            // Orthodox Christmas
	            || ((d == 7 || ((d == 8 || d == 9) && w == Weekday.MONDAY))
	                && m == Month.JANUARY.toInteger())
	            // Women's Day
	            || ((d == 8 || ((d == 9 || d == 10) && w == Weekday.MONDAY))
	                && m == Month.MARCH.toInteger())
	            // Orthodox Easter Monday
	            || (dd == em)
	            // Holy Trinity Day
	            || (dd == em+49)
	            // Workers Solidarity Days
	            || ((d == 1 || d == 2 || (d == 3 && w ==  Weekday.MONDAY)) && m == Month.MAY.toInteger())
	            // Victory Day
	            || ((d == 9 || ((d == 10 || d == 11) && w ==  Weekday.MONDAY)) && m == Month.MAY.toInteger())
	            // Constitution Day
	            || (d == 28 && m == Month.JUNE.toInteger())
	            // Independence Day
	            || (d == 24 && m == Month.AUGUST.toInteger()))
	            return false;		
		return true;
	}
	               
}