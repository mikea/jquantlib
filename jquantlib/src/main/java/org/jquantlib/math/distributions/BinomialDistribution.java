package org.jquantlib.math.distributions;

import org.jquantlib.math.Factorial;

/**
 * @author <Richard Gomes>
 */
public class BinomialDistribution {

	public BinomialDistribution(double p, int n) {

		n_=n;

		if (p==0.0) {
			logOneMinusP_ = 0.0;
		} else if (p==1.0) {
			logP_ = 0.0;
		} else {
			if (!(p>0)){
				throw new ArithmeticException("negative p not allowed");
			}
			if (!(p<1.0)){
				throw new ArithmeticException("p>1.0 not allowed");
			}

			logP_ = Math.log(p);
			logOneMinusP_ = Math.log(1.0-p);
			
		}

	}
	
	
	public double evaluate(int k) {

        if (k > n_) return 0.0;

        // p==1.0
        if (logP_==0.0) {
            return (k==n_ ? 1.0 : 0.0);
        }
        // p==0.0
        if (logOneMinusP_==0.0){
            return (k==0 ? 1.0 : 0.0);
        }
        
        return Math.exp(binomialCoefficientLn(n_, k) +
                            k * logP_ + (n_-k) * logOneMinusP_);

	}
	
	static private double binomialCoefficientLn(int n, int k) {

		if (!(n>=0)) throw new ArithmeticException("n<0 not allowed, " + n);

		if (!(k>=0)) throw new ArithmeticException("k<0 not allowed, " + k);

		if (!(n>=k)){
			throw new ArithmeticException("n<k not allowed");
		}

        return factorial_.ln(n)-factorial_.ln(k)-factorial_.ln(n-k);

    }
	
	private static final Factorial factorial_ = new Factorial();
    private int n_;
    private double logP_, logOneMinusP_;

}
