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
import org.jquantlib.lang.iterators.Iterator;
import org.jquantlib.math.functions.Sqr;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Cells;
import org.jquantlib.math.matrixutilities.Matrix;
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

        if (!aA.equals(aB)) fail("'equals' failed");

        final Iterator itA = aA.constIterator();
        final Iterator itB = aB.constIterator();

        if (! itA.equals(itB) ) fail("'equals' failed");
    }


    @Test
    public void testClone() {
        testClone(Cells.Style.JAVA,    Cells.Style.JAVA);
        testClone(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        testClone(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        testClone(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void testClone(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleA);
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleB);

        final Array array = aA.clone();
        if (array == aA) fail("'clone' must return a new instance");
        if (array == aB) fail("'clone' must return a new instance");
        if (!array.equals(aB)) fail("'clone' failed");

        final Iterator it = (Iterator) aA.constIterator().clone();
        if (! it.equals(aB.constIterator()) ) fail("'clone' failed");
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

        final Iterator itA = aA.constIterator().abs();
        final Iterator itB = aB.constIterator();
        if (! itA.equals(itB) ) fail("'abs' failed");
    }


    @Test
    public void accumulate() {
        accumulate(Cells.Style.JAVA);
        accumulate(Cells.Style.FORTRAN);
    }

    private void accumulate(final Cells.Style style) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 }, style);

        if (aA.accumulate() != 45.0) fail("'accumulate' failed");
        if (aA.accumulate(2+aA.base(), 5+aA.base(), -2.0) != 10.0) fail("'accumulate' failed");

        if (aA.constIterator().accumulate() != 45.0) fail("'accumulate' failed");
        if (aA.constIterator().accumulate(2+aA.base(), 5+aA.base(), -2.0) != 10.0) fail("'accumulate' failed");
    }

    @Test
    public void add() {
        add(Cells.Style.JAVA,    Cells.Style.JAVA);
        add(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        add(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        add(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void add(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleA);
        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 }, styleB);

        final Array a = aA.add(aB);
        if (a == aA)
            fail("'add' must return a new instance");
        if (a.size() != aA.size())
            fail("'add' failed");

        for (int i = a.base(); i < a.size() + a.base(); i++)
            if (a.get(i) != 5)
                fail("'add' failed");

        final Iterator it = aA.iterator().add(aB.constIterator());
        int count = 0;
        it.begin();
        while (it.hasNext()) {
            if (it.nextDouble() != 5)
                fail("'add' failed");
            count++;
        }
        if (count != 4)
            fail("'add' failed");
    }

    @Test
    public void addAssign() {
        addAssign(Cells.Style.JAVA,    Cells.Style.JAVA);
        addAssign(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        addAssign(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        addAssign(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void addAssign(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 }, styleA);
        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 }, styleB);

        final Array clone = aA.clone();
        final Array a = clone.addAssign(aB);
        if (a != clone) fail("addAssign must return <this>");

        for (int i=a.base(); i<a.size()+a.base(); i++)
            if (a.get(i) != 5) fail("'addAssign' failed");

        final Iterator it = aA.clone().iterator().addAssign(aB.constIterator());
        int count = 0;
        it.begin();
        while (it.hasNext()) {
            if (it.nextDouble() != 5)
                fail("'addAssign' failed");
            count++;
        }
        if (count != 4)
            fail("'addAssign' failed");
    }


    @Test
    public void sub() {
        sub(Cells.Style.JAVA,    Cells.Style.JAVA);
        sub(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        sub(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        sub(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void sub(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 9.0, 8.0, 7.0, 6.0 }, styleA);
        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 }, styleB);

        final Array a = aA.sub(aB);
        if (a == aA)
            fail("'sub' must return a new instance");
        if (a.size() != aA.size())
            fail("'sub' failed");

        for (int i = a.base(); i < a.size() + a.base(); i++)
            if (a.get(i) != 5)
                fail("'sub' failed");

        final Iterator it = aA.iterator().sub(aB.constIterator());
        int count = 0;
        while (it.hasNext()) {
            if (it.nextDouble() != 5)
                fail("'sub' failed");
            count++;
        }
        if (count != 4)
            fail("'sub' failed");
    }

    @Test
    public void subAssign() {
        subAssign(Cells.Style.JAVA,    Cells.Style.JAVA);
        subAssign(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        subAssign(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        subAssign(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void subAssign(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 9.0, 8.0, 7.0, 6.0 }, styleA);
        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 }, styleB);

        final Array clone = aA.clone();
        final Array a = clone.subAssign(aB);
        if (a != clone) fail("subAssign must return <this>");
        if (a.size() != aA.size()) fail("'subAssign' failed");

        for (int i=a.base(); i<a.size()+a.base(); i++)
            if (a.get(i) != 5) fail("'subAssign' failed");

        final Iterator it = aA.iterator().subAssign(aB.constIterator());
        int count = 0;
        while (it.hasNext()) {
            if (it.nextDouble() != 5)
                fail("'subAssign' failed");
            count++;
        }
        if (count != 4)
            fail("'subAssing' failed");
}


    @Test
    public void mul() {
        mul(Cells.Style.JAVA,    Cells.Style.JAVA);
        mul(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        mul(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        mul(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void mul(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 200.0, 100.0, 250.0, 500.0 }, styleA);
        final Array aB = new Array(new double[] {   5.0,  10.0,   4.0,   2.0 }, styleB);

        final Array clone = aA.clone();
        final Array a = clone.mul(aB);
        if (a == clone) fail("'mul' must return a new instance");
        if (a.size() != aA.size()) fail("'mul' failed");

        for (int i=a.base(); i<a.size()+a.base(); i++)
            if (a.get(i) != 1000) fail("'mul' failed");

        final Iterator it = aA.iterator().mul(aB.constIterator());
        int count = 0;
        it.begin();
        while (it.hasNext()) {
            if (it.nextDouble() != 1000)
                fail("'mul' failed");
            count++;
        }
        if (count != 4)
            fail("'mul' failed");
    }

    @Test
    public void mulAssign() {
        mulAssign(Cells.Style.JAVA,    Cells.Style.JAVA);
        mulAssign(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        mulAssign(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        mulAssign(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void mulAssign(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 200.0, 100.0, 250.0, 500.0 }, styleA);
        final Array aB = new Array(new double[] {   5.0,  10.0,   4.0,   2.0 }, styleB);

        final Array clone = aA.clone();
        final Array a = clone.mulAssign(aB);
        if (a != clone) fail("mulAssign must return <this>");
        if (a.size() != aA.size()) fail("'mulAssign' failed");

        for (int i=a.base(); i<a.size()+a.base(); i++)
            if (a.get(i) != 1000) fail("'mulAssign' failed");

        final Iterator it = aA.iterator().mulAssign(aB.constIterator());
        int count = 0;
        while (it.hasNext()) {
            if (it.nextDouble() != 1000)
                fail("'mullAssign' failed");
            count++;
        }
        if (count != 4)
            fail("'mulAssign' failed");
    }


    @Test
    public void div() {
        div(Cells.Style.JAVA,    Cells.Style.JAVA);
        div(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        div(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        div(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void div(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 20.0, 18.0, 16.0, 14.0 }, styleA);
        final Array aB = new Array(new double[] { 10.0,  9.0,  8.0,  7.0 }, styleB);

        final Array clone = aA.clone();
        final Array a = clone.div(aB);
        if (a == clone) fail("'div' must return a new instance");
        if (a.size() != aA.size()) fail("'div' failed");

        for (int i=a.base(); i<a.size()+a.base(); i++)
            if (a.get(i) != 2) fail("'div' failed");

        final Iterator it = aA.iterator().div(aB.constIterator());
        int count = 0;
        it.begin();
        while (it.hasNext()) {
            if (it.nextDouble() != 2)
                fail("'div' failed");
            count++;
        }
        if (count != 4)
            fail("'div' failed");
    }


    @Test
    public void divAssign() {
        divAssign(Cells.Style.JAVA,    Cells.Style.JAVA);
        divAssign(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        divAssign(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        divAssign(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void divAssign(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 20.0, 18.0, 16.0, 14.0 }, styleA);
        final Array aB = new Array(new double[] { 10.0,  9.0,  8.0,  7.0 }, styleB);

        final Array clone = aA.clone();
        final Array a = clone.divAssign(aB);
        if (a != clone) fail("divAssign must return <this>");
        if (a.size() != aA.size()) fail("'divAssign' failed");

        for (int i=a.base(); i<a.size()+a.base(); i++)
            if (a.get(i) != 2) fail("'divAssign' failed");

        final Iterator it = aA.iterator().divAssign(aB.constIterator());
        int count = 0;
        while (it.hasNext()) {
            if (it.nextDouble() != 2)
                fail("'divAssign' failed");
            count++;
        }
        if (count != 4)
            fail("'divAssing' failed");
    }


    @Test
    public void dotProduct() {
        dotProduct(Cells.Style.JAVA,    Cells.Style.JAVA);
        dotProduct(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        dotProduct(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        dotProduct(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void dotProduct(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 2.0, 1.0, -2.0, 3.0 }, styleA);
        final Array aB = new Array(new double[] { 3.0, 4.0,  5.0, 1.0 }, styleB);

        if (aA.dotProduct(aB) != 3) fail("'dotProduct' failed");
        if (aA.constIterator().dotProduct(aB.constIterator()) != 3) fail("'dotProduct' failed");
    }


    @Test
    public void innerProduct() {
        // when working with real numbers, both dotProduct and innerProduct give the same results
        dotProduct();
    }


    @Test
    public void outerProduct() {
        outerProduct(Cells.Style.JAVA,    Cells.Style.JAVA);
        outerProduct(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        outerProduct(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        outerProduct(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void outerProduct(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 2.0, 1.0, -2.0, }, styleA);
        final Array aB = new Array(new double[] { 3.0, 4.0,  5.0, 1.0 }, styleB);

        final Matrix m = new Matrix( new double[][] {
                {  6.0,  8.0,  10.0,  2.0 },
                {  3.0,  4.0,   5.0,  1.0 },
                { -6.0, -8.0, -10.0, -2.0 }
        });

        if (! aA.outerProduct(aB).equals(m)) fail("'outerProduct' failed");
        if (! aA.constIterator().outerProduct(aB.constIterator()).equals(m)) fail("'outerProduct' failed");
    }


    @Test
    public void transform() {
        transform1(Cells.Style.JAVA,    Cells.Style.JAVA);
        transform1(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        transform1(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        transform1(Cells.Style.FORTRAN, Cells.Style.JAVA);
        transform2(Cells.Style.JAVA,    Cells.Style.JAVA);
        transform2(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        transform2(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        transform2(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void transform1(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] {  5.0, 2.0, 3.0,  4.0 }, styleA);
        final Array aB = new Array(new double[] { 25.0, 4.0, 9.0, 16.0 }, styleB);

        Array tmp;

        tmp = aA.clone();
        final Array result = tmp.transform(new Sqr());
        if (result != tmp) fail("'transform' must return this");
        if (!result.equals(aB)) fail("'transform' failed");

        tmp = aA.clone();
        final Iterator itA = tmp.iterator().transform(new Sqr());
        final Iterator itB = aB.constIterator();
        if (! itA.equals(itB) ) fail("'transform' failed");
    }

    private void transform2(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 5.0, 2.0, 3.0,  4.0 }, styleA);
        final Array aB = new Array(new double[] { 5.0, 4.0, 9.0,  4.0 }, styleB);

        Array tmp;

        tmp = aA.clone();
        final Array result = tmp.transform(aA.base()+1, aA.base()+3, new Sqr());
        if (result != tmp) fail("'transform' must return this");
        if (!result.equals(aB)) fail("'transform' failed");

        tmp = aA.clone();
        final Iterator itA = tmp.iterator().transform(aA.base()+1, aA.base()+3, new Sqr());
        final Iterator itB = aB.constIterator();
        if (! itA.equals(itB) ) fail("'transform' failed");
    }


    @Test
    public void lowerBound() {
        lowerBound(Cells.Style.JAVA);
        lowerBound(Cells.Style.FORTRAN);
    }

    private void lowerBound(final Cells.Style style) {
        final Array aA = new Array(new double[] { -10.0, -5.0, -2.0, -1.0, 0.0, 2.0, 5.0, 10.0 }, style);
        final int base = aA.base();
        final int size = aA.size();

        lowerBound(aA, 0+base, size+base,  -12.0, 0+base);
        lowerBound(aA, 0+base, size+base,  -10.0, 0+base);
        lowerBound(aA, 0+base, size+base,   -9.0, 1+base);
        lowerBound(aA, 0+base, size+base,   -8.0, 1+base);
        lowerBound(aA, 0+base, size+base,   -6.0, 1+base);
        lowerBound(aA, 0+base, size+base,   -5.0, 1+base);
        lowerBound(aA, 0+base, size+base,   -4.0, 2+base);
        lowerBound(aA, 0+base, size+base,    1.0, 5+base);
        lowerBound(aA, 0+base, size+base,    2.0, 5+base);
        lowerBound(aA, 0+base, size+base,    3.0, 6+base);
        lowerBound(aA, 0+base, size+base,    8.0, 7+base);
        lowerBound(aA, 0+base, size+base,    9.0, 7+base);
        lowerBound(aA, 0+base, size+base,   10.0, 7+base);
        lowerBound(aA, 0+base, size+base,   11.0, 8+base);
        lowerBound(aA, 0+base, size+base,   12.0, 8+base);

        lowerBound(aA, 2+base, 5+base,  -12.0, 2+base);
        lowerBound(aA, 2+base, 5+base,  -10.0, 2+base);
        lowerBound(aA, 2+base, 5+base,   -9.0, 2+base);
        lowerBound(aA, 2+base, 5+base,   -8.0, 2+base);
        lowerBound(aA, 2+base, 5+base,   -6.0, 2+base);
        lowerBound(aA, 2+base, 5+base,   -5.0, 2+base);
        lowerBound(aA, 2+base, 5+base,   -4.0, 2+base);
        lowerBound(aA, 2+base, 5+base,    1.0, 5+base);
        lowerBound(aA, 2+base, 5+base,    2.0, 5+base);
        lowerBound(aA, 2+base, 5+base,    3.0, 5+base);
        lowerBound(aA, 2+base, 5+base,    8.0, 5+base);
        lowerBound(aA, 2+base, 5+base,    9.0, 5+base);
        lowerBound(aA, 2+base, 5+base,   10.0, 5+base);
        lowerBound(aA, 2+base, 5+base,   11.0, 5+base);
        lowerBound(aA, 2+base, 5+base,   12.0, 5+base);

    }

    private void lowerBound(final Array a, final int pos0, final int pos1, final double value, final int expected) {
        int pos;
        pos = a.lowerBound(pos0, pos1, value);                 if (pos != expected) fail("'lowerBound' failed");
        pos = a.constIterator().lowerBound(pos0, pos1, value); if (pos != expected) fail("'lowerBound' failed");
    }


    @Test
    public void upperBound() {
        upperBound(Cells.Style.JAVA);
        upperBound(Cells.Style.FORTRAN);
    }

    private void upperBound(final Cells.Style style) {
        final Array aA = new Array(new double[] { -10.0, -5.0, -2.0, -1.0, 0.0, 2.0, 5.0, 10.0 }, style);
        final int base = aA.base();
        final int size = aA.size();

        upperBound(aA, 0+base, size+base,  -12.0, 0+base);
        upperBound(aA, 0+base, size+base,  -10.0, 1+base);
        upperBound(aA, 0+base, size+base,   -9.0, 1+base);
        upperBound(aA, 0+base, size+base,   -8.0, 1+base);
        upperBound(aA, 0+base, size+base,   -6.0, 1+base);
        upperBound(aA, 0+base, size+base,   -5.0, 2+base);
        upperBound(aA, 0+base, size+base,   -4.0, 2+base);
        upperBound(aA, 0+base, size+base,    1.0, 5+base);
        upperBound(aA, 0+base, size+base,    2.0, 6+base);
        upperBound(aA, 0+base, size+base,    3.0, 6+base);
        upperBound(aA, 0+base, size+base,    8.0, 7+base);
        upperBound(aA, 0+base, size+base,    9.0, 7+base);
        upperBound(aA, 0+base, size+base,   10.0, 8+base);
        upperBound(aA, 0+base, size+base,   11.0, 8+base);
        upperBound(aA, 0+base, size+base,   12.0, 8+base);

        upperBound(aA, 1+base, 6+base,  -12.0, 1+base);
        upperBound(aA, 1+base, 6+base,  -10.0, 1+base);
        upperBound(aA, 1+base, 6+base,   -9.0, 1+base);
        upperBound(aA, 1+base, 6+base,   -8.0, 1+base);
        upperBound(aA, 1+base, 6+base,   -6.0, 1+base);
        upperBound(aA, 1+base, 6+base,   -5.0, 2+base);
        upperBound(aA, 1+base, 6+base,   -4.0, 2+base);
        upperBound(aA, 1+base, 6+base,    1.0, 5+base);
        upperBound(aA, 1+base, 6+base,    2.0, 6+base);
        upperBound(aA, 1+base, 6+base,    3.0, 6+base);
        upperBound(aA, 1+base, 6+base,    8.0, 6+base);
        upperBound(aA, 1+base, 6+base,    9.0, 6+base);
        upperBound(aA, 1+base, 6+base,   10.0, 6+base);
        upperBound(aA, 1+base, 6+base,   11.0, 6+base);
        upperBound(aA, 1+base, 6+base,   12.0, 6+base);
    }

    private void upperBound(final Array a, final int pos0, final int pos1, final double value, final int expected) {
        int pos;
        pos = a.upperBound(pos0, pos1, value);                 if (pos != expected) fail("'upperBound' failed");
        pos = a.constIterator().upperBound(pos0, pos1, value); if (pos != expected) fail("'upperBound' failed");
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
        if (result == aA) fail("'adjacentDifferences' must return a new instance");
        if (result == aB) fail("'adjacentDifferences' must return a new instance");
        if (!result.equals(aB)) fail("'adjacentDifferences' failed");

        final Iterator itA = aA.constIterator().adjacentDifference();
        final Iterator itB = aB.constIterator();
        if (! itA.equals(itB) ) fail("'adjacentDifferences' failed");
    }


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
        if (result == aA) fail("'exp' must return a new instance");
        if (result == aB) fail("'exp' must return a new instance");
        if (!result.equals(aB)) fail("'exp' failed");

        final Iterator itA = aA.constIterator().exp();
        final Iterator itB = aB.constIterator();
        if (! itA.equals(itB) ) fail("'exp' failed");
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

        if (aA.first() != 1.0) fail("'first' failed");
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

        final Iterator itA = aA.constIterator().log();
        final Iterator itB = aB.constIterator();
        if (! itA.equals(itB) ) fail("'log' failed");
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

        final Iterator itA = aA.constIterator().sqrt();
        final Iterator itB = aB.constIterator();
        if (! itA.equals(itB) ) fail("'sqrt' failed");
    }


    @Test
    public void sqr() {
        sqr(Cells.Style.JAVA,    Cells.Style.JAVA);
        sqr(Cells.Style.FORTRAN, Cells.Style.FORTRAN);
        sqr(Cells.Style.JAVA,    Cells.Style.FORTRAN);
        sqr(Cells.Style.FORTRAN, Cells.Style.JAVA);
    }

    private void sqr(final Cells.Style styleA, final Cells.Style styleB) {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0,  4.0 }, styleA);
        final Array aB = new Array(new double[] { 1.0, 4.0, 9.0, 16.0 }, styleB);

        final Array result = aA.sqr();
        if (result == aA) fail("'sqr' must return a new instance");
        if (result == aB) fail("'sqr' must return a new instance");
        if (!result.equals(aB)) fail("'sqr' failed");

        final Iterator itA = aA.constIterator().sqr();
        final Iterator itB = aB.constIterator();
        if (! itA.equals(itB) ) fail("'sqr' failed");
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

}
