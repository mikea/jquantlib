package org.jquantlib.cashflow;

import java.util.List;

/**
 * 
 * @author Richard Gomes
 */
// TODO: code review :: Please review this class! :S
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class PricerSetter {

    /**
     * Singleton instance for the whole application.
     * <p>
     * In an application server environment, it could be by class loader depending on scope of the jquantlib library to the module.
     * 
     * @see <a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">The "Double-Checked Locking is Broken"
     *      Declaration </a>
     */
    private static volatile PricerSetter instance = null;

    //
    // private constructors
    //
    
    private PricerSetter() {
        // cannot be directly instantiated
    }


    //
    // public static methods
    //
    
    public static PricerSetter getInstance() {
        if (instance == null) {
            synchronized (PricerSetter.class) {
                if (instance == null) {
                    instance = new PricerSetter();
                }
            }
        }
        return instance;
    }
    
   
public void setCouponPricer(final Leg leg, final FloatingRateCouponPricer pricer) {
    
    //TODO: Code review :: incomplete code
    if (true)
        throw new UnsupportedOperationException("Work in progress");
    
//PricerSetter setter(pricer);
//for (Size i=0; i<leg.size(); ++i) {
//leg[i]->accept(setter);
//}
}

public void setCouponPricers(final Leg leg, final List<FloatingRateCouponPricer> pricers) {
    
    //TODO: Code review :: incomplete code
    if (true)
        throw new UnsupportedOperationException("Work in progress");
    
//Size nCashFlows = leg.size();
//QL_REQUIRE(nCashFlows>0, "no cashflows");
//
//Size nPricers = pricers.size();
//QL_REQUIRE(nCashFlows >= nPricers,
//   "mismatch between leg size (" << nCashFlows <<
//   ") and number of pricers (" << nPricers << ")");
//
//for (Size i=0; i<nCashFlows; ++i) {
//PricerSetter setter(i<nPricers ? pricers[i] : pricers[nPricers-1]);
//leg[i]->accept(setter);
//}
}

    
    
}


//namespace {
//
//    class PricerSetter : public AcyclicVisitor,
//                         public Visitor<CashFlow>,
//                         public Visitor<Coupon>,
//                         public Visitor<IborCoupon>,
//                         public Visitor<CmsCoupon>,
//                         public Visitor<CappedFlooredIborCoupon>,
//                         public Visitor<CappedFlooredCmsCoupon>,
//                         public Visitor<DigitalIborCoupon>,
//                         public Visitor<DigitalCmsCoupon>,
//                         public Visitor<RangeAccrualFloatersCoupon>,
//                         public Visitor<SubPeriodsCoupon> {
//      private:
//        const boost::shared_ptr<FloatingRateCouponPricer> pricer_;
//      public:
//        PricerSetter(
//                const boost::shared_ptr<FloatingRateCouponPricer>& pricer)
//        : pricer_(pricer) {}
//
//        void visit(CashFlow& c);
//        void visit(Coupon& c);
//        void visit(IborCoupon& c);
//        void visit(CappedFlooredIborCoupon& c);
//        void visit(DigitalIborCoupon& c);
//        void visit(CmsCoupon& c);
//        void visit(CappedFlooredCmsCoupon& c);
//        void visit(DigitalCmsCoupon& c);
//        void visit(RangeAccrualFloatersCoupon& c);
//        void visit(SubPeriodsCoupon& c);
//    };
//
//    void PricerSetter::visit(CashFlow&) {
//        // nothing to do
//    }
//
//    void PricerSetter::visit(Coupon&) {
//        // nothing to do
//    }
//
//    void PricerSetter::visit(IborCoupon& c) {
//        const boost::shared_ptr<IborCouponPricer> iborCouponPricer =
//            boost::dynamic_pointer_cast<IborCouponPricer>(pricer_);
//        QL_REQUIRE(iborCouponPricer,
//                   "pricer not compatible with Ibor coupon");
//        c.setPricer(iborCouponPricer);
//    }
//
//    void PricerSetter::visit(DigitalIborCoupon& c) {
//        const boost::shared_ptr<IborCouponPricer> iborCouponPricer =
//            boost::dynamic_pointer_cast<IborCouponPricer>(pricer_);
//        QL_REQUIRE(iborCouponPricer,
//                   "pricer not compatible with Ibor coupon");
//        c.setPricer(iborCouponPricer);
//    }
//
//    void PricerSetter::visit(CappedFlooredIborCoupon& c) {
//        const boost::shared_ptr<IborCouponPricer> iborCouponPricer =
//            boost::dynamic_pointer_cast<IborCouponPricer>(pricer_);
//        QL_REQUIRE(iborCouponPricer,
//                   "pricer not compatible with Ibor coupon");
//        c.setPricer(iborCouponPricer);
//    }
//
//    void PricerSetter::visit(CmsCoupon& c) {
//        const boost::shared_ptr<CmsCouponPricer> cmsCouponPricer =
//            boost::dynamic_pointer_cast<CmsCouponPricer>(pricer_);
//        QL_REQUIRE(cmsCouponPricer,
//                   "pricer not compatible with CMS coupon");
//        c.setPricer(cmsCouponPricer);
//    }
//
//    void PricerSetter::visit(CappedFlooredCmsCoupon& c) {
//        const boost::shared_ptr<CmsCouponPricer> cmsCouponPricer =
//            boost::dynamic_pointer_cast<CmsCouponPricer>(pricer_);
//        QL_REQUIRE(cmsCouponPricer,
//                   "pricer not compatible with CMS coupon");
//        c.setPricer(cmsCouponPricer);
//    }
//
//    void PricerSetter::visit(DigitalCmsCoupon& c) {
//        const boost::shared_ptr<CmsCouponPricer> cmsCouponPricer =
//            boost::dynamic_pointer_cast<CmsCouponPricer>(pricer_);
//        QL_REQUIRE(cmsCouponPricer,
//                   "pricer not compatible with CMS coupon");
//        c.setPricer(cmsCouponPricer);
//    }
//
//    void PricerSetter::visit(RangeAccrualFloatersCoupon& c) {
//        const boost::shared_ptr<RangeAccrualPricer> rangeAccrualPricer =
//            boost::dynamic_pointer_cast<RangeAccrualPricer>(pricer_);
//        QL_REQUIRE(rangeAccrualPricer,
//                   "pricer not compatible with range-accrual coupon");
//        c.setPricer(rangeAccrualPricer);
//    }
//
//    void PricerSetter::visit(SubPeriodsCoupon& c) {
//        const boost::shared_ptr<SubPeriodsPricer> subPeriodsPricer =
//            boost::dynamic_pointer_cast<SubPeriodsPricer>(pricer_);
//        QL_REQUIRE(subPeriodsPricer,
//                   "pricer not compatible with sub-period coupon");
//        c.setPricer(subPeriodsPricer);
//    }
//
//}
//
//}
