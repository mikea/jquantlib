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

package org.jquantlib.instruments;

import org.jquantlib.exercise.Exercise;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.pricingengines.results.MoreGreeks;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.processes.StochasticProcess;

/**
 * Base class for options on a single asset with striked payoff
 * 
 * @author Richard Gomes
 */
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
     * This method accesses directly fields from base class {@link OneAssetOption.Results}.
     * <p>
     * These fields are exposed by {@link Instrument.InstrumentResults} which is the base class of {@link OneAssetOption.Results}.
     * This programming style is not recommended and we should use getters/setters instead.
     * At the moment, we keep the original implementation.
	 */
    // TODO: code review
    @Override    
	public void fetchResults(final Results results) /* @ReadOnly */ {
        // obtain results from chained results
        super.fetchResults(results);

		final MoreGreeks moreGreeks;

    	if (MoreGreeks.class.isAssignableFrom(results.getClass())) {
        	moreGreeks = (MoreGreeks) results;
    	} else {
    		throw new ClassCastException(results.getClass().getName());
    	}
    	
		strikeSensitivity = moreGreeks.strikeSensitivity;
	}
        
}
