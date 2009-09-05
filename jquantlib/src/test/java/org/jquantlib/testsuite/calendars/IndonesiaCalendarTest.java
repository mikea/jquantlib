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
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Indonesia;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jia Jia
 */

public class IndonesiaCalendarTest extends BaseCalendarTest{

    private Calendar c = null;

    public IndonesiaCalendarTest() {
        QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
    }

    @Before
    public void setUp() {
        c = Indonesia.getCalendar(Indonesia.Market.BEJ);
    }

    // Holiday figures taken from http://www.idx.co.id/MainMenu/Trading/TradingHoliday/tabid/85/language/en-US/Default.aspx
    @Test
    public void testIndonesiaYear2009() {
        final int year = 2009;
        QL.info("Testing Indonesia's holiday list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();
        // New Year
        expectedHol.add(getDate(1, JANUARY, year));
        expectedHol.add(getDate(2, JANUARY, year));
        expectedHol.add(getDate(26, JANUARY, year));
        expectedHol.add(getDate(9, MARCH, year));
        expectedHol.add(getDate(26, MARCH, year));
        expectedHol.add(getDate(10, APRIL, year));
        // Ascension Thursday
        expectedHol.add(getDate(21, MAY, year));

        // Waisak
        expectedHol.add(getDate(20, JULY, year));

        // Independence Day
        expectedHol.add(getDate(17, AUGUST, year));
        expectedHol.add(getDate(18, SEPTEMBER, year));
        expectedHol.add(getDate(21, SEPTEMBER, year));
        expectedHol.add(getDate(22, SEPTEMBER, year));
        expectedHol.add(getDate(23, SEPTEMBER, year));

        // Ied Adha
        expectedHol.add(getDate(27, NOVEMBER, year));

        // Christmas
        expectedHol.add(getDate(18, DECEMBER, year));
        expectedHol.add(getDate(24, DECEMBER, year));
        expectedHol.add(getDate(25, DECEMBER, year));
        expectedHol.add(getDate(31, DECEMBER, year));
        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    @Test
    public void testIndonesiaYear2008() {
        final int year = 2008;
        QL.info("Testing Indonesia's holiday list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();
        // New Year
        expectedHol.add(getDate(1, JANUARY, year));
        expectedHol.add(getDate(10, JANUARY, year));
        expectedHol.add(getDate(11, JANUARY, year));
        expectedHol.add(getDate(7, FEBRUARY, year));
        expectedHol.add(getDate(8, FEBRUARY, year));
        expectedHol.add(getDate(7, MARCH, year));
        expectedHol.add(getDate(20, MARCH, year));
        expectedHol.add(getDate(21, MARCH, year));
        // Ascension Thursday
        expectedHol.add(getDate(1, MAY, year));

        // National leaves
        expectedHol.add(getDate(20, MAY, year));

        // Waisak
        expectedHol.add(getDate(30, JULY, year));

        // Independence Day
        expectedHol.add(getDate(18, AUGUST, year));
        expectedHol.add(getDate(30, SEPTEMBER, year));

        // National leaves
        expectedHol.add(getDate(1, OCTOBER, year));
        expectedHol.add(getDate(2, OCTOBER, year));
        expectedHol.add(getDate(3, OCTOBER, year));


        // Ied Adha
        expectedHol.add(getDate(8, DECEMBER, year));

        // Christmas
        expectedHol.add(getDate(25, DECEMBER, year));

        expectedHol.add(getDate(29, DECEMBER, year));
        expectedHol.add(getDate(31, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    // 2007
    @Test
    public void testIndonesiaYear2007() {
        final int year = 2007;
        QL.info("Testing Indonesia's holiday list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();
        // New Year
        expectedHol.add(getDate(1, JANUARY, year));

        // Nyepi
        expectedHol.add(getDate(19, MARCH, year));

        // Good Friday
        expectedHol.add(getDate(6, APRIL, year));

        // Ascension Thursday
        expectedHol.add(getDate(17, MAY, year));

        // National leaves
        expectedHol.add(getDate(18, MAY, year));

        // Waisak
        expectedHol.add(getDate(1, JUNE, year));

        // Independence Day
        expectedHol.add(getDate(17, AUGUST, year));

        // National leaves
        expectedHol.add(getDate(12, OCTOBER, year));
        expectedHol.add(getDate(15, OCTOBER, year));
        expectedHol.add(getDate(16, OCTOBER, year));
        expectedHol.add(getDate(24, OCTOBER, year));

        // Ied Adha
        expectedHol.add(getDate(20, DECEMBER, year));

        // Christmas
        expectedHol.add(getDate(25, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    // 2006
    @Test
    public void testIndonesiaYear2006() {
        final int year = 2006;
        QL.info("Testing Indonesia's holiday list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();
        // New Year -- weekend in yr 2006
        // expectedHol.add(getDate(1,JANUARY,year));

        // Idul Adha
        expectedHol.add(getDate(10, JANUARY, year));

        // Moslem's New Year Day
        expectedHol.add(getDate(31, JANUARY, year));

        // Nyepi
        expectedHol.add(getDate(30, MARCH, year));

        // Birthday of Prophet Muhammad SAW
        expectedHol.add(getDate(10, APRIL, year));

        // Good Friday
        expectedHol.add(getDate(14, APRIL, year));

        // Ascension Thursday
        expectedHol.add(getDate(25, MAY, year));

        // Independence Day
        expectedHol.add(getDate(17, AUGUST, year));

        // Ascension of Prophet Muhammad SAW
        expectedHol.add(getDate(21, AUGUST, year));

        // National leaves
        expectedHol.add(getDate(23, OCTOBER, year));

        // Idul Fitri
        expectedHol.add(getDate(24, OCTOBER, year));
        expectedHol.add(getDate(25, OCTOBER, year));

        // National Leaves
        expectedHol.add(getDate(26, OCTOBER, year));
        expectedHol.add(getDate(27, OCTOBER, year));

        // Christmas
        expectedHol.add(getDate(25, DECEMBER, year));
        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    // 2005
    @Test
    public void testIndonesiaYear2005() {
        final int year = 2005;
        QL.info("Testing Indonesia's holiday list for the year " + year + "...");
        final List<Date> expectedHol = new Vector<Date>();
        // New Year -- weekend in yr 2005
        // expectedHol.add(getDate(1,JANUARY,year));

        // Idul Adha
        expectedHol.add(getDate(21, JANUARY, year));

        // Imlek
        expectedHol.add(getDate(9, FEBRUARY, year));

        // Moslem's New Year Day
        expectedHol.add(getDate(10, FEBRUARY, year));

        // Nyepi
        expectedHol.add(getDate(11, MARCH, year));

        // Good Friday
        expectedHol.add(getDate(25, MARCH, year));

        // Birthday of Prophet Muhammad SAW
        expectedHol.add(getDate(22, APRIL, year));

        // Ascension Thursday
        expectedHol.add(getDate(5, MAY, year));

        // Waisak
        expectedHol.add(getDate(24, MAY, year));

        // Independence Day
        expectedHol.add(getDate(17, AUGUST, year));

        // Ascension of Prophet Muhammad SAW
        expectedHol.add(getDate(2, SEPTEMBER, year));

        // National Leaves
        expectedHol.add(getDate(2, NOVEMBER, year));

        // Idul Fitri
        expectedHol.add(getDate(3, NOVEMBER, year));
        expectedHol.add(getDate(4, NOVEMBER, year));

        // National Leaves
        expectedHol.add(getDate(7, NOVEMBER, year));
        expectedHol.add(getDate(8, NOVEMBER, year));

        // Christmas -- weekend in yr 2005
        // expectedHol.add(getDate(25,DECEMBER,year));

        // National Leaves
        expectedHol.add(getDate(26, DECEMBER, year));
        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    @After
    public void destroy() {
        c = null;
    }
}
