/*
 Copyright (C) 2009 Ueli Hofstetter
 Copyright (C) 2009 Srinivas Hasti

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

import org.jquantlib.math.Array;
import org.jquantlib.methods.finitedifferences.NullCondition;
import org.jquantlib.pricingengines.VanillaOptionEngine;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

//TODO: work in progress

public class FDBermudianEngine extends VanillaOptionEngine {

    private double extraTermInBermuda;
    private FDMultiPeriodEngine fdVanillaEngine;

    
    //
    // public constructors
    //
    
    public FDBermudianEngine(GeneralizedBlackScholesProcess process, int timeSteps, int gridPoints, boolean timeDependent) {
        fdVanillaEngine = new FDBermudianMPEngine(process, timeSteps, gridPoints, timeDependent);
    }

    
    //
    // private methods
    //
    
    // TODO: verify how this method is called
    private void initializeStepCondition() {
        fdVanillaEngine.stepCondition = new NullCondition<Array>();
    }

    // TODO: verify how this method is called
    private void executeIntermediateStep(int step) {
        int size = fdVanillaEngine.intrinsicValues.size();
        for (int j = 0; j < size; j++) {
            fdVanillaEngine.prices.values().set(j, Math.max(fdVanillaEngine.prices.value(j), fdVanillaEngine.intrinsicValues.value(j)));
        }
    }
    
    
    //
    // implements PricingEngine
    //
    
    @Override
    public void calculate() {
        fdVanillaEngine.setupArguments(arguments);
        fdVanillaEngine.calculate(results);
    }

    
    //
    // private inner classes
    //
    
    private static class FDBermudianMPEngine extends FDMultiPeriodEngine {
        public FDBermudianMPEngine(GeneralizedBlackScholesProcess process, int timeSteps, int gridPoints, boolean timeDependent) {
            super(process, timeSteps, gridPoints, timeDependent);
        }


        //
        // overrides FDMultiPeriodEngine
        //
        
        @Override
        protected void executeIntermediateStep(int step) {
            int size = intrinsicValues.size();
            for (int j = 0; j < size; j++) {
                prices.values().set(j, Math.max(prices.value(j), intrinsicValues.value(j)));
            }
        }
    }
    
}