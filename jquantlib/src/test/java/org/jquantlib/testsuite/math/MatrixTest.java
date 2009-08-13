/*
 Copyright (C) 2007 Richard Gomes

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

package org.jquantlib.testsuite.math;

import static org.junit.Assert.fail;

import org.jquantlib.math.Constants;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Identity;
import org.jquantlib.math.matrixutilities.Matrix;
import org.jquantlib.math.matrixutilities.QRDecomposition;
import org.jquantlib.math.matrixutilities.SVD;
import org.jquantlib.math.matrixutilities.SymmetricSchurDecomposition;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Richard Gomes
 */
public class MatrixTest {

    private final static Logger logger = LoggerFactory.getLogger(MatrixTest.class);

    private final int N;
    private final Matrix M1, M2, M3, M4, M5, M6, M7;
    private final Matrix I;


    public MatrixTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");

        N = 3;

        M1 = new Matrix(new double[][] {
            { 1.0,  0.9,  0.7 },
            { 0.9,  1.0,  0.4 },
            { 0.7,  0.4,  1.0 }
        });

        M2 = new Matrix(new double[][] {
            { 1.0,  0.9,  0.7 },
            { 0.9,  1.0,  0.3 },
            { 0.7,  0.3,  1.0 }
        });

        I = new Identity(N);

        M3 = new Matrix(new double[][] {
            { 1,   2,   3,   4 },
            { 2,   0,   2,   1 },
            { 0,   1,   0,   0 }
        });

        M4 = new Matrix(new double[][] {
            {  1,   2,   400    },
            {  2,   0,     1    },
            { 30,   2,     0    },
            {  2,   0,     1.05 }
        });

        // from Higham - nearest correlation matrix
        M5 = new Matrix(new double[][] {
            {  2,   -1,     0,    0 },
            { -1,    2,    -1,    0 },
            {  0,   -1,     2,   -1 },
            {  0,    0,    -1,    2 },
        });

        // from Higham - nearest correlation matrix to M5
        M6 = new Matrix(new double[][] {
                {  1,              -0.8084124981,    0.1915875019,    0.106775049  },
                { -0.8084124981,    1,              -0.6562326948,    0.1915875019 },
                {  0.1915875019,   -0.6562326948,    1,              -0.8084124981 },
                {  0.106775049,     0.1915875019,   -0.8084124981,    1            }
        });

