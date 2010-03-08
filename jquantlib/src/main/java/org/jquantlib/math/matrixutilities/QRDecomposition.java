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

/**
 * Calculates the QR-decomposition of a matrix.
 * <p>
 * <b>Note:</b> This class was adapted from Apache Commons Math
 * <p>
 * <p>The QR-decomposition of a matrix A consists of two matrices Q and R
 * that satisfy: A = QR, Q is orthogonal (Q<sup>T</sup>Q = I), and R is
 * upper triangular. If A is m&times;n, Q is m&times;m and R m&times;n.</p>
 * <p>This class compute the decomposition using Householder reflectors.</p>
 * <p>For efficiency purposes, the decomposition in packed form is transposed.
 * This allows inner loop to iterate inside rows, which is much more cache-efficient
 * in Java.</p>
 *
 * @see <a href="http://mathworld.wolfram.com/QRDecomposition.html">MathWorld</a>
 * @see <a href="http://en.wikipedia.org/wiki/QR_decomposition">Wikipedia</a>
 *
 * @version $Revision: 799857 $ $Date: 2009-08-01 14:07:12 +0100 (Sat, 01 Aug 2009) $
 * @since 1.2
 */
public class QRDecomposition {

    /**
     * A packed TRANSPOSED representation of the QR decomposition.
     * <p>The elements BELOW the diagonal are the elements of the UPPER triangular
     * matrix R, and the rows ABOVE the diagonal are the Householder reflector vectors
     * from which an explicit form of Q can be recomputed if desired.</p>
     */
    final Matrix mT;
    //private final double[][] qrt;

    /** The diagonal elements of R. */
    private final double[] rDiag;

    /** Cached value of Q. */
    private Matrix cachedQ;

    /** Cached value of QT. */
    private Matrix cachedQT;

    /** Cached value of R. */
    private Matrix cachedR;

    /** Cached value of H. */
    private Matrix cachedH;

    /**
     * Calculates the QR-decomposition of the given matrix.
     * @param matrix The matrix to decompose.
     */
    public QRDecomposition(final Matrix matrix) {
        this(matrix, false);
    }


    /**
     * Calculates the QR-decomposition of the given matrix.
     * @param matrix The matrix to decompose.
     */
    public QRDecomposition(final Matrix matrix, final boolean pivoting) {

        if (pivoting) {
            QL.warn("Column pivoting not implemented yet. Going ahead without column pivoting");
        }

        final int m = matrix.rows();
        final int n = matrix.cols();
        mT = matrix.transpose();
        rDiag = new double[Math.min(m, n)];
        cachedQ  = null;
        cachedQT = null;
        cachedR  = null;
        cachedH  = null;

        /*
         * The QR decomposition of a matrix A is calculated using Householder
         * reflectors by repeating the following operations to each minor
         * A(minor,minor) of A:
         */
        for (int minor = 0; minor < Math.min(m, n); minor++) {

            //-- final double[] qrtMinor = qrt[minor];
            final int mbase = mT.addr.op(minor, 0); // "minor" means row, in fact, because mT is a transpose matrix

            /*
             * Let x be the first column of the minor, and a^2 = |x|^2.
             * x will be in the positions qr[minor][minor] through qr[m][minor].
             * The first column of the transformed minor will be (a,0,0,..)'
             * The sign of a is chosen to be opposite to the sign of the first
             * component of x. Let's find a:
             */
            double xNormSqr = 0;
            for (int row = minor; row < m; row++) {
                final double c = mT.data[mbase+row];
                xNormSqr += c * c;
            }
            final double a = (mT.data[mbase+minor] > 0) ? -Math.sqrt(xNormSqr) : Math.sqrt(xNormSqr);
            rDiag[minor] = a;

            if (a != 0.0) {

                /*
                 * Calculate the normalized reflection vector v and transform
                 * the first column. We know the norm of v beforehand: v = x-ae
                 * so |v|^2 = <x-ae,x-ae> = <x,x>-2a<x,e>+a^2<e,e> =
                 * a^2+a^2-2a<x,e> = 2a*(a - <x,e>).
                 * Here <x, e> is now qr[minor][minor].
                 * v = x-ae is stored in the column at qr:
                 */
                mT.data[mbase+minor] -= a; // now |v|^2 = -2a*(qr[minor][minor])

                /*
                 * Transform the rest of the columns of the minor:
                 * They will be transformed by the matrix H = I-2vv'/|v|^2.
                 * If x is a column vector of the minor, then
                 * Hx = (I-2vv'/|v|^2)x = x-2vv'x/|v|^2 = x - 2<x,v>/|v|^2 v.
                 * Therefore the transformation is easily calculated by
                 * subtracting the column vector (2<x,v>/|v|^2)v from x.
                 *
                 * Let 2<x,v>/|v|^2 = alpha. From above we have
                 * |v|^2 = -2a*(qr[minor][minor]), so
                 * alpha = -<x,v>/(a*qr[minor][minor])
                 */
                for (int col = minor+1; col < n; col++) {
                    //-- final double[] qrtCol = qrt[col];
                    final int caddr = mT.addr.op(col, 0); // "col" means row, in fact, because mT is a transpose matrix
                    double alpha = 0;
                    for (int row = minor; row < m; row++) {
                        //-- alpha -= qrtCol[row] * qrtMinor[row];
                        alpha -= mT.data[caddr+row] * mT.data[mbase+row];
                    }
                    //-- alpha /= a * qrtMinor[minor];
                    alpha /= a * mT.data[mbase+minor];

                    // Subtract the column vector alpha*v from x.
                    for (int row = minor; row < m; row++) {
                        //-- qrtCol[row] -= alpha * qrtMinor[row];
                        mT.data[caddr+row] -= alpha * mT.data[mbase+row];
                    }
                }
            }
        }
    }

