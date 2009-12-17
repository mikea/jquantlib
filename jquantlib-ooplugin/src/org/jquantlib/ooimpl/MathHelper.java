/*
 Copyright (C) 2008 Praneet Tiwari

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

package org.jquantlib.ooimpl;
import org.jquantlib.math.Factorial;
import org.jquantlib.math.PrimeNumbers;
import org.jquantlib.math.distributions.BinomialDistribution;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.math.distributions.CumulativePoissonDistribution;
import org.jquantlib.math.distributions.GammaDistribution;
import org.jquantlib.math.distributions.InverseCumulativeNormal;
import org.jquantlib.math.distributions.InverseCumulativePoisson;
import org.jquantlib.math.distributions.NonCentralChiSquaredDistribution;
import org.jquantlib.math.distributions.PoissonDistribution;

/**
 *
 * @author Praneet Tiwari
 */
public class MathHelper {

    public static double getFactorial(final int n) {

        final Factorial f = new Factorial();
        /* return */f.get(n);
        return n;
    }

    public static double getlNFactorial(final int n) {
        final Factorial f = new Factorial();
        return f.ln(n);
    }

    public static long getPrimeNumberAt(final int n) {
        final PrimeNumbers pn = new PrimeNumbers();
        return pn.get(n);
    }

    public static double evaluateBinomialDistributionValue(final double probability, final int k) {
        final BinomialDistribution bs = new BinomialDistribution(probability, 0);
        return bs.op(k);

    }

    public static double evaluateCumulativeNormalDistribution(final double mean, final double sigma, final double z) {
        final CumulativeNormalDistribution cnd = new CumulativeNormalDistribution(mean, sigma);
        return cnd.op(z);
    }

    public static double evaluateCumulativePoissonDistribution(final double mean, final int k) {
        final CumulativePoissonDistribution cpd = new CumulativePoissonDistribution(mean);
        return cpd.op(k);

    }

    public static double evaluateGammaDistribution(final double a, final double x) {
        final GammaDistribution gd = new GammaDistribution(a);
        return gd.op(x);
    }

    public static double evaluateInverseCumulativeNormal(final double average, final double sigma, final double x) {
        final InverseCumulativeNormal icn = new InverseCumulativeNormal(average, sigma);
        return icn.op(x);
    }

    public static double evaluateNonCentralChiSquaredDistribution(final double df, final double x, final double ncp) {
        final NonCentralChiSquaredDistribution ncd = new NonCentralChiSquaredDistribution(df, ncp);
        return ncd.op(x);

    }

    public static double evaluateInverseCumulativePoisson(final double lambda, final double x) {
        final InverseCumulativePoisson icp = new InverseCumulativePoisson(lambda);
        return icp.op(x);

    }

    // fix this return type and argument type got mixed up
    public static double evaluatePoissonDistribution(final double mu, final double sigma) {
        final PoissonDistribution pd = new PoissonDistribution(mu);
        final int k = (int) sigma;
        return pd.op(k);
    }
}