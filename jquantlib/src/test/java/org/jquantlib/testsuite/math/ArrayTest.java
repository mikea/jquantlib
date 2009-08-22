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
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Cells;
import org.junit.Test;

/**
 *
 * @author Richard Gomes
 */
public class ArrayTest {

    public ArrayTest() {
        QL.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
    }


    @Test
    public void testEquals() {
        testEquals(Cells.Style.JAVA,    Cells.Style.JAVA);
        testEquals(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        testEquals(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        testEquals(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void testEquals(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleA);
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleB);

        if (!aA.equals(aB))
            fail("'equals' failed");
    }


    @Test
    public void testClone() {
        testClone(Cells.Style.JAVA,    Cells.Style.JAVA);
        testClone(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        testClone(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        testClone(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void testClone(final Cells.Style styleA, final Cells.Style styleB) {
        final Array mA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleA);
        final Array mB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleB);

        final Array result = mA.clone();
        if (result == mA) fail("'clone' must return a new instance");
        if (result == mB) fail("'clone' must return a new instance");
        if (!result.equals(mB)) fail("'clone' failed");
    }


    @Test
    public void abs() {
        abs(Cells.Style.JAVA,    Cells.Style.JAVA);
        abs(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        abs(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        abs(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void abs(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, -2.0, -3.0, 5.0, -9.0, -11.0, -12.0 }, styleA);
        final Array aB = new Array(new double[] { 1.0,  2.0,  3.0, 5.0,  9.0,  11.0,  12.0 }, styleB);

        final Array result = aA.abs();
        if (result == aA) fail("'abs' must return a new instance");
        if (result == aB) fail("'abs' must return a new instance");
        if (!result.equals(aB)) fail("'abs' failed");
    }


    @Test
    public void accumulate() {
        accumulate(Cells.Style.JAVA);
        accumulate(Cells.Style.FORTRAN);
    }

    private void accumulate(final Cells.Style style) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 }, style);

        if (aA.accumulate() != 45.0)
            fail("'accumulate' failed");
        if (aA.accumulate(2+aA.base(), 5+aA.base(), -2.0) != 10.0)
            fail("'accumulate' failed");
    }

    @Test
    public void add() {
        fail("add failed");
    }

    @Test
    public void addAssign() {
        fail("addAssign failed");
    }


    @Test
    public void adjacentDifference() {
        adjacentDifference(Cells.Style.JAVA,    Cells.Style.JAVA);
        adjacentDifference(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        adjacentDifference(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        adjacentDifference(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void adjacentDifference(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 5.0, 9.0, 11.0, 12.0 }, styleA);
        final Array aB = new Array(new double[] { 1.0, 1.0, 1.0, 2.0, 4.0,  2.0,  1.0 }, styleB);

        final Array result = aA.adjacentDifference();
        if (result == aA)
            fail("'adjacentDifferences' must return a new instance");
        if (result == aB)
            fail("'adjacentDifferences' must return a new instance");
        if (!result.equals(aB))
            fail("'adjacentDifferences' failed");
    }

    @Test
    public void apply() {
        fail("apply failed");
    }


    @Test
    public void range() {
        range(Cells.Style.JAVA,    Cells.Style.JAVA);
        range(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        range(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        range(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void range(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 9.0, 8.0, 1.0, 2.0, 3.0, 4.0, 8.0, 9.0 }, styleA);
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleB);

        final Array result = aA.range(2, 6);
        if (result == aA) fail("'copyOfRange' must return a new instance");
        if (result == aB) fail("'copyOfRange' must return a new instance");
        if (!result.equals(aB)) fail("'copyOfRange' failed");
    }

//    @Test
//    public void div() {
//    }
//
//    @Test
//    public void divAssign() {
//    }
//
//    @Test
//    public void dotProduct() {
//    }


    @Test
    public void exp() {
        exp(Cells.Style.JAVA,    Cells.Style.JAVA);
        exp(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        exp(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        exp(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void exp(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleA);
        final Array aB = new Array(new double[] { Math.exp(1), Math.exp(2), Math.exp(3), Math.exp(4) }, styleB);

        final Array result = aA.exp();
        if (result == aA) fail("'log' must return a new instance");
        if (result == aB) fail("'log' must return a new instance");
        if (!result.equals(aB)) fail("'log' failed");
    }


    @Test
    public void fill() {
        fill(Cells.Style.JAVA);
        fill(Cells.Style.FORTRAN);
    }

    private void fill(final Cells.Style style) {
        final Array aA = new Array(new double[] { 2.0, 2.0, 2.0, 2.0 }, style);

        final Array result = new Array(4).fill(2.0);
        if (!result.equals(aA)) fail("'fill' failed");
    }

    @Test
    public void first() {
        first(Cells.Style.JAVA);
        first(Cells.Style.FORTRAN);
    }

    private void first(final Cells.Style style) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, style);

        if (aA.first() != 1.0)
            fail("'first' failed");
    }

    @Test
    public void innerProduct() {
        fail("innerProduct failed");
    }


    @Test
    public void last() {
        last(Cells.Style.JAVA);
        last(Cells.Style.FORTRAN);
    }

    private void last(final Cells.Style style) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, style);

        if (aA.last() != 4.0)
            fail("'last' failed");
    }


    @Test
    public void log() {
        log(Cells.Style.JAVA,    Cells.Style.JAVA);
        log(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        log(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        log(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void log(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { Math.exp(1), Math.exp(2), Math.exp(3), Math.exp(4) }, styleA);
        final Array aB = new Array(new double[] { 1.0,  2.0,   3.0,    4.0 }, styleB);

        final Array result = aA.log();
        if (result == aA) fail("'log' must return a new instance");
        if (result == aB) fail("'log' must return a new instance");
        if (!result.equals(aB)) fail("'log' failed");
    }

    @Test
    public void lowerBound() {
        fail("lowerBound failed");
    }


    @Test
    public void min() {
        min(Cells.Style.JAVA);
        min(Cells.Style.FORTRAN);
    }

    public void min(final Cells.Style style) {
        final Array aA = new Array(new double[] { 0.0, 1.0, 2.0, -3.0, 4.0, 0.0, -6.0, 7.0, 8.0, 0.0 }, style);

        if (aA.min() != -6.0) fail("'min' failed");
        if (aA.min(2+aA.base(), 6+aA.base()) != -3.0) fail("'min' failed");
    }


    @Test
    public void max() {
        max(Cells.Style.JAVA);
        max(Cells.Style.FORTRAN);
    }

    private void max(final Cells.Style style) {
        final Array aA = new Array(new double[] { 0.0, 1.0, 2.0, -3.0, 4.0, 0.0, -6.0, 7.0, 8.0, 0.0 }, style);

        if (aA.max() != 8.0)
            fail("'max' failed");
        if (aA.max(2+aA.base(), 6+aA.base()) != 4.0)
            fail("'max' failed");
    }


    @Test
    public void mul() {
        fail("mul failed");
    }

    @Test
    public void mulAssign() {
        fail("mulAssign failed");
    }

    @Test
    public void outerProduct() {
        fail("outerProduct failed");
    }


    @Test
    public void sort() {
        sort(Cells.Style.JAVA,    Cells.Style.JAVA);
        sort(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        sort(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        sort(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void sort(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 9.0, 8.0, 2.0, 3.0, 1.0, 4.0, 8.0, 9.0 }, styleA);
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0, 8.0, 8.0, 9.0, 9.0 }, styleB);

        final Array result = aA.sort();
        if (result != aA)
            fail("'sort' must return <this>");
        if (!result.equals(aB))
            fail("'sort' failed");
    }


    @Test
    public void sqrt() {
        sqrt(Cells.Style.JAVA,    Cells.Style.JAVA);
        sqrt(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        sqrt(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        sqrt(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void sqrt(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, 4.0, 9.0, 16.0 }, styleA);
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0,  4.0 }, styleB);

        final Array result = aA.sqrt();
        if (result == aA) fail("'sqrt' must return a new instance");
        if (result == aB) fail("'sqrt' must return a new instance");
        if (!result.equals(aB)) fail("'sqrt' failed");
    }

    @Test
    public void sub() {
        fail("sub failed");
    }

    @Test
    public void subAssign() {
        fail("subAssign failed");
    }


    @Test
    public void swap() {
        swap(Cells.Style.JAVA,    Cells.Style.JAVA);
        swap(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        swap(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        swap(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    public void swap(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleA);

        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 }, styleB);

        final Array aAclone = aA.clone();
        final Array aBclone = aB.clone();

        aA.swap(aB);
        if (!aA.equals(aBclone)) fail("'swap' failed");
        if (!aB.equals(aAclone)) fail("'swap' failed");
    }


    @Test
    public void toArray() {
        toArray(Cells.Style.JAVA);
        toArray(Cells.Style.FORTRAN);
    }

    private void toArray(final Cells.Style style) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, style);

        final double[] doubles = new double[] { 1.0, 2.0, 3.0, 4.0 };

        double[] result = (double[]) aA.toArray();
        for (int i=0; i<aA.size(); i++)
            if (result[i] != doubles[i])
                fail("toArray failed");

        result = aA.toArray(new double[4]);
        for (int i=0; i<aA.size(); i++)
            if (result[i] != doubles[i])
                fail("toArray failed");
    }

    @Test
    public void transform() {
        fail("transform failed");
    }

    @Test
    public void upperBound() {
        fail("upperBound failed");
    }

}
