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
 Copyright (C) 2003 Ferdinando Ametrano
 Copyright (C) 2004 StatPro Italia srl

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

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.lang.iterators.ConstIterator;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.interpolations.factories.CubicSpline;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.methods.finitedifferences.TridiagonalOperator;

/**
 * Cubic spline interpolation between discrete points.
 * <p>
 * Interpolations are not instantiated directly by applications, but via a factory class.
 * <p>
 * It implements different type of end conditions: not-a-knot, first derivative value, second derivative value.
 * <p>
 * It also implements Hyman's monotonicity constraint filter which ensures that the interpolating spline remains monotonic at the
 * expense of the second derivative of the curve which will no longer be continuous where the filter has been applied. If the
 * interpolating spline is already monotonic, the Hyman filter leaves it unchanged.
 * <p>
 * See R. L. Dougherty, A. Edelman, and J. M. Hyman, "Nonnegativity-, Monotonicity-, or Convexity-Preserving Cubic and Quintic
 * Hermite Interpolation" Mathematics Of Computation, v. 52, n. 186, April 1989, pp. 471-494.
 *
 * @see CubicSpline
 *
 * @author Richard Gomes
 * @author Daniel Kong
 *
 */
// TEST : needs code review and test classes
public class CubicSplineInterpolation extends AbstractInterpolation {

    //
    // private final fields
    //

    private final BoundaryCondition leftType;
    private final BoundaryCondition rightType;
    private final double leftValue;
    private final double rightValue;
    private final boolean constrained;


    //
    // private fields
    //

    private int      n;
    private Array    vp;
    private Array    va;
    private Array    vb;
    private Array    vc;
    private boolean  monotone;


    //
    // public inner enums
    //

    public enum BoundaryCondition {
        /**
         * Make second(-last) point an inactive knot
         */
        NotAKnot,

        /**
         * Match value of end-slope
         */
        FirstDerivative,

        /**
         * Match value of second derivative at end
         */
        SecondDerivative,

        /**
         * Match first and second derivative at either end
         */
        Periodic,

        /**
         * Match end-slope to the slope of the cubic that matches
         * the first four data at the respective end
         */
        Lagrange;
    }


    //
    // static public methods
    //

    /**
     * This is a factory method intended to create this interpolation.
     * <p>
     * Interpolations are not instantiated directly by applications, but via a factory class.
     *
     * @see CubicSpline
     */
    static public Interpolator getInterpolator(
            final CubicSplineInterpolation.BoundaryCondition leftCondition,
            final double leftConditionValue,
            final CubicSplineInterpolation.BoundaryCondition rightCondition,
            final double rightConditionValue,
            final boolean monotonicityConstraint) /* @ReadOnly */ {
        final CubicSplineInterpolation cubicSpline = new CubicSplineInterpolation(
                leftCondition, leftConditionValue, rightCondition, rightConditionValue, monotonicityConstraint);
        return cubicSpline. new CubicSplineInterpolationImpl(cubicSpline);
    }


    //
    // private constructors
    //

    /**
     * Constructor for a CubicSpline interpolation.
     * <p>
     * Interpolations are not instantiated directly by applications, but via a factory class.
     *
     * @see CubicSpline
     */
    private CubicSplineInterpolation(
            final CubicSplineInterpolation.BoundaryCondition leftCondition,
            final double leftConditionValue,
            final CubicSplineInterpolation.BoundaryCondition rightCondition,
            final double rightConditionValue,
            final boolean monotonicityConstraint) {
        this.leftType  = leftCondition;
        this.rightType = rightCondition;
        this.leftValue  = leftConditionValue;
        this.rightValue = rightConditionValue;
        this.monotone = false;
        this.constrained = monotonicityConstraint;
    }


    //
    // public methods
    //

    public Array getVa() {
        // TODO: code review: use of clone()
        return va.clone();
    }

    public Array getVb() {
        // TODO: code review: use of clone()
        return vb.clone();
    }

    public Array getVc() {
        // TODO: code review: use of clone()
        return vc.clone();
    }


    //
    // Overrides AbstractInterpolation
    //

