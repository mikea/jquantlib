/*
 Copyright (C) 2008 Srinivas Hasti

 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.termstructures.yieldcurves;


// FIXME: move to org.jquantlib.termstructures.yieldcurves


import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;

/**
 * @author Srinivas Hasti
 *
 */
//TODO: Finish
public class SwapRateHelper extends RelativeDateRateHelper {
	
    private Period tenor_;
    private Calendar calendar;
    private BusinessDayConvention fixedConvention;
    private Frequency fixedFrequency;
    private DayCounter fixedDayCount;
    private IborIndex iborIndex;
   // VanillaSwap swap;
    private RelinkableHandle<YieldTermStructure> termStructureHandle;
    private Handle<Quote> spread;
    private Period fwdStart;
    
    
    
//    public SwapRateHelper(final Handle<Quote> rate,
//            final SwapIndex swapIndex,
//            final Handle<Quote> spread,
//            final Period fwdStart){
//        super(rate);
//        tenor_ = swapIndex.
//    }
//: RelativeDateRateHelper(rate),
//tenor_(swapIndex->tenor()),
//calendar_(swapIndex->fixingCalendar()),
//fixedConvention_(swapIndex->fixedLegConvention()),
//fixedFrequency_(swapIndex->fixedLegTenor().frequency()),
//fixedDayCount_(swapIndex->dayCounter()),
//iborIndex_(swapIndex->iborIndex()),
//spread_(spread), fwdStart_(fwdStart) {
//registerWith(iborIndex_);
//registerWith(spread_);
//initializeDates();
//}
//
//SwapRateHelper::SwapRateHelper(const Handle<Quote>& rate,
//            const Period& tenor,
//            const Calendar& calendar,
//            Frequency fixedFrequency,
//            BusinessDayConvention fixedConvention,
//            const DayCounter& fixedDayCount,
//            const shared_ptr<IborIndex>& iborIndex,
//            const Handle<Quote>& spread,
//            const Period& fwdStart)
//: RelativeDateRateHelper(rate),
//tenor_(tenor),
//calendar_(calendar),
//fixedConvention_(fixedConvention),
//fixedFrequency_(fixedFrequency),
//fixedDayCount_(fixedDayCount),
//iborIndex_(iborIndex),
//spread_(spread), fwdStart_(fwdStart) {
//registerWith(iborIndex_);
//registerWith(spread_);
//initializeDates();
//}
//
//SwapRateHelper::SwapRateHelper(Rate rate,
//            const Period& tenor,
//            const Calendar& calendar,
//            Frequency fixedFrequency,
//            BusinessDayConvention fixedConvention,
//            const DayCounter& fixedDayCount,
//            const shared_ptr<IborIndex>& iborIndex,
//            const Handle<Quote>& spread,
//            const Period& fwdStart)
//: RelativeDateRateHelper(rate),
//tenor_(tenor),
//calendar_(calendar),
//fixedConvention_(fixedConvention),
//fixedFrequency_(fixedFrequency),
//fixedDayCount_(fixedDayCount),
//iborIndex_(iborIndex),
//spread_(spread), fwdStart_(fwdStart) {
//registerWith(iborIndex_);
//registerWith(spread_);
//initializeDates();
//}
//
//SwapRateHelper::SwapRateHelper(Rate rate,
//            const shared_ptr<SwapIndex>& swapIndex,
//            const Handle<Quote>& spread,
//            const Period& fwdStart)
//: RelativeDateRateHelper(rate),
//tenor_(swapIndex->tenor()),
//calendar_(swapIndex->fixingCalendar()),
//fixedConvention_(swapIndex->fixedLegConvention()),
//fixedFrequency_(swapIndex->fixedLegTenor().frequency()),
//fixedDayCount_(swapIndex->dayCounter()),
//iborIndex_(swapIndex->iborIndex()),
//spread_(spread), fwdStart_(fwdStart) {
//registerWith(iborIndex_);
//registerWith(spread_);
//initializeDates();
//}
//
//void SwapRateHelper::initializeDates() {
//
//// dummy ibor index with curve/swap arguments
//boost::shared_ptr<IborIndex> clonedIborIndex =
//iborIndex_->clone(termStructureHandle_);
//
//// do not pass the spread here, as it might be a Quote
//// i.e. it can dinamically change
//swap_ = MakeVanillaSwap(tenor_, clonedIborIndex, 0.0, fwdStart_)
//.withFixedLegDayCount(fixedDayCount_)
//.withFixedLegTenor(Period(fixedFrequency_))
//.withFixedLegConvention(fixedConvention_)
//.withFixedLegTerminationDateConvention(fixedConvention_)
//.withFixedLegCalendar(calendar_)
//.withFloatingLegCalendar(calendar_);
//
//earliestDate_ = swap_->startDate();
//
//// Usually...
//latestDate_ = swap_->maturityDate();
//// ...but due to adjustments, the last floating coupon might
//// need a later date for fixing
//#ifdef QL_USE_INDEXED_COUPON
//shared_ptr<FloatingRateCoupon> lastFloating =
//boost::dynamic_pointer_cast<FloatingRateCoupon>(
//                          swap_->floatingLeg().back());
//Date fixingValueDate =
//iborIndex_->valueDate(lastFloating->fixingDate());
//Date endValueDate = iborIndex_->maturityDate(fixingValueDate);
//latestDate_ = std::max(latestDate_, endValueDate);
//#endif
//}
//
//void SwapRateHelper::setTermStructure(YieldTermStructure* t) {
//// do not set the relinkable handle as an observer -
//// force recalculation when needed
//termStructureHandle_.linkTo(
//  shared_ptr<YieldTermStructure>(t,no_deletion),
//  false);
//RelativeDateRateHelper::setTermStructure(t);
//}
//
//Real SwapRateHelper::impliedQuote() const {
//QL_REQUIRE(termStructure_ != 0, "term structure not set");
//// we didn't register as observers - force calculation
//swap_->recalculate();
//// weak implementation... to be improved
//static const Spread basisPoint = 1.0e-4;
//Real floatingLegNPV = swap_->floatingLegNPV();
//Spread spread = spread_.empty() ? 0.0 : spread_->value();
//Real spreadNPV = swap_->floatingLegBPS()/basisPoint*spread;
//Real totNPV = - (floatingLegNPV+spreadNPV);
//Real result = totNPV/(swap_->fixedLegBPS()/basisPoint);
//return result;
//}
//
//Spread SwapRateHelper::spread() const {
//return spread_.empty() ? 0.0 : spread_->value();
//}
//
//shared_ptr<VanillaSwap> SwapRateHelper::swap() const {
//return swap_;
//}
//
//const Period& SwapRateHelper::forwardStart() const {
//return fwdStart_;
//}
//
//void SwapRateHelper::accept(AcyclicVisitor& v) {
//Visitor<SwapRateHelper>* v1 =
//dynamic_cast<Visitor<SwapRateHelper>*>(&v);
//if (v1 != 0)
//v1->visit(*this);
//else
//RateHelper::accept(v);
//}



//public SwapRateHelper(final Handle<Quote> rate,
//            final SwapIndex swapIndex,
//            final Handle<Quote> spread = Handle<Quote>(),
//            final Period fwdStart = 0*Days) {
//	// TODO: Finish
//}
//            
//public SwapRateHelper(final Handle<Quote> rate,
//            final Period tenor,
//            final Calendar calendar,
//            // fixed leg
//            Frequency fixedFrequency,
//            BusinessDayConvention fixedConvention,
//            final DayCounter fixedDayCount,
//            // floating leg
//            final IborIndex iborIndex,
//            final Handle<Quote> spread = Handle<Quote>(),
//            final Period fwdStart = 0*Days) {
//	// TODO: Finish
//}
//            
//public SwapRateHelper(Rate rate,
//            final Period tenor,
//            final Calendar calendar,
//            // fixed leg
//            Frequency fixedFrequency,
//            BusinessDayConvention fixedConvention,
//            final DayCounter fixedDayCount,
//            // floating leg
//            final IborIndex iborIndex,
//            final Handle<Quote> spread = Handle<Quote>(),
//            final Period fwdStart = 0*Days) {
//	// TODO: Finish
//}
//            
//public SwapRateHelper(Rate rate,
//            final SwapIndex swapIndex,
//            final Handle<Quote> spread = Handle<Quote>(),
//            final Period fwdStart = 0*Days) {
//	// TODO: Finish
//}

    
	public SwapRateHelper(
			final double d, 
			final Period tenor, 
			final Calendar calendar,
			final BusinessDayConvention fixedConvention, 
			final Frequency fixedFrequency,
			final DayCounter fixedDayCount, 
			final IborIndex iborIndex,
			final RelinkableHandle<YieldTermStructure> termStructureHandle,
			final Handle<Quote> spread, 
			final Period fwdStart) {
		super(d);
		//this.tenor = tenor;
		this.calendar = calendar;
		this.fixedConvention = fixedConvention;
		this.fixedFrequency = fixedFrequency;
		this.fixedDayCount = fixedDayCount;
		this.iborIndex = iborIndex;
		this.termStructureHandle = termStructureHandle;
		this.spread = spread;
		this.fwdStart = fwdStart;
	}

	/* (non-Javadoc)
	 * @see org.jquantlib.termstructures.yield.RelativeDateRateHelper#initializeDates()
	 */
	@Override
	protected void initializeDates() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jquantlib.termstructures.BootstrapHelper#getImpliedQuote()
	 */
	@Override
	public double impliedQuote() {
		// TODO Auto-generated method stub
		return 0;
	}

}
