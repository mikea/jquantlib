package org.jquantlib.model.marketmodels;

public abstract class BrownianGenerator {
    
    public BrownianGenerator(){
        
    }
    
    public abstract double nextStep();
    public abstract double nextPath();
    public abstract int numberOfFactors();
    public abstract int numberOfSteps();

}
