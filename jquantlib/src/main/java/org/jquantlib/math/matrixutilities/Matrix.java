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

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.lang.iterators.BulkStorage;

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
public class Matrix extends Cells implements BulkStorage<Matrix> {

    //
    // public constructors
    //

    /**
     * Default constructor
     * <p>
     * Builds an empty Matrix
     */
    public Matrix() {
        this(Style.JAVA);
    }

    /**
     * Builds an empty Matrix
     *
     * @param style allows transparent access to elements by FORTRAN based algorithms.
     */
    public Matrix(final Style style) {
        super(1, 1, style);
    }

    /**
     * Builds a Matrix of <code>rows</code> by <code>cols</code>
     *
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @throws IllegalArgumentException if parameters are less than zero
     */
    public Matrix(final int rows, final int cols) {
        this(rows, cols, Style.JAVA);
    }

    /**
     * Builds a Matrix of <code>rows</code> by <code>cols</code>
     *
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @param style allows transparent access to elements by FORTRAN based algorithms.
     * @throws IllegalArgumentException if parameters are less than zero
     *
     * @see Style
     */
    public Matrix(final int rows, final int cols, final Style style) {
        super(rows, cols, style);
    }


    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data
     */
    public Matrix(final double[][] data) {
        this(data, Style.JAVA);
    }

    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data is a bi-dimensional array, always organized as a Java index access style, zero-based double[][].
     * @param style allows transparent access to elements by FORTRAN based algorithms.
     *
     * @see Style
     */
    public Matrix(final double[][] data, final Style style) {
        super(data.length, data[0].length, style);

        for (int i=0; i<rows; i++) {
            System.arraycopy(data[i], 0, this.data, addrJ(i, 0), cols);
        }
    }

    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data
     */
    public Matrix(final Matrix m) {
        super(m.rows, m.cols, m.style);
        System.arraycopy(m.data, 0, this.data, 0, size);
    }

    /**
     * Creates a Matrix given a double[][] array
     *
     * @param m is an existing matrix
     * @param style allows transparent access to elements by FORTRAN based algorithms.
     */
    public Matrix(final Matrix m, final Style style) {
        super(m.rows, m.cols, style);
        System.arraycopy(m.data, 0, this.data, 0, size);
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
        if (rows != another.rows || cols != another.cols) return false;
        return Arrays.equals(data, another.data);
    }


    //
    // public methods
    //


    public Object toArray() {
        return toArray(Style.JAVA);
    }

    public Object toArray(final Style style) {
        final double buffer[][] = new double[rows+style.base][cols+style.base];
        return toArray(buffer, style);
    }

    public double[][] toArray(final double[][] buffer) {
        return toArray(buffer, Style.JAVA);
    }

    public double[][] toArray(final double[][] buffer, final Style style) {
        QL.require(rows+style.base == buffer.length && cols+style.base == buffer[0].length, WRONG_BUFFER_LENGTH); // QA:[RG]::verified
        int addr = 0;
        for (int row=0; row<rows; row++) {
            System.arraycopy(data, addr, buffer[row+style.base], style.base, cols);
            addr += cols;
        }
        return buffer;
    }

    /**
     * Overwrites contents of a certain row
     *
     * @param row is the requested row to be overwritten
     * @param array contains the elements to be copied
     */
    public void fillRow(final int row, final Array array) {
        QL.require(cols == array.size ,  ARRAY_IS_INCOMPATIBLE);
        System.arraycopy(array.data, 0, data, addr(row, style.base), cols);
    }