        M7 = M1.clone();
        M7.set(0, 1, 0.3); M7.set(0, 2, 0.2); M7.set(2, 1, 1.2);
    }




    @Test
    public void testEquals() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        if (!mA.equals(mB)) fail("'equals' failed");
    }


    @Test
    public void testClone() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Matrix m = mA.clone();
        if (m == mA) fail("'clone' must return a new instance");
        if (m == mB) fail("'clone' must return a new instance");
        if (!m.equals(mB)) fail("'clone' failed");
    }

    @Test
    public void toArray() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final double[][] doubles = new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        };

        double[][] result = (double[][]) mA.toArray();
        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (result[row][col] != doubles[row][col])
                    fail("toArray failed");

        result = mA.toArray(new double[3][4]);
        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (result[row][col] != doubles[row][col])
                    fail("toArray failed");
    }

    @Test
    public void empty() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        if (mA.empty()) fail("'empty' failed");
    }

    @Test
    public void fill() {
        final Matrix mA = new Matrix(new double[][] {
                { 2.0, 2.0, 2.0, 2.0 },
                { 2.0, 2.0, 2.0, 2.0 },
                { 2.0, 2.0, 2.0, 2.0 },
        });

        final Matrix m = new Matrix(3,4).fill(2.0);
        if (!m.equals(mA)) fail("'fill' failed");
    }

    @Test
    public void getRow() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 0.0, 0.0, 0.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 9.0, 9.0, 9.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 0.0, -1.0, -2.0 },
        });

        final Matrix mC = new Matrix(new double[][] {
                { 1.0 },
                { 2.0 },
                { 3.0 },
        });

        final Array aA = new Array(new double[] { 1.0, 9.0, 9.0, 9.0 });
        final Array aB = new Array(new double[] { 1.0, 0.0, -1.0, -2.0 });
        final Array aC = new Array(new double[] { 3.0 });

        if (!mA.getRow(2).equals(aA)) fail("'getRow' failed");
        if (!mB.getRow(0).equals(aB)) fail("'getRow' failed");
        if (!mC.getRow(2).equals(aC)) fail("'getRow' failed");
    }

    @Test
    public void getCol() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 0.0, 0.0, 0.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 9.0, 9.0, 9.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 0.0, -1.0, -2.0 },
        });

        final Matrix mC = new Matrix(new double[][] {
                { 1.0 },
                { 2.0 },
                { 3.0 },
        });

        final Array aA = new Array(new double[] {  0.0, 3.0, 9.0 });
        final Array aB = new Array(new double[] { -1.0 });
        final Array aC = new Array(new double[] {  1.0, 2.0, 3.0 });

        if (!mA.getCol(2).equals(aA)) fail("'getCol' failed");
        if (!mB.getCol(2).equals(aB)) fail("'getCol' failed");
        if (!mC.getCol(0).equals(aC)) fail("'getCol' failed");
    }

    @Test
    public void addAssign() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 3.0, 2.0, 1.0 },
                { 5.0, 4.0, 3.0, 2.0 },
                { 6.0, 5.0, 4.0, 3.0 },
                { 7.0, 6.0, 5.0, 4.0 },
        });


        final Matrix m = mA.addAssign(mB);
        if (m != mA) fail("addAssign must return <this>");

        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (mA.get(row, col) != row+5)
                    fail("addAssign failed");
    }

    @Test
    public void subAssign() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 5.0, 6.0,  7.0 },
                { 5.0, 6.0, 7.0,  8.0 },
                { 6.0, 7.0, 8.0,  9.0 },
                { 7.0, 8.0, 9.0, 10.0 },
        });


        final Matrix m = mB.subAssign(mA);
        if (m != mB) fail("subAssign must return <this>");

        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (mB.get(row, col) != row+3)
                    fail("subAssign failed");
    }

    @Test
    public void mulAssign() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        //        final Matrix mB = new Matrix(new double[][] {
        //                { 4.0, 3.0, 2.0, 1.0 },
        //                { 5.0, 4.0, 3.0, 2.0 },
        //                { 6.0, 5.0, 4.0, 3.0 },
        //                { 7.0, 6.0, 5.0, 4.0 },
        //            });


        final Matrix m = mA.mulAssign(2.5);
        if (m != mA) fail("mulAssign must return <this>");

        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (mA.get(row, col) != (col+1)*2.5)
                    fail("mulAssign failed");
    }

    @Test
    public void divAssign() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        //        final Matrix mB = new Matrix(new double[][] {
        //                { 4.0, 3.0, 2.0, 1.0 },
        //                { 5.0, 4.0, 3.0, 2.0 },
        //                { 6.0, 5.0, 4.0, 3.0 },
        //                { 7.0, 6.0, 5.0, 4.0 },
        //            });


        final Matrix m = mA.divAssign(2.5);
        if (m != mA) fail("divAssign must return <this>");

        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (mA.get(row, col) != (col+1)/2.5)
                    fail("divAssign failed");
    }

    @Test
    public void add() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 3.0, 2.0, 1.0 },
                { 5.0, 4.0, 3.0, 2.0 },
                { 6.0, 5.0, 4.0, 3.0 },
                { 7.0, 6.0, 5.0, 4.0 },
        });


        final Matrix m = mA.add(mB);
        if (m == mA) fail("'add' must return a new instance");
        if (m.rows != mA.rows || m.cols != mA.cols) fail("'add' failed");

        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (m.get(row, col) != row+5)
                    fail("'add' failed");
    }

    @Test
    public void sub() {

        // matrix

        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 5.0, 6.0,  7.0 },
                { 5.0, 6.0, 7.0,  8.0 },
                { 6.0, 7.0, 8.0,  9.0 },
                { 7.0, 8.0, 9.0, 10.0 },
        });


        Matrix m = mB.sub(mA);
        if (m == mB) fail("'sub' must return a new instance");
        if (m.rows != mB.rows || m.cols != mB.cols) fail("'sub' failed");

        for (int row=0; row<mB.rows; row++)
            for (int col=0; col<mB.cols; col++)
                if (m.get(row, col) != row+3)
                    fail("'sub' failed");


        // scalar

        final Matrix mC = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        m = mC.mul(2.5);
        if (m == mC) fail("'mul' must return a new instance");
        if (m.rows != mC.rows || m.cols != mC.cols) fail("'mul' failed");

        for (int row=0; row<mC.rows; row++)
            for (int col=0; col<mC.cols; col++)
                if (m.get(row, col) != (col+1)*2.5)
                    fail("'mul' failed");


        // array

        final Matrix mD = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Array aD = new Array(new double[] { 1.0, 1.0, 1.0, 1.0 } );

        final Array a = mD.mul(aD);
        if (a == mD) fail("'mul' must return a new instance");
        if (a.rows != 1 || a.cols != mD.cols) fail("'add' failed");

        for (int col=0; col<mD.cols; col++)
            if (a.get(col) != (col+1)*5)
                fail("'mul' failed");

    }


    @Test
    public void div() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        //        final Matrix mB = new Matrix(new double[][] {
        //                { 4.0, 3.0, 2.0, 1.0 },
        //                { 5.0, 4.0, 3.0, 2.0 },
        //                { 6.0, 5.0, 4.0, 3.0 },
        //                { 7.0, 6.0, 5.0, 4.0 },
        //            });


        final Matrix m = mA.div(2.5);
        if (m == mA) fail("'div' must return a new instance");
        if (m.rows != mA.rows || m.cols != mA.cols) fail("'add' failed");

        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (m.get(row, col) != (col+1)/2.5)
                    fail("'div' failed");
    }


    @Test
    public void mul() {
        final Matrix mI = new Matrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 },
        });

        final Matrix mA = new Matrix(new double[][] {
                { 4.0, 3.0, 2.0, 1.0 },
                { 5.0, 4.0, 3.0, 2.0 },
                { 6.0, 5.0, 4.0, 3.0 },
                { 7.0, 6.0, 5.0, 4.0 },
        });

        final Matrix m = mI.mul(mA);
        if (m == mI) fail("'mul' must return a new instance");
        if (m == mA) fail("'mul' must return a new instance");
        if (!m.equals(mA)) fail("'mul' failed");
    }

    @Test
    public void swap() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 3.0, 2.0, 1.0 },
                { 5.0, 4.0, 3.0, 2.0 },
                { 6.0, 5.0, 4.0, 3.0 },
                { 7.0, 6.0, 5.0, 4.0 },
        });

        final Matrix mAclone = mA.clone();
        final Matrix mBclone = mB.clone();

        mA.swap(mB);
        if (!mA.equals(mBclone)) fail("'swap' failed");
        if (!mB.equals(mAclone)) fail("'swap' failed");
    }

    @Test
    public void transpose() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 1.0, 1.0 },
                { 2.0, 2.0, 2.0 },
                { 3.0, 3.0, 3.0 },
                { 4.0, 4.0, 4.0 },
        });

        final Matrix m = mA.transpose();

        if (m == mA) fail("'transpose' must return a new instance");
        if (m == mB) fail("'transpose' must return a new instance");
        if (!m.equals(mB)) fail("'transpose' failed");
    }

    @Test
    public void diagonal() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 9.0, 9.0, 9.0 },
                { 9.0, 2.0, 9.0, 9.0 },
                { 9.0, 9.0, 3.0, 9.0 },
                { 9.0, 9.0, 9.0, 4.0 },
        });

        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        if (!mA.diagonal().equals(aA)) fail("'transpose' failed");
    }


    @Test
    public void testEigenvectors() {

        logger.info("Testing eigenvalues and eigenvectors calculation...");

        final Matrix testMatrices[] = { M1, M2 };

        for (final Matrix M : testMatrices) {

            final SymmetricSchurDecomposition schur = M.schur();
            final Array eigenValues = schur.eigenvalues();
            final Matrix eigenVectors = schur.eigenvectors();
            double minHolder = Constants.QL_MAX_REAL;

            for (int i=0; i<N; i++) {
                final Array v = new Array(N);
                for (int j=0; j<N; j++)
                    v.set( j, eigenVectors.get(j,i) );
                // check definition
                final Array a = M.mul(v);
                final Array b = v.mul(eigenValues.get(i));
                if (norm(a.sub(b)) > 1.0e-15)
                    fail("Eigenvector definition not satisfied");
                // check decreasing ordering
                if (eigenValues.get(i) >= minHolder) {
                    fail("Eigenvalues not ordered");
                } else
                    minHolder = eigenValues.get(i);
            }

            // check normalization
            final Matrix m = eigenVectors.mul(eigenVectors.transpose());
            if (norm(m.sub(I)) > 1.0e-15)
                fail("Eigenvector not normalized");
        }
    }



