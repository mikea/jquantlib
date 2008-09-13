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

import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;


import org.jquantlib.time.WesternCalendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;



//! Danish calendar
/*! Holidays:
    <ul>
        <li>Saturdays</li>
        <li>Sundays</li>
        <li>Maunday Thursday</li>
        <li>Good Friday</li>
        <li>Easter Monday</li>
        <li>General Prayer Day, 25 days after Easter Monday</li>
        <li>Ascension</li>
        <li>Whit (Pentecost) Monday</li>
        <li>New Year's Day, January 1st</li>
        <li>Constitution Day, June 5th</li>
        <li>Christmas, December 25th</li>
        <li>Boxing Day, December 26th</li> 
    </ul>

    @Author 
*/

public class Denmark extends WesternCalendar {
	private static Denmark DENMARK = new Denmark();

	private Denmark() {
	}

	public static Denmark getCalendar() {
		return DENMARK;
	}

	public boolean isBusinessDay(Date date) {
        Weekday w = date.getWeekday();
        int d = date.getDayOfMonth(), dd = date.getDayOfYear();
        Month m = date.getMonthEnum();
        int y = date.getYear();
        int em = easterMonday(y);
		//exact matching of days except for Easter Monday
        if (isWeekend(w)
            // Maunday Thursday
            || (dd == em-4)
            // Good Friday
            || (dd == em-3)
            // Easter Monday
            || (dd == em)
            // General Prayer Day
            || (dd == em+25)
            // Ascension
            || (dd == em+38)
            // Whit Monday
            || (dd == em+49)
            // New Year's Day
            || (d == 1  && m == JANUARY)
            // Constitution Day, June 5th
            || (d == 5  && m == JUNE)
            // Christmas
            || (d == 25 && m == DECEMBER)
            // Boxing Day
            || (d == 26 && m == DECEMBER))
            return false;
            
        return true;

	}
    public String getName() {
       return "Denmark";
    }
    
}
