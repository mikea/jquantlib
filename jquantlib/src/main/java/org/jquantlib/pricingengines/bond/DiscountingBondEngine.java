package org.jquantlib.pricingengines.bond;

import org.jquantlib.QL;
import org.jquantlib.cashflow.CashFlows;
import org.jquantlib.cashflow.Leg;
import org.jquantlib.pricingengines.BondEngine;
import org.jquantlib.pricingengines.arguments.BondArguments;
import org.jquantlib.pricingengines.results.BondResults;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Date;

public class DiscountingBondEngine extends BondEngine {
	
	private Handle<YieldTermStructure> discountCurve_;

	
	public DiscountingBondEngine(){
		this(new Handle<YieldTermStructure>(YieldTermStructure.class));
	}

    public DiscountingBondEngine(final Handle<YieldTermStructure>  discountCurve){
    	//FIXME: correct?
    	super(new BondArguments(), new BondResults());
    	discountCurve_ = discountCurve;
    	discountCurve_.addObserver(this);
    }
    
    @Override
    public void calculate(){
    	final Leg cashflows = arguments.cashflows;
    	final Date settlementDate = arguments.settlementDate;
    	// FIXME: valuationDate never used ???
    	Date valuationDate = discountCurve_.currentLink().referenceDate();
        QL.require(! discountCurve_.empty() , "no discounting term structure set"); 
        results.value = CashFlows.getInstance().npv(cashflows, discountCurve_);
        results.settlementValue = CashFlows.getInstance().npv(cashflows, discountCurve_, settlementDate, settlementDate, 0);
    }
    
    
    public Handle<YieldTermStructure> discountCurve(){
    	return discountCurve_;
    }
}
