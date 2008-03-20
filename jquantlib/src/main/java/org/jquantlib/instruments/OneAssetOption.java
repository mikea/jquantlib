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

import org.jquantlib.Settings;
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
    protected double delta_;
    protected double deltaForward_;
    protected double elasticity_;
    protected double gamma_;
    protected double theta_;
    protected double thetaPerDay_;
    protected double vega_;
    protected double rho_;
    protected double dividendRho_;
    protected double itmCashProbability_;
    
    // arguments
    protected StochasticProcess stochasticProcess_;


    private double targetValue_;
    private SimpleQuote vol_;

    // FIXME: move from 
    
    
    public OneAssetOption(final StochasticProcess process, final Payoff payoff, final Exercise exercise, final PricingEngine engine) {
    	super(payoff, exercise, engine);
        this.stochasticProcess_ = process;
        this.stochasticProcess_.addObserver(this);
    }

    public boolean isExpired() /* @ReadOnly */ {
        return exercise_.getLastDate().le( Settings.getInstance().getEvaluationDate() );
    }

    public double delta() /* @ReadOnly */ {
        calculate();
        if (delta_ == Double.NaN) throw new IllegalArgumentException("delta not provided");
        return delta_;
    }

    public double deltaForward() /* @ReadOnly */ {
        calculate();
        if (deltaForward_ == Double.NaN) throw new IllegalArgumentException("forward delta not provided");
        return deltaForward_;
    }

    public double elasticity() /* @ReadOnly */ {
        calculate();
        if (elasticity_ == Double.NaN) throw new IllegalArgumentException("elasticity not provided");
        return elasticity_;
    }

    public double gamma() /* @ReadOnly */ {
        calculate();
        if (gamma_ == Double.NaN) throw new IllegalArgumentException("gamma not provided");
        return gamma_;
    }

    public double theta() /* @ReadOnly */ {
        calculate();
        if (theta_ == Double.NaN) throw new IllegalArgumentException("theta not provided");
        return theta_;
    }

    public double thetaPerDay() /* @ReadOnly */ {
        calculate();
        if (thetaPerDay_ == Double.NaN) throw new IllegalArgumentException("theta per-day not provided");
        return thetaPerDay_;
    }

    public double vega() /* @ReadOnly */ {
        calculate();
        if (vega_ == Double.NaN) throw new IllegalArgumentException("vega not provided");
        return vega_;
    }

    public double rho() /* @ReadOnly */ {
        calculate();
        if (rho_ == Double.NaN) throw new IllegalArgumentException("rho not provided");
        return rho_;
    }

    public double dividendRho() /* @ReadOnly */ {
        calculate();
        if (dividendRho_ == Double.NaN) throw new IllegalArgumentException("dividend rho not provided");
        return dividendRho_;
    }

    public double itmCashProbability() /* @ReadOnly */ {
        calculate();
        if (itmCashProbability_ == Double.NaN) throw new IllegalArgumentException("in-the-money cash probability not provided");
        return itmCashProbability_;
    }

    
    
    
