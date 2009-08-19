package org.jquantlib.pricingengines.swap;

import org.jquantlib.QL;
import org.jquantlib.cashflow.CashFlows;
import org.jquantlib.math.Constants;
import org.jquantlib.pricingengines.SwapEngine;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

public class DiscountingSwapEngine extends SwapEngine implements Observer {

    private final Handle<YieldTermStructure> discountCurve;

    public DiscountingSwapEngine(final Handle<YieldTermStructure> discountCurve) /* @ReadOnly */ {
        this.discountCurve = discountCurve;

        // TODO: code review :: please verify against QL/C++ code
        this.discountCurve.getLink().addObserver(this);
    }

    @Override
    public void calculate() /* @ReadOnly */ {
        QL.require(!discountCurve.empty() , "no discounting term structure set"); // QA:[RG]::verified // TODO: message

        results.value = 0.0;
        results.errorEstimate = Constants.NULL_REAL;
        results.legNPV = new double[arguments.legs.size()];
        results.legBPS = new double[arguments.legs.size()];
        for (int i=0; i<arguments.legs.size(); ++i) {
            results.legNPV[i] = arguments.payer[i] * CashFlows.getInstance().npv(arguments.legs.get(i), discountCurve);
            results.legBPS[i] = arguments.payer[i] * CashFlows.getInstance().bps(arguments.legs.get(i), discountCurve);
            results.value += results.legNPV[i];
        }
     }

    @Override
    // TODO: code review :: please verify against QL/C++ code
    public void update(final Observable o, final Object arg) {
        // TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
    }

}
