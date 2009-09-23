package org.jquantlib.instruments;

import org.jquantlib.QL;
import org.jquantlib.cashflow.FixedRateLeg;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.DateGenerationRule;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.Schedule;

public class FixedRateBond extends Bond {

    protected Frequency frequency_;
    protected DayCounter dayCounter_;

    /**
     * 
     * @param settlementDays
     * @param faceAmount
     * @param schedule
     * @param coupons
     * @param accrualDayCounter
     * @param paymentConvention default: Following
     * @param redemption default: 100
     * @param issueDate default: new Date()
     */
    public FixedRateBond(/*@Natural*/final int settlementDays,
            /*@Real*/final double faceAmount,
            final Schedule schedule,
            final double[] coupons,
            final DayCounter accrualDayCounter,
            final BusinessDayConvention paymentConvention,
            /*Real*/final double redemption,
            final Date  issueDate){
        super(settlementDays, schedule.getCalendar(), issueDate);
        frequency_ = schedule.tenor().frequency();
        dayCounter_ = accrualDayCounter;
        //maturityDate_ = schedule.endDate();
        cashFlows_ = new FixedRateLeg(schedule, accrualDayCounter)
        .withNotionals(faceAmount)
        .withCouponRates(coupons)
        .withPaymentAdjustment(paymentConvention);

        addRedemptionsToCashflows(new double[]{redemption});

        QL.ensure(!cashFlows().isEmpty(), "bond with no cashflows!");
        QL.ensure(redemptions_.size() == 1, "multiple redemptions created");
    }

    /**
     * 
     * @param settlementDays
     * @param calendar
     * @param faceAmount
     * @param startDate
     * @param maturityDate
     * @param tenor
     * @param coupons
     * @param accrualDayCounter
     * @param accrualConvention default: Following
     * @param paymentConvention default: Following
     * @param redemption default: 100
     * @param issueDate default: Date()
     * @param stubDate default: Date()
     * @param rule default: Backward
     * @param endOfMonth default: false
     */
    public FixedRateBond(/*@Natural*/final int settlementDays,
            final Calendar  calendar,
            /*@Real*/ final double faceAmount,
            final Date  startDate,
            final Date  maturityDate,
            final Period  tenor,
            final double[] coupons,
            final DayCounter  accrualDayCounter,
            final BusinessDayConvention accrualConvention,
            final BusinessDayConvention paymentConvention,
            /*@Real*/ final double redemption,
            final Date  issueDate ,
            final Date  stubDate ,
            final DateGenerationRule  rule  ,
            final boolean endOfMonth){
        super(settlementDays, calendar, issueDate);
        frequency_=(tenor.frequency());
        dayCounter_=(accrualDayCounter);
        maturityDate_ = maturityDate.clone();

        Date firstDate = new Date();
        Date nextToLastDate = new Date();
        switch (rule) {
        case BACKWARD:
            firstDate = new Date();
            nextToLastDate = stubDate.clone();
            break;
        case FORWARD:
            firstDate = stubDate.clone();
            nextToLastDate = new Date();
            break;
        case ZERO:
            reportFalseDateGenerationRule(stubDate, rule);
            break;
        case THIRD_WEDNESDAY:
            reportFalseDateGenerationRule(stubDate, rule);
            break;
        case  TWENTIEHT:
            reportFalseDateGenerationRule(stubDate, rule);
            break;
        case  TWENTIEHTIMM:
            reportFalseDateGenerationRule(stubDate, rule);
            break;
        default:
            QL.error("unknown DateGeneration::Rule (" + rule + ")");
        }

        final Schedule schedule = new Schedule(startDate, maturityDate_, tenor,
                calendar_, accrualConvention, accrualConvention,
                rule, endOfMonth,
                firstDate, nextToLastDate);

        cashFlows_ = new FixedRateLeg(schedule, accrualDayCounter)
        .withNotionals(faceAmount)
        .withCouponRates(coupons)
        .withPaymentAdjustment(paymentConvention);

        addRedemptionsToCashflows(new double[]{redemption});

        QL.ensure(!cashFlows().isEmpty(), "bond with no cashflows!");
        QL.ensure(redemptions_.size() == 1, "multiple redemptions created");
    }

    public Frequency frequency(){
        return frequency_;
    }

    public DayCounter dayCounter(){
        return dayCounter_;
    }

    private void reportFalseDateGenerationRule(final Date stubDate, final DateGenerationRule rule){
        QL.error("stub date ("+ stubDate + ") not allowed with " +
                rule + " DateGeneration::Rule");
    }
}
