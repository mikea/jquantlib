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

import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.util.reflect.TypeToken;


//
public abstract class PdeConstantCoeff<T extends Pde> extends PdeSecondOrderParabolic {
	/* Real*/private double diffusion;
	/* Real*/private double drift;
	/* Real*/private double discount;

	public PdeConstantCoeff(GeneralizedBlackScholesProcess process,
	/*Time*/double t, /*Real*/double x) {
	    Class<T> clazz = (Class<T>) TypeToken.getClazz(this.getClass());
	    T pde = getInstance(clazz, process);
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
	
	 protected T getInstance(Class<T> clazz, GeneralizedBlackScholesProcess process) {
	        try {
	            return (T) clazz.getConstructor(GeneralizedBlackScholesProcess.class).newInstance(process);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
}
