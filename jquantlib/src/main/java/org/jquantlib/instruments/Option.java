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

import cern.colt.list.DoubleArrayList;

public abstract class Option extends Instrument {

	protected Payoff payoff_;
	protected Exercise exercise_;

	public Option(final Payoff payoff, final Exercise exercise, final PricingEngine engine) {
		this.payoff_ = payoff;
		this.exercise_ = exercise;
		if (engine != null)
			super.setPricingEngine(engine);
	}

	
	public enum Type {
		Put(-1), Call(1);

		private int value;

		private Type(final int type) {
			this.value = type;
		}

		public int toInteger() {
			return value;
		}
	}

	
	// protected OptionArguments arguments;

	/**
	 * @todo remove std::vector<Time> stoppingTimes
	 * @todo how to handle strike-less option (asian average strike, forward, etc.)?
	 *
     * @note This inner class exposes fields instead of offering getters/setters.
     * This procedure is not recommended and we should use getters/setters instead.
     * At the moment, we keep the original implementation.
     * 
	 * @author Richard Gomes
	 */
	protected class Arguments implements PricingEngine.Arguments {
		protected Payoff payoff;
		protected Exercise exercise;
		protected /*@Time*/ DoubleArrayList stoppingTimes;
		
		public void validate() {
			if (payoff_ == null)
				throw new IllegalArgumentException("no payoff given");
		}
	}

	
	/**
     * @note This inner class exposes fields instead of offering getters/setters.
     * This procedure is not recommended and we should use getters/setters instead.
     * At the moment, we keep the original implementation.
     * 
     * @author Richard Gomes
	 */
	protected class Greeks implements PricingEngine.Results {
		
		public void reset() {
			delta = gamma = theta = vega = rho = dividendRho = Double.NaN;
		}

		protected double delta;
		protected double gamma;
		protected double theta;
		protected double vega;
		protected double rho;
		protected double dividendRho;
	}
	

	/**
     * @note This inner class exposes fields instead of offering getters/setters.
     * This procedure is not recommended and we should use getters/setters instead.
     * At the moment, we keep the original implementation.
     * 
     * @author Richard Gomes
	 */
	protected class MoreGreeks implements PricingEngine.Results {
		
		public void reset() {
			itmCashProbability = deltaForward = elasticity = thetaPerDay = strikeSensitivity = Double.NaN;
		}

		protected double itmCashProbability;
		protected double deltaForward;
		protected double elasticity;
		protected double thetaPerDay;
		protected double strikeSensitivity;
	}

}
