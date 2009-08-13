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
public class Array extends Matrix {

    /**
     * Default constructor
     * <p>
     * Builds an empty Array
     */
    public Array() {
        super(1, 1);
    }

    /**
     * Builds an Array of <code>cols</code>
     *
     * @param cols is the number of columns
     */
    public Array(final int length) {
        super(1, length);
    }


    /**
     * Creates an Array given a double[] array
     *
     * @param data
     */
    public Array(final double[] array) {
        super(1, array.length);
        System.arraycopy(array, 0, this.data, 0, this.length);
    }


    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data
     */
    public Array(final Array a) {
        super(a);
    }



    //
    // Overrides Object
    //

    @Override
    public Array clone() {
        return this.copyOfRange(0, this.length);
    }


    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof Array)) return false;
        final Array another = (Array) o;
        if (this.rows != another.rows || this.cols != another.cols) return false;
        return Arrays.equals(this.data, another.data);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();

        sb.append("[");
        sb.append(this.data[0]);
        for (int col = 1; col < this.cols; col++)
            sb.append(", ").append(this.data[col]);
        sb.append(']').append('\n');
        return sb.toString();
    }


    //
    // overrides Matrix
    //
    // Not only these methods override methods from Matrix.
    // These methods are separated in this section in order to emphasise their importance.
    //

    @Override
    public Array getRow(final int row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Array getCol(final int col) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double get(final int row, final int col) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(final int row, final int col, final double value) {
        throw new UnsupportedOperationException();
    }



    //
    // public methods
    //

    // some convenience methods

    public int size() {
        return this.length;
    }

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
        return data[pos];
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
        data[pos] = value;
    }

    /**
     * Fills all elements of this {@link Array} with a given scalar
     *
     * @param scalar is the value to be used to fill in
     */
    @Override
    public Array fill(final double scalar) {
        Arrays.fill(data, scalar);
        return this;
    }

    @Override
    public Object toArray() {
        final double buffer[] = new double[this.length];
        return toArray(buffer);
    }

    public double[] toArray(final double[] buffer) {
        if (this.length != buffer.length) throw new IllegalArgumentException(); //TODO:message
        System.arraycopy(this.data, 0, buffer, 0, this.length);
        return buffer;
    }

    public double first() {
        return data[0];
    }

    public double last() {
        return data[data.length-1];
    }

    /**
     * Returns an Array containing a copy of region
     *
     * @param pos is the initial position
     * @param len is the quantity of elements
     *
     * @return a new Array containing a copy of region
     */
    public Array copyOfRange(final int pos, final int len) {
        QL.require(pos >= 0 && len > 0 && pos+len <= this.length,  INVALID_ARGUMENTS); //TODO: message
        final Array result = new Array(len);
        System.arraycopy(this.data, pos, result.data, 0, len);
        return result;
    }

    /**
     * Accumulate values in a range.
     *
     * @note Mimics std::accumulate
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#3e6040dba097b64311fce39fa87d1b29">std::accumulate</a>
     */
    public double accumulate() {
        return accumulate(0, this.length, 0.0);
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
        QL.require(from >=0 && from <=to && to <=this.length ,  INVALID_ARGUMENTS);
        double sum = init;
        for (int i=from; i<to; i++)
            sum += this.data[i];
        return sum;
    }

    public double min() {
        return min(0, this.length);
    }

    /**
     * Return the minimum element in a range using comparison functor.
     *
     * @note Mimics std::min_element
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g09af772609c56f01dd33891d51340baf">std::min_element</a>
     */
    public double min(final int from, final int to) {
        QL.require(from >=0 && from <=to && to <=this.length ,  INVALID_ARGUMENTS);
        double result = this.data[from];
        for (int i=from+1; i<to; i++) {
            final double tmp = this.data[i];
            if (tmp < result) result = tmp;
        }
        return result;
    }

    public double max() {
        return max(0, this.length);
    }

    /**
     * Return the maximum element in a range using comparison functor.
     *
     * @note Mimics std::max_element
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g595f12feaa16ea8aac6e5bd51782e123">std::max_element</a>
     */
    public double max(final int from, final int to) {
        QL.require(from >=0 && from <=to && to <=this.length ,  INVALID_ARGUMENTS);
        double result = this.data[from];
        for (int i=from+1; i<to; i++) {
            final double tmp = data[i];
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
        return adjacentDifference(0);
    }

    /**
     * Return differences between adjacent values.
     *
     * @note Mimics std::adjacent_difference
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#d7df62eaf265ba5c859998b1673fd427">std::adjacent_difference</a>
     */
    public final Array adjacentDifference(final int from) {
        QL.require(from>=0 && from <= this.length ,  INVALID_ARGUMENTS);
        final Array diff = new Array(this.length-from);
        for (int i = from; i < this.length; i++) {
            final double curr = this.data[i];
            if (i == from)
                diff.data[i-from] = curr;
            else {
                final double prev = this.data[i-1];
                diff.data[i-from] = curr - prev;
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
        return lowerBound(0, this.length-1, val);
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

        if (data[middle] < val) {
            from = middle;
            from++;
            len = len - half -1;
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
        return upperBound(0, this.length-1, val);
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

        if (val < data[middle])
            len = half;
        else {
            from = middle;
            from++;
            len = len - half -1;
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
        return transform(0, this.length, func);
    }

    /**
     * Applies a transformation function to a range of elements of <code>this</code> Array
     *
     * @return this
     */
    public Array transform(final int from, final int to, final Ops.DoubleOp f) {
        QL.require(from>=0 && from<=to && to<=this.length && f!=null,  INVALID_ARGUMENTS);
        if (f instanceof Identity) return this;
        for (int i = from; i < to; i++)
            data[i] = f.op(data[i]);
        return this;
    }




    //
    //    Assignment operations
    //
    //    opr   method     this    right    result
    //    ----- ---------- ------- -------- ------
    //    =     assign     Array            Array  (1)
    //    +=    addAssign  Array   scalar   this
    //    +=    addAssign  Array   Array    this
    //    -=    subAssign  Array   scalar   this
    //    -=    mulAssign  Array   Array    this
    //    *=    mulAssign  Array   scalar   this
    //    *=    mulAssign  Array   Array    this
    //    /=    divAssign  Array   scalar   this
    //    /=    divAssign  Array   Array    this

    public Array addAssign(final double scalar) {
        for (int i=0; i<length; i++)
            data[i] += scalar;
        return this;
    }

    public Array subAssign(final double scalar) {
        for (int i=0; i<length; i++)
            data[i] -= scalar;
        return this;
    }

    @Override
    public Array mulAssign(final double scalar) {
        for (int i=0; i<length; i++)
            data[i] *= scalar;
        return this;
    }

    @Override
    public Array divAssign(final double scalar) {
        for (int i=0; i<length; i++)
            data[i] /= scalar;
        return this;
    }

    public Array addAssign(final Array another) {
        QL.require(this.length == another.length ,  ARRAY_IS_INCOMPATIBLE);
        for (int i=0; i<length; i++)
            data[i] += another.data[i];
        return this;
    }

    @Override
    public Array subAssign(final Matrix another) {
        QL.require(this.rows == another.rows && this.length == another.length ,  MATRIX_IS_INCOMPATIBLE);
        for (int i=0; i<length; i++)
            data[i] -= another.data[i];
        return this;
    }

    public Array mulAssign(final Array another) {
        QL.require(this.length == another.length ,  ARRAY_IS_INCOMPATIBLE);
        for (int i=0; i<length; i++)
            data[i] *= another.data[i];
        return this;
    }


    public Array divAssign(final Matrix another) {
        QL.require(this.rows == another.rows && this.length == another.length ,  MATRIX_IS_INCOMPATIBLE);
        for (int i=0; i<length; i++)
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

    public Array add(final double scalar) {
        final Array result = new Array(this.length);
        for (int i=0; i<length; i++)
            result.data[i] = this.data[i] + scalar;
        return result;
    }

    public Array sub(final double scalar) {
        final Array result = new Array(this.length);
        for (int i=0; i<length; i++)
            result.data[i] = this.data[i] - scalar;
        return result;
    }

    @Override
    public Array mul(final double scalar) {
        final Array result = new Array(this.length);
        for (int i=0; i<length; i++)
            result.data[i] = this.data[i] * scalar;
        return result;
    }

    @Override
    public Array div(final double scalar) {
        final Array result = new Array(this.length);
        for (int i=0; i<length; i++)
            result.data[i] = this.data[i] / scalar;
        return result;
    }

    public Array add(final Array another) {
        QL.require(this.length == another.length ,  ARRAY_IS_INCOMPATIBLE);
        final Array result = new Array(this.length);
        for (int i=0; i<length; i++)
            result.data[i] = this.data[i] + another.data[i];
        return result;
    }

    public Array sub(final Array another) {
        QL.require(this.rows == another.rows && this.length == another.length ,  MATRIX_IS_INCOMPATIBLE);
        final Array result = new Array(this.length);
        for (int i=0; i<length; i++)
            result.data[i] = this.data[i] - another.data[i];
        return result;
    }

    @Override
    public Array mul(final Array another) {
        QL.require(this.length == another.length ,  ARRAY_IS_INCOMPATIBLE);
        final Array result = new Array(this.length);
        for (int i=0; i<length; i++)
            result.data[i] = this.data[i] * another.data[i];
        return result;
    }


    public Array div(final Matrix another) {
        QL.require(this.rows == another.rows && this.length == another.length ,  MATRIX_IS_INCOMPATIBLE);
        final Array result = new Array(this.length);
        for (int i=0; i<length; i++)
            result.data[i] = this.data[i] / another.data[i];
        return result;
    }


    //
    //    Vetorial products
    //
    //    opr   method     this    right    result
    //    ----- ---------- ------- -------- ------
    //    *     mul        Array   Matrix   Array

    @Override
    public Array mul(final Matrix matrix) {
        QL.require(this.length == matrix.rows ,  MATRIX_IS_INCOMPATIBLE);
        final Array result = new Array(this.cols);
        for (int i=0; i<this.length; i++) {
            int addr = matrix.addr(i,0);
            double sum = 0.0;
            for (int col=0; col<matrix.cols; col++) {
                sum += this.data[i] * matrix.data[addr];
                addr++;
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
        final Array result = new Array(this.length);
        for (int i=0; i<this.length; i++)
            result.data[i] = Math.abs(data[i]);
        return result;
    }

    public Array sqrt() {
        final Array result = new Array(this.length);
        for (int i=0; i<this.length; i++)
            result.data[i] = Math.sqrt(data[i]);
        return result;
    }

    public Array log() {
        final Array result = new Array(this.length);
        for (int i=0; i<this.length; i++)
            result.data[i] = Math.log(data[i]);
        return result;
    }

    public Array exp() {
        final Array result = new Array(this.length);
        for (int i=0; i<this.length; i++)
            result.data[i] = Math.exp(data[i]);
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

    public Array swap(final Array another) {
        super.swap(another);
        return this;
    }

    public Array swap(final int pos1, final int pos2) {
        final double tmp = data[pos1];
        data[pos1] = data[pos2];
        data[pos2] = tmp;
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
        QL.require(this.length == another.length ,  ARRAY_IS_INCOMPATIBLE);
        return innerProduct(another, 0, length);
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
        QL.require(this.length == another.length ,  ARRAY_IS_INCOMPATIBLE);
        return innerProduct(another, 0, length);
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
        double sum = 0.0;
        for (int i=from; i<to; i++)
            sum += this.data[i] * another.data[i];
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
        final Matrix result = new Matrix(this.length, another.length);
        for (int row=0; row<this.length; row++) {
            final int addr = result.addr(row, 0);
            for (int col=0; col<another.length; col++)
                result.data[addr+col] = this.data[row] * another.data[col];
        }
        return result;
    }


    //
    // protected methods
    //


    /**
     * This method returns the address of the first column in a given row
     * <p>
     * This method is used internally and is provided for performance reasons.
     */
    protected int addr(final int row) {
        return row;
    }

    @Override
    protected int addr(final int row, final int col) {
        throw new UnsupportedOperationException();
    }

}
