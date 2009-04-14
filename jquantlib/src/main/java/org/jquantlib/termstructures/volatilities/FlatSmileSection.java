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

package org.jquantlib.termstructures.volatilities;


import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;

public abstract class FlatSmileSection extends SmileSection {

    private double vol_;

    public FlatSmileSection(Date d, double vol, DayCounter dc, Date referenceDate) {

        super(d, dc, referenceDate);
        this.vol_ = vol;
    }

    public FlatSmileSection(Date d, double vol, DayCounter dc) {

        super(d, dc, DateFactory.getFactory().getTodaysDate());
        this.vol_ = vol;
    }

    public double variance() {
        return vol_ * vol_ * exerciseTime_;
    }

    public double volatility() {
        return vol_;
    }

    public double minStrike() {
        return 0.0;
    };

    public double maxStrike() {
        return Double.MAX_VALUE;
    };

}
