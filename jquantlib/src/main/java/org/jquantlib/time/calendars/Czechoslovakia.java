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
import static org.jquantlib.util.Month.SEPTEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.DECEMBER;

import org.jquantlib.time.WesternCalendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

//! Czechoslovakian calendar
/*! Holidays:
    <ul>
        <li>Saturdays</li>
        <li>Sundays</li>
        <li>New Year's Day, January 1st</li> ok
        <li>Easter Monday</li>
        <li>Labour Day, May 1st</li>
        <li>Liberation Day, May 8th</li>
        <li>SS. Cyril and Methodius, July 5th</li>
        <li>Jan Hus Day, July 6th</li> 
        <li>Czech Statehood Day, September 28th</li>
        <li>Independence Day, October 28th</li>
        <li>Struggle for Freedom and Democracy Day, November 17th</li>
        <li>Christmas Eve, December 24th</li>
        <li>Christmas, December 25th</li>
        <li>St. Stephen, December 26th</li>
    </ul>

    @Author 
*/

public class Czechoslovakia extends WesternCalendar {
	private static Czechoslovakia CZECHOSLOVAKIA = new Czechoslovakia();

	private Czechoslovakia() {
	}

	public static Czechoslovakia getCalendar() {
		return CZECHOSLOVAKIA;
	}

	public boolean isBusinessDay(Date date) {
        Weekday w = date.getWeekday();
        int d = date.getDayOfMonth(), dd = date.getDayOfYear();
        Month m = date.getMonthEnum();
        int y = date.getYear();
        int em = easterMonday(y);
		//exact matching of days except for Easter Monday
        if (isWeekend(w)
            // New Year's Day 
            || (d == 1  && m == JANUARY)
            // Easter MONDAY
            || (dd == em)
            // Labour Day, 1st May
            || (d == 1 && m == MAY)
            // Liberation Day, May 8th
            || (d == 8 && m == MAY)              
            // SS. Cyril and Methodius, July 5th
            || (d == 5 && m == JULY)
			//Jan Hus Day, July 6th
			|| (d == 6 && m == JULY)
			//Czech Statehood Day, September 28th
			|| (d == 28 && m == SEPTEMBER)
			//Independence Day, October 28th
			|| (d == 28 && m == OCTOBER)
			//Struggle for Freedom and Democracy Day, November 17th
			|| (d == 17 && m == NOVEMBER)
			//Christmas Eve, December 24th
			|| (d == 24 && m == DECEMBER)
			//Christmas, December 25th
			|| (d == 25 && m == DECEMBER)
			//St. Stephen, December 26th
			|| (d == 26 && m == DECEMBER))
            return false;
        return true;

	}
    public String getName() {
       return "Czechoslovakia";
    }
    
}
