package org.jquantlib.testsuite.calendars;

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.AUGUST;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Iceland;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bo Conroy
 * 
 */
public class IcelandCalendarTest {

    private final static Logger logger = LoggerFactory.getLogger(IcelandCalendarTest.class);

    private Calendar c;
    private List<Date> expectedHol;

    @Before
    public void setUp() {
        c = Iceland.getCalendar();
        expectedHol = new Vector<Date>();
    }

    @Test
    public void testIcelandYear2004() {
        int year = 2004;
        logger.info("Testing Iceland holiday list for the year " + year + "...");

		expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(8, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(31, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, AUGUST, year));

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testIcelandYear2005() {
        int year = 2005;
        logger.info("Testing Iceland holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(3, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(28, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(16, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, AUGUST, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));


        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testIcelandYear2006() {
        int year = 2006;
        logger.info("Testing Iceland holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(2, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(13, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(14, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(7, AUGUST, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testIcelandYear2007() {
        int year = 2007;
        logger.info("Testing Iceland holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(19, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(28, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, AUGUST, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    // 2008 - current year
    @Test
    public void testIcelandYear2008() {
        int year = 2008;
        logger.info("Testing Iceland holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(20, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, MARCH, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(12, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(4, AUGUST, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));
        

        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);
    }

    @Test
    public void testIcelandYear2009() {
        int year = 2009;
        logger.info("Testing Iceland holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(10, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(13, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(23, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, JUNE, year));       
        expectedHol.add(DateFactory.getFactory().getDate(17, JUNE, year));       
        expectedHol.add(DateFactory.getFactory().getDate(3, AUGUST, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testIcelandYear2010() {
        int year = 2010;
        logger.info("Testing Iceland holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(13, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(24, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, AUGUST, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testIcelandYear2011() {
        int year = 2011;
        logger.info("Testing Iceland holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(3, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(21, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(22, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(2, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(13, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, JUNE, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, AUGUST, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

    @Test
    public void testIcelandYear2012() {
        int year = 2012;
        logger.info("Testing Iceland holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(2, JANUARY, year));
        expectedHol.add(DateFactory.getFactory().getDate(5, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(9, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(19, APRIL, year));
        expectedHol.add(DateFactory.getFactory().getDate(1, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(17, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(28, MAY, year));
        expectedHol.add(DateFactory.getFactory().getDate(6, AUGUST, year));
        expectedHol.add(DateFactory.getFactory().getDate(25, DECEMBER, year));
        expectedHol.add(DateFactory.getFactory().getDate(26, DECEMBER, year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, c, year);

    }

}
