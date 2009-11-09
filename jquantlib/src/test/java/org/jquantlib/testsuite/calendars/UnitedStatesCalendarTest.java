package org.jquantlib.testsuite.calendars;

import static org.jquantlib.time.Month.*;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.UnitedStates;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * Added QL097 UnitestStates test cases
 * Fixed old tested cases to work work with QL097 UnitedStates calendar class.
 * 
 * @author Sangaran Sampanthan
 * @author Zahid Hussain
 *
 */
@QualityAssurance(quality = Quality.Q3_DOCUMENTATION, version = Version.V097, reviewers = { "Zahid Hussain" })

public class UnitedStatesCalendarTest {

	private final Calendar cNYSE;
	private final Calendar cGovBond;
	private final Calendar cNERC;
	private final Calendar cSettlement;

	public UnitedStatesCalendarTest() {
        QL.info("::::: "+this.getClass().getSimpleName()+" :::::");
        this.cNYSE      = new UnitedStates(UnitedStates.Market.NYSE);
        this.cGovBond   = new UnitedStates(UnitedStates.Market.GOVERNMENTBOND);
        this.cNERC      = new UnitedStates(UnitedStates.Market.NERC);
        this.cSettlement= new UnitedStates(UnitedStates.Market.SETTLEMENT);
	}

	@Test
	public void testUSSettlement() {
		QL.info("Testing US settlement holiday list...");
		final CalendarUtil cbt = new CalendarUtil();
	    List<Date> expectedHol = new ArrayList<Date>();
	    int year = 2004;
	    
	    expectedHol.add(new Date(1, January,year));
	    expectedHol.add(new Date(19,January,year));
	    expectedHol.add(new Date(16,February,year));
	    expectedHol.add(new Date(9,April,year));//good friday
	    expectedHol.add(new Date(31,May,year));
	    expectedHol.add(new Date(5, July,year));
	    expectedHol.add(new Date(6, September,year));
	    expectedHol.add(new Date(11,October,year));
	    expectedHol.add(new Date(11,November,year));
	    expectedHol.add(new Date(25,November,year));
	    expectedHol.add(new Date(24,December,year));
//	    expectedHol.add(new Date(31,DECEMBER,year));
	    
	    Calendar c = new UnitedStates(UnitedStates.Market.SETTLEMENT);
		cbt.checkHolidayList(expectedHol, c, year);
		
		expectedHol.clear();
		
		year = 2005;
	    expectedHol.add(new Date(17,January,year));
	    expectedHol.add(new Date(21,February,year));
	    expectedHol.add(new Date(25,March,year));//good friday
	    expectedHol.add(new Date(30,May,year));
	    expectedHol.add(new Date(4, July,year));
	    expectedHol.add(new Date(5,September,year));
	    expectedHol.add(new Date(10,October,year));
	    expectedHol.add(new Date(11,November,year));
	    expectedHol.add(new Date(24,November,2005));
	    expectedHol.add(new Date(26,December,year));
	    
		cbt.checkHolidayList(expectedHol, c, year);

	}
	
