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

package org.jquantlib.experimental.collection;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.util.Date;
import org.junit.Before;
import org.junit.Test;

public class PrimitiveCollectionVisitorTest {

    private final static Logger logger = LoggerFactory.getLogger(PrimitiveCollectionVisitorTest.class);
    
    Date startDate = null;

	@Before
	public void init() {
	}

	@Test
	public void testPrimitiveDoubleCollection() {
		long startNano = System.currentTimeMillis();
		DoubleArrayList dal = new DoubleArrayList();
		PrimitiveCollectionAddVisitor v = PrimitiveCollectionAddVisitor.impl;
		for (double i = 0.0; i < 1000; i = i + .01) {
			v.visitAddDoubleCollection(dal, i);
		}

		long totalTime = System.currentTimeMillis() - startNano;
		logger.info("      PrimitiveCollectionAddVisitor without  autoboxing : " + totalTime + " milli seconds");
	}

	@Test
	@SuppressWarnings(value = "unchecked")
	public void testJavaUtilList() {
		long startNano = System.currentTimeMillis();
		DoubleArrayList dal = new DoubleArrayList();
		java.util.List list = dal;
		for (double i = 0.0; i < 1000; i = i + .01) {
			list.add(i);
		}

		long totalTime = System.currentTimeMillis() - startNano;
		logger.info("      java.util.list with  autoboxing : " + totalTime + " milli seconds");
	}

	public static void main(String[] args) {
		// List l = new org.jquantlib.util.List(new DoubleArrayList());
		List l = new DoubleArrayList();
		l.add(77.88);// autoboxing
		((DoubleArrayList) l).add(6.66);
		((DoubleArrayList) l).add(11);

	}
}
