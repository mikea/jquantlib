package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 1-year Euribor365 index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor365_1Y extends Euribor365{
    public Euribor365_1Y(Handle<YieldTermStructure> h){
        super(new Period(1, TimeUnit.YEARS), h);
    }
}