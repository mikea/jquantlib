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


import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.methods.montecarlo.Sample;

/**
 *  Sobol low-discrepancy sequence generator
 *  <p>
 * 	A Gray code counter and bitwise operations are used for very
 * 		 fast sequence generation.<p>
 * 		 The implementation relies on primitive polynomials modulo two
 *       from the book "Monte Carlo Methods in Finance" by Peter
 *       Jaeckel.<p>
 *
 *       21 200 primitive polynomials modulo two are provided in QuantLib.
 *       Jaeckel has calculated 8 129 334 polynomials: if you need that many
 *       dimensions you can replace the primitivepolynomials.c file included
 *       in QuantLib with the one provided in the CD of the "Monte Carlo
 *        Methods in Finance" book.<p>
 *
 *       The choice of initialization numbers (also know as free direction
 *       integers) is crucial for the homogeneity properties of the sequence.
 *       Sobol defines two homogeneity properties: Property A and Property A'.<p>
 *
 *       The unit initialization numbers suggested in "Numerical
 *       Recipes in C", 2nd edition, by Press, Teukolsky, Vetterling,
 *       and Flannery (section 7.7) fail the test for Property A even
 *       for low dimensions.<p>
 *
 *		 Bratley and Fox published coefficients of the free direction
 *       integers up to dimension 40, crediting unpublished work of
 *       Sobol' and Levitan. See Bratley, P., Fox, B.L. (1988)
 *       "Algorithm 659: Implementing Sobol's quasirandom sequence
 *       generator," ACM Transactions on Mathematical Software
 *       14:88-100. These values satisfy Property A for d<=20 and d =
 *       23, 31, 33, 34, 37; Property A' holds for d<=6.<p>
 *
 *       Jaeckel provides in his book (section 8.3) initialization
 *       numbers up to dimension 32. Coefficients for d<=8 are the same
 *       as in Bradley-Fox, so Property A' holds for d<=6 but Property
 *       A holds for d<=32.<p>
 *
 *       The implementation of Lemieux, Cieslak, and Luttmer includes
 *       coefficients of the free direction integers up to dimension
 *       360.  Coefficients for d<=40 are the same as in Bradley-Fox.
 *       For dimension 40<d<=360 the coefficients have
 *       been calculated as optimal values based on the "resolution"
 *       criterion. See "RandQMC user's guide - A package for
 *       randomized quasi-Monte Carlo methods in C," by C. Lemieux,
 *       M. Cieslak, and K. Luttmer, version January 13 2004, and
 *       references cited there
 *       (http://www.math.ucalgary.ca/~lemieux/randqmc.html).
 *       The values up to d<=360 has been provided to the QuantLib team by
 *       Christiane Lemieux, private communication, September 2004.<p>
 *
 *       For more info on Sobol' sequences see also "Monte Carlo
 *       Methods in Financial Engineering," by P. Glasserman, 2004,
 *       Springer, section 5.2.3<p>
 *
 *       <strong>Test</strong>
 *       - the correctness of the returned values is tested by
 *         reproducing known good values.
 *       - the correctness of the returned values is tested by checking
 *         their discrepancy against known good values
 *
 * 
 * 
 *   
 * @author Dominik Holenstein
 *
 */

public class SobolRsg implements SequenceGenerator<Sample<List<Double>>> {
	
	// Sobol' Levitan coefficients of the free direction integers as given
    // by Bratley, P., Fox, B.L. (1988)
	final long dim02SLinitializers[] = { 1, 0 };
    final long dim03SLinitializers[] = { 1, 1, 0 };
    final long dim04SLinitializers[] = { 1, 3, 7,  0 };
    final long dim05SLinitializers[] = { 1, 1, 5,  0 };
    final long dim06SLinitializers[] = { 1, 3, 1,  1,  0 };
    final long dim07SLinitializers[] = { 1, 1, 3,  7,  0 };
    final long dim08SLinitializers[] = { 1, 3, 3,  9,  9,  0 };
    final long dim09SLinitializers[] = { 1, 3, 7, 13,  3,  0 };
    final long dim10SLinitializers[] = { 1, 1, 5, 11, 27,  0 };
    final long dim11SLinitializers[] = { 1, 3, 5,  1, 15,  0 };
    final long dim12SLinitializers[] = { 1, 1, 7,  3, 29,  0 };
    final long dim13SLinitializers[] = { 1, 3, 7,  7, 21,  0 };
    final long dim14SLinitializers[] = { 1, 1, 1,  9, 23, 37,   0 };
    final long dim15SLinitializers[] = { 1, 3, 3,  5, 19, 33,   0 };
    final long dim16SLinitializers[] = { 1, 1, 3, 13, 11,  7,   0 };
    final long dim17SLinitializers[] = { 1, 1, 7, 13, 25,  5,   0 };
    final long dim18SLinitializers[] = { 1, 3, 5, 11,  7, 11,   0 };
    final long dim19SLinitializers[] = { 1, 1, 1,  3, 13, 39,   0 };
    final long dim20SLinitializers[] = { 1, 3, 1, 15, 17, 63,  13,  0 };
    final long dim21SLinitializers[] = { 1, 1, 5,  5,  1, 27,  33,  0 };
    final long dim22SLinitializers[] = { 1, 3, 3,  3, 25, 17, 115,  0 };
    final long dim23SLinitializers[] = { 1, 1, 3, 15, 29, 15,  41,  0 };
    final long dim24SLinitializers[] = { 1, 3, 1,  7,  3, 23,  79,  0 };
    final long dim25SLinitializers[] = { 1, 3, 7,  9, 31, 29,  17,  0 };
    final long dim26SLinitializers[] = { 1, 1, 5, 13, 11,  3,  29,  0 };
    final long dim27SLinitializers[] = { 1, 3, 1,  9,  5, 21, 119,  0 };
    final long dim28SLinitializers[] = { 1, 1, 3,  1, 23, 13,  75,  0 };
    final long dim29SLinitializers[] = { 1, 3, 3, 11, 27, 31,  73,  0 };
    final long dim30SLinitializers[] = { 1, 1, 7,  7, 19, 25, 105,  0 };
    final long dim31SLinitializers[] = { 1, 3, 5,  5, 21,  9,   7,  0 };
    final long dim32SLinitializers[] = { 1, 1, 1, 15,  5, 49,  59,  0 };
    final long dim33SLinitializers[] = { 1, 1, 1,  1,  1, 33,  65,  0 };
    final long dim34SLinitializers[] = { 1, 3, 5, 15, 17, 19,  21,  0 };
    final long dim35SLinitializers[] = { 1, 1, 7, 11, 13, 29,   3,  0 };
    final long dim36SLinitializers[] = { 1, 3, 7,  5,  7, 11, 113,  0 };
    final long dim37SLinitializers[] = { 1, 1, 5,  3, 15, 19,  61,  0 };
    final long dim38SLinitializers[] = { 1, 3, 1,  1,  9, 27,  89,  7, 0 };
    final long dim39SLinitializers[] = { 1, 1, 3,  7, 31, 15,  45, 23, 0 };
    final long dim40SLinitializers[] = { 1, 3, 3,  9,  9, 25, 107, 39, 0 };
 	
 	// const unsigned long * const SLinitializers[39] = { ... };
 	final long [][] SLinitializers = {
            dim02SLinitializers,
            dim03SLinitializers,
            dim04SLinitializers,
            dim05SLinitializers,
            dim06SLinitializers,
            dim07SLinitializers,
            dim08SLinitializers,
            dim09SLinitializers,
            dim10SLinitializers,
            dim11SLinitializers,
            dim12SLinitializers,
            dim13SLinitializers,
            dim14SLinitializers,
            dim15SLinitializers,
            dim16SLinitializers,
            dim17SLinitializers,
            dim18SLinitializers,
            dim19SLinitializers,
            dim20SLinitializers,
            dim21SLinitializers,
            dim22SLinitializers,
            dim23SLinitializers,
            dim24SLinitializers,
            dim25SLinitializers,
            dim26SLinitializers,
            dim27SLinitializers,
            dim28SLinitializers,
            dim29SLinitializers,
            dim30SLinitializers,
            dim31SLinitializers,
            dim32SLinitializers,
            dim33SLinitializers,
            dim34SLinitializers,
            dim35SLinitializers,
            dim36SLinitializers,
            dim37SLinitializers,
            dim38SLinitializers,
            dim39SLinitializers,
            dim40SLinitializers
        };
 	
 	
 	// coefficients of the free direction integers as given in
 	// "Monte Carlo Methods in Finance", by Peter Jaeckel, section 8.3
 
