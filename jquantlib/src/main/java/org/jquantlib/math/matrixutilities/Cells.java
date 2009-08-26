package org.jquantlib.math.matrixutilities;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.joda.primitives.listiterator.DoubleListIterator;
import org.jquantlib.QL;
import org.jquantlib.lang.iterators.ConstIterator;
import org.jquantlib.lang.iterators.Iterator;
import org.jquantlib.math.Ops.BinaryDoubleOp;
import org.jquantlib.math.Ops.DoubleOp;
import org.jquantlib.math.functions.Abs;
import org.jquantlib.math.functions.Exp;
import org.jquantlib.math.functions.Log;
import org.jquantlib.math.functions.Minus;
import org.jquantlib.math.functions.Sqr;
import org.jquantlib.math.functions.Sqrt;

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

    // private abstract class AbstractIterator implements Iterator<AbstractIterator>, RandomAccessDouble {
    private abstract class AbstractIterator implements Iterator {

        protected final int dim;
        protected final int pos0;
        protected final int pos1;
        protected final int size;

        protected int cursor;

        protected AbstractIterator(final int dim, final int pos0, final int pos1, final int cursor) {
            this.dim = dim;
            this.pos0 = pos0;
            this.pos1 = pos1;
            this.size = pos1-pos0;
            this.cursor = cursor;
        }

        //
        // abstract methods
        //

        @Override
        protected abstract Object clone();


        //
        // implements ListIterator
        //

        @Override
        public void add(final Double e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return cursor < pos1;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > pos0;
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

        @Override
        public Iterator copy(final Iterator another) {
            QL.require(this.size() == another.size(), ITERATOR_IS_INCOMPATIBLE);
            this.begin(); another.begin();
            while (another.hasNext()) {
                final double d = another.nextDouble();
                this.set(d);
                this.forward();
            }
            return this;
        }


        //
        // implements RandomListIterator
        //

        @Override
        public int size() {
            return size;
        }

        @Override
        public int cursor() {
            return cursor;
        }

        @Override
        public void seek(final int pos) {
            if (pos-pos0 >= 0 && pos-pos0 < size) {
                cursor = pos0+pos-style.base;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void begin() {
            cursor = pos0;
        }

        @Override
        public void end() {
            cursor = pos1;
        }


        @Override
        public void forward() {
            if (cursor < pos1) {
                cursor++;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void backward() {
            if (cursor >= pos0) {
                --cursor;
            } else {
                throw new NoSuchElementException();
            }
        }


        //
        // implements DoubleListIterator
        //

        @Override
        public void addDouble(final double e) {
            throw new UnsupportedOperationException();
        }



        //
        // implements Algebra<Iterator>
        //

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         */
        @Override
        public Iterator addAssign(final double scalar) {
            begin();
            while (hasNext()) {
                set(nextDouble() + scalar);
            }
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         */
        @Override
        public Iterator addAssign(final Iterator another) {
            QL.require(this.size==another.size(), ITERATOR_IS_INCOMPATIBLE);
            begin();
            another.begin();
            while (hasNext()) {
                set(nextDouble() + another.nextDouble());
            }
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         */
        @Override
        public Iterator subAssign(final double scalar) {
            begin();
            while (hasNext()) {
                set(nextDouble() - scalar);
            }
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         */
        @Override
        public Iterator subAssign(final Iterator another) {
            QL.require(this.size==another.size(), ITERATOR_IS_INCOMPATIBLE);
            begin();
            another.begin();
            while (hasNext()) {
                set(nextDouble() - another.nextDouble());
            }
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         */
        @Override
        public Iterator mulAssign(final double scalar) {
            begin();
            while (hasNext()) {
                set(nextDouble() * scalar);
            }
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         */
        @Override
        public Iterator mulAssign(final Iterator another) {
            QL.require(this.size==another.size(), ITERATOR_IS_INCOMPATIBLE);
            begin();
            another.begin();
            while (hasNext()) {
                set(nextDouble() * another.nextDouble());
            }
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         */
        @Override
        public Iterator divAssign(final double scalar) {
            begin();
            while (hasNext()) {
                set(nextDouble() / scalar);
            }
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         */
        @Override
        public Iterator divAssign(final Iterator another) {
            QL.require(this.size==another.size(), ITERATOR_IS_INCOMPATIBLE);
            begin();
            another.begin();
            while (hasNext()) {
                set(nextDouble() / another.nextDouble());
            }
            return this;
        }


        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator add(final double scalar) {
            final Iterator clone = (Iterator) this.clone();
            return clone.addAssign(scalar);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator add(final Iterator another) {
            final Iterator clone = (Iterator) this.clone();
            return clone.addAssign(another);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator sub(final double scalar) {
            final Iterator clone = (Iterator) this.clone();
            return clone.subAssign(scalar);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator sub(final Iterator another) {
            final Iterator clone = (Iterator) this.clone();
            return clone.subAssign(another);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator mul(final double scalar) {
            final Iterator clone = (Iterator) this.clone();
            return clone.mulAssign(scalar);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator mul(final Iterator another) {
            final Iterator clone = (Iterator) this.clone();
            return clone.mulAssign(another);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator mul(final Matrix matrix) {
            QL.require(this.size == matrix.rows, MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
            begin();
            final Array result = new Array(size());
            for (int i=0; hasNext(); i++) {
                double sum = 0.0;
                final double value = next();
                int addrJ = matrix.addrJ(i, 0);
                for (int col=0; col<matrix.cols; col++) {
                    sum += value * matrix.data[addrJ];
                    addrJ++;
                }
                result.data[i] = sum;
            }
            return result.constIterator();
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator div(final double scalar) {
            final Iterator clone = (Iterator) this.clone();
            return clone.divAssign(scalar);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator div(final Iterator another) {
            final Iterator clone = (Iterator) this.clone();
            return clone.divAssign(another);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator negative() {
            final Iterator clone = (Iterator) this.clone();
            return clone.mulAssign(-1);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double dotProduct(final Iterator another) {
            QL.require(size() == another.size(), ITERATOR_IS_INCOMPATIBLE);
            this.begin(); another.begin();
            double sum = 0.0;
            while (this.hasNext()) {
                sum += this.nextDouble() * another.nextDouble();
            }
            return sum;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double dotProduct(final Iterator another, final int from, final int to) {
            return dotProduct(another.iterator(from, to));
        }


        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double innerProduct(final Iterator another) {
            // when working with real numbers, both dotProduct and innerProduct give the same results
            return dotProduct(another);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double innerProduct(final Iterator another, final int from, final int to) {
            // when working with real numbers, both dotProduct and innerProduct give the same results
            return dotProduct(another, from, to);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Matrix outerProduct(final Iterator another) {
            final Matrix m = new Matrix(this.size, another.size(), style());
            // perform calculations
            this.begin(); another.begin();
            int row = style.base; int col = style.base;
            while (hasNext()) {
                final double v = nextDouble();
                another.begin();
                while (hasNext()) {
                    m.set(row, col, v*another.nextDouble());
                    col++;
                }
                row++;
            }
            return m;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Matrix outerProduct(final Iterator another, final int from, final int to) {
            QL.require(from >= style.base && to >= from && to <= size+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
            return outerProduct(another.iterator(from, to));
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator adjacentDifference() {
            return adjacentDifference(new Minus());
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator adjacentDifference(final int from, final int to) {
            return this.iterator(from, to).adjacentDifference();
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator adjacentDifference(final BinaryDoubleOp f) {
            begin();
            final Array diff = new Array(size);
            double prev, curr;
            int addr = 0;
            if (hasNext()) {
                prev = nextDouble();
                diff.data[addr++] = prev;
                while (hasNext() && size > 0) {
                    curr = nextDouble();
                    diff.data[addr++] = f.op(curr, prev);
                    prev = curr;
                }
            }
            return diff.constIterator();
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator adjacentDifference(final int from, final int to, final BinaryDoubleOp f) {
            return this.iterator(from, to).adjacentDifference(f);
        }


        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double min() {
            begin();
            double result = Double.MAX_VALUE;
            while (hasNext()) {
                final double d = nextDouble();
                if (d < result) result = d;
            }
            return result;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double min(final int from, final int to) {
            return this.iterator(from, to).min();
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double max() {
            begin();
            double result = Double.MIN_VALUE;
            while (hasNext()) {
                final double d = nextDouble();
                if (d > result) result = d;
            }
            return result;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double max(final int from, final int to) {
            return this.iterator(from, to).max();
        }


        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator abs() {
            return transform(new Abs());
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator exp() {
            return transform(new Exp());
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator log() {
            return transform(new Log());
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator sqr() {
            return transform(new Sqr());
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator sqrt() {
            return transform(new Sqrt());
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double accumulate() {
            return accumulate(0);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double accumulate(final double init) {
            begin();
            double sum = init;
            while (hasNext()) {
                sum += nextDouble();
            }
            return sum;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public double accumulate(final int from, final int to, final double init) {
            return this.iterator(from, to).accumulate(init);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator transform(final DoubleOp func) {
            begin();
            while (hasNext()) {
                set(func.op(nextDouble()));
            }
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public Iterator transform(final int from, final int to, final DoubleOp f) {
            return this.iterator(from, to).transform(f);
        }

        @Override
        public Iterator fill(final double scalar) {
            Arrays.fill(data, pos0-style.base, pos1-style.base, scalar);
            return this;
        }

        @Override
        public Iterator sort() {
            Arrays.sort(data, pos0-style.base, pos1-style.base);
            return this;
        }

        @Override
        public Iterator swap(final Iterator another) {
            QL.require(this.size()==another.size(),  WRONG_BUFFER_LENGTH); // QA:[RG]::verified
            while (hasNext()) {
                final double tmp = nextDouble();
                this.set(another.nextDouble());
                another.set(tmp);
            }
            return this;
        }


        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public int lowerBound(final double val) {
            return lowerBound(style.base, size+style.base-1, val);
        }

        /**
         * {@inheritDoc}
         *
         * @Note This method affects cursor position of {@link Iterator}(s) in unpredictable ways
         *
         * @return a new {@link Iterator} instance, positioned in the beginning
         */
        @Override
        public int lowerBound(int from, final int to, final double val) {
            QL.require(from >= style.base && to >= from && to < size+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
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
            return upperBound(style.base, size+style.base-1, val);
        }

        @Override
        public int upperBound(int from, final int to, final double val) {
            QL.require(from >= style.base && to >= from && to < size+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
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


    private abstract class AbstractRowIterator extends AbstractIterator {

        public AbstractRowIterator(final int row) {
            this(row, style.base, cols+style.base);
        }

        public AbstractRowIterator(final int row, final int col0) {
            this(row, col0, cols+style.base);
        }

        public AbstractRowIterator(final int row, final int col0, final int col1) {
            super(row, col0, col1, col0);
            QL.require(row>=style.base && row<rows+style.base && col0 >=style.base && col1>=col0 && col1 <= cols+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        }


        //
        // overrides Object
        //

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append(this.getClass().getSimpleName()).append('\n');;
            sb.append(" dimensions=").append('{').append(dim).append(",[").append(pos0).append(':').append(pos1).append(")}");
            sb.append(" size=").append(size);
            sb.append(" style=").append(style).append('\n');
            sb.append(" cursor position=").append(cursor-pos0).append(" value=").append(data[addr(dim, cursor)]).append('\n');
            int addr = addr(dim, pos0);
            sb.append(' ').append(data[addr]);
            for (int i = 1; i < size; i++) {
                sb.append(", ");
                addr++;
                sb.append(data[addr]);
            }
            sb.append('\n');
            return sb.toString();
        }


        //
        // implements RandomListIterator
        //

        @Override
        public double first() {
            return data[addr(dim, pos0)];
        }

        @Override
        public double last() {
            return data[addr(dim, pos1-1)];
        }

        @Override
        public double nextDouble() {
            if (cursor < pos1) {
                return data[addr(dim, cursor++)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double previousDouble() {
            if (cursor > pos0) {
                return data[addr(dim, --cursor)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void set(final double e) {
            if (cursor >=pos0 && cursor < pos1) {
                data[addr(dim, cursor)] = e;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double get(final int offset) {
            return data[addr(dim, offset-pos0)];
        }

        @Override
        public void set(final int offset, final double value) {
            data[addr(dim, offset-pos0)] = value;
        }


        //
        // implements Iterator
        //

        /**
         * Builds a new RowIterator, resetting its positioning.
         */
        @Override
        public Iterator iterator() {
            final RowIterator it = new RowIterator(dim, pos0, pos1);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at a specific element <code>elem0</code>
         *
         * @param elem0 determines the element, inclusive
         */
        @Override
        public Iterator iterator(final int elem0) {
            final RowIterator it = new RowIterator(dim, pos0+elem0, pos1+size-pos0);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new RowIterator which starts at element <code>elem0</code> inclusive and goes thru <code>elem1</code> exlusive.
         *
         * @param elem0 determines the element, inclusive
         * @param elem1 determines the last element, exclusive
         */
        @Override
        public Iterator iterator(final int elem0, final int elem1) {
            final RowIterator it = new RowIterator(dim, pos0+elem0, pos0+elem1);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new constant, non-modifiable ConstRowIterator, resetting its positioning.
         */
        @Override
        public ConstIterator constIterator() {
            final ConstRowIterator it = new ConstRowIterator(dim, pos0, pos1);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new constant, non-modifiable ConstRowIterator which starts at a specific element <code>elem0</code>
         *
         * @param elem0 determines the element, inclusive
         */

        @Override
        public ConstIterator constIterator(final int elem0) {
            final ConstRowIterator it = new ConstRowIterator(dim, pos0+elem0, pos1+size-pos0);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new constant, non-modifiable ConstRowIterator which starts at a specific element <code>elem0</code> inclusive
         * and goes thru <code>elem1</code> exlusive.
         *
         * @param elem0 determines the element, inclusive
         * @param elem1 determines the last element, exclusive
         */
        @Override
        public ConstIterator constIterator(final int elem0, final int elem1) {
            final ConstRowIterator it = new ConstRowIterator(dim, pos0+elem0, pos0+elem1);
            it.cursor = it.pos0;
            return it;
        }

    }


    private abstract class AbstractColumnIterator extends AbstractIterator {

        public AbstractColumnIterator(final int col) {
            this(col, style.base, rows+style.base);
        }

        public AbstractColumnIterator(final int col, final int row0) {
            this(col, row0, rows+style.base);
        }

        public AbstractColumnIterator(final int col, final int row0, final int row1) {
            super(col, row0, row1, row0);
            QL.require(col>=style.base && col<cols+style.base && row0 >=style.base && row1>=row0 && row1 <= rows+style.base, INVALID_ARGUMENTS); // QA:[RG]::verified
        }


        //
        // overrides Object
        //

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append(this.getClass().getSimpleName()).append('\n');
            sb.append(" dimensions=").append("{[").append(pos0).append(":").append(pos1).append("),").append(dim).append('}');
            sb.append(" size=").append(size);
            sb.append(" style=").append(style).append('\n');
            sb.append(" cursor position=").append(cursor-pos0).append(" value=").append(data[addr(cursor, dim)]).append('\n');
            int addr = addr(pos0, dim);
            sb.append(' ').append(data[addr]);
            for (int i = 1; i < size; i++) {
                sb.append(", ");
                addr += cols;
                sb.append(data[addr]);
            }
            sb.append('\n');
            return sb.toString();
        }


        //
        // implements RandomListIterator
        //

        @Override
        public double nextDouble() {
            if (cursor < pos1) {
                return data[addr(cursor++, dim)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double previousDouble() {
            if (cursor > pos0) {
                return data[addr(--cursor, dim)];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void set(final double e) {
            if (cursor >=pos0 && cursor < pos1) {
                data[addr(cursor, dim)] = e;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double first() {
            return data[addr(pos0, dim)];
        }

        @Override
        public double last() {
            return data[addr(pos1-1, dim)];
        }

        @Override
        public double get(final int offset) {
            return data[addr(offset-pos0, dim)];
        }

        @Override
        public void set(final int offset, final double value) {
            data[addr(offset-pos0, dim)] = value;
        }


        //
        // implements Iterator
        //

        /**
         * Builds a new ColumnIterator, resetting its positioning.
         */
        @Override
        public Iterator iterator() {
            final ColumnIterator it = new ColumnIterator(dim, pos0, pos1);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new ColumnIterator which starts at a specific element <code>elem0</code>
         *
         * @param elem0 determines the element, inclusive
         */
        @Override
        public Iterator iterator(final int elem0) {
            final ColumnIterator it = new ColumnIterator(dim, pos0+elem0, pos1+size-pos0);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new ColumnIterator which starts at a specific element <code>elem0</code> inclusive and
         * goes thru <code>elem1</code> exlusive.
         *
         * @param elem0 determines the element, inclusive
         * @param elem1 determines the last element, exclusive
         */
        @Override
        public Iterator iterator(final int elem0, final int elem1) {
            final ColumnIterator it = new ColumnIterator(dim, pos0+elem0, pos0+elem1);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new constant, non-modifiable ConstColumnIterator, resetting its positioning.
         */
        @Override
        public ConstIterator constIterator() {
            final ConstColumnIterator it = new ConstColumnIterator(dim, pos0, pos1);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new constant, non-modifiable ConstColumnIterator which starts at a specific element <code>elem0</code>
         *
         * @param elem0 determines the element, inclusive
         */
        @Override
        public ConstIterator constIterator(final int elem0) {
            final ConstColumnIterator it = new ConstColumnIterator(dim, pos0+elem0, pos1+size-pos0);
            it.cursor = it.pos0;
            return it;
        }

        /**
         * Builds a new constant, non-modifiable ConstColumnIterator which starts at a specific element <code>elem0</code> and
         * goes thru <code>elem1</code> exclusive.
         *
         * @param elem0 determines the element, inclusive
         * @param elem1 determines the last element, exclusive
         */
        @Override
        public ConstIterator constIterator(final int elem0, final int elem1) {
            final ConstColumnIterator it = new ConstColumnIterator(dim, pos0+elem0, pos0+elem1);
            it.cursor = it.pos0;
            return it;
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
    public class RowIterator extends AbstractRowIterator implements Cloneable {

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
            final RowIterator clone = new RowIterator(dim, pos0, pos1);
            clone.cursor = this.cursor;
            return clone;
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
         * Creates a constant, non-modifiable ConstRowIterator for the entire row <code>row</code>
         *
         * @param row is the desired row
         * @return an Array obtained from row A( row , [:] )
         * @throws IllegalArgumentException when indices are out of range
         */
        public ConstRowIterator(final int row) {
            super(row);
        }

        /**
         * Creates a constant, non-modifiable ConstRowIterator for row <code>row</code>
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
         * Creates a constant, non-modifiable ConstRowIterator for row <code>row</code>
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
            final RowIterator clone = new RowIterator(dim, pos0, pos1);
            clone.cursor = this.cursor;
            return clone;
        }


        //
        // implement RandomListIterator
        //

        /**
         * Copy elements over <code>this</code> Iterator
         *
         * @param another is another iterator
         */
        @Override
        public Iterator copy(final Iterator another) {
            throw new UnsupportedOperationException();
        }


        //
        // overrides AbstractRowIterator
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
    public class ColumnIterator extends AbstractColumnIterator implements Cloneable {

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
            final ColumnIterator clone = new ColumnIterator(dim, pos0, pos1);
            clone.cursor = this.cursor;
            return clone;
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
         * @param elem0 determines the element, inclusive
         * @param elem1 determines the last element, exclusive
         */
        public ConstColumnIterator(final int col, final int row0) {
            super(col, row0);
        }

        /**
         * Creates a constant, non-modifiable ConstColumnIterator for column <code>col</code>
         *
         * @param elem0 determines the element, inclusive
         * @param elem1 determines the last element, exclusive
         */
        public ConstColumnIterator(final int col, final int row0, final int row1) {
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
            final ColumnIterator clone = new ColumnIterator(dim, pos0, pos1);
            clone.cursor = this.cursor;
            return clone;
        }


        //
        // implement RandomListIterator
        //

        /**
         * Copy elements over <code>this</code> Iterator
         *
         * @param another is another iterator
         */
        @Override
        public Iterator copy(final Iterator another) {
            throw new UnsupportedOperationException();
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

    }

}
