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
import org.jquantlib.pricingengines.PricingEngineArguments;

// FIXME: TO BE DONE
public class Option extends Instrument {

	protected Payoff payoff_;
	protected Exercise exercise_;
//	protected OptionArguments arguments;

	public Option(final Payoff payoff, final Exercise exercise, final PricingEngine engine) {
		this.payoff_ = payoff;
		this.exercise_ = exercise;
		if (engine!=null) super.setPricingEngine(engine);
	}

	public enum Type {
    	Put  (-1),
        Call (1);
    	
    	private int value;
    	
    	private Type(final int type) {
    		this.value = type;
    	}
    	
    	public int toInteger() {
    		return value;
    	}
    }
	
	/**
	 * Basic Option arguments
	 * 
	 * @todo code review
	 */
	// TODO how to handle strike-less option (asian average strike, forward, etc.)?
	// FIXME: code review
	private class OptionArguments implements PricingEngineArguments {
		public /*@Time*/ double[] stoppingTimes; // FIXME: it should be moved elsewhere
		public Payoff payoff;
		public Exercise exercise;

//		public List<Real>getArguments() {}
            
		public final void validate() {
			if (payoff_==null) throw new IllegalArgumentException("no payoff given");
		}
            
	}

          
}
