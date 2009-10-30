package org.jquantlib.instruments;

import org.jquantlib.QL;
import org.jquantlib.math.Ops.DoubleOp;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.quotes.SimpleQuote;

public class PriceError implements DoubleOp {

    private final PricingEngine engine;
    private final Instrument.ResultsImpl results;
    private final SimpleQuote vol;
    private final double targetValue;


    public PriceError(final PricingEngine engine, final SimpleQuote vol, final double targetValue) {
        this.engine = engine;
        this.vol = vol;
        this.targetValue = targetValue;
        this.results = (Instrument.ResultsImpl) engine.getResults();
        QL.require(results != null, "pricing engine does not supply needed results"); // TODO: message
    }


    //
    // implements Ops.DoubleOp
    //

    @Override
    public double op(/*@Volatility*/ final double x) /* @ReadOnly */ {
        vol.setValue(x);
        engine.calculate();

        return results.value - targetValue;
    }

}
