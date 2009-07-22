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

import org.jquantlib.math.Array;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.optimization.NoConstraint;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */

//TODO: comments, license, code review
public class NoConstraintTest {
	
    private final static Logger logger = LoggerFactory.getLogger(NoConstraintTest.class);
    
    private final NoConstraint nc;
    
    public NoConstraintTest() {
        logger.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
        this.nc = new NoConstraint();
    }

    @Test
    public void testTest() {
        if (!nc.test(new Array()))
            fail("NoConstraint test method failed");
    }

    @Test
    public void testUpdate() {
        final Array params    = new Array(new double[] { 1.0, 1.1, 2.3 });
        final Array direction = new Array(new double[] { 0.1, 0.3, 1.1 });
        double beta = 2.0;
        nc.update(params, direction, beta);
        logger.info("params after nc.update= {}", params );
        logger.info("Test 1.1 + 2.0 * 0.3 = " + (1.1 + 2.0 * 0.3));
        if (!isArrayEqual(params, new Array(new double[] { 1.2d, 1.7d, 4.5d }), 0.000001))
            // if (!params.operatorEquals (new Array (new double[]{1.2d,1.7d,4.5d})))
            fail("Constraint update method failed");
    }

    private boolean isArrayEqual(final Array one, final Array two, final double precision) {
        final Array diffArray = one.sub(two);
        logger.info("diffArray = {}", diffArray);
        return Closeness.isCloseEnough(diffArray.dotProduct(diffArray), precision * precision);
    }

}

