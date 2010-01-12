/*
Copyright (C) 2009 Ueli Hofstetter
Copyright (C) 2009 John Martin

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

/*
 Copyright (C) 2006 Giorgio Facchinetti
 Copyright (C) 2006 Mario Pucci
 Copyright (C) 2006, 2007 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.


 This program is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE. See the license for more details.
*/

package org.jquantlib.cashflow;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.SwapIndex;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Schedule;

/**
 * Helper class building a sequence of capped/floored cms-rate coupons
 *
 * @author Ueli Hofstetter
 * @author John Martin
 */
public class CmsLeg {

    private Schedule schedule_;
    private SwapIndex swapIndex_;
    private/* @Real */Array notionals_;
    private DayCounter paymentDayCounter_;
    private BusinessDayConvention paymentAdjustment_;
    private int[] fixingDays_;
    private Array gearings_;
    private/* @Spread */Array spreads_;
    private/* @Rate */Array caps_, floors_;
    private boolean inArrears_, zeroPayments_;

    public CmsLeg(final Schedule schedule, final SwapIndex swapIndex) {
        QL.validateExperimentalMode();
        
        schedule_ = (schedule);
        swapIndex_ = (swapIndex);
        paymentAdjustment_ = (BusinessDayConvention.Following);
        inArrears_ = (false);
        zeroPayments_ = (false);
    }

    public CmsLeg withNotionals(/* Real */double notional) {
        notionals_ = new Array(1).fill(notional);
        return this;
    }

    public CmsLeg withNotionals(final Array notionals) {
        notionals_ = notionals;
        return this;
    }

    public CmsLeg withPaymentDayCounter(final DayCounter dayCounter) {
        paymentDayCounter_ = dayCounter;
        return this;
    }

    public CmsLeg withPaymentAdjustment(BusinessDayConvention convention) {
        paymentAdjustment_ = convention;
        return this;
    }

    public CmsLeg withFixingDays(/* Natural */int fixingDays) {
        fixingDays_ = new int[] { fixingDays };
        return this;
    }

    public CmsLeg withFixingDays(final int[] fixingDays) {
        fixingDays_ = fixingDays;
        return this;
    }

    public CmsLeg withGearings(/* Real */double gearing) {
        gearings_ = new Array(1).fill(gearing);
        return this;
    }

    public CmsLeg withGearings(final Array gearings) {
        gearings_ = gearings;
        return this;
    }

    public CmsLeg withSpreads(/* Spread */double spread) {
        spreads_ = new Array(1).fill(spread);
        return this;
    }

    public CmsLeg withSpreads(final Array spreads) {
        spreads_ = spreads;
        return this;
    }

    public CmsLeg withCaps(/* @Rate */double cap) {
        caps_ = new Array(1).fill(cap);
        return this;
    }

    public CmsLeg withCaps(final Array caps) {
        caps_ = caps;
        return this;
    }

    public CmsLeg withFloors(/* @Rate */double floor) {
        floors_ = new Array(1).fill(floor);
        return this;
    }

    public CmsLeg withFloors(final Array floors) {
        floors_ = floors;
        return this;
    }

    public CmsLeg inArrears(boolean flag) {
        inArrears_ = flag;
        return this;
    }

    public CmsLeg withZeroPayments(boolean flag) {
        zeroPayments_ = flag;
        return this;
    }

    public Leg Leg() {
        throw new UnsupportedOperationException ("Work in progress");
        // FIXME
        
        /*
    	final Leg cashflows = new FloatingLeg <SwapIndex, CmsCoupon, CappedFlooredCmsCoupon>
          (notionals_, schedule_, swapIndex_, paymentDayCounter_, 
           paymentAdjustment_, fixingDays_, gearings_, spreads_, 
           caps_, floors_, inArrears_, zeroPayments_) {};
      
        PricerSetter.setCouponPricer (cashflows, new CmsCouponPricer 
                                    (new Handle <OptionletVolatilityStructure ()));
        */
        //return new Leg();
    }

}
