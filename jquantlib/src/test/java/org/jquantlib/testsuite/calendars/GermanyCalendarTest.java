package org.jquantlib.testsuite.calendars;

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Germany;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Sangaran Sampanthan
 */
public class GermanyCalendarTest extends BaseCalendarTest{

    private final Calendar cFrankfurt;
	private final Calendar cXetra;
	private final Calendar cEurex;
	private final Calendar cSettlement;

	public GermanyCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		cSettlement	= Germany.getCalendar(Germany.Market.SETTLEMENT);
		cFrankfurt 	= Germany.getCalendar(Germany.Market.FWB);
		cXetra 		= Germany.getCalendar(Germany.Market.XETRA);
		cEurex 		= Germany.getCalendar(Germany.Market.EUREX);
	}


	@Test
	public void testGermanyYear2004() {
		final int year = 2004;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(getDate(24,DECEMBER,year));
		//christmas
		//expectedHol.add(getDate(25,DECEMBER,year));
		//boxing day
		//expectedHol.add(getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(getDate(31,DECEMBER,year));

		//good friday
		expectedHol.add(getDate(9,APRIL,year));
		//easter monday
		expectedHol.add(getDate(12,APRIL,year));


		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		//expectedHol.add(getDate(3,OCTOBER,year));

		//ascension
		expectedHol.add(getDate(20,MAY,year));
		//whit monday
		expectedHol.add(getDate(31,MAY,year));
		//corpus christi
		expectedHol.add(getDate(10,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2005() {
		final int year = 2005;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(getDate(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(getDate(1,MAY,year));
		//christmas eve
		//expectedHol.add(getDate(24,DECEMBER,year));
		//christmas
		//expectedHol.add(getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(getDate(26,DECEMBER,year));
		//new years eve
		//expectedHol.add(getDate(31,DECEMBER,year));

		//good friday
		expectedHol.add(getDate(25,MARCH,year));
		//easter monday
		expectedHol.add(getDate(28,MARCH,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(getDate(3,OCTOBER,year));

		//ascension
		expectedHol.add(getDate(5,MAY,year));
		//whit monday
		expectedHol.add(getDate(16,MAY,year));
		//corpus christi
		expectedHol.add(getDate(26,MAY,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2006() {
		final int year = 2006;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(getDate(1,MAY,year));
		//christmas eve
		//expectedHol.add(getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(getDate(26,DECEMBER,year));
		//new years eve
		//expectedHol.add(getDate(31,DECEMBER,year));

		//good friday
		expectedHol.add(getDate(14,APRIL,year));
		//easter monday
		expectedHol.add(getDate(17,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(getDate(3,OCTOBER,year));

		//ascension
		expectedHol.add(getDate(25,MAY,year));
		//whit monday
		expectedHol.add(getDate(5,JUNE,year));
		//corpus christi
		expectedHol.add(getDate(15,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2007() {
		final int year = 2007;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(getDate(31,DECEMBER,year));

		//good friday
		expectedHol.add(getDate(6,APRIL,year));
		//easter monday
		expectedHol.add(getDate(9,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(getDate(3,OCTOBER,year));

		//ascension
		expectedHol.add(getDate(17,MAY,year));
		//whit monday
		expectedHol.add(getDate(28,MAY,year));
		//corpus christi
		expectedHol.add(getDate(7,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}


	// 2008 - current year
	@Test
	public void testGermanyYear2008() {
		final int year = 2008;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(getDate(31,DECEMBER,year));

		//good friday
		expectedHol.add(getDate(21,MARCH,year));
		//easter monday
		expectedHol.add(getDate(24,MARCH,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(getDate(3,OCTOBER,year));

		//ascension -- same day as Labor day
		//expectedHol.add(getDate(1,MAY,year));
		//whit monday
		expectedHol.add(getDate(12,MAY,year));
		//corpus christi
		expectedHol.add(getDate(22,MAY,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2009() {
		final int year = 2009;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));
		//boxing day
		//expectedHol.add(getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(getDate(31,DECEMBER,year));

		//good friday
		expectedHol.add(getDate(10,APRIL,year));
		//easter monday
		expectedHol.add(getDate(13,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		//expectedHol.add(getDate(3,OCTOBER,year));

		//ascension
		expectedHol.add(getDate(21,MAY,year));
		//whit monday
		expectedHol.add(getDate(1,JUNE,year));
		//corpus christi
		expectedHol.add(getDate(11,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2010() {
		final int year = 2010;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(getDate(24,DECEMBER,year));
		//christmas
		//expectedHol.add(getDate(25,DECEMBER,year));
		//boxing day
		//expectedHol.add(getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(getDate(31,DECEMBER,year));

		//good friday
		expectedHol.add(getDate(2,APRIL,year));
		//easter monday
		expectedHol.add(getDate(5,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		//expectedHol.add(getDate(3,OCTOBER,year));

		//ascension
		expectedHol.add(getDate(13,MAY,year));
		//whit monday
		expectedHol.add(getDate(24,MAY,year));
		//corpus christi
		expectedHol.add(getDate(3,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2011() {
		final int year = 2011;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(getDate(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(getDate(1,MAY,year));
		//christmas eve
		//expectedHol.add(getDate(24,DECEMBER,year));
		//christmas
		//expectedHol.add(getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(getDate(26,DECEMBER,year));
		//new years eve
		//expectedHol.add(getDate(31,DECEMBER,year));

		//good friday
		expectedHol.add(getDate(22,APRIL,year));
		//easter monday
		expectedHol.add(getDate(25,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(getDate(3,OCTOBER,year));

		//ascension
		expectedHol.add(getDate(2,JUNE,year));
		//whit monday
		expectedHol.add(getDate(13,JUNE,year));
		//corpus christi
		expectedHol.add(getDate(23,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2012() {
		final int year = 2012;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(getDate(31,DECEMBER,year));

		//good friday
		expectedHol.add(getDate(6,APRIL,year));
		//easter monday
		expectedHol.add(getDate(9,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(getDate(3,OCTOBER,year));

		//ascension
		expectedHol.add(getDate(17,MAY,year));
		//whit monday
		expectedHol.add(getDate(28,MAY,year));
		//corpus christi
		expectedHol.add(getDate(7,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

}