	@Test
	public void testUSGovernmentBondMarket() {
		
	    QL.info("Testing US government bond market holiday list...");
	    final CalendarUtil cbt = new CalendarUtil();
	    int year = 2004;
	    List<Date> expectedHol = new ArrayList<Date>();
	    expectedHol.add(new Date(1,January,year));
	    expectedHol.add(new Date(19,January,year));
	    expectedHol.add(new Date(16,February,year));
	    expectedHol.add(new Date(9,April,year));
	    expectedHol.add(new Date(31,May,year));
	    expectedHol.add(new Date(5,July,year));
	    expectedHol.add(new Date(6,September,year));
	    expectedHol.add(new Date(11,October,year));
	    expectedHol.add(new Date(11,November,year));
	    expectedHol.add(new Date(25,November,year));
	    expectedHol.add(new Date(24,December,year));

	    Calendar c = new UnitedStates(UnitedStates.Market.GOVERNMENTBOND);
	    cbt.checkHolidayList(expectedHol, c, year);
	}

	
	@Test
	public void testUSNewYorkStockExchange() {
	    QL.info("Testing New York Stock Exchange holiday list...");

	    final CalendarUtil cbt = new CalendarUtil();
	    Calendar c = new UnitedStates(UnitedStates.Market.NYSE);
	    List<Date> expectedHol = new ArrayList<Date>();
	    
	    int year = 2004;
	    expectedHol.add(new Date(1,January,year));
	    expectedHol.add(new Date(19,January,year));
	    expectedHol.add(new Date(16,February,year));
	    expectedHol.add(new Date(9,April,year));
	    expectedHol.add(new Date(31,May,year));
	    expectedHol.add(new Date(11,June,year));
	    expectedHol.add(new Date(5,July,year));
	    expectedHol.add(new Date(6,September,year));
	    expectedHol.add(new Date(25,November,year));
	    expectedHol.add(new Date(24,December,year));
	    
	    cbt.checkHolidayList(expectedHol, c, year);
	    
	    expectedHol.clear();
	    year = 2005;
	    expectedHol.add(new Date(17,January,year));
	    expectedHol.add(new Date(21,February,year));
	    expectedHol.add(new Date(25,March,year));
	    expectedHol.add(new Date(30,May,year));
	    expectedHol.add(new Date(4,July,year));
	    expectedHol.add(new Date(5,September,year));
	    expectedHol.add(new Date(24,November,year));
	    expectedHol.add(new Date(26,December,year));
	    
	    cbt.checkHolidayList(expectedHol, c, year);
	    
	    expectedHol.clear();
	    year = 2006;
	    expectedHol.add(new Date(2,January,year));
	    expectedHol.add(new Date(16,January,year));
	    expectedHol.add(new Date(20,February,year));
	    expectedHol.add(new Date(14,April,year));
	    expectedHol.add(new Date(29,May,year));
	    expectedHol.add(new Date(4,July,year));
	    expectedHol.add(new Date(4,September,year));
	    expectedHol.add(new Date(23,November,year));
	    expectedHol.add(new Date(25,December,year));
	    cbt.checkHolidayList(expectedHol, c, year);
	    
	    cbt.checkHolidayList(expectedHol, c, year);
	    

	    List<Date> histClose = new ArrayList<Date>();
	    histClose.add(new Date(11,June,2004));     // Reagan's funeral
	    histClose.add(new Date(14,September,2001));// September 11, 2001
	    histClose.add(new Date(13,September,2001));// September 11, 2001
	    histClose.add(new Date(12,September,2001));// September 11, 2001
	    histClose.add(new Date(11,September,2001));// September 11, 2001
	    histClose.add(new Date(14,July,1977));     // 1977 Blackout
	    histClose.add(new Date(25,January,1973));  // Johnson's funeral.
	    histClose.add(new Date(28,December,1972)); // Truman's funeral
	    histClose.add(new Date(21,July,1969));     // Lunar exploration nat. day
	    histClose.add(new Date(31,March,1969));    // Eisenhower's funeral
	    histClose.add(new Date(10,February,1969)); // heavy snow
	    histClose.add(new Date(5,July,1968));      // Day after Independence Day
	    // June 12-Dec. 31, 1968
	    // Four day week (closed on Wednesdays) - Paperwork Crisis
	    histClose.add(new Date(12,June,1968));
	    histClose.add(new Date(19,June,1968));
	    histClose.add(new Date(26,June,1968));
	    histClose.add(new Date(3,July,1968 ));
	    histClose.add(new Date(10,July,1968));
	    histClose.add(new Date(17,July,1968));
	    histClose.add(new Date(20,November,1968));
	    histClose.add(new Date(27,November,1968));
	    histClose.add(new Date(4,December,1968 ));
	    histClose.add(new Date(11,December,1968));
	    histClose.add(new Date(18,December,1968));
	    // Presidential election days
	    histClose.add(new Date(4,November,1980));
	    histClose.add(new Date(2,November,1976));
	    histClose.add(new Date(7,November,1972));
	    histClose.add(new Date(5,November,1968));
	    histClose.add(new Date(3,November,1964));

	    for (Date d: histClose) {
	        if (!c.isHoliday(d)) {
	        	QL.error(d.toString() + " should be holiday (historical close)", new Exception());
	        	Assert.fail(d.toString() + " should be holiday (historical close)");
	        }
	    }
	    

	}
	
