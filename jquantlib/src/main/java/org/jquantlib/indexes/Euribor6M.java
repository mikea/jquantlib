package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 6-months Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor6M extends Euribor {
    public Euribor6M(Handle<YieldTermStructure> h) {
        super(new Period(6, TimeUnit.MONTHS), h);
    }
}