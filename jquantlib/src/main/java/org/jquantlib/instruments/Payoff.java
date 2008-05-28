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

/*
 Copyright (C) 2003, 2006 Ferdinando Ametrano
 Copyright (C) 2006 StatPro Italia srl

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

import org.jquantlib.util.Visitable;
import org.jquantlib.util.Visitor;

/**
 * Abstract base class for option payoffs
 */
public abstract class Payoff implements Visitable<Object> {

	protected static final String UNKNOWN_OPTION_TYPE = "unknown option type";

	// FIXME code review

	// This method is not needed as this.getClass().getName() can be used
	// virtual std::string name() const = 0;
	
	// This method is not needed as this.toString(0 can be used
	// virtual std::string description() const = 0;
	

	protected abstract/* @Price */double valueOf(/* @Price */double price);


	//
	// implements Visitable pattern
	//
	
	@Override
	public final void accept(final Visitor<Object> v) {
		if (v != null) {
			v.visit(this);
		} else {
			throw new NullPointerException("null payoff visitor");
		}
	}

}
