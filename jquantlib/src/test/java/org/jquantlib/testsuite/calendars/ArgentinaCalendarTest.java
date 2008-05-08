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
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Argentina;
import org.jquantlib.time.calendars.Switzerland;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 *
 *
 */

public class ArgentinaCalendarTest {

    @Test
    public void testArgentina() {
                       
    	Calendar c = Argentina.getCalendar(Argentina.Market.BCBA);
    	
        // 2004 - leap-year in the past
        testArgentinaYear2004(c);
        
        // 2007 - regular year in the past
        testArgentinaYear2007(c);
        
        // 2008 - current year
        testArgentinaYear2008(c);
        
        // 2009 - current year in the future
        testArgentinaYear2009(c);
        
        // 2012 - next leap-year in the future
        testArgentinaYear2012(c);
    }
    
    // 2004 - leap-year in the past
    void testArgentinaYear2004(Calendar c)
    {
       	int year = 2004;
    	System.out.println("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year)); --> Sunday
    	expectedHol.add(DateFactory.getFactory().getDate(21,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(16,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year)); --> Sunday 
    
    	// Call the Holiday Check
    	CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
   
    }
    
    void testArgentinaYear2007(Calendar c) {
    	
    	int year = 2007;
    	System.out.println("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,AUGUST,year));
        // expectedHol.add(DateFactory.getDateUtil().getDate(8,DECEMBER,year)); --> Saturday
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
    	CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
    }
    
    void testArgentinaYear2008(Calendar c)
    {
      	int year = 2008;
    	System.out.println("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(20,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(16,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
    }
    
    void testArgentinaYear2009(Calendar c) {
    	
    	int year = 2009;
    	System.out.println("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(17,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
    	
    }

    void testArgentinaYear2012(Calendar c) {
    	
    	int year = 2012;
    	System.out.println("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); --> Sunday
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,AUGUST,year));
        // expectedHol.add(DateFactory.getDateUtil().getDate(8,DECEMBER,year)); --> Saturday
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarBaseTest cbt = new CalendarBaseTest();
    	cbt.HolidayListCheck(expectedHol, c, year);
    	
    }
}
