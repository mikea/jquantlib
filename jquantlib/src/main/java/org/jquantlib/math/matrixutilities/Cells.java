package org.jquantlib.math.matrixutilities;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.jquantlib.QL;
import org.jquantlib.lang.iterators.ConstIterator;
import org.jquantlib.lang.iterators.DoubleListIterator;
import org.jquantlib.lang.iterators.Iterator;
import org.jquantlib.lang.iterators.IteratorAlgebra;
import org.jquantlib.lang.iterators.RandomAccessDouble;

public class Cells {

    //
    // protected final static fields :: error messages
    //

    protected final static String INVALID_ARGUMENTS = "invalid arguments";
    protected final static String WRONG_BUFFER_LENGTH = "wrong buffer length";
    protected final static String MATRIX_IS_INCOMPATIBLE = "matrix is incompatible";
    protected final static String ARRAY_IS_INCOMPATIBLE = "array is incompatible";
    protected final static String ITERATOR_IS_INCOMPATIBLE = "iterator is incompatible";
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



    //
    // Overrides Object
    //

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[rows=").append(rows).append(" cols=").append(cols).append(" style=").append(style.toString()).append('\n');
        int addr = 0;
        for (int row = 0; row < rows; row++) {
            sb.append(" [ ");
            sb.append(data[addr++]);
            for (int col = 1; col < cols; col++) {
                sb.append(", ");
                sb.append(data[addr++]);
            }
            sb.append(" ]\n");
        }
        sb.append("]\n");
        return sb.toString();
    }




    //
    // protected methods
    //

    /**
     * Calculates the address of a given cell identified by <i>(row, col)</i>
     * <p>
     * This method provides transparent access by both JAVA and FORTRAN algorithms.
     *
     * @see Style
     */
    protected int addr(final int row, final int col) {
        return (row-style.base)*cols + (col-style.base);
    }

    /**
     * Calculates the Java index style address (zero-based) of a given cell identified by <i>(row, col)</i>
     * <p>
     * This method provides transparent access by both JAVA and FORTRAN algorithms.
     *
     * @see Style
     */
    protected int addrJ(final int row, final int col) {
        return row*cols+col;
    }

    /**
     * Calculates the address of a given cell identified by <i>col</i> in the first row.
     */
    protected int addr(final int col) {
        return addr(style.base, col);
    }

    /**
     * Calculates the address of a given cell identified by <i>col</i> in the first row.
     */
    protected int addrJ(final int col) {
        return addrJ(0, col);
    }


    //
    // private abstract inner classes
    //

    private abstract class AbstractAlgebra implements RandomAccessDouble, IteratorAlgebra {

        //
        // implements IteratorAlgebra
        //

        @Override
        public IteratorAlgebra addAssign(final double scalar) {
            for (int i=0; i<size; i++) {
                set(i, get(i) + scalar);
            }
            return this;
        }

        @Override
        public IteratorAlgebra subAssign(final double scalar) {
            for (int i=0; i<size; i++) {
                set(i, get(i) - scalar);
            }
            return this;
        }

        @Override
        public IteratorAlgebra mulAssign(final double scalar) {
            for (int i=0; i<size; i++) {
                set(i, get(i) * scalar);
            }
            return this;
        }

        @Override
        public IteratorAlgebra divAssign(final double scalar) {
            for (int i=0; i<size; i++) {
                set(i, get(i) / scalar);
            }
            return this;
        }

        @Override
        public double innerProduct(final IteratorAlgebra another) {
            return innerProduct(another, 0, size);
        }

        @Override
        public double innerProduct(final IteratorAlgebra another, final int from, final int to) {
            QL.require(from >= 0 && to >= from && to <= size, INVALID_ARGUMENTS); // QA:[RG]::verified
            double sum = 0.0;
            for (int i=from; i<to; i++)
                sum += this.get(i) * another.get(i);
            return sum;
        }

        @Override
        public int lowerBound(final double val) {
            return lowerBound(0, size-1, val);
        }

        @Override
        public int lowerBound(int from, final int to, final double val) {
            QL.require(from >= 0 && to >= from && to < size, INVALID_ARGUMENTS); // QA:[RG]::verified
            int len = to - from;
            int half;
            int middle;

            while (len > 0) {
                half = len >> 1;
                middle = from;
                middle = middle + half;

                if (this.get(middle) < val) {
                    from = middle;
                    from++;
                    len = len - half - 1;
                } else
                    len = half;
            }
            return from;
        }

        @Override
        public int upperBound(final double val) {
            return upperBound(0, size-1, val);
        }

        @Override
        public int upperBound(int from, final int to, final double val) {
            QL.require(from >= 0 && to >= from && to < size, INVALID_ARGUMENTS); // QA:[RG]::verified
            int len = to - from;
            int half;
            int middle;

            while (len > 0) {
                half = len >> 1;
                middle = from;
                middle = middle + half;

                if (val < this.get(middle))
                    len = half;
                else {
                    from = middle;
                    from++;
                    len = len - half - 1;
                }
            }
            return from;
        }

    }