 	final long dim09initializers[] = { 1, 3, 7,  7, 21,  0 };
    final long dim10initializers[] = { 1, 1, 5, 11, 27,  0 };
    final long dim11initializers[] = { 1, 1, 7,  3, 29,  0 };
    final long dim12initializers[] = { 1, 3, 7, 13,  3,  0 };
    final long dim13initializers[] = { 1, 3, 5,  1, 15,  0 };
    final long dim14initializers[] = { 1, 1, 1,  9, 23, 37,   0 };
    final long dim15initializers[] = { 1, 1, 3, 13, 11,  7,   0 };
    final long dim16initializers[] = { 1, 3, 3,  5, 19, 33,   0 };
    final long dim17initializers[] = { 1, 1, 7, 13, 25,  5,   0 };
    final long dim18initializers[] = { 1, 1, 1,  3, 13, 39,   0 };
    final long dim19initializers[] = { 1, 3, 5, 11,  7, 11,   0 };
    final long dim20initializers[] = { 1, 3, 1,  7,  3, 23,  79, 0 };
    final long dim21initializers[] = { 1, 3, 1, 15, 17, 63,  13, 0 };
    final long dim22initializers[] = { 1, 3, 3,  3, 25, 17, 115, 0 };
    final long dim23initializers[] = { 1, 3, 7,  9, 31, 29,  17, 0 };
    final long dim24initializers[] = { 1, 1, 3, 15, 29, 15,  41, 0 };
    final long dim25initializers[] = { 1, 3, 1,  9,  5, 21, 119, 0 };
    final long dim26initializers[] = { 1, 1, 5,  5,  1, 27,  33, 0 };
    final long dim27initializers[] = { 1, 1, 3,  1, 23, 13,  75, 0 };
    final long dim28initializers[] = { 1, 1, 7,  7, 19, 25, 105, 0 };
    final long dim29initializers[] = { 1, 3, 5,  5, 21,  9,   7, 0 };
    final long dim30initializers[] = { 1, 1, 1, 15,  5, 49,  59, 0 };
    final long dim31initializers[] = { 1, 3, 5, 15, 17, 19,  21, 0 };
    final long dim32initializers[] = { 1, 1, 7, 11, 13, 29,   3, 0 };
 	
 	
 	final long [][] initializers = {
            dim02SLinitializers,
            dim03SLinitializers,
            dim04SLinitializers,
            dim05SLinitializers,
            dim06SLinitializers,
            dim07SLinitializers,
            dim08SLinitializers,
            dim09initializers,
            dim10initializers,
            dim11initializers,
            dim12initializers,
            dim13initializers,
            dim14initializers,
            dim15initializers,
            dim16initializers,
            dim17initializers,
            dim18initializers,
            dim19initializers,
            dim20initializers,
            dim21initializers,
            dim22initializers,
            dim23initializers,
            dim24initializers,
            dim25initializers,
            dim26initializers,
            dim27initializers,
            dim28initializers,
            dim29initializers,
            dim30initializers,
            dim31initializers,
            dim32initializers
        };
 	
 	
 	// Lemieux coefficients of the free direction integers as given
    // in Christiane Lemieux, private communication, September 2004

