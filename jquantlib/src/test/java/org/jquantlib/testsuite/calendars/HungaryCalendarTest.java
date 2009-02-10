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
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Hungary;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jia Jia
 * 
 */
public class HungaryCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(HungaryCalendarTest.class);

    private final Calendar c;

    public HungaryCalendarTest() {
        logger.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
		c	= Hungary.getCalendar(Hungary.Market.SETTLEMENT);
    }

    // 2004 - leap-year in the past
    @Test
    public void testHungaryYear2004() {
        int year = 2004;
        logger.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(15, MARCH, year));
        expectedHol.add(df.getDate(12, APRIL, year));
        expectedHol.add(df.getDate(31, MAY, year));
        expectedHol.add(df.getDate(20, AUGUST, year));
        expectedHol.add(df.getDate(1, NOVEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testHungaryYear2007() {

        int year = 2007;
        logger.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(15, MARCH, year));
        expectedHol.add(df.getDate(9, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(28, MAY, year));
        expectedHol.add(df.getDate(20, AUGUST, year));
        expectedHol.add(df.getDate(23, OCTOBER, year));
        expectedHol.add(df.getDate(1, NOVEMBER, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    @Test
    public void testHungaryYear2008() {
        int year = 2008;
        logger.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(24, MARCH, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(12, MAY, year));
        expectedHol.add(df.getDate(20, AUGUST, year));
        expectedHol.add(df.getDate(23, OCTOBER, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    @Test
    public void testHungaryYear2009() {

        int year = 2009;
        logger.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(13, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(1, JUNE, year));
        expectedHol.add(df.getDate(20, AUGUST, year));
        expectedHol.add(df.getDate(23, OCTOBER, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testHungaryYear2012() {

        int year = 2012;
        logger.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(15, MARCH, year));
        expectedHol.add(df.getDate(9, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(28, MAY, year));
        expectedHol.add(df.getDate(20, AUGUST, year));
        expectedHol.add(df.getDate(23, OCTOBER, year));
        expectedHol.add(df.getDate(1, NOVEMBER, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

}
