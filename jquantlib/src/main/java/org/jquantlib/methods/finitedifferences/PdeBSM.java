package org.jquantlib.methods.finitedifferences;

import org.jquantlib.math.LogGrid;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Frequency;

public class PdeBSM extends PdeSecondOrderParabolic {

	private GeneralizedBlackScholesProcess process;
	private LogGrid grid;

	PdeBSM(GeneralizedBlackScholesProcess process) {
		this.process = process;
	}

	@Override
	public double diffusion(double t, double x) {
		  return process.diffusion(t, x);
	}

	@Override
	public double discount(double t, double x) {
		if (Math.abs(t) < 1e-8) t = 0;
        return process.riskFreeRate().getLink().getForwardRate(t,t,Compounding.CONTINUOUS,Frequency.NO_FREQUENCY,true).doubleValue();
	}

	@Override
	public double drift(double t, double x) {
		return process.drift(t, x);
	}

}
