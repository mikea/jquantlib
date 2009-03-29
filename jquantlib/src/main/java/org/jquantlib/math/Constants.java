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

package org.jquantlib.math;

/**
 * @author <Richard Gomes>
 */
final strictfp public class Constants {
	
	//
	// private constructor
	//

	private Constants() {
		//deliberate
	}

	
	//
	// Defines public constants
	//
	
	public final static double M_SQRT2     = 1.41421356237309504880;
	public final static double M_SQRT_2    = 0.7071067811865475244008443621048490392848359376887;
	public final static double M_SQRT2PI   = 2.50662827463100050242;
	public final static double M_1_SQRTPI  = 0.564189583547756286948;
	public final static double M_SQRTPI    = 1.77245385090551602792981;
	public final static double M_LN2       = 0.693147180559945309417;
	public final static double M_1_SQRT2PI = M_SQRT_2*M_1_SQRTPI;
	
	public final static double QL_EPSILON  = Math.ulp(1.0);             // typically about 2.2e-16
	public final static double QL_MAX_REAL = Double.MAX_VALUE;          // typically about 1.8e+308
	public final static double QL_MIN_POSITIVE_REAL = Double.MIN_VALUE; // typically about 2.22E-308 

}
