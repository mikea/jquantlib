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
import org.jquantlib.math.Matrix;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.processes.StochasticProcess1D;

/**
 * 
 * @author Praneet Tiwari
 */
public class StochasticProcessArray extends StochasticProcess {

    protected ArrayList<StochasticProcess1D> processes_;
    protected Matrix sqrtCorrelation_;

    public StochasticProcessArray(final ArrayList<StochasticProcess1D> processes, final Matrix correlation) {
        this.processes_ = processes;
        this.sqrtCorrelation_ = correlation;
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        /*
         * : processes_(processes), sqrtCorrelation_(pseudoSqrt(correlation,SalvagingAlgorithm::Spectral))
         * 
         * QL_REQUIRE(!processes.empty(), "no processes given"); QL_REQUIRE(correlation.rows() == processes.size(),
         * "mismatch between number of processes " "and size of correlation matrix"); for (Size i=0; i<processes_.size(); i++)
         * registerWith(processes_[i]);
         */
    }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double[] initialValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double[] drift(double t, double[] x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double[][] diffusion(double t, double[] x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /***
     * Size StochasticProcessArray::size() const { return processes_.size(); }
     * 
     * Disposable<Array> StochasticProcessArray::initialValues() const { Array tmp(size()); for (Size i=0; i<size(); ++i) tmp[i] =
     * processes_[i]->x0(); return tmp; }
     * 
     * Disposable<Array> StochasticProcessArray::drift(Time t, const Array& x) const { Array tmp(size()); for (Size i=0; i<size();
     * ++i) tmp[i] = processes_[i]->drift(t, x[i]); return tmp; }
     * 
     * Disposable<Matrix> StochasticProcessArray::diffusion( Time t, const Array& x) const { Matrix tmp = sqrtCorrelation_; for
     * (Size i=0; i<size(); ++i) { Real sigma = processes_[i]->diffusion(t, x[i]); std::transform(tmp.row_begin(i), tmp.row_end(i),
     * tmp.row_begin(i), std::bind2nd(std::multiplies<Real>(),sigma)); } return tmp; }
     * 
     * Disposable<Array> StochasticProcessArray::expectation(Time t0, const Array& x0, Time dt) const { Array tmp(size()); for (Size
     * i=0; i<size(); ++i) tmp[i] = processes_[i]->expectation(t0, x0[i], dt); return tmp; }
     * 
     * Disposable<Matrix> StochasticProcessArray::stdDeviation(Time t0, const Array& x0, Time dt) const { Matrix tmp =
     * sqrtCorrelation_; for (Size i=0; i<size(); ++i) { Real sigma = processes_[i]->stdDeviation(t0, x0[i], dt);
     * std::transform(tmp.row_begin(i), tmp.row_end(i), tmp.row_begin(i), std::bind2nd(std::multiplies<Real>(),sigma)); } return
     * tmp; }
     * 
     * Disposable<Matrix> StochasticProcessArray::covariance(Time t0, const Array& x0, Time dt) const { Matrix tmp =
     * stdDeviation(t0, x0, dt); return tmp*transpose(tmp); }
     * 
     * Disposable<Array> StochasticProcessArray::evolve( Time t0, const Array& x0, Time dt, const Array& dw) const { const Array dz
     * = sqrtCorrelation_ * dw;
     * 
     * Array tmp(size()); for (Size i=0; i<size(); ++i) tmp[i] = processes_[i]->evolve(t0, x0[i], dt, dz[i]); return tmp; }
     * 
     * Disposable<Array> StochasticProcessArray::apply(const Array& x0, const Array& dx) const { Array tmp(size()); for (Size i=0;
     * i<size(); ++i) tmp[i] = processes_[i]->apply(x0[i],dx[i]); return tmp; }
     * 
     * Time StochasticProcessArray::time(const Date& d) const { return processes_[0]->time(d); }
     * 
     * const boost::shared_ptr<StochasticProcess1D>& StochasticProcessArray::process(Size i) const { return processes_[i]; }
     * 
     * Disposable<Matrix> StochasticProcessArray::correlation() const { return sqrtCorrelation_ * transpose(sqrtCorrelation_); }
     */
}
