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

import static org.junit.Assert.fail;

import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;

/**
 * This is the general test base class for Calendars including generic methods.
 * 
 * @author Dominik Holenstein
 *         <p>
 */

public class CalendarUtil {

	protected void checkHolidayList(List<Date> expectedHol, Calendar c, int year) {

		List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,
				Month.JANUARY, year), DateFactory.getFactory().getDate(31,
				Month.DECEMBER, year), false);

		// JIA changed that the size of two list should be tested first
		if (hol.size() != expectedHol.size())
			fail("there were " + expectedHol.size()
					+ " expected holidays, while there are " + hol.size()
					+ " calculated holidays");

		// JIA changed so that the order of dates in the list is not relevant
		for (int i = 0; i < hol.size(); i++) {
			if(!hol.contains(expectedHol.get(i)))
				fail("expected holiday " + expectedHol.get(i)
						+ " is not in the caculated holiday list: " + hol);
		}
	}

}
