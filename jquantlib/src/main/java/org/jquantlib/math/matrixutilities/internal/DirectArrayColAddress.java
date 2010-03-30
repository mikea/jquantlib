package org.jquantlib.math.matrixutilities.internal;

import java.util.EnumSet;
import java.util.Set;


public class DirectArrayColAddress extends DirectAddress implements Address.ArrayAddress {


    //
    // public constructors
    //

    public DirectArrayColAddress(
            final int row0, final int row1,
            final Address chain,
            final int col,
            final Set<Address.Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        super(row0, row1, chain, col, col+1, flags, contiguous, rows, cols);
    }


    //
    // implements ArrayAddress
    //


    @Override
    public ArrayAddress toFortran() {
        return isFortran()
            ? this
            : new DirectArrayColAddress(row0, row1, this.chain, col0, EnumSet.of(Address.Flags.FORTRAN), contiguous, rows, cols);
    }

    @Override
    public ArrayAddress toJava() {
        return isFortran()
            ? new DirectArrayColAddress(row0+1, row1+1, this.chain, col0+1, EnumSet.noneOf(Address.Flags.class), contiguous, rows, cols)
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

//XXX
//    @Override
//    public DirectArrayColAddress clone() {
//        return new DirectArrayColAddress(row0, row1, chain, col0, flags, contiguous, rows, cols);
//    }
    @Override
    public DirectArrayColAddress clone() {
        return (DirectArrayColAddress) super.clone();
    }


    //
    // private inner classes
    //

    private class DirectArrayColAddressOffset extends DirectAddressOffset implements Address.ArrayAddress.ArrayOffset {

        public DirectArrayColAddressOffset(final int row, final int col) {
            super.row = row0+row;
            super.col = col0+col;
        }

        @Override
        public void nextIndex() {
            super.row++;
        }

        @Override
        public void prevIndex() {
            super.row--;
        }

        @Override
        public void setIndex(final int index) {
            super.row = row0+index;
        }

        @Override
        public int op() {
            return row*cols + col;
        }

    }

}