    public Matrix R() {

        if (cachedR == null) {

            // R is supposed to be m x n
            //-- final int n = qrt.length;
            //-- final int m = qrt[0].length;
            final int n = mT.rows();
            final int m = mT.cols();
            cachedR = new Matrix(m, n);

            // copy the diagonal from rDiag and the upper triangle of qr
            for (int row = Math.min(m, n) - 1; row >= 0; row--) {
                cachedR.set(row, row, rDiag[row]);
                for (int col = row + 1; col < n; col++) {
                    cachedR.set(row, col, mT.data[mT.addr.op(col,row)]);
                }
            }

        }

        // return the cached matrix
        return cachedR;

    }

    public Matrix Q() {
        if (cachedQ == null) {
            cachedQ = QT().transpose();
        }
        return cachedQ;
    }

    public Matrix QT() {
        if (cachedQT == null) {
            // QT is supposed to be m x m
            //-- final int n = qrt.length;
            //-- final int m = qrt[0].length;
            final int n = mT.rows();
            final int m = mT.cols();
            cachedQT = new Matrix(m, m);

            /*
             * Q = Q1 Q2 ... Q_m, so Q is formed by first constructing Q_m and then
             * applying the Householder transformations Q_(m-1),Q_(m-2),...,Q1 in
             * succession to the result
             */
            for (int minor = m - 1; minor >= Math.min(m, n); minor--) {
                cachedQT.set(minor, minor, 1.0);
            }

            for (int minor = Math.min(m, n)-1; minor >= 0; minor--){
                //-- final double[] qrtMinor = qrt[minor];
                final int mbase = mT.addr.op(minor, 0); // "minor" means row, in fact, because mT is a transpose matrix
                cachedQT.set(minor, minor, 1.0);
                if (mT.data[mbase+minor] != 0.0) {
                    for (int col = minor; col < m; col++) {
                        double alpha = 0;
                        for (int row = minor; row < m; row++) {
                            alpha -= cachedQT.get(col, row) * mT.data[mbase+row];
                        }
                        alpha /= rDiag[minor] * mT.data[mbase+minor];

                        for (int row = minor; row < m; row++) {
                            final double value = cachedQT.get(col, row) -alpha * mT.data[mbase+row];
                            cachedQT.set(col, row, value);
                        }
                    }
                }
            }

        }

        // return the cached matrix
        return cachedQT;
    }

