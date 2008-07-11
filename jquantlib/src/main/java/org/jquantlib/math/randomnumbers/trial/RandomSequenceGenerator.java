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

package org.jquantlib.math.randomnumbers.trial;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.lang.reflect.Constructor;
import java.util.List;

import org.jquantlib.methods.montecarlo.Sample;
import org.jquantlib.util.reflect.TypeReference;


/**
 * Random sequence generator based on a pseudo-random number generator
 * <p>
 * Random sequence generator based on a pseudo-random number generator RNG.
 * 
 * @note do not use with low-discrepancy sequence generator.
 * 
 * @author Richard Gomes
 */
public class RandomSequenceGenerator<RNG extends RandomNumberGenerator> 
        extends TypeReference
        implements UniformSequenceGenerator<Sample<List<Double>>> {
    
    private /*@NonNegative*/ int dimensionality_;
    private RNG rng_;
    private Sample<List<Double>> sequence_; // FIXME :: usage of sample_type :: typedef Sample<std::vector<Real> > sample_type;
    private List<Long> int32Sequence_; // Richard: In spite it's a Int32, it is made of Longs

    
    public RandomSequenceGenerator(final /*@NonNegative*/ int dimensionality, final RNG rng) {
        if (dimensionality<1) throw new IllegalArgumentException("dimensionality must be greater than 0");
        this.dimensionality_ = dimensionality;
        this.rng_ = rng;
        this.sequence_ = new Sample<List<Double>>(new DoubleArrayList(this.dimensionality_), 1.0);
        this.int32Sequence_ = new LongArrayList(this.dimensionality_);
    }
    
    
    public RandomSequenceGenerator(final /*@NonNegative*/ int dimensionality, final long seed) {
        if (dimensionality<1) throw new IllegalArgumentException("dimensionality must be greater than 0");
        this.dimensionality_ = dimensionality;
        this.sequence_ = new Sample<List<Double>>(new DoubleArrayList(this.dimensionality_), 1.0);
        this.int32Sequence_ = new LongArrayList(this.dimensionality_);
        
        try {
            Constructor<RNG> c = (Constructor<RNG>) getGenericParameterClass().getConstructor(long.class);
            this.rng_ = c.newInstance(seed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RandomSequenceGenerator(final /*@NonNegative*/ int dimensionality) {
        this(dimensionality, 0);
    }

    public List<Long> nextInt32Sequence() /* @ReadOnly */ { // FIXME: Aaron:: code review  :: int or long????
        for (int i=0; i<dimensionality_; i++) {
            int32Sequence_.add(rng_.nextInt32());
        }
        return int32Sequence_;
    }

    @Override
    public final Sample<List<Double>> lastSequence() /* @ReadOnly */ {
        return sequence_;
    }
    

    //
    // implements USG
    //
    
    @Override
    public final Sample<List<Double>> nextSequence() /* @ReadOnly */ {
        double weight = 1.0;
        
        DoubleArrayList array = new DoubleArrayList(dimensionality_);
        for (int i = 0; i < dimensionality_; i++) {
            Sample<Double> x = rng_.next(); // FIXME: code review :: not sure it is a Sample<Double>
            array.add(x.value);
            weight *= x.weight;
        }
        sequence_ =  new Sample<List<Double>>(array, weight);
        return sequence_;
    }

    @Override
    public /*@NonNegative*/ int dimension() /* @ReadOnly */ {
        return dimensionality_;
    }
    

}

