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

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Hungary;
import org.jquantlib.util.Date;
import org.junit.Test;

/**
 * @author Jia Jia
 *
 */
public class HungaryCalendarTest {

    private final Calendar c;

    public HungaryCalendarTest() {
        QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
		c	= Hungary.getCalendar(Hungary.Market.SETTLEMENT);
    }

    // 2004 - leap-year in the past
    @Test
    public void testHungaryYear2004() {
        final int year = 2004;
        QL.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(15, MARCH, year));
        expectedHol.add(new Date(12, APRIL, year));
        expectedHol.add(new Date(31, MAY, year));
        expectedHol.add(new Date(20, AUGUST, year));
        expectedHol.add(new Date(1, NOVEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testHungaryYear2007() {

        final int year = 2007;
        QL.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(15, MARCH, year));
        expectedHol.add(new Date(9, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(28, MAY, year));
        expectedHol.add(new Date(20, AUGUST, year));
        expectedHol.add(new Date(23, OCTOBER, year));
        expectedHol.add(new Date(1, NOVEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    @Test
    public void testHungaryYear2008() {
        final int year = 2008;
        QL.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(24, MARCH, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(12, MAY, year));
        expectedHol.add(new Date(20, AUGUST, year));
        expectedHol.add(new Date(23, OCTOBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    @Test
    public void testHungaryYear2009() {

        final int year = 2009;
        QL.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(13, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(1, JUNE, year));
        expectedHol.add(new Date(20, AUGUST, year));
        expectedHol.add(new Date(23, OCTOBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testHungaryYear2012() {

        final int year = 2012;
        QL.info("Testing " + c.getName() + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(15, MARCH, year));
        expectedHol.add(new Date(9, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(28, MAY, year));
        expectedHol.add(new Date(20, AUGUST, year));
        expectedHol.add(new Date(23, OCTOBER, year));
        expectedHol.add(new Date(1, NOVEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

}
