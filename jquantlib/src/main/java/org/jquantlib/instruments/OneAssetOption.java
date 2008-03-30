/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

package org.jquantlib.instruments;

import org.jquantlib.exercise.Exercise;
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
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;

import cern.colt.list.DoubleArrayList;

public class OneAssetOption extends Option {

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
    private StochasticProcess stochasticProcess;
    
    
    // FIXME: code review
    // Verify where this variable is initialized and where impliedVolatility(double) is called.
    // I suppose impliedVolatility(double) should be called by the constructor in order to determine the
    // implied volatility, which is based on the current supply/demand measured from the market.
    private double targetValue;

    
    public OneAssetOption(final StochasticProcess process, final Payoff payoff, final Exercise exercise, final PricingEngine engine) {
    	super(payoff, exercise, engine);
        this.stochasticProcess = process;
        this.stochasticProcess.addObserver(this);
    }

    public double delta() /* @ReadOnly */ {
        calculate();
        if (delta == Double.NaN) throw new IllegalArgumentException("delta not provided");
        return delta;
    }

    public double deltaForward() /* @ReadOnly */ {
        calculate();
        if (deltaForward == Double.NaN) throw new IllegalArgumentException("forward delta not provided");
        return deltaForward;
    }

    public double elasticity() /* @ReadOnly */ {
        calculate();
        if (elasticity == Double.NaN) throw new IllegalArgumentException("elasticity not provided");
        return elasticity;
    }

    public double gamma() /* @ReadOnly */ {
        calculate();
        if (gamma == Double.NaN) throw new IllegalArgumentException("gamma not provided");
        return gamma;
    }

    public double theta() /* @ReadOnly */ {
        calculate();
        if (theta == Double.NaN) throw new IllegalArgumentException("theta not provided");
        return theta;
    }

    public double thetaPerDay() /* @ReadOnly */ {
        calculate();
        if (thetaPerDay == Double.NaN) throw new IllegalArgumentException("theta per-day not provided");
        return thetaPerDay;
    }

    public double vega() /* @ReadOnly */ {
        calculate();
        if (vega == Double.NaN) throw new IllegalArgumentException("vega not provided");
        return vega;
    }

    public double rho() /* @ReadOnly */ {
        calculate();
        if (rho == Double.NaN) throw new IllegalArgumentException("rho not provided");
        return rho;
    }

    public double dividendRho() /* @ReadOnly */ {
        calculate();
        if (dividendRho == Double.NaN) throw new IllegalArgumentException("dividend rho not provided");
        return dividendRho;
    }

