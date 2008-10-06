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
import org.jquantlib.time.calendars.Ukraine;
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

public class UkraineCalendarTest {

	Calendar c= null;
	List<Date> expectedHol = null;
	public UkraineCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Before
	public void setup(){
		c=Ukraine.getCalendar(Ukraine.Market.USE);
		expectedHol = new Vector<Date>();
	}
	
        
    // 2004 - year in the past and leap year
	@Test
    public void testUkraineUSEHolidaysYear2004()
    {    	
       	int year = 2004;
    	System.out.println("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(7,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(3,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(31,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,AUGUST,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2005 - year in the past and leap year
	@Test
    public void testUkraineUSEHolidaysYear2005()
    {    	
       	int year = 2005;
    	System.out.println("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(3,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(7,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(16,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,AUGUST,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2006 - year in the past and leap year
	@Test
    public void testUkraineUSEHolidaysYear2006()
    {    	
       	int year = 2006;
    	System.out.println("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(9,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,AUGUST,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	// 2007 - year in the past and leap year
	@Test
    public void testUkraineUSEHolidaysYear2007()
    {    	
       	int year = 2007;
    	System.out.println("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(8,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(8,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,AUGUST,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	
	 // 2008 - Current Year
	@Test
    public void testUkraineUSEHolidaysYear2008()
    {    	
       	int year = 2008;
    	System.out.println("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(7,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,MAY,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
	 // 2020 - Future Year - Only Testing for general holidays
	@Test
    public void testUkraineUSEHolidaysYear2020()
    {    	
       	int year = 2020;
    	System.out.println("Testing " + Ukraine.Market.USE + " holidays list for the year " + year + "...");
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(7,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,AUGUST,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt= new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
   
    }
	
    @After
	public void destroy(){
		c=null;
		expectedHol = null;
	}
}
