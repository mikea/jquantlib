package org.jquantlib.math.matrixutilities.internal;

import java.util.Set;

public interface Address {

    public static final String INVALID_BACKWARD_INDEXING = "invalid backward indexing";
    public static final String INVALID_ROW_INDEX = "invalid row index";
    public static final String INVALID_COLUMN_INDEX = "invalid column index";
    public static final String GAP_INDEX_FOUND = "gap index found";


    /**
     * @return the number of rows mapped
     */
    public int rows();

    /**
     * @return the number of columns mapped
     */
    public int cols();

    /**
     * @return the base address (inclusive)
     */
    public int base();

    /**
     * @return the last address (exclusive)
     */
    public int last();

    /**
     * <code>rbase</code> is the row offset of the upmost row of a Matrix.
     * <p>
     * So, imagine that you create matrices A and B like shown below, where B is a submatrix of A:
     * <pre>
     *     Matrix A = new Matrix(5, 8);
     *     int row0 = 1; int row1 = 4;
     *     int col0 = 5; int col1 = 8;
     *     Matrix B = A.range(row0, row1, col0, col1);
     * </pre>
     * <p>
     * where <code>row0</code> determines rbase.
     *
     * @return
     */
    public int row0();

    /**
     * <code>cbase</code> is the row offset of the leftmost column of a Matrix.
     * <p>
     * So, imagine that you create matrices A and B like shown below, where B is a submatrix of A:
     * <pre>
     *     Matrix A = new Matrix(5, 8);
     *     int row0 = 1; int row1 = 4;
     *     int col0 = 5; int col1 = 8;
     *     Matrix B = A.range(row0, row1, col0, col1);
     * </pre>
     * <p>
     * where <code>col0</code> determines cbase.
     *
     * @return
     */
    public int col0();

    /**
     * This is a convenience method intended to return {@link Flags#CONTIGUOUS}
     *
     * @return Flags#CONTIGUOUS
     */
    public boolean contiguous();

    /**
     * This is a convenience method intended to return {@link Flags#FORTRAN}
     *
     * @return Flags#FORTRAN
     */
    public boolean fortran();

    /**
     * @return a set of flags in effect on this {@link Address} object.
     */
    public Set<Address.Flags> flags();


    //
    // public inner enumerations
    //

    public enum Flags {

        /**
         * Tells if the underlying memory storage can be accessed in a continuous way.
         * <p>
         * When <code>contiguous</code> is <code>true</code>, certain operations are benefited by bulk opeations.
         */
        CONTIGUOUS,

        /**
         * Tells if this {@link Address} is intended to Fortran 1-based indexing.
         * <p>
         * In FORTRAN language, access to vectors and matrices are 1-based, like this:
         * <pre>
         *     for (i = 1; i <= n; i++)
         * </pre>
         * rather than what you can see in Java, C, C++, etc:
         * <pre>
         *     for (i = 0; i < n; i++)
         * </pre>
         */
        FORTRAN,

        /**
         * Tells if rows and columns must be transposed.
         * <p>
         * This feature is particularly important in 2 situations:
         * <li>Implement Matrix transposition in constant time by simply changing the address mapping as opposed
         * to performing the actual transposition of all elements of it;</li>
         * <li>Increase performance when a Matrix is mostly used as a set of column arrays. As columns are mapped
         * internally as rows, the processor will show better performance due to memory caching of adjacent elements</li>
         * <p>
         * <b>NOT IMPLEMENTED YET</b>
         */
        TRANSPOSE //TODO: to be implemented
    }


    //
    // public inner interfaces
    //

    public interface Offset {
        public abstract int op();
    }

    public interface ArrayAddress extends Cloneable, Address {

        public ArrayAddress clone();

        public int op(int index);

        public ArrayOffset offset();
        public ArrayOffset offset(int index);

        public interface ArrayOffset extends Offset {
            public void setIndex(final int index);
            public void nextIndex();
            public void prevIndex();
        }
    }

    public interface MatrixAddress extends Cloneable, Address {

        public MatrixAddress clone();

        public int op(int row, int col);

        public MatrixOffset offset();
        public MatrixOffset offset(final int row, final int col);

        public interface MatrixOffset extends Offset {
            public void setRow(final int row);
            public void setCol(final int col);
            public void nextRow();
            public void prevRow();
            public void nextCol();
            public void prevCol();
        }

    }

}
