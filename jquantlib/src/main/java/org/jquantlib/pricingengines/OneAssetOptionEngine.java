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

package org.jquantlib.pricingengines;

import org.jquantlib.instruments.Instrument;
import org.jquantlib.instruments.Option;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.pricingengines.results.Greeks;
import org.jquantlib.pricingengines.results.MoreGreeks;
import org.jquantlib.pricingengines.results.OneAssetOptionResults;
import org.jquantlib.processes.StochasticProcess;

protected abstract class OneAssetOptionEngine
							< Arguments extends OneAssetOptionArguments, Results extends OneAssetOptionResults > 
							extends GenericEngine<Arguments, Results> {
	
	protected OneAssetOptionEngine() {
		super(innerArguments, innerResults);
	}

    /**
	 * Arguments for single-asset option calculation
	 * 
	 * @note This inner class must be kept <b>private</b> as its fields and ancertor's fields are exposed.
     * This programming style is not recommended and we should use getters/setters instead.
     * At the moment, we keep the original implementation.
     * 
     * @author Richard Gomes
	 */ 
    protected class Arguments extends Option.Arguments {
    	
      /**
       * @note This field is exposed.
       * 
       * @author Richard Gomes
       */
      public StochasticProcess stochasticProcess;

      public void validate() /*@ReadOnly*/ {
          super.validate();
          // we assume the underlying value to be the first state variable
          if (stochasticProcess.initialValues()[0] <= 0.0) throw new IllegalArgumentException("negative or zero underlying given");
      }
    }


    /**
	 * Results from single-asset option calculation
	 */
    protected class Results extends Instrument.InstrumentResults {
    	protected Greeks delegateGreeks = new Greeks();
    	protected MoreGreeks delegateMoreGreeks = new MoreGreeks();
    	
    	public void reset() {
            super.reset();
            delegateGreeks.reset();
            delegateMoreGreeks.reset();
        }
    }

}
    
