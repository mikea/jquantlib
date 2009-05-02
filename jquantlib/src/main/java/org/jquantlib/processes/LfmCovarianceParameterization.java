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

package org.jquantlib.processes;

import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.GaussKronrodAdaptive;
import org.jquantlib.util.stdlibc.Std;

public abstract class LfmCovarianceParameterization {
    protected int size_;
    private int factors_;

    public LfmCovarianceParameterization(int size, int factors) {
        this.size_ = size;
        this.factors_ = factors;
    }

    public int size() {
        return size_;
    }

    public int factors() {
        return factors_;
    }

    public abstract Matrix diffusion(/* @Time */double t, final Array x);

    public Matrix diffusion(/* @Time */double t) {
        return diffusion(t, new Array());
    }

    public Matrix covariance(/* @Time */double t, final Array x) {
        Matrix sigma = this.diffusion(t, x);
        Matrix result = sigma.operatorMultiply(sigma, sigma.transpose(sigma));
        return result;
    }

    public Matrix covariance(/* @Time */double t) {
        return diffusion(t, new Array());
    }

    public Matrix integratedCovariance(/* @Time */double t, final Array x) {
        // this implementation is not intended for production.
        // because it is too slow and too inefficient.
        // This method is useful for testing and R&D.
        // Please overload the method within derived classes.
        if (x.empty()) {
            throw new IllegalArgumentException("can not handle given x here");
        }

        Matrix tmp = new Matrix(size_, size_, 0.0);

        for (int i = 0; i < size_; ++i) {
            for (int j = 0; j <= i; ++j) {
                Var_Helper helper = new Var_Helper(this, i, j);
                GaussKronrodAdaptive integrator = new GaussKronrodAdaptive(1e-10, 10000);
                for(int k = 0; k<64; ++k){
                    tmp.set(i, j, tmp.get(i, j)+integrator.evaluate(helper, k*t/64.0,(k+1)*t/64.0));
                }
                tmp.set(j,i, tmp.get(i, j));
            }
        }

        return tmp;
    }

    public Matrix integratedCovariance(/* @Time */double t) {
        return integratedCovariance(t, new Array());
    }

    private static class Var_Helper implements UnaryFunctionDouble {

        private int i_, j_;
        private final LfmCovarianceParameterization param_;

        public Var_Helper(LfmCovarianceParameterization param, int i, int j) {
            this.i_ = i;
            this.j_ = j;
            this.param_ = param;
        }

        public double evaluate(double t) {
            final Matrix m = param_.diffusion(t);
            return Std.inner_product(new Array(m.getRow(i_)), new Array(m.getRow(j_)));
        }
    }

}
