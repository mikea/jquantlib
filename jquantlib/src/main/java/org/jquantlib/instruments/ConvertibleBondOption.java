/*
 Copyright (C) 2008 Daniel Kong
 
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
package org.jquantlib.instruments;

import java.util.List;

import org.jquantlib.cashflow.Callability;
import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.cashflow.Dividend;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.ConvertibleBondOptionArguments;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.time.Schedule;
import org.jquantlib.util.Date;

/**
 * 
 * @author Daniel Kong
 */
//TODO: Work in progress
public class ConvertibleBondOption extends OneAssetStrikedOption{
	
    private final ConvertibleBond bond;
    private final double conversionRatio;
    private List<Callability> callability;
    private List<Dividend>  dividends;
    private Handle<Quote> creditSpread;
    private List<CashFlow> cashflows;
    private DayCounter dayCounter;
    private Date issueDate;
    private Schedule schedule;
    private int settlementDays;
    private double redemption;
    
    public ConvertibleBondOption(final ConvertibleBond bond,
            final StochasticProcess process,
            final Exercise exercise,
            final PricingEngine engine,
            double conversionRatio,
            final List<Dividend> dividends,
            final List<Callability> callability,
            final Handle<Quote> creditSpread,
            final List<CashFlow> cashflows,
            final DayCounter dayCounter,
            final Schedule schedule,
            final Date issueDate,
            int settlementDays,
            double redemption){
    	super(process, new PlainVanillaPayoff(Option.Type.CALL, bond.getFaceAmount()/100.0*redemption/conversionRatio),exercise, engine);
    	this.bond = bond;
    	this.conversionRatio = conversionRatio;
    	this.dividends =dividends;
    	this.callability = callability;
    	this.creditSpread = creditSpread;
    	this.cashflows = cashflows;
    	this.dayCounter = dayCounter;
    	this.schedule = schedule;
    	this.issueDate = issueDate;
    	this.settlementDays = settlementDays;
    	this.redemption = redemption;  	
    }
    
    @Override    
    public void setupArguments(final Arguments args) /* @ReadOnly */ {
		super.setupArguments(args);
		final ConvertibleBondOptionArguments moreArgs = (ConvertibleBondOptionArguments)args;

		moreArgs.conversionRatio = conversionRatio;

//TODO: need to change stochasticProcess in OneAssetOption from private to protected
//		GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess) stochasticProcess;
		
//        boost::shared_ptr<GeneralizedBlackScholesProcess> process =
//            boost::dynamic_pointer_cast<GeneralizedBlackScholesProcess>(
//                                                          stochasticProcess_);

        int i;
        Date settlement = bond.settlementDate();
//        DayCounter dayCounter = process->riskFreeRate()->dayCounter();
//
//        moreArgs->stoppingTimes = std::vector<Time>(exercise_->dates().size());
//        for (i=0; i<exercise_->dates().size(); i++) {
//            moreArgs->stoppingTimes[i] =
//                dayCounter.yearFraction(settlement, exercise_->date(i));
//        }
//
//        Size n = callability_.size();
//        moreArgs->callabilityTimes.clear();
//        moreArgs->callabilityTypes.clear();
//        moreArgs->callabilityPrices.clear();
//        moreArgs->callabilityTriggers.clear();
//        moreArgs->callabilityTimes.reserve(n);
//        moreArgs->callabilityTypes.reserve(n);
//        moreArgs->callabilityPrices.reserve(n);
//        moreArgs->callabilityTriggers.reserve(n);
//        for (i=0; i<n; i++) {
//            if (!callability_[i]->hasOccurred(settlement)) {
//                moreArgs->callabilityTypes.push_back(callability_[i]->type());
//                moreArgs->callabilityTimes.push_back(
//                             dayCounter.yearFraction(settlement,
//                                                     callability_[i]->date()));
//                moreArgs->callabilityPrices.push_back(
//                                            callability_[i]->price().amount());
//                if (callability_[i]->price().type() ==
//                                                    Callability::Price::Clean)
//                    moreArgs->callabilityPrices.back() +=
//                        bond_->accruedAmount(callability_[i]->date());
//                boost::shared_ptr<SoftCallability> softCall =
//                    boost::dynamic_pointer_cast<SoftCallability>(
//                                                             callability_[i]);
//                if (softCall)
//                    moreArgs->callabilityTriggers.push_back(
//                                                         softCall->trigger());
//                else
//                    moreArgs->callabilityTriggers.push_back(Null<Real>());
//            }
//        }
//
//        const Leg& cashflows =
//                                                           bond_->cashflows();
//        moreArgs->couponTimes.clear();
//        moreArgs->couponAmounts.clear();
//        for (i=0; i<cashflows.size()-1; i++) {
//            if (!cashflows[i]->hasOccurred(settlement)) {
//                moreArgs->couponTimes.push_back(
//                    dayCounter.yearFraction(settlement,cashflows[i]->date()));
//                moreArgs->couponAmounts.push_back(cashflows[i]->amount());
//            }
//        }
//
//        moreArgs->dividends.clear();
//        moreArgs->dividendTimes.clear();
//        for (i=0; i<dividends_.size(); i++) {
//            if (!dividends_[i]->hasOccurred(settlement)) {
//                moreArgs->dividends.push_back(dividends_[i]);
//                moreArgs->dividendTimes.push_back(
//                              dayCounter.yearFraction(settlement,
//                                                      dividends_[i]->date()));
//            }
//        }
//

        moreArgs.creditSpread = creditSpread;
        moreArgs.dayCounter = dayCounter;
        moreArgs.issueDate = issueDate;
        moreArgs.settlementDate = settlement;
        moreArgs.settlementDays = settlementDays;
        moreArgs.redemption = redemption;
		
		
	}

        
}
