/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

package org.jquantlib.instruments;

import org.jquantlib.exercise.Exercise;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.processes.StochasticProcess;

public class OneAssetStrikedOption extends OneAssetOption {

    // results
    protected /* @Price */ double strikeSensitivity_;

    public OneAssetStrikedOption(
            final StochasticProcess process,
            final StrikedTypePayoff payoff,
            final Exercise exercise) {
    	this(process, payoff, exercise, new PricingEngine());
    }

    public OneAssetStrikedOption(
            final StochasticProcess process,
            final StrikedTypePayoff payoff,
            final Exercise exercise,
            final PricingEngine engine) {
    	super(process, payoff, exercise, engine);
    }
    
    
    /* @Price */ double getStrikeSensitivity() /* @ReadOnly */ {
        calculate();
        if (strikeSensitivity_ == Double.NaN) throw new ArithmeticException("strike sensitivity not provided");
        return strikeSensitivity_;
    }
        
        
    public void setupArguments(final PricingEngine.Arguments args) /* @ReadOnly */ {
		super.setupArguments(args);
        Arguments moreArgs = (OneAssetOption.Arguments)args;
        moreArgs.payoff = payoff_;
	}

	protected void setupExpired() /* @ReadOnly */ {
		super.setupExpired();
		strikeSensitivity_ = 0.0;
	}
	
	/**
     * @note This method accesses directly fields from base class {@link OneAssetOption.Results}.
     * These fields are exposed by {@link Instrument.InstrumentResults} which is the base class of {@link OneAssetOption.Results}.
     * This programming style is not recommended and we should use getters/setters instead.
     * At the moment, we keep the original implementation.
     * 
     * @author Richard Gomes
	 */
	public void fetchResults(final PricingEngine.Results results) /* @ReadOnly */ {
		super.fetchResults(results);
        final MoreGreeks moreGreeks = ((OneAssetOption.Results)results).delegateMoreGreeks;
		strikeSensitivity_ = moreGreeks.strikeSensitivity;
	}
        
        
	//
	// Inner class OneAssetStrikedOption.Engine
	//
	
	protected abstract class OneAssetStrikedOptionEngine extends OneAssetOption.OneAssetOptionEngine {
		
	}
        
	
}
