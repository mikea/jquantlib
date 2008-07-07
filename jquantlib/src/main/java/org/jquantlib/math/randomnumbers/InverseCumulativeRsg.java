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
 Copyright (C) 2003, 2004 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl

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

import org.jquantlib.methods.montecarlo.Sample;

import cern.colt.list.DoubleArrayList;

/**
 * Inverse cumulative random sequence generator
 * <p>
 * It uses a sequence of uniform deviate in (0, 1) as the source of cumulative distribution values. Then an inverse cumulative
 * distribution is used to calculate the distribution deviate.
 * 
 * The uniform deviate sequence is supplied by USG.
 * 
 * @author Richard Gomes
 */
public class InverseCumulativeRsg<G extends USG, I extends IC> {

    private G uniformSequenceGenerator_;
    private/*@NonNegative*/int dimension_;
    private Sample<DoubleArrayList> x_; // FIXME: usage of sample_type :: typedef Sample<std::vector<Real> > sample_type;
    private I ICD_;

    public InverseCumulativeRsg(final G uniformSequenceGenerator) {
        this.uniformSequenceGenerator_ = uniformSequenceGenerator;
        this.dimension_ = this.uniformSequenceGenerator_.dimension();
        this.x_ = new Sample<DoubleArrayList>(new DoubleArrayList(), 1.0);
        
        // FIXME: ICD_ not initialized!!!! Verify if a static is passed by template
        
    }

    public InverseCumulativeRsg(final G uniformSequenceGenerator, final I inverseCum) {
        this(uniformSequenceGenerator);
        this.ICD_ = inverseCum;
    }

    /**
     * @return next sample from the Gaussian distribution
     */
    public Sample<DoubleArrayList> nextSequence() /* @ReadOnly */{
        Sample<DoubleArrayList> sample = uniformSequenceGenerator_.nextSequence();
        DoubleArrayList sequence = sample.value;

        DoubleArrayList array = new DoubleArrayList(dimension_);
        for (int i = 0; i < dimension_; i++) {
            array.add(ICD_.evaluate(sequence.get(i)));
        }
        return new Sample<DoubleArrayList>(array, sample.weight);
    }

    public final Sample<DoubleArrayList> lastSequence() /* @ReadOnly */{
        return x_;
    }

    public/*@NonNegative*/int getDimension() /* @ReadOnly */{
        return dimension_;
    }

}
