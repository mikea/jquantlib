/*
 Copyright (C) 2008 Anand Mani
 
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

import org.jquantlib.lang.reflect.TypeToken;
import org.jquantlib.math.IntervalPrice;
import org.jquantlib.util.Date;
import org.jquantlib.util.TimeSeries;

/**
 * This template factors out common functionality found in classes which rely on the difference between the previous day's close
 * price and today's open price.
 * 
 * @author Anand Mani
 * @author Richard Gomes
 */
public class GarmanKlassOpenClose<T extends GarmanKlassAbstract> implements LocalVolatilityEstimator<IntervalPrice> {

    //
    // private fields
    //
    
    private double f;
	private double a;
	private T delegate;

	//
	// public constructors
	//
	
	@SuppressWarnings("unchecked")
	public GarmanKlassOpenClose(final double y, final double marketOpenFraction, final double a) {
		this.delegate = null;
        try {
            delegate = (T) TypeToken.getClazz(this.getClass()).getConstructor(double.class).newInstance(y);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		this.f = marketOpenFraction;
		this.a = a;
	}

	//
	// implements LocalVolatilityEstimator
	//
	
	@Override
	public TimeSeries<Double> calculate(final TimeSeries<IntervalPrice> quoteSeries) {
		final Date[] dates = quoteSeries.dates();
		final IntervalPrice[] values = quoteSeries.values().toArray(new IntervalPrice[0]);
		final TimeSeries<Double> retval = new TimeSeries<Double>() { /* anonymous */ };
		IntervalPrice prev = null;
		IntervalPrice cur = null;
		for (int i = 1; i < values.length; i++) {
			cur = values[i];
			prev = values[i - 1];
			double c0 = Math.log(prev.close());
			double o1 = Math.log(cur.open());
			double sigma2 = this.a * (o1 - c0) * (o1 - c0) / this.f + (1 - this.a) * delegate.calculatePoint(cur) / (1 - this.f);
			double s = Math.sqrt(sigma2 / delegate.getYearFraction());
			retval.add(dates[i], s);
		}
		return retval;
	}

}
