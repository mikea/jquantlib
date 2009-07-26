package org.jquantlib.cashflow;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.InterestRateIndex;
import org.jquantlib.util.Date;

import static org.jquantlib.math.Constants.NULL_Double;
import static org.jquantlib.Error.QL_REQUIRE;

public class CappedFlooredCoupon extends FloatingRateCoupon {

    // ! Capped and/or floored floating-rate coupon
    /*
     * ! The payoff \f$ P \f$ of a capped floating-rate coupon is: \f[ P = N \times T \times \min(a L + b, C). \f] The payoff of a
     * floored floating-rate coupon is: \f[ P = N \times T \times \max(a L + b, F). \f] The payoff of a collared floating-rate
     * coupon is: \f[ P = N \times T \times \min(\max(a L + b, F), C). \f]
     * 
     * where \f$ N \f$ is the notional, \f$ T \f$ is the accrual time, \f$ L \f$ is the floating rate, \f$ a \f$ is its gearing, \f$
     * b \f$ is the spread, and \f$ C \f$ and \f$ F \f$ the strikes.
     * 
     * They can be decomposed in the following manner. Decomposition of a capped floating rate coupon: \f[ R = \min(a L + b, C) = (a
     * L + b) + \min(C - b - \xi |a| L, 0) \f] where \f$ \xi = sgn(a) \f$. Then: \f[ R = (a L + b) + |a| \min(\frac{C - b}{|a|} -
     * \xi L, 0) \f]
     */

    protected FloatingRateCoupon underlying_;
    protected boolean isCapped_, isFloored_;
    protected/* Rate */double cap_, floor_;

    public CappedFlooredCoupon(final FloatingRateCoupon underlying) {
        this(underlying, 0.0, 0.0);
    }

    // FIXME: why is FloatingRateCoupon wrapped in a handle ... looks suspicious
    public CappedFlooredCoupon(final FloatingRateCoupon underlying, double cap, double floor) {
        super(underlying.date(), underlying.nominal, underlying.accrualStartDate(), underlying.accrualEndDate(), underlying
                .fixingDays(), underlying.index(), underlying.gearing(), underlying.spread(), underlying.referencePeriodStart(),
                underlying.referencePeriodEnd(), underlying.dayCounter(), underlying.isInArrears());
        this.underlying_ = (underlying);
        isCapped_ = (false);
        isFloored_ = (false);

        if (gearing_ > 0) {
            if (cap != NULL_Double) {
                isCapped_ = true;
                cap_ = cap;
            }
            if (floor != NULL_Double) {
                floor_ = floor;
                isFloored_ = true;
            }
        } else {
            if (cap != NULL_Double) {
                floor_ = cap;
                isFloored_ = true;
            }
            if (floor != NULL_Double) {
                isCapped_ = true;
                cap_ = floor;
            }
        }

        if (isCapped_ && isFloored_) {
            QL_REQUIRE(cap >= floor, "cap level (" + cap + ") less than floor level (" + floor + ")");
        }

        // registerWith(underlying);
        underlying.addObserver(this);
    }
    
    
    
//    void CappedFlooredCoupon::setPricer(
//            const boost::shared_ptr<FloatingRateCouponPricer>& pricer) {
//       if (pricer_)
//           unregisterWith(pricer_);
//       pricer_ = pricer;
//       if (pricer_)
//           registerWith(pricer_);
//       update();
//       underlying_->setPricer(pricer);
//   }
//
//Rate CappedFlooredCoupon::rate() const {
//   QL_REQUIRE(underlying_->pricer(), "pricer not set");
//   Rate swapletRate = underlying_->rate();
//   Rate floorletRate = 0.;
//   if(isFloored_)
//       floorletRate = underlying_->pricer()->floorletRate(effectiveFloor());
//   Rate capletRate = 0.;
//   if(isCapped_)
//       capletRate = underlying_->pricer()->capletRate(effectiveCap());
//   return swapletRate + floorletRate - capletRate;
//}
//
//Rate CappedFlooredCoupon::convexityAdjustment() const {
//   return underlying_->convexityAdjustment();
//}
//
//Rate CappedFlooredCoupon::cap() const {
//   if ( (gearing_ > 0) && isCapped_)
//           return cap_;
//   if ( (gearing_ < 0) && isFloored_)
//       return floor_;
//   return Null<Rate>();
//}
//
//Rate CappedFlooredCoupon::floor() const {
//   if ( (gearing_ > 0) && isFloored_)
//       return floor_;
//   if ( (gearing_ < 0) && isCapped_)
//       return cap_;
//   return Null<Rate>();
//}
//
//Rate CappedFlooredCoupon::effectiveCap() const {
//   return (cap_ - spread())/gearing();
//}
//
//Rate CappedFlooredCoupon::effectiveFloor() const {
//   return (floor_ - spread())/gearing();
//}
//
//void CappedFlooredCoupon::update() {
//   notifyObservers();
//}
//
//void CappedFlooredCoupon::accept(AcyclicVisitor& v) {
//   typedef FloatingRateCoupon super;
//   Visitor<CappedFlooredCoupon>* v1 =
//
//       dynamic_cast<Visitor<CappedFlooredCoupon>*>(&v);
//   if (v1 != 0)
//       v1->visit(*this);
//   else
//       super::accept(v);
//}

    

    //FIXME ... 
    public CappedFlooredCoupon(Date paymentDate, double nominal, Date startDate, Date endDate, int fixingDays,
            InterestRateIndex index, double gearing, double spread, Date refPeriodStart, Date refPeriodEnd, DayCounter dayCounter,
            boolean isInArrears) {
        super(paymentDate, nominal, startDate, endDate, fixingDays, index, gearing, spread, refPeriodStart, refPeriodEnd,
                dayCounter, isInArrears);
        // TODO Auto-generated constructor stub
    }

}