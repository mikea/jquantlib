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


package org.jquantlib.math.randomnumbers;

import java.util.ArrayList;

import org.jquantlib.methods.montecarlo.Sample;


/**
 * 
 * @author Dominik Holenstein
 *
 */

// TODO: Under construction


public class MersenneTwisterUniformRng implements RandomNumberGenerator<Double> {
	
	//
	// Constants
	//
	
	// constant vector a	
	private static final long MATRIX_A = 0x9908b0df;
	
	// Period parameters
	private static final int N = 624;
	private static final int M = 397;
	
	// most significant w-r bits
	private static final long UPPER_MASK = 0x80000000;
	
	// least significant r bits
	private static final long LOWER_MASK = 0x7fffffff;
	
	// Tempering Masks
	private static final long TEMPERING_MASK_B=0x9d2c5680;
	private static final long TEMPERING_MASK_C=0xefc60000;
	
	
	//
	// private fields
	//
	
	private int  mti; 
	private long[] mt = new long[N];
	
	
	//
	// Constructors
	//
	
	public MersenneTwisterUniformRng(){
	}
	
	/** Creates a new random number generator using a single long seed.
	   * @param seed the initial seed (64 bits integer)
	   */
	  public MersenneTwisterUniformRng(long seed) {
	    seedInitialization(seed);
	  }
	
	
	  //
	  // Seed initializers
	  //
	/**
	 * Initialize the generator with a given long seed.
	 * @param seed the initial seed as long
	 */
	public void seedInitialization(long seed) {
		long s = SeedGenerator.getInstance().get();
	    mt[0]= s & 0xffffffff;
	    for (mti = 1; mti < N; mti++) {
	    	mt[mti] = (1812433253 * (mt[mti-1] ^ (mt[mti-1] >> 30 )) + mti);
	    	// See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier.
	    	// initializer from the 2002-01-09 C version by Makoto Matsumoto
	    	
	    	// for >32 bit machines
	    	// mt[mti]&= 0xffffffff;
	    }
	  }
	

	protected void seedInitialization(ArrayList<Long> seeds){   
        seedInitialization(19650218);
        int i=1;
        int j=0;
        
        for (int k = Math.max(N, seeds.size()); k != 0; k--) {
            mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * 1664525)) + seeds.indexOf(j) + j; // non linear 
            // mt[i] &= 0xffffffff; // for WORDSIZE > 32 machines 
            i++; 
            j++;
            if (i>=N) { 
            	mt[0] = mt[N-1]; i=1; 
            }
            if (j>=seeds.size()) {
            	j=0;
            }
        }
        
        for (int k=N-1; k!=0; k--) {
            mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * 1566083941)) - i; // non linear 
         //    mt[i] &= 0xffffffffUL; // for WORDSIZE > 32 machines 
            i++;
            if (i>=N) { mt[0] = mt[N-1]; i=1; }
        } 
        
        mt[0] = 0x80000000; // MSB is 1; assuring non-zero initial array
	}
	
	
	/**
	 * Return a random number on [0,0xffffffff]-interval
	 * @return y 
	 * 
	 */
    @Override
	public long nextInt32() {
			  	
	  	long y;
	  	long[] mag01 = {0x0, MATRIX_A}; 

        if (mti >= N) { /* generate N words at one time */
        	int kk;
        	long mtNext = mt[0];

            for (kk=0;kk<N-M;kk++) {
                y = (mt[kk] & UPPER_MASK)|(mt[kk+1] & LOWER_MASK);
                mt[kk] = mt[kk+M] ^ (y >>> 1) ^ mag01[(int)	y & 0x1];
            }
            for (kk=N; kk<N-1;kk++) {
                y = (mt[kk] & UPPER_MASK)|(mt[kk+1] & LOWER_MASK);
                mt[kk] = mt[kk+(M-N)] ^ (y >>> 1) ^ mag01[(int)y & 0x1];
            }
            y = (mtNext & UPPER_MASK)|(mt[0] & LOWER_MASK);
            mt[N-1] = mt[M-1] ^ (y >>> 1) ^ mag01[(int)y & 0x1];

            mti = 0;
        }

        y = mt[mti++];
	  	
	  	//
	  	// Tempering
	  	//
		y ^= y >>> 11; // y ^= TEMPERING_SHIFT_U(y );
		y ^= (y << 7) & TEMPERING_MASK_B; // y ^= TEMPERING_SHIFT_S(y) & TEMPERING_MASK_B;
		y ^= (y << 15) & TEMPERING_MASK_C; // y ^= TEMPERING_SHIFT_T(y) & TEMPERING_MASK_C;	
		y ^= y >>> 18; // y ^= TEMPERING_SHIFT_L(y);

		return y;
	}
	
	/**
	 * Returns a sample with weight 1.0 containing a random number on
	 * (0.0, 1.0)-real-interval.
	 * @return 
	 */
	
	@Override
	public final Sample<Double> next() {
		// divide by 2^32
		double result = ((double)nextInt32() + 0.5)/4294967296.0;
		Sample<Double> sample_value = new Sample<Double>(result,1.0);
		return sample_value;
	}

}


