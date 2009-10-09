/*
 Copyright (C) 2008 Renjith Nair

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
import static org.jquantlib.time.Month.AUGUST;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.FEBRUARY;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.NOVEMBER;
import static org.jquantlib.time.Month.OCTOBER;
import static org.jquantlib.time.Month.SEPTEMBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.India;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Renjith Nair
 * @author Jia Jia
 *
 */

public class IndiaCalendarTest {

    private Calendar c = null;

    public IndiaCalendarTest() {
        QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
    }

    @Before
    public void setup() {
        c = new India(India.Market.NSE);
    }

    // 2005 - year in the past
    @Test
    public void testIndiaNseHolidaysYear2005() {
        final int year = 2005;
        QL.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(new Date(21, JANUARY, year));
        expectedHol.add(new Date(26, JANUARY, year));
        expectedHol.add(new Date(25, MARCH, year));
        expectedHol.add(new Date(14, APRIL, year));
        expectedHol.add(new Date(15, AUGUST, year));
        expectedHol.add(new Date(7, SEPTEMBER, year));
        expectedHol.add(new Date(12, OCTOBER, year));
        expectedHol.add(new Date(1, NOVEMBER, year));
        expectedHol.add(new Date(3, NOVEMBER, year));
        expectedHol.add(new Date(15, NOVEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    // 2006 - regular year in the past
    @Test
    public void testIndiaNseHolidaysYear2006() {

        final int year = 2006;
        QL.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(new Date(11, JANUARY, year));
        expectedHol.add(new Date(26, JANUARY, year));
        expectedHol.add(new Date(9, FEBRUARY, year));
        expectedHol.add(new Date(15, MARCH, year));
        expectedHol.add(new Date(6, APRIL, year));
        expectedHol.add(new Date(11, APRIL, year));
        expectedHol.add(new Date(14, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(15, AUGUST, year));
        expectedHol.add(new Date(2, OCTOBER, year));
        expectedHol.add(new Date(24, OCTOBER, year));
        expectedHol.add(new Date(25, OCTOBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    // 2007 - regular year in the past
    @Test
    public void testIndiaNseHolidaysYear2007() {
        final int year = 2007;
        QL.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(26, JANUARY, year));
        expectedHol.add(new Date(30, JANUARY, year));
        expectedHol.add(new Date(16, FEBRUARY, year));
        expectedHol.add(new Date(27, MARCH, year));
        expectedHol.add(new Date(6, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(2, MAY, year));
        expectedHol.add(new Date(15, AUGUST, year));
        expectedHol.add(new Date(2, OCTOBER, year));
        expectedHol.add(new Date(9, NOVEMBER, year));
        expectedHol.add(new Date(21, DECEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    // 2008 - Current Year
    @Test
    public void testIndiaNseHolidaysYear2008() {
        final int year = 2008;
        QL.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(new Date(6, MARCH, year));
        expectedHol.add(new Date(20, MARCH, year));
        expectedHol.add(new Date(21, MARCH, year)); // Good Friday
        expectedHol.add(new Date(14, APRIL, year));
        expectedHol.add(new Date(18, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(19, MAY, year));
        expectedHol.add(new Date(15, AUGUST, year));
        expectedHol.add(new Date(3, SEPTEMBER, year));
        expectedHol.add(new Date(2, OCTOBER, year));
        expectedHol.add(new Date(9, OCTOBER, year));
        expectedHol.add(new Date(28, OCTOBER, year));
        expectedHol.add(new Date(30, OCTOBER, year));
        expectedHol.add(new Date(13, NOVEMBER, year));
        expectedHol.add(new Date(9, DECEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));
//        expectedHol.add(new Date(27, NOVEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    // 2009 - Current Year
    @Test
    public void testIndiaNseHolidaysYear2009() {
        final int year = 2009;
        QL.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();
//        expectedHol.add(new Date(8, JANUARY, year));
        expectedHol.add(new Date(26, JANUARY, year));
//        expectedHol.add(new Date(23, FEBRUARY, year));
//        expectedHol.add(new Date(10, MARCH, year));
//        expectedHol.add(new Date(11, MARCH, year)); // Good Friday
//        expectedHol.add(new Date(3, APRIL, year));
//        expectedHol.add(new Date(7, APRIL, year));
        expectedHol.add(new Date(10, APRIL, year));
        expectedHol.add(new Date(14, APRIL, year));
//        expectedHol.add(new Date(1, MAY, year));
//        expectedHol.add(new Date(21, SEPTEMBER, year));
//        expectedHol.add(new Date(28, SEPTEMBER, year));
        expectedHol.add(new Date(2, OCTOBER, year));
//        expectedHol.add(new Date(19, OCTOBER, year));
//        expectedHol.add(new Date(2, NOVEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));
//        expectedHol.add(new Date(28, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }


    @After
    public void destroy() {
        c = null;
    }
}
