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

import org.jquantlib.QL;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.util.LazyObject;

/**
 * This is an abstract {@link Instrument} class which is able to use a {@link PricingEngine} implemented
 * internally or externally to it. Extended classes {@link OldInstrument} and {@link NewInstrument} are
 * responsible for defining the adequate behaviour.
 * <p>
 * An <i>old style instrument</i> must provide its own calculation logic whilst a
 * <i>new style instrument</i> relies on a certain pricing engine for performing calculations.
 *
 * @see OldInstrument
 * @see NewInstrument
 * @see PricingEngine
 * @see <a href="http://quantlib.org/reference/group__instruments.html">QuantLib: Financial Instruments</a>
 *
 * @author Richard Gomes
 */
public abstract class Instrument extends LazyObject {

    //
    // private static final fields
    //

    private static final String SHOULD_DEFINE_PRICING_ENGINE = "Should define pricing engine";
    private static final String SETUP_ARGUMENTS_NOT_IMPLEMENTED = "Instrument#setupArguments() not implemented";


    //
    // protected fields
    //

    /**
     * The value of this attribute and any other that derived classes might declare must be set during calculation.
     *
     * @see PricingEngine
     */
    protected PricingEngine engine;

    /**
     * Represents the net present value of the instrument.
     */
    protected /*@Price*/ double NPV;

    /**
     * Represents the error estimate on the NPV when available.
     */
    protected /*@Price*/ double errorEstimate;


    //
    // public abstract methods
    //

    /**
     * @return <code>true</code> if the instrument is still tradeable.
     */
    public abstract boolean isExpired();

    /**
     * Passes arguments to be used by a {@link PricingEngine}.
     * When a derived argument structure is defined for an instrument, this method should be overridden to fill it.
     *
     * @param arguments keeps values to be used by the external {@link PricingEngine}
     *
     * @see Arguments
     * @see PricingEngine
     */
    protected void setupArguments(final Arguments arguments) /* @ReadOnly */ {
        QL.error(SETUP_ARGUMENTS_NOT_IMPLEMENTED);
        throw new LibraryException(SETUP_ARGUMENTS_NOT_IMPLEMENTED);
    }



    //
    // protected constructors
    //

    protected Instrument() {
        super();
        this.NPV = Double.NaN;
        this.errorEstimate = 0.0;
    }


    //
    // public final methods
    //

    /**
     * This method defines an external {@link PricingEngine} to be used for a <i>new-style</i> {@link Instrument}.
     *
     * @param engine is the external {@link PricingEngine} to be used
     *
     * @see PricingEngine
     */
    public final void setPricingEngine(final PricingEngine engine) {
        if (this.engine != null) {
            this.engine.deleteObserver(this);
        }
        this.engine = engine;
        if (this.engine != null) {
            this.engine.addObserver(this);
        }
        update(this, null);
    }

    public final/*@Price*/double getNPV() /*@ReadOnly*/{
        calculate();
        QL.require(!Double.isNaN(this.NPV) , "NPV not provided");  // QA:[RG]::verified // TODO: message
        return NPV;
    }

    public final/*@Price*/double getErrorEstimate() /*@ReadOnly*/{
        calculate();
        QL.require(!Double.isNaN(this.errorEstimate) , "error estimate not provided"); // QA:[RG]::verified // TODO: message
        return errorEstimate;
    }


    //
    // protected methods
    //

    /**
     * Obtains the {@link Results} populated by a {@link PricingEngine}.
     * When a derived result structure is defined for an instrument, this method should be overridden to read from it.
     *
     * @param results contains the {@link Results} object populated by a {@link PricingEngine}
     *
     * @see Results
     * @see PricingEngine
     */
    protected void fetchResults(final Results results) /* @ReadOnly */{
        NPV = results.value;
        errorEstimate = results.errorEstimate;
    }


    /**
     * This method must leave the instrument in a consistent
     * state when the expiration condition is met.
     *
     * @see NewInstrument.setupExpired()
     */
    protected void setupExpired() /*@ReadOnly*/{
        NPV = 0.0;
        errorEstimate = 0.0;
    }


    //
    // overrides LazyObject
    //

    /**
     * This method performs the actual calculations and set any needed results.
     * <p>
     * When a NewInstrument is used, the default implementation is responsible for calling the pricing engine, passing arguments to
     * it and retrieving results.
     *
     * @see LazyObject#performCalculations
     */
    @Override
    protected void performCalculations() {
        QL.require(engine != null, SHOULD_DEFINE_PRICING_ENGINE); // QA:[RG]::verified
        engine.reset();
        setupArguments(engine.getArguments());
        engine.getArguments().validate();
        engine.calculate();
        fetchResults(engine.getResults());
    }

    @Override
    protected void calculate() /*@ReadOnly*/{
        if (isExpired()) {
            setupExpired();
            calculated = true;
        } else {
            super.calculate();
        }
    }

}
