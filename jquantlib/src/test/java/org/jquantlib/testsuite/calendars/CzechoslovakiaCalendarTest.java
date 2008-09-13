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

import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.SEPTEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.DECEMBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Czechoslovakia;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author
 *
 *
 */


public class CzechoslovakiaCalendarTest {
	
	public CzechoslovakiaCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
    public void testCzechoslovakia() {
                       
    	Calendar c = Czechoslovakia.getCalendar();
    	
        // 2008 - current year
        testCzechoslovakiaYear2008(c);
        
    }
    
    
    // 2008 - current year
	void testCzechoslovakiaYear2008(Calendar c) {
      	int year = 2008;
      	System.out.println("Testing Czechoslovakian holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	//new year's day
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));  
		
    	//easter monday
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
    	//Labour Day  	
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//Liberation Day
		expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year));
		//SS. Cyril and Methodius --weekend for yr 2008
		//expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year));
		//Jan Hus Day --weekend for yr 2008
		//expectedHol.add(DateFactory.getFactory().getDate(6,JULY,year));
		//Czech Statehood Day -- weekend for yr 2008
		//expectedHol.add(DateFactory.getFactory().getDate(28,SEPTEMBER,year));
		//Independence Day
		expectedHol.add(DateFactory.getFactory().getDate(28,OCTOBER,year));
		//Struggle for Freedom and Democracy Day
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year));
		//Christmas Eve	
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//Christmas
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//St. Stephen
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
    
}

