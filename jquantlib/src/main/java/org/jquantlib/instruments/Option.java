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

import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.util.Date;

/**
 * Abstract base class for Options
 * 
 * @author Richard Gomes
 */
public abstract class Option extends NewInstrument {

    //
    // protected final fields
    //
    
    protected final Payoff payoff;
	protected final Exercise exercise;
	
	//
	// private fields
	//
	
	/**
	 * This private field is automatically initialized by constructor which
	 * picks up it's value from {@link Settings} singleton. This procedure
	 * caches values from the singleton, intending to avoid contention in
	 * heavily multi-threaded environments.
	 */
	private Date evaluationDate = null;

	//
	// public constructors
	//
	
	/**
	 * This constructor
	 */
	public Option(final Payoff payoff, final Exercise exercise, final PricingEngine engine) {
		super(engine);
		this.payoff = payoff;
		this.exercise = exercise;
		this.evaluationDate = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
	}

	//
	// public methods overriden from Instrument
	//
	
	@Override
	public boolean isExpired() /* @ReadOnly */ {
        return exercise.lastDate().le( evaluationDate );
    }

	
	//
	// public static inner enums
	//
	
	/**
	 * This enumeration represents options types: CALLs and PUTs.
	 */
	public static enum Type {
		PUT(-1), CALL(1);

		private int value;

		private Type(final int type) {
			this.value = type;
		}

		/**
		 * This method returns the <i>mathematical signal</i> associated to an option type.
		 * 
		 * @return 1 for CALLs; -1 for PUTs
		 */
		public int toInteger() {
			return value;
		}
	}

}
