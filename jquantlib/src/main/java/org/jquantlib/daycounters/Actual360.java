/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/*
 Copyright (C) 2004 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib.daycounters;

import org.jquantlib.util.Date;

/**
 * "Actual/365 (Fixed)" day count convention, also know as
 * "Act/365 (Fixed)", "A/365 (Fixed)", or "A/365F".
 * 
 * @note According to ISDA, "Actual/365" (without "Fixed") is
 * an alias for "Actual/Actual (ISDA)"DayCounter (see
 * ActualActual.)  If Actual/365 is not explicitly
 * specified as fixed in an instrument specification,
 * you might want to double-check its meaning.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Day_count_convention">Day count Convention</a>
 * 
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
public class Actual360 extends AbstractDayCounter {

	private static Actual360	actual360	= new Actual360();

	private Actual360() {
	}

	public static Actual360 getDayCounter() {
		return actual360;
	}

	public final String getName() /* @ReadOnly */{
		return "Actual/360";
	}

	public/*@Time*/double getYearFraction(final Date dateStart, final Date dateEnd) /* @ReadOnly */{
		return getDayCount(dateStart, dateEnd) / 360.0;
	}

	public/*@Time*/double getYearFraction(final Date dateStart, final Date dateEnd, final Date refPeriodStart,
			final Date refPeriodEnd) /* @ReadOnly */{
		return getDayCount(dateStart, dateEnd) / 360.0;
	}

}
