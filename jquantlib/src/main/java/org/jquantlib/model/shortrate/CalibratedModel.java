/*
 Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.model.shortrate;

import java.util.ArrayList;
import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.CompositeConstraint;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.model.shortrate.EndCriteria.Type;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

/**
 * 
 * @author Praneet Tiwari
 */
public abstract class CalibratedModel implements Observer, Observable {

    protected ArrayList<Parameter> arguments;
    protected ArrayList<Parameter> arguments_;
    protected Constraint constraint_;
    protected EndCriteria.Type shortRateEndCriteria_;

    public CalibratedModel(int nArguments) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        generateArguments();
        notifyObservers();
    }

    // this is the real update method, the previous one was for satisfying hirerachy
    public void update() {
        generateArguments();
        notifyObservers();
    }

    protected void generateArguments() {
    }

    // ! Calibrate to a set of market instruments (caps/swaptions)
    /*
     * ! An additional constraint can be passed which must be satisfied in addition to the constraints of the model.
     * 
     * public void calibrate( const std::vector<boost::shared_ptr<CalibrationHelper> >&, OptimizationMethod& method, const
     * EndCriteria& endCriteria, const Constraint& constraint = Constraint(), const std::vector<Real>& weights =
     * std::vector<Real>());
     * 
     * Real value(const Array& params, const std::vector<boost::shared_ptr<CalibrationHelper> >&);
     * 
     * const boost::shared_ptr<Constraint>& constraint() const; //! returns end criteria result EndCriteria::Type endCriteria(); //!
     * Returns array of arguments on which calibration is done Disposable<Array> params() const;
     * 
     * virtual void setParams(const Array& params);
     */

    public/* Disposable<Array> */Array params() {
        int /* @Size */size = 0, i;
        for (i = 0; i < arguments_.size(); i++) {
            size += arguments_.get(i).getSize(); // size();
        }
        Array params = new Array(size);
        int /* @Size */k = 0;
        for (i = 0; i < arguments_.size(); i++) {
            for (int /* @Size */j = 0; j < arguments_.get(i).getSize(); j++, k++) {
                // params[k] = arguments_[i].params()[j];
                // TODO: params.set(k, arguments_.get(i).params(). ) ;
            }
        }
        return params;
    }

    public void calibrate(ArrayList<CalibrationHelper> instruments, OptimizationMethod method, final EndCriteria endCriteria,
            final Constraint additionalConstraint, final ArrayList<Double /* @Real */> weights) {

        if (weights.isEmpty() || weights.size() == instruments.size()) {
            throw new IllegalArgumentException("mismatch between number of instruments and weights");
        }

        Constraint c;
        if (additionalConstraint.empty()) {
            c = constraint_;
        } else {
            c = new CompositeConstraint(constraint_, additionalConstraint);
        }

        ArrayList<Double /* @Real */> w = weights.isEmpty() ? new ArrayList<Double /* @Real */>(instruments.size())/* , 1.0) */
                : weights;

        // TODO: WRITE CALIBRATIONFUCTION CLASS AS AN INNER CLASS
        CalibrationFunction f = new CalibrationFunction(this, instruments, w);

        Problem prob = new Problem(f, c, params());
        shortRateEndCriteria_ = method.minimize(prob, endCriteria);
        Array result = new Array(prob.currentValue());
        setParams(result);
        Array shortRateProblemValues_ = new Array(prob.values(result));

        notifyObservers();
    }

    Double /* @Real */value(final Array params, final ArrayList<CalibrationHelper> instruments) {
        ArrayList<Double /* @Real */> w = new ArrayList<Double /* @Real */>(instruments.size());// , 1.0);
        CalibrationFunction f = new CalibrationFunction(this, instruments, w);
        return f.value(params);
    }

    public Constraint constraint() {
        return constraint_;

    }

    // ! returns end criteria result

    public EndCriteria.Type endCriteria() {
        return shortRateEndCriteria_;
    }

    public void setParams(final Array params) {
        // TODO
        // Array::const_iterator p = params.begin();
        // for (int /*@Size*/ i=0; i<arguments_.size(); ++i) {
        // for (int /*@Size*/ j=0; j<arguments_get(i).size(); ++j, ++p) {
        // QL_REQUIRE(p!=params.end(),"parameter array too small");
        // arguments_[i].setParam(j, *p);
        // }
        // }
        // QL_REQUIRE(p==params.end(),"parameter array too big!");
        // update();
    }

    protected class CalibrationFunction extends CostFunction {

        private CalibratedModel model_;
        final ArrayList<CalibrationHelper> instruments_;
        ArrayList<Double /* @Real */> weights_;

        public CalibrationFunction(CalibratedModel model, final ArrayList<CalibrationHelper> instruments,
                final ArrayList<Double /* @Real */> weights) {
            if (System.getProperty("EXPERIMENTAL") == null) {
                throw new UnsupportedOperationException("Work in progress");
            }
            this.model_ = model;
            this.instruments_ = instruments;
            this.weights_ = weights;

        }

        @Override
        public Double /* @Real */value(final Array params) {
            model_.setParams(params);

            Double /* @Real */value = 0.0;
            for (int /* @Size */i = 0; i < instruments_.size(); i++) {
                Double /* @Real */diff = instruments_.get(i).calibrationError();
                value += diff * diff * weights_.get(i);
            }

            return Math.sqrt(value);
        }

        @Override
        public/* Disposable<Array> */Array values(final Array params) {
            model_.setParams(params);

            Array values = new Array(instruments_.size());
            for (int /* @Size */i = 0; i < instruments_.size(); i++) {
                values.set(i, instruments_.get(i).calibrationError() * Math.sqrt(weights_.get(i)));
            }

            return values;
        }

        public double /* @Real */finiteDifferenceEpsilon() {
            return 1e-6;
        }

        @Override
        public Type minimize(Problem P, EndCriteria endCriteria) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    } // CalibrationFunction
}
