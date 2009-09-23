/*
 Copyright (C) 2008 Daniel Kong

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

package org.jquantlib.daycounters;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;

/**
 * Business/252 day count convention
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Day_count_convention">Day count Convention</a>
 * 
 * @author Daniel Kong
 */
public class Business252 extends AbstractDayCounter {

    private final Calendar calendar;

    public Business252(final Calendar calendar){
        this.calendar = calendar;
    }

    @Override
    public final String name() {
        return "Business/252(" + calendar.getName() + ")";
    }

    @Override
    public final int dayCount(final Date dateStart, final Date dateEnd) {
        //TODO:int or long?
        //consider changing calendar or dayCounter? Daniel
        //        return calendar.businessDaysBetween(dateStart, dateEnd, false, false);
        return (int) calendar.businessDaysBetween(dateStart, dateEnd, false, false);

    }

    @Override
    public double yearFraction(final Date dateStart, final Date dateEnd) {
        return this.yearFraction(dateStart, dateEnd, new Date(), new Date());
    }

    @Override
    public double yearFraction(final Date dateStart, final Date dateEnd, final Date refPeriodStart, final Date refPeriodEnd) {
        return dayCount(dateStart, dateEnd)/252.0;
    }

}
