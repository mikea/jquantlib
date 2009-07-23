

package org.jquantlib.instruments;

import java.util.List;

import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Schedule;

public class VanillaSwap extends Swap {
    
    
    private Type type_;
    private /*Real*/ double nominal_;
    private Schedule fixedSchedule_;
    private /*Rate*/double fixedRate_;
    private DayCounter fixedDayCount_;
    private Schedule floatingSchedule_;
    private IborIndex iborIndex_;
    private /*Spread*/double spread_;
    private DayCounter floatingDayCount_;
    private BusinessDayConvention paymentConvention_;
    // results
    private /*mutable Rate*/ double fairRate_;
    private /*mutable Spread*/ double fairSpread_;
    
    enum Type{RECEIVER /*-1*/, PAYER /*1*/}
    
    //  class Schedule;
//  class IborIndex;
//class results;
//class engine;
//  class arguments;

    public VanillaSwap(Handle<YieldTermStructure> termStructure, List<CashFlow> firstLeg, List<CashFlow> secondLeg) {
        super(termStructure, firstLeg, secondLeg);
        // TODO Auto-generated constructor stub
    }
    
    public VanillaSwap(Type type,
            /*Real*/double nominal,
            final Schedule fixedSchedule,
            /*Rate*/ double fixedRate,
            final DayCounter fixedDayCount,
            final Schedule floatSchedule,
            final IborIndex iborIndex,
            /*Spread*/ double spread,
            final DayCounter floatingDayCount){
        this(type, nominal, fixedSchedule, fixedRate, fixedDayCount, floatSchedule, iborIndex, spread, floatingDayCount, BusinessDayConvention.FOLLOWING);
    }
    
    public VanillaSwap(Type type,
                    /*Real*/double nominal,
                    final Schedule fixedSchedule,
                    /*Rate*/ double fixedRate,
                    final DayCounter fixedDayCount,
                    final Schedule floatSchedule,
                    final IborIndex iborIndex,
                    /*Spread*/ double spread,
                    final DayCounter floatingDayCount,
                    BusinessDayConvention paymentConvention){
        super(2);
        this.type_=(type);
        this.nominal_=(nominal);
        this.fixedSchedule_=(fixedSchedule);
        this.fixedRate_=(fixedRate);
        this.fixedDayCount_=(fixedDayCount);
        this.floatingSchedule_=(floatSchedule);
       // this.iborIndex_(iborIndex);
        this.spread_=(spread);
        this.floatingDayCount_=(floatingDayCount);
        this.paymentConvention_=(paymentConvention);

//          List<CashFlow> fixedLeg = Leg.FixedRateLeg(fixedSchedule_, fixedDayCount_)
//              .withNotionals(nominal_)
//              .withCouponRates(fixedRate_)
//              .withPaymentAdjustment(paymentConvention_);
//
//          Leg floatingLeg = IborLeg(floatingSchedule_, iborIndex_)
//              .withNotionals(nominal_)
//              .withPaymentDayCounter(floatingDayCount_)
//              .withPaymentAdjustment(paymentConvention_)
//              //.withFixingDays(iborIndex->fixingDays())
//              .withSpreads(spread_);
//
//          Leg::const_iterator i;
//          for (i = floatingLeg.begin(); i < floatingLeg.end(); ++i)
//              registerWith(*i);
//
//          legs_[0] = fixedLeg;
//          legs_[1] = floatingLeg;
//          if (type_==Payer) {
//              payer_[0] = -1.0;
//              payer_[1] = +1.0;
//          } else {
//              payer_[0] = +1.0;
//              payer_[1] = -1.0;
//          }
//      }
        
    }
//        // results
//        Real fixedLegBPS() const;
//        Real fixedLegNPV() const;
//        Rate fairRate() const;
//
//        Real floatingLegBPS() const;
//        Real floatingLegNPV() const;
//        Spread fairSpread() const;
//        // inspectors
//        Type type() const;
//        Real nominal() const;
//
//        const Schedule& fixedSchedule() const;
//        Rate fixedRate() const;
//        const DayCounter& fixedDayCount() const;
//
//        const Schedule& floatingSchedule() const;
//        const boost::shared_ptr<IborIndex>& iborIndex() const;
//        Spread spread() const;
//        const DayCounter& floatingDayCount() const;
//
//        BusinessDayConvention paymentConvention() const;
//
//        const Leg& fixedLeg() const;
//        const Leg& floatingLeg() const;
//        // other
//        void setupArguments(PricingEngine::arguments* args) const;
//        void fetchResults(const PricingEngine::results*) const;
//      private:
//        void setupExpired() const;
//        Type type_;
//        Real nominal_;
//        Schedule fixedSchedule_;
//        Rate fixedRate_;
//        DayCounter fixedDayCount_;
//        Schedule floatingSchedule_;
//        boost::shared_ptr<IborIndex> iborIndex_;
//        Spread spread_;
//        DayCounter floatingDayCount_;
//        BusinessDayConvention paymentConvention_;
//        // results
//        mutable Rate fairRate_;
//        mutable Spread fairSpread_;
//    };
//
//
//    //! %Arguments for simple swap calculation
//    class VanillaSwap::arguments : public Swap::arguments {
//      public:
//        arguments() : type(Receiver),
//                      nominal(Null<Real>()) {}
//        Type type;
//        Real nominal;
//
//        std::vector<Date> fixedResetDates;
//        std::vector<Date> fixedPayDates;
//        std::vector<Time> floatingAccrualTimes;
//        std::vector<Date> floatingResetDates;
//        std::vector<Date> floatingFixingDates;
//        std::vector<Date> floatingPayDates;
//
//        std::vector<Real> fixedCoupons;
//        std::vector<Spread> floatingSpreads;
//        std::vector<Real> floatingCoupons;
//        void validate() const;
//    };
//
//    //! %Results from simple swap calculation
//    class VanillaSwap::results : public Swap::results {
//      public:
//        Rate fairRate;
//        Spread fairSpread;
//        void reset();
//    };
//
//    class VanillaSwap::engine : public GenericEngine<VanillaSwap::arguments,
//                                                     VanillaSwap::results> {};
//
//
//    // inline definitions
//
//    inline VanillaSwap::Type VanillaSwap::type() const {
//        return type_;
//    }
//
//    inline Real VanillaSwap::nominal() const {
//        return nominal_;
//    }
//
//    inline const Schedule& VanillaSwap::fixedSchedule() const {
//        return fixedSchedule_;
//    }
//
//    inline Rate VanillaSwap::fixedRate() const {
//        return fixedRate_;
//    }
//
//    inline const DayCounter& VanillaSwap::fixedDayCount() const {
//        return fixedDayCount_;
//    }
//
//    inline const Schedule& VanillaSwap::floatingSchedule() const {
//        return floatingSchedule_;
//    }
//
//    inline const boost::shared_ptr<IborIndex>& VanillaSwap::iborIndex() const {
//        return iborIndex_;
//    }
//
//    inline Spread VanillaSwap::spread() const {
//        return spread_;
//    }
//
//    inline const DayCounter& VanillaSwap::floatingDayCount() const {
//        return floatingDayCount_;
//    }
//
//    inline BusinessDayConvention VanillaSwap::paymentConvention() const {
//        return paymentConvention_;
//    }
//
//    inline const Leg& VanillaSwap::fixedLeg() const {
//        return legs_[0];
//    }
//
//    inline const Leg& VanillaSwap::floatingLeg() const {
//        return legs_[1];
//    }
//
//    std::ostream& operator<<(std::ostream& out,
//                             VanillaSwap::Type t);
}
