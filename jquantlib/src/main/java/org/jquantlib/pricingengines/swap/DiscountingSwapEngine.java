package org.jquantlib.pricingengines.swap;

import org.jquantlib.instruments.Swap;
import org.jquantlib.instruments.Swap.Arguments;
import org.jquantlib.instruments.Swap.Results;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;

public class DiscountingSwapEngine extends Swap.Engine {
    
        public DiscountingSwapEngine(Swap swap, Arguments arguments, Results results) {
        swap.super(arguments, results);
        // TODO Auto-generated constructor stub
        }
        public DiscountingSwapEngine(Handle<YieldTermStructure> termStructure) {
            new Swap().super();//super();
            //swap.super(arguments, results);
            // TODO Auto-generated constructor stub
         }

        private Handle<YieldTermStructure> discountCurve_;
}
