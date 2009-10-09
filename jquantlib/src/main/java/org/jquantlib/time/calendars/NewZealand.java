/*
 Copyright (C) 2008 Anand Mani

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

import static org.jquantlib.time.Month.APRIL;
import static org.jquantlib.time.Month.DECEMBER;
import static org.jquantlib.time.Month.FEBRUARY;
import static org.jquantlib.time.Month.JANUARY;
import static org.jquantlib.time.Month.JUNE;
import static org.jquantlib.time.Month.OCTOBER;
import static org.jquantlib.time.Weekday.MONDAY;
import static org.jquantlib.time.Weekday.TUESDAY;

import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Weekday;

/**
 *
 * New Zealand calendar
 * Holidays:
 *       <ul>
 *       <li>Saturdays</li>
 *       <li>Sundays</li>
 *       <li>New Year's Day, JANUARY 1st (possibly moved to Monday or
 *           Tuesday)</li>
 *       <li>Day after New Year's Day, JANUARY 2st (possibly moved to
 *           Monday or Tuesday)</li>
 *       <li>Anniversary Day, Monday nearest JANUARY 22nd</li>
 *       <li>Waitangi Day. February 6th</li>
 *       <li>Good Friday</li>
 *       <li>Easter Monday</li>
 *       <li>ANZAC Day. April 25th</li>
 *       <li>Queen's Birthday, first Monday in June</li>
 *       <li>Labour Day, fourth Monday in October</li>
 *       <li>Christmas, December 25th (possibly moved to Monday or Tuesday)</li>
 *       <li>Boxing Day, December 26th (possibly moved to Monday or
 *           Tuesday)</li>
 *       </ul>
 *@note    note The holiday rules for New Zealand were documented by
 *             David Gilbert for IDB (http://www.jrefinery.com/ibd/)
 *
 * @category calendars
 *
 * @see <a href="http://www.nzx.com">New Zealand Stock Exchange</a>
 *
 * @author Anand Mani
 * @author Zahid Hussain
 */

@QualityAssurance(quality = Quality.Q3_DOCUMENTATION, version = Version.V097, reviewers = { "Zahid Hussain" })

public class NewZealand extends Calendar {


    //
    // public constructors
    //

    public NewZealand() {
        impl = new Impl();
    }


    //
    // private final inner classees
    //

    private final class Impl extends WesternImpl {
	    @Override
	    public String name() { return "New Zealand"; }

	    @Override
	    public boolean isBusinessDay(final Date date) {
	        final Weekday w = date.weekday();
	        final int d = date.dayOfMonth(), dd = date.dayOfYear();
	        final Month m = date.month();
	        final int y = date.year();
	        final int em = easterMonday(y);
	        if (isWeekend(w)
	            // New Year's Day (possibly moved to Monday or Tuesday)
	            || ((d == 1 || (d == 3 && (w == MONDAY || w == TUESDAY))) &&
	                m == JANUARY)
	            // Day after New Year's Day (possibly moved to Mon or TUESDAY)
	            || ((d == 2 || (d == 4 && (w == MONDAY || w == TUESDAY))) &&
	                m == JANUARY)
	            // Anniversary Day, MONDAY nearest JANUARY 22nd
	            || ((d >= 19 && d <= 25) && w == MONDAY && m == JANUARY)
	            // Waitangi Day. February 6th
	            || (d == 6 && m == FEBRUARY)
	            // Good Friday
	            || (dd == em-3)
	            // Easter MONDAY
	            || (dd == em)
	            // ANZAC Day. April 25th
	            || (d == 25 && m == APRIL)
	            // Queen's Birthday, first MONDAY in June
	            || (d <= 7 && w == MONDAY && m == JUNE)
	            // Labour Day, fourth MONDAY in October
	            || ((d >= 22 && d <= 28) && w == MONDAY && m == OCTOBER)
	            // Christmas, December 25th (possibly MONDAY or TUESDAY)
	            || ((d == 25 || (d == 27 && (w == MONDAY || w == TUESDAY)))
	                && m == DECEMBER)
	            // Boxing Day, DECEMBER 26th (possibly MONDAY or TUESDAY)
	            || ((d == 26 || (d == 28 && (w == MONDAY || w == TUESDAY)))
	                && m == DECEMBER)) {
                return false;
            }
	        return true;
	    }
	}
}
