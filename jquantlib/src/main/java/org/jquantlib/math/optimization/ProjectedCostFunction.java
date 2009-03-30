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

import org.jquantlib.math.Array;

public class ProjectedCostFunction extends CostFunction {
    
    private int numberOfFreeParameters_;
    private Array fixedParameters_;
    private Array actualParameters_;
    private boolean [] parametersFreedoms_;
    private CostFunction costFunction_;
    

    public ProjectedCostFunction(CostFunction costFunction, Array parameterValues, boolean [] parametersFreedoms) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        numberOfFreeParameters_ = 0;
        fixedParameters_ = new Array(parameterValues);
        actualParameters_ = new Array(parameterValues);
        parametersFreedoms_ = parametersFreedoms;
        
        if(fixedParameters_.size() != parametersFreedoms_.length){
            throw new IllegalArgumentException("fixedParameters_.size()!=parametersFreedoms_.size()");
        }
        for(int i = 0; i<parametersFreedoms_.length; i++){
            if(!parametersFreedoms_[i]){
                numberOfFreeParameters_++;
            }
            if(!(numberOfFreeParameters_>0)){
                throw new ArithmeticException("numberOfFreeParameters==0");
            }
        }        
    }
    
    public void mapFreeParameters(Array parametersValues){
        if(!(parametersValues.size() == numberOfFreeParameters_)){
            throw new IllegalArgumentException("parametersValues.size()!=numberOfFreeParameters");
        }
        int i = 0;
        for(int j = 0; j<actualParameters_.size(); j++){
            if(!parametersFreedoms_[j]){
                actualParameters_.set(j, parametersValues.get(i++));
        }
    }
    }
    
    @Override
    public double value(Array freeParameters){
        mapFreeParameters(freeParameters);
        return costFunction_.value(actualParameters_);
    }
        
    //FIXME: check Disposable template
    public Array values(Array freeParameters){
        mapFreeParameters(freeParameters);
        return costFunction_.values(actualParameters_);
    }
    
    //FIXME: check Disposable template
    public Array project(Array parameters){
        if(!(parameters.size() == parametersFreedoms_.length)){
           throw new ArithmeticException("parameters.size()!=parametersFreedoms_.size()");
        }
        Array projectedParameters = new Array(numberOfFreeParameters_);
        int i = 0;
        for(int j=0; j<parametersFreedoms_.length; j++){
            if(!parametersFreedoms_[j]){
                projectedParameters.set(i++,parameters.get(j));
            }
        }
        return projectedParameters;
    }
    
    //FIXME: check Disposable template
    public Array include(Array projectedParameters){
        if(!(projectedParameters.size() == numberOfFreeParameters_)){
            throw new IllegalArgumentException("projectedParameters.size()!=numberOfFreeParameters");
        }
        Array y = new Array(fixedParameters_);
        int i = 0;
        for(int j = 0; j<y.size(); j++){
            if(!parametersFreedoms_[j]){
                y.set(j, projectedParameters.get(i++));
            }
        }
        return y;
    }
}