    /**
     * {@inheritDoc}
     *
     * @note Class factory is responsible for initializing <i>vx</i> and <i>vy</i>
     */
    @Override
    public void update() {

        super.update();

        final TridiagonalOperator L = new TridiagonalOperator(n);
        final Array dx  = new Array(n);
        final Array S   = new Array(n);
        Array tmp       = new Array(n);

        int i=0;
        dx.set(i, vx.get(i+1) - vx.get(i) );
        S.set(i,  (vy.get(i+1) - vy.get(i))/dx.get(i) );
        for (i=1; i<n-1; i++) {
            dx.set(i, vx.get(i+1) - vx.get(i) );
            S.set(i,  (vy.get(i+1) - vy.get(i))/dx.get(i) );
            L.setMidRow(i, dx.get(i), 2.0*(dx.get(i)+dx.get(i-1)), dx.get(i-1));
            tmp.set(i, 3.0*(dx.get(i)*S.get(i-1) + dx.get(i-1)*S.get(i)));
        }

        // **** BOUNDARY CONDITIONS ****

        // left condition
        switch (leftType) {
        case NotAKnot:
            // ignoring end condition value
            L.setFirstRow(dx.get(1)*(dx.get(1)+dx.get(0)),
                    (dx.get(0)+dx.get(1))*(dx.get(0)+dx.get(1)));
            tmp.set(0, S.get(0)*dx.get(1)*(2.0*dx.get(1)+3.0*dx.get(0)) + S.get(1)*dx.get(0)*dx.get(0));
            break;
        case FirstDerivative:
            L.setFirstRow(1.0, 0.0);
            tmp.set(0, leftValue);
            break;
        case SecondDerivative:
            L.setFirstRow(2.0, 1.0);
            tmp.set(0, 3.0*S.get(0) - leftValue*dx.get(0)/2.0);
            break;
        case Periodic:
        case Lagrange:
            // ignoring end condition value
            throw new UnsupportedOperationException("this end condition is not implemented yet");
        default:
            throw new LibraryException("unknown end condition"); // QA:[RG]::verified //TODO: message
        }

        // right condition
        switch (rightType) {
        case NotAKnot:
            // ignoring end condition value
            L.setLastRow(-(dx.get(n-2)+dx.get(n-3))*(dx.get(n-2)+dx.get(n-3)),
                    -dx.get(n-3)*(dx.get(n-3)+dx.get(n-2)));
            tmp.set(n-1, -S.get(n-3)*dx.get(n-2)*dx.get(n-2) - S.get(n-2)*dx.get(n-3)*(3.0*dx.get(n-2)+2.0*dx.get(n-3)));
            break;
        case FirstDerivative:
            L.setLastRow(0.0, 1.0);
            tmp.set(n-1, rightValue);
            break;
        case SecondDerivative:
            L.setLastRow(1.0, 2.0);
            tmp.set(n-1, 3.0*S.get(n-2) + rightValue*dx.get(n-2)/2.0);
            break;
        case Periodic:
        case Lagrange:
            // ignoring end condition value
            throw new UnsupportedOperationException("this end condition is not implemented yet");
        default:
            throw new LibraryException("unknown end condition"); // QA:[RG]::verified //TODO: message
        }

        // solve the system
        tmp = L.solveFor(tmp);

        if (constrained) {
            double correction;
            double pm, pu, pd, M;
            for (i=0; i<n; i++) {
                if (i==0) {
                    if (tmp.get(i)*S.get(0)>0.0) {
                        correction = tmp.get(i)/Math.abs(tmp.get(i)) * Math.min(Math.abs(tmp.get(i)), Math.abs(3.0*S.get(0)));
                    } else {
                        correction = 0.0;
                    }
                    if (!Closeness.isClose(correction, tmp.get(i))) {
                        tmp.set(i, correction);
                        monotone = true;
                    }
                } else if (i==n-1) {
                    if (tmp.get(i)*S.get(n-2)>0.0) {
                        correction = tmp.get(i)/Math.abs(tmp.get(i)) * Math.min(Math.abs(tmp.get(i)), Math.abs(3.0*S.get(n-2)));
                    } else {
                        correction = 0.0;
                    }
                    if (!Closeness.isClose(correction, tmp.get(i))) {
                        tmp.set(i, correction);
                        monotone = true;
                    }
                } else {
                    pm=(S.get(i-1)*dx.get(i)+S.get(i)*dx.get(i-1))/ (dx.get(i-1)+dx.get(i));
                    M = 3.0 * Math.min(Math.min(Math.abs(S.get(i-1)), Math.abs(S.get(i))), Math.abs(pm));
                    if (i>1) {
                        if ((S.get(i-1)-S.get(i-2))*(S.get(i)-S.get(i-1))>0.0) {
                            pd=(S.get(i-1)*(2.0*dx.get(i-1)+dx.get(i-2))
                                    -S.get(i-2)*dx.get(i-1))/
                                    (dx.get(i-2)+dx.get(i-1));
                            if (pm*pd>0.0 && pm*(S.get(i-1)-S.get(i-2))>0.0) {
                                M = Math.max(M, 1.5*Math.min(Math.abs(pm), Math.abs(pd)));
                            }
                        }
                    }
                    if (i<n-2) {
                        if ((S.get(i)-S.get(i-1))*(S.get(i+1)-S.get(i))>0.0) {
                            pu=(S.get(i)*(2.0*dx.get(i)+dx.get(i+1))-S.get(i+1)*dx.get(i))/
                            (dx.get(i)+dx.get(i+1));
                            if (pm*pu>0.0 && -pm*(S.get(i)-S.get(i-1))>0.0) {
                                M = Math.max(M, 1.5*Math.min(Math.abs(pm), Math.abs(pu)));
                            }
                        }
                    }
                    if (tmp.get(i)*pm>0.0) {
                        correction = tmp.get(i)/Math.abs(tmp.get(i)) * Math.min(Math.abs(tmp.get(i)), M);
                    } else {
                        correction = 0.0;
                    }
                    if (! Closeness.isClose(correction, tmp.get(i)) ) {
                        tmp.set(i, correction);
                        monotone = true;
                    }
                }
            }
        }

        for (i=0; i<n-1; i++) {
            va.set(i, tmp.get(i) );
            vb.set(i, (3.0*S.get(i) - tmp.get(i+1) - 2.0*tmp.get(i))/dx.get(i) );
            vc.set(i, (tmp.get(i+1) + tmp.get(i) - 2.0*S.get(i))/(dx.get(i)*dx.get(i)) );
        }

        vp.set(0, 0.0);
        for (i=1; i<n-1; i++) {
            final double value = vp.get(i-1) + dx.get(i-1) * (vy.get(i-1)
                    + dx.get(i-1) * (va.get(i-1)/2.0 + dx.get(i-1) * (vb.get(i-1)/3.0 + dx.get(i-1) * vc.get(i-1)/4.0)));
            vp.set(i, value);
        }
    }


