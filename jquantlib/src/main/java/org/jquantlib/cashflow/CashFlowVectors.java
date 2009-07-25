package org.jquantlib.cashflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.Array;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Schedule;
import org.jquantlib.util.Date;

import static org.jquantlib.Error.QL_REQUIRE;

public class CashFlowVectors {

    public static class Detail {

        // TODO ... move this somewhere else
        public static <T extends Number, U extends Number> T get(ArrayList<T> v, int i, U defaultValue) {
            if (v == null || v.size() == 0) {
                return (T) defaultValue;
            } else if (i < v.size()) {
                return v.get(i);
            } else {
                return v.get(v.size() - 1);
            }
        }

        public static/* Rate */double effectiveFixedRate(final Array spreads, final Array caps, final Array floors,
        /* Size */int i) {
            /* Rate */double result = get(spreads.toDoubleList(), i, new Double(0.0));
            /* Rate */double floor = get(floors.toDoubleList(), i, new Double(0.0));
            if (floor != 0.0) {
                result = Math.max(floor, result);
            }
            /* Rate */double cap = get(caps.toDoubleList(), i, new Double(0.0));
            if (cap != 0.0) {
                result = Math.min(cap, result);
            }
            return result;
        }

        public static boolean noOption(final Array caps, final Array floors,
        /* Size */int i) {
            return (get(caps.toDoubleList(), i, new Double(0.0)) == 0.0)
                    && (get(floors.toDoubleList(), i, /* Null<Rate>()) == Null<Rate>() */new Double(0.0)) == 0.0);
        }
    }

    public static class Leg extends ArrayList<CashFlow> {
        public Leg(int n) {
            super(n);
        }
    }

    // NOTE: two implementations, one in cpp and one in hpp with different constructors - even not ?
    public static class FloatingLeg<InterestRateIndexType, FloatingCouponType, CappedFlooredCouponType> extends CashFlowVectors.Leg {

        public FloatingLeg(final Array nominals, final Schedule schedule, final InterestRateIndexType index,
                final DayCounter paymentDayCounter, BusinessDayConvention paymentAdj, final int[] fixingDays, final Array gearings,
                final Array spreads, final Array caps, final Array floors, boolean isInArrears, boolean isZero) {

            super(schedule.size() - 1);
            int n = schedule.size() - 1;
            QL_REQUIRE(nominals.length <= n, "too many nominals (" + nominals.length + "), only " + n + " required");
            QL_REQUIRE(gearings.length <= n, "too many gearings (" + gearings.length + "), only " + n + " required");
            QL_REQUIRE(spreads.length <= n, "too many spreads (" + spreads.length + "), only " + n + " required");
            QL_REQUIRE(caps.length <= n, "too many caps (" + caps.length + "), only " + n + " required");
            QL_REQUIRE(floors.length <= n, "too many floors (" + floors.length + "), only " + n + " required");
            QL_REQUIRE(!isZero || !isInArrears, "in-arrears and zero features are not compatible");

            // the following is not always correct (orignial c++ comment)
            Calendar calendar = schedule.getCalendar();

            // FIXME: constructor for uninitialized date available ?
            Date refStart, start, refEnd, end;
            Date paymentDate;

            for (int i = 0; i < n; i++) {
                refStart = start = schedule.date(i);
                refEnd = end = schedule.date(i + 1);
                paymentDate = calendar.adjust(end, paymentAdj);

                if (i == 0 && !schedule.isRegular(i + 1)) {
                    refStart = calendar.adjust(end.decrement(schedule.tenor()), paymentAdj);
                }
                if (i == n - 1 && !schedule.isRegular(i + 1)) {
                    refEnd = calendar.adjust(start.increment(schedule.tenor()), paymentAdj);
                }
//                if (Detail.get(gearings.toDoubleList(), i, 1.0) == 0.0) { // fixed coupon
//                    new Class().getConstructor().newInstance(initargs)
//                    add(new FixedRateCoupon(Detail.get(nominals.toDoubleList(), i, new Double(1.0)),
//                                        paymentDate, Detail.effectiveFixedRate(spreads,caps,floors,i),
//                                        paymentDayCounter,
//                                        start, end, refStart, refEnd));
//                } else { // floating coupon
//                    if (Detail.noOption(caps, floors, i))
//                        add( new FloatingCouponType(paymentDate,
//                                   detail::get(nominals, i, 1.0),
//                                   start, end,
//                                   detail::get(fixingDays, i, index->fixingDays()),
//                                   index,
//                                   detail::get(gearings, i, 1.0),
//                                   detail::get(spreads, i, 0.0),
//                                   refStart, refEnd,
//                                   paymentDayCounter, isInArrears)));
//                    else {
//                        leg.push_back(boost::shared_ptr<CashFlow>(new
//                            CappedFlooredCouponType(
//                                   paymentDate,
//                                   detail::get(nominals, i, 1.0),
//                                   start, end,
//                                   detail::get(fixingDays, i, index->fixingDays()),
//                                   index,
//                                   detail::get(gearings, i, 1.0),
//                                   detail::get(spreads, i, 0.0),
//                                   detail::get(caps,   i, Null<Rate>()),
//                                   detail::get(floors, i, Null<Rate>()),
//                                   refStart, refEnd,
//                                   paymentDayCounter,
//                                   isInArrears)));
//                    }
//                }
//            }
//            }
        }
    }
    }
}
// if (get(gearings, i, 1.0) == 0.0) { // fixed coupon
// add(new FixedRateCoupon(get(nominals, i, 0), paymentDate,
// effectiveFixedRate(spreads, caps, floors, i),
// paymentDayCounter,
// start, end, refStart, refEnd));

