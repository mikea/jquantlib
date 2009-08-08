package org.jquantlib.pricingengines;

import org.jquantlib.pricingengines.arguments.BondArguments;
import org.jquantlib.pricingengines.results.BondResults;

public class BondEngine extends GenericEngine<BondArguments, BondResults> {

	protected BondEngine(BondArguments arguments, BondResults results) {
		super(arguments, results);
		// TODO Auto-generated constructor stub
	}

	@Override
	@Deprecated
	public void calculate() {
		throw new UnsupportedOperationException();
		
	}

}
