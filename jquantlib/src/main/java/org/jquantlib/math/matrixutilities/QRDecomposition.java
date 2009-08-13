/*
Copyright (C) 2008 Richard Gomes

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
package org.jquantlib.math.matrixutilities;

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;

/**
 * QR Decomposition.
 * <P>
 * For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n orthogonal matrix Q and an n-by-n upper triangular matrix R
 * so that A = Q*R.
 * <P>
 * The QR decomposition always exists, even if the matrix does not have full rank, so the constructor will never fail. The primary
 * use of the QR decomposition is in the least squares solution of nonsquare systems of simultaneous linear equations. This will
 * fail if isFullRank() returns false.
 *
 * @Note: This class was adapted from JAMA
 * @see <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a>
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q1_TRANSLATION, version = Version.OTHER, reviewers = { "Richard Gomes" })
public class QRDecomposition {

    private final static String MATRIX_IS_RANK_DEFICIENT = "Matrix is rank deficient";

    //
    // private fields
    //

    private final int m;
    private final int n;
    private final Matrix QR;
    private final double[] Rdiag;

    //
    // public constructors
    //

    /**
     * QR Decomposition, computed by Householder reflections.
     *
     * @param A is a rectangular matrix
     * @return Structure to access R and the Householder vectors and compute Q.
     */
    public QRDecomposition(final Matrix A) {
        this.m = A.rows;
        this.n = A.cols;
        this.QR = A.clone();

        this.Rdiag = new double[this.n];

        // Main loop.
        for (int k = 0; k < this.n; k++) {
            // Compute 2-norm of k-th column without under/overflow.
            double nrm = 0;
            for (int i = k; i < this.m; i++) {
                nrm = Matrix.hypot(nrm, QR.data[QR.addr(i, k)]);
            }

            if (nrm != 0.0) {
                // Form k-th Householder vector.
                if (QR.data[QR.addr(k, k)] < 0) {
                    nrm = -nrm;
                }
                for (int i = k; i < this.m; i++) {
                    QR.data[QR.addr(i, k)] /= nrm;
                }
                QR.data[QR.addr(k, k)] += 1.0;

                // Apply transformation to remaining columns.
                for (int j = k + 1; j < this.n; j++) {
                    double s = 0.0;
                    for (int i = k; i < this.m; i++) {
                        s += QR.data[QR.addr(i, k)] * QR.data[QR.addr(i, j)];
                    }
                    s = -s / QR.data[QR.addr(k, k)];
                    for (int i = k; i < this.m; i++) {
                        QR.data[QR.addr(i, j)] += s * QR.data[QR.addr(i, k)];
                    }
                }
            }
            Rdiag[k] = -nrm;
        }

    }


    //
    // public methods
    //

    /**
     * Is the matrix full rank?
     *
     * @return true if R, and hence A, has full rank.
     */
    public boolean isFullRank() {
        for (int j = 0; j < this.n; j++) {
            if (Rdiag[j] == 0)
                return false;
        }
        return true;
    }

    /**
     * Return the Householder vectors
     *
     * @return Lower trapezoidal matrix whose columns define the reflections
     */
    public Matrix H() {
        final Matrix H = new Matrix(this.m, this.n);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                if (i >= j) {
                    H.data[H.addr(i, j)] = QR.data[QR.addr(i, j)];
//XXX - not needed
//                } else {
//                    H.data[H.addr(i, j)] = 0.0;
                }
            }
        }
        return H;
    }

    /**
     * Return the upper triangular factor
     *
     * @return R
     */
    public Matrix R() {
        final Matrix R = new Matrix(this.n, this.n);
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (i < j) {
                    R.data[R.addr(i, j)] = QR.data[QR.addr(i, j)];
                } else if (i == j) {
                    R.data[R.addr(i, j)] = Rdiag[i];
//XXX - not needed
//                } else {
//                    R.data[R.addr(i, j)] = 0.0;
                }
            }
        }
        return R;
    }

    /**
     * Generate and return the (economy-sized) orthogonal factor
     *
     * @return Q
     */
    public Matrix Q() {
        final Matrix Q = new Matrix(this.m, this.n);
        for (int k = n-1; k >= 0; k--) {
           for (int i = 0; i < m; i++) {
              Q.data[Q.addr(i,k)] = 0.0;
           }

           System.out.printf("m= %d    n=%d\n", m, n);

           Q.data[Q.addr(k,k)] = 1.0;
           for (int j = k; j < n; j++) {
              if (QR.data[QR.addr(k,k)] != 0) {
                 double s = 0.0;
                 for (int i = k; i < m; i++) {
                    s += QR.data[QR.addr(i,k)] * Q.data[Q.addr(i,j)];
                 }
                 s = -s/QR.data[QR.addr(k,k)];
                 for (int i = k; i < m; i++) {
                    Q.data[Q.addr(i,j)] += s * QR.data[QR.addr(i,k)];
                 }
              }
           }
        }
        return Q;
    }

    /**
     * Least squares solution of A*X = B
     *
     * @param B a Matrix with as many this.m as A and any number of columns.
     * @return X that minimizes the two norm of Q*R*X-B.
     * @exception IllegalArgumentException Matrix row dimensions must agree.
     * @exception RuntimeException Matrix is rank deficient.
     */
    public Matrix solve(final Matrix B) {
        QL.require(B.rows == this.m, Matrix.MATRIX_IS_INCOMPATIBLE);
        if (!this.isFullRank())
            throw new RuntimeException(MATRIX_IS_RANK_DEFICIENT);

        // Copy right hand side
        final int nx = B.cols;
        final Matrix X = B.clone();

        // Compute Y = transpose(Q)*B
        for (int k = 0; k < this.n; k++) {
            for (int j = 0; j < nx; j++) {
                double s = 0.0;
                for (int i = k; i < this.m; i++) {
                    s += QR.data[QR.addr(i, k)] * X.data[X.addr(i, j)];
                }
                s = -s / QR.data[QR.addr(k, k)];
                for (int i = k; i < this.m; i++) {
                    X.data[X.addr(i, j)] += s * QR.data[QR.addr(i, k)];
                }
            }
        }
        // Solve R*X = Y;
        for (int k = this.n - 1; k >= 0; k--) {
            for (int j = 0; j < nx; j++) {
                X.data[X.addr(k, j)] /= Rdiag[k];
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < nx; j++) {
                    X.data[X.addr(i, j)] -= X.data[X.addr(k, j)] * QR.data[QR.addr(i, k)];
                }
            }
        }
        return X;
    }

}
