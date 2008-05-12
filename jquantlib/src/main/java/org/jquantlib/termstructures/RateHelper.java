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
package org.jquantlib.termstructures;

import java.util.List;

import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

/**
 * @author Srinivas Hasti
 *
 */
//TODO: Finish
public abstract class RateHelper<T extends TermStructure> implements Observer, Observable {

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}


	protected Handle<Quote> quote;
    protected T termStructure;
    protected Date earliestDate;
    protected Date latestDate;
     
	public RateHelper(final Handle<Quote> quote, final T termStructure, final Date earliestDate, final Date latestDate) {
		super();
		this.quote = quote;
		this.termStructure = termStructure;
		this.earliestDate = earliestDate;
		this.latestDate = latestDate;
		this.quote.addObserver(this);
	}
	
    public RateHelper(final Handle<Quote> quote) {
    	this.quote = quote;
    	this.quote.addObserver(this);
    	
    	// FIXME: termStructure_(0) {}
		// this.termStructure = new TermStructure(0);
    }

	public RateHelper(double quote) {
		this.quote = new Handle<Quote>(new SimpleQuote(quote));
    	
		// FIXME: termStructure_(0) {}
		// this.termStructure = new TermStructure(0); //  termStructure_(0) {}
	}

	public Date getEarliestDate() {
		return earliestDate;
	}

	public Date getLatestDate() {
		return latestDate;
	}

	public T getTermStructure() {
		return termStructure;
	}

	public void setTermStructure(T termStructure) {
		if (termStructure==null) throw new NullPointerException("null term structure given");
		this.termStructure = termStructure;
	}

	 public double getQuoteError(){
		 return quote.getLink().doubleValue()-getImpliedQuote();
	 }
	 
     public double getQuoteValue(){
    	 return quote.getLink().doubleValue();
     }
     
     public boolean quoteIsValid(){
    	 // quote_->isValid();
    	 return true; //TODO
     }
     
     public abstract double getImpliedQuote();
     
     
     /**
      * Implements multiple inheritance via delegate pattern to an inner class
      * 
      */
     private Observable delegatedObservable = new DefaultObservable(this);

     public void addObserver(Observer observer) {
         delegatedObservable.addObserver(observer);
     }

     public int countObservers() {
         return delegatedObservable.countObservers();
     }

     public void deleteObserver(Observer observer) {
         delegatedObservable.deleteObserver(observer);
     }

     public void notifyObservers() {
         delegatedObservable.notifyObservers();
     }

     public void notifyObservers(Object arg) {
         delegatedObservable.notifyObservers(arg);
     }

     public void deleteObservers() {
         delegatedObservable.deleteObservers();
     }

     public List<Observer> getObservers() {
         return delegatedObservable.getObservers();
     }
     
     
}
