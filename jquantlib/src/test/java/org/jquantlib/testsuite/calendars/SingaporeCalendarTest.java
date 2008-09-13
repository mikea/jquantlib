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
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.SEPTEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.NOVEMBER;


import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Singapore;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author
 *
 *
 */


public class SingaporeCalendarTest {
	
	public SingaporeCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
    public void testSingapore() {
                       
    	Calendar c = Singapore.getCalendar();
    	
        // 2007 
        testSingaporeYear2007(c);
		// 2006 
        testSingaporeYear2006(c);
		// 2005 
        testSingaporeYear2005(c);
		// 2004 
        testSingaporeYear2004(c);
    }
    // 2007 
	
	void testSingaporeYear2007(Calendar c) {
      	int year = 2007;
      	System.out.println("Testing Singapore's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	//New year's day
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		// Hari Raya Haji
		expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year)); 
		
		//Chinese New Year
    	expectedHol.add(DateFactory.getFactory().getDate(19,FEBRUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(20,FEBRUARY,year)); 
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));    	    	
    	
    	//labour day
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		
		//Vesak day
		expectedHol.add(DateFactory.getFactory().getDate(31,MAY,year));
		
    	// National Day
    	expectedHol.add(DateFactory.getFactory().getDate(9,AUGUST,year));
    	// Hari Raya Puasa -- weekend in 2007
		//expectedHol.add(DateFactory.getFactory().getDate(13,OCTOBER,year));  
		// Deepavali
		expectedHol.add(DateFactory.getFactory().getDate(8,NOVEMBER,year));    	    	
		
		   	    	
		// Hari Raya Haji
		expectedHol.add(DateFactory.getFactory().getDate(20,DECEMBER,year)); 
		
		//christmas 
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2006 
	
	void testSingaporeYear2006(Calendar c) {
      	int year = 2006;
      	System.out.println("Testing Singapore's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	//New year's day -- weekend in 2006
    	//expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		// Hari Raya Haji
		expectedHol.add(DateFactory.getFactory().getDate(10,JANUARY,year)); 
		
		//Chinese New Year
    	expectedHol.add(DateFactory.getFactory().getDate(30,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(31,JANUARY,year)); 
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));    	    	
    	
    	//labour day
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		
		//Vesak day
		expectedHol.add(DateFactory.getFactory().getDate(12,MAY,year));
		
    	// National Day
    	expectedHol.add(DateFactory.getFactory().getDate(9,AUGUST,year));
    	// Hari Raya Puasa -- weekend in 2007
		expectedHol.add(DateFactory.getFactory().getDate(24,OCTOBER,year));  
		
		
		
		//christmas 
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
    
	// 2005 
	
	void testSingaporeYear2005(Calendar c) {
      	int year = 2005;
      	System.out.println("Testing Singapore's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	//New year's day -- weekend in yr 2005
    	//expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		// Hari Raya Haji
		expectedHol.add(DateFactory.getFactory().getDate(21,JANUARY,year)); 
		
		//Chinese New Year
    	expectedHol.add(DateFactory.getFactory().getDate(9,FEBRUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(10,FEBRUARY,year)); 
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));    	    	
    	
    	//labour day -- weekend in yr 2005
    	//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		
		//Vesak day -- weekend in yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
		
    	// National Day
    	expectedHol.add(DateFactory.getFactory().getDate(9,AUGUST,year));
		//Diwali
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));    	
    	// Hari Raya Puasa -- weekend in 2005
		expectedHol.add(DateFactory.getFactory().getDate(3,NOVEMBER,year));  
			    	
		    	
		   	    	
		
		
		//christmas  -- weekend in yr 2005
    	//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	// 2004
	
	void testSingaporeYear2004(Calendar c) {
      	int year = 2004;
      	System.out.println("Testing Singapore's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	//New year's day 
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		//Chinese New Year
		expectedHol.add(DateFactory.getFactory().getDate(22,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(23,JANUARY,year)); 
		// Hari Raya Haji
		expectedHol.add(DateFactory.getFactory().getDate(2,FEBRUARY,year)); 
		
				
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));    	    	
    	
    	//labour day -- weekend in yr 2004
    	//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		
		//Vesak day
		expectedHol.add(DateFactory.getFactory().getDate(2,JUNE,year));
		
    	// National Day
    	expectedHol.add(DateFactory.getFactory().getDate(9,AUGUST,year));
		
		//Deepavali
		expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));    	
		
    	// Hari Raya Puasa -- 14 is in the weekends of 2004
		expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year));  
			    	
		    	
		   	    	
		
		
		//christmas  -- weekend in yr 2004
    	//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
}

