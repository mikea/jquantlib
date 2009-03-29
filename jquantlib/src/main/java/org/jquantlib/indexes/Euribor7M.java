package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 7-months Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor7M extends Euribor {
    public Euribor7M(Handle<YieldTermStructure> h) {
        super(new Period(7, TimeUnit.MONTHS), h);
    }
}