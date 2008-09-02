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

package org.jquantlib.util.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This class provides the ability of querying the run time type information of parametric types.
 * <p> 
 * This functionality is specially helpful when you'd like to do something like this (which does not compile!):
 *  * <pre>
 * class B<T> {
 *   public B() {
 *     T t = new T();
 *   }
 * } 
 * </pre>
 * 
 * Notice that this class is somewhat "crude". See also {@link TypeReference} for
 * a class which provides the same functionality but is more "elegant". 
 * 
 * @see TypeReference
 * 
 * @see <a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html">SuperTypeTokens</a>
 * @see <a href="http://java.sun.com/j2se/1.5/pdf/generics-tutorial.pdf">Generics Tutorial</a>
 * @author Richard Gomes
 */
public class TypeToken {

    static public Type getType(final Class<?> klass) {
        return getType(klass, 0);
    }

    static public Type getType(final Class<?> klass, final int pos) {
        Type superclass = klass.getGenericSuperclass();

        // FIXME: Code added for testing purposes. Will be removed later (Dominik)
        System.out.println("Class: "+klass);
        System.out.println("Generic Superclass: "+superclass);
        System.out.println("Generic Interfaces: ");
        Type genInterfaces[] = klass.getGenericInterfaces();
        for (Type type : genInterfaces) {
            System.out.println("    "+type);
        }
        
        if (superclass instanceof Class) {
            throw new IllegalArgumentException("Class should be generic");
        }
        Type[] types = ((ParameterizedType) superclass).getActualTypeArguments();
        if (pos >= types.length) {
            throw new IllegalArgumentException("Missing parameter");
        }
        return types[pos];
    }

    static public Class<?> getClazz(final Class<?> klass) {
        return getClazz(klass, 0);
    }

    static public Class<?> getClazz(final Class<?> klass, final int pos) {
        Type type = getType(klass, pos);
        return (type instanceof Class<?>) ? (Class<?>) type : (Class<?>) ((ParameterizedType) type).getRawType();
    }

}
