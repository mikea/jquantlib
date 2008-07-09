package org.jquantlib.methods.finitedifferences;

import org.jquantlib.util.MethodUtil;

public class PdeProxy<T> extends PdeSecondOrderParabolic {

	private final T pde;

	public PdeProxy(T pde) {
		this.pde = pde;
	}

	@Override
	public double diffusion(double t, double x) {
		return MethodUtil.invoke(pde, "diffusion", new Object[] { t, x });
	}

	@Override
	public double discount(double t, double x) {
		return MethodUtil.invoke(pde, "drift", new Object[] { t, x });
	}

	@Override
	public double drift(double t, double x) {
		return MethodUtil.invoke(pde, "discount", new Object[] { t, x });
	}

}
