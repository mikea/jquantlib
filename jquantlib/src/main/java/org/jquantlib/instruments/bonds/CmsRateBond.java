/*
 Copyright (C) 2010 Zahid Hussain

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
package org.jquantlib.instruments.bonds;

import org.jquantlib.QL;
import org.jquantlib.cashflow.CmsLeg;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.SwapIndex;
import org.jquantlib.instruments.Bond;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Date;
import org.jquantlib.time.Schedule;

/**
*
* @author Zahid Hussain
*/
//TODO: Work in progress

public class CmsRateBond extends Bond {
/* cmsratebond.hpp file
 *       public:
        CmsRateBond(Natural settlementDays,
                    Real faceAmount,
                    const Schedule& schedule,
                    const boost::shared_ptr<SwapIndex>& index,
                    const DayCounter& paymentDayCounter,
                    BusinessDayConvention paymentConvention
                                    = Following,
                    Natural fixingDays = Null<Natural>(),
                    const std::vector<Real>& gearings
                                    = std::vector<Real>(1, 1.0),
                    const std::vector<Spread>& spreads
                                    = std::vector<Spread>(1, 0.0),
                    const std::vector<Rate>& caps
                                    = std::vector<Rate>(),
                    const std::vector<Rate>& floors
                                    = std::vector<Rate>(),
                    bool inArrears = false,
                    Real redemption = 100.0,
                    const Date& issueDate = Date());

  * cmsratebond.ccp file
  * 
    CmsRateBond::CmsRateBond(
                           Natural settlementDays,
                           Real faceAmount,
                           const Schedule& schedule,
                           const boost::shared_ptr<SwapIndex>& index,
                           const DayCounter& paymentDayCounter,
                           BusinessDayConvention paymentConvention,
                           Natural fixingDays,
                           const std::vector<Real>& gearings,
                           const std::vector<Spread>& spreads,
                           const std::vector<Rate>& caps,
                           const std::vector<Rate>& floors,
                           bool inArrears,
                           Real redemption,
                           const Date& issueDate)
    : Bond(settlementDays, schedule.calendar(), issueDate) {

        maturityDate_ = schedule.endDate();

        cashflows_ = CmsLeg(schedule, index)
            .withNotionals(faceAmount)
            .withPaymentDayCounter(paymentDayCounter)
            .withPaymentAdjustment(paymentConvention)
            .withFixingDays(fixingDays)
            .withGearings(gearings)
            .withSpreads(spreads)
            .withCaps(caps)
            .withFloors(floors)
            .inArrears(inArrears);

        addRedemptionsToCashflows(std::vector<Real>(1, redemption));

        QL_ENSURE(!cashflows().empty(), "bond with no cashflows!");
        QL_ENSURE(redemptions_.size() == 1, "multiple redemptions created");

        registerWith(index);
    }
 */
	/**Translation 
	 * boost::shared_ptr<SwapIndex> --> SwapIndex
	 * const std::vector<Real>  	--> Array
	 * const std::vector<Spread>    --> Array
	 * const std::vector<Rate>    --> Array
	 * Spread = Rate = Real --> double
	 */
	public CmsRateBond(
            final /*Natural */ int settlementDays,
            final /*Real*/ double faceAmount,
            final Schedule schedule,
            final SwapIndex index,
            final DayCounter paymentDayCounter,
            final BusinessDayConvention paymentConvention,
            final /*Natural*/ int fixingDays,
            final Array gearings,
            final Array spreads,
            final Array caps,
            final Array floors,
            final boolean inArrears,
            final /*Real*/ double  redemption,
            final Date issueDate) {
		
		super(settlementDays, schedule.calendar(), issueDate);
		maturityDate_ = schedule.endDate().clone();
		cashflows_ = new CmsLeg(schedule, index)
				.withNotionals(faceAmount)
				.withPaymentDayCounter(paymentDayCounter)
				.withPaymentAdjustment(paymentConvention)
				.withFixingDays(fixingDays)
				.withGearings(gearings)
				.withSpreads(spreads)
				.withCaps(caps)
				.withFloors(floors)
				.inArrears(inArrears).Leg();

		addRedemptionsToCashflows(new double[] { redemption });

		QL.ensure(!cashflows().isEmpty(), "bond with no cashflows!");
		QL.ensure(redemptions_.size() == 1, "multiple redemptions created");
		index.addObserver(this);		
	}

}
