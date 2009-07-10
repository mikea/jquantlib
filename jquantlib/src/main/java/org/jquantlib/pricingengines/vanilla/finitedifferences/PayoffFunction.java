/*
 Copyright (C) 2007 Srinivas Hasti

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
package org.jquantlib.pricingengines.vanilla.finitedifferences;

import org.jquantlib.instruments.Payoff;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author Srinivas Hasti
 * 
 */
public class PayoffFunction implements UnaryFunctionDouble {
	private Payoff payOff;

	public PayoffFunction(Payoff payOff) {
		super();
		this.payOff = payOff;
	}

	
	//
	// implements UnaryFunctionDouble
	//
	
	@Override
	public double evaluate(double a) {
		return payOff.valueOf(a);
	}

}
