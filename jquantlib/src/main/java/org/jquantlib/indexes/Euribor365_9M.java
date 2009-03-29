package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 9-month Euribor365 index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor365_9M extends Euribor365{
    public Euribor365_9M(Handle<YieldTermStructure> h){
        super(new Period(9, TimeUnit.MONTHS), h);
    }
}