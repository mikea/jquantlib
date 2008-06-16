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

/*
 Copyright (C) 2002, 2003 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003 StatPro Italia srl
 Copyright (C) 2005 Gary Kennedy

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.math.distributions;

import org.jquantlib.math.Constants;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.TabulatedGaussLegendre;



/**
 * Cumulative bivariate normal distibution function (West 2004).
 * The bivariate normal distribution is a distribution of a pair of variables whose 
 * conditional distributions are normal and that satisfy certain other technical conditions. 
 * 
 * The implementation derives from the article <i>"Better Approximations To Cumulative
 * Normal Distibutions", Graeme West, Dec 2004</i> available at www.finmod.co.za.
 * Also available in <i>Wilmott Magazine, 2005, (May), 70-76,</i> The main code is a
 * port of the C++ code at <a
 * href="www.finmod.co.za/cumnorm.zip">www.finmod.co.za/cummnorm.zip</a>.
 * <p>
 * The algorithm is based on the near double-precision algorithm described in
 * <i>"Numerical Computation of Rectangular Bivariate an Trivariate Normal and t
 * Probabilities", Genz (2004), Statistics and Computing 14, 151-160. </i>(available
 * at <a
 * href="http://www.math.wsu.edu/faculty/genz/papers/bvnt/bvnt.html">www.math.wsu.edu/faculty/genz/papers/bvnt/bvnt.html</a>)
 * <p>
 * 
 * The standard bivariate normal distribution function is given by
 * {@latex[
 * 	\Phi({ \bf b}, \rho)= \frac{1}{2 \pi \sqrt{1- \rho^{2}}} \int_{- \infty}^{b_1} \int_{- \infty}^{b_2} e^{-(x^{2}-2 \rho xy +y^{2})/(2(1- \rho^{2}))}dydx
 * } <p>
 * 
 * 
 * This implementation mainly differs from the original code in two regards;
 * <ol>
 * <li>The implementation of the cumulative normal distribution is
 * {@code CumulativeNormalDistribution}</li>
 * <li> The arrays XX and W are zero-based</li>
 * </ol>
 * 
 * @author Richard Gomes
 */

public class BivariateNormalDistribution {
	
	//
	// private fields
	//
	

    private final double correlation_;
    private final static CumulativeNormalDistribution cumnorm_ = new CumulativeNormalDistribution();

    
    //
    // public constructor
    //
    
    /**
     * Constructor of BivariateNormalDistribution to initialize the correlation.
     * The correlation <code>rho</code> must be >=-1.0 and <=1.0.
     * @param rho correlation
     */
    public BivariateNormalDistribution(double rho) {
	if (rho < -1.0) {
	    throw new ArithmeticException("rho must be >= -1.0 (" + rho + " not allowed)");
	}
	if (rho > 1.0) {
	    throw new ArithmeticException("rho must be <= 1.0 (" + rho + " not allowed)");
	}
	correlation_ = rho;
    }

    
    //
    // Implements UnaryFunction Double
    //
    
    
    /**
     * Computes the Bivariate Normal Distribution of the two variables <code>x</code> and <code>y</code>
     * which can be correlated in a particular manner.
     * 
     * @param x
     *                First variable
     * @param y
     *                Second variable
     * @return BVN
     */
    public double evaluate(double x, double y) {
	TabulatedGaussLegendre gaussLegendreQuad = new TabulatedGaussLegendre(20);
	
	if (Math.abs(correlation_) < 0.3) {
	    gaussLegendreQuad.setOrder(6);
	} else if (Math.abs(correlation_) < 0.75) {
	    gaussLegendreQuad.setOrder(12);
	}

	double h = -x;
	double k = -y;
	double hk = h * k;
	double BVN = 0.0;

	if (Math.abs(correlation_) < 0.925) {
	    if (Math.abs(correlation_) > 0) {
		double asr = Math.asin(correlation_);
		eqn3 f = new eqn3(h, k, asr);
		BVN = gaussLegendreQuad.evaluate(f);
		BVN *= asr * (0.25 / Math.PI);
	    }
	    BVN += cumnorm_.evaluate(-h) * cumnorm_.evaluate(-k);
	} else {
	    if (correlation_ < 0) {
		k *= -1;
		hk *= -1;
	    }
	    if (Math.abs(correlation_) < 1) {
		double Ass = (1 - correlation_) * (1 + correlation_);
		double a = Math.sqrt(Ass);
		double bs = (h - k) * (h - k);
		double c = (4 - hk) / 8;
		double d = (12 - hk) / 16;
		double asr = -(bs / Ass + hk) / 2;
		if (asr > -100) {
		    BVN = a * Math.exp(asr) * (1 - c * (bs - Ass) * (1 - d * bs / 5) / 3 + c * d * Ass * Ass / 5);
		}
		if (-hk < 100) {
		    double B = Math.sqrt(bs);
		    BVN -= Math.exp(-hk / 2) * Constants.M_SQRT2PI * cumnorm_.evaluate(-B / a) * B
			    * (1 - c * bs * (1 - d * bs / 5) / 3);
		}
		a /= 2;
		eqn6 f = new eqn6(a, c, d, bs, hk);
		BVN += gaussLegendreQuad.evaluate(f);
		BVN /= (-2.0 * Math.PI);
	    }

	    if (correlation_ > 0) {
		BVN += cumnorm_.evaluate(-Math.max(h, k));
	    } else {
		BVN *= -1;
		if (k > h) {
		    BVN += cumnorm_.evaluate(k) - cumnorm_.evaluate(h);
		}
	    }
	}
	return BVN;
    }

