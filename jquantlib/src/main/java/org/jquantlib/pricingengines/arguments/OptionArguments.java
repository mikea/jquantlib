/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.pricingengines.arguments;

import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.Payoff;

import cern.colt.list.DoubleArrayList;

// FIXME: add comments
public class OptionArguments extends Arguments {
	
	//
	// Public fields as this class works pretty much as 
	// a Data Transfer Object
	//

	// FIXME: assign JSR-308 annotations
	public Payoff payoff;
	public Exercise exercise;
	public /*@Time*/ DoubleArrayList stoppingTimes;
	
	public OptionArguments() {
		super();
	}
	
	@Override
	public void validate() /*@ReadOnly*/ {
		if (payoff == null) throw new IllegalArgumentException("No payoff given");
	}
	
}
