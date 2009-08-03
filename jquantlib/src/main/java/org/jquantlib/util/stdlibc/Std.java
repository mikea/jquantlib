/*
 Copyright (C) 2007 Richard Gomes

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.util.stdlibc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Mimics library libstdc++ from C++ language which exposes top level functions to <code>std:: namespace</code>
 * 
 * @see <a http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/index.html">libstdc++ Source Documentation</a>
 * @see <a href="http://javadude.com/articles/passbyvalue.htm">Java is Pass-by-Value, Dammit!</a>
 * 
 * @author Dominik Holenstein
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
public final class Std {

    /**
     * Singleton instance for the whole application.
     * <p>
     * In an application server environment, it could be by class loader depending on scope of the
     * jquantlib library to the module.
     * 
     * @see <a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">The "Double-Checked Locking is Broken" Declaration </a>
     */
    private static volatile Std instance = null;

    public static Std getInstance() {
        if (instance == null)
            synchronized (Std.class) {
                if (instance == null)
                    instance = new Std();
            }
        return instance;
    }

    private Std() {
        // cannot be directly instantiated
    }


    //
    // public methods
    //

    /**
     * Return the minimum element in a range.
     * 
     * @note Mimics std::min
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g49f0c87cb0e1bf950f5c2d49aa106573">std::min</a>
     */
    // TODO: consider the parallel version of std::min (probably implementing in class GnuParallel)
    // http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00964.html#0d0e5aa5b83e8ffa90d57714f03d73bf
    public <T extends Comparable<T>> T min(final T... t) {
        assert t!=null : "argument cannot be null"; // TODO: message
        final List<T> list = Arrays.asList(t);
        Collections.sort(list);
        return list.get(0);
    }


    /**
     * Return the maximum element in a range.
     * 
     * @note Mimics std::max
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#gacf2fd7d602b70d56279425df06bd02c">std::max</a>
     */
    // TODO: consider the parallel version of std::max (probably implementing in class GnuParallel)
    // http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00964.html#992b78d1946c7c02e46bc3509637f12d
    public <T extends Comparable<T>> T max(final T... t) {
        assert t!=null : "argument cannot be null"; // TODO: message
        final List<T> list = Arrays.asList(t);
        Collections.sort(list);
        return list.get(list.size() - 1);
    }

}
