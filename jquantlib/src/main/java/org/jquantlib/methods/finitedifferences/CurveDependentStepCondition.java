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

import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.math.Array;

public class CurveDependentStepCondition implements StepCondition<Array> {

    public static interface CurveWrapper {
        double getValue(Array a, int i);
    }

    private CurveWrapper curveItem;

    public CurveDependentStepCondition(Option.Type type, double strike) {
        curveItem = new PayoffWrapper(type, strike);
    }

    public CurveDependentStepCondition(Payoff p) {
        curveItem = new PayoffWrapper(p);
    }

    public CurveDependentStepCondition(Array a) {
        curveItem = new ArrayWrapper(a);
    }

    protected double applyToValue(double a, double b) {
        throw new RuntimeException("not yet implemented");
    }

    @Override
    public void applyTo(Array a, double t) {
        for (int i = 0; i < a.size(); i++) {
            a.set(i, applyToValue(a.get(i), getValue(a, i)));
        }
    }

    protected double getValue(Array a, int index) {
        return curveItem.getValue(a, index);
    }

    static class ArrayWrapper implements CurveWrapper {
        private Array values;

        public ArrayWrapper(Array values) {
            this.values = new Array(values);
        }

        public double getValue(Array a, int i) {
            return values.get(i);
        }
    };

    static class PayoffWrapper implements CurveWrapper {
        private Payoff payoff;

        public PayoffWrapper(Payoff p) {
            this.payoff = p;
        }

        public PayoffWrapper(Option.Type type, double strike) {
            payoff = new PlainVanillaPayoff(type, strike);
        }

        public double getValue(Array a, int i) {
            return payoff.valueOf(a.get(i));
        }
    };
}