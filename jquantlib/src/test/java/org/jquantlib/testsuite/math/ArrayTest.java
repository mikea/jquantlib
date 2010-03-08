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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jquantlib.QL;
import org.jquantlib.math.Ops;
import org.jquantlib.math.functions.GreaterThanPredicate;
import org.jquantlib.math.functions.Square;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Matrix;
import org.junit.Test;


/**
 *
 * @author Richard Gomes
 */
public class ArrayTest {

    public ArrayTest() {
        QL.info("::::: " + this.getClass().getSimpleName() + " :::::");
    }

    private Array augmented(final Array array) {
        final Array result = new Array(array.size()+2);
        result.set(0, Math.random());
        for (int i=0, j=1; i<array.size(); i++,j++) {
            result.set(j, array.get(i));
        }
        result.set(result.size()-1, Math.random());
        return result;
    }

    private Array range(final Array array) {
        return array.range(1, array.size()-2 );
    }

    public static boolean equals(final Array a, final Array b) {
        if (a.size() != b.size())
            return false;
        for (int i=0; i<a.size(); i++) {
            if (a.get(i) != b.get(i))
                return false;
        }
        return true;
    }


    @Test
    public void testClone() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        testClone(aA, aB);
        testClone(range(augmented(aA)), range(augmented(aB)));
    }

    private void testClone(final Array aA, final Array aB) {
        final Array result = aA.clone();
        if (result == aA) {
            fail("'clone' must return a new instance");
        }
        if (result == aB) {
            fail("'clone' must return a new instance");
        }
        if (!equals(result, aB)) {
            fail("'clone' failed");
        }
    }

    @Test
    public void abs() {
        final Array aA = new Array(new double[] { 1.0, -2.0, -3.0, 5.0, -9.0, -11.0, -12.0 });
        final Array aB = new Array(new double[] { 1.0,  2.0,  3.0, 5.0,  9.0,  11.0,  12.0 });
        abs(aA, aB);
        abs(range(augmented(aA)), range(augmented(aB)));
    }

    private void abs(final Array aA, final Array aB) {
        final Array result = aA.abs();
        if (result == aA) {
            fail("'abs' must return a new instance");
        }
        if (result == aB) {
            fail("'abs' must return a new instance");
        }
        if (!equals(result, aB)) {
            fail("'abs' failed");
        }
    }

    @Test
    public void accumulate() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
        accumulate(aA);
        accumulate(range(augmented(aA)));
    }

    private void accumulate(final Array aA) {
        if (aA.accumulate() != 45.0) {
            fail("'accumulate' failed");
        }
        if (aA.accumulate(2, 5, -2.0) != 10.0) {
            fail("'accumulate' failed");
        }
    }

    @Test
    public void add() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 });
        add(aA, aB);
        add(range(augmented(aA)), range(augmented(aB)));
    }

    private void add(final Array aA, final Array aB) {
        final Array a = aA.add(aB);
        if (a == aA) {
            fail("'add' must return a new instance");
        }
        if (a.size() != aA.size()) {
            fail("'add' failed");
        }

        for (int i=0; i<a.size(); i++) {
            if (a.get(i) != 5) {
                fail("'add' failed");
            }
        }
    }

    @Test
    public void addAssign() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 });
        addAssign(aA, aB);
        addAssign(range(augmented(aA)), range(augmented(aB)));
    }

    private void addAssign(final Array aA, final Array aB) {
        final Array clone = aA.clone();
        final Array a = clone.addAssign(aB);
        if (a != clone) {
            fail("addAssign must return <this>");
        }

        for (int i=0; i<a.size(); i++) {
            if (a.get(i) != 5) {
                fail("'addAssign' failed");
            }
        }
    }

    @Test
    public void sub() {
        final Array aA = new Array(new double[] { 9.0, 8.0, 7.0, 6.0 });
        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 });
        sub(aA, aB);
        sub(range(augmented(aA)), range(augmented(aB)));
    }

    private void sub(final Array aA, final Array aB) {
        final Array a = aA.sub(aB);
        if (a == aA) {
            fail("'sub' must return a new instance");
        }
        if (a.size() != aA.size()) {
            fail("'sub' failed");
        }

        for (int i=0; i < a.size(); i++) {
            if (a.get(i) != 5) {
                fail("'sub' failed");
            }
        }
    }

    @Test
    public void subAssign() {
        final Array aA = new Array(new double[] { 9.0, 8.0, 7.0, 6.0 });
        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 });
        subAssign(aA, aB);
        subAssign(range(augmented(aA)), range(augmented(aB)));
    }

    private void subAssign(final Array aA, final Array aB) {
        final Array clone = aA.clone();
        final Array a = clone.subAssign(aB);
        if (a != clone) {
            fail("subAssign must return <this>");
        }
        if (a.size() != aA.size()) {
            fail("'subAssign' failed");
        }

        for (int i=0; i<a.size(); i++) {
            if (a.get(i) != 5) {
                fail("'subAssign' failed");
            }
        }
    }

    @Test
    public void mul() {
        final Array aA = new Array(new double[] { 200.0, 100.0, 250.0, 500.0 });
        final Array aB = new Array(new double[] {   5.0,  10.0,   4.0,   2.0 });
        mul(aA, aB);
        mul(range(augmented(aA)), range(augmented(aB)));
    }

    private void mul(final Array aA, final Array aB) {
        final Array clone = aA.clone();
        final Array a1 = clone.mul(aB);
        if (a1 == clone) {
            fail("'mul' must return a new instance");
        }
        if (a1.size() != aA.size()) {
            fail("'mul' failed");
        }

        for (int i=0; i<a1.size(); i++) {
            if (a1.get(i) != 1000) {
                fail("'mul' failed");
            }
        }

        // array multiplied by Matrix

        final Matrix mB = new Matrix(new double[][] {
                { 1.0,  2.0,  0.0 },
                { 2.0,  1.0,  0.0 },
                { 2.0,  1.0,  1.0 },
                { 1.0,  2.0,  0.0 }
        });
        final Array aB2 = new Array(new double[] { 1400.0,  1750.0,   250.0 });

        final Array a2 = clone.mul(mB);

        if (a2 == clone) {
            fail("'mul' must return a new instance");
        }
        if (a2.size() != mB.cols()) {
            fail("'mul' failed");
        }

        for (int i=0; i<a2.size(); i++) {
            final double elem = aB2.get(i);
            if (a2.get(i) != elem) {
                fail("'mul' failed");
            }
        }
    }

    @Test
    public void mulAssign() {
        final Array aA = new Array(new double[] { 200.0, 100.0, 250.0, 500.0 });
        final Array aB = new Array(new double[] {   5.0,  10.0,   4.0,   2.0 });
        mulAssign(aA, aB);
        mulAssign(range(augmented(aA)), range(augmented(aB)));
    }

    private void mulAssign(final Array aA, final Array aB) {
        final Array clone = aA.clone();
        final Array a = clone.mulAssign(aB);
        if (a != clone) {
            fail("mulAssign must return <this>");
        }
        if (a.size() != aA.size()) {
            fail("'mulAssign' failed");
        }

        for (int i=0; i<a.size(); i++) {
            if (a.get(i) != 1000) {
                fail("'mulAssign' failed");
            }
        }
    }


    @Test
    public void div() {
        final Array aA = new Array(new double[] { 20.0, 18.0, 16.0, 14.0 });
        final Array aB = new Array(new double[] { 10.0,  9.0,  8.0,  7.0 });
        div(aA, aB);
        div(range(augmented(aA)), range(augmented(aB)));
    }

    private void div(final Array aA, final Array aB) {
        final Array clone = aA.clone();
        final Array a = clone.div(aB);
        if (a == clone) {
            fail("'div' must return a new instance");
        }
        if (a.size() != aA.size()) {
            fail("'div' failed");
        }

        for (int i=0; i<a.size(); i++) {
            if (a.get(i) != 2) {
                fail("'div' failed");
            }
        }
    }


    @Test
    public void divAssign() {
        final Array aA = new Array(new double[] { 20.0, 18.0, 16.0, 14.0 });
        final Array aB = new Array(new double[] { 10.0,  9.0,  8.0,  7.0 });
        divAssign(aA, aB);
        divAssign(range(augmented(aA)), range(augmented(aB)));
    }

    private void divAssign(final Array aA, final Array aB) {
        final Array clone = aA.clone();
        final Array a = clone.divAssign(aB);
        if (a != clone) {
            fail("divAssign must return <this>");
        }
        if (a.size() != aA.size()) {
            fail("'divAssign' failed");
        }

        for (int i=0; i<a.size(); i++) {
            if (a.get(i) != 2) {
                fail("'divAssign' failed");
            }
        }
    }


    @Test
    public void dotProduct() {
        final Array aA = new Array(new double[] { 2.0, 1.0, -2.0, 3.0 });
        final Array aB = new Array(new double[] { 3.0, 4.0,  5.0, 1.0 });
        dotProduct(aA, aB);
        dotProduct(range(augmented(aA)), range(augmented(aB)));
    }

    private void dotProduct(final Array aA, final Array aB) {
        if (aA.dotProduct(aB) != 3) {
            fail("'dotProduct' failed");
        }
    }


    @Test
    public void innerProduct() {
        // when working with real numbers, both dotProduct and innerProduct give the same results
        dotProduct();
    }


    @Test
    public void outerProduct() {
        final Array aA = new Array(new double[] { 2.0, 1.0, -2.0, });
        final Array aB = new Array(new double[] { 3.0, 4.0,  5.0, 1.0 });
        outerProduct(aA, aB);
        outerProduct(range(augmented(aA)), range(augmented(aB)));
    }

    private void outerProduct(final Array aA, final Array aB) {
        final Matrix mC = new Matrix( new double[][] {
                {  6.0,  8.0,  10.0,  2.0 },
                {  3.0,  4.0,   5.0,  1.0 },
                { -6.0, -8.0, -10.0, -2.0 }
        });

        final Matrix m = aA.outerProduct(aB);
        if (!MatrixTest.equals(m, mC)) {
            fail("'outerProduct' failed");
        }
    }


    @Test
    public void transform() {
        final Array aA = new Array(new double[] {  5.0, 2.0, 3.0,  4.0 });
        final Array aB = new Array(new double[] { 25.0, 4.0, 9.0, 16.0 });
        transform(aA, aB);
        transform(range(augmented(aA)), range(augmented(aB)));
    }

    private void transform(final Array aA, final Array aB) {
        final Array tmp1 = aA.clone();
        Array result = tmp1.transform(new Square());
        if (result != tmp1) {
            fail("'transform' must return this");
        }
        if (!equals(result, aB)) {
            fail("'transform' failed");
        }

        final Array aC = new Array(new double[] { 5.0, 4.0, 9.0,  4.0 });

        final Array tmp2 = aA.clone();
        result = tmp2.transform(1, 3, new Square());
        if (result != tmp2) {
            fail("'transform' must return this");
        }
        if (!equals(result, aC)) {
            fail("'transform' failed");
        }
    }


    /**
     * @see <a href="http://gcc.gnu.org/viewcvs/trunk/libstdc%2B%2B-v3/testsuite/25_algorithms/lower_bound/">lower_bound test cases</a>
     */
    @Test
    public void lowerBound_Case1() {

        final String MESSAGE = "lowerBound Case 1 failed";

        //
        // test case :: 1.cc
        //

//        typedef test_container<int, forward_iterator_wrapper> Container;
//        int array[] = {0, 0, 0, 0, 1, 1, 1, 1};
//
//        void
//        test1()
//        {
//          for(int i = 0; i < 5; ++i)
//            for(int j = 4; j < 7; ++j)
//              {
//            Container con(array + i, array + j);
//            VERIFY(lower_bound(con.begin(), con.end(), 1).ptr == array + 4);
//              }
//        }

        final double array[] = {0, 0, 0, 0, 1, 1, 1, 1};

        for (int i = 0; i < 5; ++i) {
            for (int j = 4; j < 7; ++j) {
                final double container[] = new double[j-i +1];
                System.arraycopy(array, i, container, 0, j-i+1);
                final Array con = new Array(container);

                final int pos = con.lowerBound(1);
                if (pos != 4 - i) {
                    fail(MESSAGE);
                }
            }
        }
    }


    /**
     * @see <a href="http://gcc.gnu.org/viewcvs/trunk/libstdc%2B%2B-v3/testsuite/25_algorithms/lower_bound/">lower_bound test cases</a>
     */
    @Test
    public void lowerBound_Case2() {
        final Array A = new Array(new double[]{1, 2, 3, 3, 3, 5, 8});
        final Array C = new Array(new double[]{8, 5, 3, 3, 3, 2, 1});
        lowerBound_Case2(A, C);
        lowerBound_Case2(range(augmented(A)), range(augmented(C)));
    }

    private void lowerBound_Case2(final Array A, final Array C) {

        final String MESSAGE = "lowerBound Case 2 failed";

        //
        // test case :: 2.cc
        //

//        #include <algorithm>
//        #include <testsuite_hooks.h>
//
//        bool test __attribute__((unused)) = true;
//
//        const int A[] = {1, 2, 3, 3, 3, 5, 8};
//        const int C[] = {8, 5, 3, 3, 3, 2, 1};
//        const int N = sizeof(A) / sizeof(int);
//
//        // A comparison, equalivalent to std::greater<int> without the
//        // dependency on <functional>.
//        struct gt
//        {
//            bool
//            operator()(const int& x, const int& y) const
//            { return x > y; }
//        };
//
//        // Each test performs general-case, bookend, not-found condition,
//        // and predicate functional checks.
//
//        // 25.3.3.1 lower_bound, with and without comparison predicate
//        void
//        test01()
//        {
//            using std::lower_bound;
//
//            const int first = A[0];
//            const int last = A[N - 1];
//
//            const int* p = lower_bound(A, A + N, 3);
//            VERIFY(p == A + 2);
//
//            const int* q = lower_bound(A, A + N, first);
//            VERIFY(q == A + 0);
//
//            const int* r = lower_bound(A, A + N, last);
//            VERIFY(r == A + N - 1);
//
//            const int* s = lower_bound(A, A + N, 4);
//            VERIFY(s == A + 5);
//
//            const int* t = lower_bound(C, C + N, 3, gt());
//            VERIFY(t == C + 2);
//
//            const int* u = lower_bound(C, C + N, first, gt());
//            VERIFY(u == C + N - 1);
//
//            const int* v = lower_bound(C, C + N, last, gt());
//            VERIFY(v == C + 0);
//
//            const int* w = lower_bound(C, C + N, 4, gt());
//            VERIFY(w == C + 2);
//        }

        final int N = A.size();


            final double first = A.first();
            final double  last = A.last();
            int pos;

            pos = A.lowerBound(3);
            assertTrue(MESSAGE, pos==2);

            pos = A.lowerBound(first);
            assertTrue(MESSAGE, pos==0);

            pos = A.lowerBound(last);
            assertTrue(MESSAGE, pos==N-1);

            pos = A.lowerBound(4);
            assertTrue(MESSAGE, pos==5);

            final Ops.BinaryDoublePredicate gt = new GreaterThanPredicate();

            pos = C.lowerBound(3, gt);
            assertTrue(MESSAGE, pos==2);

            pos = C.lowerBound(first, gt);
            assertTrue(MESSAGE, pos==N-1);

            pos = C.lowerBound(last, gt);
            assertTrue(MESSAGE, pos==0);

            pos = C.lowerBound(4, gt);
            assertTrue(MESSAGE, pos==2);
    }



    /**
     * @see <a href="http://gcc.gnu.org/viewcvs/trunk/libstdc%2B%2B-v3/testsuite/25_algorithms/upper_bound/">upper_bound test cases</a>
     */
    @Test
    public void upperBound_Case1() {

        final String MESSAGE = "upperBound Case 1 failed";

        //
        // test case :: 1.cc
        //

//        typedef test_container<int, forward_iterator_wrapper> Container;
//        int array[] = {0, 0, 0, 0, 1, 1, 1, 1};
//
//        void
//        test1()
//        {
//          for(int i = 0; i < 5; ++i)
//            for(int j = 4; j < 7; ++j)
//              {
//            Container con(array + i, array + j);
//            VERIFY(upper_bound(con.begin(), con.end(), 0).ptr == array + 4);
//              }
//        }

        final double array[] = {0, 0, 0, 0, 1, 1, 1, 1};

        for (int i = 0; i < 5; ++i) {
            for (int j = 4; j < 7; ++j) {
                final double container[] = new double[j-i +1];
                System.arraycopy(array, i, container, 0, j-i+1);
                final Array con = new Array(container);

                final int pos = con.upperBound(0);
                if (pos != 4 - i) {
                    fail(MESSAGE);
                }
            }
        }
    }


    /**
     * @see <a href="http://gcc.gnu.org/viewcvs/trunk/libstdc%2B%2B-v3/testsuite/25_algorithms/upper_bound/">upper_bound test cases</a>
     */
    @Test
    public void upperBound_Case2() {
        final Array A = new Array(new double[]{1, 2, 3, 3, 3, 5, 8});
        final Array C = new Array(new double[]{8, 5, 3, 3, 3, 2, 1});
        upperBound_Case2(A, C);
        upperBound_Case2(range(augmented(A)), range(augmented(C)));
    }

    private void upperBound_Case2(final Array A, final Array C) {

        final String MESSAGE = "upperBound Case 2 failed";

        //
        // test case :: 2.cc
        //

//      #include <algorithm>
//      #include <testsuite_hooks.h>
//
//      bool test __attribute__((unused)) = true;
//
//        const int A[] = {1, 2, 3, 3, 3, 5, 8};
//        const int C[] = {8, 5, 3, 3, 3, 2, 1};
//        const int N = sizeof(A) / sizeof(int);
//
//        // A comparison, equalivalent to std::greater<int> without the
//        // dependency on <functional>.
//        struct gt
//        {
//            bool
//            operator()(const int& x, const int& y) const
//            { return x > y; }
//        };
//
//        // Each test performs general-case, bookend, not-found condition,
//        // and predicate functional checks.
//
//        // 25.3.3.2 upper_bound, with and without comparison predicate
//        void
//        test02()
//        {
//            using std::upper_bound;
//
//            const int first = A[0];
//            const int last = A[N - 1];
//
//            const int* p = upper_bound(A, A + N, 3);
//            VERIFY(p == A + 5);
//
//            const int* q = upper_bound(A, A + N, first);
//            VERIFY(q == A + 1);
//
//            const int* r = upper_bound(A, A + N, last);
//            VERIFY(r == A + N);
//
//            const int* s = upper_bound(A, A + N, 4);
//            VERIFY(s == A + 5);
//
//            const int* t = upper_bound(C, C + N, 3, gt());
//            VERIFY(t == C + 5);
//
//            const int* u = upper_bound(C, C + N, first, gt());
//            VERIFY(u == C + N);
//
//            const int* v = upper_bound(C, C + N, last, gt());
//            VERIFY(v == C + 1);
//
//            const int* w = upper_bound(C, C + N, 4, gt());
//            VERIFY(w == C + 2);
//        }

        final int N = A.size();


            final double first = A.first();
            final double  last = A.last();
            int pos;

            pos = A.upperBound(3);
            assertTrue(MESSAGE, pos==5);

            pos = A.upperBound(first);
            assertTrue(MESSAGE, pos==1);

            pos = A.upperBound(last);
            assertTrue(MESSAGE, pos==N);

            pos = A.upperBound(4);
            assertTrue(MESSAGE, pos==5);

            final Ops.BinaryDoublePredicate gt = new GreaterThanPredicate();

            pos = C.upperBound(3, gt);
            assertTrue(MESSAGE, pos==5);

            pos = C.upperBound(first, gt);
            assertTrue(MESSAGE, pos==N);

            pos = C.upperBound(last, gt);
            assertTrue(MESSAGE, pos==1);

            pos = C.upperBound(4, gt);
            assertTrue(MESSAGE, pos==2);
    }


    @Test
    public void adjacentDifference() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 5.0, 9.0, 11.0, 12.0 });
        final Array aB = new Array(new double[] { 1.0, 1.0, 1.0, 2.0, 4.0,  2.0,  1.0 });
        adjacentDifference(aA, aB);
        adjacentDifference(range(augmented(aA)), range(augmented(aB)));
    }


    private void adjacentDifference(final Array aA, final Array aB) {
        final Array result = aA.adjacentDifference();
        if (result == aA) {
            fail("'adjacentDifferences' must return a new instance");
        }
        if (result == aB) {
            fail("'adjacentDifferences' must return a new instance");
        }
        if (!equals(result, aB)) {
            fail("'adjacentDifferences' failed");
        }
    }


    @Test
    public void exp() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        final Array aB = new Array(new double[] { Math.exp(1), Math.exp(2), Math.exp(3), Math.exp(4) });
        exp(aA, aB);
        exp(range(augmented(aA)), range(augmented(aB)));
    }

    private void exp(final Array aA, final Array aB) {
        final Array result = aA.exp();
        if (result == aA) {
            fail("'exp' must return a new instance");
        }
        if (result == aB) {
            fail("'exp' must return a new instance");
        }
        if (!equals(result, aB)) {
            fail("'exp' failed");
        }
    }


    @Test
    public void fill() {
        final Array aA = new Array(new double[] { 2.0, 2.0, 2.0, 2.0 });
        fill(aA);
        fill(range(augmented(aA)));
    }

    private void fill(final Array aA) {
        final Array result = new Array(4).fill(2.0);
        if (!equals(result, aA)) {
            fail("'fill' failed");
        }
    }

    @Test
    public void first() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        first(aA);
        first(range(augmented(aA)));
    }

    private void first(final Array aA) {
        if (aA.first() != 1.0) {
            fail("'first' failed");
        }
    }

    @Test
    public void last() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        last(aA);
        last(range(augmented(aA)));
    }

    private void last(final Array aA) {
        if (aA.last() != 4.0) {
            fail("'last' failed");
        }
    }


    @Test
    public void log() {
        final Array aA = new Array(new double[] { Math.exp(1), Math.exp(2), Math.exp(3), Math.exp(4) });
        final Array aB = new Array(new double[] { 1.0,  2.0,   3.0,    4.0 });
        log(aA, aB);
        log(range(augmented(aA)), range(augmented(aB)));
    }

    private void log(final Array aA, final Array aB) {
        final Array result = aA.log();
        if (result == aA) {
            fail("'log' must return a new instance");
        }
        if (result == aB) {
            fail("'log' must return a new instance");
        }
        if (!equals(result, aB)) {
            fail("'log' failed");
        }
    }

    @Test
    public void min() {
        final Array aA = new Array(new double[] { 0.0, 1.0, 2.0, -3.0, 4.0, 0.0, -6.0, 7.0, 8.0, 0.0 });
        min(aA);
        min(range(augmented(aA)));
    }

    private void min(final Array aA) {
        if (aA.min() != -6.0) {
            fail("'min' failed");
        }
        if (aA.min(3, 6) != -3.0) {
            fail("'min' failed");
        }
    }


    @Test
    public void max() {
        final Array aA = new Array(new double[] { 0.0, 1.0, 2.0, -3.0, 4.0, 0.0, -6.0, 7.0, 8.0, 0.0 });
        max(aA);
        max(range(augmented(aA)));
    }

    private void max(final Array aA) {
        if (aA.max() != 8.0) {
            fail("'max' failed");
        }
        if (aA.max(2, 6) != 4.0) {
            fail("'max' failed");
        }
    }


    @Test
    public void sort() {
        final Array aA = new Array(new double[] { 9.0, 8.0, 2.0, 3.0, 1.0, 4.0, 8.0, 9.0 });
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0, 4.0, 8.0, 8.0, 9.0, 9.0 });
        sort(aA, aB);
        sort(range(augmented(aA)), range(augmented(aB)));
    }

    private void sort(final Array aA, final Array aB) {
        final Array result = aA.sort();
        if (result != aA) {
            fail("'sort' must return <this>");
        }
        if (!equals(result, aB)) {
            fail("'sort' failed");
        }
    }


    @Test
    public void sqrt() {
        final Array aA = new Array(new double[] { 1.0, 4.0, 9.0, 16.0 });
        final Array aB = new Array(new double[] { 1.0, 2.0, 3.0,  4.0 });
        sqrt(aA, aB);
        sqrt(range(augmented(aA)), range(augmented(aB)));
    }

    private void sqrt(final Array aA, final Array aB) {
        final Array result = aA.sqrt();
        if (result == aA) {
            fail("'sqrt' must return a new instance");
        }
        if (result == aB) {
            fail("'sqrt' must return a new instance");
        }
        if (!equals(result, aB)) {
            fail("'sqrt' failed");
        }
    }


    @Test
    public void sqr() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0,  4.0 });
        final Array aB = new Array(new double[] { 1.0, 4.0, 9.0, 16.0 });
        sqr(aA, aB);
        sqr(range(augmented(aA)), range(augmented(aB)));
    }

    private void sqr(final Array aA, final Array aB) {
        final Array result = aA.sqr();
        if (result == aA) {
            fail("'sqr' must return a new instance");
        }
        if (result == aB) {
            fail("'sqr' must return a new instance");
        }
        if (!equals(result, aB)) {
            fail("'sqr' failed");
        }
    }


    @Test
    public void swap() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        final Array aB = new Array(new double[] { 4.0, 3.0, 2.0, 1.0 });
        swap(aA, aB);
        swap(range(augmented(aA)), range(augmented(aB)));
    }

    private void swap(final Array aA, final Array aB) {
        final Array aAclone = aA.clone();
        final Array aBclone = aB.clone();

        aA.swap(aB);
        if (!equals(aA, aBclone)) {
            fail("'swap' failed");
        }
        if (!equals(aB, aAclone)) {
            fail("'swap' failed");
        }
    }


    @Test
    public void toArray() {
        final Array aA = new Array(new double[] { 1.0, 2.0, 3.0, 4.0 });
        toArray(aA);

        //FIXME: http://bugs.jquantlib.org/view.php?id=471
        // toArray(range(augmented(aA)));
    }

    private void toArray(final Array aA) {
        final double[] doubles = new double[] { 1.0, 2.0, 3.0, 4.0 };
        final double[] result = aA.toDoubleArray();
        for (int i=0; i<aA.size(); i++) {
            if (result[i] != doubles[i]) {
                fail("toArray failed");
            }
        }
    }

}
