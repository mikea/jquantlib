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

package org.jquantlib.pricingengines.arguments;

import org.jquantlib.processes.StochasticProcess;

/**
 * Arguments for single-asset option calculation
 * 
 * @note This inner class must be kept <b>private</b> as its fields and
 *       ancertor's fields are exposed. This programming style is not
 *       recommended and we should use getters/setters instead. At the moment,
 *       we keep the original implementation.
 * 
 * @author Richard Gomes
 */
public class OneAssetOptionArguments extends OptionArguments {

    //
    // public constructors
    //
    
    public OneAssetOptionArguments() {
        super();
    }
    
    //
    // public fields
    //
    
    // FIXME: public fields here is a bad design technique :(
	public StochasticProcess stochasticProcess; // FIXME: should use Generics

	
	//
	// public methods
	//
	
	@Override
	public void validate() /*@ReadOnly*/ {
		super.validate();
		// we assume the underlying value to be the first state variable
		if (stochasticProcess.initialValues().first() <= 0.0)
			throw new IllegalArgumentException("Negative or zero underlying given");
	}
	
}
