package org.jquantlib.examples;

import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.termstructures.yieldcurves.ForwardRateStructure;
import org.jquantlib.termstructures.yieldcurves.ForwardSpreadedTermStructure;
import org.jquantlib.termstructures.yieldcurves.ImpliedTermStructure;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.calendars.UnitedStates;
import org.jquantlib.time.calendars.UnitedStates.Market;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.StopClock;

/**
 * This class explores the functionalities provided by yield termstructures.The different types
 * of yield termstructures covered in this class are as shown below-
 * (1)FlatForward
 * (2)ForwardSpreadedTermStructure
 * (3)ImpliedTermStructure
 * (4)InterpolatedDiscountCurve
 * (5)InterpolatedForwardCurve
 * (6)InterpolatedZeroCurve
 * (7)PiecewiseYieldCurve
 * (8)PiecewiseYieldDiscountCurve
 * (9)ZeroSpreadedTermStructure
 *  
 * @author Apratim Rajendra
 *
 */
public class YieldCurveTermStructures {
	
	public static void main(String args[]){
		
		System.out.println("\n\n::::: "+YieldCurveTermStructures.class.getSimpleName()+" :::::");

		StopClock clock = new StopClock();
		clock.startClock();
		
		System.out.println("//==========================================FlatForward termstructure===================");
		SimpleQuote interestRateQuote = new SimpleQuote(0.3);
		RelinkableHandle<Quote>  handleToInterestRateQuote = new RelinkableHandle<Quote>(interestRateQuote);
		YieldTermStructure flatforward = new FlatForward(2,UnitedStates.getCalendar(Market.NYSE),handleToInterestRateQuote,Actual365Fixed.getDayCounter(),Compounding.CONTINUOUS,Frequency.DAILY);
	
		//Calculating discount factor
		System.out.println("The discount factor for the date 30 days from today is = "+flatforward.discount(DateFactory.getFactory().getTodaysDate().getDateAfter(30)));
		
		//Calculating forward rate
		System.out.println("The forward rate between the date 30 days from today to 50 days from today is = "+flatforward.forwardRate(DateFactory.getFactory().getTodaysDate().getDateAfter(30), DateFactory.getFactory().getTodaysDate().getDateAfter(50), Actual365Fixed.getDayCounter(),Compounding.CONTINUOUS).rate());   
	       
		//Calculating parRate for the dates as shown below-
		Date[] dates = {DateFactory.getFactory().getTodaysDate().getDateAfter(10),DateFactory.getFactory().getTodaysDate().getDateAfter(20),DateFactory.getFactory().getTodaysDate().getDateAfter(30),DateFactory.getFactory().getTodaysDate().getDateAfter(40),DateFactory.getFactory().getTodaysDate().getDateAfter(50)};
		System.out.println("The par rate for the bond having coupon dates as shown above is = "+flatforward.parRate(dates, Frequency.SEMI_ANNUAL, false));       
			
		//Calculating zero rate
		System.out.println("The zero rate for the bond having coupon date as 10 days from today = "+flatforward.zeroRate(DateFactory.getFactory().getTodaysDate().getDateAfter(10), Actual365Fixed.getDayCounter(), Compounding.CONTINUOUS).rate());
		
		System.out.println("//==========================================ForwardSpreadedTermStructure==================");
		//As the name suggests this termstructure adds a spread to the forward rates it calculates by getting the spread rate
		//from the spread rate quote
		SimpleQuote spreadRateQuote = new SimpleQuote(0.2);
		RelinkableHandle<Quote>  handleToSpreadRateQuote = new RelinkableHandle<Quote>(spreadRateQuote);

		ForwardRateStructure forwardSpreadedTermStructure = new ForwardSpreadedTermStructure(new RelinkableHandle<YieldTermStructure>(flatforward),handleToSpreadRateQuote);
		
		//Calculating discount factor.This termstructure adds the spread as specified by the spread quote and then calculates the discount.
		System.out.println("The discount factor for the date 30 days from today is = "+forwardSpreadedTermStructure.discount(DateFactory.getFactory().getTodaysDate().getDateAfter(30)));
		
		//Calculating forward rate.This termstructure adds the spread as specified by the spread quote and then calculates the discount.
		System.out.println("The forward rate between the date 30 days from today to 50 days from today is = "+forwardSpreadedTermStructure.forwardRate(DateFactory.getFactory().getTodaysDate().getDateAfter(30), DateFactory.getFactory().getTodaysDate().getDateAfter(50), Actual365Fixed.getDayCounter(),Compounding.CONTINUOUS).rate());   
	       
		//Calculating parRate for the dates(as used in the FlatForward case) as shown below-
		System.out.println("The par rate for the bond having coupon dates as shown above is = "+forwardSpreadedTermStructure.parRate(dates, Frequency.SEMI_ANNUAL, false));       
			
		//Calculating zero rate.This termstructure adds the spread as specified by the spread quote and then calculates the discount.
		System.out.println("The zero rate for the bond having coupon date as 10 days from today = "+forwardSpreadedTermStructure.zeroRate(DateFactory.getFactory().getTodaysDate().getDateAfter(10), Actual365Fixed.getDayCounter(), Compounding.CONTINUOUS).rate());
		
		//===========================================ImpliedTermStructure============================
		
		//As the name suggests the implied termstructure holds a reference to an underlying tremstructure and gives the same calulated values
		//as the underlying termstructure.Here the FlatForward termstructure instantiated right at the top has been taken as an underlying.
		YieldTermStructure impliedTermStructure = new ImpliedTermStructure(new RelinkableHandle<YieldTermStructure>(flatforward),DateFactory.getFactory().getTodaysDate());
		//TODO as the code has to be updated for the implied term structure.
		
		//===========================================InterpolatedDiscountCurve=======================
		//TODO as the code has to be updated for the InterpolatedDiscountCurve
		
		//===========================================InterpolatedForwardCurve========================
		//TODO as the code has to be updated for the InterpolatedForwardCurve
		
		//===========================================InterpolatedZeroCurve===========================
		//TODO as the code has to be updated for the InterpolatedZeroCurve
		
		//===========================================PiecewiseYieldCurve=============================
		//TODO as the code has to be updated for the PiecewiseYieldCurve
		
		//===========================================PiecewiseYieldDiscountCurve=====================
		//TODO as the code has to be updated for the PiecewiseYieldDiscountCurve
		
		//===========================================ZeroSpreadedTermStructure=======================		
		//TODO as the code has to be updated for the PiecewiseYieldDiscountCurve
		
		
		
		clock.stopClock();
		clock.log();
		
	}

}