// else { // floating coupon
// if (noOption(caps, floors, i))
// leg.add(FloatingCouponType.(paymentDate,
// get(nominals, i, 0),
// start, end, fixingDays, index,
// get(gearings, i, 1.0),
// get(spreads, i, 0.0),
// refStart, refEnd,
// paymentDayCounter, isInArrears)));
// else {
// leg.push_back(boost::shared_ptr<CashFlow>(new
// CappedFlooredCouponType(paymentDate,
// get(nominals, i, Null<Real>()),
// start, end, fixingDays, index,
// get(gearings, i, 1.0),
// get(spreads, i, 0.0),
// get(caps, i, Null<Rate>()),
// get(floors, i, Null<Rate>()),
// refStart, refEnd,
// paymentDayCounter, isInArrears)));
// }
// }
// }

// Leg leg; leg.reserve(n);

//
// // the following is not always correct
// Calendar calendar = schedule.calendar();
//
// Date refStart, start, refEnd, end;
// Date lastPaymentDate = calendar.adjust(schedule.date(n), paymentAdj);
//
// for (Size i=0; i<n; ++i) {
// refStart = start = schedule.date(i);
// refEnd = end = schedule.date(i+1);
// Date paymentDate =
// isZero ? lastPaymentDate : calendar.adjust(end, paymentAdj);
// if (i==0 && !schedule.isRegular(i+1)) {
// BusinessDayConvention bdc = schedule.businessDayConvention();
// refStart = calendar.adjust(end - schedule.tenor(), bdc);
// }
// if (i==n-1 && !schedule.isRegular(i+1)) {
// BusinessDayConvention bdc = schedule.businessDayConvention();
// refEnd = calendar.adjust(start + schedule.tenor(), bdc);
// }
// if (detail::get(gearings, i, 1.0) == 0.0) { // fixed coupon
// leg.push_back(boost::shared_ptr<CashFlow>(new
// FixedRateCoupon(detail::get(nominals, i, 1.0),
// paymentDate,
// detail::effectiveFixedRate(spreads,caps,
// floors,i),
// paymentDayCounter,
// start, end, refStart, refEnd)));
// } else { // floating coupon
// if (detail::noOption(caps, floors, i))
// leg.push_back(boost::shared_ptr<CashFlow>(new
// FloatingCouponType(paymentDate,
// detail::get(nominals, i, 1.0),
// start, end,
// detail::get(fixingDays, i, index->fixingDays()),
// index,
// detail::get(gearings, i, 1.0),
// detail::get(spreads, i, 0.0),
// refStart, refEnd,
// paymentDayCounter, isInArrears)));
// else {
// leg.push_back(boost::shared_ptr<CashFlow>(new
// CappedFlooredCouponType(
// paymentDate,
// detail::get(nominals, i, 1.0),
// start, end,
// detail::get(fixingDays, i, index->fixingDays()),
// index,
// detail::get(gearings, i, 1.0),
// detail::get(spreads, i, 0.0),
// detail::get(caps, i, Null<Rate>()),
// detail::get(floors, i, Null<Rate>()),
// refStart, refEnd,
// paymentDayCounter,
// isInArrears)));
// }
// }
// }
// return leg;
// }

// }
//        
// public FloatingLeg(String aLongSignature, String aLongSignature2){
//            
// }

// public static class FloatingDigitalLeg extends Leg{
//        
// }
//    
// public static class FixedRateLeg extends Leg{
//        
// }
//    
// public static class IborLeg extends Leg{
//        
// }
//    
// public static class CmsLeg extends Leg{
//        
// }
//    
// public static class FloatingZeroLeg extends Leg{
//        
// }
//    
// public static class IborZeroLeg extends Leg{
//        
// }
//    
// public static class CmsZeroLeg extends Leg{
//        
// }
//    
// public static class RangeAccrualLeg extends Leg{
//        
// }

