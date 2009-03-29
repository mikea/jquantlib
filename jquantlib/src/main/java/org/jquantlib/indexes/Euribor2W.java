package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 2-weeks Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor2W extends Euribor {
    public Euribor2W(Handle<YieldTermStructure> h) {
        super(new Period(2, TimeUnit.WEEKS), h);
    }
}