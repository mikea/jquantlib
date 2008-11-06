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

package org.jquantlib.math.interpolations;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BicubicSplineInterpolation extends AbstractInterpolation2D {

    private final static Logger logger = LoggerFactory.getLogger(BicubicSplineInterpolation.class);

    
    //
    // private fields
    //
    
    private final List<Interpolation> splines;
    
    
    
    //
    // private constructors
    //
    
    private BicubicSplineInterpolation() {
        splines = new ObjectArrayList<Interpolation>();
    }


    //
    // overrides AbstractInterpolation2D
    //
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void calculate() {
// TODO : [Richard needs to complete implementation]        
//        // splines.reserve(this.mz.length); // TODO: verify what .length returns
//        for (int i=0; i<(this.mz.length); i++) {
//            splines.add(NaturalCubicSpline(this.vx, this.vy, this.mz.row_begin(i))); // TODO: row_begin ???
//        }
    }


    @Override
    public double evaluateImpl(double x, double y) /* @ReadOnly */{
        final double[] section = new double[splines.size()];
        for (int i=0; i<splines.size(); i++) {
            section[i]=splines.get(i).evaluate(x, true);
        }

// TODO : [Richard needs to complete implementation]        
//        NaturalCubicSpline spline = new Spline(this->yBegin_, this->yEnd_, section.begin());
//        return spline.evaluate(y,true);
        
        return 1.0; // fake!!
    }

    
    //
    // static methods
    //
    
    static public Interpolator2D getInterpolator() {
        BicubicSplineInterpolation bicubicSplineInterpolation = new BicubicSplineInterpolation();
        return bicubicSplineInterpolation. new BicubicSplineInterpolationImpl(bicubicSplineInterpolation);
    }

    
    //
    // inner classes
    //
    
    /**
     * This class is a factory for BicubicSplineInterpolation instances.
     * 
     * @author Richard Gomes
     */
    private class BicubicSplineInterpolationImpl implements Interpolator2D {
        private BicubicSplineInterpolation delegate;
        
        public BicubicSplineInterpolationImpl(final BicubicSplineInterpolation delegate) {
            this.delegate = delegate;
        }

        public Interpolation2D interpolate(final double[] x, final double[] y, final double[][] z) {
            delegate.vx = x;
            delegate.vy = y;
            delegate.mz = z;
            delegate.reload();
            return delegate;
        }

    }

}

