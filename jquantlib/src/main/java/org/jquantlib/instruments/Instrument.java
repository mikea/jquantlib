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

import org.jquantlib.util.LazyObject;

    /** Defines an abstract instrument class
     * 
     * <p>An instrument may or may not make use of a pricing engine.
     * An old style instrument must provide it's own calculation logic whilst
     * a new style instrument relies on a pricing engine for performing calculations.
     * 
     * @see http://quantlib.org/reference/group__instruments.html
	 * 
	 * @author Richard Gomes
	 * 
     * @author Richard Gomes
     */
    public abstract class Instrument extends LazyObject {

    	//
    	// fields
    	//
    	
    	/**
    	 * Represents the net present value of the instrument.
    	 */
    	protected /*@Price*/ double NPV;
    	
    	/**
    	 * Represents the error estimate on the NPV when available.
    	 */
    	protected /*@Price*/ double errorEstimate;
    	

    	
    	//
    	// constructors
    	//
    	
    	// FIXME: NaN .vs. null ??
        protected Instrument() {
        	super();
        	this.NPV = Double.NaN;
        	this.errorEstimate = 0.0;
        }
        
        
        
    	//
    	// getters and setters
    	//

    	// FIXME: NaN .vs. null ??
    	public final /*@Price*/ double getNPV() /*@ReadOnly*/ {
    		calculate();
    		if (Double.isNaN(this.NPV)) throw new ArithmeticException("NPV not provided");
    		return NPV;
    	}

    	// FIXME: NaN .vs. null ??
    	public final /*@Price*/ double getErrorEstimate() /*@ReadOnly*/ {
    		calculate();
    		if (Double.isNaN(this.errorEstimate)) throw new ArithmeticException("error estimate not provided");
    		return errorEstimate;
    	}

    	
    	
    	//
    	// abstract methods
    	//
    	


		/**
    	 * @return <code>true</code> whether the instrument is still tradeable.
    	 */
    	public abstract boolean isExpired();


    	
    	//
    	// protected methods
    	//
    	
    	/**
         * This method must leave the instrument in a consistent
         * state when the expiration condition is met.
         * 
         * @see NewInstrument.setupExpired()
         */
        protected void setupExpired() /*@ReadOnly*/ {
            NPV = 0.0;
            errorEstimate = 0.0;
        }

        
        
    	//
    	// overriden methods from LazyObject
    	//
    	
    	@Override
    	protected void calculate() /*@ReadOnly*/ {
    		if (isExpired()) {
    			setupExpired();
    			calculated = true;
    		} else {
    			super.calculate();
    		}
    	}

}
