/*
 Copyright (C) 2008 Tim Swetonic
 
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

import static org.jquantlib.time.Weekday.SATURDAY;
import static org.jquantlib.time.Weekday.SUNDAY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;

import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

//! Chinese calendar
/*! Holidays:
    <ul>
    <li>Saturdays</li>
    <li>Sundays</li>
    <li>New Year's day, January 1st</li>
    <li>Labour Day, first week in May</li>
    <li>National Day, one week from October 1st</li>
    </ul>

    Other holidays for which no rule is given:
    <ul>
    <li>Lunar New Year (data available for 2004 only)</li>
    <li>Spring Festival</li>
    <li>Last day of Lunar Year</li>
    </ul>

    @Author Tim Swetonic
*/

public class China extends DelegateCalendar {
	
	private static China CHINA = new China();
	
	private China() {
	
	}
	
	public static China getCalendar() {
		return CHINA;
	}

    public final String name()  { return "China"; }
    
    public final boolean isWeekend(Weekday w) {
        return w == SATURDAY || w == SUNDAY;
    }
    
    public final boolean isBusinessDay(Date date) {
        Weekday w = date.getWeekday();
        int d = date.getDayOfMonth();
        Month m = date.getMonthEnum();
        int y = date.getYear();
        int dd = date.getDayOfYear();

        if (isWeekend(w)
            // New Year's Day
            || (d == 1 && m == JANUARY)
            // Labor Day
            || (d >= 1 && d <= 7 && m == MAY)
            // National Day
            || (d >= 1 && d <= 7 && m == OCTOBER)
            // Lunar New Year 2004
            || (d >= 22 && d <= 28 && m == JANUARY && y==2004)
            // Lunar New Year 2009
            || (d >= 26 && d <= 31 && m == JANUARY && y==2009)
            // Spring Festival
            || (dd == springFestival(y))
            // Last day of Lunar Year
            || (dd == springFestival(y)-1)
            )
            return false;
        return true;
    }
    
    //! expressed relative to first day of year
    public static int springFestival(int year) {
        final int SpringFestival[] = {
            31,  51,  39,  29,  47,  36,  25,  44,  33,  22,   // 1900-1909
            41,  30,  49,  37,  26,  45,  34,  23,  42,  32,   // 1910-1919
            52,  39,  28,  47,  37,  24,  44,  33,  23,  41,   // 1920-1929
            30,  48,  37,  26,  45,  35,  24,  42,  31,  51,   // 1930-1939
            39,  27,  46,  37,  25,  44,  33,  22,  41,  29,   // 1940-1949
            48,  37,  27,  45,  34,  24,  43,  31,  49,  39,   // 1950-1959
            28,  46,  36,  25,  44,  33,  21,  40,  30,  48,   // 1960-1969
            37,  27,  46,  34,  23,  42,  31,  49,  38,  28,   // 1970-1979
            47,  36,  25,  44,  33,  51,  40,  29,  48,  37,   // 1980-1989
            27,  46,  36,  23,  41,  31,  51,  38,  28,  47,   // 1990-1999
            36,  24,  43,  32,  22,  40,  29,  49,  38,  26,   // 2000-2009
            45,  34,  23,  41,  31,  51,  39,  28,  47,  36,   // 2010-2019
            25,  43,  32,  22,  41,  29,  48,  37,  26,  44,   // 2020-2029
            34,  23,  42,  31,  50,  39,  28,  46,  36,  24,   // 2030-2039
            43,  32,  22,  41,  30,  48,  37,  26,  45,  33,   // 2040-2049
            23,  42,  32,  50,  39,  28,  46,  35,  24,  43,   // 2050-2059
            33,  21,  40,  29,  48,  36,  26,  45,  34,  23,   // 2060-2069
            42,  31,  51,  38,  27,  46,  36,  24,  43,  33,   // 2070-2079
            22,  40,  29,  48,  37,  26,  45,  35,  24,  41,   // 2080-2089
            30,  50,  38,  27,  46,  36,  25,  43,  32,  21,   // 2090-2099
        };
        return SpringFestival[year-1900];
	}
}


