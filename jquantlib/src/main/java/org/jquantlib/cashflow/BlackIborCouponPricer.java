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

import org.jquantlib.CapletVolatilityStructure;
import org.jquantlib.Configuration;
import org.jquantlib.indexes.InterestRateIndex;
import org.jquantlib.instruments.Option;
import org.jquantlib.lang.annotation.Rate;
import org.jquantlib.pricingengines.BlackFormula;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Date;
import org.jquantlib.util.Observable;

/*
 * DONE!
 */


public class BlackIborCouponPricer extends IborCouponPricer {
    
    private final static String missing_caplet_volatility = "missing caplet volatility";

    public BlackIborCouponPricer(Handle<CapletVolatilityStructure> capletVol) {
        super(capletVol);
    }

    private IborCoupon coupon_;
    private double discount_;
    private double gearing_;
    private double spread_;
    private double spreadLegValue_;
    
    
    public void initialize( FloatingRateCoupon coupon) {
        coupon_ =  (IborCoupon)coupon;
        gearing_ = coupon_.gearing();
        spread_ = coupon_.spread();
        Date paymentDate = coupon_.date();
        InterestRateIndex index = coupon_.index();
        Handle<YieldTermStructure> rateCurve = index.getTermStructure();

        Date today = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();

        if(paymentDate.gt(today)){
            discount_ = rateCurve.getLink().discount(paymentDate);
        }
        else{
            discount_ = 1.0;
        }
        spreadLegValue_ = spread_ * coupon_.accrualPeriod()* discount_;
    }

    public double swapletPrice() {
        // past or future fixing is managed in InterestRateIndex::fixing()
        double swapletPrice = adjustedFixing()* coupon_.accrualPeriod()* discount_;
        return gearing_ * swapletPrice + spreadLegValue_;
    }

    public double swapletRate()  {
        return swapletPrice()/(coupon_.accrualPeriod()*discount_);
    }

    public double capletPrice(double effectiveCap)  {
        double capletPrice = optionletPrice(Option.Type.CALL, effectiveCap);
        return gearing_ * capletPrice;
    }

    public double capletRate(double effectiveCap) {
        return capletPrice(effectiveCap)/(coupon_.accrualPeriod()*discount_);
    }

    public double floorletPrice(double effectiveFloor)  {
        double floorletPrice = optionletPrice(Option.Type.PUT, effectiveFloor);
        return gearing_ * floorletPrice;
    }

    public double floorletRate(double effectiveFloor) {
        return floorletPrice(effectiveFloor)/
            (coupon_.accrualPeriod()*discount_);
    }

    public double optionletPrice(Option.Type optionType,
                                               double effStrike)  {
        Date fixingDate = coupon_.fixingDate();
        if (fixingDate.le(Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate())) {
            // the amount is determined
            double a, b;
            if (optionType==Option.Type.CALL) {
                a = coupon_.indexFixing();
                b = effStrike;
            } else {
                a = effStrike;
                b = coupon_.indexFixing();
            }
            return Math.max(a - b, 0.0)* coupon_.accrualPeriod()*discount_;
        } 
        else {
            if(capletVolatility()==null){
                throw new IllegalArgumentException(missing_caplet_volatility);
            }
            // not yet determined, use Black model
            double fixing =
                 BlackFormula.blackFormula(
                       optionType,
                       effStrike,
                       adjustedFixing(),
                       Math.sqrt(capletVolatility().getLink().blackVariance(fixingDate,
                                                                   effStrike)));
            return fixing * coupon_.accrualPeriod()*discount_;
        }
    }

    public double adjustedFixing() {

        double adjustement = 0.0;

        double fixing = coupon_.indexFixing();
        
        if (!coupon_.isInArrears()) {
            adjustement = 0.0;
        } else {
            // see Hull, 4th ed., page 550
            if(capletVolatility() == null){
                throw new IllegalArgumentException(missing_caplet_volatility);
            };
            Date d1 = coupon_.fixingDate(),
                 referenceDate = capletVolatility().getLink().referenceDate();
            if (d1.le(referenceDate)) {
                adjustement = 0.0;
            } else {
                Date d2 = coupon_.index().maturityDate(d1);
                double tau = coupon_.index().getDayCounter().yearFraction(d1, d2);
                double variance = capletVolatility().getLink().blackVariance(d1, fixing);
                adjustement = fixing*fixing*variance*tau/(1.0+fixing*tau);
            }
        }
        return fixing + adjustement;
    }

           
}
