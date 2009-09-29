package org.jquantlib.instruments;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.daycounters.Thirty360;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.swap.DiscountingSwapEngine;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.DateGenerationRule;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

// TODO: code review :: Please complete this class and perform another code review.
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
// TODO: consider refactoring this class and make it an inner class
public class MakeVanillaSwap {


    private final Period swapTenor_;
    private final IborIndex iborIndex_;
    private final /*Rate*/ double fixedRate_;
    private final Period forwardStart_;

    private Date effectiveDate_, terminationDate_;
    private Calendar fixedCalendar_, floatCalendar_;

    private VanillaSwap.Type type_;
    private /*Real*/double nominal_;
    private Period fixedTenor_, floatTenor_;
    private BusinessDayConvention fixedConvention_, fixedTerminationDateConvention_;
    private BusinessDayConvention floatConvention_, floatTerminationDateConvention_;
    private DateGenerationRule fixedRule_, floatRule_;
    private boolean fixedEndOfMonth_, floatEndOfMonth_;
    private Date fixedFirstDate_, fixedNextToLastDate_;
    private Date floatFirstDate_, floatNextToLastDate_;
    private /*Spread*/double floatSpread_;
    private DayCounter fixedDayCount_, floatDayCount_;
    private PricingEngine engine_;

    public MakeVanillaSwap (
            final Period swapTenor,
            final IborIndex index) {
        this(swapTenor, index, 0.0, new Period(0,TimeUnit.DAYS));
    }

    public MakeVanillaSwap (
            final Period swapTenor,
            final IborIndex index,
            final /*Rate*/ double fixedRate) {
        this(swapTenor, index, fixedRate, new Period(0,TimeUnit.DAYS));
    }

    public MakeVanillaSwap(
            final Period swapTenor,
            final IborIndex index,
            final /* @Rate */ double fixedRate,
            final Period forwardStart) {
        final Date today = Date.todaysDate();
        this.swapTenor_ = (swapTenor);
        iborIndex_ = (index);
        fixedRate_ = (fixedRate);
        forwardStart_ = (forwardStart);
        effectiveDate_ = today.clone();
        fixedCalendar_ = (index.fixingCalendar());
        floatCalendar_ = (index.fixingCalendar());
        type_ = (VanillaSwap.Type.Payer);
        nominal_ = (1.0);
        fixedTenor_ = (new Period(1, TimeUnit.YEARS));
        floatTenor_ = (index.tenor());
        fixedConvention_ = (BusinessDayConvention.MODIFIED_FOLLOWING);
        fixedTerminationDateConvention_ = (BusinessDayConvention.MODIFIED_FOLLOWING);
        floatConvention_ = (index.getConvention());
        floatTerminationDateConvention_ = (index.getConvention());
        fixedRule_ = (DateGenerationRule.BACKWARD);
        floatRule_ = (DateGenerationRule.BACKWARD);
        fixedEndOfMonth_ = (false);
        floatEndOfMonth_ = (false);
        fixedFirstDate_ = today.clone();
        fixedNextToLastDate_ = today.clone();
        floatFirstDate_ = today.clone();
        floatNextToLastDate_ = today.clone();
        floatSpread_ = (0.0);
        fixedDayCount_ = (Thirty360.getDayCounter());
        floatDayCount_ = (index.dayCounter());
        engine_ = new DiscountingSwapEngine(index.termStructure());
    }


    public VanillaSwap value() /* @ReadOnly */ {

        //TODO: Code review :: incomplete code
        if (true) {
            throw new UnsupportedOperationException("Work in progress");
        }

        return null;

        //
        //Date startDate;
        //if (effectiveDate_ != Date())
        //startDate = effectiveDate_;
        //else {
        //Natural fixingDays = iborIndex_->fixingDays();
        //Date referenceDate = Settings::instance().evaluationDate();
        //Date spotDate = floatCalendar_.advance(referenceDate,
        //                          fixingDays*Days);
        //startDate = spotDate+forwardStart_;
        //}
        //
        //Date endDate;
        //if (terminationDate_ != Date())
        //endDate = terminationDate_;
        //else
        //endDate = startDate+swapTenor_;
        //
        //Schedule fixedSchedule(startDate, endDate,
        //      fixedTenor_, fixedCalendar_,
        //      fixedConvention_,
        //      fixedTerminationDateConvention_,
        //      fixedRule_, fixedEndOfMonth_,
        //      fixedFirstDate_, fixedNextToLastDate_);
        //
        //Schedule floatSchedule(startDate, endDate,
        //      floatTenor_, floatCalendar_,
        //      floatConvention_,
        //      floatTerminationDateConvention_,
        //      floatRule_ , floatEndOfMonth_,
        //      floatFirstDate_, floatNextToLastDate_);
        //
        //Rate usedFixedRate = fixedRate_;
        //if (fixedRate_ == Null<Rate>()) {
        //QL_REQUIRE(!iborIndex_->termStructure().empty(),
        //"no forecasting term structure set to " <<
        //iborIndex_->name());
        //VanillaSwap temp(type_, nominal_,
        //    fixedSchedule, 0.0, fixedDayCount_,
        //    floatSchedule, iborIndex_,
        //    floatSpread_, floatDayCount_);
        //// ATM on the forecasting curve
        //temp.setPricingEngine(boost::shared_ptr<PricingEngine>(new
        //DiscountingSwapEngine(iborIndex_->termStructure())));
        //usedFixedRate = temp.fairRate();
    }


