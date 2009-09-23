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

import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Denmark;
import org.junit.Test;

/**
 * @author Jia Jia
 *
 *
 */

public class DenmarkCalendarTest {

	private final Calendar exchange;

	public DenmarkCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		exchange = Denmark.getCalendar(Denmark.Market.CSE);
	}

	@Test
	public void testCSEYear2004() {
		final int year = 2004;
    	QL.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(new Date(8, APRIL, year));
		// good friday
		expectedHol.add(new Date(9, APRIL, year));
		// easter monday
		expectedHol.add(new Date(12, APRIL, year));

		// great prayer day
		expectedHol.add(new Date(7, MAY, year));
		// ascension
		expectedHol.add(new Date(20, MAY, year));
		// whit monday
		expectedHol.add(new Date(31, MAY, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2005() {
		final int year = 2005;
    	QL.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(24, MARCH, year));
		// good friday
		expectedHol.add(new Date(25, MARCH, year));
		// easter monday
		expectedHol.add(new Date(28, MARCH, year));

		// great prayer day
		expectedHol.add(new Date(22, APRIL, year));
		// ascension
		expectedHol.add(new Date(5, MAY, year));
		// whit monday
		expectedHol.add(new Date(16, MAY, year));
		// boxing day
		expectedHol.add(new Date(26, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2006() {
		final int year = 2006;
    	QL.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(13, APRIL, year));
		// maunday thursday
		expectedHol.add(new Date(14, APRIL, year));
		// good friday
		expectedHol.add(new Date(17, APRIL, year));
		// easter monday
		expectedHol.add(new Date(12, MAY, year));

		// great prayer day
		expectedHol.add(new Date(25, MAY, year));
		// ascension
		expectedHol.add(new Date(5, JUNE, year));
		// christmas
		expectedHol.add(new Date(25, DECEMBER, year));
		// boxing day
		expectedHol.add(new Date(26, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2007() {
		final int year = 2007;
    	QL.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(new Date(5, APRIL, year));
		// good friday
		expectedHol.add(new Date(6, APRIL, year));
		// easter monday
		expectedHol.add(new Date(9, APRIL, year));

		// great prayer day
		expectedHol.add(new Date(4, MAY, year));
		// ascension
		expectedHol.add(new Date(17, MAY, year));
		// whit monday
		expectedHol.add(new Date(28, MAY, year));
		// constitution day
		expectedHol.add(new Date(5, JUNE, year));
		// christmas eve
		expectedHol.add(new Date(24, DECEMBER, year));
		// christmas
		expectedHol.add(new Date(25, DECEMBER, year));
		// boxing day
		expectedHol.add(new Date(26, DECEMBER, year));
		// new year's eve
		expectedHol.add(new Date(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	// 2008 - current year
	@Test
	public void testCSEYear2008() {
		final int year = 2008;
    	QL.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(new Date(20, MARCH, year));
		// good friday
		expectedHol.add(new Date(21, MARCH, year));
		// easter monday
		expectedHol.add(new Date(24, MARCH, year));

		// great prayer day
		expectedHol.add(new Date(18, APRIL, year));
		// ascension
		expectedHol.add(new Date(1, MAY, year));
		// whit monday
		expectedHol.add(new Date(12, MAY, year));
		// constitution day
		expectedHol.add(new Date(5, JUNE, year));
		// christmas eve
		expectedHol.add(new Date(24, DECEMBER, year));
		// christmas
		expectedHol.add(new Date(25, DECEMBER, year));
		// boxing day
		expectedHol.add(new Date(26, DECEMBER, year));
		// boxing day
		expectedHol.add(new Date(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2009() {
		final int year = 2009;
    	QL.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(new Date(9, APRIL, year));
		// good friday
		expectedHol.add(new Date(10, APRIL, year));
		// easter monday
		expectedHol.add(new Date(13, APRIL, year));

		// great prayer day
		expectedHol.add(new Date(8, MAY, year));
		// ascension
		expectedHol.add(new Date(21, MAY, year));
		// ascension
		expectedHol.add(new Date(22, MAY, year));
		// whit monday
		expectedHol.add(new Date(1, JUNE, year));
		// constitution day
		expectedHol.add(new Date(5, JUNE, year));
		// christmas eve
		expectedHol.add(new Date(24, DECEMBER, year));
		// christmas
		expectedHol.add(new Date(25, DECEMBER, year));
		expectedHol.add(new Date(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2010() {
		final int year = 2010;
    	QL.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1, JANUARY, year));
		// maunday thursday
		expectedHol.add(new Date(1, APRIL, year));
		// good friday
		expectedHol.add(new Date(2, APRIL, year));
		// easter monday
		expectedHol.add(new Date(5, APRIL, year));

		// great prayer day
		expectedHol.add(new Date(30, APRIL, year));
		// ascension
		expectedHol.add(new Date(13, MAY, year));
		// whit monday
		expectedHol.add(new Date(24, MAY, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2012() {
		final int year = 2012;
    	QL.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(5, APRIL, year));
		// maunday thursday
		expectedHol.add(new Date(6, APRIL, year));
		// good friday
		expectedHol.add(new Date(9, APRIL, year));
		// easter monday
		expectedHol.add(new Date(4, MAY, year));

		// great prayer day
		expectedHol.add(new Date(17, MAY, year));
		// ascension
		expectedHol.add(new Date(28, MAY, year));
		// whit monday
		expectedHol.add(new Date(5, JUNE, year));
		// christmas
		expectedHol.add(new Date(25, DECEMBER, year));
		// boxing day
		expectedHol.add(new Date(26, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

	@Test
	public void testCSEYear2011() {
		final int year = 2011;
    	QL.info("Testing " + Denmark.Market.CSE + " holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(21, APRIL, year));
		// maunday thursday
		expectedHol.add(new Date(22, APRIL, year));
		// good friday
		expectedHol.add(new Date(25, APRIL, year));
		// easter monday
		expectedHol.add(new Date(20, MAY, year));

		// great prayer day
		expectedHol.add(new Date(2, JUNE, year));
		// ascension
		expectedHol.add(new Date(13, JUNE, year));
		// boxing day
		expectedHol.add(new Date(26, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, exchange, year);
	}

}
