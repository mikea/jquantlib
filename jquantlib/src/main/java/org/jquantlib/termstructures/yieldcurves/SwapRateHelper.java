/*
 Copyright (C) 2008 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.termstructures.yieldcurves;


// FIXME: move to org.jquantlib.termstructures.yieldcurves


import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;

/**
 * @author Srinivas Hasti
 *
 */
//TODO: Finish
public class SwapRateHelper extends RelativeDateRateHelper {
	
    private Period tenor;
    private Calendar calendar;
    private BusinessDayConvention fixedConvention;
    private Frequency fixedFrequency;
    private DayCounter fixedDayCount;
    private IborIndex iborIndex;
   // VanillaSwap swap;
    private RelinkableHandle<YieldTermStructure> termStructureHandle;
    private Handle<Quote> spread;
    private Period fwdStart;
    
//public SwapRateHelper(final Handle<Quote> rate,
//            final SwapIndex swapIndex,
//            final Handle<Quote> spread = Handle<Quote>(),
//            final Period fwdStart = 0*Days) {
//	// TODO: Finish
//}
//            
//public SwapRateHelper(final Handle<Quote> rate,
//            final Period tenor,
//            final Calendar calendar,
//            // fixed leg
//            Frequency fixedFrequency,
//            BusinessDayConvention fixedConvention,
//            final DayCounter fixedDayCount,
//            // floating leg
//            final IborIndex iborIndex,
//            final Handle<Quote> spread = Handle<Quote>(),
//            final Period fwdStart = 0*Days) {
//	// TODO: Finish
//}
//            
//public SwapRateHelper(Rate rate,
//            final Period tenor,
//            final Calendar calendar,
//            // fixed leg
//            Frequency fixedFrequency,
//            BusinessDayConvention fixedConvention,
//            final DayCounter fixedDayCount,
//            // floating leg
//            final IborIndex iborIndex,
//            final Handle<Quote> spread = Handle<Quote>(),
//            final Period fwdStart = 0*Days) {
//	// TODO: Finish
//}
//            
//public SwapRateHelper(Rate rate,
//            final SwapIndex swapIndex,
//            final Handle<Quote> spread = Handle<Quote>(),
//            final Period fwdStart = 0*Days) {
//	// TODO: Finish
//}

    
	public SwapRateHelper(
			final double d, 
			final Period tenor, 
			final Calendar calendar,
			final BusinessDayConvention fixedConvention, 
			final Frequency fixedFrequency,
			final DayCounter fixedDayCount, 
			final IborIndex iborIndex,
			final RelinkableHandle<YieldTermStructure> termStructureHandle,
			final Handle<Quote> spread, 
			final Period fwdStart) {
		super(d);
		this.tenor = tenor;
		this.calendar = calendar;
		this.fixedConvention = fixedConvention;
		this.fixedFrequency = fixedFrequency;
		this.fixedDayCount = fixedDayCount;
		this.iborIndex = iborIndex;
		this.termStructureHandle = termStructureHandle;
		this.spread = spread;
		this.fwdStart = fwdStart;
	}

	/* (non-Javadoc)
	 * @see org.jquantlib.termstructures.yield.RelativeDateRateHelper#initializeDates()
	 */
	@Override
	protected void initializeDates() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jquantlib.termstructures.BootstrapHelper#getImpliedQuote()
	 */
	@Override
	public double getImpliedQuote() {
		// TODO Auto-generated method stub
		return 0;
	}

}
