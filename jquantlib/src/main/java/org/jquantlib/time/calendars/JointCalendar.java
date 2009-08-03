/*
 Copyright (C) 2008 Srinivas Hasti
 Copyright (C) 2008 Dominik Holenstein

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

package org.jquantlib.time.calendars;

import org.jquantlib.time.AbstractCalendar;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;

/**
 * 
 * Depending on the chosen rule, this calendar has a set of business days given
 * by either the union or the intersection of the sets of business days of the
 * given calendars.
 * 
 * JOIN_HOLIDAYS - A date is a holiday for the joint calendar if it is a
 * holiday for any of the given calendars
 * 
 * JOIN_BUSINESSDAYS - A date is a business day for the joint calendar if it is
 * a business day for any of the given calendars
 * 
 * @author Srinivas Hasti
 * 
 */
public class JointCalendar extends AbstractCalendar {

    // ! rules for joining calendars
    public static enum JointCalendarRule {
        JOIN_HOLIDAYS, /*
         * !< A date is a holiday for the joint calendar if it
         * is a holiday for any of the given calendars
         */
        JOIN_BUSINESSDAYS
        /*
         * !< A date is a business day for the joint calendar if it is a
         * business day for any of the given calendars
         */
    };

    private final JointCalendarRule joinRule;
    private final Calendar[]        calendars;

    public JointCalendar(final JointCalendarRule rule, final Calendar... calendar) {
        this.calendars = calendar;
        this.joinRule = rule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jquantlib.time.Calendar#getName()
     */
    public String getName() {
        final StringBuilder builder = new StringBuilder();
        switch (joinRule) {
        case JOIN_HOLIDAYS:
            builder.append("JoinHolidays(");
            break;
        case JOIN_BUSINESSDAYS:
            builder.append("JoinBusinessDays()");
            break;
        default:
            throw new AssertionError("unknown joint calendar rule"); // TODO: message
        }
        for (final Calendar cal : calendars)
            builder.append(cal.getName() + ",");
        builder.insert(builder.length() - 1, ")");
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jquantlib.time.Calendar#isBusinessDay(org.jquantlib.util.Date)
     */
    @Override
    public boolean isBusinessDay(final Date d) {
        switch (joinRule) {
        case JOIN_HOLIDAYS:
            for (final Calendar cal : calendars)
                if (cal.isHoliday(d))
                    return false;
            return true;
        case JOIN_BUSINESSDAYS:
            for (final Calendar cal : calendars)
                if (!cal.isHoliday(d))
                    return true;
            return false;
        default:
            throw new AssertionError("unknown joint calendar rule"); // TODO: message
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jquantlib.time.Calendar#isWeekend(org.jquantlib.time.Weekday)
     */
    public boolean isWeekend(final Weekday w) {
        switch (joinRule) {
        case JOIN_HOLIDAYS:
            for (final Calendar cal : calendars)
                if (cal.isWeekend(w))
                    return true;
            return false;
        case JOIN_BUSINESSDAYS:
            for (final Calendar cal : calendars)
                if (!cal.isWeekend(w))
                    return false;
            return true;
        default:
            throw new AssertionError("unknown joint calendar rule"); // TODO: message
        }
    }
}
