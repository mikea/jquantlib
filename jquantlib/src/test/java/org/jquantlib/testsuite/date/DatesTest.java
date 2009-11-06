/*
 Copyright (C) 2008 Srinivas Hasti

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

package org.jquantlib.testsuite.date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Date;
import org.jquantlib.time.DateParser;
import org.jquantlib.time.IMM;
import org.jquantlib.time.Month;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.StopClock;
import org.junit.Test;


/**
 * Test various Dates
 *
 * @author Srinivas Hasti
 *
 */
public class DatesTest {

    static private final String IMMcodes[] = { "F0", "G0", "H0", "J0", "K0", "M0", "N0", "Q0", "U0", "V0", "X0", "Z0",
        "F1", "G1", "H1", "J1", "K1", "M1", "N1", "Q1", "U1", "V1", "X1", "Z1", "F2", "G2", "H2", "J2", "K2", "M2",
        "N2", "Q2", "U2", "V2", "X2", "Z2", "F3", "G3", "H3", "J3", "K3", "M3", "N3", "Q3", "U3", "V3", "X3", "Z3",
        "F4", "G4", "H4", "J4", "K4", "M4", "N4", "Q4", "U4", "V4", "X4", "Z4", "F5", "G5", "H5", "J5", "K5", "M5",
        "N5", "Q5", "U5", "V5", "X5", "Z5", "F6", "G6", "H6", "J6", "K6", "M6", "N6", "Q6", "U6", "V6", "X6", "Z6",
        "F7", "G7", "H7", "J7", "K7", "M7", "N7", "Q7", "U7", "V7", "X7", "Z7", "F8", "G8", "H8", "J8", "K8", "M8",
        "N8", "Q8", "U8", "V8", "X8", "Z8", "F9", "G9", "H9", "J9", "K9", "M9", "N9", "Q9", "U9", "V9", "X9", "Z9" };

    public DatesTest() {
        QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
    }

    @Test
    public void immDates() {
        QL.info("Testing imm dates. It may take several minutes when Cobertura reports are generated!!!");

        final Date minDate = Date.minDate();
        final Date maxDate = Date.maxDate();

        final Date counter = minDate.clone();

        // 10 years of futures must not exceed Date::maxDate
        final Period period = new Period(-10, TimeUnit.Years);
        final Date last = maxDate.clone().addAssign(period);

        final StopClock clock = new StopClock();
        clock.startClock();
        while (counter.le(last)) {

            final Date immDate = IMM.nextDate(counter, false);

            // check that imm is greater than counter
            if (immDate.le(counter)) {
                fail("\n  " + immDate.weekday() + " " + immDate + " is not greater than " + counter.weekday() + " " + counter);
            }

            // check that imm is an IMM date
            if (!IMM.isIMMdate(immDate, false)) {
                fail("\n  " + immDate.weekday() + " " + immDate + " is not an IMM date (calculated from " + counter.weekday() + " " + counter + ")");
            }

            // check that imm is <= to the next IMM date in the main cycle
            if (immDate.gt(IMM.nextDate(counter, true))) {
                fail("\n  " + immDate.weekday() + " " + immDate + " is not less than or equal to the next future in the main cycle " + IMM.nextDate(counter, true));
            }

            //
            // COMMENTED AT SOURCE QuantLib 0.8.1
            //
            // // check that if counter is an IMM date, then imm==counter
            // if (IMM::isIMMdate(counter, false) && (imm!=counter))
            // fail("\n "
            // + counter.weekday() + " " + counter
            // + " is already an IMM date, while nextIMM() returns "
            // + IMM.getDefaultIMM().weekday() + " " + imm);

            // check that for every date IMMdate is the inverse of IMMcode
            if (!IMM.date(IMM.code(immDate), counter).equals(immDate)) {
                fail("\n  " + IMM.code(immDate) + " at calendar day " + counter + " is not the IMM code matching " + immDate);
            }

            // check that for every date the 120 IMM codes refer to future dates
            for (int i = 0; i < 40; ++i) {
                if (IMM.date(IMMcodes[i], counter).lt(counter)) {
                    fail("\n  " + IMM.date(IMMcodes[i], counter) + " is wrong for " + IMMcodes[i] + " at reference date " + counter);
                }
            }

            counter.inc();
        }
        clock.stopClock();
        clock.log();
    }

