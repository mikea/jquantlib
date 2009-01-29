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
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl

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
import org.jquantlib.time.calendars.Poland.Market;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/** Holidays for Sweden
 *  <ul>
 *  <li>Saturdays</li>
 *  <li>Sundays</li>
 *  <li>New Year's Day, January 1st</li>
 *  <li>Epiphany, January 6th</li>
 *  <li>Good Friday</li>
 *  <li>Easter Monday</li>
 *  <li>Ascension</li>
 *  <li>Whit(Pentecost) Monday </li>
 *  <li>May Day, May 1st</li>
 *  <li>National Day, June 6th</li>
 *  <li>Midsummer Eve (Friday between June 18-24)</li>
 *  <li>Christmas Eve, December 24th</li>
 *  <li>Christmas Day, December 25th</li>
 *  <li>Boxing Day, December 26th</li>
 *  <li>New Year's Eve, December 31th</li>
 *  </ul>
 *  @author Renjith Nair
*/

public class Sweden extends DelegateCalendar {

	private final static Sweden SSE_CALENDAR = new Sweden(Market.SSE);

	private Sweden(Market market) {
		Calendar delegate;
		switch (market) {
		case SSE:
			delegate = new SwedenSECalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Sweden getCalendar(Market market) {
		switch (market) {
		case SSE:
			return SSE_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}


	//
	// public enums
	//
	
	public enum Market {
		SSE, // Sweden Stock Exchange 
	}

	
	//
	// private inner classes
	//

	final private class SwedenSECalendar extends WesternCalendar {

		public String getName() {
			return "Sweden Stock Exchange";
		}

		public boolean isBusinessDay(Date date) {
			Weekday w = date.getWeekday();
			int d = date.getDayOfMonth(),dd = date.getDayOfYear();;
			int m = date.getMonth();
			int y = date.getYear();
			int em = easterMonday(y);
			if (isWeekend(w)
		            // Good Friday
		            || (dd == em-3)
		            // Easter Monday
		            || (dd == em)
		            // Ascension Thursday
		            || (dd == em+38)
		            // Whit Monday
		            || (dd == em+49)
		            // New Year's Day
		            || (d == 1  && m == Month.JANUARY.toInteger())
		            // Epiphany
		            || (d == 6  && m == Month.JANUARY.toInteger())
		            // May Day
		            || (d == 1  && m == Month.MAY.toInteger())
		            // June 6 id National Day but is not a holiday.
		            // It has been debated wheter or not this day should be
		            // declared as a holiday.
		            // As of 2002 the Stockholmborsen is open that day
		            // || (d == 6  && m == June)
		            // Midsummer Eve (Friday between June 18-24)
		            || (w == Weekday.FRIDAY && (d >= 18 && d <= 24) && m == Month.JUNE.toInteger())
		            // Christmas Eve
		            || (d == 24 && m == Month.DECEMBER.toInteger())
		            // Christmas Day
		            || (d == 25 && m == Month.DECEMBER.toInteger())
		            // Boxing Day
		            || (d == 26 && m == Month.DECEMBER.toInteger())
		            // New Year's Eve
		            || (d == 31 && m == Month.DECEMBER.toInteger()))
		            return false;
		    return true;
		}
	}

}