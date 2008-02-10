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

import org.jscience.mathematics.number.Real;


/**
 * Plain-vanilla payoff
 */
public class PlainVanillaPayoff extends StrikedTypePayoff {
	public PlainVanillaPayoff(final Option.Type type, final Real strike) {
		super(type, strike);
	}
	
    public final Real valueOf(final Real price) {
    	if (type_==Option.Type.Call) {
    		return new Real( Math.max(price.doubleValue() - strike.doubleValue(), 0.0) );
    	} else if (type_==Option.Type.Put) {
    		return new Real( Math.max(strike.doubleValue() - price.doubleValue(), 0.0) );
    	} else {
    		throw new IllegalArgumentException("unknown/illegal option type");
    	}
    }

	
// FIXME ElementVisitor    
//    @Override
//	protected void accept(final ElementVisitor<Payoff> v) {
//		if (v != null) {
//			v.visit(this);
//		} else {
//			super.accept(v);
//		}
//	}

}
