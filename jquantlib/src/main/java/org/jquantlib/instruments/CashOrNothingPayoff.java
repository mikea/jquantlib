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

import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * Binary gap payoff
 * 
 * <p>
 * This payoff is equivalent to being a) long a PlainVanillaPayoff at the first
 * strike (same Call/Put type) and b) short a CashOrNothingPayoff at the first
 * strike (same Call/Put type) with cash payoff equal to the difference between
 * the second and the first strike.
 * 
 * @note <b>WARNING:</b> this payoff can be negative depending on the strikes
 */
public class CashOrNothingPayoff extends StrikedTypePayoff {

	protected/* @Payoff */double cashPayoff;

	public CashOrNothingPayoff(final Option.Type type, final/* @Price */double strike) {
		super(type, strike);
	}

	public/* @Payoff */double getCashPayoff() /* @ReadOnly */{
		return cashPayoff;
	}

	public final/* @Price */double valueOf(final/* @Price */double price) {
		if (type == Option.Type.CALL) {
			return (price-strike > 0.0 ? cashPayoff : 0.0);
		} else if (type == Option.Type.PUT) {
			return (strike-price > 0.0 ? cashPayoff : 0.0);
		} else {
			throw new IllegalArgumentException(UNKNOWN_OPTION_TYPE);
		}
	}


	//
	// implements TypedVisitable
	//
	
	@Override
	public void accept(final TypedVisitor<Payoff> v) {
		Visitor<Payoff> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
		if (v1 != null) {
			v1.visit(this);
		} else {
			super.accept(v);
		}
	}

}
