package org.jquantlib.testsuite.calendars;

import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.OCTOBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Germany;
import org.junit.Test;

/**
 * @author Sangaran Sampanthan
 */
public class GermanyCalendarTest {

    private final Calendar cFrankfurt;
	private final Calendar cXetra;
	private final Calendar cEurex;
	private final Calendar cSettlement;

	public GermanyCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		cSettlement	= new Germany(Germany.Market.Settlement);
		cFrankfurt 	= new Germany(Germany.Market.FrankfurtStockExchange);
		cXetra 		= new Germany(Germany.Market.Xetra);
		cEurex 		= new Germany(Germany.Market.Eurex);
	}


	@Test
	public void testGermanyYear2004() {
		final int year = 2004;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(new Date(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(new Date(1,MAY,year));
		//christmas eve
		expectedHol.add(new Date(24,DECEMBER,year));
		//christmas
		//expectedHol.add(new Date(25,DECEMBER,year));
		//boxing day
		//expectedHol.add(new Date(26,DECEMBER,year));
		//new years eve
		expectedHol.add(new Date(31,DECEMBER,year));

		//good friday
		expectedHol.add(new Date(9,APRIL,year));
		//easter monday
		expectedHol.add(new Date(12,APRIL,year));


		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		//expectedHol.add(new Date(3,OCTOBER,year));

		//ascension
		expectedHol.add(new Date(20,MAY,year));
		//whit monday
		expectedHol.add(new Date(31,MAY,year));
		//corpus christi
		expectedHol.add(new Date(10,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2005() {
		final int year = 2005;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(new Date(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(new Date(1,MAY,year));
		//christmas eve
		//expectedHol.add(new Date(24,DECEMBER,year));
		//christmas
		//expectedHol.add(new Date(25,DECEMBER,year));
		//boxing day
		expectedHol.add(new Date(26,DECEMBER,year));
		//new years eve
		//expectedHol.add(new Date(31,DECEMBER,year));

		//good friday
		expectedHol.add(new Date(25,MARCH,year));
		//easter monday
		expectedHol.add(new Date(28,MARCH,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(new Date(3,OCTOBER,year));

		//ascension
		expectedHol.add(new Date(5,MAY,year));
		//whit monday
		expectedHol.add(new Date(16,MAY,year));
		//corpus christi
		expectedHol.add(new Date(26,MAY,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2006() {
		final int year = 2006;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(new Date(1,JANUARY,year));
		// Labour Day
		expectedHol.add(new Date(1,MAY,year));
		//christmas eve
		//expectedHol.add(new Date(24,DECEMBER,year));
		//christmas
		expectedHol.add(new Date(25,DECEMBER,year));
		//boxing day
		expectedHol.add(new Date(26,DECEMBER,year));
		//new years eve
		//expectedHol.add(new Date(31,DECEMBER,year));

		//good friday
		expectedHol.add(new Date(14,APRIL,year));
		//easter monday
		expectedHol.add(new Date(17,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(new Date(3,OCTOBER,year));

		//ascension
		expectedHol.add(new Date(25,MAY,year));
		//whit monday
		expectedHol.add(new Date(5,JUNE,year));
		//corpus christi
		expectedHol.add(new Date(15,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2007() {
		final int year = 2007;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(new Date(1,JANUARY,year));
		// Labour Day
		expectedHol.add(new Date(1,MAY,year));
		//christmas eve
		expectedHol.add(new Date(24,DECEMBER,year));
		//christmas
		expectedHol.add(new Date(25,DECEMBER,year));
		//boxing day
		expectedHol.add(new Date(26,DECEMBER,year));
		//new years eve
		expectedHol.add(new Date(31,DECEMBER,year));

		//good friday
		expectedHol.add(new Date(6,APRIL,year));
		//easter monday
		expectedHol.add(new Date(9,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(new Date(3,OCTOBER,year));

		//ascension
		expectedHol.add(new Date(17,MAY,year));
		//whit monday
		expectedHol.add(new Date(28,MAY,year));
		//corpus christi
		expectedHol.add(new Date(7,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}


	// 2008 - current year
	@Test
	public void testGermanyYear2008() {
		final int year = 2008;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(new Date(1,JANUARY,year));
		// Labour Day
		expectedHol.add(new Date(1,MAY,year));
		//christmas eve
		expectedHol.add(new Date(24,DECEMBER,year));
		//christmas
		expectedHol.add(new Date(25,DECEMBER,year));
		//boxing day
		expectedHol.add(new Date(26,DECEMBER,year));
		//new years eve
		expectedHol.add(new Date(31,DECEMBER,year));

		//good friday
		expectedHol.add(new Date(21,MARCH,year));
		//easter monday
		expectedHol.add(new Date(24,MARCH,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(new Date(3,OCTOBER,year));

		//ascension -- same day as Labor day
		//expectedHol.add(new Date(1,MAY,year));
		//whit monday
		expectedHol.add(new Date(12,MAY,year));
		//corpus christi
		expectedHol.add(new Date(22,MAY,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2009() {
		final int year = 2009;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(new Date(1,JANUARY,year));
		// Labour Day
		expectedHol.add(new Date(1,MAY,year));
		//christmas eve
		expectedHol.add(new Date(24,DECEMBER,year));
		//christmas
		expectedHol.add(new Date(25,DECEMBER,year));
		//boxing day
		//expectedHol.add(new Date(26,DECEMBER,year));
		//new years eve
		expectedHol.add(new Date(31,DECEMBER,year));

		//good friday
		expectedHol.add(new Date(10,APRIL,year));
		//easter monday
		expectedHol.add(new Date(13,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		//expectedHol.add(new Date(3,OCTOBER,year));

		//ascension
		expectedHol.add(new Date(21,MAY,year));
		//whit monday
		expectedHol.add(new Date(1,JUNE,year));
		//corpus christi
		expectedHol.add(new Date(11,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2010() {
		final int year = 2010;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(new Date(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(new Date(1,MAY,year));
		//christmas eve
		expectedHol.add(new Date(24,DECEMBER,year));
		//christmas
		//expectedHol.add(new Date(25,DECEMBER,year));
		//boxing day
		//expectedHol.add(new Date(26,DECEMBER,year));
		//new years eve
		expectedHol.add(new Date(31,DECEMBER,year));

		//good friday
		expectedHol.add(new Date(2,APRIL,year));
		//easter monday
		expectedHol.add(new Date(5,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		//expectedHol.add(new Date(3,OCTOBER,year));

		//ascension
		expectedHol.add(new Date(13,MAY,year));
		//whit monday
		expectedHol.add(new Date(24,MAY,year));
		//corpus christi
		expectedHol.add(new Date(3,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2011() {
		final int year = 2011;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(new Date(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(new Date(1,MAY,year));
		//christmas eve
		//expectedHol.add(new Date(24,DECEMBER,year));
		//christmas
		//expectedHol.add(new Date(25,DECEMBER,year));
		//boxing day
		expectedHol.add(new Date(26,DECEMBER,year));
		//new years eve
		//expectedHol.add(new Date(31,DECEMBER,year));

		//good friday
		expectedHol.add(new Date(22,APRIL,year));
		//easter monday
		expectedHol.add(new Date(25,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(new Date(3,OCTOBER,year));

		//ascension
		expectedHol.add(new Date(2,JUNE,year));
		//whit monday
		expectedHol.add(new Date(13,JUNE,year));
		//corpus christi
		expectedHol.add(new Date(23,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2012() {
		final int year = 2012;
		QL.info("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(new Date(1,JANUARY,year));
		// Labour Day
		expectedHol.add(new Date(1,MAY,year));
		//christmas eve
		expectedHol.add(new Date(24,DECEMBER,year));
		//christmas
		expectedHol.add(new Date(25,DECEMBER,year));
		//boxing day
		expectedHol.add(new Date(26,DECEMBER,year));
		//new years eve
		expectedHol.add(new Date(31,DECEMBER,year));

		//good friday
		expectedHol.add(new Date(6,APRIL,year));
		//easter monday
		expectedHol.add(new Date(9,APRIL,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);

		QL.info("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(new Date(3,OCTOBER,year));

		//ascension
		expectedHol.add(new Date(17,MAY,year));
		//whit monday
		expectedHol.add(new Date(28,MAY,year));
		//corpus christi
		expectedHol.add(new Date(7,JUNE,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

}
