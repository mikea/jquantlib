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
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JULY;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.NOVEMBER;
import static org.jquantlib.time.Month.OCTOBER;
import static org.jquantlib.time.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.CzechRepublic;
import org.junit.Test;

/**
 * @author Renjith Nair
 * @author Jia Jia
 *
 */

public class CzechRepublicCalendarTest {

    private final Calendar c;

    public CzechRepublicCalendarTest() {
        QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
        c = CzechRepublic.getCalendar(CzechRepublic.Market.PSE);
    }

    // 2004 - Leap Year & Extra Holidays
    @Test
    public void testCzechRepublicPSEHolidaysYear2004() {
        final int year = 2004;
        QL.info("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(2, JANUARY, year)); // only for year 2004
        expectedHol.add(new Date(12, APRIL, year));
        expectedHol.add(new Date(5, JULY, year));
        expectedHol.add(new Date(6, JULY, year));
        expectedHol.add(new Date(28, SEPTEMBER, year));
        expectedHol.add(new Date(28, OCTOBER, year));
        expectedHol.add(new Date(17, NOVEMBER, year));
        expectedHol.add(new Date(24, DECEMBER, year));
        expectedHol.add(new Date(31, DECEMBER, year)); // only for year 2004

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    // 2005 - year in the past
    @Test
    public void testCzechRepublicPSEHolidaysYear2005() {
        final int year = 2005;
        QL.info("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(28, MARCH, year));
        expectedHol.add(new Date(5, JULY, year));
        expectedHol.add(new Date(6, JULY, year));
        expectedHol.add(new Date(28, SEPTEMBER, year));
        expectedHol.add(new Date(28, OCTOBER, year));
        expectedHol.add(new Date(17, NOVEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testCzechRepublicPSEHolidaysYear2006() {
        final int year = 2006;
        QL.info("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(17, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(8, MAY, year));
        expectedHol.add(new Date(5, JULY, year));
        expectedHol.add(new Date(6, JULY, year));
        expectedHol.add(new Date(28, SEPTEMBER, year));
        expectedHol.add(new Date(17, NOVEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testCzechRepublicPSEHolidaysYear2007() {
        final int year = 2007;
        QL.info("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(9, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(8, MAY, year));
        expectedHol.add(new Date(5, JULY, year));
        expectedHol.add(new Date(6, JULY, year));
        expectedHol.add(new Date(28, SEPTEMBER, year));
        expectedHol.add(new Date(24, DECEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    // 2008 - Current Year
    @Test
    public void testCzechRepublicPSEHolidaysYear2008() {
        final int year = 2008;
        QL.info("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(24, MARCH, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(8, MAY, year));
        expectedHol.add(new Date(28, OCTOBER, year));
        expectedHol.add(new Date(17, NOVEMBER, year));
        expectedHol.add(new Date(24, DECEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    // 2009 - Year in Future
    @Test
    public void testCzechRepublicPSEHolidaysYear2009() {
        final int year = 2009;
        QL.info("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(13, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(8, MAY, year));
        expectedHol.add(new Date(6, JULY, year));
        expectedHol.add(new Date(28, SEPTEMBER, year));
        expectedHol.add(new Date(28, OCTOBER, year));
        expectedHol.add(new Date(17, NOVEMBER, year));
        expectedHol.add(new Date(24, DECEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testCzechRepublicPSEHolidaysYear2010() {
        final int year = 2010;
        QL.info("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(5, APRIL, year));
        expectedHol.add(new Date(5, JULY, year));
        expectedHol.add(new Date(6, JULY, year));
        expectedHol.add(new Date(28, SEPTEMBER, year));
        expectedHol.add(new Date(28, OCTOBER, year));
        expectedHol.add(new Date(17, NOVEMBER, year));
        expectedHol.add(new Date(24, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testCzechRepublicPSEHolidaysYear2011() {
        final int year = 2011;
        QL.info("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(25, APRIL, year));
        expectedHol.add(new Date(5, JULY, year));
        expectedHol.add(new Date(6, JULY, year));
        expectedHol.add(new Date(28, SEPTEMBER, year));
        expectedHol.add(new Date(28, OCTOBER, year));
        expectedHol.add(new Date(17, NOVEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testCzechRepublicPSEHolidaysYear2012() {
        final int year = 2012;
        QL.info("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(9, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(8, MAY, year));
        expectedHol.add(new Date(5, JULY, year));
        expectedHol.add(new Date(6, JULY, year));
        expectedHol.add(new Date(28, SEPTEMBER, year));
        expectedHol.add(new Date(24, DECEMBER, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

}
