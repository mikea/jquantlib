/*
 Copyright (C) 
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

package org.jquantlib.legacy.libormarkets;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.JQuantlib;
import org.jquantlib.model.Parameter;

public class LmCorrelationModel {
    
    private int size_;
    private List<Parameter> arguments_;
    
    public LmCorrelationModel(int size, int nArguments){
        this.size_ = size;
        this.arguments_ = new ArrayList<Parameter>(nArguments);
    }
    
    public int size(){
        return size_;
    }
    
    public int factors(){
        return size_;
    }
    
    public boolean isTimeDependent(){
        return false;
    }
    /*
    public double pseudoSqrt(double t, final Array x){
        return JQuantlib.pseudoSqrt(this->correlation(t, x),
                SalvagingAlgorithm::Spectral);
    }
    
    public  Matrix correlation(
            double t, final Array x){
        
    }
    
    public  Matrix correlation(
            double t){
        return correlation(t, new Array());
    }
    
    public double correlation(
            int i, int j, double t, final Array x){
        
    }
    
    public double correlation(
            int i, int j, double t){
        return correlation(i, j, t, new Array());
        
    }
    */

}
