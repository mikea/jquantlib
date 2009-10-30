package org.jquantlib.pricingengines.bond;

import org.jquantlib.QL;
import org.jquantlib.cashflow.CashFlows;
import org.jquantlib.cashflow.Leg;
import org.jquantlib.instruments.Bond;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Date;

public class DiscountingBondEngine extends Bond.EngineImpl {

	private final Handle<YieldTermStructure> discountCurve;

	public DiscountingBondEngine() {
		this(new Handle<YieldTermStructure>(YieldTermStructure.class));
	}

    public DiscountingBondEngine(final Handle<YieldTermStructure>  discountCurve) {
        this.discountCurve = discountCurve;
        this.discountCurve.currentLink().addObserver(this);
    }

    @Override
    public void calculate() /* @ReadOnly */ {
        //TODO: study performance .vs. defensive programming
        // QL.require(Bond.Arguments.class.isAssignableFrom(arguments.getClass()), ReflectConstants.WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
        // QL.require(Bond.Results.class.isAssignableFrom(results.getClass()), ReflectConstants.WRONG_ARGUMENT_TYPE); // QA:[RG]::verified

        final Bond.ArgumentsImpl a = (Bond.ArgumentsImpl)arguments;
        final Bond.ResultsImpl   r = (Bond.ResultsImpl)results;

    	final Leg cashflows = a.cashflows;
    	final Date settlementDate = a.settlementDate;
    	final Date valuationDate = discountCurve.currentLink().referenceDate();
        QL.require(! discountCurve.empty() , "no discounting term structure set"); //// TODO: message

        r.value           = CashFlows.getInstance().npv(cashflows, discountCurve, valuationDate,  valuationDate);
        r.settlementValue = CashFlows.getInstance().npv(cashflows, discountCurve, settlementDate, settlementDate);
    }


    public Handle<YieldTermStructure> discountCurve() /* @ReadOnly */ {
    	return discountCurve;
    }

}
