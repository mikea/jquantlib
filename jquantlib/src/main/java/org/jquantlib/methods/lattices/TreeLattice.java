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

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.math.Array;
import org.jquantlib.time.TimeGrid;

/**
 * @author Srinivas Hasti
 */
public class TreeLattice extends Lattice {

	private int n;
	private int statePricesLimit;
	protected List<Array> statePrices;

	public TreeLattice(TimeGrid t, int n) {
		super(t);
		this.n = n;
		if (n <= 0)
			throw new IllegalStateException("there is no zeronomial lattice!");
		statePrices = new ArrayList<Array>();
		statePrices.add(new Array(1, 1.0));
		statePricesLimit = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.Lattice#initialize(org.jquantlib.methods.lattices.DiscretizedAsset,
	 *      double)
	 */
	@Override
	public void initialize(DiscretizedAsset asset, double time) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.Lattice#partialRollback(org.jquantlib.methods.lattices.DiscretizedAsset,
	 *      double)
	 */
	@Override
	public void partialRollback(DiscretizedAsset asset, double to) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.Lattice#presentValue(org.jquantlib.methods.lattices.DiscretizedAsset)
	 */
	@Override
	public double presentValue(DiscretizedAsset asset) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.Lattice#rollback(org.jquantlib.methods.lattices.DiscretizedAsset,
	 *      double)
	 */
	@Override
	public void rollback(DiscretizedAsset asset, double to) {
		// TODO Auto-generated method stub

	}

}
