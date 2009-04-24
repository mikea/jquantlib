/*
Copyright (C) 2009 Ueli Hofstetter

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
package org.jquantlib.math.statistics;

import org.jquantlib.math.E_IUnaryFunction;
import org.jquantlib.util.Pair;

public interface IStatistics {
    public double mean();

    public Pair<Double, Double> expectationValue(E_IUnaryFunction<Double, Double> f, E_IUnaryFunction<Double, Boolean> inRange);

    public int getSampleSize();

    public double percentile(double y);

    public double standardDeviation();

    public void add(double data, double value);

    public int samples();

    public void reset();

    public double weightSum();

    public double min();

    public double max();

    public double variance();

    public double skewness();

    public double kurtosis();

    public double downsideVariance();

    public double downsideDeviation();

    public double semiVariance();

    public double semiDeviation();

    public double errorEstimate();

    public double gaussianPercentile(double percentile);

    public double gaussianPotentialUpside(double percentile);

    public double potentialUpside(double percentile);

    public double gaussianValueAtRisk(double percentile);

    public double valueAtRisk(double percentile);

    public double gaussianExpectedShortfall(double percentile);

    public double expectedShortfall(double percentile);

    public double regret(double target);

    public double gaussianShortfall(double target);

    public double shortfall(double target);

    public double gaussianAverageShortfall(double target);

    public double averageShortfall(double target);
    
    public Pair<Double, Double> []  data();

}
