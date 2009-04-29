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
import org.jquantlib.lang.annotation.Real;
import org.jquantlib.lang.annotation.Time;
import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.NoConstraint;
import org.jquantlib.model.Parameter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;

/**
 * 
 * @author Praneet Tiwari
 */
public class TermStructureFittingParameter extends Parameter {

    public TermStructureFittingParameter(Parameter.Impl impl) {   
        super(0, impl, new NoConstraint());
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    // TODO: write this constructor

    public TermStructureFittingParameter(final Handle<YieldTermStructure> term) {
        super(0,
        // boost::shared_ptr<Parameter::Impl>(new NumericalImpl(term)),
                // new Parameter.Impl(),
                new NumericalImpl(term), new NoConstraint());
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    public static class NumericalImpl extends Parameter.Impl {

        private ArrayList<Integer /* @Time */> times_;
        private ArrayList<Double /* @Real */> values_;
        private Handle<YieldTermStructure> termStructure_;
        
        public NumericalImpl(){
            if (System.getProperty("EXPERIMENTAL") == null) {
                throw new UnsupportedOperationException("Work in progress");
            }
        }
        @Override
        public double value(Array params, double t) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public NumericalImpl(final Handle<YieldTermStructure> termStructure) {
            this.times_ = new ArrayList<Integer /* @Time */>();
            this.values_ = new ArrayList<Double /* @Real */>();
            this.termStructure_ = termStructure;
        }

        public void set(Integer /* @Time */t, Double /* @Real */x) {
            // times_.push_back(t);
            // values_.push_back(x);
            times_.add(t);
            values_.add(x);
        }

        public void change(Double /* @Real */x) {
            // values_.back() = x;
            // TODO: fix this 1
            values_.set(1, x);
        }

        public void reset() {
            times_.clear();
            values_.clear();
        }

        public Double /* @Real */value(final Array a, Time t) {
            /*
             * std::vector<Time>::const_iterator result = std::find(times_.begin(), times_.end(), t);
             * QL_REQUIRE(result!=times_.end(), "fitting parameter not set!"); return values_[result - times_.begin()];
             */
            // to hell with this statement...
            return values_.get(0);
        }

        public Handle<YieldTermStructure> termStructure() {
            return termStructure_;
        }
    }
}
