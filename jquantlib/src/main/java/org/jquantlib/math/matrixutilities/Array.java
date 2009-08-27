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
     * Builds an empty Array
     */
    public Array() {
        this(Style.JAVA);
    }

    /**
     * Builds an empty Array
     *
     * @param style allows transparent access to elements by FORTRAN based algorithms.
     */
    public Array(final Style style) {
        super(1, 1, style);
    }

    /**
     * Builds an Array of <code>cols</code>
     *
     * @param size is the size of <code>this</code> Array
     * @throws IllegalArgumentException if size are less than zero
     */
    public Array(final int size) {
        this(size, Style.JAVA);
    }

    /**
     * Builds an Array of <code>cols</code>
     *
     * @param size is the size of <code>this</code> Array
     * @param style allows transparent access to elements by FORTRAN based algorithms.
     * @throws IllegalArgumentException if size are less than zero
     *
     * @see Style
     */
    public Array(final int size, final Style style) {
        super(1, size, style);
    }

    /**
     * Creates an Array given a double[] array
     *
     * @param data
     */
    public Array(final double[] array) {
        this(array, Style.JAVA);
    }

    /**
     * Creates an Array given a double[] array
     *
     * @param data is a uni-dimensional array, always organized as a Java index access style, zero-based double[].
     * @param style allows transparent access to elements by FORTRAN based algorithms.
     *
     * @see Style
     */
    public Array(final double[] array, final Style style) {
        super(1, array.length, style);
        System.arraycopy(array, 0, data, 0, this.size);
    }

    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data
     */
    public Array(final Array a) {
        super(1, a.cols, a.style);
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
        if (o == null || !(o instanceof Array)) return false;
        final Array another = (Array) o;
        if (this.rows != another.rows || this.cols != another.cols) return false;
        return Arrays.equals(data, another.data);
    }


    //
    // public methods
    //

    public Object toArray() {
        return toArray(Style.JAVA);
    }

    public Object toArray(final Style style) {
        final double buffer[] = new double[this.size+style.base];
        return toArray(buffer, style);
    }

    public double[] toArray(final double[] buffer) {
        return toArray(buffer, Style.JAVA);
    }

    public double[] toArray(final double[] buffer, final Style style) {
        QL.require(this.size+style.base == buffer.length, WRONG_BUFFER_LENGTH); // QA:[RG]::verified
        System.arraycopy(data, 0, buffer, style.base, this.size);
        return buffer;
    }

    public double first() {
        return data[0];
    }

    public double last() {
        return data[size-1];
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
        for (int i=0; i<size; i++)
            data[addrJ(i)] += scalar;
        return this;
    }

    @Override
    public Array subAssign(final double scalar) {
        for (int i=0; i<size; i++)
            data[addrJ(i)] -= scalar;
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
            data[addrJ(i)] -= another.data[another.addrJ(i)];
        }
        return this;
    }

    @Override
    public Array mulAssign(final double scalar) {
        for (int i=0; i<size; i++)
            data[addrJ(i)] *= scalar;
        return this;
    }

    @Override
    public Array divAssign(final double scalar) {
        for (int i=0; i<size; i++)
            data[addrJ(i)] /= scalar;
        return this;
    }

    @Override
    public Array addAssign(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++)
            data[addrJ(i)] += another.data[another.addrJ(i)];
        return this;
    }

    @Override
    public Array mulAssign(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++)
            data[addrJ(i)] *= another.data[another.addrJ(i)];
        return this;
    }

    @Override
    public Array divAssign(final Array another) {
        QL.require(this.rows == another.rows && this.size == another.size, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++)
            data[i] /= another.data[i];
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
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] + scalar;
        return result;
    }

    @Override
    public Array sub(final double scalar) {
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] - scalar;
        return result;
    }

    @Override
    public Array mul(final double scalar) {
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] * scalar;
        return result;
    }

    @Override
    public Array negative() {
        return mul(-1);
    }

    @Override
    public Array div(final double scalar) {
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] / scalar;
        return result;
    }

    @Override
    public Array add(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] + another.data[another.addrJ(i)];
        return result;
    }

    @Override
    public Array sub(final Array another) {
        QL.require(this.rows == another.rows && this.size == another.size, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] - another.data[another.addrJ(i)];
        return result;
    }

    @Override
    public Array mul(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] * another.data[another.addrJ(i)];
        return result;
    }

    @Override
    public Array div(final Array another) {
        QL.require(this.rows == another.rows && this.size == another.size, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] / another.data[another.addrJ(i)];
        return result;
    }

    @Override
    public Array mul(final Matrix matrix) {
        QL.require(this.size == matrix.rows, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.cols);
        for (int i=0; i<this.size; i++) {
            int addrJ = matrix.addrJ(i, 0);
            double sum = 0.0;
            for (int col=0; col<matrix.cols; col++) {
                sum += data[addrJ(i)] * matrix.data[addrJ];
                addrJ++;
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
        return min(style.base, this.size+style.base);
    }

    @Override
    public double min(final int from, final int to) {
        QL.require(from >= style.base && to > from && to <= size+style.base ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        double result = data[addr(from)];
        for (int i=0; i<(to-from); i++) {
            final double tmp = data[addr(from+i)];
            if (tmp < result) result = tmp;
        }
        return result;
    }

    @Override
    public double max() {
        return max(style.base, this.size+style.base);
    }

    @Override
    public double max(final int from, final int to) {
        QL.require(from >= style.base && to > from && to <= size+style.base ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        double result = data[addr(from)];
        for (int i=0; i<(to-from); i++) {
            final double tmp = data[addr(from+i)];
            if (tmp > result) result = tmp;
        }
        return result;
    }

    @Override
    public Array abs() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++)
            result.data[result.addrJ(i)] = Math.abs(data[addrJ(i)]);
        return result;
    }

    @Override
    public Array sqr() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++) {
            final double a = data[addrJ(i)];
            result.data[result.addrJ(i)] = a*a;
        }
        return result;
    }

    @Override
    public Array sqrt() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++)
            result.data[result.addrJ(i)] = Math.sqrt(data[addrJ(i)]);
        return result;
    }

    @Override
    public Array log() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++)
            result.data[result.addrJ(i)] = Math.log(data[addrJ(i)]);
        return result;
    }

    @Override
    public Array exp() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++)
            result.data[result.addrJ(i)] = Math.exp(data[addrJ(i)]);
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
    public double accumulate() {
        return accumulate(style.base, this.size+style.base, 0.0);
    }

    @Override
    public double accumulate(final double init) {
        return accumulate(style.base, this.size+style.base, init);
    }

    @Override
    public double accumulate(final int from, final int to, final double init) {
        QL.require(from >= style.base && to >= from && to <= size+style.base ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        double sum = init;
        for (int i=from; i<to; i++)
            sum += data[addr(i)];
        return sum;
    }

    @Override
    public double dotProduct(final Array another) {
        return dotProduct(another, another.style.base, another.size+another.style.base);
    }

    @Override
    public double dotProduct(final Array another, final int from, final int to) {
        QL.require(this.size() == to-from, ITERATOR_IS_INCOMPATIBLE);
        QL.require(from >= another.style.base && to >= from && to <= another.size+another.style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        double sum = 0.0;
        final int offset = from - another.base();
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
        return outerProduct(another, another.style.base, another.size+another.style.base);
    }

    @Override
    public Matrix outerProduct(final Array another, final int from, final int to) {
        QL.require(from >= another.style.base && to >= from && to <= another.size+another.style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        final Matrix m = new Matrix(this.size, to-from, style());
        for (int i=0; i<this.size(); i++) {
            for (int j=from-another.base(); j < to-another.base(); j++) {
                m.data[m.addrJ(i, j)] = this.data[i] * another.data[j];
            }
        }
        return m;
    }

    @Override
    public final Array adjacentDifference() {
        return adjacentDifference(style.base, size+style.base);
    }

    @Override
    public final Array adjacentDifference(final int from, final int to) {
        QL.require(from >= style.base && from <= to && to <= size+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        final Array diff = new Array(to-from);
        diff.data[0] = data[from-style.base];
        for (int i = 1; i < to-style.base; i++) {
            diff.data[i] = data[i] - data[i-1];
        }
        return diff;
    }

    @Override
    public Array adjacentDifference(final BinaryDoubleOp f) {
        return adjacentDifference(style.base, size+style.base, f);
    }

    @Override
    public Array adjacentDifference(final int from, final int to, final BinaryDoubleOp f) {
        QL.require(from >= style.base && from <= to && to <= size+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        final Array diff = new Array(to-from);
        diff.data[0] = data[from-style.base];
        for (int i = 1; i < to-style.base; i++) {
            diff.data[i] = f.op(data[i], data[i-1]);
        }
        return diff;
    }

    @Override
    public Array transform(final DoubleOp func) {
        return transform(style.base, this.size+style.base, func);
    }

    @Override
    public Array transform(final int from, final int to, final Ops.DoubleOp f) {
        QL.require(from>=style.base && from<=to && to<=this.size+style.base && f!=null, INVALID_ARGUMENTS); // QA:[RG]::verified
        for (int i = from; i < to; i++)
            data[addr(i)] = f.op(data[addr(i)]);
        return this;
    }

    @Override
    public int lowerBound(final double val) {
        return lowerBound(style.base, this.size+style.base, val);
    }

    @Override
    public int lowerBound(int from, final int to, final double val) {
        int len = to - from;
        int half;
        int middle;

        while (len > 0) {
            half = len >> 1;
            middle = from;
            middle = middle + half;

            if (data[addr(middle)] < val) {
                from = middle;
                from++;
                len = len - half - 1;
            } else
                len = half;
        }
        return from;
    }

    @Override
    public int upperBound(final double val) {
        return upperBound(style.base, this.size+style.base, val);
    }

    @Override
    public int upperBound(int from, final int to, final double val) {
        int len = to - from;
        int half;
        int middle;

        while (len > 0) {
            half = len >> 1;
            middle = from;
            middle = middle + half;

            if (val < data[addr(middle)])
                len = half;
            else {
                from = middle;
                from++;
                len = len - half - 1;
            }
        }
        return from;
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
        return new RowIterator(style.base);
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
        return new RowIterator(style.base, col0);
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
        return new RowIterator(style.base, col0, col1);
    }

    /**
     * Creates a constant, non-modifiable RowIterator for an entire row
     *
     * @param dim is the desired row
     * @return an Array obtained from row A( row , [;] )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstRowIterator constIterator() {
        return new ConstRowIterator(style.base);
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
        return new ConstRowIterator(style.base, col0);
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
        return new ConstRowIterator(style.base, col0, col1);
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
