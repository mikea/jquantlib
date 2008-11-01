/*
 Copyright (C) 2008 Joon Tiang Heng, Jia Jia
  
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
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Finland;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Joon Tiang Heng
 * @author Jia Jia
 * 
 */

public class FinlandCalendarTest {

    private final static Logger logger = Logger.getLogger(FinlandCalendarTest.class);

    private Calendar c;
    private List<Date> expectedHol;

    @Before
    public void setUp() {
        c = Finland.getCalendar();
        expectedHol = new Vector<Date>();
    }

    @Test
    public void testFinlandYear2004() {
        int year = 2004;
        logger.info("Testing Finnish holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(18, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testFinlandYear2005() {
        int year = 2005;
        logger.info("Testing Finnish holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(6, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(28, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));


        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testFinlandYear2006() {
        int year = 2006;
        logger.info("Testing Finnish holiday list for the year " + year + "...");
        expectedHol.add(DateFactory.getFactory().getDate(6, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(14, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(23, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testFinlandYear2007() {
        int year = 2007;
        logger.info("Testing Finnish holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    // 2008 - current year
    @Test
    public void testFinlandYear2008() {
        int year = 2008;
        logger.info("Testing Finnish holiday list for the year " + year + "...");

        // New year's day
        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        // Epiphany --weekend in 2008
        // expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year));
        // good friday
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        // easter monday
        expectedHol.add(DateFactory.getFactory().getDate(24, MARCH, year));

        // ascension thursday/labour day
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        // Midsummer Eve (Friday between June 18-24)
        expectedHol.add(DateFactory.getFactory().getDate(20, JUNE, year));
        // Independence Day -- weekend in 2008
        // expectedHol.add(DateFactory.getFactory().getDate(6,DECEMBER,year));
        // christmas eve
        expectedHol.add(DateFactory.getFactory().getDate(24, DECEMBER, year));
        // christmas
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        // boxing day
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    @Test
    public void testFinlandYear2009() {
        int year = 2009;
        logger.info("Testing Finnish holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(10, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(13, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(19, JUNE, year));       
        expectedHol.add(DateFactory.getFactory().getDate(24, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testFinlandYear2010() {
        int year = 2010;
        logger.info("Testing Finnish holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(13, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(18, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testFinlandYear2011() {
        int year = 2011;
        logger.info("Testing Finnish holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(6, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testFinlandYear2012() {
        int year = 2012;
        logger.info("Testing Finnish holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(6, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

}
