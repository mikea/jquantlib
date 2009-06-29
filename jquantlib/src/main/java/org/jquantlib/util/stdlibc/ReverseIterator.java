package org.jquantlib.util.stdlibc;

/**
 * 
 * @author Richard Gomes
 *
 * @deprecated
 */
public interface ReverseIterator {

    /**
     * Returns true if the iteration has more elements. (In other words, returns true if previousObject would return an element
     * rather than throwing an exception.)
     * 
     * @return true if the iterator has more elements.
     */
    public boolean hasPrevious();

    /**
     * Backs the given number of elements.
     * <p>
     * The effect of this call is exactly the same as that of calling Iterator.back() for n times (possibly stopping if
     * Iterator.hasNext() becomes false).
     * 
     * @param n is the number of elements to skip.
     * @return the number of elements actually skipped.
     */
    public int back(int n);

}
