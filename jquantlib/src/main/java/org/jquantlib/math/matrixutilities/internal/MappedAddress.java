package org.jquantlib.math.matrixutilities.internal;

import java.util.Arrays;
import java.util.Set;

import org.jquantlib.lang.exceptions.LibraryException;

public abstract class MappedAddress implements Address {

    protected final int row0;
    protected final int row1;
    protected final int ridx[];
    protected final Address chain;
    protected final int col0;
    protected final int col1;
    protected final int cidx[];
    protected final Set<Address.Flags> flags;
    protected final int rows;
    protected final int cols;

    protected final int base;
    protected final int last;


    //
    // public methods
    //

    public MappedAddress(
            final int row0, final int row1,
            final Address chain,
            final int[] cidx,
            final Set<Address.Flags> flags,
            final int rows, final int cols) {
        this(makeIndex(row0, row1), chain, cidx, flags, rows, cols);
    }


    public MappedAddress(
            final int[] ridx,
            final Address chain,
            final int col0, final int col1,
            final Set<Address.Flags> flags,
            final int rows, final int cols) {
        this(ridx, chain, makeIndex(col0, col1), flags, rows, cols);
    }


    public MappedAddress(
            final int[] ridx,
            final Address chain,
            final int[] cidx,
            final Set<Address.Flags> flags,
            final int rows, final int cols) {
        this.chain = chain;
        this.flags = flags;

        final int offset = ( fortran() ? 1 : 0 ) - ( (chain==null) ? 0 : ( chain.fortran() ? 1 : 0 ) );

        // obtain contiguous flag from chained address mapping
        boolean contiguous = chain.contiguous();

        // clone indexes and apply offset
        this.ridx  = ridx.clone();
        for (int i=0; i<this.ridx.length; i++) {
            this.ridx[i] += offset;
        }
        // find limiting rows from ridx
        final int[] rorder = this.ridx.clone();
        Arrays.sort(rorder);
        // find lower index and upper indexes
        final int row0 = rorder[0];
        final int row1 = rorder[rorder.length-1];
        // determine if it is a contiguous interval
        contiguous &= contiguous(rorder);

        // clone indexes and apply offset
        this.cidx  = cidx.clone();
        for (int i=0; i<this.cidx.length; i++) {
            this.cidx[i] += offset;
        }
        // find limiting rows from ridx
        final int[] corder = this.cidx.clone();
        Arrays.sort(corder);
        // find lower index and upper indexes
        final int col0 = corder[0];
        final int col1 = corder[corder.length-1];
        // determine if it is a contiguous interval
        contiguous &= contiguous(corder);

        // remove Address.Flags.CONTIGUOUS from this Set<Address.Flags> if needed
        if (!contiguous) {
            this.flags.remove(Address.Flags.CONTIGUOUS);
        }

        this.row0 = offset + ( chain==null ? row0 : row0 + chain.row0() );
        this.col0 = offset + ( chain==null ? col0 : col0 + chain.col0() );
        this.row1 = this.row0 + (row1-row0);
        this.col1 = this.col0 + (col1-col0);
        this.rows = chain==null ? rows : chain.rows();
        this.cols = chain==null ? cols : chain.cols();

        this.base = (row0-offset)*rows + (col0-offset);
        this.last = (row1-offset)*cols + (col1-offset+1);
    }


    //
    // implements Address
    //

    @Override
    public boolean contiguous() {
        return flags.contains(Address.Flags.CONTIGUOUS);
    }

    @Override
    public boolean fortran() {
        return flags.contains(Address.Flags.FORTRAN);
    }

    @Override
    public Set<Address.Flags> flags() {
        return flags;
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
    public int row0() {
        return row0;
    }

    @Override
    public int col0() {
        return col0;
    }

    @Override
    public int base() {
        return base;
    }

    @Override
    public int last() {
        if (!contiguous())
            throw new LibraryException(GAP_INDEX_FOUND);
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

    private static boolean contiguous(final int array[]) {
        final int[] rorder = array.clone();
        Arrays.sort(rorder);
        // determine if it is a contiguous interval
        boolean result = false;
        int  curr = rorder[0];
        for (final int element : rorder) {
            result = (element == curr);
            if (!result) {
                break;
            }
            curr++;
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
