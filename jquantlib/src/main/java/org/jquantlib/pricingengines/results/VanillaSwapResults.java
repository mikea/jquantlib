package org.jquantlib.pricingengines.results;

import org.jquantlib.math.Constants;

/**
 * Results from simple swap calculation
 * 
 * @author Richard Gomes
 */
// TODO: code review :: object model needs to be validated and eventually refactored
public class VanillaSwapResults extends SwapResults {

    public /*@Rate*/ double  fairRate;
    public /*@Spread*/ double  fairSpread;

    @Override
    public void reset() {
        super.reset();
        fairRate   = Constants.NULL_Double;
        fairSpread = Constants.NULL_Double;
    }


}
