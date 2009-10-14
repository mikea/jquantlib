/*
Copyright (C) 2009 Praneet Tiwari

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

import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.NoConstraint;

/**
 * Base class for model arguments
 * 
 * @author Praneet Tiwari
 */
@QualityAssurance(quality = Quality.Q1_TRANSLATION, version = Version.V097, reviewers = { "Richard Gomes" })
public class Parameter {

    //
    // protected fields
    //

    protected Constraint constraint;
    protected Array params;
    protected Impl impl;


    public Parameter() {
        constraint = new NoConstraint();

        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    protected Parameter(final int size, final Impl impl, final Constraint  constraint) {
        this.constraint = constraint;
        this.impl = impl;
        this.params = new Array(size);

        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    public final Array  params() /* @ReadOnly */ {
        return params;
    }

    public void setParam(final int i, final double x) {
        params.set(i, x);
    }

    public boolean testParams(final Array  params) /* @ReadOnly */ {
        return constraint.test(params);
    }

    // FIXME: evaluate the possibility to rename to Ops.Ops#op
    public double get(/* @Time */ final double t) /* @ReadOnly */ {
        return impl.value(params, t);
    }

    public int size() /* @ReadOnly */ {
        return params.size();
    }

    public final Impl implementation() /* @ReadOnly */ {
        return impl;
    }


    //
    // protected abstract class
    //

    protected static abstract class Impl {
        public abstract double value(final Array  params, /* @Time */ double t) /* @ReadOnly */;
    }

}
