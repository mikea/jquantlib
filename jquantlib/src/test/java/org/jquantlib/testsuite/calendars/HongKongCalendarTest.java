/*
 Copyright (C) 2008

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.testsuite.calendars;

import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.HongKong;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Test;

/**
 * @author Richard Gomes
 */
public class HongKongCalendarTest extends BaseCalendarTest{

    public HongKongCalendarTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}

    // 2009 -- taken from Exchange website http://www.hkex.com.hk

    //	 1-Jan-09   Thursday    The first day of January
    //	26-Jan-09   Monday      Lunar New Year's Day
    //	27-Jan-09   Tuesday     The second day of Lunar New Year
    //	28-Jan-09   Wednesday   The third day of Lunar New Year
    //	10-Apr-09   Friday      Good Friday
    //	13-Apr-09   Monday      Easter Monday
    //	 1-May-09   Friday      Labour Day
    //	28-May-09   Thursday    Tuen Ng Festival
    //	 1-Jul-09   Wednesday   Hong Kong Special Administrative Region Establishment Day
    //	 1-Oct-09   Thursday    National Day
    //	26-Oct-09   Monday      Chung Yeung Festival
    //	25-Dec-09   Friday      Christmas Day

	@Test public void testHongKongYear2009() {
        final int year = 2009;
        QL.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDate(1,JANUARY,year));    // The first day of January
        expectedHol.add(getDate(26,JANUARY,year));   // Lunar New Year's Day
        expectedHol.add(getDate(27,JANUARY,year));   // The second day of Lunar New Year
        expectedHol.add(getDate(28,JANUARY,year));   // The third day of Lunar New Year
        expectedHol.add(getDate(10,APRIL,year));     // Good Friday
        expectedHol.add(getDate(13,APRIL,year));     // Easter Monday
        expectedHol.add(getDate(1,MAY,year));        // Labour Day
        expectedHol.add(getDate(28,MAY,year));       // Tuen Ng Festival
        expectedHol.add(getDate(1,JULY,year));       // Hong Kong Special Administrative Region Establishment Day
        expectedHol.add(getDate(1,OCTOBER,year));    // National Day
        expectedHol.add(getDate(26,OCTOBER,year));   // Chung Yeung festival
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }


    // 2008 -- taken from Exchange website http://www.hkex.com.hk

    //     1-Jan-08   Tuesday     The first day of January
    //     7-Feb-08   Thursday    Lunar New Year's Day
    //     8-Feb-08   Friday      The second day of Lunar New Year
    //    21-Mar-08   Friday      Good Friday
    //    24-Mar-08   Monday      Easter Monday
    //     4-Apr-08   Friday      Ching Ming Festival
    //     1-May-08   Thursday    Labour Day
    //    12-May-08   Monday      The Buddha's Birthday
    //     9-Jun-08   Monday      The day following Tuen Ng Festival
    //     1-Jul-08   Tuesday     Hong Kong Special Administrative Region Establishment Day
    //    15-Sep-08   Monday      The day following Chinese Mid-Autumn Festival
    //     1-Oct-08   Wednesday   National Day
    //     7-Oct-08   Tuesday     Chung Yeung Festival
    //    25-Dec-08   Thursday    Christmas Day
    //    26-Dec-08   Friday      The first weekday after Christmas Day

	@Test public void testHongKongYear2008() {
      	final int year = 2008;
        QL.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
    	final List<Date> expectedHol = new ArrayList<Date>();

		expectedHol.add(getDate(1,JANUARY,year));    // The first day of January
    	expectedHol.add(getDate(7,FEBRUARY,year));   // Lunar New Year's Day
		expectedHol.add(getDate(8,FEBRUARY,year));   // The second day of Lunar New Year
		expectedHol.add(getDate(21,MARCH,year));     // Good Friday
		expectedHol.add(getDate(24,MARCH,year));     // Easter Monday
		expectedHol.add(getDate(4,APRIL,year));      // Ching Ming Festival
		expectedHol.add(getDate(1,MAY,year));        // Labour Day
        expectedHol.add(getDate(12,MAY,year));       // The Buddha's Birthday
		expectedHol.add(getDate(9,JUNE,year));       // The day following Tuen Ng Festival
        expectedHol.add(getDate(1,JULY,year));       // Hong Kong Special Administrative Region Establishment Day
    	expectedHol.add(getDate(15,SEPTEMBER,year)); // The day following Chinese Mid-Autumn Festival
        expectedHol.add(getDate(1,OCTOBER,year));    // National Day
        expectedHol.add(getDate(7,OCTOBER,year));    // Chung Yeung festival
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // The first weekday after Christmas Day

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }


	// 2007 -- http://www.hkfastfacts.com/Hong-Kong-Festivals-2007.html

	//     1-Jan-07         The first day of January
    //    17-Feb-07         Lunar New Year's Day
    //    18-Feb-07         The second day of Lunar New Year
    //     5-Apr-07         Ching Ming Festival
    //     6-Apr-07         Good Friday
    //     9-Apr-07         Easter Monday
    //     1-May-07         Labour Day
    //    24-May-07         The Buddha's Birthday
    //    19-Jun-07         The day following Tuen Ng Festival
    //     2-Jul-07         Hong Kong Special Administrative Region Establishment Day
    //    26-Sep-07         The day following Chinese Mid-Autumn Festival
    //     1-Oct-07         National Day
    //    19-Oct-07         Chung Yeung Festival
    //    25-Dec-07         Christmas Day
    //    26-Dec-07         The first weekday after Christmas Day

	@Test public void testHongKongYear2007() {
        final int year = 2007;
        QL.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDate(1,JANUARY,year));    // The first day of January
        expectedHol.add(getDate(1,MAY,year));        // Labour Day
        expectedHol.add(getDate(1,OCTOBER,year));    // National Day
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // The first weekday after Christmas Day

    	expectedHol.add(getDate(19,FEBRUARY,year));   // 2 Lunar New Year's Day
		expectedHol.add(getDate(20,FEBRUARY,year));   // The 3 day of Lunar New Year
		expectedHol.add(getDate(5,APRIL,year));      // Ching Ming Festival
		expectedHol.add(getDate(6,APRIL,year));     // Good Friday
		expectedHol.add(getDate(9,APRIL,year));     // Easter Monday
        expectedHol.add(getDate(24,MAY,year));       // The Buddha's Birthday
		expectedHol.add(getDate(19,JUNE,year));       // The day following Tuen Ng Festival
        expectedHol.add(getDate(2,JULY,year));       // day after Hong Kong Special Administrative Region Establishment Day
    	expectedHol.add(getDate(26,SEPTEMBER,year)); // The day following Chinese Mid-Autumn Festival
        expectedHol.add(getDate(19,OCTOBER,year));    // Chung Yeung festival

        new CalendarUtil().checkHolidayList(expectedHol, c, year);

    }


	// 2006 -- http://www.hkfastfacts.com/Hong-Kong-Festivals-2006.html

	//     2-Jan-06         The 2 day of January
    //    30-Jan-06         The 2 day Lunar New Year's Day
    //    31-Jan-06         The 3 day of Lunar New Year
    //     5-Apr-06         Ching Ming Festival
    //    14-Apr-06         Good Friday
    //    17-Apr-06         Easter Monday
    //     1-May-06         Labour Day
    //     5-May-06         The Buddha's Birthday
    //    31-May-06         The Tuen Ng Festival
    //     1-Jul-06   Saturday      Hong Kong Special Administrative Region Establishment Day
    //     2-Oct-06         The day after National Day
    //     7-Oct-06         The day following Chinese Mid-Autumn Festival
    //    30-Oct-06         Chung Yeung Festival
    //    25-Dec-06         Christmas Day
    //    26-Dec-06         The first weekday after Christmas Day

	@Test public void testHongKongYear2006() {
        final int year = 2006;
        QL.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDate(1,JANUARY,year));    // The first day of January
        expectedHol.add(getDate(1,MAY,year));        // Labour Day
        expectedHol.add(getDate(1,OCTOBER,year));    // National Day
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // The first weekday after Christmas Day

        expectedHol.remove(getDate(1,JANUARY,year));
        expectedHol.remove(getDate(1,OCTOBER,year));

    	expectedHol.add(getDate(2,JANUARY,year));   // 2 of the New Year's Day
    	expectedHol.add(getDate(30,JANUARY,year));   // 2 Lunar New Year's Day
		expectedHol.add(getDate(31,JANUARY,year));   // The 3 day of Lunar New Year
		expectedHol.add(getDate(5,APRIL,year));      // Ching Ming Festival
		expectedHol.add(getDate(14,APRIL,year));     // Good Friday
		expectedHol.add(getDate(17,APRIL,year));     // Easter Monday
        expectedHol.add(getDate(5,MAY,year));       // The Buddha's Birthday
		expectedHol.add(getDate(31,MAY,year));       // The Tuen Ng Festival
        expectedHol.add(getDate(3,JULY,year));  //Monday     // day after Hong Kong Special Administrative Region Establishment Day
        expectedHol.add(getDate(2,OCTOBER,year));    // National Day
    	//expectedHol.add(getDate(7,OCTOBER,year)); saturday // The day following Chinese Mid-Autumn Festival
        expectedHol.add(getDate(30,OCTOBER,year));    // Chung Yeung festival

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }


	// 2005 -- http://www.hkfastfacts.com/Hong-Kong-Festivals-2005.html

	//     3-Jan-05         Monday The 3 day of January
    //     9-Feb-05         Lunar New Year's Day
    //    10-Feb-05         The 2 day Lunar New Year's Day
    //    11-Feb-05         The 3 day of Lunar New Year
    //    25-Mar-05         Good Friday
    //    28-Mar-05         Easter Monday
    //     5-Apr-05         Ching Ming Festival
    //     1-May-05         Sunday Labour Day
    //    15-May-05         Sunday The Buddha's Birthday
    //    11-Jun-05         Saturday The Tuen Ng Festival
    //     1-Jul-05         Hong Kong Special Administrative Region Establishment Day
    //    19-Sep-05         The Chinese Mid-Autumn Festival
    //     1-Oct-05         Saturday The National Day
    //    11-Oct-05         Chung Yeung Festival
    //    25-Dec-05         Sunday Christmas Day
    //    26-Dec-05         The first weekday after Christmas Day

    @Test public void testHongKongYear2005() {
        final int year = 2005;
        QL.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDate(1,JANUARY,year));    // The first day of January
        expectedHol.add(getDate(1,MAY,year));        // Labour Day
        expectedHol.add(getDate(1,OCTOBER,year));    // National Day
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // The first weekday after Christmas Day

        expectedHol.remove(getDate(1,JANUARY,year));
        expectedHol.remove(getDate(1,MAY,year));
        expectedHol.remove(getDate(1,OCTOBER,year));
        expectedHol.remove(getDate(25,DECEMBER,year));

    	expectedHol.add(getDate(3,JANUARY,year));   // 3 of the New Year's Day
    	expectedHol.add(getDate(9,FEBRUARY,year));   // Lunar New Year's Day
    	expectedHol.add(getDate(10,FEBRUARY,year));   // 2 Lunar New Year's Day
		expectedHol.add(getDate(11,FEBRUARY,year));   // The 3 day of Lunar New Year
		expectedHol.add(getDate(25,MARCH,year));     // Good Friday
		expectedHol.add(getDate(28,MARCH,year));     // Easter Monday
		expectedHol.add(getDate(5,APRIL,year));      // Ching Ming Festival
        expectedHol.remove(getDate(2,MAY,year));     // Day after labor day
        expectedHol.add(getDate(16,MAY,year));       // The day after  Buddha's Birthday
		//expectedHol.add(getDate(11,JUNE,year));       // The Tuen Ng Festival
        expectedHol.add(getDate(1,JULY,year));       // Hong Kong Special Administrative Region Establishment Day
    	expectedHol.add(getDate(19,SEPTEMBER,year));   // The day following Chinese Mid-Autumn Festival
        expectedHol.add(getDate(3,OCTOBER,year));
        expectedHol.add(getDate(11,OCTOBER,year));    // Chung Yeung festival
        expectedHol.remove(getDate(27,DECEMBER,year));

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }


	// 2005 -- http://www.hkfastfacts.com/Hong-Kong-Festivals-2004.html

	//     1-Jan-04         1 day of January
    //    22-Jan-04         Lunar New Year's Day
    //    23-Jan-04         The 2 day Lunar New Year's Day
    //    24-Jan-04         The 3 day of Lunar New Year
    //     5-Apr-04         Ching Ming Festival
    //     9-Apr-04         Good Friday
    //    12-Apr-04         Easter Monday
    //     1-May-04         Saturday Labour Day
    //    26-May-04         The Buddha's Birthday
    //    22-Jun-04         The Tuen Ng Festival
    //     1-Jul-04         Hong Kong Special Administrative Region Establishment Day
    //    28-Sep-04         The Chinese Mid-Autumn Festival
    //     1-Oct-04         The National Day
    //    22-Oct-04         Chung Yeung Festival
    //    25-Dec-04         Sunday Christmas Day
    //    27-Dec-04         The second weekday after Christmas Day

    @Test public void testHongKongYear2004() {
        final int year = 2004;
        QL.info("Testing Hong Kong's holiday list for the year " + year + "...");

        final Calendar c = HongKong.getCalendar(HongKong.Market.HKEx);
        final List<Date> expectedHol = new ArrayList<Date>();

        expectedHol.add(getDate(1,JANUARY,year));    // The first day of January
        expectedHol.add(getDate(1,MAY,year));        // Labour Day
        expectedHol.add(getDate(1,OCTOBER,year));    // National Day
        expectedHol.add(getDate(25,DECEMBER,year));  // Christmas Day
        expectedHol.add(getDate(26,DECEMBER,year));  // The first weekday after Christmas Day

        expectedHol.remove(getDate(1,MAY,year));        // Labour Day

    	expectedHol.add(getDate(22,JANUARY,year));   // Lunar New Year's Day
    	expectedHol.add(getDate(23,JANUARY,year));   // 2 Lunar New Year's Day
//		expectedHol.add(getDate(24,JANUARY,year));   // The 3 day of Lunar New Year
		expectedHol.add(getDate(5,APRIL,year));      // Ching Ming Festival
		expectedHol.add(getDate(9,APRIL,year));     // Good Friday
		expectedHol.add(getDate(12,APRIL,year));     // Easter Monday
        expectedHol.add(getDate(26,MAY,year));       // The Buddha's Birthday
		expectedHol.add(getDate(22,JUNE,year));       // The Tuen Ng Festival
        expectedHol.add(getDate(1,JULY,year));       // Hong Kong Special Administrative Region Establishment Day
    	expectedHol.add(getDate(29,SEPTEMBER,year));   // The day after Chinese Mid-Autumn Festival
        //expectedHol.add(getDate(22,OCTOBER,year));    // Chung Yeung festival
        expectedHol.remove(getDate(25,DECEMBER,year));
        expectedHol.remove(getDate(26,DECEMBER,year));
        expectedHol.add(getDate(27,DECEMBER,year));

        new CalendarUtil().checkHolidayList(expectedHol, c, year);
    }

}
