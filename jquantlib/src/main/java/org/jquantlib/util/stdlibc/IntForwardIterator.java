package org.jquantlib.util.stdlibc;

import java.util.NoSuchElementException;

/**
 * @author Richard Gomes
 * 
 * @deprecated
 */
public interface IntForwardIterator extends ForwardIterator {
    
    /**
     * Returns the next element in the iteration.
     * <p>
     * Calling this method repeatedly until the hasNext() method returns false will return each element in the underlying collection
     * exactly once.
     * 
     * @return a primitive type.
     * 
     * @throws NoSuchElementException - iteration has no more elements.
     */
    public int next() throws NoSuchElementException;

}
