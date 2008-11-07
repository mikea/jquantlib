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
 Copyright (C) 2003 Ferdinando Ametrano
 Copyright (C) 2004 StatPro Italia srl

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

package org.jquantlib.math.interpolations.factories;

import org.jquantlib.math.interpolations.CubicSpline;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cubic-spline interpolation factory and traits
 *
 * @author Richard Gomes
 */
//FIXME :: work in progress [RICHARD]
//TEST : needs code review and test classes
public class Cubic implements Interpolator {
    
    private final static Logger logger = LoggerFactory.getLogger(Cubic.class);

    //
    // private final fields
    //
    
    private final Interpolator delegate;
    
    
    //
    // public constructors
    //

    public Cubic() {
        this(CubicSpline.BoundaryCondition.SecondDerivative, 0.0, CubicSpline.BoundaryCondition.SecondDerivative, 0.0, false);
    }

    
    public Cubic(
            final CubicSpline.BoundaryCondition leftCondition,
            final double leftConditionValue,
            final CubicSpline.BoundaryCondition rightCondition,
            final double rightConditionValue,
            final boolean monotonicityConstraint) {
        delegate = CubicSpline.getInterpolator(
                leftCondition, leftConditionValue, rightCondition, rightConditionValue, monotonicityConstraint);
    }
    
    
    //
    // implements Interpolator
    //
    
    @Override
    public final Interpolation interpolate(final int size, final double[] x, final double[] y) /* @ReadOnly */ {
        return delegate.interpolate(x, y);
    }

    @Override
    public final Interpolation interpolate(final double[] x, final double[] y) /* @ReadOnly */ {
        return delegate.interpolate(x, y);
    }

    @Override
    public final boolean isGlobal() /* @ReadOnly */ {
        return delegate.isGlobal();
    }
    
}

