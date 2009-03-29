package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 3-week Euribor365 index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor365_3W extends Euribor365{
    public Euribor365_3W(Handle<YieldTermStructure> h){
        super(new Period(3, TimeUnit.WEEKS), h);
    }
}