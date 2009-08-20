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

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Sweden;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Renjith Nair
 */

public class SwedenCalendarTest {

    //TODO: private final Calendar settlement;
    private final Calendar exchange;

	public SwedenCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	    //TODO: this.settlement = Sweden.getCalendar(Sweden.Market.Settlement);
	    this.exchange   = Sweden.getCalendar(Sweden.Market.SSE);
	}


    // 2004 - year in the past
	@Test
    public void testSwedenSEHolidaysYear2004()
    {
       	final int year = 2004;
    	QL.info("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(6,JANUARY,year));
    	expectedHol.add(df.getDate(9,APRIL,year));
    	expectedHol.add(df.getDate(12,APRIL,year));
    	expectedHol.add(df.getDate(20,MAY,year));
    	expectedHol.add(df.getDate(31,MAY,year));
    	expectedHol.add(df.getDate(18,JUNE,year));
    	expectedHol.add(df.getDate(24,DECEMBER,year));
    	expectedHol.add(df.getDate(31,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

	// 2005 - year in the past
	@Test
    public void testSwedenSEHolidaysYear2005()
    {
       	final int year = 2005;
    	QL.info("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(6,JANUARY,year));
    	expectedHol.add(df.getDate(25,MARCH,year));
    	expectedHol.add(df.getDate(28,MARCH,year));
    	expectedHol.add(df.getDate(5,MAY,year));
    	expectedHol.add(df.getDate(16,MAY,year));
    	expectedHol.add(df.getDate(24,JUNE,year));
    	expectedHol.add(df.getDate(26,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

	// 2006 - year in the past
	@Test
    public void testSwedenSEHolidaysYear2006()
    {
       	final int year = 2006;
    	QL.info("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(6,JANUARY,year));
    	expectedHol.add(df.getDate(14,APRIL,year));
    	expectedHol.add(df.getDate(17,APRIL,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(25,MAY,year));
    	expectedHol.add(df.getDate(5,JUNE,year));
    	expectedHol.add(df.getDate(23,JUNE,year));
    	expectedHol.add(df.getDate(25,DECEMBER,year));
    	expectedHol.add(df.getDate(26,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

	// 2007 - year in the past
	@Test
    public void testSwedenSEHolidaysYear2007()
    {
       	final int year = 2007;
    	QL.info("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(6,APRIL,year));
    	expectedHol.add(df.getDate(9,APRIL,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(17,MAY,year));
    	expectedHol.add(df.getDate(28,MAY,year));
    	expectedHol.add(df.getDate(22,JUNE,year));
    	expectedHol.add(df.getDate(24,DECEMBER,year));
    	expectedHol.add(df.getDate(25,DECEMBER,year));
    	expectedHol.add(df.getDate(26,DECEMBER,year));
    	expectedHol.add(df.getDate(31,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

	// 2008 - Current Year
	@Test
    public void testSwedenSEHolidaysYear2008()
    {
       	final int year = 2008;
    	QL.info("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(21,MARCH,year));
    	expectedHol.add(df.getDate(24,MARCH,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(12,MAY,year));
    	expectedHol.add(df.getDate(20,JUNE,year));
    	expectedHol.add(df.getDate(24,DECEMBER,year));
    	expectedHol.add(df.getDate(25,DECEMBER,year));
    	expectedHol.add(df.getDate(26,DECEMBER,year));
    	expectedHol.add(df.getDate(31,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

	// 2009 - Future Year
	@Test
    public void testSwedenSEHolidaysYear2009()
    {
       	final int year = 2009;
    	QL.info("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(6,JANUARY,year));
    	expectedHol.add(df.getDate(10,APRIL,year));
    	expectedHol.add(df.getDate(13,APRIL,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(21,MAY,year));
    	expectedHol.add(df.getDate(1,JUNE,year));
    	expectedHol.add(df.getDate(19,JUNE,year));
    	expectedHol.add(df.getDate(24,DECEMBER,year));
    	expectedHol.add(df.getDate(25,DECEMBER,year));
    	expectedHol.add(df.getDate(31,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

	// 2010 - Future Year
	@Test
    public void testSwedenSEHolidaysYear2010()
    {
       	final int year = 2010;
    	QL.info("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(6,JANUARY,year));
    	expectedHol.add(df.getDate(2,APRIL,year));
    	expectedHol.add(df.getDate(5,APRIL,year));
    	expectedHol.add(df.getDate(13,MAY,year));
    	expectedHol.add(df.getDate(24,MAY,year));
    	expectedHol.add(df.getDate(18,JUNE,year));
    	expectedHol.add(df.getDate(24,DECEMBER,year));
    	expectedHol.add(df.getDate(31,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

	// 2011 - Future Year
	@Test
    public void testSwedenSEHolidaysYear2011()
    {
       	final int year = 2011;
    	QL.info("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(6,JANUARY,year));
    	expectedHol.add(df.getDate(22,APRIL,year));
    	expectedHol.add(df.getDate(25,APRIL,year));
    	expectedHol.add(df.getDate(2,JUNE,year));
    	expectedHol.add(df.getDate(13,JUNE,year));
    	expectedHol.add(df.getDate(24,JUNE,year));
    	expectedHol.add(df.getDate(26,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

	// 2012 - Future Year
	@Test
    public void testSwedenSEHolidaysYear2012()
    {
       	final int year = 2012;
    	QL.info("Testing " + Sweden.Market.SSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(6,JANUARY,year));
    	expectedHol.add(df.getDate(6,APRIL,year));
    	expectedHol.add(df.getDate(9,APRIL,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(17,MAY,year));
    	expectedHol.add(df.getDate(28,MAY,year));
    	expectedHol.add(df.getDate(22,JUNE,year));
    	expectedHol.add(df.getDate(24,DECEMBER,year));
    	expectedHol.add(df.getDate(25,DECEMBER,year));
    	expectedHol.add(df.getDate(26,DECEMBER,year));
    	expectedHol.add(df.getDate(31,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

}
