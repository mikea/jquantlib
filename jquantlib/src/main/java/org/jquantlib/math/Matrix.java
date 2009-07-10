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
package org.jquantlib.math;

import java.util.Arrays;

/**
 * Bidimensional matrix operations
 * <p>
 * Performance of multidimensional arrays is a big concern in Java. This is because multidimensional arrays
 * are stored as arrays to arrays, spanning this concept to as many depths as necessary. In C++, a multidimensional
 * array is stored internally as a unidimensional array where depths are stacked together one after another.
 * A very simple calculation is needed to map multiple dimensional indexes to an unidimensional index.
 * <p>
 * This implementation provides the C/C++ approach of an internal unidimensional array. Everytime a bidimensional
 * index is involved (because this is a 2d matrix) it is converted to a unidimensional index. In case developers
 * are seriously concerned about performance, a unidimensional access method is provided, giving a chance for
 * application developers to 'cache' the starting of a row and reducing the number of multiplications needed for
 * offset calculations.
 * <p>
 * <p>
 * <b>Assignment operations</b>
 * <pre>
 * opr   method     this    right    result
 * ----- ---------- ------- -------- ------
 * =     assign     Matrix           Matrix (1)
 * =     assign     Array            Array  (1)
 * +=    addAssign  Matrix  Matrix   this
 * +=    addAssign  Array   scalar   this
 * +=    addAssign  Array   Array    this
 * -=    subAssign  Matrix  Matrix   this
 * -=    subAssign  Array   scalar   this
 * -=    mulAssign  Array   Array    this
 * *=    mulAssign  Matrix  scalar   this
 * *=    mulAssign  Array   scalar   this
 * *=    mulAssign  Array   Array    this
 * /=    divAssign  Matrix  scalar   this
 * /=    divAssign  Array   scalar   this
 * /=    divAssign  Array   Array    this
 * </pre>
 * <p>
 * <p>
 * <b>Algebraic products</b>
 * <pre>
 * opr   method     this    right    result
 * ----- ---------- ------- -------- ------
 * +     add        Matrix  Matrix    Matrix
 * +     positive   Array             Array  (2)
 * +     add        Array   scalar    Array
 * +     add        Array   Array     Array
 * -     sub        Matrix  Matrix    Matrix
 * -     negative   Array             Array  (3)
 * -     sub        Array   scalar    Array
 * -     sub        Array   Array     Array
 * *     mul        Matrix  scalar    Matrix
 * *     mul        Array   scalar    Array
 * *     mul        Array   Array     Array
 * /     div        Matrix  scalar    Matrix
 * /     div        Array   scalar    Array
 * /     div        Array   Array     Array
 *</pre>
 * <p>
 * <p>
 * <b>Vetorial products</b>
 * <pre>
 * opr   method     this    right    result
 * ----- ---------- ------- -------- ------
 * *     mul        Array   Matrix   Array
 * *     mul        Matrix  Array    Array
 * *     mul        Matrix  Matrix   Matrix
 *</pre>
 * <p>
 * <p>
 * <b>Math functions</b>
 * <pre>
 * opr   method     this    right    result
 * ----- ---------- ------- -------- ------
 * abs   abs        Array            Array
 * sqrt  sqrt       Array            Array
 * log   log        Array            Array
 * exp   exp        Array            Array
 *</pre>
 * <p>
 * <p>
 * <b>Miscellaneous</b>
 * <pre>
 * method       this    right    result
 * ------------ ------- -------- ------
 * transpose    Matrix           Matrix
 * diagonal     Matrix           Array
 * inverse      Matrix           Matrix
 * swap         Matrix  Matrix   this
 * swap         Array   Array    this
 * outerProduct Array   Array    Matrix
 * dotProduct   Array   Array    double
 * </pre>
 * <p>
 * <p>
 * (1): clone()<br/>
 * (2): Unary + is equivalent to: array.clone()<br/>
 * (3): Unary ? is equivalent to: array.clone().mulAssign(-1)
 * <p>
 * @Note: This is a very naive implementation: there's opportunity for several improvements, like adoption of
 * paralellism for several kinds of operations and adoption of JSR-166y for matrix multiplication.
 *
 * @author Richard Gomes
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
// TODO: refactor Array and Matrix to math.matrixutilities (or something like this)
public class Matrix {
    
    //
    // constants
    //
    
    private static final int blksize = 256; // seems to be a reasonably big enough block size
    
    
	//
	// public fields
	//
	
	public final int cols, rows;
    public final int length;


	//
	// package private
	//

	/*@PackagePrivate*/ final double[] data;

	
	//
	// public constructors
	//
	
	/**
	 * Default constructor
	 * <p>
	 * Builds an empty Matrix
	 */
	public Matrix() {
        this.rows = 0;
        this.cols = 0;
        this.length = 0;
		this.data = new double[0];
	}

	/**
	 * Builds a Matrix of <code>rows</code> by <code>cols</code>
	 * 
	 * @param rows is the number of rows
	 * @param cols is the number of columns
	 */
	public Matrix(final int rows, final int cols) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
	    if ((rows<=0 && cols>0) || (cols<=0 && rows>0)) throw new IllegalArgumentException(); // TODO: message

	    this.rows = rows;
        this.cols = cols;
        this.length = rows*cols;
        this.data = new double[length];
	}

    /**
     * Builds a Matrix of only one element which holds a <code>scalar</code> value
     * 
     * @param scalar is the scalar value
     */
    public Matrix(final int rows, final int cols, final double scalar) {
        this(rows, cols);
        fill(scalar);
    }
    
	/**
	 * Creates a Matrix given a double[][] array
	 * 
	 * @param data
	 */
	public Matrix(final double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.length = rows*cols;
        this.data = new double[length];
        
        for (int i=0; i<this.rows; i++) {
            int base=address(i);
            System.arraycopy(data[i], 0, this.data, base, this.cols);
        }
    }
	
	
    //
    // overrides Object
    //
    
    @Override
    public Matrix clone() {
        return this.copyOfRange(0, 0, this.rows, this.cols);
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
        StringBuffer sb = new StringBuffer();
        sb.append('[').append('\n');
        for (int row = 0; row < this.rows; row++) {
            int addr = address(row);
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
	// package private methods
	//
	
    /**
     * This method returns the address of the first column in a given row
     * <p>
	 * This method is used internally and is provided for performance reasons.
	 */
	/*@PackagePrivate*/ int address(final int row) {
        return row*this.cols;
    }

    /**
     * This method returns the address of a given cell identified by <i>(row, col)</i>
     * <p>
     * This method is used internally and is provided for performance reasons.
     */
    /*@PackagePrivate*/ int address(final int row, final int col) {
        return row*this.cols + col;
    }

    
    //
	// public methods
	//
    
    
    // some convenience methods
    
    
    public Object toArray() {
        double buffer[][] = new double[this.rows][this.cols];
        return toArray(buffer);
    }
    
    public double[][] toArray(double[][] buffer) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.rows != buffer.length || this.cols != buffer[0].length) throw new IllegalArgumentException(); //TODO:message
        
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
     */
    public Matrix fill(final double scalar) {
        Arrays.fill(data, scalar);
        return this;
    }

    /**
     * Returns Matrix containing a copy of a rectangular region
     * 
     * @param row is the initial row
     * @param col is the initial column
     * @param nrows is the number of rows to be copied
     * @param ncols is the number of columns to be copied
     * 
     * @return a Matrix containing a copy of a rectangular region
     */
    public Matrix copyOfRange(final int row, final int col, final int nrows, final int ncols) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if ((row < 0) || (col < 0)) throw new IllegalArgumentException(); //TODO: message
        if ((nrows <= 0) || (ncols <= 0)) throw new IllegalArgumentException(); //TODO: message
        if ((row+nrows > this.rows) || (col+ncols > this.cols)) throw new IllegalArgumentException(); //TODO: message
        
        final Matrix result = new Matrix(nrows, ncols);
        if (col+ncols == this.cols) {
            System.arraycopy(this.data, 0, result.data, 0, this.length);
        } else {
            int addr = 0;
            for (int i=0; i<nrows; i++) {
                System.arraycopy(data, address(row+i, col), data, addr, ncols);
                addr += ncols;
            }
        }
        return result;
    }
    
    
    /**
     * Retrieves an elementof <code>this</code> Matrix which identified by <i>(row, col)</i>
     * 
     * @param row coordinate
     * @param col coordinate
     * @return the contents of a given cell
     */
    public double get(final int row, final int col) {
        return data[address(row, col)];
    }

    /**
     * Stores a value into an element of <code>this</code> Matrix which is identified by <i>(row, col)</i>
     * 
     * @param row coordinate
     * @param col coordinate
     */
    public void set(final int row, final int col, final double value) {
        data[address(row, col)] = value;
    }

    
    /**
     * Retrieves an element of <code>this</code> Matrix
     * <p>
     * This method is provided for performance reasons.
     * See methods {@link #getAddress(int)} and {@link #getAddress(int, int)} for more details
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
     * This method is provided for performance reasons.
     * See methods {@link #getAddress(int)} and {@link #getAddress(int, int)} for more details
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
	 * This method returns the address of the first column in a given row
	 * <p>
	 * A typical usage of this method is when one would like to improve access to elements of a given row by reducing
	 * the number of calculations needed to obtain the address of cells belonging to that row.
	 * 
     * @param row is the desired row which the address is requested for.  
	 */
	public int getAddress(final int row) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
	    if (row < 0 || row > this.rows) throw new IllegalArgumentException(); //TODO: message
	    return row*this.cols;
	}
	
    /**
     * This method returns the address of a given cell identified by <i>(row, col)</i>
     * <p>
     * A typical usage of this method is when one would like to improve access to a given cell, basically
     * keeping its address for later use.
     * 
     * @param row is the desired row which a cell belongs to.  
     * @param col is the desired col which a cell belongs to.  
     */
	public int getAddress(final int row, final int col) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (col < 0 || row > this.cols) throw new IllegalArgumentException(); //TODO: message
        return getAddress(row)+col;
	}
	
    public Array getRow(final int row) {
        final Array vector = new Array(this.cols);
        System.arraycopy(this.data, getAddress(row), vector.data, 0, this.cols);
        return vector;
    }

    public Array setRow(final int row, final Array array) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.cols!=array.length) throw new IllegalArgumentException(); //TODO: message
        
        System.arraycopy(array.data, 0, this.data, getAddress(row), this.cols);
        return array;
    }

    public Array getCol(final int col) {
        final Array array = new Array(this.rows);
        if (this.cols == 1) {
            System.arraycopy(this.data, 0, array.data, 0, this.length);
        } else {
            int addr = getAddress(0, col);
            for (int row = 0; row < this.rows; row++) {
                array.data[row] = this.data[addr];
                addr += this.cols;
            }
        }
        return array;
    }

    public Array setColumn(final int col, final Array array) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.rows!=array.length) throw new IllegalArgumentException(); //TODO: message
        
        if (this.cols == 1) {
            System.arraycopy(array.data, 0, this.data, 0, this.length);
        } else {
            int addr = getAddress(0, col);
            for (int row = 0; row < this.rows; row++) {
                this.data[addr] = array.data[row];
                addr += this.cols;
            }
        }
        return array;
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
	
	
    public Matrix addAssign(final Matrix another) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.rows != another.rows || this.cols != another.cols) throw new IllegalArgumentException(); //TODO: message

        for (int row=0; row<rows; row++) {
            int addr = address(row);
            for (int col=0; col<cols; col++) {
                this.data[addr] += another.data[addr];
                addr++;
            }
        }
        return this;
    }

    public Matrix subAssign(final Matrix another) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.rows != another.rows || this.cols != another.cols) throw new IllegalArgumentException(); //TODO: message

        for (int row=0; row<rows; row++) {
            int addr = address(row);
            for (int col=0; col<cols; col++) {
                this.data[addr] -= another.data[addr];
                addr++;
            }
        }
        return this;
    }

    public Matrix mulAssign(final double scalar) {
        for (int row = 0; row < rows; row++) {
            int rowAddress = address(row);
            for (int col = 0; col < cols; col++) {
                int cellAddress = rowAddress + col;
                data[cellAddress] *= scalar;
            }
        }
        return this;
    }

    public Matrix divAssign(final double scalar) {
        for (int row = 0; row < rows; row++) {
            int rowAddress = address(row);
            for (int col = 0; col < cols; col++) {
                int cellAddress = rowAddress + col;
                data[cellAddress] /= scalar;
            }
        }
        return this;
    }
    
    
    	
    //	Algebraic products
    //
    //	opr   method     this    right    result
    //	----- ---------- ------- -------- ------
    //	+     add        Matrix  Matrix    Matrix
    //	-     sub        Matrix  Matrix    Matrix
    //	*     mul        Matrix  scalar    Matrix
    //	/     div        Matrix  scalar    Matrix

    public Matrix add(final Matrix another) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.rows != another.rows || this.cols != another.cols) throw new IllegalArgumentException(); //TODO: message

        Matrix result = new Matrix(this.rows, this.cols);
        for (int row=0; row<rows; row++) {
            int addr = address(row);
            for (int col=0; col<cols; col++) {
                result.data[addr] = this.data[addr] + another.data[addr];
                addr++;
            }
        }
        return result;
    }

    public Matrix sub(final Matrix another) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.rows != another.rows || this.cols != another.cols) throw new IllegalArgumentException(); //TODO: message

        Matrix result = new Matrix(this.rows, this.cols);
        for (int row=0; row<rows; row++) {
            int addr = address(row);
            for (int col=0; col<cols; col++) {
                result.data[addr] = this.data[addr] - another.data[addr];
                addr++;
            }
        }
        return result;
    }

    public Matrix mul(final double scalar) {
        Matrix result = new Matrix(this.rows, this.cols);
        for (int row=0; row<rows; row++) {
            int addr = address(row);
            for (int col=0; col<cols; col++) {
                result.data[addr] = data[addr] * scalar;
                addr++;
            }
        }
        return result;
    }

    public Matrix div(final double scalar) {
        Matrix result = new Matrix(this.rows, this.cols);
        for (int row=0; row<rows; row++) {
            int addr = address(row);
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
    
    public Array mul(final Array array) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.cols != array.length) throw new IllegalArgumentException(); //TODO: message

        final Array result = new Array(this.cols);
        for (int col=0; col<this.cols; col++) {
            int addr = address(0, col);
            double sum = 0.0;
            for (int row=0; row<this.rows; row++) {
                sum  += this.data[addr] * array.data[col];
                addr += this.cols; 
            }
            result.data[col] = sum;
        }
        return result;
    }
    
    public Matrix mul(final Matrix another) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.cols != another.rows) throw new IllegalArgumentException(); //TODO: message
        
        Matrix result = new Matrix(this.rows, another.cols);
        for (int col = 0; col < another.cols; col++) {
            int caddr = another.address(0, col);
            for (int row = 0; row < this.rows; row++) {
                int raddr = address(row, 0);
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
    //	Math functions
    //
    //	opr   method     this    right    result
    //	----- ---------- ------- -------- ------
    //  (none)
    
    
    //
    //	Miscellaneous
    //
    //	method       this    right    result
    //	------------ ------- -------- ------
    //  swap         Matrix  Matrix   this
    //	transpose    Matrix           Matrix
	//  diagonal     Matrix           Array
    //	inverse      Matrix           Matrix
	
    public Matrix swap(Matrix another) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.rows != another.rows || this.cols != another.cols) throw new IllegalArgumentException(); //TODO: message
        
        // allocate a temporary buffer for data transfer
        double[] buffer = new double[Math.min(this.length, blksize)]; 

        // swaps blocks
        for (int row=0; row<length/blksize; row++) {
            int addr = row*blksize;
            System.arraycopy(this.data,    addr, buffer,          0, blksize);
            System.arraycopy(another.data, addr, this.data,    addr, blksize);
            System.arraycopy(buffer,          0, another.data, addr, blksize);
        }

        // swaps last block
        final int addr = ((int)(length/blksize))*blksize;
        if (addr>=0 && addr<this.length) {
            final int remainder = this.length-addr;
            System.arraycopy(this.data,    addr, buffer,          0, remainder);
            System.arraycopy(another.data, addr, this.data,    addr, remainder);
            System.arraycopy(buffer,          0, another.data, addr, remainder);
        }
        
        return this;
    }

    public Matrix swap(final int pos1row, final int pos1col, final int pos2row, final int pos2col) {
        int addr1 = address(pos1row, pos1col);
        int addr2 = address(pos2row, pos2col);
        double tmp = data[addr1];
        data[addr1] = data[addr2];
        data[addr2] = tmp;
        return this;
    }
    
    
    public Matrix transpose() {
        final Matrix result = new Matrix(this.cols, this.rows);
        for (int row=0; row<this.rows; row++) {
            int raddr = this.address(row, 0);
            int caddr = result.address(0, row);
            for (int col=0; col<this.cols; col++) {
                result.data[caddr] = this.data[raddr];
                caddr += result.cols;
                raddr++;
            }
        }
        return result;
    }

    /**
     * Obtains a diagonal from a square {@link Matrix}
     * 
     * @return a row-matrix which contains the elements of <code>this</code> square Matrix
     */
    public Array diagonal() {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (this.rows != this.cols) throw new IllegalArgumentException(); //TODO: message
        
        final Array result = new Array(this.cols);
        int addr = 0;
        for (int i = 0; i < this.cols; i++) {
            result.data[i] = this.data[addr];
            addr += this.cols + 1;
        }
        return result;
    }
    
    public Matrix inverse() {
        throw new UnsupportedOperationException();

        
//        
//        Disposable<Matrix> inverse(const Matrix& m) {
//            #if !defined(__GNUC__) || __GNUC__ > 3 || __GNUC_MINOR__ > 3
//
//            QL_REQUIRE(m.rows() == m.columns(), "matrix is not square");
//
//            boost::numeric::ublas::matrix<Real> a(m.rows(), m.columns());
//
//            std::copy(m.begin(), m.end(), a.data().begin());
//
//            boost::numeric::ublas::permutation_matrix<Size> pert(m.rows());
//
//            // lu decomposition
//            const Size singular = lu_factorize(a, pert);
//            QL_REQUIRE(singular == 0, "singular matrix given");
//
//            boost::numeric::ublas::matrix<Real>
//                inverse = boost::numeric::ublas::identity_matrix<Real>(m.rows());
//
//            // backsubstitution
//            boost::numeric::ublas::lu_substitute(a, pert, inverse);
//
//            Matrix retVal(m.rows(), m.columns());
//            std::copy(inverse.data().begin(), inverse.data().end(),
//                      retVal.begin());
//
//            return retVal;
//
//            #else
//            QL_FAIL("this version of gcc does not support the Boost uBlas library");
//            #endif
//        }        
//        
        
        
    }
    
}
	
  
