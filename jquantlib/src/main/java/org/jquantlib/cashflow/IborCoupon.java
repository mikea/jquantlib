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
import org.jquantlib.indexes.IndexManager;
import org.jquantlib.indexes.InterestRateIndex;
import org.jquantlib.lang.annotation.DiscountFactor;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

public class IborCoupon extends FloatingRateCoupon {
    
    /**
     * WORK IN PROGRESS
     */

    private final static String null_term_structure = "null term structure set to par coupon";

    public IborCoupon(Date paymentDate, int nominal, Date startDate, Date endDate, int fixingDays, InterestRateIndex index,
            double gearing, double spread, Date refPeriodStart, Date refPeriodEnd, DayCounter dayCounter, boolean isInArrears) {
        super(paymentDate, nominal, startDate, endDate, fixingDays, index, gearing, spread, refPeriodStart, refPeriodEnd,
                dayCounter, isInArrears);
    }

    public double indexFixing() {
        // FIMXE: do this configuration using the settings object
        // #ifdef QL_USE_INDEXED_COUPON
        // return index_->fixing(fixingDate());
        // #else
        if (isInArrears()) {
            return index_.fixing(fixingDate());
        } else {
            Handle<YieldTermStructure> termStructure = index_.getTermStructure();
            if (termStructure == null) {
                throw new IllegalArgumentException(null_term_structure);
            }
            Date today = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
            Date fixing_date = fixingDate();
            if (fixing_date.lt(today)) {
                // must have been fixed
                // FIXME ...
                double pastFixing = 0;// IndexManager.getInstance().getHistory( index_.getName())[fixing_date.g];
                if (pastFixing == 0) {
                    throw new IllegalArgumentException("Missing " + index_.name() + " fixing for " + fixing_date);
                }
                return pastFixing;
            }
            if (fixing_date == today) {
                // might have been fixed
                try {
                    // FIXME....
                    double pastFixing = 0;// IndexManager.getInstance().getHistory(index_.name())[fixing_date];
                    if (pastFixing != 0)
                        return pastFixing;
                    else
                        ; // fall through and forecast
                } catch (Exception e) {
                    ; // fall through and forecast
                }
            }
            Date fixingValueDate = index_.fixingCalendar().advance(fixing_date, index_.getFixingDays(), TimeUnit.DAYS);
            double startDiscount = termStructure.getLink().discount(fixingValueDate);
            // ???
            Date temp = index_.fixingCalendar().advance(accrualEndDate, -(fixingDays()), TimeUnit.DAYS);
            double endDiscount = termStructure.getLink().discount(
                    index_.fixingCalendar().advance(temp, index_.getFixingDays(), TimeUnit.DAYS));
            return (startDiscount / endDiscount - 1.0) / accrualPeriod();
        }
    }

    public void accept(final TypedVisitor<Event> v) {
        Visitor<Event> v1 = (v != null) ? v.getVisitor(this.getClass()) : null;
        if (v1 != null) {
            v1.visit(this);
        } else {
            super.accept(v);
        }
    }

}
