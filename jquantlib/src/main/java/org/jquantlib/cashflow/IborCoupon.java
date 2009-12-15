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

import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.cashflow.FloatingRateCoupon;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.indexes.InterestRateIndex;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Date;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

// TODO: code review :: please verify against QL/C++ code
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class IborCoupon extends FloatingRateCoupon {

    /**
     * WORK IN PROGRESS
     */

    private final static String null_term_structure = "null term structure set to par coupon";

    /*
     * @category constuctors
     */
    public IborCoupon(final Date paymentDate,
                      final double nominal,
                      final Date startDate,
                      final Date endDate,
                      final int fixingDays,
                      final IborIndex index)
    {
        // gearing default constructor
        this (paymentDate, nominal, startDate, endDate, fixingDays, index, 1.0);
    }

    /*
     * @category constuctors
     */
    public IborCoupon(final Date paymentDate,
                      final double nominal,
                      final Date startDate,
                      final Date endDate,
                      final int fixingDays,
                      final IborIndex index,
                      final double gearing)
    {
        // spread default constructor
        this (paymentDate, nominal, startDate, endDate, fixingDays, index, gearing, 0.0);
    }


    /*
     * @category constuctors
     */
    public IborCoupon(final Date paymentDate,
                      final double nominal,
                      final Date startDate,
                      final Date endDate,
                      final int fixingDays,
                      final IborIndex index,
                      final double gearing,
                      final double spread)
    {
        // refperiodStart, refperiod end default constructor
        this (paymentDate, nominal, startDate, endDate, fixingDays, 
              index, gearing, spread, new Date(), new Date());
    }

    /*
     * @category constuctors
     */
    public IborCoupon(final Date paymentDate,
                      final double nominal,
                      final Date startDate,
                      final Date endDate,
                      final int fixingDays,
                      final IborIndex index,
                      final double gearing,
                      final double spread, 
                      final Date refPeriodStart,
                      final Date refPeriodEnd)
    {
        // default daycounter constructor
        this (paymentDate, nominal, startDate, endDate, fixingDays, 
              index, gearing, spread, refPeriodStart, refPeriodEnd, new DayCounter());
    }


    /*
     * @category constuctors
     */
    public IborCoupon(final Date paymentDate,
                      final double nominal,
                      final Date startDate,
                      final Date endDate,
                      final int fixingDays,
                      final IborIndex index,
                      final double gearing,
                      final double spread, 
                      final Date refPeriodStart,
                      final Date refPeriodEnd,
                      final DayCounter dayCounter)
    {
        // default inArrears constructor
        this (paymentDate, nominal, startDate, endDate, fixingDays, 
              index, gearing, spread, refPeriodStart, refPeriodEnd, dayCounter, false);
    }
    
    
    /* IborIndex primary constructor
     * @category constuctors
     */
    public IborCoupon(
            final Date paymentDate,
            final double nominal,
            final Date startDate,
            final Date endDate,
            final int fixingDays,
            final IborIndex index,
            final double gearing,
            final double spread,
            final Date refPeriodStart,
            final Date refPeriodEnd,
            final DayCounter dayCounter,
            final boolean isInArrears) {
        super(paymentDate, nominal, startDate, endDate, fixingDays, index,
                gearing, spread, refPeriodStart, refPeriodEnd,
                dayCounter, isInArrears);
    }

    @Override
    public double indexFixing() {
        // FIMXE: do this configuration using the settings object
        // #ifdef QL_USE_INDEXED_COUPON
        // return index_->fixing(fixingDate());
        // #else
        if (isInArrears()) {
            return index_.fixing(fixingDate());
        } else {
            final Handle<YieldTermStructure> termStructure = index_.termStructure();
            QL.require(termStructure != null , null_term_structure);  // QA:[RG]::verified // TODO: message
            final Date today = new Settings().evaluationDate();
            final Date fixing_date = fixingDate();
            if (fixing_date.lt(today)) {
                // must have been fixed
                // FIXME ...

                //TODO: Code review :: incomplete code
                if (System.getProperty("EXPERIMENTAL") == null) {
                    throw new UnsupportedOperationException("Work in progress");
                }

                final double pastFixing = 0;// IndexManager.getInstance().getHistory( index_.getName())[fixing_date.g];
                QL.require(pastFixing > 0 , "Missing fixing");  // QA:[RG]::verified // TODO: message
                return pastFixing;
            }
            if (fixing_date == today) {
                // might have been fixed
                try {
                    // FIXME....
                    final double pastFixing = Double.NaN; //IndexManager.getInstance().getHistory(index_.name())[fixing_date];
                    if (! Double.isNaN (pastFixing)) {
                        return pastFixing;
                    } else {
                        ; // fall through and forecast
                    }
                } catch (final Exception e) {
                    ; // fall through and forecast
                }
            }
            
            // start discount
            final Date fixingValueDate = index_.fixingCalendar()
                .advance(fixing_date, index_.fixingDays(), TimeUnit.Days);
            final double startDiscount = termStructure.currentLink().discount(fixingValueDate);

            // end discount
            final Date nextFixingDate = index_.fixingCalendar()
                .advance(accrualEndDate, -(fixingDays()), TimeUnit.Days);
            final Date nextFixingValueDate = index_.fixingCalendar()
                .advance (nextFixingDate, index_.fixingDays(), TimeUnit.Days);
            final double endDiscount = termStructure.currentLink().discount(nextFixingValueDate);

            // spanning time
            final double spanningTime = index_.dayCounter()
                .yearFraction (fixingValueDate, nextFixingValueDate);
            // implied fixing
            return (startDiscount / endDiscount - 1.0) / spanningTime;
        }
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
