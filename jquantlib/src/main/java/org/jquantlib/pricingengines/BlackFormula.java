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
 Copyright (C) 2007 Cristina Duminuco
 Copyright (C) 2007 Chiara Fornarola
 Copyright (C) 2003, 2004, 2005, 2006 Ferdinando Ametrano
 Copyright (C) 2006 Mark Joshi
 Copyright (C) 2001, 2002, 2003 Sadruddin Rejeb
 Copyright (C) 2006 StatPro Italia srl

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

package org.jquantlib.pricingengines;

import org.jquantlib.instruments.Option;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;

/**
 * <p>
 * Ported from 
 * <ul>
 * <li>ql/pricingengines/blackformula.cpp</li>
 * <li>ql/pricingengines/blackformula.hpp</li>
 * </ul>
 * 
 * @author <Richard Gomes>
 *
 */

public class BlackFormula {

	//Make static for ease of use and porting
	/**
	 * 
	 * Black 1976 formula
	 * <p>
	 *  warning, instead of volatility it uses standard deviation, i.e. volatility*sqrt(timeToMaturity)
	 */
 	public static double /*@Real*/ blackFormula(Option.Type optionType,
              double /*@Real*/ strike,
              double /*@Real*/ forward,
              double /*@Real*/ stdDev) {
 	
 		return blackFormula(optionType, strike, forward, stdDev, 1.0, 0.0);
 	}/**
	 * 
	 * Black 1976 formula
	 * <p>
	 *  warning, instead of volatility it uses standard deviation, i.e. volatility*sqrt(timeToMaturity)
	 */
 	public static double /*@Real*/ blackFormula(Option.Type optionType,
              double /*@Real*/ strike,
              double /*@Real*/ forward,
              double /*@Real*/ stdDev,
              double /*@Real*/ discount) {
 	
 		return blackFormula(optionType, strike, forward, stdDev, discount, 0.0);
 	}
	/**
	 * 
	 * Black 1976 formula
	 * <p>
	 *  warning, instead of volatility it uses standard deviation, i.e. volatility*sqrt(timeToMaturity)
	 */
 	public static double /*@Real*/ blackFormula(Option.Type optionType,
              double /*@Real*/ strike,
              double /*@Real*/ forward,
              double /*@Real*/ stdDev,
              double /*@Real*/ discount,
              double /*@Real*/ displacement) {
		  if (!(strike>=0.0)){
			  throw new ArithmeticException("strike (" + strike + ") must be non-negative");
		  }
		  if (!(forward>0.0)){
			  throw new ArithmeticException("forward (" + forward + ") must be positive");
		  }
		  if (!(stdDev>=0.0)){
			  throw new ArithmeticException("stdDev (" + stdDev + ") must be non-negative");
		  }
		  if (!(discount>0.0)){
			  throw new ArithmeticException("positive discount required: " + discount + " not allowed");
		  }
		  if (!(displacement>=0.0)){
			  throw new ArithmeticException("displacement (" + displacement + ") must be non-negative");
		  }

		  forward = forward + displacement;
		  strike = strike + displacement;
		  if (stdDev==0.0)
			  return Math.max((forward-strike)*optionType.toInteger(), (0.0d))*discount;
		  
		  if (strike==0.0) // strike=0 iff displacement=0
			  return (optionType==Option.Type.CALL ? forward*discount : 0.0);
		  
		  double /*@Real*/ d1 = Math.log(forward/strike)/stdDev + 0.5*stdDev;
		  double /*@Real*/ d2 = d1 - stdDev;
		  CumulativeNormalDistribution phi = new CumulativeNormalDistribution();
		  double /*@Real*/ result = discount * optionType.toInteger() *
		  	(forward*phi.evaluate(optionType.toInteger()*d1) - strike*phi.evaluate(optionType.toInteger()*d2));
		  
		  if (!(result>=0.0)){
			  throw new ArithmeticException("negative value (" + result + ") for a " + stdDev +
					  " stdDev " + optionType + " option struck at " +
					  strike + " on a " + forward + " forward");
			 
		  }
		  return result;
	  }
}
