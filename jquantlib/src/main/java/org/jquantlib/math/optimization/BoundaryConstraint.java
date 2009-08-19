package org.jquantlib.math.optimization;

import org.jquantlib.math.matrixutilities.Array;

//TODO: comments, formatting

public class BoundaryConstraint extends Constraint {
    private final double low, high; // inclusive!

    public BoundaryConstraint(final double low, final double high) {
        this.low = low;
        this.high = high;
    }

    @Override
    public boolean test(final Array array) /* @ReadOnly */ {
        for (int i=0; i<array.size(); i++)
            if ((array.get(i) < low) || (array.get(i) > high))
                return false;
        return true;
    }

}
