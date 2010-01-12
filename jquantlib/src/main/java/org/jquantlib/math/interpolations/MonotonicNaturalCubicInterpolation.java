package org.jquantlib.math.interpolations;

import org.jquantlib.math.matrixutilities.Array;

public class MonotonicNaturalCubicInterpolation extends CubicInterpolation {

    /**
     * @pre the \f$ x \f$ values must be sorted.
     */
    public MonotonicNaturalCubicInterpolation(final Array vx, final Array vy) {
        super(vx, vy, DerivativeApprox.Spline, true, BoundaryCondition.SecondDerivative, 0.0, BoundaryCondition.SecondDerivative, 0.0);
    }
}
