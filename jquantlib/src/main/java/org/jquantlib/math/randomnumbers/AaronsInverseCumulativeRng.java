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

import org.jquantlib.math.randomnumbers.UniformRng_0_1;
import org.jquantlib.math.randomnumbers.IC;
import org.jquantlib.methods.montecarlo.Sample;

/**
 *
 * @author Aaron Roth
 */
public class AaronsInverseCumulativeRng<SampleType> extends SampleGenerator<SampleType> {
    private final UniformRng_0_1 uniformRng;
    private final IC inverseCdf;
    
    public AaronsInverseCumulativeRng(final UniformRng_0_1 uniformRng, final IC inverseCdf) {
        this.uniformRng = uniformRng;
        this.inverseCdf = inverseCdf; // IC should take a type parameter of type SampleType
    }

    /**
     * @return a sample from a Gaussian distribution
     * Huh? why is the distribution necessarily Gaussian?
     */
    @Override
    public Sample<SampleType> next() /* @ReadOnly */ {
        Sample<Double> sample = uniformRng.next(); // FIXME: usage of sample_type :: typedef Sample<Real> sample_type;
        
        // Well, there's really no point in parameterizing this whole class with a SampleType,
        // since UnaryFunctionDouble#evaluate only ever returns a double, forcing SampleType
        // to also always be a double. Probably UnaryFunctionDouble should be rewritten as
        // a parameterized class UnaryFunction<T>.
        return new Sample(inverseCdf.evaluate(sample.value), sample.weight);
    }
}
