package org.jquantlib.cashflow;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.util.Date;

public class CappedFlooredIborCoupon extends CappedFlooredCoupon {

    public CappedFlooredIborCoupon(
            final Date paymentDate,
            final /*Real*/double nominal,
            final Date  startDate,
            final Date  endDate,
            final /*Natural*/int fixingDays,
            final  IborIndex  index,
            final /*Real*/double gearing/* = 1.0*/,
            final /*Spread*/double spread /*= 0.0*/,
            final /*Rate*/double cap /*= Null<Rate>()*/,
            final /*Rate*/double floor /*= Null<Rate>()*/,
            final Date refPeriodStart /*= Date()*/,
            final Date refPeriodEnd /*= Date()*/,
            final DayCounter dayCounter /* = DayCounter()*/,
            final boolean isInArrears/* = false*/){

        super( new IborCoupon(paymentDate, nominal, startDate, endDate, fixingDays,
            index, gearing, spread, refPeriodStart, refPeriodEnd,
                dayCounter, isInArrears), cap, floor);

        throw new UnsupportedOperationException("work in progress...");


    }
//  : CappedFlooredCoupon(boost::shared_ptr<FloatingRateCoupon>(new
//      IborCoupon(paymentDate, nominal, startDate, endDate, fixingDays,
//                 index, gearing, spread, refPeriodStart, refPeriodEnd,
//                 dayCounter, isInArrears)), cap, floor) {}

}
