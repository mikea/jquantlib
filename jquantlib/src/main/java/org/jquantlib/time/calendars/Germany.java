/*
 Copyright (C) 2008 Srinivas Hasti
 
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

import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * @author Srinivas Hasti
 * 
 */
public class Germany extends DelegateCalendar {
	public static enum Market {
		SETTLEMENT, // !< generic settlement calendar
		FRANKFURTSTOCKEXCHANGE, // !< Frankfurt stock-exchange
		XETRA, // !< Xetra
		EUREX
		// !< Eurex
	};

	private final static Germany SETTLEMENT_CALENDAR = new Germany(
			Market.SETTLEMENT);
	private final static Germany FRANKFURTSTOCKEXCHANGE_CALENDAR = new Germany(
			Market.FRANKFURTSTOCKEXCHANGE);
	private final static Germany XETRA_CALENDAR = new Germany(Market.XETRA);
	private final static Germany EUREX_CALENDAR = new Germany(Market.EUREX);

	private Germany(Market market) {
		Calendar delegate;
		switch (market) {
		case SETTLEMENT:
			delegate = new GermanySettlementCalendar();
			break;
		case FRANKFURTSTOCKEXCHANGE:
			delegate = new FrankfurtStockExchangeCalendar();
			break;
		case XETRA:
			delegate = new XetraCalendar();
			break;
		case EUREX:
			delegate = new EurexCalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Germany getCalendar(Market market) {
		switch (market) {
		case SETTLEMENT:
			return SETTLEMENT_CALENDAR;
		case FRANKFURTSTOCKEXCHANGE:
			return FRANKFURTSTOCKEXCHANGE_CALENDAR;
		case XETRA:
			return XETRA_CALENDAR;
		case EUREX:
			return EUREX_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}
}

final class GermanySettlementCalendar extends WesternCalendar {

	public String getName() {
		return "German settlement";
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
				// Good Friday
				|| (dd == em - 3)
				// Easter Monday
				|| (dd == em)
				// Ascension Thursday
				|| (dd == em + 38)
				// Whit Monday
				|| (dd == em + 49)
				// Corpus Christi
				|| (dd == em + 59)
				// Labour Day
				|| (d == 1 && m == MAY)
				// National Day
				|| (d == 3 && m == OCTOBER)
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

final class FrankfurtStockExchangeCalendar extends WesternCalendar {

	public String getName() {
		return "Frankfurt stock exchange";
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
				// Good Friday
				|| (dd == em - 3)
				// Easter Monday
				|| (dd == em)
				// Labour Day
				|| (d == 1 && m == MAY)
				// Christmas' Eve
				|| (d == 24 && m == DECEMBER)
				// Christmas
				|| (d == 25 && m == DECEMBER)
				// Christmas Day
				|| (d == 26 && m == DECEMBER)
				// New Year's Eve
				|| (d == 31 && m == DECEMBER))
			return false;
		return true;
	}
}

final class XetraCalendar extends WesternCalendar {

	public String getName() {
		return "Xetra";
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
				// Good Friday
				|| (dd == em - 3)
				// Easter Monday
				|| (dd == em)
				// Labour Day
				|| (d == 1 && m == MAY)
				// Christmas' Eve
				|| (d == 24 && m == DECEMBER)
				// Christmas
				|| (d == 25 && m == DECEMBER)
				// Christmas Day
				|| (d == 26 && m == DECEMBER)
				// New Year's Eve
				|| (d == 31 && m == DECEMBER))
			return false;
		return true;
	}
}

final class EurexCalendar extends WesternCalendar {

	public String getName() {
		return "Eurex";
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
				// Good Friday
				|| (dd == em - 3)
				// Easter Monday
				|| (dd == em)
				// Labour Day
				|| (d == 1 && m == MAY)
				// Christmas' Eve
				|| (d == 24 && m == DECEMBER)
				// Christmas
				|| (d == 25 && m == DECEMBER)
				// Christmas Day
				|| (d == 26 && m == DECEMBER)
				// New Year's Eve
				|| (d == 31 && m == DECEMBER))
			return false;
		return true;
	}
}
