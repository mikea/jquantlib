/*
 Copyright (C) 2008 Anand Mani

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

package org.jquantlib.testsuite.util.collection;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import org.jquantlib.util.Date;
import org.jquantlib.util.collection.PrimitiveCollectionAddVisitor;
import org.junit.Before;
import org.junit.Test;

public class PrimitiveCollectionVisitorTest {

	Date startDate = null;

	@Before
	public void init() {
	}

	@Test
	public void testPrimitiveDoubleCollection() {
		long startNano = System.nanoTime();
		DoubleArrayList dal = new DoubleArrayList();
		PrimitiveCollectionAddVisitor v = PrimitiveCollectionAddVisitor.impl;
		for (double i = 0.0; i < 1000; i = i + .01) {
			v.visitAddDoubleCollection(dal, i);
		}

		long totalTime = System.nanoTime() - startNano;
		System.out.println("      PrimitiveCollectionAddVisitor without  autoboxing : " + totalTime + " nano seconds");
	}

	@Test
	@SuppressWarnings(value = "unchecked")
	public void testJavaUtilList() {
		long startNano = System.nanoTime();
		DoubleArrayList dal = new DoubleArrayList();
		java.util.List list = dal;
		for (double i = 0.0; i < 1000; i = i + .01) {
			list.add(i);
		}

		long totalTime = System.nanoTime() - startNano;
		System.out.println("      java.util.list with  autoboxing : " + totalTime + " nano seconds");
	}

}