    @Override
    protected double opImpl(final double x) {
        final int j = locate(x);
        final double dx = x-vx.get(j);
        return vy.get(j) + dx*(va.get(j) + dx*(vb.get(j) + dx*vc.get(j)));
    }


    @Override
    protected double primitiveImpl(final double x) {
        final int j = locate(x);
        final double dx = x-vx.get(j);
        return vp.get(j) + dx*(vy.get(j) + dx*(va.get(j)/2.0 + dx*(vb.get(j)/3.0 + dx*vc.get(j)/4.0)));
    }


    @Override
    protected double derivativeImpl(final double x) {
        final int j = locate(x);
        final double dx = x-vx.get(j);
        return va.get(j) + (2.0*vb.get(j) + 3.0*vc.get(j)*dx)*dx;
    }


    @Override
    protected double secondDerivativeImpl(final double x) {
        final int j = locate(x);
        final double dx = x-vx.get(j);
        return 2.0*vb.get(j) + 6.0*vc.get(j)*dx;
    }


    //
    // private inner classes
    //


    /**
     * This class is a default implementation for {@link CubicSplineInterpolation} instances.
     *
     * @author Richard Gomes
     * @author Daniel Kong
     *
     */
    private class CubicSplineInterpolationImpl implements Interpolator {
        private final CubicSplineInterpolation delegate;

        public CubicSplineInterpolationImpl(final CubicSplineInterpolation delegate) {
            this.delegate = delegate;
        }

        @Override
        public final Interpolation interpolate(final ConstIterator x, final ConstIterator y) /* @ReadOnly */ {
            return interpolate(x.size(), x, y);
        }

        @Override
        public final Interpolation interpolate(final int size, final ConstIterator x, final ConstIterator y) /* @ReadOnly */ {
            delegate.vx = x.iterator(0, size);
            delegate.vy = y.iterator(0, size);
            n = vx.size();
            vp = new Array(n-1);
            va = new Array(n-1);
            vb = new Array(n-1);
            vc = new Array(n-1);
            delegate.update();
            return delegate;
        }

        @Override
        public final boolean global() {
            return true; // only CubicSpline and Sabr are global, whatever it means!
        }

    }

}

