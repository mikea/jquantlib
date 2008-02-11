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

package org.jquantlib.util;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleStack;


/**
 * This class implements a simple RPN calculator.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Reverse_Polish_notation">Reverse Polish Notation</a>
 * 
 * @author Richard Gomes
 */
public class RPN {

	private DoubleStack stack;
	
	public RPN() {
		this.stack = new DoubleArrayList();
	}
	
	/**
	 * This method implements the calculation performed by a RPN calculator.
	 * 
	 * @param objs is a list of arguments composed only by {@link Real}, {@link Double} and characters.
	 * It is responsibility of the caller to convert any other value to these types.
	 *  
	 * @return a {@link Real} which represents the result of the calculation
	 */
	public double expr(Object...objs) {
		if (objs==null) return 0.0;
		((DoubleList)stack).clear();
		for (int i=0; i<objs.length; i++) {
            final Object obj = objs[i];
            if (obj instanceof Double) {
            	stack.push( (Double)obj );
            } else if (obj instanceof Character) { 
                final double a = stack.pop();
                final double b = stack.pop();
                final double r;
                char c = ((Character)obj).charValue(); 
            	switch (c) {
	                case '+': r = a + b; break;
	                case '-': r = a - b; break;
	                case '*': r = a * b; break;
	                case '/': r = a / b; break;
	                default : throw new IllegalArgumentException(obj.toString());
            	}
                stack.push(r);
            } else {
            	throw new ClassCastException(obj.toString());
            }
		}
		return stack.pop();
	}

}
