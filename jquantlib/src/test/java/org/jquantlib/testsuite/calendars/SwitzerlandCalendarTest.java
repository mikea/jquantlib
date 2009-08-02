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
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Switzerland;
import org.jquantlib.time.calendars.Switzerland.Market;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 * @author Renjith Nair
 * <p>
 * <strong>Description</strong><br>
 * Switzerland Calendar Test.
 */

public class SwitzerlandCalendarTest {
	
    private final static Logger logger = LoggerFactory.getLogger(SwitzerlandCalendarTest.class);

	public SwitzerlandCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	
	@Test
	public void testSwitzerlandSWXCalendar() {
		
		logger.info("\n\n=== Switzerland SWX Calendar ===");
		
		Market market = Switzerland.Market.SWX;
		
		Calendar c = Switzerland.getCalendar(market);	
		
		testSwitzerlandCalendar(market, c);
	}	
	
	@Test
	public void testSwitzerlandSettlementCalendar() {
		
		logger.info("\n\n=== Switzerland Settlement Calendar ===");
		
		Market market = Switzerland.Market.Settlement;
		
		Calendar c = Switzerland.getCalendar(market);		
		
		testSwitzerlandCalendar(market, c);
	} 
	
	private void testSwitzerlandCalendar(Market market, Calendar c) {
		// 2004 - leap-year in the past
		testSwitzerland2004(c,market);
		
		// 2005 - year in the past
		testSwitzerland2005(c,market);
		
		// 2006 - year in the past
		testSwitzerland2006(c,market);
		
		// 2007 - regular year in the past
		testSwitzerland2007(c,market);
		
		// 2008 - current year 
		testSwitzerland2008(c,market);
		
		// 2009 - regular year in the future
		testSwitzerland2009(c,market);
		
		// 2010 -  year in the future
		testSwitzerland2010(c,market);
		
		// 2011 -  year in the future
		testSwitzerland2011(c,market);
		
		// 2012 - leap-year in the future
		testSwitzerland2012(c,market);
	} 
	
	public void testSwitzerland2004(Calendar c, Market market){
		
		int year = 2004;		
		logger.info("Testing " + market + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,MAY,year)); --> Saturday
    	expectedHol.add(DateFactory.getFactory().getDate(20,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(31,MAY,year));
    	
    	if(market == Switzerland.Market.Settlement)
    	{
	    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
	    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
    	}
    	// expectedHol.add(DateFactory.getDateUtil().getDate(26,DECEMBER,year)); --> Sunday
	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year); 
	}
	
	public void testSwitzerland2005(Calendar c, Market market){
		
		int year = 2005;
		logger.info("Testing " + market + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
    	expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,MAY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(16,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year); 
	}
	
	public void testSwitzerland2006(Calendar c, Market market){
		
		int year = 2006;
		logger.info("Testing " + market + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(5,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year); 
	}
	
	public void testSwitzerland2007(Calendar c, Market market) {
	
		int year = 2007;
		logger.info("Testing " + market + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	
    	if(market == Switzerland.Market.Settlement)
    	{
	    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
	    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
    	}
    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year);
	}
	
	public void testSwitzerland2008(Calendar c, Market market){
		
		int year = 2008;
		logger.info("Testing " + market + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	
    	if(market == Switzerland.Market.Settlement)
    	{
	    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
	    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
    	}
    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year); 
	}
	
	public void testSwitzerland2009(Calendar c, Market market) {
		
		int year = 2009;
		logger.info("Testing " + market + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(01,JUNE,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(1,AUGUST,year)); --> Saturday
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	// expectedHol.add(DateFactory.getDateUtil().getDate(26,DECEMBER,year)); --> Saturday
    	
    	if(market == Switzerland.Market.Settlement)
    	{
	    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
	    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
    	}
    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year); 
	}
	
	public void testSwitzerland2010(Calendar c, Market market){
		
		int year = 2010;
		logger.info("Testing " + market + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,MAY,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(24,MAY,year));
    	
    	if(market == Switzerland.Market.Settlement)
    	{
	    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
	    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
    	}
    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year); 
	}
	
	public void testSwitzerland2011(Calendar c, Market market){
		
		int year = 2011;
		logger.info("Testing " + market + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
    	expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(2,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year); 
	}
	
	public void testSwitzerland2012(Calendar c, Market market) {
		int year = 2012;
		logger.info("Testing " + market + " holiday list for the year " + year + "...");
		
		List<Date> expectedHol = new Vector<Date>();
		
		// expectedHol.add(DateFactory.getDateUtil().getDate(1,JANUARY,year)); --> Sunday
		expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year)); 
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year)); 
    	
    	if(market == Switzerland.Market.Settlement)
    	{
	    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year)); 
	    	expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
    	}
    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, c, year); 
	}
}
