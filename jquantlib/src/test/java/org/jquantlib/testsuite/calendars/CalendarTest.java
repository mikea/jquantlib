/*
 Copyright (C) 2009

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

import junit.framework.Assert;

import org.jquantlib.QL;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.time.calendars.UnitedStates;
import org.junit.Test;

/**
 *
 * @author Zahid Hussain
 *
 */
public class CalendarTest {

    public CalendarTest() {
        QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
    }

    @Test
    public void testAdvance() {
        final NullCalendar nullCalendar = new NullCalendar();
        final Date d = new Date(11, Month.OCTOBER, 2009);
        final Date dCopy = d.clone();
        Assert.assertEquals(dCopy, d);
        final Date advancedDate = nullCalendar.advance(d, new Period(3, TimeUnit.MONTHS));
        Assert.assertEquals(dCopy, d);
        Assert.assertFalse(advancedDate.equals(d));
    }


    /**
     * @see <a href="http://bugs.jquantlib.org/view.php?id=356">issue 356</a>
     */
    @Test
    public void testEndOfMonth() {
        final Calendar unitedStatesCalendar = new UnitedStates(UnitedStates.Market.NYSE);
        final boolean expected[] = new boolean[] { false, true, false, false, false };
        for (int days=0; days<expected.length; days++) {
            final Date date = new Date(28, 5, 2009);
            date.addAssign(days);
            final boolean result = unitedStatesCalendar.isEndOfMonth(date);
            System.out.printf("%s is the last business day? %b\n", date.isoDate().toString(), result);
            Assert.assertTrue(result==expected[days]);
        }
    }

}
