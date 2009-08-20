/*
 Copyright (C) 2007 Richard Gomes

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
 Copyright (C) 2003 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
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

package org.jquantlib.instruments;

import java.util.List;

import org.joda.primitives.list.impl.ArrayDoubleList;
import org.jquantlib.Configuration;
import org.jquantlib.QL;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.math.AbstractSolver1D;
import org.jquantlib.math.Ops;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.pricingengines.arguments.OptionArguments;
import org.jquantlib.pricingengines.results.Greeks;
import org.jquantlib.pricingengines.results.MoreGreeks;
import org.jquantlib.pricingengines.results.OneAssetOptionResults;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.util.Date;

public class OneAssetOption extends Option {

    private static final String WRONG_ARGUMENT_TYPE = "wrong argument type";


    // private fields
    //

    // results
    private double delta;
    private double deltaForward;
    private double elasticity;
    private double gamma;
    private double theta;
    private double thetaPerDay;
    private double vega;
    private double rho;
    private double dividendRho;
    private double itmCashProbability;

    // arguments
    protected StochasticProcess stochasticProcess;


    //
    // public constructors
    //

    public OneAssetOption(final StochasticProcess process, final Payoff payoff, final Exercise exercise, final PricingEngine engine) {
    	super(payoff, exercise, engine);
        this.stochasticProcess = process;
        registerWith(this.stochasticProcess);
    }

    //
    // public methods
    //

    public double delta() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(delta) , "delta not provided"); // QA:[RG]::verified // TODO: message
        return delta;
    }

    public double deltaForward() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(deltaForward) , "forward delta not provided"); // QA:[RG]::verified // TODO: message
        return deltaForward;
    }

    public double elasticity() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(elasticity) , "elasticity not provided"); // QA:[RG]::verified // TODO: message
        return elasticity;
    }

    public double gamma() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(gamma) , "gamma not provided"); // QA:[RG]::verified // TODO: message
        return gamma;
    }

    public double theta() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(theta) , "theta not provided"); // QA:[RG]::verified // TODO: message
        return theta;
    }

    public double thetaPerDay() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(thetaPerDay) , "theta per-day not provided"); // QA:[RG]::verified // TODO: message
        return thetaPerDay;
    }

    public double vega() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(vega) , "vega not provided"); // QA:[RG]::verified // TODO: message
        return vega;
    }

    public double rho() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(rho) , "rho not provided"); // QA:[RG]::verified // TODO: message
        return rho;
    }

    public double dividendRho() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(dividendRho) , "dividend rho not provided"); // QA:[RG]::verified // TODO: message
        return dividendRho;
    }

    public double itmCashProbability() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(itmCashProbability) , "in-the-money cash probability not provided"); // QA:[RG]::verified // TODO: message
        return itmCashProbability;
    }

    /**
     * Currently, this method returns the Black-Scholes implied volatility.
     * It will give non-consistent results if the pricing was performed with any other methods (such as jump-diffusion models.)
     *
     * Options with a gamma that changes sign have values that are not monotonic in the volatility, e.g binary options.
     * In these cases the calculation can fail and the result (if any) is almost meaningless.
     * Another possible source of failure is to have a target value that is not attainable with any volatility, e.g.,
     * a target value lower than the intrinsic value in the case of American options.
     */
    public /* @Volatility */ double impliedVolatility(/*@Price*/ final double targetValue) /* @ReadOnly */ {
        return impliedVolatility(targetValue, 1.0e-4, 100, 1.0e-7, 4.0);
    }

    /**
     * Currently, this method returns the Black-Scholes implied volatility.
     * It will give non-consistent results if the pricing was performed with any other methods (such as jump-diffusion models.)
     *
     * Options with a gamma that changes sign have values that are not monotonic in the volatility, e.g binary options.
     * In these cases the calculation can fail and the result (if any) is almost meaningless.
     * Another possible source of failure is to have a target value that is not attainable with any volatility, e.g.,
     * a target value lower than the intrinsic value in the case of American options.
     */
    public /* @Volatility */ double impliedVolatility(/*@Price*/ final double targetValue, final double tolerance, final int maxEvalutions) /* @ReadOnly */ {
        return impliedVolatility(targetValue, tolerance, maxEvalutions, 1.0e-7, 4.0);
    }


    /**
     * @Note Currently, this method returns the Black-Scholes implied volatility.
     * It will give non-consistent results if the pricing was performed with any other methods (such as jump-diffusion models.)
     *
     * @Note Options with a gamma that changes sign have values that are not monotonic in the volatility, e.g binary options.
     * In these cases the calculation can fail and the result (if any) is almost meaningless.
     * Another possible source of failure is to have a target value that is not attainable with any volatility, e.g.,
     * a target value lower than the intrinsic value in the case of American options.
     */
    public final /* @Volatility */ double impliedVolatility(
            final /*@Price*/ double targetValue,
            final double accuracy,
            final int maxEvaluations,
            final /* @Volatility */ double minVol,
            final /* @Volatility */ double maxVol) /* @ReadOnly */ {
        calculate();
        QL.require(!isExpired() , "option expired"); // QA:[RG]::verified // TODO: message
        /* @Volatility */ final double guess = (minVol+maxVol)/2.0;
        final ImpliedVolHelper f = new ImpliedVolHelper(engine, targetValue);
        final AbstractSolver1D<Ops.DoubleOp> solver = new Brent();
        solver.setMaxEvaluations(maxEvaluations);
        /* @Volatility */ final double result = solver.solve(f, accuracy, guess, minVol, maxVol);
        return result;
    }


    //
    // overrides Instrument
    //

    @Override
    public boolean isExpired() /* @ReadOnly */ {
        final Date evaluationDate = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
        return exercise.lastDate().le( evaluationDate );
    }


    //
    // overrides NewInstrument
    //

    /**
     * {@inheritDoc}
     *
     * Passes the {@link StochasticProcess}, {@link Exercise}
     */
    @Override
    public void setupArguments(final Arguments args) /* @ReadOnly */ {
        QL.require(args instanceof OneAssetOptionArguments , WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
        final OneAssetOptionArguments oneAssetArguments = (OneAssetOptionArguments) args;
        final OptionArguments         optionArguments   = (OptionArguments) args;
        // set up stochastic process
        oneAssetArguments.stochasticProcess = stochasticProcess;
        // setup exercise dates
        optionArguments.exercise = exercise;
        // set up stopping times
        final int n = exercise.size();
        final List<Double> list = new ArrayDoubleList(n);
        for (int i=0; i<n; ++i)
            list.add(/*@Time*/ stochasticProcess.getTime(exercise.date(i)));
        optionArguments.stoppingTimes = list;
    }

    /**
     * {@inheritDoc}
     *
     * Obtains {@link Greeks} and {@link MoreGreeks} calculated by a {@link PricingEngine}
     *
     * @see Greeks
     * @see MoreGreeks
     * @see PricingEngine
     */
    @Override
    public void fetchResults(final Results results) /* @ReadOnly */ {
        QL.require(results instanceof MoreGreeks , WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
        super.fetchResults(results);
        final MoreGreeks moreGreeks;
        final Greeks     greeks;

        // bind a Results interface to specific classes
        moreGreeks = (MoreGreeks) results;
        greeks     = (Greeks) results;

        //
        // No check on Double.NaN values - just copy. this allows:
        // a) To decide in derived options what to do when null results are returned
        //    (throw numerical calculation?)
        // b) To implement slim engines which only calculate the value.
        //    Of course care must be taken not to call the greeks methods when using these.
        //
        delta          = greeks.delta;
        gamma          = greeks.gamma;
        theta          = greeks.theta;
        vega           = greeks.vega;
        rho            = greeks.rho;
        dividendRho    = greeks.dividendRho;

        //
        // No check on Double.NaN values - just copy. this allows:
        // a) To decide in derived options what to do when null results are returned
        //    (throw numerical calculation?)
        // b) To implement slim engines which only calculate the value.
        //    Of course care must be taken not to call the greeks methods when using these.
        //
        deltaForward       = moreGreeks.deltaForward;
        elasticity         = moreGreeks.elasticity;
        thetaPerDay        = moreGreeks.thetaPerDay;
        itmCashProbability = moreGreeks.itmCashProbability;
    }

    @Override
    protected void setupExpired() /* @ReadOnly */ {
        super.setupExpired();
        delta = deltaForward = elasticity = gamma = theta =
        thetaPerDay = vega = rho = dividendRho =
        itmCashProbability = 0.0;
    }


    //
    // private inner classes
    //

    /**
     * Helper class for implied volatility calculation
     */
    private static class ImpliedVolHelper implements Ops.DoubleOp {

        //
        // private final fields
        //

        private final OneAssetOptionResults impliedResults;
        private final PricingEngine impliedEngine;
        private final Handle<Quote> vol;
        private final double targetValue;


        //
        // public constructors
        //

        public ImpliedVolHelper(final PricingEngine engine, final double targetValue)  {
        	this.impliedEngine = engine;
        	this.targetValue = targetValue;

            // obtain arguments from pricing engine
            final Arguments tmpArgs = impliedEngine.getArguments();
            QL.require(tmpArgs instanceof OneAssetOptionArguments , WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
            final OneAssetOptionArguments oneAssetArguments = (OneAssetOptionArguments)tmpArgs;

        	// Make a new stochastic process in order not to modify the given one.
        	// stateVariable, dividendTS and riskFreeTS can be copied since
        	// they won't be modified.
        	// Here the requirement for a Black-Scholes process is hard-coded.
        	// Making it work for a generic process would need some reflection
        	// technique (which is possible, but requires some thought),
			// hence its postponement.

        	// obtain original process from arguments
            final GeneralizedBlackScholesProcess originalProcess = (GeneralizedBlackScholesProcess)oneAssetArguments.stochasticProcess;
        	QL.require(originalProcess!=null , "Black-Scholes process required"); // QA:[RG]::verified // TODO: message

        	// initialize arguments for calculation of implied volatility
        	this.vol = new Handle<Quote>(new SimpleQuote(0.0));
        	final Handle<? extends Quote> stateVariable = originalProcess.stateVariable();
        	final Handle<YieldTermStructure> dividendYield = originalProcess.dividendYield();
        	final Handle<YieldTermStructure> riskFreeRate = originalProcess.riskFreeRate();
        	final Handle<BlackVolTermStructure> blackVol = originalProcess.blackVolatility();

        	// calculate implied volatility
        	final Handle<BlackVolTermStructure> volatility = new Handle<BlackVolTermStructure>(
        													new BlackConstantVol(
        															blackVol.getLink().referenceDate(),
        															vol,
        															blackVol.getLink().dayCounter()));

        	// build a new stochastic process
        	final StochasticProcess process = new GeneralizedBlackScholesProcess(stateVariable, dividendYield, riskFreeRate, volatility);

        	// set up a new stochastic process back to the engine's arguments
        	oneAssetArguments.stochasticProcess = process;

        	// obtain results from pricing engine and keep for further use
        	QL.require(impliedEngine.getResults() instanceof OneAssetOptionResults , WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
        	impliedResults = (OneAssetOptionResults)impliedEngine.getResults();
        }


        //
        // implements Ops.DoubleOp
        //

        @Override
        public final double op(final /* @Volatility */ double x) /* @ReadOnly */ {
			final SimpleQuote quote = (SimpleQuote)vol.getLink();
			quote.setValue(x);
			this.impliedEngine.calculate();
			return impliedResults.value - targetValue;
		}

    }

}
