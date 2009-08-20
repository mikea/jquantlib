package org.jquantlib.testsuite.calendars;


import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Italy;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
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
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(df.getDate(1,JANUARY,year));

		final Date goodFriday = df.getDate(9,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(df.getDate(12,APRIL,year));
		final Date christmasEve = df.getDate(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		final Date newYearEve = df.getDate(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(df.getDate(6,JANUARY,year));
		expectedHol.add(df.getDate(2,JUNE,year));
		expectedHol.add(df.getDate(1,NOVEMBER,year));
		expectedHol.add(df.getDate(8,DECEMBER,year));
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2006() {
		final int year = 2006;
		QL.info("Testing Italy holiday list for the year " + year);
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		final Date goodFriday = df.getDate(14,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(df.getDate(17,APRIL,year));
		expectedHol.add(df.getDate(1,MAY,year));
		expectedHol.add(df.getDate(15,AUGUST,year));
		expectedHol.add(df.getDate(25,DECEMBER,year));
		expectedHol.add(df.getDate(26,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);

		expectedHol.add(df.getDate(6,JANUARY,year));
		expectedHol.add(df.getDate(25,APRIL,year));
		expectedHol.add(df.getDate(2,JUNE,year));
		expectedHol.add(df.getDate(1,NOVEMBER,year));
		expectedHol.add(df.getDate(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2007() {
		final int year = 2007;
		QL.info("Testing Italy holiday list for the year " + year);
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(df.getDate(1,JANUARY,year));
		final Date goodFriday = df.getDate(6,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(df.getDate(9,APRIL,year));
		expectedHol.add(df.getDate(1,MAY,year));
		expectedHol.add(df.getDate(15,AUGUST,year));
		final Date christmasEve = df.getDate(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		expectedHol.add(df.getDate(25,DECEMBER,year));
		expectedHol.add(df.getDate(26,DECEMBER,year));
		final Date newYearEve = df.getDate(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(df.getDate(25,APRIL,year));
		expectedHol.add(df.getDate(1,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2008() {
		final int year = 2008;
		QL.info("Testing Italy holiday list for the year " + year);
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(df.getDate(1,JANUARY,year));
		final Date goodFriday = df.getDate(21,MARCH,year);
		expectedHol.add(goodFriday);
		expectedHol.add(df.getDate(24,MARCH,year));
		expectedHol.add(df.getDate(1,MAY,year));
		expectedHol.add(df.getDate(15,AUGUST,year));
		final Date christmasEve = df.getDate(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		expectedHol.add(df.getDate(25,DECEMBER,year));
		expectedHol.add(df.getDate(26,DECEMBER,year));
		final Date newYearEve = df.getDate(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(df.getDate(25,APRIL,year));
		expectedHol.add(df.getDate(2,JUNE,year));
		expectedHol.add(df.getDate(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2009() {
		final int year = 2009;
		QL.info("Testing Italy holiday list for the year " + year);
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(df.getDate(1,JANUARY,year));
		final Date goodFriday = df.getDate(10,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(df.getDate(13,APRIL,year));
		expectedHol.add(df.getDate(1,MAY,year));
		final Date christmasEve = df.getDate(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		expectedHol.add(df.getDate(25,DECEMBER,year));
		final Date newYearEve = df.getDate(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(df.getDate(6,JANUARY,year));
		expectedHol.add(df.getDate(2,JUNE,year));
		expectedHol.add(df.getDate(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testItalyYear2010() {
		final int year = 2010;
		QL.info("Testing Italy holiday list for the year " + year);
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(df.getDate(1,JANUARY,year));
		final Date goodFriday = df.getDate(2,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(df.getDate(5,APRIL,year));
		final Date christmasEve = df.getDate(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		final Date newYearEve = df.getDate(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(df.getDate(6,JANUARY,year));
		expectedHol.add(df.getDate(2,JUNE,year));
		expectedHol.add(df.getDate(1,NOVEMBER,year));
		expectedHol.add(df.getDate(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
	@Test
	public void testItalyYear2011() {

		final int year = 2011;
		QL.info("Testing Italy holiday list for the year " + year);
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		final Date goodFriday = df.getDate(22,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(df.getDate(25,APRIL,year));
		expectedHol.add(df.getDate(15,AUGUST,year));
		expectedHol.add(df.getDate(26,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);

		expectedHol.add(df.getDate(6,JANUARY,year));
		expectedHol.add(df.getDate(2,JUNE,year));
		expectedHol.add(df.getDate(1,NOVEMBER,year));
		expectedHol.add(df.getDate(8,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}


	@Test
	public void testItalyYear2012() {
		final int year = 2012;
		QL.info("Testing Italy holiday list for the year " + year);
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

		final Date goodFriday = df.getDate(6,APRIL,year);
		expectedHol.add(goodFriday);
		expectedHol.add(df.getDate(9,APRIL,year));
		expectedHol.add(df.getDate(1,MAY,year));

		expectedHol.add(df.getDate(15, AUGUST,year));
		final Date christmasEve = df.getDate(24,DECEMBER,year);
		expectedHol.add(christmasEve);
		expectedHol.add(df.getDate(25,DECEMBER,year));
		expectedHol.add(df.getDate(26,DECEMBER,year));

		final Date newYearEve = df.getDate(31,DECEMBER,year);
		expectedHol.add(newYearEve);

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cExchange, year);

		expectedHol.remove(goodFriday);
		expectedHol.remove(christmasEve);
		expectedHol.remove(newYearEve);

		expectedHol.add(df.getDate(6,JANUARY,year));
		expectedHol.add(df.getDate(25,APRIL,year));
		expectedHol.add(df.getDate(1,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
}