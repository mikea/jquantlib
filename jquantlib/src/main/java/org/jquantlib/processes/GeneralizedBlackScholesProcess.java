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

/*
 Copyright (C) 2003 Ferdinando Ametrano
 Copyright (C) 2001, 2002, 2003 Sadruddin Rejeb
 Copyright (C) 2004, 2005 StatPro Italia srl

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

package org.jquantlib.processes;

import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.LocalVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.volatilities.BlackVarianceCurve;
import org.jquantlib.termstructures.volatilities.LocalConstantVol;
import org.jquantlib.termstructures.volatilities.LocalVolCurve;
import org.jquantlib.termstructures.volatilities.LocalVolSurface;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;
import org.jscience.mathematics.number.Real;

public class GeneralizedBlackScholesProcess extends StochasticProcess1D {

    private Quote x0_;
    private YieldTermStructure riskFreeRate_;
    private YieldTermStructure dividendYield_;
    private BlackVolTermStructure blackVolatility_;
    private LocalVolTermStructure localVolatility_;
    private boolean updated_;

    
    public GeneralizedBlackScholesProcess(
	            final Quote x0,
	            final YieldTermStructure dividendTS,
	            final YieldTermStructure riskFreeTS,
	            final BlackVolTermStructure blackVolTS) {
    	super();
    	x0_ = x0;
    	riskFreeRate_ = riskFreeTS;
    	dividendYield_ = dividendTS;
    	blackVolatility_ = blackVolTS;
    	updated_ = false;
    	x0_.addObserver(this);
    	riskFreeRate_.addObserver(this);
    	dividendYield_.addObserver(this);
    	blackVolatility_.addObserver(this);
    }

            
    @Override
	public Real diffusion(final /*@Time*/ double t, final Real x) {
    	/*@Volatility*/ double vol = localVolatility().localVol(t, x, true);
    	return vol;
    }


	@Override
	public Real drift(final /*@Time*/ double t, final Real x) {
		double sigma = diffusion(t,x).doubleValue();
        // we could be more anticipatory if we know the right dt
        // for which the drift will be used
        /*@Time*/ double t1 = t + 0.0001;
        /*@Rate*/ double r = riskFreeRate_.getForwardRate(t, t1, Compounding.Continuous, Frequency.NoFrequency, true).getRate();
        double d = dividendYield_.getForwardRate(t, t1, Compounding.Continuous, Frequency.NoFrequency,true).getRate().doubleValue();
        return r-d-0.5*sigma*sigma;
    }

	@Override
	public Real x0() {
		return new Real( x0_.getValue() );
	}


// FIXME: code review

//	@Override
//	public double size() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	public Real diffusionDiscretization(/*@Time*/ double t0, Real x0, /*@Time*/ double dt) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Real driftDiscretization(/*@Time*/ double t0, Real x0, /*@Time*/ double dt) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Real varianceDiscretization(/*@Time*/ double t0, Real x0, /*@Time*/ double dt) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Matrix covarianceDiscretization(/*@Time*/ double t0, Vector x0, /*@Time*/ double dt) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Matrix diffusionDiscretization(/*@Time*/ double t0, Vector x0, /*@Time*/ double dt) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Vector driftDiscretization(/*@Time*/ double t0, Vector x0, /*@Time*/ double dt) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public void update(Observable o, Object arg) {
//		// TODO Auto-generated method stub
//
//	}


// =================================================
	


    public final Real apply(final Real x0, final Real dx) {
    	// result = x0 * e^dx
    	double result = x0.doubleValue() * Math.exp(dx.doubleValue());
    	return result;
    }

    public final /*@Time*/ double time(final Date d) {
        return riskFreeRate_.getDayCounter().getYearFraction(riskFreeRate_.getReferenceDate(), d);
    }

    public final void update() {
        updated_ = false;
        super.update();
    }

    public final Quote stateVariable() {
        return x0_;
    }

    public final YieldTermStructure dividendYield() {
        return dividendYield_;
    }

    public final YieldTermStructure riskFreeRate() {
        return riskFreeRate_;
    }

    public final BlackVolTermStructure blackVolatility() {
        return blackVolatility_;
    }

    public final LocalVolTermStructure localVolatility() {
        if (!updated_) {

            // constant Black vol?
        	if (BlackConstantVol.class.isAssignableFrom(blackVolatility().getClass())) {
        		BlackConstantVol constVol = (BlackConstantVol)blackVolatility();
        		localVolatility_ = new LocalConstantVol(
        				constVol.getReferenceDate(), 
        				constVol.blackVol(/*@Time*/ 0.0, new Real(x0_.getValue())),
        				constVol.getDayCounter());
                updated_ = true;
        		return localVolatility_;
        	}
        	
            // ok, so it's not constant. Maybe it's strike-independent?
        	if (BlackVarianceCurve.class.isAssignableFrom(blackVolatility().getClass())) {
        		BlackVarianceCurve volCurve = (BlackVarianceCurve)blackVolatility();
        		localVolatility_ = new LocalVolCurve(volCurve);
                updated_ = true;
        		return localVolatility_;
        	}
        	
            // ok, so it's strike-dependent. Never mind.
        	if (LocalVolSurface.class.isAssignableFrom(blackVolatility().getClass())) {
        		localVolatility_ = new LocalVolSurface(blackVolatility_, riskFreeRate_, dividendYield_, x0_);
                updated_ = true;
        		return localVolatility_;
        	}

        	// Note: The previous LocalVolSurface case was a catch-all condition.
        	// We decided to explicitly test the interface and throw an exception if we are not able
        	// to identify the correct interface to be used.
        	throw new UnsupportedOperationException(); // FIXME: message
        } else {
            return localVolatility_;
        }
    }











}
