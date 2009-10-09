/*
 Copyright (C) 2008 Tim Swetonic

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
import static org.jquantlib.time.Month.OCTOBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Australia;
import org.junit.Test;

/**
 * @author Tim Swetonic
 * @author Jia Jia
 *
 */


public class AustraliaCalendarTest {

	private final Calendar exchange;

	public AustraliaCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	    exchange = new Australia();//.getCalendar(Australia.Market.SETTLEMENT);
	}

    // 2008 - current year
    @Test
	public void testAustraliaYear2008() {
      	final int year = 2008;
      	QL.info("Testing Australia holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

    	expectedHol.add(new Date(1,JANUARY,year));
    	expectedHol.add(new Date(28,JANUARY,year));

    	//good friday
    	expectedHol.add(new Date(21,MARCH,year));
    	//easter monday
    	expectedHol.add(new Date(24,MARCH,year));

    	expectedHol.add(new Date(25,APRIL,year));
    	expectedHol.add(new Date(9,JUNE,year));
    	expectedHol.add(new Date(4,AUGUST,year));
        expectedHol.add(new Date(6,OCTOBER,year));
        expectedHol.add(new Date(25,DECEMBER,year));
        expectedHol.add(new Date(26,DECEMBER,year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testAustraliaYear2004() {
        final int year = 2004;
        QL.info("Testing Australia holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1,JANUARY,year));
        expectedHol.add(new Date(26,JANUARY,year));
        expectedHol.add(new Date(9,APRIL,year));
        expectedHol.add(new Date(12,APRIL,year));
        expectedHol.add(new Date(26,APRIL,year));
        expectedHol.add(new Date(14,JUNE,year));
        expectedHol.add(new Date(2,AUGUST,year));
        expectedHol.add(new Date(4,OCTOBER,year));
        expectedHol.add(new Date(27,DECEMBER,year));
        expectedHol.add(new Date(28,DECEMBER,year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testAustraliaYear2005() {
        final int year = 2005;
        QL.info("Testing Australia holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(26,JANUARY,year));
        expectedHol.add(new Date(25,MARCH,year));

        //good friday
        expectedHol.add(new Date(28,MARCH,year));

        expectedHol.add(new Date(25,APRIL,year));
        expectedHol.add(new Date(13,JUNE,year));
        expectedHol.add(new Date(1,AUGUST,year));
        expectedHol.add(new Date(3,OCTOBER,year));
        expectedHol.add(new Date(26,DECEMBER,year));
        expectedHol.add(new Date(27,DECEMBER,year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testAustraliaYear2006() {
        final int year = 2006;
        QL.info("Testing Australia holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(26,JANUARY,year));
        expectedHol.add(new Date(14,APRIL,year));
        expectedHol.add(new Date(17,APRIL,year));
        expectedHol.add(new Date(25,APRIL,year));
        expectedHol.add(new Date(12,JUNE,year));
        expectedHol.add(new Date(7,AUGUST,year));
        expectedHol.add(new Date(2,OCTOBER,year));
        expectedHol.add(new Date(25,DECEMBER,year));
        expectedHol.add(new Date(26,DECEMBER,year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testAustraliaYear2007() {
        final int year = 2007;
        QL.info("Testing Australia holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1,JANUARY,year));
        expectedHol.add(new Date(26,JANUARY,year));

        //good friday
        expectedHol.add(new Date(6,APRIL,year));
        expectedHol.add(new Date(9,APRIL,year));
        expectedHol.add(new Date(25,APRIL,year));
        expectedHol.add(new Date(11,JUNE,year));
        expectedHol.add(new Date(6,AUGUST,year));
        expectedHol.add(new Date(1,OCTOBER,year));
        expectedHol.add(new Date(25,DECEMBER,year));
        expectedHol.add(new Date(26,DECEMBER,year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testAustraliaYear2009() {
        final int year = 2009;
        QL.info("Testing Australia holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1,JANUARY,year));
        expectedHol.add(new Date(26,JANUARY,year));
        expectedHol.add(new Date(10,APRIL,year));
        expectedHol.add(new Date(13,APRIL,year));
        expectedHol.add(new Date(8,JUNE,year));
        expectedHol.add(new Date(3,AUGUST,year));
        expectedHol.add(new Date(5,OCTOBER,year));
        expectedHol.add(new Date(25,DECEMBER,year));
        expectedHol.add(new Date(28,DECEMBER,year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testAustraliaYear2010() {
        final int year = 2010;
        QL.info("Testing Australia holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1,JANUARY,year));
        expectedHol.add(new Date(26,JANUARY,year));
        expectedHol.add(new Date(2,APRIL,year));
        expectedHol.add(new Date(5,APRIL,year));
        expectedHol.add(new Date(26,APRIL,year));
        expectedHol.add(new Date(14,JUNE,year));
        expectedHol.add(new Date(2,AUGUST,year));
        expectedHol.add(new Date(4,OCTOBER,year));
        expectedHol.add(new Date(27,DECEMBER,year));
        expectedHol.add(new Date(28,DECEMBER,year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testAustraliaYear2011() {
        final int year = 2011;
        QL.info("Testing Australia holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(26,JANUARY,year));
        expectedHol.add(new Date(22,APRIL,year));
        expectedHol.add(new Date(25,APRIL,year));
        expectedHol.add(new Date(13,JUNE,year));
        expectedHol.add(new Date(1,AUGUST,year));
        expectedHol.add(new Date(3,OCTOBER,year));
        expectedHol.add(new Date(26,DECEMBER,year));
        expectedHol.add(new Date(27,DECEMBER,year));


        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testAustraliaYear2012() {
        final int year = 2012;
        QL.info("Testing Australia holiday list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(26,JANUARY,year));
        expectedHol.add(new Date(6,APRIL,year));
        expectedHol.add(new Date(9,APRIL,year));
        expectedHol.add(new Date(25,APRIL,year));
        expectedHol.add(new Date(11,JUNE,year));
        expectedHol.add(new Date(6,AUGUST,year));
        expectedHol.add(new Date(1,OCTOBER,year));
        expectedHol.add(new Date(25,DECEMBER,year));
        expectedHol.add(new Date(26,DECEMBER,year));

        // Call the Holiday Check
        final CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

}

