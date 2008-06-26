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

/*
package org.jquantlib.math.randomnumbers;

import java.util.ArrayList;
import java.util.Date;

// import org.jquantlib.math.randomnumbers.SeedGenerator;


/**
 * 
 * @author Dominik Holenstein
 *
 */

// TODO: Under construction

/*
public class MersenneTwisterUniformRng {

	private int mti; 
	
	// set initial seeds: N = 624 words
	private int[] mt = new int[N];
	
	
	//
	// Constants
	//
	
	// Period parameters
	private static final int N = 624;
	private static final int M = 397;

	
	// constant vector a	
	private static final int MATRIX_A = 0x9908b0df;
	
	// most significant w-r bits
	private static final int UPPER_MASK = 0x80000000;
	
	// least significant r bits
	private static final int LOWER_MASK = 0x7fffffff;
	
	//
	private static final int TEMPERING_MASK_B=0x9d2c5680;
	private static final int TEMPERING_MASK_C=0xefc60000;
	
	public static final int DEFAULT_SEED = 4357;
	
	
	//
	// Constructors
	//
	
	/**
	 * Constructs and returns a random number generator with a default seed, which is a <b>constant</b>.
	 * Thus using this constructor will yield generators that always produce exactly the same sequence.
	 * This method is mainly intended to ease testing and debugging.
	 **/
/*
	public MersenneTwisterUniformRng() {
		this(DEFAULT_SEED);
	}
	
	/**
	 * Constructs and returns a random number generator with the given seed.
	 */
	
	/*
	public MersenneTwisterUniformRng(int seed){
		seedInitialization(seed);
	}
	
	
	/**
	 * Constructs and returns a random number generator seeded with the given date.
	 *
	 * @param d typically <tt>new java.util.Date()</tt>
	 */
	
	/*
	public MersenneTwisterUniformRng(Date d) {
		this((int)d.getTime());
	}
	
	
	void seedInitialization(int seed){
		
		// iniitalizes mt with a seed 
		int s = seed;
		mt[0] = s & 0xffffffff;
		for (int i = 1; i < N; i++) {
			mt[i] = (1812433253 * (mt[i-1] ^ (mt[i-1] >> 30)) + i);
			
			// See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. 
	        // In the previous versions, MSBs of the seed affect   
	        // only MSBs of the array mt[].                        
	        // 2002/01/09 modified by Makoto Matsumoto             
	        mt[i] &= 0xffffffff;
	        // for >32 bit machines 
		}
		mti = N;
	}
	
	
	protected void nextBlock(){
		seedInitialization(19650218);
        int i=1;
        int j=0;
        
        //
        // for better readability 
        // c++ code:
        //  k = (N>seeds.size()) ? N : seeds.size());
        //
        
        if (seeds.size()<N) {
        	int k = N;
        }
        else {
        	int k = seeds.size();
        }
        
        for (; k; k--) {
            mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >> 30)) * 1664525)) + seeds[j] + j; // non linear 
            mt[i] &= 0xffffffff; // for WORDSIZE > 32 machines 
            i++; j++;
            
            if (i>=N) { 
            	mt[0] = mt[N-1]; 
            	i=1; 
            }
            
            if (j>=seeds.size()) {
            	j=0;
            }
        }
        
        for (k=N-1; k; k--) {
            mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >> 30)) * 1566083941)) - i; // non linear 
            mt[i] &= 0xffffffff; // for WORDSIZE > 32 machines 
            i++;
            if (i>=N) { mt[0] = mt[N-1]; i=1; }
        }

        mt[0] = 0x80000000; // MSB is 1; assuring non-zero initial array
		
	}
	
	public int nextInt32() {
		
		// Each single bit including the sign bit will be random 
	  	if (mti == N) {
	  		nextBlock(); // generate N ints at one time
	  	}
	  	
	  	int y = mt[mti++];
		y ^= y >>> 11; // y ^= TEMPERING_SHIFT_U(y );
		y ^= (y << 7) & TEMPERING_MASK_B; // y ^= TEMPERING_SHIFT_S(y) & TEMPERING_MASK_B;
		y ^= (y << 15) & TEMPERING_MASK_C; // y ^= TEMPERING_SHIFT_T(y) & TEMPERING_MASK_C;	
		// y &= 0xffffffff; //you may delete this line if word size = 32 
		y ^= y >>> 18; // y ^= TEMPERING_SHIFT_L(y);

		return y;
	}
}

*/
