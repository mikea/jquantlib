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

import org.jquantlib.math.distributions.GammaFunction;
import org.jquantlib.math.Constants;

/**
 * @author Richard Gomes
 * @author Dominik Holenstein
 */

//FIXME: Check duplicated code compared to the GammaDistribution class
public class IncompleteGamma {
	
	
	  public double incompleteGammaFunction(double a, double x, double accuracy, int maxIteration) {
		  
		 if(!(a>0.0)){
			 throw new ArithmeticException("non-positive a is not allowed");
		 }
		 
		 if(!(x>=0.0)){
			 throw new ArithmeticException("negative x is not allowed");
		 }
		 
		 if (x < (1+1.0)){
			 // Use the series representation
			 return incompleteGammaFunctionSeriesRepr(a,x,accuracy,maxIteration);
		 }
		 else {
			 // Use the continued fraction representation
			 return 1.0-incompleteGammaFunctionContinuedFractionRepr(a,x,accuracy,maxIteration);
		 }
		 
		 
	  }
	
	double incompleteGammaFunctionSeriesRepr(double a, double x, double accuracy,int maxIteration) {

        if (x==0.0) {
        	return 0.0;
        }
        
        GammaFunction gf = new GammaFunction();
        double gln = gf.logValue(a);
        double ap=a;
        double del=1.0/a;
        double sum=del;
        for (Integer n=1; n<=maxIteration; n++) {
            ++ap;
            del *= x/ap;
            sum += del;
            if (Math.abs(del) < Math.abs(sum)*accuracy) {
                return sum*Math.abs(-x+a*Math.log(x)-gln);
            }
        }
        throw new ArithmeticException("accuracy not reached");
    }

    public double incompleteGammaFunctionContinuedFractionRepr(double a, double x,
                                                      double accuracy,
                                                      Integer maxIteration) {

        Integer i;
        double an, b, c, d, del, h;
        
        GammaFunction gf = new GammaFunction();
        double gln = gf.logValue(a);
        b=x+1.0-a;
        c=1.0/Constants.QL_EPSILON;
        d=1.0/b;
        h=d;
        for (i=1; i<=maxIteration; i++) {
            an = -i*(i-a);
            b += 2.0;
            d=an*d+b;
            if (Math.abs(d) < Constants.QL_EPSILON) d=Constants.QL_EPSILON;
            c=b+an/c;
            if (Math.abs(c) < Constants.QL_EPSILON) c=Constants.QL_EPSILON;
            d=1.0/d;
            del=d*c;
            h *= del;
            if (Math.abs(del-1.0) < accuracy) {
                return Math.exp(-x+a*Math.log(x)-gln)*h;
            }
        }
        throw new ArithmeticException("accuracy not reached");
    }
}
