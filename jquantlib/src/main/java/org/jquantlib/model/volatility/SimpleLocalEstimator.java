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
 * Simple Local Estimator volatility model
 * <p>
 * Volatilities are assumed to be expressed on an annual basis.
 * 
 * @author Rajiv Chauhan
 * @author Anand Mani
 */
public class SimpleLocalEstimator {

	private final /* @Real */ double yearFraction ;
    
    public SimpleLocalEstimator(final /*@Real*/ double y) {
        this.yearFraction = y;
    }
    
    //FIXME: PERFORMANCE:: We should use (maybe!) a specialized TimeSeries backed by a double[] instead of a Double[]
    public TimeSeries</*@Volatility*/ Double> calculate(final TimeSeries</*@Real*/ Double> quoteSeries) {
        final List<Date> dates = quoteSeries.dates();
        final List</*@Real*/ Double> values = quoteSeries.values();
    	TimeSeries</*@Volatility*/ Double> retval = new TimeSeries</*@Volatility*/ Double>();
    	Double prev = null ;
    	Double cur  = null;
    	for (int i = 1; i < values.size(); i++) {
    		cur = values.get(i) ;
    		prev = values.get(i - 1);
    		double s = Math.abs(Math.log(cur/prev))/Math.sqrt(yearFraction) ;
    		retval.add(dates.get(i), s);
    	}
        return retval;
    }

}
