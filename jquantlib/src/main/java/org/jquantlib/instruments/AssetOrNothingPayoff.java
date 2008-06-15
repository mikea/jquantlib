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

package org.jquantlib.instruments;

import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * Binary <i>asset-or-nothing</i> payoff which pays off nothing if the underlying asset price {@latex$ S_{T}} finishes
 * below/above the strike price {@latex$ K}, or pays out the asset price {@latex$ S_{T}} itself if the underlying asset finishes
 * above/below the strike price.
 * <p> 
 * Definitions of Binary path-independent payoffs can be found in 
 * <i>M. Rubinstein, E. Reiner:"Unscrambling The Binary Code", Risk, Vol.4 no.9,1991</i>.
 * 
 * @see <a href="http://www.in-the-money.com/artandpap/Binary%20Options.doc">Binary Options</a>
 * 
 * @author Richard Gomes
 */
public class AssetOrNothingPayoff extends StrikedTypePayoff {

	//
    // public constructors
    //
    
    /**
     * Constructs a typed {@link Payoff} with a fixed strike price and the policy of an <i>asset-or-nothing</i> payoff
     * 
     * @param type is an {@link Option.Type}
     * @param strike is the strike price
     */
    public AssetOrNothingPayoff(final Option.Type type, final/* @Price */double strike) {
		super(type, strike);
	}

	//
    // public final methods
    //
    
    /**
     * {@inheritDoc}
     * <p>
     * Pays off nothing if the underlying asset price {@latex$ S_{T}} finishes below/above the strike price {@latex$ K}, or pays
     * out the asset price {@latex$ S_{T}} itself if the underlying asset finishes above/below the strike price.
     * <li>CALL Option: if {@latex$ S_{T}>K \rightarrow S_{T}}, otherwise zero</li>
     * <li>PUT Option: if {@latex$ K>S_{T} \rightarrow S_{T}}, otherwise zero</li>
     * where {@latex$ S_{T}} is the asset price at maturity
     */
    @Override
    public final /* @Price */double valueOf(final/* @Price */double assetPrice) {
		if (type == Option.Type.CALL) {
			return (assetPrice - strike > 0.0) ? assetPrice : 0.0;
		} else if (type == Option.Type.PUT) {
			return (strike - assetPrice > 0.0) ? assetPrice : 0.0;
		} else {
			throw new IllegalArgumentException("unknown/illegal option type");
		}
	}


	//
	// implements TypedVisitable
	//
	
	@Override
	public void accept(final TypedVisitor<Payoff> v) {
		final Visitor<Payoff> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
		if (v1 != null) {
			v1.visit(this);
		} else {
			super.accept(v);
		}
	}

}
