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
package org.jquantlib.math.optimization;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.lang.annotation.Real;
import org.jquantlib.math.Array;
import org.jquantlib.math.Constants;
import org.jquantlib.math.optimization.EndCriteria.CriteriaType;


public class Simplex extends OptimizationMethod {

    private double lambda_;
    private List<Array> vertices_;
    private Array values_, sum_;
   
    
    public Simplex(double lambda) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.lambda_ = lambda;
        
    }
    
    public double extrapolate(Problem P, int iHighest, double factor){
        Array pTry = new Array();
        do{
            double dimensions = values_.size() - 1;
            double factor1 = (1.0 - factor)/dimensions;
            double factor2 = factor1 - factor;
            pTry = sum_.operatorMultiplyCopy(factor1).operatorSubtractCopy(vertices_.get(iHighest).operatorMultiplyCopy(factor2));
            factor *= 0.5;
        }
        while(!P.constraint().test(pTry));
        
        factor *= 2.0;
        double vTry = P.value(pTry);
        if(vTry < values_.get(iHighest)){
            values_.set(iHighest, vTry);
            sum_ = sum_.operatorAddCopy(pTry.operatorSubtractCopy(vertices_.get(iHighest)));
            vertices_.set(iHighest, pTry);
        }
        return vTry;
    }

    @Override
    public CriteriaType minimize(Problem P, EndCriteria endCriteria) {
        EndCriteria.CriteriaType ecType = EndCriteria.CriteriaType.None;
        P.reset();
        Array x_ = P.currentValue();
        int iterationNumber_ = 0;
        
        boolean end = false;
        int n = x_.size();
        int i;
        
        vertices_ = new ArrayList<Array>(n+1);
        //add empty Arrays
        for(int fill = 0; fill<=n+1; fill++){
            vertices_.add(new Array(x_));
        }
        for(i = 0; i<n; i++){
            Array direction = new Array(n);
            direction.set(i, 1.0);
            P.constraint().update(vertices_.get(i+1), direction, lambda_);
        }
        
        values_ = new Array(n+1, 0.0);
        for(i = 0; i<= n; i++){
            values_.set(i, P.value(vertices_.get(i)));
        }
        
        do {
            sum_ = new Array(n, 0.0);
            for (i=0; i<=n; i++)
                sum_ = sum_.operatorAddCopy(vertices_.get(i));
            //Determine best, worst and 2nd worst vertices
            int iLowest = 0;
            int iHighest, iNextHighest;
            if (values_.get(0) < values_.get(0)) {
                iHighest = 1;
                iNextHighest = 0;
            } else {
                iHighest = 0;
                iNextHighest = 1;
            }
            for (i=1;i<=n; i++) {
                if (values_.get(i)>values_.get(iHighest)) {
                    iNextHighest = iHighest;
                    iHighest = i;
                } else {
                    if ((values_.get(i)>values_.get(iHighest)) && i!=iHighest)
                        iNextHighest = i;
                }
                if (values_.get(i)<values_.get(iLowest))
                    iLowest = i;
            }
            double low = values_.get(iLowest), high = values_.get(iHighest);
            double rtol = 2.0*Math.abs(high - low)/
                (Math.abs(high) + Math.abs(low) + Constants.QL_EPSILON);
            ++iterationNumber_;
            if (rtol < endCriteria.getFunctionEpsilon() ||
                endCriteria.checkMaxIterations(iterationNumber_, ecType)) {
                endCriteria.checkStationaryFunctionAccuracy(Constants.QL_EPSILON, true, ecType);
                endCriteria.checkMaxIterations(iterationNumber_, ecType); // WARNING: what is this good for?
                x_ = vertices_.get(iLowest);
                P.setFunctionValue(low);
                P.setCurrentValue(x_);
                return ecType;
            }

            double factor = -1.0;
            double vTry = extrapolate(P, iHighest, factor);
            if ((vTry <= values_.get(iLowest)) && (factor == -1.0)) {
                factor = 2.0;
                extrapolate(P, iHighest, factor);
            } else {
                if (vTry >= values_.get(iNextHighest)) {
                    double vSave = values_.get(iHighest);
                    factor = 0.5;
                    vTry = extrapolate(P, iHighest, factor);
                    if (vTry >= vSave) {
                        for (i=0; i<=n; i++) {
                            if (i!=iLowest) {
                                vertices_.set(i, 
                                    (vertices_.get(i).operatorAddCopy(vertices_.get(iLowest))).operatorMultiplyCopy(0.5));
                                values_.set(i, P.value(vertices_.get(i)));
                            }
                        }
                    }
                }
            }
        } while (end == false);
        throw new ArithmeticException("optimization failed: unexpected behaviour");
    }

}
