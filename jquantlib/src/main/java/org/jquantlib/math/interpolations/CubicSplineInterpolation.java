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

import it.unimi.dsi.fastutil.doubles.DoubleArrays;

import org.jquantlib.math.Array;
import org.jquantlib.math.interpolations.factories.CubicSpline;
import org.jquantlib.methods.finitedifferences.TridiagonalOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 */
// TEST : needs code review and test classes
public class CubicSplineInterpolation extends AbstractInterpolation {
    
    private final static Logger logger = LoggerFactory.getLogger(CubicSplineInterpolation.class);

    
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
    private double[] vp;
    private double[] va;
    private double[] vb;
    private double[] vc;
    private boolean  monotone;
    
    
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
        CubicSplineInterpolation cubicSpline = new CubicSplineInterpolation(
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
        
        n = vx.length;
        vp = new double[n-1];
        va = new double[n-1];
        vb = new double[n-1];
        vc = new double[n-1];
        
        this.leftType  = leftCondition;
        this.rightType = rightCondition;
        this.leftValue  = leftConditionValue;
        this.rightValue = rightConditionValue;
        this.monotone = false;
        this.constrained = monotonicityConstraint;
    }
    
    
    //
    // implements Interpolation
    //
    
    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public void update() {
        reload();
    }

    
    /**
     * {@inheritDoc}
     * 
     * @note Class factory is responsible for initializing <i>vx</i> and <i>vy</i>  
     */
    @Override
    public void reload() {

        final TridiagonalOperator L = new TridiagonalOperator(n);
        Array tmp = new Array(n);
        final double[] dx  = new double[n];
        final double[] S   = new double[n];
        
        int i=0;
        dx[i] = vx[i+1] - vx[i];
        S[i] = (vy[i+1] - vy[i])/dx[i];
        for (i=1; i<n-1; i++) {
            dx[i] = vx[i+1] - vx[i];
            S[i] = (vy[i+1] - vy[i])/dx[i];
            L.setMidRow(i, dx[i], 2.0*(dx[i]+dx[i-1]), dx[i-1]);
            tmp.set(i, 3.0*(dx[i]*S[i-1] + dx[i-1]*S[i]));
        }
        
        // **** BOUNDARY CONDITIONS ****
      
        // left condition
        switch (leftType) {
            case NotAKnot:
                // ignoring end condition value
                L.setFirstRow(dx[1]*(dx[1]+dx[0]),
                            (dx[0]+dx[1])*(dx[0]+dx[1]));
                tmp.set(0, S[0]*dx[1]*(2.0*dx[1]+3.0*dx[0]) + S[1]*dx[0]*dx[0]);
                break;
            case FirstDerivative:
                L.setFirstRow(1.0, 0.0);
                tmp.set(0, leftValue);
                break;
            case SecondDerivative:
                L.setFirstRow(2.0, 1.0);
                tmp.set(0, 3.0*S[0] - leftValue*dx[0]/2.0);
                break;
            case Periodic:
            case Lagrange:
                // ignoring end condition value
                throw new UnsupportedOperationException("this end condition is not implemented yet");
            default:
                throw new UnsupportedOperationException("unknown end condition");
        }
        
        // right condition
        switch (rightType) {
            case NotAKnot:
                // ignoring end condition value
                L.setLastRow(-(dx[n-2]+dx[n-3])*(dx[n-2]+dx[n-3]),
                             -dx[n-3]*(dx[n-3]+dx[n-2]));
                tmp.set(n-1, -S[n-3]*dx[n-2]*dx[n-2] - S[n-2]*dx[n-3]*(3.0*dx[n-2]+2.0*dx[n-3]));
                break;
            case FirstDerivative:
                L.setLastRow(0.0, 1.0);
                tmp.set(n-1, rightValue);
                break;
            case SecondDerivative:
                L.setLastRow(1.0, 2.0);
                tmp.set(n-1, 3.0*S[n-2] + rightValue*dx[n-2]/2.0);
                break;
            case Periodic:
            case Lagrange:
                // ignoring end condition value
                throw new UnsupportedOperationException("this end condition is not implemented yet");
            default:
                  throw new UnsupportedOperationException("unknown end condition");
        }

        // solve the system
        tmp = L.solveFor(tmp);
  
        if (constrained) {
            double correction;
            double pm, pu, pd, M;
            for (i=0; i<n; i++) {
                if (i==0) {
                    if (tmp.get(i)*S[0]>0.0) {
                        correction = tmp.get(i)/Math.abs(tmp.get(i)) * Math.min(Math.abs(tmp.get(i)), Math.abs(3.0*S[0]));
                    } else {
                        correction = 0.0;
                    }
                    if (correction!=tmp.get(i)) {
                        tmp.set(i, correction);
                        monotone = true;
                    }
                } else if (i==n-1) {
                    if (tmp.get(i)*S[n-2]>0.0) {
                        correction = tmp.get(i)/Math.abs(tmp.get(i)) * Math.min(Math.abs(tmp.get(i)), Math.abs(3.0*S[n-2]));
                    } else {
                        correction = 0.0;
                    }
                    if (correction!=tmp.get(i)) {
                        tmp.set(i, correction);
                        monotone = true;
                    }
                } else {
                    pm=(S[i-1]*dx[i]+S[i]*dx[i-1])/ (dx[i-1]+dx[i]);
                    M = 3.0 * Math.min(Math.min(Math.abs(S[i-1]), Math.abs(S[i])), Math.abs(pm));
                    if (i>1) {
                        if ((S[i-1]-S[i-2])*(S[i]-S[i-1])>0.0) {
                            pd=(S[i-1]*(2.0*dx[i-1]+dx[i-2])
                                  -S[i-2]*dx[i-1])/
                                  (dx[i-2]+dx[i-1]);
                            if (pm*pd>0.0 && pm*(S[i-1]-S[i-2])>0.0) {
                                M = Math.max(M, 1.5*Math.min(Math.abs(pm), Math.abs(pd)));
                            }
                        }
                    }
                    if (i<n-2) {
                        if ((S[i]-S[i-1])*(S[i+1]-S[i])>0.0) {
                            pu=(S[i]*(2.0*dx[i]+dx[i+1])-S[i+1]*dx[i])/
                                  (dx[i]+dx[i+1]);
                            if (pm*pu>0.0 && -pm*(S[i]-S[i-1])>0.0) {
                                M = Math.max(M, 1.5*Math.min(Math.abs(pm), Math.abs(pu)));
                            }
                        }
                    }
                    if (tmp.get(i)*pm>0.0) {
                        correction = tmp.get(i)/Math.abs(tmp.get(i)) * Math.min(Math.abs(tmp.get(i)), M);
                    } else {
                        correction = 0.0;
                    }
                    if (correction!=tmp.get(i)) {
                        tmp.set(i, correction);
                        monotone = true;
                    }
                }
            }
        }
            
        for (i=0; i<n-1; i++) {
            va[i] = tmp.get(i);
            vb[i] = (3.0*S[i] - tmp.get(i+1) - 2.0*tmp.get(i))/dx[i];
            vc[i] = (tmp.get(i+1) + tmp.get(i) - 2.0*S[i])/(dx[i]*dx[i]);
        }

        vp[0] = 0.0;
        for (i=1; i<n-1; i++) {
            vp[i] = vp[i-1] 
                + dx[i-1] *
                (vy[i-1] + dx[i-1] *
                 (va[i-1]/2.0 + dx[i-1] *
                  (vb[i-1]/3.0 + dx[i-1] * vc[i-1]/4.0)));
        }
    }
    
    
    @Override
    protected double evaluateImpl(double x) {
        int j = locate(x);
        double dx = x-vx[j];
        return vy[j] + dx*(va[j] + dx*(vb[j] + dx*vc[j]));
    }


