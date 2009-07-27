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

import java.util.ArrayList;
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
    private List<CashFlow> cashFlows;
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
            final List<CashFlow> cashFlows,
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
    	this.cashFlows = cashFlows;
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

		GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess) stochasticProcess;

        Date settlement = bond.settlementDate();
        DayCounter dayCounter = process.riskFreeRate().getLink().dayCounter();

        moreArgs.stoppingTimes = new ArrayList<Double>();
        for (int i=0; i<exercise.size(); i++) {
            moreArgs.stoppingTimes.add(dayCounter.yearFraction(settlement, exercise.date(i)));
        }

        int n = callability.size();
        moreArgs.callabilityTimes.clear();
        moreArgs.callabilityTypes.clear();
        moreArgs.callabilityPrices.clear();
        moreArgs.callabilityTriggers.clear();

        for (int i=0; i<n; i++) {
            if (!callability.get(i).hasOccurred(settlement)) {          	
                moreArgs.callabilityTypes.add(callability.get(i).getType());
                moreArgs.callabilityTimes.add(dayCounter.yearFraction(settlement, callability.get(i).date()));
                
                double d = callability.get(i).getPrice().amount();
                if (callability.get(i).getPrice().type() == Callability.Price.Type.CLEAN){               	
                	d += bond.accruedAmount(callability.get(i).date());
                }
                moreArgs.callabilityPrices.add(d);
                
                SoftCallability softCall = (SoftCallability)callability.get(i);
				if(softCall != null){
                    moreArgs.callabilityTriggers.add(softCall.getTrigger());
				}else{
					moreArgs.callabilityTriggers.add(0.0);
				}
            }
        }

        final List<CashFlow> cashFlows = bond.getCashFlows();

        moreArgs.couponTimes.clear();
        moreArgs.couponAmounts.clear();
        for (int i=0; i<cashFlows.size()-1; i++) {
            if (!cashFlows.get(i).hasOccurred(settlement)) {
                moreArgs.couponTimes.add(dayCounter.yearFraction(settlement,cashFlows.get(i).date()));
                moreArgs.couponAmounts.add(cashFlows.get(i).amount());
            }
        }

        moreArgs.dividends.clear();
        moreArgs.dividendTimes.clear();
        for (int i=0; i<dividends.size(); i++) {
            if (!dividends.get(i).hasOccurred(settlement)) {
                moreArgs.dividends.add(dividends.get(i));
                moreArgs.dividendTimes.add(dayCounter.yearFraction(settlement,dividends.get(i).date()));
            }
        }

        moreArgs.creditSpread = creditSpread;
        moreArgs.dayCounter = dayCounter;
        moreArgs.issueDate = issueDate;
        moreArgs.settlementDate = settlement;
        moreArgs.settlementDays = settlementDays;
        moreArgs.redemption = redemption;
				
	}
       
}
