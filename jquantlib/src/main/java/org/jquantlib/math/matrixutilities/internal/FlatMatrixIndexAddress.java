package org.jquantlib.math.matrixutilities.internal;


public class FlatMatrixIndexAddress extends FlatIndexAddress implements Address.MatrixAddress {

    public FlatMatrixIndexAddress(
            final int[] ridx,
            final int col0,
            final int col1,
            final Address.MatrixAddress chain,
            final int rows,
            final int cols) {
        super(ridx, col0, col1, chain, rows, cols);
    }

    public FlatMatrixIndexAddress(
            final int row0,
            final int row1,
            final int[] cidx,
            final Address.MatrixAddress chain,
            final int rows,
            final int cols) {
        super(row0, row1, cidx, chain, rows, cols);
    }

    public FlatMatrixIndexAddress(
            final int[] ridx,
            final int[] cidx,
            final Address.MatrixAddress chain,
            final int rows,
            final int cols) {
        super(ridx, cidx, chain, rows, cols);
    }

    //
    // implements MatrixAddress
    //

    @Override
    public MatrixOffset offset() {
        return new FastMatrixIndexAddressOffset(0, 0);
    }

    @Override
    public MatrixOffset offset(final int row, final int col) {
        return new FastMatrixIndexAddressOffset(row, col);
    }

    @Override
    public int op(final int row, final int col) {
        return (row0+ridx[row])*cols + (col0+cidx[col]);
    }


    //
    // implements Cloneable
    //

    @Override
    public FlatMatrixAddress clone() {
        return new FlatMatrixAddress(row0, row1, chain, col0, col1, contiguous, rows, cols);
    }


    //
    // private inner classes
    //

    private class FastMatrixIndexAddressOffset extends FastIndexAddressOffset implements Address.MatrixAddress.MatrixOffset {

        public FastMatrixIndexAddressOffset(final int row, final int col) {
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
            return ridx[row]*cols + cidx[col];
        }

    }

}
