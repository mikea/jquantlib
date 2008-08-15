/*

 Copyright (C) 2008 Q.Boiler

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
/*
 * This Class is a cut and past copy with some minor changes from a library Called 
 * Jama   http://math.nist.gov/javanumerics/
 *
 * Copyright Notice  This software is a cooperative product of The MathWorks and 
 * the National Institute of Standards and Technology (NIST) which has been 
 * released to the public domain. Neither The MathWorks nor NIST assumes any 
 * responsibility whatsoever for its use by other parties, and makes no guarantees, 
 * expressed or implied, about its quality, reliability, or any other characteristic. 
 */  
package org.jquantlib.math;

/**
 *
 * @author Q. Boiler
 */
public class MathUtil {
	
   /** sqrt(a^2 + b^2) without under/overflow. **/

   public static double hypot(double a, double b) {
      double r;
      if (Math.abs(a) > Math.abs(b)) {
         r = b/a;
         r = Math.abs(a)*Math.sqrt(1+r*r);
      } else if (b != 0) {
         r = a/b;
         r = Math.abs(b)*Math.sqrt(1+r*r);
      } else {
         r = 0.0;
      }
      return r;
   }

}
