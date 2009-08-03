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

import org.jquantlib.math.matrixutilities.Array;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Richard Gomes
 */
public class ArrayTest {

    private final static Logger logger = LoggerFactory.getLogger(ArrayTest.class);

    public ArrayTest() {
        logger.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
    }

    @Test
    public void testEquals() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        if (!aA.equals(aB))
            fail("'equals' failed");
    }

    @Test
    public void testClone() {
        final Array mA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        final Array mB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        final Array result = mA.clone();
        if (result == mA) fail("'clone' must return a new instance");
        if (result == mB) fail("'clone' must return a new instance");
        if (!result.equals(mB)) fail("'clone' failed");
    }

    @Test
    public void abs() {
        final Array aA = new Array(new double[] { 1.0, -2.0, -3.0, 5.0, -9.0, -11.0, -12.0 });
        final Array aB = new Array(new double[] { 1.0,  2.0,  3.0, 5.0,  9.0,  11.0,  12.0 });

        final Array result = aA.abs();
        if (result == aA) fail("'abs' must return a new instance");
        if (result == aB) fail("'abs' must return a new instance");
        if (!result.equals(aB)) fail("'abs' failed");
    }

    @Test
    public void accumulate() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });

        if (aA.accumulate() != 45.0)
            fail("'accumulate' failed");
        if (aA.accumulate(2, 5, -2.0) != 10.0)
            fail("'accumulate' failed");
    }

    @Test
    public void add() {
    }

    @Test
    public void addAssign() {
    }

    @Test
    public void adjacentDifference() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 5.0, 9.0, 11.0, 12.0 });
        final Array aB = new Array(new double[] { 1.0, 1.0, 1.0, 2.0, 4.0, 2.0, 1.0 });

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
    }

    @Test
    public void copyOfRange() {
        final Array aA = new Array(new double[] { 9.0, 8.0, 1.0, 2.0, 3.0, 4.0, 8.0, 9.0 });
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        final Array result = aA.copyOfRange(2, 4);
        if (result == aA)
            fail("'copyOfRange' must return a new instance");
        if (result == aB)
            fail("'copyOfRange' must return a new instance");
        if (!result.equals(aB))
            fail("'copyOfRange' failed");
    }

    @Test
    public void div() {
    }

    @Test
    public void divAssign() {
    }

    @Test
    public void dotProduct() {
    }

    @Test
    public void exp() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        final Array result = aA.exp();
        if (result == aA) fail("'exp' must return a new instance");
        if (result.length!=aA.length) fail("'exp' failed");
        for (int i=0; i<aA.length; i++)
            if (result.get(i) != Math.exp(aA.get(i)))
                fail("'exp' failed");
    }

    @Test
    public void fill() {
        final Array aA = new Array(new double[] { 2.0, 2.0, 2.0, 2.0 });

        final Array result = new Array(4).fill(2.0);
        if (!result.equals(aA)) fail("'fill' failed");
    }

    @Test
    public void first() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        if (aA.first() != 1.0)
            fail("'first' failed");
    }

    @Test
    public void innerProduct() {
    }

    @Test
    public void last() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        if (aA.last() != 4.0)
            fail("'last' failed");
    }

    @Test
    public void log() {
        final Array aA = new Array(new double[] { 1.0, 10.0, 100.0, 1000.0 });

        final Array result = aA.log();
        if (result == aA) fail("'log' must return a new instance");
        if (result.length!=aA.length) fail("'log' failed");
        for (int i=0; i<aA.length; i++)
            if (result.get(i) != Math.log(aA.get(i)))
                fail("'log' failed");
    }

    @Test
    public void lowerBound() {
    }

    @Test
    public void max() {
        final Array aA = new Array(new double[] { 0.0, 1.0, 2.0, -3.0, 4.0, 0.0, -6.0, 7.0, 8.0, 0.0 });

        if (aA.max() != 8.0)
            fail("'max' failed");
        if (aA.max(2, 6) != 4.0)
            fail("'max' failed");
    }

    @Test
    public void min() {
        final Array aA = new Array(new double[] { 0.0, 1.0, 2.0, -3.0, 4.0, 0.0, -6.0, 7.0, 8.0, 0.0 });

        if (aA.min() != -6.0)
            fail("'min' failed");
        if (aA.min(2, 6) != -3.0)
            fail("'min' failed");
    }

    @Test
    public void mul() {
    }

    @Test
    public void mulAssign() {
    }

    @Test
    public void outerProduct() {
    }

    @Test
    public void sort() {
        final Array aA = new Array(new double[] { 9.0, 8.0, 2.0, 3.0, 1.0, 4.0, 8.0, 9.0 });
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0, 8.0, 8.0, 9.0, 9.0 });

        final Array result = aA.sort();
        if (result != aA)
            fail("'sort' must return <this>");
        if (!result.equals(aB))
            fail("'sort' failed");
    }

    @Test
    public void sqrt() {
        final Array aA = new Array(new double[] { 1.0, 4.0, 9.0, 16.0 });
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0,  4.0 });

        final Array result = aA.sqrt();
        if (result == aA) fail("'sqrt' must return a new instance");
        if (result == aB) fail("'sqrt' must return a new instance");
        if (!result.equals(aB)) fail("'sqrt' failed");
    }

    @Test
    public void sub() {
    }

    @Test
    public void subAssign() {
    }

    @Test
    public void swap() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 });

        final Array aAclone = aA.clone();
        final Array aBclone = aB.clone();

        aA.swap(aB);
        if (!aA.equals(aBclone)) fail("'swap' failed");
        if (!aB.equals(aAclone)) fail("'swap' failed");
    }

    @Test
    public void toArray() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });

        final double[] doubles = new double[] { 1.0, 2.0, 3.0, 4.0 };

        double[] result = (double[]) aA.toArray();
        for (int i=0; i<aA.cols; i++)
            if (result[i] != doubles[i])
                fail("toArray failed");

        result = aA.toArray(new double[4]);
        for (int i=0; i<aA.cols; i++)
            if (result[i] != doubles[i])
                fail("toArray failed");
    }

    @Test
    public void transform() {
    }

    @Test
    public void upperBound() {
    }

}
