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
import org.jquantlib.pricingengines.GenericEngine;
import org.jquantlib.pricingengines.PricingEngine;
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
        ImpliedVolHelper f = new ImpliedVolHelper(engine_,targetValue);
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
    public void setupArguments(final PricingEngine.Arguments args) /* @ReadOnly */ {
    	if (! OneAssetOption.Arguments.class.isAssignableFrom(args.getClass())) throw new ClassCastException("wrong argument type");
    	
        int n = exercise_.size();

        Arguments arguments = (OneAssetOption.Arguments)args;
        arguments.stochasticProcess = stochasticProcess_;
        arguments.exercise = exercise_;
        arguments.stoppingTimes = new DoubleArrayList(n);
        for (int i=0; i<n; ++i) {
            arguments.stoppingTimes.add(/*@Time*/ stochasticProcess_.getTime(exercise_.getDate(i)));
        }
    }

    /**
     * When a derived result structure is defined for an instrument, this method should be 
     * overridden to read from it. This is mandatory in case a pricing engine is used.
     */
    public void fetchResults(final PricingEngine.Results results) /* @ReadOnly */ {
    	
    	if (! OneAssetOption.Results.class.isAssignableFrom(results.getClass())) throw new ClassCastException("wrong argument type");

    	super.fetchResults(results);
    	
        final Greeks greeks = ((OneAssetOption.Results)results).delegateGreeks;
        if (greeks ==null) throw new NullPointerException("no greeks returned from pricing engine");
        
        /*
		 * no check on null values - just copy. this allows: a) to decide in
		 * derived options what to do when null results are returned (throw?
		 * numerical calculation?) b) to implement slim engines which only
		 * calculate the value---of course care must be taken not to call
		 * the greeks methods when using these.
		 */
        delta_          = greeks.delta;
        gamma_          = greeks.gamma;
        theta_          = greeks.theta;
        vega_           = greeks.vega;
        rho_            = greeks.rho;
        dividendRho_    = greeks.dividendRho;

        final MoreGreeks moreGreeks = ((OneAssetOption.Results)results).delegateMoreGreeks;
        if (moreGreeks ==null) throw new NullPointerException("no more greeks returned from pricing engine");
        
        /*
		 * no check on null values - just copy. this allows: a) to decide in
		 * derived options what to do when null results are returned (throw?
		 * numerical calculation?) b) to implement slim engines which only
		 * calculate the value---of course care must be taken not to call
		 * the greeks methods when using these.
		 */
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
        private PricingEngine engine_;
        private double targetValue_;
        private SimpleQuote vol_;
        private final Instrument.InstrumentResults results_;
        
        
        public ImpliedVolHelper(final PricingEngine engine, double targetValue) {
        	this.engine_ = engine;
        	this.targetValue_ = targetValue;

        	if (! Arguments.class.isAssignableFrom(engine_.getArguments().getClass())) throw new ClassCastException("pricing engine does not supply needed arguments");
        	Arguments arguments_ = (Arguments)engine_.getArguments();
        	
        	// make a new stochastic process in order not to modify the
			// given one.
        	// stateVariable, dividendTS and riskFreeTS can be copied since
        	// they won't be modified.
        	// Here the requirement for a Black-Scholes process is
			// hard-coded.
        	// Making it work for a generic process would need some
			// reflection
        	// technique (which is possible, but requires some thought,
			// hence
        	// its postponement.)
        	GeneralizedBlackScholesProcess originalProcess = (GeneralizedBlackScholesProcess)arguments_.stochasticProcess;
        	// FIXME: if (originalProcess==null)
        	throw new NullPointerException("Black-Scholes process required");

        	Quote stateVariable = originalProcess.stateVariable();
        	YieldTermStructure dividendYield = originalProcess.dividendYield();
        	YieldTermStructure riskFreeRate = originalProcess.riskFreeRate();

        	final BlackVolTermStructure blackVol = originalProcess.blackVolatility();
        	vol_ = new SimpleQuote(0.0);
        	BlackVolTermStructure volatility = new BlackConstantVol(blackVol.getReferenceDate(), vol_, blackVol.getDayCounter());
        
        	StochasticProcess process = new GeneralizedBlackScholesProcess(stateVariable, dividendYield, riskFreeRate, volatility);
        	arguments_.stochasticProcess = process;

        	if (! Results.class.isAssignableFrom(engine_.getResults().getClass())) throw new ClassCastException("pricing engine does not supply needed results");
        	results_ = (Results)engine_.getResults();
        }

		private double get(/* @Volatility */ double x) /* @ReadOnly */ {
			vol_.setValue(x);
			innerEngine.calculate();
			return results_.value - targetValue_;
		}

    }
    
    
    //
    // Inner class Arguments
    //
    
    private Arguments innerArguments = new Arguments();
    
    /**
	 * Arguments for single-asset option calculation
	 * 
	 * @note This inner class must be kept <b>private</b> as its fields and ancertor's fields are exposed.
     * This programming style is not recommended and we should use getters/setters instead.
     * At the moment, we keep the original implementation.
     * 
     * @author Richard Gomes
	 */ 
    private class Arguments extends Option.Arguments {
    	
      /**
       * @note This field is exposed.
       * 
       * @author Richard Gomes
       */
      public StochasticProcess stochasticProcess;

      public void validate() /*@ReadOnly*/ {
          super.validate();
          // we assume the underlying value to be the first state variable
          if (stochasticProcess.initialValues()[0] <= 0.0) throw new IllegalArgumentException("negative or zero underlying given");
      }
    }


    //
    // Inner class Results
    //
    
    private Results innerResults = new Results();
    
    /**
	 * Results from single-asset option calculation
	 */
    private class Results extends Instrument.InstrumentResults {
    	private Greeks delegateGreeks = new Greeks();
    	private MoreGreeks delegateMoreGreeks = new MoreGreeks();
    	
    	public void reset() {
            super.reset();
            delegateGreeks.reset();
            delegateMoreGreeks.reset();
        }
    }
    
    

    //
    // Inner class Engine
    //
    
    private PricingEngine innerEngine = new OneAssetOptionEngine();

    
    protected class OneAssetOptionEngine extends GenericEngine<Arguments, Results> {
    	
    	private OneAssetOptionEngine() {
    		super(innerArguments, innerResults);
    	}

    }
        
}
