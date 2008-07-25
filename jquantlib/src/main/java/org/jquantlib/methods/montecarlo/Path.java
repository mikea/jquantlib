/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl
 Copyright (C) 2003 Ferdinando Ametrano

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.methods.montecarlo;

import java.util.List;

import org.jquantlib.math.Array;
import org.jquantlib.time.TimeGrid;
import org.jquantlib.util.stdlibc.DoubleForwardIterator;
import org.jquantlib.util.stdlibc.DoubleReverseIterator;

/**
 * 
 * Single-factor random walk
 * 
 * @note The path includes the initial asset value as its first point.
 * 
 * 
 * 
 * @author Richard Gomes
 */


// FIXME: still working on this mess !!!
// FIXME: code review: verify if DoubleReference should be used here


public class Path {

    private TimeGrid<List<Double>> timeGrid_; // FIXME: should use generic type
    private Array values_;

    public Path(final TimeGrid<List<Double>> timeGrid, final Array values) {
        this.timeGrid_ = timeGrid;
        this.values_ = values;
        if (values_.empty()) {
            values_ = new Array(timeGrid_.size());
        }
        if (values_.size() != timeGrid_.size())
            throw new IllegalArgumentException("different number of times and asset values"); // FIXME: message
    }

    // public /* @Real */ Double back() {
    // return values_.get(values_.size()-1);
    // }


    public boolean empty() /* @ReadOnly */{
        return timeGrid_.empty();
    }

    public/* @NonNegative */int length() /* @ReadOnly */{
        return timeGrid_.size();
    }    // public /* @Real */ Double back() {
    // return values_.get(values_.size()-1);
    // }



    public/* @Real */double get(/* @NonNegative */int i) /* @ReadOnly */{
        return values_.get(i);
    }

    // public /* @Real */ Double get(/*@NonNegative*/ int i) {
    // return values_[i];
    // }

    public/* @Real */double at(/* @NonNegative */int i) /* @ReadOnly */{
        return values_.at(i);
    }

    // public /* @Real */ Double (/*@NonNegative*/ int i) {
    // return values_.at(i);
    // }

    public/* @Real */double value(/* @NonNegative */int i) /* @ReadOnly */{
        return values_.get(i);
    }

    // public /* @Real */ Double value(/*@NonNegative*/ int i) {
    // return values_.get(i);
    // }

    public/* @Real */double front() /* @ReadOnly */{
        return values_.get(0);
    }

    public/* @Real */double back() /* @ReadOnly */{
        return values_.get(values_.size() - 1);
    }

    public/* @Time */double time(/* @NonNegative */int i) /* @ReadOnly */{
        return timeGrid_.get(i);
    }

    public final TimeGrid<List<Double>> timeGrid() /* @ReadOnly */{
        return timeGrid_;
    }

    public DoubleForwardIterator forwardIterator() /* @ReadOnly */{
        return values_.forwardIterator();
    }

    public DoubleReverseIterator reverseIterator() /* @ReadOnly */{
        return values_.reverseIterator();
    }

}
