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
    protected final static String MATRIX_MUST_BE_SYMMETRIC = "matrix must be symmetric";


    //
    // protected final fields
    //

    protected final int rows;
    protected final int cols;
    protected final int size;

    /**
     * This field allows transparent access to elements by FORTRAN based algorithms.
     * <p>
     * Whilst in C, C++ and Java we employ zero-based indexing, in FORTRAN we employ one-based indexing.
     * Certain algorithms make use of FORTRAN style indexing. In these you will tend to see
     * <pre>
     *     for (i = 1; i <= n; i++)
     * </pre>
     * rather than what you can see in Java, C, C++:
     * <pre>
     *     for (i = 0; i < n; i++)
     * </pre>
     *
     * @see Style
     */
    protected final Style style;


    //
    // protected fields
    //

    protected double[] data;




    /**
     * This enumeration describes how an algorithm can employ different styles of accessing data.
     * <p>
     * Whilst in C, C++ and Java we employ zero-based indexing, in FORTRAN we employ one-based indexing.
     * Certain algorithms make use of FORTRAN style indexing. In these you will tend to see
     * <pre>
     *     for (i = 1; i <= n; i++)
     * </pre>
     * rather than what you can see in Java, C, C++:
     * <pre>
     *     for (i = 0; i < n; i++)
     * </pre>
     * <p>
     * When a FORTRAN style indexing is required, you need to choose {@link Style}.FORTRAN
     * This will allow you use a FORTRAN style indexing on a data structure implemented in Java.
     * Notice that methods {@link Matrix#addr(int, int)} and {@link Array#addr(int)} transparently convert from
     * FORTRAN one-based indexing to native Java zero-based indexing.
     * <pre>
     *     for (i = 1; i <= m; i++) {
     *         for (j = 1; j <= n; j++) {
     *             // access a matrix, FORTRAN one-based indexing style
     *             matrix.addr(matrix.addr(i,j) = (i==j) ? 1.0 : 0.0;
     *         }
     *         // access an array, FORTRAN one-based indexing style
     *         array.aIndexStyleddr(array.addr(i) = (i%2==0) ? 1.0 : 0.0;
     *     }
     * </pre>
     * <p>
     * When a Java, C or C++ style indexing is required, which is what happens in general, you need to choose
     * {@link Style}.JAVA, which is the default.
     * <pre>
     *     for (i = 1; i <= m; i++) {
     *         for (j = 1; j <= n; j++) {
     *             // access a matrix, Java zero-based indexing style
     *             matrix.addr(matrix.addr(i,j) = (i==j) ? 1.0 : 0.0;
     *         }
     *         // access an array, Java zero-based indexing style
     *         array.addr(array.addr(i) = (i%2==0) ? 1.0 : 0.0;
     *     }
     * </pre>
     * <p>
     * This method is used internally and is provided for performance reasons.
     *
     *
     * @author Richard Gomes
     */
    public enum Style {

        /**
         * When a Java, C or C++ style indexing is required, which is what happens in general, you need to choose
         * {@link Style}.JAVA, which is the default.
         *
         * <pre>
         *     for (i = 1; i &lt;= m; i++) {
         *         for (j = 1; j &lt;= n; j++) {
         *             // access a matrix, Java zero-based indexing style
         *             matrix.addr(matrix.addr(i,j) = (i==j) ? 1.0 : 0.0;
         *         }
         *         // access an array, Java zero-based indexing style
         *         array.addr(array.addr(i) = (i%2==0) ? 1.0 : 0.0;
         *     }
         * </pre>
         */
        JAVA (0),

        /**
         * When a FORTRAN style indexing is required, you need to choose {@link Style}.FORTRAN This will allow you use a
         * FORTRAN style indexing on a data structure implemented in Java. Notice that methods {@link Matrix#addr(int, int)} and
         * {@link Array#addr(int)} transparently convert from FORTRAN one-based indexing to native Java zero-based indexing.
         *
         * <pre>
         *     for (i = 1; i &lt;= m; i++) {
         *         for (j = 1; j &lt;= n; j++) {
         *             // access a matrix, FORTRAN one-based indexing style
         *             matrix.addr(matrix.addr(i,j) = (i==j) ? 1.0 : 0.0;
         *         }
         *         // access an array, FORTRAN one-based indexing style
         *         array.addr(array.addr(i) = (i%2==0) ? 1.0 : 0.0;
         *     }
         * </pre>
         */
        FORTRAN (1);


        protected final int base;

        private Style(final int base) {
            this.base = base;
        }

//        /**
//         * @return the index access style as a number.
//         */
//        public int base() {
//            return base;
//        }
    }



    /**
     * Builds a Storage of <code>rows</code> by <code>cols</code>
     *
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @throws IllegalArgumentException if parameters are less than zero
     */
    protected Cells(final int rows, final int cols) {
        this(rows, cols, Style.JAVA);
    }

    /**
     * Builds a Storage of <code>rows</code> by <code>cols</code>
     *
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @param style is an {@link Style}
     * @throws IllegalArgumentException if parameters are less than zero
     */
    protected Cells(final int rows, final int cols, final Style style) {
        QL.require(rows>=0 && cols>=0 ,  INVALID_ARGUMENTS); // QA:[RG]::verified
        this.rows = rows;
        this.cols = cols;
        this.size = rows*cols;
        this.style = style;
        this.data = new double[size];
    }


    /**
     * Swaps contents of <code>this</code> Storage by <code>another</code> Storage
     *
     * @param another
     * @return this
     */
    protected Cells swap(final Cells another) {
        QL.require(this.rows==another.rows && this.cols==another.cols && this.size==another.size,  WRONG_BUFFER_LENGTH); // QA:[RG]::verified

        // swaps data
        double [] dtmp;
        dtmp = this.data; this.data = another.data; another.data = dtmp;
        return this;
    }

    //
    // public methods
    //

    public final int rows()    { return rows; }
    public final int columns() { return cols; }

    public final Style style() { return style; }
    public final int base()    { return style.base; }

    public final int size()      { return size; }
    public final boolean empty() { return size <= 0; }

//XXX
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
