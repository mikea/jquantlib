/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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


/**
 * This class provides the means of determining the Orthodox
 * Easter Monday for a given year, as well as specifying
 * Saturdays and Sundays as weekend days.
 */
public abstract class OrthodoxCalendar extends AbstractCalendar {

    static private final short ortodoxEasterMonday[] = { // Note: short range is -32,768 .. 32,767
		     105, 118, 110, 102, 121, 106, 126, 118, 102,   // 1901-1909
		122, 114,  99, 118, 110,  95, 115, 106, 126, 111,   // 1910-1919
		103, 122, 107,  99, 119, 110, 123, 115, 107, 126,   // 1920-1929
		111, 103, 123, 107,  99, 119, 104, 123, 115, 100,   // 1930-1939
		120, 111,  96, 116, 108, 127, 112, 104, 124, 115,   // 1940-1949
		100, 120, 112,  96, 116, 108, 128, 112, 104, 124,   // 1950-1959
		109, 100, 120, 105, 125, 116, 101, 121, 113, 104,   // 1960-1969
		117, 109, 101, 120, 105, 125, 117, 101, 121, 113,   // 1970-1979
		 98, 117, 109, 129, 114, 105, 125, 110, 102, 121,   // 1980-1989
		106,  98, 118, 109, 122, 114, 106, 118, 110, 102,   // 1990-1999
		122, 106, 126, 118, 103, 122, 114,  99, 119, 110,   // 2000-2009
		 95, 115, 107, 126, 111, 103, 123, 107,  99, 119,   // 2010-2019
		111, 123, 115, 107, 127, 111, 103, 123, 108,  99,   // 2020-2029
		119, 104, 124, 115, 100, 120, 112,  96, 116, 108,   // 2030-2039
		128, 112, 104, 124, 116, 100, 120, 112,  97, 116,   // 2040-2049
		108, 128, 113, 104, 124, 109, 101, 120, 105, 125,   // 2050-2059
		117, 101, 121, 113, 105, 117, 109, 101, 121, 105,   // 2060-2069
		125, 110, 102, 121, 113,  98, 118, 109, 129, 114,   // 2070-2079
		106, 125, 110, 102, 122, 106,  98, 118, 110, 122,   // 2080-2089
		114,  99, 119, 110, 102, 115, 107, 126, 118, 103    // 2090-2099
	};
	
	public final boolean isWeekend(Weekday weekday) {
        return weekday == Weekday.SATURDAY || weekday == Weekday.SUNDAY;
    }
	
    /**
     * @return the offset of the Easter Monday relative to the
     * first day of the year
     */ 
    protected final int easterMonday(int year) {
    	return ortodoxEasterMonday[year-1901];
    }
}
