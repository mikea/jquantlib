/*
 Copyright (C) 2007 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */
package org.jquantlib.pricingengines.vanilla.finitedifferences;

import java.util.Vector;

import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.math.Array;
import org.jquantlib.math.SampledCurve;
import org.jquantlib.methods.finitedifferences.BoundaryCondition;
import org.jquantlib.methods.finitedifferences.BoundaryConditionSet;
import org.jquantlib.methods.finitedifferences.NullCondition;
import org.jquantlib.methods.finitedifferences.Operator;
import org.jquantlib.methods.finitedifferences.StepCondition;
import org.jquantlib.methods.finitedifferences.TridiagonalOperator;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.pricingengines.results.OneAssetOptionResults;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * @author Srinivas Hasti
 * 
 */
public abstract class FDStepConditionEngine extends FDVanillaEngine {
	protected StepCondition<Array> stepCondition;
	protected SampledCurve prices;
	protected TridiagonalOperator controlOperator;
	protected Vector<BoundaryCondition<TridiagonalOperator>> controlBCs;
	protected SampledCurve controlPrices;

	public FDStepConditionEngine(GeneralizedBlackScholesProcess process,
			int timeSteps, int gridPoints, boolean timeDependent) {
		super(process, timeSteps, gridPoints, timeDependent);
		this.controlBCs = new Vector<BoundaryCondition<TridiagonalOperator>>();
		this.controlPrices = new SampledCurve(gridPoints);
	}

	protected abstract void initializeStepCondition();

	protected void calculate(Results r) {
		OneAssetOptionResults results = (OneAssetOptionResults) (r);
		setGridLimits();
		initializeInitialCondition();
		initializeOperator();
		initializeBoundaryConditions();
		initializeStepCondition();

		// typedef StandardSystemFiniteDifferenceModel model_type;

		// model_type::operator_type operatorSet;
		// model_type::array_type arraySet;
		// model_type::bc_set bcSet;
		// model_type::condition_type conditionSet;
		Vector<Operator> operatorSet = new Vector<Operator>();
		Vector<Array> arraySet = new Vector<Array>();
		BoundaryConditionSet bcSet = new BoundaryConditionSet();
		Vector conditionSet = new Vector();

		prices = intrinsicValues;
		controlPrices = intrinsicValues;
		controlOperator = finiteDifferenceOperator;
		controlBCs.add(bcS.get(0));
		controlBCs.add(bcS.get(1));

		operatorSet.add(finiteDifferenceOperator);
		operatorSet.add(controlOperator);

		arraySet.add(prices.values());
		arraySet.add(controlPrices.values());

		bcSet.push_back(bcS);
		bcSet.push_back(controlBCs);

		conditionSet.add(stepCondition);
		conditionSet.add(new NullCondition());

		// TODO::
		// StandardSystemFiniteDifferenceModel model(operatorSet, bcSet);
		// ParallelEvolver<CrankNicolson> model = new
		// ParallelEvolver<CrankNicolson>(operatorSet,bcSet);
		// model.rollback(arraySet, getResidualTime(),
		// 0.0, timeSteps, conditionSet);

		prices.setValues(arraySet.get(0));
		controlPrices.setValues(arraySet.get(1));

		StrikedTypePayoff striked_payoff = (StrikedTypePayoff) (payoff);
		if (striked_payoff == null)
			throw new IllegalStateException("non-striked payoff given");

		double variance = process.blackVolatility().getLink().blackVariance(
				exerciseDate, striked_payoff.getStrike());
		double dividendDiscount = process.dividendYield().getLink().discount(
				exerciseDate);
		double riskFreeDiscount = process.riskFreeRate().getLink().discount(
				exerciseDate);
		double spot = process.stateVariable().getLink().evaluate();
		double forwardPrice = spot * dividendDiscount / riskFreeDiscount;

		BlackCalculator black = new BlackCalculator(striked_payoff,
				forwardPrice, Math.sqrt(variance), riskFreeDiscount);

		results.value = prices.valueAtCenter() - controlPrices.valueAtCenter()
				+ black.value();
		results.delta = prices.firstDerivativeAtCenter()
				- controlPrices.firstDerivativeAtCenter() + black.delta(spot);
		results.gamma = prices.secondDerivativeAtCenter()
				- controlPrices.secondDerivativeAtCenter() + black.gamma(spot);
		// TODO:
		// results.additionalResults["priceCurve"] = prices;
	}
}