    public double itmCashProbability() /* @ReadOnly */ {
        calculate();
        if (itmCashProbability == Double.NaN) throw new IllegalArgumentException("in-the-money cash probability not provided");
        return itmCashProbability;
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
    private /* @Volatility */ double impliedVolatility(
    							/*@Price*/ double targetValue, double accuracy, int maxEvaluations, 
    							/* @Volatility */ double minVol, /* @Volatility */ double maxVol) /* @ReadOnly */ {
        calculate();
        if (isExpired()) throw new IllegalArgumentException("option expired");

        /* @Volatility */ double guess = (minVol+maxVol)/2.0;
        ImpliedVolatilityHelper f = new ImpliedVolatilityHelper(engine, targetValue);
        Brent solver = new Brent();
        solver.setMaxEvaluations(maxEvaluations);
        /* @Volatility */ double result = solver.solve(f, accuracy, guess, minVol, maxVol);
        return result;
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
    private /* @Volatility */ double impliedVolatility(/*@Price*/ double targetValue) /* @ReadOnly */ {
    	return impliedVolatility(targetValue, 1.0e-4, 100, 1.0e-7, 4.0);
 
    }
    
    
    
    
    
    protected void setupExpired() /* @ReadOnly */ {
        super.setupExpired();
        delta = deltaForward = elasticity = gamma = theta =
        thetaPerDay = vega = rho = dividendRho =
        itmCashProbability = 0.0;
    }

    @Override
    public void setupArguments(final Arguments arguments) /* @ReadOnly */ {
    	
    	if (! OneAssetOptionArguments.class.isAssignableFrom(arguments.getClass())) throw new ClassCastException(arguments.toString());
    	
        OneAssetOptionArguments oneAssetArguments = (OneAssetOptionArguments) arguments;
        OptionArguments         optionArguments   = (OptionArguments) arguments;

        // set up stochastic process
        oneAssetArguments.stochasticProcess = stochasticProcess;
        // setup exercise dates
        optionArguments.exercise = exercise;
        // set up stopping times
        int n = exercise.size();
        DoubleArrayList arr = new DoubleArrayList(n);
        for (int i=0; i<n; ++i) {
        	arr.add(/*@Time*/ stochasticProcess.getTime(exercise.getDate(i)));
        }
        optionArguments.stoppingTimes = arr;
    }

    /**
     * When a derived result structure is defined for an instrument, this method should be 
     * overridden to read from it. This is mandatory in case a pricing engine is used.
     */
    public void fetchResults(final Results results) /* @ReadOnly */ {
    	
    	if (! MoreGreeks.class.isAssignableFrom(results.getClass())) throw new ClassCastException(results.toString());
    	
    	// bind a Results interface to specific Classes
    	final MoreGreeks moreGreeks = (MoreGreeks) results;
    	final Greeks     greeks     = (Greeks) results;
        
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

    
    
    //
    // Inner class ImpliedVolHelper
    //
    
    /**
     * Helper class for implied volatility calculation
     */
    private class ImpliedVolatilityHelper {
    	
        private PricingEngine impliedEngine;
        private final OneAssetOptionResults impliedResults;
        private double targetValue_;
        private SimpleQuote vol_;
        
        
        public ImpliedVolatilityHelper(final PricingEngine engine, double targetValue) {
        	this.impliedEngine = engine;
        	this.targetValue_ = targetValue;

            // obtain arguments from pricing engine
            Arguments tmpArgs = impliedEngine.getArguments();
            if (! OneAssetOptionArguments.class.isAssignableFrom(tmpArgs.getClass())) throw new ClassCastException(tmpArgs.getClass().getName());
            OneAssetOptionArguments oneAssetArguments = (OneAssetOptionArguments)tmpArgs;
            
        	// Make a new stochastic process in order not to modify the given one.
        	// stateVariable, dividendTS and riskFreeTS can be copied since
        	// they won't be modified.
        	// Here the requirement for a Black-Scholes process is hard-coded.
        	// Making it work for a generic process would need some reflection
        	// technique (which is possible, but requires some thought),
			// hence its postponement.
        	
        	// obtain original process from arguments
            GeneralizedBlackScholesProcess originalProcess = (GeneralizedBlackScholesProcess)oneAssetArguments.stochasticProcess;
        	if (originalProcess==null) throw new NullPointerException("Black-Scholes process required");

        	// initialize arguments for calculation of implied volatility
        	vol_ = new SimpleQuote(0.0);
        	Quote stateVariable = originalProcess.stateVariable();
        	YieldTermStructure dividendYield = originalProcess.dividendYield();
        	YieldTermStructure riskFreeRate = originalProcess.riskFreeRate();
        	BlackVolTermStructure blackVol = originalProcess.blackVolatility();

        	// calculate implied volatility
        	BlackVolTermStructure volatility = new BlackConstantVol(blackVol.getReferenceDate(), vol_, blackVol.getDayCounter());
        
        	// build a new stochastic process
        	StochasticProcess process = new GeneralizedBlackScholesProcess(stateVariable, dividendYield, riskFreeRate, volatility);

        	// set up a new stochastic process back to the engine's arguments
        	oneAssetArguments.stochasticProcess = process;

        	// obtain results from pricing engine and keep for further use
        	if (! OneAssetOptionResults.class.isAssignableFrom(impliedEngine.getResults().getClass())) throw new ClassCastException(impliedEngine.getClass().getName());
        	impliedResults = (OneAssetOptionResults)impliedEngine.getResults();
        }

		private double get(/* @Volatility */ double x) /* @ReadOnly */ {
			vol_.setValue(x);
			this.impliedEngine.calculate();
			return impliedResults.value - targetValue_;
		}

    }
    
    
}
