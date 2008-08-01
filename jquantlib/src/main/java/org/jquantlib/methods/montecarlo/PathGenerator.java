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
 Copyright (C) 2002, 2003 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl

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

package org.jquantlib.methods.montecarlo;

import java.util.List;

import org.jquantlib.math.randomnumbers.trial.RandomSequenceGenerator;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.time.TimeGrid;

/**
 * Generates random paths using a sequence generator
 * <p>
 * Generates random paths with drift(S,t) and variance(S,t) using a gaussian sequence generator
 * 
 * @category mcarlo
 *
 * @author Richard Gomes
 */
//TEST the generated paths are checked against cached results 
public class PathGenerator<GSG extends RandomSequenceGenerator> { // should be GaussianSequenceGenerator ?

    private boolean brownianBridge_;
    private GSG generator_;
    private /*@NonNegative*/ int dimension_;
    private TimeGrid timeGrid_;
    private StochasticProcess1D process_;
    private Sample<Path> next_;
    private List</* @Real */ Double> temp_;
    private BrownianBridge bb_;


    
    
    public PathGenerator(
                          final StochasticProcess1D process, // QuantLib/C++ :: StochasticProcess
                          final /*@Time*/ double  length,
                          final /*@NonNegative*/ int timeSteps,
                          final GSG generator,
                          final boolean brownianBridge) {
        this.brownianBridge_ = brownianBridge;
        this.generator_ = generator;
        this.dimension_ = generator.dimension();
        this.timeGrid_ = new TimeGrid(length, timeSteps);
        this.process_ = process;
// TODO: code review
//        this.next_ = new Sample(new Path(timeGrid), 1.0);
//        this.temp_ = dimension;
        this.bb_ = new BrownianBridge(this.timeGrid_);
        
        if (dimension_!= timeSteps)
            throw new IllegalArgumentException(
                    "sequence generator dimensionality (" + dimension_ + ") != timeSteps (" + timeSteps + ")");
    }

    public PathGenerator(
                        final StochasticProcess1D process, // QuantLib/C++ :: StochasticProcess
                        final TimeGrid timeGrid,
                        final GSG generator,
                        final boolean brownianBridge) {
        this.brownianBridge_ = brownianBridge;
        this.generator_ = generator;
        this.dimension_ = generator.dimension();
        this.timeGrid_ = timeGrid;
        this.process_ = process;
// TODO: code review
//        this.next_ = new Sample(new Path(timeGrid), 1.0);
//        this.temp_ = dimension;
        this.bb_ = new BrownianBridge(this.timeGrid_);

        if (dimension_ != timeGrid_.size()-1)
            throw new IllegalArgumentException(
                    "sequence generator dimensionality (" + dimension_ + ") != timeSteps (" + (timeGrid_.size()-1) + ")");
    }

    private final Sample<Path> // typename PathGenerator<GSG>::sample_type&
    next() /* @ReadOnly */ {
        return next(false);
    }

    public final Sample<Path> // typename PathGenerator<GSG>::sample_type&
    antithetic() /* @ReadOnly */ {
        return next(true);
    }

    public final Sample<Path> // typename PathGenerator<GSG>::sample_type&
    next(final boolean antithetic) /* @ReadOnly */ {

        // typedef typename GSG::sample_type sequence_type; = Sample<Path>
        final Sample<Path> sequence_ =
            antithetic ? generator_.lastSequence()
                       : generator_.nextSequence();

// TODO : code review            
//        if (brownianBridge_) {
//            double tmp[] = bb_.transform(sequence_.value);
//            temp_ = new DoubleArrayList(tmp, 0, this.dimension_);
//        } else {
//            DoubleArrays.copy(sequence_.value, 0, this.dimension_);
//        }
//
//        next_.weight = sequence_.weight;

        Path path = next_.value;
        path.frontReference().setValue(process_.x0());

        for (int i=1; i<path.length(); i++) {
            /*@Time*/ double  t = timeGrid_.get(i-1);
            /*@Time*/ double  dt = timeGrid_.dt(i-1);
            double d = process_.evolve(t, path.get(i-1), dt, antithetic ? -temp_.get(i-1) : temp_.get(i-1));
            path.getReference(i).setValue(d);
        }

        return next_;
    }




}
