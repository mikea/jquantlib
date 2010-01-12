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

import java.util.Arrays;

import org.jquantlib.QL;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.lang.reflect.ReflectConstants;
import org.jquantlib.lang.reflect.TypeTokenTree;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolation.Interpolator;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.CostFunction;
import org.jquantlib.math.optimization.EndCriteria;
import org.jquantlib.math.optimization.LevenbergMarquardt;
import org.jquantlib.math.optimization.NoConstraint;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.math.optimization.Problem;
import org.jquantlib.termstructures.yieldcurves.PiecewiseCurve;
import org.jquantlib.termstructures.yieldcurves.Traits;
import org.jquantlib.time.Date;

//FIXME: This class should be declared generic, like this:
//
//      class LocalBootstrap<Curve extends Traits.Curve> implements Bootstrap
//
// ... in spite that there's no real value on doing it unless strict API resemblance to QL/C++
//
public class LocalBootstrap implements Bootstrap {

    //
    // private fields
    //

    private boolean         validCurve;
    private PiecewiseCurve  ts;
    private RateHelper[]    instruments;

    private final int               localisation;
    private final boolean           forcePositive;

    private Traits          traits;
    private Interpolator    interpolator;
    private Interpolation   interpolation;


    //
    // final private fields
    //

    final private Class<?>  classB;


    //
    // public constructors
    //

    public LocalBootstrap(final int localisation, final boolean forcePositive) {
        this(new TypeTokenTree(LocalBootstrap.class).getElement(0), localisation, forcePositive);
    }

    public LocalBootstrap(final Class<?> klass, final int localisation, final boolean forcePositive) {
        QL.validateExperimentalMode();

        if (klass==null) {
            throw new LibraryException("null PiecewiseCurve"); // TODO: message
        }
        if (!PiecewiseCurve.class.isAssignableFrom(klass)) {
            throw new LibraryException(ReflectConstants.WRONG_ARGUMENT_TYPE);
        }
        this.classB = klass;

        this.validCurve = false;
        this.ts = null;
        this.localisation = localisation;
        this.forcePositive = forcePositive;
    }


    //
    // public methods
    //

    public void setup(final PiecewiseCurve ts) {

        QL.ensure (ts != null, "TermStructure cannot be null");
        if (!classB.isAssignableFrom(ts.getClass())) {
            throw new LibraryException(ReflectConstants.WRONG_ARGUMENT_TYPE);
        }

        this.ts            = ts;
        this.interpolator  = ts.interpolator();
        this.interpolation = ts.interpolation();
        this.traits        = ts.traits();
        this.instruments   = ts.instruments();

        final int n = instruments.length;
        QL.require(n+1 >= ts.interpolator().requiredPoints(), "not enough instruments provided");
        QL.require (n >= localisation, "Not enough instruments provided");

        for (int i=0; i<n; ++i) {
            instruments[i].addObserver(ts);
        }
    }

