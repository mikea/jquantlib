package org.jquantlib.termstructures.yield;

import java.util.List;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Schedule;
import org.jquantlib.util.Date;

//TODO: Finish
public class FixedRateBondHelper<YieldTermStructure> extends RateHelper {

	public FixedRateBondHelper(
            Handle<Quote> cleanPrice,
            int settlementDays,
            double faceAmount,
            Schedule schedule,
            List<Double> coupons,
            DayCounter dayCounter,
            BusinessDayConvention paymentConvention,
            double redemption,
            Date issueDate)
 {
	 //super(cleanPrice);
	 /*
bond_ = boost::shared_ptr<FixedRateBond>(new
FixedRateBond(settlementDays, faceAmount, schedule,
  coupons, dayCounter, paymentConvention,
  redemption, issueDate));

latestDate_ = bond_->maturityDate();
registerWith(Settings::instance().evaluationDate());

boost::shared_ptr<PricingEngine> bondEngine(new
DiscountingBondEngine(termStructureHandle_));
bond_->setPricingEngine(bondEngine); */
}
            
	@Override
	public double getImpliedQuote() {
		// TODO Auto-generated method stub
		return 0;
	}

}
