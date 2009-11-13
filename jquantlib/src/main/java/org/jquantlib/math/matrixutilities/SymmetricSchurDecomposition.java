/*
 Copyright (C) 2009 Richard Gomes

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

import java.util.SortedMap;
import java.util.TreeMap;

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.math.matrixutilities.Cells.ConstColumnIterator;

/**
 * Symmetric threshold Jacobi algorithm
 * <p>
 * Given a real symmetric matrix S, the Schur decomposition finds the eigenvalues and eigenvectors of S. If D is the diagonal matrix
 * formed by the eigenvalues and U the unitarian matrix of the eigenvectors we can write the Schur decomposition as {@latex[ S = U
 * \cdot D \cdot U^T } where {@latex$ \cdot } is the standard matrix product and {@latex$ ^T } is the transpose operator.
 * <p>
 * This class implements the Schur decomposition using the symmetric threshold Jacobi algorithm. For details on the different Jacobi
 * transfomations.
 *
 * @see "Matrix computation," second edition, by Golub and Van Loan, The Johns Hopkins University Press
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q0_UNFINISHED, version = Version.V097, reviewers = { "Richard Gomes" })
public class SymmetricSchurDecomposition {

    private static final double epsPrec = 1e-15;
    private static final int maxIterations = 100;

    private final int size;
    private final Matrix A;
    private final Array diag;


    public SymmetricSchurDecomposition(final Matrix m) {
        QL.require(m.rows == m.cols, Matrix.MATRIX_MUST_BE_SQUARE); // QA:[RG]::verified

        this.size = m.rows;
        this.A = new Matrix(m.rows, m.cols);
        this.diag = new Array(size);

        final double tmpDiag[] = new double[size];
        final double tmpSum[] = new double[size];

        final Matrix s = m.clone();

        for (int q = 0; q < size; q++) {
            diag.data[diag.addr(q)] = s.data[s.addr(q, q)];
            A.data[A.addr(q, q)] = 1.0;
        }
        for (int j = 0; j < size; j++) {
        	tmpDiag[j] = diag.data[diag.addr(j)];
        }

        boolean keeplooping = true;
        int ite = 1;
        double threshold;
        do {
            // main loop
            double sum = 0;
            for (int a = 0; a < size - 1; a++) {
                for (int b = a + 1; b < size; b++) {
                    sum += Math.abs(s.data[s.addr(a, b)]);
                }
            }

            if (sum == 0) {
                keeplooping = false;
            } else {
                /*
                 * To speed up computation a threshold is introduced to make sure it is worthy to perform the Jacobi rotation
                 */
                if (ite < 5)
                    threshold = 0.2 * sum / (size * size);
                else
                    threshold = 0.0;

                int j, k, l;
                for (j = 0; j < size - 1; j++) {
                    for (k = j + 1; k < size; k++) {
                        double sine, rho, cosin, heig, tang, beta;
                        final double smll = Math.abs(s.data[s.addr(j, k)]);
                        if (ite > 5 && smll < epsPrec * Math.abs(diag.data[diag.addr(j)])
                                && smll < epsPrec * Math.abs(diag.data[diag.addr(k)])) {
                            s.data[s.addr(j, k)] = 0;
                        } else if (Math.abs(s.data[s.addr(j, k)]) > threshold) {
                            heig = diag.data[diag.addr(k)] - diag.data[diag.addr(j)];
                            if (smll < epsPrec * Math.abs(heig)) {
                                tang = s.data[s.addr(j, k)] / heig;
                            } else {
                                beta = 0.5 * heig / s.data[s.addr(j, k)];
                                tang = 1.0 / (Math.abs(beta) + Math.sqrt(1 + beta * beta));
                                if (beta < 0)
                                    tang = -tang;
                            }
                            cosin = 1 / Math.sqrt(1 + tang * tang);
                            sine = tang * cosin;
                            rho = sine / (1 + cosin);
                            heig = tang * s.data[s.addr(j, k)];
                            tmpSum[j] -= heig;
                            tmpSum[k] += heig;
                            diag.data[diag.addr(j)] -= heig;
                            diag.data[diag.addr(k)] += heig;
                            s.data[s.addr(j, k)] = 0.0;
                            for (l = 0; l + 1 <= j; l++)
                                jacobiRotate(s, rho, sine, l, j, l, k);
                            for (l = j + 1; l <= k - 1; l++)
                                jacobiRotate(s, rho, sine, j, l, l, k);
                            for (l = k + 1; l < size; l++)
                                jacobiRotate(s, rho, sine, j, l, k, l);
                            for (l = 0; l < size; l++)
                                jacobiRotate(A, rho, sine, l, j, l, k);
                        }
                    }
                }
                for (k = 0; k < size; k++) {
                    tmpDiag[k] += tmpSum[k];
                    diag.data[diag.addr(k)] = tmpDiag[k];
                    tmpSum[k] = 0.0;
                }
            }
        } while (++ite <= maxIterations && keeplooping);

        QL.ensure(ite <= maxIterations, "Too many iterations reached");
        /*
        // sort (eigenvalues, eigenvectors)
        final SortedMap<Double, ConstColumnIterator> map = new TreeMap<Double, ConstColumnIterator>();
        for (int col = 0; col < size; col++) {
            final ConstColumnIterator eigenVector = A.constColumnIterator(col);
            map.put(diag.data[diag.addr(col)], eigenVector);
        }

        final int col = 0;
        final double maxEv = map.firstKey();
        for (final Double key : map.keySet()) {
            final ConstColumnIterator eigenVector = map.get(key);
            diag.data[diag.addr(col)] = Math.abs(key / maxEv) < 1e-16 ? 0.0 : key;
            double sign = 1.0;
            if (eigenVector.get(0) < 0.0)
                sign = -1.0;
            for (int row = 0; row < size; row++) {
                A.data[A.addr(row, col)] = sign * eigenVector.get(row);
            }
        }
        */
    }

    //
    // public methods
    //

    public Matrix eigenvectors() {
        return A.clone();
    }

    public Array eigenvalues() {
        return diag.clone();
    }

    //
    // private methods
    //

    /**
     * This routines implements the Jacobi, a.k.a. Givens, rotation
     */
    private void jacobiRotate(final Matrix m, final double rot, final double dil, final int j1, final int k1, final int j2,
            final int k2) /* @ReadOnly */{
        double x1, x2;
        x1 = m.data[m.addr(j1, k1)];
        x2 = m.data[m.addr(j2, k2)];
        m.data[m.addr(j1, k1)] = x1 - dil * (x2 + x1 * rot);
        m.data[m.addr(j2, k2)] = x2 + dil * (x1 - x2 * rot);
    }

}
