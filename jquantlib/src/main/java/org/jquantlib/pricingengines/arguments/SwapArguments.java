/**
 *
 */
package org.jquantlib.pricingengines.arguments;

import java.util.List;

import org.jquantlib.cashflow.Leg;

// TODO: code review :: object model needs to be validated and eventually refactored
public class SwapArguments extends Arguments {
    public List<Leg> legs;
    public double[] payer;

    @Override
    public void validate() /* @ReadOnly */ {
        assert legs.size() == payer.length : "number of legs and multipliers differ";
    }
}