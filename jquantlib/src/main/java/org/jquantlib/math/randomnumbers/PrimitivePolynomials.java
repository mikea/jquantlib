
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




/*	PPMT : Primitive Polynomials Modulo Two
 *
 *
 * The encoding is as follows:
 *
 * The coefficients of each primitive polynomial are the bits of the given
 * integer. The leading and trailing coefficients, which are 1 for all of the
 * polynomials, have been omitted.
 *
 * Example: The polynomial
 *
 *      4    2
 *     x  + x  + 1
 *
 * is encoded as  2  in the array of polynomials of degree 4 because the
 * binary sequence of coefficients
 *
 *   10101
 *
 * becomes
 *
 *    0101
 *
 * after stripping off the top bit, and this is converted to
 *
 *     010
 *
 * by right-shifting and losing the rightmost bit. Similarly, we have
 *
 *   5    4    2
 *  x  + x  + x  + x + 1
 *
 * encoded as  13  [ (1)1101(1) ]  in the array for degree 5.
 */

/* Example: replace primitivepolynomials.c provided by QuantLib standard
 * distribution with the 8129334 polinomials version and
 * comment out the line below if you want absolutely all of the
 * provided primitive polynomials modulo two.
 *
 * #define PPMT_MAX_DIM 8129334
 *
 * Note that PPMT_MAX_DIM will be redefined to be the nearest equal or larger
 * number of polynomials up to one of the predefined macros
 * N_PRIMITIVES_UP_TO_DEGREE_XX
 * below.
 */

/*
package org.jquantlib.math.randomnumbers;

/**
 * 
 * @author Dominik Holenstein
 */

/*
public class PrimitivePolynomials {
	
	static final long N_PRIMITIVES_UP_TO_DEGREE_01        =  1;
	static final long N_PRIMITIVES_UP_TO_DEGREE_02        =  2;
	static final long N_PRIMITIVES_UP_TO_DEGREE_03        =  4;
	static final long N_PRIMITIVES_UP_TO_DEGREE_04        =  6;
	static final long N_PRIMITIVES_UP_TO_DEGREE_05        = 12;
	static final long N_PRIMITIVES_UP_TO_DEGREE_06        = 18;
	static final long N_PRIMITIVES_UP_TO_DEGREE_07        = 36;
	static final long N_PRIMITIVES_UP_TO_DEGREE_08        = 52;
	static final long N_PRIMITIVES_UP_TO_DEGREE_09       = 100;
	static final long N_PRIMITIVES_UP_TO_DEGREE_10       = 160;
	static final long N_PRIMITIVES_UP_TO_DEGREE_11       = 336;
	static final long N_PRIMITIVES_UP_TO_DEGREE_12       = 480;
	static final long N_PRIMITIVES_UP_TO_DEGREE_13      = 1110;
	static final long N_PRIMITIVES_UP_TO_DEGREE_14      = 1866;
	static final long N_PRIMITIVES_UP_TO_DEGREE_15      = 3666;
	static final long N_PRIMITIVES_UP_TO_DEGREE_16      = 5714;
	static final long N_PRIMITIVES_UP_TO_DEGREE_17     = 13424;
	static final long N_PRIMITIVES_UP_TO_DEGREE_18     = 21200;
	static final long N_PRIMITIVES_UP_TO_DEGREE_19     = 48794;
	static final long N_PRIMITIVES_UP_TO_DEGREE_20     = 72794;
	static final long N_PRIMITIVES_UP_TO_DEGREE_21    = 157466;
	static final long N_PRIMITIVES_UP_TO_DEGREE_22    = 277498;
	static final long N_PRIMITIVES_UP_TO_DEGREE_23    = 634458;
	static final long N_PRIMITIVES_UP_TO_DEGREE_24    = 910938;
	static final long N_PRIMITIVES_UP_TO_DEGREE_25   = 2206938;
	static final long N_PRIMITIVES_UP_TO_DEGREE_26   = 3926838;
	static final long N_PRIMITIVES_UP_TO_DEGREE_27   = 8129334;
	
	static final long N_PRIMITIVES = N_PRIMITIVES_UP_TO_DEGREE_27;
	
	// #ifndef  PPMT_MAX_DIM
	static long PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_18;
	// #endif
	
	static long N_MAX_DEGREE;
*/


	// example C++ for the following code
	/*
	#if      PPMT_MAX_DIM > N_PRIMITIVES
# error  PPMT_MAX_DIM cannot be greater than N_PRIMITIVES
#elif    PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_01
# undef  PPMT_MAX_DIM
# define PPMT_MAX_DIM N_PRIMITIVES_UP_TO_DEGREE_01
# define N_MAX_DEGREE 01
   */

	
/*
	if (PPMT_MAX_DIM > N_PRIMITIVES) {
		throw new ArithmeticException("PPMT_MAX_DIM cannot be greater tha N_PRIMITIVES");
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_01) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_01;
		N_MAX_DEGREE = 01;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_02) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_02;
		N_MAX_DEGREE = 02;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_03) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_03;
		N_MAX_DEGREE = 03;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_04) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_04;
		N_MAX_DEGREE = 04;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_05) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_05;
		N_MAX_DEGREE = 05;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_06) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_06;
		N_MAX_DEGREE = 06;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_07) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_07;
		N_MAX_DEGREE = 07;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_08) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_08;
		N_MAX_DEGREE = 08;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_09) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_09;
		N_MAX_DEGREE = 09;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_10) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_10;
		N_MAX_DEGREE = 10;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_11) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_11;
		N_MAX_DEGREE = 11;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_11) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_11;
		N_MAX_DEGREE = 11;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_12) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_12;
		N_MAX_DEGREE = 12;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_13) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_13;
		N_MAX_DEGREE = 13;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_14) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_14;
		N_MAX_DEGREE = 14;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_15) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_15;
		N_MAX_DEGREE = 15;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_16) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_16;
		N_MAX_DEGREE = 16;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_17) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_17;
		N_MAX_DEGREE = 17;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_18) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_18;
		N_MAX_DEGREE = 18;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_19) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_19;
		N_MAX_DEGREE = 19;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_20) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_20;
		N_MAX_DEGREE = 20;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_21) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_21;
		N_MAX_DEGREE = 21;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_22) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_22;
		N_MAX_DEGREE = 22;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_23) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_23;
		N_MAX_DEGREE = 23;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_24) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_24;
		N_MAX_DEGREE = 24;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_25) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_25;
		N_MAX_DEGREE = 25;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_26) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_26;
		N_MAX_DEGREE = 26;
	}
	else if (PPMT_MAX_DIM <= N_PRIMITIVES_UP_TO_DEGREE_27) {
		PPMT_MAX_DIM = N_PRIMITIVES_UP_TO_DEGREE_27;
		N_MAX_DEGREE = 27;
	}
	
*/
	/* You can access the following array as in PrimitivePolynomials[i][j]
    with i and j counting from 0 in C convention. PrimitivePolynomials[i][j]
    will get you the j-th (counting from zero) primitive polynomial of degree
    i+1. Each one-dimensional array of primitive polynomials of a given
    degree is terminated with an entry of -1. Accessing beyond this entry
    will result in a memory violation and must be avoided.  */
	
	/*
	static final long PrimitivePolynomials[N_MAX_DEGREE];
	
	
	
	static final long PrimitivePolynomialDegree01[]={
		0, // x+1 (1)(1) 
		-1 };
	

}
*/