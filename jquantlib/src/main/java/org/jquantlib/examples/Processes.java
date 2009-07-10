/*
 Copyright (C) 2009 Apratim Rajendra
 
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

package org.jquantlib.examples;

import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;
import org.jquantlib.math.distributions.NormalDistribution;
import org.jquantlib.processes.EulerDiscretization;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVarianceTermStructure;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackVarianceCurve;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.calendars.UnitedStates;
import org.jquantlib.time.calendars.UnitedStates.Market;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.StopClock;

/**
 * This class explores StochasticProcess1D(GeneralizedBlackScholesProcess)/LinearDiscretization(EulerDiscretization)
 * 
 * @author Apratim Rajendra
 *
 */
public class Processes {
	
	public static void main(String args[]){
		
		System.out.println("\n\n::::: "+Processes.class.getSimpleName()+" :::::");
		
		StopClock clock = new StopClock(); 
		clock.startClock();
		
		System.out.println("//============================StochasticProcess1D/LinearDiscretization=========================");
		
		//Creating stock quote handle
		SimpleQuote stockQuote = new SimpleQuote(5.6);
		RelinkableHandle<Quote>  handleToStockQuote = new RelinkableHandle<Quote>(stockQuote);
		
		//Creating black volatility term structure
		Date today = DateFactory.getFactory().getTodaysDate();
		
		//Following is the time axis
		Date[] dates = {today.getDateAfter(10),today.getDateAfter(15),today.getDateAfter(20),today.getDateAfter(25),today.getDateAfter(30),today.getDateAfter(40)};
		
		//Following is the volatility axis
		double[] volatilities = {0.1,0.2,0.3,0.4,0.5,0.6};
		
		//Following is the curve
		BlackVarianceTermStructure varianceCurve = new BlackVarianceCurve(today,dates,volatilities,Actual365Fixed.getDayCounter(),false);
		((BlackVarianceCurve)varianceCurve).setInterpolation();
		
		//Dividend termstructure
		SimpleQuote dividendQuote = new SimpleQuote(0.3);
		RelinkableHandle<Quote>  handleToInterestRateQuote = new RelinkableHandle<Quote>(dividendQuote);
		YieldTermStructure dividendTermStructure = new FlatForward(2,UnitedStates.getCalendar(Market.NYSE),handleToInterestRateQuote,Actual365Fixed.getDayCounter(),Compounding.CONTINUOUS,Frequency.DAILY);
	
		//Risk free term structure
		SimpleQuote riskFreeRateQuote = new SimpleQuote(0.3);
		RelinkableHandle<Quote>  handleToRiskFreeRateQuote = new RelinkableHandle<Quote>(riskFreeRateQuote);
		YieldTermStructure riskFreeTermStructure = new FlatForward(2,UnitedStates.getCalendar(Market.NYSE),handleToRiskFreeRateQuote,Actual365Fixed.getDayCounter(),Compounding.CONTINUOUS,Frequency.DAILY);
	
		//Creating the process
		StochasticProcess1D process = new GeneralizedBlackScholesProcess(handleToStockQuote,new RelinkableHandle<YieldTermStructure>(dividendTermStructure),new RelinkableHandle<YieldTermStructure>(riskFreeTermStructure),new RelinkableHandle<BlackVolTermStructure>(varianceCurve),new EulerDiscretization()); 
		
		//Calculating the drift of the stochastic process after time = 18th day from today with value of the stock as specified from the quote
		//The drift = (riskFreeForwardRate - dividendForwardRate) - (Variance/2)
		System.out.println("The drift of the process after time = 18th day from today with value of the stock as specified from the quote = "+process.drift(process.getTime(today.getDateAfter(18)), handleToStockQuote.getLink().evaluate()));
		
		//Calculating the diffusion of the process after time = 18th day from today with value of the stock as specified from the quote
		//The diffusion = volatiltiy of the stochastic process
		System.out.println("The diffusion of the process after time = 18th day from today with value of the stock as specified from the quote = "+process.diffusion(process.getTime(today.getDateAfter(18)), handleToStockQuote.getLink().evaluate()));
		
		//Calulating the standard deviation of the process after time = 18th day from today with value of the stock as specified from the quote
		//The standard deviation = volatility*sqrt(dt)
		System.out.println("The stdDeviation of the process after time = 18th day from today with value of the stock as specified from the quote = "+process.stdDeviation(process.getTime(today.getDateAfter(18)), handleToStockQuote.getLink().evaluate(), 0.01));
		
		//Calulating the variance of the process after time = 18th day from today with value of the stock as specified from the quote
		//The variance = volatility*volatility*dt
		System.out.println("The variance of the process after time = 18th day from today with value of the stock as specified from the quote = "+process.variance(process.getTime(today.getDateAfter(18)), handleToStockQuote.getLink().evaluate(), 0.01));
		
		//Calulating the expected value of the stock quote after time = 18th day from today with the current value of the stock as specified from the quote
		//The expectedValue = intialValue*exp(drift*dt)-----can be obtained by integrating----->dx/x= drift*dt  
		System.out.println("Expected value = "+process.expectation(process.getTime(today.getDateAfter(18)), handleToStockQuote.getLink().evaluate(), 0.01));
		
		//Calulating the exact value of the stock quote after time = 18th day from today with the current value of the stock as specified from the quote
		//The exact value = intialValue*exp(drift*dt)*exp(volatility*sqrt(dt))-----can be obtained by integrating----->dx/x= drift*dt+volatility*sqrt(dt)
		System.out.println("Exact value = "+process.evolve(process.getTime(today.getDateAfter(18)), 6.7, .001, new NormalDistribution().evaluate(Math.random())));
		
		//Calculating the drift of the stochastic process after time = 18th day from today with value of the stock as specified from the quote
		//The drift = (riskFreeForwardRate - dividendForwardRate) - (Variance/2)
		Array drift = process.drift(process.getTime(today.getDateAfter(18)), new Array().fill(5.6));
		System.out.println("The drift of the process after time = 18th day from today with value of the stock as specified from the quote");
		
		//Calculating the diffusion of the process after time = 18th day from today with value of the stock as specified from the quote
		//The diffusion = volatiltiy of the stochastic process
		Matrix diffusion = process.diffusion(process.getTime(today.getDateAfter(18)), new Array().fill(5.6));
		System.out.println("The diffusion of the process after time = 18th day from today with value of the stock as specified from the quote");
	
		//Calulating the standard deviation of the process after time = 18th day from today with value of the stock as specified from the quote
		//The standard deviation = volatility*sqrt(dt)
		Matrix stdDeviation = process.stdDeviation(process.getTime(today.getDateAfter(18)), new Array().fill(5.6), 0.01);
		System.out.println("The stdDeviation of the process after time = 18th day from today with value of the stock as specified from the quote");
		
		//Calulating the expected value of the stock quote after time = 18th day from today with the current value of the stock as specified from the quote
		//The expectedValue = intialValue*exp(drift*dt)-----can be obtained by integrating----->dx/x= drift*dt  
		Array expectation = process.expectation(process.getTime(today.getDateAfter(18)), new Array().fill(5.6), 0.01);
		System.out.println("Expected value = "+expectation.first());	
		
		//Calulating the exact value of the stock quote after time = 18th day from today with the current value of the stock as specified from the quote
		//The exact value = intialValue*exp(drift*dt)*exp(volatility*sqrt(dt))-----can be obtained by integrating----->dx/x= drift*dt+volatility*sqrt(dt)
		Array evolve = process.evolve(process.getTime(today.getDateAfter(18)), new Array().fill(6.7), .001, new Array().fill(new NormalDistribution().evaluate(Math.random()) ));
		System.out.println("Exact value = "+evolve.first());	
		
		//Calculating covariance of the process
		Matrix covariance = process.covariance(process.getTime(today.getDateAfter(18)),  new Array().fill(5.6), 0.01);
		System.out.println("Covariance = "+covariance.get(0, 0));			
			
		clock.stopClock(); 
		clock.log(); 
	}

}
