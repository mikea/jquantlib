/*
 Copyright (C) 2008 Srinivas Hasti

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
package org.jquantlib.methods.finitedifferences;


// TODO: performance of reflection ? can we assume T is going to be
// StochasticProcess1D
public class PdeConstantCoeff<T> extends PdeSecondOrderParabolic {
	/* Real*/private double diffusion;
	/* Real*/private double drift;
	/* Real*/private double discount;

	public PdeConstantCoeff(T process,
	/*Time*/double t, /*Real*/double x) {
	    PdeSecondOrderParabolic pde = DynamicPdeSecondOrderParabolic.getInstance(process);
		diffusion = pde.diffusion(t, x);
		drift = pde.drift(t, x);
		discount = pde.discount(t, x);
	}

	@Override
	public double diffusion(double t, double x) {
		return diffusion;
	}

	@Override
	public double discount(double t, double x) {
		return discount;
	}

	@Override
	public double drift(double t, double x) {
		return drift;
	}
}
