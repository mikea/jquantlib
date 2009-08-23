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
import org.jquantlib.math.Ops;
import org.jquantlib.math.Ops.DoubleOp;
import org.jquantlib.math.functions.Identity;


/**
 * 1-D array used in linear algebra.
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q2_RESEMBLANCE, version = Version.V097, reviewers = { "Richard Gomes" })
public class Array extends Cells {

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

//XXX
//    @Override
//    public String toString() {
//        final StringBuffer sb = new StringBuffer();
//
//        sb.append("[rows=").append(rows).append(" cols=").append(cols).append(" style=").append(style.toString()).append('\n');
//        sb.append(' ').append(data[0]);
//        for (int col = 1; col < this.cols; col++)
//            sb.append(", ").append(data[col]);
//        sb.append(" ]").append('\n');
//        return sb.toString();
//    }


    //
    // public methods
    //

    /**
     * Retrieves an element of <code>this</code> Matrix
     * <p>
     * This method is provided for performance reasons. See methods {@link #getAddress(int)} and {@link #getAddress(int, int)} for
     * more details
     *
     * @param row coordinate
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
     * @param row coordinate
     * @param col coordinate
     *
     * @see #getAddress(int)
     * @see #getAddress(int, int)
     */
    public void set(final int pos, final double value) {
        data[addr(pos)] = value;
    }

    /**
     * Fills all elements of this {@link Array} with a given scalar
     *
     * @param scalar is the value to be used to fill in
     */
    public Array fill(final double scalar) {
        Arrays.fill(data, scalar);
        return this;
    }

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
     * Accumulate values in a range.
     *
     * @note Mimics std::accumulate
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#3e6040dba097b64311fce39fa87d1b29">std::accumulate</a>
     */
    public double accumulate() {
        return accumulate(style.base, this.size+style.base, 0.0);
    }

    /**
     * Accumulate values in a range.
     *
     * @note Mimics std::accumulate
     *
     * @param from is the initial inclusive index
     * @param to   is the final exclusive index
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#3e6040dba097b64311fce39fa87d1b29">std::accumulate</a>
     */
    public double accumulate(final int from, final int to, final double init) {
        QL.require(from >= style.base && to >= from && to <= size+style.base ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        double sum = init;
        for (int i=from; i<to; i++)
            sum += data[addr(i)];
        return sum;
    }

    public double min() {
        return min(style.base, this.size+style.base);
    }

    /**
     * Return the minimum element in a range using comparison functor.
     *
     * @note Mimics std::min_element
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g09af772609c56f01dd33891d51340baf">std::min_element</a>
     */
    public double min(final int from, final int to) {
        QL.require(from >= style.base && to > from && to <= size+style.base ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        double result = data[addr(from)];
        for (int i=0; i<(to-from); i++) {
            final double tmp = data[addr(from+i)];
            if (tmp < result) result = tmp;
        }
        return result;
    }

    public double max() {
        return max(style.base, this.size+style.base);
    }

    /**
     * Return the maximum element in a range using comparison functor.
     *
     * @note Mimics std::max_element
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g595f12feaa16ea8aac6e5bd51782e123">std::max_element</a>
     */
    public double max(final int from, final int to) {
        QL.require(from >= style.base && to > from && to <= size+style.base ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        double result = data[addr(from)];
        for (int i=0; i<(to-from); i++) {
            final double tmp = data[addr(from+i)];
            if (tmp > result) result = tmp;
        }
        return result;
    }

    public Array sort() {
        Arrays.sort(data);
        return this;
    }

    /**
     * Return differences between adjacent values.
     *
     * @note Mimics std::adjacent_difference
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#d7df62eaf265ba5c859998b1673fd427">std::adjacent_difference</a>
     */
    public final Array adjacentDifference() {
        return adjacentDifference(style.base);
    }

    /**
     * Return differences between adjacent values.
     *
     * @note Mimics std::adjacent_difference
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#d7df62eaf265ba5c859998b1673fd427">std::adjacent_difference</a>
     */
    public final Array adjacentDifference(final int from) {
        return adjacentDifference(from, size+style.base);
    }

    /**
     * Return differences between adjacent values.
     *
     * @note Mimics std::adjacent_difference
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#d7df62eaf265ba5c859998b1673fd427">std::adjacent_difference</a>
     */
    public final Array adjacentDifference(final int from, final int to) {
        QL.require(from >= style.base && from <= to && to <= size+style.base ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        final Array diff = new Array(to-from+style.base);
        for (int i = from; i < to; i++) {
            final double curr = data[addr(i)];
            if (i == from)
                diff.data[diff.addr(i-from)] = curr;
            else {
                final double prev = data[addr(i-1)];
                diff.data[diff.addr(i-from)] = curr - prev;
            }
        }
        return diff;
    }

    /**
     * Finds the first position in which val could be inserted without changing the ordering.
     *
     * @note Mimics std::lower_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g0ff3b53e875d75731ff8361958fac68f">std::lower_bound</a>
     */
    public int lowerBound(final double val) {
        return lowerBound(style.base, this.size+style.base, val);
    }

    /**
     * Finds the first position in which val could be inserted without changing the ordering.
     *
     * @note Mimics std::lower_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g0ff3b53e875d75731ff8361958fac68f">std::lower_bound</a>
     */
    private int lowerBound(int from, final int to, final double val) {
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

    /**
     * Finds the last position in which val could be inserted without changing the ordering.
     *
     * @note Mimics std::upper_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g9bf525d5276b91ff6441e27386034a75">std::upper_bound</a>
     */
    public int upperBound(final double val) {
        return upperBound(style.base, this.size+style.base, val);
    }

    /**
     * Finds the last position in which val could be inserted without changing the ordering.
     *
     * @note Mimics std::upper_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g9bf525d5276b91ff6441e27386034a75">std::upper_bound</a>
     */
    private int upperBound(int from, final int to, final double val) {
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


    /**
     * Applies a transformation function to all elements of <code>this</code> Array
     *
     * @return this
     */
    public Array transform(final DoubleOp func) {
        return transform(style.base, this.size+style.base, func);
    }

    /**
     * Applies a transformation function to a range of elements of <code>this</code> Array
     *
     * @return this
     */
    public Array transform(final int from, final int to, final Ops.DoubleOp f) {
        QL.require(from>=style.base && from<=to && to<=this.size+style.base && f!=null, INVALID_ARGUMENTS); // QA:[RG]::verified
        if (f instanceof Identity) return this;
        for (int i = from; i < to; i++)
            data[addr(i)] = f.op(data[addr(i)]);
        return this;
    }




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

    public Array addAssign(final double scalar) {
        for (int i=0; i<size; i++)
            data[addrJ(i)] += scalar;
        return this;
    }

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
    public Array subAssign(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++) {
            data[addrJ(i)] -= another.data[another.addrJ(i)];
        }
        return this;
    }


    public Array mulAssign(final double scalar) {
        for (int i=0; i<size; i++)
            data[addrJ(i)] *= scalar;
        return this;
    }

    public Array divAssign(final double scalar) {
        for (int i=0; i<size; i++)
            data[addrJ(i)] /= scalar;
        return this;
    }

    public Array addAssign(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++)
            data[addrJ(i)] += another.data[another.addrJ(i)];
        return this;
    }

    public Array subAssign(final Matrix another) {
        QL.require(this.rows == another.rows && this.size == another.size, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++)
            data[i] -= another.data[i];
        return this;
    }

    public Array mulAssign(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int i=0; i<size; i++)
            data[addrJ(i)] *= another.data[another.addrJ(i)];
        return this;
    }


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
    //    /     div        Array   scalar    Array
    //    /     div        Array   Array     Array
    //

    public Array add(final double scalar) {
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] + scalar;
        return result;
    }

    public Array sub(final double scalar) {
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] - scalar;
        return result;
    }

    public Array mul(final double scalar) {
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] * scalar;
        return result;
    }

    public Array div(final double scalar) {
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] / scalar;
        return result;
    }

    public Array add(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] + another.data[another.addrJ(i)];
        return result;
    }

    public Array sub(final Array another) {
        QL.require(this.rows == another.rows && this.size == another.size, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] - another.data[another.addrJ(i)];
        return result;
    }

    public Array mul(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] * another.data[another.addrJ(i)];
        return result;
    }


    public Array div(final Array another) {
        QL.require(this.rows == another.rows && this.size == another.size, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.size, style);
        for (int i=0; i<size; i++)
            result.data[result.addrJ(i)] = data[addrJ(i)] / another.data[another.addrJ(i)];
        return result;
    }


    //
    //    Vetorial products
    //
    //    opr   method     this    right    result
    //    ----- ---------- ------- -------- ------
    //    *     mul        Array   Matrix   Array

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
    //    abs   abs        Array            Array
    //    sqrt  sqrt       Array            Array
    //    log   log        Array            Array
    //    exp   exp        Array            Array

    public Array abs() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++)
            result.data[result.addrJ(i)] = Math.abs(data[addrJ(i)]);
        return result;
    }

    public Array sqrt() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++)
            result.data[result.addrJ(i)] = Math.sqrt(data[addrJ(i)]);
        return result;
    }

    public Array log() {
        final Array result = new Array(this.size);
        for (int i=0; i<this.size; i++)
            result.data[result.addrJ(i)] = Math.log(data[addrJ(i)]);
        return result;
    }

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
    //    swap         Array   Array    this
    //    outerProduct Array   Array    Matrix
    //    dotProduct   Array   Array    double

    /**
     * Swaps contents of <code>this</code> Matrix by <code>another</code> Matrix
     *
     * @param another
     * @return this
     */
    public Array swap(final Array another) {
        super.swap(another);
        return this;
    }

    /**
     * Returns the <b>dot product</b> (also known as <b>scalar product</b>) of
     * <code>this</code> Array and <code>another</code> Array.
     * <p>
     * The definition of dot product is
     * {@latex[ \mathbf{a}\cdot \mathbf{b} = \sum_{i=1}^n a_ib_i = a_1b_1 + a_2b_2 + \cdots + a_nb_n }
     *
     * @param another Matrix
     * @return the dot product between this Matrix and another Matrix
     *
     * @see <a href="http://en.wikipedia.org/wiki/Dot_product">Dot Product</a>
     */
    public double dotProduct(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        return innerProduct(another, style.base, size+style.base);
    }

    /**
     * The inner product generalizes the dot product to abstract vector spaces and is normally denoted by {@latex$ <a , b>}.
     * <p>
     * As we are working in space {@latex$ \Re} (real numbers), it's sufficient to understand that both <b>inner product</b> and
     * <b>dot operator</b> give equivalent results.
     *
     * @param another Matrix
     * @return the inner product between this Matrix and another Matrix
     *
     * @see <a href="http://en.wikipedia.org/wiki/Inner_product">Inner Product</a>
     */
    public double innerProduct(final Array another) {
        QL.require(this.size == another.size, ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        return innerProduct(another, style.base, size+style.base);
    }


    /**
     * The inner product generalizes the dot product to abstract vector spaces and is normally denoted by {@latex$ <a , b>}.
     * <p>
     * As we are working in space {@latex$ \Re} (real numbers), it's sufficient to understand that both <b>inner product</b> and
     * <b>dot operator</b> give equivalent results.
     *
     * @param another Matrix
     * @param from is the start element
     * @param to is the end element
     * @return the inner product between this Matrix and another Matrix
     *
     * @see <a href="http://en.wikipedia.org/wiki/Inner_product">Inner Product</a>
     */
    public double innerProduct(final Array another, final int from, final int to) {
        QL.require(from >= style.base && to >= from && to <= size+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        double sum = 0.0;
        for (int i=from; i<to; i++)
            sum += data[addr(i)] * another.data[another.addr(i)];
        return sum;
    }


    /**
     * Performs the <b>outer product</b> of <code>this</code> Array and <code>another</code> Array
     * <p>
     * The definition of outer product is:<br/>
     * Given a vector $\mathbf{u} = (u_1, u_2, \dots, u_m)$ with <i>m</i> elements and
     *       a vector $\mathbf{v} = (v_1, v_2, \dots, v_n)$ with <i>n</i> elements,
     * their outer product $\mathbf{u} \otimes \mathbf{v}$ is defined as
     * {@latex[ $\mathbf{u} \otimes \mathbf{v} = \left[\begin{array}{c c c c}
     *    u_1v_1 & u_1v_2 & \dots  & u_1v_n \\
     *    u_2v_1 & u_2v_2 & \dots  & u_2v_n \\
     *    \vdots & \vdots & \ddots & \vdots \\
     *    u_mv_1 & u_mv_2 & \dots  & u_mv_n
     *  \end{array}\right] }
     *
     * @param another Array
     * @return the outer product of <code>this</code> Array and <code>another</code> Array
     *
     * @see <a href="http://en.wikipedia.org/wiki/Outer_product">Outer product</a>
     */
    public Matrix outerProduct(final Array another) {
        final Matrix result = new Matrix(this.size, another.size);
        for (int row=0; row<this.size; row++) {
            final int raddrJ = result.addrJ(row, 0);
            for (int col=0; col<another.size; col++)
                result.data[raddrJ+col] = data[addrJ(row)] * another.data[another.addrJ(col)];
        }
        return result;
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
     * @param row is the desired row
     * @return an Array obtained from row A( row , [:] )
     * @throws IllegalArgumentException when indices are out of range
     */
    public RowIterator iterator() {
        return new RowIterator(style.base);
    }

    /**
     * Creates a RowIterator for row <code>row</code>
     *
     * @param row is the desired row
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
     * @param row is the desired row
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
     * @param row is the desired row
     * @return an Array obtained from row A( row , [;] )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstRowIterator constIterator() {
        return new ConstRowIterator(style.base);
    }

    /**
     * Creates a constant, non-modifiable RowIterator for row <code>row</code>
     *
     * @param row is the desired row
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
     * @param row is the desired row
     * @param col0 is the initial column, inclusive
     * @param col1 is the initial column, exclusive
     * @return an Array obtained from row A( row , [col0:col1) )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstRowIterator constIterator(final int col0, final int col1) {
        return new ConstRowIterator(style.base, col0, col1);
    }

}