 	final long dim041Linitializers[] = { 1, 1, 3, 13, 7, 35, 61, 91, 0 };
    final long dim042Linitializers[] = { 1, 1, 7, 11, 5, 35, 55, 75, 0 };
    final long dim043Linitializers[] = { 1, 3, 5, 5, 11, 23, 29, 139, 0 };
    final long dim044Linitializers[] = { 1, 1, 1, 7, 11, 15, 17, 81, 0 };
    final long dim045Linitializers[] = { 1, 1, 7, 9, 5, 57, 79, 103, 0 };
    final long dim046Linitializers[] = { 1, 1, 7, 13, 19, 5, 5, 185, 0 };
    final long dim047Linitializers[] = { 1, 3, 1, 3, 13, 57, 97, 131, 0 };
    final long dim048Linitializers[] = { 1, 1, 5, 5, 21, 25, 125, 197, 0 };
    final long dim049Linitializers[] = { 1, 3, 3, 9, 31, 11, 103, 201, 0 };
    final long dim050Linitializers[] = { 1, 1, 5, 3, 7, 25, 51, 121, 0 };
    final long dim051Linitializers[] = { 1, 3, 7, 15, 19, 53, 73, 189, 0 };
    final long dim052Linitializers[] = { 1, 1, 1, 15, 19, 55, 27, 183, 0 };
    final long dim053Linitializers[] = { 1, 1, 7, 13, 3, 29, 109, 69, 0 };
    final long dim054Linitializers[] = { 1, 1, 5, 15, 15, 23, 15, 1, 57, 0 };
    final long dim055Linitializers[] = { 1, 3, 1, 3, 23, 55, 43, 143, 397, 0 };
    final long dim056Linitializers[] = { 1, 1, 3, 11, 29, 9, 35, 131, 411, 0 };
    final long dim057Linitializers[] = { 1, 3, 1, 7, 27, 39, 103, 199, 277, 0 };
    final long dim058Linitializers[] = { 1, 3, 7, 3, 19, 55, 127, 67, 449, 0 };
    final long dim059Linitializers[] = { 1, 3, 7, 3, 5, 29, 45, 85, 3, 0 };
    final long dim060Linitializers[] = { 1, 3, 5, 5, 13, 23, 75, 245, 453, 0 };
    final long dim061Linitializers[] = { 1, 3, 1, 15, 21, 47, 3, 77, 165, 0 };
    final long dim062Linitializers[] = { 1, 1, 7, 9, 15, 5, 117, 73, 473, 0 };
    final long dim063Linitializers[] = { 1, 3, 1, 9, 1, 21, 13, 173, 313, 0 };
    final long dim064Linitializers[] = { 1, 1, 7, 3, 11, 45, 63, 77, 49, 0 };
    final long dim065Linitializers[] = { 1, 1, 1, 1, 1, 25, 123, 39, 259, 0 };
    final long dim066Linitializers[] = { 1, 1, 1, 5, 23, 11, 59, 11, 203, 0 };
    final long dim067Linitializers[] = { 1, 3, 3, 15, 21, 1, 73, 71, 421, 0 };
    final long dim068Linitializers[] = { 1, 1, 5, 11, 15, 31, 115, 95, 217, 0 };
    final long dim069Linitializers[] = { 1, 1, 3, 3, 7, 53, 37, 43, 439, 0 };
    final long dim070Linitializers[] = { 1, 1, 1, 1, 27, 53, 69, 159, 321, 0 };
    final long dim071Linitializers[] = { 1, 1, 5, 15, 29, 17, 19, 43, 449, 0 };
    final long dim072Linitializers[] = { 1, 1, 3, 9, 1, 55, 121, 205, 255, 0 };
    final long dim073Linitializers[] = { 1, 1, 3, 11, 9, 47, 107, 11, 417, 0 };
    final long dim074Linitializers[] = { 1, 1, 1, 5, 17, 25, 21, 83, 95, 0 };
    final long dim075Linitializers[] = { 1, 3, 5, 13, 31, 25, 61, 157, 407, 0 };
    final long dim076Linitializers[] = { 1, 1, 7, 9, 25, 33, 41, 35, 17, 0 };
    final long dim077Linitializers[] = { 1, 3, 7, 15, 13, 39, 61, 187, 461, 0 };
    final long dim078Linitializers[] = { 1, 3, 7, 13, 5, 57, 23, 177, 435, 0 };
    final long dim079Linitializers[] = { 1, 1, 3, 15, 11, 27, 115, 5, 337, 0 };
    final long dim080Linitializers[] = { 1, 3, 7, 3, 15, 63, 61, 171, 339, 0 };
    final long dim081Linitializers[] = { 1, 3, 3, 13, 15, 61, 59, 47, 1, 0 };
    final long dim082Linitializers[] = { 1, 1, 5, 15, 13, 5, 39, 83, 329, 0 };
    final long dim083Linitializers[] = { 1, 1, 5, 5, 5, 27, 25, 39, 301, 0 };
    final long dim084Linitializers[] = { 1, 1, 5, 11, 31, 41, 35, 233, 27, 0 };
    final long dim085Linitializers[] = { 1, 3, 5, 15, 7, 37, 119, 171, 419, 0 };
    final long dim086Linitializers[] = { 1, 3, 5, 5, 3, 29, 21, 189, 417, 0 };
    final long dim087Linitializers[] = { 1, 1, 1, 1, 21, 41, 117, 119, 351, 0 };
    final long dim088Linitializers[] = { 1, 1, 3, 1, 7, 27, 87, 19, 213, 0 };
    final long dim089Linitializers[] = { 1, 1, 1, 1, 17, 7, 97, 217, 477, 0 };
    final long dim090Linitializers[] = { 1, 1, 7, 1, 29, 61, 103, 231, 269, 0 };
    final long dim091Linitializers[] = { 1, 1, 7, 13, 9, 27, 107, 207, 311, 0 };
    final long dim092Linitializers[] = { 1, 1, 7, 5, 25, 21, 107, 179, 423, 0 };
    final long dim093Linitializers[] = { 1, 3, 5, 11, 7, 1, 17, 245, 281, 0 };
    final long dim094Linitializers[] = { 1, 3, 5, 9, 1, 5, 53, 59, 125, 0 };
    final long dim095Linitializers[] = { 1, 1, 7, 1, 31, 57, 71, 245, 125, 0 };
    final long dim096Linitializers[] = { 1, 1, 7, 5, 5, 57, 53, 253, 441, 0 };
    final long dim097Linitializers[] = { 1, 3, 1, 13, 19, 35, 119, 235, 381, 0 };
    final long dim098Linitializers[] = { 1, 3, 1, 7, 19, 59, 115, 33, 361, 0 };
    final long dim099Linitializers[] = { 1, 1, 3, 5, 13, 1, 49, 143, 501, 0 };
    final long dim100Linitializers[] = { 1, 1, 3, 5, 1, 63, 101, 85, 189, 0 };
    final long dim101Linitializers[] = { 1, 1, 5, 11, 27, 63, 13, 131, 5, 0 };
    final long dim102Linitializers[] = { 1, 1, 5, 7, 15, 45, 75, 59, 455, 585, 0 };
    final long dim103Linitializers[] = { 1, 3, 1, 3, 7, 7, 111, 23, 119, 959, 0 };
    final long dim104Linitializers[] = { 1, 3, 3, 9, 11, 41, 109, 163, 161, 879, 0 };
    final long dim105Linitializers[] = { 1, 3, 5, 1, 21, 41, 121, 183, 315, 219, 0 };
    final long dim106Linitializers[] = { 1, 1, 3, 9, 15, 3, 9, 223, 441, 929, 0 };
    final long dim107Linitializers[] = { 1, 1, 7, 9, 3, 5, 93, 57, 253, 457, 0 };
    final long dim108Linitializers[] = { 1, 1, 7, 13, 15, 29, 83, 21, 35, 45, 0 };
    final long dim109Linitializers[] = { 1, 1, 3, 7, 13, 61, 119, 219, 85, 505, 0 };
    final long dim110Linitializers[] = { 1, 1, 3, 3, 17, 13, 35, 197, 291, 109, 0 };
    final long dim111Linitializers[] = { 1, 1, 3, 3, 5, 1, 113, 103, 217, 253, 0 };
    final long dim112Linitializers[] = { 1, 1, 7, 1, 15, 39, 63, 223, 17, 9, 0 };
    final long dim113Linitializers[] = { 1, 3, 7, 1, 17, 29, 67, 103, 495, 383, 0 };
    final long dim114Linitializers[] = { 1, 3, 3, 15, 31, 59, 75, 165, 51, 913, 0 };
    final long dim115Linitializers[] = { 1, 3, 7, 9, 5, 27, 79, 219, 233, 37, 0 };
    final long dim116Linitializers[] = { 1, 3, 5, 15, 1, 11, 15, 211, 417, 811, 0 };
    final long dim117Linitializers[] = { 1, 3, 5, 3, 29, 27, 39, 137, 407, 231, 0 };
    final long dim118Linitializers[] = { 1, 1, 3, 5, 29, 43, 125, 135, 109, 67, 0 };
    final long dim119Linitializers[] = { 1, 1, 1, 5, 11, 39, 107, 159, 323, 381, 0 };
    final long dim120Linitializers[] = { 1, 1, 1, 1, 9, 11, 33, 55, 169, 253, 0 };
    final long dim121Linitializers[] = { 1, 3, 5, 5, 11, 53, 63, 101, 251, 897, 0 };
    final long dim122Linitializers[] = { 1, 3, 7, 1, 25, 15, 83, 119, 53, 157, 0 };
    final long dim123Linitializers[] = { 1, 3, 5, 13, 5, 5, 3, 195, 111, 451, 0 };
    final long dim124Linitializers[] = { 1, 3, 1, 15, 11, 1, 19, 11, 307, 777, 0 };
    final long dim125Linitializers[] = { 1, 3, 7, 11, 5, 5, 17, 231, 345, 981, 0 };
    final long dim126Linitializers[] = { 1, 1, 3, 3, 1, 33, 83, 201, 57, 475, 0 };
    final long dim127Linitializers[] = { 1, 3, 7, 7, 17, 13, 35, 175, 499, 809, 0 };
    final long dim128Linitializers[] = { 1, 1, 5, 3, 3, 17, 103, 119, 499, 865, 0 };
    final long dim129Linitializers[] = { 1, 1, 1, 11, 27, 25, 37, 121, 401, 11, 0 };
    final long dim130Linitializers[] = { 1, 1, 1, 11, 9, 25, 25, 241, 403, 3, 0 };
    final long dim131Linitializers[] = { 1, 1, 1, 1, 11, 1, 39, 163, 231, 573, 0 };
    final long dim132Linitializers[] = { 1, 1, 1, 13, 13, 21, 75, 185, 99, 545, 0 };
    final long dim133Linitializers[] = { 1, 1, 1, 15, 3, 63, 69, 11, 173, 315, 0 };
    final long dim134Linitializers[] = { 1, 3, 5, 15, 11, 3, 95, 49, 123, 765, 0 };
    final long dim135Linitializers[] = { 1, 1, 1, 15, 3, 63, 77, 31, 425, 711, 0 };
    final long dim136Linitializers[] = { 1, 1, 7, 15, 1, 37, 119, 145, 489, 583, 0 };
    final long dim137Linitializers[] = { 1, 3, 5, 15, 3, 49, 117, 211, 165, 323, 0 };
    final long dim138Linitializers[] = { 1, 3, 7, 1, 27, 63, 77, 201, 225, 803, 0 };
    final long dim139Linitializers[] = { 1, 1, 1, 11, 23, 35, 67, 21, 469, 357, 0 };
    final long dim140Linitializers[] = { 1, 1, 7, 7, 9, 7, 25, 237, 237, 571, 0 };
    final long dim141Linitializers[] = { 1, 1, 3, 15, 29, 5, 107, 109, 241, 47, 0 };
    final long dim142Linitializers[] = { 1, 3, 5, 11, 27, 63, 29, 13, 203, 675, 0 };
    final long dim143Linitializers[] = { 1, 1, 3, 9, 9, 11, 103, 179, 449, 263, 0 };
    final long dim144Linitializers[] = { 1, 3, 5, 11, 29, 63, 53, 151, 259, 223, 0 };
    final long dim145Linitializers[] = { 1, 1, 3, 7, 9, 25, 5, 197, 237, 163, 0 };
    final long dim146Linitializers[] = { 1, 3, 7, 13, 5, 57, 67, 193, 147, 241, 0 };
    final long dim147Linitializers[] = { 1, 1, 5, 15, 15, 33, 17, 67, 161, 341, 0 };
    final long dim148Linitializers[] = { 1, 1, 3, 13, 17, 43, 21, 197, 441, 985, 0 };
    final long dim149Linitializers[] = { 1, 3, 1, 5, 15, 33, 33, 193, 305, 829, 0 };
    final long dim150Linitializers[] = { 1, 1, 1, 13, 19, 27, 71, 187, 477, 239, 0 };
    final long dim151Linitializers[] = { 1, 1, 1, 9, 9, 17, 41, 177, 229, 983, 0 };
    final long dim152Linitializers[] = { 1, 3, 5, 9, 15, 45, 97, 205, 43, 767, 0 };
    final long dim153Linitializers[] = { 1, 1, 1, 9, 31, 31, 77, 159, 395, 809, 0 };
    final long dim154Linitializers[] = { 1, 3, 3, 3, 29, 19, 73, 123, 165, 307, 0 };
    final long dim155Linitializers[] = { 1, 3, 1, 7, 5, 11, 77, 227, 355, 403, 0 };
    final long dim156Linitializers[] = { 1, 3, 5, 5, 25, 31, 1, 215, 451, 195, 0 };
    final long dim157Linitializers[] = { 1, 3, 7, 15, 29, 37, 101, 241, 17, 633, 0 };
    final long dim158Linitializers[] = { 1, 1, 5, 1, 11, 3, 107, 137, 489, 5, 0 };
    final long dim159Linitializers[] = { 1, 1, 1, 7, 19, 19, 75, 85, 471, 355, 0 };
    final long dim160Linitializers[] = { 1, 1, 3, 3, 9, 13, 113, 167, 13, 27, 0 };
    final long dim161Linitializers[] = { 1, 3, 5, 11, 21, 3, 89, 205, 377, 307, 0 };
    final long dim162Linitializers[] = { 1, 1, 1, 9, 31, 61, 65, 9, 391, 141, 867, 0 };
    final long dim163Linitializers[] = { 1, 1, 1, 9, 19, 19, 61, 227, 241, 55, 161, 0 };
    final long dim164Linitializers[] = { 1, 1, 1, 11, 1, 19, 7, 233, 463, 171, 1941, 0 };
    final long dim165Linitializers[] = { 1, 1, 5, 7, 25, 13, 103, 75, 19, 1021, 1063, 0 };
    final long dim166Linitializers[] = { 1, 1, 1, 15, 17, 17, 79, 63, 391, 403, 1221, 0 };
    final long dim167Linitializers[] = { 1, 3, 3, 11, 29, 25, 29, 107, 335, 475, 963, 0 };
    final long dim168Linitializers[] = { 1, 3, 5, 1, 31, 33, 49, 43, 155, 9, 1285, 0 };
    final long dim169Linitializers[] = { 1, 1, 5, 5, 15, 47, 39, 161, 357, 863, 1039, 0 };
    final long dim170Linitializers[] = { 1, 3, 7, 15, 1, 39, 47, 109, 427, 393, 1103, 0 };
    final long dim171Linitializers[] = { 1, 1, 1, 9, 9, 29, 121, 233, 157, 99, 701, 0 };
    final long dim172Linitializers[] = { 1, 1, 1, 7, 1, 29, 75, 121, 439, 109, 993, 0 };
    final long dim173Linitializers[] = { 1, 1, 1, 9, 5, 1, 39, 59, 89, 157, 1865, 0 };
    final long dim174Linitializers[] = { 1, 1, 5, 1, 3, 37, 89, 93, 143, 533, 175, 0 };
    final long dim175Linitializers[] = { 1, 1, 3, 5, 7, 33, 35, 173, 159, 135, 241, 0 };
    final long dim176Linitializers[] = { 1, 1, 1, 15, 17, 37, 79, 131, 43, 891, 229, 0 };
    final long dim177Linitializers[] = { 1, 1, 1, 1, 1, 35, 121, 177, 397, 1017, 583, 0 };
    final long dim178Linitializers[] = { 1, 1, 3, 15, 31, 21, 43, 67, 467, 923, 1473, 0 };
    final long dim179Linitializers[] = { 1, 1, 1, 7, 1, 33, 77, 111, 125, 771, 1975, 0 };
    final long dim180Linitializers[] = { 1, 3, 7, 13, 1, 51, 113, 139, 245, 573, 503, 0 };
    final long dim181Linitializers[] = { 1, 3, 1, 9, 21, 49, 15, 157, 49, 483, 291, 0 };
    final long dim182Linitializers[] = { 1, 1, 1, 1, 29, 35, 17, 65, 403, 485, 1603, 0 };
    final long dim183Linitializers[] = { 1, 1, 1, 7, 19, 1, 37, 129, 203, 321, 1809, 0 };
    final long dim184Linitializers[] = { 1, 3, 7, 15, 15, 9, 5, 77, 29, 485, 581, 0 };
    final long dim185Linitializers[] = { 1, 1, 3, 5, 15, 49, 97, 105, 309, 875, 1581, 0 };
    final long dim186Linitializers[] = { 1, 3, 5, 1, 5, 19, 63, 35, 165, 399, 1489, 0 };
    final long dim187Linitializers[] = { 1, 3, 5, 3, 23, 5, 79, 137, 115, 599, 1127, 0 };
    final long dim188Linitializers[] = { 1, 1, 7, 5, 3, 61, 27, 177, 257, 91, 841, 0 };
    final long dim189Linitializers[] = { 1, 1, 3, 5, 9, 31, 91, 209, 409, 661, 159, 0 };
    final long dim190Linitializers[] = { 1, 3, 1, 15, 23, 39, 23, 195, 245, 203, 947, 0 };
    final long dim191Linitializers[] = { 1, 1, 3, 1, 15, 59, 67, 95, 155, 461, 147, 0 };
    final long dim192Linitializers[] = { 1, 3, 7, 5, 23, 25, 87, 11, 51, 449, 1631, 0 };
    final long dim193Linitializers[] = { 1, 1, 1, 1, 17, 57, 7, 197, 409, 609, 135, 0 };
    final long dim194Linitializers[] = { 1, 1, 1, 9, 1, 61, 115, 113, 495, 895, 1595, 0 };
    final long dim195Linitializers[] = { 1, 3, 7, 15, 9, 47, 121, 211, 379, 985, 1755, 0 };
    final long dim196Linitializers[] = { 1, 3, 1, 3, 7, 57, 27, 231, 339, 325, 1023, 0 };
    final long dim197Linitializers[] = { 1, 1, 1, 1, 19, 63, 63, 239, 31, 643, 373, 0 };
    final long dim198Linitializers[] = { 1, 3, 1, 11, 19, 9, 7, 171, 21, 691, 215, 0 };
    final long dim199Linitializers[] = { 1, 1, 5, 13, 11, 57, 39, 211, 241, 893, 555, 0 };
    final long dim200Linitializers[] = { 1, 1, 7, 5, 29, 21, 45, 59, 509, 223, 491, 0 };
    final long dim201Linitializers[] = { 1, 1, 7, 9, 15, 61, 97, 75, 127, 779, 839, 0 };
    final long dim202Linitializers[] = { 1, 1, 7, 15, 17, 33, 75, 237, 191, 925, 681, 0 };
    final long dim203Linitializers[] = { 1, 3, 5, 7, 27, 57, 123, 111, 101, 371, 1129, 0 };
    final long dim204Linitializers[] = { 1, 3, 5, 5, 29, 45, 59, 127, 229, 967, 2027, 0 };
    final long dim205Linitializers[] = { 1, 1, 1, 1, 17, 7, 23, 199, 241, 455, 135, 0 };
    final long dim206Linitializers[] = { 1, 1, 7, 15, 27, 29, 105, 171, 337, 503, 1817, 0 };
    final long dim207Linitializers[] = { 1, 1, 3, 7, 21, 35, 61, 71, 405, 647, 2045, 0 };
    final long dim208Linitializers[] = { 1, 1, 1, 1, 1, 15, 65, 167, 501, 79, 737, 0 };
    final long dim209Linitializers[] = { 1, 1, 5, 1, 3, 49, 27, 189, 341, 615, 1287, 0 };
    final long dim210Linitializers[] = { 1, 1, 1, 9, 1, 7, 31, 159, 503, 327, 1613, 0 };
    final long dim211Linitializers[] = { 1, 3, 3, 3, 3, 23, 99, 115, 323, 997, 987, 0 };
    final long dim212Linitializers[] = { 1, 1, 1, 9, 19, 33, 93, 247, 509, 453, 891, 0 };
    final long dim213Linitializers[] = { 1, 1, 3, 1, 13, 19, 35, 153, 161, 633, 445, 0 };
    final long dim214Linitializers[] = { 1, 3, 5, 15, 31, 5, 87, 197, 183, 783, 1823, 0 };
    final long dim215Linitializers[] = { 1, 1, 7, 5, 19, 63, 69, 221, 129, 231, 1195, 0 };
    final long dim216Linitializers[] = { 1, 1, 5, 5, 13, 23, 19, 231, 245, 917, 379, 0 };
    final long dim217Linitializers[] = { 1, 3, 1, 15, 19, 43, 27, 223, 171, 413, 125, 0 };
    final long dim218Linitializers[] = { 1, 1, 1, 9, 1, 59, 21, 15, 509, 207, 589, 0 };
    final long dim219Linitializers[] = { 1, 3, 5, 3, 19, 31, 113, 19, 23, 733, 499, 0 };
    final long dim220Linitializers[] = { 1, 1, 7, 1, 19, 51, 101, 165, 47, 925, 1093, 0 };
    final long dim221Linitializers[] = { 1, 3, 3, 9, 15, 21, 43, 243, 237, 461, 1361, 0 };
    final long dim222Linitializers[] = { 1, 1, 1, 9, 17, 15, 75, 75, 113, 715, 1419, 0 };
    final long dim223Linitializers[] = { 1, 1, 7, 13, 17, 1, 99, 15, 347, 721, 1405, 0 };
    final long dim224Linitializers[] = { 1, 1, 7, 15, 7, 27, 23, 183, 39, 59, 571, 0 };
    final long dim225Linitializers[] = { 1, 3, 5, 9, 7, 43, 35, 165, 463, 567, 859, 0 };
    final long dim226Linitializers[] = { 1, 3, 3, 11, 15, 19, 17, 129, 311, 343, 15, 0 };
    final long dim227Linitializers[] = { 1, 1, 1, 15, 31, 59, 63, 39, 347, 359, 105, 0 };
    final long dim228Linitializers[] = { 1, 1, 1, 15, 5, 43, 87, 241, 109, 61, 685, 0 };
    final long dim229Linitializers[] = { 1, 1, 7, 7, 9, 39, 121, 127, 369, 579, 853, 0 };
    final long dim230Linitializers[] = { 1, 1, 1, 1, 17, 15, 15, 95, 325, 627, 299, 0 };
    final long dim231Linitializers[] = { 1, 1, 3, 13, 31, 53, 85, 111, 289, 811, 1635, 0 };
    final long dim232Linitializers[] = { 1, 3, 7, 1, 19, 29, 75, 185, 153, 573, 653, 0 };
    final long dim233Linitializers[] = { 1, 3, 7, 1, 29, 31, 55, 91, 249, 247, 1015, 0 };
    final long dim234Linitializers[] = { 1, 3, 5, 7, 1, 49, 113, 139, 257, 127, 307, 0 };
    final long dim235Linitializers[] = { 1, 3, 5, 9, 15, 15, 123, 105, 105, 225, 1893, 0 };
    final long dim236Linitializers[] = { 1, 3, 3, 1, 15, 5, 105, 249, 73, 709, 1557, 0 };
    final long dim237Linitializers[] = { 1, 1, 1, 9, 17, 31, 113, 73, 65, 701, 1439, 0 };
    final long dim238Linitializers[] = { 1, 3, 5, 15, 13, 21, 117, 131, 243, 859, 323, 0 };
    final long dim239Linitializers[] = { 1, 1, 1, 9, 19, 15, 69, 149, 89, 681, 515, 0 };
    final long dim240Linitializers[] = { 1, 1, 1, 5, 29, 13, 21, 97, 301, 27, 967, 0 };
    final long dim241Linitializers[] = { 1, 1, 3, 3, 15, 45, 107, 227, 495, 769, 1935, 0 };
    final long dim242Linitializers[] = { 1, 1, 1, 11, 5, 27, 41, 173, 261, 703, 1349, 0 };
    final long dim243Linitializers[] = { 1, 3, 3, 3, 11, 35, 97, 43, 501, 563, 1331, 0 };
    final long dim244Linitializers[] = { 1, 1, 1, 7, 1, 17, 87, 17, 429, 245, 1941, 0 };
    final long dim245Linitializers[] = { 1, 1, 7, 15, 29, 13, 1, 175, 425, 233, 797, 0 };
    final long dim246Linitializers[] = { 1, 1, 3, 11, 21, 57, 49, 49, 163, 685, 701, 0 };
    final long dim247Linitializers[] = { 1, 3, 3, 7, 11, 45, 107, 111, 379, 703, 1403, 0 };
    final long dim248Linitializers[] = { 1, 1, 7, 3, 21, 7, 117, 49, 469, 37, 775, 0 };
    final long dim249Linitializers[] = { 1, 1, 5, 15, 31, 63, 101, 77, 507, 489, 1955, 0 };
    final long dim250Linitializers[] = { 1, 3, 3, 11, 19, 21, 101, 255, 203, 673, 665, 0 };
    final long dim251Linitializers[] = { 1, 3, 3, 15, 17, 47, 125, 187, 271, 899, 2003, 0 };
    final long dim252Linitializers[] = { 1, 1, 7, 7, 1, 35, 13, 235, 5, 337, 905, 0 };
    final long dim253Linitializers[] = { 1, 3, 1, 15, 1, 43, 1, 27, 37, 695, 1429, 0 };
    final long dim254Linitializers[] = { 1, 3, 1, 11, 21, 27, 93, 161, 299, 665, 495, 0 };
    final long dim255Linitializers[] = { 1, 3, 3, 15, 3, 1, 81, 111, 105, 547, 897, 0 };
    final long dim256Linitializers[] = { 1, 3, 5, 1, 3, 53, 97, 253, 401, 827, 1467, 0 };
    final long dim257Linitializers[] = { 1, 1, 1, 5, 19, 59, 105, 125, 271, 351, 719, 0 };
    final long dim258Linitializers[] = { 1, 3, 5, 13, 7, 11, 91, 41, 441, 759, 1827, 0 };
    final long dim259Linitializers[] = { 1, 3, 7, 11, 29, 61, 61, 23, 307, 863, 363, 0 };
    final long dim260Linitializers[] = { 1, 1, 7, 1, 15, 35, 29, 133, 415, 473, 1737, 0 };
    final long dim261Linitializers[] = { 1, 1, 1, 13, 7, 33, 35, 225, 117, 681, 1545, 0 };
    final long dim262Linitializers[] = { 1, 1, 1, 3, 5, 41, 83, 247, 13, 373, 1091, 0 };
    final long dim263Linitializers[] = { 1, 3, 1, 13, 25, 61, 71, 217, 233, 313, 547, 0 };
    final long dim264Linitializers[] = { 1, 3, 1, 7, 3, 29, 3, 49, 93, 465, 15, 0 };
    final long dim265Linitializers[] = { 1, 1, 1, 9, 17, 61, 99, 163, 129, 485, 1087, 0 };
    final long dim266Linitializers[] = { 1, 1, 1, 9, 9, 33, 31, 163, 145, 649, 253, 0 };
    final long dim267Linitializers[] = { 1, 1, 1, 1, 17, 63, 43, 235, 287, 111, 567, 0 };
    final long dim268Linitializers[] = { 1, 3, 5, 13, 29, 7, 11, 69, 153, 127, 449, 0 };
    final long dim269Linitializers[] = { 1, 1, 5, 9, 11, 21, 15, 189, 431, 493, 1219, 0 };
    final long dim270Linitializers[] = { 1, 1, 1, 15, 19, 5, 47, 91, 399, 293, 1743, 0 };
    final long dim271Linitializers[] = { 1, 3, 3, 11, 29, 53, 53, 225, 409, 303, 333, 0 };
    final long dim272Linitializers[] = { 1, 1, 1, 15, 31, 31, 21, 81, 147, 287, 1753, 0 };
    final long dim273Linitializers[] = { 1, 3, 5, 5, 5, 63, 35, 125, 41, 687, 1793, 0 };
    final long dim274Linitializers[] = { 1, 1, 1, 9, 19, 59, 107, 219, 455, 971, 297, 0 };
    final long dim275Linitializers[] = { 1, 1, 3, 5, 3, 51, 121, 31, 245, 105, 1311, 0 };
    final long dim276Linitializers[] = { 1, 3, 1, 5, 5, 57, 75, 107, 161, 431, 1693, 0 };
    final long dim277Linitializers[] = { 1, 3, 1, 3, 19, 53, 27, 31, 191, 565, 1015, 0 };
    final long dim278Linitializers[] = { 1, 3, 5, 13, 9, 41, 35, 249, 287, 49, 123, 0 };
    final long dim279Linitializers[] = { 1, 1, 5, 7, 27, 17, 21, 3, 151, 885, 1165, 0 };
    final long dim280Linitializers[] = { 1, 1, 7, 1, 15, 17, 65, 139, 427, 339, 1171, 0 };
    final long dim281Linitializers[] = { 1, 1, 1, 5, 23, 5, 9, 89, 321, 907, 391, 0 };
    final long dim282Linitializers[] = { 1, 1, 7, 9, 15, 1, 77, 71, 87, 701, 917, 0 };
    final long dim283Linitializers[] = { 1, 1, 7, 1, 17, 37, 115, 127, 469, 779, 1543, 0 };
    final long dim284Linitializers[] = { 1, 3, 7, 3, 5, 61, 15, 37, 301, 951, 1437, 0 };
    final long dim285Linitializers[] = { 1, 1, 1, 13, 9, 51, 127, 145, 229, 55, 1567, 0 };
    final long dim286Linitializers[] = { 1, 3, 7, 15, 19, 47, 53, 153, 295, 47, 1337, 0 };
    final long dim287Linitializers[] = { 1, 3, 3, 5, 11, 31, 29, 133, 327, 287, 507, 0 };
    final long dim288Linitializers[] = { 1, 1, 7, 7, 25, 31, 37, 199, 25, 927, 1317, 0 };
    final long dim289Linitializers[] = { 1, 1, 7, 9, 3, 39, 127, 167, 345, 467, 759, 0 };
    final long dim290Linitializers[] = { 1, 1, 1, 1, 31, 21, 15, 101, 293, 787, 1025, 0 };
    final long dim291Linitializers[] = { 1, 1, 5, 3, 11, 41, 105, 109, 149, 837, 1813, 0 };
    final long dim292Linitializers[] = { 1, 1, 3, 5, 29, 13, 19, 97, 309, 901, 753, 0 };
    final long dim293Linitializers[] = { 1, 1, 7, 1, 19, 17, 31, 39, 173, 361, 1177, 0 };
    final long dim294Linitializers[] = { 1, 3, 3, 3, 3, 41, 81, 7, 341, 491, 43, 0 };
    final long dim295Linitializers[] = { 1, 1, 7, 7, 31, 35, 29, 77, 11, 335, 1275, 0 };
    final long dim296Linitializers[] = { 1, 3, 3, 15, 17, 45, 19, 63, 151, 849, 129, 0 };
    final long dim297Linitializers[] = { 1, 1, 7, 5, 7, 13, 47, 73, 79, 31, 499, 0 };
    final long dim298Linitializers[] = { 1, 3, 1, 11, 1, 41, 59, 151, 247, 115, 1295, 0 };
    final long dim299Linitializers[] = { 1, 1, 1, 9, 31, 37, 73, 23, 295, 483, 179, 0 };
    final long dim300Linitializers[] = { 1, 3, 1, 15, 13, 63, 81, 27, 169, 825, 2037, 0 };
    final long dim301Linitializers[] = { 1, 3, 5, 15, 7, 11, 73, 1, 451, 101, 2039, 0 };
    final long dim302Linitializers[] = { 1, 3, 5, 3, 13, 53, 31, 137, 173, 319, 1521, 0 };
    final long dim303Linitializers[] = { 1, 3, 1, 3, 29, 1, 73, 227, 377, 337, 1189, 0 };
    final long dim304Linitializers[] = { 1, 3, 3, 13, 27, 9, 31, 101, 229, 165, 1983, 0 };
    final long dim305Linitializers[] = { 1, 3, 1, 13, 13, 19, 19, 111, 319, 421, 223, 0 };
    final long dim306Linitializers[] = { 1, 1, 7, 15, 25, 37, 61, 55, 359, 255, 1955, 0 };
    final long dim307Linitializers[] = { 1, 1, 5, 13, 17, 43, 49, 215, 383, 915, 51, 0 };
    final long dim308Linitializers[] = { 1, 1, 3, 1, 3, 7, 13, 119, 155, 585, 967, 0 };
    final long dim309Linitializers[] = { 1, 3, 1, 13, 1, 63, 125, 21, 103, 287, 457, 0 };
    final long dim310Linitializers[] = { 1, 1, 7, 1, 31, 17, 125, 137, 345, 379, 1925, 0 };
    final long dim311Linitializers[] = { 1, 1, 3, 5, 5, 25, 119, 153, 455, 271, 2023, 0 };
    final long dim312Linitializers[] = { 1, 1, 7, 9, 9, 37, 115, 47, 5, 255, 917, 0 };
    final long dim313Linitializers[] = { 1, 3, 5, 3, 31, 21, 75, 203, 489, 593, 1, 0 };
    final long dim314Linitializers[] = { 1, 3, 7, 15, 19, 63, 123, 153, 135, 977, 1875, 0 };
    final long dim315Linitializers[] = { 1, 1, 1, 1, 5, 59, 31, 25, 127, 209, 745, 0 };
    final long dim316Linitializers[] = { 1, 1, 1, 1, 19, 45, 67, 159, 301, 199, 535, 0 };
    final long dim317Linitializers[] = { 1, 1, 7, 1, 31, 17, 19, 225, 369, 125, 421, 0 };
    final long dim318Linitializers[] = { 1, 3, 3, 11, 7, 59, 115, 197, 459, 469, 1055, 0 };
    final long dim319Linitializers[] = { 1, 3, 1, 3, 27, 45, 35, 131, 349, 101, 411, 0 };
    final long dim320Linitializers[] = { 1, 3, 7, 11, 9, 3, 67, 145, 299, 253, 1339, 0 };
    final long dim321Linitializers[] = { 1, 3, 3, 11, 9, 37, 123, 229, 273, 269, 515, 0 };
    final long dim322Linitializers[] = { 1, 3, 7, 15, 11, 25, 75, 5, 367, 217, 951, 0 };
    final long dim323Linitializers[] = { 1, 1, 3, 7, 9, 23, 63, 237, 385, 159, 1273, 0 };
    final long dim324Linitializers[] = { 1, 1, 5, 11, 23, 5, 55, 193, 109, 865, 663, 0 };
    final long dim325Linitializers[] = { 1, 1, 7, 15, 1, 57, 17, 141, 51, 217, 1259, 0 };
    final long dim326Linitializers[] = { 1, 1, 3, 3, 15, 7, 89, 233, 71, 329, 203, 0 };
    final long dim327Linitializers[] = { 1, 3, 7, 11, 11, 1, 19, 155, 89, 437, 573, 0 };
    final long dim328Linitializers[] = { 1, 3, 1, 9, 27, 61, 47, 109, 161, 913, 1681, 0 };
    final long dim329Linitializers[] = { 1, 1, 7, 15, 1, 33, 19, 15, 23, 913, 989, 0 };
    final long dim330Linitializers[] = { 1, 3, 1, 1, 25, 39, 119, 193, 13, 571, 157, 0 };
    final long dim331Linitializers[] = { 1, 1, 7, 13, 9, 55, 59, 147, 361, 935, 515, 0 };
    final long dim332Linitializers[] = { 1, 1, 1, 9, 7, 59, 67, 117, 71, 855, 1493, 0 };
    final long dim333Linitializers[] = { 1, 3, 1, 3, 13, 19, 57, 141, 305, 275, 1079, 0 };
    final long dim334Linitializers[] = { 1, 1, 1, 9, 17, 61, 33, 7, 43, 931, 781, 0 };
    final long dim335Linitializers[] = { 1, 1, 3, 1, 11, 17, 21, 97, 295, 277, 1721, 0 };
    final long dim336Linitializers[] = { 1, 3, 1, 13, 15, 43, 11, 241, 147, 391, 1641, 0 };
    final long dim337Linitializers[] = { 1, 1, 1, 1, 1, 19, 37, 21, 255, 263, 1571, 0 };
    final long dim338Linitializers[] = { 1, 1, 3, 3, 23, 59, 89, 17, 475, 303, 757, 543, 0 };
    final long dim339Linitializers[] = { 1, 3, 3, 9, 11, 55, 35, 159, 139, 203, 1531, 1825, 0 };
    final long dim340Linitializers[] = { 1, 1, 5, 3, 17, 53, 51, 241, 269, 949, 1373, 325, 0 };
    final long dim341Linitializers[] = { 1, 3, 7, 7, 5, 29, 91, 149, 239, 193, 1951, 2675, 0 };
    final long dim342Linitializers[] = { 1, 3, 5, 1, 27, 33, 69, 11, 51, 371, 833, 2685, 0 };
    final long dim343Linitializers[] = { 1, 1, 1, 15, 1, 17, 35, 57, 171, 1007, 449, 367, 0 };
    final long dim344Linitializers[] = { 1, 1, 1, 7, 25, 61, 73, 219, 379, 53, 589, 4065, 0 };
    final long dim345Linitializers[] = { 1, 3, 5, 13, 21, 29, 45, 19, 163, 169, 147, 597, 0 };
    final long dim346Linitializers[] = { 1, 1, 5, 11, 21, 27, 7, 17, 237, 591, 255, 1235, 0 };
    final long dim347Linitializers[] = { 1, 1, 7, 7, 17, 41, 69, 237, 397, 173, 1229, 2341, 0 };
    final long dim348Linitializers[] = { 1, 1, 3, 1, 1, 33, 125, 47, 11, 783, 1323, 2469, 0 };
    final long dim349Linitializers[] = { 1, 3, 1, 11, 3, 39, 35, 133, 153, 55, 1171, 3165, 0 };
    final long dim350Linitializers[] = { 1, 1, 5, 11, 27, 23, 103, 245, 375, 753, 477, 2165, 0 };
    final long dim351Linitializers[] = { 1, 3, 1, 15, 15, 49, 127, 223, 387, 771, 1719, 1465, 0 };
    final long dim352Linitializers[] = { 1, 1, 1, 9, 11, 9, 17, 185, 239, 899, 1273, 3961, 0 };
    final long dim353Linitializers[] = { 1, 1, 3, 13, 11, 51, 73, 81, 389, 647, 1767, 1215, 0 };
    final long dim354Linitializers[] = { 1, 3, 5, 15, 19, 9, 69, 35, 349, 977, 1603, 1435, 0 };
    final long dim355Linitializers[] = { 1, 1, 1, 1, 19, 59, 123, 37, 41, 961, 181, 1275, 0 };
    final long dim356Linitializers[] = { 1, 1, 1, 1, 31, 29, 37, 71, 205, 947, 115, 3017, 0 };
    final long dim357Linitializers[] = { 1, 1, 7, 15, 5, 37, 101, 169, 221, 245, 687, 195, 0 };
    final long dim358Linitializers[] = { 1, 1, 1, 1, 19, 9, 125, 157, 119, 283, 1721, 743, 0 };
    final long dim359Linitializers[] = { 1, 1, 7, 3, 1, 7, 61, 71, 119, 257, 1227, 2893, 0 };
    final long dim360Linitializers[] = { 1, 3, 3, 3, 25, 41, 25, 225, 31, 57, 925, 2139, 0 };
 	
 	
 	 final long[][] Linitializers = {
             dim02SLinitializers,
             dim03SLinitializers,
             dim04SLinitializers,
             dim05SLinitializers,
             dim06SLinitializers,
             dim07SLinitializers,
             dim08SLinitializers,
             dim09SLinitializers,
             dim10SLinitializers,
             dim11SLinitializers,
             dim12SLinitializers,
             dim13SLinitializers,
             dim14SLinitializers,
             dim15SLinitializers,
             dim16SLinitializers,
             dim17SLinitializers,
             dim18SLinitializers,
             dim19SLinitializers,
             dim20SLinitializers,
             dim21SLinitializers,
             dim22SLinitializers,
             dim23SLinitializers,
             dim24SLinitializers,
             dim25SLinitializers,
             dim26SLinitializers,
             dim27SLinitializers,
             dim28SLinitializers,
             dim29SLinitializers,
             dim30SLinitializers,
             dim31SLinitializers,
             dim32SLinitializers,
             dim33SLinitializers,
             dim34SLinitializers,
             dim35SLinitializers,
             dim36SLinitializers,
             dim37SLinitializers,
             dim38SLinitializers,
             dim39SLinitializers,
             dim40SLinitializers,
             dim041Linitializers,
             dim042Linitializers,
             dim043Linitializers,
             dim044Linitializers,
             dim045Linitializers,
             dim046Linitializers,
             dim047Linitializers,
             dim048Linitializers,
             dim049Linitializers,
             dim050Linitializers,
             dim051Linitializers,
             dim052Linitializers,
             dim053Linitializers,
             dim054Linitializers,
             dim055Linitializers,
             dim056Linitializers,
             dim057Linitializers,
             dim058Linitializers,
             dim059Linitializers,
             dim060Linitializers,
             dim061Linitializers,
             dim062Linitializers,
             dim063Linitializers,
             dim064Linitializers,
             dim065Linitializers,
             dim066Linitializers,
             dim067Linitializers,
             dim068Linitializers,
             dim069Linitializers,
             dim070Linitializers,
             dim071Linitializers,
             dim072Linitializers,
             dim073Linitializers,
             dim074Linitializers,
             dim075Linitializers,
             dim076Linitializers,
             dim077Linitializers,
             dim078Linitializers,
             dim079Linitializers,
             dim080Linitializers,
             dim081Linitializers,
             dim082Linitializers,
             dim083Linitializers,
             dim084Linitializers,
             dim085Linitializers,
             dim086Linitializers,
             dim087Linitializers,
             dim088Linitializers,
             dim089Linitializers,
             dim090Linitializers,
             dim091Linitializers,
             dim092Linitializers,
             dim093Linitializers,
             dim094Linitializers,
             dim095Linitializers,
             dim096Linitializers,
             dim097Linitializers,
             dim098Linitializers,
             dim099Linitializers,
             dim100Linitializers,
             dim101Linitializers,
             dim102Linitializers,
             dim103Linitializers,
             dim104Linitializers,
             dim105Linitializers,
             dim106Linitializers,
             dim107Linitializers,
             dim108Linitializers,
             dim109Linitializers,
             dim110Linitializers,
             dim111Linitializers,
             dim112Linitializers,
             dim113Linitializers,
             dim114Linitializers,
             dim115Linitializers,
             dim116Linitializers,
             dim117Linitializers,
             dim118Linitializers,
             dim119Linitializers,
             dim120Linitializers,
             dim121Linitializers,
             dim122Linitializers,
             dim123Linitializers,
             dim124Linitializers,
             dim125Linitializers,
             dim126Linitializers,
             dim127Linitializers,
             dim128Linitializers,
             dim129Linitializers,
             dim130Linitializers,
             dim131Linitializers,
             dim132Linitializers,
             dim133Linitializers,
             dim134Linitializers,
             dim135Linitializers,
             dim136Linitializers,
             dim137Linitializers,
             dim138Linitializers,
             dim139Linitializers,
             dim140Linitializers,
             dim141Linitializers,
             dim142Linitializers,
             dim143Linitializers,
             dim144Linitializers,
             dim145Linitializers,
             dim146Linitializers,
             dim147Linitializers,
             dim148Linitializers,
             dim149Linitializers,
             dim150Linitializers,
             dim151Linitializers,
             dim152Linitializers,
             dim153Linitializers,
             dim154Linitializers,
             dim155Linitializers,
             dim156Linitializers,
             dim157Linitializers,
             dim158Linitializers,
             dim159Linitializers,
             dim160Linitializers,
             dim161Linitializers,
             dim162Linitializers,
             dim163Linitializers,
             dim164Linitializers,
             dim165Linitializers,
             dim166Linitializers,
             dim167Linitializers,
             dim168Linitializers,
             dim169Linitializers,
             dim170Linitializers,
             dim171Linitializers,
             dim172Linitializers,
             dim173Linitializers,
             dim174Linitializers,
             dim175Linitializers,
             dim176Linitializers,
             dim177Linitializers,
             dim178Linitializers,
             dim179Linitializers,
             dim180Linitializers,
             dim181Linitializers,
             dim182Linitializers,
             dim183Linitializers,
             dim184Linitializers,
             dim185Linitializers,
             dim186Linitializers,
             dim187Linitializers,
             dim188Linitializers,
             dim189Linitializers,
             dim190Linitializers,
             dim191Linitializers,
             dim192Linitializers,
             dim193Linitializers,
             dim194Linitializers,
             dim195Linitializers,
             dim196Linitializers,
             dim197Linitializers,
             dim198Linitializers,
             dim199Linitializers,
             dim200Linitializers,
             dim201Linitializers,
             dim202Linitializers,
             dim203Linitializers,
             dim204Linitializers,
             dim205Linitializers,
             dim206Linitializers,
             dim207Linitializers,
             dim208Linitializers,
             dim209Linitializers,
             dim210Linitializers,
             dim211Linitializers,
             dim212Linitializers,
             dim213Linitializers,
             dim214Linitializers,
             dim215Linitializers,
             dim216Linitializers,
             dim217Linitializers,
             dim218Linitializers,
             dim219Linitializers,
             dim220Linitializers,
             dim221Linitializers,
             dim222Linitializers,
             dim223Linitializers,
             dim224Linitializers,
             dim225Linitializers,
             dim226Linitializers,
             dim227Linitializers,
             dim228Linitializers,
             dim229Linitializers,
             dim230Linitializers,
             dim231Linitializers,
             dim232Linitializers,
             dim233Linitializers,
             dim234Linitializers,
             dim235Linitializers,
             dim236Linitializers,
             dim237Linitializers,
             dim238Linitializers,
             dim239Linitializers,
             dim240Linitializers,
             dim241Linitializers,
             dim242Linitializers,
             dim243Linitializers,
             dim244Linitializers,
             dim245Linitializers,
             dim246Linitializers,
             dim247Linitializers,
             dim248Linitializers,
             dim249Linitializers,
             dim250Linitializers,
             dim251Linitializers,
             dim252Linitializers,
             dim253Linitializers,
             dim254Linitializers,
             dim255Linitializers,
             dim256Linitializers,
             dim257Linitializers,
             dim258Linitializers,
             dim259Linitializers,
             dim260Linitializers,
             dim261Linitializers,
             dim262Linitializers,
             dim263Linitializers,
             dim264Linitializers,
             dim265Linitializers,
             dim266Linitializers,
             dim267Linitializers,
             dim268Linitializers,
             dim269Linitializers,
             dim270Linitializers,
             dim271Linitializers,
             dim272Linitializers,
             dim273Linitializers,
             dim274Linitializers,
             dim275Linitializers,
             dim276Linitializers,
             dim277Linitializers,
             dim278Linitializers,
             dim279Linitializers,
             dim280Linitializers,
             dim281Linitializers,
             dim282Linitializers,
             dim283Linitializers,
             dim284Linitializers,
             dim285Linitializers,
             dim286Linitializers,
             dim287Linitializers,
             dim288Linitializers,
             dim289Linitializers,
             dim290Linitializers,
             dim291Linitializers,
             dim292Linitializers,
             dim293Linitializers,
             dim294Linitializers,
             dim295Linitializers,
             dim296Linitializers,
             dim297Linitializers,
             dim298Linitializers,
             dim299Linitializers,
             dim300Linitializers,
             dim301Linitializers,
             dim302Linitializers,
             dim303Linitializers,
             dim304Linitializers,
             dim305Linitializers,
             dim306Linitializers,
             dim307Linitializers,
             dim308Linitializers,
             dim309Linitializers,
             dim310Linitializers,
             dim311Linitializers,
             dim312Linitializers,
             dim313Linitializers,
             dim314Linitializers,
             dim315Linitializers,
             dim316Linitializers,
             dim317Linitializers,
             dim318Linitializers,
             dim319Linitializers,
             dim320Linitializers,
             dim321Linitializers,
             dim322Linitializers,
             dim323Linitializers,
             dim324Linitializers,
             dim325Linitializers,
             dim326Linitializers,
             dim327Linitializers,
             dim328Linitializers,
             dim329Linitializers,
             dim330Linitializers,
             dim331Linitializers,
             dim332Linitializers,
             dim333Linitializers,
             dim334Linitializers,
             dim335Linitializers,
             dim336Linitializers,
             dim337Linitializers,
             dim338Linitializers,
             dim339Linitializers,
             dim340Linitializers,
             dim341Linitializers,
             dim342Linitializers,
             dim343Linitializers,
             dim344Linitializers,
             dim345Linitializers,
             dim346Linitializers,
             dim347Linitializers,
             dim348Linitializers,
             dim349Linitializers,
             dim350Linitializers,
             dim351Linitializers,
             dim352Linitializers,
             dim353Linitializers,
             dim354Linitializers,
             dim355Linitializers,
             dim356Linitializers,
             dim357Linitializers,
             dim358Linitializers,
             dim359Linitializers,
             dim360Linitializers
         };
 	
 
	
