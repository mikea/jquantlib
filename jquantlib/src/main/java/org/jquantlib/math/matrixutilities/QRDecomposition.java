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
public class QRDecomposition extends Matrix {

    private final static String MATRIX_IS_RANK_DEFICIENT = "Matrix is rank deficient";

    //
    // private fields
    //

    // Array for internal storage of diagonal of R.
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
        super(A);

        this.Rdiag = new double[cols];

        // Main loop.
        for (int k = 0; k < cols; k++) {
            // Compute 2-norm of k-th column without under/overflow.
            double nrm = 0;
            for (int i = k; i < rows; i++) {
                nrm = hypot(nrm, this.data[this.address(i, k)]);
            }

            if (nrm != 0.0) {
                // Form k-th Householder vector.
                if (this.data[this.address(k, k)] < 0) {
                    nrm = -nrm;
                }
                for (int i = k; i < rows; i++) {
                    this.data[this.address(i, k)] /= nrm;
                }
                this.data[this.address(k, k)] += 1.0;

                // Apply transformation to remaining columns.
                for (int j = k + 1; j < cols; j++) {
                    double s = 0.0;
                    for (int i = k; i < rows; i++) {
                        s += this.data[this.address(i, k)] * this.data[this.address(i, j)];
                    }
                    s = -s / this.data[this.address(k, k)];
                    for (int i = k; i < rows; i++) {
                        this.data[this.address(i, j)] += s * this.data[this.address(i, k)];
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
        for (int j = 0; j < cols; j++) {
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
    public Matrix getH() {
        final Matrix H = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i >= j) {
                    H.data[H.address(i, j)] = this.data[this.address(i, j)];
//XXX - not needed
//                } else {
//                    H.data[H.address(i, j)] = 0.0;
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
    public Matrix getR() {
        final Matrix R = new Matrix(cols, cols);
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < cols; j++) {
                if (i < j) {
                    R.data[R.address(i, j)] = this.data[this.address(i, j)];
                } else if (i == j) {
                    R.data[R.address(i, j)] = Rdiag[i];
                } else {
                    R.data[R.address(i, j)] = 0.0;
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
    public Matrix getQ() {
        final Matrix Q = new Matrix(rows, cols);
        for (int k = cols - 1; k >= 0; k--) {
            for (int i = 0; i < rows; i++) {
                Q.data[Q.address(i, k)] = 0.0;
            }
            Q.data[Q.address(k, k)] = 1.0;
            for (int j = k; j < cols; j++) {
                if (this.data[this.address(k, k)] != 0) {
                    double s = 0.0;
                    for (int i = k; i < rows; i++) {
                        s += this.data[this.address(i, k)] * Q.data[Q.address(i, j)];
                    }
                    s = -s / this.data[this.address(k, k)];
                    for (int i = k; i < rows; i++) {
                        Q.data[Q.address(i, j)] += s * this.data[this.address(i, k)];
                    }
                }
            }
        }
        return Q;
    }

    /**
     * Least squares solution of A*X = B
     *
     * @param B a Matrix with as many rows as A and any number of columns.
     * @return X that minimizes the two norm of Q*R*X-B.
     * @exception IllegalArgumentException Matrix row dimensions must agree.
     * @exception RuntimeException Matrix is rank deficient.
     */
    @Override
    public Matrix solve(final Matrix B) {
        QL.require(B.rows == this.rows, MATRIX_IS_INCOMPATIBLE);
        if (!this.isFullRank())
            throw new RuntimeException(MATRIX_IS_RANK_DEFICIENT);

        // Copy right hand side
        final int nx = B.cols;
        final Matrix X = B.clone();

        // Compute Y = transpose(Q)*B
        for (int k = 0; k < cols; k++) {
            for (int j = 0; j < nx; j++) {
                double s = 0.0;
                for (int i = k; i < rows; i++) {
                    s += this.data[this.address(i, k)] * X.data[X.address(i, j)];
                }
                s = -s / this.data[this.address(k, k)];
                for (int i = k; i < rows; i++) {
                    X.data[X.address(i, j)] += s * this.data[this.address(i, k)];
                }
            }
        }
        // Solve R*X = Y;
        for (int k = cols - 1; k >= 0; k--) {
            for (int j = 0; j < nx; j++) {
                X.data[X.address(k, j)] /= Rdiag[k];
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < nx; j++) {
                    X.data[X.address(i, j)] -= X.data[X.address(k, j)] * this.data[this.address(i, k)];
                }
            }
        }
        return X;
    }

}
