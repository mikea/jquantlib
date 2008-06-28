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
import java.util.Date;

import org.jquantlib.math.randomnumbers.SeedGenerator;


/**
 * 
 * @author Dominik Holenstein
 *
 */

// TODO: Under construction


public class MersenneTwisterUniformRng {
	
	//
	// Constants
	//
	
	// constant vector a	
	private static final int MATRIX_A = 0x9908b0df;
	
	// Period parameters
	private static final int N = 624;
	private static final int M = 397;
	private static int[] mag01 = {0x0,MATRIX_A};
	
	
	// most significant w-r bits
	private static final int UPPER_MASK = 0x80000000;
	
	// least significant r bits
	private static final int LOWER_MASK = 0x7fffffff;
	
	//
	private static final int TEMPERING_MASK_B=0x9d2c5680;
	private static final int TEMPERING_MASK_C=0xefc60000;
	
	public static final int DEFAULT_SEED = 4357;
	
	
	//
	// private fields
	//
	
	private int mti; 
	private int[] mt = new int[N];
	
	
	
	//
	// Constructors
	//
	
	/**
	 * Constructs and returns a random number generator with the given seed.
	 */
	public MersenneTwisterUniformRng(int[] seed){
		seedInitialization(seed);
	}
	
	
	/**
	 * Constructs and returns a random number generator seeded with the given date.
	 *
	 * @param d typically <tt>new java.util.Date()</tt>
	 */
	public MersenneTwisterUniformRng() {
		seedInitialization(System.currentTimeMillis());
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
	 * Initialize the generator with a given int seed.
	 * @param seed the initial seed as a 32 bits integer
	 */
	public void seedInitialization(int seed) {
	    // we use a long masked by 0xffffffffL as a poor man unsigned int
	    long longMT = seed;
	    mt[0]= (int) longMT;
	    for (mti = 1; mti < N; ++mti) {
	      // See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier.
	      // initializer from the 2002-01-09 C version by Makoto Matsumoto
	      longMT = (1812433253l * (longMT ^ (longMT >> 30)) + mti) & 0xffffffffL; 
	      mt[mti]= (int) longMT;
	    }
	  }
	
	/**
	 * Initialize the generator with the given int seed.
	 * @param seeds
	 */
	protected void seedInitialization(int[] seeds){
		       
        seedInitialization(19650218);
        int i = 1;
        int j = 0;

        for (int k = Math.max(N, seeds.length); k != 0; k--) {
          long l0 = (mt[i] & 0x7fffffffl)   | ((mt[i]   < 0) ? 0x80000000l : 0x0l);
          long l1 = (mt[i-1] & 0x7fffffffl) | ((mt[i-1] < 0) ? 0x80000000l : 0x0l);
          long l  = (l0 ^ ((l1 ^ (l1 >> 30)) * 1664525l)) + seeds[j] + j; // non linear
          mt[i]   = (int) (l & 0xffffffffl);
          i++; j++;
          if (i >= N) {
            mt[0] = mt[N - 1];
            i = 1;
          }
          if (j >= seeds.length) {
            j = 0;
          }
        }

        for (int k = N - 1; k != 0; k--) {
          long l0 = (mt[i] & 0x7fffffffl)   | ((mt[i]   < 0) ? 0x80000000l : 0x0l);
          long l1 = (mt[i-1] & 0x7fffffffl) | ((mt[i-1] < 0) ? 0x80000000l : 0x0l);
          long l  = (l0 ^ ((l1 ^ (l1 >> 30)) * 1566083941l)) - i; // non linear
          mt[i]   = (int) (l & 0xffffffffL);
          i++;
          if (i >= N) {
            mt[0] = mt[N - 1];
            i = 1;
          }
        }

        mt[0] = 0x80000000; // MSB is 1; assuring non-zero initial array
		
	}
	
	 /** Initialize the generator with given long seed.
	   * <p>The state of the generator is exactly the same as a new
	   * generator built with the same seed.</p>
	   * @param seed the initial seed (64 bits integer)
	   */
	  public void seedInitialization(long seed) {
	    if (mt == null) {
	      // this is probably a spurious call from base class constructor,
	      // we do nothing and wait for the setSeed in our own
	      // constructors after array allocation
	      return;
	    }
	    seedInitialization(new int[] { (int) (seed >>> 32), (int) (seed & 0xffffffffl) });
	  }
	
	
	
	public long nextInt32() {
			  	
	  	int y;
	  	int k;
	  	
	  	long[] mag01 = {0x0, MATRIX_A};

        if (mti >= N) { /* generate N words at one time */
            
        	int mtNext = mt[0];

            for (k=0;k<N-M;++k) {
            	int mtThis = mtNext;
                y = (mtThis & UPPER_MASK)|(mtNext & LOWER_MASK);
                int kPlusM = k+M;
    //            mt[k] = mt[k+M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (k=N; k<N-1;++k) {
            	int mtThis = mtNext;
            	mtNext = mt[k+1];
                y = (mtThis & UPPER_MASK)|(mtNext & LOWER_MASK);
     //           mt[k] = mt[k+(M-N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mtNext & UPPER_MASK)|(mt[0] & LOWER_MASK);
     //       mt[N-1] = mt[M-1] ^ (y >>> 1) ^ mag01[y & 0x1];

            mti = 0;
        }

        y = mt[mti++];
	  	
	  	//
	  	// Tempering
	  	//
		y ^= y >>> 11; // y ^= TEMPERING_SHIFT_U(y );
		y ^= (y << 7) & TEMPERING_MASK_B; // y ^= TEMPERING_SHIFT_S(y) & TEMPERING_MASK_B;
		y ^= (y << 15) & TEMPERING_MASK_C; // y ^= TEMPERING_SHIFT_T(y) & TEMPERING_MASK_C;	
		// y &= 0xffffffff; //you may delete this line if word size = 32 
		y ^= y >>> 18; // y ^= TEMPERING_SHIFT_L(y);

		return y;
	}
}