//    void MatricesTest::testSqrt() {
//
//        BOOST_MESSAGE("Testing matricial square root...");
//
//        setup();
//
//        final Matrix m = pseudoSqrt(M1, SalvagingAlgorithm::None);
//        final Matrix temp = m*transpose(m);
//        final Real error = norm(temp - M1);
//        final Real tolerance = 1.0e-12;
//        if (error>tolerance) {
//            BOOST_FAIL("Matrix square root calculation failed\n"
//                       << "original matrix:\n" << M1
//                       << "pseudoSqrt:\n" << m
//                       << "pseudoSqrt*pseudoSqrt:\n" << temp
//                       << "\nerror:     " << error
//                       << "\ntolerance: " << tolerance);
//        }
//    }
//
//    void MatricesTest::testHighamSqrt() {
//        BOOST_MESSAGE("Testing Higham matricial square root...");
//
//        setup();
//
//        final Matrix tempSqrt = pseudoSqrt(M5, SalvagingAlgorithm::Higham);
//        final Matrix ansSqrt = pseudoSqrt(M6, SalvagingAlgorithm::None);
//        final Real error = norm(ansSqrt - tempSqrt);
//        final Real tolerance = 1.0e-4;
//        if (error>tolerance) {
//            BOOST_FAIL("Higham matrix correction failed\n"
//                       << "original matrix:\n" << M5
//                       << "pseudoSqrt:\n" << tempSqrt
//                       << "should be:\n" << ansSqrt
//                       << "\nerror:     " << error
//                       << "\ntolerance: " << tolerance);
//        }
//    }


    @Test
    public void testSVD() {

        logger.info("Testing singular value decomposition...");

        final double tol = 1.0e-12;
        final Matrix testMatrices[] = { M1, M2, M3, M4 };

        for (final Matrix A : testMatrices) {
            final SVD svd = A.svd();
            // U is m x n
            final Matrix U = svd.U();
            // s is n long
            final Array s = svd.singularValues();
            // S is n x n
            final Matrix S = svd.S();
            // V is n x n
            final Matrix V = svd.V();

            for (int i=0; i < S.rows(); i++) {
                if (S.get(i,i) != s.get(i))
                    fail("S not consistent with s");
            }

            // tests
            final Matrix U_Utranspose = U.transpose().mul(U);
            if (norm(U_Utranspose.sub(I)) > tol)
                fail("U not orthogonal");

            final Matrix V_Vtranspose = V.transpose().mul(V);
            if (norm(V_Vtranspose.sub(I)) > tol)
                fail("V not orthogonal");

            final Matrix A_reconstructed = U.mul(S).mul(V.transpose());
            if (norm(A_reconstructed.sub(A)) > tol)
                fail("Product does not recover A");
        }
    }


    @Test
    public void testQRDecomposition() {

        logger.info("Testing QR decomposition...");

        final double tol = 1.0e-12;

        // FIXME: QRDecomposition does not support rectangular matrices when rows<cols
        // Investigate if another QRDecomposition method should be employed
        //
        // In order to this test pass, we need to comment out:
        //            M3, M3.transpose(), M4, M4.transpose()
        //
        final Matrix testMatrices[] = { M1, M2, I, M3, M3.transpose(), M4, M4.transpose(), M5 };

        QRDecomposition qr;
        Matrix Q;
        Matrix R;

        for (final Matrix A : testMatrices) {


// FIXME: QRDecomposition does not support generation of pivots
// Investigate if another QRDecomposition method should be employed
/**

            qr = A.qr(true);
            Q = qr.Q();
            R = qr.R();

            final Array ipvt = qr.pivot();

            final Matrix P = new Matrix(A.columns(), A.columns());

            // reverse column pivoting
            for (int i=0; i < P.columns(); ++i) {
                P.set((int)ipvt.get(i), i, 1.0);
            }

            if (norm(Q.mul(R).sub(A.mul(P))) > tol)
                fail("Q*R does not match matrix A*P");

*/

            qr = A.qr();
            Q = qr.Q();
            R = qr.R();

            if (norm(Q.mul(R).sub(A)) > tol)
                fail("Q*R does not match matrix A");
        }
    }

