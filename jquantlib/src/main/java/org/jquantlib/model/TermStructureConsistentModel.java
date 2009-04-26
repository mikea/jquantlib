package org.jquantlib.model;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Observable;

public abstract class TermStructureConsistentModel implements Observable {
    private Handle<YieldTermStructure> termStructure_;
    
    public TermStructureConsistentModel(final Handle<YieldTermStructure> termStructure){
        this.termStructure_ = termStructure;
    }
    
    public Handle<YieldTermStructure> termStructure(){
        return termStructure_;
    }
}
