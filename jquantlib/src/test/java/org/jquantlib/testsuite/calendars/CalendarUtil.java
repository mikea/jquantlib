/*
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

package org.jquantlib.testsuite.calendars;

import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the general test base class for Calendars including generic methods.
 * 
 * @author Dominik Holenstein
 */

public class CalendarUtil {

    private final static Logger logger = LoggerFactory.getLogger(CalendarUtil.class);

    protected void checkHolidayList(final List<Date> expected, final Calendar c, final int year) {

        final List<Date> calculated = c.getHolidayList(
                DateFactory.getFactory().getDate(1, Month.JANUARY, year), 
                DateFactory.getFactory().getDate(31, Month.DECEMBER, year), 
                false);

        final StringBuilder sb = new StringBuilder();
        int error = 0;
        
        for (Date date : expected) {
            if (!calculated.contains(date)) {
                sb.append("Expected but not calculated holiday ").append(date).append('\n');
                error++;
            }
        }

        for (Date date : calculated) {
            if (!expected.contains(date)) {
                sb.append("Calculated but not expected holiday ").append(date).append('\n');
                error++;
            }
        }
        
        if (error>0) {
            logger.error(sb.toString(), new Exception());
            Assert.fail(sb.toString());
        }

    }

}
