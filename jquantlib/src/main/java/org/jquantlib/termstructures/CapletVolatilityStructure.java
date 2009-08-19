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

package org.jquantlib.termstructures;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.util.Date;

public abstract class CapletVolatilityStructure extends AbstractTermStructure {

    protected abstract double volatilityImpl(double length,
            double strike);

    public CapletVolatilityStructure(final DayCounter dc) {
        super(dc);
    }

    public CapletVolatilityStructure(final Date referenceDate, final Calendar cal, final DayCounter dc) {
        super(referenceDate, cal, dc);
    }

    public CapletVolatilityStructure(final int settlementDays, final Calendar cal, final DayCounter dc) {
        super(settlementDays, cal, dc);
    }


    public double volatility(final Date start, final double strike, final boolean extrapolate) {
        final double t = timeFromReference(start);
        checkRange(t, strike, extrapolate);
        return volatilityImpl(t, strike);
    }

    public double volatility(final double t, final double strike, final boolean extrapolate) {
        checkRange(t, strike, extrapolate);
        return volatilityImpl(t, strike);
    }

    public double volatility(final Period optionTenor, final double strike, final boolean extrapolate) {
        final Date exerciseDate = calendar().advance(referenceDate(), optionTenor, BusinessDayConvention.FOLLOWING); // FIXME
        return volatility(exerciseDate, strike, extrapolate);
    }

    public double blackVariance(final Date start, final double strike, final boolean extrapolate) {
        final double t = timeFromReference(start);
        checkRange(t, strike, extrapolate);
        final double vol = volatilityImpl(t, strike);
        return vol * vol * t;
    }

    public double blackVariance(final Date start, final double strike) {
        return blackVariance(start, strike, false);
    }

    public double blackVariance(final double t, final double strike, final boolean extrapolate) {
        checkRange(t, strike, extrapolate);
        final double vol = volatilityImpl(t, strike);
        return vol * vol * t;
    }

    public double blackVariance(final double t, final double strike) {
        return blackVariance(t, strike, false);
    }

    public double blackVariance(final Period optionTenor, final double strike, final boolean extrapolate) {
        final Date exerciseDate = calendar().advance(referenceDate(), optionTenor, BusinessDayConvention.FOLLOWING); // FIXME
        return blackVariance(exerciseDate, strike, extrapolate);

    }

    // TODO: code review :: please verify against QL/C++ code
    public void checkRange(final double t, final double k, final boolean extrapolate) {
        super.checkRange(t, extrapolate);
        QL.require(extrapolate||allowsExtrapolation()||(k >= minStrike() && k <= maxStrike()) , "strike is outside curve domain"); // QA:[RG]::verified // TODO: message
    }

    @Override
    public Date maxDate() {
        // TODO Auto-generated method stub
        return null;
    }


    public abstract double minStrike();

    public abstract double maxStrike();


}