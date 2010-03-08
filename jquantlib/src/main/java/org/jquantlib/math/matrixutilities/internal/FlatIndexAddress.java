package org.jquantlib.math.matrixutilities.internal;

import java.util.Arrays;

import org.jquantlib.lang.exceptions.LibraryException;

public abstract class FlatIndexAddress implements Address {

    protected final int[] ridx;
    protected final int[] cidx;
    protected final Address chain;

    protected final int row0;
    protected final int row1;
    protected final int col0;
    protected final int col1;

    protected final boolean contiguous;
    protected final int rows;
    protected final int cols;

    protected final int rbase;
    protected final int cbase;
    protected final int base;
    protected final int last;


    public FlatIndexAddress(
            final int row0,
            final int row1,
            final int[] cidx,
            final Address chain,
            final int rows,
            final int cols) {
        this(makeIndex(row0, row1), cidx, chain, rows, cols);
    }


    public FlatIndexAddress(
            final int[] ridx,
            final int col0,
            final int col1,
            final Address chain,
            final int rows,
            final int cols) {
        this(ridx, makeIndex(col0, col1), chain, rows, cols);
    }


    public FlatIndexAddress(
            final int[] ridx,
            final int[] cidx,
            final Address chain,
            final int rows,
            final int cols) {

        // obtain contiguous flag from chained address mapping
        boolean contiguous = chain.contiguous();

        // find limiting rows from ridx
        final int[] rorder = ridx.clone();
        Arrays.sort(rorder);
        final int row0 = ridx[0];
        final int row1 = ridx[ridx.length-1];
        // determine if a contiguous interval
        if (contiguous) {
            int row=row0;
            for (final int element : rorder) {
                contiguous = (element == row);
                if (!contiguous) {
                    break;
                }
                row++;
            }
        }

        // find limiting cols from cidx
        final int[] corder = cidx.clone();
        Arrays.sort(corder);
        final int col0 = cidx[0];
        final int col1 = cidx[cidx.length-1];
        // determine if a contiguous interval
        if (contiguous) {
            int col=col0;
            for (final int element : corder) {
                contiguous = (element == col);
                if (!contiguous) {
                    break;
                }
                col++;
            }
        }

        this.ridx  = ridx;
        this.cidx  = cidx;

        this.rbase = chain==null ? row0 : row0 + chain.rbase();
        this.cbase = chain==null ? col0 : col0 + chain.cbase();
        this.rows  = chain==null ? rows : chain.rows();
        this.cols  = chain==null ? cols : chain.cols();

        this.row0  = rbase;
        this.row1  = rbase + (row1-row0);
        this.chain = chain;
        this.col0  = cbase;
        this.col1  = cbase + (col1-col0);;

        this.base = row0*rows + col0;
        this.last = row1*cols + (col1+1);
        this.contiguous = contiguous;
    }


    //
    // implements Address
    //

    @Override
    public boolean contiguous() {
        return contiguous;
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public int rbase() {
        return rbase;
    }

    @Override
    public int cbase() {
        return cbase;
    }

    @Override
    public int base() {
        return base;
    }

    @Override
    public int last() {
        if (!contiguous) {
            throw new LibraryException(GAP_INDEX_FOUND);
        }
        return last;
    }


    //
    // private static methods
    //

    private final static int[] makeIndex(final int idx0, final int idx1) {
        final int[] result = new int[idx1-idx0+1];
        for (int i=idx0; i<=idx1; i++) {
            result[i] = i;
        }
        return result;
    }


    //
    // protected inner classes
    //

    protected abstract class FastIndexAddressOffset implements Address.Offset {

        protected int row;
        protected int col;

        protected FastIndexAddressOffset() {
            // only protected access allowed
        }

    }


}
