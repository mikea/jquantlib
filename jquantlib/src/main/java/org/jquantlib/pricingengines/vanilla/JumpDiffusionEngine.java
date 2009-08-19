/*
 Copyright (C) 2009 Richard Gomes

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

/*
 Copyright (C) 2004 Ferdinando Ametrano

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

/*
 Copyright (C) 2004 Ferdinando Ametrano
 Copyright (C) 2007 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.pricingengines.vanilla;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.Constants;
import org.jquantlib.math.distributions.PoissonDistribution;
import org.jquantlib.pricingengines.VanillaOptionEngine;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.pricingengines.results.OneAssetOptionResults;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.processes.Merton76Process;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.util.Date;

/**
 * Jump-diffusion engine for vanilla options
 *
 * @author <Richard Gomes>
 */
public class JumpDiffusionEngine extends VanillaOptionEngine {

    // TODO: refactor messages
    private static final double DEFAULT_RELATIVE_ACCURACY = 1e-4;
    private static final int DEFAULT_MAX_ITERATIONS = 100;

    private final VanillaOptionEngine baseEngine_;
    private final double relativeAccuracy_;
    private final int maxIterations_;


    public JumpDiffusionEngine(final VanillaOptionEngine baseEngine) {
        this(baseEngine, DEFAULT_RELATIVE_ACCURACY, DEFAULT_MAX_ITERATIONS);
    }

    public JumpDiffusionEngine(final VanillaOptionEngine baseEngine, final double relativeAccuracy) {
        this(baseEngine, relativeAccuracy, DEFAULT_MAX_ITERATIONS);
    }

    public JumpDiffusionEngine(final VanillaOptionEngine baseEngine, final double relativeAccuracy_, final int maxIterations) {
        this.baseEngine_ = baseEngine;
        this.maxIterations_ = maxIterations;
        this.relativeAccuracy_ = relativeAccuracy_;
        QL.require(this.baseEngine_ != null , "null base engine"); // QA:[RG]::verified // TODO: message
    }



