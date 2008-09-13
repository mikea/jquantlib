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

import static org.jquantlib.util.Month.*;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.CzechRepublic;
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

public class CzechRepublicCalendarTest {

	Calendar c= null;
	List<Date> expectedHol=null;
	public CzechRepublicCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Before
	public void setup(){
		c=CzechRepublic.getCalendar(CzechRepublic.Market.PSE);
		expectedHol = new Vector<Date>();
	}
	
	// 2004 - Leap Year & Extra Holidays
	@Test
    public void testCzechRepublicPSEHolidaysYear2004()
    {    	
       	int year = 2004;
    	System.out.println("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year)); //only for year 2004
    	expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(6,JULY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,SEPTEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year)); //only for year 2004
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
        
    // 2005 - year in the past
	@Test
    public void testCzechRepublicPSEHolidaysYear2005()
    {    	
       	int year = 2005;
    	System.out.println("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(6,JULY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,SEPTEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2008 - Current Year
	@Test
    public void testCzechRepublicPSEHolidaysYear2008()
    {    	
       	int year = 2008;
    	System.out.println("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(28,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2009 - Year in Future
	@Test
    public void testCzechRepublicPSEHolidaysYear2009()
    {    	
       	int year = 2009;
    	System.out.println("Testing " + CzechRepublic.Market.PSE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(6,JULY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(28,SEPTEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,OCTOBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
    
    @After
	public void destroy(){
		c=null;
		expectedHol=null;
	}
}
