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

import org.jquantlib.math.SampledCurve;
import org.jquantlib.methods.finitedifferences.StandardFiniteDifferenceModel;
import org.jquantlib.pricingengines.Greeks;
import org.jquantlib.pricingengines.OneAssetOptionEngine;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * Pricing engine for European options using finite-differences
 * 
 * @category vanillaengines
 * 
 * @author Srinivas Hasti
 */
public class FDEuropeanEngine extends OneAssetOptionEngine {
    private final FDVanillaEngine fdVanillaEngine;
    private SampledCurve prices;

    public FDEuropeanEngine(GeneralizedBlackScholesProcess process, int timeSteps, int gridPoints, boolean timeDependent) {
        fdVanillaEngine = new FDVanillaEngine(process, timeSteps, gridPoints, timeDependent);
        prices = new SampledCurve(gridPoints);
    }

    public FDEuropeanEngine(GeneralizedBlackScholesProcess stochProcess, int binomialSteps, int samples) {
        this(stochProcess,binomialSteps,samples,false);
    }

    @Override
    public void calculate() {
        fdVanillaEngine.setupArguments(arguments);
        fdVanillaEngine.setGridLimits();
        fdVanillaEngine.initializeInitialCondition();
        fdVanillaEngine.initializeOperator();
        fdVanillaEngine.initializeBoundaryConditions();

        StandardFiniteDifferenceModel model = new StandardFiniteDifferenceModel(fdVanillaEngine.finiteDifferenceOperator, fdVanillaEngine.bcS);

        prices = new SampledCurve(fdVanillaEngine.intrinsicValues);

        prices.setValues(model.rollback(prices.values(), fdVanillaEngine.getResidualTime(), 0, fdVanillaEngine.timeSteps));

        results.value = prices.valueAtCenter();
        results.delta = prices.firstDerivativeAtCenter();
        results.gamma = prices.secondDerivativeAtCenter();
        results.theta = Greeks.blackScholesTheta(fdVanillaEngine.process, results.value, results.delta, results.gamma);
        results.addAdditionalResult("priceCurve",prices);
    }

}
