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

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Brazil;
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
	private final static Logger logger = LoggerFactory.getLogger(DenmarkCalendarTest.class);

	private final Calendar exchange;

	public DenmarkCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		exchange = Denmark.getCalendar(Denmark.Market.CSE);
	}
        	
	@Test
	public void testCSEYear2004() {
		int year = 2004;
    	logger.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(df.getDate(8, APRIL, year));
		// good friday
		expectedHol.add(df.getDate(9, APRIL, year));
		// easter monday
		expectedHol.add(df.getDate(12, APRIL, year));

		// great prayer day
		expectedHol.add(df.getDate(7, MAY, year));
		// ascension
		expectedHol.add(df.getDate(20, MAY, year));
		// whit monday
		expectedHol.add(df.getDate(31, MAY, year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2005() {
		int year = 2005;
    	logger.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(24, MARCH, year));
		// good friday
		expectedHol.add(df.getDate(25, MARCH, year));
		// easter monday
		expectedHol.add(df.getDate(28, MARCH, year));

		// great prayer day
		expectedHol.add(df.getDate(22, APRIL, year));
		// ascension
		expectedHol.add(df.getDate(5, MAY, year));
		// whit monday
		expectedHol.add(df.getDate(16, MAY, year));
		// boxing day
		expectedHol.add(df.getDate(26, DECEMBER, year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2006() {
		int year = 2006;
    	logger.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(13, APRIL, year));
		// maunday thursday
		expectedHol.add(df.getDate(14, APRIL, year));
		// good friday
		expectedHol.add(df.getDate(17, APRIL, year));
		// easter monday
		expectedHol.add(df.getDate(12, MAY, year));

		// great prayer day
		expectedHol.add(df.getDate(25, MAY, year));
		// ascension
		expectedHol.add(df.getDate(5, JUNE, year));
		// christmas
		expectedHol.add(df.getDate(25, DECEMBER, year));
		// boxing day
		expectedHol.add(df.getDate(26, DECEMBER, year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2007() {
		int year = 2007;
    	logger.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(df.getDate(5, APRIL, year));
		// good friday
		expectedHol.add(df.getDate(6, APRIL, year));
		// easter monday
		expectedHol.add(df.getDate(9, APRIL, year));

		// great prayer day
		expectedHol.add(df.getDate(4, MAY, year));
		// ascension
		expectedHol.add(df.getDate(17, MAY, year));
		// whit monday
		expectedHol.add(df.getDate(28, MAY, year));
		// constitution day
		expectedHol.add(df.getDate(5, JUNE, year));
		// christmas eve
		expectedHol.add(df.getDate(24, DECEMBER, year));
		// christmas
		expectedHol.add(df.getDate(25, DECEMBER, year));
		// boxing day
		expectedHol.add(df.getDate(26, DECEMBER, year));
		// new year's eve
		expectedHol.add(df.getDate(31, DECEMBER, year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	// 2008 - current year
	@Test
	public void testCSEYear2008() {
		int year = 2008;
    	logger.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(df.getDate(20, MARCH, year));
		// good friday
		expectedHol.add(df.getDate(21, MARCH, year));
		// easter monday
		expectedHol.add(df.getDate(24, MARCH, year));

		// great prayer day
		expectedHol.add(df.getDate(18, APRIL, year));
		// ascension
		expectedHol.add(df.getDate(1, MAY, year));
		// whit monday
		expectedHol.add(df.getDate(12, MAY, year));
		// constitution day
		expectedHol.add(df.getDate(5, JUNE, year));
		// christmas eve
		expectedHol.add(df.getDate(24, DECEMBER, year));
		// christmas
		expectedHol.add(df.getDate(25, DECEMBER, year));
		// boxing day		
		expectedHol.add(df.getDate(26, DECEMBER, year));		
		// boxing day		
		expectedHol.add(df.getDate(31, DECEMBER, year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2009() {
		int year = 2009;
    	logger.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(df.getDate(9, APRIL, year));
		// good friday
		expectedHol.add(df.getDate(10, APRIL, year));
		// easter monday
		expectedHol.add(df.getDate(13, APRIL, year));

		// great prayer day
		expectedHol.add(df.getDate(8, MAY, year));
		// ascension
		expectedHol.add(df.getDate(21, MAY, year));
		// ascension
		expectedHol.add(df.getDate(22, MAY, year));
		// whit monday
		expectedHol.add(df.getDate(1, JUNE, year));
		// constitution day
		expectedHol.add(df.getDate(5, JUNE, year));
		// christmas eve
		expectedHol.add(df.getDate(24, DECEMBER, year));
		// christmas
		expectedHol.add(df.getDate(25, DECEMBER, year));
		expectedHol.add(df.getDate(31, DECEMBER, year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2010() {
		int year = 2010;
    	logger.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(df.getDate(1, APRIL, year));
		// good friday
		expectedHol.add(df.getDate(2, APRIL, year));
		// easter monday
		expectedHol.add(df.getDate(5, APRIL, year));

		// great prayer day
		expectedHol.add(df.getDate(30, APRIL, year));
		// ascension
		expectedHol.add(df.getDate(13, MAY, year));
		// whit monday
		expectedHol.add(df.getDate(24, MAY, year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2012() {
		int year = 2012;
    	logger.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(5, APRIL, year));
		// maunday thursday
		expectedHol.add(df.getDate(6, APRIL, year));
		// good friday
		expectedHol.add(df.getDate(9, APRIL, year));
		// easter monday
		expectedHol.add(df.getDate(4, MAY, year));

		// great prayer day
		expectedHol.add(df.getDate(17, MAY, year));
		// ascension
		expectedHol.add(df.getDate(28, MAY, year));
		// whit monday
		expectedHol.add(df.getDate(5, JUNE, year));
		// christmas
		expectedHol.add(df.getDate(25, DECEMBER, year));
		// boxing day
		expectedHol.add(df.getDate(26, DECEMBER, year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2011() {
		int year = 2011;
    	logger.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(21, APRIL, year));
		// maunday thursday
		expectedHol.add(df.getDate(22, APRIL, year));
		// good friday
		expectedHol.add(df.getDate(25, APRIL, year));
		// easter monday
		expectedHol.add(df.getDate(20, MAY, year));

		// great prayer day
		expectedHol.add(df.getDate(2, JUNE, year));
		// ascension
		expectedHol.add(df.getDate(13, JUNE, year));
		// boxing day
		expectedHol.add(df.getDate(26, DECEMBER, year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

}
