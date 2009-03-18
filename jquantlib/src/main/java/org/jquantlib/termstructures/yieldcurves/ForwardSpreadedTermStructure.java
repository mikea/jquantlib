/*
 Copyright (C) 2009 Ueli Hofstetter

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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;

public class ForwardSpreadedTermStructure extends ForwardRateStructure {
	
	private Handle<YieldTermStructure> originalCurve_;
	private Handle<Quote> spread_;
	
	public ForwardSpreadedTermStructure(final Handle<YieldTermStructure> h,
			final Handle<Quote> spread){
		this.originalCurve_ = h;
		this.spread_ = spread;
		
		//registerWith(originalCurve_);
        //registerWith(spread_);
	}
	
	@Override
	public DayCounter dayCounter(){
		return originalCurve_.getLink().dayCounter();
	}
	
	@Override
	public Calendar calendar(){
		return originalCurve_.getLink().calendar();
	}
	
	@Override
	public Date referenceDate(){
		return originalCurve_.getLink().referenceDate();
	}
	
	@Override
	public Date maxDate(){
		return originalCurve_.getLink().maxDate();
	}
	
	@Override
	public double maxTime(){
		return originalCurve_.getLink().maxTime();
	}

	@Override
	protected double forwardImpl(double t) {
		return originalCurve_.getLink().forwardRate(t, t, 
				Compounding.CONTINUOUS, Frequency.NO_FREQUENCY, true).rate()
				+ spread_.getLink().evaluate();
	}
	
	@Override 
	public double zeroYieldImpl(final double t){
		return originalCurve_.getLink().zeroRate( t, 
				Compounding.CONTINUOUS, Frequency.NO_FREQUENCY, true).rate()
				+ spread_.getLink().evaluate();
		
	}
}
