/*
 Copyright (C) 2008
  
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



import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.SEPTEMBER;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.DECEMBER;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

	//! Slovak calendars
    /*! Holidays for the Bratislava stock exchange
        (data from <http://www.bsse.sk/>):
        <ul>
        <li>Saturdays</li>
        <li>Sundays</li>
        <li>New Year's Day, January 1st</li>
        <li>Epiphany, January 6th</li>
        <li>Good Friday</li>
        <li>Easter Monday</li>
        <li>May Day, May 1st</li>
        <li>Liberation of the Republic, May 8th</li>
        <li>SS. Cyril and Methodius, July 5th</li>
        <li>Slovak National Uprising, August 29th</li>
        <li>Constitution of the Slovak Republic, September 1st</li>
        <li>Our Lady of the Seven Sorrows, September 15th</li>
        <li>All Saints Day, November 1st</li>
        <li>Freedom and Democracy of the Slovak Republic, November 17th</li>
        <li>Christmas Eve, December 24th</li>
        <li>Christmas, December 25th</li>
        <li>St. Stephen, December 26th</li>
        </ul>
	*/
public class Slovakia extends DelegateCalendar {
	public static enum Market {
		BSSE //  Bratislava stock exchange of Slovakia
	};

	
	private final static Slovakia BSSE_CALENDAR = new Slovakia(
			Market.BSSE);

	private Slovakia(Market market) {
		Calendar delegate;
		switch (market) {
		case BSSE:
			delegate = new SlovakiaBSSECalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Slovakia getCalendar(Market market) {
		switch (market) {
		case BSSE:
			return BSSE_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}
}

final class SlovakiaBSSECalendar extends WesternCalendar {

	public String getName() {
		return "Bratislava stock exchange";
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
            // Epiphany
            || (d == 6 && m == JANUARY)
            // Good Friday
            || (dd == em-3)
            // Easter Monday
            || (dd == em)
            // May Day
            || (d == 1 && m == MAY)
            // Liberation of the Republic
            || (d == 8 && m == MAY)
            // SS. Cyril and Methodius
            || (d == 5 && m == JULY)
            // Slovak National Uprising
            || (d == 29 && m == AUGUST)
            // Constitution of the Slovak Republic
            || (d == 1 && m == SEPTEMBER)
            // Our Lady of the Seven Sorrows
            || (d == 15 && m == SEPTEMBER)
            // All Saints Day
            || (d == 1 && m == NOVEMBER)
            // Freedom and Democracy of the Slovak Republic
            || (d == 17 && m == NOVEMBER)
            // Christmas Eve
            || (d == 24 && m == DECEMBER)
            // Christmas
            || (d == 25 && m == DECEMBER)
            // St. Stephen
            || (d == 26 && m == DECEMBER)
            // unidentified closing days for stock exchange
            || (d >= 24 && d <= 31 && m == DECEMBER && y == 2004)
            || (d >= 24 && d <= 31 && m == DECEMBER && y == 2005))
	            return false;
	        return true;
	}
}