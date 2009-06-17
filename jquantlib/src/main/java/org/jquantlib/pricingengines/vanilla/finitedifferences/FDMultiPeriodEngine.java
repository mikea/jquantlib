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


import java.util.List;

import org.jquantlib.cashflow.Event;
import org.jquantlib.math.Array;
import org.jquantlib.math.SampledCurve;
import org.jquantlib.methods.finitedifferences.NullCondition;
import org.jquantlib.methods.finitedifferences.StandardFiniteDifferenceModel;
import org.jquantlib.methods.finitedifferences.StepCondition;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.pricingengines.results.OneAssetOptionResults;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

//TODO: code review
public abstract class FDMultiPeriodEngine extends FDVanillaEngine {
    
    private List<Event> events;
    private List<Double> stoppingTimes;
    private int timeStepPerPeriod;
    protected SampledCurve prices;
    protected StepCondition<Array> stepCondition;
    private StandardFiniteDifferenceModel model;

    public FDMultiPeriodEngine(GeneralizedBlackScholesProcess process, int timeSteps, int gridPoints, boolean timeDependent) {
        super(process, timeSteps, gridPoints, timeDependent);
    }
    
    public FDMultiPeriodEngine(GeneralizedBlackScholesProcess process) {
        super(process, 100, 100, false);
    }
    
    public void setupArguments(final Arguments args, final List<Event> schedule){
        super.setupArguments(args);
        events = schedule;
        stoppingTimes.clear();
        int n = schedule.size();
        for(int i = 0; i<n; i++){
            stoppingTimes.add(process.getTime(events.get(i).date()));
        }
    }
    

    //
    // abstract methods
    //
    
    protected abstract void executeIntermediateStep(int step);

    
    //
    // private methods
    //
    
    private double getDividendTime(int i){
        return stoppingTimes.get(i);
    }
    
    private void initializeStepCondition() {
        stepCondition = new NullCondition<Array>();
    }

    private void initializeModel() {
        model = new StandardFiniteDifferenceModel(finiteDifferenceOperator, bcS);
    }

    
    //
    // overrides FDVanillaEngine
    //
    
    @Override
    public void setupArguments(final Arguments a){
        super.setupArguments(a);
        OneAssetOptionArguments args = (OneAssetOptionArguments) a;     
        events.clear();        
        int n = args.exercise.size();
        for (int i=0; i<n; ++i)
            stoppingTimes.add(process.getTime(args.exercise.date(i)));
    }
    
    @Override
    public void calculate(Results r){
        OneAssetOptionResults results = (OneAssetOptionResults) r;
        double beginDate, endDate;
        int dateNumber = stoppingTimes.size();
        boolean lastDateIsResTime = false;
        int firstIndex = -1;
        int lastIndex = dateNumber - 1;
        boolean firstDateIsZero = false;
        double firstNonZeroDate = getResidualTime();
        
        double dateTolerance = 1e-6;
        
        if (dateNumber > 0) {
            if (getDividendTime(0) <= 0) {
                throw new IllegalArgumentException("first date (" + getDividendTime(0) + ") cannot be negative");
            }
            if (getDividendTime(0) < getResidualTime() * dateTolerance) {
                firstDateIsZero = true;
                firstIndex = 0;
                if (dateNumber >= 2) {
                    firstNonZeroDate = getDividendTime(1);
                }
            }
            if (Math.abs(getDividendTime(lastIndex) - getResidualTime()) < dateTolerance) {
                lastDateIsResTime = true;
                lastIndex = dateNumber - 2;
            }

            if (!firstDateIsZero) {
                firstNonZeroDate = getDividendTime(0);
            }

            if (dateNumber >= 2) {
                for (int j = 1; j < dateNumber; j++)
                    if (getDividendTime(j - 1) > getDividendTime(j)) {
                        throw new IllegalArgumentException("dates must be in increasing order: " + getDividendTime(j - 1)
                                + " is not strictly smaller than " + getDividendTime(j));
                    }
            }
        }
        
        double dt = getResidualTime()/(timeStepPerPeriod*(dateNumber+1));

        // Ensure that dt is always smaller than the first non-zero date
        if (firstNonZeroDate <= dt) {
            dt = firstNonZeroDate / 2.0;
        }

        setGridLimits();
        initializeInitialCondition();
        initializeOperator();
        initializeBoundaryConditions();
        initializeModel();
        initializeStepCondition();

        prices = intrinsicValues;
        if (lastDateIsResTime) {
            executeIntermediateStep(dateNumber - 1);
        }

        int j = lastIndex;
        do {
            if (j == (dateNumber - 1)) {
                beginDate = getResidualTime();
            } else {
                beginDate = getDividendTime(j + 1);
            }

            if (j >= 0) {
                endDate = getDividendTime(j);
            } else {
                endDate = dt;
            }

            prices.setValues(model.rollback(prices.values(), beginDate, endDate, timeStepPerPeriod, stepCondition));

            if (j >= 0) {
                executeIntermediateStep(j);
            }
        } while (--j >= firstIndex);

        prices.setValues(model.rollback(prices.values(),dt, 0, 1, stepCondition));
        
        if (firstDateIsZero) {
            executeIntermediateStep(0);
        }

        results.value = prices.valueAtCenter();
        results.delta = prices.firstDerivativeAtCenter();
        results.gamma = prices.secondDerivativeAtCenter();
        results.addAdditionalResult("priceCurve", prices);
    }
    
}
