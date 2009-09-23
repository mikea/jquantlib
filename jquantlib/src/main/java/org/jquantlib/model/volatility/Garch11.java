/*
 Copyright (C) 2008 Rajiv Chauhan
 
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

import org.jquantlib.time.Date;
import org.jquantlib.time.TimeSeries;

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
	
	public Garch11(final TimeSeries<Double> qs) {
        calibrate(qs);
    }
	
	@Override
	public TimeSeries<Double> calculate(final TimeSeries<Double> vs) {
		return calculate(vs, alpha, beta, gamma* v);
	}

	@Override
	public void calibrate(final TimeSeries<Double> timeSeries) {}

	protected double costFunction (final TimeSeries<Double> vs, double alpha, double beta, double omega) {
		double retValue = 0.0;
		final TimeSeries<Double> test = calculate(vs, alpha, beta, omega);
		final /* @Volatility */ double[] testValues = test.values();
		final /* @Volatility */ double[] quoteValues = vs.values();
		//assert (testValues.size() == quoteValues.size(), "quote and test values do not match");
		double v = 0;
		double u2 = 0 ;
		for (int i = 0; i < testValues.length; i++) {
			v  = testValues[i] * testValues[i];
			u2 = quoteValues[i] * quoteValues[i];
			retValue += 2.0 * Math.log(v) + u2/(v*v) ;
		}
		return retValue ;
	}
	
	private TimeSeries<Double> calculate(final TimeSeries<Double> vs, double alpha, double beta, double omega) {
        final Date[] dates = vs.dates();
        final /* @Volatility */ double[] values = vs.values();
		final TimeSeries<Double> retValue = new TimeSeries<Double>() { /* anonymous */ };
        double zerothDayValue = values[0];
		retValue.add (dates[0], zerothDayValue) ;
		
		double u = 0;
        double sigma2 = zerothDayValue * zerothDayValue ;
        for (int i = 1; i < dates.length; i++) {
        	u = values[i];
        	sigma2 = (omega * u * u) + (beta * sigma2) ;
        	retValue.add(dates[i], Math.sqrt(sigma2)) ;
        }
		return retValue ;
	}
}
