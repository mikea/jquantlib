/*
 Copyright (C) 2008 Renjith Nair

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

import static org.jquantlib.util.Month.*;


import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Sweden;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Renjith Nair
 *
 *
 */

public class SwedenCalendarTest {

	Calendar c= null;
	List<Date> expectedHol = null;
	public SwedenCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Before
	public void setup(){
		c=Sweden.getCalendar(Sweden.Market.SSE);
		expectedHol = new Vector<Date>();
	}
	
        
    // 2004 - year in the past
	@Test
    public void testSwedenSEHolidaysYear2004()
    {    	
       	int year = 2004;
    	System.out.println("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(20,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(31,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(18,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2008 - Current Year
	@Test
    public void testSwedenSEHolidaysYear2008()
    {    	
       	int year = 2008;
    	System.out.println("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(20,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2010 - Future Year
	@Test
    public void testSwedenSEHolidaysYear2010()
    {    	
       	int year = 2010;
    	System.out.println("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(18,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
    @After
	public void destroy(){
		c=null;
		expectedHol = null;
	}
}
