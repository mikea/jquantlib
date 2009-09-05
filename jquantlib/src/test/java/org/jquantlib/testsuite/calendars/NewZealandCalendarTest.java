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
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.NewZealand;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Richard Gomes
 */
public class NewZealandCalendarTest extends BaseCalendarTest{

    //TODO: private final Calendar settlement;
    private final Calendar exchange;

	public NewZealandCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	    //TODO: this.settlement = Norway.getCalendar(Poland.Market.Settlement);
	    this.exchange   = NewZealand.getCalendar(NewZealand.Market.NZX);
	}


	// 2012 -- simply taken from rules
	// Comparing with http://www.timeanddate.com/calendar/index.html?year=2012&country=30
    //
	//     1 Jan	New Year's Day
	//     2 Jan	New Year's Day Observed
	//     3 Jan	Day after New Years Day
	//    23 Jan	Mon Anniversary Day
	//     6 Feb	Waitangi Day
	//
	//     6 Apr	Good Friday
	//     9 Apr	Easter Monday
	//    25 Apr	Anzac Day
	//     4 Jun	Queen's Birthday
	//
	//    22 Oct	Labour Day
	//    25 Dec	Christmas Day
	//    26 Dec	Boxing Day

	@Test
	public void testNewZealandYear2012() {
        final int year = 2012;
    	QL.info("Testing " + NewZealand.Market.NZX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		// Sun: expectedHol.add(df.getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(getDate( 2,JANUARY,year));   // New Years Day observed
		expectedHol.add(getDate( 3,JANUARY,year));   // Day after New Years Day
		expectedHol.add(getDate(23,JANUARY,year));   // Anniversary Day
		expectedHol.add(getDate( 6,FEBRUARY,year));  // Waitangi Day
		expectedHol.add(getDate( 6,APRIL,year));     // Good Friday
		expectedHol.add(getDate( 9,APRIL,year));     // Earter Monday
		expectedHol.add(getDate(25,APRIL,year));     // Anzac Day
        expectedHol.add(getDate( 4,JUNE,year));      // Queen's Birthday
        expectedHol.add(getDate(22,OCTOBER,year));   // Labour Day
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


	// 2011 -- simply taken from rules
	// Comparing with http://www.timeanddate.com/calendar/index.html?year=2011&country=30
    //
	//	 1 Jan	New Year's Day
	//	 3 Jan	New Year's Day Observed
	//	 4 Jan	Day after New Years Day
	//  24 Jan	Mon Anniversary Day
	//	 6 Feb	Waitangi Day
	//
	//	22 Apr	Good Friday
	//	25 Apr	Anzac Day
	//	25 Apr	Easter Monday
	//	 6 Jun	Queen's Birthday
	//
	//	24 Oct	Labour Day
	//	25 Dec	Christmas Day
	//	26 Dec	Boxing Day
	//	27 Dec	Christmas Day Observed

	@Test
	public void testNewZealandYear2011() {
        final int year = 2011;
    	QL.info("Testing " + NewZealand.Market.NZX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		// Sat: expectedHol.add(getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(getDate( 3,JANUARY,year));   // New Years Day observed
		expectedHol.add(getDate( 4,JANUARY,year));   // Day after New Years Day
		expectedHol.add(getDate(24,JANUARY,year));   // Anniversary Day
		// Sun: expectedHol.add(getDate( 6,FEBRUARY,year));  // Waitangi Day
		expectedHol.add(getDate(22,APRIL,year));     // Good Friday
		expectedHol.add(getDate(25,APRIL,year));     // Earter Monday
		expectedHol.add(getDate(25,APRIL,year));     // Anzac Day
        expectedHol.add(getDate( 6,JUNE,year));      // Queen's Birthday
        expectedHol.add(getDate(24,OCTOBER,year));   // Labour Day
        // Sun: expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // Boxing Day
        expectedHol.add(getDate(27,DECEMBER,year));  // Christmas Day observed

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


	// 2010 -- simply taken from rules
	// Comparing with http://www.timeanddate.com/calendar/index.html?year=2010&country=30
    //
	//	 1 Jan	New Year's Day
	//	 4 Jan	Day after New Years Day
	//  25 Jan	Mon Anniversary Day
	//	 6 Feb	Waitangi Day
	//	 2 Apr	Good Friday
	//
	//	 5 Apr	Easter Monday
	//	25 Apr	Anzac Day
	//	 7 Jun	Queen's Birthday
	//	25 Oct	Labour Day
	//
	//	25 Dec	Christmas Day
	//	26 Dec	Boxing Day
	//	27 Dec	Christmas Day Observed
	//	28 Dec	Boxing Day Observed

	@Test
	public void testNewZealandYear2010() {
        final int year = 2010;
    	QL.info("Testing " + NewZealand.Market.NZX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(getDate( 4,JANUARY,year));   // Day after New Years Day
		expectedHol.add(getDate(25,JANUARY,year));   // Anniversary Day
		// Sat: expectedHol.add(getDate( 6,FEBRUARY,year));  // Waitangi Day
		expectedHol.add(getDate( 2,APRIL,year));     // Good Friday
		expectedHol.add(getDate( 5,APRIL,year));     // Earter Monday
		// Sun: expectedHol.add(getDate(25,APRIL,year));     // Anzac Day
        expectedHol.add(getDate( 7,JUNE,year));      // Queen's Birthday
        expectedHol.add(getDate(25,OCTOBER,year));   // Labour Day
        // Sat: expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        // Sun: expectedHol.add(getDate(26,DECEMBER,year));  // Boxing Day
        expectedHol.add(getDate(27,DECEMBER,year));  // Christmas Day observed
        expectedHol.add(getDate(28,DECEMBER,year));  // Boxing Day observed

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2009 -- taken from Exchange website http://www.nzx.com/markets/key_dates/trading_hours

	//     1 Jan	Thu	New Year's Day
	//     2 Jan	Fri	New Year's Day Holiday
	//    19 Jan	Mon Anniversary Day
	//     6 Feb	Fri	Waitangi Day
	//    10 Apr	Fri	Good Friday
	//    13 Apr	Mon	Easter Monday
	//    25 Apr	Sat	ANZAC Day
	//     1 Jun	Mon	Queens Birthday
	//    26 Oct	Mon	Labour Day
	//    25 Dec	Fri	Christmas Day
	//    28 Dec	Mon	Boxing Day


	@Test
	public void testNewZealandYear2009() {
        final int year = 2009;
    	QL.info("Testing " + NewZealand.Market.NZX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(getDate( 2,JANUARY,year));   // Day after New Years Day
		expectedHol.add(getDate(19,JANUARY,year));   // Anniversary Day
		expectedHol.add(getDate( 6,FEBRUARY,year));  // Waitangi Day
		expectedHol.add(getDate(10,APRIL,year));     // Good Friday
		expectedHol.add(getDate(13,APRIL,year));     // Earter Monday
		// Sat: expectedHol.add(getDate(25,APRIL,year));     // Anzac Day
        expectedHol.add(getDate( 1,JUNE,year));      // Queen's Birthday
        expectedHol.add(getDate(26,OCTOBER,year));   // Labour Day
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(28,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2008 -- taken from Exchange website http://www.nzx.com/markets/key_dates/trading_hours

	//	 1 Jan	Tue	New Year's Day
	//	 2 Jan	Wed	New Year's Day Holiday
	//  21 Jan	Mon Anniversary Day
	//	 6 Feb	Wed	Waitangi Day
	//	21 Mar	Fri	Good Friday
	//	24 Mar	Mon	Easter Monday
	//	25 Apr	Fri	ANZAC Day
	//	 2 Jun	Mon	Queens Birthday
	//	27 Oct	Mon	Labour Day
	//	25 Dec	Thu	Christmas Day
	//	26 Dec	Fri	Boxing Day

	@Test
	public void testNewZealandYear2008() {
      	final int year = 2008;
    	QL.info("Testing " + NewZealand.Market.NZX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(getDate( 2,JANUARY,year));   // Day after New Years Day
		expectedHol.add(getDate(21,JANUARY,year));   // Anniversary Day
		expectedHol.add(getDate( 6,FEBRUARY,year));  // Waitangi Day
		expectedHol.add(getDate(21,MARCH,year));     // Good Friday
		expectedHol.add(getDate(24,MARCH,year));     // Earter Monday
		expectedHol.add(getDate(25,APRIL,year));     // Anzac Day
        expectedHol.add(getDate( 2,JUNE,year));      // Queen's Birthday
        expectedHol.add(getDate(27,OCTOBER,year));   // Labour Day
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


	// 2007 -- simply taken from rules
	// Comparing with http://www.timeanddate.com/calendar/index.html?year=2007&country=30
    //
	//	 1 Jan	New Year's Day
	//	 2 Jan	Day after New Years Day
	//  22 Jan	Mon Anniversary Day
	//	 6 Feb	Waitangi Day
	//	 6 Apr	Good Friday
	//
	//	 9 Apr	Easter Monday
	//	25 Apr	Anzac Day
	//	 4 Jun	Queen's Birthday
	//	22 Oct	Labour Day
	//
	//	25 Dec	Christmas Day
	//	26 Dec	Boxing Day

	@Test
	public void testNewZealandYear2007() {
        final int year = 2007;
    	QL.info("Testing " + NewZealand.Market.NZX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(getDate( 2,JANUARY,year));   // Day after New Years Day
		expectedHol.add(getDate(22,JANUARY,year));   // Anniversary Day
		expectedHol.add(getDate( 6,FEBRUARY,year));  // Waitangi Day
		expectedHol.add(getDate( 6,APRIL,year));     // Good Friday
		expectedHol.add(getDate( 9,APRIL,year));     // Earter Monday
		expectedHol.add(getDate(25,APRIL,year));     // Anzac Day
        expectedHol.add(getDate( 4,JUNE,year));      // Queen's Birthday
        expectedHol.add(getDate(22,OCTOBER,year));   // Labour Day
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2006 -- simply taken from rules
	// Comparing with http://www.timeanddate.com/calendar/index.html?year=2006&country=30
    //
	//	 1 Jan	New Year's Day
	//	 2 Jan	New Year's Day Observed
	//	 3 Jan	Day after New Years Day
	//  23 Jan	Mon Anniversary Day
	//	 6 Feb	Waitangi Day
	//
	//	14 Apr	Good Friday
	//	17 Apr	Easter Monday
	//	25 Apr	Anzac Day
	//	 5 Jun	Queen's Birthday
	//
	//	23 Oct	Labour Day
	//	25 Dec	Christmas Day
	//	26 Dec	Boxing Day

	@Test
	public void testNewZealandYear2006() {
        final int year = 2006;
    	QL.info("Testing " + NewZealand.Market.NZX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		// expectedHol.add(getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(getDate( 2,JANUARY,year));   // New Year's Day observed
		expectedHol.add(getDate( 3,JANUARY,year));   // Day after New Years Day
		expectedHol.add(getDate(23,JANUARY,year));   // Anniversary Day
		expectedHol.add(getDate( 6,FEBRUARY,year));  // Waitangi Day
		expectedHol.add(getDate(14,APRIL,year));     // Good Friday
		expectedHol.add(getDate(17,APRIL,year));     // Earter Monday
		expectedHol.add(getDate(25,APRIL,year));     // Anzac Day
        expectedHol.add(getDate( 5,JUNE,year));      // Queen's Birthday
        expectedHol.add(getDate(23,OCTOBER,year));   // Labour Day
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2005 -- simply taken from rules
	// Comparing with http://www.timeanddate.com/calendar/index.html?year=2005&country=30
    //
	//	 1 Jan	New Year's Day
	//	 3 Jan	New Year's Day Observed
	//	 4 Jan	Day after New Years Day
	//  24 Jan	Mon Anniversary Day
	//	 6 Feb	Waitangi Day
	//
	//	25 Mar	Good Friday
	//	28 Mar	Easter Monday
	//	25 Apr	Anzac Day
	//	 6 Jun	Queen's Birthday
	//
	//	24 Oct	Labour Day
	//	25 Dec	Christmas Day
	//	26 Dec	Boxing Day
	//	27 Dec	Christmas Day Observed

    @Test
    public void testNewZealandYear2005() {
        final int year = 2005;
    	QL.info("Testing " + NewZealand.Market.NZX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		// expectedHol.add(getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(getDate( 3,JANUARY,year));   // Day after New Years Day
		expectedHol.add(getDate( 4,JANUARY,year));   // New Years Day observed
		expectedHol.add(getDate(24,JANUARY,year));   // Anniversary Day
		// Sun: expectedHol.add(getDate( 6,FEBRUARY,year));  // Waitangi Day
		expectedHol.add(getDate(25,MARCH,year));     // Good Friday
		expectedHol.add(getDate(28,MARCH,year));     // Earter Monday
		expectedHol.add(getDate(25,APRIL,year));     // Anzac Day
        expectedHol.add(getDate( 6,JUNE,year));      // Queen's Birthday
        expectedHol.add(getDate(24,OCTOBER,year));   // Labour Day
        // Sun: expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // Boxing Day
        expectedHol.add(getDate(27,DECEMBER,year));  // Christmas Day observed

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2004 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/index.html?year=2004&country=30
    //
	//    1 Jan	New Year's Day
	//    2 Jan	Day after New Years Day
	//   19 Jan Mon Anniversary Day
	//    6 Feb	Waitangi Day
	//    9 Apr	Good Friday
	//
	//    12 Apr	Easter Monday
	//    25 Apr	Anzac Day
	//     7 Jun	Queen's Birthday
	//    25 Oct	Labour Day
	//
	//    25 Dec	Christmas Day
	//    26 Dec	Boxing Day
	//    27 Dec	Christmas Day Observed
	//    28 Dec	Boxing Day Observed

    @Test
    public void testNewZealandYear2004() {
        final int year = 2004;
    	QL.info("Testing " + NewZealand.Market.NZX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate( 1,JANUARY,year));   // New Year's Day
		expectedHol.add(getDate( 2,JANUARY,year));   // Day after New Years Day
		expectedHol.add(getDate(19,JANUARY,year));   // Anniversary Day
		expectedHol.add(getDate( 6,FEBRUARY,year));  // Waitangi Day
		expectedHol.add(getDate( 9,APRIL,year));     // Good Friday
		expectedHol.add(getDate(12,APRIL,year));     // Anzac Day
        expectedHol.add(getDate( 7,JUNE,year));      // Queen's Birthday
        expectedHol.add(getDate(25,OCTOBER,year));   // Labour Day
        // Sat: expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        // Sun: expectedHol.add(getDate(26,DECEMBER,year));  // Boxing Day
        expectedHol.add(getDate(27,DECEMBER,year));  // Christmas Day observed
        expectedHol.add(getDate(28,DECEMBER,year));  // Boxing Day observed

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

}
