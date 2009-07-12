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

import org.jquantlib.math.Array;

public class LmFixedVolatilityModel extends LmVolatilityModel {

    private final Array volatilities_;
    private final Array startTimes_;
    
    public LmFixedVolatilityModel(final Array volatilities, final Array startTimes) {
        super(startTimes.length, 0);
        // TODO: code review :: use of clone()
        this.volatilities_ = volatilities;
        this.startTimes_ = startTimes;
        
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (startTimes_.length<=1) 
            throw new IllegalArgumentException("too few dates");
        if (volatilities_.length != startTimes_.length)
                   throw new IllegalArgumentException("volatility array and fixing time array have to have the same size");

        for (int i = 1; i < startTimes_.length; i++) {
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
    public Array volatility(double t, Array x) {
        if (t >= startTimes_.first() && t <= startTimes_.last()) {
            throw new IllegalArgumentException("invalid time given for volatility model");
        }
        final int ti = (int) (startTimes_.upperBound(t) - startTimes_.first() - 1);

        Array tmp = new Array(size_);

        for (int i = ti; i < size_; ++i) {
            tmp.set(i, volatilities_.get(i - ti));
        }
        Array ret = new Array();
        for (int i = 0; i < tmp.length; i++) {
            ret.set(i, tmp.get(i));
        }
        return ret;
    }

    public double /* @Volatility */volatility(int i, /* @Time */double t, final Array x) {
        if (t < startTimes_.first() || t > startTimes_.last()) {
            throw new IllegalArgumentException("invalid time given for volatility model");
        }
        final int ti = (int) (startTimes_.upperBound(t) - startTimes_.first() - 1);

        return volatilities_.get(i - ti);
    }

}
