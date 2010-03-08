package org.jquantlib.math.matrixutilities.internal;


public class FlatArrayColAddress extends FlatAddress implements Address.ArrayAddress {

    public FlatArrayColAddress(
            final int row0,
            final int row1,
            final Address chain,
            final int col,
            final boolean contiguous,
            final int rows,
            final int cols) {
        super(row0, row1, chain, col, col, contiguous, rows, cols);
    }


    //
    // implements ArrayAddress
    //

    @Override
    public ArrayOffset offset() {
        return new FastArrayColAddressOffset(0, 0);
    }

    @Override
    public ArrayOffset offset(final int index) {
        return new FastArrayColAddressOffset(index, 0);
    }

    @Override
    public int op(final int index) {
        return (row0+index)*cols + col0;
    }


    //
    // implements Cloneable
    //

    @Override
    public FlatArrayColAddress clone() {
        return new FlatArrayColAddress(row0, row1, chain, col0, contiguous, rows, cols);
    }


    //
    // private inner classes
    //

    private class FastArrayColAddressOffset extends FastAddressOffset implements Address.ArrayAddress.ArrayOffset {

        public FastArrayColAddressOffset(final int row, final int col) {
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
