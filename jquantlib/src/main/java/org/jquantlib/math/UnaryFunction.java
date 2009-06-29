/*
 Copyright (C) 2008 Aaron Roth

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

package org.jquantlib.math;

/**
 * Represents a function of one variable; y = f(x)
 * 
 * @author Aaron Roth
 * @author Richard Gomes
 */
//FIXME: code review :: this interface implies on boxing/unboxing and ideally should be removed.
// In particular, this interface is being used by Monte Carlo, which is still in development: It's not clear yet
// if this class is really needed. [Richard Gomes]
public interface UnaryFunction<X, Y> {

	/**
	 * Computes the value of the function; f(x)
	 * 
	 * @param x
	 * @return f(x)
	 */
	public Y evaluate(X x);
	
	//boolean isFailed() // TODO is error handling needed?
}
