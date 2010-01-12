package org.jquantlib.math.interpolations;

import org.jquantlib.math.matrixutilities.Array;

public class NaturalCubicInterpolation extends CubicInterpolation {

    /**
     * @pre the \f$ x \f$ values must be sorted.
     */
    public NaturalCubicInterpolation(final Array vx, final Array vy) {
        super(vx, vy, DerivativeApprox.Spline, false, BoundaryCondition.SecondDerivative, 0.0, BoundaryCondition.SecondDerivative, 0.0);
    }
}