    /**
     * Overwrites contents of a certain column
     *
     * @param col is the requested column to be overwritten
     * @param array contains the elements to be copied
     */
    public void fillCol(final int col, final Array array) {
        QL.require(rows == array.size ,  ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        if (cols == 1)
            System.arraycopy(array.data, 0, data, 0, size);
        else {
            int addr = addr(style.base, col);
            for (int row = style.base; row < rows+style.base; row++) {
                data[addr] = array.data[array.addr(row)];
                addr += cols;
            }
        }
    }

    /**
     * Retrieves an element of <code>this</code> Matrix which identified by <i>(row, col)</i>
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
        data[addr(row, col)] = value;
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
        QL.require(rows == another.rows && cols == another.cols ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int row=0; row<rows; row++) {
            int addrJ = addrJ(row, 0);
            for (int col=0; col<cols; col++) {
                data[addrJ] += another.data[addrJ];
                addrJ++;
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
        QL.require(rows == another.rows && cols == another.cols ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        for (int row=0; row<rows; row++) {
            int addrJ = addrJ(row, 0);
            for (int col=0; col<cols; col++) {
                data[addrJ] -= another.data[addrJ];
                addrJ++;
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
            final int raddrJ = addrJ(row, 0);
            for (int col = 0; col < cols; col++) {
                final int addrJ = raddrJ + col;
                data[addrJ] *= scalar;
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
            final int raddrJ = addrJ(row, 0);
            for (int col = 0; col < cols; col++) {
                final int addrJ = raddrJ + col;
                data[addrJ] /= scalar;
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
        QL.require(rows == another.rows && cols == another.cols ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(rows, cols, style);
        for (int row=0; row<rows; row++) {
            int addrJ = addrJ(row, 0);
            for (int col=0; col<cols; col++) {
                result.data[addrJ] = data[addrJ] + another.data[addrJ];
                addrJ++;
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
        QL.require(rows == another.rows && cols == another.cols ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(rows, cols, style);
        for (int row=0; row<rows; row++) {
            int addrJ = addrJ(row, 0);
            for (int col=0; col<cols; col++) {
                result.data[addrJ] = data[addrJ] - another.data[addrJ];
                addrJ++;
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
        return mulAssign(-1);
    }


    /**
     * Returns the result of a multiplication of <code>this</code> Matrix by a <code>scalar</code>
     *
     * @param scalar
     * @return a new instance
     */
    public Matrix mul(final double scalar) {
        final Matrix result = new Matrix(rows, cols, style);
        for (int row=0; row<rows; row++) {
            int addrJ = addrJ(row, 0);
            for (int col=0; col<cols; col++) {
                result.data[addrJ] = data[addrJ] * scalar;
                addrJ++;
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
        final Matrix result = new Matrix(rows, cols, style);
        for (int row=0; row<rows; row++) {
            int addrJ = addrJ(row, 0);
            for (int col=0; col<cols; col++) {
                result.data[addrJ] = data[addrJ] / scalar;
                addrJ++;
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
        QL.require(cols == array.size ,  ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(cols, style);
        for (int col=0; col<cols; col++) {
            int addrJ = addrJ(0, col);
            double sum = 0.0;
            for (int row=0; row<rows; row++) {
                sum  += data[addrJ] * array.data[col];
                addrJ += cols;
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
        QL.require(cols == another.rows ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(rows, another.cols, style);
        for (int col = 0; col < another.cols; col++) {
            final int caddrJ = another.addrJ(0, col);
            for (int row = 0; row < rows; row++) {
                final int raddrJ = addrJ(row, 0);
                int addrJ = caddrJ;
                double sum = 0.0;
                for (int i = 0; i < cols; i++) {
                    sum += data[raddrJ + i] * another.data[addrJ];
                    addrJ += another.cols;
                }
                result.data[result.addrJ(row, col)] = sum;
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
     * @param moreGreeks is a rectangular Matrix
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
        final Matrix result = new Matrix(cols, rows, style);
        for (int row=0; row<rows; row++) {
            int raddrJ = addrJ(row, 0);
            int caddrJ = result.addrJ(0, row);
            for (int col=0; col<cols; col++) {
                result.data[caddrJ] = data[raddrJ];
                caddrJ += result.cols;
                raddrJ++;
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
        QL.require(rows == cols ,  MATRIX_MUST_BE_SQUARE); // QA:[RG]::verified
        final Array result = new Array(cols, style);
        int addrJ = 0;
        for (int i = 0; i < cols; i++) {
            result.data[i] = data[addrJ];
            addrJ += cols + 1;
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
        QL.require(row0 >= style.base && row1 > row0 && row1 <= rows+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        QL.require(col0 >= style.base && col1 > col0 && col1 <= cols+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified

        final Matrix result = new Matrix(row1-row0, col1-col0, style);
        if (col1-col0 == cols) {
            if (row1-row0 == rows)
                System.arraycopy(data, style.base, result.data, style.base, size);
            else
                System.arraycopy(data, addr(row0, style.base), result.data, style.base, result.size);
        } else {
            final int ncols = col1-col0;
            int addr = style.base;
            for (int i = row0; i <= row1; i++) {
                System.arraycopy(data, addr(i, col0), result.data, addr, ncols);
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
     * This parameter can a regular int[] array as usual or it can be a one-based int[] array composed by one-based indexes
     * if <code>this</code> Matrix has a FORTRAN style indexing.
     * For example, suppose we wish to pass an array made of indexes [1, 2, 0]. We can do it using the usual Java style as:
     * <pre>
     *     int [] c = new int[] { 1, 2, 0 };; // Java style, as usual. No surprises
     * </pre>
     * whilst using FORTRAN style we need to add one leading cell (which is discarded) and we need to adjust array elements:
     * <pre>
     *     int [] c = new int[] { 0, 2, 3, 1 };; // FORTRAN style : array elements are one-based.
     * <pre>
     * @return A( [row0:row1) , c(:) ), preserving the {@link Style} of <code>this</code> Matrix
     * @exception IllegalArgumentException when indices are out of range
     */
    public Matrix range(final int row0, final int row1, final int[] c) {
        QL.require(row0 >= style.base && row1 > row0 && row1 <= rows+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        for (final int col : c) {
            QL.require(col>=style.base && col<cols+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        }

        final Matrix result = new Matrix(row1-row0, c.length, style);
        for (int i = row0; i <= row1; i++) {
            for (int j = style.base; j < c.length+style.base; j++) {
                result.set(i-row0, j, get(i, c[j]));
            }
        }
        return result;
    }

    /**
     * Creates a sub-matrix made of elements of <code>this</code> Matrix, specified by a certain range of elements.
     *
     * @param r Array of row indices.
     * This parameter can a regular int[] array as usual or it can be a one-based int[] array composed by one-based indexes
     * if <code>this</code> Matrix has a FORTRAN style indexing.
     * For example, suppose we wish to pass an array made of indexes [1, 2, 0]. We can do it using the usual Java style as:
     * <pre>
     *     int [] r = new int[] { 1, 2, 0 };; // Java style, as usual. No surprises
     * </pre>
     * whilst using FORTRAN style we need to add one leading cell (which is discarded) and we need to adjust array elements:
     * <pre>
     *     int [] r = new int[] { 0, 2, 3, 1 };; // FORTRAN style : array elements are one-based.
     * <pre>
     * @param c Array of column indices.
     * This parameter can a regular int[] array as usual or it can be a one-based int[] array composed by one-based indexes
     * if <code>this</code> Matrix has a FORTRAN style indexing.
     * For example, suppose we wish to pass an array made of indexes [1, 2, 0]. We can do it using the usual Java style as:
     * <pre>
     *     int [] c = new int[] { 1, 2, 0 };; // Java style, as usual. No surprises
     * </pre>
     * whilst using FORTRAN style we need to add one leading cell (which is discarded) and we need to adjust array elements:
     * <pre>
     *     int [] c = new int[] { 0, 2, 3, 1 };; // FORTRAN style : array elements are one-based.
     * <pre>
     * @return A(r(:),c(:)), preserving the {@link Style} of <code>this</code> Matrix
     * @exception IllegalArgumentException when indices are out of range
     */
    public Matrix range(final int[] r, final int[] c) {
        for (final int row : r) {
            QL.require(row>=style.base && row<rows+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        }
        for (final int col : c) {
            QL.require(col>=style.base && col<cols+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        }

        final Matrix result = new Matrix(r.length-style.base, c.length-style.base, style);
        for (int i=style.base; i<r.length+style.base; i++) {
            final int row = r[i];
            for (int j=style.base; j<c.length+style.base; j++) {
                final int col = c[j];
                result.set(i, j, get(row, col));
            }
        }
        return result;
    }

    /**
     * Creates a sub-matrix made of elements of <code>this</code> Matrix, specified by a certain range of elements.
     *
     * @param r Array of row indices.
     * This parameter can a regular int[] array as usual or it can be a one-based int[] array composed by one-based indexes
     * if <code>this</code> Matrix has a FORTRAN style indexing.
     * For example, suppose we wish to pass an array made of indexes [1, 2, 0]. We can do it using the usual Java style as:
     * <pre>
     *     int [] r = new int[] { 1, 2, 0 };; // Java style, as usual. No surprises
     * </pre>
     * whilst using FORTRAN style we need to add one leading cell (which is discarded) and we need to adjust array elements:
     * <pre>
     *     int [] r = new int[] { 0, 2, 3, 1 };; // FORTRAN style : array elements are one-based.
     * <pre>
     * @param col0 Initial column index, inclusive
     * @param col1 Final column index, exclusive
     * @return A(r(:), [col0:col1) ), preserving the {@link Style} of <code>this</code> Matrix
     * @exception IllegalArgumentException when indices are out of range
     */
    public Matrix range(final int[] r, final int col0, final int col1) {
        for (final int row : r) {
            QL.require(row>=style.base && row<rows+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        }
        QL.require(col0 >= style.base && col1 > col0 && col1 <= cols+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified

        final Matrix result = new Matrix(r.length, col1-col0, style);
        final int ncols = col1-col0;

        int addr = style.base;
        for (final int row : r) {
            System.arraycopy(data, addr(row, col0), result.data, addr, ncols);
            addr += ncols;
        }
        return result;
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
    public ConstRowIterator constRowIterator(final int row, final int col0) {
        return new ConstRowIterator(row, col0);
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
    // implements BulkStorage
    //

    @Override
    public Matrix fill(final double scalar) {
        return (Matrix) super.bulkStorage.fill(scalar);
    }

    @Override
    public Matrix fill(final Matrix another) {
        return (Matrix) super.bulkStorage.fill(another);
    }

    @Override
    public Matrix sort() {
        return (Matrix) super.bulkStorage.sort();
    }

    @Override
    public Matrix swap(final Matrix another) {
        return (Matrix) super.bulkStorage.swap(another);
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

}


