/*
 Copyright (C) 2008 Dominik Holenstein

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

package org.jquantlib.testsuite.calendars;

import static org.junit.Assert.fail;

import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;

/**
 * This is the general test base class for Calendars including 
 * generic methods.
 *  
 * @author Dominik Holenstein <p>
 */

public class CalendarUtil {
	
	protected void HolidayListCheck(List <Date> expectedHol, Calendar c, int year) {
		
    	List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1, Month.JANUARY, year),
                DateFactory.getFactory().getDate(31, Month.DECEMBER, year), false);
    	
    	for (int i =0;i<Math.min(hol.size(), expectedHol.size()); i++) {
    		if (!hol.get(i).equals(expectedHol.get(i)))
    			fail("expected holiday was " + expectedHol.get(i)
    					+ " while calculated holiday is " + hol.get(i));
    		}
    		if (hol.size()!=expectedHol.size())
    			fail("there were " + expectedHol.size()
    					+ " expected holidays, while there are " + hol.size()
    					+ " calculated holidays");
    }

}
