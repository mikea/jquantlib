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



package org.jquantlib.math.randomnumbers.trial;

import java.util.ArrayList; // FIXME: performance
import java.util.Date;
import java.util.List;



/**
 * 
 * @author Dominik Holenstein
 *
 **/

//TODO: Under construction


public class SeedGenerator {
	
	//
	// private static fields
	//
	
    /**
     * @see <a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">The "Double-Checked Locking is Broken" Declaration </a>
     */
	private static volatile SeedGenerator instance;
	
	
	//
	// private fields
	//
	
	private MersenneTwisterUniformRng rng_ = new MersenneTwisterUniformRng();
	
	
	//
	// Constructor
	//
		
	public SeedGenerator() {
		// rng_ = 42;
	}

	public void initialize() {
		
		// firstSeed is chosen based on Date and used for the first rng 
		Date now = new Date();
		long firstSeed = now.getTime();
		MersenneTwisterUniformRng first = new MersenneTwisterUniformRng(firstSeed);
		
		// secondSeed is as random as it could be
		// feel free to suggest improvements
	 	long secondSeed = first.nextInt32();
		MersenneTwisterUniformRng second = new MersenneTwisterUniformRng(secondSeed);
		
		// use the second rng to initialize the final one
		long skip = second.nextInt32() % 1000;
		List<Long> init = new ArrayList<Long>(4);
		init.add(second.nextInt32());
		init.add(second.nextInt32());
		init.add(second.nextInt32());
		init.add(second.nextInt32());
		
	//	MerseenneTwisterUniformRng rng_ = MersenneTwisterUniformRngByArray(init);  // --> Constructor for long[] not available yet
		
		for (long i=0; i<skip; i++ ){
			rng_.nextInt32();
		}
	}
	
	//
	// Singleton pattern.
	// Create new instance only if it does not exist yet
	//
	
	public static SeedGenerator getInstance() {
		if (instance == null) {
			synchronized (SeedGenerator.class) {
				if (instance == null) {
					instance = new SeedGenerator();
				}
			}
		}
		return instance; 
	}

	
	public long get() {
		return  rng_.nextInt32();
	}
	
}


