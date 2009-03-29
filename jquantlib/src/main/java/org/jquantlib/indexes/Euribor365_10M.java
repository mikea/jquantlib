package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 10-month Euribor365 index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor365_10M extends Euribor365{
    public Euribor365_10M(Handle<YieldTermStructure> h){
        super(new Period(10, TimeUnit.MONTHS), h);
    }
}