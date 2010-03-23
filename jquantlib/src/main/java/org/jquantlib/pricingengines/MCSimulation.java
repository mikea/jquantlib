/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2007 StatPro Italia srl

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

package org.jquantlib.pricingengines;

import org.jquantlib.math.randomnumbers.RandomNumberGenerator;
import org.jquantlib.math.statistics.Statistics;
import org.jquantlib.methods.montecarlo.Variate;
//import org.jquantlib.methods.montecarlo.MonteCarloModel;

/**
 *
 * Base class for Monte Carlo engines
 * <p>
 * Eventually this class might offer greeks methods. Deriving a class from McSimulation gives an easy way to write a Monte Carlo
 * engine.
 *
 * @ee McVanillaEngine
 *
 * @author Richard Gomes
 * @author Selene Makarios
 */

/*

public abstract class McSimulation<MC extends Variate, RNG extends RandomNumberGenerator, S extends Statistics> {
    //typedef typename MonteCarloModel<MC,RNG,S>.path_generator_type
    //    path_generator_type;
    //typedef typename MonteCarloModel<MC,RNG,S>.path_pricer_type
    //    path_pricer_type;
    //typedef typename MonteCarloModel<MC,RNG,S>.stats_type
    //    stats_type;
    //typedef typename MonteCarloModel<MC,RNG,S>.result_type result_type;

    protected McSimulation(boolean antitheticVariate, boolean controlVariate) {
     antitheticVariate_ = (antitheticVariate);
      controlVariate_ = (controlVariate);
    }
    
    protected abstract MonteCarloModel<MC,RNG,S>.path_pricer_type pathPricer();
    protected abstract MonteCarloModel<MC,RNG,S>.path_generator_type pathGenerator();
    protected abstract TimeGrid timeGrid();
    protected MonteCarloModel<MC,RNG,S>.path_pricer_type controlPathPricer() {
        return new MonteCarloModel<MC,RNG,S>.path_pricer_type();
    }
    protected MonteCarloModel<MC,RNG,S>.path_generator_type controlPathGenerator() {
        return new MonteCarloModel<MC,RNG,S>.path_generator_type();
    }
    protected PricingEngine controlPricingEngine() {
        return new PricingEngine();
    }
    protected MonteCarloModel<MC,RNG,S>.result_type controlVariateValue() {
        return null;
    }
    protected static <Sequence> double maxError(Sequence sequence) {
        return *std.max_element(sequence.begin(), sequence.end());
    }
    protected static double maxError(double error) {
        return error;
    }
    
    //! add samples until the required absolute tolerance is reached
    public MonteCarloModel<MC,RNG,S>.result_type  value(double tolerance) {
    	return value(tolerance, QL_MAX_INTEGER, 1023);
    }
    
    public MonteCarloModel<MC,RNG,S>.result_type 
    	value(double tolerance, int maxSamples = QL_MAX_INTEGER, int minSamples = 1023) {
	    int sampleNumber = mcModel_.sampleAccumulator().samples();
	    if (sampleNumber<minSamples) {
	        mcModel_.addSamples(minSamples-sampleNumber);
	        sampleNumber = mcModel_.sampleAccumulator().samples();
	    }
	
	    int nextBatch;
	    double order;
	    MonteCarloModel<MC,RNG,S>.result_type error(mcModel_.sampleAccumulator().errorEstimate());
	    while (maxError(error) > tolerance) {
	        QL_REQUIRE(sampleNumber<maxSamples,
	                   "max number of samples (" << maxSamples
	                   << ") reached, while error (" << error
	                   << ") is still above tolerance (" << tolerance << ")");
	
	        // conservative estimate of how many samples are needed
	        order = maxError(error*error)/tolerance/tolerance;
	        nextBatch =
	            int(std.max<double>(sampleNumber*order*0.8-sampleNumber,
	                                minSamples));
	
	        // do not exceed maxSamples
	        nextBatch = std.min(nextBatch, maxSamples-sampleNumber);
	        sampleNumber += nextBatch;
	        mcModel_.addSamples(nextBatch);
	        error = MonteCarloModel<MC,RNG,S>.result_type(mcModel_.sampleAccumulator().errorEstimate());
	    }
	
	    return MonteCarloModel<MC,RNG,S>.result_type(mcModel_.sampleAccumulator().mean());
	}


    public MonteCarloModel<MC,RNG,S>.result_type valueWithSamples(int samples) {

    int sampleNumber = mcModel_.sampleAccumulator().samples();

    QL_REQUIRE(samples>=sampleNumber,
               "number of already simulated samples (" << sampleNumber
               << ") greater than requested samples (" << samples << ")");

    mcModel_.addSamples(samples-sampleNumber);

    return MonteCarloModel<MC,RNG,S>.result_type(mcModel_.sampleAccumulator().mean());
}


    //! basic calculate method provided to inherited pricing engines
    public void calculate(double requiredTolerance, int requiredSamples, int maxSamples) final {
    QL_REQUIRE(requiredTolerance != Null<double>() ||
               requiredSamples != Null<int>(),
               "neither tolerance nor number of samples set");

    //! Initialize the one-factor Monte Carlo
    if (this.controlVariate_) {

        MonteCarloModel<MC,RNG,S>.result_type controlVariateValue = this.controlVariateValue();
        QL_REQUIRE(controlVariateValue != Null<result_type>(),
                   "engine does not provide "
                   "control-variation price");

        boost.shared_ptr<MonteCarloModel<MC,RNG,S>.path_pricer_type> controlPP =
            this.controlPathPricer();
        QL_REQUIRE(controlPP,
                   "engine does not provide "
                   "control-variation path pricer");

        boost.shared_ptr<MonteCarloModel<MC,RNG,S>.path_generator_type> controlPG = 
            this.controlPathGenerator();

        this.mcModel_ =
            boost.shared_ptr<MonteCarloModel<MC,RNG,S> >(
                new MonteCarloModel<MC,RNG,S>(
                       pathGenerator(), this.pathPricer(), MonteCarloModel<MC,RNG,S>.stats_type(),
                       this.antitheticVariate_, controlPP,
                       controlVariateValue, controlPG));
    } else {
        this.mcModel_ =
            boost.shared_ptr<MonteCarloModel<MC,RNG,S> >(
                new MonteCarloModel<MC,RNG,S>(
                       pathGenerator(), this.pathPricer(), S(),
                       this.antitheticVariate_));
    }

    if (requiredTolerance != Null<double>()) {
        if (maxSamples != Null<int>())
            this.value(requiredTolerance, maxSamples);
        else
            this.value(requiredTolerance);
    } else {
        this.valueWithSamples(requiredSamples);
    }

}

    //! error estimated using the samples simulated so far
    public MonteCarloModel<MC,RNG,S>.result_type errorEstimate() final {
    return mcModel_.sampleAccumulator().errorEstimate();
}

    //! access to the sample accumulator for richer statistics
    public final MonteCarloModel<MC,RNG,S>.stats_type& sampleAccumulator(void) final {
    return mcModel_.sampleAccumulator();
    }

    protected mutable boost.shared_ptr<MonteCarloModel<MC,RNG,S> > mcModel_;
    protected boolean antitheticVariate_, controlVariate_;
}

*/


