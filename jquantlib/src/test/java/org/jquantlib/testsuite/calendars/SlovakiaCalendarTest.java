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

import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.SEPTEMBER;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.APRIL;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Slovakia;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author 
 *
 *
 */

public class SlovakiaCalendarTest {

	public SlovakiaCalendarTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
    public void testSlovakia() {
                       
    	Calendar c = Slovakia.getCalendar(Slovakia.Market.BSSE);
    	
        
		// 2005 
        testSlovakiaYear2005(c);
		
		// 2004 
        testSlovakiaYear2004(c);
    }
   
	// 2005 
	
	void testSlovakiaYear2005(Calendar c) {
      	int year = 2005;
      	System.out.println("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		//New Year -- 1st Jan weekend in yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	
		//Epiphany, January 6th
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year)); 
		
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year)); 
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year)); 
		
		//May Day, May 1st -- weekend in yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		
		//Liberation of the Republic, May 8th -- weekend in  yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
		
		//SS. Cyril and Methodius, July 5th
		expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
		//Slovak National Uprising, August 29th
		expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,year)); 
		//Constitution of the Slovak Republic, September 1st
    	expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year)); 
		
		//Our Lady of the Seven Sorrows, September 15th
		expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year)); 
		//All Saints Day, November 1st
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year)); 
		//Freedom and Democracy of the Slovak Republic, November 17th
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year)); 
		//Christmas Eve, December 24th -- weekend in yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
		//Christmas, December 25th -- weekend in yr 2005
		//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
		//St. Stephen, December 26th
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year)); 
		//unidentified stock exchange closing days 24-31 Dec, 24,25,31 Dec weekend in yr 2005
		expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(30,DECEMBER,year)); 
		//expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
	//2004
	void testSlovakiaYear2004(Calendar c) {
      	int year = 2004;
      	System.out.println("Testing Solvakia's holiday list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
		//New Year -- 1st Jan 
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
    	
		//Epiphany, January 6th
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year)); 
		
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year)); 
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year)); 
		
		//May Day, May 1st -- weekend in yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year)); 
		
		//Liberation of the Republic, May 8th -- weekend in  yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(8,MAY,year)); 
		
		//SS. Cyril and Methodius, July 5th
		expectedHol.add(DateFactory.getFactory().getDate(5,JULY,year)); 
		//Slovak National Uprising, August 29th -- weekend in yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(29,AUGUST,year)); 
		//Constitution of the Slovak Republic, September 1st
    	expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year)); 
		
		//Our Lady of the Seven Sorrows, September 15th
		expectedHol.add(DateFactory.getFactory().getDate(15,SEPTEMBER,year)); 
		//All Saints Day, November 1st
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year)); 
		//Freedom and Democracy of the Slovak Republic, November 17th
		expectedHol.add(DateFactory.getFactory().getDate(17,NOVEMBER,year)); 
		//Christmas Eve, December 24th 
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
		//Christmas, December 25th -- weekend in yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year)); 
		//St. Stephen, December 26th -- weekend in yr 2004
		//expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year)); 
		//unidentified stock exchange closing days 24-31 Dec, 24,25,26 Dec weekend in yr 2005
		expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(29,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(30,DECEMBER,year)); 
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year)); 
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, c, year);
    }
}
