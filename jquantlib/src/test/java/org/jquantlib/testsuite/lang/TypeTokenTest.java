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
import org.junit.Test;

/**
 * @author Richard Gomes
 */
public class TypeTokenTest {

    public TypeTokenTest() {
        System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
    }
    
    @Test
    public void testTypes() {
        C c = new C();
        if (c.getClazz() != Double.class) {
            fail("C should be java.lang.Double");
        }
        
        D d = new D();
        if (d.getClazz() != Integer.class) {
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

    private class C extends B< java.lang.Double >{}

    private class D extends B< java.lang.Integer >{}

}
