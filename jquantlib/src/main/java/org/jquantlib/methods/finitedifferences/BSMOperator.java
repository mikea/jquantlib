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

import java.util.List;

import org.jquantlib.processes.GeneralizedBlackScholesProcess;

public class BSMOperator extends TridiagonalOperator {

	public BSMOperator(int size, double dx, double r, double q, double sigma) {
		super(size);
		double sigma2 = sigma * sigma;
		double nu = r - q - sigma2 / 2;
		double pd = -(sigma2 / dx - nu) / (2 * dx);
		double pu = -(sigma2 / dx + nu) / (2 * dx);
		double pm = sigma2 / (dx * dx) + r;
		setMidRows(pd, pm, pu);
	}

	public BSMOperator(List grid, GeneralizedBlackScholesProcess process,
			double residualTime) {
		super(grid.size());
		/*
		 * PdeBSM::grid_type logGrid(grid); PdeConstantCoeff<PdeBSM>
		 * cc(process, residualTime, process->stateVariable()->value());
		 * cc.generateOperator(residualTime, logGrid, *this);
		 */
	}
}
