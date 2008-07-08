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

package org.jquantlib.pricingengines.arguments;

import java.util.List;

import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.pricingengines.PricingEngine;

/**
 * Keeps arguments used by {@link PricingEngine}s and necessary for Option valuation
 * 
 * @note Public fields as this class works pretty much as Data Transfer Objects 
 * 
 * @author Richard Gomes
 */
//TODO :: remove std::vector<Time> stoppingTimes
//TODO :: how to handle strike-less option (asian average strike, forward, etc.?
public class OptionArguments extends Arguments {
	
    /**
     * Represents the {@link Payoff} policy to be used
     * 
     * @see Payoff
     */
	public Payoff payoff;
	
	/**
	 * Represent the {@link Exercise} dates
	 * 
	 * @see Exercise
	 */
	public Exercise exercise;
	
	//TODO:Shouldn't be here. It should be moved elsewhere
	public /*@Time*/ List<Double> stoppingTimes;
	
	public OptionArguments() {
		super();
	}
	
	@Override
	public void validate() /*@ReadOnly*/ {
		if (payoff == null) throw new IllegalArgumentException("No payoff given");
	}
	
}
