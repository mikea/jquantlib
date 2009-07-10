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

package org.jquantlib.pricingengines.arguments;

import org.jquantlib.instruments.BarrierType;

/**
 * This class defines validation for option arguments
 * 
 * @author <Richard Gomes>
 *
 */
public class BarrierOptionArguments extends OneAssetStrikedOptionArguments {
    
    // TODO: refactor messages
    private static final String UNKNOWN_TYPE = "unknown type";

    //
    // public fields
    //
    
    // FIXME: public fields here is a bad design technique :(
    public BarrierType barrierType;
	public double barrier, rebate;


	//
	// public methods
	//
	
	/**
	 * This method performs additional validation of needed to conform to the barrier type.
	 * The validation is done by comparing the underlying price against the barrier type. 
	 * 
	 * @see org.jquantlib.pricingengines.arguments.OneAssetStrikedOptionArguments#validate()
	 */
	@Override
	public void validate() {
		super.validate();

		// assuming, as always, that the underlying is the first of
		// the state variables...
		double underlying = stochasticProcess.initialValues().first();
		switch (barrierType) {
		case DownIn:
			if (!(underlying >= barrier)) {
				throw new ArithmeticException("underlying (" + underlying
						+ ") < barrier (" + barrier
						+ "): down-and-in barrier undefined");
			}

			break;
		case UpIn:
			if (!(underlying <= barrier)) {
				throw new ArithmeticException("underlying (" + underlying
						+ ") > barrier (" + barrier
						+ "): up-and-in barrier undefined");
			}
			break;
		case DownOut:
			if (!(underlying >= barrier)) {
				throw new ArithmeticException("underlying (" + underlying
						+ ") < barrier (" + barrier
						+ "): down-and-out barrier undefined");
			}

			break;
		case UpOut:
			if (!(underlying <= barrier)) {
				throw new ArithmeticException("underlying (" + underlying
						+ ") > barrier (" + barrier
						+ "): up-and-out barrier undefined");
			}
			break;
		default:
			throw new ArithmeticException(UNKNOWN_TYPE);
		}

	}
}
