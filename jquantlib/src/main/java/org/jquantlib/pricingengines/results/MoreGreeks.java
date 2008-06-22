/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.pricingengines.results;

import org.jquantlib.instruments.NewInstrument;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;


/**
 * This class keeps additional Greeks and other {@link Results} calculated by a {@link PricingEngine}
 * <p>
 * In mathematical finance, the Greeks are the quantities representing the market sensitivities of derivatives such as options. Each
 * "Greek" measures a different aspect of the risk in an option position, and corresponds to a parameter on which the value of an
 * instrument or portfolio of financial instruments is dependent. The name is used because the parameters are often denoted by Greek
 * letters.
 * 
 * @note Public fields as this class works pretty much as Data Transfer Objects
 * 
 * @see Greeks
 * @see Results
 * @see NewInstrument
 * @see PricingEngine
 * @see Arguments
 * @see <a href="http://en.wikipedia.org/wiki/Greeks_(finance)">Greeks</a>
 * 
 * @author Richard Gomes
 */
public class MoreGreeks extends Greeks {

    //FIXME: comment fields
    
    /**
     * Probability that an Option expires <i>in-the-money</i>
     * 
     * @see <a href="http://www.optiontradingpedia.com/in_the_money_options.htm">In The Money Options</a>
     */
    public /*@Real*/ double itmCashProbability;
    
	public /*@Real*/ double deltaForward;
	
	
	public /*@Real*/ double elasticity;
	
	public /*@Real*/ double thetaPerDay;
	
	public /*@Real*/ double strikeSensitivity;

	public MoreGreeks() {
		super();
	}

	@Override
	public void reset() {
		super.reset();
		itmCashProbability = deltaForward = elasticity = thetaPerDay = strikeSensitivity = Double.NaN;
	}

}
