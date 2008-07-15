/*
 Copyright (C) 2007 Richard Gomes
 Copyright (C) 2008 Q. Boiler

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
package org.jquantlib.performance.microscopic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.jquantlib.performance.PerformanceResults;
import org.jquantlib.performance.PerformanceTest;

/**
 *
 * @author Q.Boiler
 */
public class Array implements PerformanceTest {

	Hashtable<Integer,List<org.jquantlib.math.Array>> ArraysHashTable
		= new Hashtable<Integer,List<org.jquantlib.math.Array>>();
	
	@Override
	public PerformanceResults execute() {
		org.jquantlib.math.Array array;
		PerformanceResults pr = new PerformanceResults();
		pr.testName="Array BuildArrays";
		long startTime =System.currentTimeMillis();

		ArraysHashTable.put(new Integer(10), PopulateArrays(10));
		ArraysHashTable.put(new Integer(100), PopulateArrays(100));
		ArraysHashTable.put(new Integer(1000), PopulateArrays(1000));
		ArraysHashTable.put(new Integer(10000), PopulateArrays(10000));
		ArraysHashTable.put(new Integer(100000), PopulateArrays(100000));
		ArraysHashTable.put(new Integer(1000000), PopulateArrays(1000000));


		pr.units=PerformanceResults.RUNTIME_UNITS.MILLISECONDS;
		pr.runtime=System.currentTimeMillis()-startTime;

		List<PerformanceResults> lists = executeDotProductCompositeTest();
		pr.compositeResults = lists;

		
		return pr;
	}
	private List<PerformanceResults> executeDotProductCompositeTest(){
		
		List<PerformanceResults> dotProductResults = new ArrayList<PerformanceResults>();

		for(int i =10;i<=1000000;i*=10){
			PerformanceResults pr = executeDotProductTest(i);
			dotProductResults.add(pr);

		}
		return dotProductResults;
		
	}
	private List<org.jquantlib.math.Array> PopulateArrays(int size){

		ArrayList<org.jquantlib.math.Array> list = new ArrayList<org.jquantlib.math.Array>();

		for(int j =0;j<10;++j){
		org.jquantlib.math.PrimeNumbers pn = new org.jquantlib.math.PrimeNumbers();

		long prime = pn.get(7000);
		org.jquantlib.math.randomnumbers.SFMTUniformRng rng
			= new org.jquantlib.math.randomnumbers.SFMTUniformRng((int)prime);

		org.jquantlib.math.Array a = new org.jquantlib.math.Array(size);
		for(int i =0;i<size;++i){
			a.set(i, (double)rng.next()/(double)prime);
		}
		list.add(a);
		}

		return list;
	}
	private PerformanceResults executeDotProductTest(int size){

		PerformanceResults pr = new PerformanceResults();
		pr.testName = "DotProduct Test "+size+": ";
		long startTime = System.currentTimeMillis();
		List<org.jquantlib.math.Array> localLists = ArraysHashTable.get(new Integer(size));
		if(localLists == null){
			//  Throw Exception.
			System.out.println("Test Failed... for size: " +size);
		}

		Iterator iter =localLists.iterator();
		org.jquantlib.math.Array arr = (org.jquantlib.math.Array)iter.next();
		while(iter.hasNext()){
			org.jquantlib.math.Array brr = (org.jquantlib.math.Array)iter.next();
			arr.dotProduct(arr, brr);
		}
		pr.units=PerformanceResults.RUNTIME_UNITS.MILLISECONDS;
		pr.runtime=System.currentTimeMillis()-startTime;
		return pr;
	}


}