	//
	// enums
	//
	public enum DirectionIntegers {Unit, Jaeckel, SobolLevitan, SobolLevitanLemieux };
	
	
	
	//
	// Constants
	//
	
	
	// Is this code necessary in Java?
    // const int SobolRsg::bits_ = 8*sizeof(unsigned long);
	
	// bits_ = 8*sizeof(long) = 8 * 8(bytes)
	private static final int bits_ = 64;		// C++:static const int bits_;
	
	// 1/(2^bits_) (written as (1/2)/(2^(bits_-1)) to avoid long overflow)
    private static final double normalizationFactor_= 0.5/(1 << (bits_-1)); // static const double normalizationFactor_;
	
	
	
	//
	// private fields
	//
	private int dimensionality_; 							// Size dimensionality_;
	private long sequenceCounter_; 							// mutable unsigned long sequenceCounter_;
	private boolean firstDraw_; 							// mutable bool firstDraw_;
	private Sample<List<Double>> sequence_; 			    // typedef Sample<std::vector<Real> > sample_type; / mutable sample_type sequence_;
	private List<Long> integerSequence_; 				// mutable std::vector<unsigned long> integerSequence_
	
	//FIXME: Is this the correct translation of std::vector<std::vector< long> > directionIntegers_?
	private List<List<Long>> directionIntegers_; 			// std::vector<std::vector< long> > directionIntegers_
	
	
	//pre dimensionality must be <= PPMT_MAX_DIM 
	
