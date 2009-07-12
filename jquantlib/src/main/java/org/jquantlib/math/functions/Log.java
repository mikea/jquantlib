/*
 Copyright (C) 2008 Srinivas Hasti

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
package org.jquantlib.math.functions;

import org.jquantlib.math.UnaryFunctionDouble;

/**
 * A log(n) function
 * 
 * @author Srinivas Hasti
 */
public final class Log implements UnaryFunctionDouble {

    // TODO: get rid of statics and singletons
	private static Log instance = new Log();

	private Log() {
	}

    // TODO: get rid of statics and singletons
	public static Log getInstance() {
		return instance;
	}


	//
    // implements UnaryFunctionDouble
    //
    
	@Override
	public double evaluate(double a) {
		return Math.log(a);
	}

}
