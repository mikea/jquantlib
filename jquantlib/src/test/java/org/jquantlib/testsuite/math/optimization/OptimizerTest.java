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

import org.joda.primitives.list.impl.ArrayDoubleList;
import org.jquantlib.lang.annotation.Real;
import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.CostFunction;
import org.jquantlib.math.optimization.EndCriteria;
import org.jquantlib.math.optimization.LevenbergMarquardt;
import org.jquantlib.math.optimization.NoConstraint;
import org.jquantlib.math.optimization.OptimizationMethod;
import org.jquantlib.math.optimization.Problem;
import org.jquantlib.math.optimization.Simplex;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.fail;

public class OptimizerTest {

    enum OptimizationMethodType {
        simplex, levenbergMarquardt, conjugateGradient, steepestDescent
    };

    @Ignore
    @Test
    public void testOptimizers() {
        
        //System.setProperty("EXPERIMENTAL", "true");
        System.out.println("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
        System.out.println("Testing optimizers... cleaned");

        // following block moved inside this method body
        List<CostFunction> costFunctions_ = new ArrayList<CostFunction>();
        List<Constraint> constraints_ = new ArrayList<Constraint>();
        List<List> initialValues_ = new ArrayList<List>();
        List<Integer> maxIterations_ = new ArrayList<Integer>();
        List<Integer> maxStationaryStateIterations_ = new ArrayList<Integer>();
        List<Double> rootEpsilons_ = new ArrayList<Double>();
        List<Double> functionEpsilons_ = new ArrayList<Double>();
        List<Double> gradientNormEpsilons_ = new ArrayList<Double>();
        List<EndCriteria> endCriterias_ = new ArrayList<EndCriteria>();
        List<List<OptimizationMethod>> optimizationMethods_ = new ArrayList<List<OptimizationMethod>>();
        List<List> xMinExpected_ = new ArrayList<List>();
        List<List> yMinExpected_ = new ArrayList<List>();

        // following block moved from setup() to here
        // keep stuff local... (move here from setup())
        // Cost function n. 1: 1D polynomial of degree 2 (parabolic function y=a*x^2+b*x+c)
        double a = 1; // require a>0
        double b = 1;
        double c = 1;

        List coefficients = new ArrayDoubleList();
        coefficients.add(c);
        coefficients.add(b);
        coefficients.add( a);
        costFunctions_.add(new OneDimensionalPolynomDegreeN(coefficients));
        // Set Constraint for optimizers: unconstrained problem
        constraints_.add(new NoConstraint());
        // Set initial guess for optimizer
        List initialValue = new ArrayDoubleList();
        initialValue.add(-100.0);
        initialValues_.add(initialValue);
        // Set end criteria for optimizer
        maxIterations_.add(1000); // maxIterations
        maxStationaryStateIterations_.add(100); // MaxStationaryStateIterations
        rootEpsilons_.add(1e-8); // rootEpsilon
        functionEpsilons_.add(1e-16); // functionEpsilon
        gradientNormEpsilons_.add(1e-8); // gradientNormEpsilon
        endCriterias_.add(new EndCriteria(maxIterations_.get(maxIterations_.size() - 1), maxStationaryStateIterations_
                .get(maxStationaryStateIterations_.size() - 1), rootEpsilons_.get(rootEpsilons_.size() - 1), functionEpsilons_
                .get(functionEpsilons_.size() - 1), gradientNormEpsilons_.get(gradientNormEpsilons_.size() - 1)));
        // Set optimization methods for optimizer
        OptimizationMethodType optimizationMethodTypes[] = { OptimizationMethodType.simplex };/* OptimizationMethodType.levenbergMarquardt};*/
        double simplexLambda = 0.1;
        double levenbergMarquardtEpsfcn = Math.pow(10, -0.8); //FIXME: how to write this as 1e-0.8???
        double levenbergMarquardtXtol = Math.pow(10, -0.8); //FIXME: how to write this as 1e-0.8???
        double levenbergMarquardtGtol = Math.pow(10, -0.8); //FIXME: how to write this as 1e-0.8???
        
        optimizationMethods_.add(makeOptimizationMethods(optimizationMethodTypes, simplexLambda, levenbergMarquardtEpsfcn, levenbergMarquardtXtol, levenbergMarquardtGtol));
        // Set expected results for optimizer
        double [] xMinExpected = new double [1];
        double [] yMinExpected = new double [1];
        xMinExpected[0] = -b/(2.0*a);
        yMinExpected[0] = -(b*b-4.0*a*c)/(4.0*a);
        //WHAT DOES THIS MEAN?????
        //xMinExpected_.add(xMinExpected);
        //yMinExpected_.add(yMinExpected);
        
        for (int i=0; i<costFunctions_.size(); ++i) {
            Problem problem = new Problem(costFunctions_.get(i), constraints_.get(i), initialValues_.get(i));
            for (int j=0; j<(optimizationMethods_.get(i)).size(); ++j) {
                EndCriteria.CriteriaType endCriteriaResult = optimizationMethods_.get(i).get(j).minimize(problem, endCriterias_.get(i));
            Array xMinCalculated = problem.currentValue();
            Array yMinCalculated = problem.values(xMinCalculated);
            // Check optimizatin results vs known solution
            for (int k=0; k < xMinCalculated.size(); ++k) {
                //if (std::fabs(yMinExpected_[k]- yMinCalculated[k]) > functionEpsilons_[i]) {
                if (false) {
                    System.out.println("Test failing.....");
                    /*
                    fail("costFunction = " + String.valueOf(i) + "\n"
                                  "optimizer =  " +  j + "\n"
                                  + "    x expected:    " + xMinExpected_[k] << "\n"
                                  + "    x calculated:  " + std::setprecision(9) << xMinCalculated[k] << "\n"
                                  + "    x difference:  " +  xMinExpected_[k]- xMinCalculated[k] << "\n"
                                  + "    rootEpsilon:   " + std::setprecision(9) << rootEpsilons_[i] << "\n"
                                  + "    y expected:    " + yMinExpected_[k] << "\n"
                                  + "    y calculated:  " + std::setprecision(9) << yMinCalculated[k] << "\n"
                                  + "    y difference:  " +  yMinExpected_[k]- yMinCalculated[k] << "\n"
                                  + "    functionEpsilon:   " + std::setprecision(9) << functionEpsilons_[i] << "\n"
                                  + "    endCriteriaResult:  " + endCriteriaResult);
                    }
                    */
                }
            }
        }
        }
    }
        
    
    private List<OptimizationMethod> makeOptimizationMethods(OptimizationMethodType optimizationMethodTypes [],
            double simplexLambda,
            double levenbergMarquardtEpsfcn,
            double levenbergMarquardtXtol,
            double levenbergMarquardtGtol){
        List<OptimizationMethod> results = new ArrayList<OptimizationMethod>();
        for(int i=0; i<optimizationMethodTypes.length; ++i){
            results.add(makeOptimizationMethod(optimizationMethodTypes[i], 
                    simplexLambda, 
                    levenbergMarquardtEpsfcn, 
                    levenbergMarquardtXtol, 
                    levenbergMarquardtGtol));
        }
        return results;
    }
    
    private OptimizationMethod makeOptimizationMethod(OptimizationMethodType optimizationMethodType,
            double simplexLambda,
            double levenbergMarquardtEpsfcn,
            double levenbergMarquardtXtol,
            double levenbergMarquardtGtol)
    {
        switch(optimizationMethodType){
        case simplex:
            return new Simplex(simplexLambda);
        case levenbergMarquardt:
            return new LevenbergMarquardt(levenbergMarquardtEpsfcn,
                    levenbergMarquardtXtol,
                    levenbergMarquardtXtol);
        default:
            throw new IllegalArgumentException("unknown Optimization Method type");
        }
    }
    

    // Set up, for each cost function, all the ingredients for optimization:
    // constraint, initial guess, end criteria, optimization methods.
    private void setup() {
        // .... TODO: how to keep stuff set up here local? implemented inline as a workaround
    }

    class OneDimensionalPolynomDegreeN extends CostFunction {

        private List<Double> coefficients_;
        private int polynominalDegree_;

        public OneDimensionalPolynomDegreeN(List coefficients) {
            this.coefficients_ = coefficients;
            this.polynominalDegree_ = coefficients.size() - 1;
        }

        @Override
        public double value(Array x) {
            if (x.size() != 1) {
                throw new IllegalArgumentException("Independent variable must be 1 dimensional");
            }
            double y = 0;
            for (int i = 0; i <= polynominalDegree_; ++i) {
                y += coefficients_.get(i) * Math.pow(x.get(0), i);
            }
            return y;
        }

        @Override
        public Array values(Array x) {
            if (x.size() != 1) {
                throw new IllegalArgumentException("Independent variable must be 1 dimensional");
            }
            Array y = new Array();
            y.set(0, value(x));
            return y;
        }
    }
}
