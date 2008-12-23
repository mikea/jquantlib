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
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Japan;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Richard Gomes
 */
public class JapanCalendarTest {
	
    private final static Logger logger = LoggerFactory.getLogger(JapanCalendarTest.class);

    public JapanCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
    // 2012 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/index.html?year=2012&country=26
    //
	//     1 Jan	New Year's Day
	//     2 Jan	'New Year's Day' observed
	//     9 Jan	Coming of Age Day
	//    11 Feb	National Foundation Day
	//    20 Mar	Spring Equinox
	//    29 Apr	Shōwa Day
	//    	 	 	
	//    30 Apr	'Shōwa Day' observed
	//     3 May	Constitution Memorial Day
	//     4 May	Greenery Day
	//     5 May	Children's Day
	//    16 Jul	Sea Day
	//    17 Sep	Respect for the Aged Day
	//    	 	 	
	//    22 Sep	Autumn Equinox
	//     8 Oct	Sports Day
	//     3 Nov	Culture Day
	//    23 Nov	Labor Thanksgiving Day
	//    23 Dec	Emperor's Birthday
	//    24 Dec	'Emperor's Birthday' observed
    
    @Test public void testJapanYear2012() {
        final int year = 2012;
        logger.info("Testing Japan's holiday list for the year " + year + "...");

        final Calendar c = Japan.getCalendar();
        
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		// Sun: expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(df.getDate( 2,JANUARY,year));   // Bank Holiday
		expectedHol.add(df.getDate( 3,JANUARY,year));   // Bank Holiday
    	expectedHol.add(df.getDate( 9,JANUARY,year));   // Coming of Age Day
		// Sat: expectedHol.add(df.getDate(11,FEBRUARY,year));  // National Foundation Day
		expectedHol.add(df.getDate(20,MARCH,year));     // Spring Equinox observed
		// Sun: expectedHol.add(df.getDate(29,APRIL,year));     // Showa Day
		expectedHol.add(df.getDate(30,APRIL,year));     // Showa Day observed
		expectedHol.add(df.getDate( 3,MAY,year));       // Constitution Memorial Day    	
		expectedHol.add(df.getDate( 4,MAY,year));       // Greenery Day   	
        // Sat: expectedHol.add(df.getDate( 5,MAY,year));       // Children's Day        
		expectedHol.add(df.getDate(16,JULY,year));      // Sea Day
        expectedHol.add(df.getDate(17,SEPTEMBER,year)); // Respect for the Aged Day
    	// Sat: expectedHol.add(df.getDate(22,SEPTEMBER,year)); // Autumn Equinox
        expectedHol.add(df.getDate( 8,OCTOBER,year));   // Sports Day
        // Sat: expectedHol.add(df.getDate( 3,NOVEMBER,year));  // Culture Day
        expectedHol.add(df.getDate(23,NOVEMBER,year));  // Labor Thanksgiving Day
        // Sun: expectedHol.add(df.getDate(23,DECEMBER,year));  // Emperor's Birthday
        expectedHol.add(df.getDate(24,DECEMBER,year));  // Emperor's Birthday observed
		expectedHol.add(df.getDate(31,DECEMBER,year));  // Bank Holiday

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }
    
    // 2011 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/index.html?year=2011&country=26
    //
	//     1 Jan	New Year's Day
	//     2 Jan	Bank Holiday
	//     3 Jan	Bank Holiday
	//    10 Jan	Coming of Age Day
	//    11 Feb	National Foundation Day
	//    21 Mar	Spring Equinox
	//    29 Apr	Shōwa Day
	//    	 	 	
	//     3 May	Constitution Memorial Day
	//     4 May	Greenery Day
	//     5 May	Children's Day
	//    18 Jul	Sea Day
	//    19 Sep	Respect for the Aged Day
	//    	 	 	
	//    23 Sep	Autumn Equinox
	//    10 Oct	Sports Day
	//     3 Nov	Culture Day
	//    23 Nov	Labor Thanksgiving Day
	//    23 Dec	Emperor's Birthday
	//    31 Dec	Bank Holiday
    
