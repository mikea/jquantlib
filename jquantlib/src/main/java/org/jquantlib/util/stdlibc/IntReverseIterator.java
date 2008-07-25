package org.jquantlib.util.stdlibc;

import java.util.NoSuchElementException;

/**
 * @author Richard Gomes
 */
public interface IntReverseIterator extends ReverseIterator {

    /**
     * Returns the previous element in the iteration.
     * <p>
     * Calling this method repeatedly until the hasPrevious() method returns false will return each element in the underlying
     * collection exactly once.
     * 
     * @return a primitive type.
     * 
     * @throws NoSuchElementException - iteration has no more elements.
     */
    public int previous()  throws NoSuchElementException;

}
