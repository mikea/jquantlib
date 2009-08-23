/**
 * 
 */
package org.jquantlib.lang.iterators;


public interface ConstIterator extends DoubleListIterator, IteratorAlgebra {
    public ConstIterator iterator();
    public ConstIterator iterator(int offset);
    public ConstIterator iterator(int offset, int size);
}