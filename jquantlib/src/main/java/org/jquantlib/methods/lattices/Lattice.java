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

import org.jquantlib.assets.DiscretizedAsset;
import org.jquantlib.math.Array;
import org.jquantlib.time.TimeGrid;

/**
 * @author Srinivas Hasti
 * 
 */
// ql/numericalmethod.hpp
public abstract class Lattice {
	protected TimeGrid t;

	public Lattice(TimeGrid t) {
		this.t = t;
	}

	public TimeGrid timeGrid() {
		return t;
	}

	/*
	 * ! \name Numerical method interface
	 * 
	 * These methods are to be used by discretized assets and must be overridden
	 * by developers implementing numerical methods. Users are advised to use
	 * the corresponding methods of DiscretizedAsset instead. @{
	 */

	// ! initialize an asset at the given time.
	public abstract void initialize(DiscretizedAsset asset,
	/* Time */double time);

	/*
	 * ! Roll back an asset until the given time, performing any needed
	 * adjustment.
	 */
	public abstract void rollback(DiscretizedAsset asset,
	/* Time */double to);

	/*
	 * ! Roll back an asset until the given time, but do not perform the final
	 * adjustment.
	 * 
	 * \warning In version 0.3.7 and earlier, this method was called
	 * rollAlmostBack method and performed pre-adjustment. This is no longer
	 * true; when migrating your code, you'll have to replace calls such as:
	 * \code method->rollAlmostBack(asset,t); \endcode with the two statements:
	 * \code method->partialRollback(asset,t); asset->preAdjustValues();
	 * \endcode
	 */
	public abstract void partialRollback(DiscretizedAsset asset,
	/* Time */double to);

	// ! computes the present value of an asset.
	public abstract/* Real */double presentValue(DiscretizedAsset asset);

	// @}

	// this is a smell, but we need it. We'll rethink it later.
	public abstract Array grid(double t);
}
