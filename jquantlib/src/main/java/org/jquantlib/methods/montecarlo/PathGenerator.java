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

import org.jquantlib.math.randomnumbers.trial.SequenceGenerator;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.time.TimeGrid;

/**
 * Generates random paths using a sequence generator
 * <p>
 * Generates random paths with drift(S,t) and variance(S,t) using a gaussian sequence generator
 * 
 *  @category mcarlo
 *
 * @author Richard Gomes
 */
//TEST the generated paths are checked against cached results 
public class PathGenerator<GSG extends SequenceGenerator> { // should be GaussianSequenceGenerator ?

//    private boolean brownianBridge_;
//    private GSG generator_;
//    private /*@NonNegative*/ int dimension_;
//    private TimeGrid timeGrid_;
//    private StochasticProcess1D process_;
//    private Sample<Path> next_;
//    private List</* @Real */ Double> temp_;
//    private BrownianBridge bb_;
//
//
//    
//    
//    public PathGenerator(
//                          final StochasticProcess process,
//                          final /*@Time*/ double  length,
//                          final /*@NonNegative*/ int timeSteps,
//                          final GSG generator,
//                          final boolean brownianBridge) {
//        this.brownianBridge_ = brownianBridge;
//        this.generator_ = generator;
//        this.dimension_ = generator.dimension();
//        this.timeGrid_ = new TimeGrid(length, timeSteps);
//        this.process_ = process;
//        this.next_ = new Sample(new Path(timeGrid), 1.0);
//        this.temp_ = dimension;
//        this.bb_ = new BrownianBridge(this.timeGrid_);
//        
//        if (dimension_!= timeSteps)
//            throw new IllegalArgumentException(
//                    "sequence generator dimensionality (" + dimension_ + ") != timeSteps (" + timeSteps + ")");
//    }
//
//    template <class GSG>
//    PathGenerator<GSG>::PathGenerator(
//                          const boost::shared_ptr<StochasticProcess>& process,
//                          const TimeGrid& timeGrid,
//                          const GSG& generator,
//                          bool brownianBridge)
//    : brownianBridge_(brownianBridge), generator_(generator),
//      dimension_(generator_.dimension()), timeGrid_(timeGrid),
//      process_(boost::dynamic_pointer_cast<StochasticProcess1D>(process)),
//      next_(Path(timeGrid_),1.0), temp_(dimension_), bb_(timeGrid_) {
//        QL_REQUIRE(dimension_==timeGrid_.size()-1,
//                   "sequence generator dimensionality (" << dimension_
//                   << ") != timeSteps (" << timeGrid_.size()-1 << ")");
//    }
//
//    template <class GSG>
//    private const typename PathGenerator<GSG>::sample_type&
//    PathGenerator<GSG>::next() const {
//        return next(false);
//    }
//
//    template <class GSG>
//    const typename PathGenerator<GSG>::sample_type&
//    PathGenerator<GSG>::antithetic() const {
//        return next(true);
//    }
//
//    template <class GSG>
//    const typename PathGenerator<GSG>::sample_type&
//    PathGenerator<GSG>::next(bool antithetic) const {
//
//        typedef typename GSG::sample_type sequence_type;
//        const sequence_type& sequence_ =
//            antithetic ? generator_.lastSequence()
//                       : generator_.nextSequence();
//
//        if (brownianBridge_) {
//            bb_.transform(sequence_.value.begin(),
//                          sequence_.value.end(),
//                          temp_.begin());
//        } else {
//            std::copy(sequence_.value.begin(),
//                      sequence_.value.end(),
//                      temp_.begin());
//        }
//
//        next_.weight = sequence_.weight;
//
//        Path& path = next_.value;
//        path.front() = process_->x0();
//
//        for (Size i=1; i<path.length(); i++) {
//            Time t = timeGrid_[i-1];
//            Time dt = timeGrid_.dt(i-1);
//            path[i] = process_->evolve(t, path[i-1], dt,
//                                       antithetic ? -temp_[i-1] :
//                                                     temp_[i-1]);
//        }
//
//        return next_;
//    }




}
