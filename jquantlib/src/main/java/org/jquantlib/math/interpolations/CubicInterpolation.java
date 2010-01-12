/*
 Copyright (C) 2009 Richard Gomes

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

import java.util.Arrays;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.math.interpolations.factories.Cubic;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.methods.finitedifferences.TridiagonalOperator;

/**
 * Cubic interpolation between discrete points.
 * <p>
 * Cubic interpolation is fully defined when the ${f_i}$ function values
 * at points ${x_i}$ are supplemented with ${f_i}$ function derivative
 * values.
 * <p>
 * Different type of first derivative approximations are implemented, both
 * local and non-local. Local schemes (Fourth-order, Parabolic,
 * Modified Parabolic, Fritsch-Butland, Akima, Kruger) use only $f$ values
 * near $x_i$ to calculate $f_i$. Non-local schemes (Spline with different
 * boundary conditions) use all ${f_i}$ values and obtain ${f_i}$ by
 * solving a linear system of equations. Local schemes produce $C^1$
 * interpolants, while the spline scheme generates $C^2$ interpolants.
 * <p>
 * Hyman's monotonicity constraint filter is also implemented: it can be
 * applied to all schemes to ensure that in the regions of local
 * monotoniticity of the input (three successive increasing or decreasing
 * values) the interpolating cubic remains monotonic. If the interpolating
 * cubic is already monotonic, the Hyman filter leaves it unchanged
 * preserving all its original features.
 * <p>
 * In the case of $C^2$ interpolants the Hyman filter ensures local
 * monotonicity at the expense of the second derivative of the interpolant
 * which will no longer be continuous in the points where the filter has
 * been applied.
 * <p>
 * While some non-linear schemes (Modified Parabolic, Fritsch-Butland,
 * Kruger) are guaranteed to be locally monotone in their original
 * approximation, all other schemes must be filtered according to the
 * Hyman criteria at the expense of their linearity.
 * <p>
 * See R. L. Dougherty, A. Edelman, and J. M. Hyman,
 * "Nonnegativity-, Monotonicity-, or Convexity-Preserving CubicSpline and
 * Quintic Hermite Interpolation"
 * Mathematics Of Computation, v. 52, n. 186, April 1989, pp. 471-494.
 * <p>
 * @todo implement missing schemes (FourthOrder and ModifiedParabolic) and
 *       missing boundary conditions (Periodic and Lagrange).
 *
 * @test to be adapted from old ones.
 *
 * @author Richard Gomes
 */
//TODO: rename to CubicInterpolation
public class CubicInterpolation extends AbstractInterpolation {

    //
    // public inner enums
    //

    public enum DerivativeApprox {
        /**
         * Spline approximation (non-local, non-monotone, linear[?]).
         * Different boundary conditions can be used on the left and right
         * boundaries: see BoundaryCondition.
         */
        Spline,

        /**
         * Fourth-order approximation (local, non-monotone, linear)
         */
        FourthOrder,

        /**
         * Parabolic approximation (local, non-monotone, linear)
         */
        Parabolic,

        /**
         * Modified-Parabolic approximation (local, monotone, non-linear)
         */
        ModifiedParabolic,

        /**
         * Fritsch-Butland approximation (local, monotone, non-linear)
         */
        FritschButland,

        /**
         * Akima approximation (local, non-monotone, non-linear)
         */
        Akima,

        /**
         * Kruger approximation (local, monotone, non-linear)
         */
        Kruger
    }

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
    // private final fields
    //

    private final CoefficientHolder coeffs;


    //
    // public constructors
    //

    /**
     * Constructor for a CubicSpline interpolation.
     * <p>
     * Interpolations are not instantiated directly by applications, but via a factory class.
     *
     * @see Cubic
     */
    public CubicInterpolation(
            final Array vx,
            final Array vy,
            final DerivativeApprox da,
            final boolean monotonic,
            final BoundaryCondition leftCondition,
            final double leftConditionValue,
            final BoundaryCondition rightCondition,
            final double rightConditionValue) {
        super.impl = new CubicInterpolationImpl(
                vx, vy,
                da, monotonic,
                leftCondition, leftConditionValue,
                rightCondition, rightConditionValue);
        super.impl.update();
        this.coeffs = ((CubicInterpolationImpl)impl).coeffs;
    }


