package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.math.Constants;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Date;

/*
Copyright (C) 2008 Richard Gomes

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

/*
Copyright (C) 2005, 2006, 2007 StatPro Italia srl

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

/**
 * Discount-curve traits
 * 
 * @author Richard Gomes
 */
public class Discount implements CurveTraits {

    @Override
    public double initialValue() {
        return 1.0;
    }

    @Override
    public double initialGuess() {
        return 0.9;
    }

    @Override
    public double guess(YieldTermStructure c, Date d) {
        return c.discount(d,true);
    }

    @Override
    public double minValueAfter(int i, Array data) {
        return Constants.QL_EPSILON;
    }

    @Override
    //TODO: solve macros
    public double maxValueAfter(int i, Array data) {
//      #if defined(QL_NEGATIVE_RATES)
      // discount are not required to be decreasing--all bets are off.
      // We choose as max a value very unlikely to be exceeded.
      return 3.0;
//      #else
//      // discounts cannot increase
//      return data[i-1];
//      #endif
    }

    @Override
    public void updateGuess(Array data, double value, int i) {
        data.set(i, value);
    }

}
