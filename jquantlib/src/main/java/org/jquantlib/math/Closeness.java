/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

package org.jquantlib.math;

/**
 *  Follows somewhat the advice of Knuth on checking for floating-point
 * equality. The closeness relationship is:
 * <p>{@latex[
   \mathrm{close}(x,y,n) \equiv |x-y| \leq \varepsilon |x|
                         \wedge |x-y| \leq \varepsilon |y|
   } 
 * <p>where {@latex$ \varepsilon} is {@latex$ n} times the machine accuracy;
 * <p>{@latex$ n} equals 42 if not given.
 */
final public class Closeness {
	static public final double epsilon = 1E-128;
	
	static public final boolean close(double x, double y) {
	    return close(x,y,42);
	}
	
	static public final boolean close(double x, double y, /*@Positive*/ int n) {
	    double tolerance = n * epsilon;
	    return (x>=y-tolerance && x<=y+tolerance);
	}
	
	static public final boolean close_enough(double x, double y) {
	    return close_enough(x,y,42);
	}
	
	static public final boolean close_enough(double x, double y, /*@Positive*/ int n) {
	    double tolerance = n * epsilon;
	    return (x>=y-tolerance || x<=y+tolerance);
	}
}
