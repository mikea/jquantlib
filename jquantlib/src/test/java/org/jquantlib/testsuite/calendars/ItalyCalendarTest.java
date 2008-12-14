package org.jquantlib.testsuite.calendars;


import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Italy;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Minh Do
 * 
 */
public class ItalyCalendarTest {
    private final static Logger logger = LoggerFactory.getLogger(ItalyCalendarTest.class);

	private Calendar cExchange;
	private Calendar cSettlement;
	private List<Date> expectedHol;

	@Before
	public void setUp() {
		cExchange		= Italy.getCalendar(Italy.Market.EXCHANGE);
		cSettlement	= Italy.getCalendar(Italy.Market.SETTLEMENT);
		expectedHol = new Vector<Date>();
	}

	@Test
	public void testItalyYear2004() {
		int year = 2004;
		logger.info("Testing Italy holiday list for the year " + year);   

		//new years day 
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		
		//Good Friday
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
		
		//Easter Monday
		expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
		
		//labor day - no testing since it is a weekend day
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		
		//Assumption day - - no testing since it is a weekend day
		//expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
		
		//christmas
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		
        //No testing since it is a weekend day
		//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		
		//St. Stephen - - no testing since it is a weekend day
		//expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		
		//New Year's Eve
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);
		
	}

	@Test
	public void testItalyYear2006() {
		int year = 2006;
		logger.info("Testing Italy holiday list for the year " + year);   

		//new years day - no testing since it is a weekend day
		//expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		
		//Good Friday 
		expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
		
		//Easter Monday 
		expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
		
		//labor day 
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		
		//Assumption day 
		expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
		
		//christmas - no testing since it is a weekend day
		//expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		
        //Christmas day
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		
		//St. Stephen 
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		
		//New Year's Eve - no testing since it is a weekend day
		//expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);
		
	}

	@Test
	public void testItalyYear2007() {
		int year = 2007;
		logger.info("Testing Italy holiday list for the year " + year);   

		//new years day 
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		
		//Good Friday 
		expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
		
		//Easter Monday 
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
		
		//labor day 
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		
		//Assumption day 
		expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
		
		//christmas 
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		
        
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		
		//St. Stephen 
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		
		//New Year's Eve 
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);
		
	}
	
	@Test
	public void testItalyYear2008() {
		int year = 2008;
		logger.info("Testing Italy holiday list for the year " + year);   

		//new years day 
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		
		//Good Friday 
		expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
		
		//Easter Monday 
		expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
		
		//labor day 
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		
		//Assumption day 
		expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
		
		//christmas 
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		
        
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		
		//St. Stephen 
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		
		//New Year's Eve 
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);
		
	}
}