/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl
 Copyright (C) 2004 Ferdinando Ametrano

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.math.matrixutilities;

import java.util.Arrays;
import java.util.EnumSet;

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.math.Ops;
import org.jquantlib.math.Ops.BinaryDoubleOp;
import org.jquantlib.math.Ops.DoubleOp;
import org.jquantlib.math.matrixutilities.internal.Address;
import org.jquantlib.math.matrixutilities.internal.DirectArrayRowAddress;



/**
 * 1-D array used in linear algebra.
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q2_RESEMBLANCE, version = Version.V097, reviewers = { "Richard Gomes" })
public class Array extends Cells<Address.ArrayAddress> implements Cloneable, Algebra<Array> {

    //
    // public constructors
    //

    /**
     * Default constructor
     * <p>
     * Builds an Array which contains only one element.
     */
    public Array() {
        super(1, 1,
              new DirectArrayRowAddress(0, null, 0, 0, EnumSet.of(Address.Flags.CONTIGUOUS), 1, 1));
    }


    /**
     * Builds an Array of <code>size</code>
     *
     * @param size is the size of <code>this</code> Array
     * @throws IllegalArgumentException if size are less than zero
     */
    public Array(final int size) {
        super(1, size,
              new DirectArrayRowAddress(0, null, 0, size-1, EnumSet.of(Address.Flags.CONTIGUOUS), 1, size));
    }

    /**
     * Creates an Array given a double[] array
     *
     * @param data is a unidimensional array
     */
    public Array(final double[] array) {
        super(1, array.length,
              new DirectArrayRowAddress(0, null, 0, array.length-1, EnumSet.of(Address.Flags.CONTIGUOUS), 1, array.length));
        System.arraycopy(array, 0, data, 0, this.size());
    }

    /**
     * Creates an Array given a double[] array and the desired number of elements
     *
     * @param data is a unidimensional array
     * @param size is the desired number of elements to be taken, counted from the first position
     */
    public Array(final double[] array, final int size) {
        super(1, size,
              new DirectArrayRowAddress(0, null, 0, size-1, EnumSet.of(Address.Flags.CONTIGUOUS), 1, size));
        System.arraycopy(array, 0, data, 0, this.size());
    }

    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data
     */
    public Array(final Array array) {
        super(1, array.cols(),
              new DirectArrayRowAddress(0, null, 0, array.cols()-1, EnumSet.of(Address.Flags.CONTIGUOUS), 1, array.cols()));
        if (array.addr.contiguous()) {
            System.arraycopy(array.data, array.begin(), data, 0, this.size());
        } else {
            for (int i=0; i<array.size(); i++) {
                this.data[i] = array.get(i);
            }
        }
    }


    //
    // protected constructors
    //

    protected Array(
            final int rows,
            final int cols,
            final double[] data,
            final Address.ArrayAddress addr) {
        super(rows, cols, data, addr);
    }


    //
    // Overrides Object
    //

    @Override
    public Array clone() {
        return new Array(this);
    }


    //
    // public methods
    //

