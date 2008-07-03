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

package org.jquantlib.testsuite.calendars;

import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.China;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Tim Swetonic
 *
 *
 */


public class ChinaCalendarTest {
	
	public ChinaCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
    public void testChina() {
                       
    	Calendar c = China.getCalendar();
    	
        // 2008 - current year
        testChinaYear2008(c);
        
    }
    
    
    // 2008 - current year
    void testChinaYear2008(Calendar c) {
      	int year = 2008;
      	System.out.println("Testing China holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(6,FEBRUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(7,FEBRUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(6,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(7,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(6,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(7,OCTOBER,year));

    	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
    
}

