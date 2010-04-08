package org.jquantlib.math.matrixutilities.internal;

import java.util.EnumSet;
import java.util.Set;

import org.jquantlib.lang.exceptions.LibraryException;


public class MappedMatrixAddress extends MappedAddress implements Address.MatrixAddress {

    public MappedMatrixAddress(
            final double[] data,
            final int[] ridx,
            final Address.MatrixAddress chain,
            final int col0, final int col1,
            final Set<Address.Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        super(data, ridx, chain, col0, col1, flags, contiguous, rows, cols);
    }

    public MappedMatrixAddress(
            final double[] data,
            final int row0, final int row1,
            final Address.MatrixAddress chain,
            final int[] cidx,
            final Set<Address.Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        super(data, row0, row1, chain, cidx, flags, contiguous, rows, cols);
    }

    public MappedMatrixAddress(
            final double[] data,
            final int[] ridx,
            final Address.MatrixAddress chain,
            final int[] cidx,
            final Set<Address.Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        super(data, ridx, chain, cidx, flags, contiguous, rows, cols);
    }

    //
    // implements MatrixAddress
    //

    @Override
    public MatrixAddress toFortran() {
        return isFortran() ? this :
            new DirectMatrixAddress(data, row0, row1, this.chain, col0, col1, EnumSet.of(Address.Flags.FORTRAN), contiguous, rows, cols);
    }

    @Override
    public MatrixAddress toJava() {
        return isFortran() ?
            new DirectMatrixAddress(data, row0+1, row1+1, this.chain, col0+1, col1+1, EnumSet.noneOf(Address.Flags.class), contiguous, rows, cols)
            : this;
    }

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
    public MappedMatrixAddress clone() {
        try {
            return (MappedMatrixAddress) super.clone();
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
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
