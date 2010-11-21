/*
 Copyright (C) 2010 Zahid Hussain

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

import java.util.List;

import org.jquantlib.time.Date;

/**
 * This class contains utility methods similar to C++ std functions
 * @author Zahid Hussain
 */
public class StdUtils {

	/**
	 * This method is equivalent to std:lower_bound function
	 * Returns an index pointing to the first element in the ordered collection is equal or greater than passed value
	 *
	 * @param valueList order collection in ascending order
	 * @param value Date to be compared
	 * @return index to element which is >= passed value
	 */

	public static <T> int lowerBound(final List<T> valueList, int fromIdx, int toIdx, final Comparable<T> value) {
        int len = toIdx - fromIdx + ( toIdx > fromIdx ? 1 : 0);
        int from = fromIdx;
        int half;
        int middle;

        while (len > 0) {
            half = len >> 1;
            middle = from;
            middle = middle + half;

            if (value.compareTo(valueList.get(middle)) == 1) { // value > 1
                from = middle;
                from++;
                len = len - half - 1;
            } else {
                len = half;
            }
        }
        return from;
    }
	
	public static <T> int lowerBound(final List<T> valueList,  final Comparable<T> value) {
		return StdUtils.lowerBound(valueList, 0, valueList != null && !valueList.isEmpty()? valueList.size()-1 :0, value);
	}

	/**
	 * This method is equivalent to C++ std:upper_bound function
	 * Returns an index pointing to the first element in the ordered collection which is greater than passed value
	 * @param <T>

	 * @param valueList order collection in ascending order
	 * @param value Date to be compared
	 * @return index to element which is > passed value
	 */
	public static <T> int upperBound(final List<T> valueList, int fromIdx, int toIdx, final Comparable<T> value) {

        int len = toIdx - fromIdx + ( toIdx > fromIdx ? 1 : 0);
        int from = fromIdx;
        int half;
        int middle;

        while (len > 0) {
            half = len >> 1;
            middle = from;
            middle = middle + half;
            if (value.compareTo(valueList.get(middle)) == -1) {
                len = half;
            } else {
                from = middle;
                from++;
                len = len - half - 1;
            }
        }
        return from;
	}
	public static <T> int upperBound(final List<T> valueList, final Comparable<T> value) {
		return StdUtils.upperBound(valueList, 0, valueList != null && !valueList.isEmpty()? valueList.size()-1 :0, value);
	}
	
	/**
	 * This method is equivalent to C++ std:distance function
	 * Return distance between indices (or iterators)
	 * Calculates the number of elements between first and last.
	 * If i is a Random Access Iterator, the function uses operator- to calculate this. Otherwise, the function uses repeatedly the increase or decrease operator (operator++ or operator--) until this distance is calculated.
	*/

	public static <T>int distance(final List<T> values, int first, int last) {
		int size = last - first;
		return size;
     }
	

}
