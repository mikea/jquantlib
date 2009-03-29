package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 8-months Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor8M extends Euribor {
    public Euribor8M(Handle<YieldTermStructure> h) {
        super(new Period(8, TimeUnit.MONTHS), h);
    }
}