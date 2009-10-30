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

import org.jquantlib.QL;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.CapletVolatilityStructure;

// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public abstract class IborCouponPricer extends FloatingRateCouponPricer {

    public static final String no_adequate_capletVol_given = "no adequate capletVol given";

    private Handle<CapletVolatilityStructure> capletVol_;

    public IborCouponPricer(final Handle<CapletVolatilityStructure> capletVol){
        this.capletVol_ = capletVol;
        this.capletVol_.currentLink().addObserver(this);
        //XXX:registerWith
        //registerWith(this.capletVol_);
    }

    public Handle<CapletVolatilityStructure> capletVolatility(){
        return capletVol_;
    }

    public void setCapletVolatility(final Handle<CapletVolatilityStructure> capletVol){
        capletVol.currentLink().deleteObserver(this);
        //XXX:registerWith
        //unregisterWith(capletVol);

        this.capletVol_ = capletVol;
        QL.require(this.capletVol_ != null , no_adequate_capletVol_given); // QA:[RG]::verified

        this.capletVol_.currentLink().addObserver(this);
        //XXX:registerWith
        // registerWith(this.capletVol_);
        update();
    }

    public void update(){
        notifyObservers();
    }
}
