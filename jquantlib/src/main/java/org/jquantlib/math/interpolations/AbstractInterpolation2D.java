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

package org.jquantlib.math.interpolations;

import static org.jquantlib.math.Closeness.isClose;

import org.jquantlib.QL;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Matrix;


public abstract class AbstractInterpolation2D implements Interpolation2D {

    //
    // protected fields
    //

    /**
     * @note Derived classes are responsible for initializing <i>vx</i>, <i>vy</i> and eventually <i>mz</i>
     */
    protected Array vx;

    /**
     * @note Derived classes are responsible for initializing <i>vx</i>, <i>vy</i> and eventually <i>mz</i>
     */
    protected Array vy;

    /**
     * @note Derived classes are responsible for initializing <i>vx</i>, <i>vy</i> and eventually <i>mz</i>
     */
    protected Matrix mz;


    //
    // protected abstract methods
    //

    protected abstract double evaluateImpl(final double x, final double y);


    //
    // public methods
    //

    /**
     * This method intentionally throws UnsupportedOperationException in order to
     * oblige derived classes to reimplement it if needed.
     *
     * @throws UnsupportedOperationException
     */
    public void calculate() {
        throw new UnsupportedOperationException();
    }


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
     * @throws IllegalArgumentException if <i>x</i> is our of range
     */
    // TODO: code review :: please verify against QL/C++ code
    // FIXME: code review : verify if parameter 'extrapolate' is really needed
    protected final void checkRange(final double x, final double y, final boolean extrapolate) {
        if (! (extrapolate || allowsExtrapolation() || isInRange(x, y)) ) {
            final StringBuilder sb = new StringBuilder();
            sb.append("interpolation range is [");
            sb.append(xMin()).append(", ").append(xMax());
            sb.append("] x [");
            sb.append(yMin()).append(", ").append(yMax());
            sb.append("]: extrapolation at (");
            sb.append(x).append(",").append(y);
            sb.append(") not allowed");
            throw new IllegalArgumentException(sb.toString());
        }
    }


    //
    // implements Interpolation
    //

    /**
     * {@inheritDoc}
     *
     * @deprecated
     */
    @Deprecated
    @Override
    public void update() {
        reload();
    }


    //
    // Overrides AbstractInterpolation
    //

    @Override
    public void reload() {
        QL.require(vx.size() >= 2 && vy.size() >= 2 , "not enough points to interpolate"); // QA:[RG]::verified // TODO: message
        for (int i = 0; i < vx.size()-1; i++) {
            QL.require(vx.get(i) <= vx.get(i+1) , "unsorted values on array X"); // QA:[RG]::verified // TODO: message
            QL.require(vy.get(i) <= vy.get(i+1) , "unsorted values on array Y"); // QA:[RG]::verified // TODO: message
        }
    }


    //
    // implements Ops.BinaryDoubleOp
    //

    @Override
    public double op(final double x, final double y) {
        checkRange(x, y, this.allowsExtrapolation());
        return evaluateImpl(x, y);
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


    //
    // implements Interpolation2D
    //

    @Override
    public double xMin() {
        return vx.first();
    }

    @Override
    public double xMax() {
        return vx.last();
    }

    @Override
    public double yMin() {
        return  vy.first();
    }

    @Override
    public double yMax() {
        return vy.last();
    }

    @Override
    public Array xValues() {
        return vx.clone();
    }

    @Override
    public Array yValues() {
        return vy.clone();
    }

    @Override
    public Matrix zData() {
        return mz.clone();
        // FIXME: code review :: return (double[][])Arrays.trimToCapacity(mz, mz.length);
    }

    @Override
    // FIXME: code review here: compare against original C++ code
    public int locateX(final double x) /* @ReadOnly */ {
        if (x <= vx.first())
            return 0;
        else if (x > vx.last())
            return vx.size()-2;
        else
            return vx.upperBound(x) - 1;
    }

    @Override
    // FIXME: code review here: compare against original C++ code
    public int locateY(final double y) /* @ReadOnly */ {
        if (y <= vy.first())
            return 0;
        else if (y > vy.last())
            return vy.size()-2;
        else
            return vy.upperBound(y) - 1;
    }

    @Override
    public boolean isInRange(final double x, final double y) {
        QL.assertion(extraSafetyChecksX(), "unsorted values on array X"); // QA:[RG]::verified // TODO: message
        final double x1 = xMin(), x2 = xMax();
        final boolean xIsInrange = (x >= x1 && x <= x2) || isClose(x,x1) || isClose(x,x2);
        if (!xIsInrange) return false;

        QL.assertion(extraSafetyChecksY(), "unsorted values on array Y"); // QA:[RG]::verified // TODO: message
        final double y1 = yMin(), y2 = yMax();
        return (y >= y1 && y <= y2) || isClose(y,y1) || isClose(y,y2);
    }

    //
    // private methods
    //

    private boolean extraSafetyChecksX() {
        for (int i=0; i<vx.size()-1; i++) {
            if (vx.get(i) > vx.get(i+1))
                return false;
        }
        return true;
    }

    private boolean extraSafetyChecksY() {
        for (int i=0; i<vy.size()-1; i++) {
            if (vy.get(i) > vy.get(i+1))
                return false;
        }
        return true;
    }

}
