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
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006, 2007 StatPro Italia srl

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
 * This class provides methods for determining the length of a time
 * period according to given market convention, both as a number
 * of days and as a year fraction.
 *
 * <p>The Bridge pattern is used to provide the base behavior of the
 * day counter.
 *
 * @category daycounters
 *   
 * @author Richard Gomes
 */
public interface DayCounter {

    /**
     * Returns the number of days between two dates
	 * 
	 * @param dateStart is the starting Date
	 * @param dateEnd is the ending Date
	 * @return the number of days between two dates.
	 */
	public int getDayCount(final Date dateStart, final Date dateEnd);
    
    /**
     * Returns the period between two dates as a fraction of year
	 * 
	 * @param dateStart is the starting Date
	 * @param dateEnd is the ending Date
	 * @return the period between two dates as a fraction of year.
	 */
	// FIXME: verify if necessary ::: document better
	public /*@Time*/ double getYearFraction(final Date dateStart, final Date dateEnd);
	
	/**
	 * Returns the period between two dates as a fraction of year, considering referencing
	 * dates for both.
	 * 
	 * @param dateStart
	 * @param dateEnd
	 * @param refPeriodStart
	 * @param refPeriodEnd
	 * @return the period between two dates as a fraction of year, considering referencing
	 * dates for both.
	 */
	// FIXME: verify if necessary ::: document better
	public /*@Time*/ double getYearFraction(final Date dateStart, final Date dateEnd, final Date refPeriodStart, final Date refPeriodEnd);

}
