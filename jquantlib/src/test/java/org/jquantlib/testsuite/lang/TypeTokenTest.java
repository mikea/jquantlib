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

package org.jquantlib.testsuite.lang;

import static org.junit.Assert.fail;

import org.jquantlib.util.reflect.TypeToken;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Richard Gomes
 */
public class TypeTokenTest {

    private final static Logger logger = LoggerFactory.getLogger(TypeTokenTest.class);

    public TypeTokenTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
    }
    
    @Test
    public void testTypeToken() {
        C c = new C();
        if (c.getClazz() != Double.class) {
            fail("C should be java.lang.Double");
        }
        
        D d = new D();
        if (d.getClazz() != Integer.class) {
            fail("D should be java.lang.Integer");
        }
    }
        
    
    @Ignore("This test case will not pass because Java reifies types :(  :: Maybe in the future when we adopt Guice or similar")
    @Test
    public void testTypeToken2() {
        K k1 = new K<java.lang.Double>();
        if (k1.getClazz() != Double.class) {
            fail("C should be java.lang.Double");
        }
        
        K k2 = new K<java.lang.Integer>();
        if (k2.getClazz() != Integer.class) {
            fail("D should be java.lang.Integer");
        }
        
    }
    
    //
    // inner classes
    //
    
    private class B<T extends Number> { 
        public Class<?> getClazz() {
            return TypeToken.getClazz(this.getClass());
        }
    }

    private class C extends B<java.lang.Double> {  }

    private class D extends B<java.lang.Integer> {  }

    private class K<T extends Number> extends B<T> {  }

}
