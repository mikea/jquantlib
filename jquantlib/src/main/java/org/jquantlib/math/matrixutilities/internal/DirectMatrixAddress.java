package org.jquantlib.math.matrixutilities.internal;

import java.util.Set;

public class DirectMatrixAddress extends DirectAddress implements Address.MatrixAddress {

    public DirectMatrixAddress(
            final int row0, final int row1,
            final Address chain,
            final int col0, final int col1,
            final Set<Address.Flags> flags,
            final int rows, final int cols) {
        super(row0, row1, chain, col0, col1, flags, rows, cols);
    }

    //
    // implements MatrixAddress
    //

    @Override
    public MatrixOffset offset() {
        return new FastMatrixAddressOffset(0, 0);
    }

    @Override
    public MatrixOffset offset(final int row, final int col) {
        return new FastMatrixAddressOffset(row, col);
    }

    @Override
    public int op(final int row, final int col) {
        return (row0+row)*cols + (col0+col);
    }


    //
    // implements Cloneable
    //

    @Override
    public DirectMatrixAddress clone() {
        return new DirectMatrixAddress(row0, row1, chain, col0, col1, flags, rows, cols);
    }


    //
    // private inner classes
    //

    private class FastMatrixAddressOffset extends FastAddressOffset implements Address.MatrixAddress.MatrixOffset {

        public FastMatrixAddressOffset(final int row, final int col) {
            super.row = row0+row;
            super.col = col0+col;
        }

        @Override
        public void nextRow() {
            super.row++;
        }

        @Override
        public void nextCol() {
            super.col++;
        }

        @Override
        public void prevRow() {
            super.row--;
        }

        @Override
        public void prevCol() {
            super.col--;
        }

        @Override
        public void setRow(final int row) {
            super.row = row0+row;
        }

        @Override
        public void setCol(final int col) {
            super.col = col0+col;
        }

        @Override
        public int op() {
            return row*cols + col;
        }

    }

}