    //boost::shared_ptr<VanillaSwap> swap(new
    //VanillaSwap(type_, nominal_,
    //       fixedSchedule, usedFixedRate, fixedDayCount_,
    //       floatSchedule, iborIndex_,
    //       floatSpread_, floatDayCount_));
    //swap->setPricingEngine(engine_);
    //return swap;
    //}



    public MakeVanillaSwap receiveFixed(final boolean flag) {
        // TODO: Code review :: incomplete code
        if (true) {
            throw new UnsupportedOperationException("Work in progress");
        }
        // TODO: code review :: please verify against QL/C++ code
        // The line below is certainly wrong!
        type_ = flag ? VanillaSwap.Type.Receiver : VanillaSwap.Type.Receiver;
        return this;
    }

    public MakeVanillaSwap withType(final VanillaSwap.Type type) {
        type_ = type;
        return this;
    }

    public MakeVanillaSwap withNominal(/* Real */final double n) {
        nominal_ = n;
        return this;
    }

    public MakeVanillaSwap withEffectiveDate(final Date effectiveDate) {
        effectiveDate_ = effectiveDate;
        return this;
    }

    public MakeVanillaSwap withTerminationDate(final Date terminationDate) {
        terminationDate_ = terminationDate;
        return this;
    }

    public MakeVanillaSwap withRule(final DateGenerationRule r) {
        fixedRule_ = r;
        floatRule_ = r;
        return this;
    }

    public MakeVanillaSwap withDiscountingTermStructure(final Handle<YieldTermStructure> discountingTermStructure) {
        engine_ = (new DiscountingSwapEngine(discountingTermStructure));
        return this;
    }

    public MakeVanillaSwap withFixedLegTenor(final Period t) {
        fixedTenor_ = t;
        return this;
    }

    public MakeVanillaSwap withFixedLegCalendar(final Calendar cal) {
        fixedCalendar_ = cal;
        return this;
    }

    public MakeVanillaSwap withFixedLegConvention(final BusinessDayConvention bdc) {
        fixedConvention_ = bdc;
        return this;
    }

    public MakeVanillaSwap withFixedLegTerminationDateConvention(final BusinessDayConvention bdc) {
        fixedTerminationDateConvention_ = bdc;
        return this;
    }

    public MakeVanillaSwap withFixedLegRule(final DateGenerationRule r) {
        fixedRule_ = r;
        return this;
    }

    public MakeVanillaSwap withFixedLegEndOfMonth(final boolean flag) {
        fixedEndOfMonth_ = flag;
        return this;
    }

    public MakeVanillaSwap withFixedLegFirstDate(final Date d) {
        fixedFirstDate_ = d;
        return this;
    }

    public MakeVanillaSwap withFixedLegNextToLastDate(final Date d) {
        fixedNextToLastDate_ = d;
        return this;
    }

    public MakeVanillaSwap withFixedLegDayCount(final DayCounter dc) {
        fixedDayCount_ = dc;
        return this;
    }

    public MakeVanillaSwap withFloatingLegTenor(final Period t) {
        floatTenor_ = t;
        return this;
    }

    public MakeVanillaSwap withFloatingLegCalendar(final Calendar cal) {
        floatCalendar_ = cal;
        return this;
    }

    public MakeVanillaSwap withFloatingLegConvention(final BusinessDayConvention bdc) {
        floatConvention_ = bdc;
        return this;
    }

    public MakeVanillaSwap withFloatingLegTerminationDateConvention(final BusinessDayConvention bdc) {
        floatTerminationDateConvention_ = bdc;
        return this;
    }

    public MakeVanillaSwap withFloatingLegRule(final DateGenerationRule r) {
        floatRule_ = r;
        return this;
    }

    public MakeVanillaSwap withFloatingLegEndOfMonth(final boolean flag) {
        floatEndOfMonth_ = flag;
        return this;
    }

    public MakeVanillaSwap withFloatingLegFirstDate(final Date d) {
        floatFirstDate_ = d;
        return this;
    }

    public MakeVanillaSwap withFloatingLegNextToLastDate(final Date d) {
        floatNextToLastDate_ = d;
        return this;
    }

    public MakeVanillaSwap withFloatingLegDayCount(final DayCounter dc) {
        floatDayCount_ = dc;
        return this;
    }

    public MakeVanillaSwap withFloatingLegSpread(/* Spread */final double sp) {
        floatSpread_ = sp;
        return this;
    }

}
