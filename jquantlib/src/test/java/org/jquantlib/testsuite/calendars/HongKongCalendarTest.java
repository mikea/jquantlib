/*
 Copyright (C) 2008 
  
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
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.HongKong;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author
 *
 *
 */


public class HongKongCalendarTest {
	
    private final static Logger logger = LoggerFactory.getLogger(HongKongCalendarTest.class);

	public HongKongCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
    public void testHongKong() {
                       
    	Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
    	
        // 2008 - current year
        testHongKongYear2008(c);
		
		// 2007 
        testHongKongYear2007(c);
        
		// 2006 
        testHongKongYear2006(c);
		
		// 2005 
        testHongKongYear2005(c);
		
		// 2004 
        testHongKongYear2004(c);
    }
   
	// 2008 -- current year
	
	void testHongKongYear2008(Calendar c) {
      	int year = 2008;
      	logger.info("Testing Hong Kong's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		//New Year
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	//Chinese New Year 7->9 Feb
    	expectedHol.add(DateFactory.getFactory().getDate(7,FEBRUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(8,FEBRUARY,year)); 
		
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year)); 
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year)); 
		//Ching Ming
		expectedHol.add(DateFactory.getFactory().getDate(4,APRIL,year));    	    	
		
		//labour day
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));    	    	
		
		//Buddha's Birthday 24 May
		expectedHol.add(DateFactory.getFactory().getDate(12,MAY,year));    	    	
		//Tuen NG festival
		expectedHol.add(DateFactory.getFactory().getDate(9,JUNE,year));    
		
		// SAR Establishment Day, July 1st (possibly moved to Monday)
    	expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
    	// Mid-autumn festival
    	expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year));
		
		// National Day, October 1st (possibly moved to Monday)
		expectedHol.add(DateFactory.getFactory().getDate(1,OCTOBER,year));		
    	//Chung Yeung festival
    	expectedHol.add(DateFactory.getFactory().getDate(7,OCTOBER,year));
		
		//christmas 
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	// 2007 
	
	void testHongKongYear2007(Calendar c) {
      	int year = 2007;
      	logger.info("Testing Hong Kong's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		//New Year
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	//Chinese New Year 17-> 20 Feb
    	expectedHol.add(DateFactory.getFactory().getDate(19,FEBRUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(20,FEBRUARY,year)); 
		
		//Ching Ming
		expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));    	    	
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year)); 
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year)); 
		//labour day
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));    	    	
		
		//Buddha's Birthday 24 May
		expectedHol.add(DateFactory.getFactory().getDate(24,MAY,year));    	    	
		//Tuen NG festival
		expectedHol.add(DateFactory.getFactory().getDate(19,JUNE,year));    
		
		// SAR Establishment Day, July 1st (possibly moved to Monday)
    	expectedHol.add(DateFactory.getFactory().getDate(2,JULY,year));
    	// Mid-autumn festival
    	expectedHol.add(DateFactory.getFactory().getDate(26,SEPTEMBER,year));
		
		// National Day, October 1st (possibly moved to Monday)
		expectedHol.add(DateFactory.getFactory().getDate(1,OCTOBER,year));		
    	//Chung Yeung festival
    	expectedHol.add(DateFactory.getFactory().getDate(19,OCTOBER,year));
		
		//christmas 
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
    // 2006 
	
	void testHongKongYear2006(Calendar c) {
      	int year = 2006;
      	logger.info("Testing Hong Kong's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		//New Year
		expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year)); 
    	//Chinese New Year 28->31 Jan
    	expectedHol.add(DateFactory.getFactory().getDate(30,JANUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(31,JANUARY,year)); 
		
		//Ching Ming
		expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));    	    	
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year)); 
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year)); 
		//labour day
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		
		//Buddha's Birthday 5 May
		expectedHol.add(DateFactory.getFactory().getDate(5,MAY,year));    	    	
		//Tuen NG festival
		expectedHol.add(DateFactory.getFactory().getDate(31,MAY,year));    	    	
    	
		
		// SAR Establishment Day, July 1st (possibly moved to Monday)
    	expectedHol.add(DateFactory.getFactory().getDate(3,JULY,year));
    	
		// National Day, October 1st (possibly moved to Monday)
		expectedHol.add(DateFactory.getFactory().getDate(2,OCTOBER,year));	
		// Mid-autumn festival -- weekend in 2006
    	//expectedHol.add(DateFactory.getFactory().getDate(7,OCTOBER,year));
		//Chung Yeung festival
    	expectedHol.add(DateFactory.getFactory().getDate(30,OCTOBER,year));
    	
		//christmas 
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
    // 2005 
	
	
	void testHongKongYear2005(Calendar c) {
      	int year = 2005;
      	logger.info("Testing Hong Kong's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		
		//New Year
		expectedHol.add(DateFactory.getFactory().getDate(3,JANUARY,year)); 
    	//Chinese New Year 28->31 Jan
    	expectedHol.add(DateFactory.getFactory().getDate(9,FEBRUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(10,FEBRUARY,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(11,FEBRUARY,year)); 
		
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year)); 
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year)); 
		//Ching Ming
		expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year)); 
		//labour day -- weekend in 2005
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		
		//Buddha's Birthday 5 May
		expectedHol.add(DateFactory.getFactory().getDate(16,MAY,year));    	 

		// SAR Establishment Day, July 1st (possibly moved to Monday)
    	expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
    	
		// Mid-autumn festival -- weekend in 2006
    	//expectedHol.add(DateFactory.getFactory().getDate(7,OCTOBER,year));		
		//Tuen NG festival -- weekend in 2005
		//expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,year));    	    	
    	// Mid-autumn festival
    	expectedHol.add(DateFactory.getFactory().getDate(19,SEPTEMBER,year));
		// National Day, October 1st (possibly moved to Monday)
		expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));	
		
		//Chung Yeung festival
    	expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,year));
		
		//christmas -- weekend in 2005 
    	//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	// 2004 
	void testHongKongYear2004(Calendar c) {
      	int year = 2004;
      	logger.info("Testing Hong Kong's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		//New Year
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
		
    	//Chinese New Year 22->24 Jan
		expectedHol.add(DateFactory.getFactory().getDate(22,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(23,JANUARY,year)); 
		//Ching Ming
		expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year)); 
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year)); 
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year)); 
		
		//labour day -- weekend in 2004
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		//Buddha's Birthday 5 May
		expectedHol.add(DateFactory.getFactory().getDate(26,MAY,year));    	    	
		//Tuen NG festival
		expectedHol.add(DateFactory.getFactory().getDate(22,JUNE,year));    

		// SAR Establishment Day, July 1st (possibly moved to Monday)
    	expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
    	
		// Mid-autumn festival -- weekend in 2004
    	//expectedHol.add(DateFactory.getFactory().getDate(19,SEPTEMBER,year));
		
		
    	// Mid-autumn festival/Chung Yeung festival
    	expectedHol.add(DateFactory.getFactory().getDate(29,SEPTEMBER,year));
		// National Day, October 1st (possibly moved to Monday)
		expectedHol.add(DateFactory.getFactory().getDate(1,OCTOBER,year));	
		//christmas -- weekend in 2004 
    	//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day 
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
}

