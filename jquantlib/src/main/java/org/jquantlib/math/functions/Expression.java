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

import java.util.List;

import org.jquantlib.math.UnaryFunctionDouble;

/**
 * Processes a sequence of functions
 * 
 * @author Ueli Hofstetter
 * @author Richard Gomes
 */
public class Expression implements UnaryFunctionDouble {

    private final List<UnaryFunctionDouble> list;
    
    public Expression(final List<UnaryFunctionDouble> list) {
        this.list = list;
    }
    
    
	//
    // implements UnaryFunctionDouble
    //
    
	@Override
	public double evaluate(double a) {
        double result = a;
        for (int i = 0; i<list.size(); i++) {
            result = list.get(i).evaluate(result);
        }
        return result;
	}

}
