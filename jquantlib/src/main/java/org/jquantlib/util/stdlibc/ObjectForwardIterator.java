package org.jquantlib.util.stdlibc;

import java.util.NoSuchElementException;

/**
 * @author Richard Gomes
 */
public interface ObjectForwardIterator<T> extends ForwardIterator {
    
    /**
     * Returns the next element in the iteration.
     * <p>
     * Calling this method repeatedly until the hasNext() method returns false will return each element in the underlying collection
     * exactly once.
     * 
     * @return a copy of the original object
     * 
     * @throws NoSuchElementException - iteration has no more elements.
     */
    public T next() throws NoSuchElementException;

}
