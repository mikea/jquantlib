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

import org.jquantlib.math.functions.DoubleFunction;
import org.jquantlib.util.stdlibc.Std;

/**
 * 
 * @author Srinivas Hasti
 *
 */
public class TransformedGrid {

	protected Array grid_;
	protected Array transformedGrid_;
	protected Array dxm_;
	protected Array dxp_;
	protected Array dx_;

	public TransformedGrid(Array grid) {
		this.grid_ = new Array(grid);
		this.transformedGrid_ = new Array(grid);
		this.dxm_ = new Array(grid.size());
		this.dxp_ = new Array(grid.size());
		this.dx_ = new Array(grid.size());
		for (int i = 1; i < transformedGrid_.size() - 1; i++) {
			dxm_.set(i, transformedGrid_.at(i) - transformedGrid_.at(i - 1));
			dxp_.set(i, transformedGrid_.at(i + 1) - transformedGrid_.at(i));
			dx_.set(i, dxm_.at(i) + dxp_.at(i));
		}
	}

	public TransformedGrid(Array grid, DoubleFunction f) {
		this.grid_ = new Array(grid);
		this.transformedGrid_ = new Array(grid);
		this.dxm_ = new Array(grid.size());
		this.dxp_ = new Array(grid.size());
		this.dx_ = new Array(grid.size());
		Std.getInstance().apply(transformedGrid_.getData(), f);
		for (int i = 1; i < transformedGrid_.size() - 1; i++) {
			dxm_.set(i, transformedGrid_.at(i) - transformedGrid_.at(i - 1));
			dxp_.set(i, transformedGrid_.at(i + 1) - transformedGrid_.at(i));
			dx_.set(i, dxm_.at(i) + dxp_.at(i));
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
		return grid_.at(i);
	}

	public double transformedGrid(int i) {
		return transformedGrid_.at(i);
	}

	public double dxm(int i) {
		return dxm_.at(i);
	}

	public double dxp(int i) {
		return dxp_.at(i);
	}

	public double dx(int i) {
		return dx_.at(i);
	}

	public int size() {
		return grid_.size();
	}
}