    public SobolRsg(int dimensionality) {
        this(dimensionality, 0);
    }

    public SobolRsg(int dimensionality, long seed) {
        this(dimensionality, seed, DirectionIntegers.Jaeckel);
    }
	
	
    public SobolRsg(int dimensionality, long seed, DirectionIntegers directionIntegers) {
    	
    	seed = 0;
    	directionIntegers = DirectionIntegers.Jaeckel;
    	   	
    	this.dimensionality_ = dimensionality; 	// dimensionality_(dimensionality)
    	this.sequenceCounter_= 0; 				// sequenceCounter_(0)
    	this.firstDraw_ = true;					// firstDraw_(true)
    	
    	// TODO: sequence_(std::vector<Real> (dimensionality), 1.0)
       	List<Double> dimDoubleArray = new DoubleArrayList(dimensionality_);
    	this.sequence_ = new Sample<List<Double>>(dimDoubleArray, 1.0);
    	
    	
        // TODO: integerSequence_(dimensionality, 0) --> where has the zero gone?
       	this.integerSequence_ = new LongArrayList(dimensionality_);
    	
        // TODO: directionIntegers_(dimensionality,std::vector<unsigned long>(bits_))
    	ArrayList<Long> bits = new ArrayList<Long>(bits_);
    	ArrayList<Long> dimLongArray = new ArrayList<Long>(dimensionality_);
    

    
        if (dimensionality>0){
        	throw new ArithmeticException("dimensionality must be greater than 0");
        }
        
        if (dimensionality <= PrimitivePolynomials.PPMT_MAX_DIM) {
        	throw new ArithmeticException("dimensionality " + dimensionality + 
        			" exceeds the number of availalbel primitive polynomials module two (" + PrimitivePolynomials.PPMT_MAX_DIM  +")");
        	
        }

        // initializes coefficient array of the k-th primitive polynomial
        // and degree of the k-th primitive polynomial
        
        // std::vector<unsigned int> degree(dimensionality_);
       ArrayList<Integer> degree = new ArrayList(dimensionality_);
        
        // std::vector<long> ppmt(dimensionality_);
        ArrayList<Long> ppmt = new ArrayList(dimensionality_);
        
        // degree 0 is not used
        ppmt.add(new Long(0));
        degree.add(new Integer(0));
        int k; 
        int index;
        int currentDegree;
        
        
        // FIXME: The PrimitivePolynomials[][] needs to be translated
        /*
        for (k=1,index=0,currentDegree=1; k<dimensionality_; k++,index++){
            ppmt.set(k,PrimitivePolynomials[currentDegree-1][index]);
            if (ppmt.indexOf(k)==-1) {
                ++currentDegree;
                index=0;
                ppmt.set(k, PrimitivePolynomials[currentDegree-1][index]);
                degree.set(k, currentDegree);
            }
        }
        */
        
        // initializes bits_ direction integers for each dimension
        // and store them into directionIntegers_[dimensionality_][bits_]
        //
        // In each dimension k with its associated primitive polynomial,
        // the first degree_[k] direction integers can be chosen freely
        // provided that only the l leftmost bits can be non-zero, and
        // that the l-th leftmost bit must be set

        // degenerate (no free direction integers) first dimension
        int j;
        for (j=0; j<bits_; j++) {
        	
        	// FIXME: Translate this line
            // directionIntegers_.get(0).get(j) = (1 << (bits_-j-1));
        }

        int maxTabulated;
        // dimensions from 2 (k=1) to maxTabulated (k=maxTabulated-1) included
        // are initialized from tabulated coefficients
        switch (directionIntegers) {
            case Unit:
                maxTabulated=dimensionality_;
                for (k=1; k<maxTabulated; k++) {
                    for (int l=1; l<=degree.get(k); l++) {
                    	// FIXME: Translate these two lines
                        // directionIntegers_[k][l-1] = 1L;
                        // directionIntegers_[k][l-1] <<= (bits_-l);
                    }
                }
                break;
            case Jaeckel:
                maxTabulated=32;
                // maxTabulated = sizeof(initializers)/sizeof(unsigned long *)+1;
            	// FIXME: How to achieve maxTabulated in Java (no sizeof() in Java...)
                for (k=1; k<Math.min(dimensionality_, maxTabulated); k++) {
                    j = 0;
                    // 0UL marks coefficients' end for a given dimension
                    while (initializers[k-1][j] != 0) {
                    	// FIXME: Translate these two lines
                        // directionIntegers_[k][j] = initializers[k-1][j];
                        // directionIntegers_[k][j] <<= (bits_-j-1);
                        j++;
                    }
                }
                break;
            case SobolLevitan:
                maxTabulated=40;
                // maxTabulated = sizeof(SLinitializers)/sizeof(unsigned long *)+1;
            	// FIXME: How to achieve maxTabulated in Java (no sizeof() in Java...)
                for (k=1; k<Math.min(dimensionality_, maxTabulated); k++) {
                    j = 0;
                    // 0UL marks coefficients' end for a given dimension
                    while (SLinitializers[k-1][j] != 0) {
                    	// FIXME: Translate these two lines
                        // directionIntegers_[k][j] = SLinitializers[k-1][j];
                        // directionIntegers_[k][j] <<= (bits_-j-1);
                        j++;
                    }
                }
                break;
            case SobolLevitanLemieux:
                maxTabulated=360;
                // maxTabulated = sizeof(Linitializers)/sizeof(unsigned long *)+1;
                // FIXME: How to achieve maxTabulated in Java (no sizeof() in Java...)
                for (k=1; k<Math.min(dimensionality_, maxTabulated); k++) {
                    j = 0;
                    // 0UL marks coefficients' end for a given dimension
                    while (Linitializers[k-1][j] != 0L) {
                    	// FIXME: Translate these two lines
                        // directionIntegers_[k][j] = Linitializers[k-1][j];
                        // directionIntegers_[k][j] <<= (bits_-j-1);
                        j++;
                    }
                }
                break;
            default:
                break;
        }
        
        
        // random initialization for higher dimensions
        
        /*
        // FIXME: Check maxTabulated below: How is it initialized?
        if (dimensionality_>maxTabulated) {
        	MersenneTwisterUniformRng uniformRng = new MersenneTwisterUniformRng(seed);
            for (k=maxTabulated; k<dimensionality_; k++) {
                for (int l=1; l<=degree.indexOf(k); l++) {
                	
                    do {
                        // u is in (0,1)
                        double u = uniformRng.next().getVal();
                        // the direction integer has at most the
                        // rightmost l bits non-zero
                        
                        // FIXME: Translate this line
                        // directionIntegers_[k][l-1] = (long)(u*(1<<l));
                    } // while (!(directionIntegers_[k][l-1] & 1));
                    
                    // iterate until the direction integer is odd
                    // that is it has the rightmost bit set

                    // shifting bits_-l bits to the left
                    // we are guaranteed that the l-th leftmost bit
                    // is set, and only the first l leftmost bit
                    // can be non-zero
                    
                	// FIXME: Translate this line
                	// directionIntegers_[k][l-1] <<= (bits_-l);
                }
            }
        } */

        // computation of directionIntegers_[k][l] for l>=degree_[k]
        // by recurrence relation
        for (k=1; k<dimensionality_; k++) {
             int gk = degree.get(k);
             for (int l=gk; l<bits_; l++) {
                // eq. 8.19 "Monte Carlo Methods in Finance" by P. Jaeckel
            	 
            	// FIXME: Correct this line (directionIntegers_)
                // long n = (directionIntegers_[k][l-gk]>>gk);
                
                // a[k][j] are the coefficients of the monomials in ppmt[k]
                // The highest order coefficient a[k][0] is not actually
                // used in the recurrence relation, and the lowest order
                // coefficient a[k][gk] is always set: this is the reason
                // why the highest and lowest coefficient of
                // the polynomial ppmt[k] are not included in its encoding,
                // provided that its degree is known.
                // That is: a[k][j] = ppmt[k] >> (gk-j-1)
                for (j=1; j<gk; j++) {
                    // XORed with a selection of (unshifted) direction
                    // integers controlled by which of the a[k][j] are set
                	
                	// FIXME: Correct this line if ((ppmt.get(k) >>> (gk-j-1)) & 1 != 0)
                    // if ((ppmt.get(k) >>> (gk-j-1)) & 1 != 0) {
                    //    n ^= directionIntegers_[k][l-j];
                    // }
                    
                }
                
                // a[k][gk] is always set, so directionIntegers_[k][l-gk]
                // will always enter
                
             // FIXME: Correct these two lines regarding directionIntegers_
             // n ^= directionIntegers_[k][l-gk];
             // directionIntegers_[k][l]=n;
            }
        }

        /* TODO: This code is not translated yet
        // in case one needs to check the directionIntegers used
        // bool printDirectionIntegers = false;
        /*
           if (printDirectionIntegers) {
               std::ofstream outStream("directionIntegers.txt");
               for (k=0; k<std::min(32UL,dimensionality_); k++) {
                   outStream << std::endl << k+1       << "\t"
                                          << degree[k] << "\t"
                                          << ppmt[k]   << "\t";
                   for (j=0; j<10; j++) {
                       outStream << io::power_of_two(
                           directionIntegers_[k][j]) << "\t";
                   }
               }
               outStream.close();
           }
        */
		
        
        // initialize the Sobol integer/double vectors
        // first draw
        for (k=0; k<dimensionality_; k++) {
        	
        	// FIXME: Correct this line regarding directionIntegers_ .
            // integerSequence_.get(k) = directionIntegers_[k][0];
        }
    }
	
	
	
