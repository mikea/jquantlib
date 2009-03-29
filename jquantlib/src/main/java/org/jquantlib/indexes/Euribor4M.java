package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 4-months Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor4M extends Euribor {
    public Euribor4M(Handle<YieldTermStructure> h) {
        super(new Period(4, TimeUnit.MONTHS), h);
    }
}