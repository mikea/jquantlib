/*
Copyright (C) 
2008 Praneet Tiwari
2009 Ueli Hofstetter

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

import java.util.List;

import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;
import org.jquantlib.math.matrixutilities.PseudoSqrt;
import org.jquantlib.math.matrixutilities.PseudoSqrt.SalvagingAlgorithm;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.util.Date;
import org.jquantlib.util.stdlibc.Std;

/**
 * 
 * @author Praneet Tiwari
 */
public class StochasticProcessArray extends StochasticProcess {
    
    private static final String no_process_given = "no process given";
    private static final String mismatch_processnumber_sizecorrelationmatrix =  "mismatch between number of processes and size of correlation matrix";

    protected List<StochasticProcess1D> processes_;
    protected Matrix sqrtCorrelation_;

    public StochasticProcessArray(final List<StochasticProcess1D> processes, final Matrix correlation) {
        this.processes_ = processes;
        this.sqrtCorrelation_ = PseudoSqrt.pseudoSqrt(correlation, SalvagingAlgorithm.Spectral);
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        
        if(processes.isEmpty()){
            throw new IllegalArgumentException(no_process_given);
        }
        
        if(correlation.rows() != processes.size()){
            throw new IllegalArgumentException(mismatch_processnumber_sizecorrelationmatrix);
        }
        
        for (int i=0; i<processes_.size(); i++){
            processes_.get(i).addObserver(this);
        }
    }
    
    public int  size()  {
        return processes_.size();
    }

    public double[] initialValues()  {
        double[] tmp = new double[size()];
        for (int i=0; i<size(); ++i){
            tmp[i] = processes_.get(i).x0();
        }
        return tmp;
    }

    public double[][] diffusion(/*Time*/ double t, final double [] x)  {
        Matrix tmp = sqrtCorrelation_;
        for (int i=0; i<size(); ++i) {
            double sigma = processes_.get(i).diffusion(t, x[i]);
            Std.getInstance().transform(tmp.getRow(i), tmp.getRow(i), Std.getInstance().multiplies(sigma));
        }
        return tmp.getRawData();
    }
    
    public double[] expectation(/*@Time*/double t0, final double[] x0, /*@Time*/double dt)  {
        double [] tmp = new double[size()];
        for (int i=0; i<size(); ++i){
            tmp[i] = processes_.get(i).expectation(t0, x0[i], dt);
        }
        return tmp;
    }

    public double[][] stdDeviation(/*@Time*/ double t0, double[] x0,
            /*@Time*/ double dt)  {
        Matrix tmp = sqrtCorrelation_;
        for (int i=0; i<size(); ++i) {
            double sigma = processes_.get(i).stdDeviation(t0, x0[i], dt);
            Std.getInstance().transform(tmp.getRow(i), tmp.getRow(i),Std.getInstance().multiplies(sigma));
        }
        return tmp.getRawData();
    }
    
    public double [][] covariance(/*@Time*/ double t0, double[] x0,
            /*@Time*/ double dt)  {
        Matrix tmp = new Matrix(stdDeviation(t0, x0, dt));
        return tmp.operatorMultiply(tmp, tmp.transpose(tmp)).getRawData();
    }

    public double[] evolve(
            /*@Time*/ double t0, final double [] x0, /*@Time*/double dt, final double[] dw)  {
        double [] dz = sqrtCorrelation_.operatorMultiply(sqrtCorrelation_, new Array(dw)).getData();

       double [] tmp = new double [size()];
        for (int i=0; i<size(); ++i){
            tmp[i] = processes_.get(i).evolve(t0, x0[i], dt, dz[i]);
        }
        return tmp;
    }

    public double [] apply(final  double [] x0,final  double [] dx)  {
        double [] tmp = new double[size()];
        for (int i=0; i<size(); ++i){
            tmp[i] = processes_.get(i).apply(x0[i],dx[i]);
        }
        return tmp;
    }
    
    public /*@Time*/ double time(final Date d)  {
        return processes_.get(0).getTime(d);
    }
    
    public StochasticProcess1D process(int i) {
        return processes_.get(i);
    }

    public double [][] correlation() {
        return sqrtCorrelation_.operatorMultiply(sqrtCorrelation_, sqrtCorrelation_.transpose(sqrtCorrelation_)).getRawData();
    }

    public double[] drift(/* @Time */double t, double[] x) {
        double[] tmp = new double[size()];
        for (int i = 0; i < size(); ++i)
            tmp[i] = processes_.get(i).drift(t, x[i]);
        return tmp;
    }

    @Override
    public int getSize() {
        return processes_.size();
    }


  
}
