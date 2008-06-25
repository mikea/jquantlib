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

// import org.jquantlib.math.randomnumbers.SeedGenerator;


/**
 * 
 * @author Dominik Holenstein
 *
 */

// TODO: Under construction

/*
public class MersenneTwisterUniformRng {
	
	
	//
	// Constants
	//
	
	// Period parameters
	final static int N = 624;
	final static int M = 397;

	
	// constant vector a	
	final long MATRIX_A = 0x9908b0df;
	
	// most significant w-r bits
	final long UPPER_MASK = 0x80000000;
	
	// least significant r bits
	final long LOWER_MASK = 0x7fffffff;
	
	
	//
	// private fields
	//
	
	// the array for the state vector
	private ArrayList<Long> mt; 
	
	// it is mutable in QuantLib
	static int mti=N+1; // mti==N+1 means mt[N] is not initialized
	
	
	//
	// Constructor
	//
	
	public MersenneTwisterUniformRng(long seed){
		seedInitialization(seed);
	}
	
	void seedInitialization(long seed){
		
		// iniitalizes mt with a seed 
		long s = (seed !=0 ? SeedGenerator.getInstance());
		mt[0] = s & 0xffffffff;
		for (mti = 1; mti<N; mti++) {
			mt[mtj] = (1812433253 * (mt[mti-1]^(mt[mti-1] >> 30)) + mti);
			
			// See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. 
	        // In the previous versions, MSBs of the seed affect   
	        // only MSBs of the array mt[].                        
	        // 2002/01/09 modified by Makoto Matsumoto             
	        mt[mti] &= 0xffffffff;
	        // for >32 bit machines 
		}
	}
	
	
	void MersenneTwisterUniformRngByArray(ArrayList<Long> seeds){
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
	
	
	
	public long nextInt32() {
		
		long y;
        final long mag01[]={0x0, MATRIX_A};
        // mag01[x] = x * MATRIX_A  for x=0,1 

        if (mti >= N) { // generate N words at one time 
            int kk;

            for (kk=0;kk<N-M;kk++) {
                y = (mt[kk]&UPPER_MASK)|(mt[kk+1]&LOWER_MASK);
                mt[kk] = mt[kk+M] ^ (y >> 1) ^ mag01[y & 0x1];
            }
            for (;kk<N-1;kk++) {
                y = (mt[kk]&UPPER_MASK)|(mt[kk+1]&LOWER_MASK);
                mt[kk] = mt[kk+(M-N)] ^ (y >> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N-1]&UPPER_MASK)|(mt[0]&LOWER_MASK);
            mt[N-1] = mt[M-1] ^ (y >> 1) ^ mag01[y & 0x1];

            mti = 0;
        }

        y = mt[mti++];

        // Tempering
        y ^= (y >> 11);
        y ^= (y << 7) & 0x9d2c5680;
        y ^= (y << 15) & 0xefc60000;
        y ^= (y >> 18);

        return y;
		
	} 
	
	
}

*/
