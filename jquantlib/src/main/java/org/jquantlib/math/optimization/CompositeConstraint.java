package org.jquantlib.math.optimization;

import org.jquantlib.math.matrixutilities.Array;

//TODO: comments, license, code review
public class CompositeConstraint extends Constraint {
    private final Constraint c1, c2;

    public CompositeConstraint(final Constraint c1, final Constraint c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public boolean test(final Array array) /* @ReadOnly */ {
        return c1.test(array) && c2.test(array);
    }

}
