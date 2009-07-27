/**
 * 
 */
package org.jquantlib.indexes;

import org.jquantlib.Validate;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;

/**
 * Actual/365 %Euribor index
 * <p>
 * Euribor rate adjusted for the mismatch between the actual/360
 * convention used for Euribor and the actual/365 convention
 * previously used by a few pre-EUR currencies.
 * 
 * @author Ueli Hofstetter
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class Euribor365 extends IborIndex {
    
    
    
    public Euribor365(final Period tenor, final Handle<YieldTermStructure> h) {
        super("Euribor365", tenor,
                2, // settlement days
                Target.getCalendar(), 
                new EURCurrency(), 
                euriborConvention(tenor), 
                euriborEOM(tenor),
                Actual365Fixed.getDayCounter(),
                h);
        Validate.QL_REQUIRE(this.tenor().units() != TimeUnit.DAYS, "for daily tenors dedicated DailyTenor constructor must be used");
    }

    
    public static Euribor365 getEuribor365_1W(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(1, TimeUnit.WEEKS), h);
    }

    public static Euribor365 getEuribor365_2W(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(2, TimeUnit.WEEKS), h);
    }

    public static Euribor365 getEuribor365_3W(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(3, TimeUnit.WEEKS), h);
    }

    public static Euribor365 getEuribor365_1M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(1, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_2M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(1, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_3M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(3, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_4M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(4, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_5M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(5, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_6M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(6, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_7M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(7, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_8M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(8, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_9M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(9, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_10M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(10, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor365_11M(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(11, TimeUnit.MONTHS), h);
    }

    public static Euribor365 getEuribor1Y(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(1, TimeUnit.YEARS), h);
    }

    public static Euribor365 getEuribor365_SW(Handle<YieldTermStructure> h) {
        return new Euribor365(new Period(1, TimeUnit.WEEKS), h);
    }    

}