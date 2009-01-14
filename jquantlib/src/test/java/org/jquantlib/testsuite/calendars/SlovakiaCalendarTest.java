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
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Slovakia;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Heng Joon Tiang 
 * @author Renjith Nair
 *
 */

public class SlovakiaCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(SlovakiaCalendarTest.class);

	public SlovakiaCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	
	//FIXME: Needs to obtain reliable data for 2004
	@Test
	public void testSlovakiaYear2004() {
      	int year = 2004;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");

        final DateFactory df = DateFactory.getFactory();
      	final Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(df.getDate(1,JANUARY,year)); 
		expectedHol.add(df.getDate(2,JANUARY,year)); 
		//expectedHol.add(df.getDate(3,JANUARY,year)); 
		//expectedHol.add(df.getDate(4,JANUARY,year)); 
		expectedHol.add(df.getDate(5,JANUARY,year)); 
		expectedHol.add(df.getDate(6,JANUARY,year)); 
		expectedHol.add(df.getDate(9,APRIL,year)); 
		expectedHol.add(df.getDate(12,APRIL,year)); 
		//expectedHol.add(df.getDate(1,MAY,year)); 
		//expectedHol.add(df.getDate(8,MAY,year)); 
		expectedHol.add(df.getDate(5,JULY,year)); 
		//expectedHol.add(df.getDate(29,AUGUST,year)); 
    	expectedHol.add(df.getDate(1,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(15,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(1,NOVEMBER,year)); 
		expectedHol.add(df.getDate(17,NOVEMBER,year)); 
		expectedHol.add(df.getDate(24,DECEMBER,year)); 
		//expectedHol.add(df.getDate(25,DECEMBER,year)); 
		//expectedHol.add(df.getDate(26,DECEMBER,year)); 
		expectedHol.add(df.getDate(27,DECEMBER,year)); 
		expectedHol.add(df.getDate(28,DECEMBER,year)); 
		expectedHol.add(df.getDate(29,DECEMBER,year)); 
		expectedHol.add(df.getDate(30,DECEMBER,year)); 
		expectedHol.add(df.getDate(31,DECEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
   

	//	2005 - BSSE Trading Holidays
	//
	//	01 Jan    Sat    New Year's/Founding of Slovak Republic
	//	02 Jan    Sun    New Year's Holiday 2
	//	03 Jan    Mon    New Year's Holiday 3
	//	04 Jan    Tue    New Year's Holiday 4
	//	05 Jan    Wed    New Year's Holiday 5
	//	06 Jan    Thu    Epiphany
	//	25 Mar    Fri    Good Fri
	//	28 Mar    Mon    Easter Mon
	//	01 May    Sun    Labour Day
	//	08 May    Sun    V-E Day
	//	05 Jul    Tue    Sts. Cyril and Methodius Day
	//	29 Aug    Mon    Anniversary of Slovak National Uprising
	//	01 Sep    Thu    Constitution Day
	//	15 Sep    Thu    Our Lady of Sorrows
	//	01 Nov    Tue    All Saints' Day
	//	17 Nov    Thu    Freedom and Democracy Day
	//	24 Dec    Sat    Christmas Eve
	//	25 Dec    Sun    Christmas Day
	//	26 Dec    Mon    Boxing Day
	//	27 Dec    Tue    Christmas Holiday 1
	//	28 Dec    Wed    Christmas Holiday 2
	//	29 Dec    Thu    Christmas Holiday 3
	//	30 Dec    Fri    Christmas Holiday 4
	//	31 Dec    Sat    New Year's Eve
	
	@Test
	public void testSlovakiaYear2005() {
      	int year = 2005;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");

        final DateFactory df = DateFactory.getFactory();
      	final Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	final List<Date> expectedHol = new ArrayList<Date>();

		//expectedHol.add(df.getDate(1,JANUARY,year)); 
		//expectedHol.add(df.getDate(2,JANUARY,year)); 
		expectedHol.add(df.getDate(3,JANUARY,year)); 
		expectedHol.add(df.getDate(4,JANUARY,year)); 
		expectedHol.add(df.getDate(5,JANUARY,year)); 
		expectedHol.add(df.getDate(6,JANUARY,year)); 
		expectedHol.add(df.getDate(25,MARCH,year)); 
		expectedHol.add(df.getDate(28,MARCH,year)); 
		//expectedHol.add(df.getDate(1,MAY,year)); 
		//expectedHol.add(df.getDate(8,MAY,year)); 
		expectedHol.add(df.getDate(5,JULY,year)); 
		expectedHol.add(df.getDate(29,AUGUST,year)); 
    	expectedHol.add(df.getDate(1,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(15,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(1,NOVEMBER,year)); 
		expectedHol.add(df.getDate(17,NOVEMBER,year)); 
		//expectedHol.add(df.getDate(24,DECEMBER,year)); 
		//expectedHol.add(df.getDate(25,DECEMBER,year)); 
		expectedHol.add(df.getDate(26,DECEMBER,year)); 
		expectedHol.add(df.getDate(27,DECEMBER,year)); 
		expectedHol.add(df.getDate(28,DECEMBER,year)); 
		expectedHol.add(df.getDate(29,DECEMBER,year)); 
		expectedHol.add(df.getDate(30,DECEMBER,year)); 
		//expectedHol.add(df.getDate(31,DECEMBER,year)); 

        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	
	//FIXME: Needs to obtain reliable data for 2006
	@Test
	public void testSlovakiaYear2006() {
      	
		int year = 2006;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");

        final DateFactory df = DateFactory.getFactory();
      	final Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	final List<Date> expectedHol = new ArrayList<Date>();

		//expectedHol.add(df.getDate(1,JANUARY,year)); 
		expectedHol.add(df.getDate(2,JANUARY,year)); 
		expectedHol.add(df.getDate(3,JANUARY,year)); 
		expectedHol.add(df.getDate(4,JANUARY,year)); 
		expectedHol.add(df.getDate(5,JANUARY,year)); 
		expectedHol.add(df.getDate(6,JANUARY,year)); 
		expectedHol.add(df.getDate(14,APRIL,year)); 
		expectedHol.add(df.getDate(17,APRIL,year));
		expectedHol.add(df.getDate(1,MAY,year)); 
		expectedHol.add(df.getDate(8,MAY,year)); 
		expectedHol.add(df.getDate(5,JULY,year)); 
		expectedHol.add(df.getDate(29,AUGUST,year)); 
    	expectedHol.add(df.getDate(1,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(15,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(1,NOVEMBER,year)); 
		expectedHol.add(df.getDate(17,NOVEMBER,year)); 
		//expectedHol.add(df.getDate(24,DECEMBER,year)); 
		expectedHol.add(df.getDate(25,DECEMBER,year)); 
		expectedHol.add(df.getDate(26,DECEMBER,year)); 
		expectedHol.add(df.getDate(27,DECEMBER,year)); 
		expectedHol.add(df.getDate(28,DECEMBER,year)); 
		expectedHol.add(df.getDate(29,DECEMBER,year)); 
		//expectedHol.add(df.getDate(30,DECEMBER,year)); 
		//expectedHol.add(df.getDate(31,DECEMBER,year)); 
        
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	//	2007 - BSSE Trading Holidays
	//
	//	01 Jan    Mon    Founding of Slovak Republic
	//	02 Jan    Tue    New Year's Holiday 2
	//	03 Jan    Wed    New Year's Holiday 3
	//	04 Jan    Thu    New Year's Holiday 4
	//	05 Jan    Fri    New Year's Holiday 5
	//	06 Jan    Sat    Epiphany
	//	06 Apr    Fri    Good Friday
	//	09 Apr    Mon    Easter Monday
	//	01 May    Tue    Labour Day
	//	08 May    Tue    V-E Day
	//	05 Jul    Thu    Sts. Cyril and Methodius Day
	//	29 Aug    Wed    Anniversary of Slovak National Uprising
	//	01 Sep    Sat    Constitution Day
	//	15 Sep    Sat    Our Lady of Sorrows
	//	01 Nov    Thu    All Saints' Day
	//	17 Nov    Sat    Freedom and Democracy Day
	//	24 Dec    Mon    Christmas Eve
	//	25 Dec    Tue    Christmas Day
	//	26 Dec    Wed    Boxing Day
	//	27 Dec    Thu    Christmas Holiday 1
	//	28 Dec    Fri    Christmas Holiday 2
	//	29 Dec    Sat    Christmas Holiday 3
	//	30 Dec    Sun    Christmas Holiday 4
	//	31 Dec    Mon    New Year's Eve

	@Test
	public void testSlovakiaYear2007() {
      	
		int year = 2007;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");

        final DateFactory df = DateFactory.getFactory();
      	final Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(2,JANUARY,year));
    	expectedHol.add(df.getDate(3,JANUARY,year));
    	expectedHol.add(df.getDate(4,JANUARY,year));
    	expectedHol.add(df.getDate(5,JANUARY,year));
    	// expectedHol.add(df.getDate(6,JANUARY,year));
		expectedHol.add(df.getDate(6,APRIL,year)); 
		expectedHol.add(df.getDate(9,APRIL,year)); 
		expectedHol.add(df.getDate(1,MAY,year)); 
		expectedHol.add(df.getDate(8,MAY,year)); 
		expectedHol.add(df.getDate(5,JULY,year)); 
		expectedHol.add(df.getDate(29,AUGUST,year));
		// expectedHol.add(df.getDate(1,SEPTEMBER,year));
		// expectedHol.add(df.getDate(15,SEPTEMBER,year));
		expectedHol.add(df.getDate(1,NOVEMBER,year)); 
		// expectedHol.add(df.getDate(17,NOVEMBER,year));
		expectedHol.add(df.getDate(24,DECEMBER,year)); 
		expectedHol.add(df.getDate(25,DECEMBER,year)); 
		expectedHol.add(df.getDate(26,DECEMBER,year));
		expectedHol.add(df.getDate(27,DECEMBER,year));
		expectedHol.add(df.getDate(28,DECEMBER,year));
		// expectedHol.add(df.getDate(29,DECEMBER,year));
		// expectedHol.add(df.getDate(30,DECEMBER,year));
		expectedHol.add(df.getDate(31,DECEMBER,year));

		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }


	//	2008 - BSSE Trading Holidays
	//
	//	01 Jan   Tue    Founding of Slovak Republic
	//	02 Jan   Wed    New Year's Holiday 2
	//	03 Jan   Thu    New Year's Holiday 3
	//	04 Jan   Fri    New Year's Holiday 4
	//	05 Jan   Sat    New Year's Holiday 5
	//	06 Jan   Sun    Epiphany
	//	21 Mar   Fri    Good Friday
	//	24 Mar   Mon    Easter Monday
	//	01 May   Thu    Labour Day
	//	08 May   Thu    V-E Day
	//	05 Jul   Sat    Sts. Cyril and Methodius Day
	//	29 Aug   Fri    Anniversary of Slovak National Uprising
	//	01 Sep   Mon    Constitution Day
	//	15 Sep   Mon    Our Lady of Sorrows
	//	01 Nov   Sat    All Saints' Day
	//	17 Nov   Mon    Freedom and Democracy Day
	//	24 Dec   Wed    Christmas Eve
	//	25 Dec   Thu    Christmas Day
	//	26 Dec   Fri    Boxing Day
	//	27 Dec   Sat    Christmas Holiday 1
	//	28 Dec   Sun    Christmas Holiday 2
	//	31 Dec   Wed    New Year's Eve

	@Test
	public void testSlovakiaYear2008() {
      	
		int year = 2008;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");

        final DateFactory df = DateFactory.getFactory();
      	final Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(2,JANUARY,year));
    	expectedHol.add(df.getDate(3,JANUARY,year));
    	expectedHol.add(df.getDate(4,JANUARY,year));
    	// expectedHol.add(df.getDate(5,JANUARY,year));
    	// expectedHol.add(df.getDate(6,JANUARY,year));
		expectedHol.add(df.getDate(21,MARCH,year)); 
		expectedHol.add(df.getDate(24,MARCH,year)); 
		expectedHol.add(df.getDate(1,MAY,year)); 
		expectedHol.add(df.getDate(8,MAY,year)); 
		expectedHol.add(df.getDate(29,AUGUST,year)); 
    	expectedHol.add(df.getDate(1,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(15,SEPTEMBER,year));
		// expectedHol.add(df.getDate(1,NOVEMBER,year)); 
		expectedHol.add(df.getDate(17,NOVEMBER,year)); 
		expectedHol.add(df.getDate(24,DECEMBER,year)); 
		expectedHol.add(df.getDate(25,DECEMBER,year)); 
		expectedHol.add(df.getDate(26,DECEMBER,year)); 
		// expectedHol.add(df.getDate(27,DECEMBER,year)); 
		// expectedHol.add(df.getDate(28,DECEMBER,year)); 
		expectedHol.add(df.getDate(29,DECEMBER,year)); 
		expectedHol.add(df.getDate(30,DECEMBER,year)); 
		expectedHol.add(df.getDate(31,DECEMBER,year)); 

		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	//FIXME: Needs to obtain reliable data for 2009
	@Test
	public void testSlovakiaYear2009() {
      	
		int year = 2009;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");

        final DateFactory df = DateFactory.getFactory();
      	final Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(1,JANUARY,year)); 
    	expectedHol.add(df.getDate(2,JANUARY,year)); 
    	//expectedHol.add(df.getDate(3,JANUARY,year)); 
    	//expectedHol.add(df.getDate(4,JANUARY,year)); 
    	expectedHol.add(df.getDate(5,JANUARY,year)); 
		expectedHol.add(df.getDate(6,JANUARY,year)); 
		expectedHol.add(df.getDate(10,APRIL,year)); 
		expectedHol.add(df.getDate(13,APRIL,year)); 
		expectedHol.add(df.getDate(1,MAY,year)); 
		expectedHol.add(df.getDate(8,MAY,year)); 
    	expectedHol.add(df.getDate(1,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(15,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(17,NOVEMBER,year)); 
		expectedHol.add(df.getDate(24,DECEMBER,year)); 
		expectedHol.add(df.getDate(25,DECEMBER,year)); 
		//expectedHol.add(df.getDate(26,DECEMBER,year)); 
		//expectedHol.add(df.getDate(27,DECEMBER,year)); 
		expectedHol.add(df.getDate(28,DECEMBER,year)); 
		expectedHol.add(df.getDate(29,DECEMBER,year)); 
		expectedHol.add(df.getDate(30,DECEMBER,year)); 
		expectedHol.add(df.getDate(31,DECEMBER,year)); 
		
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	//FIXME: Needs to obtain reliable data for 2010
	@Test
	public void testSlovakiaYear2010() {
      	
		int year = 2010;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");

        final DateFactory df = DateFactory.getFactory();
      	final Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(df.getDate(1,JANUARY,year)); 
    	//expectedHol.add(df.getDate(2,JANUARY,year)); 
    	//expectedHol.add(df.getDate(3,JANUARY,year)); 
    	expectedHol.add(df.getDate(4,JANUARY,year)); 
    	expectedHol.add(df.getDate(5,JANUARY,year)); 
		expectedHol.add(df.getDate(6,JANUARY,year)); 
		expectedHol.add(df.getDate(2,APRIL,year)); 
		expectedHol.add(df.getDate(5,APRIL,year)); 
		expectedHol.add(df.getDate(5,JULY,year)); 
    	expectedHol.add(df.getDate(1,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(15,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(1,NOVEMBER,year)); 
		expectedHol.add(df.getDate(17,NOVEMBER,year)); 
		expectedHol.add(df.getDate(24,DECEMBER,year)); 
		//expectedHol.add(df.getDate(25,DECEMBER,year)); 
		//expectedHol.add(df.getDate(26,DECEMBER,year)); 
		expectedHol.add(df.getDate(27,DECEMBER,year)); 
		expectedHol.add(df.getDate(28,DECEMBER,year)); 
		expectedHol.add(df.getDate(29,DECEMBER,year)); 
		expectedHol.add(df.getDate(30,DECEMBER,year)); 
		expectedHol.add(df.getDate(31,DECEMBER,year)); 
        
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	//FIXME: Needs to obtain reliable data for 2011
	@Test
	public void testSlovakiaYear2011() {
      	
		int year = 2011;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");

        final DateFactory df = DateFactory.getFactory();
      	final Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	final List<Date> expectedHol = new ArrayList<Date>();

		//expectedHol.add(df.getDate(1,JANUARY,year)); 
		//expectedHol.add(df.getDate(2,JANUARY,year)); 
		expectedHol.add(df.getDate(3,JANUARY,year)); 
		expectedHol.add(df.getDate(4,JANUARY,year)); 
		expectedHol.add(df.getDate(5,JANUARY,year)); 
		expectedHol.add(df.getDate(6,JANUARY,year)); 
		expectedHol.add(df.getDate(22,APRIL,year)); 
		expectedHol.add(df.getDate(25,APRIL,year)); 
		expectedHol.add(df.getDate(5,JULY,year)); 
		expectedHol.add(df.getDate(29,AUGUST,year)); 
    	expectedHol.add(df.getDate(1,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(15,SEPTEMBER,year)); 
		expectedHol.add(df.getDate(1,NOVEMBER,year)); 
		expectedHol.add(df.getDate(17,NOVEMBER,year)); 
		//expectedHol.add(df.getDate(24,DECEMBER,year)); 
		//expectedHol.add(df.getDate(25,DECEMBER,year)); 
		expectedHol.add(df.getDate(26,DECEMBER,year)); 
		expectedHol.add(df.getDate(27,DECEMBER,year)); 
		expectedHol.add(df.getDate(28,DECEMBER,year)); 
		expectedHol.add(df.getDate(29,DECEMBER,year)); 
		expectedHol.add(df.getDate(30,DECEMBER,year)); 
		//expectedHol.add(df.getDate(31,DECEMBER,year)); 
        
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	
	//FIXME: Needs to obtain reliable data for 2012
	@Test
	public void testSlovakiaYear2012() {
      	
		int year = 2012;
      	logger.info("Testing Solvakia's holiday list for the year " + year + "...");

        final DateFactory df = DateFactory.getFactory();
      	final Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	final List<Date> expectedHol = new ArrayList<Date>();

		//expectedHol.add(df.getDate(1,JANUARY,year)); 
		expectedHol.add(df.getDate(2,JANUARY,year)); 
		expectedHol.add(df.getDate(3,JANUARY,year)); 
		expectedHol.add(df.getDate(4,JANUARY,year)); 
		expectedHol.add(df.getDate(5,JANUARY,year)); 
		expectedHol.add(df.getDate(6,JANUARY,year)); 
		expectedHol.add(df.getDate(6,APRIL,year)); 
		expectedHol.add(df.getDate(9,APRIL,year)); 
		expectedHol.add(df.getDate(1,MAY,year)); 
		expectedHol.add(df.getDate(8,MAY,year)); 
		expectedHol.add(df.getDate(5,JULY,year)); 
		expectedHol.add(df.getDate(29,AUGUST,year)); 
		expectedHol.add(df.getDate(1,NOVEMBER,year));
		expectedHol.add(df.getDate(24,DECEMBER,year));
		expectedHol.add(df.getDate(25,DECEMBER,year)); 
		expectedHol.add(df.getDate(26,DECEMBER,year)); 
		expectedHol.add(df.getDate(27,DECEMBER,year)); 
		expectedHol.add(df.getDate(28,DECEMBER,year)); 
		//expectedHol.add(df.getDate(29,DECEMBER,year)); 
		//expectedHol.add(df.getDate(30,DECEMBER,year)); 
		expectedHol.add(df.getDate(31,DECEMBER,year)); 
        
		// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
}
