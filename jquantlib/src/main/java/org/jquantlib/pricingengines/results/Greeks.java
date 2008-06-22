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
 * This class keeps Greeks and other {@link Results} calculated by a {@link PricingEngine}
 * <p>
 * In mathematical finance, the Greeks are the quantities representing the market sensitivities of derivatives such as options. Each
 * "Greek" measures a different aspect of the risk in an option position, and corresponds to a parameter on which the value of an
 * instrument or portfolio of financial instruments is dependent. The name is used because the parameters are often denoted by Greek
 * letters.
 * 
 * @note Public fields as this class works pretty much as Data Transfer Objects
 * 
 * @see Results
 * @see NewInstrument
 * @see PricingEngine
 * @see Arguments
 * @see <a href="http://en.wikipedia.org/wiki/Greeks_(finance)">Greeks</a>
 * @see <a href="http://www.theponytail.net/DOL/DOLnode69.htm">The Derivatives Online Pages</a>
 * 
 * @author Richard Gomes
 */
public class Greeks extends Results {

	//
    // public fields
    //
    
    /**
     * The <i>delta</i> measures the sensitivity to changes in the price of the underlying asset. The <p>{@latex[\Delta}
     * of an instrument is the mathematical derivative of the option value <p>{@latex[V} with respect to the underlyer's
     * price, <p>{@latex[\Delta = \frac{\partial V}{\partial S}}
     * 
     * @see <a href="http://www.theponytail.net/DOL/DOLnode71.htm">Delta</a>
     */
    public /*@Real*/ double delta;

    /**
     * The <i>gamma</i> measures the rate of change in the delta. The <p>{@latex[\Gamma} is the second
     * derivative of the value function with respect to the underlying price, <p>{@latex[\Gamma = \frac{\partial^2 V}{\partial S^2}}.
     * Gamma is important because it indicates how a portfolio will react to relatively large shifts in price.
     * 
     * @see <a href="http://www.theponytail.net/DOL/DOLnode73.htm">Gamma</a>
     */
    public /*@Real*/ double gamma;
	
    /**
     * The <i>theta</i> measures sensitivity to the passage of time. <p>{@latex[\Theta} is the negative of the derivative of the option 
     * value with respect to the amount of time to expiry of the option, <p>{@latex[\Theta = -\frac{\partial V}{\partial T}}.
     * 
     * @see <a href="http://www.theponytail.net/DOL/DOLnode72.htm">Theta</a>
     * @see <a href="http://en.wikipedia.org/wiki/Option_time_value">Option time value</a> 
     */
    public /*@Real*/ double theta;
	
    /**
     * The <i>vega</i>, which is not a Greek letter (<p>{@latex[\nu}, ''nu'' is used instead), measures sensitivity to
     * volatility. The vega is the derivative of the option value with respect to the volatility of the underlying, 
     * <p>{@latex[\nu=\frac{\partial V}{\partial \sigma}}. The term <i>kappa</i>, {@latex[\kappa}, is sometimes used instead of
     * <i>vega</i>, as is <i>tau</i>, <p>{@latex[\tau}, though this is rare.
     * 
     * @see <a href="http://www.theponytail.net/DOL/DOLnode74.htm">Vega</a>
     * @see <a href="http://en.wikipedia.org/wiki/Volatility_(finance)">Volatility</a>
     */
    public /*@Real*/ double vega;
	
	/**
     * The <i>rho</i> measures sensitivity to the applicable interest rate. The <p>{@latex[\rho} is the
     * derivative of the option value with respect to the risk free rate, <p>{@latex[\rho = \frac{\partial V}{\partial r}}.
     * 
     * @see <a href="http://www.theponytail.net/DOL/DOLnode75.htm">Rho</a>
     */
    public /*@Real*/ double rho;
	
    /**
     * @see <a href="http://www.theponytail.net/DOL/DOLnode76.htm">Dividends and FX Options</a>
     */
    //PENDING: understand the meaning of this field
    public /*@Real*/ double dividendRho;

    
    //
    // public constructors
    //
    
	public Greeks() {
		super();
	}

	//
	// public methods
	//
	
	@Override
	public void reset() {
		super.reset();
		delta = gamma = theta = vega = rho = dividendRho = Double.NaN;
	}

}
