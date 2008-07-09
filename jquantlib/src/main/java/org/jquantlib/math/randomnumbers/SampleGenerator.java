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

import org.jquantlib.math.randomnumbers.UniformPseudorandomIntGenerator;
import org.jquantlib.math.randomnumbers.SeedableWithInts;
import org.jquantlib.methods.montecarlo.Sample;


/**
 *
 * @author Aaron Roth
 */
public abstract class SampleGenerator<SampleType> implements SeedableWithInts {
    protected final UniformPseudorandomIntGenerator uprig;
    
    // By default, use the SIMD-oriented Fast Mersenne Twister uniform
    // random number (that is, int) generator. And use the default seeding
    // of this RNG as well...
    public SampleGenerator() {
        this.uprig = new SFMTUniformRng();
    }
    
    public SampleGenerator(final UniformPseudorandomIntGenerator uprig) {
        this.uprig = uprig;
    }
    
    public abstract Sample<SampleType> next();
    
    @Override
    public void seed(int... seeds) {
        uprig.seed(seeds);
    }
}
