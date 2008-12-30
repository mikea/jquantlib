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
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;

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
 * @author Richard Gomes
 */

public class PolandCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(PolandCalendarTest.class);

    private Calendar settlement = Poland.getCalendar(Poland.Market.Settlement);
    private Calendar exchange   = Poland.getCalendar(Poland.Market.WSE);
	
	public PolandCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	// 2004 - Settlement trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr 11	Easter Day
	//	Apr 12	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 30	Whit Sunday
	//	Jun 10	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandSettlementHolidaysYear2004()
    {    	
       	int year = 2004;
    	logger.info("Testing " + Poland.Market.Settlement + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(11,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(30,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,JUNE,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, settlement, year);
   
    }
	
	
	// 2005 - Settlement trading holidays
	//
	//	Jan  1	New Year's Day
	//	Mar 27	Easter Day
	//	Mar 28	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 15	Whit Sunday
	//	May 26	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandSettlementHolidaysYear2005()
    {    	
       	int year = 2005;
    	logger.info("Testing " + Poland.Market.Settlement + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(27,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(15,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, settlement, year);
   
    }
	
	
	// 2006 - Settlement trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr 16	Easter Day
	//	Apr 17	testPolandWSEHolidaysYear2006Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	Jun  4	Whit Sunday
	//	Jun 15	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandSettlementHolidaysYear2006()
    {    	
       	int year = 2006;
    	logger.info("Testing " + Poland.Market.Settlement + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(16,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 4,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, settlement, year);
   
    }
	
	
	// 2007 - Settlement trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr  8	Easter Day
	//	Apr  9	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 27	Whit Sunday
	//	Jun  7	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandSettlementHolidaysYear2007()
    {    	
       	int year = 2007;
    	logger.info("Testing " + Poland.Market.Settlement + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(27,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 7,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, settlement, year);
   
    }
	
	
	// 2008 - Settlement trading holidays
	//
	//	Jan  1	New Year's Day
	//	Mar 23	Easter Day
	//	Mar 24	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 11	Whit Sunday
	//	May 22	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandSettlementHolidaysYear2008()
    {    	
       	int year = 2008;
    	logger.info("Testing " + Poland.Market.Settlement + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(11,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, settlement, year);
   
    }
	
	
	// 2009 - Settlement trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr 12	Easter Day
	//	Apr 13	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 31	Whit Sunday
	//	Jun 11	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandSettlementHolidaysYear2009()
    {    	
       	int year = 2009;
    	logger.info("Testing " + Poland.Market.Settlement + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(31,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, settlement, year);
   
    }
	
	
	// 2010 - Settlement trading holidays
	//
	//	Jan 1	New Year's Day
	//	Apr 4	Easter Day
	//	Apr 5	Easter Monday
	//	May 1	State Holiday
	//	May 3	Constitution Day
	//	May 23	Whit Sunday
	//	Jun 3	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov 1	All Saints
	//	Nov 11	Independence Day
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandSettlementHolidaysYear2010()
    {    	
       	int year = 2010;
    	logger.info("Testing " + Poland.Market.Settlement + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 4,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 5,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(23,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,JUNE,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, settlement, year);
   
    }

	
	// 2011 - Settlement trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr 24	Easter Day
	//	Apr 25	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	Jun 12	Whit Sunday
	//	Jun 23	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day

	@Test
    public void testPolandSettlementHolidaysYear2011()
    {    	
       	int year = 2011;
    	logger.info("Testing " + Poland.Market.Settlement + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(24,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(12,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, settlement, year);
   
    }
	
	
	// 2012 - Settlement trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr  8	Easter Day
	//	Apr  9	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 27	Whit Sunday
	//	Jun  7	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandSettlementHolidaysYear2012()
    {    	
       	int year = 2012;
    	logger.info("Testing " + Poland.Market.Settlement + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 8,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(27,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 7,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, settlement, year);
    }
        
    




	// 2004 - WSE trading holidays
	//
	//	Jan  1	New Year's Day
	//	Jan  2	New Year's Day (Friday gap)
	//	Apr  9	Good Friday
	//	Apr 11	Easter Day
	//	Apr 12	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 30	Whit Sunday
	//	Jun 10	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 24	Christmas Eve
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandWSEHolidaysYear2004()
    {    	
       	int year = 2004;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 9,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(11,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(30,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,JUNE,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
   
    }
	
	
	// 2005 - WSE trading holidays
	//
	//	Jan  1	New Year's Day
	//	Mar 25	Good Friday
	//	Mar 27	Easter Day
	//	Mar 28	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 15	Whit Sunday
	//	May 26	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 24	Christmas Eve
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandWSEHolidaysYear2005()
    {    	
       	int year = 2005;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(27,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(15,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
   
    }
	
	
	// 2006 - WSE trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr 14	Good Friday
	//	Apr 16	Easter Day
	//	Apr 17	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	Jun  4	Whit Sunday
	//	Jun 15	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 24	Christmas Eve
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandWSEHolidaysYear2006()
    {    	
       	int year = 2006;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(16,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 4,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
   
    }
	
	
	// 2007 - WSE trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr  6	Good Friday
	//	Apr  8	Easter Day
	//	Apr  9	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 27	Whit Sunday
	//	Jun  7	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 24	Christmas Eve
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandWSEHolidaysYear2007()
    {    	
       	int year = 2007;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 6,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(27,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 7,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
   
    }
	
	
	// 2008 - WSE trading holidays
	//
	//	Jan  1	New Year's Day
	//	Mar 21	Good Friday
	//	Mar 23	Easter Day
	//	Mar 24	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 11	Whit Sunday
	//	May 22	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 24	Christmas Eve
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandWSEHolidaysYear2008()
    {    	
       	int year = 2008;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
    	expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(11,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
   
    }
	
	
	// 2009 - WSE trading holidays
	//
	//	Jan  1	New Year's Day
	//	Jan  2	New Year's Day // Friday gap
	//	Apr 10	Good Friday
	//	Apr 12	Easter Day
	//	Apr 13	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 31	Whit Sunday
	//	Jun 11	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 24	Christmas Eve
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandWSEHolidaysYear2009()
    {    	
       	int year = 2009;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 2,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(31,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
   
    }
	
	
	// 2010 - WSE trading holidays
	//
	//	Jan 1	New Year's Day
	//	Apr 2	Good Friday
	//	Apr 4	Easter Day
	//	Apr 5	Easter Monday
	//	May 1	State Holiday
	//	May 3	Constitution Day
	//	May 23	Whit Sunday
	//	Jun 3	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov 1	All Saints
	//	Nov 11	Independence Day
	//	Dec 24	Christmas Eve
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandWSEHolidaysYear2010()
    {    	
       	int year = 2010;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 2,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 4,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 5,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(23,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,JUNE,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
   
    }

	
	// 2011 - WSE trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr 22	Good Friday
	//	Apr 24	Easter Day
	//	Apr 25	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	Jun 12	Whit Sunday
	//	Jun 23	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 24	Christmas Eve
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day

	@Test
    public void testPolandWSEHolidaysYear2011()
    {    	
       	int year = 2011;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(24,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(12,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(23,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
   
    }
	
	
	// 2012 - WSE trading holidays
	//
	//	Jan  1	New Year's Day
	//	Apr  6	Good Friday
	//	Apr  8	Easter Day
	//	Apr  9	Easter Monday
	//	May  1	State Holiday
	//	May  3	Constitution Day
	//	May 27	Whit Sunday
	//	Jun  7	Corpus Christi
	//	Aug 15	Assumption of Mary
	//	Nov  1	All Saints
	//	Nov 11	Independence Day
	//	Dec 24	Christmas Eve
	//	Dec 25	Christmas Day
	//	Dec 26	Boxing Day
	
	@Test
    public void testPolandWSEHolidaysYear2012()
    {    	
       	int year = 2012;
    	logger.info("Testing " + Poland.Market.WSE + " holidays list for the year " + year + "...");
    	List<Date> expectedHol = new Vector<Date>();
    
    	// expectedHol.add(DateFactory.getFactory().getDate( 1,JANUARY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 6,APRIL,year));
    	// expectedHol.add(DateFactory.getFactory().getDate( 8,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 9,APRIL,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 3,MAY,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(27,MAY,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 7,JUNE,year));
    	expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
    	expectedHol.add(DateFactory.getFactory().getDate( 1,NOVEMBER,year));
    	// expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
    	expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
    	    	
    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }
        
    
}
