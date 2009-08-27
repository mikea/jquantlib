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
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.lang.iterators.ConstIterator;
import org.jquantlib.math.matrixutilities.Cells.ConstColumnIterator;
import org.jquantlib.math.matrixutilities.Cells.RowIterator;

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
@QualityAssurance(quality = Quality.Q0_UNFINISHED, version = Version.OTHER, reviewers = { "Richard Gomes" })
public class QRDecomposition {

    private final static String MATRIX_IS_RANK_DEFICIENT = "Matrix is rank deficient";

    // epsmch is the machine precision
    static final double epsmch = Math.ulp(1.0);

    //
    // private fields
    //

    private final int m;
    private final int n;
    private final Matrix AT;
    private final int base; // intended to store AT.base()

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
     *@param A is an m by n array. On input A contains the matrix for which the QR factorization is to be computed. On output the
     *            strict upper trapezoidal part of A contains the strict upper trapezoidal part of R, and the lower trapezoidal part
     *            of A contains a factored form of Q.
     *@param pivot pivot is a logical input variable. If pivot is set true, then column pivoting is enforced. If pivot is set
     *            false, then no column pivoting is done.
     */


    public QRDecomposition(final Matrix A, final boolean pivot) {

        this.m = A.rows;
        this.n = A.cols;
        this.AT = new Matrix(A, A.style); // A.transpose()
        this.base = AT.base();
        this.rdiag = new double[this.n+base];
        this.acnorm = new double[n+base];

        final int[] lipvt = new int[n+base];
        final double[] wa = new double[n+base];

        qrfac_f77(m, n, AT, pivot, lipvt, rdiag, rdiag, wa);

        this.ipvt = new int[n+base];
        if (pivot) {
            for (int i=base; i < n+base; ++i)
                ipvt[i] = lipvt[i];
        } else {
            for (int i=base; i < n+base; ++i)
                ipvt[i] = i;
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
                    H.data[H.addr(i, j)] = AT.data[AT.addr(i, j)];
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
        final Matrix R = new Matrix(this.n, this.n, AT.style);

        for (int i=base; i<n+base; i++) {
            R.data[R.addr(i, i)] = rdiag[i];
            if (i<m+base) {
                final ConstColumnIterator itcol = AT.constColumnIterator(i, i+1);
                final RowIterator itrow = R.rowIterator(i, i+1);
                itrow.fill(itcol);
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
        final Matrix Q = new Matrix(this.m, this.n, Cells.Style.FORTRAN);

        for (int k=base; k<m+base; k++) {
            final Array w = new Array(m, Cells.Style.FORTRAN);
            w.data[w.addr(k)] = 1.0;

            for (int j=base; j<Math.min(n, m)+base; j++) {
                final double t3 = AT.data[AT.addr(j, j)];
                if (t3!=0.0) {
                    // final double t = AT.constRowIterator(j, j).innerProduct(w.constIterator()) / t3;

                    final ConstIterator itrow = AT.constRowIterator(j, j);
                    final ConstIterator itcol = w.constIterator(j, Math.min(m, n)+base);
                    final double t = itrow.innerProduct(itcol) / t3;

                    for (int i=j; i<m+base; i++) {
                        w.data[w.addr(i)] -= AT.data[AT.addr(j, i)] * t;
                    }
                }
                Q.data[Q.addr(k, j)] = w.data[j];
            }

            final RowIterator it = Q.rowIterator(k, Math.min(n, m)+base);
            while (it.hasNext()) {
                it.setDouble(0.0);
                it.forward();
            }
        }

        return Q;
    }



    //TODO: comment this method
    public Matrix P() {
        final Matrix P = new Matrix(this.n, this.n, AT.style);

        // reverse column pivoting
        for (int i=base; i < this.n+base; i++) {
            P.set(ipvt[i], i, 1.0);
        }

        return P;
    }


    /**
     * Least squares solution of A*X = B
     *
     * @param B a Matrix with as many this.m as A and any number of columns.
     * @return X that minimizes the two norm of Q*R*X-B.
     * @exception IllegalArgumentException Matrix row dimensions must agree.
     * @exception LibraryException Matrix is rank deficient.
     *
     * @note This implementation is based on JAMA as it does not depend on trigonometric functions
     */
    public Matrix solve(final Matrix B) {
        throw new UnsupportedOperationException();

//        QL.require(B.rows == this.m, Matrix.MATRIX_IS_INCOMPATIBLE);
//        if (!this.isFullRank())
//            throw new LibraryException(MATRIX_IS_RANK_DEFICIENT);
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
     *
     *<p>
     * The qrfac_f77 method uses Householder transformations with column pivoting (optional) to compute a QR factorization of the m
     * by n matrix A. That is, qrfac_f77 determines an orthogonal matrix Q, a permutation matrix P, and an upper trapezoidal matrix
     * R with diagonal elements of nonincreasing magnitude, such that AP = QR.
     *<p>
     * Translated by Steve Verrill on November 17, 2000 from the FORTRAN MINPACK source produced by Garbow, Hillstrom, and More.
     * <p>
     * <b>IMPORTANT:</b> The "_f77" suffixes indicate that these routines use FORTRAN style indexing. For example, you will see
     * <pre>
     *     for (i = 1; i &lt;= n; i++)
     * </pre>
     * rather than
     * <pre>
     *     for (i = 0; i &lt; n; i++)
     * </pre>
     * To use the "_f77" routines you will have to employ {@link Matrix#faddr(int, int)} and {@link Array#faddr(int)} address
     * calculation routines.
     *
     * @param m The number of rows of A.
     * @param n The number of columns of A.
     * @param a A is an m by n array.
     *          On input A contains the matrix for which the QR factorization is to be computed.
     *          On output the strict upper trapezoidal part of A contains the strict upper trapezoidal part of R, and
     *          the lower trapezoidal part of A contains a factored form of Q.
     * @param pivot pivot is a logical input variable.
     *          If pivot is set true, then column pivoting is enforced. If pivot is set false, then no column pivoting is done.
     * @param ipvt ipvt is an integer output array.
     *          ipvt defines the permutation matrix P such that A*P = Q*R.
     *          Column j of P is column ipvt[j] of the identity matrix.
     *          If pivot is false, ipvt is not referenced.
     * @param rdiag rdiag is an output array of length n which contains the diagonal elements of R.
     * @param acnorm acnorm is an output array of length n which contains the norms of the corresponding columns
     *          of the input matrix A.
     * @param wa wa is a work array of length n.
     *
     * @see Matrix#faddr(int, int)
     * @see Array#faddr(int)
     */
    public void qrfac_f77(
            final int m,
            final int n,
            final Matrix a,
            final boolean pivot,
            final int ipvt[],
            final double rdiag[],
            final double acnorm[],
            final double wa[]) {

        final double one = 1.0;
        final double p05 = .05;
        final double zero = 0.0;

        int i, j, jp1, k, kmax, minmn;
        double ajnorm, sum, temp;
        double fac;

        // Compute the initial column norms and initialize several arrays.
        for (j = 1; j <= n; j++) {
            //minpack :: acnorm[j] = Minpack_f77.enorm_f77(m,a[1][j]);
            acnorm[j] = enorm_f77(m, a.constColumnIterator(j));
            rdiag[j] = acnorm[j];
            wa[j] = rdiag[j];
            if (pivot)
                ipvt[j] = j;
        }

        // Reduce A to R with Householder transformations.
        minmn = Math.min(m, n);
        for (j = 1; j <= minmn; j++) {
            if (pivot) {
                // Bring the column of largest norm into the pivot position.
                kmax = j;
                for (k = j; k <= n; k++) {
                    if (rdiag[k] > rdiag[kmax])
                        kmax = k;
                }
                if (kmax != j) {
                    for (i = 1; i <= m; i++) {
                        temp = a.data[a.addr(i,j)];
                        a.data[a.addr(i,j)] = a.data[a.addr(i,kmax)];
                        a.data[a.addr(i,kmax)] = temp;
                    }
                    rdiag[kmax] = rdiag[j];
                    wa[kmax] = wa[j];
                    k = ipvt[j];
                    ipvt[j] = ipvt[kmax];
                    ipvt[kmax] = k;
                }
            }

            // Compute the Householder transformation to reduce the j-th column of A to a multiple of the j-th unit vector.

            //minpack :: ajnorm = Minpack_f77.enorm_f77(m-j+1,a[j][j]);
            ajnorm = enorm_f77(m-j+1, a.constColumnIterator(j, j));

            if (ajnorm != zero) {
                if (a.data[a.addr(j,j)] < zero)
                    ajnorm = -ajnorm;

                for (i = j; i <= m; i++) {
                    a.data[a.addr(i,j)] /= ajnorm;
                }
                a.data[a.addr(j,j)] += one;

                // Apply the transformation to the remaining columns
                // and update the norms.
                jp1 = j + 1;
                if (n >= jp1) {
                    for (k = jp1; k <= n; k++) {
                        sum = zero;
                        for (i = j; i <= m; i++) {
                            sum += a.data[a.addr(i,j)] * a.data[a.addr(i,k)];
                        }
                        temp = sum / a.data[a.addr(j,j)];
                        for (i = j; i <= m; i++) {
                            a.data[a.addr(i,k)] -= temp * a.data[a.addr(i,j)];
                        }
                        if (pivot && rdiag[k] != zero) {
                            temp = a.data[a.addr(j,k)] / rdiag[k];
                            rdiag[k] *= Math.sqrt(Math.max(zero, one - temp * temp));
                            fac = rdiag[k] / wa[k];
                            if (p05 * fac * fac <= epsmch) {
                                //minpack :: rdiag[k] = Minpack_f77.enorm_f77(m-j,a[jp1][k]);
                                rdiag[k] = enorm_f77(m-j, a.constColumnIterator(k, jp1));
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

    /**
     * This method calculates the Euclidean norm of a vector.
     * <p>
     * Translated by Steve Verrill on November 14, 2000 from the FORTRAN MINPACK source produced by Garbow, Hillstrom, and More.
     * <p>
     * <b>IMPORTANT:</b> The "_f77" suffixes indicate that these routines use FORTRAN style indexing. For example, you will see
     * <pre>
     *     for (i = 1; i &lt;= n; i++)
     * </pre>
     *
     * rather than
     *
     * <pre>
     *     for (i = 0; i &lt; n; i++)
     * </pre>
     * To use the "_f77" routines you will have to employ {@link Matrix#faddr(int, int)} and {@link Array#faddr(int)} address
     * calculation routines.
     *
     * @param n The length of the vector, x.
     * @param x The vector whose Euclidean norm is to be calculated.
     *
     * @see Matrix#faddr(int, int)
     * @see Array#faddr(int)
     */
    private double enorm_f77(final int n, final ConstColumnIterator it) {
        final double agiant, floatn;
        final double enorm;

        double rdwarf, rgiant;
        double s1, s2, s3, xabs, x1max, x3max;
        int i;

        final double one = 1.0;
        final double zero = 0.0;

        rdwarf = 3.834e-20;
        rgiant = 1.304e+19;

        s1 = zero;
        s2 = zero;
        s3 = zero;
        x1max = zero;
        x3max = zero;
        floatn = n;
        agiant = rgiant / floatn;

        for (i = 1; i <= n; i++) {
            //XXX xabs = Math.abs(x.data[x.addr(i)]);
            xabs = Math.abs(it.nextDouble());
            if (xabs <= rdwarf || xabs >= agiant) {
                if (xabs > rdwarf) {
                    // Sum for large components.
                    if (xabs > x1max) {
                        s1 = one + s1 * (x1max / xabs) * (x1max / xabs);
                        x1max = xabs;
                    } else {
                        s1 += (xabs / x1max) * (xabs / x1max);
                    }
                } else {
                    // Sum for small components.
                    if (xabs > x3max) {
                        s3 = one + s3 * (x3max / xabs) * (x3max / xabs);
                        x3max = xabs;
                    } else {
                        if (xabs != zero)
                            s3 += (xabs / x3max) * (xabs / x3max);
                    }
                }
            } else {
                // Sum for intermediate components.
                s2 += xabs * xabs;
            }
        }

        // Calculation of norm.
        if (s1 != zero) {
            enorm = x1max * Math.sqrt(s1 + (s2 / x1max) / x1max);
        } else {
            if (s2 != zero) {
                if (s2 >= x3max) {
                    enorm = Math.sqrt(s2 * (one + (x3max / s2) * (x3max * s3)));
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
