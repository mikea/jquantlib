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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * References a generic type.
 * 
 * @see <a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html">Super Type Tokens</a>
 * 
 * @author crazybob@google.com (Bob Lee)
 */
public abstract class TypeReference<T> {

    private final Type[] types;
    private volatile Constructor<?> constructor;

    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.types = ((ParameterizedType) superclass).getActualTypeArguments();
    }

    /**
     * Gets the referenced type of the first generic parameter
     */
    public Type getGenericType() {
        return getGenericType(0);
    }
    
    /**
     * Gets the referenced type of the n-th generic parameter
     */
    public Type getGenericType(final int n) {
        return this.types[n];
    }

    /**
     * Gets the referenced Class of the first generic parameter
     */
    public Class<?> getGenericParameterClass() {
        return getGenericParameterClass(0);
    }

    /**
     * Gets the referenced Class of the n-th generic parameter
     */
    public Class<?> getGenericParameterClass(final int n) {
        Type type = types[n];
        return ( type instanceof Class<?> ) ? (Class<?>) type : (Class<?>) ((ParameterizedType) type).getRawType();
    }
    
    /**
     * Instantiates a new instance of {@code T} using the first generic parameter
     */
    @SuppressWarnings("unchecked")
    public T newGenericInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return newGenericInstance(0);
    }
    
    /**
     * Instantiates a new instance of {@code T} using the n-th generic parameter
     */
    @SuppressWarnings("unchecked")
    public T newGenericInstance(final int n) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (constructor == null) {
            constructor = getGenericParameterClass(n).getConstructor();
        }
        return (T) constructor.newInstance();
    }

    /**
     * Instantiates a new instance of {@code T} using the n-th generic parameter
     */
    @SuppressWarnings("unchecked")
    public T newGenericInstance(final int n, final Object ... objects) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Type type = types[n];
        Class<?> rawType = type instanceof Class<?> ? (Class<?>) type : (Class<?>) ((ParameterizedType) type).getRawType();

        Class<?>[] types = new Class[objects.length];
        for (int i=0; i<objects.length; i++) {
            types[i] = objects[i].getClass();
        }
        constructor = rawType.getConstructor(types);

        return (T) constructor.newInstance(objects);
    }


    public boolean equals(Object o) {
        if (o instanceof TypeReference) {
            int len = ((TypeReference)o).types.length;
            if (len!=types.length) return false;
            for (int i=0; i<types.length; i++) {
              if (! ((TypeReference) o).types[i].equals(this.types[i]) ) return false;  
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = 0;
        for (int i=0; i<types.length; i++) {
            hash = (hash << 1) + types[i].hashCode();
        }
        return hash;
    }

}
