package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 1-month Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor1M extends Euribor {
    public Euribor1M(Handle<YieldTermStructure> h) {
        super(new Period(1, TimeUnit.MONTHS), h);
    }
}