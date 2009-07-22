package org.jquantlib.cashflow;

import java.util.List;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.lang.annotation.Rate;
import org.jquantlib.lang.annotation.Real;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Schedule;

public class FixedRateLeg {
    
private Schedule schedule_;
private List</*Real*/Double> notionals_;
private List<InterestRate> couponRates_;
private  DayCounter paymentDayCounter_, firstPeriodDayCounter_;
private  BusinessDayConvention paymentAdjustment_;
    
public FixedRateLeg(final Schedule schedule, final DayCounter paymentDayCounter){
    this.schedule_=(schedule);
    this.paymentDayCounter_=(paymentDayCounter);
    this.paymentAdjustment_ = BusinessDayConvention.FOLLOWING;
}

//public FixedRateLeg withNotionals(/*Real*/double notional) {
//notionals_ = std::vector<Real>(1,notional);
//return *this;
//}
//
//FixedRateLeg& FixedRateLeg::withNotionals(
//                  const std::vector<Real>& notionals) {
//notionals_ = notionals;
//return *this;
//}
//
//FixedRateLeg& FixedRateLeg::withCouponRates(Rate couponRate) {
//couponRates_.resize(1);
//couponRates_[0] = InterestRate(couponRate,
//                paymentDayCounter_,
//                Simple);
//return *this;
//}
//
//FixedRateLeg& FixedRateLeg::withCouponRates(
//                     const InterestRate& couponRate) {
//couponRates_ = std::vector<InterestRate>(1,couponRate);
//return *this;
//}
//
//FixedRateLeg& FixedRateLeg::withCouponRates(
//                const std::vector<Rate>& couponRates) {
//couponRates_.resize(couponRates.size());
//for (Size i=0; i<couponRates.size(); ++i)
//couponRates_[i] = InterestRate(couponRates[i],
//                    paymentDayCounter_,
//                    Simple);
//return *this;
//}
//
//FixedRateLeg& FixedRateLeg::withCouponRates(
//        const std::vector<InterestRate>& couponRates) {
//couponRates_ = couponRates;
//return *this;
//}
//
//FixedRateLeg& FixedRateLeg::withPaymentAdjustment(
//                    BusinessDayConvention convention) {
//paymentAdjustment_ = convention;
//return *this;
//}
//
//FixedRateLeg& FixedRateLeg::withFirstPeriodDayCounter(
//                        const DayCounter& dayCounter) {
//firstPeriodDayCounter_ = dayCounter;
//return *this;
//}
    
   


}
