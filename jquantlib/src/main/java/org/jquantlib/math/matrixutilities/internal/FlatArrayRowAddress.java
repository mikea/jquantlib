package org.jquantlib.math.matrixutilities.internal;


public class FlatArrayRowAddress extends FlatAddress implements Address.ArrayAddress {

    public FlatArrayRowAddress(
            final int row,
            final Address chain,
            final int col0,
            final int col1,
            final boolean contiguous,
            final int rows,
            final int cols) {
        super(row, row, chain, col0, col1, contiguous, rows, cols);
    }


    //
    // implements ArrayAddress
    //

    @Override
    public ArrayOffset offset() {
        return new FastArrayRowAddressOffset(0, 0);
    }

    @Override
    public ArrayOffset offset(final int index) {
        return new FastArrayRowAddressOffset(0, index);
    }

    @Override
    public int op(final int index) {
        return row0*cols + (col0+index);
    }


    //
    // implements Cloneable
    //

    @Override
    public FlatArrayRowAddress clone() {
        return new FlatArrayRowAddress(row0, chain, col0, col1, contiguous, rows, cols);
    }


    //
    // private inner classes
    //

    private class FastArrayRowAddressOffset extends FastAddressOffset implements Address.ArrayAddress.ArrayOffset {

        public FastArrayRowAddressOffset(final int row, final int col) {
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
