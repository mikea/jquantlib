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

/*
 Copyright (C) 2003 Neil Firth

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

    Adapted from the TNT project
    http://math.nist.gov/tnt/download.html

    This software was developed at the National Institute of Standards
    and Technology (NIST) by employees of the Federal Government in the
    course of their official duties. Pursuant to title 17 Section 105
    of the United States Code this software is not subject to copyright
    protection and is in the public domain. NIST assumes no responsibility
    whatsoever for its use by other parties, and makes no guarantees,
    expressed or implied, about its quality, reliability, or any other
    characteristic.

    We would appreciate acknowledgement if the software is incorporated in
    redistributable libraries or applications.

 */

package org.jquantlib.math.matrixutilities;



/**
 * Singular Value Decomposition
 * <p>
 * Refer to Golub and Van Loan: Matrix computation, The Johns Hopkins University Press
 * 
 * @author Richard Gomes
 */
public class SVD {

    private final Matrix A;
    private final Matrix U, V;
    private final Array s;
    private final boolean transpose;


    //
    // public constructor
    //

    /**
     * The implementation requires that rows >= columns.
     * If this is not the case, we decompose M^T instead.
     * Swapping the resulting U and V gives the desired result for M as
     * <p>
     * <pre>
     * M^T = U S V^T           (decomposition of M^T)
     * M = (U S V^T)^T         (transpose)
     * M = (V^T^T S^T U^T)     ((AB)^T = B^T A^T)
     * M = V S U^T             (idempotence of transposition, symmetry of diagonal matrix S)
     * </pre>
     */
    public SVD(final Matrix M) {
        if (M.rows >= M.cols) {
            A = M.clone();
            transpose = false;
        } else {
            A = M.transpose();
            transpose = true;
        }

        // we're sure that m_ >= A.cols

        s = new Array(A.cols);
        U = new Matrix(A.rows, A.cols);
        V = new Matrix(A.cols,A.cols);
        final Array e = new Array(A.cols);
        final Array work = new Array(A.rows);
        int i, j, k;

        // Reduce A to bidiagonal form, storing the diagonal elements
        // in s and the super-diagonal elements in e.

        final int nct = Math.min(A.rows-1,A.cols);
        final int nrt = Math.max(0,A.cols-2);
        for (k = 0; k < Math.max(nct,nrt); k++) {
            if (k < nct) {

                // Compute the transformation for the k-th column and
                // place the k-th diagonal in s[k].
                // Compute 2-norm of k-th column without under/overflow.

                s.data[k] = 0;
                for (i = k; i < A.rows; i++) {
                    s.data[k] = hypot(s.data[k], A.data[A.address(i,k)]);
                }
                if (s.data[k] != 0.0) {
                    if (A.data[A.address(k,k)] < 0.0) {
                        s.data[k] = -s.data[k];
                    }
                    for (i = k; i < A.rows; i++) {
                        A.data[A.address(i,k)] /= s.data[k];
                    }
                    A.data[A.address(k,k)] += 1.0;
                }
                s.data[k] = -s.data[k];
            }
            for (j = k+1; j < A.cols; j++) {
                if ((k < nct) && (s.data[k] != 0.0))  {

                    // Apply the transformation.

                    double t = 0;
                    for (i = k; i < A.rows; i++) {
                        t += A.data[A.address(i,k)]*A.data[A.address(i,j)];
                    }
                    t = -t/A.data[A.address(k,k)];
                    for (i = k; i < A.rows; i++) {
                        A.data[A.address(i,j)] += t*A.data[A.address(i,k)];
                    }
                }

                // Place the k-th row of A into e for the
                // subsequent calculation of the row transformation.

                e.data[j] = A.data[A.address(k,j)];
            }
            if (k < nct) {

                // Place the transformation in U for subsequent back
                // multiplication.

                for (i = k; i < A.rows; i++) {
                    U.data[U.address(i,k)] = A.data[A.address(i,k)];
                }
            }
            if (k < nrt) {

                // Compute the k-th row transformation and place the
                // k-th super-diagonal in e.data[k].
                // Compute 2-norm without under/overflow.
                e.data[k] = 0;
                for (i = k+1; i < A.cols; i++) {
                    e.data[k] = hypot(e.data[k],e.data[i]);
                }
                if (e.data[k] != 0.0) {
                    if (e.data[k+1] < 0.0) {
                        e.data[k] = -e.data[k];
                    }
                    for (i = k+1; i < A.cols; i++) {
                        e.data[i] /= e.data[k];
                    }
                    e.data[k+1] += 1.0;
                }
                e.data[k] = -e.data[k];
                if ((k+1 < A.rows) & (e.data[k] != 0.0)) {

                    // Apply the transformation.

                    for (i = k+1; i < A.rows; i++) {
                        work.data[i] = 0.0;
                    }
                    for (j = k+1; j < A.cols; j++) {
                        for (i = k+1; i < A.rows; i++) {
                            work.data[i] += e.data[j]*A.data[A.address(i,j)];
                        }
                    }
                    for (j = k+1; j < A.cols; j++) {
                        final double t = -e.data[j]/e.data[k+1];
                        for (i = k+1; i < A.rows; i++) {
                            A.data[A.address(i,j)] += t*work.data[i];
                        }
                    }
                }

                // Place the transformation in V for subsequent
                // back multiplication.

                for (i = k+1; i < A.cols; i++) {
                    V.data[V.address(i,k)] = e.data[i];
                }
            }
        }

        // Set up the final bidiagonal matrix or order n.

        if (nct < A.cols) {
            s.data[nct] = A.data[A.address(nct,nct)];
        }
        if (nrt+1 < A.cols) {
            e.data[nrt] = A.data[A.address(nrt,A.cols-1)];
        }
        e.data[A.cols-1] = 0.0;

        // generate U

        for (j = nct; j < A.cols; j++) {
            for (i = 0; i < A.rows; i++) {
                U.data[U.address(i,j)] = 0.0;
            }
            U.data[U.address(j,j)] = 1.0;
        }
        for (k = nct-1; k >= 0; --k) {
            if (s.data[k] != 0.0) {
                for (j = k+1; j < A.cols; ++j) {
                    double t = 0;
                    for (i = k; i < A.rows; i++) {
                        t += U.data[U.address(i,k)]*U.data[U.address(i,j)];
                    }
                    t = -t/U.data[U.address(k,k)];
                    for (i = k; i < A.rows; i++) {
                        U.data[U.address(i,j)] += t*U.data[U.address(i,k)];
                    }
                }
                for (i = k; i < A.rows; i++ ) {
                    U.data[U.address(i,k)] = -U.data[U.address(i,k)];
                }
                U.data[U.address(k,k)] = 1.0 + U.data[U.address(k,k)];
                for (i = 0; i < k-1; i++) {
                    U.data[U.address(i,k)] = 0.0;
                }
            } else {
                for (i = 0; i < A.rows; i++) {
                    U.data[U.address(i,k)] = 0.0;
                }
                U.data[U.address(k,k)] = 1.0;
            }
        }

        // generate V

        for (k = A.cols-1; k >= 0; --k) {
            if ((k < nrt) & (e.data[k] != 0.0)) {
                for (j = k+1; j < A.cols; ++j) {
                    double t = 0;
                    for (i = k+1; i < A.cols; i++) {
                        t += V.data[V.address(i,k)]*V.data[V.address(i,j)];
                    }
                    t = -t / V.data[V.address(k+1,k)];
                    for (i = k+1; i < A.cols; i++) {
                        V.data[V.address(i,j)] += t*V.data[V.address(i,k)];
                    }
                }
            }
            for (i = 0; i < A.cols; i++) {
                V.data[V.address(i,k)] = 0.0;
            }
            V.data[V.address(k,k)] = 1.0;
        }

        // Main iteration loop for the singular values.

        int p = A.cols;
        final int pp = p-1;
        int iter = 0;
        final double eps = Math.pow(2.0, -52.0);
        while (p > 0) {
            int kk;
            int kase;

            // Here is where a test for too many iterations would go.

            // This section of the program inspects for
            // negligible elements in the s and e arrays.  On
            // completion the variables kase and k are set as follows.

            // kase = 1     if s(p) and e.data[k-1] are negligible and k<p
            // kase = 2     if s(k) is negligible and k<p
            // kase = 3     if e.data[k-1] is negligible, k<p, and
            //              s(k), ..., s(p) are not negligible (qr step).
            // kase = 4     if e(p-1) is negligible (convergence).

            for (kk = p-2; kk >= -1; --kk) {
                if (kk == -1) {
                    break;
                }
                if (Math.abs(e.data[kk]) <= eps*(Math.abs(s.data[kk]) + Math.abs(s.data[kk+1]))) {
                    e.data[kk] = 0.0;
                    break;
                }
            }
            if (kk == p-2) {
                kase = 4;
            } else {
                Integer ks;
                for (ks = p-1; ks >= kk; --ks) {
                    if (ks == kk) {
                        break;
                    }
                    final double t = (ks != p ? Math.abs(e.data[ks]) : 0.) + (ks != kk+1 ? Math.abs(e.data[ks-1]) : 0.0);
                    if (Math.abs(s.data[ks]) <= eps*t) {
                        s.data[ks] = 0.0;
                        break;
                    }
                }
                if (ks == kk) {
                    kase = 3;
                } else if (ks == p-1) {
                    kase = 1;
                } else {
                    kase = 2;
                    kk = ks;
                }
            }
            kk++;

            // Perform the task indicated by kase.

            switch (kase) {

            // Deflate negligible s(p).

            case 1: {
                double f = e.data[p-2];
                e.data[p-2] = 0.0;
                for (j = p-2; j >= kk; --j) {
                    double t = hypot(s.data[j],f);
                    final double cs = s.data[j]/t;
                    final double sn = f/t;
                    s.data[j] = t;
                    if (j != kk) {
                        f = -sn*e.data[j-1];
                        e.data[j-1] = cs*e.data[j-1];
                    }
                    for (i = 0; i < A.cols; i++) {
                        t = cs*V.data[V.address(i,j)] + sn*V.data[V.address(i,p-1)];
                        V.data[V.address(i,p-1)] = -sn*V.data[V.address(i,j)] + cs*V.data[V.address(i,p-1)];
                        V.data[V.address(i,j)] = t;
                    }
                }
            }
            break;

            // Split at negligible s(k).

            case 2: {
                double f = e.data[kk-1];
                e.data[kk-1] = 0.0;
                for (j = kk; j < p; j++) {
                    double t = hypot(s.data[j],f);
                    final double cs = s.data[j]/t;
                    final double sn = f/t;
                    s.data[j] = t;
                    f = -sn*e.data[j];
                    e.data[j] = cs*e.data[j];
                    for (i = 0; i < A.rows; i++) {
                        t = cs*U.data[U.address(i,j)] + sn*U.data[U.address(i,kk-1)];
                        U.data[U.address(i,kk-1)] = -sn*U.data[U.address(i,j)] + cs*U.data[U.address(i,kk-1)];
                        U.data[U.address(i,j)] = t;
                    }
                }
            }
            break;

            // Perform one qr step.

            case 3: {

                // Calculate the shift.
                final double scale = Math.max(
                        Math.max(
                                Math.max(
                                        Math.max(Math.abs(s.data[p-1]),
                                                Math.abs(s.data[p-2])),
                                                Math.abs(e.data[p-2])),
                                                Math.abs(s.data[kk])),
                                                Math.abs(e.data[kk]));
                final double sp = s.data[p-1]/scale;
                final double spm1 = s.data[p-2]/scale;
                final double epm1 = e.data[p-2]/scale;
                final double sk = s.data[kk]/scale;
                final double ek = e.data[kk]/scale;
                final double b = ((spm1 + sp)*(spm1 - sp) + epm1*epm1)/2.0;
                final double c = (sp*epm1)*(sp*epm1);
                double shift = 0.0;
                if ((b != 0.0) | (c != 0.0)) {
                    shift = Math.sqrt(b*b + c);
                    if (b < 0.0) {
                        shift = -shift;
                    }
                    shift = c/(b + shift);
                }
                double f = (sk + sp)*(sk - sp) + shift;
                double g = sk*ek;

                // Chase zeros.

                for (j = kk; j < p-1; j++) {
                    double t = hypot(f,g);
                    double cs = f/t;
                    double sn = g/t;
                    if (j != kk) {
                        e.data[j-1] = t;
                    }
                    f = cs*s.data[j] + sn*e.data[j];
                    e.data[j] = cs*e.data[j] - sn*s.data[j];
                    g = sn*s.data[j+1];
                    s.data[j+1] = cs*s.data[j+1];
                    for (i = 0; i < A.cols; i++) {
                        t = cs*V.data[V.address(i,j)] + sn*V.data[V.address(i,j+1)];
                        V.data[V.address(i,j+1)] = -sn*V.data[V.address(i,j)] + cs*V.data[V.address(i,j+1)];
                        V.data[V.address(i,j)] = t;
                    }
                    t = hypot(f,g);
                    cs = f/t;
                    sn = g/t;
                    s.data[j] = t;
                    f = cs*e.data[j] + sn*s.data[j+1];
                    s.data[j+1] = -sn*e.data[j] + cs*s.data[j+1];
                    g = sn*e.data[j+1];
                    e.data[j+1] = cs*e.data[j+1];
                    if (j < A.rows-1) {
                        for (i = 0; i < A.rows; i++) {
                            t = cs*U.data[U.address(i,j)] + sn*U.data[U.address(i,j+1)];
                            U.data[U.address(i,j+1)] = -sn*U.data[U.address(i,j)] + cs*U.data[U.address(i,j+1)];
                            U.data[U.address(i,j)] = t;
                        }
                    }
                }
                e.data[p-2] = f;
                iter = iter + 1;
            }
            break;

            // Convergence.

            case 4: {

                // Make the singular values positive.

                if (s.data[kk] <= 0.0) {
                    s.data[kk] = (s.data[kk] < 0.0 ? -s.data[kk] : 0.0);
                    for (i = 0; i <= pp; i++) {
                        V.data[V.address(i,kk)] = -V.data[V.address(i,kk)];
                    }
                }

                // Order the singular values.

                while (kk < pp) {
                    if (s.data[kk] >= s.data[kk+1]) {
                        break;
                    }
                    s.swap(kk, kk+1);
                    if (kk < A.cols-1) {
                        for (i = 0; i < A.cols; i++) {
                            V.swap(i,kk, i,kk+1);
                        }
                    }
                    if (kk < A.rows-1) {
                        for (i = 0; i < A.rows; i++) {
                            U.swap(i,kk, i,kk+1);
                        }
                    }
                    kk++;
                }
                iter = 0;
                --p;
            }
            break;
            }
        }

    }


