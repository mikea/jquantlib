/*
 Copyright (C) 2007 Richard Gomes

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


package org.jquantlib.util;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * 
 * @author Dominik Holenstein
 *
 */

public class Std {
	
	
	static public List<Double> adjacent_difference(List<Double> inputList, int begin, List<Double> diffList) {
	
		for (int i = begin; i<inputList.size(); i++) {
			double curr = inputList.get(i);
			if (i == 0) {
				diffList.add(inputList.get(i));
			}
			else {
				double prev = inputList.get(i-1);
				diffList.add(curr-prev);
			}
		}
		return diffList;
	}





}
