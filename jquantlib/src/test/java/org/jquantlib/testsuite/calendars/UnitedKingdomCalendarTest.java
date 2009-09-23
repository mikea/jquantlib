/*
Copyright (C) 2008 Praneet Tiwari

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
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.UnitedKingdom;
import org.junit.Test;

/**
 * @author Praneet Tiwari
 *
 */
public class UnitedKingdomCalendarTest {

    private final Calendar metals;
    private final Calendar settlement;
    private final Calendar exchange;

    
    public UnitedKingdomCalendarTest() {
        System.out.println("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
        this.metals = UnitedKingdom.getCalendar(UnitedKingdom.Market.METALS);
        this.settlement = UnitedKingdom.getCalendar(UnitedKingdom.Market.SETTLEMENT);
        this.exchange = UnitedKingdom.getCalendar(UnitedKingdom.Market.LSE);
    }
    

    // 2004 - leap-year in the past
    @Test
    public void testUnitedKingdomMetalsYear2004() {
        int year = 2004;
        System.out.println("Testing " + UnitedKingdom.Market.METALS + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Thursday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(9, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(12, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(3, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(31, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(30, AUGUST, year));
        // Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(27, DECEMBER, year));
        expectedHol.add(new Date(28, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, metals, year);

    }
    
    
    // 2005 - year in the past
    @Test
    public void testUnitedKingdomMetalsYear2005() {
        int year = 2005;
        System.out.println("Testing " + UnitedKingdom.Market.METALS + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Saturday
        expectedHol.add(new Date(3, JANUARY, year));
        // Good Friday
        expectedHol.add(new Date(25, MARCH, year));
        // Easter Monday
        expectedHol.add(new Date(28, MARCH, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(2, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(30, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(29, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));
        expectedHol.add(new Date(27, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, metals, year);

    }
    
    
    // 2006 - year in the past
    @Test
    public void testUnitedKingdomMetalsYear2006() {
        int year = 2006;
        System.out.println("Testing " + UnitedKingdom.Market.METALS + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Sunday
        expectedHol.add(new Date(2, JANUARY, year));
        // Good Friday
        expectedHol.add(new Date(14, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(17, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(1, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(29, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(28, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, metals, year);

    }
    
    
    // 2007 - year in the past
    @Test
    public void testUnitedKingdomMetalsYear2007() {
        int year = 2007;
        System.out.println("Testing " + UnitedKingdom.Market.METALS + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Monday
        expectedHol.add(new Date(1, JANUARY, year));
        // Good Friday
        expectedHol.add(new Date(6, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(9, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(7, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(28, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(27, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, metals, year);

    }
    
    
    // 2008 - present year 
    @Test
    public void testUnitedKingdomMetalsYear2008() {
        int year = 2008;
        System.out.println("Testing " + UnitedKingdom.Market.METALS + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Tuesday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(21, MARCH, year));
        // Easter Monday
        expectedHol.add(new Date(24, MARCH, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(5, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(26, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(25, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, metals, year);

    }
    
    
    // 2009 - future year 
    @Test
    public void testUnitedKingdomMetalsYear2009() {
        int year = 2009;
        System.out.println("Testing " + UnitedKingdom.Market.METALS + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January will be a Thursday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(10, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(13, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(4, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(25, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(31, AUGUST, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // 28 th, a Monday should also be a holiday
        expectedHol.add(new Date(28, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, metals, year);

    }
    
    
    // 2010 - future year 
    @Test
    public void testUnitedKingdomMetalsYear2010() {
        int year = 2010;
        System.out.println("Testing " + UnitedKingdom.Market.METALS + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January will be a Friday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(2, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(5, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(3, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(31, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(30, AUGUST, year));
        // 27 th DEC., a Monday should also be a holiday
        expectedHol.add(new Date(27, DECEMBER, year));
        // 28 th DEC., a Tuesday should also be a holiday
        expectedHol.add(new Date(28, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, metals, year);

    }
    
    
    // test settlement dates now...
    public void testUnitedKingdomSettlementYear2004() {
        int year = 2004;
        System.out.println("Testing " + UnitedKingdom.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        // Let's check the first weekend
        expectedHol.add(new Date(2, JANUARY, year));
        expectedHol.add(new Date(3, JANUARY, year));
        // Check another weekend, incidentally V Day ;-)
        expectedHol.add(new Date(14, FEBRUARY, year));
        //Good Friday
        expectedHol.add(new Date(9, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(12, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(3, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(31, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(30, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));
        expectedHol.add(new Date(27, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);

    }
    
    
    // 2005 - year in the past
    @Test
    public void testUnitedKingdomSettlementYear2005() {
        int year = 2005;
        System.out.println("Testing " + UnitedKingdom.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Saturday
        expectedHol.add(new Date(3, JANUARY, year));
        // Good Friday
        expectedHol.add(new Date(25, MARCH, year));
        // Easter Monday
        expectedHol.add(new Date(28, MARCH, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(2, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(30, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(29, AUGUST, year));
        expectedHol.add(new Date(26, DECEMBER, year));
        expectedHol.add(new Date(27, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);

    }
    
    
    // 2006 - year in the past
    @Test
    public void testUnitedKingdomSettlementYear2006() {
        int year = 2006;
        System.out.println("Testing " + UnitedKingdom.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Sunday
        expectedHol.add(new Date(2, JANUARY, year));
        // Good Friday
        expectedHol.add(new Date(14, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(17, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(1, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(29, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(28, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);

    }
    
    
    // 2007 - year in the past
    @Test
    public void testUnitedKingdomSettlementYear2007() {
        int year = 2007;
        System.out.println("Testing " + UnitedKingdom.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Monday
        expectedHol.add(new Date(1, JANUARY, year));
        // Good Friday
        expectedHol.add(new Date(6, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(9, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(7, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(28, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(27, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));


        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);

    }
    
    
    // 2008 - present year 
    @Test
    public void testUnitedKingdomSettlementYear2008() {
        int year = 2008;
        System.out.println("Testing " + UnitedKingdom.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Tuesday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(21, MARCH, year));
        // Easter Monday
        expectedHol.add(new Date(24, MARCH, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(5, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(26, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(25, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);

    }
    
    
    // 2009 - future year 
    @Test
    public void testUnitedKingdomSettlementYear2009() {
        int year = 2009;
        System.out.println("Testing " + UnitedKingdom.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January will be a Thursday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(10, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(13, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(4, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(25, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(31, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // 28 th, a Monday should also be a holiday
        expectedHol.add(new Date(28, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);

    }
    
    
    // 2010 - future year 
    @Test
    public void testUnitedKingdomSettlementYear2010() {
        int year = 2010;
        System.out.println("Testing " + UnitedKingdom.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January will be a Friday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(2, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(5, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(3, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(31, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(30, AUGUST, year));
        expectedHol.add(new Date(27, DECEMBER, year));
        // 28 th, a Tuesday should also be a holiday
        expectedHol.add(new Date(28, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);

    }
    
    
    //test exchange dates...
    public void testUnitedKingdomExchangeYear2004() {
        int year = 2004;
        System.out.println("Testing " + UnitedKingdom.Market.LSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Thursday
        expectedHol.add(new Date(1, JANUARY, year));
        // Let's check the first weekend
        expectedHol.add(new Date(2, JANUARY, year));
        expectedHol.add(new Date(3, JANUARY, year));
        // Check another weekend, incidentally V Day ;-)
        expectedHol.add(new Date(14, FEBRUARY, year));
        //Good Friday
        expectedHol.add(new Date(9, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(12, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(3, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(31, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(30, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));
        expectedHol.add(new Date(27, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }
    
    
    // 2005 - year in the past
    @Test
    public void testUnitedKingdomExchangeYear2005() {
        int year = 2005;
        System.out.println("Testing " + UnitedKingdom.Market.LSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Saturday
        expectedHol.add(new Date(3, JANUARY, year));
        // Good Friday
        expectedHol.add(new Date(25, MARCH, year));
        // Easter Monday
        expectedHol.add(new Date(28, MARCH, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(2, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(30, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(29, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        //	expectedHol.add(new Date(25,DECEMBER,year));
        expectedHol.add(new Date(26, DECEMBER, year));
        expectedHol.add(new Date(27, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }
    
    
    // 2006 - year in the past
    @Test
    public void testUnitedKingdomExchangeYear2006() {
        int year = 2006;
        System.out.println("Testing " + UnitedKingdom.Market.LSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Sunday
        expectedHol.add(new Date(2, JANUARY, year));
        // Good Friday
        expectedHol.add(new Date(14, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(17, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(1, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(29, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(28, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }
    
    
    // 2007 - year in the past
    @Test
    public void testUnitedKingdomExchangeYear2007() {
        int year = 2007;
        System.out.println("Testing " + UnitedKingdom.Market.LSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Monday
        expectedHol.add(new Date(1, JANUARY, year));
        // Good Friday
        expectedHol.add(new Date(6, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(9, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(7, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(28, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(27, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }
    
    
    // 2008 - present year 
    @Test
    public void testUnitedKingdomExchangeYear2008() {
        int year = 2008;
        System.out.println("Testing " + UnitedKingdom.Market.LSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January was a Tuesday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(21, MARCH, year));
        // Easter Monday
        expectedHol.add(new Date(24, MARCH, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(5, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(26, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(25, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // Boxing Day, December 26th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }
    
    
    // 2009 - future year 
    @Test
    public void testUnitedKingdomExchangeYear2009() {
        int year = 2009;
        System.out.println("Testing " + UnitedKingdom.Market.LSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January will be a Thursday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(10, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(13, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(4, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(25, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(31, AUGUST, year));
        //Christmas Day, December 25th (possibly moved to Monday or Tuesday)
        expectedHol.add(new Date(25, DECEMBER, year));
        // 28 th, a Monday should also be a holiday
        expectedHol.add(new Date(28, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }
    
    
    // 2010 - future year 
    @Test
    public void testUnitedKingdomExchangeYear2010() {
        int year = 2010;
        System.out.println("Testing " + UnitedKingdom.Market.LSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

        // First January will be a Friday
        expectedHol.add(new Date(1, JANUARY, year));
        //Good Friday
        expectedHol.add(new Date(2, APRIL, year));
        // Easter Monday
        expectedHol.add(new Date(5, APRIL, year));
        // May Bank holiday, first Monday of May
        expectedHol.add(new Date(3, MAY, year));
        // Spring Bank Holiday, last Monday of May
        expectedHol.add(new Date(31, MAY, year));
        // Summer Bank Holiday, last Monday of August
        expectedHol.add(new Date(30, AUGUST, year));
        // 27 th, a Monday should also be a holiday
        expectedHol.add(new Date(27, DECEMBER, year));
        // 28 th, a Tuesday should also be a holiday
        expectedHol.add(new Date(28, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }

}
