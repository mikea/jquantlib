package org.jquantlib.math.matrixutilities.internal;

import java.util.EnumSet;
import java.util.Set;

public class DirectArrayRowAddress extends DirectAddress implements Address.ArrayAddress {

    public DirectArrayRowAddress(
            final int row,
            final Address chain,
            final int col0, final int col1,
            final Set<Address.Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        super(row, row+1, chain, col0, col1, flags, contiguous, rows, cols);
    }


    //
    // implements ArrayAddress
    //

    @Override
    public ArrayAddress toFortran() {
        return isFortran() ? this :
            new DirectArrayRowAddress(row0, this.chain, col0, col1, EnumSet.of(Address.Flags.FORTRAN), contiguous, rows, cols);
    }

    @Override
    public ArrayAddress toJava() {
        return isFortran() ?
            new DirectArrayRowAddress(row0+1, this.chain, col0+1, col1+1, EnumSet.noneOf(Address.Flags.class), contiguous, rows, cols)
            : this;
    }

    @Override
    public ArrayOffset offset() {
        return new DirectArrayRowAddressOffset(offset, offset);
    }

    @Override
    public ArrayOffset offset(final int index) {
        return new DirectArrayRowAddressOffset(offset, index);
    }

    @Override
    public int op(final int index) {
        return (row0+offset)*cols + (col0+index);
    }


    //
    // implements Cloneable
    //

//XXX
//    @Override
//    public DirectArrayRowAddress clone() {
//        return new DirectArrayRowAddress(row0, chain, col0, col1, flags, contiguous, rows, cols);
//    }
    @Override
    public DirectArrayRowAddress clone() {
        return (DirectArrayRowAddress) super.clone();
    }


    //
    // private inner classes
    //

    private class DirectArrayRowAddressOffset extends DirectAddressOffset implements Address.ArrayAddress.ArrayOffset {

        public DirectArrayRowAddressOffset(final int row, final int col) {
            super.row = row0+row;
            super.col = col0+col;
        }

        @Override
        public void nextIndex() {
            super.col++;
        }

        @Override
        public void prevIndex() {
            super.col--;
        }

        @Override
        public void setIndex(final int index) {
            super.col = col0+index;
        }

        @Override
        public int op() {
            return row*cols + col;
        }

    }

}
