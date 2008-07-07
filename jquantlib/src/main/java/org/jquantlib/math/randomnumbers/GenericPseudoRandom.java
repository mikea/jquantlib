/*
 Copyright (C) 2007 Richard Gomes

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

/*
 Copyright (C) 2004 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2004 Walter Penschke

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

package org.jquantlib.math.randomnumbers;

/**
 * @author Richard Gomes
 */
public class GenericPseudoRandom<R extends RNG, I extends IC> {
    
    // FIXME: code review
    // private enum { allowsErrorEstimate = 1 };
    
    // data
    static private IC icInstance;
    
    
    // FIXME: code review
    /*static*/ public InverseCumulativeRsg<? extends RandomSequenceGenerator<R>, I> 
                makeSequenceGenerator(final int dimension, final Class<R> klass, /* @Unsigned */ long seed) {
        
        RandomSequenceGenerator<R> g = new RandomSequenceGenerator<R>(dimension, klass, seed);
        
        if (icInstance!=null) {
            return new InverseCumulativeRsg(g, icInstance);
        } else {
            return new InverseCumulativeRsg(g);
        }
    }
    
}
