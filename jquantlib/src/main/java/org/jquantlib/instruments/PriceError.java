package org.jquantlib.instruments;

import org.jquantlib.QL;
import org.jquantlib.math.Ops.DoubleOp;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.quotes.SimpleQuote;

public class PriceError implements DoubleOp {

    private final PricingEngine engine_;
    private final Results results_;
    private final SimpleQuote vol_;
    private final double targetValue_;


    public PriceError(final PricingEngine engine, final SimpleQuote vol, final double targetValue) {
        this.engine_ = engine;
        this.vol_ = vol;
        this.targetValue_ = targetValue;
        this.results_ = engine_.getResults();
        QL.require(results_ != null, "pricing engine does not supply needed results"); // TODO: message
    }


    //
    // implements Ops.DoubleOp
    //

    @Override
    public double op(/*@Volatility*/ final double x) /* @ReadOnly */ {
        vol_.setValue(x);
        engine_.calculate();
        return results_.value - targetValue_;
    }

}
