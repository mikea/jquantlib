package org.jquantlib.math.optimization;

import org.jquantlib.math.Array;

//TODO: comments, license, code review
public class NoConstraint extends Constraint {

    public boolean test(Array nocArray) /*@ReadOnly*/ {
        return true;
    }

}
