package org.jquantlib.testsuite.calendars;


import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.AUGUST;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.NOVEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Italy;
import org.junit.Test;

/**
 * @author Minh Do
 *
 */
public class ItalyCalendarTest {

	private final Calendar cExchange;
	private final Calendar cSettlement;


	public ItalyCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		cExchange		= Italy.getCalendar(Italy.Market.EXCHANGE);
		cSettlement	= Italy.getCalendar(Italy.Market.SETTLEMENT);
	}


	@Test
	public void testItalyYear2004() {
		final int year = 2004;
		QL.info("Testing Italy holiday list for the year " + year);
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1,JANUARY,year));

		final Date goodFriday = new Date(9,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(new Date(12,APRIL,year));
		final Date christmasEve = new Date(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		final Date newYearEve = new Date(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(new Date(6,JANUARY,year));
		expectedHol.add(new Date(2,JUNE,year));
		expectedHol.add(new Date(1,NOVEMBER,year));
		expectedHol.add(new Date(8,DECEMBER,year));
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2006() {
		final int year = 2006;
		QL.info("Testing Italy holiday list for the year " + year);
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		final Date goodFriday = new Date(14,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(new Date(17,APRIL,year));
		expectedHol.add(new Date(1,MAY,year));
		expectedHol.add(new Date(15,AUGUST,year));
		expectedHol.add(new Date(25,DECEMBER,year));
		expectedHol.add(new Date(26,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);

		expectedHol.add(new Date(6,JANUARY,year));
		expectedHol.add(new Date(25,APRIL,year));
		expectedHol.add(new Date(2,JUNE,year));
		expectedHol.add(new Date(1,NOVEMBER,year));
		expectedHol.add(new Date(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2007() {
		final int year = 2007;
		QL.info("Testing Italy holiday list for the year " + year);
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1,JANUARY,year));
		final Date goodFriday = new Date(6,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(new Date(9,APRIL,year));
		expectedHol.add(new Date(1,MAY,year));
		expectedHol.add(new Date(15,AUGUST,year));
		final Date christmasEve = new Date(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		expectedHol.add(new Date(25,DECEMBER,year));
		expectedHol.add(new Date(26,DECEMBER,year));
		final Date newYearEve = new Date(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(new Date(25,APRIL,year));
		expectedHol.add(new Date(1,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2008() {
		final int year = 2008;
		QL.info("Testing Italy holiday list for the year " + year);
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1,JANUARY,year));
		final Date goodFriday = new Date(21,MARCH,year);
		expectedHol.add(goodFriday);
		expectedHol.add(new Date(24,MARCH,year));
		expectedHol.add(new Date(1,MAY,year));
		expectedHol.add(new Date(15,AUGUST,year));
		final Date christmasEve = new Date(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		expectedHol.add(new Date(25,DECEMBER,year));
		expectedHol.add(new Date(26,DECEMBER,year));
		final Date newYearEve = new Date(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(new Date(25,APRIL,year));
		expectedHol.add(new Date(2,JUNE,year));
		expectedHol.add(new Date(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2009() {
		final int year = 2009;
		QL.info("Testing Italy holiday list for the year " + year);
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1,JANUARY,year));
		final Date goodFriday = new Date(10,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(new Date(13,APRIL,year));
		expectedHol.add(new Date(1,MAY,year));
		final Date christmasEve = new Date(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		expectedHol.add(new Date(25,DECEMBER,year));
		final Date newYearEve = new Date(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(new Date(6,JANUARY,year));
		expectedHol.add(new Date(2,JUNE,year));
		expectedHol.add(new Date(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2010() {
		final int year = 2010;
		QL.info("Testing Italy holiday list for the year " + year);
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1,JANUARY,year));
		final Date goodFriday = new Date(2,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(new Date(5,APRIL,year));
		final Date christmasEve = new Date(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		final Date newYearEve = new Date(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(new Date(6,JANUARY,year));
		expectedHol.add(new Date(2,JUNE,year));
		expectedHol.add(new Date(1,NOVEMBER,year));
		expectedHol.add(new Date(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
	@Test
	public void testItalyYear2011() {

		final int year = 2011;
		QL.info("Testing Italy holiday list for the year " + year);
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		final Date goodFriday = new Date(22,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(new Date(25,APRIL,year));
		expectedHol.add(new Date(15,AUGUST,year));
		expectedHol.add(new Date(26,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);

		expectedHol.add(new Date(6,JANUARY,year));
		expectedHol.add(new Date(2,JUNE,year));
		expectedHol.add(new Date(1,NOVEMBER,year));
		expectedHol.add(new Date(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}


	@Test
	public void testItalyYear2012() {
		final int year = 2012;
		QL.info("Testing Italy holiday list for the year " + year);
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		final Date goodFriday = new Date(6,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(new Date(9,APRIL,year));
		expectedHol.add(new Date(1,MAY,year));

		expectedHol.add(new Date(15, AUGUST,year));
		final Date christmasEve = new Date(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		expectedHol.add(new Date(25,DECEMBER,year));
		expectedHol.add(new Date(26,DECEMBER,year));

		final Date newYearEve = new Date(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(new Date(6,JANUARY,year));
		expectedHol.add(new Date(25,APRIL,year));
		expectedHol.add(new Date(1,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
}