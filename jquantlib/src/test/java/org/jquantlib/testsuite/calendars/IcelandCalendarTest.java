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

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Iceland;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Bo Conroy
 *
 */
public class IcelandCalendarTest extends BaseCalendarTest{

    //TODO: private final Calendar settlement;
    private final Calendar exchange;

	public IcelandCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	    //TODO: this.settlement = Iceland.getCalendar(Iceland.Market.Settlement);
	    this.exchange   = Iceland.getCalendar(Iceland.Market.ICEX);
	}


    @Test
    public void testIcelandYear2004() {
        final int year = 2004;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDateFactory().getDate(1, JANUARY, year));
        expectedHol.add(getDateFactory().getDate(8, APRIL, year));
        expectedHol.add(getDateFactory().getDate(9, APRIL, year));
        expectedHol.add(getDateFactory().getDate(12, APRIL, year));
        expectedHol.add(getDateFactory().getDate(22, APRIL, year));
        expectedHol.add(getDateFactory().getDate(20, MAY, year));
        expectedHol.add(getDateFactory().getDate(31, MAY, year));
        expectedHol.add(getDateFactory().getDate(17, JUNE, year));
        expectedHol.add(getDateFactory().getDate(2, AUGUST, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2005() {
        final int year = 2005;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDateFactory().getDate(3, JANUARY, year));
        expectedHol.add(getDateFactory().getDate(24, MARCH, year));
        expectedHol.add(getDateFactory().getDate(25, MARCH, year));
        expectedHol.add(getDateFactory().getDate(28, MARCH, year));
        expectedHol.add(getDateFactory().getDate(21, APRIL, year));
        expectedHol.add(getDateFactory().getDate(5, MAY, year));
        expectedHol.add(getDateFactory().getDate(16, MAY, year));
        expectedHol.add(getDateFactory().getDate(17, JUNE, year));
        expectedHol.add(getDateFactory().getDate(1, AUGUST, year));
        expectedHol.add(getDateFactory().getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2006() {
        final int year = 2006;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDateFactory().getDate(2, JANUARY, year));
        expectedHol.add(getDateFactory().getDate(13, APRIL, year));
        expectedHol.add(getDateFactory().getDate(14, APRIL, year));
        expectedHol.add(getDateFactory().getDate(17, APRIL, year));
        expectedHol.add(getDateFactory().getDate(20, APRIL, year));
        expectedHol.add(getDateFactory().getDate(1, MAY, year));
        expectedHol.add(getDateFactory().getDate(25, MAY, year));
        expectedHol.add(getDateFactory().getDate(5, JUNE, year));
        expectedHol.add(getDateFactory().getDate(7, AUGUST, year));
        expectedHol.add(getDateFactory().getDate(25, DECEMBER, year));
        expectedHol.add(getDateFactory().getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2007() {
        final int year = 2007;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDateFactory().getDate(1, JANUARY, year));
        expectedHol.add(getDateFactory().getDate(5, APRIL, year));
        expectedHol.add(getDateFactory().getDate(6, APRIL, year));
        expectedHol.add(getDateFactory().getDate(9, APRIL, year));
        expectedHol.add(getDateFactory().getDate(19, APRIL, year));
        expectedHol.add(getDateFactory().getDate(1, MAY, year));
        expectedHol.add(getDateFactory().getDate(17, MAY, year));
        expectedHol.add(getDateFactory().getDate(28, MAY, year));
        expectedHol.add(getDateFactory().getDate(6, AUGUST, year));
        expectedHol.add(getDateFactory().getDate(25, DECEMBER, year));
        expectedHol.add(getDateFactory().getDate(26, DECEMBER, year));

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

        expectedHol.add(getDateFactory().getDate(1, JANUARY, year));
        expectedHol.add(getDateFactory().getDate(20, MARCH, year));
        expectedHol.add(getDateFactory().getDate(21, MARCH, year));
        expectedHol.add(getDateFactory().getDate(24, MARCH, year));
        expectedHol.add(getDateFactory().getDate(24, APRIL, year));
        expectedHol.add(getDateFactory().getDate(1, MAY, year));
        expectedHol.add(getDateFactory().getDate(12, MAY, year));
        expectedHol.add(getDateFactory().getDate(17, JUNE, year));
        expectedHol.add(getDateFactory().getDate(4, AUGUST, year));
        expectedHol.add(getDateFactory().getDate(25, DECEMBER, year));
        expectedHol.add(getDateFactory().getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2009() {
        final int year = 2009;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDateFactory().getDate(1, JANUARY, year));
        expectedHol.add(getDateFactory().getDate(9, APRIL, year));
        expectedHol.add(getDateFactory().getDate(10, APRIL, year));
        expectedHol.add(getDateFactory().getDate(13, APRIL, year));
        expectedHol.add(getDateFactory().getDate(23, APRIL, year));
        expectedHol.add(getDateFactory().getDate(1, MAY, year));
        expectedHol.add(getDateFactory().getDate(21, MAY, year));
        expectedHol.add(getDateFactory().getDate(1, JUNE, year));
        expectedHol.add(getDateFactory().getDate(17, JUNE, year));
        expectedHol.add(getDateFactory().getDate(3, AUGUST, year));
        expectedHol.add(getDateFactory().getDate(25, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2010() {
        final int year = 2010;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDateFactory().getDate(1, JANUARY, year));
        expectedHol.add(getDateFactory().getDate(1, APRIL, year));
        expectedHol.add(getDateFactory().getDate(2, APRIL, year));
        expectedHol.add(getDateFactory().getDate(5, APRIL, year));
        expectedHol.add(getDateFactory().getDate(22, APRIL, year));
        expectedHol.add(getDateFactory().getDate(13, MAY, year));
        expectedHol.add(getDateFactory().getDate(24, MAY, year));
        expectedHol.add(getDateFactory().getDate(17, JUNE, year));
        expectedHol.add(getDateFactory().getDate(2, AUGUST, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

    @Test
    public void testIcelandYear2011() {
        final int year = 2011;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDateFactory().getDate(3, JANUARY, year));
        expectedHol.add(getDateFactory().getDate(21, APRIL, year));
        expectedHol.add(getDateFactory().getDate(22, APRIL, year));
        expectedHol.add(getDateFactory().getDate(25, APRIL, year));
        expectedHol.add(getDateFactory().getDate(2, JUNE, year));
        expectedHol.add(getDateFactory().getDate(13, JUNE, year));
        expectedHol.add(getDateFactory().getDate(17, JUNE, year));
        expectedHol.add(getDateFactory().getDate(1, AUGUST, year));
        expectedHol.add(getDateFactory().getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }


    @Test
    public void testIcelandYear2012() {
        final int year = 2012;
    	QL.info("Testing " + Iceland.Market.ICEX + " holidays list for the year " + year + "...");
        final DateFactory df = DateFactory.getFactory();
    	final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDate(2, JANUARY, year));
        expectedHol.add(getDate(5, APRIL, year));
        expectedHol.add(getDate(6, APRIL, year));
        expectedHol.add(getDate(9, APRIL, year));
        expectedHol.add(getDate(19, APRIL, year));
        expectedHol.add(getDate(1, MAY, year));
        expectedHol.add(getDate(17, MAY, year));
        expectedHol.add(getDate(28, MAY, year));
        expectedHol.add(getDate(6, AUGUST, year));
        expectedHol.add(getDate(25, DECEMBER, year));
        expectedHol.add(getDate(26, DECEMBER, year));

    	// Call the Holiday Check
    	final CalendarUtil cbt = new CalendarUtil();
    	cbt.checkHolidayList(expectedHol, exchange, year);
    }

}
