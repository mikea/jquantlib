package org.jquantlib.testsuite.calendars;

import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.AUGUST;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Iceland;
import org.junit.Test;

/**
 * @author Bo Conroy
 *
 */
public class IcelandCalendarTest {

    //TODO: private final Calendar settlement;
    private final Calendar exchange;

	public IcelandCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	    //TODO: this.settlement = Iceland.getCalendar(Iceland.Market.Settlement);
	    this.exchange   = new Iceland(Iceland.Market.ICEX);
	}


    @Test
    public void testIcelandYear2004() {
        final int year = 2004;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(8, APRIL, year));
        expectedHol.add(new Date(9, APRIL, year));
        expectedHol.add(new Date(12, APRIL, year));
        expectedHol.add(new Date(22, APRIL, year));
        expectedHol.add(new Date(20, MAY, year));
        expectedHol.add(new Date(31, MAY, year));
        expectedHol.add(new Date(17, JUNE, year));
        expectedHol.add(new Date(2, AUGUST, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2005() {
        final int year = 2005;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(3, JANUARY, year));
        expectedHol.add(new Date(24, MARCH, year));
        expectedHol.add(new Date(25, MARCH, year));
        expectedHol.add(new Date(28, MARCH, year));
        expectedHol.add(new Date(21, APRIL, year));
        expectedHol.add(new Date(5, MAY, year));
        expectedHol.add(new Date(16, MAY, year));
        expectedHol.add(new Date(17, JUNE, year));
        expectedHol.add(new Date(1, AUGUST, year));
        expectedHol.add(new Date(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2006() {
        final int year = 2006;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(2, JANUARY, year));
        expectedHol.add(new Date(13, APRIL, year));
        expectedHol.add(new Date(14, APRIL, year));
        expectedHol.add(new Date(17, APRIL, year));
        expectedHol.add(new Date(20, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(25, MAY, year));
        expectedHol.add(new Date(5, JUNE, year));
        expectedHol.add(new Date(7, AUGUST, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2007() {
        final int year = 2007;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(5, APRIL, year));
        expectedHol.add(new Date(6, APRIL, year));
        expectedHol.add(new Date(9, APRIL, year));
        expectedHol.add(new Date(19, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(17, MAY, year));
        expectedHol.add(new Date(28, MAY, year));
        expectedHol.add(new Date(6, AUGUST, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    // 2008 - current year
    @Test
    public void testIcelandYear2008() {
        final int year = 2008;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(20, MARCH, year));
        expectedHol.add(new Date(21, MARCH, year));
        expectedHol.add(new Date(24, MARCH, year));
        expectedHol.add(new Date(24, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(12, MAY, year));
        expectedHol.add(new Date(17, JUNE, year));
        expectedHol.add(new Date(4, AUGUST, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2009() {
        final int year = 2009;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(9, APRIL, year));
        expectedHol.add(new Date(10, APRIL, year));
        expectedHol.add(new Date(13, APRIL, year));
        expectedHol.add(new Date(23, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(21, MAY, year));
        expectedHol.add(new Date(1, JUNE, year));
        expectedHol.add(new Date(17, JUNE, year));
        expectedHol.add(new Date(3, AUGUST, year));
        expectedHol.add(new Date(25, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2010() {
        final int year = 2010;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(1, JANUARY, year));
        expectedHol.add(new Date(1, APRIL, year));
        expectedHol.add(new Date(2, APRIL, year));
        expectedHol.add(new Date(5, APRIL, year));
        expectedHol.add(new Date(22, APRIL, year));
        expectedHol.add(new Date(13, MAY, year));
        expectedHol.add(new Date(24, MAY, year));
        expectedHol.add(new Date(17, JUNE, year));
        expectedHol.add(new Date(2, AUGUST, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2011() {
        final int year = 2011;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(3, JANUARY, year));
        expectedHol.add(new Date(21, APRIL, year));
        expectedHol.add(new Date(22, APRIL, year));
        expectedHol.add(new Date(25, APRIL, year));
        expectedHol.add(new Date(2, JUNE, year));
        expectedHol.add(new Date(13, JUNE, year));
        expectedHol.add(new Date(17, JUNE, year));
        expectedHol.add(new Date(1, AUGUST, year));
        expectedHol.add(new Date(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2012() {
        final int year = 2012;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(new Date(2, JANUARY, year));
        expectedHol.add(new Date(5, APRIL, year));
        expectedHol.add(new Date(6, APRIL, year));
        expectedHol.add(new Date(9, APRIL, year));
        expectedHol.add(new Date(19, APRIL, year));
        expectedHol.add(new Date(1, MAY, year));
        expectedHol.add(new Date(17, MAY, year));
        expectedHol.add(new Date(28, MAY, year));
        expectedHol.add(new Date(6, AUGUST, year));
        expectedHol.add(new Date(25, DECEMBER, year));
        expectedHol.add(new Date(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

}
