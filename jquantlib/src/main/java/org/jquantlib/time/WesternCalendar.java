/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.time;

import org.jquantlib.util.Date;

/**
 * This class provides the means of determining the Easter
 * Monday for a given year, as well as specifying Saturdays
 * and Sundays as weekend days.
 */
public abstract class WesternCalendar extends AbstractCalendar {

	static private final byte westernEasterMonday[] = { // Note: byte range is -128..+127
	    107,  98,  90, 103,  95, 114, 106,  91, 111, 102,   // 1900-1909
	     87, 107,  99,  83, 103,  95, 115,  99,  91, 111,   // 1910-1919
	     96,  87, 107,  92, 112, 103,  95, 108, 100,  91,   // 1920-1929
	    111,  96,  88, 107,  92, 112, 104,  88, 108, 100,   // 1930-1939
	     85, 104,  96, 116, 101,  92, 112,  97,  89, 108,   // 1940-1949
	    100,  85, 105,  96, 109, 101,  93, 112,  97,  89,   // 1950-1959
	    109,  93, 113, 105,  90, 109, 101,  86, 106,  97,   // 1960-1969
	     89, 102,  94, 113, 105,  90, 110, 101,  86, 106,   // 1970-1979
	     98, 110, 102,  94, 114,  98,  90, 110,  95,  86,   // 1980-1989
	    106,  91, 111, 102,  94, 107,  99,  90, 103,  95,   // 1990-1999
	    115, 106,  91, 111, 103,  87, 107,  99,  84, 103,   // 2000-2009
	     95, 115, 100,  91, 111,  96,  88, 107,  92, 112,   // 2010-2019
	    104,  95, 108, 100,  92, 111,  96,  88, 108,  92,   // 2020-2029
	    112, 104,  89, 108, 100,  85, 105,  96, 116, 101,   // 2030-2039
	     93, 112,  97,  89, 109, 100,  85, 105,  97, 109,   // 2040-2049
	    101,  93, 113,  97,  89, 109,  94, 113, 105,  90,   // 2050-2059
	    110, 101,  86, 106,  98,  89, 102,  94, 114, 105,   // 2060-2069
	     90, 110, 102,  86, 106,  98, 111, 102,  94, 107,   // 2070-2079
	     99,  90, 110,  95,  87, 106,  91, 111, 103,  94,   // 2080-2089
	    107,  99,  91, 103,  95, 115, 107,  91, 111, 103    // 2090-2099
	};

    public final boolean isWeekend(final Weekday weekday) {
        return weekday == Weekday.SATURDAY || weekday == Weekday.SUNDAY;
    }

    /**
     * @return the offset of the Easter Monday relative to the
     * first day of the year
     */
    protected final int easterMonday(final int year) {
    	return westernEasterMonday[year-1900];
    }


    //
    // overrides AbstractCalendar
    //

    @Override
    public final void addHoliday(final Date d) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeHoliday(final Date d) {
        throw new UnsupportedOperationException();
    }

}


