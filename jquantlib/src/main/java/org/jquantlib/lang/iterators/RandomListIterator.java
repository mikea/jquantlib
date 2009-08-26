package org.jquantlib.lang.iterators;

import java.util.ListIterator;


/**
 * This interface is similar to {@link ListIterator} but it provides methods which avoid un/boxing and methods which provide
 * direct access to elements, retrieving/modifying them or not.
 *
 * @author Richard Gomes
 */
public interface RandomListIterator extends ListIterator<Double> {

    /**
     * Inserts the specified element into <code>this</code> {@link RandomListIterator} (optional operation).
     *
     * @param e is a double, avoiding un/boxing.
     */
    public abstract void addDouble(final double e);

    /**
     * Returns the next element in <code>this</code> {@link RandomListIterator}.
     *
     * @return a primitive double, avoid un/boxing
     */
    public abstract double nextDouble();

    /**
     * Returns the previous element in <code>this</code> {@link RandomListIterator}.
     *
     * @return a primitive double, avoid un/boxing
     */
    public abstract double previousDouble();

    /**
     * Replaces the current element with the specified value (optional operation).
     *
     * @param e is a double, avoiding un/boxing.
     */
    public abstract void set(final double e);


    // --


    /**
     * @return the current cursor position.
     */
    public abstract int cursor();

    /**
     * Moves the cursor to a determined position.
     *
     * @param pos is the desired position
     */
    public abstract void seek(int pos);

    /**
     * Positions the cursor at the beginning of <code>this</code> {@link RandomListIterator}.
     */
    public abstract void begin();

    /**
     * Positions the cursor at the ending of <code>this</code> {@link RandomListIterator}.
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
     * @return the number of elements.
     */
    public abstract int size();

    /**
     * @return the first element in <code>this</code> RandomListIterator.
     */
    public abstract double first();

    /**
     * @return the last element in <code>this</code> RandomListIterator.
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
