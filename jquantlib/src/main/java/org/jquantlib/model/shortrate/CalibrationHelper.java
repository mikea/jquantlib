/*
Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.model.shortrate;

import java.util.ArrayList;
import org.jquantlib.lang.annotation.Time;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

/**
 * 
 * @author Praneet Tiwari
 */
public abstract class CalibrationHelper implements Observer, Observable {

    protected double/* @Real */marketValue;
    protected Handle<Quote> volatility;
    protected Handle<YieldTermStructure> termStructure;
    protected PricingEngine engine;
    private boolean calibrateVolatility = false;

    // private:
    // class ImpliedVolatilityHelper;

    public CalibrationHelper(final Handle<Quote> volatility, final Handle<YieldTermStructure> termStructure,
            boolean calibrateVolatility) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.volatility = volatility;
        this.termStructure = termStructure;
        this.calibrateVolatility = calibrateVolatility;
        /*
         * registerWith(volatility_); registerWith(termStructure_);
         */

    }

    // abstract double blackPrice (double /*@Volatility*/ volatility);

    public void update() {
        /* marketValue_ = blackPrice(volatility_->value()); */
        marketValue = blackPrice(volatility.getLink().evaluate());
        notifyObservers();
    }

    // ! returns the actual price of the instrument (from volatility)
    double/* @Real */marketValue() {
        return marketValue;
    }

    // ! returns the price of the instrument according to the model
    public abstract double /* @Real */modelValue();

    // ! returns the error resulting from the model valuation
    public abstract double /* @Real */calibrationError();

    public abstract void addTimesTo(ArrayList<Time> times);

    // ! Black volatility implied by the model
    public abstract double /* @Volatility */impliedVolatility(double /* @Real */targetValue, double /* @Real */accuracy,
            int /* @Size */maxEvaluations, double /* @Volatility */minVol, double /* @Volatility */maxVol);

    // ! Black price given a volatility
    public abstract double /* @Real */blackPrice(double /* @Volatility */volatility);

    public void setPricingEngine(final PricingEngine engine) {
        this.engine = engine;
    }
}
