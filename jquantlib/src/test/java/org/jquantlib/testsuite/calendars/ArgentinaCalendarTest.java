/*
 Copyright (C) 2008 Srinivas Hasti
 Copyright (C) 2008 Dominik Holenstein

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
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Argentina;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 * @author Jia Jia
 */

public class ArgentinaCalendarTest extends BaseCalendarTest{

	private final Calendar merval;
	private final Calendar settlement;

	public ArgentinaCalendarTest() {
		QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");

		settlement = Argentina.getCalendar(Argentina.Market.SETTLEMENT);
		merval = Argentina.getCalendar(Argentina.Market.MERVAL);
	}

	@Test
	public void testArgentinaMervalYear2004() {
		final int year = 2004;

		QL.info("Testing " + Argentina.Market.MERVAL
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1, JANUARY, year));
		expectedHol.add(getDate(24, MARCH, year));
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(8, APRIL, year));
		expectedHol.add(getDate(9, APRIL, year));
		expectedHol.add(getDate(25, MAY, year));
		// expectedHol.add(df.getDate(1,MAY,year)); --> Sunday
		expectedHol.add(getDate(21, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(16, AUGUST, year));
		expectedHol.add(getDate(11, OCTOBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		// expectedHol.add(df.getDate(25,DECEMBER,year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, merval, year);

	}

	@Test
	public void testArgentinaMervalYear2005() {
		final int year = 2005;

		QL.info("Testing " + Argentina.Market.MERVAL
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(24, MARCH, year));
		expectedHol.add(getDate(25, MARCH, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(20, JUNE, year));
		expectedHol.add(getDate(15, AUGUST, year));
		expectedHol.add(getDate(10, OCTOBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, merval, year);
	}

	@Test
	public void testArgentinaMervalYear2006() {

		final int year = 2006;

		QL.info("Testing " + Argentina.Market.MERVAL
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(24, MARCH, year));
		expectedHol.add(getDate(13, APRIL, year));
		expectedHol.add(getDate(14, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(19, JUNE, year));
		expectedHol.add(getDate(21, AUGUST, year));
		expectedHol.add(getDate(16, OCTOBER, year));
		expectedHol.add(getDate(6, NOVEMBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, merval, year);
	}

	// 2007 - MERVAL Trading Holidays
	//
	// 01 Jan Mon New Year's Day
	// 24 Mar Sat Truth and Justice Day
	// 02 Apr Mon Malvinas Islands Memorial
	// 05 Apr Thu Holy Thursday
	// 06 Apr Fri Good Friday
	// 01 May Tue Workers' Day
	// 25 May Fri National Holiday
	// 18 Jun Mon Flag Day
	// 09 Jul Mon Independence Day
	// 20 Aug Mon Anniversary of the Death of General San Martin
	// 15 Oct Mon Columbus Day (OBS)
	// 06 Nov Tue Bank Holiday
	// 08 Dec Sat Immaculate Conception
	// 24 Dec Mon Christmas Eve
	// 25 Dec Tue Christmas Day
	// 31 Dec Mon Last business day of year

	@Test
	public void testArgentinaMervalYear2007() {

		final int year = 2007;

		QL.info("Testing " + Argentina.Market.MERVAL
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1, JANUARY, year));
		// expectedHol.add(df.getDate(24,MARCH,year));
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(5, APRIL, year));
		expectedHol.add(getDate(6, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(18, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(20, AUGUST, year));
		expectedHol.add(getDate(15, OCTOBER, year));
		expectedHol.add(getDate(20, AUGUST, year));
		expectedHol.add(getDate(6, NOVEMBER, year));
		// expectedHol.add(df.getDate( 8,DECEMBER,year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, merval, year);
	}

	@Test
	public void testArgentinaMervalYear2008() {
		final int year = 2008;

		QL.info("Testing " + Argentina.Market.MERVAL
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1, JANUARY, year));
		expectedHol.add(getDate(20, MARCH, year));
		expectedHol.add(getDate(21, MARCH, year));
		expectedHol.add(getDate(24, MARCH, year));
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(16, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(18, AUGUST, year));
		expectedHol.add(getDate(6, NOVEMBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));

		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, merval, year);
	}

	@Test
	public void testArgentinaMervalYear2009() {

		final int year = 2009;

		QL.info("Testing " + Argentina.Market.MERVAL
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1, JANUARY, year));
		expectedHol.add(getDate(24, MARCH, year));
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(9, APRIL, year));
		expectedHol.add(getDate(10, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(15, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(17, AUGUST, year));
		expectedHol.add(getDate(12, OCTOBER, year));
		expectedHol.add(getDate(6, NOVEMBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, merval, year);

	}

	@Test
	public void testArgentinaMervalYear2010() {

		final int year = 2010;

		QL.info("Testing " + Argentina.Market.MERVAL
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1, JANUARY, year));
		expectedHol.add(getDate(24, MARCH, year));
		expectedHol.add(getDate(1, APRIL, year));
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(21, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(16, AUGUST, year));
		expectedHol.add(getDate(11, OCTOBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, merval, year);
	}

	@Test
	public void testArgentinaMervalYear2011() {
		final int year = 2011;

		QL.info("Testing " + Argentina.Market.MERVAL
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(24, MARCH, year));
		expectedHol.add(getDate(21, APRIL, year));
		expectedHol.add(getDate(22, APRIL, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(20, JUNE, year));
		expectedHol.add(getDate(15, AUGUST, year));
		expectedHol.add(getDate(10, OCTOBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, merval, year);
	}

	@Test
	public void testArgentinaMervalYear2012() {

		final int year = 2012;

		QL.info("Testing " + Argentina.Market.MERVAL
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		// expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year));
		// --> Sunday
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(5, APRIL, year));
		expectedHol.add(getDate(6, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(18, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(20, AUGUST, year));
		expectedHol.add(getDate(15, OCTOBER, year));
		expectedHol.add(getDate(6, NOVEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, merval, year);
	}

	@Test
	public void testArgentinaSettlementYear2004() {
		final int year = 2004;
		QL.info("Testing " + Argentina.Market.SETTLEMENT
				+ " holiday list for the year " + year + "...");
		final DateFactory df = DateFactory.getFactory();
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(df.getDate(1, JANUARY, year));
		expectedHol.add(df.getDate(2, APRIL, year));
		expectedHol.add(df.getDate(8, APRIL, year));
		expectedHol.add(df.getDate(9, APRIL, year));
		expectedHol.add(df.getDate(25, MAY, year));
		expectedHol.add(df.getDate(21, JUNE, year));
		expectedHol.add(df.getDate(9, JULY, year));
		expectedHol.add(df.getDate(16, AUGUST, year));
		expectedHol.add(df.getDate(11, OCTOBER, year));
		expectedHol.add(df.getDate(2, NOVEMBER, year));
		expectedHol.add(df.getDate(8, DECEMBER, year));
		expectedHol.add(df.getDate(24, DECEMBER, year));
		expectedHol.add(df.getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, settlement, year);
	}

	@Test
	public void testArgentinaSettlementYear2005() {
		final int year = 2005;
		QL.info("Testing " + Argentina.Market.SETTLEMENT
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(24, MARCH, year));
		expectedHol.add(getDate(25, MARCH, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(20, JUNE, year));
		expectedHol.add(getDate(15, AUGUST, year));
		expectedHol.add(getDate(10, OCTOBER, year));
		expectedHol.add(getDate(2, NOVEMBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(30, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, settlement, year);
	}

	@Test
	public void testArgentinaSettlementYear2006() {
		final int year = 2006;
		QL.info("Testing " + Argentina.Market.SETTLEMENT
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(13, APRIL, year));
		expectedHol.add(getDate(14, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(19, JUNE, year));
		expectedHol.add(getDate(21, AUGUST, year));
		expectedHol.add(getDate(16, OCTOBER, year));
		expectedHol.add(getDate(2, NOVEMBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, settlement, year);
	}

	@Test
	public void testArgentinaSettlementYear2007() {
		final int year = 2007;
		QL.info("Testing " + Argentina.Market.SETTLEMENT
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1, JANUARY, year));
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(5, APRIL, year));
		expectedHol.add(getDate(6, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(18, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(20, AUGUST, year));
		expectedHol.add(getDate(15, OCTOBER, year));
		expectedHol.add(getDate(2, NOVEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, settlement, year);
	}

	@Test
	public void testArgentinaSettlementYear2008() {
		final int year = 2008;
		QL.info("Testing " + Argentina.Market.SETTLEMENT
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1, JANUARY, year));
		expectedHol.add(getDate(20, MARCH, year));
		expectedHol.add(getDate(21, MARCH, year));
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(16, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(18, AUGUST, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, settlement, year);
	}

	@Test
	public void testArgentinaSettlementYear2009() {
		final int year = 2009;
		QL.info("Testing " + Argentina.Market.SETTLEMENT
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1, JANUARY, year));
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(9, APRIL, year));
		expectedHol.add(getDate(10, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(15, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(17, AUGUST, year));
		expectedHol.add(getDate(12, OCTOBER, year));
		expectedHol.add(getDate(2, NOVEMBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, settlement, year);
	}

	@Test
	public void testArgentinaSettlementYear2010() {
		final int year = 2010;
		QL.info("Testing " + Argentina.Market.SETTLEMENT
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1, JANUARY, year));
		expectedHol.add(getDate(1, APRIL, year));
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(21, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(16, AUGUST, year));
		expectedHol.add(getDate(11, OCTOBER, year));
		expectedHol.add(getDate(2, NOVEMBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, settlement, year);
	}

	@Test
	public void testArgentinaSettlementYear2011() {
		final int year = 2011;
		QL.info("Testing " + Argentina.Market.SETTLEMENT
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(21, APRIL, year));
		expectedHol.add(getDate(22, APRIL, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(20, JUNE, year));
		expectedHol.add(getDate(15, AUGUST, year));
		expectedHol.add(getDate(10, OCTOBER, year));
		expectedHol.add(getDate(2, NOVEMBER, year));
		expectedHol.add(getDate(8, DECEMBER, year));
		expectedHol.add(getDate(30, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, settlement, year);
	}

	@Test
	public void testArgentinaSettlementYear2012() {
		final int year = 2012;
		QL.info("Testing " + Argentina.Market.SETTLEMENT
				+ " holiday list for the year " + year + "...");
		final List<Date> expectedHol = new ArrayList<Date>();

		// expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year));
		// --> Sunday
		expectedHol.add(getDate(2, APRIL, year));
		expectedHol.add(getDate(5, APRIL, year));
		expectedHol.add(getDate(6, APRIL, year));
		expectedHol.add(getDate(1, MAY, year));
		expectedHol.add(getDate(25, MAY, year));
		expectedHol.add(getDate(18, JUNE, year));
		expectedHol.add(getDate(9, JULY, year));
		expectedHol.add(getDate(20, AUGUST, year));
		expectedHol.add(getDate(15, OCTOBER, year));
		expectedHol.add(getDate(2, NOVEMBER, year));
		expectedHol.add(getDate(24, DECEMBER, year));
		expectedHol.add(getDate(25, DECEMBER, year));
		expectedHol.add(getDate(31, DECEMBER, year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, settlement, year);
	}

	
}
