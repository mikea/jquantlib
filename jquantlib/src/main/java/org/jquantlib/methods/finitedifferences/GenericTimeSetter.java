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
package org.jquantlib.methods.finitedifferences;

import org.jquantlib.math.TransformedGrid;
import org.jquantlib.methods.finitedifferences.TridiagonalOperator.TimeSetter;

public class GenericTimeSetter<T> implements TimeSetter {
	private TransformedGrid grid;
	private PdeSecondOrderParabolic pde;

	public GenericTimeSetter(TransformedGrid grid, T pde) {
		this.grid = grid;
		this.pde = new PdeProxy<T>(pde);
	}

	@Override
	public void setTime(double t, TridiagonalOperator l) {
		pde.generateOperator(t, grid, l);
	}

}
