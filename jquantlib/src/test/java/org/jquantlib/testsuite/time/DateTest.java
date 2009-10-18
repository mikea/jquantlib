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

package org.jquantlib.testsuite.time;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.time.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Zahid Hussain
 *
 */
public class DateTest {

    public DateTest() {
        QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
    }


    @Test
    public void testLowerUpperBound() {
		List<Date> dates = new ArrayList<Date>();

		dates.add(new Date(1,1,2009));
		dates.add(new Date(2,1,2009));
		dates.add(new Date(3,1,2009));
		dates.add(new Date(3,1,2009));
		dates.add(new Date(4,1,2009));
		dates.add(new Date(5,1,2009));
		dates.add(new Date(7,1,2009));
		dates.add(new Date(7,1,2009));
		dates.add(new Date(8,1,2009));
		
		Date lowerDate = new Date(3,1,2009);
		Date upperDate = new Date(7,1,2009);
		int expectedLowerBound = 2;
		int expectedUpperBound = 8;
		int lowerBound = Date.lowerBound(dates, lowerDate);
		int upperBound = Date.upperBound(dates, upperDate);
		
		Assert.assertEquals(lowerBound, expectedLowerBound);
		Assert.assertEquals(upperBound, expectedUpperBound);
		
    }

 
}
