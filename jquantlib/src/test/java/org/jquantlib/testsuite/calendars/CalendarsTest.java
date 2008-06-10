/*
 Copyright (C) 2008 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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

import static org.jquantlib.time.calendars.JointCalendar.JointCalendarRule.JOIN_BUSINESSDAYS;
import static org.jquantlib.time.calendars.JointCalendar.JointCalendarRule.JOIN_HOLIDAYS;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Brazil;
import org.jquantlib.time.calendars.Germany;
import org.jquantlib.time.calendars.Italy;
import org.jquantlib.time.calendars.Japan;
import org.jquantlib.time.calendars.JointCalendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.time.calendars.UnitedKingdom;
import org.jquantlib.time.calendars.UnitedStates;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 *
 */
public class CalendarsTest {
    
	public CalendarsTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	
    @Test
    public void testModifiedCalendars() {

        System.out.println("Testing calendar modification...");

        Calendar c1 = Target.getCalendar();
        Calendar c2 = UnitedStates.getCalendar(UnitedStates.Market.NYSE);
        Date d1 = DateFactory.getFactory().getDate(1,Month.MAY,2004);      // holiday for both calendars
        Date d2 = DateFactory.getFactory().getDate(26,Month.APRIL,2004);   // business day

        if(!c1.isHoliday(d1))
            System.out.println("wrong assumption---correct the test");
        if(!c1.isBusinessDay(d2))
            System.out.println("wrong assumption---correct the test");

        if(!c2.isHoliday(d1))
            System.out.println("wrong assumption---correct the test");
        if(!c2.isBusinessDay(d2))
            System.out.println("wrong assumption---correct the test");
        /*
        // modify the TARGET calendar
        //TODO:        
        //c1.removeHoliday(d1);
        //c1.addHoliday(d2);

        // test
        if (c1.isHoliday(d1))
            throw new IllegalStateException(d1 + " still a holiday for original TARGET instance");
        if (c1.isBusinessDay(d2))
            throw new IllegalStateException(d2 + " still a business day for original TARGET instance");

        // any instance of TARGET should be modified...
        Calendar c3 = Target.getCalendar();
        if (c3.isHoliday(d1))
            throw new IllegalStateException(d1 + " still a holiday for generic TARGET instance");
        if (c3.isBusinessDay(d2))
            throw new IllegalStateException(d2 + " still a business day for generic TARGET instance");

        // ...but not other calendars
        if (c2.isBusinessDay(d1))
            throw new IllegalStateException(d1 + " business day for New York");
        if (c2.isHoliday(d2))
            throw new IllegalStateException(d2 + " holiday for New York");

        // restore original holiday set---test the other way around
        //c3.addHoliday(d1);
        //c3.removeHoliday(d2);

        if (c1.isBusinessDay(d1))
            throw new IllegalStateException(d1 + " still a business day");
        if (c1.isHoliday(d2))
            throw new IllegalStateException(d2 + " still a holiday"); */
    }


