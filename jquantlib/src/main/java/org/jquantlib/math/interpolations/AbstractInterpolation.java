/*
 Copyright (C) 2008 Richard Gomes

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

/*
 Copyright (C) 2002, 2003 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.math.interpolations;

import static org.jquantlib.math.Closeness.isClose;

import org.jquantlib.QL;
import org.jquantlib.lang.iterators.ConstIterator;



public abstract class AbstractInterpolation implements Interpolation {

    //
    // abstract methods
    //

    //
    // These methods are used by their counterparts, e.g:
    //   evaluateImpl is called by evaluate
    //   primitiveImpl is called by primitive
    //	 ... and so on.
    //
    protected abstract double opImpl(final double x);
    protected abstract double primitiveImpl(final double x);
    protected abstract double derivativeImpl(final double x);
    protected abstract double secondDerivativeImpl(final double x);


    //
    // protected fields
    //

    /**
     * @note Derived classes are responsible for initializing <i>vx</i> and <i>vy</i>
     */
    protected ConstIterator vx;

    /**
     * @note Derived classes are responsible for initializing <i>vx</i> and <i>vy</i>
     */
    protected ConstIterator vy;


    //
    // protected methods
    //

    /**
     * This method verifies if
     * <li> extrapolation is enabled;</li>
     * <li> requested <i>x</i> is valid</li>
     *
     * @param x
     * @param extrapolate
     *
     * @throws IllegalStateException if extrapolation is not enabled.
     * @throws IllegalArgumentException if <i>x</i> is out of range
     */
    // TODO: code review :: please verify against QL/C++ code
    protected final void checkRange(final double x, final boolean extrapolate) {
        if (! (extrapolate || allowsExtrapolation() || isInRange(x)) ) {
            final StringBuilder sb = new StringBuilder();
            sb.append("interpolation range is [");
            sb.append(xMin()).append(", ").append(xMax());
            sb.append("]: extrapolation at ");
            sb.append(x);
            sb.append(" not allowed");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    protected int locate(final double x) /* @ReadOnly */ {
        if (x <= vx.first())
            return 0;
        else if (x > vx.last())
            return vx.size()-2;
        else
            return vx.upperBound(x) - 1;
    }


    //
    // implements Interpolation
    //

    @Override
    public final double xMin() /* @ReadOnly */ {
        return  vx.first(); // get first element
    }

    @Override
    public final double xMax() /* @ReadOnly */ {
        return vx.last(); // get last element
    }

    @Override
    public final ConstIterator xValues() {
        return vx.iterator();
    }

    @Override
    public final ConstIterator yValues() {
        return vy.iterator();
    }

    @Override
    public final double evaluate(final double x, final boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
        return opImpl(x);
    }

    @Override
    public final double primitive(final double x) {
        return primitive(x, false);
    }

    @Override
    public final double primitive(final double x, final boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
        return primitiveImpl(x);
    }

    @Override
    public final double derivative(final double x) {
        return derivative(x, false);
    }

    @Override
    public final double derivative(final double x, final boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
        return derivativeImpl(x);
    }

    @Override
    public final double secondDerivative(final double x) {
        return secondDerivative(x, false);
    }

    @Override
    public final double secondDerivative(final double x, final boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
        return secondDerivativeImpl(x);
    }

    @Override
    public final boolean isInRange(final double x) {
        QL.assertion(extraSafetyChecks(), "unsorted values on array X"); // QA:[RG]::verified // TODO: message
        final double x1 = xMin(), x2 = xMax();
        return (x >= x1 && x <= x2) || isClose(x,x1) || isClose(x,x2);
    }

    @Override
    public void update() {
        QL.require(vx.size() >= 2 , "not enough points to interpolate"); // QA:[RG]::verified // TODO: message
        QL.assertion(extraSafetyChecks(), "unsorted values on array X"); // QA:[RG]::verified // TODO: message
    }


    //
    // private methods
    //

    private boolean extraSafetyChecks() {
        for (int i=0; i<vx.size()-1; i++) {
            if (vx.get(i) > vx.get(i+1))
                return false;
        }
        return true;
    }


    //
    // implements Ops.DoubleOp
    //

    @Override
    public final double op(final double x) {
        return evaluate(x, false);
    }


    //
    // implements Extrapolator
    //

    /**
     * Implements multiple inheritance via delegate pattern to an inner class
     *
     * @see Extrapolator
     */
    private final DefaultExtrapolator delegatedExtrapolator = new DefaultExtrapolator();

    @Override
    public final boolean allowsExtrapolation() {
        return delegatedExtrapolator.allowsExtrapolation();
    }

    @Override
    public void disableExtrapolation() {
        delegatedExtrapolator.disableExtrapolation();
    }

    @Override
    public void enableExtrapolation() {
        delegatedExtrapolator.enableExtrapolation();
    }

}
