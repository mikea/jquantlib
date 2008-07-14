/*
 Copyright (C) 2007 Richard Gomes
 Copyright (C) 2008 Q. Boiler

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
package org.jquantlib.performance.microscopic;

import org.jquantlib.performance.PerformanceResults;
import org.jquantlib.performance.PerformanceTest;

/**
 *
 * @author Q.Boiler
 */
public class Array implements PerformanceTest {

	@Override
	public PerformanceResults execute() {
		org.jquantlib.math.Array array;
		PerformanceResults pr = new PerformanceResults();
		pr.testName="Array Test";
		pr.units=PerformanceResults.RUNTIME_UNITS.NANOSECONDS;
		pr.runtime=5;

		return pr;
	}

}
