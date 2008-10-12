/*
 Copyright (C) 2008 Jia Jia
  
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
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jia Jia
 *
 *
 */


public class DenmarkCalendarTest {
	private Calendar c;
	private List<Date> expectedHol;
	
	@Before
	public void setUp() {
	    c =  Denmark.getCalendar();
	    expectedHol = new Vector<Date>();
	}

	   @Test
	    public void testDenmarkYear2004() {
	        int year = 2004;
	        System.out.println("Testing Denmark holiday list for the year " + year + "...");   
	        
	        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
	        //maunday thursday
	        expectedHol.add(DateFactory.getFactory().getDate(8,APRIL,year));               
	        //good friday
	        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));               
	        //easter monday
	        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
	        
	        //great prayer day
	        expectedHol.add(DateFactory.getFactory().getDate(7,MAY,year));
	        //ascension
	        expectedHol.add(DateFactory.getFactory().getDate(20,MAY,year));
	        //whit monday
	        expectedHol.add(DateFactory.getFactory().getDate(31,MAY,year));
	        
	        // Call the Holiday Check
	        CalendarUtil cbt = new CalendarUtil();
	        cbt.checkHolidayList(expectedHol, c, year);
	    }

	    @Test
	    public void testDenmarkYear2005() {
	        int year = 2005;
	        System.out.println("Testing Denmark holiday list for the year " + year + "...");   
	        
	        expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));               
	        //good friday
	        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));               
	        //easter monday
	        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
	        
	        //great prayer day
	        expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
	        //ascension
	        expectedHol.add(DateFactory.getFactory().getDate(5,MAY,year));
	        //whit monday
	        expectedHol.add(DateFactory.getFactory().getDate(16,MAY,year));
	        //boxing day
	        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
	        
	        // Call the Holiday Check
	        CalendarUtil cbt = new CalendarUtil();
	        cbt.checkHolidayList(expectedHol, c, year);
	    }

	    @Test
	    public void testDenmarkYear2006() {
	        int year = 2006;
	        System.out.println("Testing Denmark holiday list for the year " + year + "...");   
	        
	        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year)); 
	        //maunday thursday
	        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));               
	        //good friday
	        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));               
	        //easter monday
	        expectedHol.add(DateFactory.getFactory().getDate(12,MAY,year));
	        
	        //great prayer day
	        expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
	        //ascension
	        expectedHol.add(DateFactory.getFactory().getDate(5,JUNE,year));
	        //christmas
	        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
	        //boxing day
	        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
	        
	        // Call the Holiday Check
	        CalendarUtil cbt = new CalendarUtil();
	        cbt.checkHolidayList(expectedHol, c, year);
	    }

	    @Test
	    public void testDenmarkYear2007() {
	        int year = 2007;
	        System.out.println("Testing Denmark holiday list for the year " + year + "...");   
	        
	        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
	        //maunday thursday
	        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));               
	        //good friday
	        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));               
	        //easter monday
	        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
	        
	        //great prayer day
	        expectedHol.add(DateFactory.getFactory().getDate(4,MAY,year));
	        //ascension
	        expectedHol.add(DateFactory.getFactory().getDate(17,MAY,year));
	        //whit monday
	        expectedHol.add(DateFactory.getFactory().getDate(28,MAY,year));
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

	
	// 2008 - current year
	@Test
    public void testDenmarkYear2008() {
      	int year = 2008;
      	System.out.println("Testing Denmark holiday list for the year " + year + "...");   
    	
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

	   @Test
	    public void testDenmarkYear2009() {
	        int year = 2009;
	        System.out.println("Testing Denmark holiday list for the year " + year + "...");   
	        
	        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
	        //maunday thursday
	        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));               
	        //good friday
	        expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));               
	        //easter monday
	        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
	        
	        //great prayer day
	        expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year));
	        //ascension
	        expectedHol.add(DateFactory.getFactory().getDate(21,MAY,year));
	        //whit monday
	        expectedHol.add(DateFactory.getFactory().getDate(1,JUNE,year));
	        //constitution day
	        expectedHol.add(DateFactory.getFactory().getDate(5,JUNE,year));
	        //christmas
	        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
	        
	        // Call the Holiday Check
	        CalendarUtil cbt = new CalendarUtil();
	        cbt.checkHolidayList(expectedHol, c, year);
	    }

	    @Test
	    public void testDenmarkYear2010() {
	        int year = 2010;
	        System.out.println("Testing Denmark holiday list for the year " + year + "...");   
	        
	        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
	        //maunday thursday
	        expectedHol.add(DateFactory.getFactory().getDate(1,APRIL,year));               
	        //good friday
	        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));               
	        //easter monday
	        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
	        
	        //great prayer day
	        expectedHol.add(DateFactory.getFactory().getDate(30,APRIL,year));
	        //ascension
	        expectedHol.add(DateFactory.getFactory().getDate(13,MAY,year));
	        //whit monday
	        expectedHol.add(DateFactory.getFactory().getDate(24,MAY,year));
	        
	        // Call the Holiday Check
	        CalendarUtil cbt = new CalendarUtil();
	        cbt.checkHolidayList(expectedHol, c, year);
	    }

	    @Test
	    public void testDenmarkYear2012() {
	        int year = 2012;
	        System.out.println("Testing Denmark holiday list for the year " + year + "...");   
	        
	        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year)); 
	        //maunday thursday
	        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));               
	        //good friday
	        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));               
	        //easter monday
	        expectedHol.add(DateFactory.getFactory().getDate(4,MAY,year));
	        
	        //great prayer day
	        expectedHol.add(DateFactory.getFactory().getDate(17,MAY,year));
	        //ascension
	        expectedHol.add(DateFactory.getFactory().getDate(28,MAY,year));
	        //whit monday
	        expectedHol.add(DateFactory.getFactory().getDate(5,JUNE,year));
	        //christmas
	        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
	        //boxing day
	        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
	        
	        // Call the Holiday Check
	        CalendarUtil cbt = new CalendarUtil();
	        cbt.checkHolidayList(expectedHol, c, year);
	    }

	    @Test
	    public void testDenmarkYear2011() {
	        int year = 2011;
	        System.out.println("Testing Denmark holiday list for the year " + year + "...");   
	        
	        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year)); 
	        //maunday thursday
	        expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));               
	        //good friday
	        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));               
	        //easter monday
	        expectedHol.add(DateFactory.getFactory().getDate(20,MAY,year));
	        
	        //great prayer day
	        expectedHol.add(DateFactory.getFactory().getDate(2,JUNE,year));
	        //ascension
	        expectedHol.add(DateFactory.getFactory().getDate(13,JUNE,year));
	        //boxing day
	        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
	        
	        // Call the Holiday Check
	        CalendarUtil cbt = new CalendarUtil();
	        cbt.checkHolidayList(expectedHol, c, year);
	    }

}