    @Test public void testJapanYear2011() {
        final int year = 2011;
        logger.info("Testing Japan's holiday list for the year " + year + "...");

        final Calendar c = Japan.getCalendar();
        
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		// Sat: expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		// Sun: expectedHol.add(df.getDate( 2,JANUARY,year));   // Bank Holiday
		expectedHol.add(df.getDate( 3,JANUARY,year));   // Bank Holiday
    	expectedHol.add(df.getDate(10,JANUARY,year));   // Coming of Age Day
		expectedHol.add(df.getDate(11,FEBRUARY,year));  // National Foundation Day
		expectedHol.add(df.getDate(21,MARCH,year));     // Spring Equinox observed
		expectedHol.add(df.getDate(29,APRIL,year));     // Showa Day
		expectedHol.add(df.getDate( 3,MAY,year));       // Constitution Memorial Day    	
		expectedHol.add(df.getDate( 4,MAY,year));       // Greenery Day   	
        expectedHol.add(df.getDate( 5,MAY,year));       // Children's Day        
		expectedHol.add(df.getDate(18,JULY,year));      // Sea Day
        expectedHol.add(df.getDate(19,SEPTEMBER,year)); // Respect for the Aged Day
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); // Autumn Equinox
        expectedHol.add(df.getDate(10,OCTOBER,year));   // Sports Day
        expectedHol.add(df.getDate( 3,NOVEMBER,year));  // Culture Day
        expectedHol.add(df.getDate(23,NOVEMBER,year));  // Labor Thanksgiving Day
        expectedHol.add(df.getDate(23,DECEMBER,year));  // Emperor's Birthday
		// Sat: expectedHol.add(df.getDate(31,DECEMBER,year));  // Bank Holiday

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }
    
    // 2010 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/index.html?year=2010&country=26
    //
	//     1 Jan	New Year's Day
	//     2 Jan	Bank Holiday
	//     3 Jan	Bank Holiday
	//    11 Jan	Coming of Age Day
	//    11 Feb	National Foundation Day
	//    21 Mar	Spring Equinox
	//    22 Mar	'Spring Equinox' observed
	//    29 Apr	Shōwa Day
	//     3 May	Constitution Memorial Day
	//     4 May	Greenery Day
	//     5 May	Children's Day
	//    19 Jul	Sea Day
	//    20 Sep	Respect for the Aged Day
	//    23 Sep	Autumn Equinox
	//    11 Oct	Sports Day
	//     3 Nov	Culture Day
	//    23 Nov	Labor Thanksgiving Day
	//    23 Dec	Emperor's Birthday
	//    31 Dec	Bank Holiday
    
    
    @Test public void testJapanYear2010() {
        final int year = 2010;
        logger.info("Testing Japan's holiday list for the year " + year + "...");

        final Calendar c = Japan.getCalendar();
        
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		// Sat: expectedHol.add(df.getDate( 2,JANUARY,year));   // Bank Holiday
		// Sun: expectedHol.add(df.getDate( 3,JANUARY,year));   // Bank Holiday
    	expectedHol.add(df.getDate(11,JANUARY,year));   // Coming of Age Day
		expectedHol.add(df.getDate(11,FEBRUARY,year));  // National Foundation Day
		expectedHol.add(df.getDate(22,MARCH,year));     // Spring Equinox observed
		expectedHol.add(df.getDate(29,APRIL,year));     // Showa Day
		expectedHol.add(df.getDate( 3,MAY,year));       // Constitution Memorial Day    	
		expectedHol.add(df.getDate( 4,MAY,year));       // Greenery Day   	
        expectedHol.add(df.getDate( 5,MAY,year));       // Children's Day        
		expectedHol.add(df.getDate(19,JULY,year));      // Sea Day
        expectedHol.add(df.getDate(20,SEPTEMBER,year)); // Respect for the Aged Day
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); // Autumn Equinox
        expectedHol.add(df.getDate(11,OCTOBER,year));   // Sports Day
        expectedHol.add(df.getDate( 3,NOVEMBER,year));  // Culture Day
        expectedHol.add(df.getDate(23,NOVEMBER,year));  // Labor Thanksgiving Day
        expectedHol.add(df.getDate(23,DECEMBER,year));  // Emperor's Birthday
		expectedHol.add(df.getDate(31,DECEMBER,year));  // Bank Holiday

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }
    
	// 2009 -- simply taken from rules
	// Comparing with http://www.mizuhocbk.co.jp/english/service/custody/holiday2009.html
    //
	//     1 Jan	New Year's Day
	//     2 Jan	Bank Holiday
	////   3 Jan	Bank Holiday
	//    12 Jan	Coming of Age Day
	//    11 Feb	National Foundation Day
	//    20 Mar	Spring Equinox
	//    29 Apr	Showa Day
	////   3 May	Constitution Memorial Day
	//     4 May	Greenery Day
	//     5 May	Children's Day
	//     6 May	Alternative Constitution Memorial Day
	//    20 Jul	Sea Day1 Jan	New Year's Day
	//    21 Sep	Respect for the Aged Day
    //	  22 Sep    Bank Holiday   
    //    23 Sep	Autumn Equinox
	//    12 Oct	Sports Day
	//     3 Nov	Culture Day
	//    23 Nov	Labor Thanksgiving Day
	//    23 Dec	Emperor's Birthday
	//    31 Dec	Bank Holiday
    
	@Test public void testJapanYear2009() {
        final int year = 2009;
        logger.info("Testing Japan's holiday list for the year " + year + "...");

        final Calendar c = Japan.getCalendar();
        
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(df.getDate( 2,JANUARY,year));   // Bank Holiday
		//. Sat: expectedHol.add(df.getDate( 3,JANUARY,year));   // Bank Holiday
    	expectedHol.add(df.getDate(12,JANUARY,year));   // Coming of Age Day
		expectedHol.add(df.getDate(11,FEBRUARY,year));  // National Foundation Day
		expectedHol.add(df.getDate(20,MARCH,year));     // Spring Equinox
		expectedHol.add(df.getDate(29,APRIL,year));     // Showa Day
		// expectedHol.add(df.getDate( 3,MAY,year));       // Constitution Memorial Day    	
		expectedHol.add(df.getDate( 4,MAY,year));       // Greenery Day   	
        expectedHol.add(df.getDate( 5,MAY,year));       // Children's Day        
        expectedHol.add(df.getDate( 6,MAY,year));       // alternative Constitution Memorial Day        
		expectedHol.add(df.getDate(20,JULY,year));      // Sea Day
        expectedHol.add(df.getDate(21,SEPTEMBER,year)); // Respect for the Aged Day
        expectedHol.add(df.getDate(22,SEPTEMBER,year)); // Bank Holiday
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); // Autumn Equinox
        expectedHol.add(df.getDate(12,OCTOBER,year));   // Sports Day
        expectedHol.add(df.getDate( 3,NOVEMBER,year));  // Culture Day
        expectedHol.add(df.getDate(23,NOVEMBER,year));  // Labor Thanksgiving Day
        expectedHol.add(df.getDate(23,DECEMBER,year));  // Emperor's Birthday
		expectedHol.add(df.getDate(31,DECEMBER,year));  // Bank Holiday

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

    
	// 2008 -- simply taken from rules
	// Comparing with http://www.mizuhocbk.co.jp/english/service/custody/holiday2008.html
    //
	//     1 Jan	New Year's Day
	//     2 Jan	Bank Holiday
	//     3 Jan	Bank Holiday
	//    14 Jan	Coming of Age Day
	//    11 Feb	National Foundation Day
	//    20 Mar	Spring Equinox
	//    29 Apr	Showa Day
	////   3 May	Constitution Memorial Day
	////   4 May	Greenery Day
	//     5 May	Children's Day
	//     6 May	Alternative Greenery Day
	//    21 Jul	Sea Day
	//    15 Sep	Respect for the Aged Day4
	//    23 Sep	Autumn Equinox
	//    13 Oct	Sports Day
	//     3 Nov	Culture Day
	//    24 Nov	Labor Thanksgiving Day
	//    23 Dec	Emperor's Birthday
	//    31 Dec	Bank Holiday
    
	@Test public void testJapanYear2008() {
        final int year = 2008;
        logger.info("Testing Japan's holiday list for the year " + year + "...");

        final Calendar c = Japan.getCalendar();
        
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(df.getDate( 2,JANUARY,year));   // Bank Holiday
		expectedHol.add(df.getDate( 3,JANUARY,year));   // Bank Holiday
    	expectedHol.add(df.getDate(14,JANUARY,year));   // Coming of Age Day
		expectedHol.add(df.getDate(11,FEBRUARY,year));  // National Foundation Day
		expectedHol.add(df.getDate(20,MARCH,year));     // Spring Equinox
		expectedHol.add(df.getDate(29,APRIL,year));     // Showa Day
		// expectedHol.add(df.getDate( 3,MAY,year));       // Constitution Memorial Day    	
		// expectedHol.add(df.getDate( 4,MAY,year));       // Between Day   	
        expectedHol.add(df.getDate( 5,MAY,year));       // Children's Day        
        expectedHol.add(df.getDate( 6,MAY,year));       // alternative Greenery Day        
		expectedHol.add(df.getDate(21,JULY,year));      // Sea Day
        expectedHol.add(df.getDate(15,SEPTEMBER,year)); // Respect for the Aged Day
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); // Autumn Equinox
        expectedHol.add(df.getDate(13,OCTOBER,year));   // Sports Day
        expectedHol.add(df.getDate( 3,NOVEMBER,year));  // Culture Day
        expectedHol.add(df.getDate(24,NOVEMBER,year));  // Labor Thanksgiving Day
        expectedHol.add(df.getDate(23,DECEMBER,year));  // Emperor's Birthday
		expectedHol.add(df.getDate(31,DECEMBER,year));  // Bank Holiday

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

	
	// 2007 -- simply taken from rules
	// Comparing with http://www.mizuhocbk.co.jp/english/service/custody/holiday2007.html
    //
	//     1 Jan	New Year's Day
	//     2 Jan	Bank Holiday
	//     3 Jan	Bank Holiday
	//     8 Jan	Coming of Age Day
	//    12 Feb	National Foundation Day
	//    21 Mar	Spring Equinox
	//    30 Apr	Greenery Day
	//     3 May	Constitution Memorial Day
	//     4 May	Greenery Day
	////   5 May	Children's Day
	//    16 Jul	Sea Day
	//    17 Sep	Respect for the Aged Day
	//    24 Sep	Autumn Equinox
	//     8 Oct	Sports Day
	////   3 Nov	Culture Day
	//    23 Nov	Labor Thanksgiving Day
	//    24 Dec	Emperor's Birthday
	//    31 Dec	Bank Holiday
    
	@Test public void testJapanYear2007() {
        final int year = 2007;
        logger.info("Testing Japan's holiday list for the year " + year + "...");

        final Calendar c = Japan.getCalendar();
        
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(df.getDate( 2,JANUARY,year));   // Bank Holiday
		expectedHol.add(df.getDate( 3,JANUARY,year));   // Bank Holiday
    	expectedHol.add(df.getDate( 8,JANUARY,year));   // Coming of Age Day
		expectedHol.add(df.getDate(12,FEBRUARY,year));  // National Foundation Day
		expectedHol.add(df.getDate(21,MARCH,year));     // Spring Equinox
		expectedHol.add(df.getDate(30,APRIL,year));     // Greenery Day
		expectedHol.add(df.getDate( 3,MAY,year));       // Constitution Memorial Day    	
		expectedHol.add(df.getDate( 4,MAY,year));       // Between Day   	
        // Sat: expectedHol.add(df.getDate( 5,MAY,year));       // Children's Day        
		expectedHol.add(df.getDate(16,JULY,year));      // Sea Day
        expectedHol.add(df.getDate(17,SEPTEMBER,year)); // Respect for the Aged Day
    	expectedHol.add(df.getDate(24,SEPTEMBER,year)); // Autumn Equinox
        expectedHol.add(df.getDate( 8,OCTOBER,year));   // Sports Day
        // Sat: expectedHol.add(df.getDate( 3,NOVEMBER,year));  // Culture Day
        expectedHol.add(df.getDate(23,NOVEMBER,year));  // Labor Thanksgiving Day
        expectedHol.add(df.getDate(24,DECEMBER,year));  // Emperor's Birthday
		expectedHol.add(df.getDate(31,DECEMBER,year));  // Bank Holiday

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

    
    // 2006 -- simply taken from rules
	// Comparing with http://www.mizuhocbk.co.jp/english/service/custody/holiday2006.html
    //
	////   1 Jan	New Year's Day
	//     2 Jan	Bank Holiday
	//     3 Jan	Bank Holiday
	//     9 Jan	Coming of Age Day
	////  11 Feb	National Foundation Day
	//    21 Mar	Spring Equinox
	////  29 Apr	Greenery Day
	//     3 May	Constitution Memorial Day
	//     4 May	Between Day
	//     5 May	Children's Day
	//    17 Jul	Sea Day
	//    18 Sep	Respect for the Aged Day
	////  23 Sep	Autumn Equinox
	//     9 Oct	Sports Day
	//     3 Nov	Culture Day
	//    23 Nov	Labor Thanksgiving Day
	////  23 Dec	Emperor's Birthday
	////  31 Dec	Bank Holiday
    
	@Test public void testJapanYear2006() {
        final int year = 2006;
        logger.info("Testing Japan's holiday list for the year " + year + "...");

        final Calendar c = Japan.getCalendar();
        
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		// Sun: expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(df.getDate( 2,JANUARY,year));   // Bank Holiday
		expectedHol.add(df.getDate( 3,JANUARY,year));   // Bank Holiday
    	expectedHol.add(df.getDate( 9,JANUARY,year));   // Coming of Age Day
		// Sat: expectedHol.add(df.getDate(11,FEBRUARY,year));  // National Foundation Day
		expectedHol.add(df.getDate(21,MARCH,year));     // Spring Equinox
		// Sat: expectedHol.add(df.getDate(29,APRIL,year));     // Greenery Day
		expectedHol.add(df.getDate( 3,MAY,year));       // Constitution Memorial Day    	
		expectedHol.add(df.getDate( 4,MAY,year));       // Between Day   	
        expectedHol.add(df.getDate( 5,MAY,year));       // Children's Day        
		expectedHol.add(df.getDate(17,JULY,year));      // Sea Day
        expectedHol.add(df.getDate(18,SEPTEMBER,year)); // Respect for the Aged Day
    	// Sat: expectedHol.add(df.getDate(23,SEPTEMBER,year)); // Autumn Equinox
        expectedHol.add(df.getDate( 9,OCTOBER,year));   // Sports Day
        expectedHol.add(df.getDate( 3,NOVEMBER,year));  // Culture Day
        expectedHol.add(df.getDate(23,NOVEMBER,year));  // Labor Thanksgiving Day
        // Sat: expectedHol.add(df.getDate(23,DECEMBER,year));  // Emperor's Birthday
		// Sun: expectedHol.add(df.getDate(31,DECEMBER,year));  // Bank Holiday

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

    
    // 2005 -- simply taken from rules
	// Comparing with http://www.mizuhocbk.co.jp/english/service/custody/holiday2005.html
    //
	////   1 Jan	New Year's Day
	////   2 Jan	Bank Holiday
	//     3 Jan	Bank Holiday
	//    10 Jan	Coming of Age Day
	//    11 Feb	National Foundation Day
	//    21 Mar	Spring Equinox
	//    29 Apr	Greenery Day
	//     3 May	Constitution Memorial Day
	//     4 May	Between Day
	//     5 May	Children's Day
	//    18 Jul	Sea Day
	//    19 Sep	Respect for the Aged Day
	//    23 Sep	Autumn Equinox
	//    10 Oct	Sports Day
	//     3 Nov	Culture Day
	//    23 Nov	Labor Thanksgiving Day
	//    23 Dec	Emperor's Birthday
	////  31 Dec	Bank Holiday
    
    @Test public void testJapanYear2005() {
        final int year = 2005;
        logger.info("Testing Japan's holiday list for the year " + year + "...");

        final Calendar c = Japan.getCalendar();
        
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
		// Sat: expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		// Sun: expectedHol.add(df.getDate( 2,JANUARY,year));   // Bank Holiday
		expectedHol.add(df.getDate( 3,JANUARY,year));   // Bank Holiday
    	expectedHol.add(df.getDate(10,JANUARY,year));   // Coming of Age Day
		expectedHol.add(df.getDate(11,FEBRUARY,year));  // National Foundation Day
		expectedHol.add(df.getDate(21,MARCH,year));     // Spring Equinox
		expectedHol.add(df.getDate(29,APRIL,year));     // Greenery Day
		expectedHol.add(df.getDate( 3,MAY,year));       // Constitution Memorial Day    	
		expectedHol.add(df.getDate( 4,MAY,year));       // Between Day   	
        expectedHol.add(df.getDate( 5,MAY,year));       // Children's Day        
		expectedHol.add(df.getDate(18,JULY,year));      // Sea Day
        expectedHol.add(df.getDate(19,SEPTEMBER,year)); // Respect for the Aged Day
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); // Autumn Equinox
        expectedHol.add(df.getDate(10,OCTOBER,year));   // Sports Day
        expectedHol.add(df.getDate( 3,NOVEMBER,year));  // Culture Day
        expectedHol.add(df.getDate(23,NOVEMBER,year));  // Labor Thanksgiving Day
        expectedHol.add(df.getDate(23,DECEMBER,year));  // Emperor's Birthday
		// Sat: expectedHol.add(df.getDate(31,DECEMBER,year));  // Bank Holiday

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

    
    // 2004 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/index.html?year=2004&country=26
    //
	//     1 Jan	New Year's Day
	//     2 Jan	Bank Holiday
	////   3 Jan	Bank Holiday
	//    12 Jan	Coming of Age Day
	//    11 Feb	National Foundation Day
	////  20 Mar	Spring Equinox
	//    29 Apr	Greenery Day
	//     3 May	Constitution Memorial Day
	//     4 May	Between Day
	//     5 May	Children's Day
	//    19 Jul	Sea Day
	//    20 Sep	Respect for the Aged Day
	//    23 Sep	Autumn Equinox
	//    11 Oct	Sports Day
	//     3 Nov	Culture Day
	//    23 Nov	Labor Thanksgiving Day
	//    23 Dec	Emperor's Birthday
	//    31 Dec	Bank Holiday
    
    @Test public void testJapanYear2004() {
        final int year = 2004;
        logger.info("Testing Japan's holiday list for the year " + year + "...");

        final Calendar c = Japan.getCalendar();

        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
		expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(df.getDate( 2,JANUARY,year));   // Bank Holiday
		// Sat: expectedHol.add(df.getDate( 3,JANUARY,year));   // Bank Holiday
    	expectedHol.add(df.getDate(12,JANUARY,year));   // Coming of Age Day
		expectedHol.add(df.getDate(11,FEBRUARY,year));  // National Foundation Day
		// Sat: expectedHol.add(df.getDate(20,MARCH,year));     // Spring Equinox
		expectedHol.add(df.getDate(29,APRIL,year));     // Greenery Day
		expectedHol.add(df.getDate( 3,MAY,year));       // Constitution Memorial Day    	
		expectedHol.add(df.getDate( 4,MAY,year));       // Between Day   	
        expectedHol.add(df.getDate( 5,MAY,year));       // Children's Day        
		expectedHol.add(df.getDate(19,JULY,year));      // Sea Day
        expectedHol.add(df.getDate(20,SEPTEMBER,year)); // Respect for the Aged Day
    	expectedHol.add(df.getDate(23,SEPTEMBER,year)); // Autumn Equinox
        expectedHol.add(df.getDate(11,OCTOBER,year));   // Sports Day
        expectedHol.add(df.getDate( 3,NOVEMBER,year));  // Culture Day
        expectedHol.add(df.getDate(23,NOVEMBER,year));  // Labor Thanksgiving Day
        expectedHol.add(df.getDate(23,DECEMBER,year));  // Emperor's Birthday
		expectedHol.add(df.getDate(31,DECEMBER,year));  // Bank Holiday

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }
    
}
