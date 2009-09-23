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

import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.MAY;

import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;

/**
 * TARGET calendar relative to the European Central Bank
 * <p>
 * This is a holiday calendar representing  the 
 * <i>Trans-european Automated Real-time Gross Express-settlement Transfer</i>
 * system calendar.
 * <p>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Good Friday (since 2000)</li>
 * <li>Easter Monday (since 2000)</li>
 * <li>Labour Day, May 1st (since 2000)</li>
 * <li>Christmas, December 25th</li>
 * <li>Day of Goodwill, December 26th (since 2000)</li>
 * <li>December 31st (1998, 1999, and 2001)</li>
 * 
 * @see <a href="http://www.ecb.int">European Central Bank</a>
 * 
 * @author Srinivas Hasti
 * 
 * @category calendars
 */
//// TODO: code review :: please verify against QL/C++ code
public class Target extends DelegateCalendar {
	
	private final static Target TARGET_CALENDAR = new Target();

	private Target() {
		setDelegate(new TargetCalendar());
	}

	public static Target getCalendar() {
		return TARGET_CALENDAR;
	}
	

	//
	// private inner classes
	//
	
    private static final class TargetCalendar extends WesternCalendar {
    
    	public String getName() {
    		return "TARGET";
    	}
    
    	public boolean isBusinessDay(Date date) {
    		Weekday w = date.weekday();
    		int d = date.dayOfMonth(), dd = date.dayOfYear();
    		Month m = date.month();
    		int y = date.year();
    		int em = easterMonday(y);
    		if (isWeekend(w)
    		// New Year's Day
    				|| (d == 1 && m == JANUARY)
    				// Good Friday
    				|| (dd == em - 3 && y >= 2000)
    				// Easter Monday
    				|| (dd == em && y >= 2000)
    				// Labour Day
    				|| (d == 1 && m == MAY && y >= 2000)
    				// Christmas
    				|| (d == 25 && m == DECEMBER)
    				// Day of Goodwill
    				|| (d == 26 && m == DECEMBER && y >= 2000)
    				// December 31st, 1998, 1999, and 2001 only
    				|| (d == 31 && m == DECEMBER && (y == 1998 || y == 1999 || y == 2001)))
    			return false;
    		return true;
    	}
    }

}