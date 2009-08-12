/*
 Copyright (C)
 2009 Ueli Hofstetter

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl
 Copyright (C) 2004 Ferdinando Ametrano

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

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.util.Pair;

/**
 * Symmetric threshold Jacobi algorithm
 * <p>
 * Given a real symmetric matrix S, the Schur decomposition finds the eigenvalues and eigenvectors of S.
 * If D is the diagonal matrix formed by the eigenvalues and U the unitarian matrix of the
 * eigenvectors we can write the Schur decomposition as
 * {@latex[ S = U \cdot D \cdot U^T }
 * where {@latex$ \cdot } is the standard matrix product and {@latex$ ^T } is the transpose operator.
 * <p>
 * This class implements the Schur decomposition using the symmetric threshold Jacobi algorithm. For details on the different Jacobi
 * transfomations.
 *
 * @see "Matrix computation," second edition, by Golub and Van Loan, The Johns Hopkins University Press
 *
 * @author Ueli Hofstetter
 */
@QualityAssurance(quality = Quality.Q0_UNFINISHED, version = Version.V097, reviewers = { "Richard Gomes" })
public class SymmetricSchurDecomposition extends Matrix {

    private Array diagonal_;
    private Matrix eigenVectors_;

    public SymmetricSchurDecomposition(final Matrix s) {
        QL.require(s.rows == s.cols, MATRIX_MUST_BE_SQUARE);

        final int size = s.rows;
        for (int q=0; q<size; q++) {
            diagonal_.set(q, s.get(q,q));
            eigenVectors_.set(q, q, 1.0);
        }

        final Matrix ss = s;
        final Array tmpDiag = diagonal_.abs();
        final Array tmpAccumulate = new Array(size);

        double threshold;
        final double epsPrec = 1e-15;
        boolean keeplooping = true;
        final int maxIterations = 100;
        int ite = 1;
        do {
            //main loop
            double sum = 0;
            for (int a=0; a<size-1; a++) {
                for (int b=a+1; b<size; b++) {
                    sum += Math.abs(ss.get(a,b));
                }
            }

            if (sum==0) {
                keeplooping = false;
            } else {
                /* To speed up computation a threshold is introduced to
                   make sure it is worthy to perform the Jacobi rotation
                 */
                if (ite<5) {
                    threshold = 0.2*sum/(size*size);
                } else {
                    threshold = 0.0;
                }

                int j, k, l;
                for (j=0; j<size-1; j++) {
                    for (k=j+1; k<size; k++) {
                        double sine, rho, cosin, heig, tang, beta;
                        final double smll = Math.abs(ss.get(j, k));
                        if(ite> 5 &&
                                smll<epsPrec*Math.abs(diagonal_.get(j)) &&
                                smll<epsPrec*Math.abs(diagonal_.get(k))) {
                            ss.set(j, j, 0);
                        } else if (Math.abs(ss.get(j, k))>threshold) {
                            heig = diagonal_.get(k)-diagonal_.get(j);
                            if (smll<epsPrec*Math.abs(heig)) {
                                tang = ss.get(j, k)/heig;
                            } else {
                                beta = 0.5*heig/ss.get(j,k);
                                tang = 1.0/(Math.abs(beta)+
                                        Math.sqrt(1+beta*beta));
                                if (beta<0){
                                    tang = -tang;
                                }
                            }
                            cosin = 1/Math.sqrt(1+tang*tang);
                            sine = tang*cosin;
                            rho = sine/(1+cosin);
                            heig = tang*ss.get(j, k);
                            tmpAccumulate.set(j, tmpAccumulate.get(j) - heig);
                            tmpAccumulate.set(k, tmpAccumulate.get(k) + heig);
                            diagonal_.set(j, diagonal_.get(j)-heig);
                            diagonal_.set(k, diagonal_.get(k)+heig);
                            ss.set(j, k, 0.0);
                            for (l=0; l+1<=j; l++) {
                                jacobiRotate_(ss, rho, sine, l, j, l, k);
                            }
                            for (l=j+1; l<=k-1; l++) {
                                jacobiRotate_(ss, rho, sine, j, l, l, k);
                            }
                            for (l=k+1; l<size; l++) {
                                jacobiRotate_(ss, rho, sine, j, l, k, l);
                            }
                            for (l=0;   l<size; l++) {
                                jacobiRotate_(eigenVectors_,
                                        rho, sine, l, j, l, k);
                            }
                        }
                    }
                }
                for (k=0; k<size; k++) {
                    final double value = tmpDiag.get(k) + tmpAccumulate.get(k);
                    tmpDiag.set(k, value);
                    diagonal_.set(k,  value);
                    tmpAccumulate.set(k,0.0);
                }
            }
        } while (++ite<=maxIterations && keeplooping);

        if(ite>maxIterations)
            throw new IllegalArgumentException("Too many iterations reached");

        // sort (eigenvalues, eigenvectors)
        final List<Pair<Double, List<Double>>> temp = new ArrayList<Pair<Double, List<Double>>>(size);
        final List<Double> eigenVector = new ArrayList<Double>(size);
        int row, col;

        for (col=0; col<size; col++)
            throw new UnsupportedOperationException("work in progress");
        /*
            std::copy(eigenVectors_.column_begin(col),
                      eigenVectors_.column_end(col), eigenVector.begin());
            temp[col] = std::make_pair<Real, std::vector<Real> >(
                diagonal_[col], eigenVector);
         */
        /*
        std::sort(temp.begin(), temp.end(),
            std::greater<std::pair<Real, std::vector<Real> > >());*/
        final double maxEv = temp.get(0).getFirst();
        for (col=0; col<size; col++) {
            // check for round-off errors
            diagonal_.set(col, Math.abs(temp.get(col).getFirst()/maxEv)<1e-16 ? 0.0 :temp.get(col).getFirst());
            double sign = 1.0;
            if (temp.get(col).getSecond().get(0)<0.0){
                sign = -1.0;
            }
            for (row=0; row<size; row++) {
                eigenVectors_.set(row, col,  sign * temp.get(col).getSecond().get(row));
            }
        }
    }


    //
    // public methods
    //

    public Matrix eigenVectors(){
        return eigenVectors_;
    }

    public Array eigenvalues(){
        return diagonal_;
    }


    //
    // private methods
    //

    private void jacobiRotate_(final Matrix m, final double rot, final double dil, final int j1, final int k1, final int j2, final int k2){
        double x1, x2;
        x1 = m.get(j1, k1);
        x2 = m.get(j1, k2);
        m.set(j1, k1, x1 - dil*(x2 + x1*rot));
        m.set(j2, k2, x2 - dil*(x2 + x2*rot));
    }

}
