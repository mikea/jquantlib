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

package org.jquantlib;

import java.sql.Time;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.util.Date;

public abstract class CapletVolatilityStructure extends TermStructure {

    protected abstract double volatilityImpl(double length,
            double strike);
    
    public CapletVolatilityStructure(DayCounter dc) {
        super(dc);
    }

    public CapletVolatilityStructure(Date referenceDate, Calendar cal, DayCounter dc) {
        super(referenceDate, cal, dc);
    }

    public CapletVolatilityStructure(int settlementDays, Calendar cal, DayCounter dc) {
        super(settlementDays, cal, dc);
    }


    public double volatility(Date start, double strike, boolean extrapolate) {
        double t = timeFromReference(start);
        checkRange(t, strike, extrapolate);
        return volatilityImpl(t, strike);
    }

    public double volatility(double t, double strike, boolean extrapolate) {
        checkRange(t, strike, extrapolate);
        return volatilityImpl(t, strike);
    }

    public double volatility(Period optionTenor, double strike, boolean extrapolate) {
        Date exerciseDate = calendar().advance(referenceDate(), optionTenor, BusinessDayConvention.FOLLOWING); // FIXME
        return volatility(exerciseDate, strike, extrapolate);
    }

    public double blackVariance(Date start, double strike, boolean extrapolate) {
        double t = timeFromReference(start);
        checkRange(t, strike, extrapolate);
        double vol = volatilityImpl(t, strike);
        return vol * vol * t;
    }
    
    public double blackVariance(Date start, double strike) {
        return blackVariance(start, strike, false);
    }

    public double blackVariance(double t, double strike, boolean extrapolate) {
        checkRange(t, strike, extrapolate);
        double vol = volatilityImpl(t, strike);
        return vol * vol * t;
    }
    
    public double blackVariance(double t, double strike) {
        return blackVariance(t, strike, false);
    }

    public double blackVariance(Period optionTenor, double strike, boolean extrapolate) {
        Date exerciseDate = calendar().advance(referenceDate(), optionTenor, BusinessDayConvention.FOLLOWING); // FIXME
        return blackVariance(exerciseDate, strike, extrapolate);

    }

    public void checkRange(double t, double k, boolean extrapolate) {
        super.checkRange(t, extrapolate);
        if (!extrapolate && !allowsExtrapolation() && !(k >= minStrike() && k <= maxStrike())) {
            throw new IllegalArgumentException("strike (" + k + ") is outside the curve domain [" + minStrike() + "," + maxStrike()
                    + "]");
        }
    }

    @Override
    public Date maxDate() {
        // TODO Auto-generated method stub
        return null;
    }

    
    public abstract double minStrike();

    public abstract double maxStrike();

    
}