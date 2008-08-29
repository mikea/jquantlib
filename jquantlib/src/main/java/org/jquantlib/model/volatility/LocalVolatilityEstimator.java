package org.jquantlib.model.volatility;

import org.jquantlib.util.TimeSeries;

public interface LocalVolatilityEstimator<T> {

	TimeSeries</* @Volatility */ Double> calculate(final TimeSeries<T> quoteSeries) ;
}
