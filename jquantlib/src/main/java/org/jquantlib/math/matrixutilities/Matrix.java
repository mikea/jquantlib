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
import java.util.EnumSet;

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.math.matrixutilities.internal.Address;
import org.jquantlib.math.matrixutilities.internal.DirectArrayColAddress;
import org.jquantlib.math.matrixutilities.internal.DirectArrayRowAddress;
import org.jquantlib.math.matrixutilities.internal.DirectMatrixAddress;
import org.jquantlib.math.matrixutilities.internal.MappedMatrixAddress;
import org.jquantlib.math.matrixutilities.internal.Address.MatrixAddress.MatrixOffset;

/**
 * Bidimensional matrix operations
 * <p>
 * Performance of multidimensional arrays is a big concern in Java. This is because multidimensional arrays are stored as arrays of
 * arrays, spanning this concept to as many depths as necessary. In C++, a multidimensional array is stored internally as a
 * unidimensional array where depths are stacked together one after another. A very simple calculation is needed in order to map
 * multiple dimensional indexes to an unidimensional index.
 * <p>
 * This class provides a C/C++ like approach of internal unidimensional array backing a conceptual bidimensional matrix. This
 * mapping is provided by method <code>op(int row, int col)</code> which is responsible for returning the physical address of
 * the desired tuple <row,col>, given a certain access method.
 * <p>
 * The mentioned access method is provided by a concrete implementation of {@link Address} which is passed to constructors.
 * Doing so, it is possible to access the same underlying unidimensional data storage in various different ways, which allows us
 * obtain another Matrix (see method {@link Matrix#range(int, int, int, int) and alike}) from an existing Matrix without any
 * need of copying the underlying data. Certain operations benefit a lot from such approach, like {@link Matrix#transpose()} which
 * presents constant execution time.
 * <p>
 * The price we have to pay for such flexibility and benefits is that an access method is necessary, which means that a the bytecode
 * may potentially need a dereference to a class which contain the concrete implementation of method <code>op(int row, int col)</code>.
 * In order to keep this cost at minimum, implementation of access method keep indexes at hand whenever possible, in order to
 * being able to calculate the actual unidimensional index as fast as possible. This additional dereference impacts performance
 * more or less in the same ways dereference impacts performance when a bidimensional array (<code>double[][]</code>) is employed.
 * Contrary to the bidimensional implementation, our implementation can potentially benefit from bytecode inlining, which means
 * that calculation of the unidimensional index can be performed potentially faster than calculation of address location when a
 * bidimensional array (<code>double[][]</code>) is used as underlying storage.
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
 * <b>Vectorial products</b>
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
 * @note This class is not thread-safe
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q1_TRANSLATION, version = Version.V097, reviewers = { "Richard Gomes" })
// TODO: better documentation
public class Matrix extends Cells<Address.MatrixAddress> implements Cloneable {

    //
    // public constructors
    //

    /**
     * Default constructor
     * <p>
     * Builds a Matrix with dimensions 1x1
     */
    public Matrix() {
        super(1, 1,
              new DirectMatrixAddress(0, 0, null, 0, 0, EnumSet.of(Address.Flags.CONTIGUOUS), 1, 1));
    }

    /**
     * Builds a Matrix of <code>rows</code> by <code>cols</code>
     *
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @throws IllegalArgumentException if parameters are less than zero
     */
    public Matrix(final int rows, final int cols) {
        super(rows, cols,
              new DirectMatrixAddress(0, rows-1, null, 0, cols-1, EnumSet.of(Address.Flags.CONTIGUOUS), rows, cols));
    }


    /**
     * Creates a Matrix given a double[][] array
     *
     * @param data
     */
    public Matrix(final double[][] data) {
        super(data.length, data[0].length,
              new DirectMatrixAddress(0, data.length - 1, null, 0, data[0].length - 1, EnumSet.of(Address.Flags.CONTIGUOUS), data.length, data[0].length));
        for (int row=0; row<data.length; row++) {
            System.arraycopy(data[row], 0, this.data, row*this.cols, this.cols);
        }
    }

    /**
     * copy constructor
     *
     * @param data
     */
    public Matrix(final Matrix m) {
        super(m.rows(), m.cols(), copyData(m), m.addr.clone());
    }


    private static final double[] copyData(final Matrix m) {
        final Address addr = m.addr;
        final int size = m.rows()*m.cols();
        final double[] data = new double[size];
        if (addr.contiguous()) {
            System.arraycopy(m.data, addr.base(), data, 0, size);
        } else {
            final MatrixOffset offset = m.addr.offset();
            final int cols = m.cols();
            for (int row=0; row<m.rows(); row++) {
                System.arraycopy(m.data, offset.op(), data, row*cols, cols);
                offset.nextRow();
            }
        }
        return data;
    }



    //
    // protected constructors
    //

    protected Matrix(
            final int rows,
            final int cols,
            final double[] data,
            final Address.MatrixAddress addr) {
        super(rows, cols, data, addr);
    }


    //
    // public methods
    //


    public Object toArray() {
        final double buffer[][] = new double[rows()][cols()];
        return toArray(buffer);
    }

    public double[][] toArray(final double[][] buffer) {
        QL.require(rows() == buffer.length && cols() == buffer[0].length, WRONG_BUFFER_LENGTH); // QA:[RG]::verified
        if (addr.contiguous()) {
            int addr = 0;
            for (int row=0; row<rows(); row++) {
                System.arraycopy(data, addr, buffer[row], 0, cols());
                addr += cols();
            }
        } else {
            final Address.MatrixAddress.MatrixOffset src = this.addr.offset();
            for (int row=0; row<rows(); row++) {
                src.setRow(row);
                for (int col=0; col < cols(); col++) {
                    buffer[row][col] = this.data[src.op()];
                    src.nextCol();
                }
            }
        }
        return buffer;
    }

    /**
     * Retrieves an element of <code>this</code> Matrix which identified by <i>(row, col)</i>
     *
     * @param row coordinate
     * @param col coordinate
     * @return the contents of a given cell
     */
    public double get(final int row, final int col) {
        return data[addr.op(row, col)];
    }

    /**
     * Stores a value into an element of <code>this</code> Matrix which is identified by <i>(row, col)</i>
     *
     * @param row coordinate
     * @param col coordinate
     */
    public void set(final int row, final int col, final double value) {
        data[addr.op(row, col)] = value;
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
        QL.require(rows() == another.rows() && cols() == another.cols() ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        if (this.addr.contiguous() && another.addr.contiguous()) {
            for (int i=0; i<size(); i++) {
                this.data[i] += another.data[i];
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
            final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
            for (int row=0; row<rows(); row++) {
                toff.setRow(row);
                aoff.setRow(row);
                for (int col=0; col<cols(); col++) {
                    this.data[toff.op()] += another.data[aoff.op()];
                    addr++;
                    toff.nextCol();
                    aoff.nextCol();
                }
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
        QL.require(rows() == another.rows() && cols() == another.cols() ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        if (this.addr.contiguous() && another.addr.contiguous()) {
            for (int i=0; i<size(); i++) {
                this.data[i] -= another.data[i];
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
            final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
            for (int row=0; row<rows(); row++) {
                toff.setRow(row);
                aoff.setRow(row);
                for (int col=0; col<cols(); col++) {
                    this.data[toff.op()] -= another.data[aoff.op()];
                    addr++;
                    toff.nextCol();
                    aoff.nextCol();
                }
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
        if (addr.contiguous()) {
            for (int addr=0; addr<size(); addr++) {
                data[addr] *= scalar;
            }
        } else {
            final Address.MatrixAddress.MatrixOffset dst = this.addr.offset();
            for (int row = 0; row < rows(); row++) {
                dst.setRow(row);
                for (int col = 0; col < cols(); col++) {
                    data[dst.op()] *= scalar;
                    dst.nextCol();
                }
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
        if (addr.contiguous()) {
            for (int addr=0; addr<size(); addr++) {
                data[addr] /= scalar;
            }
        } else {
            final Address.MatrixAddress.MatrixOffset dst = this.addr.offset();
            for (int row = 0; row < rows(); row++) {
                dst.setRow(row);
                for (int col = 0; col < cols(); col++) {
                    data[dst.op()] /= scalar;
                    dst.nextCol();
                }
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
        QL.require(rows() == another.rows() && cols() == another.cols() ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(rows(), cols());
        if (this.addr.contiguous() && another.addr.contiguous()) {
            for (int i=0; i<size(); i++) {
                result.data[i] = this.data[i] + another.data[i];
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
            final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
            for (int row=0; row<rows(); row++) {
                toff.setRow(row);
                aoff.setRow(row);
                for (int col=0; col<cols(); col++) {
                    result.data[addr] = this.data[toff.op()] + another.data[aoff.op()];
                    addr++;
                    toff.nextCol();
                    aoff.nextCol();
                }
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
        QL.require(rows() == another.rows() && cols() == another.cols() ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(rows(), cols());
        if (this.addr.contiguous() && another.addr.contiguous()) {
            for (int addr=0; addr<size(); addr++) {
                result.data[addr] = this.data[addr] - another.data[addr];
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
            final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
            for (int row=0; row<rows(); row++) {
                toff.setRow(row);
                aoff.setRow(row);
                for (int col=0; col<cols(); col++) {
                    result.data[addr] = this.data[toff.op()] - another.data[aoff.op()];
                    addr++;
                    toff.nextCol();
                    aoff.nextCol();
                }
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
        final Matrix result = new Matrix(rows(), cols());
        if (addr.contiguous()) {
            for (int addr=0; addr<size(); addr++) {
                result.data[addr] = this.data[addr] * scalar;
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset src = this.addr.offset();
            for (int row = 0; row < rows(); row++) {
                src.setRow(row);
                for (int col = 0; col < cols(); col++) {
                    result.data[addr] = this.data[src.op()] * scalar;
                    addr++;
                    src.nextCol();
                }
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
        final Matrix result = new Matrix(rows(), cols());
        if (addr.contiguous()) {
            for (int addr=0; addr<size(); addr++) {
                result.data[addr] = this.data[addr] / scalar;
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset src = this.addr.offset();
            for (int row = 0; row < rows(); row++) {
                src.setRow(row);
                for (int col = 0; col < cols(); col++) {
                    result.data[addr] = this.data[src.op()] / scalar;
                    addr++;
                    src.nextCol();
                }
            }
        }
        return result;
    }


    //
    //	Vectorial products
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
        QL.require(cols() == array.size(), ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(rows());
        final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset  aoff = array.addr.offset();
        for (int row = 0; row < result.size(); row++) {
            toff.setRow(row); toff.setCol(0);
            aoff.setIndex(0);
            double sum = 0.0;
            for (int col = 0; col < this.cols(); col++) {
                final double telem = this.data[toff.op()];
                final double aelem = array.data[aoff.op()];
                sum += telem * aelem;
                toff.nextCol();
                aoff.nextIndex();
            }
            result.data[row] = sum;
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
        QL.require(cols() == another.rows(),  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(rows(), another.cols());
        final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
        final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
        for (int col = 0; col < another.cols(); col++) {
            for (int row = 0; row < this.rows(); row++) {
                toff.setRow(row); toff.setCol(0);
                aoff.setRow(0);   aoff.setCol(col);
                double sum = 0.0;
                for (int i = 0; i < this.cols(); i++) {
                    final double telem = this.data[toff.op()];
                    final double aelem = another.data[aoff.op()];
                    sum += telem * aelem;
                    toff.nextCol();
                    aoff.nextRow();
                }
                result.data[result.addr.op(row, col)] = sum;
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

    /**
     * Returns the transpose of <code>this</code> Matrix
     *
     * @return a new instance which contains the result of this operation
     */
    public Matrix transpose() {
        final Matrix result = new Matrix(cols(), rows());
        final Address.MatrixAddress.MatrixOffset src = this.addr.offset();
        final Address.MatrixAddress.MatrixOffset dst = result.addr.offset();
        for (int row=0; row<rows(); row++) {
            src.setRow(row); src.setCol(0);
            dst.setRow(0);   dst.setCol(row);
            for (int col=0; col<cols(); col++) {
                result.data[dst.op()] = data[src.op()];
                src.nextCol();
                dst.nextRow();
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
        QL.require(rows() == cols(),  MATRIX_MUST_BE_SQUARE); // QA:[RG]::verified
        final Array result = new Array(cols());
        int addr = 0;
        for (int i = 0; i < cols(); i++) {
            result.data[i] = data[addr];
            addr += cols() + 1;
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
        return solve(new Identity(rows()));
    }

    /**
     * Solve A*X = B
     *
     * @param m right hand side
     * @return solution if A is square, least squares solution otherwise
     */
    public Matrix solve (final Matrix m) {
       return (rows() == cols()
               ? (new LUDecomposition(this)).solve(m)
                       : (new QRDecomposition(this)).solve(m));
    }


    //
    //  Range
    //
    //  method       this    right    result
    //  ------------ ------- -------- ------
    //  rangeRow     Matrix           Array
    //  rangeCol     Matrix           Array
    //  range        Matrix           Matrix
    //

    public Array rangeRow(final int row) {
        return rangeRow(row, 0, cols()-1);
    }

    public Array rangeRow(final int row, final int col0) {
        return rangeRow(row, col0, cols()-1);
    }

    public Array rangeRow(final int row, final int col0, final int col1) {
        QL.require(row  >= 0 && row  < rows(), ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(col0 >= 0 && col0 < cols() && col1 >= 0 && col1 < cols(), ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(col0<=col1, Address.INVALID_BACKWARD_INDEXING);
        return new RangeRow(row, this.addr, col0, col1, data, rows(), cols());
    }


    public Array rangeCol(final int col) {
        return rangeCol(col, 0, rows()-1);
    }

    public Array rangeCol(final int col, final int row0) {
        return rangeCol(col, row0, rows()-1);
    }

    public Array rangeCol(final int col, final int row0, final int row1) {
        QL.require(col  >= 0 && col  < cols(), ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(row0 >= 0 && row0 < rows() && row1 >= 0 && row1 < rows(), ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(row0<=row1, Address.INVALID_BACKWARD_INDEXING);
        return new RangeCol(row0, row1, this.addr, col, data, rows(), cols());
    }


    public Matrix range(final int row0, final int row1, final int col0, final int col1) {
        QL.require(row0 >= 0 && row0 < rows() && row1 >= 0 && row1 < rows(), ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(row0<=row1, Address.INVALID_BACKWARD_INDEXING);
        QL.require(col0 >= 0 && col0 < cols() && col1 >= 0 && col1 < cols(), ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(col0<=col1, Address.INVALID_BACKWARD_INDEXING);
        final boolean contiguous = super.cols()==(col1-col0+1);
        return new RangeMatrix(row0, row1, this.addr, col0, col1, contiguous, data, rows(), cols());
    }

    public Matrix range(final int[] ridx, final int col0, final int col1) {
        return new RangeMatrix(ridx, this.addr, col0, col1, data, rows(), cols());
    }

    public Matrix range(final int row0, final int row1, final int[] cidx) {
        return new RangeMatrix(row0, row1, this.addr, cidx, data, rows(), cols());
    }

    public Matrix range(final int[] ridx, final int[] cidx) {
        return new RangeMatrix(ridx, this.addr, cidx, data, rows(), cols());
    }






    public Array constRangeRow(final int row) {
        return constRangeRow(row, 0, cols()-1);
    }

    public Array constRangeRow(final int row, final int col0) {
        return constRangeRow(row, col0, cols()-1);
    }

    public Array constRangeRow(final int row, final int col0, final int col1) {
        QL.require(row  >= 0 && row  < rows(), ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(col0 >= 0 && col0 < cols() && col1 >= 0 && col1 < cols(), ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(col0<=col1, Address.INVALID_BACKWARD_INDEXING);
        return new ConstRangeRow(row, this.addr, col0, col1, data, rows(), cols());
    }


    public Array constRangeCol(final int col) {
        return constRangeCol(col, 0, rows()-1);
    }

    public Array constRangeCol(final int col, final int row0) {
        return constRangeCol(col, row0, rows()-1);
    }

    public Array constRangeCol(final int col, final int row0, final int row1) {
        QL.require(col  >= 0 && col  < cols(), ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(row0 >= 0 && row0 < rows() && row1 >= 0 && row1 < rows(), ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(row0<=row1, Address.INVALID_BACKWARD_INDEXING);
        return new ConstRangeCol(row0, row1, this.addr, col, data, rows(), cols());
    }


    public Matrix constRange(final int row0, final int row1, final int col0, final int col1) {
        QL.require(row0 >= 0 && row0 < rows() && row1 >= 0 && row1 < rows(), ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(row0<=row1, Address.INVALID_BACKWARD_INDEXING);
        QL.require(col0 >= 0 && col0 < cols() && col1 >= 0 && col1 < cols(), ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(col0<=col1, Address.INVALID_BACKWARD_INDEXING);
        final boolean contiguous = super.cols()==(col1-col0+1);
        return new ConstRangeMatrix(row0, row1, this.addr, col0, col1, contiguous, data, rows(), cols());
    }

    public Matrix constRange(final int[] ridx, final int col0, final int col1) {
        return new ConstRangeMatrix(ridx, this.addr, col0, col1, data, rows(), cols());
    }

    public Matrix constRange(final int row0, final int row1, final int[] cidx) {
        return new ConstRangeMatrix(row0, row1, this.addr, cidx, data, rows(), cols());
    }

    public Matrix constRange(final int[] ridx, final int[] cidx) {
        return new ConstRangeMatrix(ridx, this.addr, cidx, data, rows(), cols());
    }














    //
    // TODO: better comments
    //

    public Matrix fill(final double scalar) {
        QL.require(addr.contiguous(), NON_CONTIGUOUS_DATA);
        Arrays.fill(data, addr.base(), addr.last(), scalar);
        return this;
    }

    public Matrix fill(final Matrix another) {
        QL.require(addr.contiguous(), NON_CONTIGUOUS_DATA);
        QL.require(another.addr.contiguous(), NON_CONTIGUOUS_DATA);
        QL.require(this.rows()==another.rows() && this.cols()==another.cols() && this.size()==another.size(), WRONG_BUFFER_LENGTH);
        // copies data
        System.arraycopy(another.data, addr.base(), this.data, 0, addr.last()-addr.base());
        return this;
    }

    /**
     * Overwrites contents of a certain row
     *
     * @param row is the requested row to be overwritten
     * @param array contains the elements to be copied
     */
    public void fillRow(final int row, final Array array) {
        QL.require(cols() == array.size() ,  ARRAY_IS_INCOMPATIBLE);
        if (this.addr.contiguous() && array.addr.contiguous()) {
            System.arraycopy(array.data, 0, data, addr.op(row, 0), cols());
        } else {
            final Address.ArrayAddress.ArrayOffset src = array.addr.offset();
            final Address.MatrixAddress.MatrixOffset dst = this.addr.offset(row, 0);
            for (int col = 0; col < cols(); col++) {
                data[dst.op()] = array.data[src.op()];
                src.nextIndex();
                dst.nextCol();
            }
        }
    }

    /**
     * Overwrites contents of a certain column
     *
     * @param col is the requested column to be overwritten
     * @param array contains the elements to be copied
     */
    public void fillCol(final int col, final Array array) {
        QL.require(rows() == array.size() ,  ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        if (this.addr.contiguous() && array.addr.contiguous() && cols() == 1) {
            System.arraycopy(array.data, 0, data, 0, size());
        } else {
            final Address.ArrayAddress.ArrayOffset src = array.addr.offset();
            final Address.MatrixAddress.MatrixOffset dst = this.addr.offset(0, col);
            for (int row = 0; row < rows(); row++) {
                data[dst.op()] = array.data[src.op()];
                src.nextIndex();
                dst.nextRow();
            }
        }
    }

    public Matrix swap(final Matrix another) {
        QL.require(addr.contiguous(), NON_CONTIGUOUS_DATA);
        QL.require(another.addr.contiguous(), NON_CONTIGUOUS_DATA);
        QL.require(this.rows()==another.rows() && this.cols()==another.cols() && this.size()==another.size(), WRONG_BUFFER_LENGTH);
        // swaps data
        final double [] tdata;
        final Address.MatrixAddress taddr;
        tdata = this.data;  this.data = another.data;  another.data = tdata;
        taddr = this.addr;  this.addr = another.addr;  another.addr = taddr;
        return this;
    }

    public Matrix sort() {
        QL.require(addr.contiguous(), NON_CONTIGUOUS_DATA);
        Arrays.sort(data, addr.base(), addr.last());
        return this;
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
    // TODO: implement equals() with a near-linear approach
    // 1. the chain of Addresses must be equal
    // 2. the base and last memory addresses must be equal in all Addresses
    // *** THIS IDEA NEEDS VALIDATION ***
    public boolean equals(final Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[rows=").append(rows()).append(" cols=").append(cols()).append('\n');
        for (int row = 0; row < rows(); row++) {
            sb.append(" [ ");
            sb.append(data[addr.op(row, 0)]);
            for (int col = 1; col < cols(); col++) {
                sb.append(", ");
                sb.append(data[addr.op(row, col)]);
            }
            sb.append(" ]\n");
        }
        sb.append("]\n");
        return sb.toString();
    }




//XXX
//  //
//  // static methods
//  //
//
//    /**
//     * sqrt(a^2 + b^2) without under/overflow.
//     */
//    //TODO: verify if it can be replaced by Math.hypot
//    public static double hypot(final double a, final double b) {
//        double r;
//        if (Math.abs(a) > Math.abs(b)) {
//            r = b / a;
//            r = Math.abs(a) * Math.sqrt(1 + r * r); Math.h
//        } else if (b != 0) {
//            r = a / b;
//            r = Math.abs(b) * Math.sqrt(1 + r * r);
//        } else {
//            r = 0.0;
//        }
//        return r;
//    }


    //
    // private inner classes
    //

    private static class RangeRow extends Array {

        public RangeRow(
            final int row,
            final Address.MatrixAddress chain,
            final int col0,
            final int col1,
            final double[] data,
            final int rows,
            final int cols) {
            super(1, col1-col0+1,
                  data,
                  new DirectArrayRowAddress(row, chain, col0, col1, EnumSet.of(Address.Flags.CONTIGUOUS), rows, cols));
        }
    }


    private static class RangeCol extends Array {

        public RangeCol(
                final int row0,
                final int row1,
                final Address.MatrixAddress chain,
                final int col,
                final double[] data,
                final int rows,
                final int cols) {
            super(row1-row0+1, 1,
                  data,
                  new DirectArrayColAddress(row0, row1, chain, col, EnumSet.of(Address.Flags.CONTIGUOUS), rows, cols));
        }
    }


    private static class RangeMatrix extends Matrix {

        public RangeMatrix(
            final int row0,
            final int row1,
            final Address.MatrixAddress chain,
            final int col0,
            final int col1,
            final boolean contiguous,
            final double[] data,
            final int rows,
            final int cols) {
            super(row1-row0+1, col1-col0+1,
                  data,
                  new DirectMatrixAddress(row0, row1, chain, col0, col1, EnumSet.of(Address.Flags.CONTIGUOUS), rows, cols));
        }

        public RangeMatrix(
            final int[] ridx,
            final Address.MatrixAddress chain,
            final int col0,
            final int col1,
            final double[] data,
            final int rows,
            final int cols) {
            super(ridx.length, col1-col0+1,
                  data,
                  new MappedMatrixAddress(ridx, chain, col0, col1, EnumSet.noneOf(Address.Flags.class), rows, cols));
        }

        public RangeMatrix(
            final int row0,
            final int row1,
            final Address.MatrixAddress chain,
            final int[] cidx,
            final double[] data,
            final int rows,
            final int cols) {
            super(row1-row0+1, cidx.length,
                  data,
                  new MappedMatrixAddress(row0, row1, chain, cidx, EnumSet.noneOf(Address.Flags.class), rows, cols));
        }

        public RangeMatrix(
            final int[] ridx,
            final Address.MatrixAddress chain,
            final int[] cidx,
            final double[] data,
            final int rows,
            final int cols) {
            super(ridx.length, cidx.length,
                  data,
                  new MappedMatrixAddress(ridx, chain, cidx, EnumSet.noneOf(Address.Flags.class), rows, cols));
        }
    }


    private static class ConstRangeRow extends RangeRow {

        public ConstRangeRow(
            final int row,
            final Address.MatrixAddress chain,
            final int col0,
            final int col1,
            final double[] data,
            final int rows,
            final int cols) {
            super(row, chain, col0, col1, data, rows, cols);
        }

        @Override
        public void set(final int pos, final double value) {
            throw new UnsupportedOperationException();
        }
    }


    private static class ConstRangeCol extends RangeCol {

        public ConstRangeCol(
                final int row0,
                final int row1,
                final Address.MatrixAddress chain,
                final int col,
                final double[] data,
                final int rows,
                final int cols) {
            super(row0, row1, chain, col, data, rows, cols);
        }

        @Override
        public void set(final int pos, final double value) {
            throw new UnsupportedOperationException();
        }
    }


    private static class ConstRangeMatrix extends RangeMatrix {

        public ConstRangeMatrix(
            final int row0,
            final int row1,
            final Address.MatrixAddress chain,
            final int col0,
            final int col1,
            final boolean contiguous,
            final double[] data,
            final int rows,
            final int cols) {
            super(row0, row1, chain, col0, col1, contiguous, data, rows, cols);
        }

        public ConstRangeMatrix(
            final int[] ridx,
            final Address.MatrixAddress chain,
            final int col0,
            final int col1,
            final double[] data,
            final int rows,
            final int cols) {
            super(ridx, chain, col0, col1, data, rows, cols);
        }

        public ConstRangeMatrix(
            final int row0,
            final int row1,
            final Address.MatrixAddress chain,
            final int[] cidx,
            final double[] data,
            final int rows,
            final int cols) {
            super(row0, row1, chain, cidx, data, rows, cols);
        }

        public ConstRangeMatrix(
            final int[] ridx,
            final Address.MatrixAddress chain,
            final int[] cidx,
            final double[] data,
            final int rows,
            final int cols) {
            super(ridx, chain, cidx, data, rows, cols);
        }

        @Override
        public void set(final int row, final int col, final double value) {
            throw new UnsupportedOperationException();
        }

    }

}
