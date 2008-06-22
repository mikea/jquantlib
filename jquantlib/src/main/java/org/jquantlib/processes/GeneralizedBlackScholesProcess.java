/*
 Copyright (C) 2008 Richard Gomes

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

import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
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
import org.jquantlib.util.Observable;

/**
 * Generalized Black-Scholes stochastic process
 * 
 * <p>This class describes the stochastic process governed by
 * <p>{@latex[
 * dS(t, S) = (r(t) - q(t) - \frac{\sigma(t, S)^2}{2}) dt + \sigma dW_t.
 * }
 * 
 * @author Richard Gomes
 */
public class GeneralizedBlackScholesProcess extends StochasticProcess1D {

    private Handle<? extends Quote> x0_;
    private Handle<YieldTermStructure> riskFreeRate_;
    private Handle<YieldTermStructure> dividendYield_;
    private Handle<BlackVolTermStructure> blackVolatility_;
    private RelinkableHandle<LocalVolTermStructure> localVolatility_;
    private boolean updated_;

    
	/**
	 * @param discretization is an Object that <b>must</b> implement {@link Discretization} <b>and</b> {@link Discretization1D}.
	 */
    public GeneralizedBlackScholesProcess(
	            final Handle<? extends Quote> x0,
	            final Handle<YieldTermStructure> dividendTS,
	            final Handle<YieldTermStructure> riskFreeTS,
	            final Handle<BlackVolTermStructure> blackVolTS) {
    	this(x0, dividendTS, riskFreeTS, blackVolTS, new EulerDiscretization());
    }
    
    
	/**
	 * @param discretization is an Object that <b>must</b> implement {@link Discretization} <b>and</b> {@link Discretization1D}.
	 */
    public GeneralizedBlackScholesProcess(
	            final Handle<? extends Quote> x0,
	            final Handle<YieldTermStructure> dividendTS,
	            final Handle<YieldTermStructure> riskFreeTS,
	            final Handle<BlackVolTermStructure> blackVolTS,
	            final LinearDiscretization discretization) {
    	super(discretization);
    	x0_ = x0;
    	riskFreeRate_ = riskFreeTS;
    	dividendYield_ = dividendTS;
    	blackVolatility_ = blackVolTS;
    	localVolatility_ = new RelinkableHandle<LocalVolTermStructure>();
    	updated_ = false;
    	x0_.addObserver(this);
    	riskFreeRate_.addObserver(this);
    	dividendYield_.addObserver(this);
    	blackVolatility_.addObserver(this);
    }

	@Override
	public /*@Price*/ double x0() {
		return x0_.getLink().doubleValue();
	}

	@Override
	public /*@Drift*/ double drift(final /*@Time*/ double t, final /*@Price*/ double x) {
		/*@Diffusion*/ double sigma = diffusion(t,x);
        // we could be more anticipatory if we know the right dt
        // for which the drift will be used
        /*@Time*/ double t1 = t + 0.0001;
        YieldTermStructure yts = riskFreeRate_.getLink();
        /*@Rate*/ double r = yts.getForwardRate(t, t1, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY, true).doubleValue();
        double d = yts.getForwardRate(t, t1, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY,true).doubleValue();
        return r-d-0.5*sigma*sigma;
    }

	@Override
	public /*@Diffusion*/ double diffusion(final /*@Time*/ double t, final /*@Price*/ double x) {
    	/*@Volatility*/ double vol = localVolatility().getLink().localVol(t, x, true);
    	return vol;
    }

    @Override
	public final /*@Price*/ double apply(final /*@Price*/ double x0, final /*@Time*/ double dx) {
    	// result = x0 * e^dx
    	double result = x0 * Math.exp(dx);
    	return result;
    }

    @Override
    public final /*@Time*/ double getTime(final Date d) {
    	YieldTermStructure yts = riskFreeRate_.getLink();
        return yts.getDayCounter().getYearFraction(yts.getReferenceDate(), d);
    }

    // FIXME: code review
    public final void update(Observable o, Object arg) {
        updated_ = false;
        super.update(o, arg);
    }

    public final Handle<? extends Quote> stateVariable() {
        return x0_;
    }

    public final Handle<YieldTermStructure> dividendYield() {
        return dividendYield_;
    }

    public final Handle<YieldTermStructure> riskFreeRate() {
        return riskFreeRate_;
    }

    public final Handle<BlackVolTermStructure> blackVolatility() {
        return blackVolatility_;
    }

    public final Handle<LocalVolTermStructure> localVolatility() {
        if (!updated_) {
        	Class<? extends BlackVolTermStructure> klass = blackVolatility_.getLink().getClass();

            // constant Black vol?
        	if (BlackConstantVol.class.isAssignableFrom(klass)) {
    			// ok, the local vol is constant too.
        		BlackConstantVol constVol = (BlackConstantVol)blackVolatility_.getLink();
        		localVolatility_.setLink(
        				new LocalConstantVol(
        						constVol.getReferenceDate(), 
        						constVol.blackVol(/*@Time*/ 0.0, /*@Price*/ x0_.getLink().doubleValue()),
        						constVol.getDayCounter()));
                updated_ = true;
        		return localVolatility_;
        	}
        	
            // ok, so it's not constant. Maybe it's strike-independent?
        	if (BlackVarianceCurve.class.isAssignableFrom(klass)) {
        		Handle<BlackVarianceCurve> volCurve = new Handle<BlackVarianceCurve>((BlackVarianceCurve)blackVolatility().getLink());
        		localVolatility_.setLink(new LocalVolCurve(volCurve));
                updated_ = true;
        		return localVolatility_;
        	}
        	
            // ok, so it's strike-dependent. Never mind.
        	if (LocalVolSurface.class.isAssignableFrom(klass)) {
        		localVolatility_.setLink(new LocalVolSurface(blackVolatility_, riskFreeRate_, dividendYield_, x0_));
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
