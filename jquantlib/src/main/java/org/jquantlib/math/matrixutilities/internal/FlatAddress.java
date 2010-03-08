package org.jquantlib.math.matrixutilities.internal;

import org.jquantlib.lang.exceptions.LibraryException;


public abstract class FlatAddress implements Address {

    protected final int row0;
    protected final int row1;
    protected final Address chain;
    protected final int col0;
    protected final int col1;

    protected final boolean contiguous;
    protected final int rows;
    protected final int cols;

    protected final int rbase;
    protected final int cbase;
    protected final int base;
    protected final int last;

    public FlatAddress(
            final int row0, final int row1,
            final Address chain,
            final int col0, final int col1,
            final boolean contiguous,
            final int rows, final int cols) {
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



    protected abstract class FastAddressOffset implements Address.Offset {

        protected int row;
        protected int col;

        protected FastAddressOffset() {
            // only protected access allowed
        }

    }


}
