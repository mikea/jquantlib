package org.jquantlib.pricingengines.swap;

import org.jquantlib.Validate;
import org.jquantlib.cashflow.CashFlows;
import org.jquantlib.math.Constants;
import org.jquantlib.pricingengines.SwapEngine;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

public class DiscountingSwapEngine extends SwapEngine implements Observer {

    private Handle<YieldTermStructure> discountCurve;
    
    public DiscountingSwapEngine(final Handle<YieldTermStructure> discountCurve) /* @ReadOnly */ {
        this.discountCurve = discountCurve;
        
        // TODO: code review :: please verify against original QL/C++ code
        this.discountCurve.getLink().addObserver(this);
    }

    @Override
    public void calculate() /* @ReadOnly */ {
        Validate.QL_REQUIRE(!discountCurve.empty(), "no discounting term structure set");

        results.value = 0.0;
        results.errorEstimate = Constants.NULL_Double;
        results.legNPV = new double[arguments.legs.size()];
        results.legBPS = new double[arguments.legs.size()];
        for (int i=0; i<arguments.legs.size(); ++i) {
            results.legNPV[i] = arguments.payer[i] * CashFlows.getInstance().npv(arguments.legs.get(i), discountCurve);
            results.legBPS[i] = arguments.payer[i] * CashFlows.getInstance().bps(arguments.legs.get(i), discountCurve);
            results.value += results.legNPV[i];
        }
     }

    @Override
    // TODO: code review :: please verify against original QL/C++ code
    public void update(Observable o, Object arg) {
        // TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
    }    
    
}
