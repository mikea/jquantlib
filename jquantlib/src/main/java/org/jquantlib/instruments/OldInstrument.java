/*
 Copyright (C) 2007 Richard Gomes

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
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006, 2007 StatPro Italia srl

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

package org.jquantlib.instruments;

import org.jquantlib.lang.reflect.ReflectConstants;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.util.LazyObject;

/**
 * Defines an <i>old-style</i> {@link Instrument} which does not make use of an external {@link PricingEngine}.
 * <p>
 * Methods inherited from {@link Instrument} which are related to {@link PricingEngine}s intentionally
 * throw {@link UnsupportedOperationException} and cannot be overridden by extended classes.
 *
 * @see Instrument
 * @see NewInstrument
 * @see PricingEngine
 * @see <a href="http://quantlib.org/reference/group__instruments.html">QuantLib: Financial Instruments</a>
 *
 * @author Richard Gomes
 */
public abstract class OldInstrument extends Instrument {

	//
    // private static final fields
    //

    /**
     * This method performs the actual calculations and set any needed results.
     * <p>
     * When an {@link OldInstrument} is used, derived classes are <i>required</i> to
     * override this method otherwise an {@link UnsupportedOperationException} is thrown.
     *
     * @see LazyObject#performCalculations
     */
	@Override
    protected void performCalculations() {
        throw new IllegalStateException(ReflectConstants.SHOULD_OVERRIDE_THIS_METHOD);
    }


    //
    // protected *final* methods overridden from Instrument
    //

    /**
     * This methods is intended to <i>catch all</i> calls and immediately throw
     * {@link UnsupportedOperationException}. It will probably happen when an {@link OldInstrument} is used as
     * base class when in fact, a {@link NewInstrument} should be used.
     *
     * @see NewInstrument
     * @see Instrument
     * @see PricingEngine
     */
    protected final void setupArguments(final Arguments arguments) {
        throw new IllegalStateException(ReflectConstants.SHOULD_NOT_EXTEND_FROM_THIS_CLASS);
    }

    /**
     * This methods is intended to <i>catch all</i> calls and immediately throw
     * {@link UnsupportedOperationException}. It will probably happen when an {@link OldInstrument} is used as
     * base class when in fact, a {@link NewInstrument} should be used.
     *
     * @see NewInstrument
     * @see Instrument
     * @see PricingEngine
     */
    protected final void fetchResults(final Results results) /* @ReadOnly */ {
        throw new IllegalStateException(ReflectConstants.SHOULD_NOT_EXTEND_FROM_THIS_CLASS);
    }

    /**
     * This methods is intended to <i>catch all</i> calls and immediately throw
     * {@link UnsupportedOperationException}. It will probably happen when an {@link OldInstrument} is used as
     * base class when in fact, a {@link NewInstrument} should be used.
     *
     * @see NewInstrument
     * @see Instrument
     * @see PricingEngine
     */
	protected final void setPricingEngine(final PricingEngine engine) {
	    throw new IllegalStateException(ReflectConstants.SHOULD_NOT_EXTEND_FROM_THIS_CLASS);
	}

}
