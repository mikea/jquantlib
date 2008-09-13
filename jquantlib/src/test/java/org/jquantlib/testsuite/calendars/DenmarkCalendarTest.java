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

package org.jquantlib.testsuite.calendars;

import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.JUNE;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Denmark;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author
 *
 *
 */


public class DenmarkCalendarTest {
	
	public DenmarkCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
    public void testDenmark() {
                       
    	Calendar c = Denmark.getCalendar();
    	
        // 2008 - current year
        testDenmarkYear2008(c);
        
    }
    // 2008 - current year
    void testDenmarkYear2008(Calendar c) {
      	int year = 2008;
      	System.out.println("Testing Denmark holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		//maunday thursday
		expectedHol.add(DateFactory.getFactory().getDate(20,MARCH,year));    	    	
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));    	    	
    	//easter monday
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
		
		//great prayer day
    	expectedHol.add(DateFactory.getFactory().getDate(18,APRIL,year));
    	//ascension
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	//whit monday
    	expectedHol.add(DateFactory.getFactory().getDate(12,MAY,year));
    	//constitution day
		expectedHol.add(DateFactory.getFactory().getDate(5,JUNE,year));
		//christmas
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
    
}

