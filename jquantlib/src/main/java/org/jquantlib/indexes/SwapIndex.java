package org.jquantlib.indexes;

import java.util.Currency;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.instruments.MakeVanillaSwap;
import org.jquantlib.instruments.VanillaSwap;
import org.jquantlib.lang.annotation.Rate;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.util.Date;

public class SwapIndex extends InterestRateIndex {
    
//    class IborIndex;
//    class Schedule;
//    class VanillaSwap;
    
    //protected double forecastFixing(const Date& fixingDate) const;
    protected Period tenor_;
    protected IborIndex iborIndex_;
    protected Period fixedLegTenor_;
    BusinessDayConvention fixedLegConvention_;

    public SwapIndex (final String familyName,
            final Period tenor,
                  /*Natural*/int settlementDays,
                  Currency currency,
                  final Calendar calendar,
                  final Period fixedLegTenor,
                  BusinessDayConvention fixedLegConvention,
                  final DayCounter fixedLegDayCounter,
                  final IborIndex iborIndex){
        super(familyName, tenor, settlementDays,
                currency, calendar, fixedLegDayCounter);
        this.tenor_=(tenor);
        this.iborIndex_=(iborIndex);
        this.fixedLegTenor_=(fixedLegTenor);
        this.fixedLegConvention_=(fixedLegConvention);
        this.iborIndex_.addObserver(this); 
    }
    
    public Handle<YieldTermStructure> termStructure() {
        return iborIndex_.getTermStructure();
    }

//    public /*@Rate*/ forecastFixing(final Date fixingDate)  {
//        return underlyingSwap(fixingDate)->fairRate();
//    }
//
//    boost::shared_ptr<VanillaSwap> SwapIndex::underlyingSwap(
//                                               const Date& fixingDate) const {
//        Rate fixedRate = 0.0;
//        return MakeVanillaSwap(tenor_, iborIndex_, fixedRate)
//            .withEffectiveDate(valueDate(fixingDate))
//            .withFixedLegCalendar(fixingCalendar())
//            .withFixedLegDayCount(dayCounter_)
//            .withFixedLegTenor(fixedLegTenor_)
//            .withFixedLegConvention(fixedLegConvention_)
//            .withFixedLegTerminationDateConvention(fixedLegConvention_);
//    }
//
//    Date SwapIndex::maturityDate(const Date& valueDate) const {
//        Date fixDate = fixingDate(valueDate);
//        return underlyingSwap(fixDate)->maturityDate();
//    }



    // inline definitions

//    inline BusinessDayConvention SwapIndex::fixedLegConvention() const {
//        return fixedLegConvention_;
//    }
//
//    inline boost::shared_ptr<IborIndex> SwapIndex::iborIndex() const {
//        return iborIndex_;
//    }

//}
    @Override
    protected double forecastFixing(Date fixingDate) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public Handle<YieldTermStructure> getTermStructure() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Date maturityDate(Date valueDate) {
        Date fixDate = fixingDate(valueDate);
        return null;//underlyingSwap(fixDate);
    }
    
    private VanillaSwap underlyingSwap(
            final Date fixingDate)  {
        /*Rate*/ double fixedRate = 0.0;
//return new MakeVanillaSwap(tenor_, iborIndex_, fixedRate)
//.withEffectiveDate(valueDate(fixingDate)))
//.withFixedLegCalendar(fixingCalendar())
//.withFixedLegDayCount(dayCounter)
//.withFixedLegTenor(fixedLegTenor_)
//.withFixedLegConvention(fixedLegConvention_)
//.withFixedLegTerminationDateConvention(fixedLegConvention_)
    return new VanillaSwap(null, null, null);
    }
}
