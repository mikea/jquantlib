package org.jquantlib.math.distributions;

import org.jquantlib.math.Constants;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author <Richard Gomes>
 */
public class NonCentralChiSquaredDistribution implements UnaryFunctionDouble {

	public NonCentralChiSquaredDistribution(double df, double ncp){
		//TODO check on valid parameters
		df_ = df;
		ncp_ = ncp;
	}
	
	public double evaluate(double x) {
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

	/** degrees of freedom */
	private double df_;
	
	/** non-centrality parameter */
	private double ncp_;
	
	private GammaFunction gammaFunction_ = new GammaFunction();
}
