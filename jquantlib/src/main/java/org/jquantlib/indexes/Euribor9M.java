package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 9-months Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor9M extends Euribor {
    public Euribor9M(Handle<YieldTermStructure> h) {
        super(new Period(9, TimeUnit.MONTHS), h);
    }
}