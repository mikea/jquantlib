package org.jquantlib.model.volatility;

import org.jquantlib.util.TimeSeries;

public interface VolatilityCompositor {

	public void calibrate(final TimeSeries</*@Volatility*/Double> timeSeries);

	public TimeSeries</*@Volatility*/Double> calculate(final TimeSeries</*@Volatility*/Double> volatilitySeries);

}