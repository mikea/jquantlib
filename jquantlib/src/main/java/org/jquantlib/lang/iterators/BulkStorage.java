package org.jquantlib.lang.iterators;

public interface BulkStorage<T> {

    /**
     * Fills all elements of <code>this</code> instance with a given scalar
     *
     * @return this
     */
    public T fill(final double scalar);

    /**
     * Fills <code>this</code> instance with contests from <code>another</code> instance.
     *
     * @param another
     * @return
     */
    public T fill(T another);

    /**
     * Swaps contents of <code>this</code> instance by contents of <code>another</code> instance.
     *
     * @param another
     * @return this
     */
    public T swap(final T another);

    /**
     * Sorts elements of <code>this</code> instance.
     *
     * @return this
     */
    public T sort();

}
