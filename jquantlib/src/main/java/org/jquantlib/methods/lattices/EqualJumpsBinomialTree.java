/*
 Copyright (C) 2008 Srinivas Hasti

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

import java.math.BigInteger;

import org.jquantlib.processes.StochasticProcess1D;

/**
 * @author Srinivas Hasti
 * @author Tim Swetonic
 *
 */
public class EqualJumpsBinomialTree<T extends BinomialTree> extends BinomialTree<T> {


    protected double dx_, pu_, pd_;

    public EqualJumpsBinomialTree() {
    }
    
    public EqualJumpsBinomialTree(
            final StochasticProcess1D process,
          /*Time*/ double end,
          /*Size*/ int steps) {
        super(process, end, steps);
    }


	@Override
	public double probability(int i, int index, int branch) {
	    return (branch == 1 ? pu_ : pd_);
	}

	@Override
	public double underlying(int i, int index) {
        BigInteger j = BigInteger.valueOf((long)index)
        .multiply(new BigInteger("2"))
            .subtract(new BigInteger(String.valueOf(i)));

        return this.x0_*Math.exp(j.intValue()*this.dx_);
	}

}
