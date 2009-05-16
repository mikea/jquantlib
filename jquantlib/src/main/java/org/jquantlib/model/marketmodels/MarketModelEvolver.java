package org.jquantlib.model.marketmodels;

//! Market-model evolver
/*! Abstract base class. The evolver does the actual gritty work of
    evolving the forward rates from one time to the next.
*/

public abstract class MarketModelEvolver {
    
    abstract int [] numeraires();
    abstract double startNewPath();
    abstract double advanceStetep();
    abstract int currentStep();
    abstract CurveState currentState();
    abstract void setInitialState(CurveState curveState);

}
