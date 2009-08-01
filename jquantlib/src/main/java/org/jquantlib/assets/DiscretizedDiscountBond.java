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
package org.jquantlib.assets;

import java.util.List;

import org.joda.primitives.list.impl.ArrayDoubleList;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.methods.lattices.Lattice;

/**
 * @author Srinivas Hasti
 * 
 */
public class DiscretizedDiscountBond extends DiscretizedAsset {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.DiscretizedAsset#initialize(org.jquantlib.methods.lattices.Lattice,
	 *      double)
	 */
	@Override
	public void initialize(Lattice lt, double t) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.DiscretizedAsset#isOnTime(double)
	 */
	@Override
	protected boolean isOnTime(double t) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.DiscretizedAsset#mandatoryTimes()
	 */
	@Override
	public List<Double> mandatoryTimes() {
		return new ArrayDoubleList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.DiscretizedAsset#partialRollback(double)
	 */
	@Override
	public void partialRollback(double to) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.DiscretizedAsset#postAdjustValues()
	 */
	@Override
	public void postAdjustValues() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.DiscretizedAsset#preAdjustValues()
	 */
	@Override
	public void preAdjustValues() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.DiscretizedAsset#presentValue()
	 */
	@Override
	public double presentValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.DiscretizedAsset#reset(int)
	 */
	@Override
	public void reset(int size) {
		values = new Array(size).fill(1.0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.methods.lattices.DiscretizedAsset#rollback(double)
	 */
	@Override
	public void rollback(double to) {
		// TODO Auto-generated method stub

	}

}
