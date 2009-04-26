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

/**
 * 
 * @author Praneet Tiwari
 */
public abstract class OneFactorAffineModel extends OneFactorModel implements AffineModel {

    public OneFactorAffineModel(int /* @Size */nArguments) {
        super(nArguments);
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    public double /* @Real */discountBond(double /* @Time */now, double /* @Time */maturity, Array factors) {
        return discountBond(now, maturity, factors.at(0));
    }

    public double /* @Real */discountBond(double /* @Time */now, double /* @Time */maturity, double /* @Rate */rate) {
        return A(now, maturity) * Math.exp(-B(now, maturity) * rate);
    }

    @Override
    public double /* @DiscountFactor */discount(double /* @Time */t) {
        double /* @Real */x0 = dynamics().process().x0();
        double /* @Rate */r0 = dynamics().shortRate(0.0, x0);
        return discountBond(0.0, t, r0);
    }

    protected abstract double /* @Real */A(double /* @Time */t, double /* @Time */T);

    protected abstract double /* @Real */B(double /* @Time */t, double /* @Time */T);
}
