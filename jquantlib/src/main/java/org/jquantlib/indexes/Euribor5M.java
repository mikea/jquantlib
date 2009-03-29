package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 5-months Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor5M extends Euribor {
    public Euribor5M(Handle<YieldTermStructure> h) {
        super(new Period(5, TimeUnit.MONTHS), h);
    }
}