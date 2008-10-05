/*
 Copyright (C) 2007 Joon Tiang Heng

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

package org.jquantlib.math.optimization;
import org.jquantlib.math.Array;

/**
 * @author Joon Tiang Heng
 */
public abstract class Constraint { //! Base constraint class
      
        public boolean empty() { return false; }
        public abstract boolean test(final Array p) ;
		//take note of precision error when comparing Arrays, only compare difference dot product
		//this is due to representation of numbers such as 0.1 in binary
        public double update(Array params,final Array direction,double beta) {

			double diff=beta;
						
			//System.out.println("1. params Value="+ "{"+params.getData()[0]+","+params.getData()[1]+","+params.getData()[2]+"}");
			
			Array newParams = params.operatorAddCopy(direction.operatorMultiplyCopy(new Array(direction.size(),diff)));
			
			//System.out.println("1. after operatorAddCopy params Value="+ "{"+params.getData()[0]+","+params.getData()[1]+","+params.getData()[2]+"}");
			boolean valid = test(newParams);
			Integer icount = 0;
			while (!valid) {
				if (icount > 200)
					throw new RuntimeException("can't update parameter vector");
				diff *= 0.5;
				icount ++;
				
				newParams = params.operatorAddCopy(direction.operatorMultiplyCopy(new Array(direction.size(),diff)));	
				
				//System.out.println("2. params Value="+ "{"+params.getData()[0]+","+params.getData()[1]+","+params.getData()[2]+"}");
				
				valid = test(newParams);
			}
			
			
			params.operatorAdd(direction.operatorMultiplyCopy(new Array(direction.size(),diff)));
			
			//System.out.println("3. params Value="+ "{"+params.getData()[0]+","+params.getData()[1]+","+params.getData()[2]+"}");
			
			return diff;
		}
}
