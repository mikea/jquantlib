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
package org.jquantlib.pricingengines.arguments;

import java.util.List;

import org.jquantlib.cashflow.Callability;
import org.jquantlib.cashflow.Dividend;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.lang.annotation.Time;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.util.Date;

/**
 *
 * @author Daniel Kong
 */
//TODO: Work in progress
public class ConvertibleBondOptionArguments extends OneAssetStrikedOptionArguments {

    //
    // public fields
    //
    
    // FIXME: public fields here is a bad design technique :(

    public double conversionRatio;
    public Handle<Quote> creditSpread;
    public List<Dividend> dividends;
    public /*@Time*/ List<Double> dividendTimes;
    public /*@Time*/ List<Double> callabilityTimes;
    public List<Callability.Type> callabilityTypes;
    public List<Double> callabilityPrices;
    public List<Double> callabilityTriggers;
    public /*@Time*/ List<Double> couponTimes;
    public List<Double> couponAmounts;
    public DayCounter dayCounter;
    public Date issueDate;
    public Date settlementDate;
    public int settlementDays;
    public double redemption;
    
	
    //
    // public constructors
    //
    
    public ConvertibleBondOptionArguments(){
		conversionRatio = 0.0;
		settlementDays = 0;
		redemption = 0.0;
	}
	
	
    //
    // public methods
    //
    
    @Override
	public void validate() /*@ReadOnly*/ {
		super.validate();
	}
    
}
