/*
 Copyright (C) 2007 Srinivas Hasti

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

package org.jquantlib.testsuite.time;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.DateGenerationRule;
import org.jquantlib.time.Period;
import org.jquantlib.time.Schedule;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.junit.Before;
import org.junit.Test;

public class ScheduleTest {

    Date startDate = null;

    @Before
    public void init() {
        DateFactory dateFactory = DateFactory.getFactory();
        startDate = dateFactory.getDate(20, Month.AUGUST, 2007);
    }

    @Test
    public void testSchedule() {
        Calendar calendar = Target.getCalendar();
        DateFactory dateFactory = DateFactory.getFactory();
        Period maturity = new Period(30, TimeUnit.YEARS);
        Date maturityDate = dateFactory.getDate(startDate.getDayOfMonth(), startDate.getMonthEnum(), startDate.getYear()).adjust(
                maturity);
        Period accPeriodTenor = new Period(6, TimeUnit.MONTHS);
        BusinessDayConvention modFollow = BusinessDayConvention.MODIFIED_FOLLOWING;
        DateGenerationRule dateRule = DateGenerationRule.BACKWARD;


        
        
        // TODO: make sure all sources are synchronized properly and Schedule API is consistent
        
        Schedule firstConstrSchedule = new Schedule(startDate, maturityDate, accPeriodTenor, calendar, modFollow, modFollow, 
                dateRule, false, null, null);
        
//        // introduced to get compatibility with v.0.8.1 - becomes redundant asa we can use the dategenerationrule style...
//        Schedule firstConstrSchedule = new Schedule(startDate, maturityDate, accPeriodTenor, calendar, modFollow, modFollow,
//                dateRule, false, true, null, null);

        
        
        List<Date> dates = new ArrayList<Date>();
        dates.add(startDate);
        dates.add(calendar.advance(startDate, new Period(10, TimeUnit.WEEKS),modFollow));

        Schedule secondConstrSchedule = new Schedule(dates, calendar, modFollow);

        testDateAfter(firstConstrSchedule);
        testDateAfter(secondConstrSchedule);

        testNextAndPrevDate(firstConstrSchedule);
        testNextAndPrevDate(secondConstrSchedule);

        testIsRegular(firstConstrSchedule);
       
    }

    private void testDateAfter(Schedule schedule) {
        Iterator<Date> dates = schedule.getDatesAfter(startDate);
        while (dates.hasNext()) {
            assertTrue(startDate.lt(dates.next()));
        }

        dates = schedule.getDatesAfter(startDate);
        while (dates.hasNext()) {
            assertTrue(startDate.lt(dates.next()));
        }

    }

    private void testNextAndPrevDate(Schedule schedule) {
        Date nextDate = schedule.getNextDate(startDate);
        assertTrue(nextDate.gt(startDate));

        Date prevDate = schedule.getPreviousDate(nextDate);
        assertTrue(nextDate.gt(prevDate));

        assertTrue(prevDate.lt(nextDate));
    }

    private void testIsRegular(Schedule schedule) {
        for (int i = 0; i < 2; i++) {
            schedule.isRegular(i+1);
        }
    }
}
