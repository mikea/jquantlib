/*
Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.model.shortrate;

import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.NoConstraint;

/**
 * 
 * @author Praneet Tiwari
 */
// ! %Parameter which is always zero \f$ a(t) = 0 \f$
public class NullParameter extends Parameter {

    protected static class Impl extends Parameter.Impl {

        @Override
        public double /* @Real */value(final Array params, double /* @Time */t) {
            return 0.0;
        }
    }

    public NullParameter() {
        super(0, new NullParameter.Impl(), new NoConstraint());
    }
}
