/*
 Copyright (C) 2008 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */
package org.jquantlib.testsuite.calendars;

import static org.jquantlib.time.calendars.JointCalendar.JointCalendarRule.JOIN_BUSINESSDAYS;
import static org.jquantlib.time.calendars.JointCalendar.JointCalendarRule.JOIN_HOLIDAYS;
import static org.jquantlib.util.Date.Month.April;
import static org.jquantlib.util.Date.Month.August;
import static org.jquantlib.util.Date.Month.December;
import static org.jquantlib.util.Date.Month.February;
import static org.jquantlib.util.Date.Month.January;
import static org.jquantlib.util.Date.Month.July;
import static org.jquantlib.util.Date.Month.June;
import static org.jquantlib.util.Date.Month.March;
import static org.jquantlib.util.Date.Month.May;
import static org.jquantlib.util.Date.Month.November;
import static org.jquantlib.util.Date.Month.October;
import static org.jquantlib.util.Date.Month.September;

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
import org.junit.Test;

/**
 * @author Srinivas Hasti
 *
 */
public class CalendarsTest {
    
    @Test
    public void testModifiedCalendars() {

        System.out.println("Testing calendar modification...");

        Calendar c1 = Target.getCalendar();
        Calendar c2 = UnitedStates.getCalendar(UnitedStates.Market.NYSE);
        Date d1 = new Date(1,May,2004);      // holiday for both calendars
        Date d2 = new Date(26,April,2004);   // business day

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
        Date firstDate = Date.getTodaysDate();
        Date endDate =  new Date(firstDate);
        endDate.add(new Period(1, TimeUnit.Years));
        
        for (Date d = firstDate; d.le(endDate); d.inc()) {
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
        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(19,January,2004));
        expectedHol.add(new Date(16,February,2004));
        expectedHol.add(new Date(31,May,2004));
        expectedHol.add(new Date(5,July,2004));
        expectedHol.add(new Date(6,September,2004));
        expectedHol.add(new Date(11,October,2004));
        expectedHol.add(new Date(11,November,2004));
        expectedHol.add(new Date(25,November,2004));
        expectedHol.add(new Date(24,December,2004));

        expectedHol.add(new Date(31,December,2004));
        expectedHol.add(new Date(17,January,2005));
        expectedHol.add(new Date(21,February,2005));
        expectedHol.add(new Date(30,May,2005));
        expectedHol.add(new Date(4,July,2005));
        expectedHol.add(new Date(5,September,2005));
        expectedHol.add(new Date(10,October,2005));
        expectedHol.add(new Date(11,November,2005));
        expectedHol.add(new Date(24,November,2005));
        expectedHol.add(new Date(26,December,2005));

        Calendar c = UnitedStates.getCalendar(UnitedStates.Market.SETTLEMENT);
        List<Date> hol = c.getHolidayList(new Date( 1, January, 2004),
                                                         new Date(31,December, 2005),false);
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
        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(19,January,2004));
        expectedHol.add(new Date(16,February,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(31,May,2004));
        expectedHol.add(new Date(5,July,2004));
        expectedHol.add(new Date(6,September,2004));
        expectedHol.add(new Date(11,October,2004));
        expectedHol.add(new Date(11,November,2004));
        expectedHol.add(new Date(25,November,2004));
        expectedHol.add(new Date(24,December,2004));

        Calendar c = UnitedStates.getCalendar(UnitedStates.Market.GOVERNMENTBOND);
        List<Date> hol = c.getHolidayList(new Date(1,January,2004),
                                               new Date(31,December,2004), false);

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
        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(19,January,2004));
        expectedHol.add(new Date(16,February,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(31,May,2004));
        expectedHol.add(new Date(11,June,2004));
        expectedHol.add(new Date(5,July,2004));
        expectedHol.add(new Date(6,September,2004));
        expectedHol.add(new Date(25,November,2004));
        expectedHol.add(new Date(24,December,2004));

        expectedHol.add(new Date(17,January,2005));
        expectedHol.add(new Date(21,February,2005));
        expectedHol.add(new Date(25,March,2005));
        expectedHol.add(new Date(30,May,2005));
        expectedHol.add(new Date(4,July,2005));
        expectedHol.add(new Date(5,September,2005));
        expectedHol.add(new Date(24,November,2005));
        expectedHol.add(new Date(26,December,2005));

        expectedHol.add(new Date(2,January,2006));
        expectedHol.add(new Date(16,January,2006));
        expectedHol.add(new Date(20,February,2006));
        expectedHol.add(new Date(14,April,2006));
        expectedHol.add(new Date(29,May,2006));
        expectedHol.add(new Date(4,July,2006));
        expectedHol.add(new Date(4,September,2006));
        expectedHol.add(new Date(23,November,2006));
        expectedHol.add(new Date(25,December,2006));

        Calendar c = UnitedStates.getCalendar(UnitedStates.Market.NYSE);
        List<Date> hol = c.getHolidayList(new Date(1,January,2004),
                                               new Date(31,December,2006),false);

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
        histClose.add(new Date(11,June,2004));     // Reagan's funeral
        histClose.add(new Date(14,September,2001));// September 11, 2001
        histClose.add(new Date(13,September,2001));// September 11, 2001
        histClose.add(new Date(12,September,2001));// September 11, 2001
        histClose.add(new Date(11,September,2001));// September 11, 2001
        histClose.add(new Date(14,July,1977));     // 1977 Blackout
        histClose.add(new Date(25,January,1973));  // Johnson's funeral.
        histClose.add(new Date(28,December,1972)); // Truman's funeral
        histClose.add(new Date(21,July,1969));     // Lunar exploration nat. day
        histClose.add(new Date(31,March,1969));    // Eisenhower's funeral
        histClose.add(new Date(10,February,1969)); // heavy snow
        histClose.add(new Date(5,July,1968));      // Day after Independence Day
        // June 12-Dec. 31, 1968
        // Four day week (closed on Wednesdays) - Paperwork Crisis
        histClose.add(new Date(12,June,1968));
        histClose.add(new Date(19,June,1968));
        histClose.add(new Date(26,June,1968));
        histClose.add(new Date(3,July,1968 ));
        histClose.add(new Date(10,July,1968));
        histClose.add(new Date(17,July,1968));
        histClose.add(new Date(20,November,1968));
        histClose.add(new Date(27,November,1968));
        histClose.add(new Date(4,December,1968 ));
        histClose.add(new Date(11,December,1968));
        histClose.add(new Date(18,December,1968));
        // Presidential election days
        histClose.add(new Date(4,November,1980));
        histClose.add(new Date(2,November,1976));
        histClose.add(new Date(7,November,1972));
        histClose.add(new Date(5,November,1968));
        histClose.add(new Date(3,November,1964));
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
        expectedHol.add(new Date(1,January,1999));
        expectedHol.add(new Date(31,December,1999));

        expectedHol.add(new Date(21,April,2000));
        expectedHol.add(new Date(24,April,2000));
        expectedHol.add(new Date(1,May,2000));
        expectedHol.add(new Date(25,December,2000));
        expectedHol.add(new Date(26,December,2000));

        expectedHol.add(new Date(1,January,2001));
        expectedHol.add(new Date(13,April,2001));
        expectedHol.add(new Date(16,April,2001));
        expectedHol.add(new Date(1,May,2001));
        expectedHol.add(new Date(25,December,2001));
        expectedHol.add(new Date(26,December,2001));
        expectedHol.add(new Date(31,December,2001));

        expectedHol.add(new Date(1,January,2002));
        expectedHol.add(new Date(29,March,2002));
        expectedHol.add(new Date(1,April,2002));
        expectedHol.add(new Date(1,May,2002));
        expectedHol.add(new Date(25,December,2002));
        expectedHol.add(new Date(26,December,2002));

        expectedHol.add(new Date(1,January,2003));
        expectedHol.add(new Date(18,April,2003));
        expectedHol.add(new Date(21,April,2003));
        expectedHol.add(new Date(1,May,2003));
        expectedHol.add(new Date(25,December,2003));
        expectedHol.add(new Date(26,December,2003));

        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(12,April,2004));

