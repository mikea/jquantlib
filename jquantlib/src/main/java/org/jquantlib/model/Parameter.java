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
package org.jquantlib.model;

import org.jquantlib.lang.annotation.Time;
import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.NoConstraint;

/**
 * 
 * @author Praneet Tiwari
 */
// ! Base class for model arguments
public abstract class Parameter {

    protected Constraint constraint;
    protected Array params;
    protected Impl impl;

    protected static abstract class Impl {

        public abstract double value(final Array params, /* Time */double t);
    }

    protected Parameter(int size, final Impl impl, final Constraint c) {
        this.constraint = c;
        this.impl = impl;
        params = new Array(size);
    }

    public Parameter() {
        constraint = new NoConstraint();
    }

    public Array getParams() {
        return params;
    }

    public void setParam(int /* @Size */i, double x) {

        params.set(i, x);

    }

    public boolean testParams(final Array p) {
        return constraint.test(p);
    }

    // there is no op overloading here. No equivalent of the method below.
    /*
     * Real operator()(Time t) const { return impl_->value(params_, t); }
     */
    public Double /* @Real */getOperatorEq(Double /* @Time */t) {
        return impl.value(params, t);
    }

    public int /* @Size */getSize() {
        return params.size();
    }

    public Impl getImplementation() {
        return impl;
    }
}
