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
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.HongKong;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 *
 *
 */
//FIXME :: work in progress [RICHARD]
public class HongKongCalendarTest {
	
    private final static Logger logger = LoggerFactory.getLogger(HongKongCalendarTest.class);

    public HongKongCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
    // 2009 -- taken from Exchange website http://www.hkex.com.hk
    
    //	 1-Jan-09   Thursday    The first day of January
    //	26-Jan-09   Monday      Lunar New Year's Day
    //	27-Jan-09   Tuesday     The second day of Lunar New Year
    //	28-Jan-09   Wednesday   The third day of Lunar New Year
    //	10-Apr-09   Friday      Good Friday
    //	13-Apr-09   Monday      Easter Monday
    //	 1-May-09   Friday      Labour Day
    //	28-May-09   Thursday    Tuen Ng Festival
    //	 1-Jul-09   Wednesday   Hong Kong Special Administrative Region Establishment Day
    //	 1-Oct-09   Thursday    National Day
    //	26-Oct-09   Monday      Chung Yeung Festival
    //	25-Dec-09   Friday      Christmas Day
	
	@Test public void testHongKongYear2009() {
        final int year = 2009;
        logger.info("Testing Hong Kong's holiday list for the year " + year + "...");
        
        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        expectedHol.add(df.getDate(1,JANUARY,year));    // The first day of January 
        expectedHol.add(df.getDate(26,JANUARY,year));   // Lunar New Year's Day 
        expectedHol.add(df.getDate(27,JANUARY,year));   // The second day of Lunar New Year 
        expectedHol.add(df.getDate(28,JANUARY,year));   // The third day of Lunar New Year 
        expectedHol.add(df.getDate(10,APRIL,year));     // Good Friday 
        expectedHol.add(df.getDate(13,APRIL,year));     // Easter Monday 
        expectedHol.add(df.getDate(1,MAY,year));        // Labour Day               
        expectedHol.add(df.getDate(28,MAY,year));       // Tuen Ng Festival    
        expectedHol.add(df.getDate(1,JULY,year));       // Hong Kong Special Administrative Region Establishment Day
        expectedHol.add(df.getDate(1,OCTOBER,year));    // National Day
        expectedHol.add(df.getDate(26,OCTOBER,year));   // Chung Yeung festival
        expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        
        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

    
    // 2008 -- taken from Exchange website http://www.hkex.com.hk
    
    //     1-Jan-08   Tuesday     The first day of January
    //     7-Feb-08   Thursday    Lunar New Year's Day
    //     8-Feb-08   Friday      The second day of Lunar New Year
    //    21-Mar-08   Friday      Good Friday
    //    24-Mar-08   Monday      Easter Monday
    //     4-Apr-08   Friday      Ching Ming Festival
    //     1-May-08   Thursday    Labour Day
    //    12-May-08   Monday      The Buddha's Birthday
    //     9-Jun-08   Monday      The day following Tuen Ng Festival
    //     1-Jul-08   Tuesday     Hong Kong Special Administrative Region Establishment Day
    //    15-Sep-08   Monday      The day following Chinese Mid-Autumn Festival
    //     1-Oct-08   Wednesday   National Day
    //     7-Oct-08   Tuesday     Chung Yeung Festival
    //    25-Dec-08   Thursday    Christmas Day
    //    26-Dec-08   Friday      The first weekday after Christmas Day
	
	@Test public void testHongKongYear2008() {
      	final int year = 2008;
        logger.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
		expectedHol.add(df.getDate(1,JANUARY,year));    // The first day of January 
    	expectedHol.add(df.getDate(7,FEBRUARY,year));   // Lunar New Year's Day 
		expectedHol.add(df.getDate(8,FEBRUARY,year));   // The second day of Lunar New Year
		expectedHol.add(df.getDate(21,MARCH,year));     // Good Friday 
		expectedHol.add(df.getDate(24,MARCH,year));     // Easter Monday 
		expectedHol.add(df.getDate(4,APRIL,year));      // Ching Ming Festival    	    	
		expectedHol.add(df.getDate(1,MAY,year));        // Labour Day    	    	
        expectedHol.add(df.getDate(12,MAY,year));       // The Buddha's Birthday             
		expectedHol.add(df.getDate(9,JUNE,year));       // The day following Tuen Ng Festival    
        expectedHol.add(df.getDate(1,JULY,year));       // Hong Kong Special Administrative Region Establishment Day
    	expectedHol.add(df.getDate(15,SEPTEMBER,year)); // The day following Chinese Mid-Autumn Festival
        expectedHol.add(df.getDate(1,OCTOBER,year));    // National Day
        expectedHol.add(df.getDate(7,OCTOBER,year));    // Chung Yeung festival
        expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(df.getDate(26,DECEMBER,year));  // The first weekday after Christmas Day
    	
        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

	
	// 2007 -- simply taken from rules
    
	@Test public void testHongKongYear2007() {
        final int year = 2007;
        logger.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = c.getHolidayList(df.getDate(01, Month.JANUARY, year), df.getDate(31, Month.DECEMBER, year), false);
        
        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

    
    // 2006 -- simply taken from rules
    
	@Test public void testHongKongYear2006() {
        final int year = 2006;
        logger.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = c.getHolidayList(df.getDate(01, Month.JANUARY, year), df.getDate(31, Month.DECEMBER, year), false);
        
        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

    
    // 2005 -- simply taken from rules
    
    @Test public void testHongKongYear2005() {
        final int year = 2005;
        logger.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = c.getHolidayList(df.getDate(01, Month.JANUARY, year), df.getDate(31, Month.DECEMBER, year), false);
        
        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

    
    // 2004 -- simply taken from rules
    
    @Test public void testHongKongYear2004() {
        final int year = 2004;
        logger.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = c.getHolidayList(df.getDate(01, Month.JANUARY, year), df.getDate(31, Month.DECEMBER, year), false);
        
        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }
    
}
