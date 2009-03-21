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

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.SaudiArabia;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joon Tiang
 * @author Renjith Nair
 *
 */

// TODO: This class needs code review againt reliable sources of data
public class SaudiArabiaCalendarTest {
	
    private final static Logger logger = LoggerFactory.getLogger(SaudiArabiaCalendarTest.class);
    private final Calendar exchange;

    public SaudiArabiaCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        this.exchange = SaudiArabia.getCalendar(SaudiArabia.Market.TADAWUL);
	}
	
	//2004
    @Test
	public void testSaudiArabiaYear2004() {
      	int year = 2004;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
    	
		// Eid Al-Adha -- 5 is on Thu,6 is on Fri, weekend in yr 2004
		expectedHol.add(df.getDate(1,FEBRUARY,year)); 
		expectedHol.add(df.getDate(2,FEBRUARY,year)); 
		expectedHol.add(df.getDate(3,FEBRUARY,year)); 
		expectedHol.add(df.getDate(4,FEBRUARY,year)); 
    	//National Day -- weekend in 2004
    	//expectedHol.add(df.getDate(23,SEPTEMBER,year)); 
		// Eid Al-Fitr -- 25,26 falls on thur,fri respectively, weekend in yr 2005
        expectedHol.add(df.getDate(27,NOVEMBER,year)); 
		expectedHol.add(df.getDate(28,NOVEMBER,year)); 
		expectedHol.add(df.getDate(29,NOVEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2005
    @Test
    public void testSaudiArabiaYear2005() {
      	int year = 2005;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
		// Eid Al-Adha -- 21 is on Fri, weekend in yr 2005
		expectedHol.add(df.getDate(22,JANUARY,year)); 
		expectedHol.add(df.getDate(23,JANUARY,year)); 
		expectedHol.add(df.getDate(24,JANUARY,year)); 
		expectedHol.add(df.getDate(25,JANUARY,year)); 
    	//National Day -- weekend in 2005
    	//expectedHol.add(df.getDate(23,SEPTEMBER,year)); 
		// Eid Al-Fitr -- 17,18 falls on thur,fri , weekend in yr 2005
        expectedHol.add(df.getDate(14,NOVEMBER,year)); 
		expectedHol.add(df.getDate(15,NOVEMBER,year)); 
		expectedHol.add(df.getDate(16,NOVEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2006
    @Test
    public void testSaudiArabiaYear2006() {
      	int year = 2006;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	//National Day 
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check        
      	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2007
    @Test
    public void testSaudiArabiaYear2007() {
      	int year = 2007;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	//National Day 
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2008
    @Test
    public void testSaudiArabiaYear2008() {
      	int year = 2008;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	//National Day 
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2009
    @Test
    public void testSaudiArabiaYear2009() {
      	int year = 2009;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	//National Day 
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2010
    @Test
    public void testSaudiArabiaYear2010() {
      	int year = 2010;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	//National Day - On a Thursday and hence a weekend
    	//expectedHol.add(df.getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2011
    @Test
    public void testSaudiArabiaYear2011() {
      	int year = 2011;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	//National Day On a Friday and hence a weekend
    	//expectedHol.add(df.getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2012
    @Test
    public void testSaudiArabiaYear2012() {
      	int year = 2012;
      	logger.info("Testing SaudiArabia's holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	//National Day 
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); 
        
      	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
}

