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

import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.AUGUST;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;

import java.util.List;
import java.util.Vector;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Switzerland;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 * @author Renjith Nair
 * <p>
 * <strong>Description</strong><br>
 * Switzerland Calendar Test.
 */

public class SwitzerlandCalendarTest {

	public SwitzerlandCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}


	@Test
	public void testSwitzerlandSWXCalendar() {

		QL.info("\n\n=== Switzerland SWX Calendar ===");

		final Calendar c = new Switzerland();

		testSwitzerlandCalendar(c);
	}

//	@Test
//	public void testSwitzerlandSettlementCalendar() {
//
//		QL.info("\n\n=== Switzerland Settlement Calendar ===");
//
//		final Calendar c = new Switzerland();
//
//		testSwitzerlandCalendar( c);
//	}

	private void testSwitzerlandCalendar( final Calendar c) {
		// 2004 - leap-year in the past
		testSwitzerland2004(c);

		// 2005 - year in the past
		testSwitzerland2005(c);

		// 2006 - year in the past
		testSwitzerland2006(c);

		// 2007 - regular year in the past
		testSwitzerland2007(c);

		// 2008 - current year
		testSwitzerland2008(c);

		// 2009 - regular year in the future
		testSwitzerland2009(c);

		// 2010 -  year in the future
		testSwitzerland2010(c);

		// 2011 -  year in the future
		testSwitzerland2011(c);

		// 2012 - leap-year in the future
		testSwitzerland2012(c);
	}

	public void testSwitzerland2004(final Calendar c){

		final int year = 2004;
		QL.info("Testing " + c.name() + " holiday list for the year " + year + "...");

		final List<Date> expectedHol = new Vector<Date>();

		expectedHol.add(new Date(1,JANUARY,year));
		expectedHol.add(new Date(2,JANUARY,year));
    	expectedHol.add(new Date(9,APRIL,year));
    	expectedHol.add(new Date(12,APRIL,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year)); --> Saturday
    	expectedHol.add(new Date(20,MAY,year));
    	expectedHol.add(new Date(31,MAY,year));

//    	if(market == Switzerland.Market.Settlement)
//    	{
//	    	expectedHol.add(new Date(24,DECEMBER,year));
//	    	expectedHol.add(new Date(31,DECEMBER,year));
//    	}
    	// expectedHol.add(DateFactory.getDateUtil().getDate(26,DECEMBER,year)); --> Sunday

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}

	public void testSwitzerland2005(final Calendar c){

		final int year = 2005;
		QL.info("Testing " + c.name() + " holiday list for the year " + year + "...");

		final List<Date> expectedHol = new Vector<Date>();

    	expectedHol.add(new Date(25,MARCH,year));
    	expectedHol.add(new Date(28,MARCH,year));
    	expectedHol.add(new Date(5,MAY,year));
    	expectedHol.add(new Date(16,MAY,year));
    	expectedHol.add(new Date(1,AUGUST,year));
    	expectedHol.add(new Date(26,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}

	public void testSwitzerland2006(final Calendar c ){

		final int year = 2006;
		QL.info("Testing " + c.name() + " holiday list for the year " + year + "...");

		final List<Date> expectedHol = new Vector<Date>();

		expectedHol.add(new Date(2,JANUARY,year));
    	expectedHol.add(new Date(14,APRIL,year));
    	expectedHol.add(new Date(17,APRIL,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(25,MAY,year));
    	expectedHol.add(new Date(5,JUNE,year));
    	expectedHol.add(new Date(1,AUGUST,year));
    	expectedHol.add(new Date(25,DECEMBER,year));
    	expectedHol.add(new Date(26,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}

	public void testSwitzerland2007(final Calendar c) {

		final int year = 2007;
		QL.info("Testing " + c.name() + " holiday list for the year " + year + "...");

		final List<Date> expectedHol = new Vector<Date>();

		expectedHol.add(new Date(1,JANUARY,year));
		expectedHol.add(new Date(2,JANUARY,year));
    	expectedHol.add(new Date(6,APRIL,year));
    	expectedHol.add(new Date(9,APRIL,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(17,MAY,year));
    	expectedHol.add(new Date(28,MAY,year));
    	expectedHol.add(new Date(1,AUGUST,year));
    	expectedHol.add(new Date(25,DECEMBER,year));
    	expectedHol.add(new Date(26,DECEMBER,year));

//    	if(market == Switzerland.Market.Settlement)
//    	{
//	    	expectedHol.add(new Date(24,DECEMBER,year));
//	    	expectedHol.add(new Date(31,DECEMBER,year));
//    	}

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}

	public void testSwitzerland2008(final Calendar c){

		final int year = 2008;
		QL.info("Testing " + c.name() + " holiday list for the year " + year + "...");

		final List<Date> expectedHol = new Vector<Date>();

		expectedHol.add(new Date(1,JANUARY,year));
		expectedHol.add(new Date(2,JANUARY,year));
    	expectedHol.add(new Date(21,MARCH,year));
    	expectedHol.add(new Date(24,MARCH,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(12,MAY,year));
    	expectedHol.add(new Date(1,AUGUST,year));
    	expectedHol.add(new Date(25,DECEMBER,year));
    	expectedHol.add(new Date(26,DECEMBER,year));

//    	if(market == Switzerland.Market.Settlement)
//    	{
//	    	expectedHol.add(new Date(24,DECEMBER,year));
//	    	expectedHol.add(new Date(31,DECEMBER,year));
//    	}

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}

	public void testSwitzerland2009(final Calendar c) {

		final int year = 2009;
		QL.info("Testing " + c.name() + " holiday list for the year " + year + "...");

		final List<Date> expectedHol = new Vector<Date>();

		expectedHol.add(new Date(1,JANUARY,year));
		expectedHol.add(new Date(2,JANUARY,year));
    	expectedHol.add(new Date(10,APRIL,year));
    	expectedHol.add(new Date(13,APRIL,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(21,MAY,year));
    	expectedHol.add(new Date(01,JUNE,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,AUGUST,year)); --> Saturday
    	expectedHol.add(new Date(25,DECEMBER,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(26,DECEMBER,year)); --> Saturday

//    	if(market == Switzerland.Market.Settlement)
//    	{
//	    	expectedHol.add(new Date(24,DECEMBER,year));
//	    	expectedHol.add(new Date(31,DECEMBER,year));
//    	}

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}

	public void testSwitzerland2010(final Calendar c){

		final int year = 2010;
		QL.info("Testing " + c.name() + " holiday list for the year " + year + "...");

		final List<Date> expectedHol = new Vector<Date>();

		expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(2,APRIL,year));
    	expectedHol.add(new Date(5,APRIL,year));
    	expectedHol.add(new Date(13,MAY,year));
    	expectedHol.add(new Date(24,MAY,year));

//    	if(market == Switzerland.Market.Settlement)
//    	{
//	    	expectedHol.add(new Date(24,DECEMBER,year));
//	    	expectedHol.add(new Date(31,DECEMBER,year));
//    	}

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}

	public void testSwitzerland2011(final Calendar c){

		final int year = 2011;
		QL.info("Testing " + c.name() + " holiday list for the year " + year + "...");

		final List<Date> expectedHol = new Vector<Date>();

    	expectedHol.add(new Date(22,APRIL,year));
    	expectedHol.add(new Date(25,APRIL,year));
    	expectedHol.add(new Date(2,JUNE,year));
    	expectedHol.add(new Date(13,JUNE,year));
    	expectedHol.add(new Date(1,AUGUST,year));
    	expectedHol.add(new Date(26,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}

	public void testSwitzerland2012(final Calendar c) {
		final int year = 2012;
		QL.info("Testing " + c.name() + " holiday list for the year " + year + "...");

		final List<Date> expectedHol = new Vector<Date>();

		// expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); --> Sunday
		expectedHol.add(new Date(2,JANUARY,year));
    	expectedHol.add(new Date(6,APRIL,year));
    	expectedHol.add(new Date(9,APRIL,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(17,MAY,year));
    	expectedHol.add(new Date(28,MAY,year));
    	expectedHol.add(new Date(1,AUGUST,year));
    	expectedHol.add(new Date(25,DECEMBER,year));
    	expectedHol.add(new Date(26,DECEMBER,year));

//    	if(market == Switzerland.Market.Settlement)
//    	{
//	    	expectedHol.add(new Date(24,DECEMBER,year));
//	    	expectedHol.add(new Date(31,DECEMBER,year));
//    	}

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}
}
