package org.jquantlib.math.optimization;

import org.jquantlib.math.Array;

//TODO: comments, formatting

public class BoundaryConstraint extends Constraint {
    private double low, high; // inclusive!

    public BoundaryConstraint(double low, double high) {
        this.low = low;
        this.high = high;
    }

    public boolean test(final Array array) /* @ReadOnly */ {
        for (int i=0; i<array.length; i++) {
            if ((array.get(i) < low) || (array.get(i) > high))
                return false;
        }
        return true;
    }

}
