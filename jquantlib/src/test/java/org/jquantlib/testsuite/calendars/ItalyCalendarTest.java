package org.jquantlib.testsuite.calendars;


import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.NOVEMBER;
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

		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		
		Date goodFriday = DateFactory.getFactory().getDate(9,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
		Date christmasEve = DateFactory.getFactory().getDate(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		Date newYearEve = DateFactory.getFactory().getDate(31,DECEMBER,year); 
		expectedHol.add(newYearEve);
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);
		
		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);
		
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year));
		expectedHol.add(DateFactory.getFactory().getDate(2,JUNE,year));
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
		expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2006() {
		int year = 2006;
		logger.info("Testing Italy holiday list for the year " + year);   
 
		Date goodFriday = DateFactory.getFactory().getDate(14,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);
		
		expectedHol.remove(goodFriday);
		
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year));
		expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
		expectedHol.add(DateFactory.getFactory().getDate(2,JUNE,year));
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
		expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2007() {
		int year = 2007;
		logger.info("Testing Italy holiday list for the year " + year);   

		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		Date goodFriday = DateFactory.getFactory().getDate(6,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
		Date christmasEve = DateFactory.getFactory().getDate(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		Date newYearEve = DateFactory.getFactory().getDate(31,DECEMBER,year);
		expectedHol.add(newYearEve);
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);
				
		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);
		
		expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
		expectedHol.add(DateFactory.getFactory().getDate(1,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
	
	@Test
	public void testItalyYear2008() {
		int year = 2008;
		logger.info("Testing Italy holiday list for the year " + year);   

		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		Date goodFriday = DateFactory.getFactory().getDate(21,MARCH,year); 
		expectedHol.add(goodFriday);
		expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		expectedHol.add(DateFactory.getFactory().getDate(15,AUGUST,year));
		Date christmasEve = DateFactory.getFactory().getDate(24,DECEMBER,year);
		expectedHol.add(christmasEve); 
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		Date newYearEve = DateFactory.getFactory().getDate(31,DECEMBER,year);
		expectedHol.add(newYearEve);
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);
		
		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);
		
		expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
		expectedHol.add(DateFactory.getFactory().getDate(2,JUNE,year));
		expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
	
	@Test
	public void testItalyYear2009() {
		int year = 2009;
		logger.info("Testing Italy holiday list for the year " + year);   

		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		Date goodFriday = DateFactory.getFactory().getDate(10,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		Date christmasEve = DateFactory.getFactory().getDate(24,DECEMBER,year); 
		expectedHol.add(christmasEve);
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		Date newYearEve = DateFactory.getFactory().getDate(31,DECEMBER,year);
		expectedHol.add(newYearEve);
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);
		
		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);
		
		expectedHol.add(DateFactory.getFactory().getDate(6,JANUARY,year));
		expectedHol.add(DateFactory.getFactory().getDate(2,JUNE,year));
		expectedHol.add(DateFactory.getFactory().getDate(8,DECEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
}