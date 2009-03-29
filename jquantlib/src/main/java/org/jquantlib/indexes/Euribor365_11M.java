package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 11-month Euribor365 index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor365_11M extends Euribor365{
    public Euribor365_11M(Handle<YieldTermStructure> h){
        super(new Period(11, TimeUnit.MONTHS), h);
    }
}