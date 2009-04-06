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

import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.methods.lattices.TrinomialTree;
import org.jquantlib.time.TimeGrid;

/**
 * 
 * @author Praneet Tiwari
 */
public abstract class OneFactorModel extends ShortRateModel {

    public OneFactorModel(int /* @Size */nArguments) {
        super(nArguments);
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    public Lattice tree(final TimeGrid grid) {
        TrinomialTree trinomial = new TrinomialTree(dynamics().process(), grid, true);
        return new ShortRateTree(trinomial, dynamics(), grid);
    }

    // ! returns the short-rate dynamics
    public abstract ShortRateDynamics dynamics();

    // ! Return by default a trinomial recombining tree
    // public abstract Lattice tree(final TimeGrid grid) ;
}
