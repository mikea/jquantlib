/*
 Copyright (C) 2006 Joseph Wang

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

package org.jquantlib.model.volatility;

import java.util.List;

import org.jquantlib.util.Date;
import org.jquantlib.util.TimeSeries;

/**
 * GARCH Volatility Model
 * <p>
 * Volatilities are assumed to be expressed on an annual basis.
 * 
 * @author Rajiv Chauhan
 */
//TODO : Test cases
public class Garch11 implements VolatilityCompositor{

	private /* @Real */ double alpha ;
	private /* @Real */ double beta ;
	private /* @Real */ double gamma ;
	private /* @Real */ double v ;
	
	public Garch11 (double alpha, double beta, double v) {
		this.alpha = alpha ;
		this.beta = beta ;
		this.v = v ;
		this.gamma = (1 - alpha - beta) ;
	}
	
	public Garch11(final TimeSeries< /* @Volatility */ Double> qs) {
        calibrate(qs);
    }
	
	@Override
	public TimeSeries<Double> calculate(final TimeSeries<Double> vs) {
		return calculate(vs, alpha, beta, gamma* v);
	}

	@Override
	public void calibrate(final TimeSeries< /* @Volatility */ Double> timeSeries) {}

	protected double costFunction (final TimeSeries< /* @Volatility */ Double> vs, double alpha, double beta, double omega) {
		double retValue = 0.0;
		TimeSeries< /* @Volatility */ Double> test = calculate(vs, alpha, beta, omega);
		final List<Double> testValues = test.values() ;
		final List<Double> quoteValues = vs.values();
		//assert (testValues.size() == quoteValues.size(), "quote and test values do not match");
		double v = 0;
		double u2 = 0 ;
		for (int i = 0; i < testValues.size(); i++) {
			v  = testValues.get(i) * testValues.get(i);
			u2 = quoteValues.get(i) * quoteValues.get(i);
			retValue += 2.0 * Math.log(v) + u2/(v*v) ;
		}
		return retValue ;
	}
	
	private TimeSeries<Double> calculate(final TimeSeries< /* @Volatility */ Double> vs, double alpha, double beta, double omega) {
        final List<Date> dates = vs.dates();
        final List</*@Volatility*/ Double> values = vs.values();
		TimeSeries< /* @Volatility */ Double> retValue = new TimeSeries< /* @Volatility */ Double>() ;
        double zerothDayValue = values.get(0);
		retValue.add (dates.get(0), zerothDayValue) ;
		
		double u = 0;
        double sigma2 = zerothDayValue * zerothDayValue ;
        for (int i = 1; i < dates.size(); i++) {
        	u = values.get (i) ;
        	sigma2 = (omega * u * u) + (beta * sigma2) ;
        	retValue.add(dates.get(i), Math.sqrt(sigma2)) ;
        }
		return retValue ;
	}
}
