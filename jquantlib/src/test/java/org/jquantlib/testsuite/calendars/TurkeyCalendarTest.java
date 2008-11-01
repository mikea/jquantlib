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
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Turkey;
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

public class TurkeyCalendarTest {

    private final static Logger logger = Logger.getLogger(TurkeyCalendarTest.class);

    private Calendar c= null;
	private List<Date> expectedHol = null;
	
	public TurkeyCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Before
	public void setup(){
		c=Turkey.getCalendar(Turkey.Market.ISE);
		expectedHol = new Vector<Date>();
	}
	
        
    // 2004 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2004()
    {    	
       	int year = 2004;
    	logger.info("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(4,FEBRUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(16,NOVEMBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2005 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2005()
    {    	
       	int year = 2005;
    	logger.info("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(19,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(20,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(4,NOVEMBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2006 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2006()
    {    	
       	int year = 2006;
    	logger.info("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(9,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,OCTOBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2007 - year in the past
	@Test
    public void testTurkeyISEHolidaysYear2007()
    {    	
       	int year = 2007;
    	logger.info("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(4,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(19,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(20,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,DECEMBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2008 - Current Year
	@Test
    public void testTurkeyISEHolidaysYear2008()
    {    	
       	int year = 2008;
    	logger.info("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,SEPTEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,SEPTEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,DECEMBER,year));
    	
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2009 - Future Year
	@Test
    public void testTurkeyISEHolidaysYear2009()
    {    	
       	int year = 2009;
    	logger.info("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,OCTOBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2010 - Future Year
	@Test
    public void testTurkeyISEHolidaysYear2010()
    {    	
       	int year = 2010;
    	logger.info("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,OCTOBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2011 - Future Year
	@Test
    public void testTurkeyISEHolidaysYear2011()
    {    	
       	int year = 2011;
    	logger.info("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2012 - Future Year
	@Test
    public void testTurkeyISEHolidaysYear2012()
    {    	
       	int year = 2012;
    	logger.info("Testing " + Turkey.Market.ISE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(23,APRIL,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(30,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(29,OCTOBER,year));
  	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	
	
    @After
	public void destroy(){
		c=null;
		expectedHol = null;
	}
}
