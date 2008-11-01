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

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Slovakia;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Heng Joon Tiang 
 * @author Renjith Nair
 *
 */

public class SlovakiaCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(SlovakiaCalendarTest.class);

	public SlovakiaCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
    public void testSlovakia() {
                       
    	Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	
    	testSlovakiaYear2004(c);
        testSlovakiaYear2005(c);
        testSlovakiaYear2006(c);
        testSlovakiaYear2007(c);
        testSlovakiaYear2008(c);
        testSlovakiaYear2009(c);
        testSlovakiaYear2010(c);
        testSlovakiaYear2011(c);
        testSlovakiaYear2012(c);
        
    }
	
	
	//2004
	void testSlovakiaYear2004(Calendar c) {
      	int year = 2004;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		//New Year -- 1st Jan 
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	
		//Epiphany, January 6th
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year)); 
		
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year)); 
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year)); 
		
		//May Day, May 1st -- weekend in yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		
		//Liberation of the Republic, May 8th -- weekend in  yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
		
		//SS. Cyril and Methodius, July 5th
		expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
		//Slovak National Uprising, August 29th -- weekend in yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,year)); 
		//Constitution of the Slovak Republic, September 1st
    	expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year)); 
		
		//Our Lady of the Seven Sorrows, September 15th
		expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year)); 
		//All Saints Day, November 1st
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year)); 
		//Freedom and Democracy of the Slovak Republic, November 17th
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year)); 
		//Christmas Eve, December 24th 
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
		//Christmas, December 25th -- weekend in yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
		//St. Stephen, December 26th -- weekend in yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year)); 
		//unidentified stock exchange closing days 24-31 Dec, 24,25,26 Dec weekend in yr 2005
		expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(30,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
   
	// 2005 
	
	void testSlovakiaYear2005(Calendar c) {
      	int year = 2005;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		//New Year -- 1st Jan weekend in yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	
		//Epiphany, January 6th
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year)); 
		
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year)); 
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year)); 
		
		//May Day, May 1st -- weekend in yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		
		//Liberation of the Republic, May 8th -- weekend in  yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
		
		//SS. Cyril and Methodius, July 5th
		expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
		//Slovak National Uprising, August 29th
		expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,year)); 
		//Constitution of the Slovak Republic, September 1st
    	expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year)); 
		
		//Our Lady of the Seven Sorrows, September 15th
		expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year)); 
		//All Saints Day, November 1st
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year)); 
		//Freedom and Democracy of the Slovak Republic, November 17th
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year)); 
		//Christmas Eve, December 24th -- weekend in yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
		//Christmas, December 25th -- weekend in yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
		//St. Stephen, December 26th
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year)); 
		//unidentified stock exchange closing days 24-31 Dec, 24,25,31 Dec weekend in yr 2005
		expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(30,DECEMBER,year)); 
		//expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	
	// 2006 - Year in the past 
	void testSlovakiaYear2006(Calendar c) {
      	
		int year = 2006;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year)); 
        
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2007 - Year in the past 
	void testSlovakiaYear2007(Calendar c) {
      	
		int year = 2007;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));

		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2008 - Current Year
	void testSlovakiaYear2008(Calendar c) {
      	
		int year = 2008;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year)); 

		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2009 - Future Year
	void testSlovakiaYear2009(Calendar c) {
      	
		int year = 2009;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
		
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2010 - Future Year
	void testSlovakiaYear2010(Calendar c) {
      	
		int year = 2010;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
        
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2011 - Future Year
	void testSlovakiaYear2011(Calendar c) {
      	
		int year = 2011;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year)); 
        
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2012 - Future Year
	void testSlovakiaYear2012(Calendar c) {
      	
		int year = 2012;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year)); 
        
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
}
