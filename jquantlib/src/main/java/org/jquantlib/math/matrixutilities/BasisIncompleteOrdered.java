/*

Copyright (C) 2008 Q.Boiler

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
Copyright (C) 2007 Mark Joshi

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
package org.jquantlib.math.matrixutilities;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;

/**
 * @author M. Do
 * @author Q. Boiler
 */

public class BasisIncompleteOrdered {

	private final int euclideanDimension;
	private final List<double[]> currentBasis;
	
	public BasisIncompleteOrdered(final int euclideanDimension) {
	    this.euclideanDimension = euclideanDimension;
	    this.currentBasis = new ObjectArrayList<double[]>();
	}
	
	public boolean addVector(final Array newVector1) {
		if (newVector1.size() != euclideanDimension) {
			throw new RuntimeException("BasisIncompleteOrdered : missized vector passed");
		}

		if (currentBasis.size() == euclideanDimension) {
			return false;
		}
		for (int j = 0; j < currentBasis.size(); ++j) {

			double innerProd = Array.dotProduct(newVector1.getData(),
				                                currentBasis.get(j));

			double[] data = newVector1.getData();

			double[] currentBasisAtJ = currentBasis.get(j);
			for (int k = 0; k < euclideanDimension; ++k) {
				data[k] -= innerProd * currentBasisAtJ[k];
			}
		}

		double norm = Math.sqrt(Array.dotProduct(newVector1.getData(),
			                                          newVector1.getData()));

		if (norm < 1e-12) // maybe this should be a tolerance
		{
			return false;
		}
		double[] data = newVector1.getData();
		for (int l = 0; l < euclideanDimension; ++l) {
			data[l] /= norm;
		}
		currentBasis.add(newVector1.getData());

		return true;
	}

	public int basisSize() {
		return currentBasis.size();
	}

	public int euclideanDimension() {
		return euclideanDimension;
	}

	public Matrix getBasisAsRowsInMatrix() {
		Matrix basis = new Matrix(currentBasis.size(), euclideanDimension);
		for (int i = 0; i < basis.rows(); ++i) {
			double[] currentBasisRow = currentBasis.get(i);
			for (int j = 0; j < basis.columns(); ++j) {
				basis.set(i, j, currentBasisRow[j]);
			}
		}
		return basis;
	}
}
