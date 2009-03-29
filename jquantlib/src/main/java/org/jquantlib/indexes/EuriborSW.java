package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 1-week Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class EuriborSW extends Euribor {
    public EuriborSW(Handle<YieldTermStructure> h) {
        super(new Period(1, TimeUnit.WEEKS), h);
    }
}