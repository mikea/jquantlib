package org.jquantlib.testsuite.calendars;

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.UnitedStates;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sangaran Sampanthan
 * 
 */
public class UnitedStatesCalendarTest {
    private final static Logger logger = LoggerFactory.getLogger(UnitedStatesCalendarTest.class);

	private final Calendar cNYSE;
	private final Calendar cGovBond;
	private final Calendar cNERC;
	private final Calendar cSettlement;

	public UnitedStatesCalendarTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        this.cNYSE      = UnitedStates.getCalendar(UnitedStates.Market.NYSE);
        this.cGovBond   = UnitedStates.getCalendar(UnitedStates.Market.GOVERNMENTBOND);
        this.cNERC      = UnitedStates.getCalendar(UnitedStates.Market.NERC);
        this.cSettlement    = UnitedStates.getCalendar(UnitedStates.Market.SETTLEMENT);
	}
	
	
	@Test
	public void testUnitedStatesYear2004() {
		int year = 2004;
		logger.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");   
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(df.getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(df.getDate(31,MAY,year));
		//independence day (following monday)
		expectedHol.add(df.getDate(5,JULY,year));
		//labor day
		expectedHol.add(df.getDate(6,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(df.getDate(25,NOVEMBER,year));
		//christmas
		//expectedHol.add(df.getDate(25,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);
		
		Date reagan_funeral = df.getDate(11,JUNE,year);
		Date good_friday = df.getDate(9,APRIL,year);
		
		//MLK day
		expectedHol.add(df.getDate(19,JANUARY,year));
		//presidents' day
		expectedHol.add(df.getDate(16,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		//christmas (previous friday)
		expectedHol.add(df.getDate(24,DECEMBER,year));
		//President Reagan's funeral
		expectedHol.add(reagan_funeral);
		
		cbt.checkHolidayList(expectedHol, cNYSE, year);
		
		expectedHol.remove(reagan_funeral);
		
		//Colombus day
		expectedHol.add(df.getDate(11,OCTOBER,year));
		//Veteran's day
		expectedHol.add(df.getDate(11,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cGovBond, year);
		
		expectedHol.remove(good_friday);
		//New Year's Eve falls on Friday
		expectedHol.add(df.getDate(31,DECEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2005() {
		int year = 2005;
		logger.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");   
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day (following monday)
		//expectedHol.add(df.getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(df.getDate(30,MAY,year));
		//independence day
		expectedHol.add(df.getDate(4,JULY,year));
		//labor day
		expectedHol.add(df.getDate(5,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(df.getDate(24,NOVEMBER,year));
		//christmas (following monday)
		expectedHol.add(df.getDate(26,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);
		
		Date good_friday = df.getDate(25,MARCH,year);
		
		//MLK day
		expectedHol.add(df.getDate(17,JANUARY,year));
		//presidents' day
		expectedHol.add(df.getDate(21,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		
		cbt.checkHolidayList(expectedHol, cNYSE, year);
		
		//Colombus day
		expectedHol.add(df.getDate(10,OCTOBER,year));
		//Veteran's day
		expectedHol.add(df.getDate(11,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cGovBond, year);
		
		expectedHol.remove(good_friday);
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2006() {
		int year = 2006;
		logger.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");   
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day (following monday)
		expectedHol.add(df.getDate(2,JANUARY,year));
		//memorial day
		expectedHol.add(df.getDate(29,MAY,year));
		//independence day
		expectedHol.add(df.getDate(4,JULY,year));
		//labor day
		expectedHol.add(df.getDate(4,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(df.getDate(23,NOVEMBER,year));
		//christmas
		expectedHol.add(df.getDate(25,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);
		
		Date good_friday = df.getDate(14,APRIL,year);
		
		//MLK day
		expectedHol.add(df.getDate(16,JANUARY,year));
		//presidents' day
		expectedHol.add(df.getDate(20,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		
		cbt.checkHolidayList(expectedHol, cNYSE, year);
		
		//Colombus day
		expectedHol.add(df.getDate(9,OCTOBER,year));
		//Veteran's day (previous friday)
		expectedHol.add(df.getDate(10,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cGovBond, year);
		
		expectedHol.remove(good_friday);
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2007() {
		int year = 2007;
		logger.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");   
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(df.getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(df.getDate(28,MAY,year));
		//independence day
		expectedHol.add(df.getDate(4,JULY,year));
		//labor day
		expectedHol.add(df.getDate(3,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(df.getDate(22,NOVEMBER,year));
		//christmas
		expectedHol.add(df.getDate(25,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		Date ford_funeral = df.getDate(2,JANUARY,year);
		Date good_friday = df.getDate(6,APRIL,year);
		
		//MLK day
		expectedHol.add(df.getDate(15,JANUARY,year));
		//presidents' day
		expectedHol.add(df.getDate(19,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		// President Ford's funeral
		expectedHol.add(ford_funeral);
		
		cbt.checkHolidayList(expectedHol, cNYSE, year);
		
		expectedHol.remove(ford_funeral);
		
		//Colombus day
		expectedHol.add(df.getDate(8,OCTOBER,year));
		//Veteran's day (next monday)
		expectedHol.add(df.getDate(12,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cGovBond, year);
		
		expectedHol.remove(good_friday);
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}


	// 2008 - current year
	@Test
	public void testUnitedStatesYear2008() {
		int year = 2008;
		logger.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");  
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(df.getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(df.getDate(26,MAY,year));
		//independence day
		expectedHol.add(df.getDate(4,JULY,year));
		//labor day
		expectedHol.add(df.getDate(1,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(df.getDate(27,NOVEMBER,year));
		//christmas
		expectedHol.add(df.getDate(25,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);
		
		Date good_friday = df.getDate(21,MARCH,year);
		
		//MLK day
		expectedHol.add(df.getDate(21,JANUARY,year));
		//presidents' day
		expectedHol.add(df.getDate(18,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		
		cbt.checkHolidayList(expectedHol, cNYSE, year);
		
		//Colombus day
		expectedHol.add(df.getDate(13,OCTOBER,year));
		//Veteran's day
		expectedHol.add(df.getDate(11,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cGovBond, year);
		
		//expectedHol.remove(good_friday);
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2009() {
		int year = 2009;
		logger.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(df.getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(df.getDate(25,MAY,year));
		//independence day 
		//expectedHol.add(df.getDate(4,JULY,year));
		//labor day
		expectedHol.add(df.getDate(7,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(df.getDate(26,NOVEMBER,year));
		//christmas
		expectedHol.add(df.getDate(25,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		Date good_friday = df.getDate(10,APRIL,year);
		
		//MLK day
		expectedHol.add(df.getDate(19,JANUARY,year));
		//preseidents' day
		expectedHol.add(df.getDate(16,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		//independence day (previous friday)
		expectedHol.add(df.getDate(3,JULY,year));
		
		cbt.checkHolidayList(expectedHol, cNYSE, year);
		
		//Colombus day
		expectedHol.add(df.getDate(12,OCTOBER,year));
		//Veteran's day
		expectedHol.add(df.getDate(11,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cGovBond, year);
		
		//expectedHol.remove(good_friday);
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2010() {
		int year = 2010;
		logger.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(df.getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(df.getDate(31,MAY,year));
		//independence day (following monday)
		expectedHol.add(df.getDate(5,JULY,year));
		//labor day
		expectedHol.add(df.getDate(6,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(df.getDate(25,NOVEMBER,year));
		//christmas
		//expectedHol.add(df.getDate(25,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);
		
		Date good_friday = df.getDate(2,APRIL,year);
		
		//MLK day
		expectedHol.add(df.getDate(18,JANUARY,year));
		//preseidents' day
		expectedHol.add(df.getDate(15,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		//christmas (previous friday)
		expectedHol.add(df.getDate(24,DECEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cNYSE, year);
		
		//Colombus day
		expectedHol.add(df.getDate(11,OCTOBER,year));
		//Veteran's day
		expectedHol.add(df.getDate(11,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cGovBond, year);
		
		expectedHol.remove(good_friday);
		//New Year's Eve falls on Friday
		expectedHol.add(df.getDate(31,DECEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2011() {
		int year = 2011;
		logger.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ..."); 
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(df.getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(df.getDate(30,MAY,year));
		//independence day
		expectedHol.add(df.getDate(4,JULY,year));
		//labor day
		expectedHol.add(df.getDate(5,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(df.getDate(24,NOVEMBER,year));
		//christmas (following monday)
		expectedHol.add(df.getDate(26,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);
		
		Date good_friday = df.getDate(22,APRIL,year);
		
		//MLK day
		expectedHol.add(df.getDate(17,JANUARY,year));
		//preseidents' day
		expectedHol.add(df.getDate(21,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
				
		cbt.checkHolidayList(expectedHol, cNYSE, year);
		
		//Colombus day
		expectedHol.add(df.getDate(10,OCTOBER,year));
		//Veteran's day
		expectedHol.add(df.getDate(11,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cGovBond, year);
		
		expectedHol.remove(good_friday);
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}
	
	@Test
	public void testUnitedStatesYear2012() {
		int year = 2012;
		logger.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ..."); 
        final DateFactory df = DateFactory.getFactory();
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day (following monday)
		expectedHol.add(df.getDate(2,JANUARY,year));
		//memorial day
		expectedHol.add(df.getDate(28,MAY,year));
		//independence day
		expectedHol.add(df.getDate(4,JULY,year));
		//labor day
		expectedHol.add(df.getDate(3,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(df.getDate(22,NOVEMBER,year));
		//christmas
		expectedHol.add(df.getDate(25,DECEMBER,year));
		
		// Call the Holiday Check
		CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);
		
		Date good_friday = df.getDate(6,APRIL,year);
		
		//MLK day
		expectedHol.add(df.getDate(16,JANUARY,year));
		//preseidents' day
		expectedHol.add(df.getDate(20,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
				
		cbt.checkHolidayList(expectedHol, cNYSE, year);
		
		//Colombus day
		expectedHol.add(df.getDate(8,OCTOBER,year));
		//Veteran's day (next monday)
		expectedHol.add(df.getDate(12,NOVEMBER,year));
		
		cbt.checkHolidayList(expectedHol, cGovBond, year);
		
		expectedHol.remove(good_friday);
		
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

}