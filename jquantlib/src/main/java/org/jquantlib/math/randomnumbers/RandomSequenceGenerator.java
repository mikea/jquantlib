/*
 Copyright (C) 2007 Richard Gomes

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

package org.jquantlib.math.randomnumbers;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;

import org.jquantlib.methods.montecarlo.Sample;
import org.jquantlib.util.reflect.TypeToken;

/**
 * Random sequence generator based on a pseudo-random number generator
 * 
 * @note Do not use with low-discrepancy sequence generator.
 * 
 * @param <RNG> is a subclass of {@link RandomNumberGenerator}
 * 
 * @author Richard Gomes
 */
// FIXME: code review :: possibly rename this class ???
public class RandomSequenceGenerator<RNG extends RandomNumberGenerator> implements RandomSequenceGeneratorIntf {

    //
    // private fields
    //

    private final /*@NonNegative*/ int  dimension;
    private final RNG                   rng;
    private final double[]              sequence;
    private final long[]                int32Sequence;


    //
    // public constructors
    //

    public RandomSequenceGenerator(final /*@NonNegative*/ int dimensionality, final RNG rng) {
        if (dimensionality < 1)
            throw new IllegalArgumentException("dimensionality must be greater than 0");
        this.dimension = dimensionality;
        this.rng = rng;
        this.sequence = new double[this.dimension];
        this.int32Sequence = new long[this.dimension];
    }

    public RandomSequenceGenerator(final /*@NonNegative*/ int dimensionality) {
        this(dimensionality, 0);
    }

    public RandomSequenceGenerator(final /*@NonNegative*/ int dimensionality, final long seed) {
        if (dimensionality < 1)
            throw new IllegalArgumentException("dimensionality must be greater than 0");
        this.dimension = dimensionality;
        this.sequence = new double[this.dimension];
        this.int32Sequence = new long[this.dimension];

        // instantiate a generic RandomNumberGenerator
        try {
            this.rng = (RNG) TypeToken.getClazz(this.getClass()).getConstructor(long.class).newInstance(seed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //
    // implements RandomSequenceGeneratorIntf
    //

    @Override
    public final Sample<DoubleList> lastSequence() /* @ReadOnly */{
        return new Sample<DoubleList>(new DoubleArrayList(sequence), 1.0);
    }

    @Override
    public final Sample<DoubleList> nextSequence() /* @ReadOnly */{
        double weight = 1.0;
        for (int i = 0; i < this.dimension; i++) {
            Sample<Double> sample = this.rng.next();
            this.sequence[i] = sample.getValue();
            weight *= sample.getWeight();
        }
        return new Sample<DoubleList>(new DoubleArrayList(sequence), weight);
    }

    @Override
    public long[] nextInt32Sequence() /* @ReadOnly */{
        for (int i = 0; i < this.dimension; i++) {
            this.int32Sequence[i] = this.rng.nextInt32();
        }
        return this.int32Sequence;
    }

    @Override
    public /*@NonNegative*/ int dimension() /* @ReadOnly */{
        return this.dimension;
    }

}
