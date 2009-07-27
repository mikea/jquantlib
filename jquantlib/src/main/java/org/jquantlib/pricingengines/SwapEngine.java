/**
 * 
 */
package org.jquantlib.pricingengines;

import org.jquantlib.pricingengines.arguments.SwapArguments;
import org.jquantlib.pricingengines.results.SwapResults;

public class SwapEngine extends GenericEngine<SwapArguments, SwapResults> {

    protected SwapEngine() {
        super(new SwapArguments(), new SwapResults());
    }
    
    @Override
    //TODO: code review
    public void calculate() /* @ReadOnly */ {
        // nothing
    }
    
}