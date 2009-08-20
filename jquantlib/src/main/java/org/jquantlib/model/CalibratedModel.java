/*
Copyright (C) 2009 Ueli Hofstetter

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

package org.jquantlib.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.primitives.list.impl.ArrayDoubleList;
import org.jquantlib.QL;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.EndCriteria;
import org.jquantlib.math.optimization.OptimizationMethod;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;


/**
 * Calibrated model class
 *
 * @author Ueli Hofstetter
 */
//TODO: comments, license, code review
public abstract class CalibratedModel implements org.jquantlib.util.Observer, Observable {

    private static final String parameter_array_to_small = "parameter array to small";
    private static final String parameter_array_to_big = "parameter array to big";

    private final DefaultObservable delegatedObservable = new DefaultObservable(this);

    protected List<Parameter> arguments_;
    protected Constraint constraint_;

    protected CalibratedModel() {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
    }

    public CalibratedModel(final int nArguments){
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");

        arguments_ = new ArrayList<Parameter>(nArguments);
        constraint_ = new PrivateConstraint(arguments_);
    }



    /**
     * Calibrate to a set of market instruments (caps/swaptions)
     * <p>
     * An additional constraint can be passed which must be
     * satisfied in addition to the constraints of the model.
     *
     * @param calibrationHelper
     * @param method
     * @param endCriteria
     * @param constraint
     * @param weights
     */
    public  void calibrate(final List<CalibrationHelper> calibrationHelper,
            final OptimizationMethod method,
            final EndCriteria endCriteria,
            final Constraint constraint,
            final List<Double> weights) {

        throw new UnsupportedOperationException();

    }

    /**
     * Calibrate to a set of market instruments (caps/swaptions)
     * <p>
     * An additional constraint can be passed which must be
     * satisfied in addition to the constraints of the model.
     *
     * @param calibrationHelper
     * @param method
     * @param endCriteria
     * @param constraint
     * @param weights
     */
    public  void calibrate(final List<CalibrationHelper> calibrationHelper,
            final OptimizationMethod method,
            final EndCriteria endCriteria){
        //FIXME: what kind of constraint? so far constraint it abstract!?
        calibrate(calibrationHelper, method, endCriteria, new PrivateConstraint(null), new ArrayDoubleList() );
    }

    public Constraint constraint(){
        return constraint_;
    }

    public Array params()  {
        int size = 0, i;
        for (i=0; i<arguments_.size(); i++)
            size += arguments_.get(i).getSize();
        final Array params = new Array(size);
        int k = 0;
        for (i=0; i<arguments_.size(); i++)
            for (int j=0; j<arguments_.get(i).getSize(); j++, k++)
                params.set(k, arguments_.get(i).getParams().get(k));
        return params;
    }

    // TODO: code review :: please verify against QL/C++ code
    public void setParams(final Array params) {
        //Array::const_iterator p = params.begin();
        int p = 0;
        for (int i=0; i<arguments_.size(); i++)
            for (int j=0; j<arguments_.get(i).getSize(); j++, p++) {
                QL.require(p>params.size() , parameter_array_to_small); // QA:[RG]::verified // TODO: message
                arguments_.get(i).setParam(j, params.get(p));
            }
        QL.require(p == params.size() , parameter_array_to_big); // QA:[RG]::verified // TODO: message
        update();
    }

    //FIXME: to be added
    public  void generateArguments(){
        throw new UnsupportedOperationException("work in progress");
    }

    public void update(){
        generateArguments();
        notifyObservers();
    }

    //
    // implements Observer
    //

    @Override
    public void registerWith(final Observable o) {
        o.addObserver(this);
    }

    @Override
    public void unregisterWith(final Observable o) {
        o.deleteObserver(this);
    }

    @Override
    public void update(final Observable o, final Object arg) {
        generateArguments();
        notifyObservers();
    }


    //
    // implements Observable
    //

    @Override
    public void addObserver(final org.jquantlib.util.Observer observer) {
        delegatedObservable.addObserver(observer);

    }

    @Override
    public int countObservers() {
        return delegatedObservable.countObservers();
    }

    @Override
    public void deleteObserver(final org.jquantlib.util.Observer observer) {
        delegatedObservable.deleteObserver(observer);

    }

    @Override
    public void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    @Override
    public List<org.jquantlib.util.Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

    @Override
    public void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    @Override
    public void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);

    }


    //
    // private inner classes
    //

    private class CalibrationFunction extends org.jquantlib.math.optimization.CostFunction{

        private final CalibratedModel model_;
        private final List<CalibrationHelper> instruments;
        private final List<Double> weights_;

        public CalibrationFunction(final CalibratedModel model, final List<CalibrationHelper> instruments, final List<Double> weights){
            this.model_ = model;
            this.instruments = instruments;
            this.weights_ = weights;
        }

        @Override
        public double value(final Array params) {
            //FIXME: certainly not the way it is intended to be
            this.model_.setParams(params);
            double value = 0.0;
            for(int i = 0; i<instruments.size(); i++){
                final double diff = instruments.get(i).calibrationError();
                value += diff*diff*weights_.get(i);
            }
            return Math.sqrt(value);
        }

        @Override
        public Array values(final Array params) {
            this.model_.setParams(params);

            final Array values = new Array(instruments.size());
            for(int i = 0; i<instruments.size(); i++)
                values.set(i, instruments.get(i).calibrationError()*Math.sqrt(weights_.get(i)));

            return values;
        }

        @Override
        public double finiteDifferenceEpsilon(){
            return 1e-6;
        }

    }


    private final class PrivateConstraint extends Constraint{

        public PrivateConstraint(final List<Parameter> arguments) {
            arguments_ = arguments; // TODO: code review: using the outermost variable???
        }

        @Override
        public boolean test(final Array array) /* @ReadOnly */ {
            int k = 0;
            for (int i = 0; i < arguments_.size(); i++) {
                final int size = arguments_.get(i).getSize();
                final Array testParams = new Array(size);
                for (int j = 0; j < size; j++, k++)
                    testParams.set(j, array.get(k));
                if (!arguments_.get(i).testParams(testParams))
                    return false;
            }
            return true;
        }
    }

}

