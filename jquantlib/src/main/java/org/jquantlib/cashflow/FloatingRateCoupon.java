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


package org.jquantlib.cashflow;

import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.InterestRateIndex;
import org.jquantlib.lang.annotation.Rate;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

public class FloatingRateCoupon extends Coupon implements Observer {
    
    private static final String null_gearing = "Null gearing: degenerate Floating Rate Coupon not admitted";
    private static final String no_adequate_pricer_given = "no adequate pricer given";
    private static final String pricer_not_set = "pricer not set";
    
    
    // ! convexity adjustment for the given index fixing
   private  double convexityAdjustmentImpl;// (Rate fixing) const;
   protected InterestRateIndex index_;
   private DayCounter dayCounter_;
   private int fixingDays_;
   private double gearing_;
   private double spread_;
   private boolean isInArrears_;
   private FloatingRateCouponPricer pricer_; //FIXME: to be implemented

    public FloatingRateCoupon(Date paymentDate, double nominal, Date startDate, Date endDate, int fixingDays,
            InterestRateIndex index, double gearing, double spread, Date refPeriodStart, Date refPeriodEnd, DayCounter dayCounter,
            boolean isInArrears) {
        super(nominal, paymentDate, startDate, endDate, refPeriodStart, refPeriodEnd);
        this.index_ = index;
        this.dayCounter_ = dayCounter;
        this.fixingDays_ = (fixingDays == 0 ? index.getFixingDays() : fixingDays);
        this.gearing_ = gearing;
        this.spread_ = spread;
        this.isInArrears_ = isInArrears;
        
        if(gearing_ == 0){
            throw new IllegalArgumentException(null_gearing);
        }
        //FIXME: empty() ???
        if(dayCounter_ == null){
            dayCounter_ = index_.getDayCounter();
        }
        
        index.addObserver(this);
        Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate().addObserver(this);
    }
    
    public void setPricer(FloatingRateCouponPricer pricer){
        if(pricer_ != null){
            pricer_.deleteObserver(this);
        }
        pricer_ = pricer;
        if(pricer_ == null){
            throw new IllegalArgumentException(no_adequate_pricer_given);
        }
        pricer_.addObserver(this);
        update();
    }
    
    public double amount(){
        return rate() * accrualPeriod() * nominal();
    }
    
    public double price(Handle<YieldTermStructure> discountingCurve){
        return amount()*discountingCurve.getLink().discount(date());
    }
    @Override
    public DayCounter dayCounter() {
        return dayCounter_;
    }

    public InterestRateIndex index()  {
        return index_;
    }

    public int fixingDays() {
        return fixingDays_;
    }

    public Date fixingDate() {
        // if isInArrears_ fix at the end of period
        Date refDate = isInArrears_ ? accrualEndDate : accrualStartDate;
        return index_.getFixingCalendar().advance(refDate,
            -fixingDays_, TimeUnit.DAYS, BusinessDayConvention.PRECEDING, /*not specified in original implementation*/isInArrears_);
    }

    public double gearing()  {
        return gearing_;
    }

    public double spread() {
        return spread_;
    }


    public double indexFixing()  {
        return index_.fixing(fixingDate());
    }
    
    public  double rate() {
        if(pricer_ == null){
            throw new IllegalArgumentException(pricer_not_set);
        }
        //FIXME: .....
        pricer_.initialize(this);
        return pricer_.swapletRate();
    }

    public double adjustedFixing(){
        return (rate()-spread())/gearing();
    }
    public double convexityAdjustmentImpl(double f)  {
       return (gearing() == 0.0 ? 0.0 : adjustedFixing()-f);
    }
    public double convexityAdjustment() {
        return convexityAdjustmentImpl(indexFixing());
    }

    public  void update() {
        notifyObservers();
    }

    @Override
    public void accept(final TypedVisitor<Event> v) {
        Visitor<Event> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
        if (v1 != null) {
            v1.visit(this);
        } else {
            super.accept(v);
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        notifyObservers();
    }

    @Override
    public double getAmount() {
        throw new UnsupportedOperationException("to be fixed ... getAmount()");
        //return 0;
    }
    
    public boolean isInArrears(){
        return isInArrears_;
    }

	@Override
	public double accruedAmount(Date date) {
		// TODO Auto-generated method stub
		return 0;
	}
    
}
