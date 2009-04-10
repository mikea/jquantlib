package org.jquantlib.math.statistics;

import org.jquantlib.math.E_IUnaryFunction;
import org.jquantlib.util.Pair;

public interface IStatistics {
    public double mean();
    public Pair<Double, Double> expectationValue(E_IUnaryFunction<Double, Double> f, E_IUnaryFunction<Double, Boolean> inRange);
    public int getSampleSize();
    public double percentile(double y);
}
