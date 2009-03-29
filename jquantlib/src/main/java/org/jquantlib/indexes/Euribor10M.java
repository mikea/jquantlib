package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 10-months Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor10M extends Euribor {
    public Euribor10M(Handle<YieldTermStructure> h) {
        super(new Period(10, TimeUnit.MONTHS), h);
    }
}