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

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl

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

package org.jquantlib.pricingengines.results;

import org.jquantlib.instruments.Instrument;
import org.jquantlib.instruments.NewInstrument;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;


/**
 * Results are used by {@link PricingEngine}s in order to store results of calculations 
 * relative to <i>new-style</i> {@link Instrument}s
 *
 * @note Public fields as this class works pretty much as Data Transfer Objects
 * 
 * @see Instrument
 * @see NewInstrument
 * @see PricingEngine
 * @see Arguments
 * 
 * @author Richard Gomes
 */
public class Results {

	//
    // public fields
    //
    
    /**
	 * Represents the calculated value of an {@link Instrument}
	 * 
	 * @see Instrument
	 * @see NewInstrument
	 */
    public /*@Price*/ double value;
    
    /**
     * Contains the estimated error due to floating point error
     */
	public /*@Real*/ double errorEstimate;


	//
	// public methods
	//
	
	/**
	 * Clean up results of calculations
	 * <p>
	 * Notice that values are <b>undefined</b> after reset.
	 */
	public void reset() {
		value = errorEstimate = Double.NaN;
	}

}
