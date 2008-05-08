/*
 Copyright (C) 2008 Srinivas Hasti
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

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Brazil;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 *
 *
 */

// Under construction
// TO-DO: add the correct dates for testing.

public class BrazilCalendarTest {
	
	@Test
    public void Brazil() {
                       
    	Calendar c = Brazil.getCalendar(Brazil.Market.BOVESPA);
    	
        // 2004 - leap-year in the past
        // testBrazilYear2004(c);
        
        // 2007 - regular year in the past
        // testBrazilYear2007(c);
        
        // 2008 - current year
        testBrazilYear2008(c);
        
        // 2009 - current year in the future
        // testBrazilYear2009(c);
        
        // 2012 - next leap-year in the future
        // testBrazilYear2012(c);
    }
    
	/*
    // 2004 - leap-year in the past
    void testBrazilYear2004(Calendar c)
    {
       	int year = 2004;
    	System.out.println("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getDateUtil().getDate(2,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(8,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(12,APRIL,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year)); --> Sunday
    	expectedHol.add(DateFactory.getDateUtil().getDate(21,JUNE,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(9,JULY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(16,AUGUST,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(8,DECEMBER,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year)); --> Sunday 
    
    	// Call the Holiday Check
    	CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
   
    }
    
    // 2007 - regular year in the past
    void testBrazilYear2007(Calendar c) {
    	
    	int year = 2007;
    	System.out.println("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getDateUtil().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(18,JUNE,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(20,AUGUST,year));
        // expectedHol.add(DateFactory.getDateUtil().getDate(8,DECEMBER,year)); --> Saturday
        expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
    	CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
    }
    */
    
    // 2008 - current year
    void testBrazilYear2008(Calendar c){
      	int year = 2008;
      	System.out.println("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(25,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(4,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,FEBRUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
    }
    
    /*
    // 2009 - current year in the future
    void testBrazilYear2009(Calendar c) {
    	
    	int year = 2009;
    	System.out.println("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getDateUtil().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(10,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(13,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(15,JUNE,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(17,AUGUST,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
    	
    }

    // 2012 - next leap-year in the future
    void testBrazilYear2012(Calendar c) {
    	int year = 2012;
    	System.out.println("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); --> Sunday
    	expectedHol.add(DateFactory.getDateUtil().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(18,JUNE,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getDateUtil().getDate(20,AUGUST,year));
        // expectedHol.add(DateFactory.getDateUtil().getDate(8,DECEMBER,year)); --> Saturday
        expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
    	
    }
    */

}
