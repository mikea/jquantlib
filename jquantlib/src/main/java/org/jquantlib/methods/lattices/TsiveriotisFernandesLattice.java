/*
 Copyright (C) 2008 Srinivas Hasti

 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */
package org.jquantlib.methods.lattices;

import org.jquantlib.assets.DiscretizedAsset;
import org.jquantlib.math.Array;
import org.jquantlib.math.Closeness;
import org.jquantlib.pricingengines.hybrid.DiscretizedConvertible;

/**
 * @author Srinivas Hasti
 * 
 */
public class TsiveriotisFernandesLattice<T extends Tree> extends
		BlackScholesLattice<T> {
	private double pd;
	private double pu;
	private double creditSpread;
	private double dt;
	private double riskFreeRate;

	public TsiveriotisFernandesLattice(T tree, double riskFreeRate, double end,
			int steps, double creditSpread, double sigma, double divYield) {
		super(tree, riskFreeRate, end, steps);

		dt = end / steps;

		pd = tree.probability(0, 0, 0);
		pu = tree.probability(0, 0, 1);

		this.riskFreeRate = riskFreeRate;
		this.creditSpread = creditSpread;

		if (pu > 1.0)
			throw new IllegalStateException("negative probability");
		if (pu < 0.0)
			throw new IllegalStateException("negative probability");
	}

	public void stepback(int i, Array values, Array conversionProbability,
			Array spreadAdjustedRate, Array newValues,
			Array newConversionProbability, Array newSpreadAdjustedRate) {

		for (int j = 0; j < size(i); j++) {

			// new conversion probability is calculated via backward
			// induction using up and down probabilities on tree on
			// previous conversion probabilities, ie weighted average
			// of previous probabilities.
			newConversionProbability.set(j, pd * conversionProbability.get(j)
					+ pu * conversionProbability.get(j + 1));

			// Use blended discounting rate
			newSpreadAdjustedRate.set(j, newConversionProbability.get(j)
					* riskFreeRate + (1 - newConversionProbability.get(j))
					* (riskFreeRate + creditSpread));

			newValues.set(j, (pd * values.get(j) / (1 + (spreadAdjustedRate.get(j) * dt))) 
			                 + (pu * values.get(j + 1) / (1 + (spreadAdjustedRate.get(j + 1) * dt))));
		}
	}

	public void rollback(DiscretizedAsset asset, double to) {
		partialRollback(asset, to);
		asset.adjustValues();
	}

	public void partialRollback(DiscretizedAsset asset, double to) {

		double from = asset.time();

		if (Closeness.isClose(from, to))
			return;

		if (from <= to)
			throw new IllegalStateException("cannot roll the asset back to "
					+ to + " (it is already at t = " + from + ")");

		DiscretizedConvertible convertible = (DiscretizedConvertible) (asset);
		
		int iFrom = t.index(from);
		int iTo = t.index(to);

		for (int i = iFrom - 1; i >= iTo; --i) {

			Array newValues = new Array(size(i));
			Array newSpreadAdjustedRate = new Array(size(i));
			Array newConversionProbability = new Array(size(i));

			stepback(i, convertible.values(), convertible
					.conversionProbability(), convertible.spreadAdjustedRate(),
		    			newValues, newConversionProbability, newSpreadAdjustedRate);

			convertible.setTime(t.get(i));
			convertible.setValues(newValues);
			convertible.setSpreadAdjustedRate(newSpreadAdjustedRate);
			convertible.setConversionProbability(newConversionProbability);

			// skip the very last adjustment
			if (i != iTo)
				convertible.adjustValues();
		}
	}

}
