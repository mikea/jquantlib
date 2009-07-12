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

import org.jquantlib.math.DoublePredicate;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * This class verifies a condition and if true, returns the evaluation of 
 * a function, otherwise returns Double.NaN.
 *
 * @author Ueli Hofstetter
 * @author Richard Gomes
 */
public final class Clipped implements UnaryFunctionDouble {

    private final DoublePredicate checker;
    private final UnaryFunctionDouble function;

    public Clipped(DoublePredicate checker, UnaryFunctionDouble function){
        this.checker = checker;
        this.function = function;
    }

    
    //
    // implements UnaryFunctionDouble
    //
    
	@Override
	public double evaluate(double a) {
        return checker.op(a) ? function.evaluate(a) : Double.NaN;
	}

}