    /**
     * Relates to equation 3, see Genz 2004.<br>
     * <a
     * href="http://www.math.wsu.edu/faculty/genz/papers/bvnt/node4.html#L1P">The
     * Transformed BVN Problem</a>
     * 
     */

    private class eqn3 implements UnaryFunctionDouble {

	private double hk_, asr_, hs_;

	/**
	 * Equation 3, see Genz 2004.
	 * 
	 * @param h
	 * @param k
	 * @param asr ASIN of <code>correlation_</code>
	 * @return Math.exp((sn * hk_ - hs_) / (1.0 - sn * sn))
	 */

	public eqn3(double h, double k, double asr) {
	    hk_ = h * k;
	    hs_ = (h * h + k * k) / 2;
	    asr_ = asr;
	}

	/**
	 * Computes equation 3, see Genz 2004.<br>
	 * <a
	 * href="http://www.math.wsu.edu/faculty/genz/papers/bvnt/node4.html#L1P">The
	 * Transformed BVN Problem</a>
	 * 
	 * @param h
	 * @param k
	 * @param asr ASIN of <code>correlation_</code>
	 * @return Math.exp((sn * hk_ - hs_) / (1.0 - sn * sn))
	 */
	public double evaluate(double x) {
	    double sn = Math.sin(asr_ * (-x + 1) * 0.5);
	    return Math.exp((sn * hk_ - hs_) / (1.0 - sn * sn));
	}
    };

    /**
     * Relates to equation 6, see Genz 2004.<br>
     * <a
     * href="http://www.math.wsu.edu/faculty/genz/papers/bvnt/node5.html#L3">Numerical
     * Integration Results</a>
     * 
     */
    private class eqn6 implements UnaryFunctionDouble {

	private double a_, c_, d_, bs_, hk_;

	/**
	 * Constructor to initialize a, b, c, d, bs and hk.
	 * @param a
	 * @param c
	 * @param d
	 * @param bs
	 * @param hk
	 */
	public eqn6(double a, double c, double d, double bs, double hk) {
	    a_ = a;
	    c_ = c;
	    d_ = d;
	    bs_ = bs;
	    hk_ = hk;
	}

	/**
	 * Computes equation 6, see Genz 2004.<br>
	 * <a
	 * href="http://www.math.wsu.edu/faculty/genz/papers/bvnt/node5.html#L3">Numerical
	 * Integration Results</a>
	 * 
	 * @param x
	 * @return <code>if (asr > -100.0) </code> return (a_ * Math.exp(asr) *
	 *         (Math.exp(-hk_ * (1 - rs) / (2 * (1 + rs))) / rs - (1 + c_ *
	 *         xs * (1 + d_ * xs))))<br>
	 *         <code>else</code> return 0.00
	 * 
	 */
	public double evaluate(double x) {
	    double xs = a_ * (-x + 1);
	    xs = Math.abs(xs * xs);
	    double rs = Math.sqrt(1 - xs);
	    double asr = -(bs_ / xs + hk_) / 2;
	    if (asr > -100.0) {
		return (a_ * Math.exp(asr) * (Math.exp(-hk_ * (1 - rs) / (2 * (1 + rs))) / rs - (1 + c_ * xs * (1 + d_ * xs))));
	    }
	    return 0.0;
	}
    };
}
