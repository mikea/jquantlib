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

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Taiwan;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renjith Nair
 *
 *
 */

public class TaiwanCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(TaiwanCalendarTest.class);

    private Calendar exchange = null;
	private List<Date> expectedHol = null;
	
	public TaiwanCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		this.exchange = Taiwan.getCalendar(Taiwan.Market.TSE);
	}
	
        
    // 2002 - year in the past
	@Test
    public void testTaiwanTSEHolidaysYear2002()
    {    	
       	int year = 2002;
        logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
    	
    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(11,FEBRUARY,year));
    	expectedHol.add(df.getDate(12,FEBRUARY,year));
    	expectedHol.add(df.getDate(13,FEBRUARY,year));
    	expectedHol.add(df.getDate(14,FEBRUARY,year));
    	expectedHol.add(df.getDate(15,FEBRUARY,year));
    	expectedHol.add(df.getDate(28,FEBRUARY,year));
    	expectedHol.add(df.getDate(5,APRIL,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(10,OCTOBER,year));
  	    	
    	// Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2003 - year in the past
	@Test
    public void testTaiwanTSEHolidaysYear2003()
    {    	
       	int year = 2003;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(31,JANUARY,year));
    	expectedHol.add(df.getDate(3,FEBRUARY,year));
    	expectedHol.add(df.getDate(4,FEBRUARY,year));
    	expectedHol.add(df.getDate(5,FEBRUARY,year));
    	expectedHol.add(df.getDate(28,FEBRUARY,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(4,JUNE,year));
    	expectedHol.add(df.getDate(11,SEPTEMBER,year));
    	expectedHol.add(df.getDate(10,OCTOBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }

	
	// 2004 - year in the past
	@Test
    public void testTaiwanTSEHolidaysYear2004()
    {    	
       	int year = 2004;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(21,JANUARY,year));
    	expectedHol.add(df.getDate(22,JANUARY,year));
    	expectedHol.add(df.getDate(23,JANUARY,year));
    	expectedHol.add(df.getDate(26,JANUARY,year));
    	expectedHol.add(df.getDate(22,JUNE,year));
    	expectedHol.add(df.getDate(28,SEPTEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2005 - year in the past
	@Test
    public void testTaiwanTSEHolidaysYear2005()
    {    	
       	int year = 2005;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(4,FEBRUARY,year));
    	expectedHol.add(df.getDate(7,FEBRUARY,year));
    	expectedHol.add(df.getDate(8,FEBRUARY,year));
    	expectedHol.add(df.getDate(9,FEBRUARY,year));
    	expectedHol.add(df.getDate(10,FEBRUARY,year));
    	expectedHol.add(df.getDate(11,FEBRUARY,year));
    	expectedHol.add(df.getDate(28,FEBRUARY,year));
    	expectedHol.add(df.getDate(5,APRIL,year));
    	expectedHol.add(df.getDate(2,MAY,year));
    	expectedHol.add(df.getDate(10,OCTOBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2006 - year in the past
	@Test
    public void testTaiwanTSEHolidaysYear2006()
    {    	
       	int year = 2006;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(30,JANUARY,year));
    	expectedHol.add(df.getDate(31,JANUARY,year));
    	expectedHol.add(df.getDate(1,FEBRUARY,year));
    	expectedHol.add(df.getDate(2,FEBRUARY,year));
    	expectedHol.add(df.getDate(3,FEBRUARY,year));
    	expectedHol.add(df.getDate(28,FEBRUARY,year));
    	expectedHol.add(df.getDate(5,APRIL,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(31,MAY,year));
    	expectedHol.add(df.getDate(6,OCTOBER,year));
    	expectedHol.add(df.getDate(10,OCTOBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2007 - year in the past
	@Test
    public void testTaiwanTSEHolidaysYear2007()
    {    	
       	int year = 2007;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(19,FEBRUARY,year));
    	expectedHol.add(df.getDate(20,FEBRUARY,year));
    	expectedHol.add(df.getDate(21,FEBRUARY,year));
    	expectedHol.add(df.getDate(22,FEBRUARY,year));
    	expectedHol.add(df.getDate(23,FEBRUARY,year));
    	expectedHol.add(df.getDate(28,FEBRUARY,year));
    	expectedHol.add(df.getDate(5,APRIL,year));
    	expectedHol.add(df.getDate(6,APRIL,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(18,JUNE,year));
    	expectedHol.add(df.getDate(19,JUNE,year));
    	expectedHol.add(df.getDate(24,SEPTEMBER,year));
    	expectedHol.add(df.getDate(25,SEPTEMBER,year));
    	expectedHol.add(df.getDate(10,OCTOBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2008 - Current Year
	@Test
    public void testTaiwanTSEHolidaysYear2008()
    {    	
       	int year = 2008;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(4,FEBRUARY,year));
    	expectedHol.add(df.getDate(5,FEBRUARY,year));
    	expectedHol.add(df.getDate(6,FEBRUARY,year));
    	expectedHol.add(df.getDate(7,FEBRUARY,year));
    	expectedHol.add(df.getDate(8,FEBRUARY,year));
    	expectedHol.add(df.getDate(11,FEBRUARY,year));
    	expectedHol.add(df.getDate(28,FEBRUARY,year));
    	expectedHol.add(df.getDate(4,APRIL,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(10,OCTOBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2009 - Future Year
	@Test
    public void testTaiwanTSEHolidaysYear2009()
    {    	
       	int year = 2009;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(1,JANUARY,year));
    	expectedHol.add(df.getDate(1,MAY,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2010 - Future Year
	@Test
    public void testTaiwanTSEHolidaysYear2010()
    {    	
       	int year = 2010;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(1,JANUARY,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2011 - Future Year
	@Test
    public void testTaiwanTSEHolidaysYear2011()
    {    	
       	int year = 2011;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(28,FEBRUARY,year));
    	expectedHol.add(df.getDate(10,OCTOBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
	// 2012 - Future Year
	@Test
    public void testTaiwanTSEHolidaysYear2012()
    {    	
       	int year = 2012;
    	logger.info("Testing " + Taiwan.Market.TSE + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();
        
    	expectedHol.add(df.getDate(28,FEBRUARY,year));
    	expectedHol.add(df.getDate(1,MAY,year));
    	expectedHol.add(df.getDate(10,OCTOBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);
    }
	
}
