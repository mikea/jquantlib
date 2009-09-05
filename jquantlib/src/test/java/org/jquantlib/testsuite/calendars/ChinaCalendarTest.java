/*
 Copyright (C) 2008 Tim Swetonic

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
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.China;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Tim Swetonic
 * @author Jia Jia
 * @author Renjith Nair
 *
 */

public class ChinaCalendarTest {

    private final Calendar exchange;

	public ChinaCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        exchange = China.getCalendar(China.Market.SSE);
	}

    @Test
    public void testChinaSSEYear2004() {
        final int year = 2004;
        QL.info("Testing China holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(19, JANUARY, year));
        expectedHol.add(df.getDate(20, JANUARY, year));
        expectedHol.add(df.getDate(21, JANUARY, year));
        expectedHol.add(df.getDate(22, JANUARY, year));
        expectedHol.add(df.getDate(23, JANUARY, year));
        expectedHol.add(df.getDate(26, JANUARY, year));
        expectedHol.add(df.getDate(27, JANUARY, year));
        expectedHol.add(df.getDate(28, JANUARY, year));
        expectedHol.add(df.getDate(3, MAY, year));
        expectedHol.add(df.getDate(4, MAY, year));
        expectedHol.add(df.getDate(5, MAY, year));
        expectedHol.add(df.getDate(6, MAY, year));
        expectedHol.add(df.getDate(7, MAY, year));
        expectedHol.add(df.getDate(1, OCTOBER, year));
        expectedHol.add(df.getDate(4, OCTOBER, year));
        expectedHol.add(df.getDate(5, OCTOBER, year));
        expectedHol.add(df.getDate(6, OCTOBER, year));
        expectedHol.add(df.getDate(7, OCTOBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2005() {
        final int year = 2005;
        QL.info("Testing China holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(3, JANUARY, year));
        expectedHol.add(df.getDate(7, FEBRUARY, year));
        expectedHol.add(df.getDate(8, FEBRUARY, year));
        expectedHol.add(df.getDate(9, FEBRUARY, year));
        expectedHol.add(df.getDate(10, FEBRUARY, year));
        expectedHol.add(df.getDate(11, FEBRUARY, year));
        expectedHol.add(df.getDate(14, FEBRUARY, year));
        expectedHol.add(df.getDate(15, FEBRUARY, year));
        expectedHol.add(df.getDate(2, MAY, year));
        expectedHol.add(df.getDate(3, MAY, year));
        expectedHol.add(df.getDate(4, MAY, year));
        expectedHol.add(df.getDate(5, MAY, year));
        expectedHol.add(df.getDate(6, MAY, year));
        expectedHol.add(df.getDate(3, OCTOBER, year));
        expectedHol.add(df.getDate(4, OCTOBER, year));
        expectedHol.add(df.getDate(5, OCTOBER, year));
        expectedHol.add(df.getDate(6, OCTOBER, year));
        expectedHol.add(df.getDate(7, OCTOBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2006() {
        final int year = 2006;
        QL.info("Testing China holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(2, JANUARY, year));
        expectedHol.add(df.getDate(3, JANUARY, year));
        expectedHol.add(df.getDate(26, JANUARY, year));
        expectedHol.add(df.getDate(27, JANUARY, year));
        expectedHol.add(df.getDate(30, JANUARY, year));
        expectedHol.add(df.getDate(31, JANUARY, year));
        expectedHol.add(df.getDate(1, FEBRUARY, year));
        expectedHol.add(df.getDate(2, FEBRUARY, year));
        expectedHol.add(df.getDate(3, FEBRUARY, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(2, MAY, year));
        expectedHol.add(df.getDate(3, MAY, year));
        expectedHol.add(df.getDate(4, MAY, year));
        expectedHol.add(df.getDate(5, MAY, year));
        expectedHol.add(df.getDate(2, OCTOBER, year));
        expectedHol.add(df.getDate(3, OCTOBER, year));
        expectedHol.add(df.getDate(4, OCTOBER, year));
        expectedHol.add(df.getDate(5, OCTOBER, year));
        expectedHol.add(df.getDate(6, OCTOBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2007() {
        final int year = 2007;
        QL.info("Testing China holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(2, JANUARY, year));
        expectedHol.add(df.getDate(3, JANUARY, year));
        expectedHol.add(df.getDate(19, FEBRUARY, year));
        expectedHol.add(df.getDate(20, FEBRUARY, year));
        expectedHol.add(df.getDate(21, FEBRUARY, year));
        expectedHol.add(df.getDate(22, FEBRUARY, year));
        expectedHol.add(df.getDate(23, FEBRUARY, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(2, MAY, year));
        expectedHol.add(df.getDate(3, MAY, year));
        expectedHol.add(df.getDate(4, MAY, year));
        expectedHol.add(df.getDate(7, MAY, year));
        expectedHol.add(df.getDate(1, OCTOBER, year));
        expectedHol.add(df.getDate(2, OCTOBER, year));
        expectedHol.add(df.getDate(3, OCTOBER, year));
        expectedHol.add(df.getDate(4, OCTOBER, year));
        expectedHol.add(df.getDate(5, OCTOBER, year));
        // Interesting Fact
        //31 Dec 2007 is included as holiday in 2008 list of holidays :)
        expectedHol.add(df.getDate(31, DECEMBER, year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    // 2008 - current year
    @Test
    public void testChinaSSEYear2008() {
        final int year = 2008;
        QL.info("Testing China holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(6, FEBRUARY, year));
        expectedHol.add(df.getDate(7, FEBRUARY, year));
        expectedHol.add(df.getDate(8, FEBRUARY, year));
        expectedHol.add(df.getDate(11, FEBRUARY, year));
        expectedHol.add(df.getDate(12, FEBRUARY, year));
        expectedHol.add(df.getDate(4, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(2, MAY, year));
        expectedHol.add(df.getDate(9, JUNE, year));
        expectedHol.add(df.getDate(15, SEPTEMBER, year));
        expectedHol.add(df.getDate(29, SEPTEMBER, year));
        expectedHol.add(df.getDate(30, SEPTEMBER, year));
        expectedHol.add(df.getDate(1, OCTOBER, year));
        expectedHol.add(df.getDate(2, OCTOBER, year));
        expectedHol.add(df.getDate(3, OCTOBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2009() {
        final int year = 2009;
        QL.info("Testing China holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(2, JANUARY, year));
        expectedHol.add(df.getDate(26, JANUARY, year));
        expectedHol.add(df.getDate(27, JANUARY, year));
        expectedHol.add(df.getDate(28, JANUARY, year));
        expectedHol.add(df.getDate(29, JANUARY, year));
        expectedHol.add(df.getDate(30, JANUARY, year));
        expectedHol.add(df.getDate(6, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(28, MAY, year));
        expectedHol.add(df.getDate(29, MAY, year));
        expectedHol.add(df.getDate(1, OCTOBER, year));
        expectedHol.add(df.getDate(2, OCTOBER, year));
        expectedHol.add(df.getDate(5, OCTOBER, year));
        expectedHol.add(df.getDate(6, OCTOBER, year));
        expectedHol.add(df.getDate(7, OCTOBER, year));
        expectedHol.add(df.getDate(8, OCTOBER, year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2010() {
        final int year = 2010;
        QL.info("Testing China holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(df.getDate(1, JANUARY, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2011() {
        final int year = 2011;
        QL.info("Testing China holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2012() {
        final int year = 2012;
        QL.info("Testing China holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
}
