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

import org.jquantlib.math.Array;
import org.jquantlib.math.LogGrid;
import org.jquantlib.math.TransformedGrid;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Frequency;

public class PdeBSM extends PdeSecondOrderParabolic {

	private GeneralizedBlackScholesProcess process;
	
	public PdeBSM(GeneralizedBlackScholesProcess process) {
		this.process = process;
	}

	@Override
	public double diffusion(double t, double x) {
		  return process.diffusion(t, x);
	}

	@Override
	public double discount(double t, double x) {
		if (Math.abs(t) < 1e-8) t = 0;
        return process.riskFreeRate().getLink().forwardRate(t,t,Compounding.CONTINUOUS,Frequency.NO_FREQUENCY,true).rate();
	}

	@Override
	public double drift(double t, double x) {
		return process.drift(t, x);
	}

    @Override
    public TransformedGrid applyGridType(Array grid) {
        return new LogGrid(grid);
    }
	
	

}
