/*
 Copyright (C) 2009 Ueli Hofstetter

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
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

//work in progress

public class FDBermudianEngine extends FDMultiPeriodEngine{

	double extraTermInBermuda;
	
	public FDBermudianEngine(GeneralizedBlackScholesProcess process,
			int timeSteps, int gridPoints, boolean timeDependent) {
		super(process, timeSteps, gridPoints, timeDependent);
		if (System.getProperty("EXPERIMENTAL")==null) throw new UnsupportedOperationException("not implemented yet!");
		/* default values. where to set them?
		timeSteps = 100;
		gridPoints = 100;
		timeDependent = false;
		*/
		
	}

	@Override
	public void calculate(Results r){
		//FIXME: derive arguments from fdmultiperiodengine
		//setupArguments()
		//setupA
	}
	
	void initializeStepCondition(){
		stepCondition_ = new NullCondition<Array>();
	}
	
	void executeIntermediateStep(int step){
		int size = intrinsicValues.size();
		for(int j = 0; j<size; j++){
			prices_.values().set(j, Math.max(prices_.value(j), intrinsicValues.value(j)));
			}
	}

}

/*

#ifndef quantlib_fd_bermudan_engine_hpp
#define quantlib_fd_bermudan_engine_hpp

#include <ql/instruments/vanillaoption.hpp>
#include <ql/pricingengines/vanilla/fdmultiperiodengine.hpp>

namespace QuantLib {

   //! Finite-differences Bermudan engine
   class FDBermudanEngine : public VanillaOption::engine,
                            public FDMultiPeriodEngine {
     public:
       // constructor
       FDBermudanEngine(Size timeSteps = 100,
                        Size gridPoints = 100,
                        bool timeDependent = false)
       : FDMultiPeriodEngine(timeSteps, gridPoints,
                             timeDependent) {}
       void calculate() const {
           setupArguments(&arguments_);
           FDMultiPeriodEngine::calculate(&results_);
       }
     protected:
       Real extraTermInBermudan ;
       void initializeStepCondition() const {
           stepCondition_ = boost::shared_ptr<StandardStepCondition>(
                                                 new NullCondition<Array>());
       };
       void executeIntermediateStep(Size ) const {
           Size size = intrinsicValues_.size();
           for (Size j=0; j<size; j++)
               prices_.value(j) = std::max(prices_.value(j),
                                           intrinsicValues_.value(j));
       }
   };

}


#endif
*/