    @Test
    public void testJointCalendars() {

        System.out.println("Testing joint calendars...");

        Calendar c1 = Target.getCalendar(),
                 c2 = UnitedKingdom.getCalendar(UnitedKingdom.Market.SETTLEMENT),
                 c3 = UnitedStates.getCalendar(UnitedStates.Market.NYSE),
                 c4 = Japan.getCalendar();

        Calendar c12h = new JointCalendar(JOIN_HOLIDAYS,c1,c2),
                 c12b = new JointCalendar(JOIN_BUSINESSDAYS,c1,c2),
                 c123h = new JointCalendar(JOIN_HOLIDAYS,c1,c2,c3),
                 c123b = new JointCalendar(JOIN_BUSINESSDAYS,c1,c2,c3),
                 c1234h = new JointCalendar(JOIN_HOLIDAYS,c1,c2,c3,c4),
                 c1234b = new JointCalendar(JOIN_BUSINESSDAYS,c1,c2,c3,c4);

        // test one year, starting today
        Date firstDate = DateFactory.getFactory().getTodaysDate();
        Date endDate =  DateFactory.getFactory().getTodaysDate();
        endDate.adjust(new Period(1, TimeUnit.YEARS));
        
        for (Date d = firstDate; d.le(endDate); d.increment()) {
            boolean b1 = c1.isBusinessDay(d),
                 b2 = c2.isBusinessDay(d),
                 b3 = c3.isBusinessDay(d),
                 b4 = c4.isBusinessDay(d);

            if ((b1 && b2) != c12h.isBusinessDay(d))
                throw new IllegalStateException("At date " + d + ":\n"
                           + "    inconsistency between joint calendar "
                           + c12h.getName() + " (joining holidays)\n"
                           + "    and its components");

            if ((b1 || b2) != c12b.isBusinessDay(d))
                throw new IllegalStateException("At date " + d + ":\n"
                           + "    inconsistency between joint calendar "
                           + c12b.getName() + " (joining business days)\n"
                           + "    and its components");

            if ((b1 && b2 && b3) != c123h.isBusinessDay(d))
                throw new IllegalStateException("At date " + d + ":\n"
                           + "    inconsistency between joint calendar "
                           + c123h.getName() + " (joining holidays)\n"
                           + "    and its components");

            if ((b1 || b2 || b3) != c123b.isBusinessDay(d))
                throw new IllegalStateException("At date " + d + ":\n"
                           + "    inconsistency between joint calendar "
                           + c123b.getName() + " (joining business days)\n"
                           + "    and its components");

            if ((b1 && b2 && b3 && b4) != c1234h.isBusinessDay(d))
                throw new IllegalStateException("At date " + d + ":\n"
                           + "    inconsistency between joint calendar "
                           + c1234h.getName() + " (joining holidays)\n"
                           + "    and its components");

            if ((b1 || b2 || b3 || b4) != c1234b.isBusinessDay(d))
                throw new IllegalStateException("At date " + d + ":\n"
                           + "    inconsistency between joint calendar "
                           + c1234b.getName() + " (joining business days)\n"
                           + "    and its components");

        }
    }

