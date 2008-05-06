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

package org.jquantlib.instruments;

import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.results.Results;

/**
 * Defines a new-style instrument
 * 
 * @note A new-style instrument makes use of a pricing engine which must be
 * properly initialized before any call to its calculate() method.
 * 
 * @see http://quantlib.org/reference/group__instruments.html
 * 
 * @author Richard Gomes
 */
public abstract class NewInstrument extends Instrument {

	private static String SHOULD_DEFINE_PRICING_ENGINE = "Should define pricing engine";

	
	//
	// fields
	//
	
	/**
	 * The value of this attribute and any other that derived
	 * classes might declare must be set during calculation.
	 */
	protected PricingEngine engine;

	

	// FIXME: we must do the best efforts to remove this constructor
	protected NewInstrument() {
		super();
		this.engine = null;
	}

	
	// FIXME : add comments
	protected NewInstrument(final PricingEngine engine) {
		super();
		this.setPricingEngine(engine);
	}
	
	
	
	//
	// protected methods
	//
	
    /**
     * Set the pricing engine to be used of a new-style instrument.
     *  
     * @param engine
     */
	// FIXME: do the best efforts to remove this method.
	// This code should be moved to the constructor. Does it make sense?
	public final void setPricingEngine(final PricingEngine engine) {
		if (this.engine!=null) {
			this.engine.deleteObserver(this);
		}
		this.engine = engine;
		if (this.engine!=null) {
	   		this.engine.addObserver(this);
	   		this.engine.notifyObservers();
		}
    }

    
    /**
     * This method performs the actual calculations and set any needed results.
     * 
     * <p>
     * When a NewInstrument is used, the default implementation is responsible
     * for calling the pricing engine, passing arguments to it and retrieving
     * results.
     */
    protected final void performCalculations() {
    	if (engine==null) throw new NullPointerException(SHOULD_DEFINE_PRICING_ENGINE);
        engine.reset();
        setupArguments(engine.getArguments());
        engine.getArguments().validate();
        engine.calculate();
        fetchResults(engine.getResults());
    }

    
    /**
     * This method must leave the instrument in a consistent
     * state when the expiration condition is met.
     * 
     * @see Instrument.setupExpired
     */
    @Override
    protected void setupExpired() {
    	super.setupExpired();
    }
	
	
    /**
     * When a derived result structure is defined for an
     * instrument, this method should be overridden to read from  
     * it.
     * 
     * @param results
     */
    protected void fetchResults(final Results results) /* @ReadOnly */ {
    	super.NPV = results.value;
    	super.errorEstimate = results.errorEstimate;
    }


    
    //
    // abstract methods
    //
	
	/**
     * When a derived argument structure is defined for an
     * instrument, this method should be overridden to fill  
     * it. This is mandatory in case a pricing engine is used.
     */
    protected abstract void setupArguments(final Arguments arguments);


}
