package org.jquantlib.pricingengines.arguments;

import java.util.List;

import org.jquantlib.instruments.VanillaSwap.Type;
import org.jquantlib.util.Date;

/**
 * Arguments for simple swap calculation
 *
 * @author Richard Gomes
 */
// TODO: code review :: object model needs to be validated and eventually refactored
public class VanillaSwapArguments extends SwapArguments {

    public Type type;
    public /*@Price*/ double nominal;

    public List<Date> fixedResetDates;
    public List<Date> fixedPayDates;
    public List</*@Time*/ Double> floatingAccrualTimes;
    public List<Date> floatingResetDates;
    public List<Date> floatingFixingDates;
    public List<Date> floatingPayDates;

    public List</*@Price*/ Double> fixedCoupons;
    public List</*@Spread*/ Double> floatingSpreads;
    public List</*@Price*/ Double> floatingCoupons;


    @Override
    public void validate() /* @ReadOnly */ {
        super.validate();
        assert !Double.isNaN(nominal) : "nominal null or not set";
        assert fixedResetDates.size() == fixedPayDates.size() : "number of fixed start dates different from number of fixed payment dates";
        assert fixedPayDates.size() == fixedCoupons.size() : "number of fixed payment dates different from number of fixed coupon amounts";
        assert floatingResetDates.size() == floatingPayDates.size() : "number of floating start dates different from number of floating payment dates";
        assert floatingFixingDates.size() == floatingPayDates.size() : "number of floating fixing dates different from number of floating payment dates";
        assert floatingAccrualTimes.size() == floatingPayDates.size() : "number of floating accrual Times different from number of floating payment dates";
        assert floatingSpreads.size() == floatingPayDates.size() : "number of floating spreads different from number of floating payment dates";
        assert floatingPayDates.size() == floatingCoupons.size() : "number of floating payment dates different from number of floating coupon amounts";
    }

}
