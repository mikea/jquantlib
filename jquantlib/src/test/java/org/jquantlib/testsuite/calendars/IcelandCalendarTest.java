package org.jquantlib.testsuite.calendars;

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Iceland;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bo Conroy
 * 
 */
public class IcelandCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(IcelandCalendarTest.class);

    //TODO: private final Calendar settlement;
    private final Calendar exchange;
	
	public IcelandCalendarTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	    //TODO: this.settlement = Iceland.getCalendar(Iceland.Market.Settlement);
	    this.exchange   = Iceland.getCalendar(Iceland.Market.ICEX);
	}


    @Test
    public void testIcelandYear2004() {
        int year = 2004;
    	logger.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
		expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(8, APRIL, year));
        expectedHol.add(df.getDate(9, APRIL, year));
        expectedHol.add(df.getDate(12, APRIL, year));
        expectedHol.add(df.getDate(22, APRIL, year));
        expectedHol.add(df.getDate(20, MAY, year));
        expectedHol.add(df.getDate(31, MAY, year));
        expectedHol.add(df.getDate(17, JUNE, year));
        expectedHol.add(df.getDate(2, AUGUST, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2005() {
        int year = 2005;
    	logger.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(3, JANUARY, year));
        expectedHol.add(df.getDate(24, MARCH, year));
        expectedHol.add(df.getDate(25, MARCH, year));
        expectedHol.add(df.getDate(28, MARCH, year));
        expectedHol.add(df.getDate(21, APRIL, year));
        expectedHol.add(df.getDate(5, MAY, year));
        expectedHol.add(df.getDate(16, MAY, year));
        expectedHol.add(df.getDate(17, JUNE, year));
        expectedHol.add(df.getDate(1, AUGUST, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2006() {
        int year = 2006;
    	logger.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(2, JANUARY, year));
        expectedHol.add(df.getDate(13, APRIL, year));
        expectedHol.add(df.getDate(14, APRIL, year));
        expectedHol.add(df.getDate(17, APRIL, year));
        expectedHol.add(df.getDate(20, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(25, MAY, year));
        expectedHol.add(df.getDate(5, JUNE, year));
        expectedHol.add(df.getDate(7, AUGUST, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2007() {
        int year = 2007;
    	logger.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(5, APRIL, year));
        expectedHol.add(df.getDate(6, APRIL, year));
        expectedHol.add(df.getDate(9, APRIL, year));
        expectedHol.add(df.getDate(19, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(17, MAY, year));
        expectedHol.add(df.getDate(28, MAY, year));
        expectedHol.add(df.getDate(6, AUGUST, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    // 2008 - current year
    @Test
    public void testIcelandYear2008() {
        int year = 2008;
    	logger.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(20, MARCH, year));
        expectedHol.add(df.getDate(21, MARCH, year));
        expectedHol.add(df.getDate(24, MARCH, year));
        expectedHol.add(df.getDate(24, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(12, MAY, year));
        expectedHol.add(df.getDate(17, JUNE, year));
        expectedHol.add(df.getDate(4, AUGUST, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2009() {
        int year = 2009;
    	logger.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(9, APRIL, year));
        expectedHol.add(df.getDate(10, APRIL, year));
        expectedHol.add(df.getDate(13, APRIL, year));
        expectedHol.add(df.getDate(23, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(21, MAY, year));
        expectedHol.add(df.getDate(1, JUNE, year));       
        expectedHol.add(df.getDate(17, JUNE, year));       
        expectedHol.add(df.getDate(3, AUGUST, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2010() {
        int year = 2010;
    	logger.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(1, JANUARY, year));
        expectedHol.add(df.getDate(1, APRIL, year));
        expectedHol.add(df.getDate(2, APRIL, year));
        expectedHol.add(df.getDate(5, APRIL, year));
        expectedHol.add(df.getDate(22, APRIL, year));
        expectedHol.add(df.getDate(13, MAY, year));
        expectedHol.add(df.getDate(24, MAY, year));
        expectedHol.add(df.getDate(17, JUNE, year));
        expectedHol.add(df.getDate(2, AUGUST, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2011() {
        int year = 2011;
    	logger.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(3, JANUARY, year));
        expectedHol.add(df.getDate(21, APRIL, year));
        expectedHol.add(df.getDate(22, APRIL, year));
        expectedHol.add(df.getDate(25, APRIL, year));
        expectedHol.add(df.getDate(2, JUNE, year));
        expectedHol.add(df.getDate(13, JUNE, year));
        expectedHol.add(df.getDate(17, JUNE, year));
        expectedHol.add(df.getDate(1, AUGUST, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2012() {
        int year = 2012;
    	logger.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();
    	
        expectedHol.add(df.getDate(2, JANUARY, year));
        expectedHol.add(df.getDate(5, APRIL, year));
        expectedHol.add(df.getDate(6, APRIL, year));
        expectedHol.add(df.getDate(9, APRIL, year));
        expectedHol.add(df.getDate(19, APRIL, year));
        expectedHol.add(df.getDate(1, MAY, year));
        expectedHol.add(df.getDate(17, MAY, year));
        expectedHol.add(df.getDate(28, MAY, year));
        expectedHol.add(df.getDate(6, AUGUST, year));
        expectedHol.add(df.getDate(25, DECEMBER, year));
        expectedHol.add(df.getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

}
