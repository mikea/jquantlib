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

package org.jquantlib.time.calendars;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.SEPTEMBER;
import static org.jquantlib.util.Month.OCTOBER;

/**
 * Taiwanese calendars Holidays for the Taiwan stock exchange
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Peace Memorial Day, February 28</li>
 * <li>Labor Day, May 1st</li>
 * <li>Double Tenth National Day, October 10th</li>
 * </ul>
 * 
 * Other holidays for which no rule is given (data available for 2002-2007
 * only:)
 * <ul>
 * <li>Chinese Lunar New Year</li>
 * <li>Tomb Sweeping Day</li>
 * <li>Dragon Boat Festival</li>
 * <li>Moon Festival</li>
 * </ul>
 * 
 * @category calendars
 * 
 * @see <a href="http://www.tse.com.tw/en/trading/trading_days.php">Taiwan Stock Exchange</a>
 * 
 * @author Renjith Nair
 * @author Jia Jia
 * 
 */
public class Taiwan extends DelegateCalendar {

	private final static Taiwan TSE_Calendar = new Taiwan(Market.TSE);

	private Taiwan(Market market) {
		Calendar delegate;
		switch (market) {
		case TSE:
			delegate = new TaiwanTSECalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Taiwan getCalendar(Market market) {
		switch (market) {
		case TSE:
			return TSE_Calendar;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}

	//
	// public enums
	//
	
	//FIXME: Settlement calendar is missing
	public enum Market {
		/**
		 * TaiWan Stock Exchange
		 */
		TSE
	}


	//
	// private final classes
	//
	
	private final class TaiwanTSECalendar extends WesternCalendar {
	    @Override
		public String getName() {
			return "Taiwan stock exchange";
		}

		public boolean isBusinessDay(final Date date /* @ReadOnly */) /* @ReadOnly */{
			Weekday w = date.getWeekday();
			int d = date.getDayOfMonth();
			Month m = date.getMonthEnum();
			int y = date.getYear();

			if (isWeekend(w)
			// New Year's Day
					|| (d == 1 && m == JANUARY)
					// Peace Memorial Day
					|| (d == 28 && m == FEBRUARY)
					// Labor Day
					|| (d == 1 && m == MAY && y != 2005) // In 2005, 2 May makes
															// up for Labor Day
					// Double Tenth
					|| (d == 10 && m == OCTOBER))
				return false;

			if (y == 2002) {
				// Dragon Boat Festival and Moon Festival fall on Saturday
				if (// Chinese Lunar New Year
				(d >= 9 && d <= 17 && m == FEBRUARY)
				// Tomb Sweeping Day
						|| (d == 5 && m == APRIL))
					return false;
			}
			if (y == 2003) {
				// Tomb Sweeping Day falls on Saturday
				if (// Chinese Lunar New Year
				((d >= 31 && m == JANUARY) || (d <= 5 && m == FEBRUARY))
				// Dragon Boat Festival
						|| (d == 4 && m == JUNE)
						// Moon Festival
						|| (d == 11 && m == SEPTEMBER))
					return false;
			}
			if (y == 2004) {
				// Tomb Sweeping Day falls on Sunday
				if (// Chinese Lunar New Year
				(d >= 21 && d <= 26 && m == JANUARY)
				// Dragon Boat Festival
						|| (d == 22 && m == JUNE)
						// Moon Festival
						|| (d == 28 && m == SEPTEMBER))
					return false;
			}
			if (y == 2005) {
				// Dragon Boat and Moon Festival fall on Saturday or Sunday
				if (// Chinese Lunar New Year
				(d >= 4 && d <= 13 && m == FEBRUARY) // JIA: This should really start
														// from 4 Feb, see the
														// website on the top
						// Tomb Sweeping Day
						|| (d == 5 && m == APRIL)
						// make up for Labor Day, not seen in other years
						|| (d == 2 && m == MAY))
					return false;
			}
			if (y == 2006) {
				// Dragon Boat and Moon Festival fall on Saturday or Sunday
				if (// Chinese Lunar New Year
				((d >= 28 && m == JANUARY) || (d <= 5 && m == FEBRUARY))
				// Tomb Sweeping Day
						|| (d == 5 && m == APRIL)
						// Dragon Boat Festival
						|| (d == 31 && m == MAY)
						// Moon Festival
						|| (d == 6 && m == OCTOBER))
					return false;
			}
			if (y == 2007) {
				if (// Chinese Lunar New Year
				(d >= 17 && d <= 25 && m == FEBRUARY)
				// Tomb Sweeping Day
						|| (d == 5 && m == APRIL)
						// adjusted holidays
						|| (d == 6 && m == APRIL) || (d == 18 && m == JUNE)
						// Dragon Boat Festival
						|| (d == 19 && m == JUNE)
						// adjusted holiday
						|| (d == 24 && m == SEPTEMBER)
						// Moon Festival
						|| (d == 25 && m == SEPTEMBER))
					return false;
			}
			if (y == 2008) {
				if (// Chinese Lunar New Year
				(d >= 4 && d <= 11 && m == FEBRUARY)
				// Tomb Sweeping Day
						|| (d == 4 && m == APRIL))
					return false;
			}
			return true;
		}
	}

}
