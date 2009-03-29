package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 1-year Euribor index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor1Y extends Euribor{
    public Euribor1Y(Handle<YieldTermStructure> h){
        super(new Period(1, TimeUnit.YEARS), h);
    }
}