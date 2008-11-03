package org.jquantlib.pricingengines.arguments;

import org.jquantlib.instruments.AverageType;

public class ContinuousAveragingAsianOptionArguments extends OneAssetStrikedOptionArguments {

	public ContinuousAveragingAsianOptionArguments() {
		//averageType(Average::Type(-1)) {}

	}
    
	@Override
	public void validate() /*@ReadOnly*/{
        super.validate();
        //FIXME review interpretation of enum=-1...
        //QL_REQUIRE(Integer(averageType) != -1, "unspecified average type");
        if(averageType==null){
        	throw new IllegalArgumentException("unspecified average type");
        }

    }

    public AverageType averageType;
}
