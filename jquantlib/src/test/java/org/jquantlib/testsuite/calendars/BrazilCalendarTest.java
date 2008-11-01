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

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Brazil;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 * @author Jia Jia
 *
 */

public class BrazilCalendarTest {
    private final static Logger logger = Logger.getLogger(BrazilCalendarTest.class);

	private Calendar bovespa;
	private Calendar settlement;
	private List<Date> expectedHol;
    
	@Before
	public void setUp() {
	    bovespa = Brazil.getCalendar(Brazil.Market.BOVESPA);
	    settlement = Brazil.getCalendar(Brazil.Market.SETTLEMENT);
	    expectedHol = new Vector<Date>();
	}
        	
    // 2004 - leap-year in the past
	@Test
    public void testBrazilBovespaYear2004()
    {
       	int year = 2004;
    	logger.info("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(23,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year)); 
    
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bovespa, year);
   
    }
    
    // 2004 - leap-year in the past
    @Test
    public void testBrazilBovespaYear2005()
    {
        int year = 2005;
        logger.info("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
        List<Date> expectedHol = new Vector<Date>();
    
        expectedHol.add(DateFactory.getFactory().getDate(25,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(7,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(30,DECEMBER,year)); 
    
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bovespa, year);
   
    }
    
    // 2004 - leap-year in the past
    @Test
    public void testBrazilBovespaYear2006()
    {
        int year = 2006;
        logger.info("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
        List<Date> expectedHol = new Vector<Date>();
    
        expectedHol.add(DateFactory.getFactory().getDate(25,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(27,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(29,DECEMBER,year)); 
    
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bovespa, year);
   
    }
	
    // 2007 - regular year in the past
    @Test
    public void testBrazilBovespaYear2007() {
    	
    	int year = 2007;
    	logger.info("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(25,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(19,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bovespa, year);
    }    
    
    // 2008 - current year
    @Test
    public void testBrazilBovespaYear2008(){
      	int year = 2008;
      	logger.info("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(25,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(4,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,FEBRUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bovespa, year);
    }
    

    // 2009 - current year in the future
    @Test
    public void testBrazilBovespaYear2009() {
    	
    	int year = 2009;
    	logger.info("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(23,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bovespa, year);
    	
    }

    // 2004 - leap-year in the past
    @Test
    public void testBrazilBovespaYear2010()
    {
        int year = 2010;
        logger.info("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
        List<Date> expectedHol = new Vector<Date>();
    
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(15,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(16,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year)); 
    
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bovespa, year);
   
    }
    
    // 2004 - leap-year in the past
    @Test
    public void testBrazilBovespaYear2011()
    {
        int year = 2011;
        logger.info("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
        List<Date> expectedHol = new Vector<Date>();
    
        expectedHol.add(DateFactory.getFactory().getDate(25,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(7,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(23,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(30,DECEMBER,year)); 
    
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bovespa, year);
   
    }
    // 2012 - next leap-year in the future
    @Test
    public void testBrazilBovespaYear2012() {
    	int year = 2012;
    	logger.info("Testing " + Brazil.Market.BOVESPA + " holiday list for the year " + year + "...");
    	expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(25,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(20,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, bovespa, year);    	
    }
    
    @Test
    public void testBrazilSettlementYear2004() {
        int year = 2004;
        logger.info("Testing " + Brazil.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(23,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year)); 

        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);       
    }
        
    @Test
    public void testBrazilSettlementYear2005() {
        int year = 2005;
        logger.info("Testing " + Brazil.Market.SETTLEMENT + " holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(7,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year)); 
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);       
    }
    
    @Test
    public void testBrazilSettlementYear2006() {
        int year = 2006;
        logger.info("Testing " + Brazil.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(27,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);       
    }
    
    @Test
    public void testBrazilSettlementYear2007() {
        int year = 2007;
        logger.info("Testing " + Brazil.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(19,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(20,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);       
    }
    
    @Test
    public void testBrazilSettlementYear2008() {
        int year = 2008;
        logger.info("Testing " + Brazil.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(4,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);       
    }
    
    @Test
    public void testBrazilSettlementYear2009() {
        int year = 2009;
        logger.info("Testing " + Brazil.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(23,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);       
    }
    
    @Test
    public void testBrazilSettlementYear2010() {
        int year = 2010;
        logger.info("Testing " + Brazil.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(15,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(16,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year)); 
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);       
    }
    
    @Test
    public void testBrazilSettlementYear2011() {
        int year = 2011;
        logger.info("Testing " + Brazil.Market.SETTLEMENT + " holiday list for the year " + year + "...");
     
        expectedHol.add(DateFactory.getFactory().getDate(7,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(23,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year)); 
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);       
    }
    
    @Test
    public void testBrazilSettlementYear2012() {
        int year = 2012;
        logger.info("Testing " + Brazil.Market.SETTLEMENT + " holiday list for the year " + year + "...");
         
        expectedHol.add(DateFactory.getFactory().getDate(20,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);       
    }
    

}
