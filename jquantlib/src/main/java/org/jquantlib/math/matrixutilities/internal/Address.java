package org.jquantlib.math.matrixutilities.internal;

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
    public int rbase();

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
    public int cbase();

    /**
     * Tells if the underlying memory storage can be accessed in a continuous way.
     * <p>
     * When <code>contiguous</code> is <code>true</code>, certain operations are benefited by bulk opeations.
     * @return
     */
    public boolean contiguous();


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
