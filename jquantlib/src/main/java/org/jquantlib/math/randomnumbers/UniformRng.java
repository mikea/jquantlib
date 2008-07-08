/*
 Copyright (C) 2007 Aaron Roth

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

package org.jquantlib.math.randomnumbers;

/**
 *
 * @author Aaron Roth
 *
 * We make the class below a class rather than an interface to make calls to
 * next() in derived classes a little more efficient.
 */
public abstract class UniformRng implements UniformPseudorandomRealNumberGenerator, SeedableWithInts {
    private static final double divisor = 2.0 * Integer.MAX_VALUE + 2.0;
    
    public abstract int nextInt();

    @Override
    public double nextDoubleInOpenUnitInterval() {
        // If nextInt() returns Integer.MIN_VALUE, take nextInt() again.
        // This still yields a uniform distribution (assuming the ints of the
        // RNG were originally uniformly distributed) on the remaining ints
        // and ensures we only return doubles in the *open* interval, (0,1).
        //
        // Note that, for the sake of efficiency, we're only using 32 random bits
        // to generate 64 random bits. If the full 64 bits are needed,
        // the calculation below must be suitably modified to make use of two
        // successive (32-bit) ints returned by nextInt().
        int n;
        do { n = nextInt(); } while (n == Integer.MIN_VALUE);
        
        return ( n / divisor + 0.5 );
    }
}
