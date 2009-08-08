package org.jquantlib.pricingengines.results;

import org.jquantlib.math.Constants;

public class BondResults extends Results {
	
	public /*@Real*/double settlementValue;
    public void reset() {
        settlementValue = Constants.NULL_REAL;
        super.reset();
    }

}
