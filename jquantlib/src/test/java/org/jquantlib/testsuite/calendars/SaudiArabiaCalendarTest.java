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

import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.SaudiArabia;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Joon Tiang
 * @author Renjith Nair
 *
 */


public class SaudiArabiaCalendarTest {
	
    private final static Logger logger = LoggerFactory.getLogger(SaudiArabiaCalendarTest.class);

    public SaudiArabiaCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
    public void testSaudiArabia() {
                       
    	Calendar c = SaudiArabia.getCalendar(SaudiArabia.Market.TADAWUL);
    	
        //2004
		testSaudiArabiaYear2004(c);
		// 2005 
        testSaudiArabiaYear2005(c);
        // 2006 
        testSaudiArabiaYear2006(c);
        // 2007 
        testSaudiArabiaYear2007(c);
        // 2008 
        testSaudiArabiaYear2008(c);
        // 2009 
        testSaudiArabiaYear2009(c);
        // 2010 
        testSaudiArabiaYear2010(c);
        // 2011 
        testSaudiArabiaYear2011(c);
        // 2012 
        testSaudiArabiaYear2012(c);
		
    }
    
	//2004
	void testSaudiArabiaYear2004(Calendar c) {
      	int year = 2004;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		// Eid Al-Adha -- 5 is on Thu,6 is on Fri, weekend in yr 2004
		expectedHol.add(DateFactory.getFactory().getDate(1,FEBRUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(2,FEBRUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(3,FEBRUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(4,FEBRUARY,year)); 
    	//National Day -- weekend in 2004
    	//expectedHol.add(DateFactory.getFactory().getDate(23,SEPTEMBER,year)); 
		// Eid Al-Fitr -- 25,26 falls on thur,fri respectively, weekend in yr 2005
        expectedHol.add(DateFactory.getFactory().getDate(27,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(28,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,NOVEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2005
	void testSaudiArabiaYear2005(Calendar c) {
      	int year = 2005;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		// Eid Al-Adha -- 21 is on Fri, weekend in yr 2005
		expectedHol.add(DateFactory.getFactory().getDate(22,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(23,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(24,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(25,JANUARY,year)); 
    	//National Day -- weekend in 2005
    	//expectedHol.add(DateFactory.getFactory().getDate(23,SEPTEMBER,year)); 
		// Eid Al-Fitr -- 17,18 falls on thur,fri , weekend in yr 2005
        expectedHol.add(DateFactory.getFactory().getDate(14,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(16,NOVEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	// 2006
	void testSaudiArabiaYear2006(Calendar c) {
      	int year = 2006;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
    	
      	List<Date> expectedHol = new Vector<Date>();
    	//National Day 
    	expectedHol.add(DateFactory.getFactory().getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check        
      	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2007
	void testSaudiArabiaYear2007(Calendar c) {
      	int year = 2007;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
      	List<Date> expectedHol = new Vector<Date>();
    	//National Day 
    	expectedHol.add(DateFactory.getFactory().getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2008
	void testSaudiArabiaYear2008(Calendar c) {
      	int year = 2008;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
      	List<Date> expectedHol = new Vector<Date>();
    	//National Day 
    	expectedHol.add(DateFactory.getFactory().getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2009
	void testSaudiArabiaYear2009(Calendar c) {
      	int year = 2009;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
      	List<Date> expectedHol = new Vector<Date>();
    	//National Day 
    	expectedHol.add(DateFactory.getFactory().getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2010
	void testSaudiArabiaYear2010(Calendar c) {
      	int year = 2010;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
      	List<Date> expectedHol = new Vector<Date>();
    	//National Day - On a Thursday and hence a weekend
    	//expectedHol.add(DateFactory.getFactory().getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2011
	void testSaudiArabiaYear2011(Calendar c) {
      	int year = 2011;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
      	List<Date> expectedHol = new Vector<Date>();
    	//National Day On a Friday and hence a weekend
    	//expectedHol.add(DateFactory.getFactory().getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	// 2012
	void testSaudiArabiaYear2012(Calendar c) {
      	int year = 2012;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
      	List<Date> expectedHol = new Vector<Date>();
    	//National Day 
    	expectedHol.add(DateFactory.getFactory().getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
}

