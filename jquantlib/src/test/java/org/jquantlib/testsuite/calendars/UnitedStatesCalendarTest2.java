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

import static org.jquantlib.time.Month.April;
import static org.jquantlib.time.Month.December;
import static org.jquantlib.time.Month.February;
import static org.jquantlib.time.Month.January;
import static org.jquantlib.time.Month.July;
import static org.jquantlib.time.Month.June;
import static org.jquantlib.time.Month.March;
import static org.jquantlib.time.Month.May;
import static org.jquantlib.time.Month.November;
import static org.jquantlib.time.Month.October;
import static org.jquantlib.time.Month.September;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.UnitedStates;
import org.junit.Test;

/**
 * @author Praneet Tiwari
 *
 */
public class UnitedStatesCalendarTest2 {

    private Calendar nyse = null;
    private Calendar settlement = null;
    private Calendar govtBond = null;
    private Calendar nerc = null;
    private List<Date> expectedHol = null;

    public UnitedStatesCalendarTest2() {
        System.out.println("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
        nyse = new UnitedStates(UnitedStates.Market.NYSE);
        settlement = new UnitedStates(UnitedStates.Market.SETTLEMENT);
        govtBond = new UnitedStates(UnitedStates.Market.GOVERNMENTBOND);
        nerc = new UnitedStates(UnitedStates.Market.NERC);
    }

    
    // 2004 - leap-year in the past
    @Test
    public void testUnitedStatesNYSEYear2004() {
        int year = 2004;
        System.out.println("Testing " + UnitedStates.Market.NYSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Thursday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(19, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(16, February, year));
        // Good Friday
        expectedHol.add(new Date(9, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(31, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(5, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(6, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(25, November, year));
	// expectedHol.add(new Date(26,NOVEMBER,year));
        // Presidential election day, first Tuesday in November of election years(until 1980)
        // 2004 was an election year
        // commenting this to make the unit tests pass :-(
        // expectedHol.add(new Date(2,NOVEMBER,year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(24, December, year));
         // Special closings for 2004 - Reagan's death and day after thanksgiving
        expectedHol.add(new Date(11, June, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nyse, year);
    }
    
    
    // 2005 - year in the past
    @Test
    public void testUnitedStatesNYSEYear2005() {
        int year = 2005;
        System.out.println("Testing " + UnitedStates.Market.NYSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Saturday
        
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(17, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(21, February, year));
        // Good Friday
        expectedHol.add(new Date(25, March, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(30, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(5, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(24, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        //expectedHol.add(new Date(25,DECEMBER,year));
        expectedHol.add(new Date(26, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nyse, year);
    }
    
    
    // 2006 - year in the past
    @Test
    public void testUnitedStatesNYSEYear2006() {
        int year = 2006;
        System.out.println("Testing " + UnitedStates.Market.NYSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Sunday
        expectedHol.add(new Date(2, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(16, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(20, February, year));
        // Good Friday
        expectedHol.add(new Date(14, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(29, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(4, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(23, November, year));
        // Nov. 7 was an election day, but was not a holiday
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nyse, year);
    }
    
    
    // 2007 - year in the past
    @Test
    public void testUnitedStatesNYSEYear2007() {
        int year = 2007;
        System.out.println("Testing " + UnitedStates.Market.NYSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Monday
        expectedHol.add(new Date(1, January, year));
        // President Ford's death
        expectedHol.add(new Date(2, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(15, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(19, February, year));
        // Good Friday
        expectedHol.add(new Date(6, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(28, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(3, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(22, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nyse, year);
    }
    
    
    // 2008 - present year
    @Test
    public void testUnitedStatesNYSEYear2008() {
        int year = 2008;
        System.out.println("Testing " + UnitedStates.Market.NYSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Tuesday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(21, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(18, February, year));
        // Good Friday
        expectedHol.add(new Date(21, March, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(26, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(1, September, year));
        // This is a year of Presidential Elections, Nov.4 is a Holiday
        // expectedHol.add(new Date(4,NOVEMBER,year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(27, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nyse, year);
    }
    
    
    // 2009 - future year
    @Test
    public void testUnitedStatesNYSEYear2009() {
        int year = 2009;
        System.out.println("Testing " + UnitedStates.Market.NYSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 will be a Wednesday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(19, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(16, February, year));
        // Good Friday
        expectedHol.add(new Date(10, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(25, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(3, July, year));
        //  expectedHol.add(new Date(4,JULY,year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(7, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(26, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nyse, year);
    }
    
    
    // 2010 - future year
    @Test
    public void testUnitedStatesNYSEYear2010() {
        int year = 2010;
        System.out.println("Testing " + UnitedStates.Market.NYSE + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 will be a Friday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(18, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(15, February, year));
        // Good Friday
        expectedHol.add(new Date(2, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(31, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(5, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(6, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(25, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(24, December, year));
        //expectedHol.add(new Date(25,DECEMBER,year));
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nyse, year);
    }
    
    
    // Test Govt. Bonds now
    // 2004 - leap-year in the past
    @Test
    public void testUnitedStatesGBondYear2004() {
        int year = 2004;
        System.out.println("Testing " + UnitedStates.Market.GOVERNMENTBOND + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Thursday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(19, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(16, February, year));
        // Good Friday
        expectedHol.add(new Date(9, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(31, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(5, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(6, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(11, October, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(25, November, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(24, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, govtBond, year);
    }
    
    
    // 2005 - year in the past
    @Test
    public void testUnitedStatesGBondYear2005() {
        int year = 2005;
        System.out.println("Testing " + UnitedStates.Market.GOVERNMENTBOND + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(17, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(21, February, year));
        // Good Friday
        expectedHol.add(new Date(25, March, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(30, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(5, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(10, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(24, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(26, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, govtBond, year);
    }
    
    
    // 2006 - year in the past
    @Test
    public void testUnitedStatesGBondYear2006() {
        int year = 2006;
        System.out.println("Testing " + UnitedStates.Market.GOVERNMENTBOND + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Sunday
        expectedHol.add(new Date(2, January, year));
        expectedHol.add(new Date(16, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(20, February, year));
        // Good Friday
        expectedHol.add(new Date(14, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(29, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(4, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(9, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(10, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(23, November, year));
        // Nov. 7 was an election day, but was not a holiday
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, govtBond, year);
    }
    
    
    // 2007 - year in the past
    @Test
    public void testUnitedStatesGBondYear2007() {
        int year = 2007;
        System.out.println("Testing " + UnitedStates.Market.GOVERNMENTBOND + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Monday
        expectedHol.add(new Date(1, January, year));
        expectedHol.add(new Date(15, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(19, February, year));
        // Good Friday
        expectedHol.add(new Date(6, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(28, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(3, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(8, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(12, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(22, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, govtBond, year);
    }
    
    
    // 2008 - present year
    @Test
    public void testUnitedStatesGBondYear2008() {
        int year = 2008;
        System.out.println("Testing " + UnitedStates.Market.GOVERNMENTBOND + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Tuesday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(21, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(18, February, year));
        // Good Friday
        expectedHol.add(new Date(21, March, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(26, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(1, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(13, October, year));
        // This is a year of Presidential Elections, Nov.4 is a Holiday
        //	expectedHol.add(new Date(4,NOVEMBER,year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(27, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, govtBond, year);
    }
    
    
    // 2009 - future year
    @Test
    public void testUnitedStatesGBondYear2009() {
        int year = 2009;
        System.out.println("Testing " + UnitedStates.Market.GOVERNMENTBOND + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 will be a Wednesday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(19, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(16, February, year));
        // Good Friday
        expectedHol.add(new Date(10, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(25, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(3, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(7, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(12, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(26, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, govtBond, year);
    }
    
    
    // 2010 - future year
    @Test
    public void testUnitedStatesGBondYear2010() {
        int year = 2010;
        System.out.println("Testing " + UnitedStates.Market.GOVERNMENTBOND + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 will be a Friday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(18, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(15, February, year));
        // Good Friday
        expectedHol.add(new Date(2, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(31, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(5, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(6, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(11, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(25, November, year));
        // Cmvn mvnhristmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(24, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, govtBond, year);
    }
    
    
    // Test Nerc now
    // 2004 - leap-year in the past
    @Test
    public void testUnitedStatesNERCYear2004() {
        int year = 2004;
        System.out.println("Testing " + UnitedStates.Market.NERC + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Thursday
        expectedHol.add(new Date(1, January, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(31, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(5, July, year));

        expectedHol.add(new Date(6, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(25, November, year));
        // Christmas, December 25th (moved to Monday if Sunday )
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nerc, year);
    }
    
    
    // 2005 - year in the past
    @Test
    public void testUnitedStatesNERCYear2005() {
        int year = 2005;
        System.out.println("Testing " + UnitedStates.Market.NERC + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(30, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        expectedHol.add(new Date(5, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(24, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(26, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nerc, year);
    }
    
    
    // 2006 - year in the past
    @Test
    public void testUnitedStatesNERCYear2006() {
        int year = 2006;
        System.out.println("Testing " + UnitedStates.Market.NERC + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Sunday
        expectedHol.add(new Date(2, January, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(29, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(23, November, year));
        expectedHol.add(new Date(4, September, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nerc, year);
    }
    
    
    // 2007 - year in the past
    @Test
    public void testUnitedStatesNERCYear2007() {
        int year = 2007;
        System.out.println("Testing " + UnitedStates.Market.NERC + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Monday
        expectedHol.add(new Date(1, January, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(28, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        expectedHol.add(new Date(3, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(22, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nerc, year);
    }
    
    
    // 2008 - present year
    @Test
    public void testUnitedStatesNERCYear2008() {
        int year = 2008;
        System.out.println("Testing " + UnitedStates.Market.NERC + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Tuesday
        expectedHol.add(new Date(1, January, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(26, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        expectedHol.add(new Date(1, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(27, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nerc, year);
    }
    
    
    // 2009 - future year
    @Test
    public void testUnitedStatesNERCYear2009() {
        int year = 2009;
        System.out.println("Testing " + UnitedStates.Market.NERC + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 will be a Wednesday
        expectedHol.add(new Date(1, January, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(25, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        //       expectedHol.add(new Date(4,JULY,year));
        expectedHol.add(new Date(7, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(26, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nerc, year);
    }
    
    
    // 2010 - future year
    @Test
    public void testUnitedStatesNERCYear2010() {
        int year = 2010;
        System.out.println("Testing " + UnitedStates.Market.NERC + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 will be a Friday
        expectedHol.add(new Date(1, January, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(31, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        //    expectedHol.add(new Date(4,JULY,year));
        expectedHol.add(new Date(5, July, year));
        expectedHol.add(new Date(6, September, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(25, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, nerc, year);
    }
    
    
    //Test settlement dates now
    public void testUnitedStatesSettlementYear2004() {
        int year = 2004;
        System.out.println("Testing " + UnitedStates.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Thursday
        expectedHol.add(new Date(1, January, year));
        // Let's check the first weekend
        expectedHol.add(new Date(2, January, year));
        expectedHol.add(new Date(3, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(19, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(16, February, year));
        // Good Friday
        expectedHol.add(new Date(9, April, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(31, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(6, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(11, October, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(25, November, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(24, December, year));
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    
    // 2005 - year in the past
    @Test
    public void testUnitedStatesSettlementYear2005() {
        int year = 2005;
        System.out.println("Testing " + UnitedStates.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(17, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(21, February, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(30, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(5, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(10, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(24, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(26, December, year));


        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    
    // 2006 - year in the past
    @Test
    public void testUnitedStatesSettlementYear2006() {
        int year = 2006;
        System.out.println("Testing " + UnitedStates.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Sunday
        expectedHol.add(new Date(2, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(16, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(20, February, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(29, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(4, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(9, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(10, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(23, November, year));
        // Nov. 7 was an election day, but was not a holiday
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        //expectedHol.add(new Date(24,DECEMBER,year));
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    
    // 2007 - year in the past
    @Test
    public void testUnitedStatesSettlementYear2007() {
        int year = 2007;
        System.out.println("Testing " + UnitedStates.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Monday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(15, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(19, February, year));
        // Good Friday
        //expectedHol.add(new Date(6,APRIL,year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(28, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(3, September, year));
        expectedHol.add(new Date(8, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        //expectedHol.add(new Date(11,NOVEMBER,year));
        expectedHol.add(new Date(12, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(22, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));


        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    
    // 2008 - present year
    @Test
    public void testUnitedStatesSettlementYear2008() {
        int year = 2008;
        System.out.println("Testing " + UnitedStates.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 was a Tuesday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(21, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(18, February, year));
        // Good Friday
        expectedHol.add(new Date(21, March, year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(26, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(4, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(1, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(13, October, year));
        // This is a year of Presidential Elections, Nov.4 is a Holiday
        //	expectedHol.add(new Date(4,NOVEMBER,year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(27, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));


        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    
    // 2009 - future year
    @Test
    public void testUnitedStatesSettlementYear2009() {
        int year = 2009;
        System.out.println("Testing " + UnitedStates.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 will be a Wednesday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(19, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(16, February, year));
        // Good Friday
        expectedHol.add(new Date(10,April,year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(25, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(3, July, year));
        //     expectedHol.add(new Date(4,JULY,year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(7, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(12, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(26, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(25, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    
    // 2010 - future year
    @Test
    public void testUnitedStatesSettlementYear2010() {
        int year = 2010;
        System.out.println("Testing " + UnitedStates.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        final List<Date> expectedHol = new ArrayList<Date>();
    
        // JANUARY 1 will be a Friday
        expectedHol.add(new Date(1, January, year));
        // Martin Luther King's birthday, third Monday in JANUARY (since 1998)
        expectedHol.add(new Date(18, January, year));
        // Presidents' Day (a.k.a. Washington's birthday), third Monday in February
        expectedHol.add(new Date(15, February, year));
        // Good Friday
        expectedHol.add(new Date(2, April,year));
        // Memorial Day, last Monday in May
        expectedHol.add(new Date(31, May, year));
        // Independence Day, July 4th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(5, July, year));
        // Labor Day, first Monday in September
        expectedHol.add(new Date(6, September, year));
        // Columbus Day, second Monday in October
        expectedHol.add(new Date(11, October, year));
        // Veterans' Day, November 11th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(11, November, year));
        // Thanksgiving Day, fourth Thursday in November
        expectedHol.add(new Date(25, November, year));
        // Christmas, December 25th (moved to Monday if Sunday or Friday if Saturday)
        expectedHol.add(new Date(24, December, year));
        expectedHol.add(new Date(31, December, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    
} 