    @Override
    protected double primitiveImpl(double x) {
        int j = locate(x);
        double dx = x-vx[j];
        return vp[j]
            + dx*(vy[j] + dx*(va[j]/2.0
            + dx*(vb[j]/3.0 + dx*vc[j]/4.0)));
    }


    @Override
    protected double derivativeImpl(double x) {
        int j = locate(x);
        double dx = x-vx[j];
        return va[j] + (2.0*vb[j] + 3.0*vc[j]*dx)*dx;
    }


    @Override
    protected double secondDerivativeImpl(double x) {
        int j = locate(x);
        double dx = x-vx[j];
        return 2.0*vb[j] + 6.0*vc[j]*dx;
    }
    
    
    
    //
    // inner classes
    //
    

    /**
     * This class is a default implementation for {@link CubicSplineInterpolation} instances.
     * 
     * @author Richard Gomes
     */
    private class CubicSplineInterpolationImpl implements Interpolator {
        private CubicSplineInterpolation delegate;
        
        public CubicSplineInterpolationImpl(final CubicSplineInterpolation delegate) {
            this.delegate = delegate;
        }
        
        public final Interpolation interpolate(final double[] x, final double[] y) /* @ReadOnly */ {
            return interpolate(x.length, x, y);
        }

        public final Interpolation interpolate(final int size, final double[] x, final double[] y) /* @ReadOnly */ {
            delegate.vx = DoubleArrays.copy(x, 0, size);
            delegate.vy = DoubleArrays.copy(y, 0, size);
            delegate.reload();
            return delegate;
        }
        
        public final boolean isGlobal() {
            return true; // only CubicSpline and Sabr are global, whatever it means!
        }
        
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
    
}

