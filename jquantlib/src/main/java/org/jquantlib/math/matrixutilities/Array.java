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

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.lang.iterators.Algebra;
import org.jquantlib.lang.iterators.BulkStorage;
import org.jquantlib.math.Ops;
import org.jquantlib.math.Ops.BinaryDoubleOp;
import org.jquantlib.math.Ops.DoubleOp;


/**
 * 1-D array used in linear algebra.
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q2_RESEMBLANCE, version = Version.V097, reviewers = { "Richard Gomes" })
public class Array extends Cells implements Algebra<Array>, BulkStorage<Array>, Cloneable {

    /**
     * Default constructor
     * <p>
     * Builds an Array which contains only one element.
     */
    public Array() {
        super(1, 1);
    }

    /**
     * Builds an Array of <code>size</code>
     *
     * @param size is the size of <code>this</code> Array
     * @throws IllegalArgumentException if size are less than zero
     */
    public Array(final int size) {
        super(1, size);
    }

    /**
     * Creates an Array given a double[] array
     *
     * @param data is a unidimensional array
     */
    public Array(final double[] array) {
        super(1, array.length);
        System.arraycopy(array, 0, data, 0, this.size);
    }

    /**
     * Creates an Array given a double[] array and the desired number of elements
     *
     * @param data is a unidimensional array
     * @param size is the desired number of elements to be taken, counted from the first position
     */
    public Array(final double[] array, final int size) {
        super(1, size);
        System.arraycopy(array, 0, data, 0, this.size);
    }

    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data
     */
    public Array(final Array a) {
        super(1, a.cols);
        System.arraycopy(a.data, 0, data, 0, this.size);
    }


    //
    // Overrides Object
    //

    @Override
    public Array clone() {
        return new Array(this);
    }


    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof Array))
            return false;
        final Array another = (Array) o;
        if (this.rows != another.rows || this.cols != another.cols)
            return false;
        return Arrays.equals(data, another.data);
    }


    //
    // public methods
    //

