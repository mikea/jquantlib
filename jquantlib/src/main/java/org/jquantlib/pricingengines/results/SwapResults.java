/**
 * 
 */
package org.jquantlib.pricingengines.results;

import java.util.Arrays;


public class SwapResults extends Results {
    
    public double[] legNPV;
    public double[] legBPS;

    @Override
    public void reset() {
        super.reset();
        Arrays.fill(legNPV, 0.0);
        Arrays.fill(legBPS, 0.0);
    }

}