    public Matrix H() {
        if (cachedH == null) {
            //-- final int n = qrt.length;
            //-- final int m = qrt[0].length;
            final int n = mT.rows();
            final int m = mT.cols();
            cachedH = new Matrix(m, n);
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < Math.min(i + 1, n); ++j) {
                    cachedH.set(i, j, mT.data[mT.addr.op(j,i)] / -rDiag[j]);
                }
            }
        }
        // return the cached matrix
        return cachedH;
    }


    /**
     * @note At the moment column pivoting is not implemented so this method returns an {@link Identity} matrix instead
     *
     * @return the permutation Matrix
     */
    public Matrix P() {
        return new Identity(mT.rows());
    }


    public boolean isNonSingular() {
        for (final double diag : rDiag) {
            if (diag == 0)
                return false;
        }
        return true;
    }


    public Array solve(final Array B) {

        //-- final int n = qrt.length;
        //-- final int m = qrt[0].length;
        final Matrix qrt = mT;
        final int n = qrt.rows();
        final int m = qrt.cols();

        QL.require(B.cols() == m, Cells.MATRIX_IS_INCOMPATIBLE);
        QL.require(isNonSingular(), Cells.MATRIX_IS_SINGULAR);

        final double[] x = new double[n];
        final double[] y = B.data;

        // apply Householder transforms to solve Q.y = b
        for (int minor = 0; minor < Math.min(m, n); minor++) {

            //final double[] qrtMinor = qrt[minor];
            final int maddr = qrt.addr.op(minor,0);

            double dotProduct = 0;
            for (int row = minor; row < m; row++) {
                //-- dotProduct += y[row] * qrtMinor[row];
                dotProduct += y[row] * qrt.data[maddr+row];
            }
            //-- dotProduct /= rDiag[minor] * qrtMinor[minor];
            dotProduct /= rDiag[minor] * qrt.data[maddr+minor];

            for (int row = minor; row < m; row++) {
                y[row] += dotProduct * qrt.data[maddr+row];
            }
        }

        // solve triangular system R.x = y
        for (int row = rDiag.length - 1; row >= 0; --row) {
            y[row] /= rDiag[row];
            final double yRow   = y[row];
            //-- final double[] qrtRow = qrt[row];
            final int raddr = qrt.addr.op(row,0);
            x[row] = yRow;
            for (int i = 0; i < row; i++) {
                y[i] -= yRow * qrt.data[raddr+i];
            }
        }

        return new Array(x);
    }


    public Matrix solve (final Matrix b) {
        throw new UnsupportedOperationException("work in progress");
//        //-- final int n = qrt.length;
//        //-- final int m = qrt[0].length;
//        final Matrix qrt = mT;
//        final int n = qrt.rows();
//        final int m = qrt.columns();
//
//        QL.require(b.rows == m, Cells.MATRIX_IS_INCOMPATIBLE);
//        QL.require(isNonSingular(), Cells.MATRIX_IS_SINGULAR);
//
//        final int columns        = b.cols;
//        final int blockSize      = BlockRealMatrix.BLOCK_SIZE;
//        final int cBlocks        = (columns + blockSize - 1) / blockSize;
//        final double[][] xBlocks = BlockRealMatrix.createBlocksLayout(n, columns);
//        //-- final double[][] y       = new double[b.rows][blockSize];
//        final Matrix y;
//
//        final double[]   alpha   = new double[blockSize];
//
//        for (int kBlock = 0; kBlock < cBlocks; ++kBlock) {
//            final int kStart = kBlock * blockSize;
//            final int kEnd   = Math.min(kStart + blockSize, columns);
//            final int kWidth = kEnd - kStart;
//
//            // get the right hand side vector
//            y = b.range(0, m-1, kStart, kEnd-1);
//
//            // apply Householder transforms to solve Q.y = b
//            for (int minor = 0; minor < Math.min(m, n); minor++) {
//                final double[] qrtMinor = qrt[minor];
//                final double factor     = 1.0 / (rDiag[minor] * qrtMinor[minor]);
//
//                Arrays.fill(alpha, 0, kWidth, 0.0);
//                for (int row = minor; row < m; ++row) {
//                    final double   d    = qrtMinor[row];
//                    final double[] yRow = y[row];
//                    for (int k = 0; k < kWidth; ++k) {
//                        alpha[k] += d * yRow[k];
//                    }
//                }
//                for (int k = 0; k < kWidth; ++k) {
//                    alpha[k] *= factor;
//                }
//
//                for (int row = minor; row < m; ++row) {
//                    final double   d    = qrtMinor[row];
//                    final double[] yRow = y[row];
//                    for (int k = 0; k < kWidth; ++k) {
//                        yRow[k] += alpha[k] * d;
//                    }
//                }
//
//            }
//
//            // solve triangular system R.x = y
//            for (int j = rDiag.length - 1; j >= 0; --j) {
//                final int      jBlock = j / blockSize;
//                final int      jStart = jBlock * blockSize;
//                final double   factor = 1.0 / rDiag[j];
//                final double[] yJ     = y[j];
//                final double[] xBlock = xBlocks[jBlock * cBlocks + kBlock];
//                for (int k = 0, index = (j - jStart) * kWidth; k < kWidth; ++k, ++index) {
//                    yJ[k]        *= factor;
//                    xBlock[index] = yJ[k];
//                }
//
//                final double[] qrtJ = qrt[j];
//                for (int i = 0; i < j; ++i) {
//                    final double rIJ  = qrtJ[i];
//                    final double[] yI = y[i];
//                    for (int k = 0; k < kWidth; ++k) {
//                        yI[k] -= yJ[k] * rIJ;
//                    }
//                }
//
//            }
//
//        }
//
//        return new BlockRealMatrix(n, columns, xBlocks, false);
    }

}
