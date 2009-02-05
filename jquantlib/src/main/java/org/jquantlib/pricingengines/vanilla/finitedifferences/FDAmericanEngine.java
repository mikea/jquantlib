/*
 Copyright (C) 2007 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */
package org.jquantlib.pricingengines.vanilla.finitedifferences;

import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * @author Srinivas Hasti
 * 
 */
public class FDAmericanEngine extends FDEngineAdapter<FDAmericanCondition> {

	public FDAmericanEngine(GeneralizedBlackScholesProcess process,
			int timeSteps, int gridPoints, boolean timeDependent) {
		super(process, timeSteps, gridPoints, timeDependent);
	}

	public FDAmericanEngine(GeneralizedBlackScholesProcess process,
			int timeSteps, int gridPoints) {
		this(process, timeSteps, gridPoints, false);
	}

	public FDAmericanEngine(BlackScholesMertonProcess process) {
		this(process, 100, 100, false);
	}
}
