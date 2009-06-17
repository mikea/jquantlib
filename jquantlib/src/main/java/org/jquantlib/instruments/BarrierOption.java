/*
 Copyright (C) 2008 Richard Gomes

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
 Copyright (C) 2003, 2004 Neil Firth
 Copyright (C) 2003, 2004 Ferdinando Ametrano
 Copyright (C) 2003, 2004, 2007 StatPro Italia srl

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
import org.jquantlib.pricingengines.arguments.BarrierOptionArguments;
import org.jquantlib.pricingengines.barrier.AnalyticBarrierEngine;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;

/**
 * Barrier option on a single asset.
 * <p>
 * The analytic pricing engine will be used if none if passed.
 * 
 * @author <Richard Gomes>
 *
 */
public class BarrierOption extends OneAssetStrikedOption {
    
    private static final String WRONG_ARGUMENT_TYPE = "wrong argument type";

    //
    // protected fields
    //
    
    protected BarrierType barrierType;
    protected double barrier;
    protected double rebate;

	
    //
    // public constructors
    //
    
    public BarrierOption(final StochasticProcess process, final Payoff payoff,
			final Exercise exercise, final PricingEngine engine) {
		
		super(process, payoff, exercise, engine);
		if (engine == null) {
			setPricingEngine(new AnalyticBarrierEngine());
		}
	}
	
    public BarrierOption(final BarrierType barrierType,
            			final double barrier,
            			final double rebate,
            			final StochasticProcess process,
            			final StrikedTypePayoff payoff,
            			final Exercise exercise,
            			final PricingEngine engine){
	   
    	super(process,payoff, exercise, engine);
    	if (engine == null){
            setPricingEngine(new AnalyticBarrierEngine());
        }
    	
    	this.barrierType = barrierType;
    	this.barrier = barrier;
    	this.rebate = rebate;
    }

    
    //
    // overrides OneAssetStrikedOption
    //
    
    @Override
    public void setupArguments(final Arguments args) {

        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (!(args instanceof BarrierOptionArguments)) {
            throw new ArithmeticException(WRONG_ARGUMENT_TYPE);
        }

        final BarrierOptionArguments moreArgs = (BarrierOptionArguments) args;
        moreArgs.barrierType = barrierType;
        moreArgs.barrier = barrier;
        moreArgs.rebate = rebate;

        super.setupArguments(args);
    }

}
