package org.jquantlib.math.matrixutilities;

import org.jquantlib.QL;

public class Cells {

    //
    // protected final static fields :: error messages
    //

    protected final static String INVALID_ARGUMENTS = "invalid arguments";
    protected final static String WRONG_BUFFER_LENGTH = "wrong buffer length";
    protected final static String MATRIX_IS_INCOMPATIBLE = "matrix is incompatible";
    protected final static String ARRAY_IS_INCOMPATIBLE = "array is incompatible";
    protected final static String MATRIX_MUST_BE_SQUARE = "matrix must be square";


    //
    // public fields
    //

    protected int cols, rows;
    protected int size;


    //
    // protected fields
    //

    protected double[] data;


    /**
     * Builds a Storage of <code>rows</code> by <code>cols</code>
     *
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @throws IllegalArgumentException if parameters are less than zero
     */
    protected Cells(final int rows, final int cols) {
        QL.require(rows>0 && cols>0 ,  INVALID_ARGUMENTS);
        this.rows = rows;
        this.cols = cols;
        this.size = rows*cols;
        this.data = new double[size];
    }


    /**
     * Swaps contents of <code>this</code> Storage by <code>another</code> Storage
     *
     * @param another
     * @return this
     */
    protected Cells swap(final Cells another) {
        int itmp;
        double [] dtmp;

        // swaps rows, cols and length
        itmp = this.rows;   this.rows   = another.rows;   another.rows   = itmp;
        itmp = this.cols;   this.cols   = another.cols;   another.cols   = itmp;
        itmp = this.size; this.size = another.size; another.size = itmp;
        // swaps data
        dtmp = this.data;   this.data   = another.data;   another.data   = dtmp;
        return this;
    }

//  /**
//  * Swaps elements given their coordinates
//  *
//  * @param pos1row : element1 row
//  * @param pos1col : element1 col
//  * @param pos2row : element2 row
//  * @param pos2col : element2 col
//  * @return this
//  */
// protected Storage swap(final int pos1row, final int pos1col, final int pos2row, final int pos2col) {
//     final int addr1 = addr(pos1row, pos1col);
//     final int addr2 = addr(pos2row, pos2col);
//     final double tmp = data[addr1];
//     data[addr1] = data[addr2];
//     data[addr2] = tmp;
//     return this;
// }

}
