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

import org.jquantlib.math.interpolations.BicubicSplineInterpolation;
import org.jquantlib.math.interpolations.Interpolation2D;
import org.jquantlib.math.interpolations.Interpolator2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BicubicSpline implements Interpolator2D {

    private final static Logger logger = LoggerFactory.getLogger(BicubicSpline.class);

    //
    // private fields
    //
    
    private Interpolator2D delegate;
    
    
    //
    // public constructors
    //
    
    public BicubicSpline() {
        delegate = BicubicSplineInterpolation.getInterpolator();
    }
    
    
    //
    // implements Interpolator2D
    //
    
    @Override
    public Interpolation2D interpolate(final double[] x, final double[] y, final double[][] z) {
        return delegate.interpolate(x, y, z);
    }

}