//    public Object toArray() {
//        final double buffer[] = new double[this.size];
//        return toArray(buffer);
//    }
//
//    public double[] toArray(final double[] buffer) {
//        QL.require(this.size == buffer.length, WRONG_BUFFER_LENGTH); // QA:[RG]::verified
//        System.arraycopy(data, 0, buffer, 0, this.size);
//        return buffer;
//    }

    /**
     * This method intentionally returns the underlying <code>double[]</code> which keeps
     * data stored in <code>this</code> Array.
     *
     * @note Tools like FindBugs complain that this method should not expose internal
     * data structures due to security reasons like stack overun, etc. As the internal
     * data structure is a fixed size double[] and the returned data type is final, we
     * are OK regarding these kind of issues.
     *
     * @return double[] which contains data stored in <code>this</code> Array.
     */
    public final double[] toDoubleArray() /* @ReadOnly */ {
        return this.data;
    }

    public int begin() {
        return 0;
    }

    public int end() {
        return size-1;
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
        return data[addr(pos)];
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
        data[addr(pos)] = value;
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
        for (int i=0; i<size; i++) {
            data[addr(i)] += scalar;
        }
        return this;
    }

    @Override
    public Array subAssign(final double scalar) {
        for (int i=0; i<size; i++) {
            data[addr(i)] -= scalar;
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
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++) {
            data[addr(i)] -= another.data[another.addr(i)];
        }
        return this;
    }

    @Override
    public Array mulAssign(final double scalar) {
        for (int i=0; i<size; i++) {
            data[addr(i)] *= scalar;
        }
        return this;
    }

    @Override
    public Array divAssign(final double scalar) {
        for (int i=0; i<size; i++) {
            data[addr(i)] /= scalar;
        }
        return this;
    }

    @Override
    public Array addAssign(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++) {
            data[addr(i)] += another.data[another.addr(i)];
        }
        return this;
    }

    @Override
    public Array mulAssign(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++) {
            data[addr(i)] *= another.data[another.addr(i)];
        }
        return this;
    }

    @Override
    public Array divAssign(final Array another) {
        QL.require(this.rows == another.rows && this.size == another.size, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++) {
            data[i] /= another.data[i];
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
        final Array result = new Array(this.size);
        for (int i=0; i<size; i++) {
            result.data[result.addr(i)] = data[addr(i)] + scalar;
        }
        return result;
    }

    @Override
    public Array sub(final double scalar) {
        final Array result = new Array(this.size);
        for (int i=0; i<size; i++) {
            result.data[result.addr(i)] = data[addr(i)] - scalar;
        }
        return result;
    }

    @Override
    public Array mul(final double scalar) {
        final Array result = new Array(this.size);
        for (int i=0; i<size; i++) {
            result.data[result.addr(i)] = data[addr(i)] * scalar;
        }
        return result;
    }

    @Override
    public Array negative() {
        return mul(-1);
    }

    @Override
    public Array div(final double scalar) {
        final Array result = new Array(this.size);
        for (int i=0; i<size; i++) {
            result.data[result.addr(i)] = data[addr(i)] / scalar;
        }
        return result;
    }

    @Override
    public Array add(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size);
        for (int i=0; i<size; i++) {
            result.data[result.addr(i)] = data[addr(i)] + another.data[another.addr(i)];
        }
        return result;
    }

    @Override
    public Array sub(final Array another) {
        QL.require(this.rows == another.rows && this.size == another.size, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size);
        for (int i=0; i<size; i++) {
            result.data[result.addr(i)] = data[addr(i)] - another.data[another.addr(i)];
        }
        return result;
    }

    @Override
    public Array mul(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size);
        for (int i=0; i<size; i++) {
            result.data[result.addr(i)] = data[addr(i)] * another.data[another.addr(i)];
        }
        return result;
    }

    @Override
    public Array div(final Array another) {
        QL.require(this.rows == another.rows && this.size == another.size, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size);
        for (int i=0; i<size; i++) {
            result.data[result.addr(i)] = data[addr(i)] / another.data[another.addr(i)];
        }
        return result;
    }

    @Override
    public Array mul(final Matrix matrix) {
        QL.require(this.size == matrix.rows, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(matrix.cols);
        for (int i=0; i<matrix.cols; i++) {
            int addr = matrix.addr(0, i);
            double sum = 0.0;
            for (int row=0; row<matrix.rows; row++) {
                sum += this.data[row] * matrix.data[addr];
                addr += matrix.cols;
            }
            result.data[i] = sum;
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
        return min(0, this.size);
    }

    @Override
    public double min(final int from, final int to) {
        QL.require(from >= 0 && to > from && to <= size ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        double result = data[addr(from)];
        for (int i=0; i<(to-from); i++) {
            final double tmp = data[addr(from+i)];
            if (tmp < result) {
                result = tmp;
            }
        }
        return result;
    }

    @Override
    public double max() {
        return max(0, this.size);
    }

    @Override
    public double max(final int from, final int to) {
        QL.require(from >= 0 && to > from && to <= size ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        double result = data[addr(from)];
        for (int i=0; i<(to-from); i++) {
            final double tmp = data[addr(from+i)];
            if (tmp > result) {
                result = tmp;
            }
        }
        return result;
    }

    @Override
    public Array abs() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++) {
            result.data[result.addr(i)] = Math.abs(data[addr(i)]);
        }
        return result;
    }

    @Override
    public Array sqr() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++) {
            final double a = data[addr(i)];
            result.data[result.addr(i)] = a*a;
        }
        return result;
    }

    @Override
    public Array sqrt() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++) {
            result.data[result.addr(i)] = Math.sqrt(data[addr(i)]);
        }
        return result;
    }

    @Override
    public Array log() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++) {
            result.data[result.addr(i)] = Math.log(data[addr(i)]);
        }
        return result;
    }

    @Override
    public Array exp() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++) {
            result.data[result.addr(i)] = Math.exp(data[addr(i)]);
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
        return dotProduct(another, 0, another.size);
    }

    @Override
    public double dotProduct(final Array another, final int from, final int to) {
        QL.require(this.size() == to-from, ITERATOR_IS_INCOMPATIBLE);
        QL.require(from >= 0 && to >= from && to <= another.size, INVALID_ARGUMENTS); // QA:[RG]::verified
        double sum = 0.0;
        final int offset = from;
        for (int i=0; i<this.size; i++) {
            sum += this.data[i] * another.data[offset + i];
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
        return outerProduct(another, 0, another.size);
    }

    @Override
    public Matrix outerProduct(final Array another, final int from, final int to) {
        QL.require(from >= 0 && to >= from && to <= another.size, INVALID_ARGUMENTS); // QA:[RG]::verified
        final Matrix m = new Matrix(this.size, to-from);
        for (int i=0; i<this.size(); i++) {
            for (int j=from; j < to; j++) {
                m.data[m.addr(i, j)] = this.data[i] * another.data[j];
            }
        }
        return m;
    }


    //
    // Routines ported from stdlibc++
    //


    @Override
    public double accumulate() {
        return accumulate(0, this.size, 0.0);
    }

    @Override
    public double accumulate(final double init) {
        return accumulate(0, this.size, init);
    }

    @Override
    public double accumulate(final int first, final int last, final double init) {
        QL.require(first>=0 && last>=first && last<=size,  INVALID_ARGUMENTS); // QA:[RG]::verified
        double sum = init;
        for (int i=first; i<last; i++) {
            sum += data[addr(i)];
        }
        return sum;
    }

    @Override
    public final Array adjacentDifference() {
        return adjacentDifference(0, size);
    }

    @Override
    public final Array adjacentDifference(final int first, final int last) {
        QL.require(first>=0 && first<=last && last<=size, INVALID_ARGUMENTS); // QA:[RG]::verified
        final Array diff = new Array(last-first);
        diff.data[0] = data[first];
        for (int i=1; i<last; i++) {
            diff.data[i] = data[i] - data[i-1];
        }
        return diff;
    }

    @Override
    public Array adjacentDifference(final BinaryDoubleOp f) {
        return adjacentDifference(0, size, f);
    }

    @Override
    public Array adjacentDifference(final int first, final int last, final BinaryDoubleOp f) {
        QL.require(first>=0 && first<=last && last<=size, INVALID_ARGUMENTS); // QA:[RG]::verified
        final Array diff = new Array(last-first);
        diff.data[0] = data[first];
        for (int i=1; i<last; i++) {
            diff.data[i] = f.op(data[i], data[i-1]);
        }
        return diff;
    }

    @Override
    public Array transform(final DoubleOp f) {
        return transform(0, this.size, f);
    }

    @Override
    public Array transform(final int first, final int last, final Ops.DoubleOp f) {
        QL.require(first>=0 && first<=last && last<=this.size && f!=null, INVALID_ARGUMENTS); // QA:[RG]::verified
        for (int i=first; i < last; i++) {
            data[addr(i)] = f.op(data[addr(i)]);
        }
        return this;
    }


    @Override
    public int lowerBound(final double val) {
        return lowerBound(0, size, val);
    }

    @Override
    public int lowerBound(int first, final int last, final double val) {
        QL.require(first>=0 && first<=last && last<=size, INVALID_ARGUMENTS); // QA:[RG]::verified
        int len = last - first;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = first + half;
            if (data[addr(middle)] < val) {
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
        return lowerBound(0, size, val, f);
    }

    @Override
    public int lowerBound(int first, final int last, final double val, final Ops.BinaryDoublePredicate f) {
        QL.require(first>=0 && first<=last && last<=size, INVALID_ARGUMENTS); // QA:[RG]::verified
        int len = last - first;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = first + half;
            if (f.op(data[addr(middle)], val)) {
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
        return upperBound(0, size, val);
    }

    @Override
    public int upperBound(int first, final int last, final double val) {
        QL.require(first>=0 && first<=last && last<=size, INVALID_ARGUMENTS); // QA:[RG]::verified
        int len = last - first;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = first + half;
            if (val < data[addr(middle)]) {
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
        return upperBound(0, size, val, f);
    }

    @Override
    public int upperBound(int first, final int last, final double val, final Ops.BinaryDoublePredicate f) {
        QL.require(first>=0 && first<=last && last<=size, INVALID_ARGUMENTS); // QA:[RG]::verified
        int len = last - first;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = first + half;
            if (f.op(val, data[addr(middle)])) {
                len = half;
            } else {
                first = middle + 1;
                len -= half + 1;
            }
        }
        return first;
    }


    //
    //  Element iterators
    //
    //  method              this    right    result
    //  ------------------- ------- -------- ------
    //  iterator            Array            RowIterator
    //  constIterator       Array            ConstRowIterator
    //


    /**
     * Creates a RowIterator for an entire row <code>row</code>
     *
     * @param dim is the desired row
     * @return an Array obtained from row A( row , [:] )
     * @throws IllegalArgumentException when indices are out of range
     */
    public RowIterator iterator() {
        return new RowIterator(0);
    }

    /**
     * Creates a RowIterator for row <code>row</code>
     *
     * @param dim is the desired row
     * @param col0 is the initial column, inclusive
     * @return an Array obtained from row A( row , [col0:) )
     * @throws IllegalArgumentException when indices are out of range
     */
    public RowIterator iterator(final int col0) {
        return new RowIterator(0, col0);
    }

    /**
     * Creates a RowIterator for row <code>row</code>
     *
     * @param dim is the desired row
     * @param col0 is the initial column, inclusive
     * @param col1 is the initial column, exclusive
     * @return an Array obtained from row A( row , [col0:col1) )
     * @throws IllegalArgumentException when indices are out of range
     */
    public RowIterator iterator(final int col0, final int col1) {
        return new RowIterator(0, col0, col1);
    }

    /**
     * Creates a constant, non-modifiable RowIterator for an entire row
     *
     * @param dim is the desired row
     * @return an Array obtained from row A( row , [;] )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstRowIterator constIterator() {
        return new ConstRowIterator(0);
    }

    /**
     * Creates a constant, non-modifiable RowIterator for row <code>row</code>
     *
     * @param dim is the desired row
     * @param col0 is the initial column, inclusive
     * @return an Array obtained from row A( row , [col0:) )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstRowIterator constIterator(final int col0) {
        return new ConstRowIterator(0, col0);
    }

    /**
     * Creates a constant, non-modifiable RowIterator for row <code>row</code>
     *
     * @param dim is the desired row
     * @param col0 is the initial column, inclusive
     * @param col1 is the initial column, exclusive
     * @return an Array obtained from row A( row , [col0:col1) )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstRowIterator constIterator(final int col0, final int col1) {
        return new ConstRowIterator(0, col0, col1);
    }


    //
    // implements BulkStorage
    //

    @Override
    public Array fill(final double scalar) {
        return (Array) super.bulkStorage.fill(scalar);
    }

    @Override
    public Array fill(final Array another) {
        return (Array) super.bulkStorage.fill(another);
    }

    @Override
    public Array sort() {
        return (Array) super.bulkStorage.sort();
    }

    @Override
    public Array swap(final Array another) {
        return (Array) super.bulkStorage.swap(another);
    }

}
