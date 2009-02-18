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

import static org.jquantlib.time.Weekday.FRIDAY;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

//  Brazilian calendar
/**
 * Banking holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Tiradentes's Day, April 21th</li>
 * <li>Labour Day, May 1st</li>
 * <li>Independence Day, September 21th</li>
 * <li>Nossa Sra. Aparecida Day, October 12th</li>
 * <li>All Souls Day, November 2nd</li>
 * <li>Republic Day, November 15th</li>
 * <li>Black Awareness Day, November 20th</li>
 * <li>Christmas, December 25th</li>
 * <li>Passion of Christ</li>
 * <li>Carnival</li>
 * <li>Corpus Christi</li>
 * </ul>
 * 
 * Holidays for the Bovespa stock exchange
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Sao Paulo City Day, January 25th</li>
 * <li>Tiradentes's Day, April 21th</li>
 * <li>Labour Day, May 1st</li>
 * <li>Revolution Day, July 9th</li>
 * <li>Independence Day, September 21th</li>
 * <li>Nossa Sra. Aparecida Day, October 12th</li>
 * <li>All Souls Day, November 2nd</li>
 * <li>Republic Day, November 15th</li>
 * <li>Black Awareness Day, November 20th (since 2007)</li>
 * <li>Christmas, December 25th</li>
 * <li>Passion of Christ</li>
 * <li>Carnival</li>
 * <li>Corpus Christi</li>
 * <li>the last business day of the year</li>
 * </ul>
 * 
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 */
public class Brazil extends DelegateCalendar {

	private final static Brazil SETTLEMENT_CALENDAR = new Brazil(Market.SETTLEMENT);
	private final static Brazil EXCHANGE_CALENDAR   = new Brazil(Market.BOVESPA);

	private Brazil(Market market) {
		Calendar delegate;
		switch (market) {
		case SETTLEMENT:
			delegate = new BrazilSettlementCalendar();
			break;
		case BOVESPA:
			delegate = new BrazilExchangeCalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Brazil getCalendar(Market market) {
		switch (market) {
		case SETTLEMENT:
			return SETTLEMENT_CALENDAR;
		case BOVESPA:
			return EXCHANGE_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}


	//
	// public enums
	//
	
	public static enum Market {
		/**
		 * Brazilian settlement calendar
		 */
		SETTLEMENT,
		
		/**
		 * BOVESPA
		 */
		BOVESPA
	}

	
	//
	// private inner classes
	//

	private static final class BrazilSettlementCalendar extends WesternCalendar {
	
		public String getName() {
			return "Brazil";
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
					// Tiradentes Day
					|| (d == 21 && m == Month.APRIL)
					// Labor Day
					|| (d == 1 && m == Month.MAY)
					// Independence Day
					|| (d == 7 && m == Month.SEPTEMBER)
					// Nossa Sra. Aparecida Day
					|| (d == 12 && m == Month.OCTOBER)
					// All Souls Day
					|| (d == 2 && m == Month.NOVEMBER)
					// Republic Day
					|| (d == 15 && m == Month.NOVEMBER)
					// Christmas
					|| (d == 25 && m == Month.DECEMBER)
					// Passion of Christ
					|| (dd == em - 3)
					// Carnival
					|| (dd == em - 49 || dd == em - 48)
					// Corpus Christi
					|| (dd == em + 59))
				return false;
			return true;
		}
	
	}
	
	private static final class BrazilExchangeCalendar extends WesternCalendar {
	
		public String getName() {
			return "BOVESPA";
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
					|| (d == 1 && m == JANUARY)
					// Sao Paulo City Day
					|| (d == 25 && m == JANUARY)
					// Tiradentes Day
					|| (d == 21 && m == APRIL)
					// Labor Day
					|| (d == 1 && m == MAY)
					// Revolution Day
					|| (d == 9 && m == JULY)
					// Nossa Sra. Aparecida Day
					// || (d == 12 && m == OCTOBER)-> not closed at the 12th October
					// All Souls Day
					// || (d == 2 && m == NOVEMBER) -> not closed at the 2nd
					// November
					// Republic Day
					// || (d == 15 && m == NOVEMBER) -> not closed at the 15th
					// November
					// Black Awareness Day
					|| (d == 20 && m == NOVEMBER && y >= 2007)
					// Christmas Eve
					|| (d == 24 && m == DECEMBER)
					// Christmas
					|| (d == 25 && m == DECEMBER)
					// Passion of Christ / Good Friday
					|| (dd == em - 3)
					// Carnival
					|| (dd == em - 49 || dd == em - 48)
					// Corpus Christi
					|| (dd == em + 59)
					// last business day of the year
					|| (m == DECEMBER && (d == 31 || (d >= 29 && w == FRIDAY))))
				return false;
			return true;
		}
	}

}