//    void MatricesTest::testQRSolve() {
//
//        BOOST_MESSAGE("Testing QR solve...");
//
//        setup();
//
//        Real tol = 1.0e-12;
//        MersenneTwisterUniformRng rng(1234);
//        Matrix bigM(50, 100, 0.0);
//        for (Size i=0; i < std::min(bigM.rows(), bigM.columns()); ++i) {
//            bigM[i][i] = i+1.0;
//        }
//        Matrix testMatrices[] = { M1, M2, M3, transpose(M3),
//                                  M4, transpose(M4), M5, I, M7, bigM, transpose(bigM) };
//
//        for (Size j = 0; j < LENGTH(testMatrices); j++) {
//            const Matrix& A = testMatrices[j];
//            Array b(A.rows());
//
//            for (Size k=0; k < 10; ++k) {
//                for (Array::iterator iter = b.begin(); iter != b.end(); ++iter) {
//                    *iter = rng.next().value;
//                }
//                const Array x = qrSolve(A, b, true);
//
//                if (A.columns() >= A.rows()) {
//                    if (norm(A*x - b) > tol)
//                        BOOST_FAIL("A*x does not match vector b (norm = "
//                                   << norm(A*x - b) << ")");
//                }
//                else {
//                    // use the SVD to calculate the reference values
//                    const Size n = A.columns();
//                    Array xr(n, 0.0);
//
//                    SVD svd(final A);
//                    const Matrix& V = svd.V();
//                    const Matrix& U = svd.U();
//                    const Array&  w = svd.singularValues();
//                    const Real threshold = n*QL_EPSILON;
//
//                    for (Size i=0; i<n; ++i) {
//                        if (w[i] > threshold) {
//                            const Real u = std::inner_product(U.column_begin(i),
//                                                              U.column_end(i),
//                                                              b.begin(), 0.0)/w[i];
//
//                            for (Size j=0; j<n; ++j) {
//                                xr[j]  +=u*V[j][i];
//                            } - eins
//                        }
//                    }
//
//                    if (norm(xr-x) > tol) {
//                        BOOST_FAIL("least square solution does not match (norm = "
//                                   << norm(x - xr) << ")");
//
//                    }
//                }
//            }
//        }
//    }

    @Test
    public void testInverse() {

        logger.info("Testing LU inverse calculation...");

        final double tol = 1.0e-12;
        final Matrix testMatrices[] = { M1, M2, I, M5 };

        for (final Matrix A : testMatrices) {
            final Matrix invA = A.inverse();

            final Matrix I1 = invA.mul(A);
            final Matrix I2 = A.mul(invA);

            final Matrix eins = new Identity(A.rows());

            if (norm(I1.sub(eins)) > tol)
                fail("inverse(A)*A does not recover unit matrix");

            if (norm(I2.sub(eins)) > tol)
                fail("A*inverse(A) does not recover unit matrix");
        }
    }

