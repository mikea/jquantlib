/*
 Copyright (c)  Q Boiler 

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
 * Mexican calendar
 * <p>
 * Banking holidays:
 * <ul>
 * <li>TODO: List the banking holidays must be filled in here
 * </ul>
 * <p>
 * Mexican calendars Holidays for the Mexican stock exchange:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Constitution Day, February 5th</li>
 * <li>Birthday of Benito Juarez, March 21st</li>
 * <li>Holy Thursday</li>
 * <li>Good Friday</li>
 * <li>Labour Day, May 1st</li>
 * <li>National Day, September 16th</li>
 * <li>Our Lady of Guadalupe, December 12th</li>
 * <li>Christmas, December 25th</li>
 * </ul>
 * 
 * @category calendars
 * 
 * @see <a href="http://www.bmv.com.mx/">Bolsa Mexicana de Valores</a>
 * 
 * @author Q Boiler
 */
public class Mexico extends DelegateCalendar {

	private final static Mexico SETTLEMENT_CALENDAR = new Mexico(Market.SETTLEMENT);
	private final static Mexico BMV_CALENDAR        = new Mexico(Market.BMV);

	private Mexico(Market market) {
		Calendar delegate;
		switch (market) {
		case SETTLEMENT:
			delegate = new MexicoSettlementCalendar();
			break;
		case BMV:
			delegate = new BMVExchangeCalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Mexico getCalendar(Market market) {
		switch (market) {
		case SETTLEMENT:
			return SETTLEMENT_CALENDAR;
		case BMV:
			return BMV_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}


	//
	// public enums
	//
	
	public enum Market {
		
		/**
		 * Generic settlement calendar
		 */
		SETTLEMENT,
		
		/**
		 * Bolsa Mexicana de Valores
		 */
		BMV
	}

	
    //
    // private inner classes
    //
    
	private final class MexicoSettlementCalendar extends WesternCalendar {

		public String getName() {
			return "Mexico stock Market";
		}

		public boolean isBusinessDay(Date date) {
			final Weekday w = date.getWeekday();
			final int d = date.getDayOfMonth();
			final Month m = date.getMonthEnum();
			final int y = date.getYear();
			final int dd = date.getDayOfYear();
			final int em = easterMonday(y);

			if (isWeekend(w)
					// New Year's Day
					|| (d == 1 && m == Month.JANUARY)
					// Constitution Day
					|| (d <= 7 && w.equals(Weekday.MONDAY) && m == Month.FEBRUARY)
					// Birthday of Benito Juarez
					|| (d == 21 && m == Month.MARCH)
					// Holy Thursday
					|| (dd == em - 4)
					// Good Friday
					|| (dd == em - 3)
					// Labour Day
					|| (d == 1 && m == Month.MAY)
					// National Day
					|| (d == 16 && m == Month.SEPTEMBER)
					// All Soul's Day
					|| (d == 2 && m == Month.NOVEMBER)
					// Mexican Revolution - 3rd Monday of November
					|| (d >= 14 && d < 21 && w.equals(Weekday.MONDAY) && m == Month.NOVEMBER)
					// Our Lady of Guadalupe
					|| (d == 12 && m == Month.DECEMBER)
					// Christmas
					|| (d == 25 && m == Month.DECEMBER))
				return false;
			
			return true;
		}

	}


	final private class BMVExchangeCalendar extends WesternCalendar {

		public String getName() {
			return "Mexican Stock Exchange";
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
					// Constitution Day - 1st Monday of November
					|| (d <= 7 && w.equals(Weekday.MONDAY) && m == Month.FEBRUARY)
					// Birthday of Benito Juarez
					|| (d == 21 && m == Month.MARCH)
					// Holy Thursday
					|| (dd == em - 4)
					// Good Friday
					|| (dd == em - 3)
					// Labour Day
					|| (d == 1 && m == Month.MAY)
					// National Day
					|| (d == 16 && m == Month.SEPTEMBER)
					// All Soul's Day
					|| (d == 2 && m == Month.NOVEMBER)
					// Mexican Revolution - 3rd Monday of November
					|| (d >= 14 && d < 21 && w.equals(Weekday.MONDAY) && m == Month.NOVEMBER)
					// Our Lady of Guadalupe
					|| (d == 12 && m == Month.DECEMBER)
					// Christmas
					|| (d == 25 && m == Month.DECEMBER))
				return false;
			
			return true;
		}
	}

}
