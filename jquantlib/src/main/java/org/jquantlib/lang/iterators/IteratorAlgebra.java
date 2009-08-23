package org.jquantlib.lang.iterators;


public interface IteratorAlgebra extends RandomAccessDouble {

    public abstract IteratorAlgebra addAssign(final double scalar);

    public abstract IteratorAlgebra subAssign(final double scalar);

    public abstract IteratorAlgebra mulAssign(final double scalar);

    public abstract IteratorAlgebra divAssign(final double scalar);

    /**
     * Finds the first position in which val could be inserted without changing the ordering.
     *
     * @note Mimics std::lower_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g0ff3b53e875d75731ff8361958fac68f">std::lower_bound</a>
     */
    public abstract int lowerBound(final double val);

    /**
     * Finds the first position in which val could be inserted without changing the ordering.
     *
     * @note Mimics std::lower_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g0ff3b53e875d75731ff8361958fac68f">std::lower_bound</a>
     */
    public abstract int lowerBound(int from, final int to, final double val);

    /**
     * Finds the last position in which val could be inserted without changing the ordering.
     *
     * @note Mimics std::upper_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g9bf525d5276b91ff6441e27386034a75">std::upper_bound</a>
     */
    public abstract int upperBound(final double val);

    /**
     * Finds the last position in which val could be inserted without changing the ordering.
     *
     * @note Mimics std::upper_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g9bf525d5276b91ff6441e27386034a75">std::upper_bound</a>
     */
    public abstract int upperBound(int from, final int to, final double val);

    /**
     * The inner product generalizes the dot product to abstract vector spaces and is normally denoted by {@latex$ <a , b>}.
     * <p>
     * As we are working in space {@latex$ \Re} (real numbers), it's sufficient to understand that both <b>inner product</b> and
     * <b>dot operator</b> give equivalent results.
     *
     * @param another Iterator
     * @return the inner product between this Matrix and another Matrix
     *
     * @see <a href="http://en.wikipedia.org/wiki/Inner_product">Inner Product</a>
     */
    public abstract double innerProduct(final IteratorAlgebra another);

    /**
     * The inner product generalizes the dot product to abstract vector spaces and is normally denoted by {@latex$ <a , b>}.
     * <p>
     * As we are working in space {@latex$ \Re} (real numbers), it's sufficient to understand that both <b>inner product</b> and
     * <b>dot operator</b> give equivalent results.
     *
     * @param another Iterator
     * @param from is the start element
     * @param to is the end element
     * @return the inner product between this Matrix and another Matrix
     *
     * @see <a href="http://en.wikipedia.org/wiki/Inner_product">Inner Product</a>
     */
    public abstract double innerProduct(final IteratorAlgebra another, final int from, final int to);

}