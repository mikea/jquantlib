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

package org.jquantlib.pricingengines.results;


// FIXME: add comments
public class MoreGreeks extends ResultsDecorator<Greeks> {

	//
	// Public fields as this class works pretty much as 
	// a Data Transfer Object
	//

	// FIXME: assign JSR-308 annotations
	public double itmCashProbability;
	public double deltaForward;
	public double elasticity;
	public double thetaPerDay;
	public double strikeSensitivity;

	public MoreGreeks() {
		super(new Greeks());
	}

	@Override
	public void reset() {
		super.reset();
		// FIXME: verify Double.NaN
		itmCashProbability = deltaForward = elasticity = thetaPerDay = strikeSensitivity = Double.NaN;
	}

}
