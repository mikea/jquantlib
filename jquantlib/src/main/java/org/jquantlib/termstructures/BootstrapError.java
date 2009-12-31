
/*
Copyright (C) 2009 John Martin

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


package org.jquantlib.termstructures;

import org.jquantlib.math.Ops;
import org.jquantlib.termstructures.yieldcurves.BootstrapTraits;
import org.jquantlib.termstructures.Bootstrapable;

public class BootstrapError implements Ops.DoubleOp{

    private Bootstrapable curve;
    
    private BootstrapTraits traits;

    private RateHelper helper;

    private int segment;
    
    // purposeful deviation from quantlib. There is no need to use CurveTraits
    // as a group of static functions and a type definition. We should use
    // an interface and well defined classes
    public BootstrapError (final Bootstrapable c, RateHelper helper, BootstrapTraits traits, int segment)
    {
        this.curve = c;
        this.traits = traits;
        this.segment = segment;
        this.helper = helper;
    }

    public double op (final double guess)
    {
        //TODO: ifndef doxygen...
        traits.updateGuess (curve.getData(), guess, segment);
        curve.getInterpolation().update();
        return helper.quoteError(); 
    }
}