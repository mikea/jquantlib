package org.jquantlib.math.optimization;

import org.jquantlib.math.matrixutilities.Array;

//TODO: comments, license, code review
public class PositiveConstraint extends Constraint {

    @Override
    public boolean test(final Array array) /* @ReadOnly */ {
        for (int i = 0; i < array.size(); ++i)
            if (array.get(i) <= 0.0)
                return false;
        return true;
    }

}