	@Test
	public void testUnitedStatesYear2004() {
		final int year = 2004;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(new Date(1,January,year));
		//memorial day
		expectedHol.add(new Date(31,May,year));
		//independence day (following monday)
		expectedHol.add(new Date(5,July,year));
		//labor day
		expectedHol.add(new Date(6,September,year));
		//thanksgiving
		expectedHol.add(new Date(25,November,year));
		//christmas
		//expectedHol.add(new Date(25,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date reagan_funeral = new Date(11,June,year);
		final Date good_friday = new Date(9,April,year);

		//MLK day
		expectedHol.add(new Date(19,January,year));
		//presidents' day
		expectedHol.add(new Date(16,February,year));
		//good friday
		expectedHol.add(good_friday);
		//christmas (previous friday)
		expectedHol.add(new Date(24,December,year));
		//President Reagan's funeral
		expectedHol.add(reagan_funeral);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		expectedHol.remove(reagan_funeral);

		//Colombus day
		expectedHol.add(new Date(11,October,year));
		//Veteran's day
		expectedHol.add(new Date(11,November,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

//		expectedHol.remove(good_friday);
		//New Year's Eve falls on Friday
//		expectedHol.add(new Date(31,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2005() {
		final int year = 2005;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day (following monday)
		//expectedHol.add(new Date(1,JANUARY,year));
		//memorial day
		expectedHol.add(new Date(30,May,year));
		//independence day
		expectedHol.add(new Date(4,July,year));
		//labor day
		expectedHol.add(new Date(5,September,year));
		//thanksgiving
		expectedHol.add(new Date(24,November,year));
		//christmas (following monday)
		expectedHol.add(new Date(26,December,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = new Date(25,March,year);

		//MLK day
		expectedHol.add(new Date(17,January,year));
		//presidents' day
		expectedHol.add(new Date(21,February,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(new Date(10,October,year));
		//Veteran's day
		expectedHol.add(new Date(11,November,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

//		expectedHol.remove(good_friday);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2006() {
		final int year = 2006;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day (following monday)
		expectedHol.add(new Date(2,January,year));
		//memorial day
		expectedHol.add(new Date(29,May,year));
		//independence day
		expectedHol.add(new Date(4,July,year));
		//labor day
		expectedHol.add(new Date(4,September,year));
		//thanksgiving
		expectedHol.add(new Date(23,November,year));
		//christmas
		expectedHol.add(new Date(25,December,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = new Date(14,April,year);

		//MLK day
		expectedHol.add(new Date(16,January,year));
		//presidents' day
		expectedHol.add(new Date(20,February,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(new Date(9,October,year));
		//Veteran's day (previous friday)
		expectedHol.add(new Date(10,November,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

//		expectedHol.remove(good_friday);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2007() {
		final int year = 2007;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(new Date(1,January,year));
		//memorial day
		expectedHol.add(new Date(28,May,year));
		//independence day
		expectedHol.add(new Date(4,July,year));
		//labor day
		expectedHol.add(new Date(3,September,year));
		//thanksgiving
		expectedHol.add(new Date(22,November,year));
		//christmas
		expectedHol.add(new Date(25,December,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date ford_funeral = new Date(2,January,year);
		final Date good_friday = new Date(6,April,year);

		//MLK day
		expectedHol.add(new Date(15,January,year));
		//presidents' day
		expectedHol.add(new Date(19,February,year));
		//good friday
		expectedHol.add(good_friday);
		// President Ford's funeral
		expectedHol.add(ford_funeral);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		expectedHol.remove(ford_funeral);

		//Colombus day
		expectedHol.add(new Date(8,October,year));
		//Veteran's day (next monday)
		expectedHol.add(new Date(12,November,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

//		expectedHol.remove(good_friday);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}


	// 2008 - current year
	@Test
	public void testUnitedStatesYear2008() {
		final int year = 2008;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(new Date(1,January,year));
		//memorial day
		expectedHol.add(new Date(26,May,year));
		//independence day
		expectedHol.add(new Date(4,July,year));
		//labor day
		expectedHol.add(new Date(1,September,year));
		//thanksgiving
		expectedHol.add(new Date(27,November,year));
		//christmas
		expectedHol.add(new Date(25,December,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = new Date(21,March,year);

		//MLK day
		expectedHol.add(new Date(21,January,year));
		//presidents' day
		expectedHol.add(new Date(18,February,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(new Date(13,October,year));
		//Veteran's day
		expectedHol.add(new Date(11,November,year));

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
		expectedHol.add(new Date(1,January,year));
		//memorial day
		expectedHol.add(new Date(25,May,year));
		//independence day
		//expectedHol.add(new Date(4,JULY,year));
		//labor day
		expectedHol.add(new Date(7,September,year));
		//thanksgiving
		expectedHol.add(new Date(26,November,year));
		//christmas
		expectedHol.add(new Date(25,December,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = new Date(10,April,year);

		//MLK day
		expectedHol.add(new Date(19,January,year));
		//preseidents' day
		expectedHol.add(new Date(16,February,year));
		//good friday
		expectedHol.add(good_friday);
		//independence day (previous friday)
		expectedHol.add(new Date(3,July,year));

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(new Date(12,October,year));
		//Veteran's day
		expectedHol.add(new Date(11,November,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);
		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2010() {
		final int year = 2010;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		expectedHol.add(new Date(1,January,year));
		//memorial day
		expectedHol.add(new Date(31,May,year));
		//independence day (following monday)
		expectedHol.add(new Date(5,July,year));
		//labor day
		expectedHol.add(new Date(6,September,year));
		//thanksgiving
		expectedHol.add(new Date(25,November,year));
		//christmas
		//expectedHol.add(new Date(25,DECEMBER,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = new Date(2,April,year);

		//MLK day
		expectedHol.add(new Date(18,January,year));
		//preseidents' day
		expectedHol.add(new Date(15,February,year));
		//good friday
		expectedHol.add(good_friday);
		//christmas (previous friday)
		expectedHol.add(new Date(24,December,year));

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(new Date(11,October,year));
		//Veteran's day
		expectedHol.add(new Date(11,November,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		//New Year's Eve falls on Friday
//		expectedHol.add(new Date(31,DECEMBER,year));

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2011() {
		final int year = 2011;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day
		//expectedHol.add(new Date(1,JANUARY,year));
		//memorial day
		expectedHol.add(new Date(30,May,year));
		//independence day
		expectedHol.add(new Date(4,July,year));
		//labor day
		expectedHol.add(new Date(5,September,year));
		//thanksgiving
		expectedHol.add(new Date(24,November,year));
		//christmas (following monday)
		expectedHol.add(new Date(26,December,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = new Date(22,April,year);

		//MLK day
		expectedHol.add(new Date(17,January,year));
		//preseidents' day
		expectedHol.add(new Date(21,February,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(new Date(10,October,year));
		//Veteran's day
		expectedHol.add(new Date(11,November,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

	@Test
	public void testUnitedStatesYear2012() {
		final int year = 2012;
		QL.info("Testing United States holiday list for the year " + year + " as recognized by markets Frankfurt Stock Exchange, Xetra, Eurex ...");
        
        final List<Date> expectedHol = new ArrayList<Date>();

		//new years day (following monday)
		expectedHol.add(new Date(2,January,year));
		//memorial day
		expectedHol.add(new Date(28,May,year));
		//independence day
		expectedHol.add(new Date(4,July,year));
		//labor day
		expectedHol.add(new Date(3,September,year));
		//thanksgiving
		expectedHol.add(new Date(22,November,year));
		//christmas
		expectedHol.add(new Date(25,December,year));

		// Call the Holiday Check
		final CalendarUtil cbt = new CalendarUtil();
		cbt.checkHolidayList(expectedHol, cNERC, year);

		final Date good_friday = new Date(6,April,year);

		//MLK day
		expectedHol.add(new Date(16,January,year));
		//preseidents' day
		expectedHol.add(new Date(20,February,year));
		//good friday
		expectedHol.add(good_friday);

		cbt.checkHolidayList(expectedHol, cNYSE, year);

		//Colombus day
		expectedHol.add(new Date(8,October,year));
		//Veteran's day (next monday)
		expectedHol.add(new Date(12,November,year));

		cbt.checkHolidayList(expectedHol, cGovBond, year);

		cbt.checkHolidayList(expectedHol, cSettlement, year);
	}

}