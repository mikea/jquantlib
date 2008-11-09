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
 Copyright (C) 2003 Ferdinando Ametrano
 Copyright (C) 2004 StatPro Italia srl

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

package org.jquantlib.math.interpolations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NaturalMonotonicCubicSpline extends CubicSpline {
	
	private final static Logger logger = LoggerFactory.getLogger(NaturalMonotonicCubicSpline.class);
	
	// 
	// constructor
	//
	//TODO: Code Review: Is this correct? 
	//TODO: Review: Where are the xBegin, xEnd, yBegin from the QuantLib code assigned?
	public NaturalMonotonicCubicSpline() {
		super(BoundaryCondition.SecondDerivative, 0.0, BoundaryCondition.SecondDerivative,0.0,true);
	}
	
	/* QuantLib Code
	 * 
	//! Natural cubic spline with monotonicity constraint
    class NaturalMonotonicCubicSpline : public CubicSpline {
      public:
        //! \pre the \f$ x \f$ values must be sorted. 
        template <class I1, class I2>
        NaturalMonotonicCubicSpline(const I1& xBegin, const I1& xEnd,
                                    const I2& yBegin)
        : CubicSpline(xBegin,xEnd,yBegin,
                      SecondDerivative, 0.0,
                      SecondDerivative, 0.0,
                      true) {}
    };
    */

}
