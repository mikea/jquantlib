/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.math.distributions;

import org.jquantlib.math.Constants;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author Richard Gomes
 */
public class NonCentralChiSquaredDistribution implements UnaryFunctionDouble {

	//
	// private fields
	//
	
	/** degrees of freedom */
	private double df_;
	
	/** non-centrality parameter */
	private double ncp_;
	
	private GammaFunction gammaFunction_ = new GammaFunction();
	
	
	//
	// public constructor
	//
	
	public NonCentralChiSquaredDistribution(double df, double ncp){
		//TODO check on valid parameters
		df_ = df;
		ncp_ = ncp;
	}
	
	
	//
	// implements UnaryFunctionDouble
	//
	
	public double evaluate(double x) /* @Read-only */ {
		//C++ appears to be based on Algorithm AS 275
		//with perhaps one addition, see below
        if (x <= 0.0)
            return 0.0;

        final double errmax = 1e-12;
        final int itrmax = 10000;
        double lam = 0.5*ncp_;

        double u = Math.exp(-lam);
        double v = u;
        double x2 = 0.5*x;
        double f2 = 0.5*df_;

        double t = 0.0;
        if (f2*Constants.QL_EPSILON > 0.125 &&
            Math.abs(x2-f2) < Math.sqrt(Constants.QL_EPSILON)*f2) {
            //TODO check if this part is AS 275?? or a known asymptotic
            t = Math.exp((1 - t) *
                         (2 - t/(f2+1)))/Math.sqrt(2.0*Math.PI*(f2 + 1.0));
        } else {
            t = Math.exp(f2*Math.log(x2) - x2 -
                         gammaFunction_.logValue(f2 + 1));
        }

        double ans = v*t;

        int n = 1;
        double f_2n = df_ + 2.0;
        double f_x_2n = df_ + 2.0 - x;

        //restructure C++ algo to avoid goto...
        while(f_x_2n<=0.0){
            u *= lam / n;
            v += u;
            t *= x / f_2n;
            ans += v*t;
            n++;
            f_2n += 2.0;
            f_x_2n += 2.0;            
        }
        
        while(n<=itrmax){
            double bound = t * x / f_x_2n;
            if (bound > errmax){
            	u *= lam / n;
                v += u;
                t *= x / f_2n;
                ans += v*t;
                n++;
                f_2n += 2.0;
                f_x_2n += 2.0;
            } else {
            	return ans;
            }
        }
        
        //return ans; ?
        throw new ArithmeticException("NonCentralChiSquared failed to converge: df " + df_ + " ncp: " + ncp_ + " x " + x);
	}
}
