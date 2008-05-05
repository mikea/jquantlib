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
import org.jquantlib.time.calendars.Switzerland;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 * <p>
 * <strong>Description</strong><br>
 * Switzerland Calendar Test.
 */

public class SwitzerlandCalendarTest {
	
	@Test
    public void testSwitzerlandd() {
		
		Calendar c = Switzerland.getCalendar(Switzerland.Market.SWX);
		
		// 2004 - leap-year in the past
		testSwitzerland2004(c);
		
		// 2007 - regular year in the past
		testSwitzerland2007(c);
		
		// 2008 - current year 
		testSwitzerland2008(c);
		
		// 2009 - regular year in the future
		testSwitzerland2009(c);
		
		// 2012 - leap-year in the future
		testSwitzerland2012(c);
		
	} 
	
	public void testSwitzerland2004(Calendar c){
		
		int year = 2004;
		System.out.println("Testing " + Switzerland.Market.SWX + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year));
		expectedHol.add(DateFactory.getDateUtil().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(12,APRIL,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year)); --> Saturday
    	expectedHol.add(DateFactory.getDateUtil().getDate(20,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(31,MAY,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,AUGUST,year)); --> Sunday
    	// expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year)); --> Saturday
    	// expectedHol.add(DateFactory.getDateUtil().getDate(26,DECEMBER,year)); --> Sunday
	
    	// Call the Holiday Check
    	CalendarBaseTest cbt = new CalendarBaseTest();
		cbt.HolidayListCheck(expectedHol, c, year); 
	}
	
	public void testSwitzerland2007(Calendar c) {
	
		int year = 2007;
		System.out.println("Testing " + Switzerland.Market.SWX + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year));
		expectedHol.add(DateFactory.getDateUtil().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(6,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(17,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(28,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,AUGUST,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(26,DECEMBER,year));
    		
    	// Call the Holiday Check
    	CalendarBaseTest cbt = new CalendarBaseTest();
		cbt.HolidayListCheck(expectedHol, c, year);
	}
	
	public void testSwitzerland2008(Calendar c){
		
		int year = 2008;
		System.out.println("Testing " + Switzerland.Market.SWX + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year));
		expectedHol.add(DateFactory.getDateUtil().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(21,MARCH,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(24,MARCH,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(12,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,AUGUST,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(26,DECEMBER,year));
    		
    	// Call the Holiday Check
    	CalendarBaseTest cbt = new CalendarBaseTest();
		cbt.HolidayListCheck(expectedHol, c, year); 
	}
	
	public void testSwitzerland2009(Calendar c) {
		
		int year = 2009;
		System.out.println("Testing " + Switzerland.Market.SWX + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year));
		expectedHol.add(DateFactory.getDateUtil().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(10,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(13,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(21,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(01,JUNE,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,AUGUST,year)); --> Saturday
    	expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(26,DECEMBER,year)); --> Saturday
    		
    	// Call the Holiday Check
    	CalendarBaseTest cbt = new CalendarBaseTest();
		cbt.HolidayListCheck(expectedHol, c, year); 
	}
	
	public void testSwitzerland2012(Calendar c) {
		int year = 2012;
		System.out.println("Testing " + Switzerland.Market.SWX + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		// expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); --> Sunday
		expectedHol.add(DateFactory.getDateUtil().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(6,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(17,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(28,MAY,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(1,AUGUST,year)); 
    	expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getDateUtil().getDate(26,DECEMBER,year)); 
    		
    	// Call the Holiday Check
    	CalendarBaseTest cbt = new CalendarBaseTest();
		cbt.HolidayListCheck(expectedHol, c, year); 
	}
}
