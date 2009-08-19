package org.jquantlib.lang.iterators;

import java.util.ListIterator;

/**
 * This interface is similar to {@link ListIterator} but it provides methods which avoid un/boxing and methods which
 * positions to the next element or to the previous element without retrieving it.
 *
 * @author Richard Gomes
 */
public interface DoubleListIterator extends ListIterator<Double> {

    /**
     * Inserts the specified element into the list (optional operation).
     *
     * @param e is a double, avoiding un/boxing.
     */
    public abstract void add(final double e);

    /**
     * Returns the next element in the list.
     *
     * @return a primitive double, avoid un/boxing
     */
    public abstract double nextDouble();

    /**
     * Returns the previous element in the list.
     *
     * @return a primitive double, avoid un/boxing
     */
    public abstract double previousDouble();

    /**
     * Forwards one element, if any, without returning any value.
     */
    public abstract void forward();

    /**
     * Backwards one element, if any, without returning any value.
     */
    public abstract void backward();

    /**
     * Replaces the last element returned by next or previous with the specified element (optional operation).
     *
     * @param e is a double, avoiding un/boxing.
     */
    public abstract void set(final double e);

}