//    void MatricesTest::testDeterminant() {
//
//        BOOST_MESSAGE("Testing LU determinant calculation...");
//
//        setup();
//        final Real tol = 1e-10;
//
//        final Matrix testMatrices[] = {M1, M2, M5, M6, I};
//        // expected results calculated with octave
//        final Real expected[] = { 0.044, -0.012, 5.0, 5.7621e-11, 1.0};
//
//        for (final Size i=0; i < LENGTH(testMatrices); ++i) {
//            const final Real calculated = determinant(testMatrices[i]);
//            if (std::fabs(expected[i] - calculated) > tol)
//                BOOST_FAIL("determinant calculation failed "
//                           << "\n matrix     :\n" << testMatrices[i]
//                           << "\n calculated : " << calculated
//                           << "\n expected   : " << expected[i]);
//        }
//
//        MersenneTwisterUniformRng rng(1234);
//        for (Size i=0; i < 100; ++i) {
//            Matrix m(3,3, 0.0);
//            for (Matrix::iterator iter = final m.begin(); iter != m.end(); ++iter)
//                *iter = rng.next().value;
//
//            if (!(i%3)) {
//                // every third matrix is a singular matrix
//                Size row = Size(3*rng.next().value);
//                std::fill(m.row_begin(row), m.row_end(row), 0.0);
//            }
//
//            const Real& a=m[0][0];
//            const Real& b=m[0][1];
//            const Real& c=m[0][2];
//            const Real& d=m[1][0];
//            const Real& e=m[1][1];
//            const Real& f=m[1][2];
//            const Real& g=m[2][0];
//            const Real& h=m[2][1];
//            const Real& i=m[2][2];
//
//            const Real expected = a*e*i+b*f*g+c*d*h-(g*e*c+h*f*a+i*d*b);
//            const Real calculated = determinant(m);
//
//            if (std::fabs(expected-calculated) > tol)
//                BOOST_FAIL("determinant calculation failed "
//                           << "\n matrix     :\n" << m
//                           << "\n calculated : " << calculated
//                           << "\n expected   : " << expected);
//        }
//    }

