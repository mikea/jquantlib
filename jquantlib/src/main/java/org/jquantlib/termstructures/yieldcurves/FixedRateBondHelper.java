/*
 Copyright (C) 2008 Srinivas Hasti

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

package org.jquantlib.termstructures.yieldcurves;

import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Date;
import org.jquantlib.time.Schedule;

//TODO: This class still needs to be finished
public class FixedRateBondHelper<YieldTermStructure> extends RateHelper {

	public FixedRateBondHelper(
            Handle<Quote> cleanPrice,
            int settlementDays,
            double faceAmount,
            Schedule schedule,
            List<Double> coupons,
            DayCounter dayCounter,
            BusinessDayConvention paymentConvention,
            double redemption,
            Date issueDate)
 {
     super (cleanPrice);
     QL.validateExperimentalMode();
     
	 //super(cleanPrice);
	 /*
bond_ = boost::shared_ptr<FixedRateBond>(new
FixedRateBond(settlementDays, faceAmount, schedule,
  coupons, dayCounter, paymentConvention,
  redemption, issueDate));

latestDate_ = bond_->maturityDate();
registerWith(Settings::instance().evaluationDate());

boost::shared_ptr<PricingEngine> bondEngine(new
DiscountingBondEngine(termStructureHandle_));
bond_->setPricingEngine(bondEngine); */
}
            
	@Override
	public double impliedQuote() {
		// TODO Auto-generated method stub
		return 0;
	}

}
