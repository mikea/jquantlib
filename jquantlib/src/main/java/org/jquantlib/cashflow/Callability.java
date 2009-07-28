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

/**
 * @author Daniel Kong
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class Callability extends Event {

	public enum Type{CALL, PUT}

	private final Price price;
	private final Type type;
	private final Date date;

	public Callability(final Price price, final Type type, final Date date){
        this.price=price;
        this.type=type;
        this.date=date;
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
	}

	@Override
	public Date date() {
		return date;
	}

	public Price getPrice(){
		return price;
	}

	public Type getType(){
		return type;
	}

	public static class Price {

		public enum Type{ DIRTY, CLEAN }

		private final double amount;
		private Type type;

        public Price() {
            amount = 0.0;
        }

        public Price(final double amount, final Type type) {
            this.amount = amount;
            this.type = type;
        }

        public double amount() {
            assert !Double.isNaN(amount) : "no amount given";
            return amount;
        }

        public Type type() {
            return type;
        }

	}

}
