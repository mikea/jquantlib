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

import org.jquantlib.QL;
import org.jquantlib.cashflow.Event;
import org.jquantlib.math.SampledCurve;
import org.jquantlib.math.matrixutilities.Array;
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

    public FDMultiPeriodEngine(final GeneralizedBlackScholesProcess process, final int timeSteps, final int gridPoints, final boolean timeDependent) {
        super(process, timeSteps, gridPoints, timeDependent);
    }

    public FDMultiPeriodEngine(final GeneralizedBlackScholesProcess process) {
        super(process, 100, 100, false);
    }

    public void setupArguments(final Arguments args, final List<Event> schedule){
        super.setupArguments(args);
        events = schedule;
        stoppingTimes.clear();
        final int n = schedule.size();
        for(int i = 0; i<n; i++)
            stoppingTimes.add(process.time(events.get(i).date()));
    }


    //
    // abstract methods
    //

    protected abstract void executeIntermediateStep(int step);


    //
    // private methods
    //

    private double getDividendTime(final int i){
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
        final OneAssetOptionArguments args = (OneAssetOptionArguments) a;
        events.clear();
        final int n = args.exercise.size();
        for (int i=0; i<n; ++i)
            stoppingTimes.add(process.time(args.exercise.date(i)));
    }

    @Override
    public void calculate(final Results r){
        final OneAssetOptionResults results = (OneAssetOptionResults) r;
        double beginDate, endDate;
        final int dateNumber = stoppingTimes.size();
        boolean lastDateIsResTime = false;
        int firstIndex = -1;
        int lastIndex = dateNumber - 1;
        boolean firstDateIsZero = false;
        double firstNonZeroDate = getResidualTime();

        final double dateTolerance = 1e-6;

        if (dateNumber > 0) {
            // TODO: code review :: please verify against QL/C++ code
            QL.require(getDividendTime(0) > 0 , "first date cannot be negative"); // QA:[RG]::verified // TODO: message
            if (getDividendTime(0) < getResidualTime() * dateTolerance) {
                firstDateIsZero = true;
                firstIndex = 0;
                if (dateNumber >= 2)
                    firstNonZeroDate = getDividendTime(1);
            }
            if (Math.abs(getDividendTime(lastIndex) - getResidualTime()) < dateTolerance) {
                lastDateIsResTime = true;
                lastIndex = dateNumber - 2;
            }

            if (!firstDateIsZero)
                firstNonZeroDate = getDividendTime(0);

            if (dateNumber >= 2)
                for (int j = 1; j < dateNumber; j++)
                    QL.require(getDividendTime(j - 1) < getDividendTime(j) , "dates must be in strictly increasing order"); // QA:[RG]::verified // TODO: message
        }

        double dt = getResidualTime()/(timeStepPerPeriod*(dateNumber+1));

        // Ensure that dt is always smaller than the first non-zero date
        if (firstNonZeroDate <= dt)
            dt = firstNonZeroDate / 2.0;

        setGridLimits();
        initializeInitialCondition();
        initializeOperator();
        initializeBoundaryConditions();
        initializeModel();
        initializeStepCondition();

        prices = intrinsicValues;
        if (lastDateIsResTime)
            executeIntermediateStep(dateNumber - 1);

        int j = lastIndex;
        do {
            if (j == (dateNumber - 1))
                beginDate = getResidualTime();
            else
                beginDate = getDividendTime(j + 1);

            if (j >= 0)
                endDate = getDividendTime(j);
            else
                endDate = dt;

            prices.setValues(model.rollback(prices.values(), beginDate, endDate, timeStepPerPeriod, stepCondition));

            if (j >= 0)
                executeIntermediateStep(j);
        } while (--j >= firstIndex);

        prices.setValues(model.rollback(prices.values(),dt, 0, 1, stepCondition));

        if (firstDateIsZero)
            executeIntermediateStep(0);

        results.value = prices.valueAtCenter();
        results.delta = prices.firstDerivativeAtCenter();
        results.gamma = prices.secondDerivativeAtCenter();
        results.addAdditionalResult("priceCurve", prices);
    }

}
