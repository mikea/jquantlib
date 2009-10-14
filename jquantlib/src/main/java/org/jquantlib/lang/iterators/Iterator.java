/**
 *
 */
package org.jquantlib.lang.iterators;

import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Matrix;


/**
 * An iterator intended to provide access to underlying data kept by classes Matrix and Array
 * <p>
 * Operations add and remove are not implemented
 *
 * @see Matrix
 * @see Array
 *
 * @author Richard Gomes
 */
public interface Iterator extends Algebra<Iterator>, RandomListIterator<Double> {

    /**
     * Builds a new Iterator, resetting its positioning.
     */
    public Iterator iterator();

    /**
     * Builds a new Iterator which starts at a specific element <code>elem0</code>
     *
     * @param elem0 determines the element, inclusive
     */
    public Iterator iterator(int elem0);

    /**
     * Builds a new Iterator which starts at a specific element <code>elem0</code> inclusive and
     * goes thru <code>elem1</code> exlusive.
     *
     * @param elem0 determines the element, inclusive
     * @param elem1 determines the last element, exclusive
     */
    public Iterator iterator(int elem0, int elem1);


    /**
     * Builds a new constant, non-modifiable ConstIterator which starts at a specific element <code>elem0</code>
     *
     * @param elem0 determines the element, inclusive
     */
    public ConstIterator constIterator();

    /**
     * Builds a new constant, non-modifiable ConstIterator which starts at a specific element <code>elem0</code>
     *
     * @param elem0 determines the element, inclusive
     */
    public ConstIterator constIterator(int elem0);

    /**
     * Builds a new constant, non-modifiable ConstIterator which starts at a specific element <code>elem0</code> and
     * goes thru <code>elem1</code> exlusive.
     *
     * @param elem0 determines the element, inclusive
     * @param elem1 determines the last element, exclusive
     */
    public ConstIterator constIterator(int elem0, int elem1);



    /**
     * Fills all elements of <code>this</code> Iterator with a given scalar
     *
     * @return this
     */
    public Iterator fill(final double scalar);

    /**
     * Fills <code>this</code> Iterator with contests from <code>another</code> Iterator.
     *
     * @param another
     * @return
     */
    public Iterator fill(Iterator another);

    /**
     * Fills <code>this</code> Iterator with <code>size</code> elements from <code>another</code> Iterator.
     *
     * @param another
     * @return
     */
    public Iterator fill(Iterator another, final int size);

    /**
     * Swaps contents of <code>this</code> Iterator by contents of <code>another</code> Iterator.
     *
     * @param another
     * @return this
     */
    public Iterator swap(final Iterator another);

}
