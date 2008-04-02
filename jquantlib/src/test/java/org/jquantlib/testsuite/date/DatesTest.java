/*
 Copyright (C) 2008 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */
package org.jquantlib.testsuite.date;

import static org.junit.Assert.assertFalse;

import org.jquantlib.time.IMM;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateParser;
import org.jquantlib.util.Date.Month;
import org.junit.Test;

/**
 * Test various Dates
 * 
 * @author Srinivas Hasti
 * 
 */
public class DatesTest {

    static final String IMMcodes[] = { "F0", "G0", "H0", "J0", "K0", "M0",
            "N0", "Q0", "U0", "V0", "X0", "Z0", "F1", "G1", "H1", "J1", "K1",
            "M1", "N1", "Q1", "U1", "V1", "X1", "Z1", "F2", "G2", "H2", "J2",
            "K2", "M2", "N2", "Q2", "U2", "V2", "X2", "Z2", "F3", "G3", "H3",
            "J3", "K3", "M3", "N3", "Q3", "U3", "V3", "X3", "Z3", "F4", "G4",
            "H4", "J4", "K4", "M4", "N4", "Q4", "U4", "V4", "X4", "Z4", "F5",
            "G5", "H5", "J5", "K5", "M5", "N5", "Q5", "U5", "V5", "X5", "Z5",
            "F6", "G6", "H6", "J6", "K6", "M6", "N6", "Q6", "U6", "V6", "X6",
            "Z6", "F7", "G7", "H7", "J7", "K7", "M7", "N7", "Q7", "U7", "V7",
            "X7", "Z7", "F8", "G8", "H8", "J8", "K8", "M8", "N8", "Q8", "U8",
            "V8", "X8", "Z8", "F9", "G9", "H9", "J9", "K9", "M9", "N9", "Q9",
            "U9", "V9", "X9", "Z9" };

    @Test
    public void immDates() {
        Date counter = Date.getMinDate();

        // 10 years of futures must not exceed Date::maxDate
        Period period = new Period(10, TimeUnit.Years);
        Date last = Date.getMaxDate().dec(period);
        Date imm;

        while (counter.le(last)) {
            imm = IMM.nextDate(counter, false);

            // check that imm is greater than counter
            assertFalse(imm + " is not greater than " + counter, imm
                    .le(counter));

            // check that imm is an IMM date
            assertFalse(
                    imm + " is not an IMM date (calculated from " + counter,
                    !IMM.isIMMdate(imm, false));

            // check that imm is <= to the next IMM date in the main cycle
            assertFalse(
                    imm
                            + " is not less than or equal to the next future in the main cycle "
                            + IMM.nextDate(counter, true), imm.gt(IMM.nextDate(
                            counter, true)));

            // check that for every date IMMdate is the inverse of IMMcode
            assertFalse(IMM.code(imm) + "at calendar day " + counter
                    + " is not the IMM code matching " + imm, !IMM.date(
                    IMM.code(imm), counter).eq(imm));

            // check that for every date the 120 IMM codes refer to future dates
            for (int i = 0; i < 40; ++i) {
                assertFalse(IMM.date(IMMcodes[i], counter) + "is wrong for "
                        + IMMcodes[i] + " at reference date " + counter, IMM
                        .date(IMMcodes[i], counter).le(counter));
            }

            counter.inc();
        }
    }

    @Test
    public void consistencyCheck() {

        System.out.println("Testing dates...");

        int minDate = Date.getMinDate().getValue() - 1;
        int maxDate = Date.getMaxDate().getValue();

        int dyold = new Date(minDate - 1).getDayOfYear(), dold = new Date(
                minDate - 1).getDayOfMonth(), mold = new Date(minDate - 1)
                .getMonth(), yold = new Date(minDate - 1).getYear(), wdold = new Date(
                minDate - 1).getWeekday().toInteger();

        for (int i = minDate; i <= maxDate; i++) {
            Date t = new Date(i);

            int serial = t.getValue();

            // check serial number consistency
            assertFalse("inconsistent serial number: \n"
                    + "    original:               \n" + "    date:          "
                    + t + "    serial number: " + serial, (serial != i));

            Integer dy = t.getDayOfYear(), d = t.getDayOfMonth(), m = t
                    .getMonth(), y = t.getYear(), wd = t.getWeekday()
                    .toInteger();

            // check if skipping any date
            assertFalse(
                    "wrong day of year increment: \n" + "    date: " + t + "\n"
                            + "    day of year: " + dy + "\n"
                            + "    Previous:    " + dyold,
                    !((dy == dyold + 1)
                            || (dy == 1 && dyold == 365 && !Date.isLeap(yold)) || (dy == 1
                            && dyold == 366 && Date.isLeap(yold))));

            dyold = dy;

            assertFalse("wrong day,month,year increment: \n" + "    date: " + t
                    + "\n" + "    day,month,year: " + d + "," + m + "," + y
                    + "\n" + "    previous:       " + dold + "," + mold + ","
                    + yold, !((d == dold + 1 && m == mold && y == yold)
                    || (d == 1 && m == mold + 1 && y == yold) || (d == 1
                    && m == 1 && y == yold + 1)));

            dold = d;
            mold = m;
            yold = y;

            // check month definition
            assertFalse("invalid month: \n" + "    date:  " + t + "\n"
                    + "    month: " + m, m < 1 || m > 12);

            // check day definition
            assertFalse("invalid day of month: \n" + "    date:  " + t + "\n"
                    + "    day: " + d, d < 1);

            assertFalse("invalid day of month: \n" + "    date:  " + t + "\n"
                    + "    day: " + d, !((m == 1 && d <= 31)
                    || (m == 2 && d <= 28)
                    || (m == 2 && d == 29 && Date.isLeap(y))
                    || (m == 3 && d <= 31) || (m == 4 && d <= 30)
                    || (m == 5 && d <= 31) || (m == 6 && d <= 30)
                    || (m == 7 && d <= 31) || (m == 8 && d <= 31)
                    || (m == 9 && d <= 30) || (m == 10 && d <= 31)
                    || (m == 11 && d <= 30) || (m == 12 && d <= 31)));

            // check weekday definition
            assertFalse("invalid weekday: \n" + "    date:  " + t + "\n"
                    + "    weekday:  " + wd + "\n" + "    previous: " + wdold,
                    !(((wd) == (wdold + 1)) || ((wd) == 1 && (wdold) == 7)));

            wdold = wd;

            // create the same date with a different constructor
            Date s = new Date(d, Month.valueOf(m), y);

            // check serial number consistency
            serial = s.getValue();
            assertFalse("inconsistent serial number:\n" + "    date:          "
                    + t + "\n" + "    serial number: " + i + "\n"
                    + "    cloned date:   " + s + "\n" + "    serial number: "
                    + serial, serial != i);
        }

    }

    @Test
    public void isoDates() {
        System.out.println("Testing ISO dates...");
        String input_date = "2006-01-15";
        Date d = DateParser.parseISO(input_date);
        assertFalse("Iso date failed\n" + " input date:    \n"
                + " day of month:  " + d.getDayOfMonth() + "\n"
                + " month:         " + d.getMonth() + "\n" + " year:         "
                + d.getYear(), d.getDayOfMonth() != 15
                || Date.Month.valueOf(d.getMonth()) != Date.Month.January
                || d.getYear() != 2006);
    }
}
