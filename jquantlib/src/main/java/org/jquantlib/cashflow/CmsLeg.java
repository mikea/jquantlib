package org.jquantlib.cashflow;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.SwapIndex;
import org.jquantlib.lang.annotation.Natural;
import org.jquantlib.lang.annotation.Real;
import org.jquantlib.math.Array;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Schedule;

public class CmsLeg {

    private Schedule schedule_;
    private SwapIndex swapIndex_;
    private/* @Real */Array notionals_;
    private DayCounter paymentDayCounter_;
    private BusinessDayConvention paymentAdjustment_;
    private int[] fixingDays_;
    private Array gearings_;
    private/* @Spread */Array spreads_;
    private/* @Rate */Array caps_, floors_;
    private boolean inArrears_, zeroPayments_;

    public CmsLeg(final Schedule schedule, final SwapIndex swapIndex) {

        schedule_ = (schedule);
        swapIndex_ = (swapIndex);
        paymentAdjustment_ = (BusinessDayConvention.FOLLOWING);
        inArrears_ = (false);
        zeroPayments_ = (false);

    }

    public CmsLeg withNotionals(/* Real */double notional) {
        notionals_ = new Array(1).fill(notional);
        return this;
    }

    public CmsLeg withNotionals(final Array notionals) {
        notionals_ = notionals;
        return this;
    }

    public CmsLeg withPaymentDayCounter(final DayCounter dayCounter) {
        paymentDayCounter_ = dayCounter;
        return this;
    }

    public CmsLeg withPaymentAdjustment(BusinessDayConvention convention) {
        paymentAdjustment_ = convention;
        return this;
    }

    public CmsLeg withFixingDays(/* Natural */int fixingDays) {
        fixingDays_ = new int[] { fixingDays };
        return this;
    }

    public CmsLeg withFixingDays(final int[] fixingDays) {
        fixingDays_ = fixingDays;
        return this;
    }

    public CmsLeg withGearings(/* Real */double gearing) {
        gearings_ = new Array(1).fill(gearing);
        return this;
    }

    public CmsLeg withGearings(final Array gearings) {
        gearings_ = gearings;
        return this;
    }

    public CmsLeg withSpreads(/* Spread */double spread) {
        spreads_ = new Array(1).fill(spread);
        return this;
    }

    public CmsLeg withSpreads(final Array spreads) {
        spreads_ = spreads;
        return this;
    }

    public CmsLeg withCaps(/* @Rate */double cap) {
        caps_ = new Array(1).fill(cap);
        return this;
    }

    public CmsLeg withCaps(final Array caps) {
        caps_ = caps;
        return this;
    }

    public CmsLeg withFloors(/* @Rate */double floor) {
        floors_ = new Array(1).fill(floor);
        return this;
    }

    public CmsLeg withFloors(final Array floors) {
        floors_ = floors;
        return this;
    }

    public CmsLeg inArrears(boolean flag) {
        inArrears_ = flag;
        return this;
    }

    public CmsLeg withZeroPayments(boolean flag) {
        zeroPayments_ = flag;
        return this;
    }

//    public CashFlowVectors.Leg Leg() {
//        return new CashFlowVectors.FloatingLeg(null, schedule_, caps_, paymentDayCounter_, paymentAdjustment_, fixingDays_, caps_, caps_, caps_, caps_, inArrears_, inArrears_);
//        // return FloatingLeg<SwapIndex, CmsCoupon, CappedFlooredCmsCoupon>(
//        // notionals_, schedule_, swapIndex_, paymentDayCounter_,
//        // paymentAdjustment_, fixingDays_, gearings_, spreads_,
//        // caps_, floors_, inArrears_, zeroPayments_);
//    }

}
