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

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.Constants;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Cells;
import org.jquantlib.math.matrixutilities.Identity;
import org.jquantlib.math.matrixutilities.Matrix;
import org.jquantlib.math.matrixutilities.QRDecomposition;
import org.jquantlib.math.matrixutilities.SVD;
import org.jquantlib.math.matrixutilities.SymmetricSchurDecomposition;
import org.jquantlib.math.matrixutilities.Matrix.ColumnIterator;
import org.jquantlib.math.matrixutilities.Matrix.RowIterator;
import org.junit.Test;

/**
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q0_UNFINISHED, version = Version.V097, reviewers = { "" })
public class MatrixTest {

    //XXX private final int N;
    private final Matrix M1, M2, M3, M4, M5, M6, M7;
    private final Matrix I;


    public MatrixTest() {
        QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");

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

        I = new Identity(3);

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
        testEquals(Cells.Style.JAVA,    Cells.Style.JAVA);
        testEquals(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        testEquals(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        testEquals(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void testEquals(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleB);

        if (!mA.equals(mB)) fail("'equals' failed");
    }



    @Test
    public void testClone() { // final Cells.Style style
        testClone(Cells.Style.JAVA,    Cells.Style.JAVA);
        testClone(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        testClone(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        testClone(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void testClone(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleB);

        final Matrix m = mA.clone();
        if (m == mA) fail("'clone' must return a new instance");
        if (m == mB) fail("'clone' must return a new instance");
        if (!m.equals(mB)) fail("'clone' failed");
    }



    @Test
    public void toArray() { // final Cells.Style style
        toArray(Cells.Style.JAVA);
        toArray(Cells.Style.FORTRAN);
    }

    private void toArray(final Cells.Style style) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, style);

        final double[][] doubles = new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        };

        double[][] result = (double[][]) mA.toArray();
        for (int row=0; row<mA.rows(); row++)
            for (int col=0; col<mA.columns(); col++)
                if (result[row][col] != doubles[row][col])
                    fail("toArray failed");

        result = mA.toArray(new double[3][4]);
        for (int row=0; row<mA.rows(); row++)
            for (int col=0; col<mA.columns(); col++)
                if (result[row][col] != doubles[row][col])
                    fail("toArray failed");
    }

    @Test
    public void empty() { // final Cells.Style style
        empty(Cells.Style.JAVA);
        empty(Cells.Style.FORTRAN);
    }

    private void empty(final Cells.Style style) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, style);

        if (mA.empty()) fail("'empty' failed");
    }



    @Test
    public void fill() { // final Cells.Style style
        fill(Cells.Style.JAVA);
        fill(Cells.Style.FORTRAN);
    }

    private void fill(final Cells.Style style) {
        final Matrix mA = new Matrix(new double[][] {
                { 2.0, 2.0, 2.0, 2.0 },
                { 2.0, 2.0, 2.0, 2.0 },
                { 2.0, 2.0, 2.0, 2.0 },
        }, style);

        final Matrix m = new Matrix(3, 4, style).fill(2.0);
        if (!m.equals(mA)) fail("'fill' failed");
    }



    @Test
    public void rangeRow() { // final Cells.Style style
        rangeRow(Cells.Style.JAVA,    Cells.Style.JAVA);
        rangeRow(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        rangeRow(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        rangeRow(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void rangeRow(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 0.0, 0.0, 0.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 7.0, 7.0, 7.0 },
                { 1.0, 8.0, 8.0, 8.0 },
                { 1.0, 9.0, 9.0, 9.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 0.0, -1.0, -2.0 },
        }, styleA);

        final Matrix mC = new Matrix(new double[][] {
                { 1.0 },
                { 2.0 },
                { 3.0 },
                { 4.0 },
                { 5.0 },
        }, styleA);

        final Array aA = new Array(new double[] { 1.0, 7.0, 7.0, 7.0 }, styleB);
        final Array aB = new Array(new double[] { 1.0, 0.0, -1.0, -2.0 }, styleB);
        final Array aC = new Array(new double[] { 3.0 }, styleB);

        if (!mA.rangeRow(2+mA.base()).equals(aA)) fail("'rangeRow' failed");
        if (!mB.rangeRow(0+mB.base()).equals(aB)) fail("'rangeRow' failed");
        if (!mC.rangeRow(2+mC.base()).equals(aC)) fail("'rangeRow' failed");
    }


    @Test
    public void rangeCol() { // final Cells.Style style
        rangeCol(Cells.Style.JAVA,    Cells.Style.JAVA);
        rangeCol(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        rangeCol(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        rangeCol(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void rangeCol(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 0.0, 0.0, 0.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 7.0, 7.0, 7.0 },
                { 1.0, 8.0, 8.0, 8.0 },
                { 1.0, 9.0, 9.0, 9.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 0.0, -1.0, -2.0 },
        }, styleA);

        final Matrix mC = new Matrix(new double[][] {
                { 1.0 },
                { 2.0 },
                { 3.0 },
                { 4.0 },
                { 5.0 },
        }, styleA);

        final Array aA = new Array(new double[] {  0.0, 3.0, 7.0, 8.0, 9.0 }, styleB);
        final Array aB = new Array(new double[] { -1.0 },                     styleB);
        final Array aC = new Array(new double[] {  1.0, 2.0, 3.0, 4.0, 5.0 }, styleB);

        //if (!mA.rangeCol(2+mA.base()).equals(aA)) fail("'rangeCol' failed");
        //if (!mB.rangeCol(2+mB.base()).equals(aB)) fail("'rangeCol' failed");
        //if (!mC.rangeCol(0+mC.base()).equals(aC)) fail("'rangeCol' failed");

        if (!mA.rangeCol(2+mA.base(), 1+mA.base()).equals(aA.range(1+aA.base()))) fail("'rangeCol' failed");
        if (!mC.rangeCol(0+mC.base(), 2+mC.base()).equals(aC.range(2+aC.base()))) fail("'rangeCol' failed");

        if (!mA.rangeCol(2+mA.base(), 1+mA.base(), 3+mA.base()).equals(aA.range(1+aA.base(), 3+aA.base()))) fail("'rangeCol' failed");
        if (!mC.rangeCol(0+mC.base(), 2+mC.base(), 3+mC.base()).equals(aC.range(2+aC.base(), 3+aC.base()))) fail("'rangeCol' failed");
    }


    @Test
    public void addAssign() { // final Cells.Style style
        addAssign(Cells.Style.JAVA,    Cells.Style.JAVA);
        addAssign(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        addAssign(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        addAssign(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void addAssign(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 3.0, 2.0, 1.0 },
                { 5.0, 4.0, 3.0, 2.0 },
                { 6.0, 5.0, 4.0, 3.0 },
                { 7.0, 6.0, 5.0, 4.0 },
        }, styleB);


        final Matrix m = mA.addAssign(mB);
        if (m != mA) fail("addAssign must return <this>");

        for (int row=m.base(); row<m.rows()+m.base(); row++)
            for (int col=m.base(); col<m.columns()+m.base(); col++)
                if (m.get(row, col) != row-m.base()+5)
                    fail("addAssign failed");
    }


    @Test
    public void subAssign() { // final Cells.Style style
        subAssign(Cells.Style.JAVA,    Cells.Style.JAVA);
        subAssign(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        subAssign(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        subAssign(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void subAssign(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 5.0, 6.0,  7.0 },
                { 5.0, 6.0, 7.0,  8.0 },
                { 6.0, 7.0, 8.0,  9.0 },
                { 7.0, 8.0, 9.0, 10.0 },
        }, styleB);


        final Matrix m = mB.subAssign(mA);
        if (m != mB) fail("subAssign must return <this>");

        for (int row=m.base(); row<m.rows()+m.base(); row++)
            for (int col=m.base(); col<m.columns()+m.base(); col++)
                if (m.get(row, col) != row-m.base()+3)
                    fail("subAssign failed");
    }



    @Test
    public void mulAssign() { // final Cells.Style style
        mulAssign(Cells.Style.JAVA);
        mulAssign(Cells.Style.FORTRAN);
    }

    public void mulAssign(final Cells.Style style) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, style);

        final Matrix m = mA.mulAssign(2.5);
        if (m != mA) fail("mulAssign must return <this>");

        for (int row=m.base(); row<m.rows()+m.base(); row++)
            for (int col=m.base(); col<m.columns()+m.base(); col++)
                if (m.get(row, col) != (col-m.base()+1)*2.5)
                    fail("mulAssign failed");
    }


    @Test
    public void divAssign() { // final Cells.Style style
        divAssign(Cells.Style.JAVA);
        divAssign(Cells.Style.FORTRAN);
    }

    private void divAssign(final Cells.Style style) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, style);

        final Matrix m = mA.divAssign(2.5);
        if (m != mA) fail("divAssign must return <this>");

        for (int row=m.base(); row<m.rows()+m.base(); row++)
            for (int col=m.base(); col<m.columns()+m.base(); col++)
                if (m.get(row, col) != (col-m.base()+1)/2.5)
                    fail("divAssign failed");
    }


    @Test
    public void add() { // final Cells.Style style
        add(Cells.Style.JAVA,    Cells.Style.JAVA);
        add(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        add(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        add(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    public void add(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 3.0, 2.0, 1.0 },
                { 5.0, 4.0, 3.0, 2.0 },
                { 6.0, 5.0, 4.0, 3.0 },
                { 7.0, 6.0, 5.0, 4.0 },
        }, styleB);


        final Matrix m = mA.add(mB);
        if (m == mA) fail("'add' must return a new instance");
        if (m.rows() != mA.rows() || m.columns() != mA.columns()) fail("'add' failed");

        for (int row=m.base(); row<m.rows()+m.base(); row++)
            for (int col=m.base(); col<m.columns()+m.base(); col++)
                if (m.get(row, col) != row-m.base()+5)
                    fail("'add' failed");
    }


    @Test
    public void sub() { // final Cells.Style style
        sub(Cells.Style.JAVA,    Cells.Style.JAVA);
        sub(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        sub(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        sub(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void sub(final Cells.Style styleA, final Cells.Style styleB) {

        // matrix

        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 5.0, 6.0,  7.0 },
                { 5.0, 6.0, 7.0,  8.0 },
                { 6.0, 7.0, 8.0,  9.0 },
                { 7.0, 8.0, 9.0, 10.0 },
        }, styleB);


        Matrix m = mB.sub(mA);
        if (m == mB) fail("'sub' must return a new instance");
        if (m.rows() != mB.rows() || m.columns() != mB.columns()) fail("'sub' failed");

        for (int row=m.base(); row<m.rows()+m.base(); row++)
            for (int col=m.base(); col<m.columns()+m.base(); col++)
                if (m.get(row, col) != row-m.base()+3)
                    fail("'sub' failed");


        // scalar

        final Matrix mC = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        m = mC.mul(2.5);
        if (m == mC) fail("'mul' must return a new instance");
        if (m.rows() != mC.rows() || m.columns() != mC.columns()) fail("'mul' failed");

        for (int row=m.base(); row<m.rows()+m.base(); row++)
            for (int col=m.base(); col<m.columns()+m.base(); col++)
                if (m.get(row, col) != (col-m.base()+1)*2.5)
                    fail("'mul' failed");


        // array

        final Matrix mD = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        final Array aD = new Array(new double[] { 1.0, 1.0, 1.0, 1.0 }, styleB);

        final Array a = mD.mul(aD);
        if (a.size() != mD.columns()) fail("'add' failed");

        for (int col=a.base(); col<a.columns()+a.base(); col++)
            if (a.get(col) != (col-a.base()+1)*5)
                fail("'mul' failed");

    }



    @Test
    public void div() { // final Cells.Style style
        div(Cells.Style.JAVA);
        div(Cells.Style.FORTRAN);
    }

    private void div(final Cells.Style style) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, style);

        final Matrix m = mA.div(2.5);
        if (m == mA) fail("'div' must return a new instance");
        if (m.rows() != mA.rows() || m.columns() != mA.columns()) fail("'add' failed");

        for (int row=m.base(); row<m.rows()+m.base(); row++)
            for (int col=m.base(); col<m.columns()+m.base(); col++)
                if (m.get(row, col) != (col-m.base()+1)/2.5)
                    fail("'div' failed");
    }



    @Test
    public void mul() { // final Cells.Style style
        mul(Cells.Style.JAVA,    Cells.Style.JAVA);
        mul(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        mul(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        mul(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void mul(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mI = new Matrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 },
        }, styleA);

        final Matrix mA = new Matrix(new double[][] {
                { 4.0, 3.0, 2.0, 1.0 },
                { 5.0, 4.0, 3.0, 2.0 },
                { 6.0, 5.0, 4.0, 3.0 },
                { 7.0, 6.0, 5.0, 4.0 },
        }, styleB);

        final Matrix m = mI.mul(mA);
        if (m == mI) fail("'mul' must return a new instance");
        if (m == mA) fail("'mul' must return a new instance");
        if (!m.equals(mA)) fail("'mul' failed");
    }


    @Test
    public void swap() { // final Cells.Style style
        swap(Cells.Style.JAVA,    Cells.Style.JAVA);
        swap(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        swap(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        swap(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void swap(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 4.0, 3.0, 2.0, 1.0 },
                { 5.0, 4.0, 3.0, 2.0 },
                { 6.0, 5.0, 4.0, 3.0 },
                { 7.0, 6.0, 5.0, 4.0 },
        }, styleB);

        final Matrix mAclone = mA.clone();
        final Matrix mBclone = mB.clone();

        mA.swap(mB);
        if (!mA.equals(mBclone)) fail("'swap' failed");
        if (!mB.equals(mAclone)) fail("'swap' failed");
    }


    @Test
    public void transpose() { // final Cells.Style style
        transpose(Cells.Style.JAVA,    Cells.Style.JAVA);
        transpose(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        transpose(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        transpose(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void transpose(final Cells.Style styleA, final Cells.Style styleB) {
        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
                { 1.0, 2.0, 3.0, 4.0 },
        }, styleA);

        final Matrix mB = new Matrix(new double[][] {
                { 1.0, 1.0, 1.0 },
                { 2.0, 2.0, 2.0 },
                { 3.0, 3.0, 3.0 },
                { 4.0, 4.0, 4.0 },
        }, styleB);

        final Matrix m = mA.transpose();

        if (m == mA) fail("'transpose' must return a new instance");
        if (m == mB) fail("'transpose' must return a new instance");
        if (!m.equals(mB)) fail("'transpose' failed");
    }


    @Test
    public void diagonal() { // final Cells.Style style
        diagonal(Cells.Style.JAVA,    Cells.Style.JAVA);
        diagonal(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        diagonal(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        diagonal(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void diagonal(final Cells.Style styleA, final Cells.Style styleB) {

        final Matrix mA = new Matrix(new double[][] {
                { 1.0, 9.0, 9.0, 9.0 },
                { 9.0, 2.0, 9.0, 9.0 },
                { 9.0, 9.0, 3.0, 9.0 },
                { 9.0, 9.0, 9.0, 4.0 },
        }, styleA);

        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleB);

        if (!mA.diagonal().equals(aA)) fail("'transpose' failed");
    }



    @Test
    public void inverse() { // final Cells.Style style
        inverse(Cells.Style.JAVA,    Cells.Style.JAVA);
        inverse(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        inverse(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        inverse(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void inverse(final Cells.Style styleA, final Cells.Style styleB) {

        QL.info("Testing LU inverse calculation...");

        final double tol = 1.0e-12;
        final Matrix testMatrices[] = { M1, M2, I, M5 };

        for (final Matrix m : testMatrices) {

            final Matrix A = new Matrix(m, styleA);

            final Matrix invA = A.inverse();

            final Matrix I1 = invA.mul(A);
            final Matrix I2 = A.mul(invA);

            final Matrix eins = new Identity(A.rows(), styleB);

            if (norm(I1.sub(eins)) > tol)
                fail("inverse(A)*A does not recover unit matrix");

            if (norm(I2.sub(eins)) > tol)
                fail("A*inverse(A) does not recover unit matrix");
        }
    }




    @Test
    public void testRowIterator() { // final Cells.Style style
        testRowIterator(Cells.Style.JAVA);
        testRowIterator(Cells.Style.FORTRAN);
    }

    private void testRowIterator(final Cells.Style style) {
        RowIterator it;
        int cells;
        double sum;

        final Matrix mA = new Matrix(new double[][] {
              { 1.0,  0.9,  0.7 },
              { 0.9,  1.0,  0.3 },
              { 0.7,  0.3,  1.0 }
          }, style);

        final int base = mA.base();

        it = mA.rowIterator(base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==6 && Closeness.isCloseEnough(sum, 5.2))) fail("RowIterator failed");

        it = mA.rowIterator(1+base, 1+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==4 && Closeness.isCloseEnough(sum, 2.6))) fail("RowIterator failed");

        it = mA.rowIterator(2+base, 2+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==2 && Closeness.isCloseEnough(sum, 2.0))) fail("RowIterator failed");

        it = mA.rowIterator(2+base, 3+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==0)) fail("RowIterator failed");

        it = mA.rowIterator(0+base, 0+base, 3+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==6 && Closeness.isCloseEnough(sum, 5.2))) fail("RowIterator failed");

        it = mA.rowIterator(0+base, 0+base, 2+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==4 && Closeness.isCloseEnough(sum, 3.8))) fail("RowIterator failed");

        it = mA.rowIterator(0+base, 0+base, 1+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==2 && Closeness.isCloseEnough(sum, 2.0))) fail("RowIterator failed");

        it = mA.rowIterator(0+base, 0+base, 0+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==0)) fail("RowIterator failed");

        // test columns out of bounds

        try {
            it = mA.rowIterator(0+base, -1+base);
            fail("RowIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

        try {
            it = mA.rowIterator(0+base, 4+base);
            fail("RowIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

        try {
            it = mA.rowIterator(0+base, 2+base, 4+base);
            fail("RowIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

        // test rows out of bounds

        try {
            it = mA.rowIterator(-1+base);
            fail("RowIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

        try {
            it = mA.rowIterator(3+base);
            fail("RowIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

    }


    @Test
    public void testColumnIterator() { // final Cells.Style style
        testColumnIterator(Cells.Style.JAVA);
        testColumnIterator(Cells.Style.FORTRAN);
    }

    public void testColumnIterator(final Cells.Style style) {
        ColumnIterator it;
        int cells;
        double sum;

        final Matrix mA = new Matrix(new double[][] {
              { 1.0,  0.9,  0.7 },
              { 0.9,  1.0,  0.3 },
              { 0.7,  0.3,  1.0 }
          }, style);

        final int base = mA.base();

        it = mA.columnIterator(base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==6 && Closeness.isCloseEnough(sum, 5.2))) fail("ColumnIterator failed");

        it = mA.columnIterator(1+base, 1+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==4 && Closeness.isCloseEnough(sum, 2.6))) fail("ColumnIterator failed");

        it = mA.columnIterator(2+base, 2+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==2 && Closeness.isCloseEnough(sum, 2.0))) fail("ColumnIterator failed");

        it = mA.columnIterator(2+base, 3+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==0)) fail("ColumnIterator failed");

        it = mA.columnIterator(0+base, 0+base, 3+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==6 && Closeness.isCloseEnough(sum, 5.2))) fail("ColumnIterator failed");

        it = mA.columnIterator(0+base, 0+base, 2+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==4 && Closeness.isCloseEnough(sum, 3.8))) fail("ColumnIterator failed");

        it = mA.columnIterator(0+base, 0+base, 1+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==2 && Closeness.isCloseEnough(sum, 2.0))) fail("ColumnIterator failed");

        it = mA.columnIterator(0+base, 0+base, 0+base);
        cells = 0; sum = 0.0;
        while (it.hasNext()) {
            sum += it.nextDouble();
            cells++;
        }
        while (it.hasPrevious()) {
            sum += it.previousDouble();
            cells++;
        }
        if (!(cells==0)) fail("ColumnIterator failed");

        // test columns out of bounds

        try {
            it = mA.columnIterator(0+base, -1+base);
            fail("ColumnIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

        try {
            it = mA.columnIterator(0+base, 4+base);
            fail("ColumnIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

        try {
            it = mA.columnIterator(0+base, 2+base, 4+base);
            fail("ColumnIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

        // test rows out of bounds

        try {
            it = mA.columnIterator(-1+base);
            fail("ColumnIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

        try {
            it = mA.columnIterator(3+base);
            fail("ColumnIterator failed");
        } catch (final Exception e) {
            // test succeeded :)
        }

    }


    @Test
    public void testEigenvectors() {

        QL.info("Testing eigenvalues and eigenvectors calculation...");

        final Matrix testMatrices[] = { M1, M2 };

        for (final Matrix M : testMatrices) {

            final SymmetricSchurDecomposition schur = M.schur();
            final Array eigenValues = schur.eigenvalues();
            final Matrix eigenVectors = schur.eigenvectors();
            double minHolder = Constants.QL_MAX_REAL;

            final int N = M.columns();

            for (int i=0; i<N; i++) {
                final Array v = new Array(N);
                for (int j=0; j<N; j++)
                    v.set( j, eigenVectors.get(j,i) );
                // check definition
                final Array a = M.mul(v);
                final Array b = v.mul(eigenValues.get(i));
                final double tol = norm(a.sub(b));
                if (tol > 1.0e-15)
                    fail("Eigenvector definition not satisfied");
                // check decreasing ordering
                if (eigenValues.get(i) >= minHolder) {
                    fail("Eigenvalues not ordered");
                } else
                    minHolder = eigenValues.get(i);
            }

            // check normalization
            final Matrix m = eigenVectors.mul(eigenVectors.transpose());
            final Identity ID = new Identity(N);
            final double tol = norm(m.sub(ID));
            if (tol > 1.0e-15)
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

        QL.info("Testing singular value decomposition...");

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

            Identity ID;

            // tests
            final Matrix U_Utranspose = U.transpose().mul(U);
            ID = new Identity(U_Utranspose.columns());
            if (norm(U_Utranspose.sub(ID)) > tol)
                fail("U not orthogonal");

            final Matrix V_Vtranspose = V.transpose().mul(V);
            ID = new Identity(V_Vtranspose.columns());
            if (norm(V_Vtranspose.sub(ID)) > tol)
                fail("V not orthogonal");

            final Matrix A_reconstructed = U.mul(S).mul(V.transpose());
            if (norm(A_reconstructed.sub(A)) > tol)
                fail("Product does not recover A");
        }
    }


    @Test
    public void testQRDecomposition() {

        QL.info("Testing QR decomposition...");

        final double tolerance = 1.0e-12;

        //final Matrix testMatrices[] = { M1, M2, I, M3, M3.transpose(), M4, M4.transpose(), M5 };

        final Matrix T1 = new Matrix( new double[][] {
                {1, -1,  4},
                {1,  4, -2},
                {1,  4,  2},
                {1, -1,  0}
            });

        final Matrix T1_R = new Matrix( new double[][] {
                {  2,  3,  2 },
                {  0,  5, -2 },
                {  0,  0,  4 }
            });

        final Matrix T1_Q = new Matrix( new double[][] {
                {  1/2,  -1/2,  1/2,  1/2 },
                {  1/2,   1/2, -1/2, -1/2 },
                {  1/2,   1/2,  1/2,  1/2 },
                {  1/2,  -1/2, -1/2,  1/2 },
            });




        final Matrix testMatrices[] = { T1 };

        final MinPack minpack = new MinPack();

        for (final Matrix A : testMatrices) {

//            final double[][] b = (double[][]) A.toArray(Cells.Style.FORTRAN);
//
//            final double[][] mT = new double[A.columns()][A.rows()];
//            for (int i=0; i<A.rows(); i++) {
//                for (int j=0; j<A.columns(); j++) {
//                    mT[j][i] = A.get(i, j);
//                }
//            }
//
////            final double[][] B = new double[mT.length+1][mT[0].length+1];
////            for (int i=0; i<mT.length; i++) {
////                for (int j=0; j<mT[0].length; j++) {
////                    // B[i+1][j+1] = A.get(i, j);
////                    B[i+1][j+1] = mT[i][j];
////                }
////            }
//
//            final int m = mT.length;
//            final int n = mT[0].length;
//            final double rdiag[] = new double[n+1];
//            final double acnorm[] = new double[n+1];
//            final double wa[] = new double[m+1];
//            final int lipvt[] = new int[m+1];
//            final boolean pivot = true;
//
//            final int OFFSET = 1;
//
//
//            minpack.qrfac_f77(m, n, b, pivot, lipvt, rdiag, rdiag, wa);
//
//            // obtain R
//
//            final double[][] r = new double[n][n];
//
//            for (int i=0; i < n; ++i) {
//                r[i][i] = rdiag[i+OFFSET];
//                if (i < m) {
//                    // std::copy(mT.column_begin(i)+i+1, mT.column_end(i), r.row_begin(i)+i+1);
//                    for (int k=i+1; k < n; k++) {
//                        r[i][k] = mT[k][i];
//                    }
//                }
//            }
//
//            // obtain Q
//
//            final double[][] q = new double[m][n];
//
//            final double w[] = new double[m];
//            for (int k=0; k < m; k++) {
//                //XXX std::fill(w.begin(), w.end(), 0.0);
//                Arrays.fill(w, 0.0);
//                w[k] = 1.0;
//
//                for (int j=0; j < Math.min(n, m); j++) {
//                    final double t3 = mT[j][j];
//                    if (t3 != 0.0) {
//
//                        // final double t = std::inner_product(mT.row_begin(j)+j, mT.row_end(j), w.begin()+j, 0.0)/t3;
//                        double t = 0.0;
//                        for (int kk=j; kk<Math.min(n, m); kk++) {
//                            t += mT[j][kk] * w[j];
//                        }
//                        t /= t3;
//
//                        for (int i=j; i<m; ++i) {
//                            w[i] -= mT[j][i]*t;
//                        }
//                    }
//                    q[k][j] = w[j];
//                }
//
//                //XXX std::fill(q.row_begin(k) + std::min(n, m), q.row_end(k), 0.0);
//            }
//
//            // obtain ipvt
//
//            final int ipvt_[] = new int[n];
//            if (pivot) {
//                // std::copy(lipvt.get(), lipvt.get()+n, ipvt.begin());
//                for (int i=0; i<n; i++) {
//                    ipvt_[i] = lipvt[i+OFFSET] - OFFSET;
//                }
//            } else {
//                for (int i=0; i < n; ++i)
//                    ipvt_[i] = i;
//            }


            QRDecomposition qr;
            Matrix Q;
            Matrix R;
            Matrix mul1;
            Matrix mul2;
            double tol;


            // QR decomposition with column pivoting
            qr = new Matrix(A, Cells.Style.FORTRAN).qr(true);
            R = qr.R();
            Q = qr.Q();
            final int[] ipvt = qr.pivot();

//            Q = new Matrix(q);
//            R = new Matrix(r);
//            final int[] ipvt = ipvt_.clone();

            final Matrix P = new Matrix(A.columns(), A.columns());

            // reverse column pivoting
            for (int i=0; i < P.columns(); ++i) {
                P.set(ipvt[i], i, 1.0);
            }

            mul1 = Q.mul(R);
            mul2 = A.mul(P);
            tol = norm( mul1.sub(mul2) ); // norm(Q*R - A*P)
            if (tol > tolerance)
                System.out.println("Q*R (pivot=true) does not match matrix A*P");
            else
                System.out.println("OK");


            // QR decomposition without column pivoting
            qr = new Matrix(A, Cells.Style.FORTRAN).qr();
            R = qr.R();
            Q = qr.Q();

            mul1 = Q.mul(R);
            tol = norm(mul1.sub(A)); // norm(Q*R - A)
            if (tol > tolerance)
                System.out.println("Q*R (pivot=false) does not match matrix A*P");
            else
                System.out.println("OK");


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
//        for (Size i=0; i < std::min(bigM.rows()(), bigM.columns()); ++i) {
//            bigM[i][i] = i+1.0;
//        }
//        Matrix testMatrices[] = { M1, M2, M3, transpose(M3),
//                                  M4, transpose(M4), M5, I, M7, bigM, transpose(bigM) };
//
//        for (Size j = 0; j < LENGTH(testMatrices); j++) {
//            const Matrix& A = testMatrices[j];
//            Array b(A.rows()());
//
//            for (Size k=0; k < 10; ++k) {
//                for (Array::iterator iter = b.begin(); iter != b.end(); ++iter) {
//                    *iter = rng.next().value;
//                }
//                const Array x = qrSolve(A, b, true);
//
//                if (A.columns() >= A.rows()()) {
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
        for (int i=m.base(); i<m.rows()+m.base(); i++)
            for (int j=m.base(); j<m.columns()+m.base(); j++)
                sum += m.get(i, j) * m.get(i, j);

        final double result = Math.sqrt(sum);
        return result;
    }

}
