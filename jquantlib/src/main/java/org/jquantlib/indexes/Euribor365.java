/**
 * 
 */
package org.jquantlib.indexes;

import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * Actual/365 %Euribor index
 * <p>
 * Euribor rate adjusted for the mismatch between the actual/360
 * convention used for Euribor and the actual/365 convention
 * previously used by a few pre-EUR currencies.
 * 
 * @author Ueli Hofstetter
 */
public class Euribor365 extends Euribor {
    
    public Euribor365(Period tenor, Handle<YieldTermStructure> h) {
        super("Euribor365", tenor, 2, Actual365Fixed.getDayCounter(), h); // settlement days
        if (tenor.units() == TimeUnit.DAYS)
            throw new IllegalArgumentException("for daily tenors dedicated DailyTenor constructor must be used"); // TODO: message
    }

}