    public void calculate () {

        final int nInsts = instruments.length;
        final Date dates[] = ts.dates();
        /*@Time*/ final double times[] = ts.times();
        double data[] = ts.data();

        // ensure rate helpers are sorted
        Arrays.sort(instruments, new BootstrapHelperSorter());

        // check that there is no instruments with the same maturity
        for (int i=1; i<nInsts; ++i) {
            final Date m1 = instruments[i-1].latestDate();
            final Date m2 = instruments[i].latestDate();
            QL.require(m1 != m2, "two instruments have the same maturity");
        }

        // check that there is no instruments with invalid quote
        for (int i=0; i<nInsts; ++i) {
            QL.require(instruments[i].quoteIsValid(), " instrument has an invalid quote");
        }

        // setup instruments
        for (int i=0; i<nInsts; ++i) {
            // don't try this at home!
            // This call creates instruments, and removes "const".
            // There is a significant interaction with observability.
            instruments[i].setTermStructure(ts);
        }

        if (validCurve) {
            QL.ensure (data.length == nInsts + 1, "Dimensions mismatch expected");
        } else {
            data = new double[nInsts+1];
            data = ts.data();
            data[0] = traits.initialValue(ts);
        }


        dates[0] = traits.initialDate (ts);
        times[0] = ts.timeFromReference (dates[0]);
        for (int i = 0; i < nInsts - 1; ++ i) {
            dates[i + 1] = instruments[i].latestDate();
            times[i+1] = ts.timeFromReference (dates[i+1]);
            if (! validCurve) {
                data[i+1] = data[i];
            }
        }

        final LevenbergMarquardt solver = new LevenbergMarquardt (ts.accuracy(), ts.accuracy(), ts.accuracy());

        final EndCriteria endCriteria = new EndCriteria (100, 10, 0.00, ts.accuracy(), 0.00);
        final Constraint solverConstraint = (forcePositive ? new PositiveConstraint() : new NoConstraint());
        int i = localisation -1;
        //FIXME, convexmonotone interpolation?
        final int dataAdjust = 1;

        for (; i < nInsts; ++ i) {
            final int initialDataPoint = i + 1 - localisation + dataAdjust;
            final double startArray[] = new double[localisation+1-dataAdjust];
            for (int j=0; j < startArray.length-1; ++j) {
                startArray[j] = data[initialDataPoint+j];

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

                ts.setInterpolation(interpolator.interpolate(new Array(times, i+2), new Array(data)));

                if (i >= localisation) {
                    startArray[localisation-dataAdjust] = traits.guess(ts, dates[i]);
                } else {
                    startArray[localisation-dataAdjust] = data[0];
                }

                final PenaltyFunction currentCost = new PenaltyFunction(initialDataPoint, (i - localisation + 1), (i + 1));

                final Problem toSolve = new Problem(currentCost, solverConstraint, new Array(startArray));
                final EndCriteria.Type endType = solver.minimize (toSolve, endCriteria);

                QL.require (endType == EndCriteria.Type.StationaryFunctionAccuracy ||
                            endType == EndCriteria.Type.StationaryFunctionValue,
                            "Unable to strip yieldcurve to required accuracy");
            }
        }
        validCurve = true;
    }


    //
    // inner classes
    //

    private class PenaltyFunction extends CostFunction {
        private final int initialIndex;
        private final int rateHelpersStart;
        private final int rateHelpersEnd;
        private final int penaltylocalisation;

        public PenaltyFunction (final int initialIndex, final int rateHelpersStart, final int rateHelpersEnd) {
            this.initialIndex = initialIndex;
            this.rateHelpersStart = rateHelpersStart;
            this.rateHelpersEnd = rateHelpersEnd;
            this.penaltylocalisation = rateHelpersEnd - rateHelpersStart;
        }

        @Override
        public double value (final Array x) {
            int i = initialIndex;
            int guessIt = 0;
            for (;guessIt < x.size(); ++guessIt, ++i) {
                traits.updateGuess (ts.data(), guessIt, i);
            }

            ts.interpolation().update();

            double penalty = 0.0;
            int j = rateHelpersStart;
            for (; j != rateHelpersEnd; ++j)
            {
                penalty += Math.abs (instruments[j].quoteError());
            }
            return penalty;
        }

        @Override
        public Array values (final Array x) {
            int guessIt = 0;
            for (int i = initialIndex; guessIt < x.size(); ++guessIt, ++i) {
                traits.updateGuess (ts.data(), x.get (guessIt), i);
            }
            interpolation.update();
            final Array penalties = new Array (penaltylocalisation);
            int instIterator = rateHelpersStart;
            for (int penIt = 0; instIterator != rateHelpersEnd; ++ instIterator, ++ penIt) {
                penalties.set (penIt, Math.abs (instruments[instIterator].quoteError()));
            }
            return penalties;
        }
    }

}