//    void MatricesTest::testOrthogonalProjection() {
//        BOOST_MESSAGE("Testing orthogonal projections...");
//
//        Size dimension = 1000;
//        Size numberVectors = 50;
//        Real multiplier = 100;
//        Real tolerance = 1e-6;
//        unsigned long seed = 1;
//
//        Real errorAcceptable = 1E-11;
//
//        Matrix test(numberVectors,dimension);
//
//        MersenneTwisterUniformRng rng(seed);
//
//        for (Size i=0; i < numberVectors; ++i)
//            for (Size j=0; j < dimension; ++j)
//                test[i][j] = rng.next().value;
//
//        OrthogonalProjections projector(final test,
//                                        multiplier,
//                                        tolerance  );
//
//        Size numberFailures =0;
//        Size failuresTwo=0;
//
//        for (Size i=0; i < numberVectors; ++i)
//        {
//            // check output vector i is orthogonal to all other vectors
//
//            if (projector.validVectors()[i])
//            {
//                for (Size j=0; j < numberVectors; ++j)
//                      if (projector.validVectors()[j] && i != j)
//                      {
//                          Real dotProduct=0.0;
//                          for (Size k=0; k < dimension; ++k)
//                              dotProduct += test[j][k]*projector.GetVector(i)[k];
//
//                          if (fabs(dotProduct) > errorAcceptable)
//                              ++numberFailures;
//
//                      }
//
//               Real innerProductWithOriginal =0.0;
//               Real normSq =0.0;
//
//               for (Size j=0; j < dimension; ++j)
//               {
//                    innerProductWithOriginal +=   projector.GetVector(i)[j]*test[i][j];
//                    normSq += test[i][j]*test[i][j];
//               }
//
//               if (fabs(innerProductWithOriginal-normSq) > errorAcceptable)
//                   ++failuresTwo;
// - eins
//            }
//
//        }
//
//        if (numberFailures > 0 || failuresTwo >0)
//            BOOST_FAIL("OrthogonalProjections test failed with " << numberFailures << " failures  of orthogonality and "
//                        << failuresTwo << " failures of projection size.");
//
//    }




    //
    // private methods
    //

    private double norm(final Array v) {
        final double result = Math.sqrt(v.dotProduct(v));
        return result;
    }

    private double norm(final Matrix m) {
        double sum = 0.0;
        for (int i=0; i<m.rows; i++)
            for (int j=0; j<m.cols; j++)
                sum += m.get(i, j) * m.get(i, j);

        final double result = Math.sqrt(sum);
        return result;
    }

}
