
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

import org.jquantlib.math.Constants;
import org.jquantlib.math.distributions.GammaFunction;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author Richard Gomes
 * @author Dominik Holenstein
 */


public class GammaDistribution {
	
	static private double a_; // alpha
	
	public GammaDistribution (double a){
    	a_=a;    	
    }
	
	public double evaluate(double x){
		if (a_ <= 0.0) {
    		throw new ArithmeticException("a_ must be > 0");
    	}
		if (x <= 0.0) {
    		throw new ArithmeticException("x must be > 0");
		}
		return GammaDist(x);
	}
	
	public double GammaDist(double x) {
     	
    	GammaFunction gf = new GammaFunction();
    	
        double gln = gf.logValue(a_);

        if (x<(a_+1.0)) {
        	double ap = a_;
        	double del = 1.0/a_; // beta
        	double sum = del;
        	for (int n=1; n<=100; n++) {
        		ap += 1.0;
        		del *= x/ap;
        		sum += del;
        		if (Math.abs(del) < Math.abs(sum)*3.0e-7)
        			return sum*Math.exp(-x + a_*Math.log(x) - gln);
        	}
        } else {
        	double b = x + 1.0 - a_;
        	double c = Constants.QL_MAX_REAL;
        	double d = 1.0/b;
        	double h = d;
        	for (int n=1; n<=100; n++) {
        		double an = -1.0*n*(n-a_);
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
        			return h*Math.exp(-x + a_*Math.log(x) - gln);
        	}
        }
        throw new ArithmeticException("too few iterations");
	}
}

