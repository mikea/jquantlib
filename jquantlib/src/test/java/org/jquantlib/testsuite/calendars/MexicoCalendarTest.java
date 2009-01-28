package org.jquantlib.testsuite.calendars;

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.SEPTEMBER;
import static org.jquantlib.util.Month.NOVEMBER;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Mexico;
import org.jquantlib.time.calendars.Mexico.Market;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MexicoCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(MexicoCalendarTest.class);

    private Calendar settlementCalendar;
    private Calendar bmvCalendar;
    private List<Date> expectedHol;

    @Before
    public void setUp() {
    	settlementCalendar = Mexico.getCalendar(Market.SETTLEMENT);
    	bmvCalendar = Mexico.getCalendar(Market.BMV);
        expectedHol = new Vector<Date>();
    }

    @Test
    public void testMexicoSettlementYear2004() {
        int year = 2004;
        logger.info("Testing Mexican Settlement holiday list for the year " + year + "...");
		
        expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 8, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(15, NOVEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlementCalendar, year);

    }

    @Test
    public void testMexicoSettlementYear2005() {
        int year = 2005;
        logger.info("Testing Mexican Settlement holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 7, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(14, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));


        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlementCalendar, year);

    }

    @Test
    public void testMexicoSettlementYear2006() {
        int year = 2006;
        logger.info("Testing Mexican Settlement holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate( 6, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(13, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(14, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlementCalendar, year);

    }

    @Test
    public void testMexicoSettlementYear2007() {
        int year = 2007;
        logger.info("Testing Mexican Settlement holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 5, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate( 5, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 6, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(19, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlementCalendar, year);

    }

    // 2008 - current year
    @Test
    public void testMexicoSettlementYear2008() {
        int year = 2008;
        logger.info("Testing Mexican Settlement holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 4, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlementCalendar, year);
    }

    @Test
    public void testMexicoSettlementYear2009() {
        int year = 2009;
        logger.info("Testing Mexican Settlement holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(10, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlementCalendar, year);

    }

    @Test
    public void testMexicoSettlementYear2010() {
        int year = 2010;
        logger.info("Testing Mexican Settlement holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(15, NOVEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlementCalendar, year);

    }

    @Test
    public void testMexicoSettlementYear2011() {
        int year = 2011;
        logger.info("Testing Mexican Settlement holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 7, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(14, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlementCalendar, year);

    }

    @Test
    public void testMexicoSettlementYear2012() {
        int year = 2012;
        logger.info("Testing Mexican Settlement holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 6, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate( 5, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 6, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(19, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlementCalendar, year);

    }

    
    @Test
    public void testMexicoBVMYear2004() {
        int year = 2004;
        logger.info("Testing Mexican BVM holiday list for the year " + year + "...");

		expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 8, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(15, NOVEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bmvCalendar, year);

    }

    @Test
    public void testMexicoBVMYear2005() {
        int year = 2005;
        logger.info("Testing Mexican BVM holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 7, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(14, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));


        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bmvCalendar, year);

    }

    @Test
    public void testMexicoBVMYear2006() {
        int year = 2006;
        logger.info("Testing Mexican BVM holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 6, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(13, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(14, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bmvCalendar, year);

    }

    
	//    2007 - BMV Trading Holidays
	//
	//    01 Jan    Mon    New Year's Day
	//    05 Feb    Mon    Constitution Day
	//    19 Mar    Mon    Juarez's Birthday
	//    05 Apr    Thu    Holy Thursday
	//    06 Apr    Fri    Good Friday
	//    07 Apr    Sat    Holy Saturday
	//    01 May    Tue    Labour Day
	//    16 Sep    Sun    Independence Day
	//    02 Nov    Fri    All Soul's Day
	//    19 Nov    Mon    Mexican Revolution
	//    12 Dec    Wed    Our Lady of Guadalupe
	//    25 Dec    Tue    Christmas Day
    
    @Test
    public void testMexicoBVMYear2007() {
        int year = 2007;
        logger.info("Testing Mexican BVM holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 5, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate( 5, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 6, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(19, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bmvCalendar, year);

    }

    // 2008 - current year
    @Test
    public void testMexicoBVMYear2008() {
        int year = 2008;
        logger.info("Testing Mexican BVM holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 4, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bmvCalendar, year);
    }

    @Test
    public void testMexicoBVMYear2009() {
        int year = 2009;
        logger.info("Testing Mexican BVM holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(10, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bmvCalendar, year);

    }

    @Test
    public void testMexicoBVMYear2010() {
        int year = 2010;
        logger.info("Testing Mexican BVM holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(15, NOVEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bmvCalendar, year);

    }

    @Test
    public void testMexicoBVMYear2011() {
        int year = 2011;
        logger.info("Testing Mexican BVM holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 7, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, SEPTEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(14, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bmvCalendar, year);

    }

    @Test
    public void testMexicoBVMYear2012() {
        int year = 2012;
        logger.info("Testing Mexican BVM holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate( 6, FEBRUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate( 5, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 6, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate( 1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate( 2, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(19, NOVEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, bmvCalendar, year);

    }

}
