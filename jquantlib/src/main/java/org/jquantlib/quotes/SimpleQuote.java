/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.quotes;

import org.jquantlib.QL;

// TODO: comments
public class SimpleQuote extends Quote {

    //
    // private fields
    //

    private double value;


    //
    // public constructors
    //

    public SimpleQuote(final SimpleQuote o) {
        this.value = o.value;
    }

    public SimpleQuote(final double d) {
        this.value = d;
    }


    //
    // public methods
    //

    public void setValue(final double value) {
        final double diff = this.value - value;
        if (diff != 0.0) {
            this.value = value;
            notifyObservers();
        }
    }

    //
    // implements FunctionDouble
    //

    @Override
    public final double op() {
        QL.require(!Double.isNaN(value) , "invalid simple quote: no value available"); // QA:[RG]::verified // TODO: message
        return value;
    }

}
