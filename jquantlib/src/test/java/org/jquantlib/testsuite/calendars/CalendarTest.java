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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jquantlib.QL;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.time.calendars.UnitedStates;
import org.junit.Assert;
import org.junit.Test;


/**
 *
 * @author Zahid Hussain
 *
 */
public class CalendarTest {

    public CalendarTest() {
        QL.info("::::: " + this.getClass().getSimpleName() + " :::::");
    }

    @Test
    public void testAdvance() {
        final NullCalendar nullCalendar = new NullCalendar();
        final Date d = new Date(11, Month.October, 2009);
        final Date dCopy = d.clone();
        assertEquals(dCopy, d);
        final Date advancedDate = nullCalendar.advance(d, new Period(3, TimeUnit.Months));
        assertEquals(dCopy, d);
        assertFalse(advancedDate.equals(d));
    }

    @Test
    public void testEndOfMonth() {
        QL.info("Testing end-of-month calculation...");

        final Calendar c = new Target(); // any calendar would be OK

        Date eom;
        final Date counter = Date.minDate();
        final Date last = Date.maxDate().sub(new Period(2, TimeUnit.Months));

        while (counter.le(last)) {
            eom = c.endOfMonth(counter);
            // check that eom is eom
            if (!c.isEndOfMonth(eom)) {
                Assert.fail(String.format("%s %s is not the last business day in %s according to %s",
                        new Object[] { eom.weekday(), eom, eom.month(), eom.year(), c.name() }));
            }
            // check that eom is in the same month as counter
            if (eom.month()!=counter.month()) {
                Assert.fail(String.format("%s is not the same month as %s",
                        new Object[] { eom, counter }));
            }
            counter.addAssign(1);
        }
    }

    @Test
    public void testAdjust_ModifiedFollowing() {
    	System.out.println("Testing BusinessDayConvention.ModifiedFollowing");

        final class Entry {
            public Date date;
            public Date expected;

            private Entry (final Date d, final Date e) {
                date = d;
                expected = e;
            }
        }

        final Entry[] entries = {
                new Entry( new Date(28, 5, 2009), new Date(28, 5, 2009) ),
                new Entry( new Date(29, 5, 2009), new Date(29, 5, 2009) ),
                new Entry( new Date(30, 5, 2009), new Date(29, 5, 2009) ),
                new Entry( new Date(31, 5, 2009), new Date(29, 5, 2009) ),
                new Entry( new Date( 1, 6, 2009), new Date( 1, 6, 2009) ),
                new Entry( new Date( 2, 6, 2009), new Date( 2, 6, 2009) ),
                new Entry( new Date( 3, 6, 2009), new Date( 3, 6, 2009) ),
                // ---
                new Entry( new Date(23, 1, 1973), new Date(23, 1, 1973) ),
                new Entry( new Date(24, 1, 1973), new Date(24, 1, 1973) ),
                new Entry( new Date(25, 1, 1973), new Date(26, 1, 1973) ),
                new Entry( new Date(26, 1, 1973), new Date(26, 1, 1973) ),
            };

        final Calendar unitedStatesCalendar = new UnitedStates(UnitedStates.Market.NYSE);
        for (final Entry entry : entries) {
            final Date result = unitedStatesCalendar.adjust(entry.date, BusinessDayConvention.ModifiedFollowing);
            System.out.println("adjusted is " + result.isoDate() + "  ::  expected is " + entry.expected.isoDate());
            assertEquals(result, entry.expected);
        }
    }

    @Test
    public void testAdjust_ModifiedPreceeding() {
        System.out.println("Testing BusinessDayConvention.ModifiedPreceding");

        final class Entry {
            public Date date;
            public Date expected;

            private Entry (final Date d, final Date e) {
                date = d;
                expected = e;
            }
        }

        final Entry[] entries = {
                new Entry( new Date(28, 5, 2009), new Date(28, 5, 2009) ),
                new Entry( new Date(29, 5, 2009), new Date(29, 5, 2009) ),
                new Entry( new Date(30, 5, 2009), new Date(29, 5, 2009) ),
                new Entry( new Date(31, 5, 2009), new Date(29, 5, 2009) ),
                new Entry( new Date( 1, 6, 2009), new Date( 1, 6, 2009) ),
                new Entry( new Date( 2, 6, 2009), new Date( 2, 6, 2009) ),
                new Entry( new Date( 3, 6, 2009), new Date( 3, 6, 2009) ),
                // ---
                new Entry( new Date(23, 1, 1973), new Date(23, 1, 1973) ),
                new Entry( new Date(24, 1, 1973), new Date(24, 1, 1973) ),
                new Entry( new Date(25, 1, 1973), new Date(24, 1, 1973) ),
                new Entry( new Date(26, 1, 1973), new Date(26, 1, 1973) ),
            };

        final Calendar unitedStatesCalendar = new UnitedStates(UnitedStates.Market.NYSE);
        for (final Entry entry : entries) {
            final Date result = unitedStatesCalendar.adjust(entry.date, BusinessDayConvention.ModifiedPreceding);
            System.out.println("adjusted is " + result.isoDate() + "  ::  expected is " + entry.expected.isoDate());
            assertEquals(result, entry.expected);
        }
    }

}
