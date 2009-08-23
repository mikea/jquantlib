/**
 * 
 */
package org.jquantlib.lang.iterators;


public interface Iterator extends DoubleListIterator, IteratorAlgebra {
    public Iterator iterator();
    public Iterator iterator(int offset);
    public Iterator iterator(int offset, int size);
}