package org.jquantlib.cashflow;

import static org.jquantlib.Error.QL_REQUIRE;

import java.lang.reflect.InvocationTargetException;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.InterestRateIndex;
import org.jquantlib.lang.reflect.TypeTokenTree;
import org.jquantlib.math.Array;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Schedule;
import org.jquantlib.util.Date;

public class FloatingLeg<InterestRateIndexType extends InterestRateIndex, FloatingCouponType, CappedFlooredCouponType> extends Leg {

    public FloatingLeg(final Array nominals, final Schedule schedule, final InterestRateIndexType index,
            final DayCounter paymentDayCounter, BusinessDayConvention paymentAdj, Array fixingDays, final Array gearings,
            final Array spreads, final Array caps, final Array floors, boolean isInArrears, boolean isZero) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {

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
            if (Detail.get(gearings, i, 1.0) == 0.0) { // fixed coupon
                add(new FixedRateCoupon(Detail.get(nominals, i, new Double(1.0)),
                                    paymentDate, Detail.effectiveFixedRate(spreads,caps,floors,i),
                                    paymentDayCounter,
                                    start, end, refStart, refEnd));
            } else { // floating coupon
                if (Detail.noOption(caps, floors, i)){
                    final Class<?> fctklass = new TypeTokenTree(this.getClass()).getRoot().get(1).getElement();                  
                    //currently only one constructor in IborLeg....<-- make this explicit
                    FloatingRateCoupon frc = (FloatingRateCoupon)fctklass.getConstructors()[0].newInstance(paymentDate,
                            Detail.get(nominals, i, new Double(1.0)),
                            start, end,
                            Detail.get(fixingDays, i, index.getFixingDays()),
                            index,
                            Detail.get(gearings, i, 1.0),
                            Detail.get(spreads, i, 0.0),
                            refStart, refEnd,
                            paymentDayCounter, isInArrears);
                    add(frc);
                }
                else {
                    final Class<?> cfcklass = new TypeTokenTree(this.getClass()).getRoot().get(2).getElement();
                    
//                    add(new
//                        CappedFlooredCouponType(
//                               paymentDate,
//                               detail::get(nominals, i, 1.0),
//                               start, end,
//                               detail::get(fixingDays, i, index->fixingDays()),
//                               index,
//                               detail::get(gearings, i, 1.0),
//                               detail::get(spreads, i, 0.0),
//                               detail::get(caps,   i, Null<Rate>()),
//                               detail::get(floors, i, Null<Rate>()),
//                               refStart, refEnd,
//                               paymentDayCounter,
//                               isInArrears)));
                }
            }
        }
        }
    }
//}