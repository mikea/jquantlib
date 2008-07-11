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

package org.jquantlib.math.randomnumbers.trial;

import java.lang.reflect.Constructor;

import org.jquantlib.util.reflect.TypeReference;


/**
 * 
 * @author Richard Gomes
 * @param <URSG>
 * @param <IC>
 */

//public class GenericPseudoRandom<URSG extends RandomSequenceGenerator<Sample<List<Double>>>, IC extends InverseCumulative> {
public class GenericPseudoRandom<RNG extends RandomNumberGenerator, IC extends InverseCumulative> extends TypeReference {

    // FIXME: static :(
    static public boolean allowsErrorEstimate = true;
    
    
    /*static*/    // FIXME: static :( 
    private IC icInstance; // FIXME: where it is initialized ???

    
    /*static*/    // FIXME: static :( 
    public InverseCumulativeRsg<RandomSequenceGenerator<RNG>, IC> makeSequenceGenerator(
            final /*@NonNegative*/ int dimension, final /*@NonNegative*/ long seed) {

        try {
            // instantiate a RandomNumberGenerator given its generic type
            Constructor<RNG> c1 = (Constructor<RNG>) getGenericParameterClass().getConstructor(long.class);
            RNG rng = c1.newInstance(seed);

            // instantiate a RandomSequenceGenerator given its dimension and a RandonNumberGenerator
            RandomSequenceGenerator<RNG> rsg = new RandomSequenceGenerator<RNG>(dimension, rng);
            
            // return an InverseCumulativeRandomSequenceGenerator given a RandomSequenceGenerator and InverseCumulative distribution
            return (icInstance!=null) ? new InverseCumulativeRsg(rsg, icInstance) : new InverseCumulativeRsg(rsg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
