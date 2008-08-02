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
 Copyright (C) 2003, 2004 Ferdinando Ametrano

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

import org.jquantlib.math.Matrix;

/**
 *  Performs a CholeskyDecomposition.
 * @author Q.Boiler
 */
public class CholeskyDecomposition {

    //
    //   These are used so that we don't have to throw exceptions.
    //   it is much more efficient to avoid throwing acceptions,
    //   and use other flow control mechanisms to get out of various 
    //   situations.
    private boolean error = false;
    private String errorMessage = null;
    
    
    

    Matrix CholeskyDecomposition(final Matrix S, boolean flexible) {

        int i, j, size = S.rows();

        //  Validate Matrix.
        //  Technically the CholeskyDecomposition works for
        //  "For a symmetric, positive definite matrix A."  
        //     - Advanced Engineering  Mathematics
        //       by Erwin Kreyszig.

        validateMatrix(S);


        //  Create a new Matrix where the result of the Decomp will go.
        //  As it turns out this is not effient from a memory perspective,
        //  because you really only need the lower half of the de-comp.
        //
        //  As a second note, the reason for using a factory is so that we 
        //  can pool matrix instances.  We will not currently do that because 
        //  we need a consistant pooling mechanism.
        // 
        // Matrix result = MatrixFactory.getMatrix(size,size,0.0d);
         Matrix result = new Matrix(size,size,0.0d);

        //Matrix result(size,size,0.0);
        
        double sum;

        for (i=0; i < size; i++) {
            for (j = i; j < size; j++) {
                sum = S.get(i, j);
                for (int k = 0; k <= i - 1; k++) {
                    sum -= result.get(i, k) * result.get(j, k);
                }
                if (i == j) {
                    //  validatePositiveDefinate
                    if(sum<=0.0d){
                        //   Set the error.
                        //   Break.
                        error = true;
                        StringBuffer sb = new StringBuffer("Validate Positive Definate Failed with :")
                                .append(sum).append(" <= 0.0 for ")
                                .append(i).append(",").append(j);
                        
                        errorMessage = sb.toString();
                        break;
                    }
                    // To handle positive semi-definite matrices take the
                    // square root of sum if positive, else zero.
                    result.set(i,i, Math.sqrt(Math.max(sum, 0.0)));

                } else {
                    // With positive semi-definite matrices is possible
                    // to have result[i][i]==0.0
                    // In this case sum happens to be zero as well
                    //result[j][i] =
                     //   (sum==0.0 ? 0.0 : sum / result.get(i,i));
                    result.set(j,i, (sum==0.0 ? 0.0 : sum / result.get(i,i)));
                }
            }
        }
        return result;
    }

    private boolean validateMatrix(Matrix S) {
	    if(S.columns()!=S.rows()){
		    throw new RuntimeException("input matrix is not square.");
	    }
	    return true;
        //  If the matrix is fine, return true.
        //  else set Error flag.

//        QL_REQUIRE(size == S.columns(),
//                   "input matrix is not a square matrix");
//        #if defined(QL_EXTRA_SAFETY_CHECKS)
//        for (i=0; i<S.rows(); i++)
//            for (j=0; j<i; j++)
//                QL_REQUIRE(S[i][j] == S[j][i],
//                           "input matrix is not symmetric");
//        #endif
        
    }
}
//
///*
// Copyright (C) 2003, 2004 Ferdinando Ametrano
//
// This file is part of QuantLib, a free-software/open-source library
// for financial quantitative analysts and developers - http://quantlib.org/
//
// QuantLib is free software: you can redistribute it and/or modify it
// under the terms of the QuantLib license.  You should have received a
// copy of the license along with this program; if not, please email
// <quantlib-dev@lists.sf.net>. The license is also available online at
// <http://quantlib.org/license.shtml>.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// FOR A PARTICULAR PURPOSE.  See the license for more details.
//*/
//
//#include <ql/math/matrixutilities/choleskydecomposition.hpp>
//
//namespace QuantLib {
//
//   const Disposable<Matrix> CholeskyDecomposition(const Matrix &S,
//                                                   bool flexible) {
//        Size i, j, size = S.rows();
//
//        QL_REQUIRE(size == S.columns(),
//                   "input matrix is not a square matrix");
//        #if defined(QL_EXTRA_SAFETY_CHECKS)
//        for (i=0; i<S.rows(); i++)
//            for (j=0; j<i; j++)
//                QL_REQUIRE(S[i][j] == S[j][i],
//                           "input matrix is not symmetric");
//        #endif
//
//        Matrix result(size, size, 0.0);
//        Real sum;
//        for (i=0; i<size; i++) {
//            for (j=i; j<size; j++) {
//                sum = S[i][j];
//                for (Integer k=0; k<=Integer(i)-1; k++) {
//                    sum -= result[i][k]*result[j][k];
//                }
//                if (i == j) {
//                    QL_REQUIRE(flexible || sum > 0.0,
//                               "input matrix is not positive definite");
//                    // To handle positive semi-definite matrices take the
//                    // square root of sum if positive, else zero.
//                    result[i][i] = std::sqrt(std::max<Real>(sum, 0.0));
//                } else {
//                    // With positive semi-definite matrices is possible
//                    // to have result[i][i]==0.0
//                    // In this case sum happens to be zero as well
//                    result[j][i] =
//                        (sum==0.0 ? 0.0 : sum/result[i][i]);
//                }
//            }
//        }
//        return result;
//    }

//}
