/*
Copyright (C) 2008 Richard Gomes

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

/*
Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl
Copyright (C) 2003, 2004 Ferdinando Ametrano

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
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.lang.iterators.DoubleListIterator;

/**
 * Bidimensional matrix operations
 * <p>
 * Performance of multidimensional arrays is a big concern in Java. This is because multidimensional arrays are stored as arrays to
 * arrays, spanning this concept to as many depths as necessary. In C++, a multidimensional array is stored internally as a
 * unidimensional array where depths are stacked together one after another. A very simple calculation is needed to map multiple
 * dimensional indexes to an unidimensional index.
 * <p>
 * This implementation provides the C/C++ approach of an internal unidimensional array. Everytime a bidimensional index is involved
 * (because this is a 2d matrix) it is converted to a unidimensional index. In case developers are seriously concerned about
 * performance, a unidimensional access method is provided, giving a chance for application developers to 'cache' the starting of a
 * row and reducing the number of multiplications needed for offset calculations.
 * <p>
 * <p>
 * <b>Assignment operations</b>
 * <pre>
 *   opr method     this    right    result
 *   --- ---------- ------- -------- ------
 *   =   assign     Matrix           Matrix (1)
 *   +=  addAssign  Matrix  Matrix   this
 *   -=  subAssign  Matrix  Matrix   this
 *   *=  mulAssign  Matrix  scalar   this
 *   /=  divAssign  Matrix  scalar   this
 * </pre>
 * <p>
 * <p>
 * <b>Algebraic products</b>
 * <pre>
 *   opr method     this    right    result
 *   --- ---------- ------- -------- ------
 *   +   add        Matrix  Matrix   Matrix
 *   -   sub        Matrix  Matrix   Matrix
 *   -   negative   Matrix           this
 *   *   mul        Matrix  scalar   Matrix
 *   /   div        Matrix  scalar   Matrix
 * </pre>
 * <p>
 * <p>
 * <b>Vetorial products</b>
 * <pre>
 *   method         this    right    result
 *   -------------- ------- -------- ------
 *   mul            Matrix  Array    Array
 *   mul            Matrix  Matrix   Matrix
 * </pre>
 * <p>
 * <p>
 * <b>Decompositions</b>
 * <pre>
 *   method         this    right    result
 *   -------------- ------- -------- ------
 *   lu             Matrix           LUDecomposition
 *   qr             Matrix           QRDecomposition
 *   cholensky      Matrix           CholeskyDecomposition
 *   svd            Matrix           SingularValueDecomposition
 *   eigenvalue     Matrix           EigenvalueDecomposition
 * </pre>
 * <p>
 * <p>
 * <b>Element iterators</b>
 * <pre>
 *   method         this    right    result
 *   ------------   ------- -------- ------
 *   rowIterator    Matrix           RowIterator
 *   columnIterator Matrix           ColumnIterator
 * </pre>
 * <p>
 * <p>
 * <b>Miscellaneous</b>
 * <pre>
 *   method         this    right    result
 *   -------------- ------- -------- ------
 *   transpose      Matrix           Matrix
 *   diagonal       Matrix           Array
 *   determinant    Matrix           double
 *   inverse        Matrix           Matrix
 *   solve          Matrix           Matrix
 *   swap           Matrix  Matrix   this
 *   range          Matrix  Matrix   Matrix
 * </pre>
 * <p>
 * <p>
 * (1): clone()<br/>
 * (2): Unary + is equivalent to: array.clone()<br/>
 * (3): Unary ? is equivalent to: array.clone().mulAssign(-1)
 * <p>
 * @Note This class is not thread-safe
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q1_TRANSLATION, version = Version.V097, reviewers = { "Richard Gomes" })
public class Matrix extends Cells {

    //
    // public constructors
    //

//    /**
//     * Default constructor
//     * <p>
//     * Builds an empty Matrix
//     */
//    public Matrix() {
//        super(0,0);
//    }

    /**
     * Builds a Matrix of <code>rows</code> by <code>cols</code>
     *
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @throws IllegalArgumentException if parameters are less than zero
     */
    public Matrix(final int rows, final int cols) {
        super(rows, cols);
    }


    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data
     */
    public Matrix(final double[][] data) {
        super(data.length, data[0].length);

        for (int i=0; i<this.rows; i++) {
            final int base=addr(i,0);
            System.arraycopy(data[i], 0, this.data, base, this.cols);
        }
    }


    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data
     */
    public Matrix(final Matrix m) {
        super(m.rows, m.cols);
        System.arraycopy(m.data, 0, this.data, 0, this.size);
    }


    //
    // overrides Object
    //

    /**
     * Returns a copy of <code>this</code> Matrix
     */
    @Override
    public Matrix clone() {
        return new Matrix(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof Matrix)) return false;
        final Matrix another = (Matrix) o;
        if (this.rows != another.rows || this.cols != another.cols) return false;
        return Arrays.equals(this.data, another.data);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append('[').append('\n');
        for (int row = 0; row < this.rows; row++) {
            int addr = addr(row,0);
            sb.append(" [");
            sb.append(this.data[addr]);
            for (int col = 1; col < this.cols; col++) {
                addr++;
                sb.append(", ");
                sb.append(this.data[addr]);
            }
            sb.append("]\n");
        }
        sb.append("]\n");
        return sb.toString();
    }


    //
    // public methods
    //


    // some convenience methods

    public final int rows()    { return rows; }
    public final int columns() { return cols; }


    public Object toArray() {
        final double buffer[][] = new double[this.rows][this.cols];
        return toArray(buffer);
    }

    public double[][] toArray(final double[][] buffer) {
        QL.require(this.rows == buffer.length && this.cols == buffer[0].length, WRONG_BUFFER_LENGTH); // QA:[RG]::verified
        int addr = 0;
        for (int row=0; row<this.rows; row++) {
            System.arraycopy(this.data, addr, buffer[row], 0, this.cols);
            addr += this.cols;
        }
        return buffer;
    }

    /**
     * @return true if the number of rows or number of columns of this {@link Matrix} is zero
     */
    public boolean empty() {
        return (rows == 0 || cols == 0);
    }

    /**
     * Fills all elements of this Matrix with a given scalar
     *
     * @param scalar is the value to be used to fill in
     * @return this
     */
    public Matrix fill(final double scalar) {
        Arrays.fill(data, scalar);
        return this;
    }

    /**
     * Fills <code>this</code> Matrix with contents from <code>another</code> Matrix
     *
     * @param another is the source Matrix where data is being copied from
     * @return <code>this</code>
     */
    public Matrix fill(final Matrix another) {
        if (this.size != another.size)
            throw new IllegalArgumentException(); // TODO: message
        System.arraycopy(another.data, 0, this.data, 0, this.size);
        return this;
    }

    /**
     * Retrieves an elementof <code>this</code> Matrix which identified by <i>(row, col)</i>
     *
     * @param row coordinate
     * @param col coordinate
     * @return the contents of a given cell
     */
    public double get(final int row, final int col) {
        return data[addr(row, col)];
    }

    /**
     * Stores a value into an element of <code>this</code> Matrix which is identified by <i>(row, col)</i>
     *
     * @param row coordinate
     * @param col coordinate
     */
    public void set(final int row, final int col, final double value) {
        this.data[addr(row, col)] = value;
    }

    /**
     * Returns an Array which contains elements of a requested row
     *
     * @param row is the requested row
     * @return a new Array instance
     */
    public Array getRow(final int row) {
        final Array vector = new Array(this.cols);
        System.arraycopy(this.data, this.addr(row,0), vector.data, 0, this.cols);
        return vector;
    }

    /**
     * Returns an Array which contains elements of a requested column
     *
     * @param col is the requested column
     * @return a new Array instance
     */
    public Array getCol(final int col) {
        final Array array = new Array(this.rows);
        if (this.cols == 1)
            System.arraycopy(this.data, 0, array.data, 0, this.size);
        else {
            int addr = addr(0, col);
            for (int row = 0; row < this.rows; row++) {
                array.data[row] = this.data[addr];
                addr += this.cols;
            }
        }
        return array;
    }

    /**
     * Overwrites contents of a certain row
     *
     * @param row is the requested row to be overwritten
     * @param array contains the elements to be copied
     */
    public void setRow(final int row, final Array array) {
        QL.require(this.cols == array.size ,  ARRAY_IS_INCOMPATIBLE);
        System.arraycopy(array.data, 0, this.data, this.addr(row,0), this.cols);
    }

    /**
     * Overwrites contents of a certain column
     *
     * @param col is the requested column to be overwritten
     * @param array contains the elements to be copied
     */
    public void setCol(final int col, final Array array) {
        QL.require(this.rows == array.size ,  ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        if (this.cols == 1)
            System.arraycopy(array.data, 0, this.data, 0, this.size);
        else {
            int addr = addr(0, col);
            for (int row = 0; row < this.rows; row++) {
                this.data[addr] = array.data[row];
                addr += this.cols;
            }
        }
    }


    //
    //	Assignment operations
    //
    //	opr   method     this    right    result
    //	----- ---------- ------- -------- ------
    //	=     assign     Matrix           Matrix (1)
    //	+=    addAssign  Matrix  Matrix   this
    //	-=    subAssign  Matrix  Matrix   this
    //	*=    mulAssign  Matrix  scalar   this
    //	/=    divAssign  Matrix  scalar   this
    //


    /**
     * Returns the result of an addition of <code>this</code> Matrix and <code>another</code> Matrix
     *
     * @param another
     * @return this
     */
    public Matrix addAssign(final Matrix another) {
        QL.require(this.rows == another.rows && this.cols == another.cols ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int row=0; row<rows; row++) {
            int addr = addr(row,0);
            for (int col=0; col<cols; col++) {
                this.data[addr] += another.data[addr];
                addr++;
            }
        }
        return this;
    }

    /**
     * Returns the result of a subtraction of <code>this</code> Matrix and <code>another</code> Matrix
     *
     * @param another
     * @return this
     */
    public Matrix subAssign(final Matrix another) {
        QL.require(this.rows == another.rows && this.cols == another.cols ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int row=0; row<rows; row++) {
            int addr = addr(row,0);
            for (int col=0; col<cols; col++) {
                this.data[addr] -= another.data[addr];
                addr++;
            }
        }
        return this;
    }

    /**
     * Returns the result of a multiplication of <code>this</code> Matrix by a <code>scalar</code>
     *
     * @param scalar
     * @return this
     */
    public Matrix mulAssign(final double scalar) {
        for (int row = 0; row < rows; row++) {
            final int rowAddress = addr(row,0);
            for (int col = 0; col < cols; col++) {
                final int cellAddress = rowAddress + col;
                data[cellAddress] *= scalar;
            }
        }
        return this;
    }

    /**
     * Returns the result of a division of <code>this</code> Matrix by a <code>scalar</code>
     *
     * @param scalar
     * @return this
     */
    public Matrix divAssign(final double scalar) {
        for (int row = 0; row < rows; row++) {
            final int rowAddress = addr(row,0);
            for (int col = 0; col < cols; col++) {
                final int cellAddress = rowAddress + col;
                data[cellAddress] /= scalar;
            }
        }
        return this;
    }



    //
    //	Algebraic products
    //
    //	opr   method     this    right    result
    //	----- ---------- ------- -------- ------
    //	+     add        Matrix  Matrix    Matrix
    //	-     sub        Matrix  Matrix    Matrix
    //  -     negative   Matrix            this
    //	*     mul        Matrix  scalar    Matrix
    //	/     div        Matrix  scalar    Matrix
    //

    /**
     * Returns the result of addition of <code>this</code> Matrix and <code>another</code> Matrix
     *
     * @param another
     * @return a new instance
     */
    public Matrix add(final Matrix another) {
        QL.require(this.rows == another.rows && this.cols == another.cols ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(this.rows, this.cols);
        for (int row=0; row<rows; row++) {
            int addr = addr(row,0);
            for (int col=0; col<cols; col++) {
                result.data[addr] = this.data[addr] + another.data[addr];
                addr++;
            }
        }
        return result;
    }

    /**
     * Returns the result of a subtraction of <code>this</code> Matrix and <code>another</code> Matrix
     *
     * @param another
     * @return a new instance
     */
    public Matrix sub(final Matrix another) {
        QL.require(this.rows == another.rows && this.cols == another.cols ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(this.rows, this.cols);
        for (int row=0; row<rows; row++) {
            int addr = addr(row,0);
            for (int col=0; col<cols; col++) {
                result.data[addr] = this.data[addr] - another.data[addr];
                addr++;
            }
        }
        return result;
    }


    /**
     * Returns the negative of <code>this</code> Matrix
     *
     * @return this
     */
    public Matrix negative() {
        return this.mulAssign(-1);
    }


    /**
     * Returns the result of a multiplication of <code>this</code> Matrix by a <code>scalar</code>
     *
     * @param scalar
     * @return a new instance
     */
    public Matrix mul(final double scalar) {
        final Matrix result = new Matrix(this.rows, this.cols);
        for (int row=0; row<rows; row++) {
            int addr = addr(row,0);
            for (int col=0; col<cols; col++) {
                result.data[addr] = data[addr] * scalar;
                addr++;
            }
        }
        return result;
    }

    /**
     * Returns the result of a division of <code>this</code> Matrix by a <code>scalar</code>
     *
     * @param scalar
     * @return a new instance
     */
    public Matrix div(final double scalar) {
        final Matrix result = new Matrix(this.rows, this.cols);
        for (int row=0; row<rows; row++) {
            int addr = addr(row,0);
            for (int col=0; col<cols; col++) {
                result.data[addr] = data[addr] / scalar;
                addr++;
            }
        }
        return result;
    }


    //
    //	Vetorial products
    //
    //	opr   method     this    right    result
    //	----- ---------- ------- -------- ------
    //	*     mul        Matrix  Array    Array
    //	*     mul        Matrix  Matrix   Matrix
    //

    /**
     * Returns an Array which represents the multiplication of <code>this</code> Matrix by an Array
     *
     * @param array is the input Array which participates in the operation
     * @return a new Array which contains the result
     */
    public Array mul(final Array array) {
        QL.require(this.cols == array.size ,  ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(this.cols);
        for (int col=0; col<this.cols; col++) {
            int addr = addr(0, col);
            double sum = 0.0;
            for (int row=0; row<this.rows; row++) {
                sum  += this.data[addr] * array.data[col];
                addr += this.cols;
            }
            result.data[col] = sum;
        }
        return result;
    }

    /**
     * Returns a Matrix which represents the multiplication of <code>this</code> Matrix and <code>another</code> Matrix
     *
     * @param another
     * @return a new Matrix which contains the result
     */
    public Matrix mul(final Matrix another) {
        QL.require(this.cols == another.rows ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(this.rows, another.cols);
        for (int col = 0; col < another.cols; col++) {
            final int caddr = another.addr(0, col);
            for (int row = 0; row < this.rows; row++) {
                final int raddr = addr(row, 0);
                int addr = caddr;
                double sum = 0.0;
                for (int i = 0; i < this.cols; i++) {
                    sum += this.data[raddr + i] * another.data[addr];
                    addr += another.cols;
                }
                result.set(row, col, sum);
            }
        }
        return result;
    }


    //
    // Decompositions
    //
    //  method       this    right    result
    //  ----------   ------- -------- ------
    //  lu           Matrix           LUDecomposition
    //  qr           Matrix           QRDecomposition
    //  cholensky    Matrix           CholeskyDecomposition
    //  svd          Matrix           SingularValueDecomposition
    //  eigenvalue   Matrix           EigenvalueDecomposition
    //

    /**
     * LU Decomposition
     *
     * @param m is a rectangular Matrix
     * @return Structure to access L, U and piv.
     */
    public LUDecomposition lu() {
        return new LUDecomposition(this);
    }

    /**
     * QR Decomposition
     *
     * @return QRDecomposition
     * @see QRDecomposition
     */
    public QRDecomposition qr() {
        return new QRDecomposition(this);
    }

    /**
     * QR Decomposition
     *
     * @return QRDecomposition
     * @see QRDecomposition
     */
    public QRDecomposition qr(final boolean pivot) {
        return new QRDecomposition(this, pivot);
    }

    /**
     * Cholesky Decomposition
     *
     * @return CholeskyDecomposition
     * @see CholeskyDecomposition
     */
    public CholeskyDecomposition cholesky() {
        return new CholeskyDecomposition(this);
    }

    /**
     * Symmetric Schur Decomposition
     *
     * @return SymmetricSchurDecomposition
     * @see SymmetricSchurDecomposition
     */
    public SymmetricSchurDecomposition schur() {
        return new SymmetricSchurDecomposition(this);
    }

    /**
     * Singular Value Decomposition
     *
     * @return SingularValueDecomposition
     * @see SVD
     */
    public SVD svd() {
        return new SVD(this);
    }

    /**
     * Eigenvalue Decomposition
     *
     * @return EigenvalueDecomposition
     * @see EigenvalueDecomposition
     */
    public EigenvalueDecomposition eigenvalue() {
        return new EigenvalueDecomposition(this);
    }


    //
    //  Element iterators
    //
    //  method              this    right    result
    //  ------------------- ------- -------- ------
    //  rowIterator         Matrix           RowIterator
    //  constRowIterator    Matrix           ConstRowIterator
    //  columnIterator      Matrix           ColumnIterator
    //  constColumnIterator Matrix           ConstColumnIterator
    //


    /**
     * Creates a RowIterator for an entire row <code>row</code>
     *
     * @param row is the desired row
     * @return an Array obtained from row A( row , [:] )
     * @throws IllegalArgumentException when indices are out of range
     */
    public RowIterator rowIterator(final int row) {
        return new RowIterator(row);
    }

    /**
     * Creates a RowIterator for row <code>row</code>
     *
     * @param row is the desired row
     * @param col0 is the initial column, inclusive
     * @return an Array obtained from row A( row , [col0:) )
     * @throws IllegalArgumentException when indices are out of range
     */
    public RowIterator rowIterator(final int row, final int col0) {
        return new RowIterator(row, col0);
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
    public RowIterator rowIterator(final int row, final int col0, final int col1) {
        return new RowIterator(row, col0, col1);
    }

    /**
     * Creates a constant, non-modifiable RowIterator for an entire row
     *
     * @param row is the desired row
     * @return an Array obtained from row A( row , [;] )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstRowIterator constRowIterator(final int row) {
        return new ConstRowIterator(row);
    }

    /**
     * Creates a constant, non-modifiable RowIterator for row <code>row</code>
     *
     * @param row is the desired row
     * @param col0 is the initial column, inclusive
     * @return an Array obtained from row A( row , [col0:) )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstRowIterator constRowIterator(final int row, final int offset) {
        return new ConstRowIterator(row, offset);
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
    public ConstRowIterator constRowIterator(final int row, final int col0, final int col1) {
        return new ConstRowIterator(row, col0, col1);
    }

    /**
     * Creates a ColumnIterator for an entire column <code>col</code>
     *
     * @param col is the desired column
     * @return an Array obtained from row A( [;] , col )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ColumnIterator columnIterator(final int col) {
        return new ColumnIterator(col);
    }

    /**
     * Creates a ColumnIterator for column <code>col</code>
     *
     * @param col is the desired column
     * @param row0 is the initial row, inclusive
     * @return an Array obtained from row A( [row0:) , col )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ColumnIterator columnIterator(final int col, final int row0) {
        return new ColumnIterator(col, row0);
    }

    /**
     * Creates a constant, non-modifiable ColumnIterator for column <code>col</code>
     *
     * @param col is the desired column
     * @param row0 is the initial row, inclusive
     * @param row1 is the final row, exclusive
     * @return an Array obtained from row A( [row0:row1) , col )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ColumnIterator columnIterator(final int col, final int row0, final int row1) {
        return new ColumnIterator(col, row0, row1);
    }

    /**
     * Creates a constant, non-modifiable ColumnIterator for the entire column <code>col</code>
     *
     * @param col is the desired column
     * @return an Array obtained from row A( [:] , col )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstColumnIterator constColumnIterator(final int col) {
        return new ConstColumnIterator(col);
    }

    /**
     * Creates a constant, non-modifiable ColumnIterator for column <code>col</code>
     *
     * @param col is the desired column
     * @param row0 is the initial row, inclusive
     * @return an Array obtained from row A( [row0:) , col )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstColumnIterator constColumnIterator(final int col, final int row0) {
        return new ConstColumnIterator(col, row0);
    }


    /**
     * Creates a constant, non-modifiable ColumnIterator for column <code>col</code>
     *
     * @param col is the desired column
     * @param row0 is the initial row, inclusive
     * @param row1 is the final row, exclusive
     * @return an Array obtained from row A( [row0:row1) , col )
     * @throws IllegalArgumentException when indices are out of range
     */
    public ConstColumnIterator constColumnIterator(final int col, final int row0, final int row1) {
        return new ConstColumnIterator(col, row0, row1);
    }







    //
    //	Miscellaneous
    //
    //	method       this    right    result
    //	------------ ------- -------- ------
    //	transpose    Matrix           Matrix
    //  diagonal     Matrix           Array
    //  determinant  Matrix           double
    //	inverse      Matrix           Matrix
    //  solve        Matrix           Matrix
    //  swap         Matrix  Matrix   this
    //  range        Matrix           Matrix
    //  rangeRow     Matrix           Array
    //  rangeCol     Matrix           Array
    //

    /**
     * Returns the transpose of <code>this</code> Matrix
     *
     * @return a new instance which contains the result of this operation
     */
    public Matrix transpose() {
        final Matrix result = new Matrix(this.cols, this.rows);
        for (int row=0; row<this.rows; row++) {
            int raddr = this.addr(row, 0);
            int caddr = result.addr(0, row);
            for (int col=0; col<this.cols; col++) {
                result.data[caddr] = this.data[raddr];
                caddr += result.cols;
                raddr++;
            }
        }
        return result;
    }

    /**
     * Returns a diagonal from <code>this</code> Matrix, if it is square
     *
     * @return a new instance which contains the result of this operation
     */
    public Array diagonal() {
        QL.require(this.rows == this.cols ,  MATRIX_MUST_BE_SQUARE); // QA:[RG]::verified
        final Array result = new Array(this.cols);
        int addr = 0;
        for (int i = 0; i < this.cols; i++) {
            result.data[i] = this.data[addr];
            addr += this.cols + 1;
        }
        return result;
    }

    /**
     * Determinant
     *
     * @return determinant of matrix
     * @exception IllegalArgumentException Matrix must be square
     */
    public double det() {
        return new LUDecomposition(this).det();
    }

    /**
     * Returns an inverse Matrix from <code>this</code> Matrix
     *
     * @return a new instance which contains the result of this operation
     */
    public Matrix inverse() {
        return solve(new Identity(rows));
    }

    /**
     * Solve A*X = B
     *
     * @param m right hand side
     * @return solution if A is square, least squares solution otherwise
     */
    public Matrix solve (final Matrix m) {
       return (rows == cols ? (new LUDecomposition(this)).solve(m) : (new QRDecomposition(this)).solve(m));
    }


    /**
     * Swaps contents of <code>this</code> Matrix by <code>another</code> Matrix
     *
     * @param another
     * @return this
     */
    public Matrix swap(final Matrix another) {
        super.swap(another);
        return this;
    }


    /**
     * Creates a sub-matrix made of elements of <code>this</code> Matrix, specified by a certain range of elements.
     *
     * @param row0 Initial row index, inclusive
     * @param row1 Final row index, exclusive
     * @param col0 Initial column index, inclusive
     * @param col1 Final column index, exclusive
     * @return A( [row0:row1) , [col0:col1) )
     * @exception IllegalArgumentException when indices are out of range
     */
    public Matrix range(final int row0, final int row1, final int col0, final int col1) {
        QL.require(row0 >= 0 && row1 > row0 && row1 <= this.rows, INVALID_ARGUMENTS); // QA:[RG]::verified
        QL.require(col0 >= 0 && col1 > col0 && col1 <= this.cols, INVALID_ARGUMENTS); // QA:[RG]::verified

        final Matrix result = new Matrix(row1-row0, col1-col0);
        if (col1-col0 == this.cols) {
            if (row1-row0 == this.rows)
                System.arraycopy(this.data, 0, result.data, 0, this.size);
            else
                System.arraycopy(this.data, this.addr(row0, 0), result.data, 0, result.size);
        } else {
            final int ncols = col1-col0;
            int addr = 0;
            for (int i = row0; i <= row1; i++) {
                System.arraycopy(this.data, addr(i, col0), result.data, addr, ncols);
                addr += ncols;
            }
        }
        return result;
    }

    /**
     * Creates a sub-matrix made of elements of <code>this</code> Matrix, specified by a certain range of elements.
     *
     * @param row0 Initial row index, inclusive
     * @param row1 Final row index, exclusive
     * @param c Array of column indices.
     * @return A( [row0:row1) , c(:) )
     * @exception IllegalArgumentException when indices are out of range
     */

    public Matrix range(final int row0, final int row1, final int[] c) {
        QL.require(row0 >= 0 && row1 > row0 && row1 <= this.rows, INVALID_ARGUMENTS); // QA:[RG]::verified
        for (final int col : c) {
            QL.require(col>=0 && col<this.cols, INVALID_ARGUMENTS); // QA:[RG]::verified
        }

        final Matrix result = new Matrix(row1-row0, c.length);
        for (int i = row0; i <= row1; i++) {
            for (int j = 0; j < c.length; j++) {
                result.set( i-row0, j, this.get(i, c[j]) );
            }
        }
        return result;
    }

    /**
     * Creates a sub-matrix made of elements of <code>this</code> Matrix, specified by a certain range of elements.
     *
     * @param r Array of row indices.
     * @param c Array of column indices.
     * @return A(r(:),c(:))
     * @exception IllegalArgumentException when indices are out of range
     */
    public Matrix range(final int[] r, final int[] c) {
        for (final int row : r) {
            QL.require(row>=0 && row<this.rows, INVALID_ARGUMENTS); // QA:[RG]::verified
        }
        for (final int col : c) {
            QL.require(col>=0 && col<this.cols, INVALID_ARGUMENTS); // QA:[RG]::verified
        }

        final Matrix result = new Matrix(r.length, c.length);
        for (int i=0; i<r.length; i++) {
            final int row = r[i];
            for (int j=0; j<c.length; j++) {
                final int col = c[j];
                result.set(i, j, this.get(row, col));
            }
        }
        return result;
    }

    /**
     * Creates a sub-matrix made of elements of <code>this</code> Matrix, specified by a certain range of elements.
     *
     * @param r Array of row indices.
     * @param col0 Initial column index, inclusive
     * @param col1 Final column index, exclusive
     * @return A(r(:), [col0:col1) )
     * @exception IllegalArgumentException when indices are out of range
     */
    public Matrix range(final int[] r, final int col0, final int col1) {
        for (final int row : r) {
            QL.require(row>=0 && row<this.rows, INVALID_ARGUMENTS); // QA:[RG]::verified
        }
        QL.require(col0 >= 0 && col1 > col0 && col1 <= this.cols, INVALID_ARGUMENTS); // QA:[RG]::verified

        final Matrix result = new Matrix(r.length, col1-col0);
        final int ncols = col1-col0;

        int addr = 0;
        for (final int row : r) {
            System.arraycopy(this.data, addr(row, col0), result.data, addr, ncols);
            addr += ncols;
        }
        return result;
    }

    /**
     * Creates an Array made of elements of <code>this</code> Matrix, specified by a certain range of elements in a row.
     *
     * @param row is a row index
     * @param offset0 Initial column index, inclusive
     * @return A(row, [col0:] )
     * @exception IllegalArgumentException when indices are out of range
     */
    public Array rangeRow(final int row, final int col0) {
        return rangeRow(row, col0, cols);
    }

    /**
     * Creates an Array made of elements of <code>this</code> Matrix, specified by a certain range of elements in a row.
     *
     * @param row row index
     * @param col0 Initial column index, inclusive
     * @param col1 Final column index, exclusive
     * @return A(row, [col0:col1) )
     * @exception IllegalArgumentException when indices are out of range
     */
    public Array rangeRow(final int row, final int col0, final int col1) {
        QL.require(row >= 0 && row < this.rows, INVALID_ARGUMENTS); // QA:[RG]::verified
        QL.require(col0 >= 0 && col1 > col0 && col1 <= this.cols, INVALID_ARGUMENTS); // QA:[RG]::verified

        final Array result = new Array(col1-col0);
        final int ncols = col1-col0;
        System.arraycopy(this.data, addr(row, col0), result.data, 0, ncols);
        return result;
    }

    /**
     * Creates an Array made of elements of <code>this</code> Matrix, specified by a certain range of elements in a row.
     *
     * @param row row index
     * @param row0 Initial column index, inclusive
     * @return A(row, [col0:] )
     * @exception IllegalArgumentException when indices are out of range
     */
    public Array rangeCol(final int col, final int row0) {
        return rangeCol(col, row0, rows);
    }

    /**
     * Creates an Array made of elements of <code>this</code> Matrix, specified by a certain range of elements in a row.
     *
     * @param row row index
     * @param row0 Initial column index, inclusive
     * @param row1 Final column index, exclusive
     * @return A(row, col0:col1)
     * @exception IllegalArgumentException when indices are out of range
     */
    public Array rangeCol(final int col, final int row0, final int row1) {
        QL.require(col >= 0 && col < this.cols, INVALID_ARGUMENTS); // QA:[RG]::verified
        QL.require(row0 >= 0 && row1 > row0 && row1 <= this.rows, INVALID_ARGUMENTS); // QA:[RG]::verified

        final Array result = new Array(row1-row0);
        final int nrows = row1-row0;

        int addr = addr(col,row0);
        for (int row = 0; row < nrows; row++) {
            result.data[row] = this.data[addr];
            addr += cols;
        }
        return result;
    }


    //
    // public inner classes
    //

    /**
     * This class implements a {@link ListIterator} over elements of a row of a {@link Matrix}
     * <p>
     * This class also implements {@link DoubleListIterator} which has the property of avoiding boxing/unboxing.
     *
     * @note Operations {@link #set(double)}, {@link #set(Double)} and {@link #remove()} throw {@link UnsupportedOperationException}
     *
     * @author Richard Gomes
     */
    public class RowIterator implements ListIterator<Double>, DoubleListIterator {

        private final int row;
        private final int col0;
        private final int col1;

        private int cursor;

        /**
         * Creates a RowIterator for the entire row <code>row</code>
         *
         * @param row is the desired row
         * @return an Array obtained from row A( row , [:] )
         * @throws IllegalArgumentException when indices are out of range
         */
        public RowIterator(final int row) {
            this(row, 0, cols);
        }

        /**
         * Creates a RowIterator for row <code>row</code>
         *
         * @param row is the desired row
         * @param col0 is the initial column, inclusive
         * @return an Array obtained from row A( row , [col0:] )
         * @throws IllegalArgumentException when indices are out of range
         */
        public RowIterator(final int row, final int col0) {
            this(row, col0, cols);
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
        public RowIterator(final int row, final int col0, final int col1) {
            QL.require(row>=0 && row<rows && col0 >=0 && col1>=col0 && col1 <= cols, INVALID_ARGUMENTS); // QA:[RG]::verified
            this.row = row;
            this.col0 = col0;
            this.col1 = col1;
            this.cursor = col0;
        }


        //
        // implements ListIterator
        //

        @Override
        public void add(final Double e) {
            add(e.doubleValue());
        }

        @Override
        public boolean hasNext() {
            return cursor < col1;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > col0;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor-1;
        }

        @Override
        public Double next() {
            return nextDouble();
        }

        @Override
        public Double previous() {
            return previousDouble();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final Double e) {
            set(e.doubleValue());
        }


        //
        // implements DoubleListIterator
        //

        @Override
        public void add(final double e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forward() {
            if (cursor < col1) {
                cursor++;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void backward() {
            if (cursor >= col0) {
                --cursor;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double nextDouble() {
            if (cursor < col1) {
                return data[addr(row,cursor++)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double previousDouble() {
            if (cursor > col0) {
                return data[addr(row,--cursor)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void set(final double e) {
            if (cursor >=col0 && cursor < col1) {
                data[addr(row,cursor)] = e;
            } else {
                throw new NoSuchElementException();
            }
        }

    }


    /**
     * This class implements a {@link ListIterator} over elements of a row of a {@link Matrix}
     * <p>
     * This class also implements {@link DoubleListIterator} which has the property of avoiding boxing/unboxing.
     *
     * @note Operations {@link #set(double)}, {@link #set(Double)} and {@link #remove()} throw {@link UnsupportedOperationException}
     *
     * @author Richard Gomes
     */
    public class ConstRowIterator extends RowIterator {

        /**
         * Creates a constant, non-modifiable RowIterator for row <code>row</code>
         *
         * @param row is the desired row
         * @return an Array obtained from row A( row , [:] )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ConstRowIterator(final int row) {
            super(row);
        }

        /**
         * Creates a constant, non-modifiable RowIterator for row <code>row</code>
         *
         * @param row is the desired row
         * @param col0 is the initial column, inclusive
         * @return an Array obtained from row A( row , [col0:) )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ConstRowIterator(final int row, final int col0) {
            super(row, col0);
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
        public ConstRowIterator(final int row, final int col0, final int col1) {
            super(row, col0, col1);
        }


        @Override
        public void set(final double e) {
            throw new UnsupportedOperationException();
        }

    }


    /**
     * This class implements a {@link ListIterator} over elements of a column of a {@link Matrix}
     * <p>
     * This class implements {@link ListIterator} and {@link DoubleListIterator} which has the property of avoiding boxing/unboxing.
     *
     * @note Operations {@link #set(double)}, {@link #set(Double)} and {@link #remove()} throw {@link UnsupportedOperationException}
     *
     * @author Richard Gomes
     */
    public class ColumnIterator implements ListIterator<Double>, DoubleListIterator {

        private final int row0;
        private final int row1;
        private final int col;

        private int cursor;

        /**
         * Creates a ColumnIterator for the entire column <code>col</code>
         *
         * @param col is the desired column
         * @return an Array obtained from row A( [:] , col )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ColumnIterator(final int col) {
            this(col, 0, cols);
        }

        /**
         * Creates a ColumnIterator for column <code>col</code>
         *
         * @param col is the desired column
         * @param row0 is the initial row, inclusive
         * @return an Array obtained from row A( [row0:] , col )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ColumnIterator(final int col, final int row0) {
            this(col, row0, cols);
        }

        /**
         * Creates a ColumnIterator for column <code>col</code>
         *
         * @param col is the desired column
         * @param row0 is the initial row, inclusive
         * @param row1 is the final row, exclusive
         * @return an Array obtained from row A( [row0:row1) , col )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ColumnIterator(final int col, final int row0, final int row1) {
            QL.require(col>=0 && col<cols && row0 >=0 && row1>=row0 && row1 <= rows, INVALID_ARGUMENTS); // QA:[RG]::verified
            this.col = col;
            this.row0 = row0;
            this.row1 = row1;
            this.cursor = row0;
        }


        //
        // implements ListIterator
        //

        @Override
        public void add(final Double e) {
            add(e.doubleValue());
        }

        @Override
        public boolean hasNext() {
            return cursor < row1;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > row0;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor-1;
        }

        @Override
        public Double next() {
            return nextDouble();
        }

        @Override
        public Double previous() {
            return previousDouble();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final Double e) {
            set(e.doubleValue());
        }


        //
        // implements DoubleListIterator
        //

        @Override
        public void add(final double e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forward() {
            if (cursor < row1-1) {
                cursor++;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void backward() {
            if (cursor > row0) {
                --cursor;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double nextDouble() {
            if (cursor < row1) {
                return data[addr(cursor++,col)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double previousDouble() {
            if (cursor > row0) {
                return data[addr(--cursor,col)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void set(final double e) {
            if (cursor >=row0 && cursor < row1) {
                data[addr(cursor, col)] = e;
            } else {
                throw new NoSuchElementException();
            }
        }

    }


    /**
     * This class implements a {@link ListIterator} over elements of a row of a {@link Matrix}
     * <p>
     * This class also implements {@link DoubleListIterator} which has the property of avoiding boxing/unboxing.
     *
     * @note Operations {@link #set(double)}, {@link #set(Double)} and {@link #remove()} throw {@link UnsupportedOperationException}
     *
     * @author Richard Gomes
     */
    public class ConstColumnIterator extends ColumnIterator {

        /**
         * Creates a constant, non-modifiable RowIterator for an entire column <code>col</code>
         *
         * @param col is the desired col
         */
        public ConstColumnIterator(final int col) {
            super(col);
        }

        /**
         * Creates a constant, non-modifiable ColumnIterator for column <code>col</code>
         *
         * @param col is the desired column
         * @param row0 is the initial row, inclusive
         * @return an Array obtained from row A( [row0:row1) , col )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ConstColumnIterator(final int col, final int row0) {
            super(col, row0);
        }

        /**
         * Creates a constant, non-modifiable ColumnIterator for column <code>col</code>
         *
         * @param col is the desired column
         * @param row0 is the initial row, inclusive
         * @param row1 is the final row, exclusive
         * @return an Array obtained from row A( [row0:row1) , col )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ConstColumnIterator(final int col, final int row0, final int row1) {
            super(col, row0, row1);
        }


        @Override
        public void set(final double e) {
            throw new UnsupportedOperationException();
        }

    }




    //
    // protected methods
    //

    /**
     * This method returns the address of a given cell identified by <i>(row, col)</i>
     * <p>
     * This method is used internally and is provided for performance reasons.
     */
    protected int addr(final int row, final int col) {
        return row*this.cols + col;
    }


    //
    // static methods
    //

    // TODO: OSGi :: remove statics


    /**
     * sqrt(a^2 + b^2) without under/overflow.
     */
    public static double hypot(final double a, final double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        } else {
            r = 0.0;
        }
        return r;
    }


    public static void copy(final DoubleListIterator src, final DoubleListIterator dst) {
        while (src.hasNext()) {
            dst.set(src.nextDouble());
            dst.forward();
        }
    }

}


