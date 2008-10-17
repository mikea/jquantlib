package org.jquantlib.testsuite.calendars;

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.OCTOBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Germany;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Sangaran Sampanthan
 * 
 */
public class GermanyCalendarTest {
	private Calendar cFrankfurt;
	private Calendar cXetra;
	private Calendar cEurex;
	private Calendar cSettlement;
	private List<Date> expectedHol;

	@Before
	public void setUp() {
		//Eurex, Xetra, Frankfort
		cFrankfurt 	= Germany.getCalendar(Germany.Market.FRANKFURTSTOCKEXCHANGE);
		cXetra 		= Germany.getCalendar(Germany.Market.XETRA);
		cEurex 		= Germany.getCalendar(Germany.Market.EUREX);
		cSettlement	= Germany.getCalendar(Germany.Market.SETTLEMENT);
		expectedHol = new Vector<Date>();
	}

	@Test
	public void testGermanyYear2004() {
		int year = 2004;
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");   

		//new years day
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//christmas
		//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
		//expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
		//easter monday
		expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));

		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);
		
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		//expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
		
		//ascension
		expectedHol.add(DateFactory.getFactory().getDate(20,MAY,year));
		//whit monday
		expectedHol.add(DateFactory.getFactory().getDate(31,MAY,year));
		//corpus christi
		expectedHol.add(DateFactory.getFactory().getDate(10,JUNE,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2005() {
		int year = 2005;
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");   

		//new years day
		//expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//christmas eve
		//expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//christmas
		//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		//new years eve
		//expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
		//easter monday
		expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);	
		
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
		
		//ascension
		expectedHol.add(DateFactory.getFactory().getDate(5,MAY,year));
		//whit monday
		expectedHol.add(DateFactory.getFactory().getDate(16,MAY,year));
		//corpus christi
		expectedHol.add(DateFactory.getFactory().getDate(26,MAY,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);	
	}

	@Test
	public void testGermanyYear2006() {
		int year = 2006;
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");   

		//new years day
		//expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//christmas eve
		//expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		//new years eve
		//expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
		//easter monday
		expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));

		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);	
		
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
		
		//ascension
		expectedHol.add(DateFactory.getFactory().getDate(25,MAY,year));
		//whit monday
		expectedHol.add(DateFactory.getFactory().getDate(5,JUNE,year));
		//corpus christi
		expectedHol.add(DateFactory.getFactory().getDate(15,JUNE,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2007() {
		int year = 2007;
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");   

		//new years day
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
		//easter monday
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);
		
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
		
		//ascension
		expectedHol.add(DateFactory.getFactory().getDate(17,MAY,year));
		//whit monday
		expectedHol.add(DateFactory.getFactory().getDate(28,MAY,year));
		//corpus christi
		expectedHol.add(DateFactory.getFactory().getDate(7,JUNE,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}


	// 2008 - current year
	@Test
	public void testGermanyYear2008() {
		int year = 2008;
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");  

		//new years day
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
		//easter monday
		expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);
		
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));  
		
		//ascension -- same day as Labor day
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//whit monday
		expectedHol.add(DateFactory.getFactory().getDate(12,MAY,year));
		//corpus christi
		expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2009() {
		int year = 2009;
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");

		//new years day
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
		//expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
		//easter monday
		expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);
		
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		//expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
		
		//ascension
		expectedHol.add(DateFactory.getFactory().getDate(21,MAY,year));
		//whit monday
		expectedHol.add(DateFactory.getFactory().getDate(1,JUNE,year));
		//corpus christi
		expectedHol.add(DateFactory.getFactory().getDate(11,JUNE,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2010() {
		int year = 2010;
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");

		//new years day
		expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//christmas
		//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
		//expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
		//easter monday
		expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);
		
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		//expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
		
		//ascension
		expectedHol.add(DateFactory.getFactory().getDate(13,MAY,year));
		//whit monday
		expectedHol.add(DateFactory.getFactory().getDate(24,MAY,year));
		//corpus christi
		expectedHol.add(DateFactory.getFactory().getDate(3,JUNE,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testGermanyYear2011() {
		int year = 2011;
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ..."); 

		//new years day
		//expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		// Labour Day
		//expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//christmas eve
		//expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//christmas
		//expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		//new years eve
		//expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
		//easter monday
		expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);
		
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
		
		//ascension
		expectedHol.add(DateFactory.getFactory().getDate(2,JUNE,year));
		//whit monday
		expectedHol.add(DateFactory.getFactory().getDate(13,JUNE,year));
		//corpus christi
		expectedHol.add(DateFactory.getFactory().getDate(23,JUNE,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
	
	@Test
	public void testGermanyYear2012() {
		int year = 2012;
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ..."); 

		//new years day
		//expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year));
		// Labour Day
		expectedHol.add(DateFactory.getFactory().getDate(1,MAY,year));
		//christmas eve
		expectedHol.add(DateFactory.getFactory().getDate(24,DECEMBER,year));
		//christmas
		expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
		//boxing day
		expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
		//new years eve
		expectedHol.add(DateFactory.getFactory().getDate(31,DECEMBER,year));
		
		//good friday
		expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
		//easter monday
		expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cFrankfurt, year);
		cbt.checkHolidayList(expectedHol, cXetra, year);
		cbt.checkHolidayList(expectedHol, cEurex, year);
		
		System.out.println("Testing Germany holiday list for the year " + year + " as recognized by Settlement ...");
		//National Day
		expectedHol.add(DateFactory.getFactory().getDate(3,OCTOBER,year));
		
		//ascension
		expectedHol.add(DateFactory.getFactory().getDate(17,MAY,year));
		//whit monday
		expectedHol.add(DateFactory.getFactory().getDate(28,MAY,year));
		//corpus christi
		expectedHol.add(DateFactory.getFactory().getDate(7,JUNE,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
}