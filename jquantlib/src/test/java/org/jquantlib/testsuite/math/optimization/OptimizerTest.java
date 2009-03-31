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

package org.jquantlib.testsuite.math.optimization;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.CostFunction;
import org.jquantlib.math.optimization.EndCriteria;
import org.jquantlib.math.optimization.OptimizationMethod;
import org.junit.Ignore;
import org.junit.Test;

public class OptimizerTest {
    
    @Ignore
    @Test
    public void testOptimizers(){
        System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        System.out.println("Testing optimizers...");
        
        //following block moved inside this method body
        List<CostFunction> costFunctions_ = new ArrayList<CostFunction>();
        List<Constraint> constraints_ = new ArrayList<Constraint>();
        List<Array> initialValues_ = new ArrayList<Array>();
        List<Integer> maxIterations_ = new ArrayList<Integer>();
        List<Integer> maxStationaryStateIterations_ = new ArrayList<Integer>();
        List<Double> rootEpsilons_ = new ArrayList<Double>();
        List<Double> functionEpsilons_ = new ArrayList<Double>();
        List<Double> gradientNormEpsilons_ = new ArrayList<Double>();
        List<EndCriteria> endCriterias_ = new ArrayList<EndCriteria>();
        List<OptimizationMethod> optimizationMethods_ = new ArrayList<OptimizationMethod>();
        List<Array> xMinExpected_ = new ArrayList<Array>();
        List<Array> yMinExpected_ = new ArrayList<Array>();
        
        //following block moved from setup() to here
        //keep stuff local... (move here from setup())
        // Cost function n. 1: 1D polynomial of degree 2 (parabolic function y=a*x^2+b*x+c)
        double a = 1; //require a>0
        double b = 1;
        double c = 1;
        
        Array coefficients = new Array();
        coefficients.set(0, c);
        coefficients.set(1, b);
        coefficients.set(2, a);
        
        //costFunctions_.add(new CostFunction(){

           
    }
    
    // Set up, for each cost function, all the ingredients for optimization:
    // constraint, initial guess, end criteria, optimization methods.
    private void setup(){
        //.... TODO: how to keep stuff set up here local? implemented inline as a workaround
    }
    
    class OneDimensionalPolynomDegreeN extends CostFunction{

        private Array coefficients_;
        private int polynominalDegree_;
        
        public OneDimensionalPolynomDegreeN(Array coefficients){
            this.coefficients_ = coefficients;
            this.polynominalDegree_ = coefficients.size() - 1;
        }
        
        @Override
        public double value(Array x) {
            if(x.size() != 1){
                throw new IllegalArgumentException("Independent variable must be 1 dimensional");
            }
            double y = 0;
            for(int i = 0; i<=polynominalDegree_; ++i){
                y += coefficients_.get(i)*Math.pow(x.get(0), i);
            }
            return y;
        }

        @Override
        public Array values(Array x) {
            if(x.size() != 1){
                throw new IllegalArgumentException("Independent variable must be 1 dimensional");
            }
            Array y = new Array();
            y.set(0, value(x));
            return y;
        }
        
    }

}
