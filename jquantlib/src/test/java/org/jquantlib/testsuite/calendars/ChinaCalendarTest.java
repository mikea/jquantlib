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

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.China;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Tim Swetonic
 * @author Jia Jia
 * @author Renjith Nair
 * 
 */

public class ChinaCalendarTest {
    private final static Logger logger = LoggerFactory.getLogger(ChinaCalendarTest.class);

    private Calendar ssh;
    private List<Date> expectedHol = null;

    @Before
    public void setUp() {
        ssh = China.getCalendar(China.Market.SSE);
        expectedHol = new Vector<Date>();
    }
    
    @Test
    public void testChinaSSEYear2004() {
        int year = 2004;
        logger.info("Testing China holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(19, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(23, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(27, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(28, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(7, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(7, OCTOBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, ssh, year);
    }

    @Test
    public void testChinaSSEYear2005() {
        int year = 2005;
        logger.info("Testing China holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(3, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(7, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(8, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(9, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(10, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(11, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(14, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(15, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(7, OCTOBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, ssh, year);
    }

    @Test
    public void testChinaSSEYear2006() {
        int year = 2006;
        logger.info("Testing China holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(2, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(27, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(30, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(31, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, OCTOBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, ssh, year);
    }

    @Test
    public void testChinaSSEYear2007() {
        int year = 2007;
        logger.info("Testing China holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(19, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(23, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(7, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, OCTOBER, year));
        // Interesting Fact
        //31 Dec 2007 is included as holiday in 2008 list of holidays :)
        expectedHol.add(DateFactory.getFactory().getDate(31, DECEMBER, year));
        
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, ssh, year);
    }

    // 2008 - current year
    @Test
    public void testChinaSSEYear2008() {
        int year = 2008;
        logger.info("Testing China holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(7, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(8, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(11, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(9, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(15, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(29, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(30, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, OCTOBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(3, OCTOBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, ssh, year);
    }

    @Test
    public void testChinaSSEYear2009() {
        int year = 2009;
        logger.info("Testing China holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, ssh, year);
    }

    @Test
    public void testChinaSSEYear2010() {
        int year = 2010;
        logger.info("Testing China holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, ssh, year);
    }

    @Test
    public void testChinaSSEYear2011() {
        int year = 2011;
        logger.info("Testing China holiday list for the year " + year + "...");

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, ssh, year);
    }

    @Test
    public void testChinaSSEYear2012() {
        int year = 2012;
        logger.info("Testing China holiday list for the year " + year + "...");

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, ssh, year);
    }
}
