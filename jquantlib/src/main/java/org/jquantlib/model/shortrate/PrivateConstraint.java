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

import java.util.ArrayList;
import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.model.Parameter;

/**
 * 
 * @author Praneet Tiwari
 */
public class PrivateConstraint extends Constraint {

    private ArrayList<Parameter> arguments;

    @Override
    public boolean test(Array p) {
        int /* @Size */k = 0;

        for (int i = 0; i < arguments.size(); i++) {
            int size = arguments.size();
            Array testParams = new Array(size);
            for (int j = 0; j < size; j++, k++) {
                testParams.set(j, p.get(k));
            }
            if (!arguments.get(i).testParams(testParams)) {
                return false;
            }
        }

        return true;
    }

    /*
     * public: PrivateConstraint(const std::vector<Parameter>& arguments) : Constraint(boost::shared_ptr<Constraint::Impl>( new
     * PrivateConstraint::Impl(arguments))) {}
     */
    public PrivateConstraint(ArrayList<Parameter> args) {
        arguments = args;
    }
}
