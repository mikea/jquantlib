/*
 Copyright (C) 2007 Richard Gomes

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

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib.instruments;

import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;

/**
 * Simple stock class
 * 
 * @author Richard Gomes
 */
public class Stock extends Instrument {
	
	//
    // private final fields
    //
    
    private final Handle<Quote> quote;
	
	//
    // public constructors
    //
    
    public Stock(final Handle<Quote> quote) {
		if (quote == null) throw new NullPointerException(); // FIXME: code review: should throw here?
		this.quote = quote;
		this.quote.addObserver(this);
	}
    
	//
	// overrides Instrument
	//
	
	@Override
	public boolean isExpired() /* @ReadOnly */ { return false; }
    
	//
    // overrides LazyObject
    //
    
    @Override
    protected void performCalculations() /* @ReadOnly */ {
		if (quote.isEmpty()) throw new NullPointerException("null quote set");
		NPV = quote.getLink().doubleValue();
	}
}
