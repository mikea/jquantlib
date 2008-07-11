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

package org.jquantlib.math.randomnumbers.trial;

import java.lang.reflect.Constructor;
import java.util.List;

import org.jquantlib.methods.montecarlo.Sample;
import org.jquantlib.util.reflect.TypeReference;

/**
 * 
 * @author Richard Gomes
 * @param <URSG>
 * @param <IC>
 */
public class GenericLowDiscrepancy<URSG extends UniformSequenceGenerator<Sample<List<Double>>>, IC extends InverseCumulative> 
            extends TypeReference {

    // FIXME: static :(
    static public boolean allowsErrorEstimate = false;
    
    
    /*static*/ // FIXME: static :( 
    private IC icInstance; // FIXME: where it is initialized ???

    /*static*/ // FIXME: static :(
    public InverseCumulativeRsg<URSG, IC> makeSequenceGenerator(
            final /*@NonNegative*/ int dimension, final /*@NonNegative*/ long seed) {

        try {
            Constructor<URSG> c1 = (Constructor<URSG>) getGenericParameterClass().getConstructor(int.class, long.class);
            URSG g = c1.newInstance(dimension, seed);
            return (icInstance!=null) ? new InverseCumulativeRsg(g, icInstance) : new InverseCumulativeRsg(g);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
