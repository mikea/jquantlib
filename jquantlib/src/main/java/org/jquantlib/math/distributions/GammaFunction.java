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

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.Constants;

/**
 * @author Richard Gomes
 * @author Dominik Holenstein
 */


public class GammaFunction implements UnaryFunctionDouble {

	private static final double c1_ = 76.18009172947146;
    private static final double c2_ = -86.50532032941677;
    private static final double c3_ = 24.01409824083091;
    private static final double c4_ = -1.231739572450155;
    private static final double c5_ = 0.1208650973866179e-2;
    private static final double c6_ = -0.5395239384953e-5;
	
    public double logValue(double x) {
        if (!(x>0.0)){
        	throw new ArithmeticException("positive argument required " + x);
        }
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
    
    public double evaluate(double x) {
    	throw new UnsupportedOperationException() ;
    }
    

    //FIXME: How is the value a obtained?
    /*
	public double evaluate(double x) {
		if (x <= 0.0) {
			return 0.0;
		}

        double gln = logValue(a);

        if (x<(a+1.0)) {
            double ap = a;
            double del = 1.0/a;
            double sum = del;
            for (int n=1; n<=100; n++) {
                ap += 1.0;
                del *= x/ap;
                sum += del;
                if (Math.abs(del) < Math.abs(sum)*3.0e-7)
                    return sum*Math.exp(-x + a*Math.log(x) - gln);
            }
        } else {
            double b = x + 1.0 - a;
            double c = Constants.QL_MAX_REAL;
            double d = 1.0/b;
            double h = d;
            for (int n=1; n<=100; n++) {
                double an = -1.0*n*(n-a);
                b += 2.0;
                d = an*d + b;
                
                if (Math.abs(d) < Constants.QL_EPSILON) {
                	d = Constants.QL_EPSILON;
                }
                c = b + an/c;
                if (Math.abs(c) < Constants.QL_EPSILON) {
                	c = Constants.QL_EPSILON;
                }
                d = 1.0/d;
                double del = d*c;
                h *= del;
                if (Math.abs(del - 1.0)<Constants.QL_EPSILON)
                    return h*Math.exp(-x + a*Math.log(x) - gln);
            }
        }
        throw new ArithmeticException("too few iterations");
	}
	*/

    

	
}
