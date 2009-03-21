/*
 Copyright (C) 2008 Jia Jia

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
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Canada;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jia Jia
 *
 */

public class CanadaCalendarTest {
    private final static Logger logger = LoggerFactory.getLogger(CanadaCalendarTest.class);

    private final Calendar settlement;
    private final Calendar exchange;
    
	public CanadaCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        settlement = Canada.getCalendar(Canada.Market.SETTLEMENT);
        exchange = Canada.getCalendar(Canada.Market.TSX);
	}
        	
    @Test
    public void testCanadaSettlementYear2004() {
        int year = 2004;
        logger.info("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(9,APRIL,year));
        expectedHol.add(df.getDate(24,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(2,AUGUST,year));
        expectedHol.add(df.getDate(6,SEPTEMBER,year));
        expectedHol.add(df.getDate(11,OCTOBER,year));
        expectedHol.add(df.getDate(11,NOVEMBER,year)); 
        expectedHol.add(df.getDate(27,DECEMBER,year));
        expectedHol.add(df.getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }

    @Test
    public void testCanadaSettlementYear2005() {
        int year = 2005;
        logger.info("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(25,MARCH,year)); 
        expectedHol.add(df.getDate(23,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(1,AUGUST,year));
        expectedHol.add(df.getDate(5,SEPTEMBER,year));
        expectedHol.add(df.getDate(10,OCTOBER,year));
        expectedHol.add(df.getDate(11,NOVEMBER,year)); 
        expectedHol.add(df.getDate(26,DECEMBER,year));
        expectedHol.add(df.getDate(27,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2006() {
        int year = 2006;
        logger.info("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(2,JANUARY,year)); 
        expectedHol.add(df.getDate(14,APRIL,year));
        expectedHol.add(df.getDate(22,MAY,year));
        expectedHol.add(df.getDate(3,JULY,year));
        expectedHol.add(df.getDate(7,AUGUST,year));
        expectedHol.add(df.getDate(4,SEPTEMBER,year));
        expectedHol.add(df.getDate(9,OCTOBER,year));
        expectedHol.add(df.getDate(13,NOVEMBER,year)); 
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2007() {
        int year = 2007;
        logger.info("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(6,APRIL,year));
        expectedHol.add(df.getDate(21,MAY,year));
        expectedHol.add(df.getDate(2,JULY,year));
        expectedHol.add(df.getDate(6,AUGUST,year));
        expectedHol.add(df.getDate(3,SEPTEMBER,year));
        expectedHol.add(df.getDate(8,OCTOBER,year));
        expectedHol.add(df.getDate(12,NOVEMBER,year)); 
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2008() {
        int year = 2008;
        logger.info("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(18,FEBRUARY,year));
        expectedHol.add(df.getDate(21,MARCH,year));
        expectedHol.add(df.getDate(19,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(4,AUGUST,year));
        expectedHol.add(df.getDate(1,SEPTEMBER,year));
        expectedHol.add(df.getDate(13,OCTOBER,year));
        expectedHol.add(df.getDate(11,NOVEMBER,year)); 
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2009() {
        int year = 2009;
        logger.info("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(16,FEBRUARY,year));
        expectedHol.add(df.getDate(10,APRIL,year));
        expectedHol.add(df.getDate(18,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(3,AUGUST,year));
        expectedHol.add(df.getDate(7,SEPTEMBER,year));
        expectedHol.add(df.getDate(12,OCTOBER,year));
        expectedHol.add(df.getDate(11,NOVEMBER,year)); 
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2010() {
        int year = 2010;
        logger.info("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(15,FEBRUARY,year));
        expectedHol.add(df.getDate(2,APRIL,year));
        expectedHol.add(df.getDate(24,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(2,AUGUST,year));
        expectedHol.add(df.getDate(6,SEPTEMBER,year));
        expectedHol.add(df.getDate(11,OCTOBER,year));
        expectedHol.add(df.getDate(11,NOVEMBER,year)); 
        expectedHol.add(df.getDate(27,DECEMBER,year));
        expectedHol.add(df.getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2011() {
        int year = 2011;
        logger.info("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(21,FEBRUARY,year)); 
        expectedHol.add(df.getDate(22,APRIL,year));
        expectedHol.add(df.getDate(23,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(1,AUGUST,year));
        expectedHol.add(df.getDate(5,SEPTEMBER,year));
        expectedHol.add(df.getDate(10,OCTOBER,year));
        expectedHol.add(df.getDate(11,NOVEMBER,year)); 
        expectedHol.add(df.getDate(26,DECEMBER,year));
        expectedHol.add(df.getDate(27,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }    
    
    @Test
    public void testCanadaSettlementYear2012() {
        int year = 2012;
        logger.info("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(2,JANUARY,year)); 
        expectedHol.add(df.getDate(20,FEBRUARY,year));
        expectedHol.add(df.getDate(6,APRIL,year));
        expectedHol.add(df.getDate(21,MAY,year));
        expectedHol.add(df.getDate(2,JULY,year));
        expectedHol.add(df.getDate(6,AUGUST,year));
        expectedHol.add(df.getDate(3,SEPTEMBER,year));
        expectedHol.add(df.getDate(8,OCTOBER,year));
        expectedHol.add(df.getDate(12,NOVEMBER,year)); 
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
 
    @Test
    public void testCanadaTSXYear2004() {
        int year = 2004;
        logger.info("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(9,APRIL,year));
        expectedHol.add(df.getDate(24,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(2,AUGUST,year));
        expectedHol.add(df.getDate(6,SEPTEMBER,year));
        expectedHol.add(df.getDate(11,OCTOBER,year));
        expectedHol.add(df.getDate(27,DECEMBER,year));
        expectedHol.add(df.getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }
    
    @Test
    public void testCanadaTSXYear2005() {
        int year = 2005;
        logger.info("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(25,MARCH,year));
        expectedHol.add(df.getDate(23,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(1,AUGUST,year));
        expectedHol.add(df.getDate(5,SEPTEMBER,year));
        expectedHol.add(df.getDate(10,OCTOBER,year));
        expectedHol.add(df.getDate(27,DECEMBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }

    @Test
    public void testCanadaTSXYear2006() {
        int year = 2006;
        logger.info("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(2,JANUARY,year)); 
        expectedHol.add(df.getDate(14,APRIL,year));
        expectedHol.add(df.getDate(22,MAY,year));
        expectedHol.add(df.getDate(3,JULY,year));
        expectedHol.add(df.getDate(7,AUGUST,year));
        expectedHol.add(df.getDate(4,SEPTEMBER,year));
        expectedHol.add(df.getDate(9,OCTOBER,year));
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }

    @Test
    public void testCanadaTSXYear2007() {
        int year = 2007;
        logger.info("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(6,APRIL,year));
        expectedHol.add(df.getDate(21,MAY,year));
        expectedHol.add(df.getDate(2,JULY,year));
        expectedHol.add(df.getDate(6,AUGUST,year));
        expectedHol.add(df.getDate(3,SEPTEMBER,year));
        expectedHol.add(df.getDate(8,OCTOBER,year));
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }

    @Test
    public void testCanadaTSXYear2008() {
        int year = 2008;
        logger.info("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(18,FEBRUARY,year));
        expectedHol.add(df.getDate(21,MARCH,year));
        expectedHol.add(df.getDate(19,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(4,AUGUST,year));
        expectedHol.add(df.getDate(1,SEPTEMBER,year));
        expectedHol.add(df.getDate(13,OCTOBER,year));
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }

    @Test
    public void testCanadaTSXYear2009() {
        int year = 2009;
        logger.info("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(16,FEBRUARY,year));
        expectedHol.add(df.getDate(10,APRIL,year));
        expectedHol.add(df.getDate(18,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(3,AUGUST,year));
        expectedHol.add(df.getDate(7,SEPTEMBER,year));
        expectedHol.add(df.getDate(12,OCTOBER,year));
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }

    @Test
    public void testCanadaTSXYear2010() {
        int year = 2010;
        logger.info("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1,JANUARY,year)); 
        expectedHol.add(df.getDate(15,FEBRUARY,year));
        expectedHol.add(df.getDate(2,APRIL,year));
        expectedHol.add(df.getDate(24,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(2,AUGUST,year));
        expectedHol.add(df.getDate(6,SEPTEMBER,year));
        expectedHol.add(df.getDate(11,OCTOBER,year));
        expectedHol.add(df.getDate(27,DECEMBER,year));
        expectedHol.add(df.getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }

    @Test
    public void testCanadaTSXYear2011() {
        int year = 2011;
        logger.info("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(21,FEBRUARY,year)); 
        expectedHol.add(df.getDate(22,APRIL,year));
        expectedHol.add(df.getDate(23,MAY,year));
        expectedHol.add(df.getDate(1,JULY,year));
        expectedHol.add(df.getDate(1,AUGUST,year));
        expectedHol.add(df.getDate(5,SEPTEMBER,year));
        expectedHol.add(df.getDate(10,OCTOBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        expectedHol.add(df.getDate(27,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }

    @Test
    public void testCanadaTSXYear2012() {
        int year = 2012;
        logger.info("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(2,JANUARY,year)); 
        expectedHol.add(df.getDate(20,FEBRUARY,year));
        expectedHol.add(df.getDate(6,APRIL,year));
        expectedHol.add(df.getDate(21,MAY,year));
        expectedHol.add(df.getDate(2,JULY,year));
        expectedHol.add(df.getDate(6,AUGUST,year));
        expectedHol.add(df.getDate(3,SEPTEMBER,year));
        expectedHol.add(df.getDate(8,OCTOBER,year));
        expectedHol.add(df.getDate(25,DECEMBER,year));
        expectedHol.add(df.getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, exchange, year);

    }

}
