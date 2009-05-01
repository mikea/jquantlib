/*
 Copyright (C)
 2009  Ueli Hofstetter

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

import org.jquantlib.math.Array;
import org.jquantlib.util.stdlibc.Std;

public class LmFixedVolatilityModel extends LmVolatilityModel {

    private Array volatilities_;
    private List</* @Time */Double> startTimes_;
    
    public LmFixedVolatilityModel(
            final Array volatilities,
            final List</* @Time */Double> startTimes){
        super(startTimes.size(), 0);
        this.volatilities_ = volatilities;
        this.startTimes_ = startTimes;
        if(startTimes_.size()<=1){
            throw new IllegalArgumentException("too few dates");
        }
        if(volatilities_.size() != startTimes_.size()){
                   throw new IllegalArgumentException("volatility array and fixing time array have to have the same size");
        }
        for (int i = 1; i < startTimes_.size(); i++) {
            if(startTimes_.get(i) <= startTimes_.get(i-1)){
                throw new IllegalArgumentException("invalid time (" + startTimes_.get(i) + ", vs " + startTimes_.get(i) + ")");
            }
                      
        }
    }

    @Override
    protected void generateArguments() {
        return;
    }

    @Override
    public List<Double> volatility(double t, List x) {
        if (t >= startTimes_.get(0) && t <= startTimes_.get(startTimes_.size() - 1)) {
            throw new IllegalArgumentException("invalid time given for volatility model");
        }

        final int ti = (int) (Std.upper_bound(startTimes_.toArray(new Double[startTimes_.size()]), t) - startTimes_.get(0) - 1);

        Array tmp = new Array(size_, 0.0);

        for (int i = ti; i < size_; ++i) {
            tmp.set(i, volatilities_.get(i - ti));
        }
        List<Double> ret = new ArrayList<Double>();
        for (int i = 0; i < tmp.size(); i++) {
            ret.set(i, tmp.get(i));
        }
        return ret;
    }

    public double /* @Volatility */volatility(int i, /* @Time */double t, final Array x) {
        if (t < startTimes_.get(0) || t > startTimes_.get(startTimes_.size() - 1)) {
            throw new IllegalArgumentException("invalid time given for volatility model");
        }
        final int ti = (int) (Std.upper_bound(startTimes_.toArray(new Double[startTimes_.size()]), t) - startTimes_.get(0) - 1.0);

        return volatilities_.get(i - ti);
    }

}
