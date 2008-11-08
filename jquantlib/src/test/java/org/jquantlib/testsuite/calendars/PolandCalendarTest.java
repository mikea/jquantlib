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

import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.DECEMBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Poland;
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

public class PolandCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(PolandCalendarTest.class);

    private Calendar c= Poland.getCalendar(Poland.Market.WSE);
	
	public PolandCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	// 2004 - year in the past
	@Test
    public void testPolandWSEHolidaysYear2004()
    {    	
       	int year = 2004;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2005 - year in the past
	@Test
    public void testPolandWSEHolidaysYear2005()
    {    	
       	int year = 2005;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2006 - year in the past
	@Test
    public void testPolandWSEHolidaysYear2006()
    {    	
       	int year = 2006;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2007 - year in the past
	@Test
    public void testPolandWSEHolidaysYear2007()
    {    	
       	int year = 2007;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(7,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2008 - Current Year
	@Test
    public void testPolandWSEHolidaysYear2008()
    {    	
       	int year = 2008;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2009 - Year in the Future
	@Test
    public void testPolandWSEHolidaysYear2009()
    {    	
       	int year = 2009;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2010 - Year in the Future
	@Test
    public void testPolandWSEHolidaysYear2010()
    {    	
       	int year = 2010;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2011 - Year in the Future
	@Test
    public void testPolandWSEHolidaysYear2011()
    {    	
       	int year = 2011;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2012 - Year in the Future
	@Test
    public void testPolandWSEHolidaysYear2012()
    {    	
       	int year = 2012;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(7,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
        
    
}
