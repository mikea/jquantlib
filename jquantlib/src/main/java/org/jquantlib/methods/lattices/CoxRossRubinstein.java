/*
 Copyright (C) 2008 Srinivas Hasti
 Copyright (C) 2008 Tim Swetonic

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
package org.jquantlib.methods.lattices;

import org.jquantlib.processes.StochasticProcess1D;

/**
 * @author Srinivas Hasti
 * @author Tim Swetonic
 *
 */
//concrete impl
public class CoxRossRubinstein extends EqualJumpsBinomialTree<CoxRossRubinstein> {

    public CoxRossRubinstein(final StochasticProcess1D process,
               /*Time*/ double end, 
               /*Size*/ int steps, 
               /*Real*/ double d) {
        
        super(process, end, steps); 

        dx_ = process.stdDeviation(0.0, x0_, dt_);
        pu_ = 0.5 + 0.5*driftPerStep_/dx_;;
        pd_ = 1.0 - pu_;

        if(pu_ > 1.0 || pu_ < 0.0)
            throw new IllegalStateException("negative probability");
//        QL_REQUIRE(pu_<=1.0, "negative probability");
//        QL_REQUIRE(pu_>=0.0, "negative probability");
    }

}
