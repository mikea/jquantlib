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

import org.jquantlib.QL;
import org.jquantlib.cashflow.Callability;
import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.cashflow.Dividend;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.lang.reflect.ReflectConstants;
import org.jquantlib.math.Constants;
import org.jquantlib.pricingengines.GenericEngine;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.time.Date;
import org.jquantlib.time.Schedule;

/**
 *
 * @author Daniel Kong
 */
//TODO: Work in progress
public class ConvertibleBondOption extends OneAssetOption {

    private final ConvertibleBond bond;
    private final double conversionRatio;
    private final List<Callability> callability;
    private final List<Dividend>  dividends;
    private final Handle<Quote> creditSpread;
    private final List<CashFlow> cashFlows;
    private final DayCounter dayCounter;
    private final Date issueDate;
    private final Schedule schedule;
    private final int settlementDays;
    private final double redemption;

    public ConvertibleBondOption(
            final ConvertibleBond bond,
            final Exercise exercise,
            final double conversionRatio,
            final List<Dividend> dividends,
            final List<Callability> callability,
            final Handle<Quote> creditSpread,
            final List<CashFlow> cashFlows,
            final DayCounter dayCounter,
            final Schedule schedule,
            final Date issueDate,
            final int settlementDays,
            final double redemption){
    	super(new PlainVanillaPayoff(Option.Type.CALL, bond.getFaceAmount()/100.0*redemption/conversionRatio),exercise);
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
    public void setupArguments(final PricingEngine.Arguments arguments) /* @ReadOnly */ {
		super.setupArguments(arguments);

		QL.require(ConvertibleBondOption.Arguments.class.isAssignableFrom(arguments.getClass()), ReflectConstants.WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
		final ConvertibleBondOption.ArgumentsImpl moreArgs = (ConvertibleBondOption.ArgumentsImpl)arguments;

		moreArgs.conversionRatio = conversionRatio;
        final Date settlement = bond.settlementDate();
        final int n = callability.size();
        moreArgs.callabilityTimes.clear();
        moreArgs.callabilityTypes.clear();
        moreArgs.callabilityPrices.clear();
        moreArgs.callabilityTriggers.clear();

        for (int i=0; i<n; i++) {
            if (!callability.get(i).hasOccurred(settlement)) {
                moreArgs.callabilityTypes.add(callability.get(i).getType());
                moreArgs.callabilityTimes.add(dayCounter.yearFraction(settlement, callability.get(i).date()));

                double d = callability.get(i).getPrice().amount();
                if (callability.get(i).getPrice().type() == Callability.Price.Type.Clean){
                	d += bond.accruedAmount(callability.get(i).date());
                }
                moreArgs.callabilityPrices.add(d);

                final SoftCallability softCall = (SoftCallability)callability.get(i);
				if(softCall != null){
                    moreArgs.callabilityTriggers.add(softCall.getTrigger());
				}else{
					moreArgs.callabilityTriggers.add(0.0);
				}
            }
        }

        final List<CashFlow> cashFlows = bond.cashflows();

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



    //
    // ????? inner interfaces
    //

    public interface Arguments extends OneAssetOption.Arguments { }

    public interface Results extends Instrument.Results, Option.Greeks, Option.MoreGreeks { }



    //
    // ????? inner classes
    //


    static public class ArgumentsImpl extends OneAssetOption.ArgumentsImpl {

        //
        // public fields
        //

        // FIXME: public fields here is a bad design technique :(

        public double conversionRatio;
        public Handle<Quote> creditSpread;
        public List<Dividend> dividends;
        public /*@Time*/ List<Double> dividendTimes;
        public List<Date> callabilityDates;
        public /*@Time*/ List<Double> callabilityTimes;
        public List<Callability.Type> callabilityTypes;
        public List<Double> callabilityPrices;
        public List<Double> callabilityTriggers;
        public /*@Time*/ List<Double> couponTimes;
        public List<Date> couponDates;
        public List<Double> couponAmounts;
        public DayCounter dayCounter;
        public Date issueDate;
        public Date settlementDate;
        public int settlementDays;
        public double redemption;


        //
        // public constructors
        //

        public ArgumentsImpl() {
            conversionRatio = Constants.NULL_REAL;
            settlementDays = Constants.NULL_INTEGER;
            redemption = Constants.NULL_REAL;
        }


        //
        // public methods
        //

        @Override
        public void validate() /*@ReadOnly*/ {
            super.validate();

            // TODO: message
            QL.require(!Double.isNaN(conversionRatio), "null conversion ratio");
            QL.require(conversionRatio > 0.0, "positive conversion ratio required");
            QL.require(!Double.isNaN(redemption), "null redemption");
            QL.require(redemption >= 0.0, "positive redemption required");
            QL.require(!settlementDate.isNull(), "null settlement date");
            QL.require(settlementDays != Constants.NULL_INTEGER, "null settlement days");
            QL.require(callabilityDates.size() == callabilityTypes.size(),    "different number of callability dates and types");
            QL.require(callabilityDates.size() == callabilityPrices.size(),   "different number of callability dates and prices");
            QL.require(callabilityDates.size() == callabilityTriggers.size(), "different number of callability dates and triggers");
            QL.require(couponDates.size() == couponAmounts.size(), "different number of coupon dates and amounts");
        }

    }


    static public class ResultsImpl extends OneAssetOption.ResultsImpl {

        @Override
        public void reset() /* @ReadOnly */ {
            super.reset();
        }

    }


    static public abstract class EngineImpl extends GenericEngine<ConvertibleBondOption.ArgumentsImpl, ConvertibleBondOption.ResultsImpl> {

        protected EngineImpl() {
            super(new ArgumentsImpl(), new ResultsImpl());
        }

    }

}
