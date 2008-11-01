/*
 Copyright (C) 2008 Srinivas Hasti
 Copyright (C) 2008 Dominik Holenstein

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
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Argentina;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 * @author Jia Jia
 *
 *
 */

public class ArgentinaCalendarTest {
    private final static Logger logger = LoggerFactory.getLogger(ArgentinaCalendarTest.class);

    private Calendar bcba = null;
    private Calendar settlement = null;
    private List<Date> expectedHol = null;
    
	public ArgentinaCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
    @Before
    public void setup() {
        bcba = Argentina.getCalendar(Argentina.Market.BCBA);
        settlement = Argentina.getCalendar(Argentina.Market.SETTLEMENT);
        expectedHol = new Vector<Date>();
    }
	
    // 2004 - leap-year in the past
    @Test
    public void testArgentinaBCBAYear2004()
    {
       	int year = 2004;
    	logger.info("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year)); --> Sunday
    	expectedHol.add(DateFactory.getFactory().getDate(21,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(16,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(25,DECEMBER,year)); --> Sunday 
    
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bcba, year);
   
    }
    
    @Test
    public void testArgentinaBCBAYear2005() {        
        int year = 2005;
        logger.info("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bcba, year);
    }
    
    @Test
    public void testArgentinaBCBAYear2006() {
        
        int year = 2006;
        logger.info("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(19,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bcba, year);
    }
    
    @Test
    public void testArgentinaBCBAYear2010() {
        
        int year = 2010;
        logger.info("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(16,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bcba, year);
    }
    
    @Test
    public void testArgentinaBCBAYear2011() {        
        int year = 2011;
        logger.info("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bcba, year);
    }
    
    @Test
    public void testArgentinaBCBAYear2007() {
    	
    	int year = 2007;
    	logger.info("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,AUGUST,year));
        // expectedHol.add(DateFactory.getDateUtil().getDate(8,DECEMBER,year)); --> Saturday
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bcba, year);
    }
    
    @Test
    public void testArgentinaBCBAYear2008()
    {
      	int year = 2008;
    	logger.info("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(20,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(16,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bcba, year);
    }
    
    @Test
    public void testArgentinaBCBAYear2009() {
    	
    	int year = 2009;
    	logger.info("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(17,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bcba, year);
    	
    }

    @Test
    public void testArgentinaBCBAYear2012() {
    	
    	int year = 2012;
    	logger.info("Testing " + Argentina.Market.BCBA + " holiday list for the year " + year + "...");
    	
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); --> Sunday
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,AUGUST,year));
        // expectedHol.add(DateFactory.getDateUtil().getDate(8,DECEMBER,year)); --> Saturday
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bcba, year);    	
    }
    
    @Test
    public void testArgentinaSettlementYear2004() {
        int year = 2004;        
        logger.info("Testing " + Argentina.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(16,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testArgentinaSettlementYear2005() {
        int year = 2005;        
        logger.info("Testing " + Argentina.Market.SETTLEMENT + " holiday list for the year " + year + "...");
    
        expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(30,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testArgentinaSettlementYear2006() {
        int year = 2006;        
        logger.info("Testing " + Argentina.Market.SETTLEMENT + " holiday list for the year " + year + "...");
    
        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(19,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(16,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testArgentinaSettlementYear2007() {
        int year = 2007;        
        logger.info("Testing " + Argentina.Market.SETTLEMENT + " holiday list for the year " + year + "...");
    
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testArgentinaSettlementYear2008() {
        int year = 2008;        
        logger.info("Testing " + Argentina.Market.SETTLEMENT + " holiday list for the year " + year + "...");
    
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(20,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(16,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testArgentinaSettlementYear2009() {
        int year = 2009;        
        logger.info("Testing " + Argentina.Market.SETTLEMENT + " holiday list for the year " + year + "...");
    
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(17,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testArgentinaSettlementYear2010() {
        int year = 2010;        
        logger.info("Testing " + Argentina.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(16,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
                
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testArgentinaSettlementYear2011() {
        int year = 2011;        
        logger.info("Testing " + Argentina.Market.SETTLEMENT + " holiday list for the year " + year + "...");
    
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(30,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testArgentinaSettlementYear2012() {
        int year = 2012;        
        logger.info("Testing " + Argentina.Market.SETTLEMENT + " holiday list for the year " + year + "...");
    
        // expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); --> Sunday
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
}
