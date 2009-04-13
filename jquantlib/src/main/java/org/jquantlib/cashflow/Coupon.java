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

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.util.Date;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;
//! %coupon accruing over a fixed period
/*! This class implements part of the CashFlow interface but it is
    still abstract and provides derived classes with methods for
    accrual period calculations.
*/
public abstract class Coupon extends CashFlow {
    
    protected double nominal_;
    protected Date paymentDate_,
                   accrualStartDate_,
                   accrualEndDate_,
                   refPeriodStart_,
                   refPeriodEnd_;
    
    public Coupon(double nominal,
            Date paymentDate,
            Date accrualStartDate,
            Date accrualEndDate,
            Date refPeriodStart,
            Date refPeriodEnd){
        this.nominal_ = nominal;
        this.paymentDate_ = paymentDate;
        this.accrualStartDate_ = accrualStartDate;
        this.accrualEndDate_ = accrualEndDate;
        this.refPeriodStart_ = refPeriodEnd;
        this.refPeriodEnd_ = refPeriodEnd;
        
        // TODO: this allows as to provide only one constructor, no clue why this is done this way in c++
        if(refPeriodStart_ == null){
            refPeriodStart_ = accrualEndDate_;
        }
        if(refPeriodEnd_ == null){
            refPeriodEnd_ = accrualEndDate_;
        }
    }
    
    public double nominal(){
        return nominal_;
    }
    
    public Date accrualStartDate(){
        return accrualStartDate_;
    }

    public Date accrualEndDate(){
        return accrualEndDate_;
    }
    
    public Date referencePeriodStart() {
        return refPeriodStart_;
    }

    public Date referencePeriodEnd() {
        return refPeriodEnd_;
    }

    public double accrualPeriod() {
        return dayCounter().yearFraction(accrualStartDate_,
                                         accrualEndDate_,
                                         refPeriodStart_,
                                         refPeriodEnd_);
    }

    public int accrualDays() {
        return dayCounter().dayCount(accrualStartDate_,
                                     accrualEndDate_);
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
    
    public abstract DayCounter dayCounter();

    @Override
    public Date date() {
        return paymentDate_;
    }

}
