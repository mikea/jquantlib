package org.jquantlib.pricingengines.vanilla.finitedifferences;

import org.jquantlib.pricingengines.VanillaOptionEngine;

//work in progress


public class FDBermudianEngine {

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