    //
    // public methods
    //

    public Array aCoefficients() /* @ReadOnly */ {
        return coeffs.a_.clone();
    }

    public Array bCoefficients() /* @ReadOnly */ {
        return coeffs.b_.clone();
    }

    public Array cCoefficients() /* @ReadOnly */ {
        return coeffs.c_.clone();
    }

    public boolean[] monotonicityAdjustments() /* @ReadOnly */ {
        return coeffs.monotonicityAdjustments_.clone();
    }


    //
    // private inner classes
    //

    private class CoefficientHolder {
        // P[i](x) = y[i] +
        //           a[i]*(x-x[i]) +
        //           b[i]*(x-x[i])^2 +
        //           c[i]*(x-x[i])^3
        private final Array     primitiveConst_;

        private final int       n_;
        private final Array     a_;
        private final Array     b_;
        private final Array     c_;
        private final boolean[] monotonicityAdjustments_;

        public CoefficientHolder(final int n) {
            this.n_ = n;
            this.primitiveConst_ = new Array(n-1);
            this.a_ = new Array(n-1);
            this.b_ = new Array(n-1);
            this.c_ = new Array(n-1);
            this.monotonicityAdjustments_ = new boolean[n];
        }
    }


    private class CubicInterpolationImpl extends AbstractInterpolation.Impl {

        //
        // private final fields
        //

        private final DerivativeApprox da;
        private final boolean monotonic;
        private final BoundaryCondition leftType;
        private final BoundaryCondition rightType;
        private final double leftValue;
        private final double rightValue;
        private final CoefficientHolder coeffs;

        // variables defined for convenience
        private final int n;
        private final Array va;
        private final Array vb;
        private final Array vc;
        private final Array vp;
        private final boolean[] ma;


        //
        // protected constructors
        //

        protected CubicInterpolationImpl(
                final Array vx,
                final Array vy,
                final DerivativeApprox da,
                final boolean monotonic,
                final CubicInterpolation.BoundaryCondition leftCondition,
                final double leftConditionValue,
                final CubicInterpolation.BoundaryCondition rightCondition,
                final double rightConditionValue) {
            super(vx, vy);
            this.coeffs      = new CoefficientHolder(vx.size());

            this.da = da;
            this.monotonic = monotonic;
            this.leftType    = leftCondition;
            this.rightType   = rightCondition;
            this.leftValue   = leftConditionValue;
            this.rightValue  = rightConditionValue;

            // Convenience variables
            this.n = coeffs.n_;
            this.va = coeffs.a_;
            this.vb = coeffs.b_;
            this.vc = coeffs.c_;
            this.vp = coeffs.primitiveConst_;
            this.ma = coeffs.monotonicityAdjustments_;
        }


        //
        // overrides AbstractInterpolation.Impl
        //

