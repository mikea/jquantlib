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
import org.jquantlib.time.calendars.Turkey;
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

public class TurkeyCalendarTest {

	Calendar c= null;
	List<Date> expectedHol = null;
	public TurkeyCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Before
	public void setup(){
		c=Turkey.getCalendar(Turkey.Market.ISE);
		expectedHol = new Vector<Date>();
	}
	
        
    // 2004 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2004()
    {    	
       	int year = 2004;
    	System.out.println("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(4,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(16,NOVEMBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2005 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2005()
    {    	
       	int year = 2005;
    	System.out.println("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(19,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(20,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(4,NOVEMBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2006 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2006()
    {    	
       	int year = 2006;
    	System.out.println("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(9,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,OCTOBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2007 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2007()
    {    	
       	int year = 2007;
    	System.out.println("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(4,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(19,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(20,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,DECEMBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2008 - Current Year
	@Test
    public void testTurkeyISEHolidaysYear2008()
    {    	
       	int year = 2008;
    	System.out.println("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,SEPTEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,SEPTEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,DECEMBER,year));
    	
  	    	
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
