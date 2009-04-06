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
import org.jquantlib.math.optimization.Constraint;

/**
 * 
 * @author Praneet Tiwari
 */
public class PositiveConstraint extends Constraint {
    // Ideally we should have a class Constraint.Impl
    // since it's not there, the test method will have to reside in the main method

    public boolean test(final Array params) {
        for (int /* @Size */i = 0; i < params.size(); ++i) {
            if (params.get(i) <= 0.0) {
                return false;
            }
        }
        return true;
    }
    /****
     * no need for the constructor now public PositiveConstraint() { } : Constraint(boost::shared_ptr<Constraint::Impl>( new
     * PositiveConstraint::Impl)) {}
     */
}
