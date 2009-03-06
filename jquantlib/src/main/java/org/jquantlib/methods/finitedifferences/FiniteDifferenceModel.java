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

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleOpenHashSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jquantlib.math.Array;
import org.jquantlib.util.reflect.TypeToken;

/**
 * @author Srinivas Hasti
 * 
 */
public class FiniteDifferenceModel<S extends Operator, T extends MixedScheme<S>> {
    private final T evolver;
    private final List<Double> stoppingTimes;

    public FiniteDifferenceModel(final S L, final List<BoundaryCondition<S>> bcs, final List<Double> stoppingTimes) {
        this.evolver = getEvolver(L, bcs);
        // This takes care of removing duplicates
        Set<Double> times = new DoubleOpenHashSet(stoppingTimes);
        this.stoppingTimes = new DoubleArrayList(times);
        // Now sort
        Collections.sort(stoppingTimes);
    }
    
    public FiniteDifferenceModel(final S L, final List<BoundaryCondition<S>> bcs) {
       this(L,bcs, new ArrayList<Double>());
    }

    public FiniteDifferenceModel(final T evolver, final List<Double> stoppingTimes) {
        this.evolver = evolver;
        // This takes care of removing duplicates
        Set<Double> times = new DoubleOpenHashSet(stoppingTimes);
        this.stoppingTimes = new DoubleArrayList(times);
        // Now sort
        Collections.sort(stoppingTimes);
    }

    public T getEvolver() {
        return evolver;
    }

    public <V extends Array> void rollback(final V a, final /*@Time*/ double from, final /*@Time*/double to, final int steps) {
        rollbackImpl(a, from, to, steps, null);
    }

    /*
     * ! solves the problem between the given times, applying a condition at every step. \warning being this a rollback, <tt>from</tt>
     * must be a later time than <tt>to</tt>.
     */
    public <V extends Array> void rollback(final V a, final /*@Time*/double from, final /*@Time*/double to, final int steps, final StepCondition<V> condition) {
        rollbackImpl(a, from, to, steps, condition);
    }

    private <V extends Array> void rollbackImpl(final V a, final /*@Time*/double from, final /*@Time*/double to, final int steps, final StepCondition<V> condition) {
        if (from <= to)
            throw new IllegalStateException("trying to roll back from " + from + " to " + to);

        /* @Time */double dt = (from - to) / steps;
        double t = from;
        evolver.setStep(dt);

        for (int i = 0; i < steps; ++i, t -= dt) {
            /* Time */double now = t, next = t - dt;
            boolean hit = false;
            for (int j = stoppingTimes.size() - 1; j >= 0; --j) {
                if (next <= stoppingTimes.get(j) && stoppingTimes.get(j) < now) {
                    // a stopping time was hit
                    hit = true;

                    // perform a small step to stoppingTimes_[j]...
                    evolver.setStep(now - stoppingTimes.get(j));
                    evolver.step(a, now);
                    if (condition != null)
                        condition.applyTo(a, stoppingTimes.get(j));
                    // ...and continue the cycle
                    now = stoppingTimes.get(j);
                }
            }
            // if we did hit...
            if (hit) {
                // ...we might have to make a small step to
                // complete the big one...
                if (now > next) {
                    evolver.setStep(now - next);
                    evolver.step(a, now);
                    if (condition != null)
                        condition.applyTo(a, next);
                }
                // ...and in any case, we have to reset the
                // evolver to the default step.
                evolver.setStep(dt);
            } else {
                // if we didn't, the evolver is already set to the
                // default step, which is ok for us.
                evolver.step(a, now);
                if (condition != null)
                    condition.applyTo(a, next);
            }
        }
    }

    protected T getEvolver(final S l, final List<BoundaryCondition<S>> bcs) {
        try {
            return (T) TypeToken.getClazz(this.getClass(),1).getConstructor(Operator.class, List.class).newInstance(l, bcs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
