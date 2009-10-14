package org.jquantlib.lang.iterators;

import java.util.ListIterator;

import org.jquantlib.math.matrixutilities.Cells.Style;


/**
 * This interface is similar to {@link ListIterator} but it provides methods which avoid un/boxing and methods which provide
 * direct access to elements, retrieving/modifying them or not.
 *
 * @author Richard Gomes
 */
public interface RandomListIterator<T> extends ListIterator<T> {

    /**
     * Inserts the specified element into <code>this</code> {@link RandomListIterator} (optional operation).
     *
     * @param e is a double, avoiding un/boxing.
     *
     * @see ListIterator#add(Object)
     */
    public abstract void addDouble(final double e);

    /**
     * Returns the next element in <code>this</code> {@link RandomListIterator}.
     *
     * @return a primitive double, avoid un/boxing
     *
     * @see ListIterator#next()
     */
    public abstract double nextDouble();

    /**
     * Returns the previous element in <code>this</code> {@link RandomListIterator}.
     *
     * @return a primitive double, avoid un/boxing
     *
     * @see ListIterator#previous()
     */
    public abstract double previousDouble();

    /**
     * Replaces the current element with the specified value (optional operation).
     *
     * @param e is a double, avoiding un/boxing.
     *
     * @see ListIterator#set(Object)
     */
    public abstract void setDouble(final double e);


    // --

    /**
     * Returns the number of elements.
     *
     * @return scalar
     */
    public abstract int size();

    /**
     * Returns the number of remaining elements
     */
    public abstract int remaining();

    /**
     * Returns the current cursor position according to the {@link Style} representation
     *
     * @return a scalar
     */
    public abstract int cursor();

    /**
     * Moves the cursor to a determined position.
     *
     * @param pos is the desired position
     */
    public abstract void seek(int pos);

    /**
     * Positions the cursor at the beginning.
     */
    public abstract void begin();

    /**
     * Positions the cursor at the end.
     */
    public abstract void end();

    /**
     * Forwards the cursor one element, if any, without returning any value.
     */
    public abstract void forward();

    /**
     * Backwards the cursor one element, if any, without returning any value.
     */
    public abstract void backward();


    // --


    /**
     * @return the first element without touching the cursor pointer.
     */
    public abstract double first();

    /**
     * @return the last element without touching the cursor pointer.
     */
    public abstract double last();

    /**
     *
     * @param offset
     * @return
     */
    public abstract double get(int offset);

    /**
     * Replaces the specified element with the specified value (optional operation).
     *
     * @param offset specified element position
     * @param value to be stored
     */
    public abstract void set(int offset, double value);

}
