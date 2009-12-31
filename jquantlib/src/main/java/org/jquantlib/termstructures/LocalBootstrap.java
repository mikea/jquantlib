/*
Copyright (C) 2009 John Martin

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


package org.jquantlib.termstructures;

import java.util.Collections;
import java.util.List;
import org.jquantlib.QL;
import org.jquantlib.instruments.Instrument;
import org.jquantlib.termstructures.yieldcurves.BootstrapTraits;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.termstructures.Bootstrapable;
import org.jquantlib.termstructures.BootstrapHelperSorter;
import org.jquantlib.time.Date;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.CostFunction;
import org.jquantlib.math.optimization.EndCriteria;
import org.jquantlib.math.optimization.LevenbergMarquardt;
import org.jquantlib.math.optimization.NoConstraint;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.math.optimization.Problem;
import org.jquantlib.termstructures.Bootstrapper;

public class LocalBootstrap implements Bootstrapper
{
    private class PenaltyFunction extends CostFunction
    {
        private int initialIndex;
        private int rateHelpersStart;
        private int rateHelpersEnd;
        private int penaltylocalisation;
        
        public PenaltyFunction (int initialIndex, int rateHelpersStart, int rateHelpersEnd)
        {
            this.initialIndex = initialIndex;
            this.rateHelpersStart = rateHelpersStart;
            this.rateHelpersEnd = rateHelpersEnd;
            this.penaltylocalisation = rateHelpersEnd - rateHelpersStart;
        }

        public double value (final Array x)
        {
            int i = initialIndex;
            int guessIt = 0;
            for (;guessIt < x.size(); ++guessIt, ++i)
            {
                traits.updateGuess (bootstrapable.getData(), guessIt, i);
            }

            bootstrapable.getInterpolation().update();

            double penalty = 0.0;
            int j = rateHelpersStart;
            for (; j != rateHelpersEnd; ++j)
            {
                penalty += Math.abs (instruments[j].quoteError());
            }
            return penalty;
        }

        public Array values (final Array x)
        {
            int i = initialIndex;
            int guessIt = 0;
            for (;guessIt < x.size(); ++guessIt, ++i)
            {
                traits.updateGuess (bootstrapable.getData(), x.get (guessIt), i);
            }
            bootstrapable.getInterpolation().update();
            Array penalties = new Array (penaltylocalisation);
            int instIterator = rateHelpersStart;
            int penIt = 0;
            for (; instIterator != rateHelpersEnd; ++ instIterator, ++ penIt)
            {
                penalties.set (penIt, Math.abs (instruments[instIterator].quoteError()));
            }
            return penalties;
        }
    }
    
    private boolean validCurve;
    
    private YieldTermStructure termStructure;
    
    private Bootstrapable bootstrapable;
    
    private RateHelper [] instruments;
    
    private final int localisation;
    
    private boolean forcePositive;
    
    private BootstrapTraits traits;

    public LocalBootstrap ()
    {
        this (2, true);
    }

    public LocalBootstrap (int localisation, boolean forcePositive)
    {
        this.localisation = localisation;
        this.forcePositive = forcePositive;
        throw new UnsupportedOperationException ("work in progress...");
    }

    public void setup (YieldTermStructure termStructure, Bootstrapable bootstrapable, 
                       RateHelper [] instruments, BootstrapTraits traits)
    {
        QL.ensure (termStructure != null, "TermStructure cannot be null");
        this.termStructure = termStructure;
        this.bootstrapable = bootstrapable;
        this.instruments = instruments;
        this.traits = traits;
        int n = instruments.length;
        // FIXME
        QL.require (n >= 2, "Not enough instruments provided");
        QL.ensure (termStructure != null, "TermStructure cannot be null");
        // FIXME

        //QL.require (n >= InterpolatorType::requiredPoints, "Not enough instruments provided");

        QL.require (n >= localisation, "Not enough instruments provided");
        for (RateHelper i : instruments)
        {
            termStructure.addObserver (i);
        }
    }

    public void calculate ()
    {
        validCurve = false;
        int isize = instruments.length;
        
        // checkInstruments must ensure this
        //Collections.sort (instruments, new BootstrapHelperSorter <RateHelper> ());

        Array data = bootstrapable.getData();
        Date [] dates  = bootstrapable.getDates();
        Array times = bootstrapable.getTimes();

        // assert no instruments with same maturity, again now done in check instruments
        /*
        for (int i = 1; i < isize; ++ i)
        {
            Date m1 = instruments[i].latestDate();
            Date m2 = instruments[i - 1].latestDate();
            QL.require (! m1.equals (m2), "Two instruments cannot have the same maturity");
        }
        */

        // assert no invalid quotes
        for (RateHelper i : instruments)
        {
            // set term structure
            QL.ensure (i.quoteIsValid(), " Instrument cannot have invalid quote.");
            i.setTermStructure (termStructure);
        }
        
        if (validCurve)
        {
            QL.ensure (data.size() == isize + 1, "Dimensions mismatch expected");
        }
        else
        {
            bootstrapable.resetData (data.size());
            data = bootstrapable.getData();
            data.set (0, traits.initialValue ());
        }


        dates[0] = traits.initialDate (termStructure);
        times.set (0, termStructure.timeFromReference (dates[0]));
        for (int i = 0; i < isize - 1; ++ i)
        {
            dates[i + 1] = instruments[i].latestDate();
            times.set (i + 1, termStructure.timeFromReference (dates[i+1]));
            if (! validCurve)
            {
                data.set (i + 1, data.get(i));
            }
        }

        LevenbergMarquardt solver = new LevenbergMarquardt (traits.getAccuracy(), 
                traits.getAccuracy(), 
                traits.getAccuracy());

        EndCriteria endCriteria = new EndCriteria (100, 10, 0.00, traits.getAccuracy (), 0.00);
        Constraint solverConstraint = (Constraint)(forcePositive ? 
                    new PositiveConstraint() : new NoConstraint());
        int i = localisation -1;
        //FIXME, convexmonotone interpolation? 
        int dataAdjust = 1;
        
        for (; i < isize; ++ i)
        {
            int initialDataPoint = i + 1 - localisation + dataAdjust;
            Array startArray = new Array (localisation + 1 - dataAdjust);
            for (int j = 0; j < startArray.size() - 1; ++ j)
            {
                startArray.set (j, data.get (initialDataPoint + j));

                // here we are extending the interpolation a point at a
                // time... but the local interpolator can make an
                // approximation for the final localisation period.
                // e.g. if the localisation is 2, then the first section
                // of the curve will be solved using the first 2
                // instruments... with the local interpolator making
                // suitable boundary conditions.

                /*
                           ts_->interpolation_ =
                               ts_->interpolator_.localInterpolate(
                                                    ts_->times_.begin(),
                                              ts_->times_.begin()+(iInst + 2),
                                              ts_->data_.begin(),
                                              localisation_,
                                              ts_->interpolation_,
                                              nInsts+1);
                */
                //bootstrapable.getInterpolation ().update ();
                
                bootstrapable.setInterpolation 
                    (bootstrapable.getInterpolator().interpolate (i + 2,
                         times.constIterator (), data.constIterator ()));


                if (i >= localisation)
                {
                    startArray.set (localisation - dataAdjust, traits.guess (termStructure, dates[i]));
                }
                else
                {
                    startArray.set (localisation - dataAdjust, data.get(0));
                }

                PenaltyFunction currentCost = new PenaltyFunction(initialDataPoint,
                                                     (i - localisation + 1),
                                                     (i + 1));

                Problem toSolve = new Problem((CostFunction) currentCost, solverConstraint, startArray);
                EndCriteria.Type endType = solver.minimize (toSolve, endCriteria);

                QL.require (endType == EndCriteria.Type.StationaryFunctionAccuracy || 
                            endType == EndCriteria.Type.StationaryFunctionValue, 
                            "Unable to strip yieldcurve to required accuracy");
            }
        }
        validCurve = true;
    }

}