	//
	// Methods
	//
	
	
    @Override
	public final List<Long> nextInt32Sequence() /* @Read-only */ {
		if (firstDraw_) {
            // it was precomputed in the constructor
            firstDraw_ = false;
            return integerSequence_;
        }
        
		// increment the counter
        sequenceCounter_++;
        
        // did we overflow?
        if(sequenceCounter_ != 0) {
        	throw new ArithmeticException("period exceeded");
        }

        // instead of using the counter n as new unique generating integer
        // for the n-th draw use the Gray code G(n) as proposed
        // by Antonov and Saleev
        long n = sequenceCounter_;
        
        // Find rightmost zero bit of n
        int j = 0;
        while ((n & 1) != 0) {  // FIXME: Is this correct? See original C++ code: while (n & 1) {....}
        	n >>= 1; j++; 
        }
        
        for (int k=0; k<dimensionality_; k++) {
            // XOR the appropriate direction number into each component of
            // the integer sequence to obtain a new Sobol integer for that
            // component
        	
        	// FIXME: Correct this line regarding directionIntegers_ .
            // integerSequence_[k] = directionIntegers_[k][j];
        }
        return integerSequence_;
	}
	
	
	
	 // skip to the n-th sample in the low-discrepancy sequence 
    @Override
    public void skipTo(/*@NonNegative*/ long skip){
    	long N = skip+1;
    	
    	//FIXME: Check the constant M_LN2. Where is it defined in QuantLib?
        // int ops = (int)(Math.log((double)N)/M_LN2)+1;

        // Convert to Gray code
        long G = N ^ (N>>1);
        
     // FIXME: Correct the following for loop regarding directionIntegers_ .
        /*
        for (int k=0; k<dimensionality_; k++) {
            integerSequence_[k] = 0;
            for (Size index=0; index<ops; index++) {
                if (G>>index & 1)
                    integerSequence_[k] = directionIntegers_[k][index];
            }
        } */
        sequenceCounter_ = skip;
    }
    

    @Override
    public final Sample<List<Double>> nextSequence() /* @Read-only */ {
        final List<Long> v = nextInt32Sequence();
        // normalize to get a double in (0,1)
        for (int k=0; k<dimensionality_; ++k) {
        	// FIXME: Check orignial C++ code: see comment code block below.
            // sequence_.getWeight() = v.get(k) * normalizationFactor_; 
        }
        return sequence_; 
    } 
    
    
    /*
     * C++ code for the method above:
     * 
     * const SobolRsg::sample_type& nextSequence() const {
            const std::vector<unsigned long>& v = nextInt32Sequence();
            // normalize to get a double in (0,1)
            for (Size k=0; k<dimensionality_; ++k)
                sequence_.value[k] = v[k] * normalizationFactor_;
            return sequence_;
        }
     */
	
    
    @Override
    public int dimension() /* @Read-only */ { 
    	return dimensionality_; 
    }
    
    @Override
    public final Sample<List<Double>> lastSequence() /* @Read-only*/  { 
    	return sequence_; 
    }
    
}


