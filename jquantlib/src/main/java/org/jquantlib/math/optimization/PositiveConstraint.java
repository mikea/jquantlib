package org.jquantlib.math.optimization;

import org.jquantlib.math.Array;

//TODO: comments, license, code review
public class PositiveConstraint extends Constraint {

    public PositiveConstraint() {
    }

    public boolean test(final Array params) {
        for (int i = 0; i < params.length; ++i) {
            if (params.get(i) <= 0.0)
                return false;
        }
        return true;
    }

}
