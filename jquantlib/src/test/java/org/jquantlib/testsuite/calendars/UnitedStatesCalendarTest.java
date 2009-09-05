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

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.UnitedStates;
import org.jquantlib.util.Date;
import org.junit.Test;

/**
 * @author Sangaran Sampanthan
 *
 */
public class UnitedStatesCalendarTest extends BaseCalendarTest{

	private final Calendar cNYSE;
	private final Calendar cGovBond;
	private final Calendar cNERC;
	private final Calendar cSettlement;

	public UnitedStatesCalendarTest() {
        QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        this.cNYSE      = UnitedStates.getCalendar(UnitedStates.Market.NYSE);
        this.cGovBond   = UnitedStates.getCalendar(UnitedStates.Market.GOVERNMENTBOND);
        this.cNERC      = UnitedStates.getCalendar(UnitedStates.Market.NERC);
        this.cSettlement    = UnitedStates.getCalendar(UnitedStates.Market.SETTLEMENT);
	}


	@Test
	public void testUnitedStatesYear2004() {
		final int year = 2004;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(getDate(31,MAY,year));
		//independence day (following monday)
		expectedHol.add(getDate(5,JULY,year));
		//labor day
		expectedHol.add(getDate(6,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(getDate(25,NOVEMBER,year));
		//christmas
		//expectedHol.add(getDate(25,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date reagan_funeral = getDate(11,JUNE,year);
		final Date good_friday = getDate(9,APRIL,year);

		//MLK day
		expectedHol.add(getDate(19,JANUARY,year));
		//presidents' day
		expectedHol.add(getDate(16,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		//christmas (previous friday)
		expectedHol.add(getDate(24,DECEMBER,year));
		//President Reagan's funeral
		expectedHol.add(reagan_funeral);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		expectedHol.remove(reagan_funeral);

		//Colombus day
		expectedHol.add(getDate(11,OCTOBER,year));
		//Veteran's day
		expectedHol.add(getDate(11,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		expectedHol.remove(good_friday);
		//New Year's Eve falls on Friday
		expectedHol.add(getDate(31,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2005() {
		final int year = 2005;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day (following monday)
		//expectedHol.add(getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(getDate(30,MAY,year));
		//independence day
		expectedHol.add(getDate(4,JULY,year));
		//labor day
		expectedHol.add(getDate(5,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(getDate(24,NOVEMBER,year));
		//christmas (following monday)
		expectedHol.add(getDate(26,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = getDate(25,MARCH,year);

		//MLK day
		expectedHol.add(getDate(17,JANUARY,year));
		//presidents' day
		expectedHol.add(getDate(21,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(getDate(10,OCTOBER,year));
		//Veteran's day
		expectedHol.add(getDate(11,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		expectedHol.remove(good_friday);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2006() {
		final int year = 2006;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day (following monday)
		expectedHol.add(getDate(2,JANUARY,year));
		//memorial day
		expectedHol.add(getDate(29,MAY,year));
		//independence day
		expectedHol.add(getDate(4,JULY,year));
		//labor day
		expectedHol.add(getDate(4,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(getDate(23,NOVEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = getDate(14,APRIL,year);

		//MLK day
		expectedHol.add(getDate(16,JANUARY,year));
		//presidents' day
		expectedHol.add(getDate(20,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(getDate(9,OCTOBER,year));
		//Veteran's day (previous friday)
		expectedHol.add(getDate(10,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		expectedHol.remove(good_friday);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2007() {
		final int year = 2007;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(getDate(28,MAY,year));
		//independence day
		expectedHol.add(getDate(4,JULY,year));
		//labor day
		expectedHol.add(getDate(3,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(getDate(22,NOVEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date ford_funeral = getDate(2,JANUARY,year);
		final Date good_friday = getDate(6,APRIL,year);

		//MLK day
		expectedHol.add(getDate(15,JANUARY,year));
		//presidents' day
		expectedHol.add(getDate(19,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		// President Ford's funeral
		expectedHol.add(ford_funeral);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		expectedHol.remove(ford_funeral);

		//Colombus day
		expectedHol.add(getDate(8,OCTOBER,year));
		//Veteran's day (next monday)
		expectedHol.add(getDate(12,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		expectedHol.remove(good_friday);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}


	// 2008 - current year
	@Test
	public void testUnitedStatesYear2008() {
		final int year = 2008;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(getDate(26,MAY,year));
		//independence day
		expectedHol.add(getDate(4,JULY,year));
		//labor day
		expectedHol.add(getDate(1,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(getDate(27,NOVEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = getDate(21,MARCH,year);

		//MLK day
		expectedHol.add(getDate(21,JANUARY,year));
		//presidents' day
		expectedHol.add(getDate(18,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(getDate(13,OCTOBER,year));
		//Veteran's day
		expectedHol.add(getDate(11,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		//expectedHol.remove(good_friday);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2009() {
		final int year = 2009;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(getDate(25,MAY,year));
		//independence day
		//expectedHol.add(getDate(4,JULY,year));
		//labor day
		expectedHol.add(getDate(7,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(getDate(26,NOVEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = getDate(10,APRIL,year);

		//MLK day
		expectedHol.add(getDate(19,JANUARY,year));
		//preseidents' day
		expectedHol.add(getDate(16,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		//independence day (previous friday)
		expectedHol.add(getDate(3,JULY,year));

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(getDate(12,OCTOBER,year));
		//Veteran's day
		expectedHol.add(getDate(11,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2010() {
		final int year = 2010;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(getDate(31,MAY,year));
		//independence day (following monday)
		expectedHol.add(getDate(5,JULY,year));
		//labor day
		expectedHol.add(getDate(6,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(getDate(25,NOVEMBER,year));
		//christmas
		//expectedHol.add(getDate(25,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = getDate(2,APRIL,year);

		//MLK day
		expectedHol.add(getDate(18,JANUARY,year));
		//preseidents' day
		expectedHol.add(getDate(15,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);
		//christmas (previous friday)
		expectedHol.add(getDate(24,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(getDate(11,OCTOBER,year));
		//Veteran's day
		expectedHol.add(getDate(11,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		//New Year's Eve falls on Friday
		expectedHol.add(getDate(31,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2011() {
		final int year = 2011;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(getDate(1,JANUARY,year));
		//memorial day
		expectedHol.add(getDate(30,MAY,year));
		//independence day
		expectedHol.add(getDate(4,JULY,year));
		//labor day
		expectedHol.add(getDate(5,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(getDate(24,NOVEMBER,year));
		//christmas (following monday)
		expectedHol.add(getDate(26,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = getDate(22,APRIL,year);

		//MLK day
		expectedHol.add(getDate(17,JANUARY,year));
		//preseidents' day
		expectedHol.add(getDate(21,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(getDate(10,OCTOBER,year));
		//Veteran's day
		expectedHol.add(getDate(11,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2012() {
		final int year = 2012;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day (following monday)
		expectedHol.add(getDate(2,JANUARY,year));
		//memorial day
		expectedHol.add(getDate(28,MAY,year));
		//independence day
		expectedHol.add(getDate(4,JULY,year));
		//labor day
		expectedHol.add(getDate(3,SEPTEMBER,year));
		//thanksgiving
		expectedHol.add(getDate(22,NOVEMBER,year));
		//christmas
		expectedHol.add(getDate(25,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = getDate(6,APRIL,year);

		//MLK day
		expectedHol.add(getDate(16,JANUARY,year));
		//preseidents' day
		expectedHol.add(getDate(20,FEBRUARY,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(getDate(8,OCTOBER,year));
		//Veteran's day (next monday)
		expectedHol.add(getDate(12,NOVEMBER,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

}