    @Test
    public void testUSSettlement() {
        System.out.println("Testing US settlement holiday list...");

        List<Date> expectedHol = new ArrayList<Date>();
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(19,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(16,FEBRUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,MAY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(5,JULY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(6,SEPTEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(25,NOVEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2004));

        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(17,JANUARY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(21,FEBRUARY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(30,MAY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(4,JULY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(5,SEPTEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(10,OCTOBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(24,NOVEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2005));

        Calendar c = UnitedStates.getCalendar(UnitedStates.Market.SETTLEMENT);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate( 1, JANUARY, 2004),
                                                         DateFactory.getFactory().getDate(31,DECEMBER, 2005),false);
        for (int i=0; i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }

    @Test
    public void testUSGovernmentBondMarket() {
        System.out.println("Testing US government bond market holiday list...");

        List<Date> expectedHol = new ArrayList<Date>();
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(19,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(16,FEBRUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,MAY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(5,JULY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(6,SEPTEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(25,NOVEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2004));

        Calendar c = UnitedStates.getCalendar(UnitedStates.Market.GOVERNMENTBOND);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2004),
                                               DateFactory.getFactory().getDate(31,DECEMBER,2004), false);

        for (int i=0; i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }

    @Test
    public void testUSNewYorkStockExchange() {
        System.out.println("Testing New York Stock Exchange holiday list...");

        List<Date> expectedHol = new Vector<Date>();
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(19,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(16,FEBRUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,MAY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,2004));
        expectedHol.add(DateFactory.getFactory().getDate(5,JULY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(6,SEPTEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(25,NOVEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2004));

        expectedHol.add(DateFactory.getFactory().getDate(17,JANUARY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(21,FEBRUARY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(30,MAY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(4,JULY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(5,SEPTEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(24,NOVEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2005));

        expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(16,JANUARY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(20,FEBRUARY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(29,MAY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(4,JULY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(4,SEPTEMBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(23,NOVEMBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2006));

        Calendar c = UnitedStates.getCalendar(UnitedStates.Market.NYSE);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2004),
                                               DateFactory.getFactory().getDate(31,DECEMBER,2006),false);

        int i;
        for (i=0; i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");

        List<Date> histClose = new Vector<Date>(0);
        histClose.add(DateFactory.getFactory().getDate(11,JUNE,2004));     // Reagan's funeral
        histClose.add(DateFactory.getFactory().getDate(14,SEPTEMBER,2001));// SEPTEMBER 11, 2001
        histClose.add(DateFactory.getFactory().getDate(13,SEPTEMBER,2001));// SEPTEMBER 11, 2001
        histClose.add(DateFactory.getFactory().getDate(12,SEPTEMBER,2001));// SEPTEMBER 11, 2001
        histClose.add(DateFactory.getFactory().getDate(11,SEPTEMBER,2001));// SEPTEMBER 11, 2001
        histClose.add(DateFactory.getFactory().getDate(14,JULY,1977));     // 1977 Blackout
        histClose.add(DateFactory.getFactory().getDate(25,JANUARY,1973));  // Johnson's funeral.
        histClose.add(DateFactory.getFactory().getDate(28,DECEMBER,1972)); // Truman's funeral
        histClose.add(DateFactory.getFactory().getDate(21,JULY,1969));     // Lunar exploration nat. day
        histClose.add(DateFactory.getFactory().getDate(31,MARCH,1969));    // Eisenhower's funeral
        histClose.add(DateFactory.getFactory().getDate(10,FEBRUARY,1969)); // heavy snow
        histClose.add(DateFactory.getFactory().getDate(5,JULY,1968));      // Day after Independence Day
        // JUNE 12-Dec. 31, 1968
        // Four day week (closed on Wednesdays) - Paperwork Crisis
        histClose.add(DateFactory.getFactory().getDate(12,JUNE,1968));
        histClose.add(DateFactory.getFactory().getDate(19,JUNE,1968));
        histClose.add(DateFactory.getFactory().getDate(26,JUNE,1968));
        histClose.add(DateFactory.getFactory().getDate(3,JULY,1968 ));
        histClose.add(DateFactory.getFactory().getDate(10,JULY,1968));
        histClose.add(DateFactory.getFactory().getDate(17,JULY,1968));
        histClose.add(DateFactory.getFactory().getDate(20,NOVEMBER,1968));
        histClose.add(DateFactory.getFactory().getDate(27,NOVEMBER,1968));
        histClose.add(DateFactory.getFactory().getDate(4,DECEMBER,1968 ));
        histClose.add(DateFactory.getFactory().getDate(11,DECEMBER,1968));
        histClose.add(DateFactory.getFactory().getDate(18,DECEMBER,1968));
        // Presidential election days
        histClose.add(DateFactory.getFactory().getDate(4,NOVEMBER,1980));
        histClose.add(DateFactory.getFactory().getDate(2,NOVEMBER,1976));
        histClose.add(DateFactory.getFactory().getDate(7,NOVEMBER,1972));
        histClose.add(DateFactory.getFactory().getDate(5,NOVEMBER,1968));
        histClose.add(DateFactory.getFactory().getDate(3,NOVEMBER,1964));
        for (i=0; i<histClose.size(); i++) {
            if (!c.isHoliday(histClose.get(i)))
                throw new IllegalStateException(histClose.get(i)
                           + " should be holiday (historical close)");
        }


    }

    @Test
    public void testTARGET() {
        System.out.println("Testing TARGET holiday list...");

        List<Date> expectedHol = new Vector<Date>();
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,1999));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,1999));

        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,2000));
        expectedHol.add(DateFactory.getFactory().getDate(24,APRIL,2000));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2000));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2000));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2000));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2001));
        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,2001));
        expectedHol.add(DateFactory.getFactory().getDate(16,APRIL,2001));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2001));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2001));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2001));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2001));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2002));
        expectedHol.add(DateFactory.getFactory().getDate(29,MARCH,2002));
        expectedHol.add(DateFactory.getFactory().getDate(1,APRIL,2002));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2002));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2002));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2002));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(18,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2003));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,2004));

        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2005));

        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2006));

        Calendar c = Target.getCalendar();
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,1999),
                                               DateFactory.getFactory().getDate(31,DECEMBER,2006), false);

        for (int i=0; i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");

    }

    @Test
    public void testGermanyFrankfurt() {
        System.out.println("Testing Frankfurt Stock Exchange holiday list...");

        List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(18,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2003));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2004));

        Calendar c = Germany.getCalendar(Germany.Market.FRANKFURTSTOCKEXCHANGE);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2003),
                                               DateFactory.getFactory().getDate(31,DECEMBER,2004), false);
        for (int i=0; i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }

    @Test
    public void testGermanyEurex() {
        System.out.println("Testing Eurex holiday list...");

        List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(18,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2003));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2004));

        Calendar c = Germany.getCalendar(Germany.Market.EUREX);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2003),
                                               DateFactory.getFactory().getDate(31,DECEMBER,2004), false);
        for (int i=0; i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }

    @Test
    public void testGermanyXetra() {
        System.out.println("Testing Xetra holiday list...");

        List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(18,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2003));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2004));

        Calendar c = Germany.getCalendar(Germany.Market.XETRA);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2003),
                                               DateFactory.getFactory().getDate(31,DECEMBER,2004), false);
        for (int i =0;i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }
    
    @Test
    public void testUKSettlement() {
        System.out.println("Testing UK settlement holiday list...");

        List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(3,MAY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,MAY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,2004));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,2004));

        expectedHol.add(DateFactory.getFactory().getDate(3,JANUARY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(2,MAY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(30,MAY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,2005));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,2005));

        expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(29,MAY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(28,AUGUST,2006));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2006));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2007));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,2007));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2007));
        expectedHol.add(DateFactory.getFactory().getDate(7,MAY,2007));
        expectedHol.add(DateFactory.getFactory().getDate(28,MAY,2007));
        expectedHol.add(DateFactory.getFactory().getDate(27,AUGUST,2007));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2007));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2007));

        Calendar c = UnitedKingdom.getCalendar(UnitedKingdom.Market.SETTLEMENT);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2004),
                                              DateFactory.getFactory().getDate(31,DECEMBER,2007),false);
        for (int i =0;i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }

    @Test
    public void testUKExchange() {
        System.out.println("Testing London Stock Exchange holiday list...");

        List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(3,MAY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,MAY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,2004));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,2004));

        expectedHol.add(DateFactory.getFactory().getDate(3,JANUARY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(2,MAY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(30,MAY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,2005));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,2005));

        expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(29,MAY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(28,AUGUST,2006));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2006));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2007));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,2007));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2007));
        expectedHol.add(DateFactory.getFactory().getDate(7,MAY,2007));
        expectedHol.add(DateFactory.getFactory().getDate(28,MAY,2007));
        expectedHol.add(DateFactory.getFactory().getDate(27,AUGUST,2007));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2007));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2007));

        Calendar c = UnitedKingdom.getCalendar(UnitedKingdom.Market.EXCHANGE);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2004),
                                               DateFactory.getFactory().getDate(31,DECEMBER,2007),false);
        for (int i =0;i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }

    @Test
    public void testUKMetals() {
        System.out.println("Testing London Metals Exchange holiday list...");

        List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(3,MAY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,MAY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,2004));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,2004));

        expectedHol.add(DateFactory.getFactory().getDate(3,JANUARY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(2,MAY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(30,MAY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,2005));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,2005));

        expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(29,MAY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(28,AUGUST,2006));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2006));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2007));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,2007));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2007));
        expectedHol.add(DateFactory.getFactory().getDate(7,MAY,2007));
        expectedHol.add(DateFactory.getFactory().getDate(28,MAY,2007));
        expectedHol.add(DateFactory.getFactory().getDate(27,AUGUST,2007));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2007));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2007));

        Calendar c = UnitedKingdom.getCalendar(UnitedKingdom.Market.METALS);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2004),
                                              DateFactory.getFactory().getDate(31,DECEMBER,2007),false);
        for (int i =0;i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }

    @Test
    public void testItalyExchange() {
        System.out.println("Testing Milan Stock Exchange holiday list...");

        List<Date> expectedHol = new Vector<Date>();

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2002));
        expectedHol.add(DateFactory.getFactory().getDate(29,MARCH,2002));
        expectedHol.add(DateFactory.getFactory().getDate(1,APRIL,2002));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2002));
        expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,2002));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2002));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2002));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2002));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2002));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(18,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,2003));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2003));
        expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,2003));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,2003));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2003));

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,2004));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,2004));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,2004));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,2004));

        Calendar c = Italy.getCalendar(Italy.Market.EXCHANGE);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2002),
                                              DateFactory.getFactory().getDate(31,DECEMBER,2004),false);
        for (int i =0;i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }

    @Test
    public void testBrazil() {
        System.out.println("Testing Brazil holiday list...");
        
        List<Date> expectedHol = new Vector<Date>();

        //expectedHol.add(DateUtil.getDateUtil().getDate(1,JANUARY,2005)); // Saturday
        expectedHol.add(DateFactory.getFactory().getDate(7,FEBRUARY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(8,FEBRUARY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,2005));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,2005));
        //expectedHol.add(DateUtil.getDateUtil().getDate(1,MAY,2005)); // Sunday
        expectedHol.add(DateFactory.getFactory().getDate(26,MAY,2005));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,2005));
        expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,2005));
        //expectedHol.add(DateUtil.getDateUtil().getDate(25,DECEMBER,2005)); // Sunday
        
        
        //expectedHol.add(DateUtil.getDateUtil().getDate(1,JANUARY,2006)); // Sunday
        expectedHol.add(DateFactory.getFactory().getDate(27,FEBRUARY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(28,FEBRUARY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,2006));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,2006));
        expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,2006));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,2006));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,2006));

        Calendar c = Brazil.getCalendar(Brazil.Market.SETTLEMENT);
        List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,2005),
                                             DateFactory.getFactory().getDate(31,DECEMBER,2006),false);
        for (int i =0;i<Math.min(hol.size(), expectedHol.size()); i++) {
            if (!hol.get(i).equals(expectedHol.get(i)))
                throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
                           + " while calculated holiday is " + hol.get(i));
        }
        if (hol.size()!=expectedHol.size())
            throw new IllegalStateException("there were " + expectedHol.size()
                       + " expected holidays, while there are " + hol.size()
                       + " calculated holidays");
    }

    @Test
    public void testEndOfMonth() {
        System.out.println("Testing end-of-month calculation...");

        Calendar c = Target.getCalendar(); // any calendar would be OK

        Date eom, counter = DateFactory.getFactory().getMinDate();
        Date last = DateFactory.getFactory().getMaxDate().adjust(new Period(-2, TimeUnit.MONTHS));

        while (counter.le(last)) {
            eom = c.getEndOfMonth(counter);
            // check that eom is eom
            if (!c.isEndOfMonth(eom))
                throw new IllegalStateException("\n  "
                           + eom.getWeekday() + " " + eom
                           + " is not the last business day in "
                           + eom.getMonthEnum() + " " + eom.getYear()
                           + " according to " + c.getName());
            // check that eom is in the same month as counter
            if (eom.getMonth() != counter.getMonth())
                throw new IllegalStateException("\n  "
                           + eom
                           + " is not in the same month as "
                           + counter);
            counter = counter.increment(1);
        }
    }

    @Test
    public void testBusinessDaysBetween() {

        System.out.println("Testing calculation of business days between dates...");

        List<Date> testDates = new Vector<Date>();
        testDates.add(DateFactory.getFactory().getDate(1,FEBRUARY,2002));
        testDates.add(DateFactory.getFactory().getDate(4,FEBRUARY,2002));
        testDates.add(DateFactory.getFactory().getDate(16,MAY,2003));
        testDates.add(DateFactory.getFactory().getDate(17,DECEMBER,2003));
        testDates.add(DateFactory.getFactory().getDate(17,DECEMBER,2004));
        testDates.add(DateFactory.getFactory().getDate(19,DECEMBER,2005));
        testDates.add(DateFactory.getFactory().getDate(2,JANUARY,2006));
        testDates.add(DateFactory.getFactory().getDate(13,MARCH,2006));
        testDates.add(DateFactory.getFactory().getDate(15,MAY,2006));
        testDates.add(DateFactory.getFactory().getDate(17,MARCH,2006));
        testDates.add(DateFactory.getFactory().getDate(15,MAY,2006));
        testDates.add(DateFactory.getFactory().getDate(26,JULY,2006));

        int expected[] = {
            1,
            321,
            152,
            251,
            252,
            10,
            48,
            42,
            -38,
            38,
            51
        };

        Calendar calendar = Brazil.getCalendar(Brazil.Market.SETTLEMENT);

        for (int i=1; i<testDates.size(); i++) {
            long calculated = calendar.businessDaysBetween(testDates.get(i-1),
                                                              testDates.get(i),true, false);
            if (calculated != expected[i-1]) {
                throw new IllegalStateException("from " + testDates.get(i-1)
                            + " to " + testDates.get(i) + ":\n"
                            + "    calculated: " + calculated + "\n"
                            + "    expected:   " + expected[i]);
            }
        }
    }


}
