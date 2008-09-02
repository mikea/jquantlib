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

package org.jquantlib.model.volatility.garmanklass;

import org.jquantlib.math.IntervalPrice;
import org.jquantlib.util.Date;
import org.jquantlib.util.TimeSeries;

public class GarmanKlassOpenClose<T extends GarmanKlassAbstract> extends GarmanKlassAbstract {

	private/* @Real */Double f;
	private/* @Real */Double a;
	private T t;

	public GarmanKlassOpenClose(T t, /* @Real */Double marketOpenFraction,
	/* @Real */Double a) {
		super(t.getYearFraction());
		this.t = t;
		this.f = marketOpenFraction;
		this.a = a;
	}

	public TimeSeries</* @Volatility*/Double> calculate(TimeSeries<IntervalPrice> quoteSeries /* @ReadOnly*/) {
		final Date[] dates = quoteSeries.dates();
		final IntervalPrice[] values = quoteSeries.values();
		TimeSeries</*@Volatility*/Double> retval = new TimeSeries</*@Volatility*/Double>();
		IntervalPrice prev = null;
		IntervalPrice cur = null;
		for (int i = 1; i < values.length; i++) {
			cur = values[i];
			prev = values[i - 1];
			double c0 = Math.log(prev.getClose());
			double o1 = Math.log(cur.getOpen());
			double sigma2 = this.a * (o1 - c0) * (o1 - c0) / this.f + (1 - this.a) * calculatePoint(cur) / (1 - this.f);
			double s = Math.sqrt(sigma2 / getYearFraction());
			retval.add(dates[i], s);
		}
		return retval;
	}

	@Override
	protected Double calculatePoint(IntervalPrice p) {
		return t.calculatePoint(p);
	}
}
