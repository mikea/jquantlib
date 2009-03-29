package org.jquantlib.indexes;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * 2-week Euribor365 index
 * 
 * @author Ueli Hofstetter
 */
public class Euribor365_2W extends Euribor365{
    public Euribor365_2W(Handle<YieldTermStructure> h){
        super(new Period(1, TimeUnit.WEEKS), h);
    }
}