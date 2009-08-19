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
import org.jquantlib.math.matrixutilities.Matrix.RowIterator;

/**
 * QR Decomposition.
 *<p>
 * This class uses Householder transformations with optional column pivoting to compute a QR factorization of the m by n matrix A.
 *<p>
 * It's determined an orthogonal matrix Q, a permutation matrix P, and an upper trapezoidal matrix R with diagonal elements of
 * non-increasing magnitude, such that
 * <p>
 * {@latex[ A \times P = Q \times R } when column pivoting is requested or
 * <p>
 * {@latex[ A = Q \times R } when columns pivoting is not needed.
 * <P>
 * The QR decomposition always exists, even if the matrix does not have full rank, so the constructor will never fail. The primary
 * use of the QR decomposition is in the least squares solution of nonsquare systems of simultaneous linear equations. This will
 * fail if isFullRank() returns false.
 *
 * @note This class is adapted both from JAMA and MINPACK. Whilst JAMA offers a well structured code and clean design, MINPACK
 *       offers column pivoting. We opted by adopting the general structure and organization of JAMA, which can be
 *       seen on classes like LUDecomposition and alike, and adapt code from MINPACK in order to implement column pivoting.
 *
 * @note MINPACK is actually implemented in Fortran, so we based our implementation in a previous translation to Java which was done
 *       by Steve Verrill on November 17, 2000 from the FORTRAN MINPACK source produced by Garbow, Hillstrom, and More.
 *
 *@param m The number of rows of A.
 *@param n The number of columns of A.
 *@param a A is an m by n array. On input A contains the matrix for which the QR factorization is to be computed. On output the
 *            strict upper trapezoidal part of A contains the strict upper trapezoidal part of R, and the lower trapezoidal part of
 *            A contains a factored form of Q.
 *@param pivot pivot is a logical input variable. If pivot is set true, then column pivoting is enforced. If pivot is set false,
 *            then no column pivoting is done.
 *
 *@param ipvt ipvt is an integer output array. ipvt defines the permutation matrix P such that A*P = Q*R. Column j of P is column
 *            ipvt[j] of the identity matrix. If pivot is false, ipvt is not referenced.
 *@param rdiag rdiag is an output array of length n which contains the diagonal elements of R.
 *@param acnorm acnorm is an output array of length n which contains the norms of the corresponding columns of the input matrix A.
 *@param wa wa is a work array of length n.
 *
 * @see <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a>
 * @see <a href="http://www1.fpl.fs.fed.us/optimization.html">Nonlinear Optimization Java Package</a>
 * @see <a href="http://icl.cs.utk.edu/f2j/">FORTRAN to Java translator</a>
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q1_TRANSLATION, version = Version.OTHER, reviewers = { "Richard Gomes" })
public class QRDecomposition {

    private final static String MATRIX_IS_RANK_DEFICIENT = "Matrix is rank deficient";

    // epsmch is the machine precision
    static final double epsmch = Math.ulp(1.0);

    //
    // private fields
    //

    private final int m;
    private final int n;
    private final Matrix A;

    /**
     * rdiag is an output array of length n which contains the diagonal elements of R.
     */
    private final double[] rdiag;

    /**
     * ipvt is an integer output array. ipvt defines the permutation matrix P such that {@latex$ A*P = Q*R } Column j of P is column
     * ipvt[j] of the identity matrix. If pivot is false, ipvt is not referenced.
     */
    final int ipvt[];

    /**
     * acnorm is an output array of length n which contains the norms of the corresponding columns of the input matrix A
     */
    final double acnorm[];

    //
    // public constructors
    //

    /**
     * This constructor is equivalent to QRDecomposition(a, false)
     */
    public QRDecomposition(final Matrix a) {
        this(a, false);
    }

    /**
     * Uses Householder transformations with column optional pivoting to compute a QR factorization of the m by n matrix A.
     * <p>
     * It's determined an orthogonal matrix Q, a permutation matrix P, and an upper trapezoidal matrix R with diagonal elements of
     * non-increasing magnitude, such that {@latex$ A \times P = Q \times R}.
     *<p>
     *
     * @note Based on code translated by Steve Verrill on November 17, 2000 from the FORTRAN MINPACK source produced by Garbow,
     *       Hillstrom, and More.
     *
     *@param a is an m by n array. On input A contains the matrix for which the QR factorization is to be computed. On output the
     *            strict upper trapezoidal part of A contains the strict upper trapezoidal part of R, and the lower trapezoidal part
     *            of A contains a factored form of Q.
     *@param pivot pivot is a logical input variable. If pivot is set true, then column pivoting is enforced. If pivot is set
     *            false, then no column pivoting is done.
     */
    public QRDecomposition(final Matrix a, final boolean pivot) {

        this.A = a.transpose();
        this.m = A.rows;
        this.n = A.cols;

        this.rdiag = new double[this.n];
        this.ipvt = new int[n];
        this.acnorm = new double[n];

        final double wa[] = new double[n];

        int i, j, jp1, k, kmax, minmn;
        double ajnorm, sum, temp;
        double fac;

        final double tempvec[] = new double[m];

        // Compute the initial column norms and initialize several arrays.
        for (j = 0; j < n; j++) {
            for (i = 0; i < m; i++) {
                tempvec[i] = A.data[A.addr(i, j)];
            }
            acnorm[j] = norm(m, tempvec);
            rdiag[j] = acnorm[j];
            wa[j] = rdiag[j];
            if (pivot)
                ipvt[j] = j;
        }

        // Reduce A to R with Householder transformations.
        minmn = Math.min(m, n);
        for (j = 0; j < minmn; j++) {
            if (pivot) {
                // Bring the column of largest norm into the pivot position.
                kmax = j;
                for (k = j; k < n; k++) {
                    if (rdiag[k] > rdiag[kmax])
                        kmax = k;
                }
                if (kmax != j) {
                    for (i = 0; i < m; i++) {
                        temp = A.data[A.addr(i, j)];
                        A.data[A.addr(i, j)] = A.data[A.addr(i, kmax)];
                        A.data[A.addr(i, kmax)] = temp;
                    }
                    rdiag[kmax] = rdiag[j];
                    wa[kmax] = wa[j];
                    k = ipvt[j];
                    ipvt[j] = ipvt[kmax];
                    ipvt[kmax] = k;
                }
            }

            // Compute the Householder transformation to reduce the j-th column of A to a multiple of the j-th unit vector.
            for (i = j; i < m; i++) {
                tempvec[i-j] = A.data[A.addr(i, j)];
            }
            ajnorm = norm(m-j, tempvec);
            if (ajnorm != 0.0) {
                if (A.data[A.addr(j, j)] < 0.0)
                    ajnorm = -ajnorm;
                for (i = j; i < m; i++) {
                    A.data[A.addr(i, j)] /= ajnorm;
                }
                A.data[A.addr(j, j)] += 1.0;

                // Apply the transformation to the remaining columns and update the norms.
                jp1 = j + 1;
                if (n >= jp1) {
                    for (k = jp1; k < n; k++) {
                        sum = 0.0;
                        for (i = j; i < m; i++) {
                            sum += A.data[A.addr(i, j)] * A.data[A.addr(i, k)];
                        }
                        temp = sum / A.data[A.addr(j, j)];
                        for (i = j; i < m; i++) {
                            A.data[A.addr(i, k)] -= temp * A.data[A.addr(i, j)];
                        }

                        if (pivot && rdiag[k] != 0.0) {
                            temp = A.data[A.addr(j, k)] / rdiag[k];
                            rdiag[k] *= Math.sqrt(Math.max(0.0, 1.0 - temp * temp));

                            fac = rdiag[k] / wa[k];
                            if (0.05 * fac * fac <= epsmch) {
                                for (i = jp1; i < m; i++) {
                                    tempvec[i-j] = A.data[A.addr(i, k)];
                                }

                                rdiag[k] = norm(m-j, tempvec);
                                wa[k] = rdiag[k];
                            }
                        }
                    }
                }
            }

            rdiag[j] = -ajnorm;
        }

        return;
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
            if (rdiag[j] == 0)
                return false;
        }
        return true;
    }

    /**
     * @return pivots
     */
    public int[] pivot() {
        return ipvt.clone();
    }

    /**
     * Return the Householder vectors
     *
     * @return Lower trapezoidal matrix whose columns define the reflections
     */
    public Matrix H() {
        final Matrix H = new Matrix(this.m, this.n);
        for (int i=0; i<this.m; i++) {
            for (int j=0; j<this.n; j++) {
                if (i>=j) {
                    H.data[H.addr(i, j)] = A.data[A.addr(i, j)];
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

        for (int i=0; i<n; i++) {
            R.data[R.addr(i, i)] = rdiag[i];
        }
        for (int i=0; i<n; i++) {
            if (i<m) {
                Matrix.copy(A.columnIterator(i, i+1), R.rowIterator(i, i+1));
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

        for (int k=0; k<m; k++) {
            final Array w = new Array(m);
            w.data[k] = 1.0;

            for (int j=0; j<Math.min(n, m); j++) {
                final double t3 = A.data[A.addr(j, j)];
                if (t3!=0.0) {
                    final double t = A.rangeRow(j, j).innerProduct(w.range(j)) / t3;
                    for (int i=j; i<m; i++) {
                        w.data[i] -= A.data[A.addr(j, i)] * t;
                    }
                }
                Q.data[Q.addr(k, j)] = w.data[j];
            }

            final RowIterator it = Q.rowIterator(k, Math.min(n, m));
            while (it.hasNext()) {
                it.set(0.0);
                it.forward();
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
     *
     * @note This implementation is based on JAMA as it does not depend on trigonometric functions
     */
    public Matrix solve(final Matrix B) {
        throw new UnsupportedOperationException();

//        QL.require(B.rows == this.m, Matrix.MATRIX_IS_INCOMPATIBLE);
//        if (!this.isFullRank())
//            throw new RuntimeException(MATRIX_IS_RANK_DEFICIENT);
//
//        // Copy right hand side
//        final int nx = B.cols;
//        final Matrix X = B.clone();
//
//        // Compute Y = transpose(Q)*B
//        for (int k = 0; k < this.n; k++) {
//            for (int j = 0; j < nx; j++) {
//                double s = 0.0;
//                for (int i = k; i < this.m; i++) {
//                    s += A.data[A.addr(i, k)] * X.data[X.addr(i, j)];
//                }
//                s = -s / A.data[A.addr(k, k)];
//                for (int i = k; i < this.m; i++) {
//                    X.data[X.addr(i, j)] += s * A.data[A.addr(i, k)];
//                }
//            }
//        }
//        // Solve R*X = Y;
//        for (int k = this.n - 1; k >= 0; k--) {
//            for (int j = 0; j < nx; j++) {
//                X.data[X.addr(k, j)] /= rdiag[k];
//            }
//            for (int i = 0; i < k; i++) {
//                for (int j = 0; j < nx; j++) {
//                    X.data[X.addr(i, j)] -= X.data[X.addr(k, j)] * A.data[A.addr(i, k)];
//                }
//            }
//        }
//        return X;
    }

    /**
     * This method calculates the Euclidean norm of a vector.
     * <p>
     * Translated by Steve Verrill on November 14, 2000 from the FORTRAN MINPACK source produced by Garbow, Hillstrom, and More.
     * <p>
     *
     *@param n The length of the vector, x.
     *@param x The vector whose Euclidean norm is to be calculated.
     *
     */
    private double norm(final int n, final double x[]) {
        int i;
        double agiant, floatn, rdwarf, rgiant, s1, s2, s3, xabs, x1max, x3max;
        double enorm;

        rdwarf = 3.834e-20;
        rgiant = 1.304e+19;

        s1 = 0.0;
        s2 = 0.0;
        s3 = 0.0;
        x1max = 0.0;
        x3max = 0.0;
        floatn = n;
        agiant = rgiant / floatn;

        for (i = 0; i < n; i++) {
            xabs = Math.abs(x[i]);
            if (xabs <= rdwarf || xabs >= agiant) {
                if (xabs > rdwarf) {
                    // Sum for large components.
                    if (xabs > x1max) {
                        s1 = 1.0 + s1 * (x1max / xabs) * (x1max / xabs);
                        x1max = xabs;
                    } else {
                        s1 += (xabs / x1max) * (xabs / x1max);
                    }
                } else {
                    // Sum for small components.
                    if (xabs > x3max) {
                        s3 = 1.0 + s3 * (x3max / xabs) * (x3max / xabs);
                        x3max = xabs;
                    } else {
                        if (xabs != 0.0)
                            s3 += (xabs / x3max) * (xabs / x3max);
                    }
                }
            } else {
                // Sum for intermediate components.
                s2 += xabs * xabs;
            }
        }

        // Calculation of norm.
        if (s1 != 0.0) {
            enorm = x1max * Math.sqrt(s1 + (s2 / x1max) / x1max);
        } else {
            if (s2 != 0.0) {
                if (s2 >= x3max) {
                    enorm = Math.sqrt(s2 * (1.0 + (x3max / s2) * (x3max * s3)));
                } else {
                    enorm = Math.sqrt(x3max * ((s2 / x3max) + (x3max * s3)));
                }
            } else {
                enorm = x3max * Math.sqrt(s3);
            }
        }
        return enorm;
    }

}
