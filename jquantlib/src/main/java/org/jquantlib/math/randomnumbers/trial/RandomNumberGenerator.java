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

package org.jquantlib.math.randomnumbers.trial;

import org.jquantlib.methods.montecarlo.Sample;

/**
 * @author Richard Gomes
 */
public interface RandomNumberGenerator<T> {

    //FIXME: code review: should use parameterized type instead of Double ??
    public Sample<T> next() /* @ReadOnly */;
    
    public /*@NonNegative*/ long nextInt32() /* @ReadOnly */;
    
    // public boolean allowErrorEstimate(); // FIXME: should declare this method???
}
