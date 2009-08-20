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
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Norway;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Richard Gomes
 */
public class NorwayCalendarTest {

    //TODO: private final Calendar settlement;
    private final Calendar exchange;

	public NorwayCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	    //TODO: this.settlement = Norway.getCalendar(Poland.Market.Settlement);
	    this.exchange   = Norway.getCalendar(Norway.Market.OsloBors);
	}


	// 2012 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/custom.html?year=2012&country=18&lang=en&hol=9&moon=on
    //
	//    Jan  1	New Year's Day
	//    Apr  1	Palm Sunday
	//    Apr  5	Maundy Thursday
	//    Apr  6	Good Friday
	//    Apr  8	Easter Day
	//
	//    Apr  9	Easter Monday
	//    May  1	Official holiday
	//    May 17	Ascension Day
	//    May 17	Constitution Day
	//    May 27	Whit Sunday
	//
	//    May 28	Whit Monday
	//    Dec 25	Christmas Day
	//    Dec 26	Boxing Day

	@Test
	public void testNorwayYear2012() {
       	final int year = 2012;
    	QL.info("Testing " + Norway.Market.OsloBors + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		// Sun: expectedHol.add(df.getDate( 1,JANUARY,year));	// New Year's Day
		// Sun: expectedHol.add(df.getDate( 1,APRIL,year));		// Palm Sunday
		expectedHol.add(df.getDate( 5,APRIL,year));		// Maundy Thursday
		expectedHol.add(df.getDate( 6,APRIL,year));		// Good Friday
		// Sun: expectedHol.add(df.getDate( 8,APRIL,year));     // Easter Day
		expectedHol.add(df.getDate( 9,APRIL,year));     // Easter Monday
		expectedHol.add(df.getDate( 1,MAY,year));		// Official holiday
        expectedHol.add(df.getDate(17,MAY,year));		// Ascension Day
        expectedHol.add(df.getDate(17,MAY,year));		// Constitution Day
        // Sun: expectedHol.add(df.getDate(27,MAY,year));   	// Whit Sunday
        expectedHol.add(df.getDate(28,MAY,year));   	// Whit Monday
        expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(df.getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


	// 2011 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/custom.html?year=2011&country=18&lang=en&hol=9&moon=on
    //
	//	Jan  1	New Year's Day
	//	Apr 17	Palm Sunday
	//	Apr 21	Maundy Thursday
	//	Apr 22	Good Friday
	//	Apr 24	Easter Day
	//
	//	Apr 25	Easter Monday
	//	May  1	Official holiday
	//	May 17	Constitution Day
	//	Jun  2	Ascension Day
	//	Jun 12	Whit Sunday
	//
	//	Jun 13	Whit Monday
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day

	@Test
	public void testNorwayYear2011() {
        final int year = 2011;
    	QL.info("Testing " + Norway.Market.OsloBors + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();


		// Sat: expectedHol.add(df.getDate( 1,JANUARY,year));	// New Year's Day
		// Sun: expectedHol.add(df.getDate(17,APRIL,year));		// Palm Sunday
		expectedHol.add(df.getDate(21,APRIL,year));		// Maundy Thursday
		expectedHol.add(df.getDate(22,APRIL,year));		// Good Friday
		// Sun: expectedHol.add(df.getDate(24,APRIL,year));     // Easter Day
		expectedHol.add(df.getDate(25,APRIL,year));     // Easter Monday
		// Sun: expectedHol.add(df.getDate( 1,MAY,year));		// Official holiday
        expectedHol.add(df.getDate(17,MAY,year));		// Constitution Day
        expectedHol.add(df.getDate( 2,JUNE,year));		// Ascension Day
        // Sun: expectedHol.add(df.getDate(12,JUNE,year));   	// Whit Sunday
        expectedHol.add(df.getDate(13,JUNE,year));   	// Whit Monday
        // Sun: expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(df.getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


	// 2010 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/custom.html?year=2010&country=18&lang=en&hol=9&moon=on
    //
	//	Jan  1	New Year's Day
	//	Mar 28	Palm Sunday
	//	Apr  1	Maundy Thursday
	//	Apr  2	Good Friday
	//	Apr  4	Easter Day
	//
	//	Apr  5	Easter Monday
	//	May  1	Official holiday
	//	May 13	Ascension Day
	//	May 17	Constitution Day
	//	May 23	Whit Sunday
	//
	//	May 24	Whit Monday
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day

	@Test
	public void testNorwayYear2010() {
        final int year = 2010;
    	QL.info("Testing " + Norway.Market.OsloBors + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();


		expectedHol.add(df.getDate( 1,JANUARY,year));	// New Year's Day
		// Sun: expectedHol.add(df.getDate(28,MARCH,year));		// Palm Sunday
		expectedHol.add(df.getDate( 1,APRIL,year));		// Maundy Thursday
		expectedHol.add(df.getDate( 2,APRIL,year));		// Good Friday
		// Sun: expectedHol.add(df.getDate( 4,APRIL,year));     // Easter Day
		expectedHol.add(df.getDate( 5,APRIL,year));     // Easter Monday
		// Sat: expectedHol.add(df.getDate( 1,MAY,year));		// Official holiday
        expectedHol.add(df.getDate(13,MAY,year));		// Ascension Day
        expectedHol.add(df.getDate(17,MAY,year));		// Constitution Day
        // Sun: expectedHol.add(df.getDate(23,MAY,year));   	// Whit Sunday
        expectedHol.add(df.getDate(24,MAY,year));   	// Whit Monday
        // Sat: expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        // Sun: expectedHol.add(df.getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2009 -- taken from Exchange website http://www.oslobors.no/ob/aapningstider?languageID=1
	//
	//	Jan 1	New Year's Day
	//	Apr 5	Palm Sunday
	//	Apr 9	Maundy Thursday
	//	Apr 10	Good Friday
	//	Apr 12	Easter Day
	//
	//	Apr 13	Easter Monday
	//	May 1	Official holiday
	//	May 17	Constitution Day
	//	May 21	Ascension Day
	//	May 31	Whit Sunday
	//
	//	Jun 1	Whit Monday
	//	Dec 24	Christmas Eve  :: see http://bugs.jquantlib.org/view.php?id=73
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	//	Dec 31	New Year Eve :: see http://bugs.jquantlib.org/view.php?id=73


	@Test
	public void testNorwayYear2009() {
      	final int year = 2009;
    	QL.info("Testing " + Norway.Market.OsloBors + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();


		expectedHol.add(df.getDate( 1,JANUARY,year));	// New Year's Day
		// Sun: expectedHol.add(df.getDate( 5,APRIL,year));		// Palm Sunday
		expectedHol.add(df.getDate( 9,APRIL,year));		// Maundy Thursday
		expectedHol.add(df.getDate(10,APRIL,year));		// Good Friday
		// Sun: expectedHol.add(df.getDate(12,APRIL,year));     // Easter Day
		expectedHol.add(df.getDate(13,APRIL,year));     // Easter Monday
		expectedHol.add(df.getDate( 1,MAY,year));		// Official holiday
        // Sun: expectedHol.add(df.getDate(17,MAY,year));		// Constitution Day
        expectedHol.add(df.getDate(21,MAY,year));		// Ascension Day
        // Sun: expectedHol.add(df.getDate(31,MAY,year));   	// Whit Sunday
        expectedHol.add(df.getDate( 1,JUNE,year));   	// Whit Monday
        expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        // Sat: expectedHol.add(df.getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2008 -- taken from Exchange website http://www.oslobors.no/ob/aapningstider?languageID=1
	//
	//	Jan  1	New Year's Day
	//	Mar 16	Palm Sunday
	//	Mar 20	Maundy Thursday
	//	Mar 21	Good Friday
	//	Mar 23	Easter Day
	//
	//	Mar 24	Easter Monday
	//	May  1	Official holiday
	//	May  1	Ascension Day
	//	May 11	Whit Sunday
	//	May 12	Whit Monday
	//
	//	May 17	Constitution Day
	//	Dec 24	Christmas Eve  :: see http://bugs.jquantlib.org/view.php?id=73
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	//	Dec 31	New Year Eve :: see http://bugs.jquantlib.org/view.php?id=73

	@Test
	public void testNorwayYear2008() {
      	final int year = 2008;
    	QL.info("Testing " + Norway.Market.OsloBors + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();


		expectedHol.add(df.getDate( 1,JANUARY,year));	// New Year's Day
		// Sun: expectedHol.add(df.getDate(16,MARCH,year));		// Palm Sunday
		expectedHol.add(df.getDate(20,MARCH,year));		// Maundy Thursday
		expectedHol.add(df.getDate(21,MARCH,year));		// Good Friday
		// Sun: expectedHol.add(df.getDate(23,MARCH,year));     // Easter Day
		expectedHol.add(df.getDate(24,MARCH,year));     // Easter Monday
		expectedHol.add(df.getDate( 1,MAY,year));		// Official holiday
        expectedHol.add(df.getDate( 1,MAY,year));		// Ascension Day
        // Sun: expectedHol.add(df.getDate(11,MAY,year));   	// Whit Sunday
        expectedHol.add(df.getDate(12,MAY,year));   	// Whit Monday
        // Sat: expectedHol.add(df.getDate(17,MAY,year));		// Constitution Day
        expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(df.getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


	// 2007 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/custom.html?year=2007&country=18&lang=en&hol=9&moon=on
    //
	//	Jan  1	New Year's Day
	//	Apr  1	Palm Sunday
	//	Apr  5	Maundy Thursday
	//	Apr  6	Good Friday
	//	Apr  8	Easter Day
	//
	//	Apr  9	Easter Monday
	//	May  1	Official holiday
	//	May 17	Ascension Day
	//	May 17	Constitution Day
	//	May 27	Whit Sunday
	//
	//	May 28	Whit Monday
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day

	@Test
	public void testNorwayYear2007() {
        final int year = 2007;
    	QL.info("Testing " + Norway.Market.OsloBors + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(df.getDate( 1,JANUARY,year));	// New Year's Day
		// Sun: expectedHol.add(df.getDate( 1,APRIL,year));		// Palm Sunday
		expectedHol.add(df.getDate( 5,APRIL,year));		// Maundy Thursday
		expectedHol.add(df.getDate( 6,APRIL,year));		// Good Friday
		// Sun: expectedHol.add(df.getDate( 8,APRIL,year));     // Easter Day
		expectedHol.add(df.getDate( 9,APRIL,year));     // Easter Monday
		expectedHol.add(df.getDate( 1,MAY,year));		// Official holiday
        expectedHol.add(df.getDate(17,MAY,year));		// Constitution Day
        expectedHol.add(df.getDate(17,MAY,year));		// Ascension Day
        // Sun: expectedHol.add(df.getDate(27,MAY,year));   	// Whit Sunday
        expectedHol.add(df.getDate(28,MAY,year));   	// Whit Monday
        expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(df.getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2006 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/custom.html?year=2006&country=18&lang=en&hol=9&moon=on
    //
	//	Jan 1	New Year's Day
	//
	//	Apr 9	Palm Sunday
	//	Apr 13	Maundy Thursday
	//	Apr 14	Good Friday
	//	Apr 16	Easter Day
	//	Apr 17	Easter Monday
	//
	//	May 1	Official holiday
	//	May 17	Constitution Day
	//	May 25	Ascension Day
	//
	//	Jun 4	Whit Sunday
	//	Jun 5	Whit Monday
	//
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day

	@Test
	public void testNorwayYear2006() {
        final int year = 2006;
    	QL.info("Testing " + Norway.Market.OsloBors + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		// Sun: expectedHol.add(df.getDate( 1,JANUARY,year));	// New Year's Day
		// Sun: expectedHol.add(df.getDate( 9,APRIL,year));		// Palm Sunday
		expectedHol.add(df.getDate(13,APRIL,year));		// Maundy Thursday
		expectedHol.add(df.getDate(14,APRIL,year));		// Good Friday
		// Sun: expectedHol.add(df.getDate(16,APRIL,year));     // Easter Day
		expectedHol.add(df.getDate(17,APRIL,year));     // Easter Monday
		expectedHol.add(df.getDate( 1,MAY,year));		// Official holiday
        expectedHol.add(df.getDate(17,MAY,year));		// Constitution Day
        expectedHol.add(df.getDate(25,MAY,year));		// Ascension Day
        // Sun: expectedHol.add(df.getDate( 4,JUNE,year));   	// Whit Sunday
        expectedHol.add(df.getDate( 5,JUNE,year));   	// Whit Monday
        expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(df.getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2005 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/custom.html?year=2005&country=18&lang=en&hol=9&moon=on
    //
	//	Jan  1	New Year's Day
	//	Mar 20	Palm Sunday
	//	Mar 24	Maundy Thursday
	//	Mar 25	Good Friday
	//	Mar 27	Easter Day
	//
	//	Mar 28	Easter Monday
	//	May  1	Official holiday
	//	May  5	Ascension Day
	//	May 15	Whit Sunday
	//	May 16	Whit Monday
	//	May 17	Constitution Day
	//
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day

    @Test
    public void testNorwayYear2005() {
        final int year = 2005;
    	QL.info("Testing " + Norway.Market.OsloBors + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		// Sat: expectedHol.add(df.getDate( 1,JANUARY,year));	// New Year's Day
		// Sun: expectedHol.add(df.getDate(20,APRIL,year));		// Palm Sunday
		expectedHol.add(df.getDate(24,MARCH,year));		// Maundy Thursday
		expectedHol.add(df.getDate(25,MARCH,year));		// Good Friday
		// Sun: expectedHol.add(df.getDate(27,MARCH,year));     // Easter Day
		expectedHol.add(df.getDate(28,MARCH,year));     // Easter Monday
		// Sun: expectedHol.add(df.getDate( 1,MAY,year));		// Official holiday
        expectedHol.add(df.getDate( 5,MAY,year));		// Ascension Day
        // Sun: expectedHol.add(df.getDate(15,MAY,year));   	// Whit Sunday
        expectedHol.add(df.getDate(16,MAY,year));   	// Whit Monday
        expectedHol.add(df.getDate(17,MAY,year));		// Constitution Day
        // Sun: expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(df.getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    // 2004 -- simply taken from rules
    // Comparing with http://www.timeanddate.com/calendar/custom.html?year=2004&country=18&lang=en&hol=9&moon=on
    //
	//    Jan  1	New Year's Day
	//    Apr  4	Palm Sunday
	//    Apr  8	Maundy Thursday
	//    Apr  9	Good Friday
	//    Apr 11	Easter Day
	//
	//    Apr 12	Easter Monday
	//    May  1	Official holiday
	//    May 17	Constitution Day
	//    May 20	Ascension Day
	//    May 30	Whit Sunday
	//
	//    May 31	Whit Monday
	//    Dec 25	Christmas Day
	//    Dec 26	Boxing Day

    @Test
    public void testNorwayYear2004() {
        final int year = 2004;
    	QL.info("Testing " + Norway.Market.OsloBors + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(df.getDate( 1,JANUARY,year));	// New Year's Day
		// Sun: expectedHol.add(df.getDate( 4,APRIL,year));		// Palm Sunday
		expectedHol.add(df.getDate( 8,APRIL,year));		// Maundy Thursday
		expectedHol.add(df.getDate( 9,APRIL,year));		// Good Friday
		// Sun: expectedHol.add(df.getDate(11,APRIL,year));     // Easter Day
		expectedHol.add(df.getDate(12,APRIL,year));     // Easter Monday
		// Sat: expectedHol.add(df.getDate( 1,MAY,year));		// Official holiday
        expectedHol.add(df.getDate(17,MAY,year));		// Constitution Day
        expectedHol.add(df.getDate(20,MAY,year));		// Ascension Day
        // Sun: expectedHol.add(df.getDate(30,MAY,year));   	// Whit Sunday
        expectedHol.add(df.getDate(31,MAY,year));   	// Whit Monday
        // Sat: expectedHol.add(df.getDate(25,DECEMBER,year));  // Christmas Day
        // Sun: expectedHol.add(df.getDate(26,DECEMBER,year));  // Boxing Day

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

}
