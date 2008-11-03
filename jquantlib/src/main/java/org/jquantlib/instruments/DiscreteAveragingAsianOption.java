/*
 Copyright (C) 2007 Gary Kennedy

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
 Copyright (C) 2004, 2007 StatPro Italia srl

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

/*! \file asianoption.hpp
    \brief Asian option on a single asset
*/



package org.jquantlib.instruments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jquantlib.exercise.Exercise;
import org.jquantlib.pricingengines.GenericEngine;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.DiscreteAveragingAsianOptionArguments;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.pricingengines.results.OneAssetOptionResults;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.util.Date;

/**
 * Description of the terms and conditions of a discrete average out fixed strike
 * option.
 * 
 * <p>
 * Ported from 
 * <ul>
 * <li>ql/instruments/asianoption.hpp</li>
 * <li>ql/instruments/asianoption.cpp</li>
 * </ul>
 * @author gary_kennedy
 *
 */
public class DiscreteAveragingAsianOption extends OneAssetStrikedOption{

	public DiscreteAveragingAsianOption(StochasticProcess process,
			Payoff payoff, Exercise exercise, PricingEngine engine) {
		super(process, payoff, exercise, engine);
	}
	
    public DiscreteAveragingAsianOption(
            AverageType averageType,
            /*@Real*/ double runningAccumulator,
            /*@Size*/ int pastFixings,
            List<Date> fixingDates,
            final StochasticProcess process,
            final StrikedTypePayoff payoff,
            final Exercise exercise,
            final PricingEngine engine){
    	super(process,payoff,exercise,engine);
    	this.averageType_ = averageType;
    	this.runningAccumulator_ = runningAccumulator;
    	this.fixingDates_ = new ArrayList<Date>(fixingDates);
    	Collections.sort(this.fixingDates_);
    }
    
    @Override    
    public void setupArguments(final Arguments args) /* @ReadOnly */ {
        super.setupArguments(args);

        if (!(args instanceof DiscreteAveragingAsianOptionArguments)){
        	throw new IllegalArgumentException("wrong argument type");
        }
        DiscreteAveragingAsianOptionArguments moreArgs = (DiscreteAveragingAsianOptionArguments)args;
        moreArgs.averageType = averageType_;
        moreArgs.runningAccumulator = runningAccumulator_;
        moreArgs.pastFixings = pastFixings_;
        moreArgs.fixingDates = fixingDates_;

    }
    
    protected AverageType averageType_;
    protected /*@Real*/ double runningAccumulator_;
    protected /*@Size*/ int pastFixings_;
    protected List<Date> fixingDates_;


    
}
