/*
 Copyright (C) 2008 
  
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

package org.jquantlib.testsuite.math.optimization;
import static org.junit.Assert.fail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.math.Array;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.optimization.BoundaryConstraint;
import org.jquantlib.math.optimization.CompositeConstraint;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.junit.Test;

/**
 * @author
 *
 *
 */


public class CompositeConstraintTest {
	
    private final static Logger logger = LoggerFactory.getLogger(CompositeConstraintTest.class);

	public CompositeConstraintTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
    public void testConstraint() {
                       
    	BoundaryConstraint bc = new BoundaryConstraint(-1,5);
    	PositiveConstraint pc = new PositiveConstraint();
		//effective conditions becomes (0,5], i.e exclusive 0 and inclusive 5
        CompositeConstraint cc = new CompositeConstraint(bc,pc); //order does not matter
		
        testTest(cc);
		testUpdate(cc);
		testEmpty(cc);
    }
    
	void testTest(CompositeConstraint cc) {
	  
      if (!cc.test(new Array(new double[]{1.6,1.2,5.0})))
		fail("CompositeConstraint test method failed");
	  
    }
	void testUpdate(CompositeConstraint cc) {
	  Array params = new Array(new double[]{1.2,1.1,5.3});
	  Array direction = new Array(new double[]{1.1,0.1,0.3});
	  //note that boundary constraint have issues of non-convergence when the values in an array are at either boundary
	  double beta = -1.0;
	  cc.update(params,direction,beta);
	  logger.info("params after co.update=" + "{"+params.getData()[0]+","+params.getData()[1]+","+params.getData()[2]+"}");
	  //if (!params.operatorEquals(new double[]{0.9,0.8,0.2}))
	  if (!IsArrayEqual(params,new Array(new double[]{0.0,1.0,5.0}),0.000001))
		fail("CompositeConstraint update method failed");
	  
    }
	void testEmpty(Constraint cc) {
      if (cc.empty())
		fail("Constraint empty method failed");
	  
    }
	boolean IsArrayEqual(Array one,Array two,double precision){
		Array diffArray = one.operatorSubtractCopy(two);
		logger.info("diffArray =" + "{"+diffArray.getData()[0]+","+diffArray.getData()[1]+","+diffArray.getData()[2]+"}");
		return Closeness.isCloseEnough(diffArray.dotProduct(diffArray,diffArray) , precision*precision );
		
	}
}

