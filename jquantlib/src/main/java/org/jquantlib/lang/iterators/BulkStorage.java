/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.lang.iterators;

/**
 * This interface defines 'bulk' operations performed on data stores.
 * 
 * @author Richard Gomes
 *
 * @param <T>
 */
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
