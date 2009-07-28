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
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.InterestRateIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 *
 * @author Ueli Hofstetter
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
// TODO: code review :: Please review this class! :S
public class FloatingRateCoupon extends Coupon implements Observer {

    private static final String null_gearing = "Null gearing: degenerate Floating Rate Coupon not admitted";
    private static final String no_adequate_pricer_given = "no adequate pricer given";
    private static final String pricer_not_set = "pricer not set";


    //
    // private final fields
    //


    private final DayCounter dayCounter;
    private final int fixingDays;
    private final double spread;
    private final boolean isInArrears;


    //
    // private fields
    //

    /**
     * convexity adjustment for the given index fixing
     */
    // TODO: what's the need of it?
    //XXX private double convexityAdjustmentImpl;

    private FloatingRateCouponPricer pricer;

    //TODO: code review :: please verify comment below against original QL/C++ code
    //XXX (Rate fixing) const;


    //
    // protected fields
    //

    //TODO: code review :: please verify against original QL/C++ code
    protected final double gearing_;

    //TODO: code review :: please verify against original QL/C++ code
    protected final InterestRateIndex index_;


    //
    // public constructors
    //

    public FloatingRateCoupon(
                final Date paymentDate,
                final double nominal,
                final Date startDate,
                final Date endDate,
                final int fixingDays,
                final InterestRateIndex index,
                final double gearing,
                final double spread,
                final Date refPeriodStart,
                final Date refPeriodEnd,
                final DayCounter dayCounter,
                final boolean isInArrears) {
        super(nominal, paymentDate, startDate, endDate, refPeriodStart, refPeriodEnd);

        // TODO: code review :: please verify against original QL/C++ code
        assert gearing > 0 : null_gearing;

        this.index_ = index;
        this.fixingDays = (fixingDays == 0 ? index.fixingDays() : fixingDays);
        this.gearing_ = gearing;
        this.spread = spread;
        this.isInArrears = isInArrears;

        if (dayCounter != null)
            this.dayCounter = dayCounter;
        else
            this.dayCounter = index_.dayCounter();

        index.addObserver(this);
        Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate().addObserver(this);
    }


    //
    // public methods
    //

    public void setPricer(final FloatingRateCouponPricer pricer){
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if(pricer == null)
            throw new IllegalArgumentException(no_adequate_pricer_given);

        if (this.pricer != null)
            this.pricer.deleteObserver(this);
        this.pricer = pricer;
        this.pricer.addObserver(this);
        update();
    }

    @Override
    public double amount(){
        return rate() * accrualPeriod() * nominal();
    }

    public double price(final Handle<YieldTermStructure> discountingCurve){
        return amount()*discountingCurve.getLink().discount(date());
    }

    public InterestRateIndex index()  {
        return index_;
    }

    public int fixingDays() {
        return fixingDays;
    }

    public Date fixingDate() {
        // if isInArrears_ fix at the end of period
        final Date refDate = isInArrears ? accrualEndDate : accrualStartDate;
        // FIXME: "isInArrears" : not specified in original implementation
        return index_.fixingCalendar().advance(refDate, -fixingDays, TimeUnit.DAYS, BusinessDayConvention.PRECEDING, isInArrears);
    }

    public double gearing()  {
        return gearing_;
    }

    public double spread() {
        return spread;
    }

    public double indexFixing()  {
        return index_.fixing(fixingDate());
    }

    public double adjustedFixing(){
        return (rate()-spread())/gearing();
    }

    // TODO: code review :: Please review this method! What's the need of it???
    public double convexityAdjustmentImpl(final double f)  {
       return (gearing() == 0.0 ? 0.0 : adjustedFixing()-f);
    }

    public double convexityAdjustment() {
        return convexityAdjustmentImpl(indexFixing());
    }

    //TODO: verify where this method is called and if it can be replaced by update(Observable o, Object arg)
    public  void update() {
        notifyObservers();
    }

    public boolean isInArrears(){
        return isInArrears;
    }


    //
    // Overrides Coupon
    //

    @Override
    public DayCounter dayCounter() {
        return dayCounter;
    }

    @Override
    public  double rate() {
        if(this.pricer == null)
            throw new IllegalArgumentException(pricer_not_set);

        // TODO: code review :: please verify against original QL/C++ code
        this.pricer.initialize(this);
        return this.pricer.swapletRate();
    }

	@Override
	public double accruedAmount(final Date date) {
		// TODO: code review :: please verify against original QL/C++ code
		throw new UnsupportedOperationException();
	}


	//
	// implements Observer
	//

	@Override
    public void update(final Observable o, final Object arg) {
        notifyObservers();
    }


	//
	// implements TypedVisitable
	//

    @Override
    public void accept(final TypedVisitor<Object> v) {
        final Visitor<Object> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
        if (v1 != null)
            v1.visit(this);
        else
            super.accept(v);
    }

}
