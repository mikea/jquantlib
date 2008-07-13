/*
 Copyright (C) 2008 Aaron Roth

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

package org.jquantlib.math.randomnumbers;

import org.jquantlib.math.randomnumbers.UniformRng;
import org.jquantlib.math.randomnumbers.InverseCdf;
import org.jquantlib.methods.montecarlo.Sample;

/**
 *
 * @author Aaron Roth
 */
public class AaronsInverseCumulativeRng<UniformRngNumberType, SampleValueType> extends SampleGenerator<UniformRngNumberType, SampleValueType>  {
    private final InverseCdf<UniformRngNumberType, SampleValueType> inverseCdf;

    public AaronsInverseCumulativeRng( final UniformRng<UniformRngNumberType> uniformRng
                                     , final InverseCdf<UniformRngNumberType, SampleValueType> inverseCdf)
    {
        super(uniformRng);
        this.inverseCdf = inverseCdf;
    }

    /**
     * @return a sample from a Gaussian distribution
     * Huh? why is the distribution necessarily Gaussian?
     */
    @Override
    public Sample<SampleValueType> next() /* @ReadOnly */ {
        return new Sample(inverseCdf.evaluate(uniformRng.next()), 1.0);
    }
}
