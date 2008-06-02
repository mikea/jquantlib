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
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.pricingengines.results.MoreGreeks;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.processes.StochasticProcess;

public class OneAssetStrikedOption extends OneAssetOption {

    // results
    private /* @Price */ double strikeSensitivity;

// FIXME: code review
//    public OneAssetStrikedOption(
//            final StochasticProcess process,
//            final Payoff payoff,
//            final Exercise exercise) {
//    	this(process, payoff, exercise, new PricingEngine());
//    }

    public OneAssetStrikedOption(
            final StochasticProcess process,
            final Payoff payoff,
            final Exercise exercise,
            final PricingEngine engine) {
    	super(process, payoff, exercise, engine);
    }
    
    public /* @Price */ double getStrikeSensitivity() /* @ReadOnly */ {
        calculate();
        if (Double.isNaN(strikeSensitivity)) throw new ArithmeticException("strike sensitivity not provided");
        return strikeSensitivity;
    }
        
    @Override
	protected void setupExpired() /* @ReadOnly */ {
		super.setupExpired();
		strikeSensitivity = 0.0;
	}
	
    @Override    
    public void setupArguments(final Arguments args) /* @ReadOnly */ {
		super.setupArguments(args);
		final OneAssetOptionArguments moreArgs = (OneAssetOptionArguments)args;
        moreArgs.payoff = payoff;
	}

	/**
     * @note This method accesses directly fields from base class {@link OneAssetOption.Results}.
     * These fields are exposed by {@link Instrument.InstrumentResults} which is the base class of {@link OneAssetOption.Results}.
     * This programming style is not recommended and we should use getters/setters instead.
     * At the moment, we keep the original implementation.
     * 
     * @author Richard Gomes
	 */
    @Override    
	public void fetchResults(final Results results) /* @ReadOnly */ {
		final MoreGreeks moreGreeks;

		super.fetchResults(results);

    	if (MoreGreeks.class.isAssignableFrom(results.getClass())) {
        	moreGreeks = (MoreGreeks) results;
    	} else {
    		throw new ClassCastException(results.getClass().getName());
    	}
    	
		strikeSensitivity = moreGreeks.strikeSensitivity;
	}
        
}
