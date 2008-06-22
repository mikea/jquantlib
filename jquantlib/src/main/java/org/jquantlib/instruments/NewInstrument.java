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

import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.util.LazyObject;

/**
 * Defines a <i>new-style</i> {@link Instrument} which makes use of an external {@link PricingEngine}.
 * 
 * which must be properly initialized before any call to its calculate() method.
 * 
 * @see Instrument
 * @see NewInstrument
 * @see PricingEngine
 * @see <a href="http://quantlib.org/reference/group__instruments.html">QuantLib: Financial Instruments</a>
 * 
 * @author Richard Gomes
 */
public abstract class NewInstrument extends Instrument {

    //
    // private static final fields
    //

    private static final String SHOULD_DEFINE_PRICING_ENGINE = "Should define pricing engine";

    //
    // protected fields
    //

    /**
     * The value of this attribute and any other that derived classes might declare must be set during calculation.
     * 
     * @see PricingEngine
     */
    protected PricingEngine engine;

    //
    // abstract methods
    //

    /**
     * Passes arguments to be used by a {@link PricingEngine}.
     * When a derived argument structure is defined for an instrument, this method should be overridden to fill it.
     * 
     * @param arguments keeps values to be used by the external {@link PricingEngine}
     * 
     * @see Arguments
     * @see PricingEngine
     */
    protected abstract void setupArguments(final Arguments arguments);

    //
    // protected constructors
    //

    /**
     * Default constructor for a <i>new-style</i> {@link Instrument}
     */
    protected NewInstrument() {
        super();
        this.engine = null;
    }

    /**
     * Constructor which initializes an {@link Instrument} with an external {@link PricingEngine}
     * 
     * @param engine is the external {@link PricingEngine} to be used
     * 
     * @see PricingEngine
     */
    protected NewInstrument(final PricingEngine engine) {
        super();
        this.setPricingEngine(engine);
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
        // XXX this.engine.notifyObservers();
        update(this, null);
    }

    //
    // protected *final* methods
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
    protected final void performCalculations() {
        // verify if a PricingEngine was previously defined
        if (engine == null)
            throw new NullPointerException(SHOULD_DEFINE_PRICING_ENGINE);

        // go ahead
        engine.reset();
        setupArguments(engine.getArguments());
        engine.getArguments().validate();
        engine.calculate();
        fetchResults(engine.getResults());
    }

    //
    // protected *virtual* methods
    //

    /**
     * @InheritDoc
     * 
     * This method must leave the instrument in a consistent state when the expiration condition is met.
     * 
     * @see Instrument#setupExpired
     */
    @Override
    protected void setupExpired() {
        super.setupExpired();
    }

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
        super.NPV = results.value;
        super.errorEstimate = results.errorEstimate;
    }

}
