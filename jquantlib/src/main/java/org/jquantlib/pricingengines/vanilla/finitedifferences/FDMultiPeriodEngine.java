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


import org.jquantlib.cashflow.Event;
import org.jquantlib.math.Array;
import org.jquantlib.math.SampledCurve;
import org.jquantlib.methods.finitedifferences.NullCondition;
import org.jquantlib.methods.finitedifferences.StandardFiniteDifferenceModel;
import org.jquantlib.methods.finitedifferences.StepCondition;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.pricingengines.results.OptionResults;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * 
 * This class needs review!!!!!!!!!!!!!!!!
 *
 */

public class FDMultiPeriodEngine extends FDVanillaEngine {
    
    Event [] events_;
    Double[] stoppingTimes_;
    int timeStepPerPeriod_;
    SampledCurve prices_;
    StepCondition stepCondition_;
    StandardFiniteDifferenceModel model_;

    public FDMultiPeriodEngine(GeneralizedBlackScholesProcess process, int timeSteps, int gridPoints, boolean timeDependent) {
        super(process, timeSteps, gridPoints, timeDependent);
        if (System.getProperty("EXPERIMENTAL")==null) throw new UnsupportedOperationException("not implemented yet!");    }
    
    public FDMultiPeriodEngine(int gridPoints, int timeSteps, boolean timeDependent){
        //where to get the process from?
        super(null, timeSteps, gridPoints, timeDependent);
        if (System.getProperty("EXPERIMENTAL")==null) throw new UnsupportedOperationException("not implemented yet!");
    }
    
    public void setupArguments(final Arguments args, final Event[] schedule){
        super.setupArguments(args);
        events_ = schedule;
        //necessary?
        //stoppingTimes_.clear();
        int n = schedule.length;
        for(int i = 0; i<n; i++){
            //TODO: why is ther no date field in Event.java ??????
            //stoppingTimes_[i]=process.getTime(events_[i].getDate());
        }
    }
    
    public void setupArguments(final Arguments a){
        super.setupArguments(a);
        /* check this!!!!!!!!!!!
        const OneAssetOption::arguments *args =
            dynamic_cast<const OneAssetOption::arguments*>(a);
        QL_REQUIRE(args, "incorrect argument type");
        */
        //necessary?
        //events_.clear();        
        stoppingTimes_ = ((OneAssetOptionArguments)a).stoppingTimes.toArray(new Double[0]);
    }
    
    double getDividendTime(int i){
        return stoppingTimes_[i];
    }
    
    public void calculate(Results r){
        double beginDate, endDate;
        int dateNumber = stoppingTimes_.length;
        boolean lastDateIsResTime = false;
        int firstIndex = -1;
        int lastIndex = dateNumber - 1;
        boolean firstDateIsZero = false;
        double firstNonZeroDate = getResidualTime();
        
        double dateTolerance = 1e-6;
        
        if(dateNumber>0){
            if(getDividendTime(0)<=0){
                throw new IllegalArgumentException("first date (" + getDividendTime(0) + ") cannot be negative");
            }
            if(getDividendTime(0)<getResidualTime() *dateTolerance){
                firstDateIsZero = true;
                firstIndex = 0;
                if(dateNumber >= 2){
                    firstNonZeroDate = getDividendTime(1);
                }
            }
            if(Math.abs(getDividendTime(lastIndex) - getResidualTime())<dateTolerance){
                    lastDateIsResTime = true;
                    lastIndex = dateNumber - 2;
                }

                if (!firstDateIsZero){
                    firstNonZeroDate = getDividendTime(0);
                }

                if (dateNumber >= 2) {
                    for (int j = 1; j < dateNumber; j++)
                        if(getDividendTime(j-1) > getDividendTime(j)){
                            throw new IllegalArgumentException("dates must be in increasing order: "
                                   + getDividendTime(j-1) +" is not strictly smaller than " + getDividendTime(j));
                        }       
                }
        }
        double dt = getResidualTime()/(timeStepPerPeriod_*(dateNumber+1));

        // Ensure that dt is always smaller than the first non-zero date
        if (firstNonZeroDate <= dt){
            dt = firstNonZeroDate/2.0;
        }

        setGridLimits();
        initializeInitialCondition();
        initializeOperator();
        initializeBoundaryConditions();
        initializeModel();
        initializeStepCondition();

        prices_ = intrinsicValues;
        if(lastDateIsResTime){
            //FIXME:not implementd yet
            //executeIntermediateStep(dateNumber - 1);
        }

        Integer j = lastIndex;
        do{
            if (j == (dateNumber - 1)){
                beginDate = getResidualTime();
            }
            else{
                beginDate = getDividendTime(j+1);
            }

            if (j >= 0){
                endDate = getDividendTime(j);
            }
            else{
                endDate = dt;
            }

            model_.rollback(prices_.values(),beginDate, endDate,timeStepPerPeriod_, stepCondition_);
            
            if (j >= 0){
                //FIXME: not implementd yet
                //executeIntermediateStep(j);
            }
        } while (--j >= firstIndex);

        model_.rollback(prices_.values(),dt, 0, 1, stepCondition_);
        

        if(firstDateIsZero){
            //FIXME: not implementd yet
            //executeIntermediateStep(0);
        }

        ((OptionResults)r).value = prices_.valueAtCenter();
        ((OptionResults)r).delta = prices_.firstDerivativeAtCenter();
        ((OptionResults)r).gamma = prices_.secondDerivativeAtCenter();
        ((OptionResults)r).addAdditionalResult("priceCurve", prices_);
    }
    
    void initializeStepCondition() {
        stepCondition_ = new NullCondition<Array>();
    }

    void initializeModel() {
        model_ = new StandardFiniteDifferenceModel(finiteDifferenceOperator, bcS);
    }
}