    @Test
    public void consistencyCheck() {

        QL.info("Testing dates...");

        final Date minD = Date.minDate();
        final Date maxD = Date.maxDate();

        int dyold = minD.dayOfYear();
        int dold  = minD.dayOfMonth();
        int mold  = minD.month().value();
        int yold  = minD.year();
        Weekday wdold = minD.weekday();

        final Date minDate = minD.clone().inc();
        final Date maxDate = maxD.clone().dec();

        final StopClock clock = new StopClock();
        clock.startClock();
        for (final Date t = minDate; t.le(maxDate); t.inc()) {
            final int dy = t.dayOfYear();
            final int d  = t.dayOfMonth();
            final int m  = t.month().value();
            final int y  = t.year();
            final Weekday wd = t.weekday();

            // check if skipping any date
            if (!((dy == dyold + 1)
                    || (dy == 1 && dyold == 365 && !Date.isLeap(yold))
                    || (dy == 1 && dyold == 366 && Date.isLeap(yold)))) {
                fail("wrong day of year increment: \n"
                        + "    date: " + t + "\n"
                        + "    day of year: " + dy + "\n"
                        + "    previous:    " + dyold);
            }

            dyold = dy;

            if (!((d == dold + 1 && m == mold && y == yold) || (d == 1 && m == mold + 1 && y == yold) || (d == 1 && m == 1 && y == yold + 1)) ) {
                fail("wrong day,month,year increment: \n"
                        + "    date: " + t + "\n"
                        + "    day,month,year: " + d + "," + m + "," + y + "\n"
                        + "    previous:       " + dold + "," + mold + "," + yold);
            }

            dold = d;
            mold = m;
            yold = y;

            // check month definition
            if ((m < 1 || m > 12)) {
                fail("invalid month: \n" + "    date:  " + t + "\n" + "    month: " + m);
            }

            // check day definition
            if ((d < 1)) {
                fail("invalid day of month: \n" + "    date:  " + t + "\n" + "    day: " + d);
            }

            if (!((m == 1 && d <= 31)
                    || (m == 2 && d <= 28) || (m == 2 && d == 29 && Date.isLeap(y))
                    || (m == 3 && d <= 31) || (m == 4 && d <= 30) || (m == 5 && d <= 31) || (m == 6 && d <= 30)
                    || (m == 7 && d <= 31) || (m == 8 && d <= 31) || (m == 9 && d <= 30) || (m == 10 && d <= 31)
                    || (m == 11 && d <= 30) || (m == 12 && d <= 31))) {
                fail("invalid day of month: \n" + "    date:  " + t + "\n" + "    day: " + d);
            }

            // check weekday definition
            if (!((wd.value() == wdold.value() + 1) || (wd.value() == 1 && wdold.value() == 7))) {
                fail("invalid weekday: \n"
                        + "    date:  " + t + "\n"
                        + "    weekday:  " + wd + "\n"
                        + "    previous: " + wdold);
            }
            wdold = wd;
        }
        clock.stopClock();
        clock.log();

    }

    @Test
    public void isoDates() {
        QL.info("Testing ISO dates...");
        final StopClock clock = new StopClock();
        clock.startClock();
        final String input_date = "2006-01-15";
        final Date d = DateParser.parseISO(input_date);
        if ((d.dayOfMonth() != 15) || (d.month() != Month.January) || (d.year() != 2006)) {
            fail("Iso date failed\n"
                    + " input date:    " + input_date + "\n"
                    + " day of month:  " + d.dayOfMonth() + "\n"
                    + " month:         " + d.month() + "\n"
                    + " year:          " + d.year());
        }
        clock.stopClock();
        clock.log();
    }

    @Test
    public void testLowerUpperBound() {
        final List<Date> dates = new ArrayList<Date>();

        dates.add(new Date(1,1,2009));
        dates.add(new Date(2,1,2009));
        dates.add(new Date(3,1,2009));
        dates.add(new Date(3,1,2009));
        dates.add(new Date(4,1,2009));
        dates.add(new Date(5,1,2009));
        dates.add(new Date(7,1,2009));
        dates.add(new Date(7,1,2009));
        dates.add(new Date(8,1,2009));

        final Date lowerDate = new Date(3,1,2009);
        final Date upperDate = new Date(7,1,2009);
        final int expectedLowerBound = 2;
        final int expectedUpperBound = 8;
        final int lowerBound = Date.lowerBound(dates, lowerDate);
        final int upperBound = Date.upperBound(dates, upperDate);

        assertEquals(lowerBound, expectedLowerBound);
        assertEquals(upperBound, expectedUpperBound);

    }

}
