package org.jquantlib.math.matrixutilities.internal;

import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.jquantlib.lang.exceptions.LibraryException;


public class DirectArrayColAddress extends DirectAddress implements Address.ArrayAddress {


    //
    // public constructors
    //

    public DirectArrayColAddress(
            final double[] data,
            final int row0, final int row1,
            final Address chain,
            final int col,
            final Set<Address.Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        super(data, row0, row1, chain, col, col+1, flags, contiguous, rows, cols);
    }


    //
    // implements ArrayAddress
    //


    @Override
    public ArrayAddress toFortran() {
        return isFortran()
            ? this
            : new DirectArrayColAddress(data, row0, row1, this.chain, col0, EnumSet.of(Address.Flags.FORTRAN), contiguous, rows, cols);
    }

    @Override
    public ArrayAddress toJava() {
        return isFortran()
            ? new DirectArrayColAddress(data, row0+1, row1+1, this.chain, col0+1, EnumSet.noneOf(Address.Flags.class), contiguous, rows, cols)
            : this;
    }

    @Override
    public ArrayOffset offset() {
        return new DirectArrayColAddressOffset(offset, offset);
    }

    @Override
    public ArrayOffset offset(final int index) {
        return new DirectArrayColAddressOffset(index, offset);
    }

    @Override
    public int op(final int index) {
        return (row0+index)*cols + (col0+offset);
    }


    //
    // implements Cloneable
    //

    @Override
    public DirectArrayColAddress clone() {
        try {
            return (DirectArrayColAddress) super.clone();
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
    }


    //
    // private inner classes
    //

    private class DirectArrayColAddressOffset extends DirectAddressOffset implements Address.ArrayAddress.ArrayOffset {

        public DirectArrayColAddressOffset(final int row, final int col) {
            super.row = row0+row;
            super.col = col0+col;
        }

        //
        // implements Offset
        //

        @Override
        public int op() {
            return row*cols + col;
        }

        //
        // implements ArrayOffset
        //

        @Override
        public void setIndex(final int index) {
            super.row = row0+index;
        }


        //
        // implements ListIterator
        //

        @Override
        public void add(final Double e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            return super.row == rows ? rows : super.row++;
        }

        @Override
        public int previousIndex() {
            return super.row == -1 ? -1 : super.row--;
        }

        @Override
        public boolean hasNext() {
            return super.row < row1;
        }

        @Override
        public boolean hasPrevious() {
            return super.row > -1;
        }

        @Override
        public Double next() {
            final int idx = op();
            nextIndex();
            if (idx>=row1) throw new NoSuchElementException();
            return data[idx];
        }

        @Override
        public Double previous() {
            final int idx = previousIndex();
            if (idx==-1) throw new NoSuchElementException();
            return data[op()];
        }

        @Override
        public void set(final Double e) {
            final int idx = op();
            if ((idx==-1)||(idx==rows)) throw new IllegalStateException();
            data[idx] = e;
        }

    }

}
