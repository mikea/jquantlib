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
package org.jquantlib.math;

import org.jquantlib.math.matrixutilities.Array;


/**
 * 
 * @author Srinivas Hasti
 *
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class TransformedGrid {

	protected Array grid_;
	protected Array transformedGrid_;
	protected Array dxm_;
	protected Array dxp_;
	protected Array dx_;

	public TransformedGrid(final Array grid) {
	    // TODO: code review :: use of clone()
		this.grid_ = grid;
		this.transformedGrid_ = grid.clone();
		this.dxm_ = new Array(grid.length);
		this.dxp_ = new Array(grid.length);
		this.dx_ = new Array(grid.length);
		for (int i = 1; i < transformedGrid_.length - 1; i++) {
			dxm_.set(i, transformedGrid_.get(i) - transformedGrid_.get(i - 1));
			dxp_.set(i, transformedGrid_.get(i + 1) - transformedGrid_.get(i));
			dx_.set(i, dxm_.get(i) + dxp_.get(i));
		}
	}

	public TransformedGrid(final Array grid, final Ops.DoubleOp f) {
	    // TODO: code review :: use of clone()
	    this.grid_ = grid;
		this.transformedGrid_ = grid.clone().transform(f);
		this.dxm_ = new Array(grid.length);
		this.dxp_ = new Array(grid.length);
		this.dx_ = new Array(grid.length);
		for (int i = 1; i < transformedGrid_.length - 1; i++) {
			dxm_.set(i, transformedGrid_.get(i) - transformedGrid_.get(i - 1));
			dxp_.set(i, transformedGrid_.get(i + 1) - transformedGrid_.get(i));
			dx_.set(i, dxm_.get(i) + dxp_.get(i));
		}
	}

	public Array gridArray() {
		return grid_;
	}

	public Array transformedGridArray() {
		return transformedGrid_;
	}

	public Array dxmArray() {
		return dxm_;
	}

	public Array dxpArray() {
		return dxp_;
	}

	public Array dxArray() {
		return dx_;
	}

	public double grid(int i) {
		return grid_.get(i);
	}

	public double transformedGrid(int i) {
		return transformedGrid_.get(i);
	}

	public double dxm(int i) {
		return dxm_.get(i);
	}

	public double dxp(int i) {
		return dxp_.get(i);
	}

	public double dx(int i) {
		return dx_.get(i);
	}

	public int size() {
		return grid_.length;
	}
}
