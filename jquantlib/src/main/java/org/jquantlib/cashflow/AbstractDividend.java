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

package org.jquantlib.cashflow;

import org.jquantlib.util.Date;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * @author Daniel Kong
 */

public abstract class AbstractDividend extends CashFlow {
	
	protected Date date;
	
	public AbstractDividend (final Date date){
		super();
		this.date = date;
	}

	@Override
	protected Date getDate() {
		return date;
	}
	
	public abstract double getAmount(final double underlying);
	
	@Override
	public void accept(final TypedVisitor<Event> event) {
		Visitor<Event> event1 = (event!=null) ? event.getVisitor(this.getClass()) : null;
		if (event1 != null) {
			event1.visit(this);
		} else {
			super.accept(event);
		}
	}

}
