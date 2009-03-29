package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 2-months Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor2M extends Euribor {
    public Euribor2M(Handle<YieldTermStructure> h) {
        super(new Period(2, TimeUnit.MONTHS), h);
    }
}