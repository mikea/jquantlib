package org.jquantlib.instruments;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * Dummy payoff class.
 */
public class NullPayoff extends Payoff {

    //
    // overrides Payoff
    //

    @Override
    public String name() /* @ReadOnly */ {
        return "Null";
    }

    @Override
    public String description() /* @ReadOnly */ {
        return name();
    }

    @Override
    public final double get(final double price) /* @ReadOnly */ {
        throw new LibraryException("dummy payoff given");
    }

    @Override
    public void accept(final TypedVisitor<Payoff> v) {
        final Visitor<Payoff> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
        if (v1 != null) {
            v1.visit(this);
        } else {
            super.accept(v);
        }
    }

}
