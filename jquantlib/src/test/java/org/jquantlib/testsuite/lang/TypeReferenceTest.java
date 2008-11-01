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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.util.reflect.TypeReference;
import org.junit.Test;

/**
 * @author Richard Gomes
 */
public class TypeReferenceTest {

    private final static Logger logger = LoggerFactory.getLogger(TypeReferenceTest.class);

    public TypeReferenceTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
    }
    
    @Test
    public void testTypeReference() {
        B b = new B();
        
        Object[] objs = b.getGenericClasses();
        if (objs[0].getClass() != ArrayList.class) {
            fail("Generic parameter should be " + ArrayList.class.getName());
        }
        if (objs[1].getClass() != TreeMap.class) {
            fail("Generic parameter should be " + TreeMap.class.getName());
        }
        if (objs[2].getClass() != HashSet.class) {
            fail("Generic parameter should be " + HashSet.class.getName());
        }
    }
    
    //
    // inner classes
    //
    
    private class A<L extends List, M extends Map, S extends Set> extends TypeReference { 
        public Object[] getGenericClasses() {
            Object[] objs = new Object[3];
            
            try {
                for (int i=0; i<3; i++) {
                    Constructor<Object> c = getGenericParameterClass(i).getConstructor();
                    objs[i] = c.newInstance();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return objs;
        }
    }

    private class B extends A< ArrayList, TreeMap, HashSet > { /* nothing */ }

}
