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
	private static final long N = 624;
	private static final long M = 397;
	
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
	private long[] mt = new long[(int)N];
	
	
	
	//
	// Constructors
	//
	
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
	    for (mti = 1; mti < N; ++mti) {
	    	mt[mti] = (1812433253 * (mt[mti-1] ^ (mt[mti-1] >> 30 )) + mti);
	    	// See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier.
	    	// initializer from the 2002-01-09 C version by Makoto Matsumoto
	    	mt[mti]&= 0xffffffff;
	    }
	  }
	
	/*
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
	*/
	
	/*
	  public void seedInitialization(long seed) {
	    if (mt == null) {
	      // this is probably a spurious call from base class constructor,
	      // we do nothing and wait for the setSeed in our own
	      // constructors after array allocation
	      return;
	    }
	    seedInitialization(new int[] { (int) (seed >>> 32), (int) (seed & 0xffffffffl) });
	  }
	*/
	
	
	public long nextInt32() {
			  	
	  	long y;
	  	int k;
	  	
	  	long[] mag01 = {0x0, MATRIX_A};

        if (mti >= N) { /* generate N words at one time */
            
        	long mtNext = mt[0];

            for (k=0;k<N-M;k++) {
            	long mtThis = mtNext;
                y = (mtThis & UPPER_MASK)|(mtNext & LOWER_MASK);
  //              mt[k] = mt[k+(int)M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (k=(int)N; k<N-1;++k) {
            	long mtThis = mtNext;
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


