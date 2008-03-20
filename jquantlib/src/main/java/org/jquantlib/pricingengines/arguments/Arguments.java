/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

package org.jquantlib.pricingengines.arguments;

import org.jquantlib.pricingengines.PricingEngine;

/**
 * Arguments are used by new-style instruments in order to
 * inform inputs to {@link PricingEngine}s
 * 
 * @see PricingEngine
 * @see ArgumentsDecorator
 * 
 * @author Richard Gomes
 */
public interface Arguments {
    public void validate() /*@ReadOnly*/;
}
