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

package org.jquantlib.math.randomnumbers;

import org.jquantlib.util.reflect.TypeToken;

/**
 * The original C++ code has a static variable defined more or less like this:
 * <pre>
 *     private static volatile IC icInstance; // translated to Java
 * </pre>
 * 
 * C++ Template engine creates multiple "incarnations" of code for every invocation of the template. In particular, when template
 * parameters are identical to previous invocations, binary code is identical to previous invocations. The link editor is
 * responsible for removing duplicates which means that all identical invocations will "share" the same commonly declared static
 * variable.
 * <p>
 * Java Generics engine creates multiple, distinct objects for every invocation, but all sharing the same class signature. The Java
 * compiler does not make any distinction between static variables in these multiple objects because static variables are associated
 * to the class and not to a certain instance.
 * <p>
 * It means that, in order to mimic the behaviour of C++ code, we would be obliged to keep a cache at runtime which returns a
 * singleton associated to a certain combination of Generic parameters retrieved. This effort does not payoff the benefits obtained
 * (if any) whilst it imposes additional performance penalties in order to manage the cache.
 * <p>
 * For this reason, we are not providing the static variable responsible for keeping a certain generic IC.
 * 
 * @author Richard Gomes
 * @param <T> represents the sample type
 * @param <URSG> represents the UniformRandomSequenceGenerator<T>
 * @param <IC> represents the InverseCumulative
 */
public class GenericLowDiscrepancy<T, URSG extends UniformRandomSequenceGenerator<T>, IC extends InverseCumulative> { 

    // TODO :: code review
    static public boolean allowsErrorEstimate = false;
    
    
    // TODO :: code review
    /*static*/ public InverseCumulativeRsg<T, URSG, IC> makeSequenceGenerator(
            final /*@NonNegative*/ int dimension, final /*@NonNegative*/ long seed) {

        try {
            // instantiate a generic holder for Sample values
            URSG g = null;
            try {
                g = (URSG) TypeToken.getClazz(this.getClass(), 1).getConstructor(int.class, long.class).newInstance(dimension, seed);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // return an InverseCumulativeRandomSequenceGenerator given a RandomSequenceGenerator and InverseCumulative distribution
            // TODO: decide how InverseCumulativeRsg must be instantiated
            return /*(icInstance!=null) ? new InverseCumulativeRsg(g, icInstance) :*/ new InverseCumulativeRsg(g);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
