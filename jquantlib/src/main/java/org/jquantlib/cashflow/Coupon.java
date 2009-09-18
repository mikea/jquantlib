/*
 Copyright (C) 2009 Ueli Hofstetter
 Copyright (C) 2009 Daniel Kong

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

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.util.Date;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * Coupon accruing over a fixed period
 * <p>
 * This class implements part of the CashFlow interface but it is still abstract and provides derived classes with methods for
 * accrual period calculations.
 *
 * @author Ueli Hofstetter
 * @author Daniel Kong
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public abstract class Coupon extends CashFlow {

    //
    // protected fields
    //

    protected double nominal;
    protected Date paymentDate;
    protected Date accrualStartDate;
    protected Date accrualEndDate;
    protected Date refPeriodStart;
    protected Date refPeriodEnd;


    //
    // public constructors
    //

    public Coupon(final double nominal,
            final Date paymentDate,
            final Date accrualStartDate,
            final Date accrualEndDate){
        this(nominal, paymentDate, accrualStartDate, accrualEndDate, new Date(), new Date());

        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    public Coupon(final double nominal,
            final Date paymentDate,
            final Date accrualStartDate,
            final Date accrualEndDate,
            final Date refPeriodStart,
            final Date refPeriodEnd){
        this.nominal = nominal;
        this.paymentDate = paymentDate;
        this.accrualStartDate = accrualStartDate;
        this.accrualEndDate = accrualEndDate;
        this.refPeriodStart = refPeriodEnd;
        this.refPeriodEnd = refPeriodEnd;

        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }


    //
    // public abstract methods
    //

    public abstract /*Rate*/ double rate();

    public abstract DayCounter dayCounter();

    public abstract double accruedAmount(final Date date);


    //
    // public methods
    //

    public double nominal(){
        return nominal;
    }

    public Date accrualStartDate(){
        return accrualStartDate;
    }

    public Date accrualEndDate(){
        return accrualEndDate;
    }

    public Date referencePeriodStart() {
        return refPeriodStart;
    }

    public Date referencePeriodEnd() {
        return refPeriodEnd;
    }

    public double accrualPeriod() {
        return dayCounter().yearFraction(accrualStartDate,
                accrualEndDate,
                refPeriodStart,
                refPeriodEnd);
    }

    public int accrualDays() {
        return dayCounter().dayCount(accrualStartDate,
                accrualEndDate);
    }


    //
    // implements Event
    //

    @Override
    public Date date() {
        return paymentDate.clone();
    }


    //
    // implements TypedVisitable
    //

    @Override
    public void accept(final TypedVisitor<Object> v) {
        final Visitor<Object> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
        if (v1 != null) {
            v1.visit(this);
        } else {
            super.accept(v);
        }
    }

}