    @Override
    public void calculate() {
        QL.require((this.arguments.stochasticProcess instanceof Merton76Process) , "not a jump diffusion process"); // QA:[RG]::verified // TODO: message

        final Merton76Process jdProcess = (Merton76Process) arguments.stochasticProcess;
        final double /* @Real */jumpSquareVol =
            jdProcess.logJumpVolatility().getLink().evaluate() * jdProcess.logJumpVolatility().getLink().evaluate();
        final double /* @Real */muPlusHalfSquareVol = jdProcess.logMeanJump().getLink().evaluate() + 0.5 * jumpSquareVol;

        // mean jump size
        final double /* @Real */k = Math.exp(muPlusHalfSquareVol) - 1.0;
        final double /* @Real */lambda = (k + 1.0) * jdProcess.jumpIntensity().getLink().evaluate();

        // dummy strike
        final double /* @Real */variance = jdProcess.blackVolatility().getLink().blackVariance(arguments.exercise.lastDate(), 1.0);

        final DayCounter voldc = jdProcess.blackVolatility().getLink().dayCounter();
        final Date volRefDate = jdProcess.blackVolatility().getLink().referenceDate();
        final double /* @Time */t = voldc.yearFraction(volRefDate, arguments.exercise.lastDate());

        final double /* @Rate */riskFreeRate = -Math.log(jdProcess.riskFreeRate().getLink().discount(arguments.exercise.lastDate())) / t;

        final Date rateRefDate = jdProcess.riskFreeRate().getLink().referenceDate();

        final PoissonDistribution p = new PoissonDistribution(lambda * t);

        baseEngine_.reset();

        final OneAssetOptionArguments baseArguments = baseEngine_.getArguments();

        baseArguments.payoff = arguments.payoff;
        baseArguments.exercise = arguments.exercise;
        final Handle<? extends Quote> stateVariable = jdProcess.stateVariable();
        final Handle<YieldTermStructure> dividendTS = jdProcess.dividendYield();
        final RelinkableHandle<YieldTermStructure> riskFreeTS =
            new RelinkableHandle<YieldTermStructure>(jdProcess.riskFreeRate().getLink());
        final RelinkableHandle<BlackVolTermStructure> volTS =
            new RelinkableHandle<BlackVolTermStructure>(jdProcess.blackVolatility().getLink());
        baseArguments.stochasticProcess = new GeneralizedBlackScholesProcess(stateVariable, dividendTS, riskFreeTS, volTS);
        baseArguments.validate();

        final OneAssetOptionResults baseResults = baseEngine_.getResults();

        results.value = 0.0;
        results.delta = 0.0;
        results.gamma = 0.0;
        results.theta = 0.0;
        results.vega = 0.0;
        results.rho = 0.0;
        results.dividendRho = 0.0;

        double /* @Real */r, v, weight, lastContribution = 1.0;
        int i;
        double /* @Real */theta_correction;

        // TODO: code review :: please verify against QL/C++ code
        // Haug arbitrary criterium is:
        // for (i=0; i<11; i++) {

        for (i = 0; lastContribution > relativeAccuracy_ && i < maxIterations_; i++) {

            // constant vol/rate assumption. It should be relaxed
            v = Math.sqrt((variance + i * jumpSquareVol) / t);
            r = riskFreeRate - jdProcess.jumpIntensity().getLink().evaluate() * k + i * muPlusHalfSquareVol / t;
            riskFreeTS.setLink(new FlatForward(rateRefDate, r, voldc));
            volTS.setLink(new BlackConstantVol(rateRefDate, v, voldc));

            baseArguments.validate();
            baseEngine_.calculate();

            weight = p.op(i);
            results.value += weight * baseResults.value;
            results.delta += weight * baseResults.delta;
            results.gamma += weight * baseResults.gamma;
            results.vega += weight * (Math.sqrt(variance / t) / v) * baseResults.vega;
            // theta modified
            theta_correction = baseResults.vega * ((i * jumpSquareVol) / (2.0 * v * t * t)) + baseResults.rho * i
            * muPlusHalfSquareVol / (t * t);
            results.theta += weight * (baseResults.theta + theta_correction + lambda * baseResults.value);
            if (i != 0)
                results.theta -= (p.op(i-1) * lambda * baseResults.value);
            // end theta calculation
            results.rho += weight * baseResults.rho;
            results.dividendRho += weight * baseResults.dividendRho;

            lastContribution = Math.abs(baseResults.value / (Math.abs(results.value) > Constants.QL_EPSILON ? results.value : 1.0));

            lastContribution = Math.max(lastContribution, Math.abs(baseResults.delta
                    / (Math.abs(results.delta) > Constants.QL_EPSILON ? results.delta : 1.0)));

            lastContribution = Math.max(lastContribution, Math.abs(baseResults.gamma
                    / (Math.abs(results.gamma) > Constants.QL_EPSILON ? results.gamma : 1.0)));

            lastContribution = Math.max(lastContribution, Math.abs(baseResults.theta
                    / (Math.abs(results.theta) > Constants.QL_EPSILON ? results.theta : 1.0)));

            lastContribution = Math.max(lastContribution, Math.abs(baseResults.vega
                    / (Math.abs(results.vega) > Constants.QL_EPSILON ? results.vega : 1.0)));

            lastContribution = Math.max(lastContribution, Math.abs(baseResults.rho
                    / (Math.abs(results.rho) > Constants.QL_EPSILON ? results.rho : 1.0)));

            lastContribution = Math.max(lastContribution, Math.abs(baseResults.dividendRho
                    / (Math.abs(results.dividendRho) > Constants.QL_EPSILON ? results.dividendRho : 1.0)));

            lastContribution *= weight;
        }

        QL.ensure(i < maxIterations_ , "accuracy not reached"); // QA:[RG]::verified // TODO: message
    }

}
