/*
 Copyright (C) 2009 Ueli Hofstetter

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

package org.jquantlib.math;

// FIXME: code review: 
public abstract class E_BinaryFunction<X, Z> implements E_IBinaryFunction<X, Z> {

    public X bounded_x1;
    public X bounded_x2; // FIXME: should it be Y instead of X ?

    /**
     * Computes the value of the function; f(x)
     * 
     * @param x
     * @return f(x)
     */
    public abstract Z evaluate(X x_1, X x_2);

    public void bind_x_1(X bind_x1) {
        this.bounded_x1 = bind_x1;
    }

    public void bind_x_2(X bind_x2) {
        this.bounded_x2 = bind_x2;
    }

}
