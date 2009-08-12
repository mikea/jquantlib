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

import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;

/**
 * Singular Value Decomposition
 * <P>
 * For an m-by-n matrix A with m >= n, the singular value decomposition is an m-by-n orthogonal matrix U, an n-by-n diagonal matrix
 * S, and an n-by-n orthogonal matrix V so that A = U*S*V'.
 * <P>
 * The singular values, sigma[k] = S[k][k], are ordered so that sigma[0] >= sigma[1] >= ... >= sigma[n-1].
 * <P>
 * The singular value decompostion always exists, so the constructor will never fail. The matrix condition number and the effective
 * numerical rank can be computed from this decomposition.
 *
 * @Note: This class was adapted from JAMA
 * @see <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a>
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q1_TRANSLATION, version = Version.OTHER, reviewers = { "Richard Gomes" })
public class SingularValueDecomposition extends Matrix {

    /** Arrays for internal storage of U and V. */
    private final Matrix U, V;

    /** Array for internal storage of singular values. */
    private final double[] s;

    //
    // public constructors
    //

    /**
     * Construct the singular value decomposition
     *
     * @param A is a rectangular matrix
     * @return Structure to access U, S and V.
     */
    public SingularValueDecomposition(final Matrix A) {
        super(A);

        /*
         * THESE COMMENTS CAME FROM JAMA:
         *
         * Apparently the failing cases are only a proper subset of (m<n), so let's not throw error.
         * Correct fix to come later? if (m<n) { throw new IllegalArgumentException("Jama SVD only works for m >= n"); }
         */
        final int nu = Math.min(rows, cols);
        s = new double[Math.min(cols + 1, rows)];
        U = new Matrix(rows, nu);
        V = new Matrix(cols, cols);
        final double[] e = new double[cols];
        final double[] work = new double[rows];
        final boolean wantu = true;
        final boolean wantv = true;

        // Reduce A to bidiagonal form, storing the diagonal elements
        // in s and the super-diagonal elements in e.

        final int nct = Math.min(rows - 1, cols);
        final int nrt = Math.max(0, Math.min(cols - 2, rows));
        for (int k = 0; k < Math.max(nct, nrt); k++) {
            if (k < nct) {

                // Compute the transformation for the k-th column and
                // place the k-th diagonal in s[k].
                // Compute 2-norm of k-th column without under/overflow.
                s[k] = 0;
                for (int i = k; i < rows; i++) {
                    s[k] = hypot(s[k], this.data[this.address(i, k)]);
                }
                if (s[k] != 0.0) {
                    if (this.data[this.address(k, k)] < 0.0) {
                        s[k] = -s[k];
                    }
                    for (int i = k; i < rows; i++) {
                        this.data[this.address(i, k)] /= s[k];
                    }
                    this.data[this.address(k, k)] += 1.0;
                }
                s[k] = -s[k];
            }
            for (int j = k + 1; j < cols; j++) {
                if ((k < nct) & (s[k] != 0.0)) {

                    // Apply the transformation.

                    double t = 0;
                    for (int i = k; i < rows; i++) {
                        t += this.data[this.address(i, k)] * this.data[this.address(i, j)];
                    }
                    t = -t / this.data[this.address(k, k)];
                    for (int i = k; i < rows; i++) {
                        this.data[this.address(i, j)] += t * this.data[this.address(i, k)];
                    }
                }

                // Place the k-th row of A into e for the
                // subsequent calculation of the row transformation.

                e[j] = this.data[this.address(k, j)];
            }
            if (wantu & (k < nct)) {

                // Place the transformation in U for subsequent back
                // multiplication.

                for (int i = k; i < rows; i++) {
                    U.data[U.address(i, k)] = this.data[this.address(i, k)];
                }
            }
            if (k < nrt) {

                // Compute the k-th row transformation and place the
                // k-th super-diagonal in e[k].
                // Compute 2-norm without under/overflow.
                e[k] = 0;
                for (int i = k + 1; i < cols; i++) {
                    e[k] = hypot(e[k], e[i]);
                }
                if (e[k] != 0.0) {
                    if (e[k + 1] < 0.0) {
                        e[k] = -e[k];
                    }
                    for (int i = k + 1; i < cols; i++) {
                        e[i] /= e[k];
                    }
                    e[k + 1] += 1.0;
                }
                e[k] = -e[k];
                if ((k + 1 < rows) & (e[k] != 0.0)) {

                    // Apply the transformation.

                    for (int i = k + 1; i < rows; i++) {
                        work[i] = 0.0;
                    }
                    for (int j = k + 1; j < cols; j++) {
                        for (int i = k + 1; i < rows; i++) {
                            work[i] += e[j] * this.data[this.address(i, j)];
                        }
                    }
                    for (int j = k + 1; j < cols; j++) {
                        final double t = -e[j] / e[k + 1];
                        for (int i = k + 1; i < rows; i++) {
                            this.data[this.address(i, j)] += t * work[i];
                        }
                    }
                }
                if (wantv) {

                    // Place the transformation in V for subsequent
                    // back multiplication.

                    for (int i = k + 1; i < cols; i++) {
                        V.data[V.address(i, k)] = e[i];
                    }
                }
            }
        }

        // Set up the final bidiagonal matrix or order p.

        int p = Math.min(cols, rows + 1);
        if (nct < cols) {
            s[nct] = this.data[this.address(nct, nct)];
        }
        if (rows < p) {
            s[p - 1] = 0.0;
        }
        if (nrt + 1 < p) {
            e[nrt] = this.data[this.address(nrt, p - 1)];
        }
        e[p - 1] = 0.0;

        // If required, generate U.

        if (wantu) {
            for (int j = nct; j < nu; j++) {
                for (int i = 0; i < rows; i++) {
                    U.data[U.address(i, j)] = 0.0;
                }
                U.data[U.address(j, j)] = 1.0;
            }
            for (int k = nct - 1; k >= 0; k--) {
                if (s[k] != 0.0) {
                    for (int j = k + 1; j < nu; j++) {
                        double t = 0;
                        for (int i = k; i < rows; i++) {
                            t += U.data[U.address(i, k)] * U.data[U.address(i, j)];
                        }
                        t = -t / U.data[U.address(k, k)];
                        for (int i = k; i < rows; i++) {
                            U.data[U.address(i, j)] += t * U.data[U.address(i, k)];
                        }
                    }
                    for (int i = k; i < rows; i++) {
                        U.data[U.address(i, k)] = -U.data[U.address(i, k)];
                    }
                    U.data[U.address(k, k)] = 1.0 + U.data[U.address(k, k)];
                    for (int i = 0; i < k - 1; i++) {
                        U.data[U.address(i, k)] = 0.0;
                    }
                } else {
                    for (int i = 0; i < rows; i++) {
                        U.data[U.address(i, k)] = 0.0;
                    }
                    U.data[U.address(k, k)] = 1.0;
                }
            }
        }

        // If required, generate V.

        if (wantv) {
            for (int k = cols - 1; k >= 0; k--) {
                if ((k < nrt) & (e[k] != 0.0)) {
                    for (int j = k + 1; j < nu; j++) {
                        double t = 0;
                        for (int i = k + 1; i < cols; i++) {
                            t += V.data[V.address(i, k)] * V.data[V.address(i, j)];
                        }
                        t = -t / V.data[V.address(k + 1, k)];
                        for (int i = k + 1; i < cols; i++) {
                            V.data[V.address(i, j)] += t * V.data[V.address(i, k)];
                        }
                    }
                }
                for (int i = 0; i < cols; i++) {
                    V.data[V.address(i, k)] = 0.0;
                }
                V.data[V.address(k, k)] = 1.0;
            }
        }

        // Main iteration loop for the singular values.

        final int pp = p - 1;
        int iter = 0;
        final double eps = Math.pow(2.0, -52.0);
        final double tiny = Math.pow(2.0, -966.0);
        while (p > 0) {
            int k, kase;

            // Here is where a test for too many iterations would go.

            // This section of the program inspects for
            // negligible elements in the s and e arrays. On
            // completion the variables kase and k are set as follows.

            // kase = 1 if s(p) and e[k-1] are negligible and k<p
            // kase = 2 if s(k) is negligible and k<p
            // kase = 3 if e[k-1] is negligible, k<p, and
            // s(k), ..., s(p) are not negligible (qr step).
            // kase = 4 if e(p-1) is negligible (convergence).

            for (k = p - 2; k >= -1; k--) {
                if (k == -1) {
                    break;
                }
                if (Math.abs(e[k]) <= tiny + eps * (Math.abs(s[k]) + Math.abs(s[k + 1]))) {
                    e[k] = 0.0;
                    break;
                }
            }
            if (k == p - 2) {
                kase = 4;
            } else {
                int ks;
                for (ks = p - 1; ks >= k; ks--) {
                    if (ks == k) {
                        break;
                    }
                    final double t = (ks != p ? Math.abs(e[ks]) : 0.) + (ks != k + 1 ? Math.abs(e[ks - 1]) : 0.);
                    if (Math.abs(s[ks]) <= tiny + eps * t) {
                        s[ks] = 0.0;
                        break;
                    }
                }
                if (ks == k) {
                    kase = 3;
                } else if (ks == p - 1) {
                    kase = 1;
                } else {
                    kase = 2;
                    k = ks;
                }
            }
            k++;

            // Perform the task indicated by kase.

            switch (kase) {

            // Deflate negligible s(p).

            case 1:
                {
                    double f = e[p - 2];
                    e[p - 2] = 0.0;
                    for (int j = p - 2; j >= k; j--) {
                        double t = hypot(s[j], f);
                        final double cs = s[j] / t;
                        final double sn = f / t;
                        s[j] = t;
                        if (j != k) {
                            f = -sn * e[j - 1];
                            e[j - 1] = cs * e[j - 1];
                        }
                        if (wantv) {
                            for (int i = 0; i < cols; i++) {
                                t = cs * V.data[V.address(i, j)] + sn * V.data[V.address(i, p - 1)];
                                V.data[V.address(i, p - 1)] = -sn * V.data[V.address(i, j)] + cs * V.data[V.address(i, p - 1)];
                                V.data[V.address(i, j)] = t;
                            }
                        }
                    }
                }
                break;

            // Split at negligible s(k).

            case 2:
                {
                    double f = e[k - 1];
                    e[k - 1] = 0.0;
                    for (int j = k; j < p; j++) {
                        double t = hypot(s[j], f);
                        final double cs = s[j] / t;
                        final double sn = f / t;
                        s[j] = t;
                        f = -sn * e[j];
                        e[j] = cs * e[j];
                        if (wantu) {
                            for (int i = 0; i < rows; i++) {
                                t = cs * U.data[U.address(i, j)] + sn * U.data[U.address(i, k - 1)];
                                U.data[U.address(i, k - 1)] = -sn * U.data[U.address(i, j)] + cs * U.data[U.address(i, k - 1)];
                                U.data[U.address(i, j)] = t;
                            }
                        }
                    }
                }
                break;

            // Perform one qr step.

            case 3:
                {
                    // Calculate the shift.
                    final double scale = Math.max(Math.max(Math.max(Math.max(Math.abs(s[p - 1]), Math.abs(s[p - 2])), Math
                            .abs(e[p - 2])), Math.abs(s[k])), Math.abs(e[k]));
                    final double sp = s[p - 1] / scale;
                    final double spm1 = s[p - 2] / scale;
                    final double epm1 = e[p - 2] / scale;
                    final double sk = s[k] / scale;
                    final double ek = e[k] / scale;
                    final double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
                    final double c = (sp * epm1) * (sp * epm1);
                    double shift = 0.0;
                    if ((b != 0.0) | (c != 0.0)) {
                        shift = Math.sqrt(b * b + c);
                        if (b < 0.0) {
                            shift = -shift;
                        }
                        shift = c / (b + shift);
                    }
                    double f = (sk + sp) * (sk - sp) + shift;
                    double g = sk * ek;

                    // Chase zeros.

                    for (int j = k; j < p - 1; j++) {
                        double t = hypot(f, g);
                        double cs = f / t;
                        double sn = g / t;
                        if (j != k) {
                            e[j - 1] = t;
                        }
                        f = cs * s[j] + sn * e[j];
                        e[j] = cs * e[j] - sn * s[j];
                        g = sn * s[j + 1];
                        s[j + 1] = cs * s[j + 1];
                        if (wantv) {
                            for (int i = 0; i < cols; i++) {
                                t = cs * V.data[V.address(i, j)] + sn * V.data[V.address(i, j + 1)];
                                V.data[V.address(i, j + 1)] = -sn * V.data[V.address(i, j)] + cs * V.data[V.address(i, j + 1)];
                                V.data[V.address(i, j)] = t;
                            }
                        }
                        t = hypot(f, g);
                        cs = f / t;
                        sn = g / t;
                        s[j] = t;
                        f = cs * e[j] + sn * s[j + 1];
                        s[j + 1] = -sn * e[j] + cs * s[j + 1];
                        g = sn * e[j + 1];
                        e[j + 1] = cs * e[j + 1];
                        if (wantu && (j < rows - 1)) {
                            for (int i = 0; i < rows; i++) {
                                t = cs * U.data[U.address(i, j)] + sn * U.data[U.address(i, j + 1)];
                                U.data[U.address(i, j + 1)] = -sn * U.data[U.address(i, j)] + cs * U.data[U.address(i, j + 1)];
                                U.data[U.address(i, j)] = t;
                            }
                        }
                    }
                    e[p - 2] = f;
                    iter = iter + 1;
                }
                break;

            // Convergence.

            case 4:
                {
                    // Make the singular values positive.
                    if (s[k] <= 0.0) {
                        s[k] = (s[k] < 0.0 ? -s[k] : 0.0);
                        if (wantv) {
                            for (int i = 0; i <= pp; i++) {
                                V.data[V.address(i, k)] = -V.data[V.address(i, k)];
                            }
                        }
                    }

                    // Order the singular values.

                    while (k < pp) {
                        if (s[k] >= s[k + 1]) {
                            break;
                        }
                        double t = s[k];
                        s[k] = s[k + 1];
                        s[k + 1] = t;
                        if (wantv && (k < cols - 1)) {
                            for (int i = 0; i < cols; i++) {
                                t = V.data[V.address(i, k + 1)];
                                V.data[V.address(i, k + 1)] = V.data[V.address(i, k)];
                                V.data[V.address(i, k)] = t;
                            }
                        }
                        if (wantu && (k < rows - 1)) {
                            for (int i = 0; i < rows; i++) {
                                t = U.data[U.address(i, k + 1)];
                                U.data[U.address(i, k + 1)] = U.data[U.address(i, k)];
                                U.data[U.address(i, k)] = t;
                            }
                        }
                        k++;
                    }
                    iter = 0;
                    p--;
                }
                break;
            }
        }
    }

    //
    // public methods
    //

    /**
     * Return the left singular vectors
     *
     * @return U
     */
    public Matrix getU() {
        return U.clone(); // new Matrix(U,m,Math.min(m+1,n));
    }

    /**
     * Return the right singular vectors
     *
     * @return V
     */
    public Matrix getV() {
        return V.clone();
    }

    /**
     * Return the one-dimensional array of singular values
     *
     * @return diagonal of S.
     */
    public double[] getSingularValues() {
        return s;
    }

    /**
     * Return the diagonal matrix of singular values
     *
     * @return S
     */
    public Matrix getS() {
        final Matrix S = new Matrix(this.rows, this.rows);
        for (int i = 0; i < rows; i++) {
            S.data[S.address(i, i)] = this.s[i];
        }
        return S;
    }

    /**
     * Two norm
     *
     * @return max(S)
     */
    public double norm2() {
        return s[0];
    }

    /**
     * Two norm condition number
     *
     * @return max(S)/min(S)
     */
    public double cond() {
        return s[0] / s[Math.min(rows, cols) - 1];
    }

    /**
     * Effective numerical matrix rank
     *
     * @return Number of non-negligible singular values.
     */
    public int rank() {
        final double eps = Math.pow(2.0, -52.0);
        final double tol = Math.max(this.rows, this.cols) * s[0] * eps;
        int r = 0;
        for (final double element : s) {
            if (element > tol) {
                r++;
            }
        }
        return r;
    }

}
