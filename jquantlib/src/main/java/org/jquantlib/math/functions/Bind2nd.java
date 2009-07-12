/*
 Copyright (C) 2009 Ueli Hofstetter
 Copyright (C) 2009 Richard Gomes

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

import org.jquantlib.math.BinaryFunctionDouble;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * This method binds the 2nd argument of a binary function to a scalar value, effectively enabling
 * a binary function to be called in a context intended for a unary function.
 *
 * @author Ueli Hofstetter
 * @author Richard Gomes
 */
public final class Bind2nd implements UnaryFunctionDouble {

    private final BinaryFunctionDouble f;   // 1st argument
    private final double scalar;            // 2nd argument
    
	public Bind2nd(final BinaryFunctionDouble f, final double scalar) {
	    this.f = f;
        this.scalar = scalar;
	}

	
	//
    // implements UnaryFunctionDouble
    //
    
	@Override
	public double evaluate(double a) {
		return f.evaluate(a, scalar);
	}

}
