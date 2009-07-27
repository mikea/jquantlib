package org.jquantlib.cashflow;

import org.jquantlib.Validate;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.InterestRateIndex;
import org.jquantlib.math.Constants;
import org.jquantlib.util.Date;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * Capped and/or floored floating-rate coupon
 * <p>
 * The payoff {@latex$ P } of a capped floating-rate coupon is:
 * {@latex[ P = N \times T \times \min(a L + b, C) }
 * <p>
 * The payoff of a floored floating-rate coupon is:
 * {@latex[ P = N \times T \times \max(a L + b, F) }
 * <p>
 * The payoff of a collared floating-rate coupon is:
 * {@latex[ P = N \times T \times \min(\max(a L + b, F), C) } where
 * <p>
 * {@latex$ N } is the notional, {@latex$ T }is the accrual time, {@latex$ L } is the floating rate, {@latex$ a } is its gearing,
 * {@latex$ b } is the spread, and {@latex$ C } and {@latex$ F } are the strikes.
 * <p> 
 * They can be decomposed in the following manner. Decomposition of a capped floating rate coupon:
 * {@latex[ R = \min(a L + b, C) = (a L + b) + \min(C - b - \xi |a| L, 0) } where 
 * {@latex$ \xi = sgn(a) }. Then: 
 * {@latex[ R = (a L + b) + |a| \min(\frac{C - b}{|a|} - \xi L, 0) }
 * 
 * @author Ueli Hofstetter
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class CappedFlooredCoupon extends FloatingRateCoupon {

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
            if (!Double.isNaN(cap)) {
                isCapped_ = true;
                cap_ = cap;
            }
            if (!Double.isNaN(floor)) {
                floor_ = floor;
                isFloored_ = true;
            }
        } else {
            if (!Double.isNaN(cap)) {
                floor_ = cap;
                isFloored_ = true;
            }
            if (!Double.isNaN(floor)) {
                isCapped_ = true;
                cap_ = floor;
            }
        }

        if (isCapped_ && isFloored_) {
            Validate.QL_REQUIRE(cap >= floor, "cap level (" + cap + ") less than floor level (" + floor + ")");
        }

        // registerWith(underlying);
        underlying.addObserver(this);
    }
    
    
    
    private void setPricer() {
            //TODO: Code review :: incomplete code
            if (true)
                throw new UnsupportedOperationException("Work in progress");
            
//            const boost::shared_ptr<FloatingRateCouponPricer>& pricer) {
//       if (pricer_)
//           unregisterWith(pricer_);
//       pricer_ = pricer;
//       if (pricer_)
//           registerWith(pricer_);
//       update();
//       underlying_->setPricer(pricer);
   }

    
    @Override
    public /*@Rate*/ double  rate() /* @ReadOnly */ {
        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
//   QL_REQUIRE(underlying_->pricer(), "pricer not set");
//   Rate swapletRate = underlying_->rate();
//   Rate floorletRate = 0.;
//   if(isFloored_)
//       floorletRate = underlying_->pricer()->floorletRate(effectiveFloor());
//   Rate capletRate = 0.;
//   if(isCapped_)
//       capletRate = underlying_->pricer()->capletRate(effectiveCap());
//   return swapletRate + floorletRate - capletRate;

    return Double.NaN;
    }

    @Override
    public /*@Rate*/ double convexityAdjustment() /* @ReadOnly */ {
        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
//   return underlying_->convexityAdjustment();

        return Double.NaN;
}

    private /*@Rate*/ double cap() /* @ReadOnly */ {
        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
//   if ( (gearing_ > 0) && isCapped_)
//           return cap_;
//   if ( (gearing_ < 0) && isFloored_)
//       return floor_;
        return Constants.NULL_Double;
}

    private /*@Rate*/ double floor() /* @ReadOnly */ {
        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
//   if ( (gearing_ > 0) && isFloored_)
//       return floor_;
//   if ( (gearing_ < 0) && isCapped_)
//       return cap_;
        return Constants.NULL_Double;
}

    private /*@Rate*/ double effectiveCap() /* @ReadOnly */ {
        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
//   return (cap_ - spread())/gearing();
        return Constants.NULL_Double;
}

    private /*@Rate*/ double effectiveFloor() /* @ReadOnly */ {
        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
//   return (floor_ - spread())/gearing();
        return Constants.NULL_Double;
}

    @Override
    public void update() {
        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
//   notifyObservers();
}

        //
        // implements TypedVisitable
        //
        
        // TODO: code review :: object model needs to be validated and eventually refactored
        @Override
        public void accept(final TypedVisitor<Event> v) {
            final Visitor<Event> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
            if (v1 != null) {
                v1.visit(this);
            } else {
                super.accept(v);
            }
        }

    

    //FIXME ... 
    public CappedFlooredCoupon(Date paymentDate, double nominal, Date startDate, Date endDate, int fixingDays,
            InterestRateIndex index, double gearing, double spread, Date refPeriodStart, Date refPeriodEnd, DayCounter dayCounter,
            boolean isInArrears) {
        super(paymentDate, nominal, startDate, endDate, fixingDays, index, gearing, spread, refPeriodStart, refPeriodEnd,
                dayCounter, isInArrears);
        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
    }

}