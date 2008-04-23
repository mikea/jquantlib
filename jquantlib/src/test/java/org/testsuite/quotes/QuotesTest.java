/*
 Copyright (C) 2007 Richard Gomes

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

package org.testsuite.quotes;

import static org.junit.Assert.assertFalse;

import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.util.Observer;
import org.jquantlib.util.Utilities.Flag;
import org.junit.Test;


/**
 * @note Class Handle is deprecated and MUST NEVER be used
 * 
 * @author Richard Gomes
 */
public class QuotesTest {


	private double add10(final double x) { return x+10; }
	private double mul10(final double x) { return x*10; }
	private double sub10(final double x) { return x-10; }

	private double add(final double x, final double y) { return x+y; }
	private double mul(final double x, final double y) { return x*y; }
	private double sub(final double x, final double y) { return x-y; }

	@Test
	public void testObservable() {

	    System.out.println("Testing observability of quotes...");

	    SimpleQuote me = new SimpleQuote(0.0);
	    Flag f = new Flag();
	    me.addObserver(f);
	    me.setValue(3.14);

        assertFalse("Observer was not notified of quote change", !f.isUp());
	}

	@Test
	public void testObservableHandle() {

		System.out.println("Testing observability of quote handles...");

	    SimpleQuote me1 = new SimpleQuote(0.0);
	    Quote h = me1;
	    assertFalse("Should have zero observers", h.countObservers()!=0 );
	    
	    Flag f = new Flag();
	    h.addObserver(f);
	    assertFalse("Should have one observer", h.countObservers()!=1 );

	    me1.setValue(3.14);
        assertFalse("Observer was not notified of quote change", !f.isUp());

	    f.lower();
	    SimpleQuote me2 = new SimpleQuote(0.0);
	    
	    // h.linkTo(me2);
	    for (Observer observer : h.getObservers()) {
	      me2.addObserver(observer);
	    }
	    assertFalse("Should have one observer", me2.countObservers()!=1 );
	    me2.notifyObservers();
	    
        assertFalse("Observer was not notified of quote change", !f.isUp());

	}

//	@Test
//	public void testDerived() {
//
//		System.out.println("Testing derived quotes...");
//
//	    typedef Real (*unary_f)(Real);
//	    unary_f funcs[3] = { add10, mul10, sub10 };
//
//	    Quote me = new SimpleQuote(17.0);
//	    Handle<Quote> h = new Handle<Quote>(me);
//
//	    for (Integer i=0; i<3; i++) {
//	        DerivedQuote<unary_f> derived(h,funcs[i]);
//	        Real x = derived.value(),
//	             y = funcs[i](me->value());
//	        if (Math.abs(x-y) > 1.0e-10)
//	            assertFalse("derived quote yields " << x << "\n"
//	                       << "function result is " << y);
//	    }
//	}

//	@Test
//	public void testComposite() {
//
//		System.out.println("Testing composite quotes...");
//
//	    typedef Real (*binary_f)(Real,Real);
//	    binary_f funcs[3] = { add, mul, sub };
//
//	    Quote me1 = new SimpleQuote(12.0);
//	    Quote me2 = new SimpleQuote(13.0);
//	    
//	    Handle<Quote> h1 new Handle<Quote>(me1);
//	    Handle<Quote> h2 new Handle<Quote>(me2);
//
//	    for (Integer i=0; i<3; i++) {
//	        CompositeQuote<binary_f> composite(h1,h2,funcs[i]);
//	        Real x = composite.value(),
//	             y = funcs[i](me1->value(),me2->value());
//	        if (Math.abs(x-y) > 1.0e-10)
//	            assertFalse("composite quote yields " << x << "\n"
//	                       << "function result is " << y);
//	    }
//	}

//	@Test
//	public void testForwardValueQuoteAndImpliedStdevQuote(){
//		
//		System.out.println("Testing forward-value and implied-stdev quotes...");
//		
//	    Real forwardRate = .05;
//	    DayCounter dc = ActualActual();
//	    Calendar calendar = TARGET();
//	    SimpleQuote forwardQuote(new SimpleQuote(forwardRate));
//	    Quote forwardHandle(forwardQuote);
//	    Date evaluationDate = Settings::instance().evaluationDate();
//	    YieldTermStructure>yc (new FlatForward(
//	        evaluationDate, forwardHandle, dc));
//	    YieldTermStructure> ycHandle(yc);
//	    Period euriborTenor(1,Years);
//	    Index> euribor(new Euribor(euriborTenor, ycHandle));
//	    Date fixingDate = calendar.advance(evaluationDate, euriborTenor);
//	    ForwardValueQuote forwardValueQuote( new
//	        ForwardValueQuote(euribor, fixingDate));
//	    Rate forwardValue =  forwardValueQuote->value();
//	    Rate expectedForwardValue = euribor->fixing(fixingDate, true);
//	    // we test if the forward value given by the quote is consistent
//	    // with the one directly given by the index
//	    if (Math.abs(forwardValue-expectedForwardValue) > 1.0e-15)
//	        assertFalse("Foward Value Quote quote yields " << forwardValue << "\n"
//	                   << "expected result is " << expectedForwardValue);
//	    // then we test the observer/observable chain
//	    Flag f;
//	    f.registerWith(forwardValueQuote);
//	    forwardQuote->setValue(0.04);
//	    if (!f.isUp())
//	        assertFalse("Observer was not notified of quote change");
//
//	    // and we retest if the values are still matching
//	    forwardValue =  forwardValueQuote->value();
//	    expectedForwardValue = euribor->fixing(fixingDate, true);
//	    if (Math.abs(forwardValue-expectedForwardValue) > 1.0e-15)
//	        assertFalse("Foward Value Quote quote yields " << forwardValue << "\n"
//	                   << "expected result is " << expectedForwardValue);
//	    // we test the ImpliedStdevQuote class
//	    f.unregisterWith(forwardValueQuote);
//	    f.lower();
//	    Real price = 0.02;
//	    Rate strike = 0.04;
//	    Volatility guess = .15;
//	    Real accuracy = 1.0e-6;
//	    Option::Type optionType = Option::Call;
//	    SimpleQuote priceQuote(new SimpleQuote(price));
//	    Quote priceHandle(priceQuote);
//	    ImpliedStdDevQuote impliedStdevQuote(new
//	        ImpliedStdDevQuote(optionType, forwardHandle, priceHandle,
//	                           strike, guess, accuracy));
//	    Real impliedStdev = impliedStdevQuote->value();
//	    Real expectedImpliedStdev =
//	        blackFormulaImpliedStdDev(optionType, strike,
//	                                  forwardQuote->value(), price,
//	                                  1.0, guess, 1.0e-6);
//	    if (Math.abs(impliedStdev-expectedImpliedStdev) > 1.0e-15)
//	        assertFalse("impliedStdevQuote yields " << impliedStdev << "\n"
//	                << "expected result is " << expectedImpliedStdev);
//	    // then we test the observer/observable chain
//	    Quote quote = impliedStdevQuote;
//	    f.registerWith(quote);
//	    forwardQuote->setValue(0.05);
//	    if (!f.isUp())
//	        assertFalse("Observer was not notified of quote change");
//	    quote->value();
//	    f.lower();
//	    priceQuote->setValue(0.11);
//	    if (!f.isUp())
//	        assertFalse("Observer was not notified of quote change");
//
//	}
	

}
