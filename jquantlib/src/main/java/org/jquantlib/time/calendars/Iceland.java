/*
 Copyright (C) 2008 Siju Odeyemi
 
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

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * @author Siju Odeyemi
 */
public class Iceland extends DelegateCalendar {

	private final static Iceland ICEX_CALENDAR = new Iceland(Market.ICEX);

	private Iceland(Market market) {
		Calendar delegate;
		switch (market) {
		case ICEX:
			delegate = new IcelandICEXCalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Iceland getCalendar(Market market) {
		switch (market) {
		case ICEX:
			return ICEX_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}


	//
	// public enums
	//
	
	public enum Market {
		/**
		 * Iceland Stock Exchange 
		 */
		ICEX 
	}

	
	//
	// private inner classes
	//

	private static final class IcelandICEXCalendar extends WesternCalendar {

		public String getName() {
			return "ICEX";
		}

		public boolean isBusinessDay(Date date) {
	        Weekday w = date.getWeekday();
	         
	        int d = date.getDayOfMonth(), dd = date.getDayOfYear();
	        int m = date.getMonth();
	        int y = date.getYear();
	        int em = easterMonday(y);
	        if(isWeekend(w)
	            // New Year's Day (possibly moved to Monday)
	            || ((d == 1 || ((d == 2 || d == 3) && w == Weekday.MONDAY)) && m == Month.JANUARY.toInteger())
	            // Holy Thursday
	            || (dd == em-4)
	            // Good Friday
	            || (dd == em-3)
	            // Easter Monday
	            || (dd == em)
	            // First day of Summer
	            || (d >= 19 && d <= 25 && w == Weekday.THURSDAY && m == Month.APRIL.toInteger())
	            // Ascension Thursday
	            || (dd == em+38)
	            // Pentecost Monday
	            || (dd == em+49)
	            // Labour Day
	            || (d == 1 && m == Month.MAY.toInteger())
	            // Independence Day
	            || (d == 17 && m == Month.JUNE.toInteger())
	            // Commerce Day
	            || (d <= 7 && w == Weekday.MONDAY && m == Month.AUGUST.toInteger())
	            // Christmas
	            || (d == 25 && m == Month.DECEMBER.toInteger())
	            // Boxing Day
	            || (d == 26 && m == Month.DECEMBER.toInteger()))

	        	return false;      
		
			return true;  
		}
	}

}