//    ======================================================
//
//    H E R E  !!!!!
//    
//    Verify how impliedVolatility is used !!!!!!
//
//    ======================================================
    
    
    
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
    			double targetValue, 
    			double accuracy, 
    			int maxEvaluations, 
                /* @Volatility */ double minVol, 
                /* @Volatility */ double maxVol) /* @ReadOnly */ {
        calculate();
        if (isExpired()) throw new IllegalArgumentException("option expired");

        /* @Volatility */ double guess = (minVol+maxVol)/2.0;
        ImpliedVolHelper f = new ImpliedVolHelper(engine_, targetValue);
        Brent solver = new Brent();
        solver.setMaxEvaluations(maxEvaluations);
        /* @Volatility */ double result = solver.solve(f, accuracy, guess, minVol, maxVol);
        return result;
    }

    protected void setupExpired() /* @ReadOnly */ {
        super.setupExpired();
        delta_ = deltaForward_ = elasticity_ = gamma_ = theta_ =
        thetaPerDay_ = vega_ = rho_ = dividendRho_ =
        itmCashProbability_ = 0.0;
    }

    /**
     * @note This method accesses directly fields from {@link Arguments}.
     * These fields are exposed by {@link Option.Arguments} which is the base class of {@link Arguments}.
     * This programming style is not recommended and we should use getters/setters instead.
     * At the moment, we keep the original implementation.
     * 
     * @author Richard Gomes
     */
    public void setupArguments(final Arguments arguments) /* @ReadOnly */ {
    	
    	// FIXME: code review
    	// super.setupArguments(args);

    	if (! OneAssetOptionArguments.class.isAssignableFrom(arguments.getClass())) throw new ClassCastException(arguments.toString());
    	
        OneAssetOptionArguments oneAssetArguments = (OneAssetOptionArguments)arguments;
        OptionArguments optionArguments = oneAssetArguments.getOptionArguments();

        // set up stochastic process
        oneAssetArguments.stochasticProcess = stochasticProcess_;
        // setup exercise dates
        optionArguments.exercise = exercise_;
        // set up stopping times
        int n = exercise_.size();
        DoubleArrayList arr = new DoubleArrayList(n);
        for (int i=0; i<n; ++i) {
        	arr.add(/*@Time*/ stochasticProcess_.getTime(exercise_.getDate(i)));
        }
        optionArguments.stoppingTimes = arr;
    }

    /**
     * When a derived result structure is defined for an instrument, this method should be 
     * overridden to read from it. This is mandatory in case a pricing engine is used.
     */
    public void fetchResults(final Results results) /* @ReadOnly */ {
    	
    	// bind a Results interface to specific Classes
    	final MoreGreeks moreGreeks = (MoreGreeks) results.findClass(MoreGreeks.class);
    	final Greeks     greeks     = (Greeks) moreGreeks.findClass(Greeks.class);
        
        //
		// No check on Double.NaN values - just copy. this allows:
		// a) To decide in derived options what to do when null results are returned
		//    (throw numerical calculation?)
		// b) To implement slim engines which only calculate the value.
		//    Of course care must be taken not to call the greeks methods when using these.
		//
        delta_          = greeks.delta;
        gamma_          = greeks.gamma;
        theta_          = greeks.theta;
        vega_           = greeks.vega;
        rho_            = greeks.rho;
        dividendRho_    = greeks.dividendRho;

        //
		// No check on Double.NaN values - just copy. this allows:
		// a) To decide in derived options what to do when null results are returned
		//    (throw numerical calculation?)
		// b) To implement slim engines which only calculate the value.
		//    Of course care must be taken not to call the greeks methods when using these.
		//
        deltaForward_       = moreGreeks.deltaForward;
        elasticity_         = moreGreeks.elasticity;
        thetaPerDay_        = moreGreeks.thetaPerDay;
        itmCashProbability_ = moreGreeks.itmCashProbability;
    }

    
    
    //
    // Inner class ImpliedVolHelper
    //
    
    /**
     * Helper class for implied volatility calculation
     */
    private class ImpliedVolHelper {
        private PricingEngine impliedEngine;
        private final OneAssetOptionResults impliedResults;
        private double targetValue_;
        private SimpleQuote vol_;
        
        
        public ImpliedVolHelper(final PricingEngine engine, double targetValue) {
        	this.impliedEngine = engine;
        	this.targetValue_ = targetValue;

            // obtain arguments from pricing engine
            Arguments tmpArgs = impliedEngine.getArguments();
            if (! OneAssetOptionArguments.class.isAssignableFrom(tmpArgs.getClass())) throw new ClassCastException(tmpArgs.getClass().getName());
            OneAssetOptionArguments oneAssetArguments = (OneAssetOptionArguments)tmpArgs;
            
        	// Make a new stochastic process in order not to modify the given one.
        	// stateVariable, dividendTS and riskFreeTS can be copied since
        	// they won't be modified.
        	// Here the requirement for a Black-Scholes process is
			// hard-coded.
        	// Making it work for a generic process would need some
			// reflection
        	// technique (which is possible, but requires some thought,
			// hence
        	// its postponement.
        	
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
        
        	
        	/*
    		public GeneralizedBlackScholesProcess(
	            final Quote x0,
	            final YieldTermStructure dividendTS,
	            final YieldTermStructure riskFreeTS,
	            final BlackVolTermStructure blackVolTS,
	            final T discretization) {
        	 */
        	
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
