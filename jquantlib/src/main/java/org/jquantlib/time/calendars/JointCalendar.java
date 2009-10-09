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

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Weekday;

/**
 * Depending on the chosen rule, this calendar has a set of business days given
 * by either the union or the intersection of the sets of business days of the
 * given calendars.
 *
 * @category calendars
 *
 * @author Srinivas Hasti
 * @author Richard Gomes
 *
 */
@QualityAssurance(quality = Quality.Q3_DOCUMENTATION, version = Version.V097, reviewers = { "Richard Gomes" })
public class JointCalendar extends Calendar {

    /**
     * Rules for joining calendars
     */
    public static enum JointCalendarRule {
        /**
         * A date is a holiday for the joint calendar if it
         * is a holiday for any of the given calendars
         */
        JoinHolidays,

        /**
         * A date is a business day for the joint calendar if it is a
         * business day for any of the given calendars
         */
        JoinBusinessDays
    };


    //
    // private fields
    //

    private final JointCalendarRule joinRule;
    private final Calendar[]        calendars;


    //
    // public constructors
    //

    public JointCalendar(final Calendar c1, final Calendar c2, final JointCalendarRule rule) {
        this(rule, c1, c2);
    }

    public JointCalendar(final Calendar c1, final Calendar c2, final Calendar c3, final JointCalendarRule rule) {
        this(rule, c1, c2, c3);
    }

    public JointCalendar(final Calendar c1, final Calendar c2, final Calendar c3, final Calendar c4, final JointCalendarRule rule) {
        this(rule, c1, c2, c3, c4);
    }


    //
    // private constructors
    //

    private JointCalendar(final JointCalendarRule rule, final Calendar ...calendars) {
        this.calendars = new Calendar[calendars.length];
        for (int i=0; i<calendars.length; i++) {
            this.calendars[i] = calendars[i];
        }
        this.joinRule = rule;
        this.impl = new Impl();
    }


    //
    // private final inner classes
    //

    private final class Impl extends Calendar.Impl {
        @Override
        public String name() /* @ReadOnly */{
            final StringBuilder sb = new StringBuilder();

            switch (joinRule) {
            case JoinHolidays:
                sb.append("JoinHolidays(");
                break;
            case JoinBusinessDays:
                sb.append("JoinBusinessDays(");
                break;
            default:
                QL.error(UNKNOWN_MARKET);
                throw new LibraryException(UNKNOWN_MARKET);
            }

            int count = 0;
            for (final Calendar calendar : calendars) {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append(calendar.name());
                count++;
            }
            sb.append(')');
            return sb.toString();
        }

        @Override
        public boolean isWeekend(final Weekday weekday) /* @ReadOnly */{
            switch (joinRule) {
            case JoinHolidays:
                for (final Calendar calendar : calendars) {
                    if (calendar.isWeekend(weekday)) {
                        return true;
                    }
                }
                return false;
            case JoinBusinessDays:
                for (final Calendar calendar : calendars) {
                    if (! calendar.isWeekend(weekday)) {
                        return false;
                    }
                }
                return true;
            default:
                QL.error(UNKNOWN_MARKET);
                throw new LibraryException(UNKNOWN_MARKET);
            }
        }

        @Override
        public boolean isBusinessDay(final Date date) /* @ReadOnly */{
            switch (joinRule) {
            case JoinHolidays:
                for (final Calendar calendar : calendars) {
                    if (calendar.isBusinessDay(date)) {
                        return true;
                    }
                }
                return false;
            case JoinBusinessDays:
                for (final Calendar calendar : calendars) {
                    if (! calendar.isBusinessDay(date)) {
                        return false;
                    }
                }
                return true;
            default:
                QL.error(UNKNOWN_MARKET);
                throw new LibraryException(UNKNOWN_MARKET);
            }
        }
    }

}
