/*
 Copyright (C) 2007 Richard Gomes

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

import org.jquantlib.number.Time;
import org.jquantlib.util.Date;

/**
 * "Actual/365 (Fixed)" day count convention, also know as
 * "Act/365 (Fixed)", "A/365 (Fixed)", or "A/365F".
 * 
 * <p><b>Note:</b> According to ISDA, "Actual/365" (without "Fixed") is
 * an alias for "Actual/Actual (ISDA)"DayCounter (see
 * ActualActual.)  If Actual/365 is not explicitly
 * specified as fixed in an instrument specification,
 * you might want to double-check its meaning.
 * 
 * @category daycounters
 * 
 * @author Richard Gomes
 */
public class Actual365Fixed extends DayCounterImpl {

	/**
	 * @see DayCounter#getYearFraction(Date, Date)
	 */
	public Time getYearFraction(final Date dateStart, final Date dateEnd) {
		return new Time(getDayCount(dateStart, dateEnd)/365.0);
	}
	
	/**
	 * @see DayCounter#getYearFraction(Date, Date, Date, Date)
	 */
	public Time getYearFraction(final Date dateStart, final Date dateEnd, final Date refPeriodStart, final Date refPeriodEnd) {
		return new Time(getDayCount(dateStart, dateEnd)/365.0);
	}
	
}
