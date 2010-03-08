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

package org.jquantlib.math.matrixutilities;

import org.jquantlib.math.matrixutilities.internal.Address;

/**
 * This class provides efficient basement for matrix operations by mapping matrices onto
 * a linear array, which performs better than bidimensional arrays.
 * <p>
 * In addition, access to elements is calculated by a certain policy which is intended to
 *  Matrix elements need to be translated to the underlying linear
 * storage when they are read or written.
 * <p>
 * In addition, this class support Java and Fortran programming languages, being more
 * specific: the way how these languages perform loops. Whilst in C, C++ and Java we
 * employ zero-based indexing, in FORTRAN we employ one-based indexing.
 * <p>
 * Certain algorithms make use of FORTRAN style indexing. In these you will tend to see
 * <pre>
 *     for (i = 1; i <= n; i++)
 * </pre>
 * rather than what you can see in Java, C, C++:
 * <pre>
 *     for (i = 0; i < n; i++)
 * </pre>
 *
 * @author Richard Gomes
 */
public abstract class Cells<T extends Address> {

    //
    // protected final static fields :: error messages
    //

    protected final static String INVALID_ARGUMENTS = "invalid arguments";
    protected final static String WRONG_BUFFER_LENGTH = "wrong buffer length";
    protected final static String MATRIX_IS_INCOMPATIBLE = "matrix is incompatible";
    protected final static String ARRAY_IS_INCOMPATIBLE = "array is incompatible";
    protected final static String ITERATOR_IS_INCOMPATIBLE = "iterator is incompatible";
    protected final static String NOT_ENOUGH_STORAGE = "not enough storage area for operation";
    protected final static String MATRIX_MUST_BE_SQUARE = "matrix must be square";
    protected final static String MATRIX_MUST_BE_SYMMETRIC = "matrix must be symmetric";
    protected final static String MATRIX_IS_SINGULAR = "matrix is singular";
    protected final static String NON_CONTIGUOUS_DATA = "Operation not supported on non-contiguous data";


    //
    // protected fields
    //

    protected final int rows;
    protected final int cols;
    protected final int size;


    //
    // protected fields
    //

    protected double[] data;
    protected T addr;



    /**
     * Builds a Storage of <code>rows</code> by <code>cols</code>
     *
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @throws IllegalArgumentException if parameters are less than zero
     */
    protected Cells(
            final int rows,
            final int cols,
            final T addr) {
        this.rows = rows;
        this.cols = cols;
        this.addr = addr;
        this.size = rows*cols;
        this.data = new double[size];
    }


    /**
     * Performs a shallow copy of another <code>Cells</code> instance
     * <p>
     * This constructor accepts a custom <code>Offset</code> implementation, which is responsible for
     * mapping a virtual address map onto its physical representation on an already existing <code>Cells</code>.
     *
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @param data is an existing data buffer
     * @param addr is an Address calculation policy responsible for mapping virtual addresses to physical addresses
     */
    protected Cells(
            final int rows,
            final int cols,
            final double data[],
            final T addr) {
        this.rows = rows;
        this.cols = cols;
        this.data = data;
        this.addr = addr;
        this.size = rows*cols;
        if (data.length != addr.rows()*addr.cols())
            throw new IllegalArgumentException("declared dimension do not match underlying storage size");
    }


    //
    // public methods
    //

    public final int rows()      { return rows; }
    public final int columns()   { return cols; }
    public final int cols()      { return cols; } // FIXME: remove this
    public final int size()      { return size; }
    public final boolean empty() { return size <= 0; }

}
