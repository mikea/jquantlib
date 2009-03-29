package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 11-months Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor11M extends Euribor {
    public Euribor11M(Handle<YieldTermStructure> h) {
        super(new Period(11, TimeUnit.MONTHS), h);
    }
}