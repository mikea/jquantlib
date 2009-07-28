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

package org.jquantlib.cashflow;

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.SwaptionVolatilityStructure;

//base pricer for vanilla CMS coupons
public abstract class CmsCouponPricer extends FloatingRateCouponPricer {

    private Handle<SwaptionVolatilityStructure> swaptionVol_;
    private static final String no_adequate_swaptionVol_given = "no adequate swaptionVol given";

    public CmsCouponPricer(final Handle<SwaptionVolatilityStructure> swaptionVol) {
        this.swaptionVol_ = swaptionVol;
        swaptionVol_.addObserver(this);
    }

    public Handle<SwaptionVolatilityStructure> swaptionVolatility() {
        return swaptionVol_;
    }

    public void setSwaptionVolatility(final Handle<SwaptionVolatilityStructure> swaptionVol) {
        swaptionVol_.deleteObserver(this);
        swaptionVol_ = swaptionVol;
        assert swaptionVol_!=null && swaptionVol_.getLink() != null : no_adequate_swaptionVol_given;
        swaptionVol_.addObserver(this);
        update();
    }

    private void update() {
        notifyObservers();
    }

}
