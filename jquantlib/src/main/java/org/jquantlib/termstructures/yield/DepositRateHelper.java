/*
 Copyright (C) 2007 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */
package org.jquantlib.termstructures.yield;


//FIXME: move to org.jquantlib.termstructures.yieldcurves



import java.util.List;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.number.Rate;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.util.Date;
import org.jquantlib.util.Observer;

/**
 * @author shasti
 * 
 */
// TODO:
public class DepositRateHelper<T extends TermStructure> extends RelativeDateRateHelper<T> {

	private Date fixingDate;
	private IborIndex iborIndex;
	private RelinkableHandle<YieldTermStructure> termStructureHandle;

	  // TODO
	  public DepositRateHelper( Handle<Quote> rate,
                Period tenor,
               int fixingDays,
               Calendar calendar,
               BusinessDayConvention convention,
               boolean endOfMonth,
               DayCounter dayCounter){
		  super(0); //TODO
		   
	   }
	  
// TODO
   public DepositRateHelper(Rate rate,
               Period tenor,
               int fixingDays,
               Calendar calendar,
               BusinessDayConvention convention,
               boolean endOfMonth,
               DayCounter dayCounter){
	   super(0); //TODO
   }

   // TODO
   public DepositRateHelper( Handle<Quote> rate,
               IborIndex iborIndex){
	   super(0); //TODO
	
}
   // TODO
   public DepositRateHelper(double rate,
               IborIndex iborIndex){
	   super(0); //TODO
	   
   }

    // TODO: remove following constructors when above ported
	public DepositRateHelper(double d) {
		super(d);
		// TODO Auto-generated constructor stub
	}

	public DepositRateHelper(Handle<Quote> quote, T termStructure,
			Date earliestDate, Date latestDate) {
		super(quote, termStructure, earliestDate, latestDate);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.termstructures.yield.RelativeDateRateHelper#initializeDates()
	 */
	@Override
	protected void initializeDates() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.termstructures.BootstrapHelper#getImpliedQuote()
	 */
	@Override
	public double getImpliedQuote() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.util.Observable#addObserver(org.jquantlib.util.Observer)
	 */
	@Override
	public void addObserver(Observer observer) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.util.Observable#countObservers()
	 */
	@Override
	public int countObservers() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.util.Observable#deleteObserver(org.jquantlib.util.Observer)
	 */
	@Override
	public void deleteObserver(Observer observer) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.util.Observable#deleteObservers()
	 */
	@Override
	public void deleteObservers() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.util.Observable#getObservers()
	 */
	@Override
	public List<Observer> getObservers() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.util.Observable#notifyObservers()
	 */
	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.util.Observable#notifyObservers(java.lang.Object)
	 */
	@Override
	public void notifyObservers(Object arg) {
		// TODO Auto-generated method stub

	}

}
