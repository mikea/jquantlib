/*
 Copyright (C) 2008 Jia Jia

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

import static org.jquantlib.time.Month.AUGUST;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.MARCH;
import static org.jquantlib.time.Month.MAY;
import static org.jquantlib.time.Month.NOVEMBER;
import static org.jquantlib.time.Month.OCTOBER;

import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Weekday;


/**
 *  Hungarian calendar
 *  Holidays:
 *       <ul>
 *       <li>Saturdays</li>
 *       <li>Sundays</li>
 *       <li>Easter Monday</li>
 *       <li>Whit(Pentecost) Monday </li>
 *       <li>New Year's Day, JANUARY 1st</li>
 *       <li>National Day, March 15th</li>
 *       <li>Labour Day, May 1st</li>
 *       <li>Constitution Day, August 20th</li>
 *       <li>Republic Day, October 23rd</li>
 *       <li>All Saints Day, November 1st</li>
 *       <li>Christmas, December 25th</li>
 *       <li>2nd Day of Christmas, December 26th</li>
 *       </ul>
 *       in group calendars
 *
 * @author Jia Jia
 * @author Zahid Hussain
 *
 */

@QualityAssurance(quality = Quality.Q3_DOCUMENTATION, version = Version.V097, reviewers = { "Zahid Hussain" })

public class Hungary extends Calendar {

    //
    // public constructors
    //

	public Hungary() {
		impl = new Impl();
	}

    //
    // private final inner classes
    //

    private final class Impl extends WesternImpl {

    	@Override
    	public String name() { return "Hungary"; }

    	@Override
        public boolean isBusinessDay(final Date date) {
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth(), dd = date.dayOfYear();
            final Month m = date.month();
            final int y = date.year();
            final int em = easterMonday(y);
            if (isWeekend(w)
                // Easter Monday
                || (dd == em)
                // Whit Monday
                || (dd == em+49)
                // New Year's Day
                || (d == 1  && m == JANUARY)
                // National Day
                || (d == 15  && m == MARCH)
                // Labour Day
                || (d == 1  && m == MAY)
                // Constitution Day
                || (d == 20  && m == AUGUST)
                // Republic Day
                || (d == 23  && m == OCTOBER)
                // All Saints Day
                || (d == 1  && m == NOVEMBER)
                // Christmas
                || (d == 25 && m == DECEMBER)
                // 2nd Day of Christmas
                || (d == 26 && m == DECEMBER)) {
                return false;
            }
            return true;
        }
    }
}
