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
package org.jquantlib.cashflow;


/**
 * @author Srinivas Hasti
 * 
 */
public abstract class CashFlow extends Event implements Comparable<CashFlow> {

	/**
	 * Returns amount of the cash flow. The amount is not discounted, i.e., it
	 * is the actual amount paid at the cash flow date.
	 * 
	 * @return
	 */
	public abstract double getAmount();

	@Override
	public int compareTo(CashFlow c2) {
		   if (date().lt(c2.date()))
               return -1;

           if (date().equals(c2.date())) {
               try {
                   if (getAmount()<c2.getAmount())
                       return -1;
               } catch (Exception e) {
                       return -1;
               }
               return 0;
           }

           return 1;
	}
	
	

}
