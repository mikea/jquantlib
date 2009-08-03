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

import org.jquantlib.math.Closeness;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Matrix;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Richard Gomes
 */
public class MatrixTest {

    private final static Logger logger = LoggerFactory.getLogger(MatrixTest.class);

    public MatrixTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
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

        final Matrix result = mA.clone();
        if (result == mA) fail("'clone' must return a new instance");
        if (result == mB) fail("'clone' must return a new instance");
        if (!result.equals(mB)) fail("'clone' failed");
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

        final Matrix result = new Matrix(3,4).fill(2.0);
        if (!result.equals(mA)) fail("'fill' failed");
    }

    @Test
    public void copyOfRange() {
        // TODO: to be done
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


        final Matrix result = mA.addAssign(mB);
        if (result != mA) fail("addAssign must return <this>");

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


        final Matrix result = mB.subAssign(mA);
        if (result != mB) fail("subAssign must return <this>");

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


        final Matrix result = mA.mulAssign(2.5);
        if (result != mA) fail("mulAssign must return <this>");

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


        final Matrix result = mA.divAssign(2.5);
        if (result != mA) fail("divAssign must return <this>");

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


        final Matrix result = mA.add(mB);
        if (result == mA) fail("'add' must return a new instance");
        if (result.rows != mA.rows || result.cols != mA.cols) fail("'add' failed");

        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (result.get(row, col) != row+5)
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


        Matrix result = mB.sub(mA);
        if (result == mB) fail("'sub' must return a new instance");
        if (result.rows != mB.rows || result.cols != mB.cols) fail("'sub' failed");

        for (int row=0; row<mB.rows; row++)
            for (int col=0; col<mB.cols; col++)
                if (result.get(row, col) != row+3)
                    fail("'sub' failed");


        // scalar

        final Matrix mC = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        });

        result = mC.mul(2.5);
        if (result == mC) fail("'mul' must return a new instance");
        if (result.rows != mC.rows || result.cols != mC.cols) fail("'mul' failed");

        for (int row=0; row<mC.rows; row++)
            for (int col=0; col<mC.cols; col++)
                if (result.get(row, col) != (col+1)*2.5)
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

        result = mD.mul(aD);
        if (result == mD) fail("'mul' must return a new instance");
        if (result.rows != 1 || result.cols != mD.cols) fail("'add' failed");

        for (int col=0; col<mD.cols; col++)
            if (result.get(col) != (col+1)*5)
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


        final Matrix result = mA.div(2.5);
        if (result == mA) fail("'div' must return a new instance");
        if (result.rows != mA.rows || result.cols != mA.cols) fail("'add' failed");

        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (result.get(row, col) != (col+1)/2.5)
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

        final Matrix result = mI.mul(mA);
        if (result == mI) fail("'mul' must return a new instance");
        if (result == mA) fail("'mul' must return a new instance");
        if (!result.equals(mA)) fail("'mul' failed");
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

        final Matrix result = mA.transpose();

        if (result == mA) fail("'transpose' must return a new instance");
        if (result == mB) fail("'transpose' must return a new instance");
        if (!result.equals(mB)) fail("'transpose' failed");
    }

    @Test
    public void diagonal() {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 9.0, 9.0, 9.0 },
                { 9.0, 2.0, 9.0, 9.0 },
                { 9.0, 9.0, 3.0, 9.0 },
                { 9.0, 9.0, 9.0, 4.0 },
        });

        final Matrix aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        if (!mA.diagonal().equals(aA)) fail("'transpose' failed");
    }

    @Test
    public void inverse() {
        final Matrix mA = new Matrix(new double[][] {
                { 3.0, 5.0 },
                { 2.0, 3.0 },
        });

        final Matrix mB = new Matrix(new double[][] {
                { -3.0,  5.0 },
                {  2.0, -3.0 },
        });

        Matrix result = mA.inverse();
        if (result == mA) fail("'inverse' must return a new instance");

        for (int row=0; row<mA.rows; row++)
            for (int col=0; col<mA.cols; col++)
                if (Closeness.isCloseEnough( result.get(row,col), mB.get(row,col) ))
                    fail("'inverse' failed");

        final Matrix mC = new Matrix(new double[][] {
                {  2.0,  0.0, -1.0 },
                { -3.0,  0.0,  2.0 },
                { -2.0, -1.0,  0.0 },
        });

        final Matrix mD = new Matrix(new double[][] {
                {  3.0,  1.0,  0.0 },
                { -4.0, -2.0, -1.0 },
                {  3.0,  2.0,  0.0 },
        });

        result = mA.inverse();
        if (result == mC) fail("'inverse' must return a new instance");

        for (int row=0; row<mC.rows; row++)
            for (int col=0; col<mC.cols; col++)
                if (Closeness.isCloseEnough( result.get(row,col), mD.get(row,col) ))
                    fail("'inverse' failed");

    }

}
