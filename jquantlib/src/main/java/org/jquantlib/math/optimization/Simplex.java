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

import org.jquantlib.math.Constants;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.optimization.EndCriteria.Type;

// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
@Deprecated
public class Simplex extends OptimizationMethod {

    private final double lambda;
    private List<Array> vertices;
    private Array values, sum;


    public Simplex(final double lambda) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        this.lambda = lambda;

    }

    public double extrapolate(final Problem P, final int iHighest, double factor){
        Array pTry = new Array();
        do{
            final double dimensions = values.size() - 1;
            final double factor1 = (1.0 - factor)/dimensions;
            final double factor2 = factor1 - factor;
            pTry = sum.mul(factor1).sub(vertices.get(iHighest).mul(factor2));
            factor *= 0.5;
        }
        while(!P.constraint().test(pTry));

        factor *= 2.0;
        final double vTry = P.value(pTry);
        if(vTry < values.get(iHighest)){
            values.set(iHighest, vTry);
            sum = sum.add(pTry.sub(vertices.get(iHighest)));
            vertices.set(iHighest, pTry);
        }
        return vTry;
    }

    @Override
    public Type minimize(final Problem P, final EndCriteria endCriteria) {
        final EndCriteria.Type ecType = EndCriteria.Type.None;
        P.reset();
        Array x_ = P.currentValue();
        int iterationNumber_ = 0;

        final boolean forever = true;
        final int n = x_.size();
        int i;

        vertices = new ArrayList<Array>(n+1);
        //add empty Arrays
        for (int fill = 0; fill<=n+1; fill++) {
            // TODO: code review :: use of clone()
            vertices.add(x_.clone());
        }
        for(i = 0; i<n; i++){
            final Array direction = new Array(n);
            direction.set(i, 1.0);
            P.constraint().update(vertices.get(i+1), direction, lambda);
        }

        values = new Array(n+1);
        for(i = 0; i<= n; i++) {
            values.set(i, P.value(vertices.get(i)));
        }

        do {
            sum = new Array(n);
            for (i=0; i<=n; i++) {
                sum = sum.add(vertices.get(i));
            }
            //Determine best, worst and 2nd worst vertices
            int iLowest = 0;
            int iHighest, iNextHighest;
            if (values.first() < values.first()) {
                iHighest = 1;
                iNextHighest = 0;
            } else {
                iHighest = 0;
                iNextHighest = 1;
            }
            for (i=1;i<=n; i++) {
                if (values.get(i)>values.get(iHighest)) {
                    iNextHighest = iHighest;
                    iHighest = i;
                } else if ((values.get(i)>values.get(iHighest)) && i!=iHighest) {
                    iNextHighest = i;
                }
                if (values.get(i)<values.get(iLowest)) {
                    iLowest = i;
                }
            }
            final double low = values.get(iLowest), high = values.get(iHighest);
            final double rtol = 2.0*Math.abs(high - low)/
            (Math.abs(high) + Math.abs(low) + Constants.QL_EPSILON);
            ++iterationNumber_;
            if (rtol < endCriteria.getFunctionEpsilon() ||
                    endCriteria.checkMaxIterations(iterationNumber_, ecType)) {
                endCriteria.checkStationaryFunctionAccuracy(Constants.QL_EPSILON, true, ecType);
                endCriteria.checkMaxIterations(iterationNumber_, ecType); // WARNING: what is this good for?
                x_ = vertices.get(iLowest);
                P.setFunctionValue(low);
                P.setCurrentValue(x_);
                return ecType;
            }

            double factor = -1.0;
            double vTry = extrapolate(P, iHighest, factor);
            if ((vTry <= values.get(iLowest)) && (factor == -1.0)) {
                factor = 2.0;
                extrapolate(P, iHighest, factor);
            } else if (vTry >= values.get(iNextHighest)) {
                final double vSave = values.get(iHighest);
                factor = 0.5;
                vTry = extrapolate(P, iHighest, factor);
                if (vTry >= vSave) {
                    for (i=0; i<=n; i++)
                        if (i!=iLowest) {
                            vertices.set(i,
                                    (vertices.get(i).add(vertices.get(iLowest))).mul(0.5));
                            values.set(i, P.value(vertices.get(i)));
                        }
                }
            }
        } while (forever);
        //-- throw new ArithmeticException("optimization failed: unexpected behaviour");
    }

}
