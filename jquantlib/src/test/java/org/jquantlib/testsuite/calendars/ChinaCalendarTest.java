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

import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.FEBRUARY;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.OCTOBER;
import static org.jquantlib.time.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.China;
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
        exchange = new China(China.Market.SSE);
	}

    @Test
    public void testChinaSSEYear2004() {
        final int year = 2004;
        QL.info("Testing China holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(19, JANUARY, year));
        expectedHol.add(new Date(20, JANUARY, year));
        expectedHol.add(new Date(21, JANUARY, year));
        expectedHol.add(new Date(22, JANUARY, year));
        expectedHol.add(new Date(23, JANUARY, year));
        expectedHol.add(new Date(26, JANUARY, year));
        expectedHol.add(new Date(27, JANUARY, year));
        expectedHol.add(new Date(28, JANUARY, year));
        expectedHol.add(new Date(3, MAY, year));
        expectedHol.add(new Date(4, MAY, year));
        expectedHol.add(new Date(5, MAY, year));
        expectedHol.add(new Date(6, MAY, year));
        expectedHol.add(new Date(7, MAY, year));
        expectedHol.add(new Date(1, OCTOBER, year));
        expectedHol.add(new Date(4, OCTOBER, year));
        expectedHol.add(new Date(5, OCTOBER, year));
        expectedHol.add(new Date(6, OCTOBER, year));
        expectedHol.add(new Date(7, OCTOBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2005() {
        final int year = 2005;
        QL.info("Testing China holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(3, JANUARY, year));
        expectedHol.add(new Date(7, FEBRUARY, year));
        expectedHol.add(new Date(8, FEBRUARY, year));
        expectedHol.add(new Date(9, FEBRUARY, year));
        expectedHol.add(new Date(10, FEBRUARY, year));
        expectedHol.add(new Date(11, FEBRUARY, year));
        expectedHol.add(new Date(14, FEBRUARY, year));
        expectedHol.add(new Date(15, FEBRUARY, year));
        expectedHol.add(new Date(2, MAY, year));
        expectedHol.add(new Date(3, MAY, year));
        expectedHol.add(new Date(4, MAY, year));
        expectedHol.add(new Date(5, MAY, year));
        expectedHol.add(new Date(6, MAY, year));
        expectedHol.add(new Date(3, OCTOBER, year));
        expectedHol.add(new Date(4, OCTOBER, year));
        expectedHol.add(new Date(5, OCTOBER, year));
        expectedHol.add(new Date(6, OCTOBER, year));
        expectedHol.add(new Date(7, OCTOBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2006() {
        final int year = 2006;
        QL.info("Testing China holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(2, JANUARY, year));
        expectedHol.add(new Date(3, JANUARY, year));
        expectedHol.add(new Date(26, JANUARY, year));
        expectedHol.add(new Date(27, JANUARY, year));
        expectedHol.add(new Date(30, JANUARY, year));
        expectedHol.add(new Date(31, JANUARY, year));
        expectedHol.add(new Date(1, FEBRUARY, year));
        expectedHol.add(new Date(2, FEBRUARY, year));
        expectedHol.add(new Date(3, FEBRUARY, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(2, MAY, year));
        expectedHol.add(new Date(3, MAY, year));
        expectedHol.add(new Date(4, MAY, year));
        expectedHol.add(new Date(5, MAY, year));
        expectedHol.add(new Date(2, OCTOBER, year));
        expectedHol.add(new Date(3, OCTOBER, year));
        expectedHol.add(new Date(4, OCTOBER, year));
        expectedHol.add(new Date(5, OCTOBER, year));
        expectedHol.add(new Date(6, OCTOBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2007() {
        final int year = 2007;
        QL.info("Testing China holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(2, JANUARY, year));
        expectedHol.add(new Date(3, JANUARY, year));
        expectedHol.add(new Date(19, FEBRUARY, year));
        expectedHol.add(new Date(20, FEBRUARY, year));
        expectedHol.add(new Date(21, FEBRUARY, year));
        expectedHol.add(new Date(22, FEBRUARY, year));
        expectedHol.add(new Date(23, FEBRUARY, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(2, MAY, year));
        expectedHol.add(new Date(3, MAY, year));
        expectedHol.add(new Date(4, MAY, year));
        expectedHol.add(new Date(7, MAY, year));
        expectedHol.add(new Date(1, OCTOBER, year));
        expectedHol.add(new Date(2, OCTOBER, year));
        expectedHol.add(new Date(3, OCTOBER, year));
        expectedHol.add(new Date(4, OCTOBER, year));
        expectedHol.add(new Date(5, OCTOBER, year));
        // Interesting Fact
        //31 Dec 2007 is included as holiday in 2008 list of holidays :)
        expectedHol.add(new Date(31, DECEMBER, year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    // 2008 - current year
    @Test
    public void testChinaSSEYear2008() {
        final int year = 2008;
        QL.info("Testing China holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(6, FEBRUARY, year));
        expectedHol.add(new Date(7, FEBRUARY, year));
        expectedHol.add(new Date(8, FEBRUARY, year));
        expectedHol.add(new Date(11, FEBRUARY, year));
        expectedHol.add(new Date(12, FEBRUARY, year));
        expectedHol.add(new Date(4, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(2, MAY, year));
        expectedHol.add(new Date(9, JUNE, year));
        expectedHol.add(new Date(15, SEPTEMBER, year));
        expectedHol.add(new Date(29, SEPTEMBER, year));
        expectedHol.add(new Date(30, SEPTEMBER, year));
        expectedHol.add(new Date(1, OCTOBER, year));
        expectedHol.add(new Date(2, OCTOBER, year));
        expectedHol.add(new Date(3, OCTOBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2009() {
        final int year = 2009;
        QL.info("Testing China holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(2, JANUARY, year));
        expectedHol.add(new Date(26, JANUARY, year));
        expectedHol.add(new Date(27, JANUARY, year));
        expectedHol.add(new Date(28, JANUARY, year));
        expectedHol.add(new Date(29, JANUARY, year));
        expectedHol.add(new Date(30, JANUARY, year));
        expectedHol.add(new Date(6, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(28, MAY, year));
        expectedHol.add(new Date(29, MAY, year));
        expectedHol.add(new Date(1, OCTOBER, year));
        expectedHol.add(new Date(2, OCTOBER, year));
        expectedHol.add(new Date(5, OCTOBER, year));
        expectedHol.add(new Date(6, OCTOBER, year));
        expectedHol.add(new Date(7, OCTOBER, year));
        expectedHol.add(new Date(8, OCTOBER, year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2010() {
        final int year = 2010;
        QL.info("Testing China holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2011() {
        final int year = 2011;
        QL.info("Testing China holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testChinaSSEYear2012() {
        final int year = 2012;
        QL.info("Testing China holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
}