    private abstract class AbstractRowIterator extends AbstractAlgebra implements DoubleListIterator {

        protected final int row;
        protected final int col0;
        protected final int col1;
        protected final int size;

        protected int cursor;

        public AbstractRowIterator(final int row) {
            this(row, style.base, cols+style.base);
        }

        public AbstractRowIterator(final int row, final int col0) {
            this(row, col0, cols+style.base);
        }

        public AbstractRowIterator(final int row, final int col0, final int col1) {
            QL.require(row>=style.base && row<rows+style.base && col0 >=style.base && col1>=col0 && col1 <= cols+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
            this.row = row;
            this.col0 = col0;
            this.col1 = col1;
            this.size = col1-col0;
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
                return data[addr(row, cursor++)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double previousDouble() {
            if (cursor > col0) {
                return data[addr(row, --cursor)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void set(final double e) {
            if (cursor >=col0 && cursor < col1) {
                data[addr(row, cursor)] = e;
            } else {
                throw new NoSuchElementException();
            }
        }


        //
        // implements RandomAcessDouble
        //

        @Override
        public double first() {
            return data[addr(row, col0)];
        }

        @Override
        public double last() {
            return data[addr(row, col1-1)];
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public double get(final int offset) {
            return data[addr(row, offset-col0)];
        }

        @Override
        public void set(final int offset, final double value) {
            data[addr(row, offset-col0)] = value;
        }

    }


    private abstract class AbstractColumnIterator extends AbstractAlgebra implements DoubleListIterator {

        protected final int row0;
        protected final int row1;
        protected final int col;
        protected final int size;

        protected int cursor;

        /**
         * Creates a ColumnIterator for the entire column <code>col</code>
         *
         * @param col is the desired column
         * @return an Array obtained from row A( [:] , col )
         * @throws IllegalArgumentException when indices are out of range
         */
        public AbstractColumnIterator(final int col) {
            this(col, style.base, cols+style.base);
        }

        /**
         * Creates a ColumnIterator for column <code>col</code>
         *
         * @param col is the desired column
         * @param row0 is the initial row, inclusive
         * @return an Array obtained from row A( [row0:] , col )
         * @throws IllegalArgumentException when indices are out of range
         */
        public AbstractColumnIterator(final int col, final int row0) {
            this(col, row0, cols+style.base);
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
        public AbstractColumnIterator(final int col, final int row0, final int row1) {
            QL.require(col>=style.base && col<cols+style.base && row0 >=style.base && row1>=row0 && row1 <= rows+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
            this.col = col;
            this.row0 = row0;
            this.row1 = row1;
            this.size = row1-row0;
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
                return data[addr(cursor++, col)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double previousDouble() {
            if (cursor > row0) {
                return data[addr(--cursor, col)];
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


        //
        // implements RandomAcessDouble
        //

        @Override
        public double first() {
            return data[addr(row0, col)];
        }

        @Override
        public double last() {
            return data[addr(row1-1, col)];
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public double get(final int offset) {
            return data[addr(offset-row0, col)];
        }

        @Override
        public void set(final int offset, final double value) {
            data[addr(offset-row0, col)] = value;
        }

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
    public class RowIterator extends AbstractRowIterator implements Iterator, Cloneable {

        /**
         * Creates a RowIterator for the entire row <code>row</code>
         *
         * @param row is the desired row
         * @return an Array obtained from row A( row , [:] )
         * @throws IllegalArgumentException when indices are out of range
         */
        public RowIterator(final int row) {
            super(row);
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
            super(row, col0);
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
            super(row, col0, col1);
        }


        //
        // implements Cloneable
        //

        /**
         * Clones <code>this</code> RowIterator, keeping its current positioning unchanged.
         */
        @Override
        public Object clone() {
            final RowIterator clone = new RowIterator(row, col0, col1);
            clone.cursor = this.cursor;
            return clone;
        }


        //
        // implements Iterator
        //

        /**
         * Builds a new RowIterator, resetting its positioning.
         */
        @Override
        public Iterator iterator() {
            final RowIterator it = new RowIterator(row, col0, col1);
            it.cursor = it.col0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at a specific <code>offset</code>
         *
         * @param offset determines the first element
         */
        @Override
        public Iterator iterator(final int offset) {
            final RowIterator it = new RowIterator(row, col0+offset, col1);
            it.cursor = it.col0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at a specific <code>offset</code> and contains up to <code>size</code> elements.
         *
         * @param offset determines the first element
         * @param size determines how many elements are included
         */
        @Override
        public Iterator iterator(final int offset, final int size) {
            final RowIterator it = new RowIterator(row, col0+offset, col0+size);
            it.cursor = it.col0;
            return it;
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
    public class ConstRowIterator extends AbstractRowIterator implements ConstIterator, Cloneable {

        /**
         * Creates a RowIterator for the entire row <code>row</code>
         *
         * @param row is the desired row
         * @return an Array obtained from row A( row , [:] )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ConstRowIterator(final int row) {
            super(row);
        }

        /**
         * Creates a RowIterator for row <code>row</code>
         *
         * @param row is the desired row
         * @param col0 is the initial column, inclusive
         * @return an Array obtained from row A( row , [col0:] )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ConstRowIterator(final int row, final int col0) {
            super(row, col0);
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
        public ConstRowIterator(final int row, final int col0, final int col1) {
            super(row, col0, col1);
        }


        //
        // implements Cloneable
        //

        /**
         * Clones <code>this</code> RowIterator, keeping its current positioning unchanged.
         */
        @Override
        public Object clone() {
            final RowIterator clone = new RowIterator(row, col0, col1);
            clone.cursor = this.cursor;
            return clone;
        }


        //
        // implements Iterator
        //

        /**
         * Builds a new RowIterator, resetting its positioning.
         */
        @Override
        public ConstIterator iterator() {
            final ConstRowIterator it = new ConstRowIterator(row, col0, col1);
            it.cursor = it.col0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at a specific <code>offset</code>
         *
         * @param offset determines the first element
         */
        @Override
        public ConstIterator iterator(final int offset) {
            final ConstRowIterator it = new ConstRowIterator(row, col0+offset, col1);
            it.cursor = it.col0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at a specific <code>offset</code> and contains up to <code>size</code> elements.
         *
         * @param offset determines the first element
         * @param size determines how many elements are included
         */
        @Override
        public ConstIterator iterator(final int offset, final int size) {
            final ConstRowIterator it = new ConstRowIterator(row, col0+offset, col0+size);
            it.cursor = it.col0;
            return it;
        }


        //
        // Overrides AbstractRowIterator
        //

        @Override
        public void set(final double e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final int pos, final double value) {
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
    public class ColumnIterator extends AbstractColumnIterator implements Iterator, Cloneable {


        /**
         * Creates a ColumnIterator for the entire column <code>col</code>
         *
         * @param col is the desired column
         * @return an Array obtained from row A( [:] , col )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ColumnIterator(final int col) {
            super(col);
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
            super(col, row0);
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
            super(col, row0, row1);
        }


        //
        // implements Cloneable
        //

        /**
         * Clones <code>this</code> RowIterator, keeping its current positioning unchanged.
         */
        @Override
        public Object clone() {
            final ColumnIterator clone = new ColumnIterator(col, row0, row1);
            clone.cursor = this.cursor;
            return clone;
        }


        //
        // implements Iterator
        //

        /**
         * Builds a new RowIterator, resetting its positioning.
         */
        @Override
        public Iterator iterator() {
            final ColumnIterator it = new ColumnIterator(col, row0, row1);
            it.cursor = it.row0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at a specific <code>offset</code>
         *
         * @param offset determines the first element
         */
        @Override
        public Iterator iterator(final int offset) {
            final ColumnIterator it = new ColumnIterator(col, row0+offset, row1);
            it.cursor = it.row0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at a specific <code>offset</code> and contains up to <code>size</code> elements.
         *
         * @param offset determines the first element
         * @param size determines how many elements are included
         */
        @Override
        public Iterator iterator(final int offset, final int size) {
            final ColumnIterator it = new ColumnIterator(col, row0+offset, row0+size);
            it.cursor = it.row0;
            return it;
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
    public class ConstColumnIterator extends AbstractColumnIterator implements ConstIterator, Cloneable {

        /**
         * Creates a constant, non-modifiable ConstColumnIterator for an entire column <code>col</code>
         *
         * @param col is the desired col
         */
        public ConstColumnIterator(final int col) {
            super(col);
        }

        /**
         * Creates a constant, non-modifiable ConstColumnIterator for column <code>col</code>
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
         * Creates a constant, non-modifiable ConstColumnIterator for column <code>col</code>
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


        //
        // Overrides AbstractColumnIterator
        //

        @Override
        public void set(final double e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final int pos, final double value) {
            throw new UnsupportedOperationException();
        }

        //
        // implements Iterator
        //

        /**
         * Builds a new RowIterator, resetting its positioning.
         */
        @Override
        public ConstIterator iterator() {
            final ConstColumnIterator it = new ConstColumnIterator(col, row0, row1);
            it.cursor = it.row0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at a specific <code>offset</code>
         *
         * @param offset determines the first element
         */
        @Override
        public ConstIterator iterator(final int offset) {
            final ConstColumnIterator it = new ConstColumnIterator(col, row0+offset, row1);
            it.cursor = it.row0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at a specific <code>offset</code> and contains up to <code>size</code> elements.
         *
         * @param offset determines the first element
         * @param size determines how many elements are included
         */
        @Override
        public ConstIterator iterator(final int offset, final int size) {
            final ConstColumnIterator it = new ConstColumnIterator(col, row0+offset, row0+size);
            it.cursor = it.row0;
            return it;
        }

    }

}