        expectedHol.add(new Date(25,March,2005));
        expectedHol.add(new Date(28,March,2005));
        expectedHol.add(new Date(26,December,2005));

        expectedHol.add(new Date(14,April,2006));
        expectedHol.add(new Date(17,April,2006));
        expectedHol.add(new Date(1,May,2006));
        expectedHol.add(new Date(25,December,2006));
        expectedHol.add(new Date(26,December,2006));

        Calendar c = Target.getCalendar();
        List<Date> hol = c.getHolidayList(new Date(1,January,1999),
                                               new Date(31,December,2006), false);

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

        expectedHol.add(new Date(1,January,2003));
        expectedHol.add(new Date(18,April,2003));
        expectedHol.add(new Date(21,April,2003));
        expectedHol.add(new Date(1,May,2003));
        expectedHol.add(new Date(24,December,2003));
        expectedHol.add(new Date(25,December,2003));
        expectedHol.add(new Date(26,December,2003));
        expectedHol.add(new Date(31,December,2003));

        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(12,April,2004));
        expectedHol.add(new Date(24,December,2004));
        expectedHol.add(new Date(31,December,2004));

        Calendar c = Germany.getCalendar(Germany.Market.FRANKFURTSTOCKEXCHANGE);
        List<Date> hol = c.getHolidayList(new Date(1,January,2003),
                                               new Date(31,December,2004), false);
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

        expectedHol.add(new Date(1,January,2003));
        expectedHol.add(new Date(18,April,2003));
        expectedHol.add(new Date(21,April,2003));
        expectedHol.add(new Date(1,May,2003));
        expectedHol.add(new Date(24,December,2003));
        expectedHol.add(new Date(25,December,2003));
        expectedHol.add(new Date(26,December,2003));
        expectedHol.add(new Date(31,December,2003));

        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(12,April,2004));
        expectedHol.add(new Date(24,December,2004));
        expectedHol.add(new Date(31,December,2004));

        Calendar c = Germany.getCalendar(Germany.Market.EUREX);
        List<Date> hol = c.getHolidayList(new Date(1,January,2003),
                                               new Date(31,December,2004), false);
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

        expectedHol.add(new Date(1,January,2003));
        expectedHol.add(new Date(18,April,2003));
        expectedHol.add(new Date(21,April,2003));
        expectedHol.add(new Date(1,May,2003));
        expectedHol.add(new Date(24,December,2003));
        expectedHol.add(new Date(25,December,2003));
        expectedHol.add(new Date(26,December,2003));
        expectedHol.add(new Date(31,December,2003));

        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(12,April,2004));
        expectedHol.add(new Date(24,December,2004));
        expectedHol.add(new Date(31,December,2004));

        Calendar c = Germany.getCalendar(Germany.Market.XETRA);
        List<Date> hol = c.getHolidayList(new Date(1,January,2003),
                                               new Date(31,December,2004), false);
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

        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(12,April,2004));
        expectedHol.add(new Date(3,May,2004));
        expectedHol.add(new Date(31,May,2004));
        expectedHol.add(new Date(30,August,2004));
        expectedHol.add(new Date(27,December,2004));
        expectedHol.add(new Date(28,December,2004));

        expectedHol.add(new Date(3,January,2005));
        expectedHol.add(new Date(25,March,2005));
        expectedHol.add(new Date(28,March,2005));
        expectedHol.add(new Date(2,May,2005));
        expectedHol.add(new Date(30,May,2005));
        expectedHol.add(new Date(29,August,2005));
        expectedHol.add(new Date(26,December,2005));
        expectedHol.add(new Date(27,December,2005));

        expectedHol.add(new Date(2,January,2006));
        expectedHol.add(new Date(14,April,2006));
        expectedHol.add(new Date(17,April,2006));
        expectedHol.add(new Date(1,May,2006));
        expectedHol.add(new Date(29,May,2006));
        expectedHol.add(new Date(28,August,2006));
        expectedHol.add(new Date(25,December,2006));
        expectedHol.add(new Date(26,December,2006));

        expectedHol.add(new Date(1,January,2007));
        expectedHol.add(new Date(6,April,2007));
        expectedHol.add(new Date(9,April,2007));
        expectedHol.add(new Date(7,May,2007));
        expectedHol.add(new Date(28,May,2007));
        expectedHol.add(new Date(27,August,2007));
        expectedHol.add(new Date(25,December,2007));
        expectedHol.add(new Date(26,December,2007));

        Calendar c = UnitedKingdom.getCalendar(UnitedKingdom.Market.SETTLEMENT);
        List<Date> hol = c.getHolidayList(new Date(1,January,2004),
                                              new Date(31,December,2007),false);
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

        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(12,April,2004));
        expectedHol.add(new Date(3,May,2004));
        expectedHol.add(new Date(31,May,2004));
        expectedHol.add(new Date(30,August,2004));
        expectedHol.add(new Date(27,December,2004));
        expectedHol.add(new Date(28,December,2004));

        expectedHol.add(new Date(3,January,2005));
        expectedHol.add(new Date(25,March,2005));
        expectedHol.add(new Date(28,March,2005));
        expectedHol.add(new Date(2,May,2005));
        expectedHol.add(new Date(30,May,2005));
        expectedHol.add(new Date(29,August,2005));
        expectedHol.add(new Date(26,December,2005));
        expectedHol.add(new Date(27,December,2005));

        expectedHol.add(new Date(2,January,2006));
        expectedHol.add(new Date(14,April,2006));
        expectedHol.add(new Date(17,April,2006));
        expectedHol.add(new Date(1,May,2006));
        expectedHol.add(new Date(29,May,2006));
        expectedHol.add(new Date(28,August,2006));
        expectedHol.add(new Date(25,December,2006));
        expectedHol.add(new Date(26,December,2006));

        expectedHol.add(new Date(1,January,2007));
        expectedHol.add(new Date(6,April,2007));
        expectedHol.add(new Date(9,April,2007));
        expectedHol.add(new Date(7,May,2007));
        expectedHol.add(new Date(28,May,2007));
        expectedHol.add(new Date(27,August,2007));
        expectedHol.add(new Date(25,December,2007));
        expectedHol.add(new Date(26,December,2007));

        Calendar c = UnitedKingdom.getCalendar(UnitedKingdom.Market.EXCHANGE);
        List<Date> hol = c.getHolidayList(new Date(1,January,2004),
                                               new Date(31,December,2007),false);
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

        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(12,April,2004));
        expectedHol.add(new Date(3,May,2004));
        expectedHol.add(new Date(31,May,2004));
        expectedHol.add(new Date(30,August,2004));
        expectedHol.add(new Date(27,December,2004));
        expectedHol.add(new Date(28,December,2004));

        expectedHol.add(new Date(3,January,2005));
        expectedHol.add(new Date(25,March,2005));
        expectedHol.add(new Date(28,March,2005));
        expectedHol.add(new Date(2,May,2005));
        expectedHol.add(new Date(30,May,2005));
        expectedHol.add(new Date(29,August,2005));
        expectedHol.add(new Date(26,December,2005));
        expectedHol.add(new Date(27,December,2005));

        expectedHol.add(new Date(2,January,2006));
        expectedHol.add(new Date(14,April,2006));
        expectedHol.add(new Date(17,April,2006));
        expectedHol.add(new Date(1,May,2006));
        expectedHol.add(new Date(29,May,2006));
        expectedHol.add(new Date(28,August,2006));
        expectedHol.add(new Date(25,December,2006));
        expectedHol.add(new Date(26,December,2006));

        expectedHol.add(new Date(1,January,2007));
        expectedHol.add(new Date(6,April,2007));
        expectedHol.add(new Date(9,April,2007));
        expectedHol.add(new Date(7,May,2007));
        expectedHol.add(new Date(28,May,2007));
        expectedHol.add(new Date(27,August,2007));
        expectedHol.add(new Date(25,December,2007));
        expectedHol.add(new Date(26,December,2007));

        Calendar c = UnitedKingdom.getCalendar(UnitedKingdom.Market.METALS);
        List<Date> hol = c.getHolidayList(new Date(1,January,2004),
                                              new Date(31,December,2007),false);
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

        expectedHol.add(new Date(1,January,2002));
        expectedHol.add(new Date(29,March,2002));
        expectedHol.add(new Date(1,April,2002));
        expectedHol.add(new Date(1,May,2002));
        expectedHol.add(new Date(15,August,2002));
        expectedHol.add(new Date(24,December,2002));
        expectedHol.add(new Date(25,December,2002));
        expectedHol.add(new Date(26,December,2002));
        expectedHol.add(new Date(31,December,2002));

        expectedHol.add(new Date(1,January,2003));
        expectedHol.add(new Date(18,April,2003));
        expectedHol.add(new Date(21,April,2003));
        expectedHol.add(new Date(1,May,2003));
        expectedHol.add(new Date(15,August,2003));
        expectedHol.add(new Date(24,December,2003));
        expectedHol.add(new Date(25,December,2003));
        expectedHol.add(new Date(26,December,2003));
        expectedHol.add(new Date(31,December,2003));

        expectedHol.add(new Date(1,January,2004));
        expectedHol.add(new Date(9,April,2004));
        expectedHol.add(new Date(12,April,2004));
        expectedHol.add(new Date(24,December,2004));
        expectedHol.add(new Date(31,December,2004));

        Calendar c = Italy.getCalendar(Italy.Market.EXCHANGE);
        List<Date> hol = c.getHolidayList(new Date(1,January,2002),
                                              new Date(31,December,2004),false);
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

        //expectedHol.add(new Date(1,January,2005)); // Saturday
        expectedHol.add(new Date(7,February,2005));
        expectedHol.add(new Date(8,February,2005));
        expectedHol.add(new Date(25,March,2005));
        expectedHol.add(new Date(21,April,2005));
        //expectedHol.add(new Date(1,May,2005)); // Sunday
        expectedHol.add(new Date(26,May,2005));
        expectedHol.add(new Date(7,September,2005));
        expectedHol.add(new Date(12,October,2005));
        expectedHol.add(new Date(2,November,2005));
        expectedHol.add(new Date(15,November,2005));
        //expectedHol.add(new Date(25,December,2005)); // Sunday

        //expectedHol.add(new Date(1,January,2006)); // Sunday
        expectedHol.add(new Date(27,February,2006));
        expectedHol.add(new Date(28,February,2006));
        expectedHol.add(new Date(14,April,2006));
        expectedHol.add(new Date(21,April,2006));
        expectedHol.add(new Date(1,May,2006));
        expectedHol.add(new Date(15,June,2006));
        expectedHol.add(new Date(7,September,2006));
        expectedHol.add(new Date(12,October,2006));
        expectedHol.add(new Date(2,November,2006));
        expectedHol.add(new Date(15,November,2006));
        expectedHol.add(new Date(25,December,2006));

        Calendar c = Brazil.getCalendar(Brazil.Market.SETTLEMENT);
        List<Date> hol = c.getHolidayList(new Date(1,January,2005),
                                             new  Date(31,December,2006),false);
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

        Date eom, counter = Date.getMinDate();
        Date last = new Date(Date.getMaxDate()).subtract(new Period(2, TimeUnit.Months));

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
            counter = counter.add(1);
        }
    }

    @Test
    public void testBusinessDaysBetween() {

        System.out.println("Testing calculation of business days between dates...");

        List<Date> testDates = new Vector<Date>();
        testDates.add(new Date(1,February,2002));
        testDates.add(new Date(4,February,2002));
        testDates.add(new Date(16,May,2003));
        testDates.add(new Date(17,December,2003));
        testDates.add(new Date(17,December,2004));
        testDates.add(new Date(19,December,2005));
        testDates.add(new Date(2,January,2006));
        testDates.add(new Date(13,March,2006));
        testDates.add(new Date(15,May,2006));
        testDates.add(new Date(17,March,2006));
        testDates.add(new Date(15,May,2006));
        testDates.add(new Date(26,July,2006));

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
