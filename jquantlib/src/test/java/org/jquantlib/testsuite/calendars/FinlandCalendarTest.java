/*
 Copyright (C) 2008 Joon Tiang Heng, Jia Jia
  
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

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Finland;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joon Tiang Heng
 * @author Jia Jia
 */

public class FinlandCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(FinlandCalendarTest.class);

    //TODO: private final Calendar settlement;
    private final Calendar exchange;
	
	public FinlandCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	    //TODO: this.settlement = Finland.getCalendar(Finland.Market.Settlement);
	    this.exchange   = Finland.getCalendar(Finland.Market.HSE);
	}

    @Test
    public void testFinlandYear2004() {
        int year = 2004;
    	logger.info("Testing " + Finland.Market.HSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(6, JANUARY, year));
        expectedHol.add(df.getDate(9, APRIL, year));
        expectedHol.add(df.getDate(12, APRIL, year));
        expectedHol.add(df.getDate(20, MAY, year));
        expectedHol.add(df.getDate(18, JUNE, year));
        expectedHol.add(df.getDate(6, DECEMBER, year));
        expectedHol.add(df.getDate(24, DECEMBER, year));
        expectedHol.add(df.getDate(31, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testFinlandYear2005() {
        int year = 2005;
    	logger.info("Testing " + Finland.Market.HSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(6, JANUARY, year));
        expectedHol.add(df.getDate(25, MARCH, year));
        expectedHol.add(df.getDate(28, MARCH, year));
        expectedHol.add(df.getDate(5, MAY, year));
        expectedHol.add(df.getDate(24, JUNE, year));
        expectedHol.add(df.getDate(6, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testFinlandYear2006() {
        int year = 2006;
    	logger.info("Testing " + Finland.Market.HSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(6, JANUARY, year));
        expectedHol.add(df.getDate(14, APRIL, year));
        expectedHol.add(df.getDate(17, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(25, MAY, year));
        expectedHol.add(df.getDate(23, JUNE, year));
        expectedHol.add(df.getDate(6, DECEMBER, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testFinlandYear2007() {
        int year = 2007;
    	logger.info("Testing " + Finland.Market.HSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(6, APRIL, year));
        expectedHol.add(df.getDate(9, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(17, MAY, year));
        expectedHol.add(df.getDate(22, JUNE, year));
        expectedHol.add(df.getDate(6, DECEMBER, year));
        expectedHol.add(df.getDate(24, DECEMBER, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));
        expectedHol.add(df.getDate(31, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    // 2008 - current year
    @Test
    public void testFinlandYear2008() {
        int year = 2008;
    	logger.info("Testing " + Finland.Market.HSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(21, MARCH, year));
        expectedHol.add(df.getDate(24, MARCH, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(20, JUNE, year));
        expectedHol.add(df.getDate(24, DECEMBER, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));
        expectedHol.add(df.getDate(31, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testFinlandYear2009() {
        int year = 2009;
    	logger.info("Testing " + Finland.Market.HSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(6, JANUARY, year));
        expectedHol.add(df.getDate(10, APRIL, year));
        expectedHol.add(df.getDate(13, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(21, MAY, year));
        expectedHol.add(df.getDate(19, JUNE, year));       
        expectedHol.add(df.getDate(24, DECEMBER, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(31, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testFinlandYear2010() {
        int year = 2010;
    	logger.info("Testing " + Finland.Market.HSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(6, JANUARY, year));
        expectedHol.add(df.getDate(2, APRIL, year));
        expectedHol.add(df.getDate(5, APRIL, year));
        expectedHol.add(df.getDate(13, MAY, year));
        expectedHol.add(df.getDate(18, JUNE, year));
        expectedHol.add(df.getDate(6, DECEMBER, year));
        expectedHol.add(df.getDate(24, DECEMBER, year));
        expectedHol.add(df.getDate(31, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testFinlandYear2011() {
        int year = 2011;
    	logger.info("Testing " + Finland.Market.HSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(6, JANUARY, year));
        expectedHol.add(df.getDate(22, APRIL, year));
        expectedHol.add(df.getDate(25, APRIL, year));
        expectedHol.add(df.getDate(2, JUNE, year));
        expectedHol.add(df.getDate(24, JUNE, year));
        expectedHol.add(df.getDate(6, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testFinlandYear2012() {
        int year = 2012;
    	logger.info("Testing " + Finland.Market.HSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(6, JANUARY, year));
        expectedHol.add(df.getDate(6, APRIL, year));
        expectedHol.add(df.getDate(9, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(17, MAY, year));
        expectedHol.add(df.getDate(22, JUNE, year));
        expectedHol.add(df.getDate(6, DECEMBER, year));
        expectedHol.add(df.getDate(24, DECEMBER, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));
        expectedHol.add(df.getDate(31, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

}
