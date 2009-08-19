package org.jquantlib.instruments;

import java.util.ArrayList;

import org.jquantlib.QL;
import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.cashflow.FixedRateCoupon;
import org.jquantlib.cashflow.FixedRateLeg;
import org.jquantlib.cashflow.IborCoupon;
import org.jquantlib.cashflow.IborLeg;
import org.jquantlib.cashflow.Leg;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.math.Constants;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.VanillaSwapArguments;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.pricingengines.results.VanillaSwapResults;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Schedule;
import org.jquantlib.util.Date;

/**
 * Plain-vanilla swap
 *
 * @note if QL_TODAYS_PAYMENTS was defined (in userconfig.hpp
                 or when calling ./configure; it is undefined by
                 default) payments occurring at the settlement date of
                 the swap are included in the NPV, and therefore
                 affect the fair-rate and fair-spread
                 calculation. This might not be what you want.

    @category instruments

 * @author Richard Gomes
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class VanillaSwap extends Swap {

    static final /*@Spread*/ double  basisPoint = 1.0e-4;



    private final Type type;
    private final /*@Price*/ double nominal;
    private final Schedule fixedSchedule;
    private final /*@Rate*/ double  fixedRate;
    private final DayCounter fixedDayCount;
    private final Schedule floatingSchedule;
    private final IborIndex iborIndex;
    private final /*@Spread*/ double spread;
    private final DayCounter floatingDayCount;
    private final BusinessDayConvention paymentConvention;

    // results
    private /*@Rate*/ double fairRate;
    private /*@Spread*/ double fairSpread;


    public VanillaSwap(
            final Type type,
            final /*@Price*/ double nominal,
            final Schedule fixedSchedule,
            final /*@Rate*/ double fixedRate,
            final DayCounter fixedDayCount,
            final Schedule floatSchedule,
            final IborIndex iborIndex,
            final /*@Spread*/ double spread,
            final DayCounter floatingDayCount,
            final BusinessDayConvention paymentConvention) {

        super(2);
        this.type = type;
        this.nominal = nominal;
        this.fixedSchedule = fixedSchedule;
        this.fixedRate = fixedRate;
        this.fixedDayCount = fixedDayCount;
        this.floatingSchedule = floatSchedule;
        this.iborIndex = iborIndex;
        this.spread = spread;
        this.floatingDayCount = floatingDayCount;
        this.paymentConvention = paymentConvention;

        final Leg fixedLeg = new FixedRateLeg(fixedSchedule, fixedDayCount)
            .withNotionals(nominal)
            .withCouponRates(fixedRate)
            .withPaymentAdjustment(paymentConvention).Leg();

        final Leg floatingLeg = new IborLeg(floatingSchedule, iborIndex)
            .withNotionals(nominal)
            .withPaymentDayCounter(floatingDayCount)
            .withPaymentAdjustment(paymentConvention)
            .withFixingDays(iborIndex.fixingDays())   // TODO: code review :: please verify against QL/C++ code
            .withSpreads(spread).Leg();

        for (final CashFlow item : floatingLeg) item.addObserver(this);

        super.legs.add(fixedLeg);
        super.legs.add(floatingLeg);
        if (type==Type.Payer) {
            super.payer[0] = -1.0;
            super.payer[1] = +1.0;
        } else {
            super.payer[0] = +1.0;
            super.payer[1] = -1.0;
        }
    }

    public /*@Rate*/ double  fairRate() /* @ReadOnly */ {
        calculate();
        QL.require(!Double.isNaN(fairRate) , "result not available"); // QA:[RG]::verified // TODO: message
        return fairRate;
    }

    public /*@Spread*/ double fairSpread() /* @ReadOnly */ {
        calculate();
        QL.require(!Double.isNaN(fairSpread) , "result not available"); // QA:[RG]::verified // TODO: message
        return fairSpread;
    }


    public final Leg fixedLeg() /* @ReadOnly */ {
        return legs.get(0);
    }

    public final Leg floatingLeg() /* @ReadOnly */ {
        return legs.get(1);
    }


    public /*@Price*/ double fixedLegBPS() /* @ReadOnly */ {
        calculate();
        QL.require(!Double.isNaN(legBPS[0]) , "result not available"); // QA:[RG]::verified // TODO: message
        return legBPS[0];
    }

    public /*@Price*/ double floatingLegBPS() /* @ReadOnly */ {
        calculate();
        QL.require(!Double.isNaN(legBPS[1]) , "result not available");
        return legBPS[1];
    }

    public /*@Price*/ double fixedLegNPV() /* @ReadOnly */ {
        calculate();
        QL.require(!Double.isNaN(legNPV[0]) , "result not available"); // QA:[RG]::verified // TODO: message
        return legNPV[0];
    }

    public /*@Price*/ double floatingLegNPV() /* @ReadOnly */ {
        calculate();
        QL.require(!Double.isNaN(legNPV[1]) , "result not available"); // QA:[RG]::verified // TODO: message
        return legNPV[1];
    }

    @Override
    public void setupExpired() /* @ReadOnly */ {
        super.setupExpired();
        legBPS[0] = 0.0;
        legBPS[1] = 0.0;
        fairRate   = Constants.NULL_REAL;
        fairSpread = Constants.NULL_REAL;
    }

    @Override
    public void setupArguments(final Arguments args) /* @ReadOnly */ {
        super.setupArguments(args);
        if (!args.getClass().isAssignableFrom(VanillaSwapArguments.class)) return;

        final VanillaSwapArguments arguments = (VanillaSwapArguments) args;

        arguments.type = type;
        arguments.nominal = nominal;

        final Leg fixedCoupons = fixedLeg();

        arguments.fixedResetDates = new ArrayList<Date>(fixedCoupons.size());
        arguments.fixedPayDates = new ArrayList<Date>(fixedCoupons.size());
        arguments.fixedCoupons = new ArrayList</*@Price*/ Double>(fixedCoupons.size());

        for (int i=0; i<fixedCoupons.size(); i++) {
            final FixedRateCoupon coupon = (FixedRateCoupon) fixedCoupons.get(i);
            arguments.fixedPayDates.set(i, coupon.date());
            arguments.fixedResetDates.set(i, coupon.accrualStartDate());
            arguments.fixedCoupons.set(i, coupon.amount());
        }

        final Leg floatingCoupons = floatingLeg();

        arguments.floatingResetDates = new ArrayList<Date>(floatingCoupons.size());
        arguments.floatingPayDates = new ArrayList<Date>(floatingCoupons.size());
        arguments.floatingFixingDates = new ArrayList<Date>(floatingCoupons.size());

        arguments.floatingAccrualTimes = new ArrayList</*@Time*/ Double>(floatingCoupons.size());
        arguments.floatingSpreads = new ArrayList</*@Spread*/ Double>(floatingCoupons.size());
        arguments.floatingCoupons = new ArrayList</*@Price*/ Double>(floatingCoupons.size());
        for (int i=0; i<floatingCoupons.size(); ++i) {
            final IborCoupon coupon = (IborCoupon) floatingCoupons.get(i);

            arguments.floatingResetDates.set(i, coupon.accrualStartDate());
            arguments.floatingPayDates.set(i, coupon.date());

            arguments.floatingFixingDates.set(i, coupon.fixingDate());
            arguments.floatingAccrualTimes.set(i, coupon.accrualPeriod());
            arguments.floatingSpreads.set(i, coupon.spread());
            try {
                arguments.floatingCoupons.set(i, coupon.amount());
            } catch (final Exception e) {
                arguments.floatingCoupons.set(i, Constants.NULL_REAL);
            }
        }
    }


    @Override
    public void fetchResults(final Results r) /* @ReadOnly */ {

        super.fetchResults(r);
        if (r.getClass().isAssignableFrom(VanillaSwapResults.class)) {
            final VanillaSwapResults results = (VanillaSwapResults) r;
            fairRate = results.fairRate;
            fairSpread = results.fairSpread;
        } else {
            fairRate   = Constants.NULL_REAL;
            fairSpread = Constants.NULL_REAL;
        }

        if (Double.isNaN(fairRate))
            // calculate it from other results
            if (!Double.isNaN(legBPS[0]))
                fairRate = fixedRate- NPV/(legBPS[0]/basisPoint);
        if (Double.isNaN(fairSpread))
            // ditto
            if (!Double.isNaN(legBPS[1]))
                fairSpread = spread - NPV/(legBPS[1]/basisPoint);
    }


    @Override
    public String toString() {
        return type.toString();
    }


    //
    // inner public enums
    //

    public enum Type {
        Receiver (-1),
        Payer (1);

        private final int enumValue;

        private Type(final int frequency) {
            this.enumValue = frequency;
        }

        static public Type valueOf(final int value) {
            switch (value) {
            case -1:
                return Type.Receiver;
            case 1:
                return Type.Payer;
            default:
                throw new LibraryException("value must be one of -1, 1"); // QA:[RG]::verified // TODO: message
            }
        }

        public int toInteger() {
            return this.enumValue;
        }
    }

}