    //
    // public methods
    //

    // results

    public final Matrix U() /*@ReadOnly*/ {
        return (transpose ? V : U);
    }

    public final Matrix V() /*@ReadOnly*/ {
        return (transpose ? U : V);
    }

    public final Array singularValues() /*@ReadOnly*/ {
        return s;
    }

    public /*@Disposable*/ Matrix S() /*@ReadOnly*/ {
        final Matrix S = new Matrix(A.cols,A.cols);
        int addr = 0;
        for (int i=0; i<A.cols; i++) {
            S.set(addr, s.data[i]);
            addr += A.cols+1;
        }
        return S;
    }

    public double norm2() {
        return s.data[0];
    }

    public double cond() {
        return s.data[0] / s.data[A.cols-1];
    }

    public int rank() {
        final double eps = Math.pow(2.0, -52.0);
        final double tol = A.rows * s.data[0] * eps;
        int r = 0;
        for (int i=0; i<s.length; i++) {
            if (s.data[i] > tol) {
                r++;
            }
        }
        return r;
    }

    // utilities

    public Matrix inverse() {
        final Matrix W = new Matrix(A.cols, A.cols);
        int addr = 0;
        for (int i=0; i<A.cols; i++) {
            final double value = 1.0 / s.data[i];
            W.set(addr, value);
            addr += A.cols+1;
        }
        return V().mul(W).mul(U().transpose());
    }

    public /*@Disposable*/ Array solveFor(final Array array) /*@ReadOnly*/ {
        return inverse().mul(array);
    }


    //
    // private methods
    //

    /**
     * @returns hypotenuse of real (non-complex) scalars <code>a</code> and <code>b</code> by
     * avoiding underflow/overflow using {@latex$ a * \sqrt{ 1 + {b/a}^2}}, rather than
     * {@latex$ \sqrt{{a}^2 + {b}^2}}
     */
    private double hypot(final double a, final double b) {
        if (a == 0) return Math.abs(b);
        final double c = b/a;
        return Math.abs(a) * Math.sqrt(1 + c*c);
    }

}
