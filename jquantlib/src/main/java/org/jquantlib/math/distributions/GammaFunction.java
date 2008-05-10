/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.math.distributions;

/**
 * @author Richard Gomes
 * @author Dominik Holenstein
 */


public class GammaFunction {
	
	private static final double c1_ = 76.18009172947146;
    private static final double c2_ = -86.50532032941677;
    private static final double c3_ = 24.01409824083091;
    private static final double c4_ = -1.231739572450155;
    private static final double c5_ = 0.1208650973866179e-2;
    private static final double c6_ = -0.5395239384953e-5;
	    
	public double logValue(double x) {
        
		if (x<=0.0)
        	throw new ArithmeticException("positive argument required " + x);

        double temp = x + 5.5;
        temp -= (x + 0.5)*Math.log(temp);
        double ser=1.000000000190015;
        ser += c1_/(x + 1.0);
        ser += c2_/(x + 2.0);
        ser += c3_/(x + 3.0);
        ser += c4_/(x + 4.0);
        ser += c5_/(x + 5.0);
        ser += c6_/(x + 6.0);

        return -temp+Math.log(2.5066282746310005*ser/x);
    }
}
