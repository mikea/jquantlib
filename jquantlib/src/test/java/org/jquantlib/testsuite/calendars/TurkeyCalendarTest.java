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
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.NOVEMBER;
import static org.jquantlib.time.Month.OCTOBER;
import static org.jquantlib.time.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Turkey;
import org.junit.Test;

/**
 * @author Renjith Nair
 *
 *
 */

public class TurkeyCalendarTest {

    private final Calendar exchange;

	public TurkeyCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        this.exchange = new Turkey();
	}

    // 2004 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2004()
    {
       	final int year = 2004;
    	QL.info("Testing " + exchange.name() + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(2,FEBRUARY,year));
    	expectedHol.add(new Date(3,FEBRUARY,year));
    	expectedHol.add(new Date(4,FEBRUARY,year));
    	expectedHol.add(new Date(23,APRIL,year));
    	expectedHol.add(new Date(19,MAY,year));
    	expectedHol.add(new Date(30,AUGUST,year));
    	expectedHol.add(new Date(29,OCTOBER,year));
    	expectedHol.add(new Date(15,NOVEMBER,year));
    	expectedHol.add(new Date(16,NOVEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2005 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2005()
    {
       	final int year = 2005;
    	QL.info("Testing " + exchange.name() + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(19,JANUARY,year));
    	expectedHol.add(new Date(20,JANUARY,year));
    	expectedHol.add(new Date(21,JANUARY,year));
    	expectedHol.add(new Date(19,MAY,year));
    	expectedHol.add(new Date(30,AUGUST,year));
    	expectedHol.add(new Date(2,NOVEMBER,year));
    	expectedHol.add(new Date(3,NOVEMBER,year));
    	expectedHol.add(new Date(4,NOVEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2006 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2006()
    {
       	final int year = 2006;
    	QL.info("Testing " + exchange.name() + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(9,JANUARY,year));
    	expectedHol.add(new Date(10,JANUARY,year));
    	expectedHol.add(new Date(11,JANUARY,year));
    	expectedHol.add(new Date(12,JANUARY,year));
    	expectedHol.add(new Date(13,JANUARY,year));
    	expectedHol.add(new Date(19,MAY,year));
    	expectedHol.add(new Date(30,AUGUST,year));
    	expectedHol.add(new Date(23,OCTOBER,year));
    	expectedHol.add(new Date(24,OCTOBER,year));
    	expectedHol.add(new Date(25,OCTOBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2007 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2007()
    {
       	final int year = 2007;
    	QL.info("Testing " + exchange.name() + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(2,JANUARY,year));
    	expectedHol.add(new Date(3,JANUARY,year));
    	expectedHol.add(new Date(4,JANUARY,year));
    	expectedHol.add(new Date(23,APRIL,year));
    	expectedHol.add(new Date(30,AUGUST,year));
    	expectedHol.add(new Date(11,OCTOBER,year));
    	expectedHol.add(new Date(12,OCTOBER,year));
    	expectedHol.add(new Date(29,OCTOBER,year));
    	expectedHol.add(new Date(19,DECEMBER,year));
    	expectedHol.add(new Date(20,DECEMBER,year));
    	expectedHol.add(new Date(21,DECEMBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2008 - Current Year
	@Test
    public void testTurkeyISEHolidaysYear2008()
    {
       	final int year = 2008;
    	QL.info("Testing " + exchange.name() + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(23,APRIL,year));
    	expectedHol.add(new Date(19,MAY,year));
    	expectedHol.add(new Date(29,SEPTEMBER,year));
    	expectedHol.add(new Date(30,SEPTEMBER,year));
    	expectedHol.add(new Date(1,OCTOBER,year));
    	expectedHol.add(new Date(2,OCTOBER,year));
    	expectedHol.add(new Date(29,OCTOBER,year));
    	expectedHol.add(new Date(8,DECEMBER,year));
    	expectedHol.add(new Date(9,DECEMBER,year));
    	expectedHol.add(new Date(10,DECEMBER,year));
    	expectedHol.add(new Date(11,DECEMBER,year));


    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2009 - Future Year
	@Test
    public void testTurkeyISEHolidaysYear2009()
    {
       	final int year = 2009;
    	QL.info("Testing " + exchange.name() + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(23,APRIL,year));
    	expectedHol.add(new Date(19,MAY,year));
    	expectedHol.add(new Date(29,OCTOBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2010 - Future Year
	@Test
    public void testTurkeyISEHolidaysYear2010()
    {
       	final int year = 2010;
    	QL.info("Testing " + exchange.name() + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(23,APRIL,year));
    	expectedHol.add(new Date(19,MAY,year));
    	expectedHol.add(new Date(30,AUGUST,year));
    	expectedHol.add(new Date(29,OCTOBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2011 - Future Year
	@Test
    public void testTurkeyISEHolidaysYear2011()
    {
       	final int year = 2011;
    	QL.info("Testing " + exchange.name() + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(19,MAY,year));
    	expectedHol.add(new Date(30,AUGUST,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2012 - Future Year
	@Test
    public void testTurkeyISEHolidaysYear2012()
    {
       	final int year = 2012;
    	QL.info("Testing " + exchange.name() + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(23,APRIL,year));
    	expectedHol.add(new Date(30,AUGUST,year));
    	expectedHol.add(new Date(29,OCTOBER,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

}
