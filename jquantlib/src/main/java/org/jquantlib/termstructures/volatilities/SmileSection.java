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

package org.jquantlib.termstructures.volatilities;

import java.util.List;

import org.jquantlib.Configuration;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

public abstract class SmileSection implements Observable {

    private Date exerciseDate_;
    private DayCounter dc_;
    protected double exerciseTime_;

    // ! interest rate volatility smile section
    /* ! This abstract class provides volatility smile section interface */

    public SmileSection(Date d) {
        // this(d,Actual365Fixed.getDayCounter(), DateFactory.getFactory().getTodaysDate());
    }

    public abstract double minStrike();

    public abstract double maxStrike();

    public abstract double variance(double strike);

    public abstract double volatility();

    public Date exerciseDate() {
        return exerciseDate_;
    }

    public double exerciseTime() {
        return exerciseTime_;
    }

    public DayCounter dayCounter() {
        return dc_;
    }

    public SmileSection(Date d, DayCounter dc, Date referenceDate) {
        exerciseDate_ = d;
        dc_ = dc;
        // FIXME: should be compared to new Date()...
        Date refDate = (!referenceDate.eq(DateFactory.getFactory().getTodaysDate())) ? referenceDate : Configuration
                .getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
        if (d.ge(refDate)) {
            throw new IllegalArgumentException("expiry date (" + d + ") must be greater than reference date (" + refDate + ")");
        }
        exerciseTime_ = dc_.yearFraction(refDate, d);
    }

    public SmileSection(double exerciseTime, DayCounter dc) {
        this.dc_ = dc;
        this.exerciseTime_ = exerciseTime;
        if (exerciseTime_ < 0.0) {
            throw new IllegalArgumentException("expiry time must be positive: " + exerciseTime_ + " not allowed");
        }

    }

    public SmileSection(double timeToExpiry) {
        this(timeToExpiry, Actual365Fixed.getDayCounter());
    }

    @Override
    public void addObserver(Observer observer) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int countObservers() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void deleteObserver(Observer observer) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteObservers() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Observer> getObservers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void notifyObservers() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void notifyObservers(Object arg) {
        // TODO Auto-generated method stub
        
    }

}
