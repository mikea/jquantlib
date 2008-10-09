/*
 Copyright (C) 2008 Tim Swetonic
 
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
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Australia;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Tim Swetonic
 * @author Jia Jia
 *
 */


public class AustraliaCalendarTest {
	private Calendar c;
	List<Date> expectedHol;
	
    
    public AustraliaCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
    @Before
    public void setUp() {
        c = Australia.getCalendar();
        expectedHol = new Vector<Date>();
    }      
    
    // 2008 - current year
    @Test
	public void testAustraliaYear2008() {
      	int year = 2008;
      	System.out.println("Testing Australia holiday list for the year " + year + "...");
    	
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(28,JANUARY,year));
    	
    	//good friday
    	expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
    	//easter monday
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
    	
    	expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(4,AUGUST,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(6,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
    
    @Test
    public void testAustraliaYear2004() {       
        int year = 2004;
        System.out.println("Testing Australia holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year));        
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(14,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,AUGUST,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(4,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }
    
    @Test
    public void testAustraliaYear2005() {       
        int year = 2005;
        System.out.println("Testing Australia holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
        
        //good friday
        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
        
        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }
    
    @Test
    public void testAustraliaYear2006() {       
        int year = 2006;
        System.out.println("Testing Australia holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));               
        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,AUGUST,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(2,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));

        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }
    
    @Test
    public void testAustraliaYear2007() {       
        int year = 2007;
        System.out.println("Testing Australia holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year));
        
        //good friday
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,AUGUST,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(1,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));

        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }   
    
    @Test
    public void testAustraliaYear2009() {       
        int year = 2009;
        System.out.println("Testing Australia holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));                
        expectedHol.add(DateFactory.getFactory().getDate(8,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,AUGUST,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(5,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year));

        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }
    
    @Test
    public void testAustraliaYear2010() {       
        int year = 2010;
        System.out.println("Testing Australia holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));        
        expectedHol.add(DateFactory.getFactory().getDate(26,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(14,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,AUGUST,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(4,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year));

        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }
    
    @Test
    public void testAustraliaYear2011() {       
        int year = 2011;
        System.out.println("Testing Australia holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));               
        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));

        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }
    
    @Test
    public void testAustraliaYear2012() {       
        int year = 2012;
        System.out.println("Testing Australia holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(26,JANUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,AUGUST,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(1,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }   
    
}