        @Override
        public void update() {

            // helper variables
            final double[] x = vx.toDoubleArray();
            final double[] y = vy.toDoubleArray();
            final double[] a = va.toDoubleArray();
            final double[] b = vb.toDoubleArray();
            final double[] c = vc.toDoubleArray();
            final double[] p = vp.toDoubleArray();

            // temporary variables
            final double[] dx  = new double[n-1];
            final double[] S   = new double[n-1];
            double[] tmp = new double[n];

            for (int i=0; i<n-1; ++i) {
                dx[i] = x[i+1] - x[i];
                S[i] = (y[i+1] - y[i])/dx[i];
            }

            // first derivative approximation
            if (da==CubicInterpolation.DerivativeApprox.Spline) {
                final TridiagonalOperator L = new TridiagonalOperator(n);
                for (int i=1; i<n-1; ++i) {
                    L.setMidRow(i, dx[i], 2.0*(dx[i]+dx[i-1]), dx[i-1]);
                    tmp[i] = 3.0*(dx[i]*S[i-1] + dx[i-1]*S[i]);
                }

                // left boundary condition
                switch (leftType) {
                  case NotAKnot:
                    // ignoring end condition value
                    L.setFirstRow(dx[1]*(dx[1]+dx[0]),
                                  (dx[0]+dx[1])*(dx[0]+dx[1]));
                    tmp[0] = S[0]*dx[1]*(2.0*dx[1]+3.0*dx[0]) +
                             S[1]*dx[0]*dx[0];
                    break;
                  case FirstDerivative:
                    L.setFirstRow(1.0, 0.0);
                    tmp[0] = leftValue;
                    break;
                  case SecondDerivative:
                    L.setFirstRow(2.0, 1.0);
                    tmp[0] = 3.0*S[0] - leftValue*dx[0]/2.0;
                    break;
                  case Periodic:
                  case Lagrange:
                    // ignoring end condition value
                    throw new LibraryException("this end condition is not implemented yet");
                  default:
                    throw new LibraryException("unknown end condition");
                }

                // right boundary condition
                switch (rightType) {
                  case NotAKnot:
                    // ignoring end condition value
                    L.setLastRow(-(dx[n-2]+dx[n-3])*(dx[n-2]+dx[n-3]),
                                 -dx[n-3]*(dx[n-3]+dx[n-2]));
                    tmp[n-1] = -S[n-3]*dx[n-2]*dx[n-2] -
                                 S[n-2]*dx[n-3]*(3.0*dx[n-2]+2.0*dx[n-3]);
                    break;
                  case FirstDerivative:
                    L.setLastRow(0.0, 1.0);
                    tmp[n-1] = rightValue;
                    break;
                  case SecondDerivative:
                    L.setLastRow(1.0, 2.0);
                    tmp[n-1] = 3.0*S[n-2] + rightValue*dx[n-2]/2.0;
                    break;
                  case Periodic:
                  case Lagrange:
                    // ignoring end condition value
                    throw new LibraryException("this end condition is not implemented yet");
                  default:
                    throw new LibraryException("unknown end condition");
                }

                // solve the system
                tmp = L.solveFor(new Array(tmp)).toDoubleArray();
            } else { // local schemes
                if (n==2) {
                    tmp[0] = tmp[1] = S[0];
                } else {
                    switch (da) {
                        case FourthOrder:
                            throw new LibraryException("FourthOrder not implemented yet");
                        case Parabolic:
                            throw new LibraryException("Parabolic not implemented yet");
                        case ModifiedParabolic:
                            throw new LibraryException("ModifiedParabolic not implemented yet");
                        case FritschButland:
                            throw new LibraryException("FritschButland not implemented yet");
                        case Akima:
                            throw new LibraryException("Akima not implemented yet");
                        case Kruger:
                            // intermediate points
                            for (int i=1; i<n-1; ++i) {
                                if (S[i-1]*S[i]<0.0) {
                                    tmp[i] = 0.0;
                                } else {
                                    tmp[i] = 2.0/(1.0/S[i-1]+1.0/S[i]);
                                }
                            }
                            // end points
                            tmp[0] = (3.0*S[0]-tmp[1])/2.0;
                            tmp[n-1] = (3.0*S[n-2]-tmp[n-2])/2.0;
                            break;
                        default:
                            throw new LibraryException("unknown scheme");
                    }
                }
            }

            Arrays.fill(ma, false);
            // Hyman monotonicity constrained filter
            if (monotonic) {
                double correction;
                double pm, pu, pd, M;
                for (int i=0; i<n; ++i) {
                    if (i==0) {
                        if (tmp[i]*S[0]>0.0) {
                            correction = tmp[i]/Math.abs(tmp[i]) * Math.min(Math.abs(tmp[i]), Math.abs(3.0*S[0]));
                        } else {
                            correction = 0.0;
                        }
                        if (correction!=tmp[i]) {
                            tmp[i] = correction;
                            ma[i] = true;
                        }
                    } else if (i==n-1) {
                        if (tmp[i]*S[n-2]>0.0) {
                            correction = tmp[i]/Math.abs(tmp[i]) * Math.min(Math.abs(tmp[i]), Math.abs(3.0*S[n-2]));
                        } else {
                            correction = 0.0;
                        }
                        if (correction!=tmp[i]) {
                            tmp[i] = correction;
                            ma[i] = true;
                        }
                    } else {
                        pm=(S[i-1]*dx[i]+S[i]*dx[i-1])/
                            (dx[i-1]+dx[i]);
                        M = 3.0 * Math.min(Math.min(Math.abs(S[i-1]), Math.abs(S[i])), Math.abs(pm));
                        if (i>1) {
                            if ((S[i-1]-S[i-2])*(S[i]-S[i-1])>0.0) {
                                pd=(S[i-1]*(2.0*dx[i-1]+dx[i-2])
                                    -S[i-2]*dx[i-1])/
                                    (dx[i-2]+dx[i-1]);
                                if (pm*pd>0.0 && pm*(S[i-1]-S[i-2])>0.0) {
                                    M = Math.max(M, 1.5*Math.min(Math.abs(pm),Math.abs(pd)));
                                }
                            }
                        }
                        if (i<n-2) {
                            if ((S[i]-S[i-1])*(S[i+1]-S[i])>0.0) {
                                pu=(S[i]*(2.0*dx[i]+dx[i+1])-S[i+1]*dx[i])/
                                    (dx[i]+dx[i+1]);
                                if (pm*pu>0.0 && -pm*(S[i]-S[i-1])>0.0) {
                                    M = Math.max(M, 1.5*Math.min(Math.abs(pm),Math.abs(pu)));
                                }
                            }
                        }
                        if (tmp[i]*pm>0.0) {
                            correction = tmp[i]/Math.abs(tmp[i]) *
                                Math.min(Math.abs(tmp[i]), M);
                        } else {
                            correction = 0.0;
                        }
                        if (correction!=tmp[i]) {
                            tmp[i] = correction;
                            ma[i] = true;
                        }
                    }
                }
            }


            // cubic coefficients
            for (int i=0; i<n-1; ++i) {
                a[i] = tmp[i];
                b[i] = (3.0*S[i] - tmp[i+1] - 2.0*tmp[i])/dx[i];
                c[i] = (tmp[i+1] + tmp[i] - 2.0*S[i])/(dx[i]*dx[i]);
            }

            p[0] = 0.0;
            for (int i=1; i<n-1; ++i) {
                p[i] = p[i-1] + dx[i-1] * (y[i-1] + dx[i-1] * (a[i-1]/2.0 + dx[i-1] * (b[i-1]/3.0 + dx[i-1] * c[i-1]/4.0)));
            }
        }


        @Override
        public double op(final double x) {
            final int j = locate(x);
            final double dx = x - vx.get(j);
            return vy.get(j) + dx*(va.get(j) + dx*(vb.get(j) + dx*vc.get(j)));
        }


        @Override
        public double primitive(final double x) {
            final int j = locate(x);
            final double dx = x - vx.get(j);
            return vp.get(j) + dx*(vy.get(j) + dx*(va.get(j)/2.0 + dx*(vb.get(j)/3.0 + dx*vc.get(j)/4.0)));
        }


        @Override
        public double derivative(final double x) {
            final int j = locate(x);
            final double dx = x - vx.get(j);
            return va.get(j) + (2.0*vb.get(j) + 3.0*vc.get(j)*dx)*dx;
        }


        @Override
        public double secondDerivative(final double x) {
            final int j = locate(x);
            final double dx = x - vx.get(j);
            return 2.0*vb.get(j) + 6.0*vc.get(j)*dx;
        }

    }

}
