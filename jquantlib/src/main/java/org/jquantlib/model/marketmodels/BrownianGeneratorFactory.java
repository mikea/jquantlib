package org.jquantlib.model.marketmodels;

public abstract class  BrownianGeneratorFactory {
    public BrownianGeneratorFactory() {}

    public abstract BrownianGenerator create(int factors,int steps) ;

}
