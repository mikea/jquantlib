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

import org.jquantlib.CapletVolatilityStructure;
import org.jquantlib.quotes.Handle;

public abstract class IborCouponPricer extends FloatingRateCouponPricer {
    
    public static final String no_adequate_capletVol_given = "no adequate capletVol given";
    
    private Handle<CapletVolatilityStructure> capletVol_;
    
    public IborCouponPricer(Handle<CapletVolatilityStructure> capletVol){
        this.capletVol_ = capletVol;
        capletVol.addObserver(this);
    }
    
    public Handle<CapletVolatilityStructure> capletVolatility(){
        return capletVol_;
    }
    
    public void setCapletVolatility(Handle<CapletVolatilityStructure> capletVol){
        capletVol_.deleteObserver(this);
        capletVol_ = capletVol;
        if(capletVol_ == null){
            throw new IllegalArgumentException(no_adequate_capletVol_given);
        }
        capletVol.addObserver(this);
        update();
    }
    
    public void update(){
        notifyObservers();
    }
}
