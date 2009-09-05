/*
 Copyright (C) 2009 Zahid Hussain
 
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

import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;

/**
 * Calendar tests related common methods
 * 
* @author Zahid Hussain
*
*/
public class BaseCalendarTest {
	private final DateFactory df = DateFactory.getFactory();

	public DateFactory getDateFactory() {
		return df;
	}
	
	public Date getDate(int day, Month month, int year){
		return df.getDate(day, month, year);
	}
}
