package org.jquantlib.pricingengines.bond;

import org.jquantlib.pricingengines.BondEngine;
import org.jquantlib.pricingengines.arguments.BondArguments;
import org.jquantlib.pricingengines.results.BondResults;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;

public class DiscountingBondEngine extends BondEngine {
	
	private Handle<YieldTermStructure> discountCurve_;

	
	public DiscountingBondEngine(){
		this(new Handle<YieldTermStructure>(YieldTermStructure.class));
	}

    public DiscountingBondEngine(final Handle<YieldTermStructure>  discountCurve){
    	//FIXME: correct?
    	super(new BondArguments(), new BondResults());
    }
    
    @Override
    public void calculate(){
    	throw new UnsupportedOperationException();
//    	const Leg& cashflows = arguments_.cashflows;
//        const Date& settlementDate = arguments_.settlementDate;
//
//        Date valuationDate = (*discountCurve())->referenceDate();
//
//        QL_REQUIRE(!discountCurve().empty(),
//                   "no discounting term structure set");
//        results_.value = CashFlows::npv(cashflows,
//                                        **discountCurve(),
//                                        valuationDate, valuationDate);
//        results_.settlementValue = CashFlows::npv(cashflows,
//                                                  **discountCurve(),
//                                                  settlementDate,
//                                                  settlementDate);
    }
    
    
    public Handle<YieldTermStructure> discountCurve(){
    	return discountCurve_;
    }
}
