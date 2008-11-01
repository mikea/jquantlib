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
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.India;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Renjith Nair
 *
 *
 */

public class IndiaCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(IndiaCalendarTest.class);

    private Calendar c= null;
	
	public IndiaCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Before
	public void setup(){
		c=India.getCalendar(India.Market.NSE);
	}
	
        
    // 2005 - year in the past
	@Test
    public void testIndiaNseHolidaysYear2005()
    {    	
       	int year = 2005;
    	logger.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(21,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2006 - regular year in the past
	@Test
    public void testIndiaNseHolidaysYear2006() {
    	
		int year = 2006;
    	logger.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    	
    	expectedHol.add(DateFactory.getFactory().getDate(11,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
    
	// 2007 - regular year in the past
	@Test
    public void testIndiaNseHolidaysYear2007() {
		int year = 2007;
    	logger.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
          	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(16,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(27,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    	
    }
	
	// 2008 - Current Year
	@Test
    public void testIndiaNseHolidaysYear2008() {
		int year = 2008;
    	logger.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
        	
    	expectedHol.add(DateFactory.getFactory().getDate(6,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(20,MARCH,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year)); //Good Friday
    	expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(18,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,SEPTEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    	
    }
    
	
	// 2020 - Future Year
	//Remember that the holidays for 2020 are not complete
	//so we test for only the general holidays
	@Test
    public void testIndiaNseHolidaysYear2020() {
		int year = 2020;
    	logger.info("Testing " + India.Market.NSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
        	
    	//expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year)); //Sunday
    	expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year)); // Good Friday 
    	expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year)); 
    	//expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year)); //Saturday
    	expectedHol.add(DateFactory.getFactory().getDate(2,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    	
    }
    
    @After
	public void destroy(){
		c=null;
	}
}
