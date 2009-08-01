package org.jquantlib.math.optimization;

import org.jquantlib.math.matrixutilities.Array;

//TODO: comments, license, code review
public class NoConstraint extends Constraint {

    @Override
    public boolean test(final Array array) /* @ReadOnly */ {
        return true;
    }

}
