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
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Ukraine;
import org.junit.Test;

/**
 * @author Renjith Nair
 *
 *
 */

public class UkraineCalendarTest {

    private final Calendar exchange;

	public UkraineCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        this.exchange = Ukraine.getCalendar(Ukraine.Market.USE);
	}

    // 2004 - year in the past and leap year
	@Test
    public void testUkraineUSEHolidaysYear2004()
    {
       	final int year = 2004;
    	QL.info("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(7,JANUARY,year));
    	expectedHol.add(new Date(8,MARCH,year));
    	expectedHol.add(new Date(12,APRIL,year));
    	expectedHol.add(new Date(3,MAY,year));
    	expectedHol.add(new Date(10,MAY,year));
    	expectedHol.add(new Date(31,MAY,year));
    	expectedHol.add(new Date(28,JUNE,year));
    	expectedHol.add(new Date(24,AUGUST,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2005 - year in the past and leap year
	@Test
    public void testUkraineUSEHolidaysYear2005()
    {
       	final int year = 2005;
    	QL.info("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(3,JANUARY,year));
    	expectedHol.add(new Date(7,JANUARY,year));
    	expectedHol.add(new Date(8,MARCH,year));
    	expectedHol.add(new Date(28,MARCH,year));
    	expectedHol.add(new Date(2,MAY,year));
    	expectedHol.add(new Date(9,MAY,year));
    	expectedHol.add(new Date(16,MAY,year));
    	expectedHol.add(new Date(28,JUNE,year));
    	expectedHol.add(new Date(24,AUGUST,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2006 - year in the past and leap year
	@Test
    public void testUkraineUSEHolidaysYear2006()
    {
       	final int year = 2006;
    	QL.info("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(2,JANUARY,year));
    	expectedHol.add(new Date(9,JANUARY,year));
    	expectedHol.add(new Date(8,MARCH,year));
    	expectedHol.add(new Date(17,APRIL,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(2,MAY,year));
    	expectedHol.add(new Date(9,MAY,year));
    	expectedHol.add(new Date(5,JUNE,year));
    	expectedHol.add(new Date(28,JUNE,year));
    	expectedHol.add(new Date(24,AUGUST,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2007 - year in the past and leap year
	@Test
    public void testUkraineUSEHolidaysYear2007()
    {
       	final int year = 2007;
    	QL.info("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(8,JANUARY,year));
    	expectedHol.add(new Date(8,MARCH,year));
    	expectedHol.add(new Date(9,APRIL,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(2,MAY,year));
    	expectedHol.add(new Date(9,MAY,year));
    	expectedHol.add(new Date(28,MAY,year));
    	expectedHol.add(new Date(28,JUNE,year));
    	expectedHol.add(new Date(24,AUGUST,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }


	 // 2008 - Current Year
	@Test
    public void testUkraineUSEHolidaysYear2008()
    {
       	final int year = 2008;
    	QL.info("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(7,JANUARY,year));
    	expectedHol.add(new Date(10,MARCH,year));
    	expectedHol.add(new Date(24,MARCH,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(2,MAY,year));
    	expectedHol.add(new Date(9,MAY,year));
    	expectedHol.add(new Date(12,MAY,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	 // 2009 - Future Year
	@Test
    public void testUkraineUSEHolidaysYear2009()
    {
       	final int year = 2009;
    	QL.info("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(7,JANUARY,year));
    	expectedHol.add(new Date(9,MARCH,year));
    	expectedHol.add(new Date(13,APRIL,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(11,MAY,year));
    	expectedHol.add(new Date(1,JUNE,year));
    	expectedHol.add(new Date(24,AUGUST,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2010 - Future Year
	@Test
    public void testUkraineUSEHolidaysYear2010()
    {
       	final int year = 2010;
    	QL.info("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(7,JANUARY,year));
    	expectedHol.add(new Date(8,MARCH,year));
    	expectedHol.add(new Date(5,APRIL,year));
    	expectedHol.add(new Date(3,MAY,year));
    	expectedHol.add(new Date(10,MAY,year));
    	expectedHol.add(new Date(24,MAY,year));
    	expectedHol.add(new Date(28,JUNE,year));
    	expectedHol.add(new Date(24,AUGUST,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2011 - Future Year
	@Test
    public void testUkraineUSEHolidaysYear2011()
    {
       	final int year = 2011;
    	QL.info("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(3,JANUARY,year));
    	expectedHol.add(new Date(7,JANUARY,year));
    	expectedHol.add(new Date(8,MARCH,year));
    	expectedHol.add(new Date(25,APRIL,year));
    	expectedHol.add(new Date(2,MAY,year));
    	expectedHol.add(new Date(9,MAY,year));
    	expectedHol.add(new Date(13,JUNE,year));
    	expectedHol.add(new Date(28,JUNE,year));
    	expectedHol.add(new Date(24,AUGUST,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

	// 2012 - Future Year
	@Test
    public void testUkraineUSEHolidaysYear2012()
    {
       	final int year = 2012;
    	QL.info("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(2,JANUARY,year));
    	expectedHol.add(new Date(9,JANUARY,year));
    	expectedHol.add(new Date(8,MARCH,year));
    	expectedHol.add(new Date(9,APRIL,year));
    	expectedHol.add(new Date(1,MAY,year));
    	expectedHol.add(new Date(2,MAY,year));
    	expectedHol.add(new Date(9,MAY,year));
    	expectedHol.add(new Date(28,MAY,year));
    	expectedHol.add(new Date(28,JUNE,year));
    	expectedHol.add(new Date(24,AUGUST,year));

    	// Call the Holiday Check
    	final CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);

    }

}
