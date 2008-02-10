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

import java.util.Map;
import java.util.TreeMap;

import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.PricingEngineArguments;
import org.jquantlib.pricingengines.PricingEngineResults;
import org.jquantlib.util.LazyObject;
import org.jquantlib.util.Observer;
import org.jscience.mathematics.number.Real;

//FIXME: TO BE DONE

public abstract class Instrument extends LazyObject implements Observer {


	/**
	 * The value of this attribute and any other that derived
	 * classes might declare must be set during calculation.
	 * 
	 * @todo verify how these variables are used
	 */
	// FIXME: verify how these variables are used
	private PricingEngine engine_;
	
	/**
	 * Represents the net present value of the instrument.
	 */
	private Real NPV_;
	
	/**
	 * Represents the error estimate on the NPV when available.
	 */
	private Real errorEstimate_;
	
	/**
	 * Represents all additional result returned by the pricing engine.
	 */
	private Map<String, Object> additionalResults_;
	
	

    public Instrument() {
    	super();
    	this.NPV_ = null;
    	this.errorEstimate_ = null;
    }

    

	
	public final Real getNPV() {
		calculate();
		if (this.NPV_==null) throw new ArithmeticException("NPV not provided"); // FIXME: find something better
		return NPV_;
	}

	public final Real getErrorEstimate() {
		calculate();
		if (this.errorEstimate_==null) throw new ArithmeticException("error estimate not provided"); // FIXME: find something better
		return errorEstimate_;
	}

	/**
	 * @return <code>true</code> whether the instrument is still tradeable.
	 */
	public abstract boolean isExpired();
	
	


    /**
     * Set the pricing engine to be used.
     * 
     * <p><b>Note:</b> calling this method will have no effects in 
     * case the <b>performCalculation</b> method
     * was overridden in a derived class.
     *  
     * @param engine
     */
    protected void setPricingEngine(final PricingEngine engine) {
    	if (this.engine_!=null) {
    		this.engine_.deleteObserver(this);
    	}
    	this.engine_ = engine;
    	if (this.engine_!=null) {
    		this.engine_.addObserver(this);
    	}
    	update();
    }

    /**
     * When a derived argument structure is defined for an
     * instrument, this method should be overridden to fill
     * it. This is mandatory in case a pricing engine is used.
     *  
     * @param arguments
     * @todo code review
     */
    // FIXME: code review
    public abstract void setupArguments(final PricingEngineArguments arguments);

    /**
     * When a derived result structure is defined for an
     * instrument, this method should be overridden to read from
     * it. This is mandatory in case a pricing engine is used.
     *   
     * @param results
     * @todo code review
     */
    // FIXME: code review
    public abstract void fetchResults(final PricingEngineResults results);
    
    
    
	protected void calculate() {
		if (isExpired()) {
			setupExpired();
			calculated_ = true;
		} else {
			super.calculate();
		}
	}
    
	
	
	
	
    /**
     * This method must leave the instrument in a consistent
     * state when the expiration condition is met.
     */
    protected final void setupExpired() {
        NPV_ = new Real(0.0);
        errorEstimate_ = new Real(0.0);
        additionalResults_.clear();
    }

    /**
     * In case a pricing engine is <b>not</b> used, this
     * method must be overridden to perform the actual
     * calculations and set any needed results. In case
     * a pricing engine is used, the default implementation
     * can be used.
     */
    protected final void performCalculations() {
        if (engine_==null) throw new NullPointerException("null pricing engine");
        engine_.reset();
        setupArguments(engine_.getArguments());
        engine_.getArguments().validate();
        engine_.calculate();
        fetchResults(engine_.getResults());
    }
        
//    template <class T>
//    inline T Instrument::result(const std::string& tag) const {
//        calculate();
//        std::map<std::string,boost::any>::const_iterator value =
//            additionalResults_.find(tag);
//        QL_REQUIRE(value != additionalResults_.end(),
//                   tag << " not provided");
//        return boost::any_cast<T>(value->second);
//    }
    
    // FIXME: verify how this method is used
    protected final Object getResult(final String tag) {
    	calculate();
    	Object value = additionalResults_.get(tag);
    	if (value==null) throw new IllegalArgumentException(tag+" not provided");
        return value;
    }

    public final Map<String, Object> getAdditionalResults() {
        return additionalResults_;
    }

	
    /**
     * @todo code review
     */
   // FIXME: code review
    private class InstrumentResults implements PricingEngineResults {
        private Real value;
        private Real errorEstimate;
        private Map<String, Object> additionalResults;

        public void reset() {
    		value = null;
    		errorEstimate = null;
    		additionalResults.clear();
    	}

        public final Real getValue() {
        	return value;
        }
        
        public final Real getErrorEstimate() {
        	return errorEstimate;
        }
        
        public Map<String,Object> getAdditionalResults() {
        	return copy(this.additionalResults);

        }
        
        public void setValue(final Real value) {
        	this.value = value;
        }
        
        public void setErrorEstimate(final Real errorEstimate) {
        	this.errorEstimate = errorEstimate;
        }
        
        public void setAdditionalResults(final Map<String,Object> additionalResults) {
        	this.additionalResults = copy(additionalResults);
        }
        
        private Map<String,Object> copy(Map<String,Object> src) {
        	Map<String,Object> dest;
        	if (src==null) {
        		dest = null;
        	} else {
        		dest = new TreeMap<String,Object>();
        		dest.putAll(src);
        	}
        	return dest;
        }
        
    }
	
}
