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

import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.results.Results;

/**
 * Defines an old-style instrument, which <i>does not</i> make use of a pricing engine.
 * 
 * @see http://quantlib.org/reference/group__instruments.html
 * 
 * @author Richard Gomes
 */
public abstract class OldInstrument extends Instrument {

	private static String SHOULD_NOT_EXTEND_FROM_THIS_CLASS = "Should not extend from this class";
	private static String SHOULD_OVERRIDE_THIS_METHOD = "Should override this method";
	
    /**
     * This method performs the actual calculations and set any needed results.
     * 
     * <p>
     * When an OldInstrument is used, derived classes are obliged to
     * override this method otherwise an Exception is thrown.
     */
	@Override
    protected void performCalculations() {
        throw new UnsupportedOperationException(SHOULD_OVERRIDE_THIS_METHOD);
    }

    
    //
    // final methods
    //
    
    /**
     * This methods is intended to "catch all" calls and immediately throw
     * an exception. It will probably happen when OldInstrument is used as
     * base class when in fact, NewInstrument should be used.
     */
    protected final void setupArguments(final Arguments arguments) {
        throw new UnsupportedOperationException(SHOULD_NOT_EXTEND_FROM_THIS_CLASS);
    }
    
    /**
     * This methods is intended to "catch all" calls and immediately throw
     * an exception. It will probably happen when OldInstrument is used as
     * base class when in fact, NewInstrument should be used.
     */
    protected final void fetchResults(final Results results) /* @ReadOnly */ {
        throw new UnsupportedOperationException(SHOULD_NOT_EXTEND_FROM_THIS_CLASS);
    }
    
    /**
     * This methods is intended to "catch all" calls and immediately throw
     * an exception. It will probably happen when OldInstrument is used as
     * base class when in fact, NewInstrument should be used.
     */
	protected final void setPricingEngine(final PricingEngine engine) {
        throw new UnsupportedOperationException(SHOULD_NOT_EXTEND_FROM_THIS_CLASS);
	}
    
}
