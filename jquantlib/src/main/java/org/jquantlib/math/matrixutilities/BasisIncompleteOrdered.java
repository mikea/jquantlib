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

import java.util.List;
import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;

/**
 *
 * @author Q. Boiler
 */
public class BasisIncompleteOrdered {

	private int euclideanDimension;
	private List<double[]> currentBasis = new java.util.ArrayList<double[]>();

	public boolean addVector(final Array newVector1) {
		if (newVector1.size() != euclideanDimension) {
			//  TODO  set the error condition.
			return true;
		//  Either Raise an exception, or set an Error Flag.
		//throw new RuntimeException("");
		}

		if (currentBasis.size() == euclideanDimension) {
			return false;
		}
		for (int j = 0; j < currentBasis.size(); ++j) {

			double innerProd = newVector1.dotProduct(
				newVector1.getData(),
				currentBasis.get(j));

			double[] data = newVector1.getData();

			double[] currentBasisAtJ = currentBasis.get(j);
			for (int k = 0; k < euclideanDimension; ++k) {
				data[k] -= innerProd * currentBasisAtJ[k];
			}
		}

		double norm = Math.sqrt(newVector1.dotProduct(newVector1.getData(),
			newVector1.getData()));
		//                newVector_.begin(),
		//                newVector_.end(),
		//                newVector_.begin(), 0.0));

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

	int basisSize() {
		return currentBasis.size();
	}

	int euclideanDimension() {
		return euclideanDimension;
	}

	Matrix getBasisAsRowsInMatrix() {
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

/*
namespace QuantLib {

BasisIncompleteOrdered::BasisIncompleteOrdered(Size euclideanDimension)
: euclideanDimension_(euclideanDimension) {}

bool BasisIncompleteOrdered::addVector(const Array& newVector1) {

QL_REQUIRE(newVector1.size() == euclideanDimension_,
"missized vector passed to "
"BasisIncompleteOrdered::addVector");

newVector_ = newVector1;

if (currentBasis_.size()==euclideanDimension_)
return false;

for (Size j=0; j<currentBasis_.size(); ++j) {
Real innerProd = std::inner_product(newVector_.begin(),
newVector_.end(),
currentBasis_[j].begin(), 0.0);

for (Size k=0; k<euclideanDimension_; ++k)
newVector_[k] -=innerProd*currentBasis_[j][k];
}

Real norm = sqrt(std::inner_product(newVector_.begin(),
newVector_.end(),
newVector_.begin(), 0.0));

if (norm<1e-12) // maybe this should be a tolerance
return false;

for (Size l=0; l<euclideanDimension_; ++l)
newVector_[l]/=norm;

currentBasis_.push_back(newVector_);

return true;
}

Size BasisIncompleteOrdered::basisSize() const {
return currentBasis_.size();
}

Size BasisIncompleteOrdered::euclideanDimension() const {
return euclideanDimension_;
}


Matrix BasisIncompleteOrdered::getBasisAsRowsInMatrix() const {
Matrix basis(currentBasis_.size(), euclideanDimension_);
for (Size i=0; i<basis.rows(); ++i)
for (Size j=0; j<basis.columns(); ++j)
basis[i][j] = currentBasis_[i][j];

return basis;
}

}
 */
