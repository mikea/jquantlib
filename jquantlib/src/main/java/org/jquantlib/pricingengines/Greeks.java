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
 Copyright (C) 2005 StatPro Italia srl

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

import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Frequency;

/**
 * @author <Richard Gomes>
 */

public class Greeks {

    static public /*@Real*/ double  blackScholesTheta(
            final GeneralizedBlackScholesProcess p,
            final /*@Real*/ double value, final /*@Real*/ double delta, final /*@Real*/ double gamma) {

    	/*@Real*/ final double u = p.stateVariable().currentLink().value();
    	//TODO update zeroRate so that we do not need to set frequency and extrapolate
    	/*@Rate*/ final double r = p.riskFreeRate().currentLink().zeroRate(0.0, Compounding.Continuous, Frequency.Annual, false).rate();
    	/*@Rate*/ final double q = p.dividendYield().currentLink().zeroRate(0.0, Compounding.Continuous, Frequency.Annual, false).rate();
    	/*@Volatility*/ final double v = p.localVolatility().currentLink().localVol(0.0, u);

    	return r*value -(r-q)*u*delta - 0.5*v*v*u*u*gamma;
    }

    static public /*@Real*/ double defaultThetaPerDay(/*@Real*/ final double theta) {
    	return theta/365.0;
    }


}
