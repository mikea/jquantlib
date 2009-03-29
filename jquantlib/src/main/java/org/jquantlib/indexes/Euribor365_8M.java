package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 8-month Euribor365 index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor365_8M extends Euribor365{
    public Euribor365_8M(Handle<YieldTermStructure> h){
        super(new Period(8, TimeUnit.MONTHS), h);
    }
}