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
package org.jquantlib.time.calendars;

import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;

import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * @author Srinivas Hasti
 */
public class Target extends DelegateCalendar {
    private final static Target TARGET_CALENDAR = new Target();

    private Target() {
        setDelegate(new TargetCalendarImpl());
    }

    public static Target getCalendar() {
        return TARGET_CALENDAR;
    }

    private final class TargetCalendarImpl extends WesternCalendar {

        public String getName() {
            return "TARGET";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            Month m = date.getMonthEnum();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            		// New Year's Day
                    || (d == 1 && m == JANUARY)
                    // Good Friday
                    || (dd == em - 3 && y >= 2000)
                    // Easter Monday
                    || (dd == em && y >= 2000)
                    // Labour Day
                    || (d == 1 && m == MAY && y >= 2000)
                    // Christmas
                    || (d == 25 && m == DECEMBER)
                    // Day of Goodwill
                    || (d == 26 && m == DECEMBER && y >= 2000)
                    // December 31st, 1998, 1999, and 2001 only
                    || (d == 31 && m == DECEMBER && (y == 1998 || y == 1999 || y == 2001)))
                return false;
            return true;
        }
    }
}
