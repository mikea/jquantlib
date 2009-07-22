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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.math.Array;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.termstructures.IYieldTermStructure;
import org.jquantlib.util.Date;
import org.jquantlib.util.Pair;

/**
 * 
 * @author Richard Gomes
 */
public interface YieldTraits<I extends Interpolator> extends IYieldTermStructure {

    //
    // common
    //
    public abstract Date maxDate() /* @ReadOnly */;
    public abstract Array times() /* @ReadOnly */;
    public abstract Date[] dates() /* @ReadOnly */;
    public abstract Pair<Date, /* @Rate */Double>[] nodes() /* @ReadOnly */;

    // exclusive to discount curve
    public abstract/* @DiscountFactor */Array discounts() /* @ReadOnly */;

    // exclusive to forward curve
    public abstract/* @Rate */Array forwards() /* @ReadOnly */;

    // exclusive to zero rate
    public abstract/* @Rate */Array zeroRates() /* @ReadOnly */;


    //
    // The following methods should be protected in order to mimick as it is done in C++
    //
    
    // exclusive to discount curve :: SHOULD BE "protected"
    public abstract/* @DiscountFactor */double discountImpl(final/* @Time */double t) /* @ReadOnly */;

    // exclusive to forward curve :: SHOULD BE "protected"
    public abstract/* @Rate */double forwardImpl(final/* @Time */double t) /* @ReadOnly */;

    // common to forward curve and to zero curve :: SHOULD BE "protected"
    public abstract/* @Rate */double zeroYieldImpl(final/* @Time */double t) /* @ReadOnly */;

}