//XXX
//    public Object toArray() {
//        final double buffer[] = new double[this.size()];
//        return toArray(buffer);
//    }
//
//    public double[] toArray(final double[] buffer) {
//        QL.require(this.size() == buffer.length, WRONG_BUFFER_LENGTH); // QA:[RG]::verified
//        System.arraycopy(data, 0, buffer, 0, this.size());
//        return buffer;
//    }

    /**
     * This method intentionally returns the underlying <code>double[]</code> which keeps
     * data stored in <code>this</code> Array, for performance reasons.
     *
     * @note Tools like FindBugs complain that this method should not expose internal
     * data structures due to security reasons like stack overun, etc. As the internal
     * data structure is a fixed size double[] and the returned data type is final, we
     * are OK regarding these kind of issues.
     *
     * @return double[] which contains data stored in <code>this</code> Array.
     */
    //FIXME: http://bugs.jquantlib.org/view.php?id=471
    public final double[] toDoubleArray() /* @ReadOnly */ {
        QL.require(addr.contiguous() && addr.base()==0, UnsupportedOperationException.class, "must be contiguous");
        return data;
    }

    public int begin() {
        return addr.op(0);
    }

    public int end() {
        return addr.op(size()-1);
    }

    public double first() {
        return data[begin()];
    }

    public double last() {
        return data[end()];
    }

    /**
     * Retrieves an element of <code>this</code> Matrix
     * <p>
     * This method is provided for performance reasons. See methods {@link #getAddress(int)} and {@link #getAddress(int, int)} for
     * more details
     *
     * @param dim coordinate
     * @param col coordinate
     * @return the contents of a given cell
     *
     * @see #getAddress(int)
     * @see #getAddress(int, int)
     */
    public double get(final int pos) {
        return data[addr.op(pos)];
    }

    /**
     * Stores a value into an element of <code>this</code> Matrix
     * <p>
     * This method is provided for performance reasons. See methods {@link #getAddress(int)} and {@link #getAddress(int, int)} for
     * more details
     *
     * @param dim coordinate
     * @param col coordinate
     *
     * @see #getAddress(int)
     * @see #getAddress(int, int)
     */
    public void set(final int pos, final double value) {
        data[addr.op(pos)] = value;
    }



    ////////////////////////////////////////////////////////////
    //
    // implements Algebra<Array>
    //
    ////////////////////////////////////////////////////////////


    //
    //    Assignment operations
    //
    //    opr   method     this    right    result
    //    ----- ---------- ------- -------- ------
    //    +=    addAssign  Array   scalar   this
    //    +=    addAssign  Array   Array    this
    //    -=    subAssign  Array   scalar   this
    //    -=    subAssign  Array   Array    this
    //    *=    mulAssign  Array   scalar   this
    //    *=    mulAssign  Array   Array    this
    //    /=    divAssign  Array   scalar   this
    //    /=    divAssign  Array   Array    this
    //

    @Override
    public Array addAssign(final double scalar) {
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<size(); i++) {
            data[src.op()] += scalar;
            src.nextIndex();
        }
        return this;
    }

    @Override
    public Array subAssign(final double scalar) {
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<size(); i++) {
            data[src.op()] -= scalar;
            src.nextIndex();
        }
        return this;
    }

    /**
     * Returns the result of a subtraction of <code>this</code> Array and <code>another</code> Array
     *
     * @param another
     * @return this
     */
    @Override
    public Array subAssign(final Array another) {
        QL.require(this.size() == another.size(), ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int i=0; i<size(); i++) {
            data[toff.op()] -= another.data[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return this;
    }

    @Override
    public Array mulAssign(final double scalar) {
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<size(); i++) {
            data[src.op()] *= scalar;
            src.nextIndex();
        }
        return this;
    }

    @Override
    public Array divAssign(final double scalar) {
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<size(); i++) {
            data[src.op()] /= scalar;
            src.nextIndex();
        }
        return this;
    }

    @Override
    public Array addAssign(final Array another) {
        QL.require(this.size() == another.size(), ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int i=0; i<size(); i++) {
            data[toff.op()] += another.data[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return this;
    }

    @Override
    public Array mulAssign(final Array another) {
        QL.require(this.size() == another.size(), ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int i=0; i<size(); i++) {
            data[toff.op()] *= another.data[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return this;
    }

    @Override
    public Array divAssign(final Array another) {
        QL.require(this.size() == another.size(), ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int i=0; i<size(); i++) {
            data[toff.op()] /= another.data[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return this;
    }


    //
    //    Algebraic products
    //
    //    opr   method     this    right    result
    //    ----- ---------- ------- -------- ------
    //    +     positive   Array             Array  (2)
    //    +     add        Array   scalar    Array
    //    +     add        Array   Array     Array
    //    -     negative   Array             Array  (3)
    //    -     sub        Array   scalar    Array
    //    -     sub        Array   Array     Array
    //    *     mul        Array   scalar    Array
    //    *     mul        Array   Array     Array
    //    *     mul        Array   Matrix    Array
    //    /     div        Array   scalar    Array
    //    /     div        Array   Array     Array
    //

    @Override
    public Array add(final double scalar) {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int col=0; col<size(); col++) {
            result.data[col] = data[src.op()] + scalar;
            src.nextIndex();
        }
        return result;
    }

    @Override
    public Array sub(final double scalar) {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int col=0; col<size(); col++) {
            result.data[col] = data[src.op()] - scalar;
            src.nextIndex();
        }
        return result;
    }

    @Override
    public Array mul(final double scalar) {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int col=0; col<size(); col++) {
            result.data[col] = data[src.op()] * scalar;
            src.nextIndex();
        }
        return result;
    }

    @Override
    public Array negative() {
        return mul(-1);
    }

    @Override
    public Array div(final double scalar) {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int col=0; col<size(); col++) {
            result.data[col] = data[src.op()] / scalar;
            src.nextIndex();
        }
        return result;
    }

    @Override
    public Array add(final Array another) {
        QL.require(this.size() == another.size(), MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int col=0; col<size(); col++) {
            result.data[col] = data[toff.op()] + another.data[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return result;
    }

    @Override
    public Array sub(final Array another) {
        QL.require(this.size() == another.size(), MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int col=0; col<size(); col++) {
            result.data[col] = data[toff.op()] - another.data[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return result;
    }

    @Override
    public Array mul(final Array another) {
        QL.require(this.size() == another.size(), MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int col=0; col<size(); col++) {
            result.data[col] = data[toff.op()] * another.data[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return result;
    }

    @Override
    public Array div(final Array another) {
        QL.require(this.size() == another.size(), MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int col=0; col<size(); col++) {
            result.data[col] = data[toff.op()] / another.data[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return result;
    }

    @Override
    public Array mul(final Matrix matrix) {
        QL.require(this.size() == matrix.rows(), MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(matrix.cols());
        final Address.ArrayAddress.ArrayOffset  toff = this.addr.offset();
        final Address.MatrixAddress.MatrixOffset aoff = matrix.addr.offset();
        for (int col=0; col<matrix.cols(); col++) {
            toff.setIndex(0);
            aoff.setRow(0); aoff.setCol(col);
            double sum = 0.0;
            for (int row=0; row<matrix.rows(); row++) {
                final double telem = this.data[toff.op()];
                final double aelem = matrix.data[aoff.op()];
                sum += telem * aelem;
                toff.nextIndex();
                aoff.nextRow();
            }
            result.data[col] = sum;
        }
        return result;
    }




    //
    //    Math functions
    //
    //    opr   method     this    right    result
    //    ----- ---------- ------- -------- ------
    //    min   min        Array            scalar
    //    max   max        Array            scalar
    //    abs   abs        Array            Array
    //    sqrt  sqrt       Array            Array
    //    log   log        Array            Array
    //    exp   exp        Array            Array

    @Override
    public double min() {
        return min(0, this.size());
    }

    @Override
    public double min(final int from, final int to) {
        QL.require(from >= 0 && to > from && to <= size(),  INVALID_ARGUMENTS); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset(from);
        double result = data[src.op()];
        for (int i=0; i<(to-from); i++) {
            final double tmp = data[src.op()];
            src.nextIndex();
            if (tmp < result) {
                result = tmp;
            }
        }
        return result;
    }

    @Override
    public double max() {
        return max(0, this.size());
    }

    @Override
    public double max(final int from, final int to) {
        QL.require(from >= 0 && to > from && to <= size(),  INVALID_ARGUMENTS); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset(from);
        double result = data[src.op()];
        for (int i=0; i<(to-from); i++) {
            final double tmp = data[src.op()];
            src.nextIndex();
            if (tmp > result) {
                result = tmp;
            }
        }
        return result;
    }

    @Override
    public Array abs() {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<this.size(); i++) {
            result.data[i] = Math.abs(data[src.op()]);
            src.nextIndex();
        }
        return result;
    }

    @Override
    public Array sqr() {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<this.size(); i++) {
            final double a = data[src.op()];
            result.data[i] = a*a;
            src.nextIndex();
        }
        return result;
    }

    @Override
    public Array sqrt() {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<this.size(); i++) {
            result.data[i] = Math.sqrt(data[src.op()]);
            src.nextIndex();
        }
        return result;
    }

    @Override
    public Array log() {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<this.size(); i++) {
            result.data[i] = Math.log(data[src.op()]);
            src.nextIndex();
        }
        return result;
    }

    @Override
    public Array exp() {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<this.size(); i++) {
            result.data[i] = Math.exp(data[src.op()]);
            src.nextIndex();
        }
        return result;
    }



    //
    //    Miscellaneous
    //
    //    method       this    right    result
    //    ------------ ------- -------- ------
    //    outerProduct Array   Array    Matrix
    //    dotProduct   Array   Array    double
    //

    @Override
    public double dotProduct(final Array another) {
        return dotProduct(another, 0, another.size());
    }

    @Override
    public double dotProduct(final Array another, final int from, final int to) {
        QL.require(this.size() == to-from, ITERATOR_IS_INCOMPATIBLE);
        QL.require(from >= 0 && to >= from && to <= another.size(), INVALID_ARGUMENTS); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = this.addr.offset(from);
        double sum = 0.0;
        for (int i=0; i<this.size(); i++) {
            final double telem = this.data[toff.op()];
            final double aelem = another.data[aoff.op()];
            sum += telem * aelem;
            toff.nextIndex();
            aoff.nextIndex();
        }
        return sum;
    }

    @Override
    public double innerProduct(final Array another) {
        // when working with real numbers, both dotProduct and innerProduct give the same results
        return dotProduct(another);
    }

    @Override
    public double innerProduct(final Array another, final int from, final int to) {
        // when working with real numbers, both dotProduct and innerProduct give the same results
        return dotProduct(another, from, to);
    }

    @Override
    public Matrix outerProduct(final Array another) {
        return outerProduct(another, 0, another.size());
    }

    @Override
    public Matrix outerProduct(final Array another, final int from, final int to) {
        QL.require(from >= 0 && to >= from && to <= another.size(), INVALID_ARGUMENTS); // QA:[RG]::verified
        final Matrix result = new Matrix(this.size(), to-from);
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        int addr = 0;
        for (int i=0; i<this.size(); i++) {
            final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset(from);
            for (int j=from; j < to; j++) {
                result.data[addr] = this.data[toff.op()] * another.data[aoff.op()];
                addr++;
                aoff.nextIndex();
            }
            toff.nextIndex();
        }
        return result;
    }


    //
    // Routines ported from stdlibc++
    //


    @Override
    public double accumulate() {
        return accumulate(0, this.size(), 0.0);
    }

    @Override
    public double accumulate(final double init) {
        return accumulate(0, this.size(), init);
    }

    @Override
    public double accumulate(final int first, final int last, final double init) {
        QL.require(first>=0 && last>first && last<=size(),  INVALID_ARGUMENTS); // QA:[RG]::verified
        double sum = init;
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset(first);
        for (int i=0; i<last-first; i++) {
            final double elem = this.data[src.op()];
            sum += elem;
            src.nextIndex();
        }
        return sum;
    }

    @Override
    public final Array adjacentDifference() {
        return adjacentDifference(0, size());
    }

    @Override
    public final Array adjacentDifference(final int first, final int last) {
        QL.require(first>=0 && first<=last && last<=size(), INVALID_ARGUMENTS); // QA:[RG]::verified
        final Array diff = new Array(last-first);
        diff.data[0] = data[addr.op(first)];
        for (int i=1; i<last; i++) {
            diff.data[i] = data[addr.op(i)] - data[addr.op(i-1)];
        }
        return diff;
    }

    @Override
    public Array adjacentDifference(final BinaryDoubleOp f) {
        return adjacentDifference(0, size(), f);
    }

    @Override
    public Array adjacentDifference(final int first, final int last, final BinaryDoubleOp f) {
        QL.require(first>=0 && first<=last && last<=size(), INVALID_ARGUMENTS); // QA:[RG]::verified
        final Array diff = new Array(last-first);
        diff.data[0] = data[first];
        for (int i=1; i<last; i++) {
            diff.data[i] = f.op(data[i], data[i-1]);
        }
        return diff;
    }

    @Override
    public Array transform(final DoubleOp f) {
        return transform(0, this.size(), f);
    }

    @Override
    public Array transform(final int first, final int last, final Ops.DoubleOp f) {
        QL.require(first>=0 && first<=last && last<=this.size() && f!=null, INVALID_ARGUMENTS); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset(first);
        for (int i=first; i<last; i++) {
            data[src.op()] = f.op(data[src.op()]);
            src.nextIndex();
        }
        return this;
    }


    @Override
    public int lowerBound(final double val) {
        return lowerBound(0, size(), val);
    }

    @Override
    public int lowerBound(int first, final int last, final double val) {
        QL.require(first>=0 && first<=last && last<=size(), INVALID_ARGUMENTS); // QA:[RG]::verified
        int len = last - first;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = first + half;
            if (data[addr.op(middle)] < val) {
                first = middle + 1;
                len -= half + 1;
            } else {
                len = half;
            }
        }
        return first;
    }

    @Override
    public int lowerBound(final double val, final Ops.BinaryDoublePredicate f) {
        return lowerBound(0, size(), val, f);
    }

    @Override
    public int lowerBound(int first, final int last, final double val, final Ops.BinaryDoublePredicate f) {
        QL.require(first>=0 && first<=last && last<=size(), INVALID_ARGUMENTS); // QA:[RG]::verified
        int len = last - first;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = first + half;
            if (f.op(data[addr.op(middle)], val)) {
                first = middle + 1;
                len -= half + 1;
            } else {
                len = half;
            }
        }
        return first;
    }

    @Override
    public int upperBound(final double val) {
        return upperBound(0, size(), val);
    }

    @Override
    public int upperBound(int first, final int last, final double val) {
        QL.require(first>=0 && first<=last && last<=size(), INVALID_ARGUMENTS); // QA:[RG]::verified
        int len = last - first;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = first + half;
            if (val < data[addr.op(middle)]) {
                len = half;
            } else {
                first = middle + 1;
                len -= half + 1;
            }
        }
        return first;
    }


    @Override
    public int upperBound(final double val, final Ops.BinaryDoublePredicate f) {
        return upperBound(0, size(), val, f);
    }

    @Override
    public int upperBound(int first, final int last, final double val, final Ops.BinaryDoublePredicate f) {
        QL.require(first>=0 && first<=last && last<=size(), INVALID_ARGUMENTS); // QA:[RG]::verified
        int len = last - first;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = first + half;
            if (f.op(val, data[addr.op(middle)])) {
                len = half;
            } else {
                first = middle + 1;
                len -= half + 1;
            }
        }
        return first;
    }


    //
    //  Range
    //
    //  method       this    right    result
    //  ------------ ------- -------- ------
    //  range        Array            Array
    //


    public Array range(final int col0) {
        return range(col0, cols()-1);
    }

    public Array range(final int col0, final int col1) {
        QL.require(col0 >= 0 && col0 < cols() && col1 >= 0 && col1 < cols(), Address.INVALID_COLUMN_INDEX);
        return new Range(this.addr, data, col0, col1, rows(), cols());
    }



    //TODO: better comments
    //
    // methods moved from Cells
    //

    public Array fill(final double scalar) {
        QL.require(addr.contiguous(), NON_CONTIGUOUS_DATA);
        Arrays.fill(data, addr.base(), addr.last(), scalar);
        return this;
    }

    public Array fill(final Array another) {
        QL.require(addr.contiguous(), NON_CONTIGUOUS_DATA);
        QL.require(another.addr.contiguous(), NON_CONTIGUOUS_DATA);
        QL.require(this.rows()==another.rows() && this.cols()==another.cols() && this.size()==another.size(), WRONG_BUFFER_LENGTH);
        // copies data
        System.arraycopy(another.data, addr.base(), this.data, 0, addr.last()-addr.base());
        return this;
    }

    public Array swap(final Array another) {
        QL.require(addr.contiguous(), NON_CONTIGUOUS_DATA);
        QL.require(another.addr.contiguous(), NON_CONTIGUOUS_DATA);
        QL.require(this.rows()==another.rows() && this.cols()==another.cols() && this.size()==another.size(), WRONG_BUFFER_LENGTH);
        // swaps data
        final double [] tdata;
        final Address.ArrayAddress taddr;
        tdata = this.data;  this.data = another.data;  another.data = tdata;
        taddr = this.addr;  this.addr = another.addr;  another.addr = taddr;
        return this;
    }

    public Array sort() {
        QL.require(addr.contiguous(), NON_CONTIGUOUS_DATA);
        Arrays.sort(data, addr.base(), addr.last());
        return this;
    }


    //
    // Overrides Object
    //


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[rows()=").append(rows()).append(" cols()=").append(cols()).append('\n');
        sb.append(" [ ");
        sb.append(this.data[this.addr.op(0)]);
        for (int pos = 1; pos < size(); pos++) {
            sb.append(", ");
            sb.append(data[addr.op(pos)]);
        }
        sb.append(" ]\n");
        return sb.toString();
    }



    //
    // private inner classes
    //

    private class Range extends Array {

        public Range(
            final Address.ArrayAddress chain,
            final double[] data,
            final int col0,
            final int col1,
            final int rows, final int cols) {
            super(1,
                  col1-col0+1,
                  data,
                  new DirectArrayRowAddress(0, chain, col0, col1, EnumSet.of(Address.Flags.CONTIGUOUS), rows, cols));
        }
    }

}
